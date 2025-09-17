package com.lion.minitodo.dao;

import com.lion.minitodo.dto.TodoDTO;
import com.lion.minitodo.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTodoDAO implements TodoDAO {

    @Override
    public int create(TodoDTO todo) throws Exception {
        String sql = "INSERT INTO todos (user_id, title, content, due, done) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, todo.getUserId());
            ps.setString(2, todo.getTitle());
            ps.setString(3, todo.getContent());
            ps.setString(4, todo.getDue());
            ps.setBoolean(5, todo.isDone());

            int affected = ps.executeUpdate();
            if (affected == 0) return 0;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    @Override
    public int update(TodoDTO todo) throws Exception {
        String sql = "UPDATE todos SET title=?, content=?, due=?, done=? WHERE todo_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, todo.getTitle());
            ps.setString(2, todo.getContent());
            ps.setString(3, todo.getDue());
            ps.setBoolean(4, todo.isDone());
            ps.setInt(5, todo.getTodoId());
            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(int todoId) throws Exception {
        String sql = "DELETE FROM todos WHERE todo_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, todoId);
            return ps.executeUpdate();
        }
    }

    @Override
    public TodoDTO findById(int todoId) throws Exception {
        String sql = "SELECT * FROM todos WHERE todo_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, todoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapRow(rs);
            }
        }
    }

    @Override
    public List<TodoDTO> findByUser(int userId) throws Exception {
        String sql = "SELECT * FROM todos WHERE user_id=? ORDER BY due ASC";
        List<TodoDTO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public int markDone(int todoId, boolean done) throws Exception {
        String sql = "UPDATE todos SET done=? WHERE todo_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, done);
            ps.setInt(2, todoId);
            return ps.executeUpdate();
        }
    }

    private TodoDTO mapRow(ResultSet rs) throws Exception {
        TodoDTO t = new TodoDTO();
        t.setTodoId(rs.getInt("todo_id"));
        t.setUserId(rs.getInt("user_id"));
        t.setTitle(rs.getString("title"));
        t.setContent(rs.getString("content"));
        t.setDue(rs.getString("due"));
        t.setDone(rs.getBoolean("done"));
        t.setCreatedAt(rs.getString("created_at"));
        return t;
    }
}