package com.mkoryak.tictactoe.integrations;

import com.mkoryak.tictactoe.dto.Battle;
import com.mkoryak.tictactoe.repositories.BattleRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.mkoryak.tictactoe.services.TicTacToeUtils.generateEmptyFields;

public class CommonIntegrationTest {

    @Autowired
    protected BattleRepository battleRepository;

    @Autowired
    protected WebApplicationContext context;
    protected MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    protected Battle createBattle() {
        return createBattle(null);
    }

    protected Battle createBattle(String secondUser) {
        Battle battle = new Battle();
        battle.setUser1Id("user1");
        battle.setUser2Id(secondUser);
        battle = battleRepository.save(battle);
        battle.setFields(generateEmptyFields(battle.getBattleId()));
        battle = battleRepository.save(battle);
        return battle;
    }
}
