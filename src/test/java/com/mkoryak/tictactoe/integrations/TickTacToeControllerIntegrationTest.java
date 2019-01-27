package com.mkoryak.tictactoe.integrations;

import com.mkoryak.tictactoe.services.BattleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class TickTacToeControllerIntegrationTest {

    @Autowired
    private BattleService battleService;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void createBattle_hp() throws Exception{
        this.mockMvc.perform(post("/api/battle")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.battleId", equalTo(1)))
                .andExpect(jsonPath("$.user1Id", equalTo("user1")))
                .andExpect(jsonPath("$.user2Id", nullValue()));
    }

    @Test
    public void joinBattle_hp() throws Exception{
        battleService.createBattle();

        this.mockMvc.perform(get("/api/battle/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.battleId", equalTo(1)))
                .andExpect(jsonPath("$.user1Id", equalTo("user1")))
                .andExpect(jsonPath("$.user2Id", equalTo("user2")));
    }

    @Test
    public void joinBattle_noBattleFieldFound() throws Exception{
        this.mockMvc.perform(get("/api/battle/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("No battlefield found")));
    }

    @Test
    public void joinBattle_battelFieldFull() throws Exception{
        battleService.createBattle();

        //first join - OK
        this.mockMvc.perform(get("/api/battle/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user2Id", equalTo("user2")));
        //second join - exception
        this.mockMvc.perform(get("/api/battle/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("The battlefield is full")));
    }
}
