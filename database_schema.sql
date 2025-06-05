-- Rabbit Farm Management Database Schema

-- Table for Cages
CREATE TABLE cages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cage_number VARCHAR(50) NOT NULL UNIQUE,
    capacity INT NOT NULL,
    location VARCHAR(100)
);

-- Table for Rabbits
CREATE TABLE rabbits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    breed VARCHAR(100),
    date_of_birth DATE,
    gender VARCHAR(10) CHECK (gender IN ('MALE', 'FEMALE')), -- MALE or FEMALE
    status VARCHAR(20) DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE', 'SOLD', 'DECEASED', 'QUARANTINED')), -- e.g., AVAILABLE, SOLD, DECEASED
    cage_id BIGINT,
    notes TEXT,
    FOREIGN KEY (cage_id) REFERENCES cages(id)
);

-- Table for Feed
CREATE TABLE feed (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50), -- e.g., Pellets, Hay, Greens
    quantity_kg DECIMAL(10, 2) NOT NULL,
    purchase_date DATE,
    expiry_date DATE,
    supplier VARCHAR(100)
);

-- Table for Sales
CREATE TABLE sales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rabbit_id BIGINT NOT NULL,
    sale_date DATE NOT NULL,
    customer_name VARCHAR(100),
    price DECIMAL(10, 2) NOT NULL,
    notes TEXT,
    FOREIGN KEY (rabbit_id) REFERENCES rabbits(id)
);

-- Table for Mating Records
CREATE TABLE matings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    male_rabbit_id BIGINT NOT NULL,
    female_rabbit_id BIGINT NOT NULL,
    mating_date DATE NOT NULL,
    expected_birth_date DATE,
    actual_birth_date DATE,
    number_of_kits INT,
    notes TEXT,
    FOREIGN KEY (male_rabbit_id) REFERENCES rabbits(id),
    FOREIGN KEY (female_rabbit_id) REFERENCES rabbits(id)
);

-- Junction table for Feed Consumption by Rabbits (Many-to-Many)
-- This is a more advanced scenario, for now, we might simplify feed tracking
-- or implement it later if needed.
-- CREATE TABLE rabbit_feed (
--    rabbit_id BIGINT NOT NULL,
--    feed_id BIGINT NOT NULL,
--    consumption_date DATE NOT NULL,
--    quantity_consumed_grams DECIMAL(10,2),
--    PRIMARY KEY (rabbit_id, feed_id, consumption_date),
--    FOREIGN KEY (rabbit_id) REFERENCES rabbits(id),
--    FOREIGN KEY (feed_id) REFERENCES feed(id)
-- );

-- Table for Users (for security)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Store hashed passwords
    role VARCHAR(20) NOT NULL -- e.g., ROLE_USER, ROLE_ADMIN
);
