package org.example.libraryjavafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.libraryjavafx.controller.employee.EmployeeController;
import org.example.libraryjavafx.controller.regular.RegularController;
import org.example.libraryjavafx.exception.Message;
import org.example.libraryjavafx.exception.ServiceException;
import org.example.libraryjavafx.exception.ValidationException;
import org.example.libraryjavafx.factory.Container;
import org.example.libraryjavafx.model.User;
import org.example.libraryjavafx.model.UserRole;
import org.example.libraryjavafx.service.AuthenticationService;
import org.example.libraryjavafx.service.dto.authenticationDTO.SignInRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignInController implements Initializable {

    public Button buttonSignIn;
    public Button buttonSignUp;
    public TextField textFieldUsername;
    public PasswordField textFieldPassword;
    private Container container;
    private AuthenticationService authenticationService;
    private Stage stageSelf;

    public static void openSignInWindow(Container containerSource) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Library App - Sign In");
        FXMLLoader loader=new FXMLLoader(RegularController.class.getResource("/org/example/libraryjavafx/signIn-window.fxml"));
        AnchorPane myPane = (AnchorPane) loader.load();

        SignInController controller = loader.getController();
        controller.setResources(containerSource,stage);

        Scene myScene = new Scene(myPane);
        stage.setScene(myScene);

        stage.show();
    }

    public void setResources(Container container,Stage stageSelf){
        this.container = container;
        this.authenticationService = container.getAuthenticationService();
        this.stageSelf = stageSelf;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void handleSignIn(ActionEvent actionEvent) {
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        try{
            User user = authenticationService.signInUser(SignInRequest.builder()
                                              .username(username)
                                              .password(password)
                                              .build());
            if(user.getUserRole() == UserRole.REGULAR){
                RegularController.openMainWindowRegular(user,this.stageSelf,this.container);
            }else{
                EmployeeController.openMainWindowEmployee(user,this.stageSelf,this.container);
            }
        } catch (ServiceException | ValidationException e) {
            Message.showError(e.getMessage());
        } catch (IOException e) {
            Message.showError("Error opening the window");
        }
    }

    public void handleSignUp(ActionEvent actionEvent) throws IOException {
        SignUpController.openSignUnWindow(this.container);
        this.stageSelf.close();
    }
}
