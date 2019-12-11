package com.siemens.internal.exceptions;

import com.siemens.internal.models.TmsInput;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
@Getter
@Setter
public class PartialDataSaveException extends RuntimeException {

    List<TmsInput> errorList;

    public PartialDataSaveException() {
        super();
    }

    public PartialDataSaveException(String s) {
        super(s);
    }

    public PartialDataSaveException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PartialDataSaveException(Throwable throwable) {
        super(throwable);
    }

    public PartialDataSaveException(List<TmsInput> errorList) {
        this.errorList = errorList;
    }

}
