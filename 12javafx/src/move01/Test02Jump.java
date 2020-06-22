package move01;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Random;

/**
 * @Title: Test02Jump.java
 * @description: 跳动
 * @time: 2020/6/22 15:12
 * @author: liyongda
 * @version: 1.0
 */
public class Test02Jump extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new Label("跳动")));
        primaryStage.show();
        new Thread(() -> {
            Stage stage = primaryStage;
            long sleep = 10;
            while (true) {
                Rectangle2D rectangle2D = Screen.getPrimary().getBounds();
                double maxWidth = rectangle2D.getWidth() - stage.getWidth();
                double maxHeight = rectangle2D.getHeight() - stage.getHeight();
                Random random = new Random();
                stage.setX(random.nextDouble() * maxWidth);
                stage.setY(random.nextDouble() * maxHeight);
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
