package com.kosmin;

import com.kosmin.service.DataRelationService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ApiGatewayApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("int")
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class BaseIntegrationTest {

  @Autowired protected DataRelationService dataRelationService;

  @BeforeAll
  void setUp(@Autowired JdbcTemplate jdbcTemplate) {
    jdbcTemplate.execute("DROP TABLE IF EXISTS checking_records, credit_records");
    jdbcTemplate.execute(
        """
          CREATE TABLE checking_records (
          checking_record_id      SERIAL PRIMARY KEY,
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
            checking_record_id      INTEGER        NOT NULL,
            transaction_date        DATE           NOT NULL,
            transaction_description VARCHAR(255)   NOT NULL,
            transaction_category    VARCHAR(255)   NOT NULL,
            transaction_type        VARCHAR(255)   NOT NULL,
            transaction_amount      DECIMAL(10, 2) NOT NULL,
            FOREIGN KEY (checking_record_id) REFERENCES checking_records (checking_record_id)
            ON DELETE CASCADE
              )""");
    jdbcTemplate.execute(
        """
           INSERT INTO checking_records (transaction_description, transaction_date, transaction_type, transaction_amount, balance)\s
           VALUES('Withdrawal from Credit Card Payment', '2024-10-02', 'Debit', '2964.03', '2688.42')
          \s""");
    jdbcTemplate.execute(
        """
          INSERT INTO credit_records (checking_record_id, transaction_date, transaction_description, transaction_category, transaction_type, transaction_amount)\s
          VALUES(1, '2024-09-28', 'BAKERY', 'Food & Drink', 'Sale', '-25.00')""");
  }

  @AfterAll
  void tearDown(@Autowired JdbcTemplate jdbcTemplate) {
    jdbcTemplate.execute("DROP TABLE IF EXISTS checking_records, credit_records");
  }
}
