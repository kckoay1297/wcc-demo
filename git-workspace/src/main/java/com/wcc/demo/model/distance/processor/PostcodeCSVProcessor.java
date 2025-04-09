package com.wcc.demo.model.distance.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.wcc.demo.model.distance.model.PostcodeInfoModel;

@Component
public class PostcodeCSVProcessor {

	private static final Logger logger = LoggerFactory.getLogger(PostcodeCSVProcessor.class);
	
	public List<List<PostcodeInfoModel>> loadBatchDataFromCSV(String filePath) throws IOException {
		logger.info("loadDataFromSheet");
		
		List<List<PostcodeInfoModel>> postCodeInfoModelList = new ArrayList<>();
		
    	ClassPathResource resource = new ClassPathResource(filePath);
    	if(!resource.exists()) {
    		logger.debug("file doesnt exists");
    	}
    	
    	
        try (InputStream is = getClass().getResourceAsStream(filePath);
               BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

               String line;
               boolean isFirst = true;
               final int BATCH_SIZE = 1000;
               int totalLoaded = 0;
               
               while ((line = reader.readLine()) != null) {
            	   // skip header
            	   if (isFirst) { 
                	   isFirst = false; 
                	   continue; 
                   } 
                   
                   List<PostcodeInfoModel> batchList = new ArrayList<>();

                   try {
                       String[] parts = line.split(",");
                       if (parts.length < 4) {
                    	   continue;
                       }

                       String id = parts[0].trim().toUpperCase();
                       String postcode = parts[1].trim().toUpperCase();
                       double latitude = Double.parseDouble(parts[2].trim());
                       double longitude = Double.parseDouble(parts[3].trim());

                       batchList.add(new PostcodeInfoModel(id, postcode, latitude, longitude));
                       
                       if (batchList.size() >= BATCH_SIZE) {
                    	   postCodeInfoModelList.add(batchList);
                           totalLoaded += batchList.size();

                           if (totalLoaded % 10000 == 0) {
                               logger.debug("Loaded " + totalLoaded + " rows so far...");
                           }
                       }
                   }catch(Exception e) {
                	   logger.error(e.getLocalizedMessage());
                	   logger.warn("Skipping bad lines");
                   }

               }

           }
		return postCodeInfoModelList;
        
	}
	
	public List<PostcodeInfoModel> loadDataFromCSV(String filePath) throws IOException {
		logger.info("loadDataFromSheet");
		
		List<PostcodeInfoModel> postCodeInfoModelList = new ArrayList<>();
		
    	ClassPathResource resource = new ClassPathResource(filePath);
    	if(!resource.exists()) {
    		logger.debug("file doesnt exists");
    	}
    	
    	
        try (InputStream is = getClass().getResourceAsStream(filePath);
               BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

               String line;
               boolean isFirst = true;

               while ((line = reader.readLine()) != null) {
                   if (isFirst) { 
                	   isFirst = false; continue; 
                   } // skip header
                   
                   
                   String[] parts = line.split(",");
                   if (parts.length < 4) {
                	   continue;
                   }

                   String id = parts[0].trim().toUpperCase();
                   String postcode = parts[1].trim().toUpperCase();
                   double latitude = Double.parseDouble(parts[2].trim());
                   double longitude = Double.parseDouble(parts[3].trim());

                   postCodeInfoModelList.add(new PostcodeInfoModel(id, postcode, latitude, longitude));
               }

           }
		return postCodeInfoModelList;
        
	}
}
