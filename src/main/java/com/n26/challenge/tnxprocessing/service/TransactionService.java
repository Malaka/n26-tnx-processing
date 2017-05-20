package com.n26.challenge.tnxprocessing.service;

import com.n26.challenge.tnxprocessing.domain.Stat;
import com.n26.challenge.tnxprocessing.domain.Transaction;

/**
 * Transaction service
 * @author: malaka
 * Date: 5/20/17
 * Time: 3:37 AM
 */
public interface TransactionService {

	/**
	 * record transaction with the system
	 * @param transaction tnx
	 * @return true if the transaction happens within the last 60 seconds false otherwise
	 */
	public Boolean recordTnx(Transaction transaction);

	/**
	 * Returns the stat for last 60 seconds
	 * @return stats
	 */
	public Stat getStat();
}
