/**
 * JPARatingConnector.java
 * Created Mar 14, 2017
 */
package com.piccritic.database.feedback;

import com.piccritic.database.JPAConnector;
import com.vaadin.addon.jpacontainer.EntityItem;

/**
 * This class enables a connection to the database using JPAContainers. It has a number of methods for 
 * performing rating-related operations on the database. Implements {@link RatingConnector}. Extends {@link JPAConnector}.
 * @author Frank Bosse<br>Jonathan Ignacio
 */
public class JPARatingConnector extends JPAConnector<Rating> implements RatingConnector {

	/**
	 * Initializes the JPAContainer for this RatingConnector.
	 */
	public JPARatingConnector() {
		super(Rating.class);
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#selectComment(java.lang.Long)
	 */
	public Rating selectRating(Long id) {
		EntityItem<Rating> ratingItem = container.getItem(id);
		return (ratingItem != null) ? ratingItem.getEntity() : null;
	}

	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#insertComment(com.piccritic.database.feedback.Comment)
	 */
	public Rating insertRating(Rating rating) throws RatingException {
		validate(rating);
		rating.setId(null);
		rating.setId((Long)container.addEntity(rating));
		return selectRating(rating.getId());
	}

	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#updateComment(com.piccritic.database.feedback.Comment)
	 */
	public Rating updateRating(Rating rating) throws RatingException {
		validate(rating);
		EntityItem<Rating> ratingItem = container.getItem(rating.getId());
		
		ratingItem.commit();
		
		return selectRating(rating.getId());
	}

	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#deleteComment(com.piccritic.database.feedback.Comment)
	 */
	public boolean deleteRating(Rating rating) {
		container.removeItem(rating.getId());
		return !container.containsId(rating.getId());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.JPAConnector#validate(java.lang.Object)
	 */
	protected void validate(Rating rating) throws RatingException {
		try {
			super.validate(rating);
		} catch (Exception e) {
			throw new RatingException(e.getMessage());
		}
	}
}
