DROP TABLE IF EXISTS password_reset_tokens;
DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255),
    full_name  VARCHAR(255),
    status     VARCHAR(255),
    role       VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME,
    UNIQUE (username),
    UNIQUE (email)
);

CREATE TABLE password_reset_tokens (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    token       VARCHAR(255) NOT NULL,
    account_id  BIGINT,
    expiry_date DATETIME,
    UNIQUE (token),
    CONSTRAINT fk_prt_account FOREIGN KEY (account_id) REFERENCES accounts (id)
);
