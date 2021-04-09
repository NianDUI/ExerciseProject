package top.niandui.common.uitls.file;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * ZIP压缩工具类
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/6/22 10:24
 */
@Slf4j
public class ZipUtil {

    /**
     * 解压文件到指定目录
     *
     * @param zipPath   压缩文件路径
     * @param unzipPath 解压目录
     * @return 解压出的文件列表
     */
    public static List<File> unZip(String zipPath, String unzipPath) {
        List<File> fileList = new ArrayList<>();
        try {
            unzipPath = new File(unzipPath).getAbsolutePath();
            ZipFile zipFile = new ZipFile(zipPath, Charset.forName("GBK"));
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                File file = new File(unzipPath + File.separator + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    file.mkdirs();
                } else {
                    try (InputStream is = zipFile.getInputStream(zipEntry)) {
                        Files.copy(is, file.toPath());
                    }
                    fileList.add(file);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("文件解压失败", e);
        }
        return fileList;
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
        if (!zipName.endsWith(".zip")) {
            zipName += ".zip";
        }
        String filePath = file.getAbsolutePath() + File.separator + zipName;
        try (ZipOutputStream zos = getZipOutputStream(filePath)) {
            writeZipRoot(source, zos);
        } catch (IOException e) {
            throw new RuntimeException("创建ZIP文件失败", e);
        }
        return filePath;
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
                Files.copy(file.toPath(), zos);
            }
        } catch (IOException e) {
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
