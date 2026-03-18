package com.onerivet.repository;

import java.util.List;
import java.util.Optional;

import com.onerivet.model.Users;

public interface UserRepository {
	void save(Users user);
    Optional<Users> findById(String userId);
    Optional<Users> findByEmail(String userEmail);
    List<Users> findByUserName(String userName);
    List<Users> findAll();
    void delete(String userId);
    void deleteAll();
}

