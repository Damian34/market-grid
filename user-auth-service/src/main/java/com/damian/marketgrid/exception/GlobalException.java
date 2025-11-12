package com.damian.marketgrid.exception;

import org.springframework.http.HttpStatus;

public abstract class GlobalException extends RuntimeException {

    public abstract HttpStatus getCode();
}
