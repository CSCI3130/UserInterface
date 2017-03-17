/**
 * FeedbackServiceTest.java
 * Created Mar 14, 2017
 */

package com.piccritic.compute.feedback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.piccritic.database.feedback.Comment;
import com.piccritic.database.feedback.CommentConnector;
import com.piccritic.database.feedback.CommentException;
import com.piccritic.database.feedback.JPACommentConnector;
import com.piccritic.database.feedback.JPAVoteConnector;
import com.piccritic.database.feedback.Rating;
import com.piccritic.database.feedback.Vote;
import com.piccritic.database.feedback.VoteConnector;
import com.piccritic.database.feedback.VoteException;
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
 * Tests for FeedbackService.java
 * 
 * @author Amelia Stead<br>Jonathan Ignacio
 */
public class FeedbackServiceTest {

	Critic critic = new Critic();
	Critic voter = new Critic();
	Album album = new Album();
	Post post = new Post();
	Comment comment = new Comment();
	Vote vote1 = new Vote();
	Vote vote2 = new Vote();
	
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
	
	FeedbackService fs = FeedbackService.createService();
	
	@Before
	public void init() {
		critic.setHandle("tester");
		critic.setFirstName("firstName");
		critic.setLastName("lastName");
		critic.setJoinDate(date);
		critic.setAlbums(albums);
		critic.setComments(criticComments);
		critic.setRatings(ratings);
		
		voter.setHandle("voter");
		voter.setFirstName("firstName");
		voter.setLastName("lastName");
		voter.setJoinDate(date);
		voter.setAlbums(null);
		voter.setComments(null);
		voter.setRatings(null);

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
		
		vote1.setRating(true);
		vote2.setRating(true);
		
		try {
			critic = uc.insertCritic(critic, "hash");
			voter = uc.insertCritic(voter, "hash");
			album.setCritic(critic);
			album = pc.insertAlbum(album);
			post.setAlbum(album);
			posts.add(post);
			post = pc.insertPost(post);comment.setPost(post);
			comment.setCritic(critic);
			vote1.setCritic(critic);
			vote1.setComment(comment);
			vote2.setCritic(voter);
			vote2.setComment(comment);
			comment = fs.insertComment(comment);
		} catch (UserException | AlbumException | PostException | CommentException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testInsertComment() {
		try {
			cc.deleteComment(comment);
			comment = fs.insertComment(comment);
			assertEquals(comment, cc.selectComment(comment.getId()));
		} catch (CommentException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testGetComments() {
		try {
			assertEquals(fs.getComments(post).size(), 1);
			cc.deleteComment(comment);
			assertEquals(fs.getComments(post).size(), 0);
			fs.insertComment(comment);
		} catch (PostException | CommentException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testGetVote() {
		try {
			Vote v = fs.insertVote(vote1);
			assertEquals(v, fs.getVote(critic, comment));
			vc.deleteVote(v);
		} catch (VoteException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testInsertVote() {
		try {
			Vote v = fs.insertVote(vote1);
			assertEquals(v, vc.selectVote(v.getId()));
			vc.deleteVote(v);
		} catch (VoteException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testUpdateVote() {
		try {
			Vote v = fs.insertVote(vote1);
			vote1.setRating(false);
			v = fs.insertVote(vote1);
			assertEquals(v, vc.selectVote(v.getId()));
			vc.deleteVote(v);
		} catch (VoteException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testDeleteVote() {
		try {
			Vote v = fs.insertVote(vote1);
			assertEquals(true, vc.deleteVote(v));
		} catch (VoteException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testGetScore() {
		try {
			vote1.setRating(true);
			Vote v = fs.insertVote(vote1);
			assertEquals(1, fs.getScore(comment));
			fs.deleteVote(v);
			assertEquals(0, fs.getScore(comment));
		} catch (VoteException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testGetCriticCommentReputation() {
		try {
			//should have votes equal to upperscorelimit to properly reflect reputation gain
			Vote v1 = fs.insertVote(vote1);
			Vote v2 = fs.insertVote(vote2);
			assertEquals(fs.getRepGain(), fs.getCriticCommentReputation(critic)); //gained rep
			fs.deleteVote(v1);
			fs.deleteVote(v2);
			assertEquals(0, fs.getCriticCommentReputation(critic));
		} catch (VoteException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@After
	public void tearDown() {
		try {
			cc.deleteComment(comment);
			pc.deletePost(post);
			pc.deleteAlbum(album);
		} catch (PostException | AlbumException e) {
			e.getLocalizedMessage();
		}
		uc.deleteCritic(critic);
	}
}
