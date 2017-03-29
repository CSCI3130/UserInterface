/**
 * JPALicenseConnector.java
 * Created March ??, 2017
 */
package com.piccritic.database.license;

import com.piccritic.database.JPAConnector;
import com.vaadin.addon.jpacontainer.EntityItem;

/**
 * This class enables a connection to the database using JPAContainers. It has a method for selecting a license from the database
 * and a method to verify if a license exists. Implements {@link LicenseConnector}. Extends {@link JPAConnector}.
 * @author Damien Robichaud
 */
public class JPALicenseConnector extends JPAConnector<License> implements LicenseConnector {
	
	/**
	 * Initializes the JPAContainer for this Connector.
	 */
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
	
	/**
	 * Checks if the given type of license exists.
	 * @param licenseType - The type of license.
	 * @return true if the license exists. False otherwise.
	 */
	private boolean exists(String licenseType) {
		EntityItem<License> licenseItem = container.getItem(licenseType);
		return licenseItem != null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.license.LicenseConnector#selectLicense(java.lang.String)
	 */
	public License selectLicense(String licenseType) {
		EntityItem<License> licenseItem = container.getItem(licenseType);
		return (licenseItem != null) ? licenseItem.getEntity() : null;
	}
}
