/**
 * JPAPostConnector.java
 * Created Feb 24, 2017
 */
package com.piccritic.database.post;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

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
		albumItem.getItemProperty("posts").setValue(album.getPosts());
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
		postItem.getItemProperty("rating").setValue(post.getRating());
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
}
