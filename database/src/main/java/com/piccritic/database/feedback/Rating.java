/**
 * Rating.java
 * Created Mar 6, 2017
 */
package com.piccritic.database.feedback;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.piccritic.database.post.Post;
import com.piccritic.database.user.Critic;

/**
 * 
 * @author Ryan Lowe<br>Amelia Stead
 */
@Entity
public class Rating {
	
	public static enum Aspect {
		LIGHTING,
		EXPOSURE,
		COMPOSITION,
		FOCUS,
		COLOR
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@NotNull
	private Post post;
	
	@ManyToOne
	@NotNull
	private Critic critic;
	
	@NotNull
	private Aspect aspect;
	
	@NotNull
	private int rating;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Critic getCritic() {
		return critic;
	}

	public void setCritic(Critic critic) {
		this.critic = critic;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
}
