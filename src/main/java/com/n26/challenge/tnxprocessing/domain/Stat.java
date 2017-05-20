package com.n26.challenge.tnxprocessing.domain;

import com.google.common.base.Objects;

/**
 * Stats for a given time duration
 *
 * @author: malaka
 * Date: 5/20/17
 * Time: 4:06 AM
 */
public class Stat {

	private Double sum;

	private Double avg;

	private Double max;

	private Double min;

	private Long count;

	public static Stat of(Double sum, Double avg, Double max, Double min, Long count) {
		return new Stat(sum, avg, max, min, count);
	}

	private Stat(Double sum, Double avg, Double max, Double min, Long count) {
		this.sum = sum;
		this.avg = avg;
		this.max = max;
		this.min = min;
		this.count = count;
	}

	public Double getSum() {
		return sum;
	}

	public Double getAvg() {
		return avg;
	}

	public Double getMax() {
		return max;
	}

	public Double getMin() {
		return min;
	}

	public Long getCount() {
		return count;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Stat)) return false;
		Stat stat = (Stat) o;
		return Objects.equal(sum, stat.sum) &&
			Objects.equal(avg, stat.avg) &&
			Objects.equal(max, stat.max) &&
			Objects.equal(min, stat.min) &&
			Objects.equal(count, stat.count);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(sum, avg, max, min, count);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Stat{");
		sb.append("sum=").append(sum);
		sb.append(", avg=").append(avg);
		sb.append(", max=").append(max);
		sb.append(", min=").append(min);
		sb.append(", count=").append(count);
		sb.append('}');
		return sb.toString();
	}
}
