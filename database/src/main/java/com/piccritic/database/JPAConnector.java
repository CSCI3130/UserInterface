package com.piccritic.database;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public abstract class JPAConnector {
	
	protected static EntityManager entity;
	
	public JPAConnector() {
		Map<String, Object> configOverrides = new HashMap<String, Object>();
		configOverrides.put("hibernate.connection.url", System.getenv("JDBC_DATABASE_URL"));

		entity = Persistence.createEntityManagerFactory("postgres", configOverrides).createEntityManager();
	}

}
