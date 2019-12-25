package top.niandui.I19适配器模式;

/**
 * 接口适配器模式_缺省适配模式
 *      适配器模式是一种结构型设计模式。
 * 适配器模式的思想是：
 *      把一个类的接口变换成客户端所期待的另一种接口，从而使原本因接口不匹配而无法在一起工作的两个类能够在一起工作。
 *
 * 适配器模式涉及3个角色：
 *      源（Adaptee）：需要被适配的对象或类型，相当于插头。
 *      适配器（Adapter）：连接目标和源的中间对象，相当于插头转换器。
 *      目标（Target）：期待得到的目标，相当于插座。
 * 适配器模式包括3种形式：类适配器模式、对象适配器模式、接口适配器模式（或又称作缺省适配器模式）。
 *
 * 适配器模式的优缺点
 *      优点
 *          更好的复用性：系统需要使用现有的类，而此类的接口不符合系统的需要。那么通过适配器模式就可以让这些功能得到更好的复用。
 *          更好的扩展性：在实现适配器功能的时候，可以扩展自己源的行为（增加方法），从而自然地扩展系统的功能。
 *      缺点
 *          会导致系统紊乱：滥用适配器，会让系统变得非常零乱。例如，明明看到调用的是A接口，其实内部被适配成了B接口的实现，
 *              一个系统如果太多出现这种情况，无异于一场灾难。因此如果不是很有必要，可以不使用适配器，而是直接对系统进行重构。
 */
public class AdapterTest {
}
