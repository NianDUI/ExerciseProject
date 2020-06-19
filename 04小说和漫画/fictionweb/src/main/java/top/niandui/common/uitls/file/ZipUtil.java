package top.niandui.common.uitls.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author huangwx
 * @version 1.0
 * @title ZipUtil.java
 * @description 压缩工具类
 * @time 2020/4/24 11:05
 */
@Slf4j
public class ZipUtil {
    /**
     * 解压文件到指定目录
     *
     * @return
     */
    public static void unZipFiles(String zipDir, String descDir) {
        try {
            File zipFile = new File(zipDir);
            ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));//解决中文文件夹乱码

            File pathFile = new File(descDir);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }

            for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                String outPath = (descDir + "/" + zipEntryName).replaceAll("\\*", "/");

                // 判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                // 输出文件路径信息
                //			System.out.println(outPath);

                FileOutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                out.close();
                in.close();
            }
            zip.close();
        } catch (Exception e) {
            throw new RuntimeException("压缩文件异常");
        }
    }


    /**
     * 创建压缩文件
     *
     * @param source  压缩原目录
     * @param zipName 压缩文件名称
     * @param zipPath 压缩文件路径
     * @return 压缩文件全路径
     */
    public static String createZip(String source, String zipName, String zipPath) {
        File file = new File(zipPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String name = file.getAbsolutePath() + File.separator + zipName;
        if (!name.endsWith(".zip")) {
            name += ".zip";
        }
        try (ZipOutputStream zos = getZipOutputStream(file.getAbsolutePath() + File.separator + zipName)) {
            writeZipRoot(source, zos);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("创建ZIP文件失败", e);
        }
        return name;
    }

    /**
     * 获取压缩文件输出流对象
     *
     * @param zipPath 压缩文件路径
     * @param zipName 压缩文件名称
     * @return 压缩文件输出流对象
     * @throws FileNotFoundException
     */
    public static ZipOutputStream getZipOutputStream(String zipPath, String zipName) throws FileNotFoundException {
        File file = new File(zipPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return getZipOutputStream(file.getAbsolutePath() + File.separator + zipName);
    }

    /**
     * 获取压缩文件输出流对象
     *
     * @param filePath 文件路径
     * @return 压缩文件输出流对象
     * @throws FileNotFoundException
     */
    public static ZipOutputStream getZipOutputStream(String filePath) throws FileNotFoundException {
        return new ZipOutputStream(new FileOutputStream(filePath));
    }

    /**
     * 将文件或文件里的文件写到压缩包的根目录
     *
     * @param filePath 文件路径
     * @param zos      压缩文件输出流
     */
    public static void writeZipRoot(String filePath, ZipOutputStream zos) {
        writeZipRoot(new File(filePath), zos);
    }

    /**
     * 将文件或文件里的文件写到压缩包的根目录
     *
     * @param file File对象
     * @param zos  压缩文件输出流
     */
    public static void writeZipRoot(File file, ZipOutputStream zos) {
        if (file.exists()) {
            String parent = "";
            if (file.isDirectory()) {
                writeZip(file.listFiles(), parent, zos);
            } else {
                fileWriteZip(file, parent, zos);
            }
        }
    }

    /**
     * 将文件夹写到压缩包
     *
     * @param dir    要压缩的文件夹对象
     * @param parent 压缩包中的父目录
     * @param zos    压缩文件输出流
     */
    public static void dirWriteZip(File dir, String parent, ZipOutputStream zos) {
        if (dir.exists() && dir.isDirectory()) {
            writeZip(dir.listFiles(), parent.concat(dir.getName()).concat(File.separator), zos);
        }
    }

    /**
     * 将文件写到压缩包
     *
     * @param file   要压缩的文件对象
     * @param parent 压缩包中的父目录
     * @param zos    压缩文件输出流
     */
    public static void fileWriteZip(File file, String parent, ZipOutputStream zos) {
        try {
            if (file.exists() && file.isFile()) {
                zos.putNextEntry(new ZipEntry(parent.concat(file.getName())));
                FileUtils.copyFile(file, zos);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("创建ZIP文件失败", e);
        }
    }

    /**
     * 将File对象数组中的File对象写到压缩包
     *
     * @param files  File对象数组
     * @param parent 压缩包中的父目录
     * @param zos    压缩文件输出流
     */
    public static void writeZip(File[] files, String parent, ZipOutputStream zos) {
        for (File file : files) {
            if (file.isDirectory()) {
                dirWriteZip(file, parent, zos);
            } else {
                fileWriteZip(file, parent, zos);
            }
        }
    }
}
