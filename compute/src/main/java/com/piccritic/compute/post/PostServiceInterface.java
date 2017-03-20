package com.piccritic.compute.post;

import java.io.File;
import java.util.List;

import com.piccritic.database.feedback.CommentException;
import com.piccritic.database.feedback.VoteException;
import com.piccritic.database.post.Album;
import com.piccritic.database.post.AlbumException;
import com.piccritic.database.post.Post;
import com.piccritic.database.post.PostException;

/**
 * 
 * @author Damien Robichaud <br>
 *         Francis Bosse <br>
 *         ian-dawson
 *
 */
public interface PostServiceInterface {

	/**
	 * Returns a FileOutputStream where the current file should be stored. Used
	 * by the front-end to store an Image object.
	 * 
	 * It's important to make sure that the file get's
	 * 
	 * @param handle
	 * @return File used for Image storage
	 */
	public File getImageFile(String handle);

	/**
	 * Creates a post in the database.
	 * 
	 * @param post
	 *            to store
	 * @return Post Object created
	 * @throws PostException
	 * @throws AlbumException 
	 */
	public Post createPost(Post post) throws PostException, AlbumException;

	/**
	 * Deletes a post from the database, returns null if not found
	 * 
	 * @param post
	 *            to delete
	 * @return Post object deleted
	 * @throws CommentException 
	 * @throws VoteException 
	 */
	public boolean deletePost(Post post) throws PostException, CommentException, VoteException;

	/**
	 * Returns a user's default album
	 * 
	 * @param handle of user
	 * @return Album default
	 */
	public Album getDefaultAlbum(String handle);
	
	/**
	 * Returns a post from its handle
	 * 
	 * @param post path
	 * @return Post matching
	 */
	public Post getPost(String path);

	/**
	 * Updates and returns an album
	 *
	 * @param album (updated)
	 * @return Album selected by id in the DB
	 */
	public Album updateAlbum(Album album) throws AlbumException;
	
	/**
	 * Returns a list of posts from the database
	 * 
	 * @param number of posts to get
	 * @return List of posts
	 * @throws PostException
	 */
	public List<Post> getPosts(int number) throws PostException;
}
