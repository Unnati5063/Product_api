create database product_api;

use product_api;
CREATE TABLE user(
id INT NOT NULL AUTO_INCREMENT,
username VARCHAR(50) NOT NULL,
password VARCHAR(50) NOT NULL,
role VARCHAR(50) NOT NULL,
PRIMARY KEY(id)
);

CREATE TABLE book(
id INT NOT NULL AUTO_INCREMENT,
title VARCHAR(50) NOT NULL,
author VARCHAR(50),
upc float,
price float,
rating FLOAT,
PRIMARY KEY (id)
);

INSERT INTO book
	VALUES(null, 'Anna Karenina', 'Leo Tolstoy', '896745', 15, 9.5),
    (null, 'War and Peace', 'Leo Tolstoy', '1239870', 25, 9.5),
    (null, 'Middlemarch', 'George Eliot', '456782', 19, 9.4),
    (null, 'In Search of Lost Time', 'Marcel Proust', '9872350', 16, 9.2),
    (null, 'Hamlet', 'William Shakespeare', '4562340', 21, 8.9);
    
CREATE TABLE user_book(
user_id INT NOT NULL,
book_id INT NOT NULL,
FOREIGN KEY (user_id)
	REFERENCES user (id),
FOREIGN KEY (book_id)
	REFERENCES book (id)
	ON UPDATE CASCADE
	ON DELETE CASCADE
);

INSERT INTO user
	value(null, 'admin1', 'admin123', 'ROLE_ADMIN');
    