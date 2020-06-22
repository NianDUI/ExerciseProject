package test01;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @Title: SceneChanage.java
 * @description: 场景切换
 * @time: 2020/6/22 13:42
 * @author: liyongda
 * @version: 1.0
 */
public class SceneChanage extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Button button1 = new Button("场景1 Button按钮");
        Button button2 = new Button("场景2 Button按钮");

        VBox vBox = new VBox();
        vBox.getChildren().add(button1);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(button2);

        Scene scene1 = new Scene(vBox, 400, 400);
        Scene scene2 = new Scene(stackPane, 400, 400);

        // 场景一
        button1.setOnMouseClicked(event -> primaryStage.setScene(scene2));
        // 场景二
        button2.setOnMouseClicked(event -> primaryStage.setScene(scene1));

        primaryStage.setScene(scene1);
        primaryStage.setTitle("场景切换Scene");
        primaryStage.show();
    }
}
