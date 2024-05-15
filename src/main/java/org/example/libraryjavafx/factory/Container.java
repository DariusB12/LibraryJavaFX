package org.example.libraryjavafx.factory;


import org.example.libraryjavafx.service.AuthenticationService;
import org.example.libraryjavafx.service.RegularService;

public class Container {
    private final AuthenticationService authenticationService;
    private final RegularService regularService;

    public Container(AuthenticationService authenticationService, RegularService regularService) {
        this.authenticationService = authenticationService;
        this.regularService = regularService;

    }

    public RegularService getRegularService() {
        return regularService;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }
}
