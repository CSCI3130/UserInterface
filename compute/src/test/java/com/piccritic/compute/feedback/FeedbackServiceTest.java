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

import com.piccritic.compute.MasterConnector;
import com.piccritic.database.feedback.Comment;
import com.piccritic.database.feedback.CommentConnector;
import com.piccritic.database.feedback.CommentException;
import com.piccritic.database.feedback.JPACommentConnector;
import com.piccritic.database.feedback.JPARatingConnector;
import com.piccritic.database.feedback.JPAVoteConnector;
import com.piccritic.database.feedback.Rating;
import com.piccritic.database.feedback.RatingConnector;
import com.piccritic.database.feedback.RatingException;
import com.piccritic.database.feedback.Vote;
import com.piccritic.database.feedback.VoteConnector;
import com.piccritic.database.feedback.VoteException;
import com.piccritic.database.license.AttributionLicense;
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
	Rating rating1 = new Rating();
	Rating rating2 = new Rating();
	
	private Date date = new Date(0);
	
	private Set<Post> posts = new HashSet<Post>();
	private Set<Album> albums = new HashSet<Album>();
	private Set<Comment> criticComments = new HashSet<Comment>();
	private Set<Comment> postComments = new HashSet<Comment>();
	private Set<Rating> ratings = new HashSet<Rating>();
	private AttributionLicense license = new AttributionLicense();
	
	//private JPALicenseConnector lc = new JPALicenseConnector(); //commented out because unused
	private UserConnector uc;
	private PostConnector pc;
	private AlbumConnector ac;
	private CommentConnector cc;
	private VoteConnector vc;
	private RatingConnector rc;
	
	private FeedbackServiceInterface fs = FeedbackService.createService();
	
	@Before
	public void init() {
		MasterConnector.init();
		uc = MasterConnector.userConnector;
		pc = MasterConnector.postConnector;
		ac = MasterConnector.albumConnector;
		cc = MasterConnector.commentConnector;
		vc = MasterConnector.voteConnector;
		rc = MasterConnector.ratingConnector;
		
		critic.setHandle("tester");
		critic.setFirstName("firstName");
		critic.setLastName("lastName");
		critic.setJoinDate(date);
		critic.setAlbums(albums);
		critic.setComments(criticComments);
		critic.setLicense(license);
		critic.setRatings(ratings);
		
		voter.setHandle("voter");
		voter.setFirstName("firstName");
		voter.setLastName("lastName");
		voter.setJoinDate(date);
		voter.setAlbums(albums);
		voter.setComments(criticComments);
		voter.setRatings(ratings);
		voter.setLicense(license);

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
		
		postComments.add(comment);
		comment.setContent("this is a comment");
		comment.setCreationDate(date);
		criticComments.add(comment);
		comment.setReplies(new HashSet<Comment>());
		comment.setScore(0);
		
		vote1.setRating(true);
		vote2.setRating(true);
		
		ratings.add(rating1);
		rating1.setColor(1d);
		rating1.setComposition(2d);
		rating1.setExposure(3d);
		rating1.setFocus(4d);
		rating1.setLighting(5d);
		
		ratings.add(rating2);
		rating2.setColor(1d);
		rating2.setComposition(1d);
		rating2.setExposure(1d);
		rating2.setFocus(1d);
		rating2.setLighting(1d);
		
		try {
			critic = uc.insertCritic(critic, "hash");
			voter = uc.insertCritic(voter, "hash");

			album.setCritic(critic);
			album = ac.insertAlbum(album);
			
			post.setAlbum(album);
			posts.add(post);
			post = pc.insertPost(post);comment.setPost(post);
			comment.setCritic(critic);
			
			vote1.setCritic(critic);
			vote1.setComment(comment);
			vote2.setCritic(voter);
			vote2.setComment(comment);
			comment = fs.insertComment(comment);
			
			rating1.setCritic(critic);
			rating1.setPost(post);
			rating1 = fs.insertRating(rating1);
			
			rating2.setCritic(voter);
			rating2.setPost(post);
			rating2 = fs.insertRating(rating2);
			post.setRatings(ratings);
			
		} catch (UserException | AlbumException | PostException | CommentException | RatingException e) {
			fail(e.getLocalizedMessage());
		}
	}
	

	@Test
	public void testInsertRating() {
		try {
			rc.deleteRating(rating1);
			rating1 = fs.insertRating(rating1);
			assertEquals(rating1, rc.selectRating(rating1.getId()));
		} catch (RatingException e){
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testDeleteRating() throws RatingException {
		assertTrue(fs.deleteRating(rating1));
		fs.insertRating(rating1);
	}
	
	@Test
	public void testUpdateRating() throws RatingException {
		rating1.setColor(-1d);
		fs.updateRating(rating1);
		assertEquals(rating1, fs.selectRating(rating1.getId()));
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
	
	@Test
	public void testGetCriticPostReputation() {
		try {
			assertEquals(fs.getRepLoss(), fs.getCriticPostReputation(critic)); //current average is below 2.5
			rc.deleteRating(rating2);
			assertEquals(fs.getRepGain(), fs.getCriticPostReputation(critic)); //should go above 2.5
			rc.insertRating(rating2);
		} catch (RatingException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testCalculateReputation() {
		try {
			Vote v1 = fs.insertVote(vote1);
			Vote v2 = fs.insertVote(vote2);
			assertEquals(fs.getRepGain() + fs.getRepLoss()*fs.getRatingWeight(), fs.calculateReputation(critic)); //rep gain from votes
			fs.deleteVote(v1);
			fs.deleteVote(v2);
			assertEquals(fs.getRepLoss()*fs.getRatingWeight(), fs.calculateReputation(critic)); //only rep loss from rating remain
		} catch (VoteException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@After
	public void tearDown() {
		rc.deleteRating(rating1);
		rc.deleteRating(rating2);
		try {
			cc.deleteComment(comment);
			pc.deletePost(post);
			ac.deleteAlbum(album);
		} catch (PostException | AlbumException e) {
			e.getLocalizedMessage();
		}
		uc.deleteCritic(critic);
		uc.deleteCritic(voter);
	}
}
