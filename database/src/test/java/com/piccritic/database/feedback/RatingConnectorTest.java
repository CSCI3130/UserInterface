package com.piccritic.database.feedback;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.piccritic.database.license.AttributionLicense;
import com.piccritic.database.license.JPALicenseConnector;
import com.piccritic.database.post.Album;
import com.piccritic.database.post.AlbumConnector;
import com.piccritic.database.post.AlbumException;
import com.piccritic.database.post.JPAAlbumConnector;
import com.piccritic.database.post.JPAPostConnector;
import com.piccritic.database.post.Post;
import com.piccritic.database.post.PostConnector;
import com.piccritic.database.post.PostException;
import com.piccritic.database.user.Critic;
import com.piccritic.database.user.JPAUserConnector;
import com.piccritic.database.user.UserConnector;
import com.piccritic.database.user.UserException;
/**
 * 
 * @author Frank Bosse
 *
 */
public class RatingConnectorTest {
	Critic critic = new Critic();
	Album album = new Album();
	Post post = new Post();
	Comment comment = new Comment();
	Vote vote = new Vote();
	Rating rating = new Rating();
	
	private Date date = new Date(0);
	
	private Set<Post> posts = new HashSet<Post>();
	private Set<Album> albums = new HashSet<Album>();
	private Set<Comment> criticComments = new HashSet<Comment>();
	private Set<Comment> postComments = new HashSet<Comment>();
	private Set<Rating> ratings = new HashSet<Rating>();
	private AttributionLicense license = new AttributionLicense();
	
	JPALicenseConnector lc = new JPALicenseConnector();
	UserConnector uc = new JPAUserConnector();
	PostConnector pc = new JPAPostConnector();
	AlbumConnector ac = new JPAAlbumConnector();
	CommentConnector cc = new JPACommentConnector();
	VoteConnector vc = new JPAVoteConnector();
	RatingConnector rc = new JPARatingConnector();
	
	@Before
	public void init() {
		critic.setHandle("testerRating");
		critic.setFirstName("firstName");
		critic.setLastName("lastName");
		critic.setJoinDate(date);
		critic.setAlbums(albums);
		critic.setComments(criticComments);
		critic.setLicense(license);
		critic.setRatings(ratings);

		albums.add(album);
		album.setCreationDate(date);
		album.setName("albumName");
		album.setPosts(posts);
		
		post.setComments(postComments);
		post.setTitle("My photo");
		post.setUploadDate(date);
		post.setDescription("description");
		post.setPath("/path");
		post.setRatings(ratings);
		post.setLicense(license);
	
		ratings.add(rating);
		rating.setColor(1.0);
		rating.setComposition(2.0);
		rating.setExposure(3.0);
		rating.setFocus(4.0);
		rating.setLighting(5.0);
		
		try {
			critic = uc.insertCritic(critic, "hash");
			album.setCritic(critic);
			album = ac.insertAlbum(album);
			
			post.setAlbum(album);
			posts.add(post);
			post = pc.insertPost(post);
			
			rating.setCritic(critic);
			rating.setPost(post);
			rating = rc.insertRating(rating);
			post.setRatings(ratings);
		
			
		} catch (UserException | AlbumException | PostException | RatingException e) {
			e.getLocalizedMessage();
		}
	}
	
	@Test
	public void testSelectRating() {
		assertEquals(rating, rc.selectRating(rating.getId()));
	}
	
	@Test
	public void testUpdateRating() {
		rating.setComposition(-1d);
		try {
			rc.updateRating(rating);
			assertEquals( rating, rc.selectRating(rating.getId()));
		} catch (RatingException e) {
			e.getLocalizedMessage();
		}
	}
	
	@Test
	public void testDeleteRating() {
		Long id = rating.getId();
		assertTrue(rc.deleteRating(rating));
		rating.setId(null);
		assertNull(rc.selectRating(id));
		
		try {
			rating = rc.insertRating(rating);
		} catch (RatingException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void TestQueryRating() {
	  assertNotNull(rc.queryRating(post, critic));
	  /*
	  Post tmpPost = new Post();
	  assertNull(rc.queryRating(tmpPost, critic));
	  Critic tmpCritic = new Critic();
	  assertNull(rc.queryRating(tmpPost, tmpCritic));
	  assertNull(rc.queryRating(post, tmpCritic));
	  */
	}
	
	@After
	public void cleanUp() {
		rc.deleteRating(rating);
		
		try {
			pc.deletePost(post);
			ac.deleteAlbum(album);
		} catch (PostException | AlbumException e) {
			e.getLocalizedMessage();
		}
		uc.deleteCritic(critic);
	}
}
