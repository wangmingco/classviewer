package dev.wangming.classviewer.ui;

import dev.wangming.classviewer.net.handler.GetAllClassHandler;
import dev.wangming.classviewer.net.TcpClient;
import dev.wangming.classviewer.util.AgentAttach;
import dev.wangming.classviewer.util.ClassDumper;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final Logger logger = LogManager.getLogger(Cache.class);

    public ChoiceBox processList;
    public TextField classname;
    public ListView classNames;
    public Button fileSave;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        processList.setItems(Cache.getProcessList());

        processList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String[] array = newValue.toString().split(" ");
            String pid = array[0];

            Cache.clear();

            String currentPid = Cache.getCurrentPid();
            if (currentPid != null) {
                TcpClient.close(currentPid);
            }

            Cache.setCurrentPid(pid);
            Cache.clear();
            AgentAttach.attach(pid, o -> new GetAllClassHandler().request(pid, ""));
        });

        fileSave.setOnAction(arg0 -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose Folder");
            File directory = directoryChooser.showDialog(new Stage());
            if (directory != null) {
                Cache.setSavePath(directory.getAbsolutePath());
                logger.info("class 文件保存路径:{}", Cache.getSavePath());
            }
        });

        FilteredList<String> filteredList = new FilteredList<>(Cache.getClassNames(), data -> true);
        classNames.setItems(filteredList);
        classNames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.info("Dump class {}, {}", Cache.getCurrentPid(), newValue.toString());
            ClassDumper.dump(Cache.getCurrentPid(), newValue.toString(), Cache.getSavePath());
        });

        classname.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(data -> {
                if (data == null) {
                    return false;
                }
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseSearch = newValue.toLowerCase().trim();
                return data.contains(lowerCaseSearch);
            });
        });
    }
}
