package top.niandui.I19适配器模式.I2对象适配器模式;

import org.junit.Test;

/**
 * 对象适配器模式
 *      对象适配器模式是另外6种结构型设计模式的起源。
 *      从下面的结构图可以看出，Adaptee类并没有method2()方法，而客户端则期待这个方法。
 *      与类适配器模式一样，为使客户端能够使用Adaptee类，我们把Adaptee与Target衔接起来。
 *      但这里我们不继承Adaptee，而是把Adaptee封装进Adapter里。这里Adaptee与Adapter是组合关系。
 */
public class AdapterTest {

    @Test
    public void test() {
        Adapter adapter = new Adapter(new Adaptee());
        adapter.method1();
        adapter.method2();
    }

    /**
     * 适配器模式涉及3个角色：
     * 源（Adaptee）：需要被适配的对象或类型，相当于插头。
     * 适配器（Adapter）：连接目标和源的中间对象，相当于插头转换器。
     * 目标（Target）：期待得到的目标，相当于插座。
     */
    public static class Adaptee {
        public void method1() {
            System.out.println("Adaptee.method1");
        }
    }
    public interface Target {
        void method1();
        void method2();
    }

    public static class Adapter implements Target {
        private Adaptee adaptee;

        public Adapter(Adaptee adaptee) {
            this.adaptee = adaptee;
        }
        @Override
        public void method1() {
            adaptee.method1();
        }
        @Override
        public void method2() {
            System.out.println("Target.method2");
        }
    }
}
