package org.example.libraryjavafx.service;

import com.google.gson.Gson;
import org.example.libraryjavafx.exception.ServiceException;
import org.example.libraryjavafx.model.Appointment;
import org.example.libraryjavafx.model.AppointmentType;
import org.example.libraryjavafx.model.User;
import org.example.libraryjavafx.service.dto.ErrorObject;
import org.example.libraryjavafx.service.dto.emplloyeeDTO.EmployeeRequest;
import org.example.libraryjavafx.service.dto.regularDTO.RegularRequest;
import org.example.libraryjavafx.service.dto.regularDTO.RegularResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class EmployeeService extends RegularService {
    private static final String APPOINTMENTS_URL = "http://localhost:8080/appointments";
    private static final String CONTENT_TYPE = "application/json";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private final Gson gsonFormatter;

    public EmployeeService(Gson gson) {
        super(gson);
        this.gsonFormatter = gson;
    }

    /**
     * requests the server to get a list with all the appointments (confirmed/not confirmed)
     * @throws ServiceException if the request fails
     */
    public List<Appointment> getAllAppointments(User user) throws ServiceException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(APPOINTMENTS_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setRequestProperty(AUTHORIZATION, BEARER + user.getToken());

            int statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    return gsonFormatter.fromJson(response.toString(), RegularResponse.class).getAppointments();
                }
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
     * sends a request to server in order to confirm the given appointment (depending on the type of the appointment)
     * @throws ServiceException if an error encounters while sending the request
     */
    public void confirmAppointment(User user, Appointment appointment) throws ServiceException {
        String endpoint = appointment.getType() == AppointmentType.BORROW ? "/borrow" : "/return";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(APPOINTMENTS_URL + endpoint);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setRequestProperty(AUTHORIZATION, BEARER + user.getToken());
            connection.setDoOutput(true); // Enable writing to output stream

            EmployeeRequest employeeRequest = EmployeeRequest.builder()
                    .appointment(appointment)
                    .build();

            // Convert JSON data to bytes and write to output stream
            byte[] jsonDataBytes = gsonFormatter.toJson(employeeRequest).getBytes("UTF-8");
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
}
