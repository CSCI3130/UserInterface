/**
 * @author damienr74, ian-dawson
 */

package com.piccritic.database.tag;

import java.util.List;

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
	 * @throws TagException
	 */
	public List<Tag> findTags(String query) throws TagException;
	
	/**
	 * Deletes a tag
	 * @param tag
	 * @return
	 * @throws TagException
	 */
	public boolean deleteTag(Tag tag) throws TagException;
	
	

}
