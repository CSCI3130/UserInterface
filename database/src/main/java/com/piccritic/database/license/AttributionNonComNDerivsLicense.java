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
public class AttributionNonComNDerivsLicense extends License {
	public AttributionNonComNDerivsLicense() {
		licenseType = ATTRIBUTION_NON_COMMERCIAL_NO_DERIVS;
		description = DATTRIBUTION_NON_COMMERCIAL_NO_DERIVS;
	}
}
