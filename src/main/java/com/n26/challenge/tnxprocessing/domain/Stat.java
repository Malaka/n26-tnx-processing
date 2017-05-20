package com.n26.challenge.tnxprocessing.domain;

/**
 * Stats for last 60 seconds
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
