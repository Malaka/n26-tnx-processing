package com.n26.challenge.tnxprocessing.controller;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.n26.challenge.tnxprocessing.TnxProcessingApp;
import com.n26.challenge.tnxprocessing.service.StatBuilder;

import static java.math.BigDecimal.ROUND_HALF_EVEN;
import static java.math.BigDecimal.valueOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * TnxProcessingController unit test
 *
 * @author: malaka
 * Date: 5/20/17
 * Time: 4:44 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TnxProcessingApp.class)
public class TnxProcessingControllerTest {

	private MediaType contentType = MediaType.APPLICATION_JSON_UTF8;

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webAppContext;

	@Before
	public void before() {
		this.mockMvc = webAppContextSetup(webAppContext).build();
		StatBuilder.getInstance().initState();
	}

	@After
	public void after() {
		StatBuilder.getInstance().initState();
	}

	@Test
	public void recordTransaction() throws Exception {
		String content = new JSONObject()
			.put("amount", 12123.45)
			.put("timestamp", DateTime.now().minus(Duration.standardSeconds(10)).getMillis())
			.toString();

		this.mockMvc.perform(post("/transactions")
			.contentType(contentType)
			.content(content))
			.andExpect(status().isCreated());
	}

	@Test
	public void recordIgnore() throws Exception {
		String content = new JSONObject()
			.put("amount", 12123.45)
			.put("timestamp", DateTime.now().minus(Duration.standardSeconds(100)).getMillis())
			.toString();

		this.mockMvc.perform(post("/transactions")
			.contentType(contentType)
			.content(content))
			.andExpect(status().isNoContent());
	}

	@Test
	public void invalidRequest() throws Exception {

		String content = new JSONObject()
			.put("amount", 12123.45)
			.toString();

		// missing timestamp
		this.mockMvc.perform(post("/transactions")
			.contentType(contentType)
			.content(content))
			.andExpect(status().is4xxClientError());

		content = new JSONObject()
			.put("amount", "$2342")
			.put("timestamp", DateTime.now().minus(Duration.standardSeconds(10)).getMillis())
			.toString();

		// invalid amount
		this.mockMvc.perform(post("/transactions")
			.contentType(contentType)
			.content(content))
			.andExpect(status().is4xxClientError());

		// invalid timestamp
		content = new JSONObject()
			.put("amount", 12123.45)
			.put("timestamp", DateTime.now().minus(Duration.standardSeconds(10)).toString())
			.toString();

		this.mockMvc.perform(post("/transactions")
			.contentType(contentType)
			.content(content))
			.andExpect(status().is4xxClientError());
	}

	@Test
	public void testStat() throws Exception {

		this.mockMvc.perform(get("/statistics"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$", hasEntry("sum", 0D)))
			.andExpect(jsonPath("$", hasEntry("min", 0D)))
			.andExpect(jsonPath("$", hasEntry("max", 0D)))
			.andExpect(jsonPath("$", hasEntry("avg", 0D)))
			.andExpect(jsonPath("$", hasEntry("count", 0)));

		this.mockMvc.perform(post("/transactions")
			.contentType(contentType)
			.content(new JSONObject()
				.put("amount", 124.45)
				.put("timestamp", DateTime.now().minus(Duration.standardSeconds(10)).getMillis())
				.toString()))
			.andExpect(status().isCreated());

		this.mockMvc.perform(get("/statistics"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$", hasEntry("sum", 124.45D)))
			.andExpect(jsonPath("$", hasEntry("max", 124.45D)))
			.andExpect(jsonPath("$", hasEntry("min", 124.45D)))
			.andExpect(jsonPath("$", hasEntry("avg", 124.45D)))
			.andExpect(jsonPath("$", hasEntry("count", 1)));

		this.mockMvc.perform(post("/transactions")
			.contentType(contentType)
			.content(new JSONObject()
				.put("amount", 200.356)
				.put("timestamp", DateTime.now().minus(Duration.standardSeconds(10)).getMillis())
				.toString()))
			.andExpect(status().isCreated());

		double avg = valueOf((124.45 + 200.356)).divide(valueOf(2), ROUND_HALF_EVEN).doubleValue();
		this.mockMvc.perform(get("/statistics"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$", hasEntry("sum", (124.45 + 200.356))))
			.andExpect(jsonPath("$", hasEntry("max", 200.356D)))
			.andExpect(jsonPath("$", hasEntry("min", 124.45D)))
			.andExpect(jsonPath("$", hasEntry("avg", avg)))
			.andExpect(jsonPath("$", hasEntry("count", 2)));
	}
}