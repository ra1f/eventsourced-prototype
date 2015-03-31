CREATE TABLE eventlog (\
  id bigint NOT NULL,\
  event varchar(80) NOT NULL,\
  animalId varchar(80) NOT NULL,\
  occurence timestamp NOT NULL,\
  PRIMARY KEY(id)\
);
CREATE TABLE zoo (\
  animalId varchar(80) NOT NULL,\
  last_occurence timestamp NOT NULL,\
  feeling_of_satiety varchar(40),\
  mindstate varchar(40),\
  hygiene varchar(40),\
  PRIMARY KEY (animalId)\
);
CREATE UNIQUE INDEX eventsourced_occurence_idx ON eventlog (occurence);

