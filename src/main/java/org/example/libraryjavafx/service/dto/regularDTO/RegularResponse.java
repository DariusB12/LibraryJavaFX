package org.example.libraryjavafx.service.dto.regularDTO;


import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.libraryjavafx.model.Appointment;
import org.example.libraryjavafx.model.Book;
import org.example.libraryjavafx.model.Terminal;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegularResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("books")
    private List<Book> books;
    @SerializedName("terminals")
    private List<Terminal> terminals;
    @SerializedName("appointments")
    private List<Appointment> appointments;
}
