package org.example.libraryjavafx.factory;



import com.google.gson.*;
import org.example.libraryjavafx.service.AuthenticationService;
import org.example.libraryjavafx.service.EmployeeService;
import org.example.libraryjavafx.service.RegularService;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Factory {
    private static Container container = null;
    private Factory(){}
    private static final Gson gsonFormatter = new GsonBuilder()
            .serializeNulls() // Include null values in JSON output
            .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    String dateTimeString = json.getAsString();
                    // Use DateTimeFormatter to parse the string
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                    if(dateTimeString !=null){
                        return LocalDateTime.parse(dateTimeString, formatter);
                    }
                    else{
                        return null;
                    }
                }

            })
            .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                @Override
                public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                    return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")));
                }
            })
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    String dateTimeString = json.getAsString();
                    // Use DateTimeFormatter to parse the string
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    return LocalDate.parse(dateTimeString, formatter);
                }
            })
            .registerTypeAdapter(LocalDate.class,new JsonSerializer<LocalDate>(){
                @Override
                public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); // "yyyy-mm-dd"
                }
            })

            .create();

    public static Container getContainer(){
        if(container == null){
            AuthenticationService authenticationService = new AuthenticationService(gsonFormatter);
            RegularService regularService = new RegularService(gsonFormatter);
            EmployeeService employeeService = new EmployeeService(gsonFormatter);

            container = new Container(authenticationService,regularService,employeeService);
            return container;
        }
        return container;
    }
}
