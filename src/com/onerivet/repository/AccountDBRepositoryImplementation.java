package com.onerivet.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.onerivet.config.DBConnection;
import com.onerivet.exception.DuplicateAccountException;
import com.onerivet.model.Accounts;

public class AccountDBRepositoryImplementation implements AccountRepository{
	
	public void save(Accounts account) {
		
		String sql = "insert into accounts(account_number,user_id,balance) values (?,?,?)";
		
		try(Connection conn = DBConnection.getConnection();
			PreparedStatement pstatement = conn.prepareStatement(sql)){
			
			pstatement.setString(1, account.getAccountNumber());
			pstatement.setString(2, account.getUserId());
			pstatement.setDouble(3, account.getBalance());
			pstatement.executeUpdate();
		}catch(SQLException e) {
			if ("23505".equals(e.getSQLState())) {
				throw new DuplicateAccountException("Account already exists.");
			}
            throw new RuntimeException(e.getMessage());

		}
	}
	
	
	public Optional<Accounts> findByAccountNumber(String accNum) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM accounts WHERE account_number = ?")) {
            stmt.setString(1, accNum);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Accounts acc = new Accounts(rs.getString("user_id"), rs.getDouble("balance"));
                acc.setAccountNumber(rs.getString("account_number"));
                return Optional.of(acc);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }
	
	
	public List<Accounts> findAll() {
        List<Accounts> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM accounts")) {
            while (rs.next()) {
                Accounts acc = new Accounts(rs.getString("user_id"), rs.getDouble("balance"));
                acc.setAccountNumber(rs.getString("account_number"));
                list.add(acc);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }
	
	
	public void delete(String accNum) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM accounts WHERE account_number = ?")) {
            stmt.setString(1, accNum);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
	
	
	public void deleteAll() {
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM accounts");
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
	

}
