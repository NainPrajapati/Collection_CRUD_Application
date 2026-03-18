package com.onerivet.service;

import java.util.List;

import com.onerivet.exception.DuplicateUserException;
import com.onerivet.exception.UserNotFoundException;
import com.onerivet.model.Accounts;
import com.onerivet.model.Users;
import com.onerivet.repository.AccountRepository;
import com.onerivet.repository.UserRepository;

public class UserServiceImplementation implements UserService{
	
	private final UserRepository userRepo;
	private final AccountRepository accountRepo;
    public UserServiceImplementation(UserRepository userRepo , AccountRepository accountRepo) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
    }

    
    
    public void createUser(Users user) {

    	if (user.getUserId() == null || user.getUserId().isEmpty()) {
	        throw new RuntimeException("User ID is required.");
	    }
    	
    	if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            throw new RuntimeException("User Name Cannot Be Empty!!..");
        }
    	
    	if (user.getUserEmail() == null || user.getUserEmail().trim().isEmpty()) {
            throw new RuntimeException("User Email Cannot Be Empty!!..");
        }
    	
    	validateEmail(user.getUserEmail());

        if (userRepo.findById(user.getUserId()).isPresent()) {
            throw new DuplicateUserException("User ID already exists");
        }

        if (userRepo.findByEmail(user.getUserEmail()).isPresent()) {
            throw new DuplicateUserException("Email already exists");
        }

        userRepo.save(user);
    }

    public Users getUserById(String userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID : "+ userId));
    }

    public List<Users> getAllUsers() {
    	List<Users> users = userRepo.findAll();

    	if (users.isEmpty()) {
            throw new UserNotFoundException("No users found in the system.");
        }

    	return users.stream()
                .sorted((u1, u2) -> u1.getUserName().compareToIgnoreCase(u2.getUserName()))
                .toList();
    }
    
    public Users getUserByEmail(String email) {

        validateEmail(email);

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with Email: " + email));
    }

    public List<Users> getUserByUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new RuntimeException("Username cannot be empty for search.");
        }

        List<Users> results = userRepo.findByUserName(userName);

        if (results.isEmpty()) {
            throw new UserNotFoundException("No users found containing the name: " + userName);
        }

        return results;
    }
    
    public void updateUser(String userId, String email, String newName, String newEmail) {

    	Users user = getUserById(userId);

        if (!user.getUserEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("Authentication failed. Please Insert Valid Registered Email!!..");
        }

        if (user.getUserName() == null || user.getUserEmail().trim().isEmpty()) {
            throw new RuntimeException("New User Name Cannot be Empty!..");
        }
        
        validateEmail(newEmail);

        Users existingUser = userRepo.findByEmail(newEmail).orElse(null);

        if (existingUser != null && !existingUser.getUserId().equals(userId)) {
            throw new DuplicateUserException("Email already used.");
        }

        user.setUserName(newName);
        user.setUserEmail(newEmail);

        userRepo.save(user);
    }

    public void deleteUser(String userId) {
    	
    	Users user = getUserById(userId);
    	List<Accounts> allAccounts = accountRepo.findAll();

    	for (Accounts acc : allAccounts) {
    	    if (acc.getUserId().equals(userId)) {
    	        accountRepo.delete(acc.getAccountNumber());
    	    }
    	}

        userRepo.delete(userId);
    }
    
    public void deleteAllUsers() {
        if (userRepo.findAll().isEmpty()) {
            throw new RuntimeException("No users found to delete.");
        }

        accountRepo.deleteAll(); 

        userRepo.deleteAll();
        
        System.out.println("All users and their associated accounts have been cleared.");
    }
    
    
    private void validateEmail(String email) {

        if (email == null || email.trim().isEmpty() ||
            !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

            throw new RuntimeException("Invalid email format.");
        }
    }
}
