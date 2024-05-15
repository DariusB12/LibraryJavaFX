package org.example.libraryjavafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.libraryjavafx.exception.Message;
import org.example.libraryjavafx.exception.ServiceException;
import org.example.libraryjavafx.exception.ValidationException;
import org.example.libraryjavafx.factory.Container;
import org.example.libraryjavafx.model.User;
import org.example.libraryjavafx.model.UserRole;
import org.example.libraryjavafx.service.AuthenticationService;
import org.example.libraryjavafx.service.dto.authenticationDTO.SignUpRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    public TextField textFieldUsername;
    public PasswordField textFieldPassword;
    public TextField textFieldFirstName;
    public TextField textFieldLastName;
    public TextField textFieldAddress;
    public TextField textFieldPhone;
    public TextField textFieldCNP;
    public TextField textFieldEmail;
    public Button buttonSignUp;
    public ChoiceBox choiceBoxRole;
    public TextField textFieldEmployeePassword;
    private Container container;
    private Stage stageSelf;
    private AuthenticationService authenticationService;
    public static void openSignInWindow(Container containerSource) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Library App - Sign Up");
        FXMLLoader loader=new FXMLLoader(RegularController.class.getResource("/org/example/libraryjavafx/signUp-window.fxml"));
        AnchorPane myPane = (AnchorPane) loader.load();

        SignUpController controller = loader.getController();
        controller.setResources(containerSource,stage);

        Scene myScene = new Scene(myPane);
        stage.setScene(myScene);

        stage.show();
    }

    public void setResources(Container container, Stage stageSelf) {
        this.container = container;
        this.stageSelf = stageSelf;
        this.authenticationService = container.getAuthenticationService();

        initialize();
    }

    private void initialize(){
        //initialize choice box
        ObservableList<UserRole> userRoles = FXCollections.observableArrayList();
        userRoles.add(UserRole.REGULAR);
        userRoles.add(UserRole.EMPLOYEE);
        choiceBoxRole.setItems(userRoles);
        //disable pass field if not Employee
        choiceBoxRole.getSelectionModel().selectedItemProperty().addListener(
                (observable,oldValue,newValue)->{
                    if(newValue == UserRole.EMPLOYEE){
                        textFieldEmployeePassword.setDisable(false);
                    }
                    else{
                        textFieldEmployeePassword.setDisable(true);
                    }
                }
        );
        choiceBoxRole.setValue(UserRole.REGULAR);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleSignUp(ActionEvent actionEvent) throws IOException {
        UserRole userRole = (UserRole) choiceBoxRole.getValue();

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username(textFieldUsername.getText())
                .password(textFieldPassword.getText())
                .firstName(textFieldFirstName.getText())
                .lastName(textFieldLastName.getText())
                .address(textFieldAddress.getText())
                .phone(textFieldPhone.getText())
                .cnp(textFieldCNP.getText())
                .email(textFieldEmail.getText())
                .role(userRole)
                .build();
        signUpRequest.setRolePassword(null);

        if(userRole == UserRole.EMPLOYEE){
            String passwordEmployee = textFieldEmployeePassword.getText();
            signUpRequest.setRolePassword(passwordEmployee);
            if(passwordEmployee == "" || passwordEmployee == null){
                Message.showError("Provide the password for EMPLOYEE account");
                return;
            }
        }
        try{
            User user = authenticationService.signUpUser(signUpRequest);
            if(user.getUserRole() == UserRole.REGULAR){
                RegularController.openMainWindowRegular(user,this.stageSelf,this.container);
            }else{
                EmployeeController.openMainWindowEmployee(user,this.stageSelf,this.container);
            }
        } catch (ValidationException | ServiceException e) {
            Message.showError(e.getMessage());
        }
    }


}
