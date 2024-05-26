package org.example.libraryjavafx.service.dto.emplloyeeDTO;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.example.libraryjavafx.model.Appointment;

@Data
@Builder
public class EmployeeRequest {
    @SerializedName("appointment")
    private Appointment appointment;
}
