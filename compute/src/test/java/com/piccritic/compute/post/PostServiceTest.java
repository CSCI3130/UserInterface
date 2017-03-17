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

import com.piccritic.database.license.AttributionLicense;
import com.piccritic.database.post.Album;
import com.piccritic.database.post.AlbumException;
import com.piccritic.database.post.JPAPostConnector;
import com.piccritic.database.post.Post;
import com.piccritic.database.post.PostException;
import com.piccritic.database.user.Critic;
import com.piccritic.database.user.JPAUserConnector;
import com.piccritic.database.user.UserException;

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
		critic.setLicense(new AttributionLicense());
		critic.setHandle("handle");
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
		
		uc.insertCritic(critic, "hash");
		pc.insertAlbum(album);
		post.setPath("path");
	}
	
	@Test
	public void testCreatePost() {
		try {
			Post created = ps.createPost(post);
			assertNotNull( created );
			assertNotNull( created.getUploadDate());
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
			ps.deletePost(created);
			pc.insertPost(post);
		} catch(PostException | AlbumException e){
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test 
	public void testGetImageFile(){
		//TODO test later...
		try {
			ps.createPost(post);
		} catch (PostException | AlbumException e) {
		}
	}
		
	@After
	public void tearDown() {
		try {
			pc.deletePost(post);
			album.setPosts(null);
			pc.updateAlbum(album);
			pc.deleteAlbum(album);
			critic.setAlbums(null);
			uc.updateCritic(critic);
			uc.deleteCritic(critic);
		} catch (PostException|AlbumException | UserException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}
}
