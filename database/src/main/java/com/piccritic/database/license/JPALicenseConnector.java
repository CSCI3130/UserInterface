/**
 *
 */
package com.piccritic.database.license;

import com.piccritic.database.JPAConnector;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

public class JPALicenseConnector extends JPAConnector implements LicenseConnector {
	
	private static JPAContainer<License> licenses;

	public JPALicenseConnector() {
		licenses = JPAContainerFactory.make(License.class, entity);
		if (!exists(License.ATTRIBUTION)) {
			licenses.addEntity(new AttributionLicense());
		}
		if (!exists(License.ATTRIBUTION_SHAREALIKE)) {
			licenses.addEntity(new AttributionShareAlikeLicense());
		}
		if (!exists(License.ATTRIBUTION_NO_DERIVS)) {
			licenses.addEntity(new AttributionNoDerivsLicense());
		}
		if (!exists(License.ATTRIBUTION_NON_COMMERCIAL)) {
			licenses.addEntity(new AttributionNonCommercialLicense());
		}
		if (!exists(License.ATTRIBUTION_NON_COMMERCIAL_SHAREALIKE)) {
			licenses.addEntity(new AttributionNonComShareLicense());
		}
		if (!exists(License.ATTRIBUTION_NON_COMMERCIAL_NO_DERIVS)) {
			licenses.addEntity(new AttributionNonComNDerivsLicense());
		}
	}
	
	private static boolean exists(String licenseType) {
		EntityItem<License> licenseItem = licenses.getItem(licenseType);
		return licenseItem != null;
	}
	
	public License selectLicense(String licenseType) {
		EntityItem<License> licenseItem = licenses.getItem(licenseType);
		return (licenseItem != null) ? licenseItem.getEntity() : null;
	}
}
