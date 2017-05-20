package com.n26.challenge.tnxprocessing.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.google.common.base.Objects;

/**
 * @author: malaka
 * Date: 5/20/17
 * Time: 2:38 AM
 */
@Entity
public class Transaction implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@Column(name = "amount", nullable = false)
	private Double amount;

	@NotNull
	@Column(name = "timestamp", nullable = false)
	private Long timestamp;

	public static Transaction of(Double amount, Long timestamp) {
		return new Transaction(amount, timestamp);
	}

	private Transaction(Double amount, Long timestamp) {
		this.amount = amount;
		this.timestamp = timestamp;
	}

	// hibernate dependency
	public Transaction() {

	}

	public Long getId() {
		return id;
	}

	public Double getAmount() {
		return amount;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Transaction)) return false;
		Transaction that = (Transaction) o;
		return Objects.equal(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Transaction{");
		sb.append("id=").append(id);
		sb.append(", amount=").append(amount);
		sb.append(", timestamp=").append(timestamp);
		sb.append('}');
		return sb.toString();
	}
}
