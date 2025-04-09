package com.wcc.demo.model.distance.model;

import java.io.Serializable;

public class DistanceInfoModel implements Serializable{

	private static final long serialVersionUID = -7781151184447558235L;

	private PostcodeInfoModel postcodeInfoFrom;
	private PostcodeInfoModel postcodeInfoTo;
	private Double distance;
	private String unit;
	
	public DistanceInfoModel(PostcodeInfoModel postcodeInfoFrom, PostcodeInfoModel postcodeInfoTo, Double distance, String unit) {
		this.postcodeInfoFrom = postcodeInfoFrom;
		this.postcodeInfoTo = postcodeInfoTo;
		this.distance = distance;
		this.unit = unit;
	}

	public PostcodeInfoModel getPostcodeInfoFrom() {
		return postcodeInfoFrom;
	}

	public void setPostcodeInfoFrom(PostcodeInfoModel postcodeInfoFrom) {
		this.postcodeInfoFrom = postcodeInfoFrom;
	}

	public PostcodeInfoModel getPostcodeInfoTo() {
		return postcodeInfoTo;
	}

	public void setPostcodeInfoTo(PostcodeInfoModel postcodeInfoTo) {
		this.postcodeInfoFrom = postcodeInfoTo;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
	
}
