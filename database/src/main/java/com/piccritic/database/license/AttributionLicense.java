package com.piccritic.database.license;

import javax.persistence.Entity;

/**
 * AttributionLicense class definition.
 *
 * class implements the licensing information for a photo.
 *
 * @Author damienr74 <br>
 * teaswirll
 */
@Entity
public class AttributionLicense extends License {
	public AttributionLicense() {
		licenseType = ATTRIBUTION;
		description = DATTRIBUTION;
	}
}
