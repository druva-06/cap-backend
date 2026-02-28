package com.meritcap.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class AlreadyExistException extends RuntimeException {
    private final List<String> errors;
    public AlreadyExistException(List<String> errors) {
        super("Already exists");
        this.errors = errors;
    }
}
