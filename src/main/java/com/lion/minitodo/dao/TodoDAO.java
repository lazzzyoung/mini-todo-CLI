package com.lion.minitodo.dao;

import com.lion.minitodo.dto.TodoDTO;
import java.util.List;

public interface TodoDAO {
    int create(TodoDTO todo) throws Exception;
    int update(TodoDTO todo) throws Exception;
    int delete(int todoId) throws Exception;
    TodoDTO findById(int todoId) throws Exception;
    List<TodoDTO> findByUser(int userId) throws Exception;
    int markDone(int todoId, boolean done) throws Exception;
}
