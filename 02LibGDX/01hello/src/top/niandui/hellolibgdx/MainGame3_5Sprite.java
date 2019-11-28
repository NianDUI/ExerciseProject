package top.niandui.hellolibgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * LibGDX_3.5: 精灵（Sprite）
 *
 * 1. 概述
 * Sprite（精灵）继承自 TextureRegion（纹理区域），本质上可以看作是一个 TextureRegion，但它比 TextureRegion 要强大的多，封装得更完善，除了表示一张图片外，还附加拥有许多属性，例如 在屏幕中的位置/坐标（绘制起点），缩放比，旋转角度，透明度/颜色 等。Sprite 更加具体地描述了游戏中的一个元素（游戏人物，道具，背景图片等），可以看做是一张图片加上绘制这张图片时附加的各种变换属性的封装。
 *
 * Sprite 可以看做是如下构成：
 *
 * Sprite = Texture/TextrueRegion + 属性（坐标，缩放比，旋转角度，是否X / Y轴方向取镜像，透明度/颜色 等）
 * Sprite 类的继承关系图.png
 */
public class MainGame3_5Sprite extends ApplicationAdapter {

    private SpriteBatch batch;

    // 纹理
    private Texture texture;

    // 精灵
    private Sprite sprite;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // 创建纹理, badlogic.jpg 图片的宽高为 256 * 256
        texture = new Texture(Gdx.files.internal("badlogic.jpg"));

        // 使用纹理创建精灵, 精灵宽高为该纹理的宽高
        sprite = new Sprite(texture);

        // 设置精灵的位置（左下角绘制起点）
        sprite.setPosition(64, 128);

        // 设置 旋转 和 缩放 的 起点（参考点/旋转点/旋转中心）为图片的左下角, 默认为 Sprite（图片）宽高的一半, 即图片的中心点
        sprite.setOrigin(0, 0);

        // 设置精灵的旋转角度, 单位为度, 逆时针方向为正
        sprite.setRotation(15.0F);

        // 设置精灵的 X 和 Y 轴方向的缩放比, 均缩小为原来的 1/2
        sprite.setScale(0.5F, 0.5F);

        // 设置精灵在水平方向取镜像, 竖直方不取镜像
        sprite.flip(true, false);
    }

    @Override
    public void render() {
        // 黑色清屏
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 绘制精灵前后也需要分别调用 begin() 和 end()
        batch.begin();

        /*
         * 绘制精灵, 因为精灵自身拥有 坐标值, 缩放比, 旋转角度, 透明度/颜色 等属性,
         * 所以只需要将 SpriteBatch 传给 Sprite, 它就可以根据它表示的图片结合各种属性 将结果主动绘制到屏幕上
         */
        sprite.draw(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        // 当应用退出时释放资源
        if (batch != null) {
            batch.dispose();
        }
        /*
         * 这里只需要释放 texture 资源, sprite 和 TextureRegion 一样, 也没有 dispose() 方法, 不需要销毁 sprite,
         * 因为 sprite 只是描述了 texture/region 和 其相关的一些属性, texture 才是最终真正实体资源,
         * texture 销毁后, sprite 不能再使用, 因为 sprite 也是依赖 texture 存在的。
         */
        if (texture != null) {
            texture.dispose();
        }
    }
}
