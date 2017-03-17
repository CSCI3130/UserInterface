package com.piccritic.website.licence;

import java.util.ArrayList;
import java.util.Collection;

import com.piccritic.database.license.AttributionLicense;
import com.piccritic.database.license.AttributionNoDerivsLicense;
import com.piccritic.database.license.AttributionNonComNDerivsLicense;
import com.piccritic.database.license.AttributionNonComShareLicence;
import com.piccritic.database.license.AttributionNonCommercialLicense;
import com.piccritic.database.license.AttributionShareAlikeLicense;
import com.piccritic.database.license.License;
import com.vaadin.ui.ComboBox;

public class LicenseChooser extends ComboBox {
	static String caption = "Licence";
	static Collection<License> licenses;

	static {
		licenses = new ArrayList<>(6);
		licenses.add(new AttributionLicense());
		licenses.add(new AttributionShareAlikeLicense());
		licenses.add(new AttributionNoDerivsLicense());
		licenses.add(new AttributionNonCommercialLicense());
		licenses.add(new AttributionNonComShareLicence());
		licenses.add(new AttributionNonComNDerivsLicense());
	}

	public LicenseChooser() {
		super(caption, licenses);
		setTextInputAllowed(false);
		setSizeFull();
	}
	
	public License getValue() {
		return (License) super.getValue();
	}
}
