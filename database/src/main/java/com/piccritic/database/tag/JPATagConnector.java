/**
 * @author damienr74, ian-dawson
 */

package com.piccritic.database.tag;

import java.util.List;

import javax.persistence.TypedQuery;

import com.piccritic.database.JPAConnector;

public class JPATagConnector extends JPAConnector<Tag> implements TagConnector {

	public JPATagConnector() {
		super(Tag.class);
	}

	@Override
	public void insertTag(Tag tag) throws TagException {
		validate(tag);
		container.addEntity(tag);
	}

	@Override
	public List<Tag> findTags(String query) throws TagException {
		String q = "SELECT t FROM Tag t WHERE t.tag LIKE CONCAT('%', :query, '%')";
		TypedQuery<Tag> t = entity.createQuery(q, Tag.class).setParameter("query", query);
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
