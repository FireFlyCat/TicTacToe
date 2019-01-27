package com.mkoryak.tictactoe.services;

import com.mkoryak.tictactoe.configs.BattleValidationException;
import com.mkoryak.tictactoe.dto.*;
import com.mkoryak.tictactoe.mapper.BattleMapper;
import com.mkoryak.tictactoe.repositories.BattleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleRepository battleRepository;

    private final BattleMapper battleMapper;

    public BattleDto createBattle() {
        Battle battle = new Battle();
        battle.setUser1Id("user1");
        battle = battleRepository.save(battle);
        battle.setFields(generateEmptyFields(battle.getBattleId()));
        return battleMapper.toBattleDto(battle);
    }

    public BattleDto joinBattle(Integer id) {
        Optional<Battle> battleOpt = battleRepository.findById(id);
        if(!battleOpt.isPresent()) {
            throw new BattleValidationException("No battlefield found");
        }
        Battle battle = battleOpt.get();
        if(battle.getUser2Id() != null){
            throw new BattleValidationException("The battlefield is full");
        }
        battle.setUser2Id("user2");
        battle = battleRepository.save(battle);
        return battleMapper.toBattleDto(battle);
    }

    private List<Field> generateEmptyFields(Integer battleId) {
        List<Field> result = new ArrayList<>();
        for(int x = 0; x < 3; x ++){
            for(int y = 0; y < 3; y++){
                result.add(Field.builder().battleId(battleId).x(x).y(y).build());
            }
        }
        return result;
    }
}
