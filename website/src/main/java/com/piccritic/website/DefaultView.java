package com.piccritic.website;


import java.util.ArrayList;
import java.util.List;

import com.piccritic.compute.MasterService;
import com.piccritic.database.post.Post;
import com.piccritic.database.post.PostConnector.PostSortOption;
import com.piccritic.database.post.PostException;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * This class implements the home page for the PicCritic App
 * 
 * @author Damien Robichaud <br> Francis Bosse
 *
 */
public class DefaultView extends PostQuickView implements View {
	List<Post> posts = new ArrayList<>();
	
	 public DefaultView(){
		try {
			//TODO
			posts.addAll(MasterService.postService.getPosts(9));
		} catch (PostException e) {
			Notification.show(e.getLocalizedMessage(), Type.WARNING_MESSAGE);
			e.printStackTrace();
		}
		initPosts(posts);
	 }
	
	
	public static final String NAME = "home";
	

	@Override
	public void enter(ViewChangeEvent event) {
		String sortOption = event.getParameters();
		PostSortOption option = null;
		
		if (sortOption.equals("Title")) {
			option = PostSortOption.TITLE;
		} else if (sortOption.equals("UploadDate")) {
			option = PostSortOption.UPLOAD_DATE;
		} else if (sortOption.equals("License")) {
			option = PostSortOption.LICENSE;
		}
		posts.clear();
		
		try {
			if (option != null) {
				posts.addAll(MasterService.postService.getPosts(9, option));
			} else {
				posts.addAll(MasterService.postService.getPosts(9));
			}
		} catch (PostException e) {
			e.printStackTrace();
		}
		initPosts(posts);
	}

}
