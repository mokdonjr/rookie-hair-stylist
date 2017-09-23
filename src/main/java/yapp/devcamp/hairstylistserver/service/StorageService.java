package yapp.devcamp.hairstylistserver.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

	/*
	 * [multipartfiles store strategies]
	 * 
	 * 1. /upload/{스타일리스트id}/미용사 인증 사진.jpg 
	 * 2. /upload/{스타일리스트id}/{샵name}/thumbnail.jpg , 포트폴리오 사진들.jpg 
	 * 3. /upload/{스타일리스트id}/postscript/{샵name}/유저id_후기 이미지.jpg
	 *
	 * ...
	 */
	public void storeStylistEnrollImage(int stylist_code, MultipartFile file) {

		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		//사진 이름 변경 - Lookies_20170923_밀리세컨드.확장자
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateTime= sdf.format(cal.getTime());
		dateTime += "_"+System.currentTimeMillis();
		
		String[] subStr = filename.split("\\.");
		if(subStr.length>1){
			subStr[0] = "Loolies_"+dateTime;
			filename = subStr[0]+"."+subStr[subStr.length-1];
		} 
		
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

	public void storeShopImage(int stylist_code, String shop_name, MultipartFile file,int index) {
		String filename="";
		if(index !=0){
			filename = StringUtils.cleanPath(file.getOriginalFilename());
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String dateTime= sdf.format(cal.getTime());
			dateTime += "_"+System.currentTimeMillis();
			
			String[] subStr = filename.split("\\.");
			if(subStr.length>1){
				subStr[0] = "Loolies_"+dateTime;
				filename = subStr[0]+"."+subStr[subStr.length-1];
			} 
		}
		else
			filename =StringUtils.cleanPath("thumnail.jpg");
		
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

	public void storePostscriptImage(int stylist_code, String shop_name, int user_id, MultipartFile file) {

		String filename = user_id +"_"+ StringUtils.cleanPath(file.getOriginalFilename());
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateTime= sdf.format(cal.getTime());
		dateTime += "_"+System.currentTimeMillis();
		
		String[] subStr = filename.split("\\.");
		if(subStr.length>1){
			subStr[0] = user_id+"_"+dateTime;
			filename = subStr[0]+"."+subStr[subStr.length-1];
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
	}

	/*
	 * public void store(MultipartFile file){
	 * 
	 * String filename = StringUtils.cleanPath(file.getOriginalFilename());
	 * 
	 * try { if(filename.contains("..")){ // security check throw new
	 * StorageException("Cannot store file with relative path outside current directory "
	 * + filename); } logger.warn("store메서드 : " +
	 * rootLocation.resolve(filename).toString());
	 * Files.copy(file.getInputStream(), this.rootLocation.resolve(filename), //
	 * 'upload\filename' StandardCopyOption.REPLACE_EXISTING);
	 * 
	 * } catch(IOException e) { throw new
	 * StorageException("Failed to store file " + filename, e); } }
	 */

	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}
	
	public Path stylistLoad(int stylistCode){
		return this.rootLocation.resolve(String.valueOf(stylistCode));
	}
	
	public Path shopLoad(int stylistCode,String shopName){
		
		return this.rootLocation.resolve(String.valueOf(stylistCode)).resolve(shopName);
	}
	public Path postscriptLoad(int stylistCode,String shopName){
		return this.rootLocation.resolve(String.valueOf(stylistCode)).resolve("postscript").resolve(shopName);
	}

	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
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
