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
import com.n26.challenge.tnxprocessing.repo.TransactionRepo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

	private MediaType contentType = MediaType.APPLICATION_JSON;

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webAppContext;

	@Autowired
	private TransactionRepo transactionRepo;

	@Before
	public void before() {
		this.mockMvc = webAppContextSetup(webAppContext).build();
		transactionRepo.deleteAll();
	}

	@After
	public void after() {
		transactionRepo.deleteAll();
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

		this.mockMvc.perform(post("/transactions")
			.contentType(contentType)
			.content(content))
			.andExpect(status().is4xxClientError());

		content = new JSONObject()
			.put("amount", "$2342")
			.put("timestamp", DateTime.now().minus(Duration.standardSeconds(10)).getMillis())
			.toString();

		this.mockMvc.perform(post("/transactions")
			.contentType(contentType)
			.content(content))
			.andExpect(status().is4xxClientError());

		content = new JSONObject()
			.put("amount", 12123.45)
			.put("timestamp", DateTime.now().minus(Duration.standardSeconds(10)).toString())
			.toString();

		this.mockMvc.perform(post("/transactions")
			.contentType(contentType)
			.content(content))
			.andExpect(status().is4xxClientError());
	}
}