package org.example.libraryjavafx.service.dto.regularDTO;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import org.example.libraryjavafx.model.Appointment;
import org.example.libraryjavafx.model.AppointmentType;
import org.example.libraryjavafx.model.Book;
import org.example.libraryjavafx.model.Terminal;

@Data
@Builder
public class RegularRequest {
    @SerializedName("password")
    private String password;
    @SerializedName("type_appointment")
    private AppointmentType typeAppointment;
    @SerializedName("terminal")
    private Terminal terminal;
    @SerializedName("description")
    private String description;
    @SerializedName("book")
    private Book book;
    @SerializedName("appointment")
    private Appointment appointment;
}
