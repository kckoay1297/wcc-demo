package com.wcc.demo.model.distance.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wcc.demo.model.distance.dao.PostcodeRepository;
import com.wcc.demo.model.distance.entity.PostcodeEntity;
import com.wcc.demo.model.distance.model.PostcodeInfoModel;
import com.wcc.demo.model.distance.processor.PostcodeCSVProcessor;
import com.wcc.demo.model.distance.processor.PostcodeExcelProcessor;



@Service
@Transactional
public class PostcodeService {

	private static final Logger logger = LoggerFactory.getLogger(PostcodeService.class);
			
	private final PostcodeRepository postcodeRepository; 
	private final PostcodeExcelProcessor postcodeExcelProcessor;
	private final PostcodeCSVProcessor postcodeCSVProcessor;
	
	private final int BATCH_SIZE = 1000;
	
	public PostcodeService(PostcodeRepository postcodeRepository, PostcodeExcelProcessor postcodeExcelProcessor, PostcodeCSVProcessor postcodeCSVProcessor) {
		this.postcodeRepository = postcodeRepository;
		this.postcodeExcelProcessor = postcodeExcelProcessor;
		this.postcodeCSVProcessor = postcodeCSVProcessor;
	}
	
	@Transactional(readOnly = true)
	public PostcodeInfoModel findPostcodeById(String id) {
		Optional<PostcodeEntity> postcodeEntity = postcodeRepository.findById(id);
		if(postcodeEntity.isPresent()) {
			PostcodeInfoModel postcodeInfoModel = new PostcodeInfoModel();
			BeanUtils.copyProperties(postcodeEntity.get(), postcodeInfoModel);
			return postcodeInfoModel;
		} else {
			return null;
		}
	}
	
	@Transactional(readOnly = true)
	public PostcodeInfoModel findPostcodeByPostcode(String postcode) {
		Optional<PostcodeEntity> postcodeEntity = postcodeRepository.findByPostcode(postcode);
		if(postcodeEntity.isPresent()) {
			PostcodeInfoModel postcodeInfoModel = new PostcodeInfoModel();
			BeanUtils.copyProperties(postcodeEntity.get(), postcodeInfoModel);

			return postcodeInfoModel;
		} else {
			return null;
		}
	}
	
	public PostcodeInfoModel createPostcode(PostcodeInfoModel postcodeInfoModel) {
		PostcodeEntity postcodeEntity = new PostcodeEntity();
		BeanUtils.copyProperties(postcodeInfoModel, postcodeEntity);
		
		if(!postcodeRepository.existsById(postcodeEntity.getPostcode())){
			postcodeRepository.save(postcodeEntity);
		} else {
			return null;
		}
		
		return postcodeInfoModel;
	}
	
	public PostcodeInfoModel updatePostcode(PostcodeInfoModel postcodeInfoModel) {
		PostcodeEntity postcodeEntity = new PostcodeEntity();
		BeanUtils.copyProperties(postcodeInfoModel, postcodeEntity);
		
		if(postcodeRepository.existsById(postcodeEntity.getId())){
			postcodeRepository.save(postcodeEntity);
		} else {
			return null;
		}
		
		return postcodeInfoModel;
	}
	
	public void deleteAllPostcode() {
		postcodeRepository.deleteAll();
	}
	
	public void loadExcelCreatePostcode(String filePath) throws IOException{
		logger.info("loadExcelCreatePostcode");
		
		long dataBaseCount = postcodeRepository.count();
		logger.info("dataBaseCount= {}", dataBaseCount);

		if(dataBaseCount <= 0) {
			//List<PostcodeInfoModel> postCodeList = postcodeExcelProcessor.loadDataFromSheet(filePath);
			List<PostcodeInfoModel> postCodeList = postcodeCSVProcessor.loadDataFromCSV(filePath);

			if(CollectionUtils.isNotEmpty(postCodeList))
			{
				logger.debug("postCodeListCont= {}", postCodeList.size());

				List<PostcodeEntity> postcodeEntityList = postCodeList.stream()
				        .map(dto -> {
				        	PostcodeEntity entity = new PostcodeEntity();
				            BeanUtils.copyProperties(dto, entity);
				            return entity;
				        })
				        .collect(Collectors.toList());
				
				postcodeRepository.saveAll(postcodeEntityList);
			}
		}


	}
	
	public void replaceDataIntoExcel(String filePath) {
		logger.info("replaceDataIntoExcel");
        List<PostcodeEntity> postcodeList = postcodeRepository.findAll();

        if(CollectionUtils.isNotEmpty(postcodeList))
        {
        	try {
				postcodeExcelProcessor.loadDataToSheet(postcodeList, filePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
}
