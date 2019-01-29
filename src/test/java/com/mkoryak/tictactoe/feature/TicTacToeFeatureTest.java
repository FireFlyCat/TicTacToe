package com.mkoryak.tictactoe.feature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkoryak.tictactoe.dto.BattleDto;
import com.mkoryak.tictactoe.dto.MoveDto;
import com.mkoryak.tictactoe.integrations.CommonIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class TicTacToeFeatureTest extends CommonIntegrationTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void game_twoPlayersStart_firstPlayerMakeALine_firstPlayerWin() throws Exception {
        //first user creates a battle
        BattleDto battle = createBattleField();

        //second user joins a battle
        battle = joinBattleField(battle);

        //place X on center
        battle = makeMove(battle.getUser1Id(), 1, 1, battle.getBattleId());

        //place O on left top corner
        battle = makeMove(battle.getUser2Id(), 0, 0, battle.getBattleId());

        //place X on right top corner
        battle = makeMove(battle.getUser1Id(), 0, 2, battle.getBattleId());

        //place O on top
        battle = makeMove(battle.getUser2Id(), 0, 1, battle.getBattleId());

        //place X on left bottom corner
        String result = makeMoveAndWin(battle.getUser1Id(), 2, 0, battle.getBattleId());

        assertThat(result, containsString("First 'X' Player won! Congratulations!"));
    }

    @Test
    public void game_twoPlayersStart_allFieldsFilled_draw() throws Exception {
        //first user creates a battle
        BattleDto battle = createBattleField();

        //second user joins a battle
        battle = joinBattleField(battle);


        //place X on center
        battle = makeMove(battle.getUser1Id(), 1, 1, battle.getBattleId());

        //place O on left top corner
        battle = makeMove(battle.getUser2Id(), 0, 0, battle.getBattleId());

        //place X on top
        battle = makeMove(battle.getUser1Id(), 0, 1, battle.getBattleId());

        //place O on right top corner
        battle = makeMove(battle.getUser2Id(), 0, 2, battle.getBattleId());

        //place X on left
        battle = makeMove(battle.getUser1Id(), 1, 0, battle.getBattleId());

        //place O on right
        battle = makeMove(battle.getUser2Id(), 1, 2, battle.getBattleId());

        //place X on left bottom corner
        battle = makeMove(battle.getUser1Id(), 2, 0, battle.getBattleId());

        //place O on bottom
        battle = makeMove(battle.getUser2Id(), 2, 1, battle.getBattleId());

        //place X on right bottom corner
        String result = makeMoveAndWin(battle.getUser1Id(), 2, 2, battle.getBattleId());

        // result
        // O | X | O
        // X | X | O
        // X | O | X

        assertThat(result, containsString("All fields populated! It's a Draw!"));
    }

    private BattleDto createBattleField() throws Exception {
        String firstResult = this.mockMvc.perform(post("/api/battle")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return mapper.readValue(firstResult, BattleDto.class);
    }

    private BattleDto joinBattleField(BattleDto battle) throws Exception {
        String secondResult = this.mockMvc.perform(get("/api/battle/{id}", battle.getBattleId().toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //just to have last version of battle
        battle = mapper.readValue(secondResult, BattleDto.class);
        return battle;
    }


    private BattleDto makeMove(String userId, int x, int y, int battleId) throws Exception {
        MoveDto move = new MoveDto();
        move.setBattleId(battleId);
        move.setUserId(userId);
        move.setX(x);
        move.setY(y);
        String battle = this.mockMvc.perform(put("/api/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(move)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return mapper.readValue(battle, BattleDto.class);
    }

    private String makeMoveAndWin(String userId, int x, int y, int battleId) throws Exception {
        MoveDto move = new MoveDto();
        move.setBattleId(battleId);
        move.setUserId(userId);
        move.setX(x);
        move.setY(y);
        String result = this.mockMvc.perform(put("/api/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(move)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return result;
    }

}
