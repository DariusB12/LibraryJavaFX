package org.example.libraryjavafx.controller.dto;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.libraryjavafx.model.Appointment;

import java.time.format.DateTimeFormatter;

@Data
@Builder
public class AppointmentDisplay extends HBox {

    private final Appointment appointment;

    public AppointmentDisplay(Appointment appointment) {
        this.appointment = appointment;
        initView();
    }

    private void initView() {
        setSpacing(10);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(5));

        // First item: Vertical box with book image and title
        VBox bookBox = new VBox();
        bookBox.setAlignment(Pos.CENTER);
        ImageView bookImage = new ImageView(new Image(appointment.getBook().getImagePath()));
        bookImage.setFitWidth(90); // Adjust image width as needed
        bookImage.setFitHeight(110);
        bookImage.setPreserveRatio(true);
        Label bookTitle = new Label(appointment.getBook().getTitle());
        bookBox.getChildren().addAll(bookImage, bookTitle);

        // Second item: Vertical box with issued, appointment type, and terminal name
        VBox infoBox = new VBox();
        infoBox.setMaxWidth(150);
        Label issuedLabel = new Label("Issued: " + appointment.getIssued().format(DateTimeFormatter.ISO_DATE));
        Label typeLabel = new Label("Type: " + appointment.getType());
        Label terminalNameLabel = new Label("Terminal: " + appointment.getTerminal().getName());
        Label terminalLocationLabel = new Label("Location: " + appointment.getTerminal().getLocation());
        Label descriptionLabel = new Label("Description:\n" + appointment.getDescription());
        Label username = new Label("username:" + appointment.getUsername());
        infoBox.getChildren().addAll(issuedLabel, typeLabel, terminalNameLabel,terminalLocationLabel,descriptionLabel,username);
        //ensure that the texts fits in the VBox
        issuedLabel.setWrapText(true);
        typeLabel.setWrapText(true);
        terminalNameLabel.setWrapText(true);
        terminalLocationLabel.setWrapText(true);
        descriptionLabel.setWrapText(true);

        // Third item: Confirmation
        Label confirmationLabel = new Label(appointment.getConfirmed() ? "Confirmed" : "Not\nConfirmed");

        // Add all items to the horizontal box
        getChildren().addAll(bookBox, infoBox, confirmationLabel);
    }
}