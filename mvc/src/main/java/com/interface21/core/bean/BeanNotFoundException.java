package com.interface21.core.bean;

public class BeanNotFoundException extends IllegalArgumentException {

    public BeanNotFoundException() {
        super("Bean not found");
    }
}
