-- --------------------------------------------------------
-- ----- MASS-MAILER DB DUMP ------------------------------
-- --------------------------------------------------------

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `massmailer`
--
CREATE DATABASE `massmailer` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `massmailer`;

-- Email Addresses Table of Group one Set.

CREATE TABLE Group1
(
tuple int NOT NULL AUTO_INCREMENT,
email varchar(100),
PRIMARY KEY (tuple)
);

insert into Group1 (tuple, email) values (1, "harshato@gmail.com");
insert into Group1 (tuple, email) values (2, "harshadura@gmail.com");
insert into Group1 (tuple, email) values (3, "test1@gmail.com");
insert into Group1 (tuple, email) values (4, "test2@gmail.com");
 
