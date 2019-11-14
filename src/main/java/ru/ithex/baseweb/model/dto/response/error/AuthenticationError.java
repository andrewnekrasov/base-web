package ru.ithex.baseweb.model.dto.response.error;

import java.util.Date;

public class AuthenticationError extends BaseError{
    public AuthenticationError(String description) {
        super(ErrorType.AUTHENTICATION_ERROR, description, new Date());
    }
}
