CREATE TABLE public.address
(
	id integer NOT NULL,
	custumer_id integer NOT NULL,
	street1 character varying COLLATE pg_catalog."default",
	street2 character varying COLLATE pg_catalog."default",
	city character varying COLLATE pg_catalog."default",
	state character varying COLLATE pg_catalog."default",
	country character varying COLLATE pg_catalog."default",
	zip_pin_postal_code character varying COLLATE pg_catalog."default",
	CONSTRAINT address_pkey PRIMARY KEY (id),
	CONSTRAINT customer_fkey FOREIGN KEY (custumer_id)
    	REFERENCES public.customer (id) MATCH SIMPLE
    	ON UPDATE NO ACTION
    	ON DELETE NO ACTION
)
WITH (
	OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.address
	OWNER to postgres;