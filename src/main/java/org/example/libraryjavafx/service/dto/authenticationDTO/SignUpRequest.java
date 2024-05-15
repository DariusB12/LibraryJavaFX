package org.example.libraryjavafx.service.dto.authenticationDTO;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.libraryjavafx.model.UserRole;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String username;
    private String password;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String address;
    private String phone;
    private String cnp;
    private String email;
    private UserRole role;
    @SerializedName("role password")
    private String rolePassword;
}
