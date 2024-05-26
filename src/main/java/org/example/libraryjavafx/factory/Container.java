package org.example.libraryjavafx.factory;


import org.example.libraryjavafx.service.AuthenticationService;
import org.example.libraryjavafx.service.EmployeeService;
import org.example.libraryjavafx.service.RegularService;

public class Container {
    private final AuthenticationService authenticationService;
    private final RegularService regularService;
    private final EmployeeService employeeService;

    public Container(AuthenticationService authenticationService, RegularService regularService, EmployeeService employeeService) {
        this.authenticationService = authenticationService;
        this.regularService = regularService;
        this.employeeService = employeeService;

    }

    public RegularService getRegularService() {
        return regularService;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }
}
