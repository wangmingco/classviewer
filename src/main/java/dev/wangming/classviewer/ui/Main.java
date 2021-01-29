package dev.wangming.classviewer.ui;

import dev.wangming.classviewer.net.handler.GetAllClassHandler;
import dev.wangming.classviewer.net.handler.HandlerRegister;
import dev.wangming.classviewer.util.ProcessUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Properties;

public class Main extends Application {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Properties props = System.getProperties();
        for (Object o : props.keySet()) {
            logger.debug("property {}, {}", o.toString(), props.getProperty(o.toString()));
        }

        ProcessUtil.process();
        HandlerRegister.register(1, new GetAllClassHandler());

        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        URL fxml = getClass().getResource("Main.fxml");
        if (fxml == null) {
            logger.error("Main.fxml 不存在");
            return;
        }

        Parent root = FXMLLoader.load(fxml);

        primaryStage.setTitle("ClassViewer");
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
