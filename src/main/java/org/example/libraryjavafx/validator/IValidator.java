package org.example.libraryjavafx.validator;


import org.example.libraryjavafx.exception.ValidationException;

public interface IValidator<T> {
    public void validate(T entity) throws ValidationException;
}
