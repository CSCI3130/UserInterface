/**
 *
 */
package com.piccritic.database.license;

import com.piccritic.database.JPAConnector;
import com.vaadin.addon.jpacontainer.EntityItem;

public class JPALicenseConnector extends JPAConnector<License> implements LicenseConnector {
	

	public JPALicenseConnector() {
		super(License.class);
		if (!exists(License.ATTRIBUTION)) {
			container.addEntity(new AttributionLicense());
		}
		if (!exists(License.ATTRIBUTION_SHAREALIKE)) {
			container.addEntity(new AttributionShareAlikeLicense());
		}
		if (!exists(License.ATTRIBUTION_NO_DERIVS)) {
			container.addEntity(new AttributionNoDerivsLicense());
		}
		if (!exists(License.ATTRIBUTION_NON_COMMERCIAL)) {
			container.addEntity(new AttributionNonCommercialLicense());
		}
		if (!exists(License.ATTRIBUTION_NON_COMMERCIAL_SHAREALIKE)) {
			container.addEntity(new AttributionNonComShareLicense());
		}
		if (!exists(License.ATTRIBUTION_NON_COMMERCIAL_NO_DERIVS)) {
			container.addEntity(new AttributionNonComNDerivsLicense());
		}
	}
	
	private boolean exists(String licenseType) {
		EntityItem<License> licenseItem = container.getItem(licenseType);
		return licenseItem != null;
	}
	
	public License selectLicense(String licenseType) {
		EntityItem<License> licenseItem = container.getItem(licenseType);
		return (licenseItem != null) ? licenseItem.getEntity() : null;
	}
}
