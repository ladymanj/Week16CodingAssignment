/**
 * 
 */
package com.promineotech.jeep.service;

import com.promineotech.jeep.entity.Order;
import com.promineotech.jeep.entity.OrderRequest;

/**
 * @author Jordan
 *
 */
public interface JeepOrderService {
  
  Order createOrder(OrderRequest orderRequest);

}
