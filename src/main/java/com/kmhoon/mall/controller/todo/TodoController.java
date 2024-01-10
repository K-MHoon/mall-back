package com.kmhoon.mall.controller.todo;

import com.kmhoon.mall.dto.common.page.PageRequestDto;
import com.kmhoon.mall.dto.common.page.PageResponseDto;
import com.kmhoon.mall.dto.domain.todo.TodoDto;
import com.kmhoon.mall.dto.response.todo.TodoResponse;
import com.kmhoon.mall.service.todo.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/{tno}")
    @ResponseStatus(HttpStatus.OK)
    public TodoDto get(@PathVariable(name = "tno") Long tno) {
        log.info("Get: " + tno);
        return todoService.get(tno);
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<TodoDto> list(PageRequestDto pageRequestDto) {
        log.info("List: " + pageRequestDto);
        return todoService.list(pageRequestDto);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public TodoResponse.Register register(@RequestBody TodoDto todoDto) {
        log.info("Register: " + todoDto);
        Long tno = todoService.register(todoDto);
        return TodoResponse.Register.builder()
                .tno(tno)
                .build();
    }

    @PutMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public TodoResponse.Modify modify(@RequestBody TodoDto todoDto) {
        log.info("Modify: " + todoDto);
        todoService.modify(todoDto);
        return TodoResponse.Modify.builder().result("success").build();
    }

    @DeleteMapping("/{tno}")
    @ResponseStatus(HttpStatus.OK)
    public TodoResponse.Remove remove(@PathVariable(name = "tno") Long tno) {
        log.info("Remove: " + tno);
        todoService.remove(tno);
        return TodoResponse.Remove.builder().result("success").build();
    }

}
