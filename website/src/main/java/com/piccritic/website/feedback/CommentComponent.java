package com.piccritic.website.feedback;

import java.util.Random;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class CommentComponent extends CustomComponent {
	private boolean upvoted;
	private boolean downvoted;
	
	public CommentComponent(String author, String commentStr) {
        Random random = new Random();
        upvoted = false;
        downvoted = false;
		
		// A layout structure used for composition
        Panel panel = new Panel(author);
        VerticalLayout panelContent = new VerticalLayout();
        panel.setContent(panelContent);

        // Compose from multiple components
        Label comment = new Label(commentStr);
        panelContent.addComponent(comment);
        
        Button upvote = new Button("Upvote", this::upvote);
        Button downvote = new Button("Downvote", this::downvote);
        Label votes = new Label("" + (random.nextInt() % 200));
        HorizontalLayout row = new HorizontalLayout(upvote, downvote, votes);
        row.setSpacing(true);
        panelContent.addComponent(row);

        // Set the size as undefined at all levels
        panelContent.setSizeFull();
        panel.setSizeFull();
        setSizeFull();
        
        setWidth("70%");

        // The composition root MUST be set
        setCompositionRoot(panel);
    }
	
	public void upvote(Button.ClickEvent event) {
		HorizontalLayout row = (HorizontalLayout) event.getButton().getParent();
		Label votes = (Label) row.getComponent(2);
		
		if (!upvoted && !downvoted) {
			votes.setValue(Integer.toString(Integer.parseInt(votes.getValue()) + 1));
			upvoted = true;
		} else if (!upvoted) {
			votes.setValue(Integer.toString(Integer.parseInt(votes.getValue()) + 2));
			upvoted = true;
			downvoted = false;
		}
		
	}
	
	public void downvote(Button.ClickEvent event) {
		HorizontalLayout row = (HorizontalLayout) event.getButton().getParent();
		Label votes = (Label) row.getComponent(2);
		
		if (!downvoted && !upvoted) {
			votes.setValue(Integer.toString(Integer.parseInt(votes.getValue()) - 1));
			downvoted = true;
		} else if (!downvoted) {
			votes.setValue(Integer.toString(Integer.parseInt(votes.getValue()) - 2));
			downvoted = true;
			upvoted = false;
		}
		
	}
}
