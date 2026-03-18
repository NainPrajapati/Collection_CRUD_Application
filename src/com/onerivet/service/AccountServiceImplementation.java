package com.onerivet.service;

import java.util.List;

import com.onerivet.exception.AccountNotFoundException;
import com.onerivet.exception.DuplicateAccountException;
import com.onerivet.model.Accounts;
import com.onerivet.model.Users;
import com.onerivet.repository.AccountRepository;
import com.onerivet.repository.UserRepository;

public class AccountServiceImplementation implements AccountService{
	
	private final AccountRepository accountRepo;
	private final UserRepository userRepo;

    public AccountServiceImplementation(AccountRepository accountRepo , UserRepository userRepo) {
        this.accountRepo = accountRepo;
        this.userRepo=userRepo;
    }

    private static long accountCounter = 2026000000L;
    public void createAccount(Accounts account) {

    	 
    	if (account.getUserId() == null || account.getUserId().isEmpty()) {
	        throw new RuntimeException("User ID is required.");
	    }
    	
    	if (account.getBalance() == null || account.getBalance() < 0) {
	        throw new RuntimeException("Balance must be a valid positive number.");
	    }
    	
    	 userRepo.findById(account.getUserId())
         .orElseThrow(() ->
             new RuntimeException("User does not exist. Please create user first."));
    	
    	 accountCounter++; 
         String generatedId = String.valueOf(accountCounter);
         account.setAccountNumber(generatedId);
         
    	    accountRepo.save(account);
    }

    public Accounts getAccount(String accountNumber) {
        return accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with Account No. : " + accountNumber));
    }

    public List<Accounts> getAllAccounts() {
    	List<Accounts> accounts = accountRepo.findAll();

        if (accounts.isEmpty()) {
            throw new AccountNotFoundException("No accounts found in the system.");
        }
        
        return accounts.stream()
                .sorted((u1, u2) -> u1.getAccountNumber().compareToIgnoreCase(u2.getAccountNumber()))
                .toList();
    }
    
    
    public void updateAccount(String userId, String accountNumber, Double newBalance) {

    	Accounts account = getAccount(accountNumber);

        if (!account.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied. Account does not belong to user.");
        }

        if (account.getBalance() == null || account.getBalance() < 0) {
	        throw new RuntimeException("Balance must be a valid positive number.");
	    }
        
        account.setBalance(newBalance);
        accountRepo.save(account);
    }

    public void deleteAccount(String accountNumber) {
    	 Accounts account = getAccount(accountNumber);

    	 boolean hasOtherAccounts = false;

    	 List<Accounts> allAccounts = accountRepo.findAll();

    	 for (Accounts acc : allAccounts) {
    	     if (acc.getUserId().equals(account.getUserId())) {
    	         hasOtherAccounts = true;
    	         break;
    	     }
    	 }

    	    if (!hasOtherAccounts) {
    	        userRepo.delete(account.getUserId());
    	    }
    	    accountRepo.delete(accountNumber);
    }

    public void deleteAllAccounts() {
        if (accountRepo.findAll().isEmpty()) {
            throw new RuntimeException("No accounts found to delete.");
        }
        accountRepo.deleteAll();
    }
}
