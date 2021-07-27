package top.niandui.common.uitls.file;

import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 7-Zip-JBinding工具
 * <br/>7-Zip-JBinding 库提供两种不同的接口：标准接口和简单接口。标准接口直接映射所有提供类似 C++ 接口的本机 7-Zip 库方法。简单的界面是尝试提供更像Java的易于使用的界面。它的设计目的是为了快速编码和轻松启动，而不是为了功能丰富，也不是为了性能。
 * <br/><a href="http://sevenzipjbind.sourceforge.net/extraction_snippets.html">解压：提取片段</a>
 * <br/><a href="http://sevenzipjbind.sourceforge.net/compression_snippets.html">创建更新：压缩片段</a>
 * <br/><a href="http://sevenzipjbind.sourceforge.net/javadoc/index.html">文档</a>
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/7/15 17:15
 */
public class Zip7Util {
    private Zip7Util() {
    }

    /**
     * 解压文件到指定目录(自动检测文档类型)
     *
     * @param zipPath   压缩文件路径
     * @param unzipPath 解压目录
     * @return 解压出的文件列表
     */
    public static List<File> unZip(String zipPath, String unzipPath) {
        try (
                // accessFile 和 archive 需要分开关闭
                RandomAccessFile accessFile = new RandomAccessFile(zipPath, "r");
                // 第一个参数null自动检测文档类型
                IInArchive archive = SevenZip.openInArchive(null, new RandomAccessFileInStream(accessFile))
        ) {
            // 返回存档中的项目数。根据档案类型，目录被认为是项目与否。
            int numberOfItems = archive.getNumberOfItems();
            // 创建返回文件列表对象
            List<File> fileList = new ArrayList<>(numberOfItems);
            // 解压父目录检查
            File dir = new File(unzipPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            unzipPath = dir.getAbsolutePath();
            // 逐个解压
            for (int i = 0; i < numberOfItems; i++) {
                // 获取该项的名称
                String name = archive.getStringProperty(i, PropID.PATH).replace('\\', '/');
                // 创建写出文件对象
                File file = Paths.get(unzipPath, name).toFile();
                // 判断该项类型
                if ((Boolean) archive.getProperty(i, PropID.IS_FOLDER)) {
                    // 是文件夹创建文件夹
                    file.mkdirs();
                } else {
                    // 是文件
                    try (OutputStream os = new FileOutputStream(file)) {
                        archive.extractSlow(i, data -> {
                            // 当文件较大是会被多次调用, 一次大小为32768, BufferedOutputStream的缓冲区为8192
                            try {
                                os.write(data);
                                return data.length;
                            } catch (IOException e) {
                                throw new SevenZipException(e.getMessage(), e);
                            }
                        });
                    }
                    // 文件加入文件列表
                    fileList.add(file);
                }
            }
            return fileList;
        } catch (IOException e) {
            throw new RuntimeException("文件解压失败", e);
        }
    }

}
