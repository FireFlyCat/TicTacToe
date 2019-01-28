package com.mkoryak.tictactoe.services;

import com.mkoryak.tictactoe.dto.Field;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeUtils {

    public static List<Field> generateEmptyFields() {
        return generateEmptyFields(null);
    }

    public static List<Field> generateEmptyFields(Integer battleId) {
        List<Field> result = new ArrayList<>();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                result.add(Field.builder().battleId(battleId).x(x).y(y).build());
            }
        }
        return result;
    }
}
