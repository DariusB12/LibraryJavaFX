package org.example.libraryjavafx.validator;

import org.example.libraryjavafx.exception.Message;
import org.example.libraryjavafx.exception.ValidationException;
import org.example.libraryjavafx.service.dto.authenticationDTO.SignInRequest;

public class SignInReqValidator implements IValidator<SignInRequest> {
    @Override
    public void validate(SignInRequest entity) throws ValidationException {
        String err = "";
        if(entity.getUsername() == null || entity.getUsername().isEmpty()){
            err+="Invalid username";
        }
        if(entity.getPassword() == null || entity.getPassword().isEmpty()){
            err +="Invalid password\n";
        }

        if(!err.isEmpty()){
            throw new ValidationException(err);
        }
    }
}
