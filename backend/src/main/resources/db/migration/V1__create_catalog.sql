CREATE TABLE funds (
    id BIGINT PRIMARY KEY,
    ticker VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    segment VARCHAR(50) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    country VARCHAR(2) NOT NULL,
    price NUMERIC(19,4) NOT NULL CHECK (price > 0),
    monthly_distribution NUMERIC(19,4) NOT NULL CHECK (monthly_distribution >= 0),
    as_of DATE NOT NULL,
    source VARCHAR(200) NOT NULL,
    demo BOOLEAN NOT NULL
);
CREATE TABLE properties (
    id BIGINT PRIMARY KEY,
    fund_id BIGINT NOT NULL REFERENCES funds(id),
    name VARCHAR(200) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(50) NOT NULL,
    country VARCHAR(2) NOT NULL,
    region VARCHAR(20) NOT NULL CHECK (region IN ('NORTE','NORDESTE','CENTRO_OESTE','SUDESTE','SUL'))
);
CREATE INDEX idx_properties_region ON properties(region);
CREATE INDEX idx_properties_fund ON properties(fund_id);
