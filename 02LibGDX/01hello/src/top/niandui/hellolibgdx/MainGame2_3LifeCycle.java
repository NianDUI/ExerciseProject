package top.niandui.hellolibgdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

/**
 * LibGDX_2.3: 游戏的生命周期（ApplicationListener）
 */
public class MainGame2_3LifeCycle implements ApplicationListener {

    private static final String TAG = MainGame2_3LifeCycle.class.getSimpleName();

    private boolean isFirstRender = true;

    @Override
    public void create() {
        // 首先设置 log 输出级别为 debug 级别
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Gdx.app.debug(TAG, "create()");

    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.debug(TAG, width + " * " + height);
    }

    @Override
    public void render() {
        // 因为 render() 是每时每刻都在循环被调用的, 所以这里设置一个标记, 只在第一次调用时输出 log
        if (isFirstRender) {
            Gdx.app.debug(TAG, "render()");
            isFirstRender = false;
        }

    }

    @Override
    public void pause() {
        Gdx.app.debug(TAG, "pause()");
    }

    @Override
    public void resume() {
        Gdx.app.debug(TAG, "resume()");
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "dispose()");
    }
}
