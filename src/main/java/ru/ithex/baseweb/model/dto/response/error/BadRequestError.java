package ru.ithex.baseweb.model.dto.response.error;

import java.util.Date;

public class BadRequestError extends BaseError{

    public BadRequestError(String description) {
        super("BAD_REQUEST_ERROR", description, new Date());
    }
}
