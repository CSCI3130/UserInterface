/**
 * Rating.java
 * Created Mar 6, 2017
 */
package com.piccritic.database.feedback;

import javax.persistence.Column;
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
 * March 16, 2017: Modified by Frank Bosse, added get and set for aspect
 * March 17, 2017: Modified by Frank Bosse, removed enum aspect replaced it with an aspectRatings object
 */
@Entity
public class Rating {

	@Column(name = "rating")
	private RatingAspects aspects;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@ManyToOne
	private Post post;
	
	@NotNull
	@ManyToOne
	private Critic critic;
	
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

	public RatingAspects getRatings() {
		return aspects;
	}
	public void setRatings( RatingAspects ratings) {
		aspects = ratings;
	}
}
