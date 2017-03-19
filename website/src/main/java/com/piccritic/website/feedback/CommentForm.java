package com.piccritic.website.feedback;

import com.piccritic.compute.feedback.FeedbackServiceInterface;
import com.piccritic.compute.user.UserService;
import com.piccritic.database.feedback.Comment;
import com.piccritic.database.feedback.CommentException;
import com.piccritic.database.post.Post;
import com.piccritic.website.login.LoginService;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Window;

public class CommentForm extends FormLayout {
	private Label formLabel = new Label("Add Comments");
	private TextArea commentContent = new TextArea();
	private Button login = new Button("Submit Comment", event -> {
		try {
			submitComment(event);
		} catch (CommentException e) {
			e.printStackTrace();
		}
	});
	private Post post;
	private UserService us = UserService.createService();
	private FeedbackServiceInterface fs;

	public CommentForm(FeedbackServiceInterface fs) {
		this.fs = fs;
		commentContent.setRequired(true);
		commentContent.setColumns(30);
		addComponents(formLabel, commentContent, login);
	}
	
	public void setPost(Post post) {
		this.post = post;
	}
	
	public void submitComment(Button.ClickEvent event) throws CommentException {
		commentContent.validate();
		
		Comment comment = new Comment();
		
		if (post != null) {
			comment.setPost(post);
			comment.setContent(commentContent.getValue());
			comment.setCritic(us.select(LoginService.getHandle()));
			fs.insertComment(comment);
			Page.getCurrent().reload();
		}
		
		commentContent.setValue("");
	}
}
