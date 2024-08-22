DROP TABLE IF EXISTS account CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS event CASCADE;
DROP TABLE IF EXISTS compilation CASCADE;
DROP TABLE IF EXISTS compilation_event CASCADE;
DROP TABLE IF EXISTS participation CASCADE;

CREATE TABLE IF NOT EXISTS account (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	email VARCHAR NOT NULL,
	name VARCHAR NOT NULL,
	CONSTRAINT pk_account PRIMARY KEY (id),
	CONSTRAINT uq_account_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS category (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	name VARCHAR(50) NOT NULL,
	CONSTRAINT pk_category PRIMARY KEY (id),
	CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS location (
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat        FLOAT NOT NULL,
    lon        FLOAT NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS event (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	annotation VARCHAR NOT NULL,
	category_id BIGINT,
	initiator_id BIGINT,
	paid BOOLEAN DEFAULT FALSE,
	title VARCHAR NOT NULL,
	created_on TIMESTAMP WITHOUT TIME ZONE,
	description VARCHAR NOT NULL,
	event_date TIMESTAMP WITHOUT TIME ZONE,
	location_id BIGINT,
	participant_limit INTEGER DEFAULT 0,
	request_moderation BOOLEAN DEFAULT TRUE,
	published_on TIMESTAMP WITHOUT TIME ZONE,
	confirmed_requests BIGINT,
	state VARCHAR,
	views BIGINT DEFAULT 0,
	CONSTRAINT pk_event PRIMARY KEY (id),
	CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(id),
    CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES location(id),
    CONSTRAINT fk_initiator FOREIGN KEY (initiator_id) REFERENCES account(id)
);

CREATE TABLE IF NOT EXISTS compilation (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	title VARCHAR(50) NOT NULL,
	pinned BOOLEAN NOT NULL,
	CONSTRAINT pk_compilation PRIMARY KEY (id),
	CONSTRAINT uq_compilation_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS compilation_event (
    event_id BIGINT NOT NULL CONSTRAINT FK_EVENT_COMPILATION REFERENCES event,
    compilation_id BIGINT NOT NULL CONSTRAINT FK_COMPILATION_EVENTS REFERENCES compilation
    --CONSTRAINT pk_compilation_event PRIMARY KEY (event_id)
);

CREATE TABLE IF NOT EXISTS participation (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	created TIMESTAMP WITHOUT TIME ZONE,
	event_id BIGINT NOT NULL,
	requester_id BIGINT NOT NULL,
	status VARCHAR(30) NOT NULL,
	CONSTRAINT pk_participation PRIMARY KEY (id),
	CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES event(id),
	CONSTRAINT fk_requester FOREIGN KEY (requester_id) REFERENCES account(id)
);
