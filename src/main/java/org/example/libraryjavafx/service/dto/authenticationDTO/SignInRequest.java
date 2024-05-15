package org.example.libraryjavafx.service.dto.authenticationDTO;


import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest{
    private String username;
    private String password;
}
