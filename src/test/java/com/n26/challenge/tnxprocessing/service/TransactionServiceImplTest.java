package com.n26.challenge.tnxprocessing.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

import com.google.common.collect.Lists;
import com.n26.challenge.tnxprocessing.TnxProcessingApp;
import com.n26.challenge.tnxprocessing.domain.Stat;
import com.n26.challenge.tnxprocessing.domain.Transaction;

import static java.math.BigDecimal.ROUND_HALF_EVEN;
import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertTrue;

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
		assertTrue("all tnx withing 60 sec should retain", transactionService.recordTnx(newTnx));
		Stat stat = transactionService.getStat();
		Stat exp = Stat.of(123.45, 123.45, 123.45, 123.45, 1L);
		Assert.assertEquals(exp, stat);

		// old tnx
		Transaction old = Transaction.of(345123.45, DateTime.now().minus(Duration.standardSeconds(61)).getMillis());
		Assert.assertFalse("all tnx older than 60 sec should ignore", transactionService.recordTnx(old));
		stat = transactionService.getStat();
		exp = Stat.of(123.45, 123.45, 123.45, 123.45, 1L);
		Assert.assertEquals("stats should not change as transaction is ignored", exp, stat);
	}

	@Test
	public void recordExpiration() throws Exception {

		// new tnx
		Transaction newTnx = Transaction.of(50.567, DateTime.now().minus(Duration.standardSeconds(1)).getMillis());
		assertTrue("all tnx withing 60 sec should retain", transactionService.recordTnx(newTnx));
		Stat stat = transactionService.getStat();
		Stat exp = Stat.of(50.567, 50.567, 50.567, 50.567, 1L);
		Assert.assertEquals(exp, stat);

		List<Runnable> runnables = Lists.newArrayList();
		for (int i = 1; i < 12; i++) {
			runnables.add(() -> {
				long timestamp = DateTime.now().minus(Duration.standardSeconds(50)).getMillis();
				Transaction tnx = Transaction.of(Math.random(), timestamp);
				assertTrue("all tnx withing 60 sec should retain", transactionService.recordTnx(tnx));
			});
		}
		runConcurrent("new transaction recode", runnables, 2);

		// wait for second set to expire
		Thread.sleep(11 * 1000);

		stat = transactionService.getStat();
		double avg = valueOf(50.567).divide(valueOf(1), ROUND_HALF_EVEN).doubleValue();
		exp = Stat.of(50.567, avg, 50.567, 50.567, 1L);
		Assert.assertEquals(exp, stat);
	}


	private void runConcurrent(final String message, final List<? extends Runnable> runnables, final int maxTimeoutSeconds)
		throws InterruptedException {
		final int numThreads = runnables.size();
		final List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<>());
		final ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
		try {
			final CountDownLatch afterInitBlocker = new CountDownLatch(1);
			final CountDownLatch allDone = new CountDownLatch(numThreads);
			for (final Runnable submittedTestRunnable : runnables) {
				threadPool.submit(() -> {
					try {
						afterInitBlocker.await();
						submittedTestRunnable.run();
					} catch (final Throwable e) {
						exceptions.add(e);
					} finally {
						allDone.countDown();
					}
				});
			}

			// start all test runners
			afterInitBlocker.countDown();
			assertTrue(message + " timeout! More than" + maxTimeoutSeconds + "seconds",
				allDone.await(maxTimeoutSeconds, TimeUnit.SECONDS));
		} finally {
			threadPool.shutdownNow();
		}
		assertTrue(message + "failed with exception(s)" + exceptions, exceptions.isEmpty());
	}
}