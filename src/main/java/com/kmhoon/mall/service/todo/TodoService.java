package com.kmhoon.mall.service.todo;

import com.kmhoon.mall.domain.todo.Todo;
import com.kmhoon.mall.dto.common.page.PageRequestDto;
import com.kmhoon.mall.dto.common.page.PageResponseDto;
import com.kmhoon.mall.dto.domain.todo.TodoDto;
import com.kmhoon.mall.repository.todo.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TodoService {

    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Long register(TodoDto todoDto) {
        Todo todo = modelMapper.map(todoDto, Todo.class);
        Todo savedTodo = todoRepository.save(todo);
        return savedTodo.getTno();
    }

    @Transactional(readOnly = true)
    public TodoDto get(Long tno) {
        Todo todo = getTodo(tno);
        return modelMapper.map(todo, TodoDto.class);
    }

    private Todo getTodo(Long tno) {
        return todoRepository.findById(tno).orElseThrow(() -> new EntityNotFoundException("요청한 tno에 해당하는 Todo가 존재하지 않습니다."));
    }

    @Transactional
    public void modify(TodoDto todoDto) {
        Todo todo = getTodo(todoDto.getTno());
        todo.changeTitle(todoDto.getTitle());
        todo.changeDueDate(todoDto.getDueDate());
        todo.changeComplete(todoDto.isComplete());
        todoRepository.save(todo);
    }

    @Transactional
    public void remove(Long tno) {
        Todo todo = getTodo(tno);
        todoRepository.delete(todo);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<TodoDto> list(PageRequestDto pageRequestDto) {

        PageRequest pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize(), Sort.by("tno").descending());
        Page<Todo> result = todoRepository.findAll(pageable);

        List<TodoDto> dtoList = result.getContent().stream().map(todo -> modelMapper.map(todo, TodoDto.class)).collect(Collectors.toList());
        long totalCount = result.getTotalElements();

        return PageResponseDto.<TodoDto>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDto)
                .totalCount(totalCount)
                .build();
    }
}
