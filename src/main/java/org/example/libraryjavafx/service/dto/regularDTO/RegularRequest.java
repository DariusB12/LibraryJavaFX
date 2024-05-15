package org.example.libraryjavafx.service.dto.regularDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class RegularRequest {
    private String password;
}
