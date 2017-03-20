package com.piccritic.website.post;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.piccritic.database.feedback.Comment;
import com.piccritic.database.feedback.CommentConnector;
import com.piccritic.database.feedback.JPACommentConnector;
import com.piccritic.database.feedback.Rating;
import com.piccritic.database.feedback.Vote;
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
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.GridLayoutElement;
import com.vaadin.testbench.elements.PanelElement;
import com.vaadin.testbench.elements.TextAreaElement;

public class AddPostComponentsIT extends PicCriticIT {
	
	Post post = new Post();
	Album album = new Album();
	Critic critic = new Critic();
	Rating rating = new Rating();
	Comment comment = new Comment();
	Vote vote = new Vote();
	
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
	private Set<Rating> ratingSet = new HashSet<Rating>();
	private Set<Comment> commentSet = new HashSet<Comment>();
	//private Set<Vote> voteSet = new HashSet<Vote>();
	
	PostConnector pc = new JPAPostConnector();
	UserConnector uc = new JPAUserConnector();
//	RatingConnector rc = new JPARatingConnector();
	CommentConnector cc = new JPACommentConnector();
//	VoteConnector vc = new JPAVoteConnector();
	
	private void openPostPage(Post post) {
		getDriver().get("http://localhost:8080/#!post/" + post.getPath());
	}
	
	@Before
	public void initthings() {
		
		
		/*
		$(ButtonElement.class).caption("Create Post").first().click();
		$(TextFieldElement.class).caption("Post Title").first().setValue(postTitle);
		$(TextAreaElement.class).caption("Post Description").first().setValue("lul");
	*/	
		critic.setFirstName(firstName);
		critic.setLastName(lastName);
		critic.setHandle(handle);
		critic.setJoinDate(date);
		critic.setAlbums(albumSet);
	
		albumSet.add(album);
		album.setCreationDate(date);
		album.setPosts(postSet);
		album.setName(albumName);
		
		postSet.add(post);
		post.setUploadDate(date);
		post.setTitle(postTitle);
		post.setDescription("description");
		post.setRatings(ratingSet);
		post.setComments(commentSet);
		
		/*ratingSet.add(rating);
		rating.setColor(1d);
		rating.setComposition(2d);
		rating.setExposure(3d);
		rating.setFocus(4d);
		rating.setLighting(5d);
		
		commentSet.add(comment);
		comment.setContent("lul");
		comment.setCritic(critic);
		comment.setCreationDate(date);
		comment.setPost(post);
		comment.setVotes(voteSet);
		
		voteSet.add(vote);
		vote.setComment(comment);
		vote.setCritic(critic);
		vote.setRating(true);*/
		
		try {
			image.createNewFile();
			uc.insertCritic(critic, hash);
			
			album.setCritic(critic);
			pc.insertAlbum(album);
			
			post.setPath(path);
			post.setAlbum(album);
			pc.insertPost(post);
			
		} catch (UserException|PostException|AlbumException|IOException e) {
			e.getMessage();
		}
	}

	@Test
	public void test() throws UserException{
		openPostPage(post);
		String user = "frank";
		if(userService.select(user) == null) {
			createUserUI(user);//
		}
		loginUserUI(user);

		assertTrue($(GridLayoutElement.class).exists());
		assertTrue($(PanelElement.class).caption("Average Rating").exists());
		assertTrue($(PanelElement.class).caption("Rating Category").exists());
		assertTrue($(PanelElement.class).caption("Your Rating").exists());
		
		$(TextAreaElement.class).first().setValue("lul");
		$(ButtonElement.class).caption("Submit Comment").first().click();
		
		assertTrue($(ButtonElement.class).caption("Downvote").exists());
		assertTrue($(ButtonElement.class).caption("Upvote").exists());
		
		/*$(ButtonElement.class).caption("Create Post").first().click();
		$(TextFieldElement.class).caption("Post Title").first().setValue(postTitle);
		$(TextAreaElement.class).caption("Post Description").first().setValue("lul");
		*/

		/*// Adds rating to post
		ratingSet.add(rating);
		rating.setColor(1d);
		rating.setComposition(2d);
		rating.setExposure(3d);
		rating.setFocus(4d);
		rating.setLighting(5d);
		
		// Adds comment to post
		commentSet.add(comment);
		comment.setContent("lul");
		comment.setCritic(critic);
		comment.setCreationDate(date);
		comment.setPost(post);
		comment.setVotes(voteSet);
		
		// Adds Vote to comment
		voteSet.add(vote);
		vote.setComment(comment);
		vote.setCritic(critic);
		vote.setRating(true);
		
		try {
			cc.insertComment(comment);
			rc.insertRating(rating);
			vc.insertVote(vote);

		} catch ( CommentException | RatingException | VoteException e) {
			e.getMessage();
		}
		*/
		// Reload page
		//openPostPage(post);
	}
	
	@After
	public void tearDown() {
		try {
			cc.deleteComment(cc.getComments(post).get(0));
			pc.deletePost(post);
			pc.deleteAlbum(album);
		} catch (PostException | AlbumException e) {
			e.getLocalizedMessage();
		}
		uc.deleteCritic(critic);
	}

}
