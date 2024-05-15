package org.example.libraryjavafx.service;

import com.google.gson.Gson;
import org.example.libraryjavafx.controller.dto.BookDisplay;
import org.example.libraryjavafx.exception.ServiceException;
import org.example.libraryjavafx.model.User;
import org.example.libraryjavafx.service.dto.JsonUtilsConverter;
import org.example.libraryjavafx.service.dto.regularDTO.BookDTO;
import org.example.libraryjavafx.service.dto.regularDTO.RegularRequest;
import org.example.libraryjavafx.service.dto.regularDTO.RegularResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RegularService {
    private final Gson gsonFormatter;
    public RegularService(Gson gson) {
        this.gsonFormatter = gson;
    }

    public List<BookDisplay> getAllBooks(User user) throws ServiceException {
        try{
            URL url = new URL("http://localhost:8080/regular/getAllBooks");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + user.getToken());

            int statusCode = connection.getResponseCode();

            if(statusCode == 200){
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine = in.readLine();
                connection.disconnect();

                List<BookDisplay> allBooks = new ArrayList<>();
                RegularResponse regularResponse = gsonFormatter.fromJson(inputLine, RegularResponse.class);
                //extract the BookDisplay objects from request
                for(BookDTO bookDTO : regularResponse.getBooks()){
                    BookDisplay bookDisplay = JsonUtilsConverter.bookDTOtoBookDisplay(bookDTO);
                    allBooks.add(bookDisplay);
                }
                return allBooks;
            } else if (statusCode == 400) {//400 bad req
                connection.disconnect();
                throw new ServiceException("Your session is corrupted");
            } else if (statusCode == 401) {////401 jwt expired
                connection.disconnect();
                throw new ServiceException("Your session is expired");
            } else{
                connection.disconnect();
                throw new ServiceException("Server error, please try again later");
            }
        }catch (IOException e) {
            throw new ServiceException("Error sending the request");
        }
    }

    public void deleteAccount(User user,String password) throws ServiceException {
        RegularRequest request = RegularRequest.builder()
                .password(password)
                .build();
        try{
            URL url = new URL("http://localhost:8080/regular/deleteAccount");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + user.getToken());
            connection.setDoOutput(true); // Enable writing to output stream

            // Convert JSON data to bytes
            byte[] jsonDataBytes = gsonFormatter.toJson(request).getBytes("UTF-8");

            // Write JSON data to connection output stream
            connection.getOutputStream().write(jsonDataBytes);
            int statusCode = connection.getResponseCode();

            if(statusCode == 200){
                connection.disconnect();
            } else if (statusCode == 401) {
                connection.disconnect();
                throw new ServiceException("Your session has expired");
            } else if (statusCode == 400) {
                connection.disconnect();
                throw new ServiceException("Wrong password");
            } else{
                connection.disconnect();
                throw new ServiceException("Server error, please try again later");
            }
        }catch (IOException e) {
            throw new ServiceException("Error sending the request");
        }
    }

}
