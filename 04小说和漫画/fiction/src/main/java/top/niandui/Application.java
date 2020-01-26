package top.niandui;

import sun.net.www.protocol.jar.JarURLConnection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Title: Application.java
 * @description: Application
 * @time: 2020/1/26 21:32
 * @author: liyongda
 * @version: 1.0
 */
public class Application {

    public static void main(String[] args) {
        try {
            List<String> files = getFiles(ClassLoader.getSystemResource("top/niandui/component"));
            for (int i = 0; i < files.size(); i++) {
                files.set(i, files.get(i).substring(0, files.get(i).indexOf('.')));
                System.out.println((i + 1) + "：" + files.get(i));
            }
            Scanner sc = new Scanner(System.in);
            String tip = "请选择组件(1-" + files.size() + ")：";
            System.out.print(tip);
            String line;
            String comName;
            while (true) {
                line = sc.nextLine();
                try {
                    int index = Integer.parseInt(line) - 1;
                    if (index >= 0 && index < files.size()) {
                        comName = files.get(index);
                        break;
                    }
                } catch (Exception ex) {
                    System.out.print(tip);
                }
            }
            if (comName != null) {
                Class<?> comClass = Class.forName("top.niandui.component." + comName);
                Method startMethod = comClass.getMethod("start");
                startMethod.invoke(comClass.newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据组件所在url获取组件名称
     * @param dirUrl 组件所在url
     * @return       有哪些组件
     * @throws IOException
     */
    public static List<String> getFiles(URL dirUrl) throws IOException {
        List<String> files = null;
        String protocol = dirUrl.getProtocol();
        if ("file".equals(protocol)) {
            File dir = new File(dirUrl.getFile());
            files = Arrays.asList(Objects.requireNonNull(dir.list((_dir, name) -> name.endsWith(".class"))));
        } else if ("jar".equals(protocol)) {
            files = new ArrayList<>();
            String path = dirUrl.getPath();
            path = path.substring(path.lastIndexOf("!/") + 2);
            JarFile jarFile = ((JarURLConnection) dirUrl.openConnection()).getJarFile();
            Enumeration<JarEntry> entries = jarFile.entries();
            boolean bl = false;
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if (jarEntry.toString().contains(path) && !jarEntry.isDirectory()) {
                    String comFile = jarEntry.toString();
                    files.add(comFile.substring(comFile.lastIndexOf('/') + 1));
                    bl = true;
                } else if (bl) {
                    break;
                }
            }
        }
        return files == null ? Collections.EMPTY_LIST : files;
    }
}
