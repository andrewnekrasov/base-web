package ru.ithex.baseweb.model.dto.response.error;

import java.util.Date;

public class InternalServerError extends BaseError{

    public InternalServerError(String description) {
        super("INTERNAL_SERVER_ERROR", description, new Date());
    }
}
