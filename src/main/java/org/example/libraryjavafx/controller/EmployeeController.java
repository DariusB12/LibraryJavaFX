package org.example.libraryjavafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.libraryjavafx.controller.dto.BookDisplay;
import org.example.libraryjavafx.exception.Message;
import org.example.libraryjavafx.exception.ServiceException;
import org.example.libraryjavafx.factory.Container;
import org.example.libraryjavafx.model.User;
import org.example.libraryjavafx.service.RegularService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {
    public VBox verticalBox;
    private Container container;
    private RegularService service;
    private User user;
    private Stage stageSelf;
    private List<BookDisplay> allBooks = new ArrayList<>();

    public static void openMainWindowEmployee(User user, Stage stageSource, Container containerSource) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Library App - " + user.getUsername() + " - EMPLOYEE");
        FXMLLoader loader=new FXMLLoader(RegularController.class.getResource("/org/example/libraryjavafx/main-window-employee.fxml"));
        AnchorPane myPane = (AnchorPane) loader.load();

        EmployeeController controller = loader.getController();
        controller.setResources(containerSource,stage,user);

        Scene myScene = new Scene(myPane);
        stage.setScene(myScene);

        stageSource.close();
        stage.show();
    }

    public void setResources(Container container,Stage stageSelf,User user){
        this.container = container;
        this.stageSelf = stageSelf;
        this.user = user;
        this.service = container.getRegularService();
        loadBooks();
    }
    private void loadBooks(){
        try{
            allBooks = service.getAllBooks(this.user);
        } catch (ServiceException e) {
            Message.showError(e.getMessage());
            return;
        }

        for(BookDisplay book : allBooks){
            book.setOnMouseClicked(event -> {
                // Handle click event on any child element
                System.out.println("Clicked on book details: " + getClass().getSimpleName());
                // You can add custom logic here based on the clicked element
            });
        }
        for(BookDisplay book : allBooks){
            this.verticalBox.getChildren().add(book);
        }
    }
    private void onclickevent(){
        //TODO:ONCLIK
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleDeleteAccount(ActionEvent actionEvent) {
    }
}
