package test01;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @Title: Main01.java
 * @description: Main01
 * @time: 2020/6/22 11:47
 * @author: liyongda
 * @version: 1.0
 */
public class Main01 extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 实例化按钮
        Button button = new Button("按钮上的文字");
        // 添加按钮点击事件
        button.setOnMouseClicked(event ->  System.out.println("按钮" + event));


        // 创建布局控件
        StackPane stackPane = new StackPane();
        // 将button添加到布局
        stackPane.getChildren().add(button);
        // 创建场景 宽=400 高=400
        Scene scene = new Scene(stackPane, 400, 400);
        // 给场   景添加事件处理的对象 场景点击事件
        scene.setOnMousePressed(event -> System.out.println("场景" + event));

        // 将场景添加到窗口
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main01");
        // 显示窗口
        primaryStage.show();
    }
}
