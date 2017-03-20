package com.piccritic.database.license;

import javax.persistence.Entity;

/**
 * class definition.
 *
 * class implements the licensing information for a photo.
 *
 * @Author damienr74 <br>
 * teaswirll
 */
@Entity
public class AttributionNoDerivsLicense extends License {
	public AttributionNoDerivsLicense() {
		licenseType = ATTRIBUTION_NO_DERIVS;
		description = DATTRIBUTION_NO_DERIVS;
	}
}
