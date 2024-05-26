package org.example.libraryjavafx.controller.dto;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.libraryjavafx.model.Book;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowedBookDisplay extends HBox {
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

    public BorrowedBookDisplay(Book book) {
        this.idBook = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.publishDate = book.getPublishDate();
        this.pages = book.getPages();
        this.description = book.getDescription();
        this.imagePath = book.getImagePath();

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
        numPagesLabel = new Label("Pages: " + pages);

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
        VBox thirdCompartment = new VBox(publisherLabel, publishDateLabel, numPagesLabel);
        thirdCompartment.setAlignment(Pos.CENTER);
        thirdCompartment.setSpacing(5);

        // Create horizontal box for the entire book display
        this.getChildren().addAll(firstCompartment, descriptionTextArea, thirdCompartment);
        setSpacing(10); // Add spacing between compartments
    }

    public BorrowedBookDisplay deepCopy() {
        BorrowedBookDisplay copy = new BorrowedBookDisplay();
        copy.idBook = this.idBook;
        copy.title = this.title;
        copy.author = this.author;
        copy.publisher = this.publisher;
        copy.publishDate = this.publishDate;
        copy.pages = this.pages;
        copy.description = this.description;
        copy.imagePath = this.imagePath;
        copy.booksAvailable = this.booksAvailable;

        // Create new instances for mutable objects
        copy.image = new ImageView(new Image(this.imagePath, 100, 100, true, false));
        copy.titleLabel = new Label(this.title);
        copy.authorLabel = new Label(this.author);
        copy.publisherLabel = new Label(this.publisher);
        copy.publishDateLabel = new Label(this.publishDate.toString());
        copy.numPagesLabel = new Label("Pages: " + this.pages);
        copy.descriptionTextArea = new TextArea(this.description);
        copy.descriptionTextArea.setPrefHeight(100);
        copy.descriptionTextArea.setPrefWidth(400);
        copy.descriptionTextArea.setEditable(false);
        copy.descriptionTextArea.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

        // Create new VBox instances
        VBox firstCompartment = new VBox(copy.image, copy.titleLabel, copy.authorLabel);
        firstCompartment.setSpacing(5);

        VBox thirdCompartment = new VBox(copy.publisherLabel, copy.publishDateLabel, copy.numPagesLabel);
        thirdCompartment.setSpacing(5);

        copy.getChildren().addAll(firstCompartment, copy.descriptionTextArea, thirdCompartment);
        copy.setSpacing(10);

        return copy;
    }
}
