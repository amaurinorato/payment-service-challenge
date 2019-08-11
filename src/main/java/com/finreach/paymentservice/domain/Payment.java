package com.finreach.paymentservice.domain;

import java.util.Date;

public class Payment {
	
	private String id;
	private Double amount;
	private String sourceAccountId;
	private String destinationAccountId;
	private Date createdOn;
	private Date updatedOn;
	private StateEnum state;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public String getSourceAccountId() {
		return sourceAccountId;
	}
	public void setSourceAccountId(String sourceAccountId) {
		this.sourceAccountId = sourceAccountId;
	}
	public String getDestinationAccountId() {
		return destinationAccountId;
	}
	public void setDestinationAccountId(String destinationAccountId) {
		this.destinationAccountId = destinationAccountId;
	}
	public StateEnum getState() {
		return state;
	}
	public void setState(StateEnum state) {
		this.state = state;
	}
}
