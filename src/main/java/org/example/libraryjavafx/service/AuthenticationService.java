package org.example.libraryjavafx.service;

import com.google.gson.*;
import org.example.libraryjavafx.exception.ServiceException;
import org.example.libraryjavafx.exception.ValidationException;
import org.example.libraryjavafx.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.example.libraryjavafx.service.dto.ErrorObject;
import org.example.libraryjavafx.service.dto.authenticationDTO.SignInRequest;
import org.example.libraryjavafx.service.dto.authenticationDTO.SignUpRequest;
import org.example.libraryjavafx.validator.SignInReqValidator;
import org.example.libraryjavafx.validator.SignUpReqValidator;


public class AuthenticationService {
    private final SignUpReqValidator signUpReqValidator;
    private final SignInReqValidator signInReqValidator;
    private final Gson gsonFormatter;
    public AuthenticationService(Gson gson) {
        signUpReqValidator = new SignUpReqValidator();
        signInReqValidator = new SignInReqValidator();
        this.gsonFormatter = gson;
    }

    public User signInUser(SignInRequest signInRequest) throws ServiceException, ValidationException {
        signInReqValidator.validate(signInRequest);
        try{
            URL url = new URL("http://localhost:8080/auth/signIn");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true); // Enable writing to output stream

            // Convert JSON data to bytes
            byte[] jsonDataBytes = gsonFormatter.toJson(signInRequest).getBytes("UTF-8");

            // Write JSON data to connection output stream
            connection.getOutputStream().write(jsonDataBytes);
            int statusCode = connection.getResponseCode();

            if(statusCode == 200){
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine = in.readLine();
                connection.disconnect();
                return gsonFormatter.fromJson(inputLine,User.class);
            } else if (statusCode == 401) {
                connection.disconnect();
                throw new ServiceException("Username or password incorrect");
            }else{
                connection.disconnect();
                throw new ServiceException("Server error, please try again later");
            }
        }catch (IOException e) {
            throw new ServiceException("Error sending the request");
        }
    }

    public User signUpUser(SignUpRequest signUpRequest) throws ServiceException, ValidationException, IOException {
        signUpReqValidator.validate(signUpRequest);
        try{
            URL url = new URL("http://localhost:8080/auth/signUp");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true); // Enable writing to output stream

            // Convert JSON data to bytes
            byte[] jsonDataBytes = gsonFormatter.toJson(signUpRequest).getBytes("UTF-8");

            // Write JSON data to connection output stream
            connection.getOutputStream().write(jsonDataBytes);
            int statusCode = connection.getResponseCode();

            if(statusCode == 200){
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine = in.readLine();
                connection.disconnect();
                return gsonFormatter.fromJson(inputLine,User.class);
            } else if (statusCode >= 400) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String inputLine = in.readLine();
                throw new ServiceException(gsonFormatter.fromJson(inputLine, ErrorObject.class).getMessage());
            }else{
                connection.disconnect();
                throw new ServiceException("Server error, please try again later");
            }
        }catch (IOException e) {
            throw new ServiceException("Error sending the request");
        }
    }
}
