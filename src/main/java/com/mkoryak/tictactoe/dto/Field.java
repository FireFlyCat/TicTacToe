package com.mkoryak.tictactoe.dto;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
public class Field {
    @Id
    @GeneratedValue
    private Integer fieldId;
    private Integer battleId;
    private Integer x;
    private Integer y;
    private String value;
}
