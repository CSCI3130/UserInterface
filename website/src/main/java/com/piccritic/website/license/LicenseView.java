package com.piccritic.website.license;

import com.piccritic.database.license.License;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

public class LicenseView extends VerticalLayout implements View {
	public static final String NAME = "licenses";

	private LicenseChooser lc = new LicenseChooser();
	private Label description = new Label();

	public LicenseView() {
		addComponent(lc);
		Panel p = new Panel();
		addComponent(p);
		setExpandRatio(p, 1);
		lc.setNullSelectionItemId("Select a License for the short description");

		lc.addValueChangeListener(e-> {
			License license = lc.getValue();
			if (license != null) {
				description = new Label(license.getDescription()); 
				description.setContentMode(ContentMode.PREFORMATTED);
				p.setContent(description);
			}
		});
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
