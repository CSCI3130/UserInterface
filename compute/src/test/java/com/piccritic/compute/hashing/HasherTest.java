package com.piccritic.compute.hashing;

import java.sql.Date;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.piccritic.compute.hashing.BCrypt;
import com.piccritic.compute.hashing.Hasher;
import com.piccritic.database.user.DBUserConnector;
import com.piccritic.database.user.User;
import com.piccritic.database.user.UserConnector;

public class HasherTest {
	
	UserConnector con;
	User user1, user2;
	User inserted1, inserted2;
	Hasher hasher;
  
  	private String handle1 = "ian-dawson";
  	private String firstName1 = "Ian";
  	private String lastName1 = "Dawson";
  	private Date joinDate1 = new Date(0);
  	private String password1 = "hunter2";
  	
  	private String handle2 = "ajsteadly";
  	private String password2 = "password";
  	private String firstName2 = "Amelia";
  	private String lastName2 = "Stead";
  	private Date joinDate2 = new Date(0);
  	
	@Before
	public void init() throws SQLException {
		con = new DBUserConnector();
		user1 = new User();
		user2 = new User();
		hasher = new Hasher();
		
		user1.setHandle(handle1);
		user1.setFirstName(firstName1);
		user1.setLastName(lastName1);
		user1.setJoinDate(joinDate1);
		inserted1 = con.insertUser(user1, hasher.generateHash(password1), "");
		
		user2.setHandle(handle2);
		user2.setFirstName(firstName2);
		user2.setLastName(lastName2);
		user2.setJoinDate(joinDate2);
		inserted2 = con.insertUser(user2, hasher.generateHash(password2), "");
		
	}
	
	@Test
	public void testGenerateHash() {
		String hash = hasher.generateHash(password1);
		assertTrue(BCrypt.checkpw(password1, hash));
		assertFalse(BCrypt.checkpw("NotThePassword", hash));
	}
	
	@Test
	public void testCheckLogin() {
		assertTrue(hasher.checkLogin(handle1, password1));
		assertTrue(hasher.checkLogin(handle2, password2));
		assertFalse(hasher.checkLogin(handle1, password2));
		assertFalse(hasher.checkLogin(handle1, "wrongpassword"));
	}
	
	@After
	public void clearUsers() {
		con.deleteUser(user1);
		con.deleteUser(user2);
	}
	
}
