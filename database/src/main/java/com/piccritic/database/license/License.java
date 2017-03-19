package com.piccritic.database.license;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import com.piccritic.database.post.Post;
import com.piccritic.database.user.Critic;

/**
 * License class entity definition.
 *
 * class implements the licensing information for a photo.
 */
@Entity
public abstract class License {

	public static final String ATTRIBUTION = "Attribution";
	public static final String ATTRIBUTION_SHAREALIKE = "Attribution-ShareAlike";
	public static final String ATTRIBUTION_NO_DERIVS = "Attribution-NoDerivs";
	public static final String ATTRIBUTION_NON_COMMERCIAL = "Attribution-NonCommercial";
	public static final String ATTRIBUTION_NON_COMMERCIAL_SHAREALIKE = "Attribution-NonCommercial-ShareAlike";
	public static final String ATTRIBUTION_NON_COMMERCIAL_NO_DERIVS = "Attribution-NonCommercial-NoDerivs";

	public static final String DATTRIBUTION = "You are free to\n\n" +
		"Share - copy and redistribute the material in any medium or format\n" +
		"Adapt - remix, transform, and build upon the material for any purpose, even commercially\n" +
		"Under the following conditions:\n\n" +
		"Attribution - You must give appropriate credit, provide a link to the license,\n"+
		" and indicate if changes where made";

	public static final String DATTRIBUTION_SHAREALIKE = DATTRIBUTION +
		"\nShareAlike - if you remix, transform, or build upon the material, " +
		"you must\n distribute your contributions under the same license as the original";

	public static final String DATTRIBUTION_NO_DERIVS = DATTRIBUTION +
		"\nNoDerivatives - if you remix, transform, or build upon the material," +
		"\nyou may not distribute the modified material.";

	public static final String DATTRIBUTION_NON_COMMERCIAL = "You are free to\n\n"+
		"Share - copy and redistribute the material in any medium or format\n" +
		"Adapt - remix, transform, and build upon the material\n" +
		"Under the following conditions:\n\n" +
		"Attribution - You must give appropriate credit, provide a link to the license,\n " +
		"and indicate if changes where made\n" +
		"NonCommercial - You may not use the material for commercial purposes.";

	public static final String DATTRIBUTION_NON_COMMERCIAL_SHAREALIKE = DATTRIBUTION_NON_COMMERCIAL +
		"\nShareAlike - if you remix, transform, or build upon the material,\n" +
		"you must distribute your contributions under the same license as the original";

	public static final String DATTRIBUTION_NON_COMMERCIAL_NO_DERIVS = DATTRIBUTION_NON_COMMERCIAL +
		"\nNoDerivatives - if you remix, transform, or build upon the material,\n" +
		"you may not distribute the modified material.";
	
	@Id @Size(max= 50)
	protected String licenseType;

	@Size(max = 1024)
	protected String description;

	@OneToMany(mappedBy = "license")
	protected Set<Critic> critics;

	@OneToMany(mappedBy = "license")
	protected Set<Post> posts;
	
	protected License() { } 

	public String getLicenseType() {
		return licenseType;
	}

	public String getDescription() {
		return description;
	}

	public Set<Critic> getCritics() {
		return critics;
	}

	public void setCritics(Set<Critic> critics) {
		this.critics = critics;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	public boolean equals(Object o) {
		if (o != null && o instanceof License) {
			License other = (License) o;
			return other.licenseType.equals(licenseType) &&
				other.description.equals(description);
		}
		
		return false;
	}
	
	public String toString() {
		return licenseType;
	}
}
