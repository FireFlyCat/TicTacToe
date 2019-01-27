package com.mkoryak.tictactoe.mapper;

import com.mkoryak.tictactoe.dto.Battle;
import com.mkoryak.tictactoe.dto.BattleDto;
import com.mkoryak.tictactoe.dto.Field;
import com.mkoryak.tictactoe.dto.FieldDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface BattleMapper {
    BattleDto toBattleDto(Battle battle);

    FieldDto toFieldDto(Field field);
}
