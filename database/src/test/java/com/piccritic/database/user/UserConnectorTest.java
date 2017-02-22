/**
 * UserConnectorTest.java
 * Created Feb 15, 2017
 */
package com.piccritic.database.user;

import static org.junit.Assert.*;

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

	UserConnector con = new JPAUserConnector();
	Critic critic;
	Critic inserted;
  
  	private String handle = "damienr74";
  	private String firstName = "Damien";
  	private String differentName = "Different";
  	private String lastName = "Lowe";
  	private String bio = "I'm cool";
  	private String hash = "hash";

	@Before
	public void init() {
		critic = new Critic();

		critic.setHandle(handle);
		critic.setFirstName(firstName);
		critic.setLastName(lastName);
		critic.setJoinDate(new Date(0));
		critic.setLicenseID(0);
		critic.setBio(bio);
		inserted = con.insertCritic(critic, hash);
	}

	@Test
	public void testSelectUser() {
		assertEquals(critic, con.selectCritic(inserted.getHandle()));
	}

	@Test
	public void testDelete() {
		assertTrue(con.deleteCritic(inserted));
		con.insertCritic(critic, hash);
	}

	@Test
	public void testSimpleUpdateUser() {
		critic.setFirstName(differentName);
		Critic updated = con.updateCritic(critic);
		assertEquals(critic, updated);
	}

	@Test
	public void testUpdateUser() {
		String newHash = "newhash";
		Critic updated = con.updateCritic(critic, newHash);
      	String storedHash = con.getUserHash(updated.getHandle());
      	assertEquals(newHash, storedHash);
	}

  	@Test
  	public void testLoginUser() {
      	String storedHash = con.getUserHash(handle);
      	assertEquals(storedHash, hash);
    }
    
	@After
	public void clearUsers() {
		con.deleteCritic(critic);
	}
}
