package com.piccritic.website.post;

import java.io.File;
import java.util.List;

import com.piccritic.compute.MasterService;
import com.piccritic.compute.feedback.FeedbackService;
import com.piccritic.compute.feedback.FeedbackServiceInterface;
import com.piccritic.compute.post.PostService;
import com.piccritic.compute.post.PostServiceInterface;
import com.piccritic.compute.user.UserService;
import com.piccritic.compute.user.UserServiceInterface;
import com.piccritic.database.feedback.Comment;
import com.piccritic.database.feedback.CommentException;
import com.piccritic.database.feedback.VoteException;
import com.piccritic.database.post.Post;
import com.piccritic.database.post.PostException;
import com.piccritic.database.user.Critic;
import com.piccritic.website.feedback.CommentComponent;
import com.piccritic.website.feedback.CommentForm;
import com.piccritic.website.feedback.RatingComponent;
import com.piccritic.website.login.LoginService;
import com.piccritic.website.login.LoginService.LoginStatus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This class implements the view post page where the creator
 * can edit/delete and other users cannot. everyone can view.
 * 
 * @author Damien Robichaud <br>
 * 		Francis Bosse
 *
 */
public class ViewPost extends VerticalLayout implements View {
	private Post post; // post object to view
	private Image image = new Image();
	private Label postDescription = new Label();
	private RatingComponent ratings;
	private List<Comment> comments;
	private UserServiceInterface us = MasterService.userService;
	private FeedbackServiceInterface fs = MasterService.feedbackService;
	private CommentForm commentForm = new CommentForm(fs);
	private Label license = new Label();

	private Button delete = new Button("Delete post");

	private Button edit = new Button("Edit post");

	public static final String NAME = "post";

	public ViewPost() {
		addComponent(image);
		addComponent(postDescription);
		addComponent(license);
		setMargin(true);
		setSpacing(true);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		PostServiceInterface service = MasterService.postService;
		post = service.getPost(event.getParameters());
		if (post == null) {
			Notification.show("Error 404"+event.getParameters(), Type.ERROR_MESSAGE);
			return;
		}
		commentForm.setPost(post);
		image.setCaption(post.getTitle());
		image.setSource(new FileResource(new File(post.getPath())));
		image.setSizeFull();
		postDescription.setValue(post.getDescription());
		license.setCaption(post.getLicense().getLicenseType());
		if (event.getParameters().matches("users/" + LoginService.getHandle() + "/.*")) {
			addComponent(delete);
			delete.addClickListener(e -> {
				try {
					service.deletePost(post);
					Notification.show("Post deleted successfully", Type.TRAY_NOTIFICATION);
				} catch (PostException | CommentException | VoteException e1) {
					Notification.show(e1.getLocalizedMessage(), Type.WARNING_MESSAGE);
				}
			});
			addComponent(edit);
			edit.addClickListener(e -> {
				UI.getCurrent().addWindow(new CreatePost(LoginService.getHandle(), post));
			});
		}
		
		if (LoginService.getLoginStatus() == LoginStatus.LOGGED_IN) {
			addComponent(commentForm);
      Critic loggedInUser = MasterService.userService.select(LoginService.getHandle());
      ratings = new RatingComponent(post, loggedInUser);
			addComponent(ratings);
		}
		
		try {
			comments = fs.getComments(post);
		} catch (PostException e) {
			e.printStackTrace();
		}
		
		for (Comment comment : comments) {
			CommentComponent commentComponent = 
					new CommentComponent(comment, fs, us);
			addComponent(commentComponent);
		}
	}

}
