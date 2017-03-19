/**
 * HasherTest.java
 * Created Feb 21, 2017
 */
package com.piccritic.compute.hashing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class is for testing the Hasher class.
 * 
 * @author ian-dawson <br>
 * 		ajsteadly
 */

import java.sql.Date;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.piccritic.database.license.AttributionNoDerivsLicense;
import com.piccritic.database.user.Critic;
import com.piccritic.database.user.JPAUserConnector;
import com.piccritic.database.user.UserConnector;
import com.piccritic.database.user.UserException;

public class HasherTest {

	UserConnector con;
	Critic user1, user2;
	Critic inserted1, inserted2;
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
	public void init() throws SQLException, UserException {
		con = new JPAUserConnector();
		user1 = new Critic();
		user2 = new Critic();
		hasher = new Hasher();

		user1.setHandle(handle1);
		user1.setFirstName(firstName1);
		user1.setLastName(lastName1);
		user1.setLicense(new AttributionNoDerivsLicense());
		user1.setJoinDate(joinDate1);
		inserted1 = con.insertCritic(user1, hasher.generateHash(password1));
		user2.setHandle(handle2);
		user2.setFirstName(firstName2);
		user2.setLastName(lastName2);
		user2.setLicense(new AttributionNoDerivsLicense());
		user2.setJoinDate(joinDate2);
		inserted2 = con.insertCritic(user2, hasher.generateHash(password2));
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

	@Test
	public void testInvalidUser() {
		assertFalse(hasher.checkLogin("newUser", "somepassword"));
	}

	@After
	public void clearUsers() {
		con.deleteCritic(user1);
		con.deleteCritic(user2);
	}

}
