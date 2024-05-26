package org.example.libraryjavafx.service;

import com.google.gson.*;
import org.example.libraryjavafx.exception.ServiceException;
import org.example.libraryjavafx.exception.ValidationException;
import org.example.libraryjavafx.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
    private static final String AUTHENTICATION_URL = "http://localhost:8080/auth";
    private static final String CONTENT_TYPE = "application/json";

    private final SignUpReqValidator signUpReqValidator;
    private final SignInReqValidator signInReqValidator;

    private final Gson gsonFormatter;
    public AuthenticationService(Gson gson) {
        signUpReqValidator = new SignUpReqValidator();
        signInReqValidator = new SignInReqValidator();
        this.gsonFormatter = gson;
    }

    /**
     * send request to the server in order to sign in a user
     * @throws ServiceException if an error while sending the req occurred
     * @throws ValidationException if credentials provided are not valid
     */
    public User signInUser(SignInRequest signInRequest) throws ServiceException, ValidationException {
        signInReqValidator.validate(signInRequest);
        HttpURLConnection connection = null;
        try{
            URL url = new URL(AUTHENTICATION_URL + "/signIn");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setDoOutput(true); // Enable writing to output stream

            // Convert JSON data to bytes
            byte[] jsonDataBytes = gsonFormatter.toJson(signInRequest).getBytes("UTF-8");

            // Write JSON data to connection output stream
            connection.getOutputStream().write(jsonDataBytes);
            int statusCode = connection.getResponseCode();

            if(statusCode == 200){
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine = in.readLine();
                return gsonFormatter.fromJson(inputLine,User.class);
            } else {
                InputStream errorStream = connection.getErrorStream();
                if (errorStream != null) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(errorStream))) {
                        StringBuilder response = new StringBuilder();
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        throw new ServiceException(gsonFormatter.fromJson(response.toString(), ErrorObject.class).getMessage());
                    }
                } else {
                    throw new ServiceException("An unknown error occurred with status code: " + statusCode);
                }
            }
        }catch (IOException e) {
            throw new ServiceException("Error sending the request");
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * send request to the server in order to sign up a user
     * @throws ServiceException if an error while sending the req occurred
     * @throws ValidationException if credentials provided are not valid
     */
    public User signUpUser(SignUpRequest signUpRequest) throws ServiceException, ValidationException {
        signUpReqValidator.validate(signUpRequest);
        HttpURLConnection connection = null;
        try{
            URL url = new URL(AUTHENTICATION_URL + "/signUp");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setDoOutput(true); // Enable writing to output stream

            // Convert JSON data to bytes
            byte[] jsonDataBytes = gsonFormatter.toJson(signUpRequest).getBytes("UTF-8");

            // Write JSON data to connection output stream
            connection.getOutputStream().write(jsonDataBytes);
            int statusCode = connection.getResponseCode();

            if(statusCode == 200){
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine = in.readLine();
                return gsonFormatter.fromJson(inputLine,User.class);
            } else{
                InputStream errorStream = connection.getErrorStream();
                if (errorStream != null) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(errorStream))) {
                        StringBuilder response = new StringBuilder();
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        throw new ServiceException(gsonFormatter.fromJson(response.toString(), ErrorObject.class).getMessage());
                    }
                } else {
                    throw new ServiceException("An unknown error occurred with status code: " + statusCode);
                }
            }
        }catch (IOException e) {
            throw new ServiceException("Error sending the request");
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
