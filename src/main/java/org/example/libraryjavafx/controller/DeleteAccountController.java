package org.example.libraryjavafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.libraryjavafx.exception.Message;
import org.example.libraryjavafx.exception.ServiceException;
import org.example.libraryjavafx.factory.Container;
import org.example.libraryjavafx.model.User;
import org.example.libraryjavafx.service.RegularService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeleteAccountController implements Initializable {
    public PasswordField passwordField;
    private Container container;
    private RegularService service;
    private Stage stageSource;
    private Stage stageSelf;
    private User user;
    public void setResources(Container container, Stage stageSelf, User user,Stage stageSoruce){
        this.container = container;
        this.stageSelf = stageSelf;
        this.user = user;
        this.service = container.getRegularService();
        this.stageSource = stageSoruce;
    }
    public static void openDeleteAccountWindow(User user, Stage stageSource, Container containerSource) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Library App - Delete Account");
        FXMLLoader loader=new FXMLLoader(RegularController.class.getResource("/org/example/libraryjavafx/delete-account-window.fxml"));
        AnchorPane myPane = (AnchorPane) loader.load();

        DeleteAccountController controller = loader.getController();
        controller.setResources(containerSource,stage,user,stageSource);

        Scene myScene = new Scene(myPane);
        stage.setScene(myScene);

        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleDeleteAccount(ActionEvent actionEvent) throws IOException {
        String password = passwordField.getText();
        if(password == null || password == ""){
            Message.showError("Please provide a password");
            return;
        }
        try{
            service.deleteAccount(user,password);
            this.stageSelf.close();
            this.stageSource.close();
            Message.showMessage("Success","Account deleted with success");
            SignInController.openSignInWindow(this.container);
        } catch (ServiceException e) {
            this.stageSelf.close();
            Message.showError(e.getMessage());
        }
    }
}
