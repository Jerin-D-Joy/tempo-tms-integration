package com.siemens.internal.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {


    @ExceptionHandler(PartialDataSaveException.class)
    public final ResponseEntity<ExceptionResponse> handlePartialDataSaveException(final PartialDataSaveException ex) throws Exception {
        log.info("TMS #PartialDataSaveException occured : ", ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setHttpStatus(HttpStatus.PARTIAL_CONTENT);
        exceptionResponse.setDetails("Data partially saved into the TMS database");
        exceptionResponse.setMessage("Please add these data manually into TMS");
        exceptionResponse.setErrorList(ex.getErrorList());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.PARTIAL_CONTENT);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(final Exception ex) throws Exception {
        log.info("TMS #Exception occured : ", ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionResponse.setDetails(ex.getMessage());
        exceptionResponse.setMessage("Exception Occured!!!");
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
