package top.niandui.hellolibgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * LibGDX_3.1: 纹理（Texture）
 *
 * 1. 概述
 * 纹理类（Texture）负责解码一个图片文件并加载到 GPU 内存，可以看做代表的是一张图片，图片文件通常是放在 assets 资源文件夹中，一个图片文件被加载为 Texture 实例后，如果不再需要使用到这个 texture，需要手动调用释放资源的方法 texture.dispose() 进行资源释放。
 *
 * Texture 常用构造方法：
 * Texture(FileHandle file)： 使用指定的 FileHandle 创建为纹理
 * Texture(String internalPath)： 使用 assets 资源文件夹中的文件的相对路径名创建纹理
 *
 * Texture 常用方法：
 * int getWidth()： 获取纹理的宽度
 * int getHeight()： 获取纹理的高度
 * void dispose()： 释放纹理资源
 *
 */
public class MainGame3_1Texture extends ApplicationAdapter {

    private SpriteBatch batch;

    private Texture texture;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // 加载 assets 文件夹下的 badlogic.jpg 文件，创建纹理
        texture = new Texture(Gdx.files.internal("badlogic.jpg"));

        // 获取纹理（图片）的原始像素宽高
        int width = texture.getWidth();
        int height = texture.getHeight();
        System.out.println(width + " * " + height);

    }

    @Override
    public void resize(int width, int height) {
        // 红色清屏
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // 将纹理绘制到屏幕左下角
        batch.draw(texture, 0,0);

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
