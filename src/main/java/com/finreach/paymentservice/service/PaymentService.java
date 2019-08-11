package com.finreach.paymentservice.service;

import com.finreach.paymentservice.api.request.CreatePayment;
import com.finreach.paymentservice.domain.Payment;
import com.finreach.paymentservice.exception.BadRequestException;
import com.finreach.paymentservice.exception.NotFoundException;

public interface PaymentService {

	public Payment create(CreatePayment payment) throws NotFoundException, BadRequestException;
	public Payment get(String id) throws NotFoundException;
	public Payment cancel(String id) throws NotFoundException, BadRequestException;
	public Payment execute(String id) throws NotFoundException, BadRequestException;
	
}
