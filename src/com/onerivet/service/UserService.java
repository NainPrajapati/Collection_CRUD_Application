package com.onerivet.service;

import java.util.List;

import com.onerivet.model.Users;

public interface UserService {
	void createUser(Users user);
    Users getUserById(String userId);
    Users getUserByEmail(String email);
    List<Users> getUserByUserName(String userName);
    List<Users> getAllUsers();
    void updateUser(String userId, String email, String newName, String newEmail);
    void deleteUser(String userId);
    void deleteAllUsers();

}

