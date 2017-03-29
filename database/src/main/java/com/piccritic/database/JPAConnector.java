package com.piccritic.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

/**
 * Abstract class to define a JPAConnector's constructor and validate methods. Has EntityManager entity 
 * and JPAContainer<T> container.
 * @param <T> Type of item to create the JPAContainer for.
 * @author Damien Robichaud <br> Jonathan Ignacio
 */
public abstract class JPAConnector<T> {
	
	protected EntityManager entity;
	protected JPAContainer<T> container;
	
	/**
	 * Constructs the JPAConnector by establishing the database connection and instantiating the connector's JPA container.
	 * @param c - The class of the inherited JPAConnector, necessary to create a JPAContainer of a particular type.
	 */
	public JPAConnector(Class<T> c) {
		Map<String, Object> configOverrides = new HashMap<String, Object>();
		configOverrides.put("hibernate.connection.url", System.getenv("JDBC_DATABASE_URL"));

		entity = Persistence.createEntityManagerFactory("postgres", configOverrides).createEntityManager();
		container = JPAContainerFactory.make(c, entity);
	}
	
	/**
	 * Validates the fields and throws exceptions when the fields
	 * do not currently abide by the rules defined in the given object's class
	 * @param t the object to be checked.
	 * @throws Exception - general exception, varies by the object's validate method.
	 */
	protected void validate(T t) throws Exception {
		Set<ConstraintViolation<T>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(t);
		for (ConstraintViolation<T> violation : violations) {
			throw new Exception(violation.getMessage());
		}
	}
}

