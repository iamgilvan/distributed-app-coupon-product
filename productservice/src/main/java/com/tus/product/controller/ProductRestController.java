package com.tus.product.controller;

import java.math.BigDecimal;
import java.util.List;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.tus.product.dto.Coupon;
import com.tus.product.model.Product;
import com.tus.product.repos.ProductRepo;

@RestController
@RequestMapping("/productapi")
public class ProductRestController {

	@Autowired
	private ProductRepo repo;

	@Autowired
	private RestTemplate restTemplate;

	private String couponServiceURL = "http://COUPON-SERVICE/couponapi/coupons/";

	@RequestMapping(value = "/products", method = RequestMethod.POST)
	@CircuitBreaker(name = "couponService", fallbackMethod = "handleFallback")
	public Product create(@RequestBody Product product) {
		Coupon coupon = restTemplate.getForObject(couponServiceURL + product.getCouponCode(), Coupon.class);
		if (coupon != null && coupon.getDiscount() != null) {
			BigDecimal discount = coupon.getDiscount();
			product.setPrice(product.getPrice().subtract(discount));
		} else {
			System.out.println("Coupon not found: " + product.getCouponCode());
		}
		return repo.save(product);
	}

	// PLAN B: If the Coupon Service goes down, this method is called automatically.
	public Product handleFallback(Product product, Throwable t) {
		System.out.println("Coupon Service is down! Saving product without discount.");
		return repo.save(product); // Save the product with the original price.
	}

	@GetMapping(value = "/products")
	public List<Product> getAllProducts() {
		return repo.findAll();
	}
}
