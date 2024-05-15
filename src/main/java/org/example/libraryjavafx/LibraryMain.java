package org.example.libraryjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import org.example.libraryjavafx.controller.SignInController;
import org.example.libraryjavafx.factory.Container;
import org.example.libraryjavafx.factory.Factory;

public class LibraryMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SignInController.openSignInWindow(Factory.getContainer());
    }
    public static void main(String[] args) {
        launch();
    }
}