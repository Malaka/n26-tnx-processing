package com.n26.challenge.tnxprocessing.service;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.challenge.tnxprocessing.TnxProcessingApp;
import com.n26.challenge.tnxprocessing.domain.Stat;
import com.n26.challenge.tnxprocessing.domain.Transaction;

import static java.math.BigDecimal.ROUND_HALF_EVEN;
import static java.math.BigDecimal.valueOf;

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

	@Before
	@After
	public void initState() {
		StatBuilder.getInstance().initState();
	}

	@Test
	public void recordTnx() throws Exception {

		// new tnx
		Transaction newTnx = Transaction.of(123.45, DateTime.now().minus(Duration.standardSeconds(50)).getMillis());
		Assert.assertTrue("all tnx withing 60 sec should retain", transactionService.recordTnx(newTnx));
		Stat stat = transactionService.getStat();
		Stat exp = Stat.of(123.45, 123.45, 123.45, 123.45, 1L);
		Assert.assertEquals(exp, stat);

		// old tnx
		Transaction old = Transaction.of(345123.45, DateTime.now().minus(Duration.standardSeconds(61)).getMillis());
		Assert.assertFalse("all tnx older than 60 sec should ignore", transactionService.recordTnx(old));
		stat = transactionService.getStat();
		exp = Stat.of(123.45, 123.45, 123.45, 123.45, 1L);
		Assert.assertEquals("stats should not change as transaction is ignored", exp, stat);

		// new tnx
		Transaction newTnx2 = Transaction.of(50.567, DateTime.now().minus(Duration.standardSeconds(45)).getMillis());
		Assert.assertTrue("all tnx withing 60 sec should retain", transactionService.recordTnx(newTnx2));
		stat = transactionService.getStat();
		double avg = valueOf(174.017).divide(valueOf(2), ROUND_HALF_EVEN).doubleValue();
		exp = Stat.of(174.017, avg, 123.45, 50.567, 2L);
		Assert.assertEquals(exp, stat);

		// wait for first item to expire
		Thread.sleep(11 * 1000);

		stat = transactionService.getStat();
		avg = valueOf(50.567).divide(valueOf(1), ROUND_HALF_EVEN).doubleValue();
		exp = Stat.of(50.567, avg, 50.567, 50.567, 1L);
		Assert.assertEquals(exp, stat);
	}

}