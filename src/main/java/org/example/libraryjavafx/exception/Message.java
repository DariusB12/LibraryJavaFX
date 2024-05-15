package org.example.libraryjavafx.exception;

import javafx.scene.control.Alert;

public class Message {
    private Message(){}
    public static void showError(String context){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setContentText(context);
        alert.showAndWait();
    }
    public static void showMessage(String title, String context){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(context);
        alert.showAndWait();
    }

}
