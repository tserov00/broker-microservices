--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: customer_accounts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer_accounts (
    id integer NOT NULL,
    customer_id integer NOT NULL,
    phone_number character varying(20) NOT NULL,
    email character varying(100) NOT NULL,
    login character varying(50) NOT NULL,
    password_hash character varying(255) NOT NULL
);


ALTER TABLE public.customer_accounts OWNER TO postgres;

--
-- Name: customer_accounts_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.customer_accounts_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.customer_accounts_id_seq OWNER TO postgres;

--
-- Name: customer_accounts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.customer_accounts_id_seq OWNED BY public.customer_accounts.id;


--
-- Name: customers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customers (
    id integer NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    date_of_birth date NOT NULL,
    passport_series character varying(30) NOT NULL,
    address character varying(255),
    tax_id character varying(30) NOT NULL
);


ALTER TABLE public.customers OWNER TO postgres;

--
-- Name: customers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.customers_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.customers_id_seq OWNER TO postgres;

--
-- Name: customers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.customers_id_seq OWNED BY public.customers.id;


--
-- Name: customer_accounts id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_accounts ALTER COLUMN id SET DEFAULT nextval('public.customer_accounts_id_seq'::regclass);


--
-- Name: customers id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customers ALTER COLUMN id SET DEFAULT nextval('public.customers_id_seq'::regclass);


--
-- Data for Name: customer_accounts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.customer_accounts (id, customer_id, phone_number, email, login, password_hash) FROM stdin;
1	1	+1-212-555-0101	james.smith@email.com	jsmith	hash_jsmith
2	2	+1-310-555-0102	mary.johnson@email.com	mjohnson	hash_mjohnson
3	3	+1-713-555-0103	robert.williams@email.com	rwilliams	hash_rwilliams
4	4	+1-312-555-0104	patricia.brown@email.com	pbrown	hash_pbrown
5	5	+1-602-555-0105	michael.jones@email.com	mjones	hash_mjones
6	6	+1-215-555-0106	linda.garcia@email.com	lgarcia	hash_lgarcia
7	7	+1-210-555-0107	david.miller@email.com	dmiller	hash_dmiller
8	8	+1-619-555-0108	elizabeth.davis@email.com	edavis	hash_edavis
9	9	+1-214-555-0109	william.rodriguez@email.com	wrodriguez	hash_wrodriguez
10	10	+1-512-555-0110	barbara.martinez@email.com	bmartinez	hash_bmartinez
11	11	+1-904-555-0111	richard.hernandez@email.com	rhernandez	hash_rhernandez
12	12	+1-408-555-0112	jennifer.lopez@email.com	jlopez	hash_jlopez
13	13	+1-317-555-0113	charles.gonzalez@email.com	cgonzalez	hash_cgonzalez
14	14	+1-704-555-0114	susan.wilson@email.com	swilson	hash_swilson
15	15	+1-206-555-0115	joseph.anderson@email.com	janderson	hash_janderson
16	16	+1-303-555-0116	jessica.thomas@email.com	jthomas	hash_jthomas
17	17	+1-202-555-0117	thomas.taylor@email.com	ttaylor	hash_ttaylor
18	18	+1-617-555-0118	sarah.moore@email.com	smoore	hash_smoore
19	19	+1-615-555-0119	christopher.jackson@email.com	cjackson	hash_cjackson
20	20	+1-915-555-0120	karen.martin@email.com	kmartin	hash_kmartin
21	21	+1-313-555-0121	matthew.lee@email.com	mlee	hash_mlee
22	22	+1-503-555-0122	lisa.perez@email.com	lperez	hash_lperez
23	23	+1-901-555-0123	daniel.thompson@email.com	dthompson	hash_dthompson
24	24	+1-502-555-0124	nancy.white@email.com	nwhite	hash_nwhite
25	25	+1-410-555-0125	anthony.harris@email.com	aharris	hash_aharris
26	26	+1-414-555-0126	betty.sanchez@email.com	bsanchez	hash_bsanchez
27	27	+1-520-555-0127	mark.clark@email.com	mclark	hash_mclark
28	28	+1-559-555-0128	margaret.ramirez@email.com	mramirez	hash_mramirez
29	29	+1-916-555-0129	paul.lewis@email.com	plewis	hash_plewis
30	30	+1-404-555-0130	kimberly.robinson@email.com	krobinson	hash_krobinson
\.


--
-- Data for Name: customers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.customers (id, first_name, last_name, date_of_birth, passport_series, address, tax_id) FROM stdin;
1	James	Smith	1985-03-14	A1234567	123 Main St, Springfield, IL 62701	123-45-6789
2	Mary	Johnson	1990-07-22	B2345678	456 Oak Ave, Los Angeles, CA 90001	234-56-7890
3	Robert	Williams	1978-11-03	C3456789	789 Pine Rd, Houston, TX 77001	345-67-8901
4	Patricia	Brown	1982-02-17	D4567890	321 Elm St, Chicago, IL 60601	456-78-9012
5	Michael	Jones	1995-09-08	E5678901	654 Maple Dr, Phoenix, AZ 85001	567-89-0123
6	Linda	Garcia	1965-12-25	F6789012	987 Birch Ln, Philadelphia, PA 19101	678-90-1234
7	David	Miller	1973-04-30	G7890123	159 Cedar Ct, San Antonio, TX 78201	789-01-2345
8	Elizabeth	Davis	1988-06-10	H8901234	753 Walnut Way, San Diego, CA 92101	890-12-3456
9	William	Rodriguez	1992-01-19	I9012345	951 Spruce Cir, Dallas, TX 75201	901-23-4567
10	Barbara	Martinez	1970-08-05	J0123456	357 Cherry Blvd, Austin, TX 73301	012-34-5678
11	Richard	Hernandez	1983-10-12	K1234560	246 Ash St, Jacksonville, FL 32201	111-22-3333
12	Jennifer	Lopez	1998-05-27	L0987654	135 Poplar Ave, San Jose, CA 95101	222-33-4444
13	Charles	Gonzalez	1980-03-03	M9876543	864 Magnolia Dr, Indianapolis, IN 46201	333-44-5555
14	Susan	Wilson	1975-07-14	N8765432	579 Dogwood Ln, Charlotte, NC 28201	444-55-6666
15	Joseph	Anderson	1968-11-21	O7654321	753 Redwood Rd, Seattle, WA 98101	555-66-7777
16	Jessica	Thomas	1993-02-06	P6543210	951 Cypress Pl, Denver, CO 80201	666-77-8888
17	Thomas	Taylor	1987-09-15	Q5432109	159 Palm Blvd, Washington, DC 20001	777-88-9999
18	Sarah	Moore	1971-06-28	R4321098	753 Laurel Dr, Boston, MA 02101	888-99-0000
19	Christopher	Jackson	1994-12-11	S3210987	951 Olive St, Nashville, TN 37201	999-00-1111
20	Karen	Martin	1962-04-18	T2109876	357 Hickory Ave, El Paso, TX 79901	121-23-3434
21	Matthew	Lee	1996-08-23	U1098765	246 Cottonwood Ln, Detroit, MI 48201	232-34-4545
22	Lisa	Perez	1984-01-07	V0987654	864 Sycamore Rd, Portland, OR 97201	343-45-5656
23	Daniel	Thompson	1976-10-31	W9876540	135 Beech St, Memphis, TN 38101	454-56-6767
24	Nancy	White	1990-04-09	X8765439	579 Alder Pl, Louisville, KY 40201	565-67-7878
25	Anthony	Harris	1969-12-02	Y7654328	753 Fir Ct, Baltimore, MD 21201	676-78-8989
26	Betty	Sanchez	1997-07-19	Z6543217	951 Hemlock Way, Milwaukee, WI 53201	787-89-9090
27	Mark	Clark	1981-05-13	AB543216	159 Evergreen Dr, Tucson, AZ 85701	898-90-0101
28	Margaret	Ramirez	1972-03-26	CD432105	753 Chestnut St, Fresno, CA 93701	909-01-1212
29	Paul	Lewis	1991-09-04	EF321094	951 Juniper Ln, Sacramento, CA 94201	010-12-2323
30	Kimberly	Robinson	1986-11-11	GH210983	357 Acacia Ave, Atlanta, GA 30301	121-34-4545
\.


--
-- Name: customer_accounts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.customer_accounts_id_seq', 30, true);


--
-- Name: customers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.customers_id_seq', 30, true);


--
-- Name: customer_accounts customer_accounts_login_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_accounts
    ADD CONSTRAINT customer_accounts_login_key UNIQUE (login);


--
-- Name: customer_accounts customer_accounts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_accounts
    ADD CONSTRAINT customer_accounts_pkey PRIMARY KEY (id);


--
-- Name: customers customers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customers
    ADD CONSTRAINT customers_pkey PRIMARY KEY (id);


--
-- Name: customer_accounts uq_customer_accounts_email; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_accounts
    ADD CONSTRAINT uq_customer_accounts_email UNIQUE (email);


--
-- Name: customers uq_customers_passport_series; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customers
    ADD CONSTRAINT uq_customers_passport_series UNIQUE (passport_series);


--
-- Name: customers uq_customers_tax_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customers
    ADD CONSTRAINT uq_customers_tax_id UNIQUE (tax_id);


--
-- Name: customer_accounts fk_customer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_accounts
    ADD CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES public.customers(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

