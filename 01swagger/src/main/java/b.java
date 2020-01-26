/**
 * @Title: b.java
 * @description: TODO
 * @time: 2020/1/21 16:14
 * @author: liyongda
 * @version: 1.0
 */
public class b implements Ia {
    public static void aaa() {
        System.out.println("b.aaa");
    }

    @Override
    public void bbb() {
        System.out.println("b.bbb");
    }

    public static void main(String[] args) {
        Ia.aaa();
        b.aaa();
        b bb = new b();
        bb.bbb();
        Ia a = bb;
        a.bbb();
    }


}


