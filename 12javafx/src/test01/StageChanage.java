package test01;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @Title: StageChanage.java
 * @description: 舞台(窗体)切换
 * @time: 2020/6/22 14:05
 * @author: liyongda
 * @version: 1.0
 */
public class StageChanage extends Application {
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        // 窗口点击叉号关闭询问
        primaryStage.setOnCloseRequest(event -> {
            // 消除默认事件
            event.consume();
            handleClose();
        });

        Button button = new Button("关闭窗口");
        button.setOnMouseClicked(event -> handleClose());
        VBox vBox = new VBox(button);
        Scene scene = new Scene(vBox, 400, 400);
        primaryStage.setTitle("舞台(窗体)切换");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void handleClose() {
        boolean ret = alert("关闭窗口", "是否关闭窗口？");
        System.out.println("ret = " + ret);
        if (ret) {
            stage.close();
        }
    }

    public static boolean alert(String title, String msg) {
        final boolean[] bl = {false};
        // 创建舞台
        Stage stage = new Stage();
        // 设置显示模式
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        Label label = new Label(msg);
        Button buttonYes = new Button("是");
        buttonYes.setOnMouseClicked(event -> {
            bl[0] = true;
            stage.close();
        });
        Button buttonNo = new Button("否");
        buttonNo.setOnMouseClicked(event -> {
            bl[0] = false;
            stage.close();
        });
        VBox vBox = new VBox(label, buttonYes, buttonNo);
        // 布局居中显示
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox, 200, 200);
        stage.setScene(scene);

        // 等待窗体关闭才继续
        stage.showAndWait();
        return bl[0];
    }
}
