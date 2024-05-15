package org.example.libraryjavafx.controller.dto;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;

public class BookDisplay extends HBox {

    // Define image, title, author, description, publisher, publish date, and number of pages
    private ImageView image;
    private Label titleLabel;
    private Label authorLabel;
    private TextArea descriptionTextArea;
    private Label publisherLabel;
    private Label publishDateLabel;
    private Label numPagesLabel;
    public BookDisplay(String imagePath, String title, String author, String description,
                       String publisher, String publishDate, int numPages) {

        // Create image view
        try{
            image = new ImageView(new Image(imagePath, 100, 100, true, false));
        }catch (Exception e){
            image = new ImageView(new Image(BookDisplay.class.getClassLoader().getResourceAsStream("NotAvailableImage.png"), 100, 100, true, false));
        }

        // Create labels for title, author, publisher, publish date, and number of pages
        titleLabel = new Label(title);
        authorLabel = new Label(author);
        publisherLabel = new Label(publisher);
        publishDateLabel = new Label(publishDate);
        numPagesLabel = new Label("Pages: " + numPages);

        // Create text area for description
        descriptionTextArea = new TextArea(description);
        descriptionTextArea.setPrefHeight(100);
        descriptionTextArea.setPrefWidth(400);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

        // Create vertical box for first compartment (image, title, author)
        VBox firstCompartment = new VBox(image, titleLabel, authorLabel);
        firstCompartment.setSpacing(5); // Add spacing between elements

        // Create vertical box for third compartment (publisher, publish date, number of pages)
        VBox thirdCompartment = new VBox(publisherLabel, publishDateLabel, numPagesLabel);
        thirdCompartment.setSpacing(5);

        // Create horizontal box for the entire book display
        this.getChildren().addAll(firstCompartment, descriptionTextArea, thirdCompartment);
        setSpacing(10); // Add spacing between compartments
    }
}