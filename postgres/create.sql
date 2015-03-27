CREATE TABLE eventlog {
  event varchar(80) NOT NULL,
  animalId varchar(80) NOT NULL,
  occurence timestamp NOT NULL,
  PRIMARY KEY(event, animalId, occurence)
}

CREATE TABLE zoo {
  animalId varchar(80) NOT NULL,
  last_occurence timestamp NOT NULL,
  feeling_of_satiety varchar(40),
  mindstate varchar(40),
  hygiene varchar(40),
  PRIMARY KEY (animalId)
}