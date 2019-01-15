-- CREATE SEQUENCE
CREATE SEQUENCE ingswschema.sequence_id INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;
ALTER SEQUENCE ingswschema.sequence_id OWNER TO ingswuser;
GRANT ALL ON SEQUENCE ingswschema.sequence_id TO ingswuser;

-- Table: ingswschema.supply
-- DROP TABLE ingswschema.supply;
CREATE TABLE ingswschema.supply
(
	id bigint NOT NULL,
    CONSTRAINT supply_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE tbs_ingsw
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_ingsw;
ALTER TABLE ingswschema.supply OWNER to ingswuser;
GRANT ALL ON TABLE ingswschema.supply TO ingswuser;



-- Table: ingswschema.users
-- DROP TABLE ingswschema.users;
CREATE TABLE ingswschema.users
(
	id_user character varying(255) COLLATE pg_catalog."default" NOT NULL,
    email character varying(255) COLLATE pg_catalog."default",
    nick_name character varying(255) COLLATE pg_catalog."default",
    telephone_number character varying(255) COLLATE pg_catalog."default",
    path_image character varying(255) COLLATE pg_catalog."default",
    password_registration character varying(255) COLLATE pg_catalog."default",
    is_administrator boolean,
    id_supply bigint,
    timestamp_one_time_pass timestamp,
    one_time_pass character varying(255),
    CONSTRAINT users_pkey PRIMARY KEY (id_user)
        USING INDEX TABLESPACE tbs_ingsw,
    CONSTRAINT user_id_supply_fkey FOREIGN KEY (id_supply)
    REFERENCES ingswschema.supply (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_ingsw;
ALTER TABLE ingswschema.users OWNER to ingswuser;
GRANT ALL ON TABLE ingswschema.users TO ingswuser;


-- Table: ingswschema.home
-- DROP TABLE ingswschema.home;
CREATE TABLE ingswschema.home
(
	id bigint NOT NULL,
    CONSTRAINT home_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE tbs_ingsw
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_ingsw;
ALTER TABLE ingswschema.home OWNER to ingswuser;
GRANT ALL ON TABLE ingswschema.home TO ingswuser;




-- Table: ingswschema.interaction_user_home
-- DROP TABLE ingswschema.interaction_user_home;
CREATE TABLE ingswschema.interaction_user_home
(
	id bigint NOT NULL,
	id_user character varying(255),
	id_home bigint,
	time_request timestamp NOT NULL DEFAULT NOW(),
	command character varying(255),
    CONSTRAINT interaction_user_home_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE tbs_ingsw,
    CONSTRAINT user_id_interaction_user_home_fkey FOREIGN KEY (id_user)
    REFERENCES ingswschema.users (id_user) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT home_id_interaction_user_home_fkey FOREIGN KEY (id_home)
    REFERENCES ingswschema.home (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_ingsw;
ALTER TABLE ingswschema.interaction_user_home OWNER to ingswuser;
GRANT ALL ON TABLE ingswschema.interaction_user_home TO ingswuser;




