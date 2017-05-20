package com.n26.challenge.tnxprocessing.service;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.challenge.tnxprocessing.TnxProcessingApp;
import com.n26.challenge.tnxprocessing.domain.Transaction;

/**
 * @author: malaka
 * Date: 5/20/17
 * Time: 3:56 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TnxProcessingApp.class)
public class TransactionServiceImplTest {

	@Autowired
	private TransactionServiceImpl transactionService;

	@Test
	public void recordTnx() throws Exception {
		// old tnx
		Transaction old = Transaction.of(12123.45, DateTime.now().minus(Duration.standardSeconds(61)).getMillis());
		Assert.assertFalse("all tnx older than 60 sec should ignore", transactionService.recordTnx(old));

		// new tnx
		Transaction newTnx = Transaction.of(12123.45, DateTime.now().minus(Duration.standardSeconds(10)).getMillis());
		Assert.assertTrue("all tnx withing 60 sec should retain", transactionService.recordTnx(newTnx));
	}

}