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
 * March 16, 2017: Modified by Frank Bosse, added get and set for aspect
 * March 17, 2017: Modified by Frank Bosse, removed enum aspect replaced it with an aspectRatings object
 */
@Entity
public class Rating {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@ManyToOne
	private Post post;
	
	@NotNull
	@ManyToOne
	private Critic critic;
	
	@NotNull
	private Double lighting;
	
	@NotNull
	private Double composition;
	
	@NotNull
	private Double color;
	
	@NotNull
	private Double focus;
	
	@NotNull
	private Double exposure;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getLighting() {
		return lighting;
	}

	public void setLighting(Double lighting) {
		this.lighting = lighting;
	}

	public Double getComposition() {
		return composition;
	}

	public void setComposition(Double composition) {
		this.composition = composition;
	}

	public Double getColor() {
		return color;
	}

	public void setColor(Double color) {
		this.color = color;
	}

	public Double getFocus() {
		return focus;
	}

	public void setFocus(Double focus) {
		this.focus = focus;
	}

	public Double getExposure() {
		return exposure;
	}

	public void setExposure(Double exposure) {
		this.exposure = exposure;
	}

	public Double getAverage() {
		return (lighting + composition + color + focus + exposure)/5;
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
	
	public String toString() {
		return String.format("Rating{ Color=%s, Composition=%s, Exposure=%s,"
				+ " Focus=%s, Lighting=%s, id=%s }",
				color, composition, exposure, focus, lighting, id);
	}
	
	public boolean equals(Object o) {
		return this.toString().equals(o.toString());
	}
}
