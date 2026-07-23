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
-- Name: securities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.securities (
    id integer NOT NULL,
    security_type integer NOT NULL,
    ticker character varying(20) NOT NULL,
    isin character varying(12) NOT NULL,
    company_name character varying(100) NOT NULL,
    stock_exchange integer NOT NULL,
    currency_id integer NOT NULL,
    last_price numeric(10,2) NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    CONSTRAINT securities_last_price_check CHECK ((last_price >= (0)::numeric))
);


ALTER TABLE public.securities OWNER TO postgres;

--
-- Name: security_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.security_types (
    id integer NOT NULL,
    security_type character varying(10) NOT NULL
);


ALTER TABLE public.security_types OWNER TO postgres;

--
-- Name: stock_exchanges; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.stock_exchanges (
    id integer NOT NULL,
    stock_exchange character varying(50) NOT NULL
);


ALTER TABLE public.stock_exchanges OWNER TO postgres;

--
-- Name: stocks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.stocks (
    id integer NOT NULL,
    dividend_declaration_date date,
    ex_dividend_date date,
    divident_payment_date date,
    divident_amount numeric(10,2),
    dividend_currency_id integer
);


ALTER TABLE public.stocks OWNER TO postgres;

--
-- Data for Name: securities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.securities (id, security_type, ticker, isin, company_name, stock_exchange, currency_id, last_price, updated_at) FROM stdin;
1	1	AAPL	US0378331005	Apple Inc.	2	1	271.08	2026-04-26 19:50:48.563743
5	1	GOOGL	US02079K3059	Alphabet Inc. Class A	2	1	344.42	2026-04-26 19:50:48.563743
9	1	LLY	US5324571083	Eli Lilly and Company	1	1	883.98	2026-04-26 19:50:48.56474
15	1	HD	US4370761029	The Home Depot, Inc.	1	1	335.91	2026-04-26 19:50:48.565741
27	1	TMUS	US8725901040	T-Mobile US, Inc.	2	1	189.82	2026-04-26 19:50:48.565741
10	1	JPM	US46625H1005	JPMorgan Chase & Co.	1	1	308.30	2026-04-26 19:50:48.566741
16	1	PG	US7427181091	The Procter & Gamble Company	1	1	148.20	2026-04-26 19:50:48.566741
22	1	INTC	US4581401001	Intel Corporation	2	1	82.56	2026-04-26 19:50:48.567742
3	1	NVDA	US67066G1040	NVIDIA Corporation	2	1	208.29	2026-04-26 19:50:48.567742
4	1	AMZN	US0231351067	Amazon.com, Inc.	2	1	264.01	2026-04-26 19:50:48.568741
8	1	BRK.A	US0846701086	Berkshire Hathaway Inc.	1	1	704760.02	2026-04-26 19:50:48.568741
17	1	COST	US22160K1051	Costco Wholesale Corporation	2	1	1011.17	2026-04-26 19:50:48.569742
24	1	AMD	US0079031078	Advanced Micro Devices, Inc.	2	1	347.83	2026-04-26 19:50:48.569742
12	1	UNH	US91324P1021	UnitedHealth Group Incorporated	1	1	354.94	2026-04-26 19:50:48.570741
21	1	ADBE	US00724F1012	Adobe Inc.	2	1	245.46	2026-04-26 19:50:48.570741
25	1	QCOM	US7475251036	Qualcomm Incorporated	2	1	148.87	2026-04-26 19:50:48.571741
18	1	KO	US1912161007	The Coca-Cola Company	1	1	76.65	2026-04-26 19:50:48.572742
7	1	TSLA	US88160R1014	Tesla, Inc.	2	1	376.32	2026-04-26 19:50:48.541598
26	1	AVGO	US11135F1012	Broadcom Inc.	2	1	422.78	2026-04-26 19:50:48.55517
19	1	PEP	US7134481081	PepsiCo, Inc.	2	1	155.46	2026-04-26 19:50:48.55617
11	1	WMT	US9311421039	Walmart Inc.	1	1	129.94	2026-04-26 19:50:48.55617
20	1	CSCO	US17275R1023	Cisco Systems, Inc.	2	1	89.03	2026-04-26 19:50:48.557167
13	1	V	US92826C8394	Visa Inc.	1	1	309.44	2026-04-26 19:50:48.558746
28	1	BAC	US0605051046	Bank of America Corporation	1	1	52.07	2026-04-26 19:50:48.558746
29	1	GS	US38141G1040	The Goldman Sachs Group, Inc.	1	1	926.93	2026-04-26 19:50:48.559743
6	1	META	US30303M1027	Meta Platforms, Inc.	2	1	675.05	2026-04-26 19:50:48.559743
23	1	NFLX	US64110L1061	Netflix, Inc.	2	1	92.46	2026-04-26 19:50:48.56074
30	1	MRK	US58933Y1055	Merck & Co., Inc.	1	1	111.92	2026-04-26 19:50:48.561743
14	1	MA	US57636Q1040	Mastercard Incorporated	1	1	504.19	2026-04-26 19:50:48.561743
2	1	MSFT	US5949181045	Microsoft Corporation	2	1	424.64	2026-04-26 19:50:48.562742
\.


--
-- Data for Name: security_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.security_types (id, security_type) FROM stdin;
1	STOCK
\.


--
-- Data for Name: stock_exchanges; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.stock_exchanges (id, stock_exchange) FROM stdin;
1	NYSE
2	NASDAQ
3	LSE
4	EN
5	MOEX
\.


--
-- Data for Name: stocks; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.stocks (id, dividend_declaration_date, ex_dividend_date, divident_payment_date, divident_amount, dividend_currency_id) FROM stdin;
\.


--
-- Name: securities securities_isin_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.securities
    ADD CONSTRAINT securities_isin_key UNIQUE (isin);


--
-- Name: securities securities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.securities
    ADD CONSTRAINT securities_pkey PRIMARY KEY (id);


--
-- Name: securities securities_ticker_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.securities
    ADD CONSTRAINT securities_ticker_key UNIQUE (ticker);


--
-- Name: security_types security_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.security_types
    ADD CONSTRAINT security_types_pkey PRIMARY KEY (id);


--
-- Name: security_types security_types_security_type_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.security_types
    ADD CONSTRAINT security_types_security_type_key UNIQUE (security_type);


--
-- Name: stock_exchanges stock_exchanges_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_exchanges
    ADD CONSTRAINT stock_exchanges_pkey PRIMARY KEY (id);


--
-- Name: stock_exchanges stock_exchanges_stock_exchange_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_exchanges
    ADD CONSTRAINT stock_exchanges_stock_exchange_key UNIQUE (stock_exchange);


--
-- Name: stocks stocks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stocks
    ADD CONSTRAINT stocks_pkey PRIMARY KEY (id);


--
-- Name: securities securities_security_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.securities
    ADD CONSTRAINT securities_security_type_fkey FOREIGN KEY (security_type) REFERENCES public.security_types(id);


--
-- Name: securities securities_stock_exchange_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.securities
    ADD CONSTRAINT securities_stock_exchange_fkey FOREIGN KEY (stock_exchange) REFERENCES public.stock_exchanges(id);


--
-- Name: stocks stocks_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stocks
    ADD CONSTRAINT stocks_id_fkey FOREIGN KEY (id) REFERENCES public.securities(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

