package com.n26.challenge.tnxprocessing.domain;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * Represent transaction entry to the system
 *
 * @author: malaka
 * Date: 5/20/17
 * Time: 2:38 AM
 */
public class Transaction implements Serializable {

	@NotNull
	@ApiModelProperty(notes = "Transaction amount", required = true)
	private Double amount;

	@NotNull
	@ApiModelProperty(notes = "Transaction time in epoch in millis in UTC time zone", required = true)
	private Long timestamp;

	public static Transaction of(Double amount, Long timestamp) {
		return new Transaction(amount, timestamp);
	}

	private Transaction(Double amount, Long timestamp) {
		this.amount = amount;
		this.timestamp = timestamp;
	}

	public Transaction() {

	}


	public Double getAmount() {
		return amount;
	}

	public Long getTimestamp() {
		return timestamp;
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Transaction{");
		sb.append("amount=").append(amount);
		sb.append(", timestamp=").append(timestamp);
		sb.append('}');
		return sb.toString();
	}
}
