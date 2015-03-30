CREATE DATABASE eventsourced;

CREATE TABLE eventsourced.eventlog (
  id bigint NOT NULL,\
  event varchar(80) NOT NULL,\
  animalId varchar(80) NOT NULL,\
  occurence timestamp NOT NULL\
  PRIMARY KEY(id)\
);

CREATE UNIQUE INDEX eventsourced.occurence_idx ON eventsourced.eventlog (occurence);

CREATE TABLE eventsourced.zoo (\
  animalId varchar(80) NOT NULL,\
  last_occurence timestamp NOT NULL,\
  feeling_of_satiety varchar(40),\
  mindstate varchar(40),\
  hygiene varchar(40),\
  PRIMARY KEY (animalId)\
);