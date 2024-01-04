package com.kmhoon.mall.repository;

import com.kmhoon.mall.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
