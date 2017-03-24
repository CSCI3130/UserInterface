package com.piccritic.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import com.piccritic.database.feedback.Vote;
import com.piccritic.database.feedback.VoteException;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

public abstract class JPAConnector<T> {
	
	protected EntityManager entity;
	protected JPAContainer<T> container;
	
	public JPAConnector(Class<T> c) {
		container = JPAContainerFactory.make(c, entity);
		Map<String, Object> configOverrides = new HashMap<String, Object>();
		configOverrides.put("hibernate.connection.url", System.getenv("JDBC_DATABASE_URL"));

		entity = Persistence.createEntityManagerFactory("postgres", configOverrides).createEntityManager();
	}
	
	/**
	 * Ensures that object follows constraints correctly.
	 * @param t the object to be checked
	 * @throws Exception
	 */
	protected void validate(T t) throws Exception {
		Set<ConstraintViolation<T>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(t);
		for (ConstraintViolation<T> violation : violations) {
			throw new Exception(violation.getMessage());
		}
	}
}
