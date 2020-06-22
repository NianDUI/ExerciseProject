package test02var;

/**
 * @Title: TestVar.java
 * @description: var简单测试
 * @time: 2020/6/22 10:09
 * @author: liyongda
 * @version: 1.0
 */
public class TestVar {
    public static void main(String[] args) {
        for (var i = 0; i < 5; i++) {
            System.out.println("i = " + i);
        }
        var a = "aa";
        System.out.println("a = " + a);
        var b = 156L;
        System.out.println("b = " + b);
        var c = """
                <html>
                    <a>asdfa</a>
                </html>
                """;
        System.out.println("c = " + c);
    }
}
