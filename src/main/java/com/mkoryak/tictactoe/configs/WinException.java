package com.mkoryak.tictactoe.configs;

public class WinException extends RuntimeException {
    public WinException(String message) {
        super(message);
    }
}
