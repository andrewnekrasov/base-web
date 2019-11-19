package ru.ithex.baseweb.model.dto.response.error;

import java.util.Date;

public class ValidationError extends BaseError{
    public ValidationError(String description) {
        super(ErrorType.VALIDATION_ERROR, description, new Date());
    }
}
