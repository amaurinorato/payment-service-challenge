package com.finreach.paymentservice.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.finreach.paymentservice.domain.Payment;

public class Payments {
	
	private static final Map<String, Payment> PAYMENTS = new HashMap<>();
	
	public static void create(Payment payment) {
		PAYMENTS.put(payment.getId(), payment);
	}

	public static Optional<Payment> get(String id) {
		return Optional.ofNullable(PAYMENTS.get(id));
	}

}
