package org.example.libraryjavafx.controller.regular;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.libraryjavafx.controller.dto.BookDisplay;
import org.example.libraryjavafx.controller.dto.BorrowedBookDisplay;
import org.example.libraryjavafx.exception.Message;
import org.example.libraryjavafx.exception.ServiceException;
import org.example.libraryjavafx.factory.Container;
import org.example.libraryjavafx.model.Book;
import org.example.libraryjavafx.model.Terminal;
import org.example.libraryjavafx.model.TerminalType;
import org.example.libraryjavafx.model.User;
import org.example.libraryjavafx.service.RegularService;
import org.example.libraryjavafx.service.dto.JsonUtilsConverter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReturnBookController implements Initializable {
//    public Pane paneBorrowBook;
    public Stage stageSelf;
    public Container container;
    public User user;
    public RegularService regularService;
    public BorrowedBookDisplay book;
    public Terminal terminalReturn;
    public VBox verticalBox;
    public TextArea textAreaDescription;
    public Label labelReturnTerminal;

    public void setResources(Container container, Stage stageSelf, User user, BorrowedBookDisplay book){
        this.container = container;
        this.stageSelf = stageSelf;
        this.user = user;
        this.regularService = container.getRegularService();
        this.book = book.deepCopy();
        initializePanel();
        initializeTerminalLabel();
    }
    private void initializeTerminalLabel() {
        try {
            List<Terminal> allTerminals = regularService.getAllTerminals(user);
            terminalReturn = null;
            for(Terminal t : allTerminals){
                if(t.getTerminalType() == TerminalType.RETURN){
                    terminalReturn =t;
                }
            }
            if(terminalReturn ==null){
                Message.showError("cannot load the terminal for returns\n" +
                        "please try again later");
                return;
            }
            labelReturnTerminal.setText(terminalReturn.getName() + "\n" + terminalReturn.getLocation());
        } catch (ServiceException e) {
            Message.showError(e.getMessage());
        }
    }
    private void initializePanel(){
        verticalBox.getChildren().add(book);
    }

    public static void openBorrowReturnWindow(Container container, User user, BorrowedBookDisplay book, Stage parentStage) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Library App - Return Appointment");
        FXMLLoader loader=new FXMLLoader(RegularController.class.getResource("/org/example/libraryjavafx/regular-window/return-book-window.fxml"));
        AnchorPane myPane = (AnchorPane) loader.load();

        ReturnBookController controller = loader.getController();
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

    public void handleSendReturn(ActionEvent actionEvent) {
        String description = textAreaDescription.getText();
        Book book = JsonUtilsConverter.borrowBookDisplayToBook(this.book);
        if(terminalReturn == null){
            Message.showError("no terminal selected");
            return;
        }
        try{
            regularService.sendReturnAppointment(terminalReturn,book,description,user);
            Message.showMessage("Success","return appointment made");
            stageSelf.close();
        } catch (ServiceException e) {
            Message.showError(e.getMessage());
            stageSelf.close();
        }
    }
}
