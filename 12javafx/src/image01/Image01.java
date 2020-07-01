package image01;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * @Title: Image01.java
 * @description: 图片展示
 * @time: 2020/7/1 10:51
 * @author: liyongda
 * @version: 1.0
 */
public class Image01 extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Image image = new Image("https://t8.baidu.com/it/u=1484500186,1503043093&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1594176761&t=d984b90bc6e998a8c4a8f8684c6a420c");

        ImageView imageView = new ImageView();
        imageView.setImage(image);

        VBox vBox = new VBox();
        vBox.getChildren().add(imageView);

        Scene scene = new Scene(vBox, image.getWidth(), image.getHeight());

        primaryStage.setScene(scene);
        primaryStage.show();

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ImageIO.write(bufferedImage, "png", new File("zz1.png"));
    }
}
