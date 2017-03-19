/**
 * UserConnectorTest.java
 * Created Feb 15, 2017
 */
package com.piccritic.database.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.piccritic.database.license.AttributionLicense;
import com.piccritic.database.license.JPALicenseConnector;
import com.piccritic.database.post.AlbumException;

/**
 * This class uses JUnit to test the methods
 * of JPAUserConnector.
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
		new JPALicenseConnector();
		critic = new Critic();

		critic.setHandle(handle);
		critic.setFirstName(firstName);
		critic.setLastName(lastName);
		critic.setJoinDate(new Date(0));
		critic.setLicense(new AttributionLicense());
		critic.setBio(bio);
		try {
			inserted = con.insertCritic(critic, hash);
		} catch (UserException e) {

		}
	}

	@Test
	public void testSelectUser() {
		assertEquals(critic, con.selectCritic(inserted.getHandle()));
	}

	@Test
	public void testDelete() {
		assertTrue(con.deleteCritic(inserted));
		try {
			con.insertCritic(critic, hash);
		} catch (UserException e) {
		}
	}

	@Test
	public void testSimpleUpdateUser() {
		critic.setFirstName(differentName);
		Critic updated;
		try {
			updated = con.updateCritic(critic);
		} catch (UserException e) {
			assertTrue(false);
			return;
		}
		assertEquals(critic, updated);
	}

	@Test
	public void testUpdateUser() {
		String newHash = "newhash";
		Critic updated;
		try {
			updated = con.updateCritic(critic, newHash);
		} catch (UserException e) {
			assertTrue(false);
			return;
		}
		String storedHash = con.getUserHash(updated.getHandle());
		assertEquals(newHash, storedHash);
	}

	@Test
	public void testLoginUser() {
		String storedHash = con.getUserHash(handle);
		assertEquals(storedHash, hash);
	}

	@After
	public void clearUsers() throws AlbumException {
		con.deleteCritic(critic);
	}
}
