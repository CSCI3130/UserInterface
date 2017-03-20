/**
 * JPAPostConnector.java
 * Created Feb 24, 2017
 */
package com.piccritic.database.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import com.piccritic.database.feedback.Comment;
import com.piccritic.database.feedback.Rating;
import com.piccritic.database.user.Critic;
import com.piccritic.database.JPAConnector;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

/**
 * This class implements {@link PostConnector} using
 * Vaadin JPAContainers.
 * 
 * @author Ryan Lowe<br>Jonathan Ignacio<br>Damien Robichaud
 */
public class JPAPostConnector extends JPAConnector implements PostConnector {
	
	private JPAContainer<Album> albums;
	private JPAContainer<Post> posts;
	
	/**
	 * Initializes the JPAContainers for this PostConnector.
	 */
	public JPAPostConnector() {
		albums = JPAContainerFactory.make(Album.class, entity);
		posts = JPAContainerFactory.make(Post.class, entity);
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.post.PostConnector#insertAlbum(com.piccritic.database.post.Album)
	 */
	public Album insertAlbum(Album album) throws AlbumException {
		validate(album);
		album.setId(null);
		album.setId((Long) albums.addEntity(album));
		return selectAlbum(album.getId());
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.post.PostConnector#updateAlbum(com.piccritic.database.post.Album)
	 */
	@SuppressWarnings("unchecked")
	public Album updateAlbum(Album album) throws AlbumException {
		EntityItem<Album> albumItem = albums.getItem(album.getId());
		
		validate(album);
		albumItem.getItemProperty("name").setValue(album.getName());
		albumItem.commit();
		
		return selectAlbum(album.getId());
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.post.PostConnector#selectAlbum(Long)
	 */
	public Album selectAlbum(Long id) {
		EntityItem<Album> albumItem = albums.getItem(id);
		return (albumItem != null) ? albumItem.getEntity() : null;
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.post.PostConnector#deleteAlbum(com.piccritic.database.post.Album)
	 */
	public boolean deleteAlbum(Album album) throws AlbumException {
		validate(album);
		if (album == null) {
			throw new AlbumException("Cannot delete null album");
		}
		albums.removeItem(album.getId());
		return !albums.containsId(album.getId());
		
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.post.PostConnector#insertPost(com.piccritic.database.post.Post)
	 */
	public Post insertPost(Post post) throws PostException {
		validate(post);
		posts.addEntity(post);
		return selectPost(post.getPath());
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.post.PostConnector#updatePost(com.piccritic.database.post.Post)
	 */
	@SuppressWarnings("unchecked")
	public Post updatePost(Post post) throws PostException {
		validate(post);
		EntityItem<Post> postItem = posts.getItem(post.getPath());
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
		EntityItem<Post> postItem = posts.getItem(path);
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
		posts.removeItem(post.getPath());
		return !posts.containsId(post.getPath());
	}
	
	/**
	 * Validates the fields and throws exceptions when the fields
	 * do not currently abide by the rules defined in the album class
	 * 
	 * @param album
	 * @throws AlbumException Message for the UI portion of the code.
	 */
	private void validate(Album album) throws AlbumException {
		Set<ConstraintViolation<Album>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(album);
		for (ConstraintViolation<Album> violation : violations) {
			throw new AlbumException(violation.getMessage());
		}
	}
	
	/**
	 * Validates the fields and throws exceptions when the fields
	 * do not currently abide by the rules defined in the post class
	 * 
	 * @param post
	 * @throws PostException Message for the UI portion of the code.
	 */
	private void validate(Post post) throws PostException {
		Set<ConstraintViolation<Post>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(post);
		for (ConstraintViolation<Post> violation : violations) {
			throw new PostException(violation.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.post.PostConnector#getPosts(com.piccritic.database.user.Critic)
	 */
	public List<Post> getPosts(Critic critic){
		critic.getAlbums();
		String query1 = "SELECT a FROM Album a WHERE a.critic = :crit ORDER BY a.creationDate";
		TypedQuery<Album> q = albums.getEntityProvider().getEntityManager().createQuery(query1, Album.class)
				.setParameter("crit", critic);
		List<Post> postList = new ArrayList<Post>();
		List<Album> albums = q.getResultList();
		
		for(Album album: albums){
			String query2 = "SELECT p FROM Post p WHERE p.album = :album";
			TypedQuery<Post> q2 = posts.getEntityProvider().getEntityManager().createQuery(query2, Post.class)
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
			TypedQuery<Post> q = posts.getEntityProvider().getEntityManager().createQuery("SELECT c from Post c", Post.class);
			q.setMaxResults(number);
			return q.getResultList();
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
}
