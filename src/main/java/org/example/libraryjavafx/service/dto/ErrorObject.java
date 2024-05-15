package org.example.libraryjavafx.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorObject {
    public Integer statusCode;
    public String message;
    public LocalDateTime dateTime;
}
