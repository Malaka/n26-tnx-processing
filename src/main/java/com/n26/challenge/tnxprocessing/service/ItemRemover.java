package com.n26.challenge.tnxprocessing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * runnable task of removing entry when expires
 * @author: malaka
 * Date: 5/20/17
 * Time: 8:48 AM
 */
public class ItemRemover implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(ItemRemover.class);

	private Double removingItem;

	public static ItemRemover of(Double removingItem) {
		return new ItemRemover(removingItem);
	}

	private ItemRemover(Double removingItem) {
		this.removingItem = removingItem;
	}

	@Override
	public void run() {
		log.info("removing item=[{}] from stat as expired", removingItem);
		StatBuilder.getInstance().remove(removingItem);
	}
}
