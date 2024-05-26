package org.example.libraryjavafx.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    private Integer id;
    private LocalDateTime issued;
    private String username;
    private AppointmentType type;
    private Terminal terminal;
    private String description;
    private Boolean confirmed;
    private Book book;
}
