/**
 * @author damienr74, ian-dawson
 */

package com.piccritic.database.tag;

import java.util.List;

import com.piccritic.database.post.Post;
import com.piccritic.database.user.Critic;

public interface TagConnector {
	/**
	 * Inserts a new tag into the database
	 * 
	 * @param tag
	 * @throws TagException
	 */
	public void insertTag(Tag tag) throws TagException;
	
	/**
	 * Returns a list of tags that match the user query
	 * 
	 * @param query
	 * @return List of tags
	 */
	public List<Tag> findTags(String query);
	
	/**
	 * Deletes a tag
	 * @param tag
	 * @return
	 * @throws TagException
	 */
	public boolean deleteTag(Tag tag) throws TagException;
	
	/**
	 * Selects posts by tags
	 * @param tags
	 * @param critic
	 * @return
	 */
	public List<Post> findPosts(List<String> tags, Critic critic);	
	

}
