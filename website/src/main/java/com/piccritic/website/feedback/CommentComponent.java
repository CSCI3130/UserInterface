package com.piccritic.website.feedback;

import com.piccritic.compute.feedback.FeedbackService;
import com.piccritic.compute.user.UserService;
import com.piccritic.database.feedback.Comment;
import com.piccritic.database.feedback.Vote;
import com.piccritic.database.feedback.VoteException;
import com.piccritic.database.user.Critic;
import com.piccritic.website.login.LoginService;
import com.piccritic.website.login.LoginService.LoginStatus;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class CommentComponent extends CustomComponent {
	private Comment comment;
	private FeedbackService fs;
	private UserService us;
	private LoginService.LoginStatus loginStatus;
	private Critic critic;
	private boolean upvoted;
	private boolean downvoted;
	private Button upvote;
	private Button downvote;
	
	public CommentComponent(Comment comment, FeedbackService fs, UserService us) {
        this.comment = comment;
        upvoted = false;
        downvoted = false;
        this.fs = fs;
        this.loginStatus = LoginService.getLoginStatus();
        critic = us.select(LoginService.getHandle());
        
		
		// A layout structure used for composition
        Panel panel = new Panel("" + comment.getCritic().getHandle());
        VerticalLayout panelContent = new VerticalLayout();
        panel.setContent(panelContent);
        
        // Compose from multiple components
        Label commentLabel = new Label("" + comment.getContent());
        panelContent.addComponent(commentLabel);
        
        if (loginStatus == LoginStatus.LOGGED_IN) {
	        upvote = new Button("Upvote", event -> {
				try {
					upvote(event);
				} catch (VoteException e) {
					e.getLocalizedMessage();
				}
			});
	        downvote = new Button("Downvote", event -> {
				try {
					downvote(event);
				} catch (VoteException e) {
					e.getLocalizedMessage();
				}
			});
	        Label votes = new Label("" + fs.getScore(comment));
	        HorizontalLayout row = new HorizontalLayout(upvote, downvote, votes);
	        row.setSpacing(true);
	        panelContent.addComponent(row);
	        
	        Vote vote = fs.getVote(critic, comment);
	        if (vote != null) {
	        	if (vote.getRating()) {
	        		upvote.setCaption("UPVOTE");
	        		upvoted = true;
	        	} else {
	        		downvote.setCaption("DOWNVOTE");
	        		downvoted = true;
	        	}
	        }
	        
        } else {
 	        Label votes = new Label("" + fs.getScore(comment));
 	        HorizontalLayout row = new HorizontalLayout(votes);
 	        row.setSpacing(true);
 	        panelContent.addComponent(row);
        }

        // Set the size as undefined at all levels
        panelContent.setSizeFull();
        panel.setSizeFull();
        setSizeFull();
        
        setWidth("70%");

        // The composition root MUST be set
        setCompositionRoot(panel);
    }
	
	public void upvote(Button.ClickEvent event) throws VoteException {
		HorizontalLayout row = (HorizontalLayout) event.getButton().getParent();
		Label votes = (Label) row.getComponent(2);
		
		Vote vote = new Vote();
		vote.setCritic(critic);
		vote.setComment(comment);
		
		if (!upvoted && !downvoted) {
			votes.setValue("" + (Integer.parseInt(votes.getValue()) + 1));
			vote.setRating(true);
			fs.insertVote(vote);
			upvote.setCaption("UPVOTE");
			downvote.setCaption("Downvote");
			upvoted = true;
			downvoted = false;
		} else if (!upvoted) {
			votes.setValue("" + (Integer.parseInt(votes.getValue()) + 2));
			vote.setRating(true);
			fs.insertVote(vote);
			upvote.setCaption("UPVOTE");
			downvote.setCaption("Downvote");
			upvoted = true;
			downvoted = false;
		} else {
			votes.setValue("" + (Integer.parseInt(votes.getValue()) - 1));
			fs.deleteVote(vote);
			upvote.setCaption("Upvote");
			downvote.setCaption("Downvote");
			upvoted = false;
			downvoted = false;
		}
		
	}
	
	public void downvote(Button.ClickEvent event) throws VoteException {
		HorizontalLayout row = (HorizontalLayout) event.getButton().getParent();
		Label votes = (Label) row.getComponent(2);
		
		Vote vote = new Vote();
		vote.setCritic(critic);
		vote.setComment(comment);
		
		if (!downvoted && !upvoted) {
			votes.setValue("" + (Integer.parseInt(votes.getValue()) - 1));
			vote.setRating(false);
			fs.insertVote(vote);
			upvote.setCaption("Upvote");
			downvote.setCaption("DOWNVOTE");
			downvoted = true;
			upvoted = false;
		} else if (!downvoted) {
			votes.setValue("" + (Integer.parseInt(votes.getValue()) - 2));
			vote.setRating(false);
			fs.insertVote(vote);
			upvote.setCaption("Upvote");
			downvote.setCaption("DOWNVOTE");
			downvoted = true;
			upvoted = false;
		} else {
			votes.setValue("" + (Integer.parseInt(votes.getValue()) + 1));
			fs.deleteVote(vote);
			upvote.setCaption("Upvote");
			downvote.setCaption("Downvote");
			downvoted = false;
			upvoted = false;
		}
		
	}
}
