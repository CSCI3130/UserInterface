/**
 * PostConnectorTest.java
 * Created Feb 24, 2017
 */
package com.piccritic.database.post;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.piccritic.database.user.Critic;
import com.piccritic.database.user.JPAUserConnector;
import com.piccritic.database.user.UserConnector;
import com.piccritic.database.user.UserException;

/**
 * This class uses JUnit to test the functionality
 * of the JPAPostConnector methods.
 * 
 * @author Damien Robichaud<br>Ryan Lowe
 */
public class PostConnectorTest {
	
	Post post = new Post();
	Album album = new Album();
	Critic critic = new Critic();

	private String firstName = "firstName";
	private String handle = "handle";
	private String lastName = "lastName";
	private String hash = "hash";
	private String albumName = "album";
	private String postTitle = "title";
	private String path = "/path";

	private Date date = new Date(0);

	private Set<Post> postSet = new HashSet<Post>();
	private Set<Album> albumSet = new HashSet<Album>();

	PostConnector pc = new JPAPostConnector();
	UserConnector uc = new JPAUserConnector();

	@Before
	public void init() {
		postSet.add(post);
		albumSet.add(album);
		critic.setHandle(handle);
		critic.setJoinDate(date);
		critic.setFirstName(firstName);
		critic.setLastName(lastName);
		critic.setAlbums(albumSet);
		
		album.setCreationDate(date);
		album.setPosts(postSet);
		album.setCritic(critic);
		album.setName(albumName);

		post.setUploadDate(date);
		post.setAlbum(album);
		post.setPath(path);
		post.setTitle(postTitle);

		try {
			uc.insertCritic(critic, hash);
			pc.insertAlbum(album);
			pc.insertPost(post);
		} catch (UserException|PostException|AlbumException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSelectPost() {
		assertEquals(post, pc.selectPost(post.getId()));
	}
	
	@Test
	public void testUpdatePost() {
		post.setTitle("Better title");
		post.setDescription("Really cool");
		post.setRating(5.0f);
		try {
			assertEquals(post, pc.updatePost(post));
		} catch (PostException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testDeletePost() {
		try {
			assertTrue(pc.deletePost(post));
			assertNull(pc.selectPost(post.getId()));
			pc.insertPost(post);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testSelectAlbum() {
		assertEquals(album, pc.selectAlbum(album.getId()));
	}
	
	@Test
	public void testUpdateAlbum() {
		album.setName("new name");
		try {
			assertEquals(album, pc.updateAlbum(album));
		} catch (AlbumException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testDeleteAlbum() {
		try {
			pc.deletePost(post);
			assertTrue(pc.deleteAlbum(album));
			assertNull(pc.selectAlbum(album.getId()));
			pc.insertAlbum(album);
			pc.insertPost(post);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	
	@After
	public void tearDown() {
		try {
			pc.deletePost(post);
			pc.deleteAlbum(album);
		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		uc.deleteCritic(critic);
	}
}
