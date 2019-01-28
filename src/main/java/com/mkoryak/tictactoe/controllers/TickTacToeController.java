package com.mkoryak.tictactoe.controllers;

import com.mkoryak.tictactoe.dto.BattleDto;
import com.mkoryak.tictactoe.dto.MoveDto;
import com.mkoryak.tictactoe.services.BattleService;
import com.mkoryak.tictactoe.services.MoveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TickTacToeController {

    private final BattleService battleService;

    private final MoveService moveService;

    @PostMapping("/battle")
    public BattleDto createBattle() {
        return battleService.createBattle();
    }

    @GetMapping("/battle/{id}")
    public BattleDto joinBattle(@PathVariable("id") Integer id){
        return battleService.joinBattle(id);
    }

    @PutMapping(value = "/move")
    public BattleDto placeMarker(@RequestBody MoveDto move){
        return moveService.makeMove(move);
    }
}
