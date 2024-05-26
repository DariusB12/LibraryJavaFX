package org.example.libraryjavafx.service;

import com.google.gson.Gson;
import javafx.application.Platform;
import org.example.libraryjavafx.controller.dto.BookDisplay;
import org.example.libraryjavafx.controller.dto.NotifyType;
import org.example.libraryjavafx.exception.ServiceException;
import org.example.libraryjavafx.exception.ValidationException;
import org.example.libraryjavafx.model.*;
import org.example.libraryjavafx.service.dto.ErrorObject;
import org.example.libraryjavafx.service.dto.JsonUtilsConverter;
import org.example.libraryjavafx.service.dto.emplloyeeDTO.EmployeeRequest;
import org.example.libraryjavafx.service.dto.regularDTO.RegularRequest;
import org.example.libraryjavafx.service.dto.regularDTO.RegularResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RegularService {
    private static final String APPOINTMENTS_URL = "http://localhost:8080/appointments";
    private static final String BOOKS_URL = "http://localhost:8080/books";
    private static final String REGULAR_URL = "http://localhost:8080/regular";
    private static final String TERMINALS_URL = "http://localhost:8080/terminals";
    private static final String NOTIFICATIONS_URL = "http://localhost:8080/notifications";

    private static final String CONTENT_TYPE = "application/json";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private final Gson gsonFormatter;

    public RegularService(Gson gson) {
        this.gsonFormatter = gson;
    }

    /**
     * sends a request to get all the books with their no of pieces
     * @param user User
     * @return all the books from DB
     * @throws ServiceException if the statusCode differs from 200
     */
    public List<BookDisplay> getAllBooks(User user) throws ServiceException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BOOKS_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setRequestProperty(AUTHORIZATION, BEARER + user.getToken());

            int statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine = in.readLine();

                List<BookDisplay> allBooks = new ArrayList<>();
                RegularResponse regularResponse = gsonFormatter.fromJson(inputLine, RegularResponse.class);
                //extract the BookDisplay objects from request
                for (Book book : regularResponse.getBooks()) {
                    BookDisplay bookDisplay = JsonUtilsConverter.bookDTOtoBookDisplay(book);
                    allBooks.add(bookDisplay);
                }
                return allBooks;
            }else if (statusCode == 401) {////401 if jwt expired
                throw new ServiceException("Your session has expired\nplease sign in again");
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
        } catch (IOException e) {
            throw new ServiceException("Error sending the request");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * sends a request to delete the account of the given user
     * @param user User
     * @param password String
     * @throws ServiceException if the request was not send with success
     */
    public void deleteAccount(User user, String password) throws ServiceException {
        RegularRequest request = RegularRequest.builder()
                .password(password)
                .build();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(REGULAR_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setRequestProperty(AUTHORIZATION, BEARER + user.getToken());
            connection.setDoOutput(true); // Enable writing to output stream

            // Convert JSON data to bytes
            byte[] jsonDataBytes = gsonFormatter.toJson(request).getBytes("UTF-8");

            // Write JSON data to connection output stream
            connection.getOutputStream().write(jsonDataBytes);
            int statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                return;
            } else if (statusCode == 401) {
                throw new ServiceException("Your session has expired\nplease sign in again");
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
        } catch (IOException e) {
            throw new ServiceException("Error sending the request");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * sends a request to borrow a book with the book to be borrowed and the appointment
     * also include the sender user's token
     * */
    public void sendBorrowAppointment(Terminal terminal, Book book, String description, User user) throws ServiceException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(APPOINTMENTS_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setRequestProperty(AUTHORIZATION, BEARER + user.getToken());
            connection.setDoOutput(true); // Enable writing to output stream

            RegularRequest request = RegularRequest.builder()
                    .typeAppointment(AppointmentType.BORROW)
                    .terminal(terminal)
                    .book(book)
                    .description(description)
                    .build();

            // Convert JSON data to bytes
            byte[] jsonDataBytes = gsonFormatter.toJson(request).getBytes("UTF-8");

            // Write JSON data to connection output stream
            connection.getOutputStream().write(jsonDataBytes);
            int statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                return;
            } else if (statusCode == 401) {
                throw new ServiceException("Your session has expired\nplease sign in again");
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
        } catch (IOException e) {
            throw new ServiceException("Error sending the request");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * sends a request to get all the terminals
     * @param user User
     * @return all the terminals from DB
     * @throws ServiceException if the statusCode differs from 200
     */
    public List<Terminal> getAllTerminals(User user) throws ServiceException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(TERMINALS_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setRequestProperty(AUTHORIZATION, BEARER + user.getToken());

            int statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine = in.readLine();

                RegularResponse regularResponse = gsonFormatter.fromJson(inputLine, RegularResponse.class);
                return regularResponse.getTerminals();
            } else if (statusCode == 401) {
                throw new ServiceException("Your session has expired\nplease sign in again");
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
        } catch (IOException e) {
            throw new ServiceException("Error sending the request");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * sends request to server to get all the appointments of a specified user
     * @param user User
     * @return all the appointments of the given user
     */
    public List<Appointment> getUserAppointments(User user) throws ServiceException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(APPOINTMENTS_URL + "/" + user.getUsername());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setRequestProperty(AUTHORIZATION, BEARER + user.getToken());

            int statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine = in.readLine();

                RegularResponse regularResponse = gsonFormatter.fromJson(inputLine, RegularResponse.class);
                return regularResponse.getAppointments();
            } else if (statusCode == 401) {
                throw new ServiceException("Your session has expired\nplease sign in again");
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
        } catch (IOException e) {
            throw new ServiceException("Error sending the request");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void cancelAppointment(User user, Appointment appointment) throws ServiceException {
        HttpURLConnection connection = null;
        String endpoint = appointment.getType() == AppointmentType.BORROW ? "/borrow" : "/return";
        try {
            URL url = new URL(APPOINTMENTS_URL + endpoint);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setRequestProperty(AUTHORIZATION, BEARER + user.getToken());
            connection.setDoOutput(true); // Enable writing to output stream

            RegularRequest regularRequest = RegularRequest.builder()
                    .appointment(appointment)
                    .build();

            // Convert JSON data to bytes and write to output stream
            byte[] jsonDataBytes = gsonFormatter.toJson(regularRequest).getBytes("UTF-8");
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonDataBytes);
            }

            int statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                return; // Success
            } else if (statusCode == 401) { // 401 if JWT expired
                throw new ServiceException("Your session has expired\nplease sign in again");
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
        } catch (IOException e) {
            throw new ServiceException("Error sending the request", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * sends a request to the server to get all the book borrowed by the given user
     * @throws ServiceException if the statusCode differs from 200
     */
    public List<Book> getAllBooksBorrowedByUser(User user) throws ServiceException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BOOKS_URL + "/" + user.getUsername());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setRequestProperty(AUTHORIZATION, BEARER + user.getToken());

            int statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine = in.readLine();

                RegularResponse regularResponse = gsonFormatter.fromJson(inputLine, RegularResponse.class);
                //extract the BookDisplay objects from request
                return regularResponse.getBooks();
            } else if (statusCode == 401) {
                throw new ServiceException("Your session has expired\nplease sign in again");
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
        } catch (IOException e) {
            throw new ServiceException("Error sending the request");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * sends a request to the server to create a return appointment for the given user
     */
    public void sendReturnAppointment(Terminal terminalReturn, Book book, String description, User user) throws ServiceException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(APPOINTMENTS_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setRequestProperty(AUTHORIZATION, BEARER + user.getToken());
            connection.setDoOutput(true); // Enable writing to output stream

            RegularRequest request = RegularRequest.builder()
                    .typeAppointment(AppointmentType.RETURN)
                    .terminal(terminalReturn)
                    .book(book)
                    .description(description)
                    .build();

            // Convert JSON data to bytes
            byte[] jsonDataBytes = gsonFormatter.toJson(request).getBytes("UTF-8");

            // Write JSON data to connection output stream
            connection.getOutputStream().write(jsonDataBytes);
            int statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                return;
            }  else if (statusCode == 401) {
                throw new ServiceException("Your session has expired\nplease sign in again");
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
        } catch (IOException e) {
            throw new ServiceException("Error sending the request");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * sends a request to the server to close the connection
     */
    public void signOut(User user) throws ServiceException {
        try {
            URL url = new URL(NOTIFICATIONS_URL + "/unsubscribe");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept", "text/event-stream");
            connection.setRequestProperty(AUTHORIZATION, BEARER + user.getToken());
            int statusCode = connection.getResponseCode();
            if(statusCode !=200){
                connection.disconnect();
                throw new ServiceException("error closing the connection\n status code: " + statusCode);
            }
        } catch (IOException e) {
            throw new ServiceException("error closing the connection:\n " + e.getMessage());
        }
    }
}
