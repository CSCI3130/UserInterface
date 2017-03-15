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
import com.piccritic.database.feedback.JPAVoteConnector;


/**
 * 
 * @author Jonathan Ignacio <br> Ryan Lowe
 */
public class VoteConnectorTest {
	
	Critic critic = new Critic();
	Album album = new Album();
	Post post = new Post();
	Comment comment = new Comment();
	Vote vote = new Vote();
	
	private Date date = new Date(0);
	
	private Set<Post> posts = new HashSet<Post>();
	private Set<Album> albums = new HashSet<Album>();
	private Set<Comment> criticComments = new HashSet<Comment>();
	private Set<Comment> postComments = new HashSet<Comment>();
	private Set<Rating> ratings = new HashSet<Rating>();
	private Set<Vote> criticVotes = new HashSet<Vote>();
	private Set<Vote> commentVotes = new HashSet<Vote>();
	
	UserConnector uc = new JPAUserConnector();
	PostConnector pc = new JPAPostConnector();
	CommentConnector cc = new JPACommentConnector();
	VoteConnector vc = new JPAVoteConnector();
	
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
		
		vote.setRating(true);  //true for upvote
		criticVotes.add(vote);
		commentVotes.add(vote);
		
		
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
			
			vote.setCritic(critic);
			vote.setComment(comment);
			vote = vc.insertVote(vote);
		} catch (VoteException | UserException | AlbumException | PostException | CommentException e) {
			fail(e.getLocalizedMessage());
		}
	
	}
	
	@Test
	public void testSelectVote() {
		assertEquals(vote, vc.selectVote(vote.getId()));
	}
	
	@Test
	public void testGetVoteId() {
		assertEquals(vote.getId(), vc.getVoteId(critic, comment));
	}
	
	@Test
	public void testGetVoteIdNull() {
		try {
			vc.deleteVote(vote);
			assertEquals(null, vc.getVoteId(critic, comment));
			vc.insertVote(vote);
		} catch (VoteException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testUpdateVote() {
		vote.setRating(false);
		try {
			int score = vc.selectVote(vote.getId()).getComment().getScore();
			vc.updateVote(vote);
			assertEquals(vote, vc.selectVote(vote.getId()));
			assertEquals(cc.selectComment(comment.getId()), (vc.selectVote(vote.getId()).getComment()));
			assertEquals(score - 1, vc.selectVote(vote.getId()).getComment().getScore());
		} catch (VoteException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@After
	public void cleanUp() {
		vc.deleteVote(vote);
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
