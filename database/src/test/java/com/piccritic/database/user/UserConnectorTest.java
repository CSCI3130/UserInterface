/**
 * UserConnectorTest.java
 * Created Feb 15, 2017
 */
package com.piccritic.database.user;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import java.sql.Date;

/**
 * Because of the database structure, insert must work for any
 * of the tests to work.
 *
 * @author Ryan Lowe<br>Damien Robichaud
 */
public class UserConnectorTest {

	UserConnector con;
	User user;
	User inserted;

	@Before
	public void init() throws SQLException {
		con = new DBUserConnector();
		user = new User();

		user.setHandle("damienr74");
		user.setFirstName("Damien");
		user.setLastName("Lowe");
		user.setJoinDate(new Date(0));
		user.setLicenseID(0);
		user.setBio("I'm cool");
		inserted = con.insertUser(user, "", "");
	}

	@Test
	public void testSelectUser() {
		assertEquals(user, con.selectUser(inserted.getHandle()));
	}

	@Test
	public void testDelete() {
		assertTrue(con.deleteUser(inserted));
		con.insertUser(user, "", "");
	}

	@Test
	public void testSimpleUpdateUser() {
		user.setFirstName("different");
		User updated = con.updateUser(user);
		assertEquals(user, updated);
	}

	/* TODO implement test for other updates() method when
	 * login is created
	 */
	@Test
	public void testUpdateUser() {
		User updated = con.updateUser(user, "hash");
		// get login credentials to check hash and salt
		// check that the hash has been changed
	}

	@After
	public void clearUsers() {
		con.deleteUser(user);
	}
}
