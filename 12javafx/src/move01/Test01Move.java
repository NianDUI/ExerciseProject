package move01;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @Title: Test01Move.java
 * @description: Test01Move
 * @time: 2020/6/22 14:53
 * @author: liyongda
 * @version: 1.0
 */
public class Test01Move extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new Label("滑动")));
        primaryStage.show();
        new Thread(() -> {
            Stage stage = primaryStage;
            long sleep = 1;
            while (true) {
                try {
                    Rectangle2D rectangle2D = Screen.getPrimary().getBounds();
                    double maxWidth = rectangle2D.getWidth() - stage.getWidth();
                    double maxHeight = rectangle2D.getHeight() - stage.getHeight();
                    for (double i = 0; i <= maxWidth; i++) {
                        stage.setX(i);
                        Thread.sleep(sleep);
                    }
                    stage.setX(maxWidth);
                    for (double i = 0; i <= maxHeight; i++) {
                        stage.setY(i);
                        Thread.sleep(sleep);
                    }
                    stage.setY(maxHeight);
                    for (double i = maxWidth; i >= 0; i--) {
                        stage.setX(i);
                        Thread.sleep(sleep);
                    }
                    stage.setX(0);
                    for (double i = maxHeight; i >= 0; i--) {
                        stage.setY(i);
                        Thread.sleep(sleep);
                    }
                    stage.setY(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
