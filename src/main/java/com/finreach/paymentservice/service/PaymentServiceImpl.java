package com.finreach.paymentservice.service;

import java.util.Date;

import com.finreach.paymentservice.api.request.CreatePayment;
import com.finreach.paymentservice.domain.Account;
import com.finreach.paymentservice.domain.Payment;
import com.finreach.paymentservice.domain.StateEnum;
import com.finreach.paymentservice.exception.BadRequestException;
import com.finreach.paymentservice.exception.NotFoundException ;
import com.finreach.paymentservice.store.Accounts;
import com.finreach.paymentservice.store.Payments;

public class PaymentServiceImpl implements PaymentService {

	@Override
	public Payment get(String id) throws NotFoundException  {
		return Payments.get(id).orElseThrow(() -> new NotFoundException ("Payment " + id + " not Found!"));
	}

	@Override
	public Payment cancel(String id) throws NotFoundException, BadRequestException  {
		Payment payment = Payments.get(id).orElseThrow(() -> new NotFoundException ("Payment " + id + " not Found!"));
		if (StateEnum.CREATED.equals(payment.getState())) {
			return updatePayment(payment, StateEnum.CANCELED);
		} else {
			throw new BadRequestException("Payment to be cancelled is in an invalid state");
		}
	}

	@Override
	public Payment execute(String id) throws NotFoundException, BadRequestException {
		Payment payment = Payments.get(id).orElseThrow(() -> new NotFoundException ("Payment " + id + " not Found!"));
		if (StateEnum.CREATED.equals(payment.getState())) {
			Account source = getAccount(payment.getSourceAccountId());
			if (payment.getAmount() > source.getBalance()) {
				return updatePayment(payment, StateEnum.REJECTED);
			} else {
				Account destination = getAccount(payment.getDestinationAccountId());

				Accounts.transaction(source.getId(), negativeValue(payment.getAmount()));
				Accounts.transaction(destination.getId(), payment.getAmount());
				
				return updatePayment(payment, StateEnum.EXECUTED);
			}
		} else {
			throw new BadRequestException("Payment to be executed is in an invalid state");
		}
	}
	
	@Override
	public Payment create(CreatePayment payment) throws BadRequestException, NotFoundException {
		Payment paymentModel = createValidPaymentModel(payment);
		Payments.create(paymentModel);
		return paymentModel;
	}
	
	private Payment updatePayment(Payment payment, StateEnum state) {
		payment.setState(state);
		payment.setUpdatedOn(new Date());
		return payment;
	}

	private Payment createValidPaymentModel(CreatePayment payment) throws NotFoundException, BadRequestException {
		Payment paymentModel = new Payment();
		paymentModel.setDestinationAccountId(getAccount(payment.getDestinationAccountId()).getId());
		paymentModel.setSourceAccountId(getAccount(payment.getSourceAccountId()).getId());
		
		accountsValidation(payment);
		amountsValidation(payment);
		
		paymentModel.setId(String.valueOf(System.nanoTime()));
		paymentModel.setAmount(payment.getAmount());
		paymentModel.setCreatedOn(new Date());
		paymentModel.setState(StateEnum.CREATED);
		return paymentModel;
	}
	
	private Account getAccount(String id) throws NotFoundException {
		return Accounts.get(id).orElseThrow(() -> new NotFoundException("Account " + id + " not Found!"));
	}

	private double negativeValue(Double value) {
		return value < 0 ? value : value * -1;
	}

	private void amountsValidation(CreatePayment payment) throws BadRequestException {
		if (payment.getAmount() < 0) {
			throw new BadRequestException("Amount must be positive");
		}
	}

	private void accountsValidation(CreatePayment payment) throws BadRequestException {
		if (payment.getSourceAccountId().equals(payment.getDestinationAccountId())) {
			throw new BadRequestException("Destination account and Source account must be different");
		}
	}

}
