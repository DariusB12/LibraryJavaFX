module org.example.libraryjavafx {
    requires javafx.fxml;
    requires javafx.graphics;
    requires static lombok;
    requires com.google.gson;
    requires javafx.controls;

    exports org.example.libraryjavafx;
    exports org.example.libraryjavafx.controller;
    opens org.example.libraryjavafx.service.dto.authenticationDTO;
    opens org.example.libraryjavafx.model;
    opens org.example.libraryjavafx.service;
    opens org.example.libraryjavafx.service.dto;
    exports org.example.libraryjavafx.service.dto.regularDTO;
    opens org.example.libraryjavafx.service.dto.regularDTO;
}