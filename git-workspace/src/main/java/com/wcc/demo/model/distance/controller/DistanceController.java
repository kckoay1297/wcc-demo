package com.wcc.demo.model.distance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcc.demo.model.distance.model.DistanceInfoModel;
import com.wcc.demo.model.distance.service.DistanceService;


@RestController
@RequestMapping("/api/distance")
public class DistanceController {
    private final DistanceService distanceService;

    public DistanceController(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    @GetMapping("/findDistance")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> findDistance(@RequestBody DistanceInfoModel distanceInfoModell) {
        return distanceService.findDistance(distanceInfoModell);
    }
}
