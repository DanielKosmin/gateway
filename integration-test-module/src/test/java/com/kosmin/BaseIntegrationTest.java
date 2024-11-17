package com.kosmin;

import com.kosmin.model.AuthRequest;
import com.kosmin.service.async.service.AsyncCsvProcessingService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
    classes = ApiGatewayApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("int")
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureWebTestClient(timeout = "36000")
public abstract class BaseIntegrationTest {

  protected static final String POST_URL = "gateway/v1";
  protected static String TOKEN = "";
  @Autowired protected AsyncCsvProcessingService asyncCsvProcessingService;
  @Autowired protected WebTestClient webTestClient;

  @BeforeAll
  void setUp(@Autowired JdbcTemplate jdbcTemplate, @Autowired WebTestClient webTestClient) {
    TOKEN =
        webTestClient
            .post()
            .uri("api/auth/login")
            .bodyValue(AuthRequest.builder().username("admin").password("admin").build())
            .exchange()
            .expectBody(String.class)
            .returnResult()
            .getResponseBody();
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
