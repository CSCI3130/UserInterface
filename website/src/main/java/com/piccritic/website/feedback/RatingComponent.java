package com.piccritic.website.feedback;

import java.util.Random;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;

public class RatingComponent extends CustomComponent {
	private String[] categories = {"Lighting", "Exposure", "Composition", "Focus", "Color"};
	Random random = new Random();
	
	public RatingComponent() {
		Panel panel = new Panel();
        GridLayout grid = new GridLayout(3, categories.length+1);
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
        	grid.addComponent(label, 0, i+1);
        	
        	Slider userRating = new Slider(0, 5);
            userRating.setValue(0.0);
            userRating.setResolution(1);
            grid.addComponent(userRating, 1, i+1);

            
        	Label avgRating = new Label(("" + random.nextDouble()*5).substring(0, 4));
            grid.addComponent(avgRating, 2, i+1);
        }
        
        // Set the size as undefined at all levels
        grid.setSizeFull();
        panel.setSizeFull();
        setSizeFull();
        
        setWidth("40%");

        // The composition root MUST be set
        setCompositionRoot(panel);
	}
}
