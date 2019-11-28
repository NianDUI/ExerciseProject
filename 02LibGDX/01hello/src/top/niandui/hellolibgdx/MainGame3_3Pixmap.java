package top.niandui.hellolibgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * LibGDX_3.3: 内存中的图片（Pixmap）
 *
 * 1. 概述
 *      一个 Pixmap 代表内存中的一张图片，它支持加载现有的图片，有以像素为单位表示的宽高，并且每一个像素点有指定的颜色值。
 *      Pixmap 支持点、线、圆、矩形、三角形，以及将另一 Pixmap 绘制到当前 Pixmap 等基本图形图像的绘制操作，最后 Pixmap 需要加载为 Texture （纹理）才能绘制到屏幕上。
 *      Pixmap 的数据存储在本地堆内存中，一个 Pixmap 对象不再需要使用到时需要调用其 dispose() 方法释放资源的占用。
 *
 *      在 Pixmap 上绘制图形时的坐标原点为 Pixmap 的左上角，X轴正方向水平向左，Y轴正方向竖直向下。
 *      SpriteBatch 与 Pixmap 坐标关系.png
 *
 * 2. 根据现有的一张图片创建 Pixmap 步骤
 * // 根据给定的图片创建一个 Pixmap 实例，创建后 图片“贴”在 pixmap 上， pixmap 宽高为图片的宽高
 * Pixmap pixmap = new Pixmap(Gdx.files.internal("badlogic.jpg"));
 *
 * // 设置 pixmap 绘图颜色
 * pixmap.setColor(1, 0, 0, 1);
 *
 * // 在 Pixmap 的 (128, 128) 坐标位置填充一个半径为 64 的圆
 * pixmap.fillCircle(128, 128, 64);
 *
 * // 需要将 pixmap 绘制到屏幕上，需要将 pixmap 加载为纹理
 * Texture texture = new Texture(pixmap);
 *
 * // pixmap 不再需要使用到时，释放占用的资源
 * pixmap.dispose();
 */
public class MainGame3_3Pixmap extends ApplicationAdapter {

    private SpriteBatch batch;

    private Texture texture;

    private Pixmap pixmap;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // 创建一个宽高都为 256, 像素点颜色值格式为 RGBA8888(每个像素颜色值占 4 个字节) 的 Pixmap
        pixmap = new Pixmap(256, 256, Pixmap.Format.RGBA8888);

        // 设置绘图颜色为白色
        pixmap.setColor(1,1,1,1);
        // 将整个 pixmap 填充为当前设置的颜色
        pixmap.fill();

        // 设置绘图颜色为红色
        pixmap.setColor(1, 0, 0, 1);
        // 画一个空心圆
        pixmap.drawCircle(64,64,32);

        // 设置绘图颜色为绿色
        pixmap.setColor(Color.GREEN);
        // 画一条线段, 线段两点为 (0, 0) 到 (256, 128)
        pixmap.drawLine(0,0,256,128);

        // 设置绘图颜色为蓝色
        pixmap.setColor(Color.BLUE);
        // 画一个矩形, 矩形左上角坐标(128, 128), 宽高均为64
        pixmap.drawRectangle(128,128,64,64);

        // 设置绘图颜色为黄色
        pixmap.setColor(Color.YELLOW);
        // 填充一个三角形, 三点(0, 256), (0, 128), (128, 128)
        pixmap.fillTriangle(0,256,0,128,128,128);

        // pixmap 处理完成后转换成纹理
        texture = new Texture(pixmap);

        // pixmap 已不再需要用到, 释放资源
        pixmap.dispose();

    }

    @Override
    public void render() {
        // 黑色清屏
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // 在屏幕左下角绘制纹理
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
