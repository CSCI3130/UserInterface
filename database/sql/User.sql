CREATE SCHEMA piccritic;

CREATE TABLE piccritic."User"
(
  "User_handle" character varying(15) NOT NULL,
  "User_hash" text NOT NULL,
  "User_salt" text NOT NULL,
  "User_firstName" character varying(15) NOT NULL,
  "User_lastName" character varying(15) NOT NULL,
  "User_joinDate" date NOT NULL,
  "User_bio" character varying(200),
  "License_ID" integer,
  CONSTRAINT "User_pkey" PRIMARY KEY ("User_handle")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE piccritic."User"
  OWNER TO postgres;
