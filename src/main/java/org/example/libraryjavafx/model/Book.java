package org.example.libraryjavafx.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Integer id;
    private String title;
    private String author;
    private String publisher;
    @SerializedName("publish_date")
    private LocalDate publishDate;
    private Integer pages;
    private String description;
    @SerializedName("image_path")
    private String imagePath;
    @SerializedName("books_available")
    private Integer booksAvailable;
}
