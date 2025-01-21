package com.interface21.core.servlet;

public class UncheckedServletException extends RuntimeException {

    public UncheckedServletException(Exception e) {
        super(e);
    }
}
