package org.example.libraryjavafx.validator;

import org.example.libraryjavafx.exception.ValidationException;
import org.example.libraryjavafx.service.dto.authenticationDTO.SignUpRequest;

import java.util.Objects;
import java.util.regex.Pattern;

public class SignUpReqValidator implements IValidator<SignUpRequest> {
    @Override
    public void validate(SignUpRequest entity) throws ValidationException {
        String errors = "";
        if(entity.getUsername() == "" ||entity.getUsername() == null ){
            errors += "invalid username\n";
        }
        if(entity.getPassword()=="" || entity.getPassword()==null ){
            errors += "invalid password\n";
        }
        if(entity.getFirstName()=="" || entity.getFirstName()==null){
            errors += "invalid firstName\n";
        }
        if(entity.getLastName()==""|| entity.getLastName()== null){
            errors += "invalid lastName\n";
        }
        if(entity.getAddress()=="" || entity.getAddress() == null){
            errors += "invalid address\n";
        }

        Pattern pattern = Pattern.compile("[a-zA-Z]");
        if(pattern.matcher(entity.getPhone()).find()){
            errors+="invalid phone\n";
        }
        if(pattern.matcher(entity.getCnp()).find()){
            errors+="invalid cnp\n";
        }
        if(!entity.getEmail().contains("@")){
            errors += "invalid email\n";
        }
        if(!errors.isEmpty()){
            throw new ValidationException(errors);
        }
    }
}
