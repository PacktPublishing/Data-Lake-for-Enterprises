CREATE TABLE public.customer
(
	id integer NOT NULL,
	first_name character varying COLLATE pg_catalog."default",
	last_name character varying COLLATE pg_catalog."default",
	dob date,
	CONSTRAINT "customer_pkey" PRIMARY KEY (id)
)
WITH (
	OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.customer
	OWNER to postgres;
