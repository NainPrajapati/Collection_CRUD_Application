package com.onerivet.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.onerivet.model.Users;

public class UserRepositoryImplementation implements UserRepository {
	private final Map<String, Users> userStorage = new HashMap<>();
	
	public void save(Users user) {
		userStorage.put(user.getUserId(),user);
	}
	
	public Optional<Users> findById(String userId) {
		return Optional.ofNullable(userStorage.get(userId));
	}
	
	public List<Users> findByUserName(String userName) {
	    List<Users> matches = new ArrayList<>();
	    for (Users user : userStorage.values()) {
	        if (user.getUserName().toLowerCase().contains(userName.toLowerCase())) {
	            matches.add(user);
	        }
	    }
	    return matches;
	}
	
	public Optional<Users> findByEmail(String userEmail) {
		for (Users user : userStorage.values()) {
		    if (user.getUserEmail().equalsIgnoreCase(userEmail)) {
		        return Optional.of(user);
		    }
		}
		return Optional.empty();
    }
	
	public List<Users> findAll(){
		return new ArrayList<>(userStorage.values());
	}
	
	public void delete(String userId) {
		userStorage.remove(userId);
	}
	
	public void deleteAll() {
	    userStorage.clear();
	}
}
