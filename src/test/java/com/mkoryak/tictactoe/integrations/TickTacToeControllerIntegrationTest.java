package com.mkoryak.tictactoe.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkoryak.tictactoe.dto.Battle;
import com.mkoryak.tictactoe.dto.MoveDto;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class TickTacToeControllerIntegrationTest extends CommonIntegrationTest {

    @Test
    public void createBattle_hp() throws Exception {
        this.mockMvc.perform(post("/api/battle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.battleId", equalTo(1)))
                .andExpect(jsonPath("$.user1Id", equalTo("user1")))
                .andExpect(jsonPath("$.user2Id", nullValue()));
    }

    @Test
    public void joinBattle_hp() throws Exception {
        createBattle();

        this.mockMvc.perform(get("/api/battle/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.battleId", equalTo(1)))
                .andExpect(jsonPath("$.user1Id", equalTo("user1")))
                .andExpect(jsonPath("$.user2Id", equalTo("user2")));
    }

    @Test
    public void joinBattle_noBattleFieldFound() throws Exception {
        this.mockMvc.perform(get("/api/battle/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("No battlefield found")));
    }

    @Test
    public void joinBattle_battelFieldFull() throws Exception {
        createBattle();

        //first join - OK
        this.mockMvc.perform(get("/api/battle/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user2Id", equalTo("user2")));
        //second join - exception
        this.mockMvc.perform(get("/api/battle/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("The battlefield is full")));
    }

    @Test
    public void placeMarker_correctMove() throws Exception {
        Battle battle = createBattle("user2");
        MoveDto move = new MoveDto();
        move.setBattleId(battle.getBattleId());
        move.setUserId(battle.getUser1Id());
        move.setX(1);
        move.setY(1);

        Map<String, Object> expected = new HashMap<>();
        expected.put("x", 1);
        expected.put("y", 1);
        expected.put("value", "X");

        this.mockMvc.perform(put("/api/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(move)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fields", Matchers.hasItem(expected)));
    }


    @Test
    public void placeMarker_incorrectMove_secondPlayerMove() throws Exception {
        Battle battle = createBattle("user2");
        MoveDto move = new MoveDto();
        move.setBattleId(battle.getBattleId());
        move.setUserId(battle.getUser2Id());
        move.setX(1);
        move.setY(1);

        this.mockMvc.perform(put("/api/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(move)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Incorrect move: now first players turn")));
    }

}
