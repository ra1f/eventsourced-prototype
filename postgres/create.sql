CREATE TABLE eventlog (\
  id bigint NOT NULL,\
  event varchar(80) NOT NULL,\
  animal_id varchar(80) NOT NULL,\
  occurence timestamp NOT NULL,\
  PRIMARY KEY(id)\
);
CREATE TABLE zoo (\
  animal_id varchar(80) NOT NULL,\
  last_occurence timestamp NOT NULL,\
  feeling_of_satiety varchar(40),\
  mindstate varchar(40),\
  hygiene varchar(40),\
  PRIMARY KEY (animal_id)\
);
CREATE UNIQUE INDEX eventsourced_occurence_idx ON eventlog (occurence);
CREATE SEQUENCE hibernate_sequence\
    START WITH 1\
    INCREMENT BY 1\
    NO MINVALUE\
    NO MAXVALUE\
    CACHE 1;

