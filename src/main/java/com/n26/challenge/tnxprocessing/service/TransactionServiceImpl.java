package com.n26.challenge.tnxprocessing.service;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.n26.challenge.tnxprocessing.domain.Stat;
import com.n26.challenge.tnxprocessing.domain.Transaction;
import com.n26.challenge.tnxprocessing.repo.TransactionRepo;

/**
 * Transaction service impl
 *
 * @author: malaka
 * Date: 5/20/17
 * Time: 3:37 AM
 */
@Service
public class TransactionServiceImpl implements TransactionService {

	private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

	@Autowired
	private TransactionRepo transactionRepo;

	@Value("${n26.stat.retain.duration.sec}")
	private String statDuration;

	@Override
	public Boolean recordTnx(Transaction transaction) {
		DateTime now = DateTime.now();
		Integer statWindow = Integer.valueOf(statDuration);
		Duration duration = Duration.millis(now.getMillis() - transaction.getTimestamp());
		log.debug("tnx duration from now is {}", duration);

		boolean status;
		if (duration.compareTo(Duration.standardSeconds(statWindow)) > 0) {
			log.info("transaction {} is older than {} seconds, ignoring", statWindow, transaction);
			status = false;
		} else {
			log.info("adding the {} to DB for include in stat", transaction);
			transactionRepo.save(transaction);
			status = true;
		}

		return status;
	}

	@Override
	public Stat getStat() {
		return Stat.of(1d, 1d, 1d, 1d, 1L);
	}
}
