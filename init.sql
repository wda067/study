CREATE DATABASE mydb;

GRANT ALL PRIVILEGES ON mydb.* TO 'myuser'@'%';

FLUSH PRIVILEGES;

CREATE TABLE payment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         payment_key VARCHAR(255),
                         order_id VARCHAR(255),
                         order_name VARCHAR(255),
                         method VARCHAR(255),
                         total_amount BIGINT,
                         member_id BIGINT,
                         status VARCHAR(255),
                         requested_at TIMESTAMP,
                         FOREIGN KEY (member_id) REFERENCES member(id)
);