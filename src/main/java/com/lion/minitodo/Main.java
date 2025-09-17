package com.lion.minitodo;

import com.lion.minitodo.dao.JdbcTodoDAO;
import com.lion.minitodo.dao.JdbcUserDAO;
import com.lion.minitodo.dto.TodoDTO;
import com.lion.minitodo.dto.UserDTO;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        JdbcUserDAO userDao = new JdbcUserDAO();
        JdbcTodoDAO todoDao = new JdbcTodoDAO();

        // 1. 유저 생성
        UserDTO newUser = new UserDTO();
        newUser.setEmail("test@example.com");
        newUser.setPassword("1234");
        int userId = userDao.create(newUser);
        System.out.println("유저 생성됨: id=" + userId);

        // 2. 같은 이메일 중복 체크
        boolean exists = userDao.existsByEmail("test@example.com");
        System.out.println("이메일 존재? " + exists);

        // 3. 할 일 추가
        TodoDTO todo = new TodoDTO();
        todo.setUserId(userId);
        todo.setTitle("자바 공부하기");
        todo.setContent("DTO, DAO 복습");
        todo.setDue("2025-09-20");
        todo.setDone(false);
        int todoId = todoDao.create(todo);
        System.out.println("할 일 생성됨: id=" + todoId);

        // 4. 유저별 할 일 조회
        List<TodoDTO> todos = todoDao.findByUser(userId);
        System.out.println("할 일 갯수: " + todos.size());
        for (TodoDTO t : todos) {
            System.out.println("제목: " + t.getTitle() + ", 완료여부: " + t.isDone());
        }

        // 5. 완료 처리
        todoDao.markDone(todoId, true);
        TodoDTO updated = todoDao.findById(todoId);
        System.out.println("완료상태: " + updated.isDone());
    }
}
