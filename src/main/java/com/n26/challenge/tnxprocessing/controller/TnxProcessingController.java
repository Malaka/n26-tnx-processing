package com.n26.challenge.tnxprocessing.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.n26.challenge.tnxprocessing.domain.Transaction;
import com.n26.challenge.tnxprocessing.service.TransactionService;

/**
 * @author: malaka
 * Date: 5/20/17
 * Time: 2:53 AM
 */
@RestController
public class TnxProcessingController {

	private static final Logger log = LoggerFactory.getLogger(TnxProcessingController.class);

	@Autowired
	private TransactionService transactionService;

	@RequestMapping(value = "/transactions", method = RequestMethod.POST)
	public HttpEntity<Void> recordTransaction(@Valid @RequestBody Transaction transaction) {

		log.info("transaction record with {}", transaction);
		Boolean status = transactionService.recordTnx(transaction);

		HttpStatus httpStatus = status ? HttpStatus.CREATED : HttpStatus.NO_CONTENT;
		return new ResponseEntity<>(httpStatus);
	}
}
