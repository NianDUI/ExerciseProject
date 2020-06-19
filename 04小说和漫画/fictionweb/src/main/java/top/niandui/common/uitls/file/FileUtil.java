package top.niandui.common.uitls.file;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @Title: FileUtil.java
 * @description: 文件工具
 * @time: 2020/6/19 15:22
 * @author: liyongda
 * @version: 1.0
 */
@Slf4j
public class FileUtil {

    /**
     * 获取文件编码格式
     *
     * @param pathName 文件路径
     * @return 文件编码格式
     */
    public static String getFileCharset(String pathName) {
        return getFileCharset(new File(pathName));
    }

    /**
     * 获取文件编码格式
     *
     * @param file 文件对象
     * @return 文件编码格式
     */
    public static String getFileCharset(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset; //文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; //文件编码为 Unicode
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; //文件编码为 Unicode big endian
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; //文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80 - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件编码读取错误", e);
        }
        return charset;
    }

    /**
     * 将字符串保存为text文件
     *
     * @param fileName 文件名称
     * @param content  文件内容
     * @param path     保存的文件夹
     * @param append   是否追加写入
     */
    public static void saveText(String fileName, String content, String path, boolean append) {
        if (!fileName.endsWith(".txt")) {
            fileName = fileName + ".txt";
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        try (OutputStreamWriter os = new FileWriter(path + "/" + fileName, append)) {
            os.write(content);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("写入到文件错误", e);
        }
    }
}
