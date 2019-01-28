package com.mkoryak.tictactoe.dto;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Battle {
    @Id
    @GeneratedValue
    private Integer battleId;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "battleId")
    private List<Field> fields;
    private String user1Id;
    private String user2Id;
}
