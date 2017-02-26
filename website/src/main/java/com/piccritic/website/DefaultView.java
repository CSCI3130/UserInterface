package com.piccritic.website;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class DefaultView extends Panel implements View {
	
	public static final String NAME = "home";

	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();
		setContent(new Label("Welcome to Pic Critic!!"));
	}

}
