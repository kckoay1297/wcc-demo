package com.wcc.demo.model.distance.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PostcodeEntity {
	
    @Id
    private String id;
    
    private String postcode;
    private double latitude;
    private double longitude;
    
    public PostcodeEntity() {
    	
    }
    
    public PostcodeEntity(String id, String postcode, double latitude, double longitude) {
    	this.id = id;
    	this.postcode = postcode;
    	this.latitude = latitude;
    	this.longitude = longitude;
    }
  
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
    
    
}
