package com.n26.challenge.tnxprocessing.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.n26.challenge.tnxprocessing.domain.Stat;

import static java.math.BigDecimal.ROUND_HALF_EVEN;
import static java.math.BigDecimal.valueOf;

/**
 * @author: malaka
 * Date: 5/20/17
 * Time: 8:24 AM
 */
public class StatBuilderTest {

	private StatBuilder builder;

	@Before
	@After
	public void initState() {
		builder = StatBuilder.getInstance();
		builder.initState();
	}

	@Test
	public void testStatBuilder() {

		builder.add(10D);
		builder.add(20.56D);
		builder.add(40D);
		builder.add(30D);

		// check stat after add
		Stat stat = builder.getStat();
		Stat exp = Stat.of(100.56D, valueOf(100.56D).divide(valueOf(4), ROUND_HALF_EVEN).doubleValue(), 40D, 10D, 4L);
		Assert.assertEquals(exp, stat);

		//remove max
		builder.remove(40D);
		stat = builder.getStat();
		exp = Stat.of(60.56D, valueOf(60.56D).divide(valueOf(3), ROUND_HALF_EVEN).doubleValue(), 30D, 10D, 3L);
		Assert.assertEquals(exp, stat);

		//remove min
		builder.remove(10D);
		stat = builder.getStat();
		exp = Stat.of(50.56D, valueOf(50.56D).divide(valueOf(2), ROUND_HALF_EVEN).doubleValue(), 30D, 20.56D, 2L);
		Assert.assertEquals(exp, stat);

		//remove wrong item
		builder.remove(35D);
		stat = builder.getStat();
		exp = Stat.of(50.56D, valueOf(50.56D).divide(valueOf(2), ROUND_HALF_EVEN).doubleValue(), 30D, 20.56D, 2L);
		Assert.assertEquals(exp, stat);

		builder.remove(30D);
		stat = builder.getStat();
		exp = Stat.of(20.56D, valueOf(20.56D).divide(valueOf(1), ROUND_HALF_EVEN).doubleValue(), 20.56D, 20.56D, 1L);
		Assert.assertEquals(exp, stat);

		//remove last
		builder.remove(20.56D);
		stat = builder.getStat();
		exp = Stat.of(0D, 0D, 0D, 0D, 0L);
		Assert.assertEquals(exp, stat);
	}

}