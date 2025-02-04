-- drop index ix_auth_username; (does not appear to be required)
drop table if exists authorities;
drop table if exists users;

CREATE TABLE users (
  username VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL,
  pubkey VARCHAR(100) NOT NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (username)
);

CREATE TABLE authorities (
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  FOREIGN KEY (username) REFERENCES users(username)
);

CREATE UNIQUE INDEX ix_auth_username on authorities (username,authority);