package top.niandui;

import sun.net.www.protocol.jar.JarURLConnection;
import top.niandui.model.IBaseComponent;
import top.niandui.utils.PrintUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
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
            List<IBaseComponent> components = getFiles(ClassLoader.getSystemResource("top/niandui/component"));
            for (int i = 0; i < components.size(); i++) {
                String print = (i + 1) + "：" + components.get(i).getClass().getSimpleName();
                PrintUtil.println(print);
                components.get(i).startBeforePrint();
            }
            Scanner sc = new Scanner(System.in);
            String tip = "请选择组件(1-" + components.size() + ")：";
            PrintUtil.print(tip);
            String line;
            while (true) {
                line = sc.nextLine();
                try {
                    int index = Integer.parseInt(line) - 1;
                    if (index >= 0 && index < components.size()) {
                        components.get(index).start();
                        break;
                    }
                } catch (Exception ex) {
                    PrintUtil.print(tip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据组件所在url获取组件名称
     *
     * @param dirUrl 组件所在url
     * @return 有哪些组件
     * @throws IOException
     */
    public static List<IBaseComponent> getFiles(URL dirUrl) throws Exception {
        List<String> files = null;
        String protocol = dirUrl.getProtocol();
        if ("file".equals(protocol)) {
            File dir = new File(URLDecoder.decode(dirUrl.getFile(), "utf-8"));
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
        files = files == null ? Collections.EMPTY_LIST : files;
        List<IBaseComponent> list = new ArrayList<>();
        for (String file : files) {
            list.add((IBaseComponent) (Class.forName("top.niandui.component." + file.substring(0, file.indexOf('.'))).newInstance()));
        }
        return list;
    }
}
