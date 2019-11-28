package top.niandui.hellolibgdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 游戏主程序的入口类, 实现 ApplicationListener 接口
 *
 *      LibGDX 应用的生命周期事件方法定义在 ApplicationListener 接口中，开发人员通过在实现该接口类中处理生命周期事件方法的调用。
 *      ApplicationListener 接口的实现类，可以理解为一个游戏的实例，游戏最高级别的控制器，也是游戏主程序开始执行的入口类，
 *      在平台启动器中实例化实现该接口的实现类启动游戏。
 *
 *      2. ApplicationAdapter 抽象类
 *          在游戏开发中如果只需要使用到生命周期方法中的几个方法，
 *          MainGame 可以继承 ApplicationAdapter 抽象类，ApplicationAdapter 空实现了 ApplicationListener 接口的所有方法。
 *
 *      a
 */
public class MainGame implements ApplicationListener {

    // 纹理画布
    private SpriteBatch batch;

    // 纹理
    private Texture texture;

    // 当应用被创建时调用一次。
    @Override
    public void create() {
        // 创建纹理画布
        batch = new SpriteBatch();

        /*
         * 使用 assets 文件夹中的图片 badlogic.jpg 创建纹理,
         * 文件路径相对于 assets 文件夹根目录, 如果图片放在子目录, 则路径为 "xxx/badlogic.jpg"
         */
        texture = new Texture("badlogic.jpg");
    }

    // 游戏屏幕尺寸改变并且不处于暂停状态将被调用，在 create() 方法之后也会被调用一次。
    @Override
    public void resize(int width, int height) {
        System.out.printf("resize(int %d, int %d)\n", width, height);
    }

    // ApplicationListener 中的游戏循环渲染方法，每时每刻都在被调用。游戏逻辑的更新通常也是在这个方法中被执行。
    @Override
    public void render() {
        /*
         * 设置清屏颜色为红色（RGBA）,
         *
         * LibGDX 中使用 4 个浮点类型变量（值范围 0.0 ~ 1.0）表示一个颜色（分别表示颜色的 RGBA 四个通道）,
         *
         * 十六进制颜色与浮点颜色之间的转换: 将十六进制颜色的每一个分量除以 255 得到的浮点数就是浮点颜色对应的通道值。
         */
        Gdx.gl.glClearColor(1, 0, 0,1);

        // 清屏
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* 使用画笔将纹理绘制在屏幕上 */
        batch.begin();                  // 绘制开始
        batch.draw(texture, 0,0); // 在屏幕左下角绘制纹理
        batch.end();                    // 绘制结束

        /*                   日志输出                    */
        // 输出 debug 级别 log
        Gdx.app.debug("MyTag", "my debug message");

        // 输出 info 级别 log
        Gdx.app.log("MyTag", "my informative message");

        // 输出 error 级别 log
        Gdx.app.error("MyTag", "my error message");
        Gdx.app.error("MyTag", "my error message", new Exception("exception"));

        // 限制日志输出指定的级别
        Gdx.app.setLogLevel(Application.LOG_NONE);
        // 可以选择以下常量之一：
        // Application.LOG_NONE： 所有级别的 log 都不输出；
        // Application.LOG_DEBUG： 输出 debug，info，error 级别的 log （即输出所有级别的 log ）；
        // Application.LOG_INFO： 输出 info，error 级别的 log ；
        // Application.LOG_ERROR： 只输出 error 级别的 log 。

    }

    //当游戏界面被新的一个界面覆盖时（例如按下 Home 键回到主界面时被主界面覆盖，来电时被来电界面覆盖），该方法将被调用。
    // 通常在这里保存暂停时的游戏状态。
    @Override
    public void pause() {

    }

    //  被其他界面覆盖后（pause 状态后），重新回到游戏界面时，该方法被调用。
    @Override
    public void resume() {

    }

    //  当应用被销毁时调用。
    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
    }
}
