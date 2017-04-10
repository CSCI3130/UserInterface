/**
 * @author damienr74, ian-dawson
 */

package com.piccritic.database.tag;

import java.util.List;

import javax.persistence.TypedQuery;

import com.piccritic.database.JPAConnector;
import com.piccritic.database.post.Post;
import com.piccritic.database.user.Critic;

public class JPATagConnector extends JPAConnector<Tag> implements TagConnector {

	public JPATagConnector() {
		super(Tag.class);
	}

	@Override
	public void insertTag(Tag tag) throws TagException {
		validate(tag);
		if (container.containsId(tag.getTag())) {
			return;
		}
		container.addEntity(tag);
	}

	@Override
	public List<Tag> findTags(String query) {
		String q = "SELECT t FROM Tag t WHERE t.tag LIKE CONCAT('%', :query, '%')";
		TypedQuery<Tag> t = entity.createQuery(q, Tag.class).setParameter("query", query);
		return t.getResultList();
	}
	
	public List<Post> findPosts(List<String> tags, Critic critic) {
		if (tags == null || tags.size() < 1) {
			return null;
		}
		
		String beforeConcat = "t.tag LIKE CONCAT('%', :query";
		String afterConcat = ", '%')";
		// select c FROM Course c JOIN c.enrolledStudents u WHERE u.id = :userId
		String q = "SELECT p FROM Post p JOIN p.tags t WHERE ";
		
		for (int i = 0; i < tags.size(); i++) {
			q += beforeConcat+i+afterConcat;
			if (i+1 < tags.size()) {
				q += " AND ";
			}
		}
		
		if (critic != null) {
			q += " AND p.album.critic = :critic";
		}
		
		TypedQuery<Post> t = entity.createQuery(q, Post.class);
		for (int i = 0; i < tags.size(); i++) {
			t = t.setParameter("query"+i, tags.get(i));
		}
		
		if (critic != null) {
			t = t.setParameter("critic", critic);
		}
		
		return t.getResultList();
	}

	@Override
	public boolean deleteTag(Tag tag) throws TagException {
		return container.removeItem(tag.getTag());
	}
	
	protected void validate(Tag tag) throws TagException {
		try {
			super.validate(tag);
		} catch (Exception e) {
			throw new TagException(e.getLocalizedMessage());
		} 
	}

}
