package com.lion.minitodo.dao;

import com.lion.minitodo.dto.UserDTO;

public interface UserDAO {
    int create (UserDTO user) throws Exception;
    int updatePassword(int userId, String newPassword) throws Exception;

    UserDTO findById(int id) throws Exception;
    UserDTO findByEmail(String email) throws Exception;

    boolean existsByEmail(String email) throws Exception;


}
