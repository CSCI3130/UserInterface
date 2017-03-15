/**
 * CommentConnector.java
 * Created Mar 6, 2017
 */
package com.piccritic.database.feedback;

import java.util.List;

import com.piccritic.database.post.Post;
import com.piccritic.database.user.Critic;

/**
 * This interface provides methods that read to and write from the database
 * with regard to {@link Comment} objects.
 * 
 * @author Ryan Lowe<br>Amelia Stead
 */
public interface CommentConnector {
	
	/**
	 * 
	 * @param id the ID of the comment to get
	 * @return the comment with the matching ID
	 */
	public Comment selectComment(Long id);
	
	/**
	 * 
	 * @param comment the comment to insert
	 * @return the same comment with an auto-generated ID
	 * @throws CommentException 
	 */
	public Comment insertComment(Comment comment) throws CommentException;
	
	/**
	 * 
	 * @param comment the comment to update
	 * @return the updated comment
	 * @throws CommentException 
	 */
	public Comment updateComment(Comment comment) throws CommentException;
	
	/**
	 * 
	 * @param comment the comment to delete
	 * @return true on successful deletion
	 */
	public boolean deleteComment(Comment comment);
	
	/**
	 * 
	 * @param post - the post to retrieve the comments from
	 * @return a list of all comments from the post.
	 */
	public List<Comment> getComments(Post post);
	
	/**
	 * 
	 * @param critic - the critic to retrieve the comments from
	 * @return a list of all comments from the user.
	 */
	public List<Comment> getComments(Critic critic);
}
