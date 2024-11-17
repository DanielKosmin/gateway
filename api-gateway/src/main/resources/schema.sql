CREATE TABLE IF NOT EXISTS users
(
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    enabled  BOOLEAN      NOT NULL
);

CREATE TABLE IF NOT EXISTS authorities
(
    username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users (username)
);

-- prevent duplicate
CREATE UNIQUE INDEX IF NOT EXISTS unique_auth_username ON authorities (username, authority);

CREATE TABLE IF NOT EXISTS checking_records
(
    checking_record_id      SERIAL PRIMARY KEY,
    transaction_description VARCHAR(255)   NOT NULL,
    transaction_date        DATE           NOT NULL,
    transaction_type        VARCHAR(255)   NOT NULL,
    transaction_amount      DECIMAL(10, 2) NOT NULL,
    balance                 DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS credit_records
(
    credit_record_id        SERIAL PRIMARY KEY,
    checking_record_id      INTEGER        NOT NULL,
    transaction_date        DATE           NOT NULL,
    transaction_description VARCHAR(255)   NOT NULL,
    transaction_category    VARCHAR(255)   NOT NULL,
    transaction_type        VARCHAR(255)   NOT NULL,
    transaction_amount      DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (checking_record_id) REFERENCES checking_records (checking_record_id)
        ON DELETE CASCADE
);