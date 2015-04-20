CREATE TABLE eventlog (
  agg_id varchar(80) NOT NULL,
  seq_id int NOT NULL,
  trans_id int NOT NULL,
  event varchar(80) NOT NULL,
  occurence timestamp NOT NULL,
  PRIMARY KEY(agg_id, seq_id)
);
--CREATE UNIQUE INDEX eventsourced_occurence_idx ON eventlog (occurence);
--CREATE SEQUENCE hibernate_sequence
--    START WITH 1
--    INCREMENT BY 1
--    NO MINVALUE
--    NO MAXVALUE
--    CACHE 1;

