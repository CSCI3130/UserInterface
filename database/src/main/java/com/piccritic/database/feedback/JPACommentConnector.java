/**
 * JPACommentConnector.java
 * Created Mar 6, 2017
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

import com.piccritic.database.post.Post;
import com.piccritic.database.user.Critic;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

/**
 * This class implements {@link CommentConnector} using Vaadin JPAContainers.
 * 
 * @author Ryan Lowe
 */
public class JPACommentConnector implements CommentConnector {

	private JPAContainer<Comment> comments;
	
	public JPACommentConnector() {
		Map<String, Object> configOverrides = new HashMap<String, Object>();
		configOverrides.put("hibernate.connection.url", System.getenv("JDBC_DATABASE_URL"));

		EntityManager entity = Persistence.createEntityManagerFactory("postgres", configOverrides).createEntityManager();

		comments = JPAContainerFactory.make(Comment.class, entity);
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#selectComment(java.lang.Long)
	 */
	public Comment selectComment(Long id) {
		EntityItem<Comment> commentItem = comments.getItem(id);
		return (commentItem != null) ? commentItem.getEntity() : null;
	}

	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#insertComment(com.piccritic.database.feedback.Comment)
	 */
	public Comment insertComment(Comment comment) throws CommentException {
		validate(comment);
		comment.setId(null);
		comment.setId((Long) comments.addEntity(comment));
		return selectComment(comment.getId());
	}

	/* (non-Javadoc)
	 * @see com.piccritic.database.feedback.CommentConnector#updateComment(com.piccritic.database.feedback.Comment)
	 */
	@SuppressWarnings("unchecked")
	public Comment updateComment(Comment comment) throws CommentException {
		validate(comment);
		EntityItem<Comment> commentItem = comments.getItem(comment.getId());
		
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
		EntityItem<Comment> commentItem = comments.getItem(comment.getId());
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
			comments.removeItem(comment.getId());
			// Return true if comment was deleted
			return !comments.containsId(comment.getId());
		}
		// If there are replies, remove the author and the content but keep the comment
		Critic author = (Critic) commentItem.getItemProperty("author");
		author.getComments().remove(comment);
		commentItem.getItemProperty("author").setValue(null);
		commentItem.getItemProperty("content").setValue("<Deleted>");
		commentItem.commit();
		return comments.getItem(comment.getId()).getItemProperty("author") == null;
	}
	
	private void validate(Comment comment) throws CommentException {
		Set<ConstraintViolation<Comment>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(comment);
		for (ConstraintViolation<Comment> violation : violations) {
			throw new CommentException(violation.getMessage());
		}
	}

	@Override
	public List<Comment> getComments(Post post) {
		String query = "SELECT c FROM Comment c WHERE c.post = :path ORDER BY c.creationDate";
		TypedQuery<Comment> q = comments.getEntityProvider().getEntityManager().createQuery(query, Comment.class).setParameter("path", post);
		return q.getResultList();
	}
	
	@Override
	public List<Comment> getComments(Critic critic) {
		String query = "SELECT c FROM Comment c WHERE c.post = :path ORDER BY c.creationDate";
		TypedQuery<Comment> q = comments.getEntityProvider().getEntityManager().createQuery(query, Comment.class).setParameter("path", critic);
		return q.getResultList();
	}
	
}
