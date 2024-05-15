package org.example.libraryjavafx.factory;



import com.google.gson.*;
import org.example.libraryjavafx.service.AuthenticationService;
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
                    // Use DateTimeFormatter to parse the string (adjust format as needed)
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                    return LocalDateTime.parse(dateTimeString, formatter);
                }

            })
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    String dateTimeString = json.getAsString();
                    // Use DateTimeFormatter to parse the string (adjust format as needed)
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    return LocalDate.parse(dateTimeString, formatter);
                }

            })
            .create();

    public static Container getContainer(){
        if(container == null){
            AuthenticationService authenticationService = new AuthenticationService(gsonFormatter);
            RegularService regularService = new RegularService(gsonFormatter);

            container = new Container(authenticationService,regularService);
            return container;
        }
        return container;
    }
}
