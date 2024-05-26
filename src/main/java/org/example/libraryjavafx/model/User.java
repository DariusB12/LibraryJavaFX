package org.example.libraryjavafx.model;


import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User{
    @SerializedName("token")
    private String token;
    @SerializedName("username")
    private String username;
    @SerializedName("user_role")
    private UserRole userRole;
}
