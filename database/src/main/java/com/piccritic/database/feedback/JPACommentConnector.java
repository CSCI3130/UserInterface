/**
 * JPACommentConnector.java
 * Created Mar 6, 2017
 */
package com.piccritic.database.feedback;

import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;

import com.piccritic.database.JPAConnector;
import com.piccritic.database.post.Post;
import com.piccritic.database.user.Critic;
import com.vaadin.addon.jpacontainer.EntityItem;

/**
 * This class enables a connection to the database using JPAContainers. It has a number of methods for 
 * performing comment-related operations on the database. Implements {@link CommentConnector}. Extends {@link JPAConnector}.
 * @author Ryan Lowe
 */
public class JPACommentConnector extends JPAConnector<Comment> implements CommentConnector {


	/**
	 * Initializes the JPAContainer for this CommentConnector.
	 */
	public JPACommentConnector() {
		super(Comment.class);
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#selectComment(java.lang.Long)
	 */
	public Comment selectComment(Long id) {
		EntityItem<Comment> commentItem = container.getItem(id);
		return (commentItem != null) ? commentItem.getEntity() : null;
	}

	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#insertComment(com.piccritic.database.feedback.Comment)
	 */
	public Comment insertComment(Comment comment) throws CommentException {
		validate(comment);
		comment.setId(null);
		comment.setId((Long) container.addEntity(comment));
		return selectComment(comment.getId());
	}

	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#updateComment(com.piccritic.database.feedback.Comment)
	 */
	@SuppressWarnings("unchecked")
	public Comment updateComment(Comment comment) throws CommentException {
		validate(comment);
		EntityItem<Comment> commentItem = container.getItem(comment.getId());
		
		commentItem.getItemProperty("content").setValue(comment.getContent());
		commentItem.getItemProperty("votes").setValue(comment.getVotes());
		commentItem.commit();
		
		return selectComment(comment.getId());
	}

	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#deleteComment(com.piccritic.database.feedback.Comment)
	 */
	@SuppressWarnings("unchecked")
	public boolean deleteComment(Comment comment) {
		EntityItem<Comment> commentItem = container.getItem(comment.getId());
		// Check if the comment has any replies
		Set<Comment> replies = (Set<Comment>) commentItem.getItemProperty("replies").getValue();
		if (replies == null || replies.isEmpty()) {
			// Check if the comment is a reply to a comment
			Comment anchor = comment.getAnchor();
			if (anchor != null) {
				anchor.getReplies().remove(comment);
			}
			// Remove the comment from the post
			comment.getPost().getComments().remove(comment);
			// Delete the comment
			container.removeItem(comment.getId());
			// Return true if comment was deleted
			return !container.containsId(comment.getId());
		}
		// If there are replies, remove the author and the content but keep the comment
		Critic author = (Critic) commentItem.getItemProperty("author");
		author.getComments().remove(comment);
		commentItem.getItemProperty("author").setValue(null);
		commentItem.getItemProperty("content").setValue("<Deleted>");
		commentItem.commit();
		return container.getItem(comment.getId()).getItemProperty("author") == null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.JPAConnector#validate(java.lang.Object)
	 */
	protected void validate(Comment comment) throws CommentException {
		try {
			super.validate(comment);
		} catch (Exception e) {
			throw new CommentException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#getComments(com.piccritic.database.post.Post)
	 */
	@Override
	public List<Comment> getComments(Post post) {
		String query = "SELECT c FROM Comment c WHERE c.post = :path ORDER BY c.creationDate";
		TypedQuery<Comment> q = container.getEntityProvider().getEntityManager().createQuery(query, Comment.class).setParameter("path", post);
		return q.getResultList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#getComments(com.piccritic.database.user.Critic)
	 */
	@Override
	public List<Comment> getComments(Critic critic) {
		String query = "SELECT c FROM Comment c WHERE c.critic = :crit ORDER BY c.creationDate";
		TypedQuery<Comment> q = container.getEntityProvider().getEntityManager().createQuery(query, Comment.class).setParameter("crit", critic);
		return q.getResultList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#getCommentCount(com.piccritic.database.user.Critic)
	 */
	@Override
	public int getCommentCount(Critic critic) {
		String query = "SELECT c FROM Comment c WHERE c.critic = :critic";
		TypedQuery<Comment> q = container.getEntityProvider().getEntityManager().createQuery(query, Comment.class)
				.setParameter("critic", critic);
		List<Comment> commentList = q.getResultList();
		
		int total = 0;
		for(Comment comment: commentList) {
			if(comment.getId() != null) {
				total++;
			}
		}
		
		return total;
	}
	
}
