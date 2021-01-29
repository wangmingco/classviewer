package dev.wangming.classviewer.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Cache {
    private static final Logger logger = LogManager.getLogger(Cache.class);

    private static final ObservableList<String> processList = FXCollections.observableArrayList(new ArrayList<>());
    private static final ObservableList<String> classNames = FXCollections.observableArrayList(new ArrayList<>());
    private static String currentPid = "";
    private static String savePath = "./";

    public static synchronized void clear() {
        classNames.clear();
    }

    public static synchronized void addClassName(String className) {
        Platform.runLater(() -> classNames.add(className));
    }

    public static synchronized ObservableList<String> getClassNames() {
        return classNames;
    }

    public static synchronized void setClassNames(List<String> list) {
        classNames.setAll(list);
    }

    public static synchronized String getCurrentPid() {
        return currentPid;
    }

    public static synchronized void setCurrentPid(String currentPid) {
        Cache.currentPid = currentPid;
    }

    public static String getSavePath() {
        return savePath;
    }

    public static void setSavePath(String savePath) {
        Cache.savePath = savePath;
    }

    public static synchronized void addProcess(String pid) {
        Platform.runLater(() -> processList.add(pid));
    }

    public static synchronized void removeProcess(String pid) {
        Platform.runLater(() -> {
            processList.remove(pid);
        });
    }

    public static synchronized ObservableList<String> getProcessList() {
        return processList;
    }
}
