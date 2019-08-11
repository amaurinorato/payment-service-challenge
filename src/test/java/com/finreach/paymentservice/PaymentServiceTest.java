package com.finreach.paymentservice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.finreach.paymentservice.api.request.CreateAccount;
import com.finreach.paymentservice.api.request.CreatePayment;
import com.finreach.paymentservice.domain.Account;
import com.finreach.paymentservice.domain.Payment;
import com.finreach.paymentservice.domain.StateEnum;
import com.finreach.paymentservice.exception.BadRequestException;
import com.finreach.paymentservice.exception.NotFoundException;
import com.finreach.paymentservice.service.PaymentService;
import com.finreach.paymentservice.service.PaymentServiceImpl;
import com.finreach.paymentservice.store.Accounts;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentServiceTest {
	
	PaymentService paymentService;
	Account source;
	Account destination;
	CreatePayment request = new CreatePayment();
	
	
	
	@Before
	public void setUp() {
		paymentService = new PaymentServiceImpl();
		source = new Account(Accounts.create(new CreateAccount(10d)));
		destination = new Account(Accounts.create(new CreateAccount(20d)));
		request.setAmount(10d);
		request.setDestinationAccountId(destination.getId());
		request.setSourceAccountId(source.getId());
	}
	
	@Test(expected = NotFoundException.class)
	public void testCreatePaymentFaillWithNotFoundException() throws NotFoundException, BadRequestException {
		request.setDestinationAccountId("1");
		request.setSourceAccountId("2");

		paymentService.create(request);
	}

	@Test(expected = BadRequestException.class)
	public void testCreatePaymentFailWithBadRequestException() throws NotFoundException, BadRequestException {
		request.setAmount(-10.0d);

		paymentService.create(request);
	}
	
	@Test
	public void testCreatPaymentSuccess() throws NotFoundException, BadRequestException {
		Payment payment = paymentService.create(request);
		
		Assert.assertNotNull(payment);
		Assert.assertEquals(payment.getState(), StateEnum.CREATED);
		Assert.assertEquals(payment.getDestinationAccountId(), this.destination.getId());
		Assert.assertEquals(payment.getSourceAccountId(), this.source.getId());
	}
	
	@Test()
	public void testCancelPaymentSuccess() throws NotFoundException, BadRequestException {
		
		Payment payment = paymentService.create(request);
		Assert.assertNotNull(payment);
		
		Assert.assertEquals(payment.getState(), StateEnum.CREATED);
		
		payment = paymentService.cancel(payment.getId());
		
		Assert.assertNotNull(payment);
		Assert.assertEquals(payment.getState(), StateEnum.CANCELED);
	}
	
	@Test()
	public void testExecutePaymentSuccess() throws NotFoundException, BadRequestException {
		Double sourceBalanceBeforePayment = Accounts.get(this.source.getId()).get().getBalance();
		Double destinationBalanceBeforePayment = Accounts.get(this.destination.getId()).get().getBalance();

		Payment payment = paymentService.create(request);
		Assert.assertNotNull(payment);
		
		Assert.assertEquals(payment.getState(), StateEnum.CREATED);
		
		payment = paymentService.execute(payment.getId());
		
		Assert.assertNotNull(payment);
		Assert.assertEquals(payment.getState(), StateEnum.EXECUTED);
		Assert.assertEquals(Accounts.get(this.source.getId()).get().getBalance(), Double.valueOf(sourceBalanceBeforePayment - payment.getAmount()));
		Assert.assertEquals(Accounts.get(this.destination.getId()).get().getBalance(), Double.valueOf(destinationBalanceBeforePayment + payment.getAmount()));
	}
	
	@Test
	public void testRejectPaymentSuccess() throws NotFoundException, BadRequestException {
		request.setAmount(200d);
		
		Payment payment = paymentService.create(request);
		
		Assert.assertNotNull(payment);
		Assert.assertEquals(payment.getState(), StateEnum.CREATED);
		
		payment = paymentService.execute(payment.getId());
		
		Assert.assertNotNull(payment);
		Assert.assertEquals(payment.getState(), StateEnum.REJECTED);
	}
	
	@Test(expected=NotFoundException.class)
	public void testCancelPaymentFailWithNotFoundException() throws NotFoundException, BadRequestException {
		Payment payment = paymentService.create(request);
		Assert.assertNotNull(payment);
		
		Assert.assertEquals(payment.getState(), StateEnum.CREATED);
		
		payment = paymentService.cancel("not found");
	}
	
	@Test(expected=BadRequestException.class)
	public void testExecutePaymentFailWithBadrequest() throws NotFoundException, BadRequestException {
		Payment payment = paymentService.create(request);
		Assert.assertNotNull(payment);
		
		Assert.assertEquals(payment.getState(), StateEnum.CREATED);
		
		payment = paymentService.execute(payment.getId());
		
		payment = paymentService.execute(payment.getId());
	}
}
