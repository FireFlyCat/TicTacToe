package com.mkoryak.tictactoe.services;

import com.mkoryak.tictactoe.configs.BattleValidationException;
import com.mkoryak.tictactoe.dto.Battle;
import com.mkoryak.tictactoe.dto.BattleDto;
import com.mkoryak.tictactoe.mapper.BattleMapper;
import com.mkoryak.tictactoe.repositories.BattleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mkoryak.tictactoe.services.TicTacToeUtils.generateEmptyFields;

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
        battle = battleRepository.save(battle);
        return battleMapper.toBattleDto(battle);
    }

    public BattleDto joinBattle(Integer id) {
        Optional<Battle> battleOpt = battleRepository.findById(id);
        if (!battleOpt.isPresent()) {
            throw new BattleValidationException("No battlefield found");
        }
        Battle battle = battleOpt.get();
        if (battle.getUser2Id() != null) {
            throw new BattleValidationException("The battlefield is full");
        }
        battle.setUser2Id("user2");
        battle = battleRepository.save(battle);
        return battleMapper.toBattleDto(battle);
    }

}
