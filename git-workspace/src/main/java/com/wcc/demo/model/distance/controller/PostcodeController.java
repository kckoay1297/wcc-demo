package com.wcc.demo.model.distance.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wcc.demo.model.distance.model.PostcodeInfoModel;
import com.wcc.demo.model.distance.service.PostcodeService;

@RestController
@RequestMapping("/api/postalcode")
public class PostcodeController {

	private final PostcodeService postService;
	
	public PostcodeController(PostcodeService postService) {
		this.postService = postService;
	}

	@GetMapping("/findPostCodeById")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<?> findPostcodeById(@RequestParam String id) {
		PostcodeInfoModel postcodeInfoModel = postService.findPostcodeById(id);
		if(postcodeInfoModel != null) {
			return ResponseEntity.ok(postcodeInfoModel);
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/findPostCode")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<?> findPostCode(@RequestParam String postCode) {
		PostcodeInfoModel postcodeInfoModel = postService.findPostcodeByPostcode(postCode);
		if(postcodeInfoModel != null) {
			return ResponseEntity.ok(postcodeInfoModel);
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/updatePostcode")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updatePostcode(@RequestBody PostcodeInfoModel postcodeInfoModel) {
		postcodeInfoModel = postService.updatePostcode(postcodeInfoModel);
		if(postcodeInfoModel != null) {
			return ResponseEntity.ok(postcodeInfoModel);
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	@DeleteMapping("/deleteAllPostcode")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteAllPostcode(@RequestBody PostcodeInfoModel postcodeInfoModel) {
		try {
			postService.deleteAllPostcode();
			return ResponseEntity.ok(postcodeInfoModel);
		}catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}
}

