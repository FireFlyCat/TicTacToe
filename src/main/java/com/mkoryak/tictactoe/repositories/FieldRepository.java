package com.mkoryak.tictactoe.repositories;

import com.mkoryak.tictactoe.dto.Field;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends CrudRepository<Field, Integer> {
}
