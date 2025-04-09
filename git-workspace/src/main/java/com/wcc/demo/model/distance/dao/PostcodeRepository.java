package com.wcc.demo.model.distance.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wcc.demo.model.distance.entity.PostcodeEntity;

@Repository
public interface PostcodeRepository extends JpaRepository<PostcodeEntity, String>{
	
	Optional<PostcodeEntity> findByPostcode(String postcode);
}
