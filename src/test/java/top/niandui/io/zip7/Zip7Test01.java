package top.niandui.io.zip7;

import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Paths;

/**
 * 7-Zip-JBinding测试
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/7/6 11:29
 */
public class Zip7Test01 {
    public static String path = "d:/ztest/7zip";

    @Test
    public void test01() throws Exception {
        // 要使用 7-Zip-JBinding 库，您将需要来自类路径中发行版lib文件夹的两个 JAR 文件：
        //Sevenzipjbinding.jar - 7-Zip-JBinding 的 java 部分。( JavaDoc )
        //Sevenzip-jbinding-‹OS›.jar - 目标操作系统‹OS›的本机库
        // 这是一个简单的测试程序，可用于检查所有 7-Zip-JBinding 要求：
        SevenZip.initSevenZipFromPlatformJAR();
    }

    @Test
    public void test02() throws Exception {
        // https://www.cnblogs.com/wangshunyao/p/6811248.html
        // http://sevenzipjbind.sourceforge.net/first_steps.html
        String fileName = "rar5.rar";
        try (
                // accessFile 和 archive 需要分开关闭
                RandomAccessFile accessFile = new RandomAccessFile(path + "/" + fileName, "r");
                // 第一个参数null自动检测文档类型
                IInArchive archive = SevenZip.openInArchive(null, new RandomAccessFileInStream(accessFile))
        ) {
            // 为存档返回简单的 7-Zip 界面
            ISimpleInArchive simpleInterface = archive.getSimpleInterface();
            // 返回打开的存档的存档格式。
            ArchiveFormat archiveFormat = archive.getArchiveFormat();
            // 返回此归档类型是否支持创建/更新操作
            boolean outArchiveSupported = archiveFormat.isOutArchiveSupported();
            // 返回存档中的项目数。根据档案类型，目录被认为是项目与否。
            int numberOfItems = archive.getNumberOfItems();

            String targetPath = path + "/un" + fileName.substring(0, fileName.indexOf('.'));
            File dir = new File(targetPath);
            dir.mkdirs();
            targetPath = dir.getAbsolutePath();
            // 逐个解压
            for (int i = 0; i < numberOfItems; i++) {
                String name = archive.getStringProperty(i, PropID.PATH).replace('\\', '/');
                File file = Paths.get(targetPath, name).toFile();
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
                }
            }

            String format = "\t%-10s\tpropID: %-22s\t= %s ( %s )";
            System.out.println(String.format("A%-5sB", "c"));

            // 压缩文件中的文件属性
            System.out.println("使用索引index获取有关归档项目属性的信息");
            int numberOfProperties = archive.getNumberOfProperties();
            for (int i = 0; i < numberOfItems; i++) {
                System.out.println("i = " + i);
                for (int j = 0; j < numberOfProperties; j++) {
                    PropertyInfo propertyInfo = archive.getPropertyInfo(j);
                    PropID propID = propertyInfo.propID;
                    Object property = archive.getProperty(i, propID);
                    String stringProperty = archive.getStringProperty(i, propID);
                    System.out.println("\tj = " + j + String.format(
                            format, propertyInfo.varType.getSimpleName(), propID, property, stringProperty));
                }
                System.out.println();
            }

            // 压缩文件的属性
            System.out.println("使用索引index获取有关存档属性的信息");
            int numberOfArchiveProperties = archive.getNumberOfArchiveProperties();
            for (int i = 0; i < numberOfArchiveProperties; i++) {
                PropertyInfo propertyInfo = archive.getArchivePropertyInfo(i);
                PropID propID = propertyInfo.propID;
                Object archiveProperty = archive.getArchiveProperty(propID);
                String stringArchiveProperty = archive.getStringArchiveProperty(propID);
                System.out.println("i = " + i + String.format(
                        format, propertyInfo.varType.getSimpleName(), propID, archiveProperty, stringArchiveProperty));
            }

            System.out.println();
        }
    }


}


