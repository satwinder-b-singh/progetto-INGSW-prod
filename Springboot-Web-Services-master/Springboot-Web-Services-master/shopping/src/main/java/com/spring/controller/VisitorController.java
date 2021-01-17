/**
 * 
 */
package com.spring.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.constants.ResponseCode;
import com.spring.constants.WebConstants;
import com.spring.model.Address;
import com.spring.model.Bufcart;
import com.spring.model.PlaceOrder;
import com.spring.model.Product;
import com.spring.model.User;
import com.spring.repository.AddressRepository;
import com.spring.repository.CartRepository;
import com.spring.repository.OrderRepository;
import com.spring.repository.ProductRepository;
import com.spring.repository.UserRepository;
import com.spring.response.cartResp;
import com.spring.response.prodResp;
import com.spring.response.response;
import com.spring.response.serverResp;
import com.spring.response.userResp;
import com.spring.util.Validator;
import com.spring.util.jwtUtil;

/**
 * @author satwinder.b.singh 
 * version : 1.0
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/visitor")
public class VisitorController {

	@Autowired
	private ProductRepository prodRepo;

	@RequestMapping("/getProductsVisitor") // Funziona , ma non carica l'img
	public ResponseEntity<prodResp> getProductsForVisitor() throws IOException {

		prodResp resp = new prodResp();

		try {
			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.LIST_SUCCESS_MESSAGE);

			resp.setOblist(prodRepo.findAll());
		} catch (Exception e) {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(e.getMessage());

		}

		return new ResponseEntity<prodResp>(resp, HttpStatus.ACCEPTED);
	}
	@PostMapping("/getProductsById")
	public ResponseEntity<prodResp> getProductsById( @RequestBody    String id)//@Valid @RequestBody int id
	{
 		prodResp resp = new prodResp();
		Integer i = Integer.parseInt(id);
		try {resp.setStatus(ResponseCode.SUCCESS_CODE);
		resp.setMessage(ResponseCode.LIST_SUCCESS_MESSAGE);
	
		resp.setProduct(prodRepo.findByProductid(i));
	    System.out.println(resp.getProduct());		
		}
		catch(Exception e)
		{ 
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(e.getMessage());
		}
		return new ResponseEntity<prodResp>(resp, HttpStatus.ACCEPTED);
		
	}
}
