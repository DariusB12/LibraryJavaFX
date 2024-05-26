package org.example.libraryjavafx.controller.regular;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.example.libraryjavafx.controller.dto.AppointmentDisplay;
import org.example.libraryjavafx.controller.dto.BookDisplay;
import org.example.libraryjavafx.controller.dto.BorrowedBookDisplay;
import org.example.libraryjavafx.controller.dto.NotifyType;
import org.example.libraryjavafx.exception.Message;
import org.example.libraryjavafx.exception.ServiceException;
import org.example.libraryjavafx.factory.Container;
import org.example.libraryjavafx.model.*;
import org.example.libraryjavafx.service.RegularService;
import org.example.libraryjavafx.service.dto.JsonUtilsConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class RegularController implements Initializable {
    public ListView listViewUnconfirmedAppointments;
    public ListView listViewConfirmedAppointments;
    public ListView listViewBorrowedBooks;
    public ListView listViewBooks;
    private Container container;
    private RegularService service;
    private Stage stageSelf;
    private User user;
    private List<BookDisplay> allBooks;
    public static void openMainWindowRegular(User user,Stage stageSource,Container containerSource) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Library App - " + user.getUsername() + " - REGULAR");
        FXMLLoader loader=new FXMLLoader(RegularController.class.getResource("/org/example/libraryjavafx/regular-window/main-window-regular.fxml"));
        AnchorPane myPane = (AnchorPane) loader.load();

        RegularController controller = loader.getController();
        controller.setResources(containerSource,stage,user);

        Scene myScene = new Scene(myPane);
        stage.setScene(myScene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controller.signOut(user);
                System.exit(0);
            }
        });

        stageSource.close();
        stage.show();
    }

    /**
     * sign out or show error message
     */
    private void signOut(User user) {
        try {
            service.signOut(user);
        } catch (ServiceException e) {
            Message.showError(e.getMessage());
        }
    }

    public void setResources(Container container,Stage stageSelf,User user){
        this.container = container;
        this.stageSelf = stageSelf;
        this.user = user;
        this.service = container.getRegularService();
        loadBooks();
        loadAppointments();
        loadBorrowedBooks();

        //start the thread to read updates form the server
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    /**
     * Loads both the unconfirmed and confirmed listViews with the current user's appointments
     */
    private void loadAppointments(){
        ObservableList<AppointmentDisplay> appointmentDisplaysObsListUnconfirmed = FXCollections.observableArrayList();
        ObservableList<AppointmentDisplay> appointmentDisplaysObsListConfirmed =FXCollections.observableArrayList();
        List<Appointment> allUserAppointments = null;
        try {
            allUserAppointments = service.getUserAppointments(user);
        } catch (ServiceException e) {
            Message.showError("error fetching the appointments");
            return;
        }
        for(Appointment appointment:allUserAppointments){
            if(appointment.getConfirmed() == false){
                appointmentDisplaysObsListUnconfirmed.add(new AppointmentDisplay(appointment));
            }
            else{
                appointmentDisplaysObsListConfirmed.add(new AppointmentDisplay(appointment));
            }
        }
        listViewUnconfirmedAppointments.setItems(appointmentDisplaysObsListUnconfirmed);
        listViewConfirmedAppointments.setItems(appointmentDisplaysObsListConfirmed);
    }

    /**
     * Load the verticalBox with all the books
     */
    private void loadBooks(){
        ObservableList<BookDisplay> bookDisplayObservableList = FXCollections.observableArrayList();
        try{
            allBooks = service.getAllBooks(this.user);
        } catch (ServiceException e) {
            Message.showError(e.getMessage());
            return;
        }
        for(BookDisplay book : allBooks){
            book.setOnMouseClicked(event -> {
//                System.out.println("Clicked on book details: " + getClass().getSimpleName());
                try {
                    onclickeventBorrow(book);
                } catch (IOException e) {
                    Message.showError("error trying to open the borrow book window");
                }
            });
            bookDisplayObservableList.add(book);
        }
        listViewBooks.setItems(bookDisplayObservableList);
    }

    /**
     * reloads all the borrowed books of the current user
     */
    private void loadBorrowedBooks(){
        ObservableList<BorrowedBookDisplay> borrowedBookDisplayObservableList = FXCollections.observableArrayList();
        List<Book> allBooksBorrowedByUser = null;
        try {
            allBooksBorrowedByUser = service.getAllBooksBorrowedByUser(user);
        } catch (ServiceException e) {
            Message.showError("error fetching the borrowed books");
            return;
        }
        for(Book book : allBooksBorrowedByUser){
            BorrowedBookDisplay borrowedBookDisplay =new BorrowedBookDisplay(book);
            borrowedBookDisplay.setOnMouseClicked(event -> {
                try {
                    onclickeventReturn(borrowedBookDisplay);
                } catch (IOException e) {
                    Message.showError("error trying to open the return book window");
                }
            });
            borrowedBookDisplayObservableList.add(borrowedBookDisplay);
        }
        listViewBorrowedBooks.setItems(borrowedBookDisplayObservableList);
    }

    /**
     * handle the event when the user is clicking a book in the verticalBox
     * @param book the book to load the borrowBookWindow
     * @throws IOException if the borrowBook window cannot be opened
     */
    private void onclickeventBorrow(BookDisplay book) throws IOException {
        BorrowBookController.openBorrowBookWindow(container,user,book,stageSelf);
    }
    /**
     * handle the event when the user is clicking a book in the verticalBox (the one for borrowed books)
     * @param book the book to load the returnBookWindow
     * @throws IOException if the returnBook window cannot be opened
     */
    private void onclickeventReturn(BorrowedBookDisplay book) throws IOException {
        ReturnBookController.openBorrowReturnWindow(container,user,book,stageSelf);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Opens the window for deleting account
     * @throws IOException is the deleteAccountWindow cannot be opened
     */
    public void handleDeleteAccount(ActionEvent actionEvent) throws IOException {
        DeleteAccountController.openDeleteAccountWindow(this.user,this.stageSelf,this.container);
    }

    /**
     * cancels the appointment selected
     */
    public void handleCancelAppointment(ActionEvent actionEvent) {
        AppointmentDisplay appointmentDisplay = (AppointmentDisplay) listViewUnconfirmedAppointments.getSelectionModel().getSelectedItem();
        if(appointmentDisplay == null){
            Message.showError("no appointment selected\nNote: you can cancel only 'not confirmed' appointments");
            return;
        }
        Appointment appointment = appointmentDisplay.getAppointment();
        try {
            service.cancelAppointment(user,appointment);
            Message.showMessage("Success","appointment canceled with success");
        } catch (ServiceException e) {
            Message.showError(e.getMessage());
        }
    }

    /**
     * class used in order to register the client as an observable, so it gets updates from the server
     */
    private class ReaderThread implements Runnable {
        public void run() {
            int tries = 0;
            while (true) {
                try {
                    URL url = new URL("http://localhost:8080/notifications/subscribe");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Accept", "text/event-stream");
                    connection.setRequestProperty("Authorization", "Bearer " + user.getToken());

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        tries = 0;
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("data:")) {
                                String message = line.substring(5).trim();
                                Platform.runLater(()->handleUpdate(NotifyType.valueOf(message)));
                                System.out.println("Received message: " + message);
                            }
                        }
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                    System.out.println("trying to connect again: " + e.getMessage());
                    tries++;
                    if(tries >=3){
                        break;
                    }
                }
                try {
                    Thread.sleep(2000); // Wait 2 seconds before trying to reconnect
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * call various methods depending on the type of the update
     */
    private void handleUpdate(NotifyType type) {
        switch (type) {
            case BOOK_BORROWED:
                Platform.runLater(this::loadBooks);
                System.out.println("books updates");
                break;
            case APPOINTMENT_CREATED, APPOINTMENT_CONFIRMED:
                Platform.runLater(this::loadAppointments);
                Platform.runLater(this::loadBorrowedBooks);
                Platform.runLater(this::loadBooks);
                System.out.println("appointments updated");
                break;
            case APPOINTMENT_CANCELED:
                Platform.runLater(this::loadAppointments);
                Platform.runLater(this::loadBooks);
                System.out.println("appointments updated");
                break;
        }
    }

}
