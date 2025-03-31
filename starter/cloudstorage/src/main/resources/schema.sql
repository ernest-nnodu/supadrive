CREATE TABLE IF NOT EXISTS USERS (
  userid serial PRIMARY KEY,
  username VARCHAR(20) UNIQUE,
  salt VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  firstname VARCHAR(20),
  lastname VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS NOTES (
    noteId serial PRIMARY KEY,
    noteTitle VARCHAR(20),
    noteDescription VARCHAR (1000),
    userId INT,
    foreign key (userId) references USERS(userId)
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
