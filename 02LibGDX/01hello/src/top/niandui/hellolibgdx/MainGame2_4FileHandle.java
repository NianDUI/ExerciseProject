package top.niandui.hellolibgdx;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;

import java.util.Arrays;

/**
 * LibGDX_2.4: 文件的操作（FileHandle）
 * 1. 概述
 * LibGDX 应用运行在不同的平台： Desktop 系统（Windows，Linux，MAC OS X），Android， iOS 和 支持 JavaScript/WebGL 的浏览器，每一个平台对文件的读写操作都完全不同。LibGDX 的 Files 模块（Gdx.files）提供了统一的抽象的接口对不同平台的文件进行统一的读写操作。
 *
 * 2. FileHandle
 * FileHandle 类，代表一个文件或文件夹，文件或文件夹可以来自 文件系统，classpath， Android SD 卡，assets 资源文件目录。
 *
 * 文件可以存储在多个地方，分别对应不同的存储类型，下面介绍常用的其中 3 种存储类型的文件：
 *
 * Internal Files： 内部文件（只读），所有在 assets 资源文件夹中的文件（图片，音频等文件）属于内部文件，最终将被打包进 jar 包或 APK 文件中。
 * Local Files： 本地文件（可读写），用于保存一些轻量级的数据，例如保存游戏状态。Android 平台将保存到应用私有目录。
 * External Files： 外部文件（可读写），用于保存大文件，例如游戏截屏图片，从网上下载的文件。Android 平台（AndroidManifest.xml 需要配置外部存储器读写权限）将保存到外部 SD 卡中。
 *
 *
 */
public class MainGame2_4FileHandle implements ApplicationListener {
    @Override
    public void create() {
        // 在不同的平台，某些存储类型可能不可用，使用前需要校验。校验外部/本地存储是否可用：
        // 注意： HTML5 平台只有 Internal 类型的文件才可用并且只读，其他类型文件的读写均不支持。
        boolean isExtAvailable = Gdx.files.isExternalStorageAvailable();
        boolean isLocAvailable = Gdx.files.isLocalStorageAvailable();

        System.out.println("外部存储：" + isExtAvailable);
        System.out.println("内部存储：" + isLocAvailable);

        // 查询本地/外部存储的根目录：
        String extRoot = Gdx.files.getExternalStoragePath();
        String locRoot = Gdx.files.getLocalStoragePath();
        System.out.println("外部存储根目录：" + extRoot);
        System.out.println("内部存储根目录：" + locRoot);

        // 获取一个 FileHandle 实例：
        FileHandle intHandle = Gdx.files.internal("data/myfile.txt");
        FileHandle locHandle = Gdx.files.local("data/myfile.txt");
        FileHandle extHandle = Gdx.files.external("data/myfile.txt");

        // 校验文件或文件夹是否存在：
        boolean exists = Gdx.files.external("doitexist.txt").exists();

        // 校验一个 FileHandle 实例是否是文件夹：
        boolean isDirectory = Gdx.files.local("02LibGDX/").isDirectory();

        // 遍历一个文件夹下的所有文件：
        FileHandle[] files = Gdx.files.local("02LibGDX/").list();
        for (FileHandle file : files) {
            System.out.println(file.toString());
        }


        // 2. 读文件
        FileHandle file = Gdx.files.internal("data/myfile.txt");
        // 将文件内容读取为字符串：
        String text = file.readString();
        System.out.println(text);

        // 将文件内容读取为二进制数据：
        byte[] bytes = file.readBytes();
        System.out.println(Arrays.toString(bytes));

        // 3. 写文件
        file = Gdx.files.local("02LibGDX/data/myfile.txt");
        // 写字符串数据到文件：
        file.writeString("\nMy god, it's full of stars", true);

        // 写二进制数据到文件：
        file.writeBytes(new byte[]{20, 3, -2, 10}, true);

        // 4. 键值对存储: Preferences
        // Preferences 的值支持 String，long，int，float，boolean 类型。
        // 获取一个 Preferences 实例：
        Preferences prefs = Gdx.app.getPreferences("MyPreferences");
        // 读写数据：
        prefs.putString("name", "Donald Duck");

        String name = prefs.getString("name", "No name stored");
        System.out.println(name);

        prefs.putBoolean("soundOn", true);
        prefs.putInteger("highscore", 10);

        // 修改了数据后，必须要调用 flush() 方法才能将数据同步到磁盘持久化：
        prefs.flush();
        // Preferences 文件的实际存储位置：
        //
        // Windows： %UserProfile%/.prefs/MyPreferences ；
        // Linux 和 OS X： ~/.prefs/MyPreferences ；
        // Android：应用私有目录，使用 Android 的 SharedPreferences 进行存储。


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
