package com.onerivet.repository;

import java.util.List;
import java.util.Optional;

import com.onerivet.model.Accounts;

public interface AccountRepository {
	void save(Accounts account);
    Optional<Accounts> findByAccountNumber(String accountNumber);
    List<Accounts> findAll();
    void delete(String accountNumber);
    void deleteAll();
}

