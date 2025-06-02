-- Active: 1748709464767@@sql12.freesqldatabase.com@3306@sql12782324
CREATE DATABASE cuci_sepatu_db;

USE cuci_sepatu_db;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    shoe_type VARCHAR(100) NOT NULL,
    wash_price DECIMAL(10, 2) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
