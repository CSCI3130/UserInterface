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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.piccritic.database.feedback.Comment;
import com.piccritic.database.feedback.Rating;
import com.piccritic.database.license.AttributionNonCommercialLicense;
import com.piccritic.database.license.JPALicenseConnector;
import com.piccritic.database.license.License;
import com.piccritic.database.license.LicenseConnector;
import com.piccritic.database.post.PostConnector.PostSortOption;
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
	Comment comments[] = new Comment[10];

	private String firstName = "firstName";
	private String handle = "handlePCT";
	private String lastName = "lastName";
	private String hash = "hash";
	private String albumName = "album";
	private String postTitle = "title";
	private String path = "/path";
	private String path1 = "/path1";
	private String path2 = "/path2";
	private String path3 = "/path3";
	private String path4 = "/path4";
	private long day = 24*3600000;
	private Date date = new Date(0);
	private Date date1 = new Date(day);
	private Date date2 = new Date(2*day);
	private Date date3 = new Date(3*day);
	private Date date4 = new Date(4*day);

	private Set<Post> postSet = new HashSet<Post>();
	private Set<Album> albumSet = new HashSet<Album>();
	private Set<Comment> commentSet = new HashSet<Comment>();
	private Set<Rating> ratingSet = new HashSet<Rating>();

	PostConnector pc = new JPAPostConnector();
	AlbumConnector ac = new JPAAlbumConnector();
	UserConnector uc = new JPAUserConnector();
	LicenseConnector lc = new JPALicenseConnector();

	@Before
	public void init() {
		new JPALicenseConnector();
		
		postSet.add(post);
		postSet.add(post1);
		postSet.add(post2);
		postSet.add(post3);
		postSet.add(post4);
		albumSet.add(album);
		critic.setHandle(handle);
		critic.setJoinDate(date);
		critic.setFirstName(firstName);
		critic.setLicense(new AttributionNonCommercialLicense());
		critic.setLastName(lastName);
		critic.setAlbums(albumSet);
		critic.setComments(commentSet);
		
		album.setCreationDate(date);
		album.setPosts(postSet);
		album.setName(albumName);

		post.setUploadDate(date4);
		post.setTitle("Epsilon");
		post.setDescription("description");
		post.setRatings(ratingSet);
		post.setComments(commentSet);
		post.setLicense(lc.selectLicense(License.ATTRIBUTION_NON_COMMERCIAL_NO_DERIVS));

		post1.setUploadDate(date2);
		post1.setTitle("Delta");
		post1.setDescription("description");
		post1.setLicense(lc.selectLicense(License.ATTRIBUTION_NON_COMMERCIAL));
		
		post2.setUploadDate(date);
		post2.setTitle("Cookie");
		post2.setDescription("description");
		post2.setLicense(lc.selectLicense(License.ATTRIBUTION_NO_DERIVS));
		
		post3.setUploadDate(date1);
		post3.setTitle("Beta");
		post3.setDescription("description");
		post3.setLicense(lc.selectLicense(License.ATTRIBUTION_NON_COMMERCIAL_SHAREALIKE));
		
		post4.setUploadDate(date3);
		post4.setTitle("Alpha");
		post4.setDescription("description");
		post4.setLicense(lc.selectLicense(License.ATTRIBUTION_SHAREALIKE));
		try {
			uc.insertCritic(critic, hash);
			album.setCritic(critic);
			ac.insertAlbum(album);
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
		assertEquals(album, ac.selectAlbum(album.getId()));
	}
	
	@Test
	public void testUpdateAlbum() {
		album.setName("new name");
		try {
			assertEquals(album, ac.updateAlbum(album));
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
			assertTrue(ac.deleteAlbum(album));
			assertNull(ac.selectAlbum(album.getId()));
			ac.insertAlbum(album);
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
	public void testGetPostsFromAlbum() {
		try {
			assertEquals(5, pc.getPosts(critic).size());
			pc.deletePost(post);
			assertEquals(4, pc.getPosts(critic).size());
			pc.insertPost(post);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testGetPostNumber() {
		try {
			assertEquals(1, pc.getPosts(1).size());
			assertEquals(5, pc.getPosts(5).size());
			assertEquals(5, pc.getPosts(25).size());
		} catch (PostException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testGetPostSortTitle() {
		try {
			List<Post> posts = pc.getPosts(5, PostSortOption.TITLE);
			assertEquals(post4, posts.get(0));
			assertEquals(post3, posts.get(1));
			assertEquals(post2, posts.get(2));
			assertEquals(post1, posts.get(3));
			assertEquals(post, posts.get(4));
		} catch (PostException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testGetPostSortDate() {
		try {
			List<Post> posts = pc.getPosts(5, PostSortOption.UPLOAD_DATE);
			assertEquals(post2, posts.get(0));
			assertEquals(post3, posts.get(1));
			assertEquals(post1, posts.get(2));
			assertEquals(post4, posts.get(3));
			assertEquals(post, posts.get(4));
		} catch (PostException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testGetPostSortLicense() {
		try {
			List<Post> posts = pc.getPosts(5, PostSortOption.LICENSE);
			
			assertEquals(post2, posts.get(0));
			assertEquals(post1, posts.get(1));
			assertEquals(post, posts.get(2));
			assertEquals(post3, posts.get(3));
			assertEquals(post4, posts.get(4));
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
			ac.deleteAlbum(album);
		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		uc.deleteCritic(critic);
	}
}
