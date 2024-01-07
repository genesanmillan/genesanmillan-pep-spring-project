// Note: Wasn't able to use automatic named queries as the models have underscores(_) for some/all of their attributes

package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {}
