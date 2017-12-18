package yapp.devcamp.hairstylistserver.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import yapp.devcamp.hairstylistserver.exception.StorageException;
import yapp.devcamp.hairstylistserver.exception.StorageFileNotFoundException;
import yapp.devcamp.hairstylistserver.storage.StorageProperties;

@Service
public class StorageService {
	Logger logger = LoggerFactory.getLogger(StorageService.class);

	private final Path rootLocation;

	@Autowired
	public StorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}
	
	public static final String STYLIST_ENROLL_IMAGE_NAME = "qualification.jpg";
	public static final String SHOP_THUMBNAIL_IMAGE_NAME = "thumbnail.jpg";

	/*
	 * [multipartfiles store strategies]
	 * 
	 * 1. /upload/{스타일리스트id}/qualification.jpg 
	 * 2. /upload/{스타일리스트id}/{샵name}/thumbnail.jpg , 포트폴리오 사진들.jpg 
	 * 3. /upload/{스타일리스트id}/postscript/{샵name}/유저id_후기 이미지.jpg
	 *
	 * ...
	 */
	public void storeStylistEnrollImage(int stylist_code, MultipartFile file) {

//		String filename = StringUtils.cleanPath(file.getOriginalFilename());
//		//사진 이름 변경 - Lookies_20170923_밀리세컨드.확장자
//		Calendar cal = Calendar.getInstance();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		String dateTime = sdf.format(cal.getTime());
//		dateTime += "_" + System.currentTimeMillis();
//		
//		String[] subStr = filename.split("\\.");
//		if(subStr.length > 1){
//			subStr[0] = "Loolies_" + dateTime; // ?? 자격증사진도 Loolies?
//			filename = subStr[0]+"."+subStr[subStr.length-1];
//		} 
		
		String filename = STYLIST_ENROLL_IMAGE_NAME;
		
		Path stylistEnrollImageLocation = this.rootLocation.resolve(String.valueOf(stylist_code));

		try {
			if (filename.contains("..")) { // security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory " + filename);
			}
			logger.warn("storeStylistEnrollImage메서드 : " + stylistEnrollImageLocation.resolve(filename).toString());

			Files.createDirectories(stylistEnrollImageLocation);// mkdir

			Files.copy(file.getInputStream(), stylistEnrollImageLocation.resolve(filename),	StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}

//	public void storeShopImage(int stylist_code, String shop_name, MultipartFile file,int index) {
//		String filename="";
//		if(index !=0){
//			filename = StringUtils.cleanPath(file.getOriginalFilename());
//			Calendar cal = Calendar.getInstance();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//			String dateTime= sdf.format(cal.getTime());
//			dateTime += "_"+System.currentTimeMillis();
//			
//			String[] subStr = filename.split("\\.");
//			if(subStr.length>1){
//				subStr[0] = "Loolies_"+dateTime;
//				filename = subStr[0]+"."+subStr[subStr.length-1];
//			} 
//		}
//		else
//			filename =StringUtils.cleanPath("thumnail.jpg");
//		
//		Path shopImageLocation = this.rootLocation.resolve(String.valueOf(stylist_code)).resolve(shop_name);
//		
//		try {
//			if (filename.contains("..")) { // security check
//				throw new StorageException("Cannot store file with relative path outside current directory " + filename);
//			}
//			
//			logger.warn("storeShopImage메서드 : " + shopImageLocation.resolve(filename).toString());
//
//			Files.createDirectories(shopImageLocation);// mkdir
//			
//			Files.copy(file.getInputStream(), shopImageLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
//
//		} catch (DirectoryNotEmptyException e){} 
//		catch (IOException e) {
//			throw new StorageException("Failed to store file " + filename, e);
//		} 
//	}
	
	
	public void storeShopImage(int stylist_code, String shop_name, MultipartFile file) {
		
		String filename = SHOP_THUMBNAIL_IMAGE_NAME;
		
		Path shopImageLocation = this.rootLocation.resolve(String.valueOf(stylist_code)).resolve(shop_name);
		try {
			if (filename.contains("..")) { // security check
				throw new StorageException("Cannot store file with relative path outside current directory " + filename);
			}
			
			logger.warn("storeShopImage메서드 : " + shopImageLocation.resolve(filename).toString());

			Files.createDirectories(shopImageLocation);// mkdir
			
			Files.copy(file.getInputStream(), shopImageLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

		} catch (DirectoryNotEmptyException e){} 
		catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		} 
	}

	public void storePortfolioImage(int stylist_code, String shop_name, MultipartFile file) {
		
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateTime = sdf.format(cal.getTime());
		dateTime += "_" + System.currentTimeMillis();
		
		String[] subStr = filename.split("\\.");
		if(subStr.length > 1){
			subStr[0] = "Rookies_" + dateTime;
			filename = subStr[0] + "." + subStr[subStr.length-1];
		}
		
		Path portfolioImageLocation = this.rootLocation.resolve(String.valueOf(stylist_code)).resolve(shop_name);
		
		try {
			if (filename.contains("..")) { // security check
				throw new StorageException("Cannot store file with relative path outside current directory " + filename);
			}
			
			logger.warn("storePortfolioImage 메서드 : " + portfolioImageLocation.resolve(filename).toString());

			Files.createDirectories(portfolioImageLocation);// mkdir
			
			Files.copy(file.getInputStream(), portfolioImageLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

		} catch (DirectoryNotEmptyException e){} 
		catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		} 
	}
	
	

	public String storePostscriptImage(int stylist_code, String shop_name, MultipartFile file) {

		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateTime = sdf.format(cal.getTime());
		dateTime += "_" + System.currentTimeMillis();
		
		String[] subStr = filename.split("\\.");
		if(subStr.length > 1){
			subStr[0] = "Rookies_" + dateTime;
			filename = subStr[0] + "." + subStr[subStr.length-1];
		}
		
		Path postscriptImageLocation = this.rootLocation.resolve(String.valueOf(stylist_code)).resolve("postscript").resolve(shop_name);

		try {
			if (filename.contains("..")) { // security check
				throw new StorageException("Cannot store file with relative path outside current directory " + filename);
			}
			logger.warn("storePostscriptImage메서드 : " + postscriptImageLocation.resolve(filename).toString());

			Files.createDirectories(postscriptImageLocation);// mkdir

			Files.copy(file.getInputStream(), postscriptImageLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

		}catch (DirectoryNotEmptyException e){} 
		catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
		
		return filename;
	}
	
//	public Path stylistLoad(int stylistCode){
//		return this.rootLocation.resolve(String.valueOf(stylistCode));
//	}
	
	public Path shopLoad(int stylistCode,String shopName){
		
		return this.rootLocation.resolve(String.valueOf(stylistCode)).resolve(shopName);
	}
	
	public Path loadPostscriptImg(int stylistCode,String shopName, String filename){
		return this.rootLocation.resolve(String.valueOf(stylistCode)).resolve("postscript").resolve(shopName).resolve(filename);
	}
	
	public Path loadStylistEnrollImage(int stylistCode){
		return this.rootLocation.resolve(String.valueOf(stylistCode)).resolve(STYLIST_ENROLL_IMAGE_NAME);
	}
	
	public Path loadShopImage(int stylistCode, String shopName,String filename){
		return this.rootLocation.resolve(String.valueOf(stylistCode)).resolve(shopName).resolve(filename);
		
	}
//	public Path loadShopImage(int stylistCode, String shopName){
//		return this.rootLocation.resolve(String.valueOf(stylistCode)).resolve(shopName).resolve(SHOP_THUMBNAIL_IMAGE_NAME);
//	}
	
	public Stream<Path> loadAllShopImages(){ // get all thumbnails
		try{
			return Files.walk(this.rootLocation, 3)
					.filter(path -> path.toString().contains(SHOP_THUMBNAIL_IMAGE_NAME))
					.map(path -> this.rootLocation.relativize(path));
		} catch (IOException e) {
			throw new StorageException("Failed to read stored all shop images(thumbnail.jpg)");
		}
	}
	
	public Stream<Path> loadImagesByLocation(Path location) {
        try {
            return Files.walk(location, 1)
                    .filter(path -> !path.equals(location))
                    .map(path -> location.relativize(path));
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

	public Resource loadStylistEnrollImageAsResource(String filename, int stylistCode) {
		try {
			Path file = loadStylistEnrollImage(stylistCode);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file : " + filename);
			}

		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file : " + filename, e);
		}
	}
	
	public Resource loadShopImageAsResource(String filename, int stylistCode, String shopName) {
		try {
			Path file = loadShopImage(stylistCode, shopName,filename);
			//logger.warn("loadShopImageAsResources 메서드 : " + file.toUri().toString());
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file : " + filename);
			}

		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file : " + filename, e);
		}
	}
	
	public Resource loadPostscriptImgAsResource(String filename,int stylistCode,String shopName) {
		try {
			Path file = loadPostscriptImg(stylistCode, shopName, filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file : " + filename);
			}

		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file : " + filename, e);
		}
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	public void init() {
		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

}
