package com.github.bercik.cli;

public class BadCliArgumentsException extends Exception {
    BadCliArgumentsException(String message) {
        super(message);
    }
}
