package org.example.libraryjavafx.controller.dto;

import com.google.gson.annotations.SerializedName;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import lombok.*;
import org.example.libraryjavafx.model.Book;

import java.time.LocalDate;
import java.util.Objects;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDisplay extends HBox {
    private Integer idBook;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publishDate;
    private Integer pages;
    private String description;
    private String imagePath;
    private Integer booksAvailable;

    private ImageView image;
    private Label titleLabel;
    private Label authorLabel;
    private TextArea descriptionTextArea;
    private Label publisherLabel;
    private Label publishDateLabel;
    private Label numPagesLabel;
    private Label numBooksAvailable;

    public BookDisplay(Integer id, String imagePath, String title, String author, String description,
                       String publisher, LocalDate publishDate, int numPages, int booksAvailable) {
        this.idBook = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.pages = numPages;
        this.description = description;
        this.imagePath = imagePath;
        this.booksAvailable = booksAvailable;


        // Create image view
        try {
            image = new ImageView(new Image(imagePath, 100, 100, true, false));
        } catch (Exception e) {
            image = new ImageView(new Image(BookDisplay.class.getClassLoader().getResourceAsStream("NotAvailableImage.png"), 100, 100, true, false));
        }

        // Create labels for title, author, publisher, publish date, and number of pages
        titleLabel = new Label(title);
        authorLabel = new Label(author);
        publisherLabel = new Label(publisher);
        publishDateLabel = new Label(publishDate.toString());
        numPagesLabel = new Label("Pages: " + numPages);
        numBooksAvailable = new Label("Available: " + booksAvailable);

        // Create text area for description
        descriptionTextArea = new TextArea(description);
        descriptionTextArea.setPrefHeight(100);
        descriptionTextArea.setPrefWidth(400);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

        // Create vertical box for first compartment (image, title, author)
        VBox firstCompartment = new VBox(image, titleLabel, authorLabel);
        firstCompartment.setAlignment(Pos.CENTER);
        firstCompartment.setSpacing(5); // Add spacing between elements

        // Create vertical box for third compartment (publisher, publish date, number of pages)
        VBox thirdCompartment = new VBox(publisherLabel, publishDateLabel, numPagesLabel, numBooksAvailable);
        thirdCompartment.setAlignment(Pos.CENTER);
        thirdCompartment.setSpacing(5);

        // Create horizontal box for the entire book display
        this.getChildren().addAll(firstCompartment, descriptionTextArea, thirdCompartment);
        setSpacing(10); // Add spacing between compartments
    }

    // Copy constructor for deep copying
    public BookDisplay(BookDisplay original) {
        this.idBook = original.getIdBook();
        this.title = original.getTitle();
        this.author = original.getAuthor();
        this.publisher = original.getPublisher();
        this.publishDate = original.getPublishDate();
        this.pages = original.getPages();
        this.description = original.getDescription();
        this.imagePath = original.getImagePath();
        this.booksAvailable = original.getBooksAvailable();
        // Create new instances of the image
        this.image = new ImageView(original.image.getImage());

        // Create new instances of the labels and copy their texts
        this.titleLabel = new Label(original.titleLabel.getText());
        this.authorLabel = new Label(original.authorLabel.getText());
        this.publisherLabel = new Label(original.publisherLabel.getText());
        this.publishDateLabel = new Label(original.publishDateLabel.getText());
        this.numPagesLabel = new Label(original.numPagesLabel.getText());
        this.numBooksAvailable = new Label(original.numBooksAvailable.getText());

        // Create new instance of the text area and copy its text
        this.descriptionTextArea = new TextArea(original.descriptionTextArea.getText());
        this.descriptionTextArea.setPrefHeight(100);
        this.descriptionTextArea.setPrefWidth(400);
        this.descriptionTextArea.setEditable(false);
        this.descriptionTextArea.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

        // Create vertical box for first compartment (image, title, author)
        VBox firstCompartment = new VBox(image, titleLabel, authorLabel);
        firstCompartment.setSpacing(5); // Add spacing between elements

        // Create vertical box for third compartment (publisher, publish date, number of pages)
        VBox thirdCompartment = new VBox(publisherLabel, publishDateLabel, numPagesLabel, numBooksAvailable);
        thirdCompartment.setSpacing(5);

        // Create horizontal box for the entire book display
        this.getChildren().addAll(firstCompartment, descriptionTextArea, thirdCompartment);
        setSpacing(10); // Add spacing between compartments
    }

    // Method to create a deep copy
    public BookDisplay deepCopy() {
        return new BookDisplay(this);
    }
}
