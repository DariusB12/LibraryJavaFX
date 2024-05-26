package org.example.libraryjavafx.controller.employee;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.example.libraryjavafx.controller.dto.AppointmentDisplay;
import org.example.libraryjavafx.controller.dto.BookDisplay;
import org.example.libraryjavafx.controller.dto.NotifyType;
import org.example.libraryjavafx.controller.regular.RegularController;
import org.example.libraryjavafx.exception.Message;
import org.example.libraryjavafx.exception.ServiceException;
import org.example.libraryjavafx.factory.Container;
import org.example.libraryjavafx.model.Appointment;
import org.example.libraryjavafx.model.User;
import org.example.libraryjavafx.service.EmployeeService;
import org.example.libraryjavafx.service.RegularService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {
    public ListView listViewNotConfirmedAppointments;
    public ListView listViewConfirmedAppointments;
    private Container container;
    private EmployeeService service;
    private User user;
    private Stage stageSelf;
    private List<BookDisplay> allBooks = new ArrayList<>();

    public static void openMainWindowEmployee(User user, Stage stageSource, Container containerSource) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Library App - " + user.getUsername() + " - EMPLOYEE");
        FXMLLoader loader=new FXMLLoader(RegularController.class.getResource("/org/example/libraryjavafx/employee-window/main-window-employee.fxml"));
        AnchorPane myPane = (AnchorPane) loader.load();

        EmployeeController controller = loader.getController();
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
        this.service = container.getEmployeeService();
        loadAppointments();

        //start the thread to read updates form the server
        Thread tw = new Thread(new EmployeeController.ReaderThread());
        tw.start();
    }

    /**
     * reload the appointments lists, both not confirmed and confirmed lists
     */
    private void loadAppointments(){
        ObservableList<AppointmentDisplay> appointmentDisplaysObsListNotConfirmed = FXCollections.observableArrayList();
        ObservableList<AppointmentDisplay> appointmentDisplaysObsListConfirmed =FXCollections.observableArrayList();
        List<Appointment> allAppointments = null;
        try {
            allAppointments = service.getAllAppointments(user);
        } catch (ServiceException e) {
            Message.showError(e.getMessage());
            return;
        }

        for(Appointment appointment:allAppointments){
            if(appointment.getConfirmed() == false){
                appointmentDisplaysObsListNotConfirmed.add(new AppointmentDisplay(appointment));
            }
            else{
                appointmentDisplaysObsListConfirmed.add(new AppointmentDisplay(appointment));
            }
        }
        listViewNotConfirmedAppointments.setItems(appointmentDisplaysObsListNotConfirmed);
        listViewConfirmedAppointments.setItems(appointmentDisplaysObsListConfirmed);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * confirms the appointment if selected or shows error message otherwise
     */
    public void handleConfirm(ActionEvent actionEvent) {
        AppointmentDisplay appointmentDisplay = (AppointmentDisplay) listViewNotConfirmedAppointments.getSelectionModel().getSelectedItem();
        if(appointmentDisplay == null) {
            Message.showError("no appointment selected");
            return;
        }
        Appointment appointment = appointmentDisplay.getAppointment();
        try {
            service.confirmAppointment(user,appointment);
            Message.showMessage("Success","Appointment confirmed with success");
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
                    if(tries >=10){
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
            case APPOINTMENT_CREATED,APPOINTMENT_CONFIRMED,APPOINTMENT_CANCELED,USER_DELETED:
                Platform.runLater(this::loadAppointments);
                System.out.println("appointments updated");
                break;
        }
    }
}
