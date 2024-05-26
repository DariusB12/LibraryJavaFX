package org.example.libraryjavafx.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Terminal {
    private Integer id;
    private String name;
    private String location;
    @SerializedName("terminal_type")
    private TerminalType terminalType;

    @Override
    public String toString() {
        return name + " location: " + location + " " + terminalType;
    }
}
