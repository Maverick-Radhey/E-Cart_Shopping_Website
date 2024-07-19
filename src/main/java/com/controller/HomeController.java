package com.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.model.Category;
import com.model.Product;
import com.model.UserDtls;
import com.repository.CategoryRepository;
import com.repository.ProductRepository;
import com.service.CartService;
import com.service.CategoryService;
import com.service.ProductService;
import com.service.UserService;
import com.util.CommonUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
   private ProductRepository productRepository;
	
	@Autowired
    private CategoryRepository categoryRepository;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
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
		return "index";
	}
	 @GetMapping("/signin")
	public String login() {
		return "login";
	}
	 @GetMapping("/register")
	public String register() {
		return "register";
	}
	 
		
		@GetMapping("/contacts")
		public String helpLine() {
			return "helpLine";
		}
	 
	 @GetMapping("/profile")
		public String showProfile(Principal p, Model m) {
		 if(p!=null) {
				String email = p.getName();
			UserDtls userDtls =	userService.getUserByEmail(email);
			m.addAttribute("user", userDtls);}
			return "/profile";
		}
	 
	 
	 @GetMapping("/admin/AdminProfile")
		public String showAdminProfile(Principal p, Model m) {
		 if(p!=null) {
				String email = p.getName();
			UserDtls userDtls =	userService.getUserByEmail(email);
			m.addAttribute("user", userDtls);}
			return "/admin/AdminProfile";
		}
	 
	 @GetMapping("/products")
	 public String product(Model m, @RequestParam (value = "category", defaultValue="") String category) {
		 
		 System.out.println("category =  "+category);
		List<Category> categories = categoryService.getAllActiveCategory();
		
		List<Product> products = productService.getAllActiveProducts(category);
		
		m.addAttribute("categories",categories);
		m.addAttribute("products", products);
		m.addAttribute("paramValue", category);
		 
		 return "product";
	 }
	 @GetMapping("/view_product/{id}")
	 public String viewProduct(@PathVariable int id, Model m) {
		 
		 Product productById = productService.getProductById(id);
		 
		 m.addAttribute("product", productById);
		 return "view_product";
	 }
	
	 @PostMapping("/saveUser")
	 public String saveUser(@ModelAttribute UserDtls user,HttpSession session, @RequestParam("img") MultipartFile file) throws IOException {
		 
	String imageName =	 file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
		 
	user.setProfile_image(imageName);
		UserDtls saveUser =  userService.saveUser(user);
		 
		 if(!ObjectUtils.isEmpty(saveUser)) {
			 if(!file.isEmpty()) {
				 File saveFile= new ClassPathResource("static/img").getFile();
				 
					Path path=  Paths.get(saveFile.getAbsolutePath()+File.separator+"profile"+File.separator+file.getOriginalFilename());
					 
					// System.out.println(path);
					 Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			 }
			 session.setAttribute("succMsg", "Registration Successfully.....");
		 }else {
			 session.setAttribute("errorMsg", "something went wrong on server......");
		 }
		 
		 
		 return"redirect:/register";
	 }
	 
// forget password..
	 
	 @GetMapping("/forgetPassword")
	 public String showForgetPassword() {
		
		 
		 return "forget_password";
		 
	 }
	 
	 @PostMapping("/forgetPassword")
	 public String processForgetPassword(@RequestParam String email,HttpSession session,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		UserDtls userByEmail =userService.getUserByEmail(email);
		
		if(ObjectUtils.isEmpty(userByEmail)) {
			session.setAttribute("errorMsg", "invalid email......");
		}else {
			
			String resetToken = UUID.randomUUID().toString();
			
			userService.updateUserResetToken(email,resetToken);
			
			//genrate url
			
		String url =	CommonUtil.generateUrl(request)+"/resetPassword?token="+resetToken;
			
			Boolean sendMail =commonUtil.sendMail(url, email);
			
			if(sendMail) {
				session.setAttribute("succMsg", "please check your email....");
			}else {
				session.setAttribute("errorMsg", "something went wrong on server  !! email not send....");
			}
		}
		 
		 return "redirect:/forgetPassword";
		 
	 }
	 
	 @GetMapping("/resetPassword")
	 public String showResetPassword(@RequestParam String token, HttpSession session,Model m) {
		
		 UserDtls userByToken = userService.getUserByToken(token);
		 if(userByToken==null) {
			m.addAttribute("msg", "your link is invalid orexpired....");
			return "message";
		 }
		 m.addAttribute("token", token);
		 return "reset_password";
		 
	 }
	 
	 @PostMapping("/resetPassword")
	 public String ResetPassword(@RequestParam String token,@RequestParam String password, HttpSession session,Model m) {
		
		 UserDtls userByToken = userService.getUserByToken(token);
		 if(userByToken==null) {
				m.addAttribute("errorMsg", "your link is invalid orexpired....");
				return "message";
			 }else {
				 userByToken.setPassword(passwordEncoder.encode(password));
				 userByToken.setResetToken(null);
				 userService.updateUser(userByToken);
				 
				// session.setAttribute("succMsg", "Password change successfully......");
				 m.addAttribute("msg", "Password change successfully......");
				 return "message";
			 }
		 }
	 @GetMapping("/categories")
	 public List<Category> getAllActiveCategory(Model m) {
			List<Category> categories =	categoryRepository.findByIsActiveTrue();
			m.addAttribute("categories", categoryService.getAllActiveCategory());
				return categories;


}
	  @GetMapping("/home")
	    public String home(Model model) {
	        model.addAttribute("recentProducts", productService.listAll());
	        return "home"; 
	    }
	  
	  @GetMapping("/new-products")
	  public String displayNewProducts(Model model) {
	      List<Product> newProducts = productService.findNewProducts();
	      model.addAttribute("newProducts", newProducts);
	      return "home"; 
	  }
}
