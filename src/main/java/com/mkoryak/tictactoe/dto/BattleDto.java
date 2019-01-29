package com.mkoryak.tictactoe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BattleDto {
    private Integer battleId;
    private List<FieldDto> fields;
    private String user1Id;
    private String user2Id;
}
