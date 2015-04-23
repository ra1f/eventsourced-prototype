CREATE TABLE eventlog (\
  agg_id varchar(80) NOT NULL,\
  seq_id bigint NOT NULL,\
  event varchar(80) NOT NULL,\
  occurence timestamp NOT NULL,\
  PRIMARY KEY(agg_id, seq_id)\
);
CREATE TABLE zoo (\
  animal_id varchar(80) NOT NULL,\
  seq_id bigint NOT NULL,\
  last_occurence timestamp NOT NULL,\
  feeling_of_satiety varchar(40) NOT NULL,\
  mindstate varchar(40) NOT NULL,\
  hygiene varchar(40) NOT NULL,\
  optlock integer NOT NULL,\
  PRIMARY KEY (animal_id)\
);

