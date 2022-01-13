package com.app.model;

import lombok.AllArgsConstructor;

public class AuthenticationFailedException extends Exception {
    public AuthenticationFailedException() {
        super("AuthenticationFailedException");
    }
}
