package top.niandui.file;

import org.junit.Test;

import java.io.File;

/**
 * File类的方法测试
 */
public class FileMethodTest {
    File file = new File("target/aaa.txt");

    @Test
    public void test01() {
        System.out.println("测试应用程序是否可以执行此抽象路径名表示的文件。" + file.canExecute());
        System.out.println("测试应用程序是否可以读取此抽象路径名表示的文件。" + file.canRead());
        System.out.println("测试应用程序是否可以修改此抽象路径名表示的文件。" + file.canWrite());
    }
    @Test
    public void test02() throws Exception {
        // 当且仅当不存在具有此抽象路径名指定名称的文件时，不可分地创建一个新的空文件。
        System.out.println(file.createNewFile());
        // 在默认临时文件目录中创建一个空文件，使用给定前缀和后缀生成其名称。
//        System.out.println(File.createTempFile("target/", "aaa.txt"));
        // 在指定目录中创建一个新的空文件，使用给定的前缀和后缀字符串生成其名称。
//        System.out.println(File.createTempFile("prefix", "suffix", file.getParentFile()));
//        Thread.sleep(1000 * 5);
        // 删除此抽象路径名表示的文件或目录。
//        System.out.println(file.delete());
        // 在虚拟机终止时，请求删除此抽象路径名表示的文件或目录。
//        file.deleteOnExit();
//        System.out.println("file.deleteOnExit();");
//        Thread.sleep(1000 * 2);
        // 测试此抽象路径名表示的文件或目录是否存在
//        System.out.println(file.exists());
        // 返回此抽象路径名的绝对路径名形式。
//        System.out.println(file.getAbsoluteFile());
        // 返回此抽象路径名的绝对路径名字符串。
//        System.out.println(file.getAbsolutePath());
        // 返回此抽象路径名的规范形式。
//        System.out.println(file.getCanonicalFile());
        // 返回此抽象路径名的规范路径名字符串。
//        System.out.println(file.getCanonicalPath());
        // 返回此抽象路径名指定的分区中未分配的字节数。
//        System.out.println(file.getFreeSpace());
        // 返回由此抽象路径名表示的文件或目录的名称。
        System.out.println(file.getName());
        // 返回此抽象路径名父目录的路径名字符串；如果此路径名没有指定父目录，则返回 null。
//        System.out.println(file.getParent());
        // 返回此抽象路径名父目录的抽象路径名；如果此路径名没有指定父目录，则返回 null。
//        System.out.println(file.getParentFile());
        // 将此抽象路径名转换为一个路径名字符串。
//        System.out.println(file.getPath());
        // 返回此抽象路径名指定的分区大小。
//        System.out.println(file.getTotalSpace());
        // 返回此抽象路径名指定的分区上可用于此虚拟机的字节数。
//        System.out.println(file.getUsableSpace());
        // 计算此抽象路径名的哈希码。
//        System.out.println(file.hashCode());
        // 测试此抽象路径名是否为绝对路径名。
//        System.out.println(file.isAbsolute());
        // 测试此抽象路径名表示的文件是否是一个目录。
//        System.out.println(file.isDirectory());
        // 测试此抽象路径名表示的文件是否是一个标准文件。
//        System.out.println(file.isFile());
        // 测试此抽象路径名指定的文件是否是一个隐藏文件。
//        System.out.println(file.isHidden());
        // 返回此抽象路径名表示的文件最后一次被修改的时间。
//        System.out.println(file.lastModified());
        // 返回由此抽象路径名表示的文件的长度。
//        System.out.println(file.length());
        // 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中的文件和目录。
//        System.out.println(Arrays.toString(file.list()));
        // 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中满足指定过滤器的文件和目录。
//        System.out.println(Arrays.toString(file.getParentFile().list(new FilenameFilter() {
//            @Override
//            public boolean accept(File dir, String name) {
//                System.out.println(dir + " : " + name);
//                return true;
//            }
//        })));
        // 返回一个抽象路径名数组，这些路径名表示此抽象路径名表示的目录中的文件。
//        System.out.println(Arrays.toString(file.getParentFile().listFiles()));
        // 返回抽象路径名数组，这些路径名表示此抽象路径名表示的目录中满足指定过滤器的文件和目录。
//        System.out.println(Arrays.toString(file.getParentFile().listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File pathname) {
//                System.out.println(pathname);
//                return true;
//            }
//        })));
        // 返回抽象路径名数组，这些路径名表示此抽象路径名表示的目录中满足指定过滤器的文件和目录。
//        System.out.println(Arrays.toString(file.getParentFile().listFiles(new FilenameFilter() {
//            @Override
//            public boolean accept(File dir, String name) {
//                System.out.println(dir + " : " + name);
//                return true;
//            }
//        })));
        // 列出可用的文件系统根。
//        System.out.println(Arrays.toString(file.listRoots()));
        // 创建此抽象路径名指定的目录。
//        System.out.println(file.mkdir());
        // 创建此抽象路径名指定的目录，包括所有必需但不存在的父目录。
//        System.out.println(file.mkdirs());
        // 重新命名此抽象路径名表示的文件。
//        System.out.println(file.renameTo(new File("target/bbb.txt")));
        // 设置此抽象路径名所有者执行权限的一个便捷方法
//        System.out.println(file.setExecutable(true));
        // 设置此抽象路径名的所有者或所有用户的执行权限。
//        System.out.println(file.setExecutable(true, true));
        // 设置此抽象路径名指定的文件或目录的最后一次修改时间。
//        System.out.println(file.setLastModified(System.currentTimeMillis()));
        // 设置此抽象路径名所有者读权限的一个便捷方法。
//        System.out.println(file.setReadable(true));
        // 设置此抽象路径名的所有者或所有用户的读权限。
//        System.out.println(file.setReadable(true, true));
        // 标记此抽象路径名指定的文件或目录，从而只能对其进行读操作。
//        System.out.println(file.setReadOnly());
        // 设置此抽象路径名所有者写权限的一个便捷方法。
//        System.out.println(file.setWritable(true));
        // 设置此抽象路径名的所有者或所有用户的写权限。
//        System.out.println(file.setWritable(true, true));
        // 返回此抽象路径名的路径名字符串。
//        System.out.println(file.toString());
        // 构造一个表示此抽象路径名的 file: URI。
//        System.out.println(file.toURI());
        // 已过时。 此方法不会自动转义 URL 中的非法字符。建议新的代码使用以下方式将抽象路径名转换为 URL：首先通过 toURI 方法将其转换为 URI，然后通过 URI.toURL 方法将 URI 装换为 URL。
//        System.out.println(file.toURL().toURI());
    }



}
