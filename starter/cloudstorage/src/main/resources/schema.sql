CREATE TABLE IF NOT EXISTS USERS (
  userid serial PRIMARY KEY,
  username VARCHAR(20) UNIQUE,
  salt VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  firstname VARCHAR(20),
  lastname VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS NOTES (
    noteid serial PRIMARY KEY,
    notetitle VARCHAR(20),
    notedescription VARCHAR (1000),
    userId INT,
    foreign key (userid) references USERS(userid)
);

CREATE TABLE IF NOT EXISTS FILES (
    fileId serial PRIMARY KEY,
    filename VARCHAR,
    contenttype VARCHAR,
    filesize VARCHAR,
    userid INT,
    filedata BYTEA,
    foreign key (userid) references USERS(userid)
);

CREATE TABLE IF NOT EXISTS CREDENTIALS (
    credentialid serial PRIMARY KEY,
    url VARCHAR(255),
    username VARCHAR (20),
    password VARCHAR (255),
    enckey VARCHAR(255),
    userid INT,
    foreign key (userid) references USERS(userId)
);