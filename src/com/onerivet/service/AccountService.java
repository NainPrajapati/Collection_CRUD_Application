package com.onerivet.service;

import java.util.List;

import com.onerivet.model.Accounts;

public interface AccountService {
	void createAccount(Accounts account);
    Accounts getAccount(String accountNumber);
    List<Accounts> getAllAccounts();
    void updateAccount(String userId, String accountNumber, Double newBalance);
    void deleteAccount(String accountNumber);
    void deleteAllAccounts();

}
