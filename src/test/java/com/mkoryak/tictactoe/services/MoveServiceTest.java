package com.mkoryak.tictactoe.services;

import com.mkoryak.tictactoe.dto.Battle;
import com.mkoryak.tictactoe.dto.BattleDto;
import com.mkoryak.tictactoe.dto.FieldDto;
import com.mkoryak.tictactoe.dto.MoveDto;
import com.mkoryak.tictactoe.mapper.BattleMapper;
import com.mkoryak.tictactoe.repositories.BattleRepository;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

import static com.mkoryak.tictactoe.services.TicTacToeUtils.generateEmptyFields;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MoveServiceTest {

    private  BattleRepository repository;

    private  BattleMapper battleMapper;

    private  ValidationService validationService;

    private MoveService subject;

    @Before
    public void setUp(){
        repository = mock(BattleRepository.class);
        battleMapper = Mappers.getMapper(BattleMapper.class);
        validationService = mock(ValidationService.class);
        subject = new MoveService(repository, battleMapper, validationService);
    }

    @Test
    public void makeMove(){
        Battle battle = new Battle();
        battle.setBattleId(1);
        battle.setFields(generateEmptyFields());
        when(repository.findById(1)).thenReturn(Optional.of(battle));
        //return parameter
        when(repository.save(any())).then(invocation -> invocation.getArguments()[0]);
        when(validationService.isFirstPlayerMove(any())).thenReturn(true);

        MoveDto move = new MoveDto();
        move.setBattleId(battle.getBattleId());
        move.setUserId(battle.getUser1Id());
        move.setX(1);
        move.setY(1);

        BattleDto battleDto =  subject.makeMove(move);
        List<FieldDto> fields = battleDto.getFields();
        assertThat(fields.size(), equalTo(9));

        FieldDto result = fields.stream()
                .filter(fieldDto -> (fieldDto.getX().equals(1) && fieldDto.getY().equals(1)))
                .findFirst().get();
        assertThat(result.getValue(),equalTo('X'));
    }

}