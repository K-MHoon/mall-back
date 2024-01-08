package com.kmhoon.mall.service;

import com.kmhoon.mall.domain.todo.Todo;
import com.kmhoon.mall.dto.common.page.PageRequestDto;
import com.kmhoon.mall.dto.common.page.PageResponseDto;
import com.kmhoon.mall.dto.domain.todo.TodoDto;
import com.kmhoon.mall.repository.todo.TodoRepository;
import com.kmhoon.mall.service.todo.TodoService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Log4j2
class TodoServiceTest {

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testRegister() {
        // given
        TodoDto todoDto = TodoDto.builder()
                .title("서비스 테스트")
                .writer("tester")
                .dueDate(LocalDate.of(2023, 10, 10))
                .build();

        // when
        Long tno = todoService.register(todoDto);

        // then
        Todo result = todoRepository.findById(tno).get();
        assertThat(result)
                .extracting("title", "writer", "dueDate")
                .contains("서비스 테스트", "tester", LocalDate.of(2023, 10, 10));
    }

    @Test
    public void testGet() {
        // given
        Todo todoDto = Todo.builder()
                .title("서비스 테스트")
                .writer("tester")
                .dueDate(LocalDate.of(2023, 10, 10))
                .build();
        Todo savedTodo = todoRepository.save(todoDto);

        // when
        TodoDto result = todoService.get(savedTodo.getTno());

        // then
        assertThat(result)
                .extracting("title", "writer", "dueDate")
                .contains("서비스 테스트", "tester", LocalDate.of(2023,10,10));
    }

    @Test
    public void testList() {
        // given
        List<Todo> todoList = IntStream.rangeClosed(1, 30)
                .mapToObj(i -> Todo.builder()
                        .title("서비스 테스트" + i)
                        .writer("tester" + i)
                        .dueDate(LocalDate.of(2023, 12, i))
                        .build())
                .collect(Collectors.toList());
        todoRepository.saveAll(todoList);

        PageRequestDto request = PageRequestDto.builder()
                .page(2)
                .size(5)
                .build();

        // when
        PageResponseDto<TodoDto> result = todoService.list(request);

        // then
        assertThat(result)
                .extracting("totalCount", "prevPage", "nextPage", "totalPage", "current")
                .contains(20, 0, 0, 4, 2);
    }
}