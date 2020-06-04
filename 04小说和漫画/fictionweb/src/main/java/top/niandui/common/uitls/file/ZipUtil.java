package top.niandui.common.uitls.file;

import lombok.extern.slf4j.Slf4j;

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

    /*
     * @title createZip
     * @description 压缩文件夹
     * @param [sourcePath, zipName, zipPath]
     * @return java.lang.String
     * @author huangwx
     * @date 2020/5/14 22:07
     */
    public static String createZip(String sourcePath, String zipName, String zipPath) {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            zipPath = zipPath + "/" + zipName + ".zip";
            fos = new FileOutputStream(zipPath);
            zos = new ZipOutputStream(fos);
            //((Object) zos).setEncoding("gbk");//此处修改字节码方式。
            //createXmlFile(sourcePath,"293.xml");
            writeZip(new File(sourcePath), "", zos);
            return zipPath;
        } catch (FileNotFoundException e) {
            log.error("创建ZIP文件失败", e);
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                log.error("创建ZIP文件失败", e);
            }

        }
        return sourcePath;
    }

    /**
     * @return void
     * @Title
     * @Description 处理文件夹
     * @author lvl
     * @date 2017-9-19 下午12:58:08
     */
    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        if (file.exists()) {
            if (file.isDirectory()) {
                //处理文件夹
                parentPath += file.getName() + File.separator;
                File[] files = file.listFiles();
                if (files.length != 0) {
                    for (File f : files) {
                        writeZip(f, parentPath, zos);
                    }
                } else {
                    //空目录则创建当前目录
                    try {
                        zos.putNextEntry(new ZipEntry(parentPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }

                } catch (FileNotFoundException e) {
                    log.error("创建ZIP文件失败", e);
                } catch (IOException e) {
                    log.error("创建ZIP文件失败", e);
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        log.error("创建ZIP文件失败", e);
                    }
                }
            }
        }
    }
}
