package com.piccritic.database.post;

public interface AlbumConnector {

	/**
	 * Inserts the specified Album into the database.
	 * 
	 * @param album the album to insert
	 * @return the same album, with a generated ID
	 * @throws AlbumException if the album validation fails
	 */
	public Album insertAlbum(Album album) throws AlbumException;
	
	/**
	 * Updates the specified Album in the database.
	 * 
	 * @param album the album to update
	 * @return the same album
	 * @throws AlbumException if the album validation fails
	 */
	public Album updateAlbum(Album album) throws AlbumException;
	
	/**
	 * Gets the Album with the matching ID from the database.
	 * 
	 * @param id the id to search for
	 * @return the album with the given ID, or null if it doesn't exist
	 */
	public Album selectAlbum(Long id);
	
	/**
	 * Deletes the specified Album from the database.
	 * 
	 * @param album the album to delete
	 * @return true on successful delete
	 * @throws AlbumException if the album validation fails
	 */
	public boolean deleteAlbum(Album album) throws AlbumException;
}
