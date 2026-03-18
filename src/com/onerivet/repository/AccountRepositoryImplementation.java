package com.onerivet.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.onerivet.model.Accounts;

public class AccountRepositoryImplementation implements AccountRepository {
	private final Map<String, Accounts> accountStorage = new HashMap<>();
	
	public void save(Accounts account) {
		accountStorage.put(account.getAccountNumber(), account);
	}
	
	public Optional<Accounts> findByAccountNumber(String accountNumber) {
		return Optional.ofNullable(accountStorage.get(accountNumber));
	}
	
	public List<Accounts> findAll(){
		return new ArrayList<>(accountStorage.values());
	}
	
	public void delete(String accountNumber) {
		accountStorage.remove(accountNumber);
	}
	
	public void deleteAll() {
	    accountStorage.clear();
	}
}
