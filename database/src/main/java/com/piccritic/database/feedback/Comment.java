/**
 * Comment.java
 * Created Mar 6, 2017
 */
package com.piccritic.database.feedback;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.piccritic.database.post.Post;
import com.piccritic.database.user.Critic;

/**
 * 
 * @author Ryan Lowe<br>Amelia Stead<br>Jonathan Ignacio
 */
@Entity
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@NotNull
	private Post post;
	
	@ManyToOne
	private Comment anchor;
	
	@ManyToOne
	private Vote vote;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="anchor")
	private Set<Comment> replies;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="comment")
	private Set<Vote> votes;
	
	@ManyToOne
	private Critic critic;
	
	@NotNull
	@Size(max=1000)
	private String content;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@NotNull
	private int score;

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

	public Comment getAnchor() {
		return anchor;
	}

	public void setAnchor(Comment anchor) {
		this.anchor = anchor;
	}

	public Set<Comment> getReplies() {
		return replies;
	}

	public void setReplies(Set<Comment> replies) {
		this.replies = replies;
	}

	public Critic getCritic() {
		return critic;
	}

	public void setCritic(Critic critic) {
		this.critic = critic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int votes) {
		this.score = votes;
	}
	
	public Set<Vote> getVotes() {
		return votes;
	}

	public void setVotes(Set<Vote> votes) {
		this.votes = votes;
	}
	
	public String toString() {
		return String.format("Comment{id=%d, post.path=%s, anchor.id=%d, critic.handle=%s, content=%s, creationDate=%s, score=%d}",
				id, post.getPath(), (anchor != null) ? anchor.getId() : null, critic.getHandle(), content, creationDate.toString(), score);
	}
	
	public boolean equals(Object o) {
		if (o instanceof Comment) {
			return this.toString().equals(o.toString());
		}
		return false;
	}
}
