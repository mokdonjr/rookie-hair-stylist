package yapp.devcamp.hairstylistserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yapp.devcamp.hairstylistserver.dao.ProductOptionRepository;
import yapp.devcamp.hairstylistserver.dao.ProductRepository;
import yapp.devcamp.hairstylistserver.model.Product;
import yapp.devcamp.hairstylistserver.model.ProductOption;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductOptionRepository productOptionRepository;
	
	public Product selectProductByCode(int productCode){
		return productRepository.findByProductCode(productCode);
	}
	
	public ProductOption selectOptionByCode(int optionCode){
		return productOptionRepository.findByOptionCode(optionCode);
	}
}