package org.example.libraryjavafx.service.dto;

import org.example.libraryjavafx.controller.dto.BookDisplay;
import org.example.libraryjavafx.controller.dto.BorrowedBookDisplay;
import org.example.libraryjavafx.model.Book;

public class JsonUtilsConverter {
    public static BookDisplay bookDTOtoBookDisplay(Book book){
        return new BookDisplay(
                book.getId(),
                book.getImagePath(),
                book.getTitle(),
                book.getAuthor(),
                book.getDescription(),
                book.getPublisher(),
                book.getPublishDate(),
                book.getPages(),
                book.getBooksAvailable()
        );
    }

    public static Book bookDisplayToBook(BookDisplay bookDisplay){
        return Book.builder()
                .id(bookDisplay.getIdBook())
                .title(bookDisplay.getTitle())
                .author(bookDisplay.getAuthor())
                .publisher(bookDisplay.getPublisher())
                .publishDate(bookDisplay.getPublishDate())
                .description(bookDisplay.getDescription())
                .pages(bookDisplay.getPages())
                .booksAvailable(bookDisplay.getBooksAvailable())
                .imagePath(bookDisplay.getImagePath())
                .build();
    }
    public static Book borrowBookDisplayToBook(BorrowedBookDisplay borrowedBookDisplay){
        return Book.builder()
                .id(borrowedBookDisplay.getIdBook())
                .title(borrowedBookDisplay.getTitle())
                .author(borrowedBookDisplay.getAuthor())
                .publisher(borrowedBookDisplay.getPublisher())
                .publishDate(borrowedBookDisplay.getPublishDate())
                .description(borrowedBookDisplay.getDescription())
                .pages(borrowedBookDisplay.getPages())
                .booksAvailable(borrowedBookDisplay.getBooksAvailable())
                .imagePath(borrowedBookDisplay.getImagePath())
                .build();
    }
}
