/**
 * RatingConnector.java
 * Created Mar 6, 2017
 */
package com.piccritic.database.feedback;

/**
 * This interface provides methods that read to and write from the database
 * with regard to {@link Rating} objects.
 * 
 * @author Ryan Lowe<br>Amelia Stead
 */
public interface RatingConnector {
	
	/**
	 * 
	 * @param id the ID of the rating to get
	 * @return the rating with the matching ID
	 */
	public Rating selectRating(Long id);
	
	/**
	 * 
	 * @param rating the rating to insert
	 * @return the same rating with an auto-generated ID
	 */
	public Rating insertRating(Rating rating);
	
	/**
	 * 
	 * @param rating the rating to update
	 * @return the updated rating
	 */
	public Rating updateRating(Rating rating);
	
	/**
	 * 
	 * @param rating the rating to delete
	 * @return true on successful deletion
	 */
	public boolean deleteRating(Rating rating);
}
