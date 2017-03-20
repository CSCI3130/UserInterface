/**
 * JPARatingConnector.java
 * Created Mar 14, 2017
 */
package com.piccritic.database.feedback;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

/**
 * This class implements {@link RatingConnector} using Vaadin JPAContainers.
 * 
 * @author Frank Bosse <br> Jonathan Ignacio
 */
public class JPARatingConnector implements RatingConnector {

	private JPAContainer<Rating> ratings;
	
	public JPARatingConnector() {
		Map<String, Object> configOverrides = new HashMap<String, Object>();
		configOverrides.put("hibernate.connection.url", System.getenv("JDBC_DATABASE_URL"));

		EntityManager entity = Persistence.createEntityManagerFactory("postgres", configOverrides).createEntityManager();

		ratings = JPAContainerFactory.make(Rating.class, entity);
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#selectComment(java.lang.Long)
	 */
	public Rating selectRating(Long id) {
		EntityItem<Rating> ratingItem = ratings.getItem(id);
		return (ratingItem != null) ? ratingItem.getEntity() : null;
	}

	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#insertComment(com.piccritic.database.feedback.Comment)
	 */
	public Rating insertRating(Rating rating) throws RatingException {
		validate(rating);
		rating.setId(null);
		rating.setId((Long)ratings.addEntity(rating));
		return selectRating(rating.getId());
	}

	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#updateComment(com.piccritic.database.feedback.Comment)
	 */
	public Rating updateRating(Rating rating) throws RatingException {
		validate(rating);
		EntityItem<Rating> ratingItem = ratings.getItem(rating.getId());
		
		ratingItem.commit();
		
		return selectRating(rating.getId());
	}

	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#deleteComment(com.piccritic.database.feedback.Comment)
	 */
	public boolean deleteRating(Rating rating) {
		ratings.removeItem(rating.getId());
		return !ratings.containsId(rating.getId());
	}
		
	private void validate(Rating rating) throws RatingException {
		Set<ConstraintViolation<Rating>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(rating);
		for (ConstraintViolation<Rating> violation : violations) {
			throw new RatingException(violation.getMessage());
		}
	}
}
