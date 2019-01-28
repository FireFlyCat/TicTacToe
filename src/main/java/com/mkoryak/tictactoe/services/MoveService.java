package com.mkoryak.tictactoe.services;

import com.mkoryak.tictactoe.dto.Battle;
import com.mkoryak.tictactoe.dto.BattleDto;
import com.mkoryak.tictactoe.dto.Field;
import com.mkoryak.tictactoe.dto.MoveDto;
import com.mkoryak.tictactoe.mapper.BattleMapper;
import com.mkoryak.tictactoe.repositories.BattleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoveService {

    private final BattleRepository repository;

    private final BattleMapper battleMapper;

    private final ValidationService validationService;

    public BattleDto makeMove(MoveDto move) {
        Battle battle = repository.findById(move.getBattleId()).orElse(null);

        validationService.validateMove(move, battle);

        battle = populateChangedField(move, battle);

        validationService.checkPlayersWin(battle.getFields());

        return battleMapper.toBattleDto(battle);
    }

    private Battle populateChangedField(MoveDto move, Battle battle) {
        Field fieldToPopulate = battle.getFields().stream().filter(field -> (field.getX().equals(move.getX()) &&
                field.getY().equals(move.getY()))).findFirst().orElseThrow(() -> new RuntimeException("We shouldn't get here"));

        boolean isFirstPlayerMove = validationService.isFirstPlayerMove(battle);
        fieldToPopulate.setValue(isFirstPlayerMove ? "X" : "O");
        battle = repository.save(battle);
        return battle;
    }
}
