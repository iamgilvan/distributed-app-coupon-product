package com.tus.coupon.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tus.coupon
.model.Coupon;
import com.tus.coupon.repo.CouponRepo;


@RestController
@RequestMapping("/couponapi")
public class CouponRestController {
//comment for version control
	@Autowired
	CouponRepo repo;
	
	//this is called constructor based injection.
	// you can now use normal mocks to test
	/*
	 * public CouponRestController(CouponRepo repo) { this.repo=repo; }
	 */
	//setter injection
//	@Autowired
//	public void setRepo(CouponRepo repo) {
//		this.repo=repo;
//	}
	
	@PostMapping(value = "/coupons")
	public ResponseEntity<Coupon> create(@RequestBody Coupon coupon) {
		//return repo.save(coupon);
		return new ResponseEntity<>(repo.save(coupon), HttpStatus.OK);
	}
	
	@GetMapping("/coupons/{code}")
	Coupon getCouponByCouponCode(@PathVariable String code) {
		System.out.println(code);
			return repo.findByCode(code);
	}
	
	
	@GetMapping(value = "/coupons")
	public List<Coupon> getAllCoupons() {
		return repo.findAll();

	}

    // CPU-bound busy work for `durationMs` milliseconds
    @GetMapping("/cpu")
    public Map<String,Object> cpuLoad(@RequestParam(defaultValue = "1000") long durationMs,
                                      @RequestParam(defaultValue = "1") int workers) {
        long start = System.currentTimeMillis();
        ExecutorService ex = Executors.newFixedThreadPool(Math.max(1, workers));
        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 0; i < workers; i++) {
            futures.add(ex.submit(() -> {
                long end = System.currentTimeMillis() + durationMs;
                long counter = 0;
                while (System.currentTimeMillis() < end) {
                    // small CPU work
                    double x = Math.sqrt(Math.random()*Math.random()*12345.6789);
                    counter++;
                }
                return counter;
            }));
        }
        long total = 0;
        for (Future<Long> f : futures) {
            try { total += f.get(); } catch(Exception e) { /* ignore */ }
        }
        ex.shutdownNow();
        Map<String,Object> m = new HashMap<>();
        m.put("durationMs", durationMs);
        m.put("workers", workers);
        m.put("iterations", total);
        m.put("when", Instant.now().toString());
        return m;
    }

}
