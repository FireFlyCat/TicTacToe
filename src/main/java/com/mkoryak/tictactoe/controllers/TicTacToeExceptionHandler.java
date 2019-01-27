package com.mkoryak.tictactoe.controllers;

import com.mkoryak.tictactoe.configs.BattleValidationException;
import com.mkoryak.tictactoe.configs.WinException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class TicTacToeExceptionHandler {

    @ExceptionHandler(BattleValidationException.class)
    public ResponseEntity handleException(BattleValidationException e){
        return ResponseEntity.badRequest().body(wrapMessage(e.getMessage()));
    }

    public ResponseEntity handleException(WinException e){
        return ResponseEntity.ok().body(wrapMessage(e.getMessage()));
    }
    private Map<String, String> wrapMessage(String message){
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }

}
