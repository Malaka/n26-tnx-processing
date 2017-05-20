package com.n26.challenge.tnxprocessing.repo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.challenge.tnxprocessing.TnxProcessingApp;
import com.n26.challenge.tnxprocessing.domain.Transaction;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;

/**
 * @author: malaka
 * Date: 5/20/17
 * Time: 2:56 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TnxProcessingApp.class)
public class TransactionRepoTest {

	@Autowired
	private TransactionRepo transactionRepo;

	@Before
	@After
	public void setUp() throws Exception {
		transactionRepo.deleteAll();
	}

	@Test
	public void contextLoads() {

		Transaction t1 = Transaction.of(12123.45, 12342342342L);
		Transaction t2 = Transaction.of(4123.45, 4564342342342L);
		transactionRepo.save(t1);
		transactionRepo.save(t2);
		Iterable<Transaction> result = transactionRepo.findAll();
		assertThat(result, is(iterableWithSize(2)));
	}

}