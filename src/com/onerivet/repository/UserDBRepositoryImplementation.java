package com.onerivet.repository;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.onerivet.config.DBConnection;
import com.onerivet.exception.DuplicateUserException;
import com.onerivet.model.Users;

public class UserDBRepositoryImplementation implements UserRepository{
	
	public void save(Users user) {
		String sql = "insert into users (user_id, user_name, user_email) values (?, ?, ?)";
		
		try (Connection conn = DBConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, user.getUserId());
	            stmt.setString(2, user.getUserName());
	            stmt.setString(3, user.getUserEmail());
	            stmt.executeUpdate();
	        } catch (SQLException e) {
	            if ("23505".equals(e.getSQLState())) // PostgreSQL code for Unique Violation
	                throw new DuplicateUserException("Database Error: User or Email already exists.");
	            throw new RuntimeException(e.getMessage());
	        }
	    }
	
	public Optional<Users> findById(String userId){
		return findBy("select * from users where user_id = ?",userId);
	}
	
	public Optional<Users> findByEmail(String email){
		return findBy("select * from users where user_email = ?",email);
	}
	
	public List<Users> findByUserName(String name){
		List<Users> list = new ArrayList<>();
		
		try(Connection conn = DBConnection.getConnection();
			PreparedStatement pstatement = conn.prepareStatement("select * from users where user_name ilike ?")){
			
			pstatement.setString(1, "%" + name + "%");
            ResultSet result = pstatement.executeQuery();
            while (result.next()) list.add(mapRow(result));
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return list;
	}
	
	public List<Users> findAll() {
        List<Users> list = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery("SELECT * FROM users")) {
        	
            while (result.next()) list.add(mapRow(result));
        } catch (SQLException e) { 
        	throw new RuntimeException(e); 
        }
        return list;
    }
	
	public void delete(String userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstatement = conn.prepareStatement("DELETE FROM users WHERE user_id = ?")) {
        	
        	pstatement.setString(1, userId);
        	pstatement.executeUpdate();
        } catch (SQLException e) { 
        	throw new RuntimeException(e); 
        }
    }
	
	public void deleteAll() {
        try (Connection conn = DBConnection.getConnection(); 
        	Statement statement = conn.createStatement()) {
        	
        	statement.executeUpdate("DELETE FROM users");
        } catch (SQLException e) { 
        	throw new RuntimeException(e); 
        }
    }
	
	private Optional<Users> findBy(String sql, String val) {
		
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstatement = conn.prepareStatement(sql)) {
        	
        	pstatement.setString(1, val);
            ResultSet result = pstatement.executeQuery();
            if (result.next()) return Optional.of(mapRow(result));
        } catch (SQLException e) { 
        	throw new RuntimeException(e); 
        }
        return Optional.empty();
    }
	
	private Users mapRow(ResultSet rs) throws SQLException {
        Users user = new Users(rs.getString("user_name"), rs.getString("user_email"));
        try {
            Field field = Users.class.getDeclaredField("userId");
            field.setAccessible(true);
            field.set(user, rs.getString("user_id"));
        } catch (Exception e) { throw new RuntimeException(e); }
        return user;
    }
	
}

