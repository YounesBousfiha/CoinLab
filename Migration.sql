CREATE DATABASE coinlab;

\connect coinlab;


CREATE TYPE wallet_type AS ENUM (
        'BITCOIN',
        'ETHEREUM'
    );


CREATE TYPE transaction_priority AS ENUM (
    'ECONOMIC',
    'STANDARD',
    'RAPID'
    );

CREATE TYPE transaction_status AS ENUM(
    'PENDING',
    'CONFIRMED',
    'REJECTED'
    );

CREATE TABLE wallet (
    id SERIAL PRIMARY KEY,
    balance DECIMAL(20, 8) NOT NULL DEFAULT 0.0,
    type wallet_type NOT NULL,
    address VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE transaction (
    id SERIAL PRIMARY KEY,
    source VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    amount  DECIMAL(20, 8) NOT NULL,
    fee DECIMAL(20, 8) NOT NULL DEFAULT 0.0,
    priority transaction_priority NOT NULL,
    status transaction_status NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    wallet_id INTEGER,
    FOREIGN KEY (wallet_id) REFERENCES wallet(id) ON DELETE SET NULL
);

CREATE TYPE log_level AS ENUM (
        'INFO',
        'WARN',
        'ERROR',
        'FATAL'
    );


CREATE TABLE log_message (
    id SERIAL PRIMARY KEY,
    timestamp TIMESTAMP DEFAULT current_timestamp,
    level log_level NOT NULL,
    logger VARCHAR(255) NOT NULL,
    message TEXT NOT NULL
);

CREATE INDEX idx_transaction_status ON transaction(status);