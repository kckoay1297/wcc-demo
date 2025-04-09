package com.wcc.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.wcc.demo.model.distance.service.PostcodeService;

import jakarta.annotation.PreDestroy;

@SpringBootApplication
public class DemoSpringBootApplication implements CommandLineRunner{

    @Autowired
    private PostcodeService postcodeService;
    
    private final static String postalFilePath = "/data/postal.csv";
    
	public static void main(String[] args) {
		SpringApplication.run(DemoSpringBootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
    	postcodeService.loadExcelCreatePostcode(postalFilePath);

    }
	
    @PreDestroy
    public void onShutdown()  {
    	//postcodeService.replaceDataIntoExcel(postalFilePath);
    }
}
