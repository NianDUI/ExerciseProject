/**
 * @Title: Ia.java
 * @description: TODO
 * @time: 2020/1/21 16:13
 * @author: liyongda
 * @version: 1.0
 */
public interface Ia {
    static void aaa() {
        System.out.println("Ia.aaa");
    }

    default void bbb() {
        System.out.println("Ia.bbb");
    }
}
