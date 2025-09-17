package com.lion.minitodo.DTO;
public class TodoDTO {
    private int todoId;
    private int userId;
    private String title;
    private String content;
    private String due;
    private boolean done;
    private String createdAt;

    public TodoDTO(int todoId, int userId, String title, String content, String due, boolean done, String createdAt) {
        this.todoId = todoId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.due = due;
        this.done = done;
        this.createdAt = createdAt;
    }

    public int getTodoId() { return todoId; }
    public void setTodoId(int todoId) { this.todoId = todoId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDue() { return due; }
    public void setDue(String due) { this.due = due; }

    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
