package com.mkoryak.tictactoe.services;

import com.mkoryak.tictactoe.configs.BattleValidationException;
import com.mkoryak.tictactoe.configs.WinException;
import com.mkoryak.tictactoe.dto.Battle;
import com.mkoryak.tictactoe.dto.Field;
import com.mkoryak.tictactoe.dto.MoveDto;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.mkoryak.tictactoe.services.TicTacToeUtils.generateEmptyFields;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


public class ValidationServiceTest {

    private ValidationService subject;

    @Before
    public void setUp() {
        subject = new ValidationService();
    }

    @Test
    public void isFirstPlayerMove_emptyField() {
        Battle battle = new Battle();
        battle.setFields(generateEmptyFields());

        boolean firstPlayerMove = subject.isFirstPlayerMove(battle);
        assertThat(firstPlayerMove, equalTo(true));
    }

    @Test
    public void isFirstPlayerMove_FieldWithOneXAndOneY() {
        Battle battle = new Battle();
        battle.setFields(generateEmptyFields());
        battle.getFields().get(0).setValue("X");
        battle.getFields().get(1).setValue("O");

        boolean firstPlayerMove = subject.isFirstPlayerMove(battle);
        assertThat(firstPlayerMove, equalTo(true));
    }

    @Test
    public void isFirstPlayerMove_FieldWithTwoXAndOneY() {
        Battle battle = new Battle();
        battle.setFields(generateEmptyFields());
        battle.getFields().get(0).setValue("X");
        battle.getFields().get(1).setValue("O");
        battle.getFields().get(2).setValue("X");

        boolean firstPlayerMove = subject.isFirstPlayerMove(battle);
        assertThat(firstPlayerMove, equalTo(false));
    }

    @Test(expected = BattleValidationException.class)
    public void validateMove_noBattleFound() {
        subject.validateMove(new MoveDto(), null);
    }

    @Test(expected = BattleValidationException.class)
    public void validateMove_noSecondPlayer() {
        subject.validateMove(new MoveDto(), new Battle());
    }

    @Test(expected = BattleValidationException.class)
    public void validateMove_emptyUser_excep() {
        MoveDto move = fillMove();
        move.setUserId(null);
        subject.validateMove(move, fillBattle());
    }

    @Test(expected = BattleValidationException.class)
    public void validateMove_incorrectUser_excep() {
        MoveDto move = fillMove();
        move.setUserId("INCORRECT");
        subject.validateMove(move, fillBattle());
    }

    @Test(expected = BattleValidationException.class)
    public void validateMove_bigX_excep() {
        MoveDto move = fillMove();
        move.setX(50);
        subject.validateMove(move, fillBattle());
    }

    @Test(expected = BattleValidationException.class)
    public void validateMove_secondPlayerMoving_whenFirstPlayerTurn_excep() {
        MoveDto move = fillMove();
        move.setUserId("user2");
        subject.validateMove(move, fillBattle());
    }

    @Test(expected = BattleValidationException.class)
    public void validateMove_firstPlayerMoving_whenSecondPlayerTurn_excep() {
        MoveDto move = fillMove();
        move.setUserId("user1");
        Battle battle = fillBattle();
        battle.getFields().get(0).setValue("X");
        subject.validateMove(move, battle);
    }

    @Test
    public void validateMove_hp() {
        subject.validateMove(fillMove(), fillBattle());
    }


    @Test
    public void checkPlayersWin_emptyField_nothing() {
        subject.checkPlayersWin(generateEmptyFields());
    }

    @Test
    public void checkPlayersWin_XOnEachAngleOInCenter_nothing() {
        Field f1 = Field.builder().x(0).y(0).value("X").build();
        Field f2 = Field.builder().x(2).y(0).value("X").build();
        Field f3 = Field.builder().x(0).y(2).value("X").build();
        Field f4 = Field.builder().x(2).y(2).value("X").build();
        Field f5 = Field.builder().x(1).y(1).value("O").build();
        subject.checkPlayersWin(Arrays.asList(f1, f2, f3, f4, f5));
    }

    @Test(expected = WinException.class)
    public void checkPlayersWin_checkXInRow_win() {
        Field f1 = Field.builder().x(1).y(0).value("X").build();
        Field f2 = Field.builder().x(1).y(1).value("X").build();
        Field f3 = Field.builder().x(1).y(2).value("X").build();
        subject.checkPlayersWin(Arrays.asList(f1, f2, f3));
    }

    @Test(expected = WinException.class)
    public void checkPlayersWin_checkOInColumns_win() {
        Field f1 = Field.builder().x(0).y(2).value("O").build();
        Field f2 = Field.builder().x(1).y(2).value("O").build();
        Field f3 = Field.builder().x(2).y(2).value("O").build();
        subject.checkPlayersWin(Arrays.asList(f1, f2, f3));
    }

    @Test(expected = WinException.class)
    public void checkPlayersWin_checkMainDiagonalWithX_win() {
        Field f1 = Field.builder().x(0).y(0).value("X").build();
        Field f2 = Field.builder().x(1).y(1).value("X").build();
        Field f3 = Field.builder().x(2).y(2).value("X").build();
        subject.checkPlayersWin(Arrays.asList(f1, f2, f3));
    }

    @Test(expected = WinException.class)
    public void checkPlayersWin_checkSecondDiagonalWithO_win() {
        Field f1 = Field.builder().x(0).y(2).value("O").build();
        Field f2 = Field.builder().x(1).y(1).value("O").build();
        Field f3 = Field.builder().x(2).y(0).value("O").build();
        subject.checkPlayersWin(Arrays.asList(f1, f2, f3));
    }

    @Test(expected = WinException.class)
    public void checkPlayersWin_allFieldsFilled_draw() {
        Field f1 = Field.builder().x(0).y(0).value("X").build();
        Field f2 = Field.builder().x(0).y(1).value("X").build();
        Field f3 = Field.builder().x(0).y(2).value("O").build();
        Field f4 = Field.builder().x(1).y(0).value("O").build();
        Field f5 = Field.builder().x(1).y(1).value("X").build();
        Field f6 = Field.builder().x(1).y(2).value("X").build();
        Field f7 = Field.builder().x(2).y(0).value("X").build();
        Field f8 = Field.builder().x(2).y(1).value("O").build();
        Field f9 = Field.builder().x(2).y(2).value("O").build();
        subject.checkPlayersWin(Arrays.asList(f1, f2, f3, f4, f5, f6, f7, f8, f9));
    }

    private MoveDto fillMove() {
        MoveDto move = new MoveDto();
        move.setX(0);
        move.setY(0);
        move.setUserId("user1");
        return move;
    }

    private Battle fillBattle() {
        Battle battle = new Battle();
        battle.setUser1Id("user1");
        battle.setUser2Id("user2");
        battle.setFields(generateEmptyFields());
        return battle;
    }
}