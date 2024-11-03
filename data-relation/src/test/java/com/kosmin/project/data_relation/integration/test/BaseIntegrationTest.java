package com.kosmin.project.data_relation.integration.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("int")
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

  protected static final String COMMON_POST_URL = "table_queries/v1/insert";
  protected static final String COMMON_PUT_URL = "table_queries/v1/update";
  @Autowired protected WebTestClient webTestClient;

  @BeforeAll
  void setUp(@Autowired JdbcTemplate jdbcTemplate) {
    jdbcTemplate.execute("DROP TABLE IF EXISTS checking_records, credit_records");
    jdbcTemplate.execute(
        """
        CREATE TABLE checking_records (
              banking_record_id       SERIAL PRIMARY KEY,
              transaction_description VARCHAR(255)   NOT NULL,
              transaction_date        DATE           NOT NULL,
              transaction_type        VARCHAR(255)   NOT NULL,
              transaction_amount      DECIMAL(10, 2) NOT NULL,
              balance                 DECIMAL(10, 2) NOT NULL
              )""");
    jdbcTemplate.execute(
        """
        CREATE TABLE credit_records (
              credit_record_id        SERIAL PRIMARY KEY,
              banking_record_id       INTEGER,
              transaction_date        DATE           NOT NULL,
              transaction_description VARCHAR(255)   NOT NULL,
              transaction_category    VARCHAR(255)   NOT NULL,
              transaction_type        VARCHAR(255)   NOT NULL,
              transaction_amount      DECIMAL(10, 2) NOT NULL,
              FOREIGN KEY (banking_record_id) REFERENCES checking_records (banking_record_id)
              )""");
    jdbcTemplate.execute(
        """
           INSERT INTO checking_records (transaction_description, transaction_date, transaction_type, transaction_amount, balance)\s
           VALUES('Withdrawal from Credit Card Payment', '2024-10-02', 'Debit', '2964.03', '2688.42')
          \s""");
    jdbcTemplate.execute(
        """
          INSERT INTO credit_records (transaction_date, transaction_description, transaction_category, transaction_type, transaction_amount)\s
          VALUES('2024-09-28', 'BAKERY', 'Food & Drink', 'Sale', '-25.00')""");
  }

  @AfterAll
  void tearDown(@Autowired JdbcTemplate jdbcTemplate) {
    jdbcTemplate.execute("DROP TABLE IF EXISTS checking_records, credit_records");
  }
}
