package com.siemens.internal.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class TempoTmsException {

    private static final long serialVersionUID = 1L;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}
