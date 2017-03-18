package com.piccritic.database.feedback;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
/**
 * 
 * @author Frank Bosse
 *
 */
@Entity
public class RatingAspects {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	private Double lighting;
	
	@NotNull
	private Double composition;
	
	@NotNull
	private Double color;
	
	@NotNull
	private Double focus;
	
	@NotNull
	private Double exposure;
	
	/*@OneToOne(mappedBy="ratin@OneToOne(mappedBy="rating") @NotNull
	private Critic rater;g") @NotNull
	private Critic rater;*/
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getLighting() {
		return lighting;
	}

	public void setLighting(Double lighting) {
		this.lighting = lighting;
	}

	public Double getComposition() {
		return composition;
	}

	public void setComposition(Double composition) {
		this.composition = composition;
	}

	public Double getColor() {
		return color;
	}

	public void setColor(Double color) {
		this.color = color;
	}

	public Double getFocus() {
		return focus;
	}

	public void setFocus(Double focus) {
		this.focus = focus;
	}

	public Double getExposure() {
		return exposure;
	}

	public void setExposure(Double exposure) {
		this.exposure = exposure;
	}

	/*public Critic getRater() {
		return rater;
	}

	public void setRater(Critic rater) {
		this.rater = rater;
	}*/

	public Double getAverage() {
		return (lighting + composition + color + focus + exposure)/5;
	}
}
