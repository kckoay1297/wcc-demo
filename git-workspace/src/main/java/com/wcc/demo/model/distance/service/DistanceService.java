package com.wcc.demo.model.distance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wcc.demo.model.distance.model.DistanceInfoModel;
import com.wcc.demo.model.distance.model.PostcodeInfoModel;

@Service
public class DistanceService {
    private static final int EARTH_RADIUS = 6371;

    private static final Logger logger = LoggerFactory.getLogger(DistanceService.class);
    
    private final PostcodeService postcodeService;
    
    private static String defaultUnit = "km";
    
    public DistanceService(PostcodeService postcodeService) {
    	this.postcodeService = postcodeService;
    }

    
    public ResponseEntity<?> findDistance(DistanceInfoModel distanceInfoModel)
    {
    	if(distanceInfoModel != null) {
            if (distanceInfoModel.getPostcodeInfoFrom() == null || distanceInfoModel.getPostcodeInfoTo() == null) {
                return ResponseEntity.badRequest().body("Invalid postcode(s)");
            }
            
            PostcodeInfoModel postalFrom = postcodeService.findPostcodeByPostcode(distanceInfoModel.getPostcodeInfoFrom().getPostcode());
            PostcodeInfoModel postalTo = postcodeService.findPostcodeByPostcode(distanceInfoModel.getPostcodeInfoTo().getPostcode());
            
            if (postalFrom == null || postalTo == null) {
                return ResponseEntity.badRequest().body("Postcode(s) doesn't exist");
            }
            
            logger.info("Postal Code From: {}", postalFrom.getPostcode());
            logger.info("Postal Code To: {}", postalTo.getPostcode());
            
            double distance = calculateDistance(
                    postalFrom.getLatitude(), postalFrom.getLongitude(),
                    postalTo.getLatitude(), postalTo.getLongitude());
            
            distanceInfoModel.getPostcodeInfoFrom().setLatitude(postalFrom.getLatitude());
            distanceInfoModel.getPostcodeInfoFrom().setLongitude(postalFrom.getLongitude());
            distanceInfoModel.getPostcodeInfoTo().setLatitude(postalTo.getLatitude());
            distanceInfoModel.getPostcodeInfoTo().setLongitude(postalTo.getLongitude());
            
            distanceInfoModel.setDistance(distance);
            distanceInfoModel.setUnit(defaultUnit);
            
    		return ResponseEntity.ok(distanceInfoModel);
    	}
    	else {
    		return ResponseEntity.badRequest().body("Invalid request");
    	}

    	
    }
    
    
    private double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
    	double lon1Radians = Math.toRadians(longitude1);
    	double lon2Radians = Math.toRadians(longitude2);
    	double lat1Radians = Math.toRadians(latitude1);
    	double lat2Radians = Math.toRadians(latitude2);
    	double a = haversine(lat1Radians, lat2Radians) + Math.cos(lat1Radians) * Math.cos(lat2Radians) * haversine(lon1Radians, lon2Radians);
    	double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); 	
    	
    	return (EARTH_RADIUS * c);
    	
    }
    
    private double haversine(double deg1, double deg2) {
    	return square(Math.sin((deg1 - deg2) / 2.0));
    }
    
    private double square(double x) {
    	return x * x;
    }
}
