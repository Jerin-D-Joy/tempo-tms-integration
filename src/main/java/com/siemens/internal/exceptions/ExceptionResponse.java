package com.siemens.internal.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ExceptionResponse {

    private HttpStatus httpStatus;
    private String errorStatusCode;
    private Instant timestamp = Instant.now();
    private String message;
    private String details;
    private String type;
    private List errorList;

    public ExceptionResponse(String message, String details) {
        this.message = message;
        this.details = details;
    }

    public ExceptionResponse(String message, String details, String type) {
        this.message = message;
        this.details = details;
        this.type = type;
    }

    public ExceptionResponse(HttpStatus status, String errorStatusCode, String message, String details) {
        super();
        this.httpStatus = status;
        this.errorStatusCode = errorStatusCode;
        this.message = message;
        this.details = details;
    }
}
