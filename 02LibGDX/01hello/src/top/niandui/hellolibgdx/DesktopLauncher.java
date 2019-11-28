package top.niandui.hellolibgdx;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

    public static void main(String[] args) {
        // 应用配置
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 320;     // 指定窗口宽度
        config.height = 480;    // 指定窗口高度

        config.resizable = false; // 窗口设置为大小不可改变

        // 创建游戏主程序启动入口类 MainGame 对象, 传入配置 config, 启动游戏程序
//        new LwjglApplication(new MainGame(), config);
//        new LwjglApplication(new MainGame2_3LifeCycle(), config);
//        new LwjglApplication(new MainGame2_4FileHandle(), config);
//        new LwjglApplication(new MainGame3_1Texture(), config);
//        new LwjglApplication(new MainGame3_2SpriteBatch(), config);
//        new LwjglApplication(new MainGame3_3Pixmap(), config);
//        new LwjglApplication(new MainGame3_4TextureRegion(), config);
        new LwjglApplication(new MainGame3_5Sprite(), config);

    }

}
