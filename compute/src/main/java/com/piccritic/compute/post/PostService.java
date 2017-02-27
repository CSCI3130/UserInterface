package com.piccritic.compute.post;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Base64;
import java.util.Calendar;
import java.util.Random;
import java.util.Set;

import org.hibernate.Hibernate;

import com.piccritic.database.post.Album;
import com.piccritic.database.post.JPAPostConnector;
import com.piccritic.database.post.Post;
import com.piccritic.database.post.PostConnector;
import com.piccritic.database.post.PostException;
import com.piccritic.database.user.Critic;
import com.piccritic.database.user.JPAUserConnector;	
/**
 * This class implements the PostServiceInterface.
 * 
 * @author Amir Abbasnejad 
 * @author Rhianna Goguen
 */
public class PostService implements PostServiceInterface {
	
	public static final String USERS_DIR = "users";

	static PostConnector pc = new JPAPostConnector();
	
	public File getImageFile(String handle) {
		Path p0 = Paths.get(USERS_DIR, handle);
		File directory = p0.toFile();
		File[] flist = directory.listFiles();

		String newFileName="";
		boolean used = true;
		while(used) {
			newFileName = getRandomName();
			used = false;
		    for (File f : flist) {
		        if (f.getName().equals(newFileName)) {
		            used = true;
		        }
		    }
		}
		Path p1 = Paths.get(USERS_DIR, handle, newFileName);
		return p1.toFile();
	}

	private String getRandomName() {
		byte[] rbytes = new byte[24];
		Random random = new Random();
		random.nextBytes(rbytes);
		return Base64.getUrlEncoder().encodeToString(rbytes);

	}

	public Post createPost(Post post) throws PostException{
		
		if (post.getUploadDate() == null) {
			post.setUploadDate( new Date(Calendar.getInstance().getTime().getTime()) );
			return pc.insertPost(post) ;
		}	
		return pc.updatePost(post) ;
	}

	public boolean deletePost(Post post) throws PostException{
		if (post == null) {
			throw new PostException("Cannot delete null post");
		}
		File image = new File(post.getPath());
		image.delete();
		return pc.deletePost(post);		
	}

	@Override
	public Album getDefaultAlbum(String handle) {
		JPAUserConnector uc = new JPAUserConnector();
		Critic user = uc.selectCritic(handle);
		Set<Album> albums = user.getAlbums();
		Hibernate.initialize(albums);
		Album defaultAlbum = null;
		for (Album a : albums) {
			if (a.getName().equals("default")) {
				defaultAlbum = a;
			}
		}

		return defaultAlbum;
	}

	@Override
	public Post getPost(String path) {
		return pc.selectPost(path);
	}
	
}
