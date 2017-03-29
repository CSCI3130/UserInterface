package com.piccritic.compute.post;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.piccritic.compute.MasterConnector;
import com.piccritic.compute.feedback.FeedbackService;
import com.piccritic.compute.feedback.FeedbackServiceInterface;
import com.piccritic.database.feedback.Comment;
import com.piccritic.database.feedback.CommentException;
import com.piccritic.database.feedback.VoteException;
import com.piccritic.database.license.AttributionLicense;
import com.piccritic.database.post.Album;
import com.piccritic.database.post.AlbumConnector;
import com.piccritic.database.post.AlbumException;
import com.piccritic.database.post.Post;
import com.piccritic.database.post.PostConnector;
import com.piccritic.database.post.PostException;
import com.piccritic.database.user.Critic;
import com.piccritic.database.user.UserConnector;
import com.piccritic.database.user.UserException;

/**
 * 
 * @author Ryan Lowe <br>Damian Robichaud<br>Jonathan Ignacio
 */
public class PostServiceTest {
	
	private Post post;
	private Album album;
	private Set<Post> postSet; 
	private Critic critic;
	private Set<Album> albumSet;
	private Comment comment;
	private Set<Comment> criticComments = new HashSet<Comment>();
	private Set<Comment> postComments = new HashSet<Comment>();
	
	private UserConnector uc;
	private PostConnector pc;
	private AlbumConnector ac;
	
	private PostService ps = new PostService();
	private FeedbackServiceInterface fs = FeedbackService.createService();
	
	@Before
	public void setup() throws Exception{
		MasterConnector.init();
		uc = MasterConnector.userConnector;
		pc = MasterConnector.postConnector;
		ac = MasterConnector.albumConnector;
		
		album = new Album();
		post = new Post();
		postSet = new HashSet<Post>();  
		postSet.add(post);
		critic = new Critic();
		albumSet = new HashSet<Album>();
		comment = new Comment();
		
		critic.setFirstName("firstName");
		critic.setLastName("lastName");
		critic.setJoinDate(new Date(0));
		critic.setLicense(new AttributionLicense());
		critic.setHandle("handlePST");
		critic.setComments(criticComments);
		albumSet.add(album);

		album.setName("albumName");
		album.setCreationDate(new Date(0));
		album.setCritic(critic);
		album.setPosts(postSet);
		
		post.setDescription("Description");
		post.setTitle("Title");
		post.setUploadDate(null);
		post.setLicense(new AttributionLicense());
		post.setAlbum(album);
		post.setComments(postComments);
		
		postComments.add(comment);
		comment.setContent("this is a comment");
		comment.setCreationDate(new Date(0));
		criticComments.add(comment);
		comment.setReplies(new HashSet<Comment>());
		comment.setScore(0);
		
		critic = uc.insertCritic(critic, "hash");
		album = ac.insertAlbum(album);
		post.setPath("path");
	}
	
	@Test
	public void testCreatePost() {
		try {
			ps.createPost(post);
			assertNotNull( post );
			assertNotNull( post.getUploadDate());
		} catch(PostException | AlbumException e){
			fail(e.getLocalizedMessage());
		}		
	}
	
	@Test
	public void testEditPost(){
		
		try{
			ps.createPost(post);
			post.setDescription("different");
			Post edited = ps.createPost(post);
			assertEquals("different", edited.getDescription());
			
		} catch(PostException | AlbumException e){
			fail(e.getLocalizedMessage());
		}
		
	}
	
	@Test
	public void testDeletePost(){
		try{
			Post created = ps.createPost(post);
			comment.setPost(created);
			comment.setCritic(critic);
			comment = fs.insertComment(comment);
			ps.deletePost(created);
			post.setComments(new HashSet<Comment>());
			post.setUploadDate(null);
			ps.createPost(post);
		} catch(PostException | AlbumException | CommentException | VoteException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test 
	public void testGetImageFile(){
		//TODO test later...
		try {
			post = ps.createPost(post);
		} catch (PostException | AlbumException e) {
			fail(e.getLocalizedMessage());
		}
	}
		
	@After
	public void tearDown() {
		try {
			pc.deletePost(post);
			album.setPosts(null);
			ac.updateAlbum(album);
			ac.deleteAlbum(album);
			critic.setAlbums(null);
			uc.updateCritic(critic);
			uc.deleteCritic(critic);
		} catch (PostException|AlbumException|UserException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}
}
