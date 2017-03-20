/**
 * PostConnector.java
 * Created Feb 15, 2017
 */
package com.piccritic.database.post;

import java.util.List;

import com.piccritic.database.feedback.Comment;
import com.piccritic.database.feedback.Rating;
import com.piccritic.database.user.Critic;

/**
 * This interface provides methods that read from and write to the database
 * with regard to the {@link Album} and {@link Post} classes.
 * 
 * @author Ryan Lowe<br>
 * 			Jonathan Ignacio<br>
 * 			ian-dawson
 */
public interface PostConnector {
	
	/**
	 * Inserts the specified Album into the database.
	 * 
	 * @param album the album to insert
	 * @return the same album, with a generated ID
	 * @throws AlbumException if the album validation fails
	 */
	public Album insertAlbum(Album album) throws AlbumException;
	
	/**
	 * Updates the specified Album in the database.
	 * 
	 * @param album the album to update
	 * @return the same album
	 * @throws AlbumException if the album validation fails
	 */
	public Album updateAlbum(Album album) throws AlbumException;
	
	/**
	 * Gets the Album with the matching ID from the database.
	 * 
	 * @param id the id to search for
	 * @return the album with the given ID, or null if it doesn't exist
	 */
	public Album selectAlbum(Long id);
	
	/**
	 * Deletes the specified Album from the database.
	 * 
	 * @param album the album to delete
	 * @return true on successful delete
	 * @throws AlbumException if the album validation fails
	 */
	public boolean deleteAlbum(Album album) throws AlbumException;
	
	/**
	 * Inserts the specified Post into the database.
	 * 
	 * @param post the post to insert
	 * @return the same post, with a generated ID
	 * @throws PostException if the post validation fails.
	 */
	public Post insertPost(Post post) throws PostException;
	
	/**
	 * Updates the specified Post in the database.
	 * 
	 * @param post the post to update
	 * @return the same post
	 * @throws PostException if the post validation fails.
	 */
	public Post updatePost(Post post) throws PostException;
	
	/**
	 * Gets the Post with the matching ID from the database.
	 * 
	 * @param id the id to search for
	 * @return the post with the given ID, or null if it doesn't exist
	 */
	public Post selectPost(String path);
	
	/**
	 * Deletes the specified Post from the database.
	 * 
	 * @param post the post to delete
	 * @return true on successful delete
	 * @throws AlbumException if the post validation fails
	 */
	public boolean deletePost(Post post) throws PostException;
	
	/**
	 * 
	 * @param critic - the critic to retrieve the posts from
	 * @return a list of all posts from the user.
	 * @throws Exception 
	 */
	public List<Post> getPosts(Critic critic);

	/*
	 * Gets a specified number of posts from the database.
	 * @param number of posts to get.
	 * @return list of posts from the database.
	 * @throws PostException
	 */
	public List<Post> getPosts(int number) throws PostException;
}
