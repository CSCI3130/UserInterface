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
  
  	private String handle = "damienr74";
  	private String firstName = "Damien";
  	private String differentName = "Different";
  	private String lastName = "Lowe";
  	private String bio = "I'm cool";
  	private String hash = "hash";
  	private String salt = "salt";

	@Before
	public void init() throws SQLException {
		con = new DBUserConnector();
		user = new User();

		user.setHandle(handle);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setJoinDate(new Date(0));
		user.setLicenseID(0);
		user.setBio(bio);
		inserted = con.insertUser(user, hash, salt);
	}

	@Test
	public void testSelectUser() {
		assertEquals(user, con.selectUser(inserted.getHandle()));
	}

	@Test
	public void testDelete() {
		assertTrue(con.deleteUser(inserted));
		con.insertUser(user, hash, salt);
	}

	@Test
	public void testSimpleUpdateUser() {
		user.setFirstName(differentName);
		User updated = con.updateUser(user);
		assertEquals(user, updated);
	}

	@Test
	public void testUpdateUser() {
		User updated = con.updateUser(user, hash+"new");
      	UserLogin login = con.getUserLogin(updated.getHandle());
      	assertEquals(hash+"new", login.getHash());
	}

  	@Test
  	public void testLoginUser() {
      	UserLogin login = con.getUserLogin(handle);
      	assertEquals(handle, login.getHandle());
      	assertEquals(hash, login.getHash());
      	assertEquals(salt, login.getSalt());
    }
    
	@After
	public void clearUsers() {
		con.deleteUser(user);
	}
}
