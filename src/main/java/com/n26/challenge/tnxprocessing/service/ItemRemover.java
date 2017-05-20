package com.n26.challenge.tnxprocessing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * runnable task of removing entry when expires
 *
 * @author: malaka
 * Date: 5/20/17
 * Time: 8:48 AM
 */
public class ItemRemover implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(ItemRemover.class);

	private Double removingItem;

	private StatBuilder statBuilder;

	public static ItemRemover of(StatBuilder statBuilder, Double removingItem) {
		return new ItemRemover(statBuilder, removingItem);
	}

	private ItemRemover(StatBuilder statBuilder, Double removingItem) {
		this.statBuilder = statBuilder;
		this.removingItem = removingItem;
	}

	@Override
	public void run() {
		log.info("removing item=[{}] from stat as expired", removingItem);
		statBuilder.remove(removingItem);
	}
}
