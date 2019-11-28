package top.niandui.hellolibgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * LibGDX_3.2: 纹理画布（SpriteBatch）
 *
 * 1. 概述
 * SpriteBatch 类，英文直译则为“精灵批处理”，
 *      它主要用于将纹理绘制到屏幕上，类似于 Android 和 HTML5 中的 Canvas （画布），因此这里我姑且把它称为“纹理画布”。
 *      SpriteBatch 将对所有绘制的纹理进行批处理并经过优化后发送到 GPU。
 *      SpriteBatch 绘制的坐标原点为屏幕左下角，X 轴正方向水平向左，Y 轴正方向水平向上
 *
 * 注意：
 *      在绘制前后必须调用 batch.begin() 和 batch.end() 方法，
 *      在不需要这个 SpriteBatch 对象时，需要手动调用 batch.dispose() 方法释放资源。
 */
public class MainGame3_2SpriteBatch extends ApplicationAdapter {

    // 声明纹理画布
    private SpriteBatch batch;

    private Texture texture;

    @Override
    public void create() {
        // 创建 SpriteBatch
        batch = new SpriteBatch();

        texture = new Texture("badlogic.jpg");
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 绘制开始
        batch.begin();

        // 在这里进行各种纹理的绘制
        batch.draw(texture, 0, 0);
        // batch.draw(...);
        // ...
        // batch.draw(...);

        // 绘制结束
        batch.end();
    }

    @Override
    public void dispose() {
        // 当应用退出时释放资源
        if (texture != null) {
            texture.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
    }
}
