package com.service;

import com.model.OrderRequest;
import com.model.ProductOrder;

public interface OrderService {

	public ProductOrder saveOrder(Integer userId);

	public void saveOrder(Integer userid,OrderRequest orderRequest);
}
