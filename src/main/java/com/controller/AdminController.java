package com.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.model.Category;
import com.model.Product;
import com.model.UserDtls;
import com.service.CartService;
import com.service.CategoryService;
import com.service.ProductService;
import com.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	
	
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartService cartService;
	

	
	@ModelAttribute
	public void getUserDetails(Principal p,Model m) {
		
		if(p!=null) {
			String email = p.getName();
		UserDtls userDtls =	userService.getUserByEmail(email);
		m.addAttribute("user", userDtls);
		Integer countCart =	cartService.getCountCart(userDtls.getId());
		m.addAttribute("countCart", countCart);
		}
		List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		m.addAttribute("categorys", allActiveCategory);
	}
	
	@GetMapping("/")
	public String index() {
		return "admin/index";
	}
	
	
	 @GetMapping("/login")
	 public String admin_login() {
		 return "admin/login";
	 }
	 @GetMapping("/addProduct")
		public String addProduct(Model m) {
		 List<Category> catagories = categoryService.getAllCategory();
		 m.addAttribute("categories", catagories);
			return "admin/add_product";
		}
	 @GetMapping("/category")
		public String category(Model m) {
		 m.addAttribute("categorys", categoryService.getAllCategory());
			return "admin/category";
		}
	 
	 @PostMapping("/saveCategory")
	 public String saveCategory(@ModelAttribute Category category,@RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
		 
		 String imageName= file!=null ? file.getOriginalFilename(): "default.jpg";
		 category.setImageName(imageName);
		 
		 Boolean existCategory =categoryService.existCategory( category.getName());
		 
		 if(existCategory) {
			 session.setAttribute("errorMsg" , "Category name already exists....");
		 }else {
		 Category saveCategory = categoryService.saveCategory(category);
		 
		 if(ObjectUtils.isEmpty(saveCategory)) {
			 session.setAttribute("errorMsg", "Not saved ! internal server error...");
		 }else {
			 
			 File saveFile= new ClassPathResource("static/img").getFile();
			 
			Path path=  Paths.get(saveFile.getAbsolutePath()+File.separator+"product"+File.separator+file.getOriginalFilename());
			 
			 System.out.println(path);
			 Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			 
			 session.setAttribute("succMsg", " Saved successfully ...");
		 }
		    }
		
		 
		 
		 return "redirect:/admin/category";
	 }
	 
	 @GetMapping("/deleteCategory/{id}")
	 public String deleteCategory(@PathVariable int id, HttpSession session) {
		 
		Boolean deleteCategory= categoryService.deleteCategory(id);
		if(deleteCategory) {
			session.setAttribute("succMsg", "category delete successfully....");
		}else {
			session.setAttribute("errorMsg", "something went wrong on server....");
		}
		 
		 return "redirect:/admin/category";
	 }
	 
	 @GetMapping("/editCategory/{id}")
	 public String editCategory(@PathVariable int id, Model m) {
		 m.addAttribute("category", categoryService.getCategoryById(id));
		 
		 return "admin/edit_category";
	 }
	 
	 @PostMapping("/updateCategory")
	 public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,HttpSession session) throws IOException {
		 
		 Category Oldcategory= categoryService.getCategoryById(category.getId());
		 String imageName= file.isEmpty() ? Oldcategory.getImageName() : file.getOriginalFilename();
		 
		 if(!ObjectUtils.isEmpty(category)) {
			 
			 Oldcategory.setName(category.getName());
			 Oldcategory.setIsActive(category.getIsActive());
			 Oldcategory.setImageName(imageName);
		 }
		Category updateCategory= categoryService.saveCategory(Oldcategory);
		
		if(!ObjectUtils.isEmpty(updateCategory)) {
			
			if(!file.isEmpty()) {
				
				 File saveFile= new ClassPathResource("static/img").getFile();
				 
					Path path=  Paths.get(saveFile.getAbsolutePath()+File.separator+"product"+File.separator+file.getOriginalFilename());
					 
					 System.out.println(path);
					 Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			
			session.setAttribute("succMsg", "Category update successfulyy.......");
		}else {
			session.setAttribute("errorMsg", "something went wrong on server....");
		}
		 
		 return "redirect:/admin/editCategory/"+category.getId();
	 }
	 @PostMapping("/saveProduct")
	 public String saveProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile image, HttpSession session) throws IOException {
		 
		String imageName= image.isEmpty() ? "default.jpg": image.getOriginalFilename();
		product.setImage(imageName);
		 product.setDiscount(0);
		 product.setDiscountPrice(product.getPrice());
		 Product saveProduct =productService.saveProduct(product);
		 
		 if(!ObjectUtils.isEmpty(saveProduct)) {
			 
			 File saveFile= new ClassPathResource("static/img").getFile();
			 
				Path path=  Paths.get(saveFile.getAbsolutePath()+File.separator+"product"+File.separator+image.getOriginalFilename());
				 
				 System.out.println(path);
				 Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			 
			 session.setAttribute("succMsg", "Product save Successfully....");
		 }else {
			 session.setAttribute("errorMsg", "Something went  wrong on server....");
		 }
		 
		 return "redirect:/admin/addProduct";
	 }
	 
	 @GetMapping("/products")
	 public String viewProducts(Model m) {
		 
		 m.addAttribute("products", productService.getAllProducts());
		 return"admin/products";
	 }
	 
	 @GetMapping("/deleteProduct/{id}")
	 public String deleteProduct(@PathVariable int id,HttpSession session) {
		 
		 Boolean deleteProduct = productService.deleteProduct(id);
		 if(deleteProduct) {
			 session.setAttribute("succMsg", "Product delete successfully.....");
		 }else {
			 session.setAttribute("errorMsg", "Somthinge went wrong on server....");
		 }
		 return"redirect:/admin/products";
	 }
	 
	 @GetMapping("/editProduct/{id}")
	 public String editProduct(@PathVariable int id, Model m) {
		
			m.addAttribute("product", productService.getProductById(id));
		m.addAttribute("categories", categoryService.getAllCategory());
		 return "admin/edit_product";
	 }
	 
	 @PostMapping("/updateProduct")
	 public String updateProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile image, Model m, HttpSession session) {
		
		 if(product.getDiscount()< 0 || product.getDiscount()>100) {
			 session.setAttribute("errorMsg", "Invalide Discount....");
		 }else {
		 
		Product updateProduct =productService.updateProduct(product, image);
		if(!ObjectUtils.isEmpty(updateProduct)) {
			 session.setAttribute("succMsg", "Product Update successfully.....");
		}else {
			 session.setAttribute("errorMsg", "Somthinge went wrong on server....");
		 }
		 }
		 return "redirect:/admin/editProduct/" + product.getId();
	 }
	 
	 @GetMapping("/users")
	 public String getAllUsers(Model m) {
		 
		List<UserDtls> users= userService.getUsers("ROLE_USER");
		m.addAttribute("users", users);
		
		return "/admin/users";
	}
	 
	 @GetMapping("/updateSts")
	 public String updateUserAccountStatus(@RequestParam Boolean status,@RequestParam Integer id,HttpSession session) {
		 
		 Boolean f = userService.updateAccountStatus(id, status);
		 if(f) {
			 session.setAttribute("succMsg", "Account Status update successfully...");
		 }else {
			 session.setAttribute("succMsg", "Something went wrong on server....");
		 }
		 
		return"redirect:/admin/users"; 
	 }
	 
	 @GetMapping("/new-products")
	    public String displayNewProducts(Model model) {
	        List<Product> newProducts = productService.findNewProducts();
	        model.addAttribute("newProducts", newProducts);
	        return "home"; // Adjust the view name as needed
	    }
	
}
