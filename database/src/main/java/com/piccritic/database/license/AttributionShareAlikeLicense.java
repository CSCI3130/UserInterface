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
public class AttributionShareAlikeLicense extends License {
	public AttributionShareAlikeLicense() {
		licenseType = ATTRIBUTION_SHAREALIKE;
		description = DATTRIBUTION_SHAREALIKE;
	}
}
