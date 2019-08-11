package com.finreach.paymentservice.api;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finreach.paymentservice.api.request.CreatePayment;
import com.finreach.paymentservice.exception.BadRequestException;
import com.finreach.paymentservice.exception.NotFoundException;
import com.finreach.paymentservice.service.PaymentService;
import com.finreach.paymentservice.service.PaymentServiceImpl;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
	
	private PaymentService paymentService;
	
	public PaymentController() {
		this.paymentService = new PaymentServiceImpl();
	}
	
	@PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreatePayment request) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.create(request));
		} catch (BadRequestException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch(NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
    }
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(paymentService.get(id));
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PutMapping(path = "/cancel/{id}")
	public ResponseEntity<?> cancel(@PathVariable("id") String id) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(paymentService.cancel(id));
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch(BadRequestException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PutMapping(path = "/execute/{id}")
	public ResponseEntity<?> execute(@PathVariable("id") String id) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(paymentService.execute(id));
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch(BadRequestException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
