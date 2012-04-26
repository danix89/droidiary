--
-- Creazione schema Droidiary
--


CREATE DATABASE IF NOT EXISTS my_droidiary;
USE my_droidiary;


-- TABELLE


--
-- Definizione tabella account
--


DROP TABLE IF EXISTS `account`;
create table `account`(
`_id` integer PRIMARY KEY,
`userName` VARCHAR(6) NOT NULL,
`password` VARCHAR(5) NOT NULL);


--
-- Riempimento tabella account
--


INSERT INTO `account` (`_id`, `username`,`password`) VALUES
 ('0001','marpir','m0001'),
 ('0002','saladd','s0001'),
 ('0003','aledel','a0001');



--
-- Definizione tabella contatti
--


DROP TABLE IF EXISTS `contatto`;
create table `contatto`(
`_id` integer PRIMARY KEY,
`id_account` integer NOT NULL,
`nome` VARCHAR(15) NOT NULL,
`cognome` VARCHAR(15) NOT NULL,
`citta` VARCHAR(15),
`cellulare` VARCHAR(11),
`numeroCasa` VARCHAR(11),
`mail` VARCHAR(11),
FOREIGN KEY(`id_account`) REFERENCES account(`_id`));


--
-- Riempimento tabella contatti
--


INSERT INTO `contatto` (`_id`,`id_account`,`nome`,`cognome`,`citta`, `cellulare`, `numeroCasa`, `mail`) VALUES
 ('0001','0001','marco', 'rossi', 'napoli', '3931567893', '082523185', 'mr78@hotmail.it'),
 ('0002','0001','francesco', 'vergassola', 'milano', '3924566789', '081234565', 'fv@hotmail.it');


--
-- Definizione tabella appuntamenti
--


DROP TABLE IF EXISTS `appuntamento`;
create table `appuntamento`(
`_id` integer PRIMARY KEY,
`id_contatto` integer,
`id_account` integer NOT NULL,
`data_ora` DATETIME NOT NULL,
`citta` VARCHAR(15),
FOREIGN KEY(`id_account`) REFERENCES account(`_id`),
FOREIGN KEY(`id_contatto`) REFERENCES contatto(`_id`));


--
-- Riempimento tabella appuntamenti
--


INSERT INTO `appuntamento` (`_id`, `id_contatto`,`id_account`,`data/ora`,`citta`) VALUES
 ('0001','0001','0001', '2012/11/11 13:20:00', 'napoli'),
 ('0002','0001','0001', '2012/09/11 15:40:00', 'roma');