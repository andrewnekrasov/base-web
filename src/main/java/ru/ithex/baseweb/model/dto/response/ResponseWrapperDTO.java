package ru.ithex.baseweb.model.dto.response;

import ru.ithex.baseweb.model.dto.response.error.BaseError;

public class ResponseWrapperDTO<T> {
    private final T data;
    private final BaseError error;

    private ResponseWrapperDTO(T data, BaseError error) {
        this.data = data;
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public BaseError getError() {
        return error;
    }

    public static <T>ResponseWrapperDTO ok(T data){
        return new ResponseWrapperDTO(data, null);
    }

    public static ResponseWrapperDTO ok(BaseError error){
        return new ResponseWrapperDTO(null, error);
    }
}
