package com.piccritic.website.feedback;

import java.util.HashMap;
import java.util.Map;

import com.piccritic.compute.MasterService;
import com.piccritic.database.feedback.Rating;
import com.piccritic.database.feedback.RatingException;
import com.piccritic.database.post.Post;
import com.piccritic.database.user.Critic;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;

public class RatingComponent extends CustomComponent {
	
	private static final String lighting = "Lighting";
	private static final String exposure = "Exposure";
	private static final String composition = "Composition";
	private static final String focus = "Focus";
	private static final String color = "Color";
	private static final String[] categories = { lighting, exposure, composition, focus, color };
	
	private Map<String, Slider> sliders = new HashMap<>();
	private Post post;
	private Critic critic;
	private Rating rating;
	
	/**
	 * Constructs a new rating component for the specified Post. Passing a Post
	 * allows it to pull the correct Rating objects from the database.
	 */
	public RatingComponent(Post post, Critic critic) {
		this.post = post;
		this.critic = critic;
		Panel panel = new Panel();
		GridLayout grid = new GridLayout(3, categories.length + 1);
		grid.setColumnExpandRatio(0, 0.4f);
		grid.setColumnExpandRatio(1, 0.2f);
		grid.setColumnExpandRatio(2, 0.4f);
		panel.setContent(grid);
		
		grid.addComponent(new Panel("Rating Category"), 0, 0);
		grid.addComponent(new Panel("Your Rating"), 1, 0);
		grid.addComponent(new Panel("Average Rating"), 2, 0);
		
		// Compose from multiple components
		for (int i = 0; i < categories.length; i++) {
			Label label = new Label(categories[i]);
			grid.addComponent(label, 0, i + 1);
			
			// Map sliders to strings for easy access
			Slider userRating = new Slider(0, 5);
			sliders.put(categories[i], userRating);
			userRating.setValue(0.0);
			userRating.setResolution(1);
			grid.addComponent(userRating, 1, i + 1);
		}
		
		Rating avgRating = MasterService.feedbackService.getAvgRatings(post);
		Label avgLighting = new Label("" + avgRating.getLighting());
		Label avgExposure = new Label("" + avgRating.getExposure());
		Label avgComposition = new Label("" + avgRating.getComposition());
		Label avgFocus = new Label("" + avgRating.getFocus());
		Label avgColor = new Label("" + avgRating.getColor());
		grid.addComponent(avgLighting, 2, 1);
		grid.addComponent(avgExposure, 2, 2);
		grid.addComponent(avgComposition, 2, 3);
		grid.addComponent(avgFocus, 2, 4);
		grid.addComponent(avgColor, 2, 5);
		rating = MasterService.feedbackService.queryRating(post, critic);
		if (rating != null) {
			setRating(rating);
		}
		
		Button submit = new Button("Submit Rating", e -> {
			try {
				if (rating == null) {
					rating = MasterService.feedbackService.insertRating(getRating());
				} else {
					rating = MasterService.feedbackService.updateRating(getRating());
				}
			} catch (RatingException ex) {
				Notification.show(ex.getLocalizedMessage(), Type.WARNING_MESSAGE);
			}
		});
		
		grid.addComponent(submit);
		
		// Set the size as undefined at all levels
		grid.setSizeFull();
		panel.setSizeFull();
		setSizeFull();
		
		setWidth("40%");
		
		// The composition root MUST be set
		setCompositionRoot(panel);
	}
	
	public Rating getRating() {
		if (rating == null) rating = new Rating();
		rating.setLighting(sliders.get(lighting).getValue());
		rating.setExposure(sliders.get(exposure).getValue());
		rating.setComposition(sliders.get(composition).getValue());
		rating.setFocus(sliders.get(focus).getValue());
		rating.setColor(sliders.get(color).getValue());
		rating.setCritic(critic);
		rating.setPost(post);
		
		return rating;
	}
	
	public void setRating(Rating rating) {
		this.rating = rating;
		if (rating != null) {
			sliders.get(lighting).setValue(rating.getLighting());
			sliders.get(exposure).setValue(rating.getExposure());
			sliders.get(composition).setValue(rating.getComposition());
			sliders.get(focus).setValue(rating.getFocus());
			sliders.get(color).setValue(rating.getColor());
		}
	}
}
