package com.mkoryak.tictactoe.repositories;

import com.mkoryak.tictactoe.dto.Battle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleRepository extends CrudRepository<Battle, Integer> {
}
