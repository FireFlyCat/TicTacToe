package com.mkoryak.tictactoe.dto;

import lombok.Data;

@Data
public class MoveDto {
    private Integer battleId;
    private String userId;
    private Integer x;
    private Integer y;
}
