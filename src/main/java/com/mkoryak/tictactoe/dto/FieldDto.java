package com.mkoryak.tictactoe.dto;

import lombok.Data;

@Data
public class FieldDto {
    private Integer x;
    private Integer y;
    private Character value;
}
