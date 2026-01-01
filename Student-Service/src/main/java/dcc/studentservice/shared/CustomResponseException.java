package com.example.Mylab.shared;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomResponseException  extends RuntimeException{

    private int code;
    private String message;

    public static CustomResponseException ResourceNotFound(String message) {
        return new CustomResponseException(404, message);
    }

    public static CustomResponseException BadRequest(String message) {
        return new CustomResponseException(400, message);
    }

    public static CustomResponseException BadCredentials() {
        return new CustomResponseException(401, "Bad Credentials");
    }

    public static CustomResponseException Conflict(String message) {
        return new CustomResponseException(409, message);
    }
    public static CustomResponseException Forbidden(String message) {
        return new CustomResponseException(403, message);
    }

    public static CustomResponseException InternalError(String message) {
        return new CustomResponseException(500, message);
    }

    public static CustomResponseException UnauthorizedToken(String message) {
        return new CustomResponseException(401, message);
    }
    public static CustomResponseException Created(String message) {
        return new CustomResponseException(201, message);
    }

}
