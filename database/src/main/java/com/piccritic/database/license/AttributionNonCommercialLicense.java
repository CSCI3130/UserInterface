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
public class AttributionNonCommercialLicense extends License {
	public AttributionNonCommercialLicense() {
		licenseType = ATTRIBUTION_NON_COMMERCIAL;
		description = DATTRIBUTION_NON_COMMERCIAL;
	}
}
