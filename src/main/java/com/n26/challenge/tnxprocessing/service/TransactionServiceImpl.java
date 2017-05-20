package com.n26.challenge.tnxprocessing.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.n26.challenge.tnxprocessing.domain.Stat;
import com.n26.challenge.tnxprocessing.domain.Transaction;

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

	private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

	@Value("${n26.stat.retain.duration.sec}")
	private String statDuration;

	@Override
	public Boolean recordTnx(Transaction tnx) {
		DateTime now = DateTime.now();
		Integer statWindow = Integer.valueOf(statDuration);
		Duration duration = Duration.millis(now.getMillis() - tnx.getTimestamp());
		log.debug("tnx duration from now is {}", duration);

		boolean status;
		Duration statDuration = Duration.standardSeconds(statWindow);
		if (duration.compareTo(statDuration) > 0) {
			log.info("tnx {} is older than {} seconds, Ignoring the record", tnx, statWindow);
			status = false;
		} else {
			Duration ttl = statDuration.minus(duration.getMillis());
			StatBuilder statBuilder = StatBuilder.getInstance();
			statBuilder.add(tnx.getAmount());
			ItemRemover removalTask = ItemRemover.of(statBuilder, tnx.getAmount());
			scheduledExecutorService.schedule(removalTask, ttl.getMillis(), TimeUnit.MILLISECONDS);
			log.info("adding the {} to stat with expiration={}", tnx, ttl);
			status = true;
		}
		return status;
	}

	@Override
	public Stat getStat() {
		return StatBuilder.getInstance().getStat();
	}
}
