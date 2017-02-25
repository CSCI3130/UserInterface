package com.piccritic.compute.post;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.piccritic.database.post.Album;
import com.piccritic.database.post.JPAPostConnector;
import com.piccritic.database.post.Post;
import com.piccritic.database.post.PostException;
import com.piccritic.database.user.Critic;
import com.piccritic.database.user.JPAUserConnector;

public class PostServiceTest {
	
	private Post post;
	private Album album;
	private Set<Post> postSet; 
	private Critic critic;
	private Set<Album> albumSet;
	private JPAUserConnector uc = new JPAUserConnector();
	private JPAPostConnector pc = new JPAPostConnector();
	private PostService ps = new PostService();
	
	@Before
	public void setup() throws Exception{
		album = new Album();
		post = new Post();
		postSet = new HashSet<Post>();  
		postSet.add(post);
		critic = new Critic();
		albumSet = new HashSet<Album>();
		
		critic.setFirstName("firstName");
		critic.setLastName("lastName");
		critic.setJoinDate(new Date(0));
		critic.setHandle("handle");
		albumSet.add(album);

		album.setName("albumName");
		album.setCreationDate(new Date(0));
		album.setCritic(critic);
		
		post.setDescription("Description");
		post.setTitle("Title");
		post.setPath("path");
		post.setUploadDate(new Date(0));
		post.setAlbum(album);
		
		uc.insertCritic(critic, "hash");
		pc.insertAlbum(album);
	}
	
	@Test
	public void testCreatePost() {
		try {
			Post created = ps.createPost(post);
			assertNotNull( created );
			assertNotNull( created.getId());
		} catch(PostException e){
			e.printStackTrace();
			fail("Create Post Failed...");
		}		
	}
	
	@Test
	public void testEditPost(){
		
		try{
			ps.createPost(post);
			post.setDescription("different");
			Post edited = ps.createPost(post);
			assertEquals("different", edited.getDescription());
			
		} catch(PostException e){
			e.printStackTrace();
			fail("Edit Post Failed...");
		}
		
	}
	
	@Test
	public void testDeletePost(){
		try{
			Post created = pc.insertPost(post);
			ps.deletePost(created);			
		} catch(PostException e){
			e.printStackTrace();
			fail("Delete Post Failed...");
		}
	}
	
	@Test 
	public void testGetImageFile(){
		//TODO test later...
	}
		
}
