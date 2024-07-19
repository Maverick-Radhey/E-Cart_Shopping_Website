package com.service;

import java.util.List;



import com.model.Cart;


public interface CartService {
	
	public Cart saveCart(Integer productId, Integer userId);
	
	public List<Cart> getCartsByUser(Integer usrId);
	
	public Integer getCountCart(Integer userId);

	public void updateQuantity(String sy, Integer cid);

}
