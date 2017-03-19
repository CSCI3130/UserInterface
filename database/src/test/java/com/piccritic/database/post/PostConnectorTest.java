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
 * @author Damien Robichaud<br>
 * 			Ryan Lowe<br>
 * 			ian-dawson
 */
public class PostConnectorTest {
	
	Post post = new Post();
	Post post1 = new Post();
	Post post2 = new Post();
	Post post3 = new Post();
	Post post4 = new Post();
	Album album = new Album();
	Critic critic = new Critic();

	private String firstName = "firstName";
	private String handle = "handle";
	private String lastName = "lastName";
	private String hash = "hash";
	private String albumName = "album";
	private String postTitle = "title";
	private String path = "/path";
	private String path1 = "/path1";
	private String path2 = "/path2";
	private String path3 = "/path3";
	private String path4 = "/path4";
	private Date date = new Date(0);

	private Set<Post> postSet = new HashSet<Post>();
	private Set<Album> albumSet = new HashSet<Album>();

	PostConnector pc = new JPAPostConnector();
	UserConnector uc = new JPAUserConnector();

	@Before
	public void init() {
		postSet.add(post);
		postSet.add(post1);
		postSet.add(post2);
		postSet.add(post3);
		postSet.add(post4);
		albumSet.add(album);
		critic.setHandle(handle);
		critic.setJoinDate(date);
		critic.setFirstName(firstName);
		critic.setLastName(lastName);
		critic.setAlbums(albumSet);
		
		album.setCreationDate(date);
		album.setPosts(postSet);
		album.setName(albumName);

		post.setUploadDate(date);
		post.setTitle(postTitle);
		post.setDescription("description");
		post1.setUploadDate(date);
		post1.setTitle(postTitle);
		post1.setDescription("description");
		
		post2.setUploadDate(date);
		post2.setTitle(postTitle);
		post2.setDescription("description");
		
		post3.setUploadDate(date);
		post3.setTitle(postTitle);
		post3.setDescription("description");
		
		post4.setUploadDate(date);
		post4.setTitle(postTitle);
		post4.setDescription("description");
		try {
			uc.insertCritic(critic, hash);
			album.setCritic(critic);
			pc.insertAlbum(album);
			post.setPath(path);
			post1.setPath(path1);
			post2.setPath(path2);
			post3.setPath(path3);
			post4.setPath(path4);
			post.setAlbum(album);
			post1.setAlbum(album);
			post2.setAlbum(album);
			post3.setAlbum(album);
			post4.setAlbum(album);
			pc.insertPost(post);
			pc.insertPost(post1);
			pc.insertPost(post2);
			pc.insertPost(post3);
			pc.insertPost(post4);
		} catch (UserException|PostException|AlbumException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSelectPost() {
		assertEquals(post, pc.selectPost(post.getPath()));
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
			assertNull(pc.selectPost(post.getPath()));
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
			pc.deletePost(post1);
			pc.deletePost(post2);
			pc.deletePost(post3);
			pc.deletePost(post4);
			assertTrue(pc.deleteAlbum(album));
			assertNull(pc.selectAlbum(album.getId()));
			pc.insertAlbum(album);
			pc.insertPost(post);
			pc.insertPost(post1);
			pc.insertPost(post2);
			pc.insertPost(post3);
			pc.insertPost(post4);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	
	@Test
	public void testGetPosts() {
		try {
			assertEquals(1, pc.getPosts(1).size());
			assertEquals(5, pc.getPosts(5).size());
			assertEquals(5, pc.getPosts(25).size());
		} catch (PostException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	
	@After
	public void tearDown() {
		try {
			pc.deletePost(post);
			pc.deletePost(post1);
			pc.deletePost(post2);
			pc.deletePost(post3);
			pc.deletePost(post4);
			pc.deleteAlbum(album);
		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		uc.deleteCritic(critic);
	}
}
