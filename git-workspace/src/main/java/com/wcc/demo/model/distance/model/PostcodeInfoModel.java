package com.wcc.demo.model.distance.model;

import java.io.Serializable;

public class PostcodeInfoModel implements Serializable{
	

	private static final long serialVersionUID = -4676760151927856408L;
	
	private String id;
	private String postcode;
    private Double latitude;
    private Double longitude;

    public PostcodeInfoModel () {}
    
    public PostcodeInfoModel(String id, String postcode, Double latitude, Double longitude) {
    	this.id = id;
        this.postcode = postcode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPostcode() { 
    	return postcode; 
    }
    
    public Double getLatitude() { 
    	return latitude; 
    }
    
    public Double getLongitude() { 
    	return longitude; 
    }

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
    
    
}
