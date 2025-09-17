package com.lion.minitodo.dto;

public class UserDTO {
    private int userId;
    private String email;
    private String password;
    private String createAt;

    public UserDTO() {
        System.out.println("user DTO 생성");

    }
    public UserDTO(int userId, String email, String password, String createAt) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.createAt = createAt;

        System.out.println("user DTO 생성");
    }

    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getCreateAt() {return createAt;}
    public void setCreateAt(String createAt) {this.createAt = createAt;}
}
