/**
 * @author damienr74, ian-dawson
 */

package com.piccritic.database.tag;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.piccritic.database.post.Post;


@Entity
public class Tag {
	
	@Id
	@Size(max=15)
	@NotNull
	@Pattern(regexp="[a-z]+")
	private String tag;	
	
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<Post> posts;
	
	public String getTag() {
		return tag;
	}



	public void setTag(String tag) {
		
		this.tag = tag;
	}



	public Set<Post> getPosts() {
		return posts;
	}



	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}


}
