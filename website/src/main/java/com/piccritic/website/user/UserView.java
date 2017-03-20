package com.piccritic.website.user;

import com.piccritic.compute.user.UserService;
import com.piccritic.compute.user.UserServiceInterface;
import com.piccritic.database.post.Album;
import com.piccritic.database.post.Post;
import com.piccritic.database.user.Critic;
import com.piccritic.website.PostQuickView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class implements the users posts page
 * 
 * @author ian-dawson <br> Damien Robichaud
 *
 */

public class UserView extends PostQuickView implements View {
	
	List<Post> posts;
	
	public static final String NAME = "user";

	@Override
	public void enter(ViewChangeEvent event) {
		if (event.getParameters() != null && event.getParameters() != "") {
			UserServiceInterface service = UserService.createService();
			Critic critic = service.select(event.getParameters());
			if (critic != null) {
				Iterator<Album> i = critic.getAlbums().iterator();
				if (i.hasNext()) {
					Album a = i.next();
					posts = new ArrayList<Post>(a.getPosts());
					if (!posts.isEmpty()) {
						initPosts(posts);
					} else {
						Notification.show("This user has no posts", Type.WARNING_MESSAGE);
					}
				}
			} else {
				Notification.show("Invalid user", Type.WARNING_MESSAGE); //critic is null
			}
		} else {
			Notification.show("Invalid user", Type.WARNING_MESSAGE); //invalid parameters
		}
		
	}

}
