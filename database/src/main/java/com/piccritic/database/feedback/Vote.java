/**
 * Vote.java
 * Created Mar 12, 2017
 */

package com.piccritic.database.feedback;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.piccritic.database.user.Critic;

/**
 * 
 * @author Jonathan Ignacio	<br> Frank Bosse
 */
@Entity
public class Vote {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;	
	
	@ManyToOne
	@NotNull
	private Critic critic;
	
	@NotNull
	/**
	 * Rating will be either true for upvote or false for downvote
	 */
	private boolean rating;
	
	@ManyToOne
	@NotNull
	private Comment comment;
	
	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public boolean getRating(){
		return rating;
	}
	
	public void setRating(boolean rating){
		this.rating = rating;
	}
	
	public Critic getCritic(){
		return critic;
	}
	
	public void setCritic(Critic critic){
		this.critic = critic;
	}
	
	public Comment getComment(){
		return comment;
	}
	
	public void setComment(Comment comment){
		this.comment = comment;
	}
	
	
	public String toString() {
		return String.format("Vote{id=%d, critic.handle=%s, comment.id=%d, rating=%s}",
				id, critic.getHandle(), comment.getId(), rating ? "UPVOTE" : "DOWNVOTE");
	}
}
