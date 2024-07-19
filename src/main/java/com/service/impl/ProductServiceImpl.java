package com.service.impl;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.model.Product;
import com.repository.ProductRepository;
import com.service.ProductService;


@Service
public class ProductServiceImpl implements ProductService {
	
	

	@Autowired
	private ProductRepository productRepository;
	
	
	
	@Override
	public Product saveProduct(Product product) {
		
		
		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllProducts() {
		
		return productRepository.findAll();
	}

	@Override
	public Boolean deleteProduct(Integer id) {
	Product product=	productRepository.findById(id).orElse(null);
		if(!ObjectUtils.isEmpty(product)) {
			
			productRepository.delete(product);
			return true;
		}
		
		return false;
	}

	@Override
	public Product getProductById(Integer id) {
		 Product product =	productRepository.findById(id).orElse(null);
		return product;
	}

	@Override
	public Product updateProduct(Product product,MultipartFile image)  {
		 Product dbproduct = getProductById(product.getId());
		 
			String imageName =	 image.isEmpty() ? dbproduct.getImage(): image.getOriginalFilename();
			
			dbproduct.setTitle(product.getTitle());
			dbproduct.setDescription(product.getDescription());
			dbproduct.setCategory(product.getCategory());
			dbproduct.setPrice(product.getPrice());
			dbproduct.setStock(product.getStock());
			dbproduct.setImage(imageName);
			dbproduct.setIsActive(product.getIsActive());
			
			dbproduct.setDiscount(product.getDiscount());
			//5=100*(5/100); 100-5=95
		Double discount  =	product.getPrice()*(product.getDiscount()/100.0);
		Double discountPrice  =	product.getPrice()-discount;
			
			dbproduct.setDiscountPrice(discountPrice);
			
			Product updateProduct=productRepository.save(dbproduct);
			if(!ObjectUtils.isEmpty(updateProduct)) {
				if(!image.isEmpty()) {
					try {
					 File saveFile= new ClassPathResource("static/img").getFile();
					 
						Path path=  Paths.get(saveFile.getAbsolutePath()+File.separator+"product"+File.separator+image.getOriginalFilename());
						 
						 Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					}catch(Exception e) {
						e.printStackTrace();
					}
						
				}
				return product;
			}
		return null;
	}

	@Override
	public List<Product> getAllActiveProducts(String category) {
		List<Product> products = null;
		if(ObjectUtils.isEmpty(category)) {
			
		products =	productRepository.findByIsActiveTrue();
		}else {
			products = productRepository.findByCategory(category);
		}
		
		return products;
	}

	  

	 

		@Override
		public List<Product> listAll() {
			  List<Product> products = new ArrayList<>();
		        productRepository.findAll().forEach(products::add); // Java 8 fun
		        return products;
		}

		@Override
		public List<Product> getNewProducts() {
			 return productRepository.findNewProducts();
		}

		@Override
		public List<Product> findNewProducts() {
			return productRepository.findNewProducts();
		}
	
}
