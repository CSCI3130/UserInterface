/**
 * JPAPostConnector.java
 * Created Feb 24, 2017
 */
package com.piccritic.database.post;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import com.piccritic.database.JPAConnector;
import com.piccritic.database.user.Critic;
import com.vaadin.addon.jpacontainer.EntityItem;

/**
 * This class enables a connection to the database using JPAContainers. It has a number of methods for 
 * performing post-related operations on the database. Implements {@link PostConnector}. Extends {@link JPAConnector}.
 * @author Ryan Lowe<br>Jonathan Ignacio<br>Damien Robichaud
 */
public class JPAPostConnector extends JPAConnector<Post> implements PostConnector {
	
	/**
	 * Initializes the JPAContainer for this PostConnector.
	 */
	public JPAPostConnector() {
		super(Post.class);
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.post.PostConnector#insertPost(com.piccritic.database.post.Post)
	 */
	public Post insertPost(Post post) throws PostException {
		validate(post);
		container.addEntity(post);
		return selectPost(post.getPath());
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.post.PostConnector#updatePost(com.piccritic.database.post.Post)
	 */
	@SuppressWarnings("unchecked")
	public Post updatePost(Post post) throws PostException {
		validate(post);
		EntityItem<Post> postItem = container.getItem(post.getPath());
		if (postItem == null) {
			throw new PostException("Cannot update null post");
		}
		postItem.getItemProperty("title").setValue(post.getTitle());
		postItem.getItemProperty("description").setValue(post.getDescription());
		postItem.getItemProperty("album").setValue(post.getAlbum());
		postItem.commit();
		return selectPost(post.getPath());
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.post.PostConnector#selectPost(Long)
	 */
	public Post selectPost(String path) {
		EntityItem<Post> postItem = container.getItem(path);
		return (postItem != null) ? postItem.getEntity() : null;
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.post.PostConnector#deletePost(com.piccritic.database.post.Post)
	 */
	public boolean deletePost(Post post) throws PostException {
		validate(post);
		if (post == null) {
			throw new PostException("Cannot delete null post");
		}
		post.getAlbum().getPosts().remove(post);
		container.removeItem(post.getPath());
		return !container.containsId(post.getPath());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.JPAConnector#validate(java.lang.Object)
	 */
	protected void validate(Post post) throws PostException {
		try {
			super.validate(post);
		} catch(Exception e) {
			throw new PostException(e.getLocalizedMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.post.PostConnector#getPosts(com.piccritic.database.user.Critic)
	 */
	public List<Post> getPosts(Critic critic){
		String query1 = "SELECT a FROM Album a WHERE a.critic = :crit ORDER BY a.creationDate";
		TypedQuery<Album> q = entity.createQuery(query1, Album.class)
				.setParameter("crit", critic);
		List<Post> postList = new ArrayList<Post>();
		List<Album> albums = q.getResultList();
		
		for(Album album: albums){
			String query2 = "SELECT p FROM Post p WHERE p.album = :album";
			TypedQuery<Post> q2 = entity.createQuery(query2, Post.class)
					.setParameter("album", album);
			postList.addAll(q2.getResultList());
		}
		return postList;
	}

	/**
	 * Gets a specified number of posts from the database.
	 * @param number of posts to get.
	 * @return list of posts from the database.
	 * @throws PostException
	 */
	public List<Post> getPosts(int number) throws PostException {	
		try {
			TypedQuery<Post> q = entity.createQuery("SELECT c from Post c", Post.class);
			q.setMaxResults(number);
			return q.getResultList();
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	
	/**
	 * Gets a specified number of posts from the database, sorted by option.
	 * @param number of posts to get.
	 * @param option to sort posts by.
	 * @return sorted list of posts from the database.
	 * @throws PostException
	 */
	public List<Post> getPosts(int number, PostSortOption option) throws PostException {	
		try {
			TypedQuery<Post> q;
			switch (option) {
				case UPLOAD_DATE:
					q = entity.createQuery("SELECT c from Post c ORDER BY c.uploadDate", Post.class);
					break;
				case TITLE:
					q = entity.createQuery("SELECT c from Post c ORDER BY c.title", Post.class);
					break;
				default:
					q = entity.createQuery("SELECT c from Post c", Post.class);
					break;
			}
			q.setMaxResults(number);
			return q.getResultList();
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
}
