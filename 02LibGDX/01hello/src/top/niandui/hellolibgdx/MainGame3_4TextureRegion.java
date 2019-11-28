package top.niandui.hellolibgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * LibGDX_3.4: 纹理区域（TextureRegion）
 *
 * 1. 概述
 *  TextureRegion（纹理区域） 表示 Texture（纹理）的一部分矩形区域（当然也可以表示整个 Texture 区域），可以用来绘制纹理中的一部分显示在屏幕上。
 *  TextureRegion 可以通过记录 Texture 中的一个 坐标起点 和 从这个起点开始截取的宽高 来表示 Texture 的一部分。
 *
 *  TextureRegion 描述 Texture 区域时的 坐标点 是相对于 Texture 的左上角原点
 *  TextureRegion描述Texture区域时的坐标点的位置关系.png
 */
public class MainGame3_4TextureRegion extends ApplicationAdapter {

    private SpriteBatch batch;

    // 纹理
    private Texture texture;

    // 纹理区域
    private TextureRegion region;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // 创建纹理, badlogic.jpg 图片的宽高为 256 * 256
        texture = new Texture(Gdx.files.internal("badlogic.jpg"));

        // 创建纹理区域, region 表示在 texture 中 坐标 (0, 0) 为起点, 宽高均为 128 的矩形区域（即为图片 1/4 的左上角部分）
        region = new TextureRegion(texture, 0,0,128,128);
    }

    @Override
    public void render() {
        // 黑色清屏
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        /*
         * 这次在屏幕左上角绘制纹理区域,
         * SpriteBatch 绘制时参考屏幕左下角为原点, Texture 左下角为绘制起点,
         * 所以让 region 刚好出现在左上角, 则 batch 绘制开始坐标为 (0, 屏幕高度 - region 高度),
         * Gdx.graphics.getHeight()表示屏幕的原始像素高度。
         */
        batch.draw(region,      // 需要绘制的 region
                0,           // region 左下角在屏幕中的 X 轴坐标
                Gdx.graphics.getHeight() - region.getRegionHeight()); // region 左下角在屏幕中的 Y 轴坐标

        batch.end();
    }

    @Override
    public void dispose() {
        // 当应用退出时释放资源
        if (batch != null) {
            batch.dispose();
        }

        /*
         * 这里只需要释放 texture 资源, region 没有 dispose() 方法, 不需要销毁 region,
         * 因为 region 只是通过参数描述了 texture 的一部分区域, texture 才是最终真正实体资源,
         * texture 销毁后, region 不能再使用, 因为 region 是依赖 texture 存在的。
         */
        if (texture != null) {
            texture.dispose();
        }

    }
}
