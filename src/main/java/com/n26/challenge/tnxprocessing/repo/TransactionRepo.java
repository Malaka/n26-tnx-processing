package com.n26.challenge.tnxprocessing.repo;

import org.springframework.data.repository.CrudRepository;

import com.n26.challenge.tnxprocessing.domain.Transaction;

/**
 * @author: malaka
 * Date: 5/20/17
 * Time: 2:48 AM
 */
public interface TransactionRepo extends CrudRepository<Transaction, Long> {

}
