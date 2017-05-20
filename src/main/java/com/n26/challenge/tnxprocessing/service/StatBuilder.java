package com.n26.challenge.tnxprocessing.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;

import com.google.common.collect.Lists;
import com.n26.challenge.tnxprocessing.domain.Stat;

/**
 * Stat Aggregator for a given duration.
 *
 * @author: malaka
 * Date: 5/20/17
 * Time: 6:24 AM
 */
public class StatBuilder {

	private static final StatBuilder instance = new StatBuilder();

	private BigDecimal sum;

	private BigDecimal avg;

	private BigDecimal max;

	private BigDecimal min;

	private Long count;

	private LinkedList<BigDecimal> data;

	private StatBuilder() {
		initState();
	}

	public static StatBuilder getInstance() {
		return instance;
	}

	public void initState() {
		this.sum = BigDecimal.ZERO;
		this.avg = BigDecimal.ZERO;
		this.max = BigDecimal.valueOf(Double.MIN_VALUE);
		this.min = BigDecimal.valueOf(Double.MAX_VALUE);
		this.count = 0L;
		this.data = Lists.newLinkedList();
	}

	public synchronized void add(Double item) {
		BigDecimal itemBD = BigDecimal.valueOf(item);
		this.sum = this.sum.add(itemBD);
		this.count++;
		if (itemBD.compareTo(max) > 0) {
			this.max = itemBD;
		}

		if (itemBD.compareTo(min) < 0) {
			this.min = itemBD;
		}

		this.avg = sum.divide(BigDecimal.valueOf(count), BigDecimal.ROUND_HALF_EVEN);
		this.data.add(itemBD);
	}

	public synchronized void remove(Double item) {
		BigDecimal itemBD = BigDecimal.valueOf(item);
		boolean remove = this.data.remove(itemBD);
		if (remove) {
			this.sum = this.sum.add(itemBD.negate());
			this.count--;
			boolean lastItem = count.compareTo(0L) == 0;
			if (itemBD.compareTo(max) == 0 || itemBD.compareTo(min) == 0) {
				Collections.sort(data);
				this.min = lastItem ? BigDecimal.ZERO : data.getFirst();
				this.max = lastItem ? BigDecimal.ZERO : data.getLast();
			}
			this.avg = lastItem ? BigDecimal.ZERO : sum.divide(BigDecimal.valueOf(count), BigDecimal.ROUND_HALF_EVEN);
		}
	}

	public synchronized Stat getStat() {
		if (count == 0) {
			return Stat.of(0D, 0D, 0D, 0D, 0L);
		} else {
			return Stat.of(sum.doubleValue(), avg.doubleValue(), max.doubleValue(), min.doubleValue(), count);
		}
	}
}
