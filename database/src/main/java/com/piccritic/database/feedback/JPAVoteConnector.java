/**
 * JPAVoteConnector.java
 * Created Mar 12, 2017
 */

package com.piccritic.database.feedback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import com.piccritic.database.user.Critic;
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
	 * @see com.piccritic.database.feedback.VoteConnector#getVoteId(com.piccritic.database.user.Critic, com.piccritic.database.feedback.Comment)
	 */
	public Long getVoteId(Critic critic, Comment comment) {
		if (critic == null || comment == null) {
			return null;
		}
		String query = "SELECT v from Vote v WHERE v.critic = :cr AND v.comment = :co";
		TypedQuery<Vote> v = votes.getEntityProvider().getEntityManager().createQuery(query, Vote.class)
				.setParameter("cr", critic)
				.setParameter("co", comment);
		List<Vote> votes = v.getResultList();
		if (votes != null && votes.size() >= 1) {
			return v.getResultList().get(0).getId();
		} else {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.VoteConnector#insertVote(com.piccritic.database.feedback.Vote)
	 */
	@SuppressWarnings("unchecked")
	public Vote insertVote(Vote vote) throws VoteException {
		validate(vote);
		
		vote.setId(null);
		vote.setId((Long) votes.addEntity(vote));
		
		EntityItem<Vote> voteItem = votes.getItem(vote.getId());
		boolean rating = (boolean) voteItem.getItemProperty("rating").getValue();
		Comment change = (Comment) voteItem.getItemProperty("comment").getValue();
		if(rating){
			change.setScore(change.getScore() + 1);
			voteItem.getItemProperty("comment").setValue(change);
		}
		else {
			change.setScore(change.getScore() - 1);
			voteItem.getItemProperty("comment").setValue(change);
		}
		voteItem.commit();
		
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

		boolean rating = (boolean) voteItem.getItemProperty("rating").getValue();
		Comment change = (Comment) voteItem.getItemProperty("comment").getValue();
		if(rating){
			change.setScore(change.getScore() + 1);
			voteItem.getItemProperty("comment").setValue(change);
		}
		else {
			change.setScore(change.getScore() - 1);
			voteItem.getItemProperty("comment").setValue(change);
		}
		voteItem.commit();
		
		return selectVote(vote.getId());
	}

	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.VoteConnector#deleteVote(com.piccritic.database.feedback.Vote)
	 */
	@SuppressWarnings("unchecked")
	public boolean deleteVote(Vote vote) throws VoteException {
		validate(vote);
		EntityItem<Vote> voteItem = votes.getItem(vote.getId());
		
		voteItem.getItemProperty("rating").setValue(vote.getRating());

		boolean rating = (boolean) voteItem.getItemProperty("rating").getValue();
		Comment change = (Comment) voteItem.getItemProperty("comment").getValue();
		if(rating){
			change.setScore(change.getScore() - 1);
			voteItem.getItemProperty("comment").setValue(change);
		}
		else {
			change.setScore(change.getScore() + 1);
			voteItem.getItemProperty("comment").setValue(change);
		}
		voteItem.commit();
		
		votes.removeItem(vote.getId());
		return !votes.containsId(vote.getId());
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
	
	@Override
	public int getScore(Comment comment) {
		String query = "SELECT v FROM Vote v WHERE v.comment = :comment";
		TypedQuery<Vote> q = votes.getEntityProvider().getEntityManager().createQuery(query, Vote.class)
				.setParameter("comment", comment);
		List<Vote> voteList = q.getResultList();
		
		int score = 0;
		for (Vote vote : voteList) {
			if (vote.getRating()) {
				score++;
			} else {
				score--;
			}
		}
		return score;
	}
}
