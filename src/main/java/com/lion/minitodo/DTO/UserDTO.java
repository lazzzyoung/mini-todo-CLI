package com.lion.minitodo.DTO;

public class UserDTO {
    private int userId;
    private String email;
    private String password;
    private String createAt;

    public UserDTO(int userId, String email, String password, String createAt) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.createAt = createAt;
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
