CREATE USER gis_server WITH PASSWORD '1b44mgfzl8i1nhxl94x5';
CREATE USER gis_basic WITH PASSWORD '897jiqi4eqnttbfspqco';
ALTER USER gis_server SUPERUSER;
ALTER USER gis_basic SUPERUSER;
GRANT UPDATE ON project TO gis_server;
GRANT UPDATE ON project TO gis_basic;



CREATE TABLE "student" (
  "id" SERIAL PRIMARY KEY,
  "username" varchar(30) UNIQUE NOT NULL,
  "password" char(48),
  "fname" varchar(30),
  "lname" varchar(30),
  "sex" char(1),
  "created_at" timestamp,
  "dob" date,
  "faculty" varchar(40) NOT NULL,
  "profession" varchar(40) NOT NULL,
  "gtype" varchar(20) NOT NULL,
  "gyear" date,
  "syear" date,
  "description" text,
  "languages" varchar(40),
  "skills" varchar(80),
  "file_path" varchar(100)
);

CREATE TABLE "post" (
  "id" SERIAL PRIMARY KEY,
  "from" int NOT NULL,
  "title" varchar(100) NOT NULL,
  "content" text,
  "file_path" varchar(100),
  "like" int,
  "created_at" timestamp
);

CREATE TABLE "job_add" (
  "id" SERIAL PRIMARY KEY,
  "from" int NOT NULL,
  "title" varchar(100) NOT NULL,
  "company" varchar(100) NOT NULL,
  "content" text,
  "created_at" timestamp NOT NULL
);

CREATE TABLE "form" (
  "id" SERIAL PRIMARY KEY,
  "from" int NOT NULL UNIQUE,
  "to" int NOT NULL,
  "file" int UNIQUE NOT NULL
);

CREATE TABLE "experiences" (
  "id" SERIAL PRIMARY KEY,
  "owner" int NOT NULL,
  "start_date" date NOT NULL,
  "end_date" date,
  "title" varchar(40) NOT NULL,
  "at" varchar(40) NOT NULL
);

CREATE TABLE "fp" (
  "id" SERIAL PRIMARY KEY,
  "from" int NOT NULL,
  "name" varchar(20),
  "path" varchar(60) NOT NULL,
  "type" varchar(10)
);

ALTER TABLE "post" ADD FOREIGN KEY ("from") REFERENCES "student" ("id");

ALTER TABLE "job_add" ADD FOREIGN KEY ("from") REFERENCES "student" ("id");

ALTER TABLE "student" ADD FOREIGN KEY ("id") REFERENCES "form" ("from");

ALTER TABLE "form" ADD FOREIGN KEY ("to") REFERENCES "job_add" ("id");

ALTER TABLE "experiences" ADD FOREIGN KEY ("owner") REFERENCES "student" ("id");

ALTER TABLE "fp" ADD FOREIGN KEY ("from") REFERENCES "student" ("id");

ALTER TABLE "fp" ADD FOREIGN KEY ("from") REFERENCES "post" ("id");

ALTER TABLE "fp" ADD FOREIGN KEY ("from") REFERENCES "fp" ("id");

ALTER TABLE "fp" ADD FOREIGN KEY ("id") REFERENCES "form" ("file");