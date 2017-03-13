package com.piccritic.database.feedback;

import static org.junit.Assert.*;

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

/**
 * CommentConnectorTest.java
 * Created Mar 10, 2017
 */

/**
 * 
 * @author Ryan Lowe
 */
public class CommentConnectorTest {
	
	Critic critic = new Critic();
	Album album = new Album();
	Post post = new Post();
	Comment comment = new Comment();
	
	private Date date = new Date(0);
	
	private Set<Post> posts = new HashSet<Post>();
	private Set<Album> albums = new HashSet<Album>();
	private Set<Comment> criticComments = new HashSet<Comment>();
	private Set<Comment> postComments = new HashSet<Comment>();
	private Set<Rating> ratings = new HashSet<Rating>();
	
	UserConnector uc = new JPAUserConnector();
	PostConnector pc = new JPAPostConnector();
	CommentConnector cc = new JPACommentConnector();
	
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
		
		postComments.add(comment);
		comment.setContent("this is a comment");
		comment.setCreationDate(date);
		criticComments.add(comment);
		comment.setReplies(new HashSet<Comment>());
		comment.setScore(0);
		
		try {
			critic = uc.insertCritic(critic, "hash");
			album.setCritic(critic);
			album = pc.insertAlbum(album);
			post.setAlbum(album);
			posts.add(post);
			post = pc.insertPost(post);
			comment.setPost(post);
			comment.setCritic(critic);
			comment = cc.insertComment(comment);
		} catch (UserException | AlbumException | PostException | CommentException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testSelectComment() {
		assertEquals(comment, cc.selectComment(comment.getId()));
	}
	
	@Test
	public void testUpdateComment() {
		comment.setContent("updated comment");
		try {
			cc.updateComment(comment);
			assertEquals(comment, cc.selectComment(comment.getId()));
		} catch (CommentException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@After
	public void tearDown() {
		cc.deleteComment(comment);
		try {
			pc.deletePost(post);
			pc.deleteAlbum(album);
		} catch (PostException | AlbumException e) {
			e.getLocalizedMessage();
		}
		uc.deleteCritic(critic);
	}
	
}
