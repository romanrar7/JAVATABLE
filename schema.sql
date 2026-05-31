CREATE DATABASE IF NOT EXISTS library_db CHARACTER SET utf8mb4;

USE library_db;

CREATE TABLE IF NOT EXISTS books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(150) NOT NULL,
    publish_year INT NOT NULL,
    copies INT NOT NULL DEFAULT 1
);

INSERT INTO books (title, author, publish_year, copies) VALUES
('Кобзар', 'Тарас Шевченко', 1840, 5),
('Енеїда', 'Іван Котляревський', 1798, 3),
('Лісова пісня', 'Леся Українка', 1911, 2);
