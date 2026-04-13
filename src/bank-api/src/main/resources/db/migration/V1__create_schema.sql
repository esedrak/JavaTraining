-- V1__create_schema.sql
-- Creates the javabank schema: accounts, transactions, transfers.

CREATE TABLE accounts
(
    id         UUID           NOT NULL,
    owner      VARCHAR(200)   NOT NULL,
    balance    NUMERIC(18, 2) NOT NULL,
    created_at TIMESTAMPTZ    NOT NULL,
    updated_at TIMESTAMPTZ    NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE transactions
(
    id          UUID           NOT NULL,
    account_id  UUID           NOT NULL,
    amount      NUMERIC(18, 2) NOT NULL,
    type        VARCHAR(50)    NOT NULL,
    created_at  TIMESTAMPTZ    NOT NULL,
    description VARCHAR(500),
    PRIMARY KEY (id),
    CONSTRAINT fk_transactions_account FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE INDEX idx_transactions_account_id ON transactions (account_id);

CREATE TABLE transfers
(
    id              UUID           NOT NULL,
    from_account_id UUID           NOT NULL,
    to_account_id   UUID           NOT NULL,
    amount          NUMERIC(18, 2) NOT NULL,
    status          VARCHAR(50)    NOT NULL,
    failure_reason  TEXT,
    created_at      TIMESTAMPTZ    NOT NULL,
    updated_at      TIMESTAMPTZ    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_transfers_from_account FOREIGN KEY (from_account_id) REFERENCES accounts (id),
    CONSTRAINT fk_transfers_to_account FOREIGN KEY (to_account_id) REFERENCES accounts (id)
);

CREATE INDEX idx_transfers_from_account_id ON transfers (from_account_id);
CREATE INDEX idx_transfers_to_account_id ON transfers (to_account_id);
