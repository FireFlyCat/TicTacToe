package com.mkoryak.tictactoe.services;

import com.mkoryak.tictactoe.configs.BattleValidationException;
import com.mkoryak.tictactoe.configs.WinException;
import com.mkoryak.tictactoe.dto.Battle;
import com.mkoryak.tictactoe.dto.Field;
import com.mkoryak.tictactoe.dto.MoveDto;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ValidationService {

    public void validateMove(MoveDto move, Battle battle) {
        if (battle == null) {
            throw new BattleValidationException("No battlefield found");
        }
        if (battle.getUser2Id() == null) {
            throw new BattleValidationException("Second player hasn't joined yet");
        }
        if (move.getUserId() == null ||
                (!move.getUserId().equals(battle.getUser1Id()) && !move.getUserId().equals(battle.getUser2Id()))) {
            throw new BattleValidationException("Incorrect user");
        }
        String message = null;
        if (move.getX() > 2 || move.getX() < 0 || move.getY() > 2 || move.getY() < 0) {
            message = "Missed the field";
        }
        boolean isFirstPlayerMove = isFirstPlayerMove(battle);
        if (isFirstPlayerMove && move.getUserId().equals(battle.getUser2Id())) {
            message = "Incorrect move: now first players turn";
        }
        if (!isFirstPlayerMove && move.getUserId().equals(battle.getUser1Id())) {
            message = "Incorrect move: now second players turn";
        }
        // search for value of current field
        String currentFieldValue = battle.getFields().stream()
                .filter(field -> field.getX().equals(move.getX()) && field.getY().equals(move.getY()))
                .findFirst().orElse(new Field()).getValue();
        if (currentFieldValue != null) {
            message = "Incorrect move: the field has been placed";
        }

        if (message != null) {
            throw new BattleValidationException(message);
        }
    }

    public boolean isFirstPlayerMove(Battle battle) {
        //count how many populated fields we have
        long count = battle.getFields().stream().filter(field -> field.getValue() != null).count();
        //first player move if number is even
        return count % 2 == 0;
    }

    public void checkPlayersWin(List<Field> fields) {
        List<Field> fieldX = fields.stream().filter(field -> "X".equals(field.getValue())).collect(Collectors.toList());
        List<Field> fieldO = fields.stream().filter(field -> "O".equals(field.getValue())).collect(Collectors.toList());

        if ((fieldX.size() + fieldO.size()) == 9) {
            //check the draw - all fields filled
            throw new WinException("All fields populated! It's a Draw!");
        }

        boolean isPlayer1Win = checkLines(fieldX.stream().map(Field::getX).collect(Collectors.toList())) |
                checkLines(fieldX.stream().map(Field::getY).collect(Collectors.toList())) | checkDiagonals(fieldX);
        if (isPlayer1Win) {
            throw new WinException("First 'X' Player won! Congratulations!");
        }

        boolean isPlayer2Win = checkLines(fieldO.stream().map(Field::getX).collect(Collectors.toList())) |
                checkLines(fieldO.stream().map(Field::getY).collect(Collectors.toList())) | checkDiagonals(fieldO);
        if (isPlayer2Win) {
            throw new WinException("Second 'O' Player won! Congratulations!");
        }
    }

    private boolean checkLines(List<Integer> rows) {
        //count all fields in row using group by. then return maximum number
        Long maxNumberInColumns = rows.stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getValue)
                .orElse(0L);
        return maxNumberInColumns == 3;
    }

    private boolean checkDiagonals(List<Field> fields) {
        //I can't come up with anything better than this.
        // Otherwise convert list to array of arrays and check more traditionally
        boolean isDiagonal1Valid = true;
        boolean isDiagonal2Valid = true;
        for (int i = 0; i < 3; i++) {
            final int ii = i;
            isDiagonal1Valid &= fields.stream().anyMatch(field -> field.getX().equals(ii) && field.getY().equals(ii));
            isDiagonal2Valid &= fields.stream().anyMatch(field -> field.getX().equals(ii) && field.getY().equals(2 - ii));
        }
        return isDiagonal1Valid | isDiagonal2Valid;
    }

}
