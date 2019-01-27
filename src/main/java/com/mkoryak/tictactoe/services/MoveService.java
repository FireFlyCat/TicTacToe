package com.mkoryak.tictactoe.services;

import com.mkoryak.tictactoe.configs.BattleValidationException;
import com.mkoryak.tictactoe.configs.WinException;
import com.mkoryak.tictactoe.dto.Battle;
import com.mkoryak.tictactoe.dto.BattleDto;
import com.mkoryak.tictactoe.dto.Field;
import com.mkoryak.tictactoe.dto.MoveDto;
import com.mkoryak.tictactoe.mapper.BattleMapper;
import com.mkoryak.tictactoe.repositories.BattleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoveService {

    private final BattleRepository repository;

    private final BattleMapper battleMapper;

    public BattleDto makeMove(MoveDto move) {
        Battle battle = repository.findById(move.getBattleId()).orElse(null);
        validateMove(move, battle);

        battle = populateChangedField(move, battle);

        checkPlayersWin(battle.getFields());

        return battleMapper.toBattleDto(battle);
    }

    private void validateMove(MoveDto move, Battle battle){
        if(battle == null) {
            throw new BattleValidationException("No battlefield found");
        }
        String message = null;
        if(move.getUserId() == null ||
                (!move.getUserId().equals(battle.getUser1Id()) && !move.getUserId().equals(battle.getUser2Id()))){
            message = "Incorrect user";
        }
        if(move.getX() > 2 || move.getX() < 0 || move.getY() > 2 || move.getY() < 0){
            message = "Missed the field";
        }
        boolean isFirstPlayerMove = isFirstPlayerMove(battle);
        if(isFirstPlayerMove && move.getUserId().equals(battle.getUser2Id())){
            message = "Incorrect move: now first players turn";
        }
        if(!isFirstPlayerMove && move.getUserId().equals(battle.getUser1Id())){
            message = "Incorrect move: now second players turn";
        }

        if(message != null){
            throw new BattleValidationException(message);
        }
    }

    private boolean isFirstPlayerMove(Battle battle) {
        //count how many populated fields we have
        long count = battle.getFields().stream().filter(field -> field.getValue() != null).count();
        //first player move if number is even
        return count % 2 == 0;
    }

    private Battle populateChangedField(MoveDto move, Battle battle) {
        Field fieldToPopulate = battle.getFields().stream().filter(field -> field.getX().equals(move.getX()) &&
                field.getX().equals(move.getY())).findFirst().orElseThrow(() -> new RuntimeException("We shouldn't get here"));

        fieldToPopulate.setX(move.getX());
        fieldToPopulate.setY(move.getY());
        boolean isFirstPlayerMove = isFirstPlayerMove(battle);
        fieldToPopulate.setValue(isFirstPlayerMove ? "X" : "O");
        battle = repository.save(battle);
        return battle;
    }


    private void checkPlayersWin(List<Field> fields) {
        List<Field> fieldX = fields.stream().filter(field -> "X".equals(field.getValue())).collect(Collectors.toList());
        List<Field> fieldO = fields.stream().filter(field -> "O".equals(field.getValue())).collect(Collectors.toList());

        if((fieldX.size() + fieldO. size()) == 9) {
            //check the draw
            throw new WinException("All fields populated! It's a Draw!");
        }

        boolean isPlayer1Win = checkRows(fieldX.stream().map(Field::getX).collect(Collectors.toList())) |
                checkRows(fieldX.stream().map(Field::getY).collect(Collectors.toList())) | checkDiagonals(fieldX);
        if(isPlayer1Win){
            throw new WinException("First \"X\" Player won! Congratulations!");
        }

        boolean isPlayer2Win = checkRows(fieldO.stream().map(Field::getX).collect(Collectors.toList())) |
                checkRows(fieldO.stream().map(Field::getY).collect(Collectors.toList())) | checkDiagonals(fieldO);
        if(isPlayer2Win){
            throw new WinException("Second \"O\" Player won! Congratulations!");
        }
    }

    private boolean checkRows(List<Integer> rows) {
        //count all fields in row using group by. then return maximum number
        Long maxNumberInColumns = rows.stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getValue)
                .orElse(0L);
        return maxNumberInColumns == 3;
    }

    private boolean checkDiagonals(List<Field> field) {
        //we take a map of x as key and y as value

        //TODO fix it
        Map<Integer, Integer> keyValue = field.stream().collect(Collectors.toMap(Field::getX, Field::getY));
        boolean isDiagonal1Valid = true;
        boolean isDiagonal2Valid = true;
        for(int i = 0; i < 3; i++){
            isDiagonal1Valid &= keyValue.get(i) == i;
            isDiagonal2Valid &= keyValue.get(i) == (2 - i);
        }
        return isDiagonal1Valid | isDiagonal2Valid;
    }

}
