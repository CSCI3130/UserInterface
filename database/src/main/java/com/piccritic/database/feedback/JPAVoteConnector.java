/**
 * JPAVoteConnector.java
 * Created Mar 12, 2017
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
 * This class implements {@link VoteConnector} using Vaadin JPAContainers.
 * 
 * @author Jonathan Ignacio and Frank Bosse
 */
public class JPAVoteConnector implements VoteConnector{

	private JPAContainer<Vote> votes;
	
	public JPAVoteConnector(){
		Map<String, Object> configOverrides = new HashMap<String, Object>();
		configOverrides.put("hibernate.connection.url", System.getenv("JDBC_DATABASE_URL"));

		EntityManager entity = Persistence.createEntityManagerFactory("postgres", configOverrides).createEntityManager();

		votes = JPAContainerFactory.make(Vote.class, entity);
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.VoteConnector#selectVote(java.lang.Long)
	 */
	public Vote selectVote(Long id) {
		EntityItem<Vote> voteItem = votes.getItem(id);
		return (voteItem != null) ? voteItem.getEntity() : null;
	}

	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.VoteConnector#insertVote(com.piccritic.database.feedback.Vote)
	 */
	public Vote insertVote(Vote vote) throws VoteException {
		validate(vote);
		
		vote.setId(null);
		vote.setId((Long) votes.addEntity(vote));
		
		return selectVote(vote.getId());
	}

	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.VoteConnector#updateVote(com.piccritic.database.feedback.Vote)
	 */
	@SuppressWarnings("unchecked")
	public Vote updateVote(Vote vote) throws VoteException {
		validate(vote);
		EntityItem<Vote> voteItem = votes.getItem(vote.getId());
		
		voteItem.getItemProperty("rating").setValue(vote.getRating());
		voteItem.commit();
		
		return selectVote(vote.getId());
	}

	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.VoteConnector#deleteVote(com.piccritic.database.feedback.Vote)
	 */
	public boolean deleteVote(Vote vote) {
		votes.removeItem(vote.getId());
		return votes.containsId(vote.getId());
	}
	
	/**
	 * Ensures that vote follows constraints correctly.
	 * @param vote the vote object to be checked
	 * @throws VoteException
	 */
	private void validate(Vote vote) throws VoteException {
		Set<ConstraintViolation<Vote>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(vote);
		for (ConstraintViolation<Vote> violation : violations) {
			throw new VoteException(violation.getMessage());
		}
	}

}
