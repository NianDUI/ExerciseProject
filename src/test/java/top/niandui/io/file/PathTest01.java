package top.niandui.io.file;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Path测试
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/6/4 14:19
 */
public class PathTest01 {

    @Test
    public void test01() {
        Path path1 = Paths.get("D:", "ztest", "test.txt");
        // D:\ztest
        Path basePath = Paths.get("D:", "ztest");

        // resolve 将参数拼接到后面，产生子路径 D:\ztest\testdir
        Path testdir = basePath.resolve("testdir");

        // resolveSibling 将最后一个使用参数替换 D:\ztest\testdir02
        Path testdir02 = testdir.resolveSibling("testdir02");

        System.out.println();
    }



}
