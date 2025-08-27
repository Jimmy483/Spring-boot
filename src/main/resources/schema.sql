CREATE database IF NOT EXISTS test;

USE test;

CREATE TABLE IF NOT EXISTS userInfo (
  id bigint NOT NULL AUTO_INCREMENT,
  firstName varchar(255) DEFAULT NULL,
  lastName varchar(255) DEFAULT NULL,
  username varchar(255) DEFAULT NULL,
  passwd varchar(255) DEFAULT NULL,
  displayPicture longtext,
  PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS userRole (
  id int NOT NULL AUTO_INCREMENT,
  userId bigint DEFAULT NULL,
  role varchar(100) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY userId (userId),
  CONSTRAINT 'userrole_ibfk_1' FOREIGN KEY (userId) REFERENCES userinfo (id)
);

CREATE TABLE IF NOT EXISTS userSetting (
   id bigint NOT NULL AUTO_INCREMENT,
   userId bigint DEFAULT NULL,
   theme varchar(50) DEFAULT NULL,
   PRIMARY KEY (id)
 );

CREATE TABLE IF NOT EXISTS food (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  price int DEFAULT NULL,
  image varchar(500) DEFAULT NULL,
  lastUpdated date DEFAULT NULL,
  createdBy bigint DEFAULT NULL,
  updatedBy bigint DEFAULT NULL,
  isDeleted boolean DEFAULT false,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS messages (
  id bigint NOT NULL AUTO_INCREMENT,
  receiverId bigint DEFAULT NULL,
  senderId bigint DEFAULT NULL,
  content longtext,
  isRead Boolean DEFAULT false,
  lastRead datetime DEFAULT NULL,
  sentDate datetime DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS userCreateStatus (
  id bigint NOT NULL AUTO_INCREMENT,
  url varchar(255) DEFAULT NULL,
  status varchar(10) DEFAULT NULL,
  PRIMARY KEY (id)
);
