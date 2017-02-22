package com.piccritic.compute;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.piccritic.database.user.DBUserConnector;
import com.piccritic.database.user.User;
import com.piccritic.database.user.UserConnector;

public class LoginConnectorTest {
	UserConnector con;
	User user1, user2;
	User inserted;
	LoginConnector loginconn;
  
  	private String handle1 = "ian-dawson";
  	private String hash1 = "hash";
  	private String salt1 = "salt";
  	
  	private String handle2 = "ajsteadly";
  	private String password2 = "password";

	@Before
	public void init() throws SQLException {
		con = new DBUserConnector();
		user1 = new User();
		user2 = new User();


		user1.setHandle(handle1);
		inserted = con.insertUser(user1, hash1, salt1);
		loginconn = new LoginConnector();
		
		user2.setHandle(handle2);
	}
	
	@Test
	public void testGetLoginStatus() {
		assertEquals(loginconn.getLoginStatus(), LoginInterface.LoginStatus.LOGGED_OUT);
		//loginconn.loginUser(handle1, password1);
	}
	
	@Test
	public void testGenerateHashSalt() {
		String[] hashSalt = loginconn.generateHashSalt(handle2, password2);
		assertEquals(hashSalt[1], handle2);
		//assertEquals(hashSalt[0], "");
	}
	
	@After
	public void clearUsers() {
		con.deleteUser(user1);
		con.deleteUser(user2);
	}
}
