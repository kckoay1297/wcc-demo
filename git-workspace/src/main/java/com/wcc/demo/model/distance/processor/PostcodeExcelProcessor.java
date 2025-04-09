package com.wcc.demo.model.distance.processor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.wcc.demo.model.distance.entity.PostcodeEntity;
import com.wcc.demo.model.distance.model.PostcodeInfoModel;

@Service
public class PostcodeExcelProcessor {

	private static final Logger logger = LoggerFactory.getLogger(PostcodeExcelProcessor.class);
	
    public List<PostcodeInfoModel> loadDataFromSheet(String filePath) throws IOException {
    	IOUtils.setByteArrayMaxOverride(300_000_000);
    	
    	List<PostcodeInfoModel> postCodeInfoModelList = new ArrayList<>();

    	logger.info("loadDataFromSheet");
    	
    	ClassPathResource resource = new ClassPathResource(filePath);
    	if(!resource.exists()) {
    		logger.debug("file doesnt exists");
    	}
    	
        try (InputStream is = getClass().getResourceAsStream(filePath)) {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header

                String id = String.valueOf(row.getCell(0).getNumericCellValue());
                id = id.contains(".") ? id.substring(0, id.indexOf(".")) : id;
                String postcode = row.getCell(1).getStringCellValue().trim().toUpperCase();
                double lat = row.getCell(2).getNumericCellValue();
                double lon = row.getCell(3).getNumericCellValue();
                
                PostcodeInfoModel postcodeInfoModel = new PostcodeInfoModel(id, postcode, lat, lon);
                postCodeInfoModelList.add(postcodeInfoModel);
            }
            
            workbook.close();

        } catch (IOException e) {
			e.printStackTrace();
			throw e;
		} 
        
        return postCodeInfoModelList;
    }
    
    public void loadDataToSheet(List<PostcodeEntity> postcodeList, String filePath) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Postal Info");

        // Create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Postal Code");
        header.createCell(2).setCellValue("Latitude");
        header.createCell(3).setCellValue("Longitude");

        // Write data
        int rowNum = 1;
        for (PostcodeEntity postcodeInfoModel : postcodeList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(postcodeInfoModel.getId());
            row.createCell(1).setCellValue(postcodeInfoModel.getPostcode());
            row.createCell(2).setCellValue(postcodeInfoModel.getLatitude());
            row.createCell(3).setCellValue(postcodeInfoModel.getLongitude());
        }

        FileOutputStream fileOut = new FileOutputStream(filePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }
    
}
