package com.piccritic.database.feedback;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
	
	UserConnector uc = new JPAUserConnector();
	PostConnector pc = new JPAPostConnector();
	CommentConnector cc = new JPACommentConnector();
	VoteConnector vc = new JPAVoteConnector();
	RatingConnector rc = new JPARatingConnector();
	
	@Before
	public void init() {
		critic.setHandle("tester");
		critic.setFirstName("firstName");
		critic.setLastName("lastName");
		critic.setJoinDate(date);
		critic.setAlbums(albums);
		critic.setComments(criticComments);
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
	
		ratings.add(rating);
		RatingAspects aspectRatings = new RatingAspects();
		aspectRatings.setColor(1.0);
		aspectRatings.setComposition(2.0);
		aspectRatings.setExposure(3.0);
		aspectRatings.setFocus(4.0);
		aspectRatings.setLighting(5.0);
		
		rating.setRatings(aspectRatings);
		
		
		try {
			critic = uc.insertCritic(critic, "hash");
			album.setCritic(critic);
			album = pc.insertAlbum(album);
			
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
		try {
			rc.updateRating(rating);
			assertEquals( rating, rc.selectRating(rating.getId()));
		} catch (RatingException e) {
			e.getLocalizedMessage();
		}
	}
	
	@After
	public void cleanUp() {
		rc.deleteRating(rating);
		
		try {
			pc.deletePost(post);
			pc.deleteAlbum(album);
		} catch (PostException | AlbumException e) {
			e.getLocalizedMessage();
		}
		uc.deleteCritic(critic);
	}
}