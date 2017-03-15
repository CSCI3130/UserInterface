package com.piccritic.website;


import java.util.ArrayList;
import java.util.List;

import com.piccritic.database.post.Post;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * This class implements the home page for the PicCritic App
 * 
 * @author Damien Robichaud <br> Francis Bosse
 *
 */
public class DefaultView extends PostQuickView implements View {
	List<Post> posts = new ArrayList<>();
	
	 public DefaultView(){
		 for (int i = 0; i < 9; i++) {
			 posts.add(PicCritic.postService.getPost("users/rezdamir/CUH5F1Z0R8ZvxZG8NacF-kjODTGRWsqW"));
		 }
		 func(posts);
	 }
	
	
	public static final String NAME = "home";
	

	@Override
	public void enter(ViewChangeEvent event) {
		//addComponent(new Label("Welcome to Pic Critic!!"));
	}

}
