-- CREATE USER gis_server WITH PASSWORD '1b44mgfzl8i1nhxl94x5';
-- CREATE USER gis_basic WITH PASSWORD '897jiqi4eqnttbfspqco';
-- ALTER USER gis_server SUPERUSER;
-- ALTER USER gis_basic SUPERUSER;
-- GRANT UPDATE ON project TO gis_server;
-- GRANT UPDATE ON project TO gis_basic;

ALTER TABLE "user"
    DROP CONSTRAINT user_profession_fkey;
ALTER TABLE "form"
    DROP CONSTRAINT form_from_fkey;
ALTER TABLE "form"
    DROP CONSTRAINT form_to_fkey;

DROP TABLE "university";
DROP TABLE "job_ad";
DROP TABLE "form";
DROP TABLE "announcement";
DROP TABLE "user";

CREATE TABLE "user"
(
    "id"         SERIAL PRIMARY KEY NOT NULL,
    "f_name"     varchar(30)        NOT NULL,
    "l_name"     varchar(30)        NOT NULL,
    "mail"       varchar(60) UNIQUE NOT NULL,
    "phone"      varchar(15) UNIQUE NOT NULL,
    "password"   varchar(30)        NOT NULL,
    "gender"     varchar(15)            NOT NULL,         -- enum : male female other
    "dob"        date               NOT NULL,
    "created_at" timestamp DEFAULT clock_timestamp(), -- automatically initialized
    -- "faculty" varchar(40) NOT NULL,
    "profession" varchar(100)       NOT NULL,
    "g_year"     date               NOT NULL,

    "company"    varchar(100),
    "open2work"  boolean NOT NULL DEFAULT false,
    "about"      text,
    "image_path" varchar(100),
    "cv_path"    varchar(100)
);

CREATE TABLE "university"
(
    "id"              SERIAL PRIMARY KEY NOT NULL,
    "faculty_name"    varchar(100)       NOT NULL,
    "profession_name" varchar(100)       NOT NULL UNIQUE
);

CREATE TABLE "announcement"
(
    "id"         SERIAL PRIMARY KEY NOT NULL,
    "from"       int                NOT NULL,
    "title"      varchar(100)       NOT NULL,
    "content"    text               NOT NULL,
    "link"       text,
    "like"       int                NOT NULL DEFAULT 0,
    "created_at" timestamp          NOT NULL DEFAULT clock_timestamp() -- automatically initialized

);

CREATE TABLE "job_ad"
(
    "id"         SERIAL PRIMARY KEY NOT NULL,
    "from"       int                NOT NULL,
    "title"      varchar(100)       NOT NULL,
    "content"    text               NOT NULL,
    "created_at" timestamp DEFAULT clock_timestamp() -- automatically initialized

);

CREATE TABLE "form"
(
    "id"         SERIAL PRIMARY KEY,
    "from"       int NOT NULL,
    "to"         int NOT NULL,
    "created_at" timestamp DEFAULT clock_timestamp() -- automatically initialized

);
ALTER TABLE "job_ad"
    ADD FOREIGN KEY ("from") REFERENCES "user" ("id");

ALTER TABLE "announcement"
    ADD FOREIGN KEY ("from") REFERENCES "user" ("id");

ALTER TABLE "form"
    ADD FOREIGN KEY ("from") REFERENCES "user" ("id");

ALTER TABLE "form"
    ADD FOREIGN KEY ("to") REFERENCES "job_ad" ("id");

ALTER TABLE "user"
    ADD FOREIGN KEY ("profession") REFERENCES "university" ("profession_name");
