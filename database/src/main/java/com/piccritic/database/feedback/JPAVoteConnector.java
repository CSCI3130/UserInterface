/**
 * JPAVoteConnector.java
 * Created Mar 12, 2017
 */

package com.piccritic.database.feedback;

import java.util.List;

import javax.persistence.TypedQuery;

import com.piccritic.database.JPAConnector;
import com.piccritic.database.user.Critic;
import com.vaadin.addon.jpacontainer.EntityItem;

/**
 * This class enables a connection to the database using JPAContainers. It has a number of methods for 
 * performing vote-related operations on the database. Implements {@link VoteConnector}. Extends {@link JPAConnector}.
 * @author Jonathan Ignacio<br>Frank Bosse
 */
public class JPAVoteConnector extends JPAConnector<Vote> implements VoteConnector{
	
	/**
	 * Initializes the JPAContainer for this VoteConnector.
	 */
	public JPAVoteConnector(){
		super(Vote.class);
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.VoteConnector#selectVote(java.lang.Long)
	 */
	public Vote selectVote(Long id) {
		EntityItem<Vote> voteItem = container.getItem(id);
		return (voteItem != null) ? voteItem.getEntity() : null;
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.VoteConnector#insertVote(com.piccritic.database.feedback.Vote)
	 */
	@SuppressWarnings("unchecked")
	public Vote insertVote(Vote vote) throws VoteException {
		validate(vote);
		
		vote.setId(null);
		vote.setId((Long) container.addEntity(vote));
		
		EntityItem<Vote> voteItem = container.getItem(vote.getId());
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
		EntityItem<Vote> voteItem = container.getItem(vote.getId());
		
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
		EntityItem<Vote> voteItem = container.getItem(vote.getId());
		
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
		
		container.removeItem(vote.getId());
		return !container.containsId(vote.getId());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.JPAConnector#validate(java.lang.Object)
	 */
	protected void validate(Vote vote) throws VoteException {
		try{
			super.validate(vote);
		} catch(Exception e) {
			throw new VoteException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.VoteConnector#getVoteId(com.piccritic.database.user.Critic, com.piccritic.database.feedback.Comment)
	 */
	public Long getVoteId(Critic critic, Comment comment) {
		if (critic == null || comment == null) {
			return null;
		}
		String query = "SELECT v from Vote v WHERE v.critic = :cr AND v.comment = :co";
		TypedQuery<Vote> v = entity.createQuery(query, Vote.class)
				.setParameter("cr", critic)
				.setParameter("co", comment);
		List<Vote> votes = v.getResultList();
		if (votes != null && votes.size() >= 1) {
			return v.getResultList().get(0).getId();
		} else {
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.feedback.VoteConnector#getScore(com.piccritic.database.feedback.Comment)
	 */
	@Override
	public int getScore(Comment comment) {
		String query = "SELECT v FROM Vote v WHERE v.comment = :comment";
		TypedQuery<Vote> q = entity.createQuery(query, Vote.class)
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
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.feedback.VoteConnector#getVotes(com.piccritic.database.feedback.Comment)
	 */
	@Override
	public List<Vote> getVotes(Comment comment) {
		String query = "SELECT v FROM Vote v WHERE v.comment = :comment";
		TypedQuery<Vote> q = entity.createQuery(query, Vote.class)
				.setParameter("comment", comment);
		return q.getResultList();
	}
}
