package ru.ithex.baseweb.model.dto.response.error;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public abstract class BaseError {
    private final ErrorType type;
    private final String description;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    private final Date timestamp;

    public BaseError(ErrorType type, String description, Date timestamp) {
        this.type = type;
        this.description = description;
        this.timestamp = timestamp;
    }

    public ErrorType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
