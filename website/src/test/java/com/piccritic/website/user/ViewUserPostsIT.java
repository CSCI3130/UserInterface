package com.piccritic.website.user;

import com.piccritic.database.license.AttributionLicense;
import com.piccritic.database.license.JPALicenseConnector;
import com.piccritic.database.post.Album;
import com.piccritic.database.post.AlbumException;
import com.piccritic.database.post.JPAPostConnector;
import com.piccritic.database.post.Post;
import com.piccritic.database.post.PostConnector;
import com.piccritic.database.post.PostException;
import com.piccritic.database.user.Critic;
import com.piccritic.database.user.JPAUserConnector;
import com.piccritic.database.user.UserConnector;
import com.piccritic.database.user.UserException;
import com.piccritic.website.PicCriticIT;
import com.vaadin.testbench.elements.ImageElement;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class contains JUnit tests, which are run using Vaadin TestBench 4.
 *
 * To run this, first get an evaluation license from
 * https://vaadin.com/addon/vaadin-testbench and follow the instructions at
 * https://vaadin.com/directory/help/installing-cval-license to install it.
 *
 * Once the license is installed, you can run this class as a JUnit test.
 */
public class ViewUserPostsIT extends PicCriticIT {
	
	Post post = new Post();
	Album album = new Album();
	Critic critic = new Critic();
	
	private String firstName = "firstName";
	private String handle = "handle";
	private String lastName = "lastName";
	private String hash = "hash";
	private String albumName = "album";
	private String postTitle = "title";
	private String path = "path";
	private Date date = new Date(0);
	private File image = new File(path);
	
	private Set<Post> postSet = new HashSet<Post>();
	private Set<Album> albumSet = new HashSet<Album>();
	
	JPALicenseConnector lc = new JPALicenseConnector();
	PostConnector pc = new JPAPostConnector();
	UserConnector uc = new JPAUserConnector();
	
	private void openUserPage(String user) {
		getDriver().get("http://localhost:8080/#!user/" + user);
	}
	
	@Before
	public void initthings() {
		postSet.add(post);
		albumSet.add(album);
		critic.setHandle(handle);
		critic.setJoinDate(date);
		critic.setFirstName(firstName);
		critic.setLastName(lastName);
		critic.setAlbums(albumSet);
		critic.setLicense(new AttributionLicense());
		
		album.setCreationDate(date);
		album.setPosts(postSet);
		album.setName(albumName);

		post.setUploadDate(date);
		post.setTitle(postTitle);
		post.setDescription("description");
		post.setLicense(new AttributionLicense());

		try {
			image.createNewFile();
			uc.insertCritic(critic, hash);
			album.setCritic(critic);
			pc.insertAlbum(album);
			post.setPath(path);
			post.setAlbum(album);
			pc.insertPost(post);
		} catch (UserException|PostException|AlbumException|IOException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void postExists() throws UserException {
		openUserPage(critic.getHandle());
		hasUserException();
		assertTrue($(ImageElement.class).caption(post.getTitle()).exists());
		$(ImageElement.class).first().click();
		assertTrue(getDriver().getCurrentUrl().contains("post"));
	}
	
	@After
	public void tearDown() {
		try {
			image.delete();
			pc.deletePost(post);
			pc.deleteAlbum(album);
		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		uc.deleteCritic(critic);
	}

}
