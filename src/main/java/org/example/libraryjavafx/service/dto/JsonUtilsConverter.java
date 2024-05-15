package org.example.libraryjavafx.service.dto;

import org.example.libraryjavafx.controller.dto.BookDisplay;
import org.example.libraryjavafx.service.dto.regularDTO.BookDTO;

import java.time.LocalDate;

public class JsonUtilsConverter {
    public static BookDisplay bookDTOtoBookDisplay(BookDTO book){
        return new BookDisplay(
                book.getImagePath(),
                book.getTitle(),
                book.getAuthor(),
                book.getDescription(),
                book.getPublisher(),
                book.getPublishDate().toString(),
                book.getPages()
        );
    }
}
