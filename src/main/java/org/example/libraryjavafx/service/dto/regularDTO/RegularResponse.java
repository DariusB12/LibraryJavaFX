package org.example.libraryjavafx.service.dto.regularDTO;


import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegularResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("books")
    private List<BookDTO> books;
}
