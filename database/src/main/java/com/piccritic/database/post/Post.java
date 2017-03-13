/**
 * Post.java
 * Created Feb 22, 2017
 */
package com.piccritic.database.post;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.piccritic.database.feedback.Comment;
import com.piccritic.database.feedback.Rating;

/**
 * this class is a Java bean that defines the database table
 * for the PicCritic project.
 *
 * It holds all the necessary fields for a post data object.
 * The primary key is the post's id (Long) defined by the database.
 * It is assigned to an {@link Album} object.
 *
 * @author Ryan Lowe<br>Johnathan Ignacio
 */
@Entity
public class Post {

	@Id
	private String path;
	@NotNull
	private Date uploadDate;
	private String title;
	private String description;

	@ManyToOne(optional=false)
	private Album album;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="post") 
	private Set<Comment> comments;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="post")
	private Set<Rating> ratings;

	/*public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}*/

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	/**
	 * @return the title of this Post
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Set<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}

	public String toString() {
		return String.format("Post{path=%s, uploadDate=%s, title=%s, description=%s, album.id=%d}", 
				/*(id == null) ? null:id.longValue(),*/ path, (uploadDate == null) ? null :uploadDate.toString(),
				title, description, (album == null) ? null : album.getId());
	}
	
	/**
	 * Compares this Post to another object and returns true if they are equal.
	 * 
	 * @param o the object to compare
	 * @return true if the object is an identical Post to this one
	 */
	public boolean equals(Object o) {
		if (o != null && o instanceof Post) {
			Post p = (Post) o;
			return this.toString().equals(p.toString());
		}
		return false;
	}
	
	
}
