package org.example.libraryjavafx.controller.regular;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.libraryjavafx.controller.dto.BookDisplay;
import org.example.libraryjavafx.exception.Message;
import org.example.libraryjavafx.exception.ServiceException;
import org.example.libraryjavafx.factory.Container;
import org.example.libraryjavafx.model.*;
import org.example.libraryjavafx.service.RegularService;
import org.example.libraryjavafx.service.dto.JsonUtilsConverter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BorrowBookController implements Initializable {
//    public Pane paneBorrowBook;
    public ChoiceBox choiceBoxBorrowTerminals;
    public Stage stageSelf;
    public Container container;
    public User user;
    public RegularService regularService;
    public BookDisplay book;
    public VBox verticalBox;
    public TextArea textAreaDescription;

    public void setResources(Container container, Stage stageSelf, User user, BookDisplay book){
        this.container = container;
        this.stageSelf = stageSelf;
        this.user = user;
        this.regularService = container.getRegularService();
        this.book = book.deepCopy();
        initializePanel();
        initializeChoiceBox();
    }
    private void initializeChoiceBox() {
        try {
            ObservableList<Terminal> terminalObsList = FXCollections.observableArrayList();
            for(Terminal terminal : regularService.getAllTerminals(user)){
                if(terminal.getTerminalType() == TerminalType.BORROW)
                    terminalObsList.add(terminal);
            }
            choiceBoxBorrowTerminals.setItems(terminalObsList);
            if (!terminalObsList.isEmpty()) {
                choiceBoxBorrowTerminals.getSelectionModel().selectFirst();
            }
        } catch (ServiceException e) {
            Message.showError(e.getMessage());
        }
    }
    private void initializePanel(){
        verticalBox.getChildren().add(book);
    }

    public static void openBorrowBookWindow(Container container, User user, BookDisplay book, Stage parentStage) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Library App - Borrow Appointment");
        FXMLLoader loader=new FXMLLoader(RegularController.class.getResource("/org/example/libraryjavafx/regular-window/borrow-book-window.fxml"));
        AnchorPane myPane = (AnchorPane) loader.load();

        BorrowBookController controller = loader.getController();
        controller.setResources(container,stage,user,book);

        Scene myScene = new Scene(myPane);
        stage.setScene(myScene);
        // Set the modality to APPLICATION_MODAL to block interaction with the parent stage
        stage.initModality(Modality.APPLICATION_MODAL);

        // Set the owner of the second stage to the parent stage
        stage.initOwner(parentStage);

        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleSendBorrow(ActionEvent actionEvent) {
        String description = textAreaDescription.getText();
        Book book = JsonUtilsConverter.bookDisplayToBook(this.book);
        Terminal terminal = (Terminal) choiceBoxBorrowTerminals.getSelectionModel().getSelectedItem();
        if(terminal == null){
            Message.showError("no terminal selected");
            return;
        }
        try{
            regularService.sendBorrowAppointment(terminal,book,description,user);
            Message.showMessage("Success","borrow appointment made");
            stageSelf.close();
        } catch (ServiceException e) {
            Message.showError(e.getMessage());
            stageSelf.close();
        }
    }
}
