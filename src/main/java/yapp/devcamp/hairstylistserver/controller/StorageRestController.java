package yapp.devcamp.hairstylistserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import yapp.devcamp.hairstylistserver.service.StorageService;

@RestController
public class StorageRestController {
	
	@Autowired
	private StorageService storageService;
	
	@GetMapping("/files/{stylistCode}/{filename:.+}")
    public ResponseEntity<Resource> serveStylistEnrollImage(@PathVariable("stylistCode") int stylistCode, @PathVariable String filename) {

        Resource file = storageService.loadStylistEnrollImageAsResource(filename, stylistCode);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
	
	@GetMapping("/files/{stylistCode}/{shopName}/{filename:.+}")
    public ResponseEntity<Resource> serveShopImage(@PathVariable("stylistCode") int stylistCode, @PathVariable("shopName") String shopName, @PathVariable String filename) {

        Resource file = storageService.loadShopImageAsResource(filename, stylistCode, shopName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
	
	@GetMapping("/files/{stylistCode}/postscript/{shopName}/{filename:.+}")
    public ResponseEntity<Resource> servePostscriptImage(@PathVariable("stylistCode") int stylistCode, @PathVariable("shopName") String shopName, @PathVariable String filename) {

        Resource file = storageService.loadPostscriptImgAsResource(filename, stylistCode, shopName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
