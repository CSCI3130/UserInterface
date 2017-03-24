package com.piccritic.database.post;

import com.piccritic.database.JPAConnector;
import com.vaadin.addon.jpacontainer.EntityItem;

/**
 * This class enables a connection to the database using JPAContainers. It has a number of methods for 
 * performing album-related operations on the database. Implements {@link AlbumConnector}. Extends {@link JPAConnector}.
 * @author Ryan Lowe<br>Jonathan Ignacio<br>Damien Robichaud
 */
public class JPAAlbumConnector extends JPAConnector<Album> implements AlbumConnector{
	
	/**
	 * Initializes the JPAContainer for this AlbumConnector.
	 */
	public JPAAlbumConnector() {
		super(Album.class);
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.post.PostConnector#insertAlbum(com.piccritic.database.post.Album)
	 */
	public Album insertAlbum(Album album) throws AlbumException {
		validate(album);
		album.setId(null);
		album.setId((Long) container.addEntity(album));
		return selectAlbum(album.getId());
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.post.PostConnector#updateAlbum(com.piccritic.database.post.Album)
	 */
	@SuppressWarnings("unchecked")
	public Album updateAlbum(Album album) throws AlbumException {
		EntityItem<Album> albumItem = container.getItem(album.getId());
		
		validate(album);
		albumItem.getItemProperty("name").setValue(album.getName());
		albumItem.commit();
		
		return selectAlbum(album.getId());
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.post.PostConnector#selectAlbum(Long)
	 */
	public Album selectAlbum(Long id) {
		EntityItem<Album> albumItem = container.getItem(id);
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
		container.removeItem(album.getId());
		return !container.containsId(album.getId());
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.JPAConnector#validate(java.lang.Object)
	 */
	protected void validate(Album album) throws AlbumException {
		try{
			super.validate(album);
		} catch (Exception e) {
			throw new AlbumException(e.getMessage());
		}
	}

}