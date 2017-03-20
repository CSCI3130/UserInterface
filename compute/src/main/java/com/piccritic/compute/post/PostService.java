package com.piccritic.compute.post;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.hibernate.Hibernate;

import com.piccritic.compute.feedback.FeedbackService;
import com.piccritic.compute.feedback.FeedbackServiceInterface;
import com.piccritic.database.feedback.Comment;
import com.piccritic.database.feedback.CommentException;
import com.piccritic.database.feedback.Vote;
import com.piccritic.database.feedback.VoteException;
import com.piccritic.database.post.Album;
import com.piccritic.database.post.AlbumException;
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
 * @author ian-dawson
 */
public class PostService implements PostServiceInterface {
	
	public static final String USERS_DIR = "users";

	static PostConnector pc = new JPAPostConnector();
	private FeedbackServiceInterface fs = FeedbackService.createService();
	
	public File getImageFile(String handle) {
		Path p0 = Paths.get(USERS_DIR, handle);
		File directory = p0.toFile();
		File[] flist = directory.listFiles();
		String newFileName="";

		if (flist == null) {
			newFileName = getRandomName();
			return Paths.get(USERS_DIR, handle, newFileName).toFile();
		}

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

	public Post createPost(Post post) throws PostException, AlbumException{
		
		if (post.getUploadDate() == null) {
			post.setUploadDate( new Date(Calendar.getInstance().getTime().getTime()) );

			
			if (post.getLicense() == null) {
				post.setLicense(post.getAlbum().getCritic().getLicense());
			}

			return pc.insertPost(post);
		}	

		return pc.updatePost(post) ;
	}

	public boolean deletePost(Post post) throws PostException, CommentException, VoteException {
		if (post == null) {
			throw new PostException("Cannot delete null post");
		}
		
		List<Comment> comments = fs.getComments(post);
		for (Comment comment : comments) {
			List<Vote> votes = fs.getVotes(comment);
			for (Vote vote : votes) {
				fs.deleteVote(vote);
			}
			fs.deleteComment(comment);
		}
		
		File image = new File(post.getPath());
		if (image.exists()) {
			image.delete();
		}
		
		return pc.deletePost(post);
	}
	
	@Override
	public Album updateAlbum(Album album) throws AlbumException {
		return pc.updateAlbum(album);
	}

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

	public Post getPost(String path) {
		return pc.selectPost(path);
	}
	
	@Override
	public List<Post> getPosts(int number) throws PostException {
		return pc.getPosts(number);
	}
	
}
