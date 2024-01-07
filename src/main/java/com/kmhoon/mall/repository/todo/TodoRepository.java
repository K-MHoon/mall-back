package com.kmhoon.mall.repository.todo;

import com.kmhoon.mall.domain.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
