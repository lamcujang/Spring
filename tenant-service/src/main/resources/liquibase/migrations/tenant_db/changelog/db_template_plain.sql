

--liquibase formatted sql
--changeset dbiz:create-tables-data

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 8 (class 2615 OID 394270)
-- Name: multidb; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA multidb;


ALTER SCHEMA multidb OWNER TO postgres;

--
-- TOC entry 7 (class 2615 OID 385480)
-- Name: pos; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA pos;


ALTER SCHEMA pos OWNER TO postgres;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 2 (class 3079 OID 387588)
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA pos;


--
-- TOC entry 5537 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


--
-- TOC entry 420 (class 1255 OID 387601)
-- Name: test_function(); Type: FUNCTION; Schema: pos; Owner: postgres
--

CREATE FUNCTION pos.test_function() RETURNS integer
    LANGUAGE plpgsql
    AS $$
    DECLARE
      some_var INT;
    BEGIN
      	insert into "D_Expense_Category"
			(
			d_tenant_id,
			created_by,
			updated_by,
			name
			)
			values 
			(
			0,
			0,
			0,
			'MUAXANG'
			);
        
        RETURN some_var;
        
         EXCEPTION WHEN OTHERS THEN
         RETURN -1;
    END;
    $$;


ALTER FUNCTION pos.test_function() OWNER TO postgres;

--
-- TOC entry 401 (class 1259 OID 394872)
-- Name: d_api_trace_log_sq; Type: SEQUENCE; Schema: pos; Owner: adempiere
--

CREATE SEQUENCE pos.d_api_trace_log_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_api_trace_log_sq OWNER TO adempiere;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 402 (class 1259 OID 394886)
-- Name: d_api_trace_log; Type: TABLE; Schema: pos; Owner: adempiere
--

CREATE TABLE pos.d_api_trace_log (
    d_api_trace_log numeric(10,0) DEFAULT nextval('pos.d_api_trace_log_sq'::regclass) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    description character varying(255) DEFAULT NULL::character varying,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_api_trace_log_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    payload text,
    data_type character varying(32),
    in_out character varying(3),
    exception text
);


ALTER TABLE pos.d_api_trace_log OWNER TO adempiere;

--
-- TOC entry 330 (class 1259 OID 392795)
-- Name: d_assign_org_sq; Type: SEQUENCE; Schema: pos; Owner: adempiere
--

CREATE SEQUENCE pos.d_assign_org_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_assign_org_sq OWNER TO adempiere;

--
-- TOC entry 331 (class 1259 OID 392797)
-- Name: d_assign_org_product; Type: TABLE; Schema: pos; Owner: adempiere
--

CREATE TABLE pos.d_assign_org_product (
    d_assign_org_id numeric(10,0) DEFAULT nextval('pos.d_assign_org_sq'::regclass) NOT NULL,
    d_tenant_id numeric(10,0),
    d_product_id numeric(10,0),
    d_org_id numeric(10,0),
    d_assign_org_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    created date DEFAULT CURRENT_TIMESTAMP,
    created_by numeric(10,0),
    updated date DEFAULT CURRENT_TIMESTAMP,
    updated_by numeric(10,0),
    is_active character varying(1) DEFAULT 'Y'::character varying
);


ALTER TABLE pos.d_assign_org_product OWNER TO adempiere;

--
-- TOC entry 333 (class 1259 OID 392842)
-- Name: d_attribute; Type: TABLE; Schema: pos; Owner: adempiere
--

CREATE TABLE pos.d_attribute (
    d_attribute_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created date DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated date DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    code character varying(32),
    d_attribute_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    d_org_id numeric(10,0)
);


ALTER TABLE pos.d_attribute OWNER TO adempiere;

--
-- TOC entry 332 (class 1259 OID 392832)
-- Name: d_attribute_sq; Type: SEQUENCE; Schema: pos; Owner: adempiere
--

CREATE SEQUENCE pos.d_attribute_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_attribute_sq OWNER TO adempiere;

--
-- TOC entry 351 (class 1259 OID 393182)
-- Name: d_attribute_value; Type: TABLE; Schema: pos; Owner: adempiere
--

CREATE TABLE pos.d_attribute_value (
    d_attribute_value_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created date DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated date DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    value character varying(32),
    name character varying(32),
    d_attribute_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    d_org_id numeric(10,0),
    d_attribute_id numeric(10,0)
);


ALTER TABLE pos.d_attribute_value OWNER TO adempiere;

--
-- TOC entry 350 (class 1259 OID 393180)
-- Name: d_attribute_value_sq; Type: SEQUENCE; Schema: pos; Owner: adempiere
--

CREATE SEQUENCE pos.d_attribute_value_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_attribute_value_sq OWNER TO adempiere;

--
-- TOC entry 352 (class 1259 OID 393233)
-- Name: d_bank_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_bank_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_bank_sq OWNER TO postgres;

--
-- TOC entry 353 (class 1259 OID 393235)
-- Name: d_bank; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_bank (
    d_bank_id numeric(10,0) DEFAULT nextval('pos.d_bank_sq'::regclass) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(255),
    bin_code character varying(15),
    swift_code character varying(15),
    d_image_id numeric(10,0),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_bank_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL
);


ALTER TABLE pos.d_bank OWNER TO postgres;

--
-- TOC entry 355 (class 1259 OID 393261)
-- Name: d_bankaccount; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_bankaccount (
    d_bankaccount_id numeric(10,0) NOT NULL,
    d_bank_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    account_no character varying(32) NOT NULL,
    description character varying(255),
    name character varying(255) NOT NULL,
    is_default character varying(1) DEFAULT 'N'::character varying NOT NULL,
    bankaccount_type character varying(3) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_bankaccount_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL
);


ALTER TABLE pos.d_bankaccount OWNER TO postgres;

--
-- TOC entry 354 (class 1259 OID 393259)
-- Name: d_bankaccount_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_bankaccount_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_bankaccount_sq OWNER TO postgres;

--
-- TOC entry 327 (class 1259 OID 390960)
-- Name: d_cancel_reason; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_cancel_reason (
    d_cancel_reason_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    name character varying(64) NOT NULL,
    description character varying(255),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_cancel_reason_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_cancel_reason OWNER TO postgres;

--
-- TOC entry 326 (class 1259 OID 390958)
-- Name: d_cancel_reason_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_cancel_reason_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_cancel_reason_sq OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 385481)
-- Name: d_changelog; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_changelog (
    d_changelog_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    table_name character varying NOT NULL,
    old_value character varying(512) NOT NULL,
    new_value character varying(512) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_changelog_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    entity_id integer NOT NULL
);


ALTER TABLE pos.d_changelog OWNER TO postgres;

--
-- TOC entry 265 (class 1259 OID 386825)
-- Name: d_changelog_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_changelog_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_changelog_sq OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 385491)
-- Name: d_config; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_config (
    d_config_id numeric(10,0) DEFAULT nextval('pos.d_config'::regclass) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    name character varying(32) NOT NULL,
    value character varying(512) NOT NULL,
    description character varying(255),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    d_config_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying
);


ALTER TABLE pos.d_config OWNER TO postgres;

--
-- TOC entry 266 (class 1259 OID 386827)
-- Name: d_config_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_config_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_config_sq OWNER TO postgres;

--
-- TOC entry 337 (class 1259 OID 392910)
-- Name: d_coupon; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_coupon (
    d_coupon_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    code character varying(32) NOT NULL,
    balance_amount numeric NOT NULL,
    description character varying(255),
    d_pos_terminal_id numeric(10,0),
    is_available character varying(1) DEFAULT 'N'::character varying,
    d_vendor_id numeric(10,0),
    d_customer_id numeric(10,0),
    erp_coupon_id numeric(10,0),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_coupon_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL
);


ALTER TABLE pos.d_coupon OWNER TO postgres;

--
-- TOC entry 336 (class 1259 OID 392908)
-- Name: d_coupon_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_coupon_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_coupon_sq OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 385501)
-- Name: d_currency; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_currency (
    d_currency_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    currency_code character varying(3) NOT NULL,
    description character varying(255),
    standard_precision numeric(2,0) DEFAULT 0 NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_currency_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_currency OWNER TO postgres;

--
-- TOC entry 267 (class 1259 OID 386829)
-- Name: d_currency_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_currency_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_currency_sq OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 385512)
-- Name: d_customer; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_customer (
    d_customer_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0),
    code character varying(32),
    name character varying(255),
    phone1 character varying(15),
    phone2 character varying(15),
    address1 character varying(255),
    address2 character varying(255),
    customer_point bigint,
    tax_code character varying(15),
    email character varying(64),
    debit_amount numeric,
    company character varying(255),
    birthday date,
    d_image_id numeric(10,0),
    created date DEFAULT CURRENT_TIMESTAMP,
    created_by numeric(10,0),
    updated date DEFAULT CURRENT_TIMESTAMP,
    updated_by numeric(10,0),
    is_active character varying(1) DEFAULT 'Y'::character varying,
    d_customer_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_customer_type character varying(1) DEFAULT 'P'::character varying,
    area character varying(100),
    wards character varying(100),
    d_partner_group_id numeric(10,0),
    gender character varying(1),
    description character varying(255),
    discount numeric,
    erp_customer_id numeric(10,0)
);


ALTER TABLE pos.d_customer OWNER TO postgres;

--
-- TOC entry 268 (class 1259 OID 386831)
-- Name: d_customer_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_customer_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_customer_sq OWNER TO postgres;

--
-- TOC entry 269 (class 1259 OID 386833)
-- Name: d_doctype_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_doctype_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_doctype_sq OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 385528)
-- Name: d_doctype; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_doctype (
    d_doctype_id numeric(10,0) DEFAULT nextval('pos.d_doctype_sq'::regclass) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    code character varying(32) NOT NULL,
    name character varying(255) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_doctype_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_doctype OWNER TO postgres;

--
-- TOC entry 357 (class 1259 OID 393356)
-- Name: d_erp_integration; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_erp_integration (
    d_erp_integration_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    erp_platform character varying(32),
    erp_url character varying(128) NOT NULL,
    ad_client_id numeric(10,0),
    ad_org_id numeric(10,0),
    ad_role_id numeric(10,0),
    m_warehouse_id numeric(10,0),
    username character varying(32),
    password character varying(32),
    description character varying(255),
    is_default character varying(1) DEFAULT 'N'::character varying NOT NULL,
    bankaccount_type character varying(3) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_erp_integration_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL
);


ALTER TABLE pos.d_erp_integration OWNER TO postgres;

--
-- TOC entry 356 (class 1259 OID 393354)
-- Name: d_erp_integration_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_erp_integration_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_erp_integration_sq OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 385535)
-- Name: d_expense; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_expense (
    d_expense_id numeric(10,0) NOT NULL,
    d_expense_category_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    expense_date date NOT NULL,
    name character varying(255) NOT NULL,
    payment_method character varying(5),
    amount numeric,
    document_no character varying(32),
    description character varying(255),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_expense_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_expense OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 385546)
-- Name: d_expense_category; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_expense_category (
    d_expense_category_id numeric(10,0) NOT NULL,
    name character varying(32) NOT NULL,
    description character varying(255),
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_expense_category_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_expense_category OWNER TO postgres;

--
-- TOC entry 271 (class 1259 OID 386837)
-- Name: d_expense_category_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_expense_category_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_expense_category_sq OWNER TO postgres;

--
-- TOC entry 270 (class 1259 OID 386835)
-- Name: d_expense_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_expense_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_expense_sq OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 385554)
-- Name: d_floor; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_floor (
    d_floor_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    floor_no character varying(5) NOT NULL,
    name character varying(255),
    description character varying(255),
    d_floor_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    display_index numeric,
    d_pos_terminal_id numeric(10,0)
);


ALTER TABLE pos.d_floor OWNER TO postgres;

--
-- TOC entry 272 (class 1259 OID 386839)
-- Name: d_floor_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_floor_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_floor_sq OWNER TO postgres;

--
-- TOC entry 282 (class 1259 OID 386859)
-- Name: d_org_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_org_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_org_sq OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 385716)
-- Name: d_org; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_org (
    d_org_id numeric(10,0) DEFAULT nextval('pos.d_org_sq'::regclass) NOT NULL,
    code character varying(32) NOT NULL,
    name character varying(255) NOT NULL,
    d_tenant_id numeric(10,0),
    address character varying(512),
    tax_code character varying(15),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    d_org_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    updated_by numeric NOT NULL,
    email character varying(64),
    phone character varying(15),
    area character varying(100),
    wards character varying(100),
    erp_org_id numeric(10,0)
);


ALTER TABLE pos.d_org OWNER TO postgres;

--
-- TOC entry 260 (class 1259 OID 385920)
-- Name: d_userorg_access; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_userorg_access (
    d_user_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    d_userorg_access_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_userorg_access OWNER TO postgres;

--
-- TOC entry 398 (class 1259 OID 394842)
-- Name: d_get_user_org_access_v; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.d_get_user_org_access_v AS
 SELECT duo.d_tenant_id,
    duo.d_user_id,
    duo.d_org_id,
    duo.created,
    duo.created_by,
    duo.updated,
    duo.updated_by,
    duo.is_active,
    dog.name
   FROM (pos.d_org dog
     LEFT JOIN pos.d_userorg_access duo ON ((dog.d_org_id = duo.d_org_id)));


ALTER VIEW pos.d_get_user_org_access_v OWNER TO adempiere;

--
-- TOC entry 293 (class 1259 OID 386881)
-- Name: d_role_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_role_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_role_sq OWNER TO postgres;

--
-- TOC entry 252 (class 1259 OID 385843)
-- Name: d_role; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_role (
    d_role_id numeric(10,0) DEFAULT nextval('pos.d_role_sq'::regclass) NOT NULL,
    d_tenant_id numeric(10,0),
    code character varying(32),
    name character varying(255),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    d_role_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_role OWNER TO postgres;

--
-- TOC entry 261 (class 1259 OID 385924)
-- Name: d_user_role_access; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_user_role_access (
    d_user_id numeric(10,0) NOT NULL,
    d_role_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    d_userrole_access_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_user_role_access OWNER TO postgres;

--
-- TOC entry 400 (class 1259 OID 394865)
-- Name: d_get_user_role_access_v; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.d_get_user_role_access_v AS
 SELECT dr.d_tenant_id,
    dur.d_user_id,
    dur.d_role_id,
    dr.created,
    dr.created_by,
    dr.updated,
    dr.updated_by,
    dr.is_active,
    dr.name
   FROM (pos.d_user_role_access dur
     LEFT JOIN pos.d_role dr ON ((dr.d_role_id = dur.d_role_id)));


ALTER VIEW pos.d_get_user_role_access_v OWNER TO adempiere;

--
-- TOC entry 232 (class 1259 OID 385568)
-- Name: d_image; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_image (
    d_image_id numeric(10,0) NOT NULL,
    image_url character varying(255),
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_image_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    image_code character varying(15)
);


ALTER TABLE pos.d_image OWNER TO postgres;

--
-- TOC entry 273 (class 1259 OID 386841)
-- Name: d_image_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_image_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_image_sq OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 385579)
-- Name: d_industry; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_industry (
    d_industry_id numeric(10,0) NOT NULL,
    code character varying(32) NOT NULL,
    name character varying(255),
    is_active character varying(1) DEFAULT 'Y'::character varying,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    created_by numeric(10,0),
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_by numeric(10,0),
    d_industry_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    d_tenant_id numeric(10,0)
);


ALTER TABLE pos.d_industry OWNER TO postgres;

--
-- TOC entry 274 (class 1259 OID 386843)
-- Name: d_industry_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_industry_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_industry_sq OWNER TO postgres;

--
-- TOC entry 373 (class 1259 OID 393795)
-- Name: d_integration_history; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_integration_history (
    d_integration_history_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_user_id numeric(10,0),
    int_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    int_type character varying(3) NOT NULL,
    int_flow character varying(3) NOT NULL,
    int_status character varying(3) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_integration_history_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL
);


ALTER TABLE pos.d_integration_history OWNER TO postgres;

--
-- TOC entry 289 (class 1259 OID 386873)
-- Name: d_reference_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_reference_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_reference_sq OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 385794)
-- Name: d_reference; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_reference (
    d_reference_id numeric(10,0) DEFAULT nextval('pos.d_reference_sq'::regclass) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    name character varying(32) NOT NULL,
    description character varying(255),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_reference_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying
);


ALTER TABLE pos.d_reference OWNER TO postgres;

--
-- TOC entry 290 (class 1259 OID 386875)
-- Name: d_reference_list_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_reference_list_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_reference_list_sq OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 385816)
-- Name: d_reference_list; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_reference_list (
    d_reference_list_id numeric(10,0) DEFAULT nextval('pos.d_reference_list_sq'::regclass) NOT NULL,
    d_reference_id numeric(10,0) NOT NULL,
    value character varying(15) NOT NULL,
    name character varying(64) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_reference_list_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL
);


ALTER TABLE pos.d_reference_list OWNER TO postgres;

--
-- TOC entry 300 (class 1259 OID 386895)
-- Name: d_user_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_user_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_user_sq OWNER TO postgres;

--
-- TOC entry 259 (class 1259 OID 385911)
-- Name: d_user; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_user (
    d_user_id numeric(19,0) DEFAULT nextval('pos.d_user_sq'::regclass) NOT NULL,
    user_name character varying(64) NOT NULL,
    full_name character varying(128) DEFAULT 'Thanh'::character varying,
    phone character varying(15),
    password character varying(100),
    d_image_id numeric(10,0),
    email character varying(64),
    birth_day date,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    d_tenant_id numeric(10,0),
    user_pin character varying(15),
    is_locked character varying(10) DEFAULT 'Y'::character varying,
    date_locked date,
    date_last_login date,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    created_by numeric(10,0),
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_by numeric(10,0),
    d_user_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_user OWNER TO postgres;

--
-- TOC entry 393 (class 1259 OID 394705)
-- Name: d_integration_history_get_v; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.d_integration_history_get_v AS
 SELECT dih.d_integration_history_id,
    dih.is_active,
    dih.d_tenant_id,
    dih.d_org_id,
    dih.created,
    dih.created_by,
    dih.updated,
    dih.updated_by,
    dih.int_date,
    du.full_name,
    du.d_user_id,
    drl2.value AS type_value,
    drl2.name AS int_type,
    drl.value AS flow_value,
    drl.name AS int_flow,
    drl1.value AS status_value,
    drl1.name AS int_status
   FROM (((((((pos.d_integration_history dih
     LEFT JOIN pos.d_user du ON ((dih.d_user_id = du.d_user_id)))
     LEFT JOIN pos.d_reference_list drl ON (((dih.int_flow)::text = (drl.value)::text)))
     LEFT JOIN pos.d_reference dr ON (((drl.d_reference_id = dr.d_reference_id) AND ((dr.name)::text = 'Flow Integration'::text))))
     LEFT JOIN pos.d_reference_list drl1 ON (((dih.int_status)::text = (drl1.value)::text)))
     LEFT JOIN pos.d_reference dr1 ON (((drl1.d_reference_id = dr1.d_reference_id) AND ((dr1.name)::text = 'Status Integration'::text))))
     LEFT JOIN pos.d_reference_list drl2 ON (((dih.int_type)::text = (drl2.value)::text)))
     LEFT JOIN pos.d_reference dr2 ON (((drl2.d_reference_id = dr2.d_reference_id) AND ((dr2.name)::text = 'Data Type Integration'::text))))
  WHERE (((dr.name)::text = 'Flow Integration'::text) AND ((dr1.name)::text = 'Status Integration'::text) AND ((dr2.name)::text = 'Data Type Integration'::text));


ALTER VIEW pos.d_integration_history_get_v OWNER TO adempiere;

--
-- TOC entry 372 (class 1259 OID 393793)
-- Name: d_integration_history_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_integration_history_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_integration_history_sq OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 385587)
-- Name: d_invoice; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_invoice (
    d_invoice_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_customer_id numeric(10,0),
    d_vendor_id numeric(10,0),
    d_order_id numeric(10,0),
    document_no character varying(32) NOT NULL,
    date_invoiced date NOT NULL,
    d_doctype_id numeric(10,0) NOT NULL,
    d_currency_id numeric(10,0) NOT NULL,
    accounting_date date,
    buyer_name character varying(255),
    buyer_tax_code character varying(15),
    buyer_email character varying(32),
    buyer_address character varying(255),
    buyer_phone character varying(15),
    total_amount numeric,
    invoice_status character varying(5) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_invoice_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    d_pricelist_id numeric(10,0),
    d_user_id numeric(10,0),
    reference_invoice_id numeric(10,0),
    invoice_form character varying(32),
    invoice_sign character varying(32),
    invoice_no character varying(15),
    search_code character varying(32),
    search_link character varying(255),
    invoice_error character varying(255),
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    d_pos_order_id numeric(10,0)
);


ALTER TABLE pos.d_invoice OWNER TO postgres;

--
-- TOC entry 275 (class 1259 OID 386845)
-- Name: d_invoice_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_invoice_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_invoice_sq OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 385728)
-- Name: d_payment; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_payment (
    d_payment_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_doctype_id numeric(10,0) NOT NULL,
    d_customer_id numeric(10,0),
    d_vendor_id numeric(10,0),
    d_invoice_id numeric(10,0),
    d_bankaccount_id numeric(10,0),
    payment_date date NOT NULL,
    d_currency_id numeric(10,0) NOT NULL,
    payment_status character varying(5) NOT NULL,
    payment_amount numeric NOT NULL,
    d_order_id numeric(10,0),
    document_no character varying(32) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_payment_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    d_pos_order_id numeric(10,0)
);


ALTER TABLE pos.d_payment OWNER TO postgres;

--
-- TOC entry 284 (class 1259 OID 386863)
-- Name: d_pos_order_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_pos_order_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_pos_order_sq OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 385742)
-- Name: d_pos_order; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_pos_order (
    d_pos_order_id numeric(10,0) DEFAULT nextval('pos.d_pos_order_sq'::regclass) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_customer_id numeric(10,0),
    phone character varying(15),
    order_status character varying(5) NOT NULL,
    source character varying(10) NOT NULL,
    is_locked character varying(1) DEFAULT 'Y'::character varying,
    d_table_id numeric(10,0) NOT NULL,
    d_floor_id numeric(10,0) NOT NULL,
    d_user_id numeric(10,0) NOT NULL,
    order_guests smallint,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    order_date date NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric NOT NULL,
    customer_name character varying(255),
    document_no character varying(32) NOT NULL,
    d_currency_id numeric(10,0) NOT NULL,
    d_pricelist_id numeric(10,0),
    d_pos_id numeric(10,0),
    d_pos_order_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    total_amount numeric,
    erp_pos_order_id numeric(10,0),
    is_applied_sercharge character varying(1) DEFAULT 'N'::character varying,
    flat_discount numeric,
    d_pos_terminal_id numeric(10,0),
    erp_pos_order_no character varying(32),
    bill_no character varying(64),
    d_shift_control_id numeric(10,0),
    is_processed character varying(1) DEFAULT 'N'::character varying,
    qrcode_payment character varying(255),
    ftcode character varying(255),
    d_reconciledetail_id numeric(10,0),
    is_sync_erp character varying(1) DEFAULT 'N'::character varying,
    d_bankaccount_id numeric(10,0),
    d_bank_id numeric(10,0),
    total_line numeric,
    d_doctype_id numeric(10,0)
);


ALTER TABLE pos.d_pos_order OWNER TO postgres;

--
-- TOC entry 308 (class 1259 OID 388750)
-- Name: d_pricelist_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_pricelist_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_pricelist_sq OWNER TO postgres;

--
-- TOC entry 309 (class 1259 OID 388752)
-- Name: d_pricelist; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_pricelist (
    d_pricelist_id numeric(10,0) DEFAULT nextval('pos.d_pricelist_sq'::regclass) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    name character varying(64) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    from_date timestamp without time zone NOT NULL,
    to_date timestamp without time zone,
    is_saleprice character varying(1) DEFAULT 'Y'::character varying,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_pricelist_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    general_pricelist character varying(1) DEFAULT 'N'::character varying
);


ALTER TABLE pos.d_pricelist OWNER TO postgres;

--
-- TOC entry 262 (class 1259 OID 385928)
-- Name: d_vendor; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_vendor (
    d_vendor_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0),
    code character varying(32),
    name character varying(255),
    phone1 character varying(15),
    phone2 character varying(15),
    address1 character varying(255),
    address2 character varying(255),
    tax_code character varying(15),
    email character varying(64),
    birthday date,
    d_image_id numeric(10,0),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0),
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0),
    is_active character varying(1) DEFAULT 'Y'::character varying,
    d_vendor_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    debit_amount numeric,
    d_partner_group_id numeric(10,0),
    description character varying(255),
    area character varying(100),
    wards character varying(100)
);


ALTER TABLE pos.d_vendor OWNER TO postgres;

--
-- TOC entry 408 (class 1259 OID 394991)
-- Name: d_invoice_v; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.d_invoice_v AS
 SELECT do2.document_no AS pos_order_no,
    do2.d_pos_order_id,
    di.d_tenant_id,
    di.d_org_id,
    di.created_by,
    di.updated_by,
    di.created,
    di.updated,
    di.is_active,
    di.d_invoice_id,
    di.accounting_date,
    di.date_invoiced,
    di.document_no,
    di.d_doctype_id,
    di.total_amount,
    di.invoice_status,
    di.d_customer_id,
    dc.name AS customer_name,
    di.d_vendor_id,
    dv.name AS vendor_name,
    di.buyer_name,
    di.buyer_tax_code,
    di.buyer_email,
    di.buyer_address,
    di.buyer_phone,
    di.d_pricelist_id,
    dpl.name AS pricelist_name,
    di.d_user_id,
    du.user_name,
    di.reference_invoice_id,
    di.invoice_form,
    di.invoice_sign,
    di.invoice_no,
    di.search_code,
    di.search_link,
    di.invoice_error,
    drl.value AS value_status,
    drl.name AS order_status,
    sum(dp.payment_amount) AS paid
   FROM ((((((((pos.d_invoice di
     LEFT JOIN pos.d_customer dc ON ((dc.d_customer_id = di.d_customer_id)))
     LEFT JOIN pos.d_vendor dv ON ((dv.d_vendor_id = di.d_vendor_id)))
     LEFT JOIN pos.d_pos_order do2 ON ((di.d_pos_order_id = do2.d_pos_order_id)))
     LEFT JOIN pos.d_payment dp ON ((dp.d_invoice_id = di.d_invoice_id)))
     LEFT JOIN pos.d_pricelist dpl ON ((di.d_pricelist_id = dpl.d_pricelist_id)))
     LEFT JOIN pos.d_user du ON ((du.d_user_id = di.d_user_id)))
     LEFT JOIN pos.d_reference_list drl ON (((do2.order_status)::text = (drl.value)::text)))
     LEFT JOIN pos.d_reference dr ON (((drl.d_reference_id = dr.d_reference_id) AND ((dr.name)::text = 'Document Status'::text))))
  GROUP BY di.d_tenant_id, di.d_org_id, di.created_by, di.updated_by, di.created, di.updated, di.is_active, di.d_invoice_id, di.accounting_date, di.date_invoiced, di.total_amount, di.invoice_status, di.document_no, di.d_doctype_id, di.d_customer_id, dc.name, di.d_vendor_id, dv.name, di.buyer_name, di.buyer_tax_code, di.buyer_email, di.buyer_address, di.buyer_phone, di.d_pricelist_id, dpl.name, di.d_user_id, du.user_name, di.reference_invoice_id, di.invoice_form, di.invoice_sign, di.invoice_no, di.search_code, di.search_link, di.invoice_error, do2.document_no, do2.d_pos_order_id, drl.name, drl.value;


ALTER VIEW pos.d_invoice_v OWNER TO adempiere;

--
-- TOC entry 235 (class 1259 OID 385611)
-- Name: d_invoiceline; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_invoiceline (
    d_invoiceline_id numeric(10,0) NOT NULL,
    d_invoice_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_product_id numeric(10,0),
    d_tax_id numeric(10,0),
    lineno numeric(2,0),
    qty numeric(5,0) DEFAULT 0 NOT NULL,
    price_entered numeric DEFAULT 0 NOT NULL,
    linenet_amt numeric DEFAULT 0 NOT NULL,
    grand_total numeric DEFAULT 0 NOT NULL,
    d_invoiceline_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    d_orderline_id numeric(10,0),
    d_pos_orderline_id numeric(10,0)
);


ALTER TABLE pos.d_invoiceline OWNER TO postgres;

--
-- TOC entry 276 (class 1259 OID 386847)
-- Name: d_invoiceline_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_invoiceline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_invoiceline_sq OWNER TO postgres;

--
-- TOC entry 319 (class 1259 OID 390871)
-- Name: d_kitchen_order; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_kitchen_order (
    d_kitchen_order_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    documentno character varying(32) NOT NULL,
    d_pos_order_id numeric(10,0) NOT NULL,
    d_doctype_id numeric(10,0),
    d_warehouse_id numeric(10,0),
    dateordered timestamp without time zone NOT NULL,
    d_user_id numeric(10,0),
    d_floor_id numeric(10,0),
    d_table_id numeric(10,0),
    order_status character varying(5),
    description character varying(255),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_kitchen_order_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    d_pos_terminal_id numeric(10,0),
    erp_kitchen_order_id numeric(10,0),
    is_sync_erp character varying(1) DEFAULT 'N'::character varying NOT NULL
);


ALTER TABLE pos.d_kitchen_order OWNER TO postgres;

--
-- TOC entry 318 (class 1259 OID 390869)
-- Name: d_kitchen_order_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_kitchen_order_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_kitchen_order_sq OWNER TO postgres;

--
-- TOC entry 321 (class 1259 OID 390892)
-- Name: d_kitchen_orderline; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_kitchen_orderline (
    d_kitchen_orderline_id numeric(10,0) NOT NULL,
    d_kitchen_order_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    orderline_status character varying(5),
    d_product_id numeric(10,0) NOT NULL,
    note character varying(255),
    qty numeric DEFAULT 0 NOT NULL,
    transfer_qty numeric,
    cancel_qty numeric,
    priority character varying(5),
    is_active character varying(1) DEFAULT 'Y'::character varying,
    description character varying(255),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_kitchen_orderline_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    d_cancel_reason_id numeric(10,0),
    d_pos_orderline_id numeric(10,0),
    d_production_id numeric(10,0)
);


ALTER TABLE pos.d_kitchen_orderline OWNER TO postgres;

--
-- TOC entry 320 (class 1259 OID 390890)
-- Name: d_kitchen_orderline_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_kitchen_orderline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_kitchen_orderline_sq OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 385625)
-- Name: d_language; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_language (
    d_language_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    language_code character varying(3) NOT NULL,
    name character varying(32) NOT NULL,
    country_code character varying(3),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_language_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_language OWNER TO postgres;

--
-- TOC entry 277 (class 1259 OID 386849)
-- Name: d_language_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_language_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_language_sq OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 385635)
-- Name: d_locator; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_locator (
    d_locator_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0),
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    code character varying(32),
    description character varying(255),
    x character varying(32),
    y character varying(32),
    z character varying(32),
    d_warehouse_id numeric(10,0) NOT NULL,
    d_locator_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    name character varying(100)
);


ALTER TABLE pos.d_locator OWNER TO postgres;

--
-- TOC entry 278 (class 1259 OID 386851)
-- Name: d_locator_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_locator_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_locator_sq OWNER TO postgres;

--
-- TOC entry 325 (class 1259 OID 390937)
-- Name: d_note; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_note (
    d_note_id numeric(10,0) NOT NULL,
    d_note_group_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    name character varying(32) NOT NULL,
    description character varying(255),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_note_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    product_category_ids character varying(255)
);


ALTER TABLE pos.d_note OWNER TO postgres;

--
-- TOC entry 323 (class 1259 OID 390919)
-- Name: d_note_group; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_note_group (
    d_note_group_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    group_name character varying(32) NOT NULL,
    description character varying(255),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_note_group_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_note_group OWNER TO postgres;

--
-- TOC entry 322 (class 1259 OID 390917)
-- Name: d_note_group_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_note_group_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_note_group_sq OWNER TO postgres;

--
-- TOC entry 324 (class 1259 OID 390935)
-- Name: d_note_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_note_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_note_sq OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 385645)
-- Name: d_notification; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_notification (
    d_notification_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    title character varying(64) NOT NULL,
    content character varying(255) NOT NULL,
    notification_type character varying(5) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    status character varying(5) NOT NULL,
    d_notification_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_notification OWNER TO postgres;

--
-- TOC entry 279 (class 1259 OID 386853)
-- Name: d_notification_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_notification_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_notification_sq OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 385682)
-- Name: d_order; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_order (
    d_order_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    document_no character varying(32),
    d_customer_id numeric(10,0) NOT NULL,
    phone character varying(15),
    order_status character varying(5) NOT NULL,
    source character varying(10) NOT NULL,
    is_locked character varying(1) DEFAULT 'N'::character varying NOT NULL,
    d_table_id numeric(10,0),
    d_floor_id numeric(10,0),
    d_user_id numeric(10,0) NOT NULL,
    order_guests smallint,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    order_date date NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric NOT NULL,
    customer_name character varying(255),
    d_currency_id numeric(10,0) NOT NULL,
    d_pricelist_id numeric(10,0),
    payment_method character varying(5),
    d_doctype_id numeric(10,0) NOT NULL,
    d_order_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    total_amount numeric,
    d_pos_terminal_id numeric(10,0)
);


ALTER TABLE pos.d_order OWNER TO postgres;

--
-- TOC entry 280 (class 1259 OID 386855)
-- Name: d_order_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_order_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_order_sq OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 385703)
-- Name: d_orderline; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_orderline (
    d_orderline_id numeric(10,0) NOT NULL,
    d_order_id numeric(10,0),
    qty numeric NOT NULL,
    price_entered numeric NOT NULL,
    total_amount numeric NOT NULL,
    d_tax_id numeric(10,0),
    tax_amount numeric,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    discount_percent numeric(10,0),
    discount_amount numeric,
    d_orderline_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric NOT NULL,
    d_product_id numeric(10,0),
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL
);


ALTER TABLE pos.d_orderline OWNER TO postgres;

--
-- TOC entry 281 (class 1259 OID 386857)
-- Name: d_orderline_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_orderline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_orderline_sq OWNER TO postgres;

--
-- TOC entry 315 (class 1259 OID 389549)
-- Name: d_partner_group; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_partner_group (
    d_partner_group_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    is_customer character varying(1) DEFAULT 'N'::character varying,
    is_default character varying(1) DEFAULT 'N'::character varying,
    group_code character varying(32),
    group_name character varying(128),
    description character varying(255),
    is_active character varying(1) DEFAULT 'Y'::character varying,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_partner_group_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    discount numeric(10,2),
    is_summary character varying(1) DEFAULT 'N'::character varying,
    d_partner_group_parent_id numeric(10,0),
    erp_bp_group_id numeric(10,0)
);


ALTER TABLE pos.d_partner_group OWNER TO postgres;

--
-- TOC entry 314 (class 1259 OID 389417)
-- Name: d_partner_group_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_partner_group_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_partner_group_sq OWNER TO postgres;

--
-- TOC entry 369 (class 1259 OID 393709)
-- Name: d_pay_method; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_pay_method (
    d_pay_method_id numeric(10,0) NOT NULL,
    d_bank_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    description character varying(255),
    name character varying(255) NOT NULL,
    d_image_id numeric(10,0),
    is_default character varying(1) DEFAULT 'N'::character varying NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_pay_method_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL
);


ALTER TABLE pos.d_pay_method OWNER TO postgres;

--
-- TOC entry 368 (class 1259 OID 393707)
-- Name: d_pay_method_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_pay_method_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_pay_method_sq OWNER TO postgres;

--
-- TOC entry 283 (class 1259 OID 386861)
-- Name: d_payment_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_payment_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_payment_sq OWNER TO postgres;

--
-- TOC entry 371 (class 1259 OID 393764)
-- Name: d_paymethod_org; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_paymethod_org (
    d_paymethod_org_id numeric(10,0) NOT NULL,
    d_pay_method_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    access_code character varying(32),
    terminal_id character varying(32),
    hash_key character varying(512),
    description character varying(255),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_paymethod_org_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    merchant_code character varying(32)
);


ALTER TABLE pos.d_paymethod_org OWNER TO postgres;

--
-- TOC entry 370 (class 1259 OID 393739)
-- Name: d_paymethod_org_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_paymethod_org_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_paymethod_org_sq OWNER TO postgres;

--
-- TOC entry 349 (class 1259 OID 393129)
-- Name: d_pc_terminalaccess; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_pc_terminalaccess (
    d_pc_terminalaccess_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_pos_terminal_id numeric(10,0),
    erp_pc_terminalaccess_id numeric(10,0),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_pc_terminalaccess_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL
);


ALTER TABLE pos.d_pc_terminalaccess OWNER TO postgres;

--
-- TOC entry 348 (class 1259 OID 393127)
-- Name: d_pc_terminalaccess_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_pc_terminalaccess_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_pc_terminalaccess_sq OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 385767)
-- Name: d_product; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_product (
    d_product_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    d_product_category_id numeric(10,0) NOT NULL,
    code character varying(32),
    name character varying(255),
    qrcode character varying(36),
    saleprice numeric(10,2),
    costprice numeric(10,2),
    d_uom_id numeric(10,0),
    on_hand numeric(10,2),
    is_purchased character varying(1) DEFAULT 'Y'::character varying,
    d_image_id numeric(10,0),
    product_type character varying(32),
    attribute1 character varying(255),
    attribute2 character varying(255),
    attribute3 character varying(255),
    attribute4 character varying(255),
    attribute5 character varying(255),
    attribute6 character varying(255),
    attribute7 character varying(255),
    attribute8 character varying(255),
    attribute9 character varying(255),
    attribute10 character varying(255),
    description character varying(255),
    d_product_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_topping character varying(1) DEFAULT 'N'::character varying,
    min_on_hand numeric,
    max_on_hand numeric,
    d_tax_id numeric(10,0),
    group_type character varying(45),
    d_product_parent_id numeric(10,0),
    d_locator_id character varying,
    qty_conversion numeric,
    weight numeric,
    erp_product_id numeric(10,0),
    brand character varying(200),
    qty numeric DEFAULT 0
);


ALTER TABLE pos.d_product OWNER TO postgres;

--
-- TOC entry 382 (class 1259 OID 394310)
-- Name: d_purchase_orderline_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_purchase_orderline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_purchase_orderline_sq OWNER TO postgres;

--
-- TOC entry 383 (class 1259 OID 394312)
-- Name: d_purchase_orderline; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_purchase_orderline (
    d_purchase_orderline_id numeric(10,0) DEFAULT nextval('pos.d_purchase_orderline_sq'::regclass) NOT NULL,
    d_purchase_order_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_product_id numeric(10,0),
    d_uom_id numeric(10,0),
    qty numeric,
    priceentered numeric,
    d_tax_id numeric(10,0),
    tax_amount numeric,
    net_amount numeric,
    total_amount numeric,
    description character varying(255),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_purchase_orderline_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL
);


ALTER TABLE pos.d_purchase_orderline OWNER TO postgres;

--
-- TOC entry 384 (class 1259 OID 394365)
-- Name: d_po_detail_v; Type: VIEW; Schema: pos; Owner: postgres
--

CREATE VIEW pos.d_po_detail_v AS
 SELECT dpo.d_tenant_id,
    dpo.d_org_id,
    dpo.d_purchase_orderline_id,
    dpo.created_by,
    dpo.updated_by,
    dpo.created,
    dpo.updated,
    dpo.is_active,
    dpo.d_purchase_order_id,
    dpo.qty,
    dpo.priceentered,
    dpo.net_amount,
    dpo.total_amount,
    dpo.description,
    dpo.d_product_id,
    dpt.name AS product_name
   FROM (pos.d_purchase_orderline dpo
     JOIN pos.d_product dpt ON ((dpo.d_product_id = dpt.d_product_id)));


ALTER VIEW pos.d_po_detail_v OWNER TO postgres;

--
-- TOC entry 380 (class 1259 OID 394284)
-- Name: d_purchase_order_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_purchase_order_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_purchase_order_sq OWNER TO postgres;

--
-- TOC entry 381 (class 1259 OID 394286)
-- Name: d_purchase_order; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_purchase_order (
    d_purchase_order_id numeric(10,0) DEFAULT nextval('pos.d_purchase_order_sq'::regclass) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_doctype_id numeric(10,0) NOT NULL,
    d_user_id numeric(10,0),
    d_vendor_id numeric(10,0),
    documentno character varying(32) NOT NULL,
    order_status character varying(3) NOT NULL,
    order_date timestamp without time zone NOT NULL,
    total_amount numeric,
    description character varying(255),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_purchase_order_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    d_warehouse_id numeric(10,0)
);


ALTER TABLE pos.d_purchase_order OWNER TO postgres;

--
-- TOC entry 367 (class 1259 OID 393667)
-- Name: d_reference_get_v; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.d_reference_get_v AS
 SELECT dr.d_reference_id,
    dr.name AS name_reference,
    drl.name,
    drl.value
   FROM (pos.d_reference dr
     JOIN pos.d_reference_list drl ON ((dr.d_reference_id = drl.d_reference_id)));


ALTER VIEW pos.d_reference_get_v OWNER TO adempiere;

--
-- TOC entry 264 (class 1259 OID 385963)
-- Name: d_warehouse; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_warehouse (
    d_warehouse_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    code character varying(32) NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(255),
    address character varying(255),
    is_negative character varying(1) DEFAULT 'N'::character varying,
    d_warehouse_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    printer_ip character varying(100)
);


ALTER TABLE pos.d_warehouse OWNER TO postgres;

--
-- TOC entry 386 (class 1259 OID 394445)
-- Name: d_po_header_v; Type: VIEW; Schema: pos; Owner: postgres
--

CREATE VIEW pos.d_po_header_v AS
 SELECT dpo.d_tenant_id,
    dpo.d_org_id,
    dpo.d_purchase_order_id,
    dpo.created_by,
    dpo.updated_by,
    dpo.created,
    dpo.updated,
    dpo.is_active,
    dpo.order_status,
    dpo.total_amount,
    dpo.order_date,
    drgv.name AS name_reference,
    dpo.d_user_id,
    dpo.d_warehouse_id,
    dwh.name AS warehouse_name,
    du.full_name,
    dpo.d_vendor_id,
    dvd.name AS vendorname,
    (0)::numeric AS vendordebt,
    (0)::numeric AS vendorpaid,
    ( SELECT count(d_purchase_orderline.d_product_id) AS count
           FROM pos.d_purchase_orderline
          WHERE (d_purchase_orderline.d_purchase_order_id = dpo.d_purchase_order_id)) AS totalproduct,
    ( SELECT sum(d_purchase_orderline.qty) AS sum
           FROM pos.d_purchase_orderline
          WHERE (d_purchase_orderline.d_purchase_order_id = dpo.d_purchase_order_id)) AS totalqty
   FROM ((((pos.d_purchase_order dpo
     LEFT JOIN pos.d_user du ON ((dpo.d_user_id = du.d_user_id)))
     LEFT JOIN pos.d_warehouse dwh ON ((dpo.d_warehouse_id = dwh.d_warehouse_id)))
     LEFT JOIN pos.d_vendor dvd ON ((dpo.d_vendor_id = dvd.d_vendor_id)))
     LEFT JOIN pos.d_reference_get_v drgv ON ((((dpo.order_status)::text = (drgv.value)::text) AND ((drgv.name_reference)::text = 'Document Status'::text))));


ALTER VIEW pos.d_po_header_v OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 385754)
-- Name: d_pos_orderline; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_pos_orderline (
    d_pos_orderline_id numeric(10,0) NOT NULL,
    qty numeric NOT NULL,
    d_product_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_pos_order_id numeric(10,0) NOT NULL,
    description character varying(255),
    d_pos_orderline_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    salesprice numeric,
    d_production_id numeric(10,0),
    d_tax_id numeric(10,0),
    tax_amount numeric,
    discount_percent numeric(10,0),
    discount_amount numeric,
    linenet_amt numeric,
    grand_total numeric
);


ALTER TABLE pos.d_pos_orderline OWNER TO postgres;

--
-- TOC entry 285 (class 1259 OID 386865)
-- Name: d_pos_orderline_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_pos_orderline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_pos_orderline_sq OWNER TO postgres;

--
-- TOC entry 394 (class 1259 OID 394728)
-- Name: d_pos_orderline_v_all; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.d_pos_orderline_v_all AS
 SELECT dpo.d_pos_orderline_id,
    dpo.d_tenant_id,
    dpo.d_org_id,
    dpo.created,
    dpo.created_by,
    dpo.updated,
    dpo.updated_by,
    dpo.is_active,
    dpo.d_pos_order_id,
    dpo.salesprice,
    dpo.qty,
    dpo.description,
    dpo.tax_amount,
    dpo.linenet_amt,
    dpo.grand_total,
    dpo.d_tax_id,
    drl.name AS status,
    drl.value AS value_status,
    dpo.d_product_id,
    dp.name AS name_product,
    dp.d_product_id AS product_id,
    dp.code AS code_product,
    dp.product_type
   FROM ((((pos.d_pos_orderline dpo
     LEFT JOIN pos.d_product dp ON ((dpo.d_product_id = dp.d_product_id)))
     LEFT JOIN pos.d_kitchen_orderline dko ON ((dko.d_pos_orderline_id = dpo.d_pos_orderline_id)))
     LEFT JOIN pos.d_reference_get_v drl ON (((dko.orderline_status)::text = (drl.value)::text)))
     LEFT JOIN pos.d_reference dr ON ((drl.d_reference_id = dr.d_reference_id)));


ALTER VIEW pos.d_pos_orderline_v_all OWNER TO adempiere;

--
-- TOC entry 345 (class 1259 OID 393071)
-- Name: d_pos_payment; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_pos_payment (
    d_pos_payment_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_pos_order_id numeric(10,0) NOT NULL,
    payment_method character varying(3) NOT NULL,
    voucher_code character varying(36),
    total_amount numeric,
    transaction_id character varying(36),
    tip_amount numeric,
    note character varying(255),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_pos_payment_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    is_processed character varying(1) DEFAULT 'N'::character varying NOT NULL
);


ALTER TABLE pos.d_pos_payment OWNER TO postgres;

--
-- TOC entry 344 (class 1259 OID 393069)
-- Name: d_pos_payment_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_pos_payment_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_pos_payment_sq OWNER TO postgres;

--
-- TOC entry 347 (class 1259 OID 393099)
-- Name: d_pos_taxline; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_pos_taxline (
    d_pos_taxline_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_pos_order_id numeric(10,0) NOT NULL,
    d_tax_id numeric(10,0) NOT NULL,
    tax_amount numeric,
    tax_base_amount numeric,
    is_price_intax character varying(1) DEFAULT 'N'::character varying,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_pos_taxline_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL
);


ALTER TABLE pos.d_pos_taxline OWNER TO postgres;

--
-- TOC entry 346 (class 1259 OID 393097)
-- Name: d_pos_taxline_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_pos_taxline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_pos_taxline_sq OWNER TO postgres;

--
-- TOC entry 335 (class 1259 OID 392879)
-- Name: d_pos_terminal; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_pos_terminal (
    d_pos_terminal_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    name character varying(128) NOT NULL,
    description character varying(255),
    d_user_id numeric(10,0),
    d_bank_account_cash_id numeric(10,0),
    is_restaurant character varying(1) DEFAULT 'N'::character varying,
    d_pricelist_id numeric(10,0),
    d_warehouse_id numeric(10,0),
    d_bank_account_id numeric(10,0),
    printer_ip character varying(64),
    printer_port numeric(10,0),
    is_bill_merge character varying(1) DEFAULT 'N'::character varying,
    is_notify_bill character varying(1) DEFAULT 'N'::character varying,
    d_bank_account_visa_id numeric(10,0),
    erp_pos_id numeric(10,0),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_pos_terminal_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    d_doctype_id numeric(10,0)
);


ALTER TABLE pos.d_pos_terminal OWNER TO postgres;

--
-- TOC entry 334 (class 1259 OID 392877)
-- Name: d_pos_terminal_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_pos_terminal_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_pos_terminal_sq OWNER TO postgres;

--
-- TOC entry 387 (class 1259 OID 394526)
-- Name: d_pos_terminal_v; Type: VIEW; Schema: pos; Owner: postgres
--

CREATE VIEW pos.d_pos_terminal_v AS
 SELECT dpt.d_pos_terminal_id,
    dpt.d_tenant_id,
    dpt.d_org_id,
    dpt.name AS terminal_name,
    dpt.description,
    dpt.d_user_id,
    du.full_name,
    du.user_name,
    dpt.d_bank_account_cash_id,
    dbcash.name AS bank_cash_name,
    dpt.is_restaurant,
    dpt.d_pricelist_id,
    dp.name AS pricelist_name,
    dpt.d_warehouse_id,
    dw.name AS warehouse_name,
    dpt.d_bank_account_id,
    db.name AS bank_name,
    dpt.printer_ip,
    dpt.printer_port,
    dpt.is_bill_merge,
    dpt.is_notify_bill,
    dpt.d_bank_account_visa_id,
    dbvisa.name AS bank_visa_name,
    dpt.created,
    dpt.updated,
    dpt.created_by,
    dpt.updated_by,
    dpt.is_active,
    dpt.d_doctype_id,
    dd.name AS doctype_name
   FROM (((((((pos.d_pos_terminal dpt
     LEFT JOIN pos.d_pricelist dp ON ((dp.d_pricelist_id = dpt.d_pricelist_id)))
     LEFT JOIN pos.d_user du ON ((du.d_user_id = dpt.d_user_id)))
     LEFT JOIN pos.d_warehouse dw ON ((dw.d_warehouse_id = dpt.d_warehouse_id)))
     LEFT JOIN pos.d_doctype dd ON ((dd.d_doctype_id = dpt.d_doctype_id)))
     LEFT JOIN pos.d_bankaccount db ON ((db.d_bankaccount_id = dpt.d_bank_account_id)))
     LEFT JOIN pos.d_bankaccount dbcash ON ((db.d_bankaccount_id = dpt.d_bank_account_cash_id)))
     LEFT JOIN pos.d_bankaccount dbvisa ON ((db.d_bankaccount_id = dpt.d_bank_account_visa_id)));


ALTER VIEW pos.d_pos_terminal_v OWNER TO postgres;

--
-- TOC entry 409 (class 1259 OID 395000)
-- Name: d_posorder_complete_v; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.d_posorder_complete_v AS
 SELECT dpo.d_pos_order_id,
    dpo.d_tenant_id,
    dpo.d_org_id,
    dpo.is_active,
    dpo.created_by,
    dpo.created,
    dpo.updated_by,
    dpo.updated,
    dpo.document_no,
    dpo.order_date,
    sum(div.total_amount) AS total_amount,
    sum(dp.payment_amount) AS payment_amount,
    drl.name AS order_status,
    drl.value AS order_value,
    dc.name AS customer_name,
    dc.d_customer_id,
    sum(
        CASE
            WHEN ((dp.payment_status)::text = 'COM'::text) THEN dp.payment_amount
            ELSE (0)::numeric
        END) AS paid
   FROM (((((pos.d_pos_order dpo
     LEFT JOIN pos.d_invoice_v div ON ((dpo.d_pos_order_id = div.d_pos_order_id)))
     LEFT JOIN pos.d_payment dp ON ((div.d_invoice_id = dp.d_invoice_id)))
     LEFT JOIN pos.d_customer dc ON ((dpo.d_customer_id = dc.d_customer_id)))
     JOIN pos.d_reference_list drl ON (((dpo.order_status)::text = (drl.value)::text)))
     JOIN pos.d_reference dr ON (((drl.d_reference_id = dr.d_reference_id) AND ((dr.name)::text = 'Document Status'::text))))
  WHERE ((dpo.order_status)::text = 'COM'::text)
  GROUP BY dpo.d_pos_order_id, dpo.d_tenant_id, dpo.d_org_id, dpo.is_active, dpo.created_by, dpo.created, dpo.updated_by, dpo.updated, dpo.document_no, dpo.order_date, drl.name, drl.value, dc.name, dc.d_customer_id;


ALTER VIEW pos.d_posorder_complete_v OWNER TO adempiere;

--
-- TOC entry 397 (class 1259 OID 394786)
-- Name: d_posterminal_org_access; Type: TABLE; Schema: pos; Owner: adempiere
--

CREATE TABLE pos.d_posterminal_org_access (
    d_user_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_pos_terminal_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    d_posterminal_org_access_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_posterminal_org_access OWNER TO adempiere;

--
-- TOC entry 399 (class 1259 OID 394852)
-- Name: d_posterminal_org_access_v; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.d_posterminal_org_access_v AS
 SELECT dpoa.d_tenant_id,
    dpoa.d_pos_terminal_id,
    dpoa.d_user_id,
    dpoa.d_org_id,
    dpoa.created,
    dpoa.created_by,
    dpoa.updated,
    dpoa.updated_by,
    dpoa.is_active,
    dpt.name
   FROM (pos.d_posterminal_org_access dpoa
     LEFT JOIN pos.d_pos_terminal dpt ON ((dpoa.d_pos_terminal_id = dpt.d_pos_terminal_id)));


ALTER VIEW pos.d_posterminal_org_access_v OWNER TO adempiere;

--
-- TOC entry 313 (class 1259 OID 388792)
-- Name: d_pricelist_org; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_pricelist_org (
    d_pricelist_org_id numeric(10,0) NOT NULL,
    d_pricelist_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    isall character varying(1) DEFAULT 'Y'::character varying,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_pricelist_org_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_pricelist_org OWNER TO postgres;

--
-- TOC entry 366 (class 1259 OID 393623)
-- Name: d_pricelist_org_info_v; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.d_pricelist_org_info_v AS
 SELECT dpo.d_pricelist_id,
    dpo.d_tenant_id,
    dpo.isall,
    org.d_org_id,
    org.name,
    org.area,
    org.phone,
    org.address,
    org.code,
    org.is_active
   FROM (pos.d_pricelist_org dpo
     JOIN pos.d_org org ON ((dpo.d_org_id = org.d_org_id)));


ALTER VIEW pos.d_pricelist_org_info_v OWNER TO adempiere;

--
-- TOC entry 312 (class 1259 OID 388790)
-- Name: d_pricelist_org_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_pricelist_org_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_pricelist_org_sq OWNER TO postgres;

--
-- TOC entry 310 (class 1259 OID 388769)
-- Name: d_pricelist_product_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_pricelist_product_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_pricelist_product_sq OWNER TO postgres;

--
-- TOC entry 311 (class 1259 OID 388771)
-- Name: d_pricelist_product; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_pricelist_product (
    d_pricelist_product_id numeric(10,0) DEFAULT nextval('pos.d_pricelist_product_sq'::regclass) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_product_id numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    costprice numeric,
    standardprice numeric,
    salesprice numeric,
    lastorderprice numeric,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_pricelist_product_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    d_pricelist_id numeric(10,0) NOT NULL
);


ALTER TABLE pos.d_pricelist_product OWNER TO postgres;

--
-- TOC entry 365 (class 1259 OID 393588)
-- Name: d_pricelist_v; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.d_pricelist_v AS
 SELECT DISTINCT dp.d_pricelist_id,
    dp.d_tenant_id,
    dp.d_org_id,
    dp.name,
    dpo.isall,
    dp.general_pricelist,
    dp.created,
    dp.created_by,
    dp.updated,
    dp.updated_by,
    dp.is_active,
    dp.from_date,
    dp.to_date,
    org.d_org_id AS org_id,
    org.name AS org_name,
    org.code AS org_code,
    org.phone AS org_phone,
    org.area AS org_area,
    org.is_active AS org_is_active
   FROM ((pos.d_pricelist dp
     LEFT JOIN pos.d_pricelist_org dpo ON ((dp.d_pricelist_id = dpo.d_pricelist_id)))
     LEFT JOIN pos.d_org org ON ((org.d_org_id = dpo.d_org_id)));


ALTER VIEW pos.d_pricelist_v OWNER TO adempiere;

--
-- TOC entry 364 (class 1259 OID 393562)
-- Name: d_pricelist_v_find_id; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.d_pricelist_v_find_id AS
 SELECT dp.d_pricelist_id,
    dp.d_tenant_id,
    dp.d_org_id,
    dp.created_by,
    dp.created,
    dp.updated_by,
    dp.updated,
    dp.general_pricelist,
    dp.is_active,
    dp.is_saleprice,
    dp.name AS name_pricelist,
    dp.from_date,
    dpo.d_org_id AS d_pricelist_org_id,
    org.name AS name_org,
    org.address,
    org.area,
    org.phone,
    org.email
   FROM ((pos.d_pricelist dp
     LEFT JOIN pos.d_pricelist_org dpo ON ((dp.d_pricelist_id = dpo.d_pricelist_id)))
     LEFT JOIN pos.d_org org ON ((org.d_org_id = dpo.d_org_id)));


ALTER VIEW pos.d_pricelist_v_find_id OWNER TO adempiere;

--
-- TOC entry 317 (class 1259 OID 390225)
-- Name: d_print_report; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_print_report (
    d_print_report_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    report_type character varying(32) NOT NULL,
    report_source text NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_print_report_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_default character varying(1) DEFAULT 'N'::character varying,
    is_active character varying(1) DEFAULT 'Y'::character varying
);


ALTER TABLE pos.d_print_report OWNER TO postgres;

--
-- TOC entry 316 (class 1259 OID 390223)
-- Name: d_print_report_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_print_report_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_print_report_sq OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 385779)
-- Name: d_product_category; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_product_category (
    d_product_category_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    code character varying(32),
    name character varying(255),
    d_product_category_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    d_product_category_parent_id numeric(10,0),
    is_summary character varying(1) DEFAULT 'N'::character varying,
    qty_product numeric,
    erp_product_category_id numeric(10,0)
);


ALTER TABLE pos.d_product_category OWNER TO postgres;

--
-- TOC entry 287 (class 1259 OID 386869)
-- Name: d_product_category_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_product_category_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_product_category_sq OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 385787)
-- Name: d_product_combo; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_product_combo (
    d_product_combo_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_product_id numeric(10,0) NOT NULL,
    d_product_component_id numeric(10,0) NOT NULL,
    description character varying(255),
    d_product_combo_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_item character varying(1) DEFAULT 'N'::character varying,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    qty numeric
);


ALTER TABLE pos.d_product_combo OWNER TO postgres;

--
-- TOC entry 288 (class 1259 OID 386871)
-- Name: d_product_combo_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_product_combo_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_product_combo_sq OWNER TO postgres;

--
-- TOC entry 343 (class 1259 OID 393046)
-- Name: d_product_location; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_product_location (
    d_product_location_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_warehouse_id numeric(10,0) NOT NULL,
    d_pos_terminal_id numeric(10,0),
    d_product_id numeric(10,0) NOT NULL,
    erp_product_location_id numeric(10,0),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_product_location_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    is_default character varying(1) DEFAULT 'N'::character varying NOT NULL
);


ALTER TABLE pos.d_product_location OWNER TO postgres;

--
-- TOC entry 342 (class 1259 OID 393044)
-- Name: d_product_location_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_product_location_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_product_location_sq OWNER TO postgres;

--
-- TOC entry 286 (class 1259 OID 386867)
-- Name: d_product_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_product_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_product_sq OWNER TO postgres;

--
-- TOC entry 359 (class 1259 OID 393403)
-- Name: d_production; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_production (
    d_production_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_product_id numeric(10,0) NOT NULL,
    d_doctype_id numeric(10,0) NOT NULL,
    documentno character varying(32) NOT NULL,
    name character varying(255),
    movement_date timestamp without time zone,
    production_qty numeric,
    documentstatus character varying(32) NOT NULL,
    description character varying(255),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_production_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    is_processed character varying(1) DEFAULT 'N'::character varying NOT NULL,
    is_sync_erp character varying(1) DEFAULT 'N'::character varying NOT NULL,
    erp_production_id numeric(10,0),
    d_warehouse_id numeric(10,0),
    d_locator_id numeric(10,0)
);


ALTER TABLE pos.d_production OWNER TO postgres;

--
-- TOC entry 358 (class 1259 OID 393401)
-- Name: d_production_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_production_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_production_sq OWNER TO postgres;

--
-- TOC entry 361 (class 1259 OID 393431)
-- Name: d_productionline; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_productionline (
    d_productionline_id numeric(10,0) NOT NULL,
    d_production_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    lineno integer,
    d_product_id numeric(10,0) NOT NULL,
    is_end_product character varying(1) DEFAULT 'N'::character varying NOT NULL,
    planned_qty numeric,
    description character varying(255),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_productionline_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL
);


ALTER TABLE pos.d_productionline OWNER TO postgres;

--
-- TOC entry 360 (class 1259 OID 393429)
-- Name: d_productionline_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_productionline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_productionline_sq OWNER TO postgres;

--
-- TOC entry 363 (class 1259 OID 393488)
-- Name: d_reconciledetail; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_reconciledetail (
    d_reconciledetail_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_reconciledetail_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    cmd character varying(60),
    merchant_code character varying(30),
    access_code character varying(100),
    version character varying(5),
    check_sum character varying(2000),
    error_code character varying(2000),
    transaction_id character varying(60),
    payment_status character varying(10),
    return_check_sum character varying(2000),
    hash_key character varying(200),
    merchant_name character varying(255),
    terminalid character varying(40),
    user_update character varying(50),
    payment_amount numeric,
    qrcode_payment character varying(255),
    transaction_no character varying(128),
    reference_code character varying(32),
    d_bankaccount_id numeric(10,0),
    d_bank_id numeric(10,0),
    accountno character varying(32),
    d_customer_id numeric(10,0),
    d_pos_order_id numeric(10,0),
    ftcode character varying(255)
);


ALTER TABLE pos.d_reconciledetail OWNER TO postgres;

--
-- TOC entry 362 (class 1259 OID 393486)
-- Name: d_reconciledetail_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_reconciledetail_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_reconciledetail_sq OWNER TO postgres;

--
-- TOC entry 388 (class 1259 OID 394549)
-- Name: d_request_order_sq; Type: SEQUENCE; Schema: pos; Owner: adempiere
--

CREATE SEQUENCE pos.d_request_order_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_request_order_sq OWNER TO adempiere;

--
-- TOC entry 390 (class 1259 OID 394573)
-- Name: d_request_order; Type: TABLE; Schema: pos; Owner: adempiere
--

CREATE TABLE pos.d_request_order (
    d_request_order_id numeric(10,0) DEFAULT nextval('pos.d_request_order_sq'::regclass) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric NOT NULL,
    d_doctype_id numeric(10,0),
    document_no character varying(32) NOT NULL,
    order_status character varying(5) NOT NULL,
    d_floor_id numeric(10,0),
    d_table_id numeric(10,0),
    order_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    d_request_order_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    d_customer_id numeric(10,0),
    is_active character varying(1) DEFAULT 'Y'::character varying
);


ALTER TABLE pos.d_request_order OWNER TO adempiere;

--
-- TOC entry 254 (class 1259 OID 385866)
-- Name: d_table; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_table (
    d_table_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    table_no character varying(5) NOT NULL,
    name character varying(32) NOT NULL,
    description character varying(255),
    d_floor_id numeric(10,0),
    table_status character varying(3) NOT NULL,
    d_table_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    number_seats numeric,
    display_index numeric,
    number_guests numeric(10,0),
    is_room character varying(1) DEFAULT 'N'::character varying,
    d_customer_id numeric(10,0),
    is_locked character varying(1) DEFAULT 'N'::character varying,
    erp_table_id numeric(10,0)
);


ALTER TABLE pos.d_table OWNER TO postgres;

--
-- TOC entry 395 (class 1259 OID 394744)
-- Name: d_request_order_get_all_v; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.d_request_order_get_all_v AS
 SELECT dro.d_request_order_id,
    dro.d_tenant_id,
    dro.d_org_id,
    dro.created_by,
    dro.created,
    dro.updated_by,
    dro.updated,
    dro.is_active,
    dro.d_request_order_uu,
    dc.name AS customer_name,
    dc.d_customer_id,
    dro.order_time,
    dc.phone1,
    df.d_floor_id,
    df.name AS floor_name,
    dt.d_table_id,
    dt.name AS table_name,
    dro.order_status
   FROM ((((((pos.d_request_order dro
     LEFT JOIN pos.d_table dt ON ((dro.d_table_id = dt.d_table_id)))
     LEFT JOIN pos.d_floor df ON ((dro.d_floor_id = df.d_floor_id)))
     LEFT JOIN pos.d_customer dc ON ((dro.d_customer_id = dc.d_customer_id)))
     LEFT JOIN pos.d_doctype dd ON ((dro.d_doctype_id = dd.d_doctype_id)))
     LEFT JOIN pos.d_reference_list drl ON (((dro.order_status)::text = (drl.value)::text)))
     LEFT JOIN pos.d_reference dr ON ((dr.d_reference_id = drl.d_reference_id)))
  WHERE ((dr.name)::text = 'Request Order Status'::text);


ALTER VIEW pos.d_request_order_get_all_v OWNER TO adempiere;

--
-- TOC entry 389 (class 1259 OID 394551)
-- Name: d_request_orderline_sq; Type: SEQUENCE; Schema: pos; Owner: adempiere
--

CREATE SEQUENCE pos.d_request_orderline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_request_orderline_sq OWNER TO adempiere;

--
-- TOC entry 391 (class 1259 OID 394610)
-- Name: d_request_orderline; Type: TABLE; Schema: pos; Owner: adempiere
--

CREATE TABLE pos.d_request_orderline (
    d_request_orderline_id numeric(10,0) DEFAULT nextval('pos.d_request_orderline_sq'::regclass) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric NOT NULL,
    d_product_id numeric(10,0),
    d_request_order_id numeric(10,0),
    qty numeric(10,0),
    description text,
    saleprice numeric(10,2),
    total_amount numeric(10,2),
    d_request_orderline_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying
);


ALTER TABLE pos.d_request_orderline OWNER TO adempiere;

--
-- TOC entry 258 (class 1259 OID 385904)
-- Name: d_uom; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_uom (
    d_uom_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    code character varying(5),
    name character varying(15),
    description character varying(255),
    d_uom_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying
);


ALTER TABLE pos.d_uom OWNER TO postgres;

--
-- TOC entry 396 (class 1259 OID 394759)
-- Name: d_request_orderline_get_all_v; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.d_request_orderline_get_all_v AS
 SELECT drol.d_request_orderline_id,
    drol.d_request_order_id,
    drol.d_tenant_id,
    drol.d_org_id,
    drol.created_by,
    drol.created,
    drol.updated_by,
    drol.updated,
    drol.is_active,
    drol.d_request_orderline_uu,
    dp.name AS name_product,
    dp.code AS code_product,
    dp.product_type,
    dp.d_product_id,
    du.name AS name_uom,
    du.d_uom_id,
    drol.qty,
    drol.description,
    drol.saleprice,
    drol.total_amount
   FROM ((pos.d_request_orderline drol
     LEFT JOIN pos.d_product dp ON ((drol.d_product_id = dp.d_product_id)))
     LEFT JOIN pos.d_uom du ON ((dp.d_uom_id = du.d_uom_id)));


ALTER VIEW pos.d_request_orderline_get_all_v OWNER TO adempiere;

--
-- TOC entry 250 (class 1259 OID 385824)
-- Name: d_reservation_line; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_reservation_line (
    d_reservation_line_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    description character varying(255),
    d_reservation_order_id numeric(10,0) NOT NULL,
    d_product_id numeric(10,0) NOT NULL,
    qty numeric(10,0) NOT NULL,
    d_reservation_line_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_reservation_line OWNER TO postgres;

--
-- TOC entry 291 (class 1259 OID 386877)
-- Name: d_reservation_line_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_reservation_line_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_reservation_line_sq OWNER TO postgres;

--
-- TOC entry 251 (class 1259 OID 385832)
-- Name: d_reservation_order; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_reservation_order (
    d_reservation_order_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    name character varying(255),
    d_floor_id numeric(10,0),
    d_table_id numeric(10,0),
    customer_name character varying(255),
    phone character varying(15),
    company character varying(255),
    total_cus numeric,
    reservation_time timestamp without time zone,
    reser_amount numeric,
    status character varying(10) NOT NULL,
    d_reservation_order_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    code character varying(255),
    d_customer_id numeric(10,0),
    time_tocome character varying(20),
    d_user_id numeric(10,0),
    qty_baby numeric,
    qty_adult numeric
);


ALTER TABLE pos.d_reservation_order OWNER TO postgres;

--
-- TOC entry 292 (class 1259 OID 386879)
-- Name: d_reservation_order_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_reservation_order_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_reservation_order_sq OWNER TO postgres;

--
-- TOC entry 392 (class 1259 OID 394682)
-- Name: d_reservation_v_all; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.d_reservation_v_all AS
 SELECT dro.d_reservation_order_id,
    dro.d_tenant_id,
    dro.d_org_id,
    dro.created,
    dro.created_by,
    dro.updated,
    dro.updated_by,
    dro.is_active,
    dro.phone,
    dro.total_cus,
    dro.company,
    dro.customer_name,
    dro.reservation_time,
    dro.reser_amount,
    dro.status,
    dro.d_reservation_order_uu,
    dro.time_tocome,
    dro.qty_adult,
    dro.qty_baby,
    dt.d_table_id,
    dt.name AS name_table,
    dt.table_no,
    dt.display_index AS display_index_table,
    dt.table_status,
    df.d_floor_id,
    df.display_index AS display_index_floor,
    df.name AS name_floor,
    df.floor_no,
    dc.name AS name_customer,
    dc.d_customer_id,
    dc.email AS email_customer,
    dc.address1 AS address_customer,
    du.d_user_id,
    du.full_name AS name_user,
    du.email AS email_user,
    dr.name AS name_reference,
    drl.name AS name_reference_list,
    drl.value AS reference_list_value
   FROM ((((((pos.d_reservation_order dro
     LEFT JOIN pos.d_table dt ON ((dro.d_table_id = dt.d_table_id)))
     LEFT JOIN pos.d_floor df ON ((dro.d_floor_id = df.d_floor_id)))
     LEFT JOIN pos.d_reference_list drl ON (((drl.value)::text = (dro.status)::text)))
     LEFT JOIN pos.d_reference dr ON ((drl.d_reference_id = dr.d_reference_id)))
     LEFT JOIN pos.d_customer dc ON ((dro.d_customer_id = dc.d_customer_id)))
     LEFT JOIN pos.d_user du ON ((dro.d_user_id = du.d_user_id)));


ALTER VIEW pos.d_reservation_v_all OWNER TO adempiere;

--
-- TOC entry 341 (class 1259 OID 393021)
-- Name: d_shift_control; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_shift_control (
    d_shift_control_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_user_id numeric(10,0),
    sequence_no integer,
    d_pos_terminal_id numeric(10,0),
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    shift_type character varying(3),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_shift_control_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    is_closed character varying(1) DEFAULT 'N'::character varying NOT NULL,
    erp_shift_control_id numeric(10,0)
);


ALTER TABLE pos.d_shift_control OWNER TO postgres;

--
-- TOC entry 340 (class 1259 OID 392997)
-- Name: d_shift_control_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_shift_control_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_shift_control_sq OWNER TO postgres;

--
-- TOC entry 253 (class 1259 OID 385855)
-- Name: d_storage_onhand; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_storage_onhand (
    d_storage_onhand_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    qty numeric,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    d_warehouse_id numeric(10,0) NOT NULL,
    d_locator_id numeric(10,0),
    reservation_qty numeric,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_storage_onhand_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_storage_onhand OWNER TO postgres;

--
-- TOC entry 294 (class 1259 OID 386883)
-- Name: d_storage_onhand_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_storage_onhand_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_storage_onhand_sq OWNER TO postgres;

--
-- TOC entry 375 (class 1259 OID 393893)
-- Name: d_table_pos_v; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.d_table_pos_v AS
 SELECT dt.d_table_id,
    dt.d_tenant_id,
    dt.d_org_id,
    dt.created,
    dt.created_by,
    dt.updated,
    dt.updated_by,
    dt.is_active,
    dt.d_table_uu,
    dt.name,
    dt.table_status,
    dt.display_index,
    dt.number_seats,
    dt.table_no,
    dt.d_floor_id,
    dp.d_pos_order_id,
    dro.d_reservation_order_id,
    dro.name AS name_reservation,
    dro.customer_name,
    dro.d_customer_id,
    dro.d_user_id,
    dro.time_tocome,
    dro.reservation_time
   FROM (((pos.d_table dt
     LEFT JOIN pos.d_floor df ON (((dt.d_floor_id = df.d_floor_id) AND ((dt.is_active)::text = 'Y'::text))))
     LEFT JOIN pos.d_pos_order dp ON (((dp.d_table_id = dt.d_table_id) AND ((dp.is_active)::text = 'Y'::text) AND ((dp.order_status)::text = 'DRA'::text))))
     LEFT JOIN pos.d_reservation_order dro ON ((dro.d_table_id = dt.d_table_id)));


ALTER VIEW pos.d_table_pos_v OWNER TO adempiere;

--
-- TOC entry 295 (class 1259 OID 386885)
-- Name: d_table_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_table_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_table_sq OWNER TO postgres;

--
-- TOC entry 339 (class 1259 OID 392962)
-- Name: d_table_use_ref; Type: TABLE; Schema: pos; Owner: adempiere
--

CREATE TABLE pos.d_table_use_ref (
    d_table_use_ref_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created date DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated date DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    domain_name character varying(32),
    d_table_use_ref_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    d_org_id numeric(10,0),
    d_reference_id numeric(10,0),
    domain_column character varying
);


ALTER TABLE pos.d_table_use_ref OWNER TO adempiere;

--
-- TOC entry 338 (class 1259 OID 392960)
-- Name: d_table_use_ref_sq; Type: SEQUENCE; Schema: pos; Owner: adempiere
--

CREATE SEQUENCE pos.d_table_use_ref_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_table_use_ref_sq OWNER TO adempiere;

--
-- TOC entry 255 (class 1259 OID 385874)
-- Name: d_tax; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_tax (
    d_tax_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    name character varying(64) NOT NULL,
    d_tax_category_id numeric(10,0) NOT NULL,
    tax_rate numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    is_default character varying(1) DEFAULT 'N'::character varying NOT NULL,
    is_saletax character varying(1) DEFAULT 'Y'::character varying,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_tax_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_tax OWNER TO postgres;

--
-- TOC entry 256 (class 1259 OID 385884)
-- Name: d_tax_category; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_tax_category (
    d_tax_category_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    name character varying(32) NOT NULL,
    description character varying(255),
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    is_default character varying(1) DEFAULT 'N'::character varying NOT NULL,
    created_by numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_tax_category_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_tax_category OWNER TO postgres;

--
-- TOC entry 297 (class 1259 OID 386889)
-- Name: d_tax_category_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_tax_category_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_tax_category_sq OWNER TO postgres;

--
-- TOC entry 296 (class 1259 OID 386887)
-- Name: d_tax_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_tax_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_tax_sq OWNER TO postgres;

--
-- TOC entry 298 (class 1259 OID 386891)
-- Name: d_tenant_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_tenant_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_tenant_sq OWNER TO postgres;

--
-- TOC entry 257 (class 1259 OID 385893)
-- Name: d_tenant; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_tenant (
    d_tenant_id numeric(10,0) DEFAULT nextval('pos.d_tenant_sq'::regclass) NOT NULL,
    code character varying(32) NOT NULL,
    name character varying(255) NOT NULL,
    domain_url character varying(255),
    d_industry_id numeric(10,0),
    is_active character varying(1) DEFAULT 'Y'::character varying,
    tax_code character varying(15),
    d_image_id numeric(10,0),
    expired_date date,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_tenant_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_tenant OWNER TO postgres;

--
-- TOC entry 328 (class 1259 OID 391463)
-- Name: d_uom_product; Type: TABLE; Schema: pos; Owner: adempiere
--

CREATE TABLE pos.d_uom_product (
    d_uom_product_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    d_product_id numeric(10,0),
    d_uom_id character varying(15),
    conversion_value numeric,
    costprice numeric,
    d_uom_product_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying,
    d_org_id numeric
);


ALTER TABLE pos.d_uom_product OWNER TO adempiere;

--
-- TOC entry 329 (class 1259 OID 391485)
-- Name: d_uom_product_sq; Type: SEQUENCE; Schema: pos; Owner: adempiere
--

CREATE SEQUENCE pos.d_uom_product_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_uom_product_sq OWNER TO adempiere;

--
-- TOC entry 299 (class 1259 OID 386893)
-- Name: d_uom_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_uom_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_uom_sq OWNER TO postgres;

--
-- TOC entry 305 (class 1259 OID 388478)
-- Name: d_vendor_audit; Type: TABLE; Schema: pos; Owner: adempiere
--

CREATE TABLE pos.d_vendor_audit (
    d_vendor_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0),
    code character varying(32),
    name character varying(255),
    phone1 character varying(15),
    phone2 character varying(15),
    address1 character varying(255),
    address2 character varying(255),
    tax_code character varying(15),
    email character varying(64),
    birthday date,
    d_image_id numeric(10,0),
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0),
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0),
    is_active character varying(1) DEFAULT 'Y'::character varying,
    d_vendor_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    rev integer,
    revtype integer,
    id integer NOT NULL
);


ALTER TABLE pos.d_vendor_audit OWNER TO adempiere;

--
-- TOC entry 301 (class 1259 OID 386897)
-- Name: d_vendor_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_vendor_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_vendor_sq OWNER TO postgres;

--
-- TOC entry 263 (class 1259 OID 385939)
-- Name: d_voucher; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.d_voucher (
    d_voucher_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    voucher_code character varying(64) NOT NULL,
    amount numeric,
    expired_date date NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    d_voucher_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL
);


ALTER TABLE pos.d_voucher OWNER TO postgres;

--
-- TOC entry 302 (class 1259 OID 386899)
-- Name: d_voucher_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_voucher_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_voucher_sq OWNER TO postgres;

--
-- TOC entry 303 (class 1259 OID 386901)
-- Name: d_warehouse_sq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.d_warehouse_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.d_warehouse_sq OWNER TO postgres;

--
-- TOC entry 374 (class 1259 OID 393872)
-- Name: get_kitchen_order_bystatus_v; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.get_kitchen_order_bystatus_v AS
 SELECT dkol.d_tenant_id,
    dkol.d_org_id,
    dkol.created,
    dkol.created_by,
    dkol.updated,
    dkol.updated_by,
    dkol.is_active,
    dkol.d_kitchen_orderline_uu,
    dkol.d_kitchen_orderline_id,
    dkol.d_kitchen_order_id,
    dkol.note,
    dkol.qty,
    dkol.orderline_status AS status_value,
    dkol.description,
    dkol.d_pos_orderline_id,
    dpr.d_production_id,
    dpr.documentno,
    dcr.name AS cancelreason,
    drgv.name AS orderline_status,
    dp.name AS name_product,
    dp.d_product_id,
    dp.code AS code_product,
    dp.product_type
   FROM ((((pos.d_kitchen_orderline dkol
     LEFT JOIN pos.d_product dp ON ((dkol.d_product_id = dp.d_product_id)))
     LEFT JOIN pos.d_reference_get_v drgv ON (((drgv.value)::text = (dkol.orderline_status)::text)))
     LEFT JOIN pos.d_production dpr ON ((dkol.d_production_id = dpr.d_production_id)))
     LEFT JOIN pos.d_cancel_reason dcr ON ((dcr.d_cancel_reason_id = dkol.d_cancel_reason_id)))
  WHERE ((drgv.name_reference)::text = 'Order Status'::text);


ALTER VIEW pos.get_kitchen_order_bystatus_v OWNER TO adempiere;

--
-- TOC entry 307 (class 1259 OID 388552)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: pos; Owner: adempiere
--

CREATE SEQUENCE pos.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.hibernate_sequence OWNER TO adempiere;

--
-- TOC entry 385 (class 1259 OID 394438)
-- Name: report_sales; Type: VIEW; Schema: pos; Owner: adempiere
--

CREATE VIEW pos.report_sales AS
 SELECT row_number() OVER (ORDER BY dpo.d_pos_order_id) AS stt,
    dpo.d_pos_order_id,
    dog.name AS name_org,
    dpo.order_date,
    dpo.document_no AS docno_order,
    di.document_no AS docno_invoice,
    du.full_name AS sales_rep,
    dc.name AS cus_name,
    dp.name AS product_name,
    duo.name AS uom_name,
    dp.saleprice,
    dpol.linenet_amt,
    dpol.tax_amount,
    dpol.grand_total
   FROM (((((((pos.d_pos_order dpo
     LEFT JOIN pos.d_org dog ON ((dpo.d_org_id = dog.d_org_id)))
     LEFT JOIN pos.d_pos_orderline dpol ON ((dpo.d_pos_order_id = dpol.d_pos_order_id)))
     LEFT JOIN pos.d_user du ON ((dpo.d_user_id = du.d_user_id)))
     LEFT JOIN pos.d_invoice di ON ((dpo.d_pos_order_id = di.d_pos_order_id)))
     LEFT JOIN pos.d_customer dc ON ((dpo.d_customer_id = dc.d_customer_id)))
     LEFT JOIN pos.d_product dp ON ((dpol.d_product_id = dp.d_product_id)))
     LEFT JOIN pos.d_uom duo ON ((dp.d_uom_id = dp.d_uom_id)));


ALTER VIEW pos.report_sales OWNER TO adempiere;

--
-- TOC entry 304 (class 1259 OID 388439)
-- Name: revinfo; Type: TABLE; Schema: pos; Owner: adempiere
--

CREATE TABLE pos.revinfo (
    rev bigint NOT NULL,
    revtstmp bigint NOT NULL,
    revtype smallint
);


ALTER TABLE pos.revinfo OWNER TO adempiere;

--
-- TOC entry 406 (class 1259 OID 394978)
-- Name: tenants; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.tenants (
    id bigint,
    name character varying,
    db_name character varying,
    user_name character varying,
    db_password character varying,
    creation_status character varying
);


ALTER TABLE pos.tenants OWNER TO postgres;

--
-- TOC entry 407 (class 1259 OID 394984)
-- Name: tenants_id_seq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.tenants_id_seq
    START WITH 2
    INCREMENT BY 1
    MINVALUE 2
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.tenants_id_seq OWNER TO postgres;

--
-- TOC entry 405 (class 1259 OID 394972)
-- Name: user_roles; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.user_roles (
    user_id bigint,
    role character varying
);


ALTER TABLE pos.user_roles OWNER TO postgres;

--
-- TOC entry 403 (class 1259 OID 394963)
-- Name: users; Type: TABLE; Schema: pos; Owner: postgres
--

CREATE TABLE pos.users (
    id bigint,
    email character varying,
    password character varying,
    tenant_id bigint
);


ALTER TABLE pos.users OWNER TO postgres;

--
-- TOC entry 404 (class 1259 OID 394969)
-- Name: users_id_seq; Type: SEQUENCE; Schema: pos; Owner: postgres
--

CREATE SEQUENCE pos.users_id_seq
    START WITH 3
    INCREMENT BY 1
    MINVALUE 3
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pos.users_id_seq OWNER TO postgres;

--
-- TOC entry 5538 (class 0 OID 0)
-- Dependencies: 404
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: pos; Owner: postgres
--

ALTER SEQUENCE pos.users_id_seq OWNED BY pos.users.id;


--
-- TOC entry 219 (class 1259 OID 384914)
-- Name: address; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.address (
    address_id integer NOT NULL,
    user_id integer,
    full_address character varying(255),
    postal_code character varying(255),
    city character varying(255),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.address OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 384912)
-- Name: address_address_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.address_address_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.address_address_id_seq OWNER TO postgres;

--
-- TOC entry 5539 (class 0 OID 0)
-- Dependencies: 218
-- Name: address_address_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.address_address_id_seq OWNED BY public.address.address_id;


--
-- TOC entry 210 (class 1259 OID 384851)
-- Name: carts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.carts (
    cart_id integer NOT NULL,
    user_id integer,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.carts OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 384849)
-- Name: carts_cart_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.carts_cart_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.carts_cart_id_seq OWNER TO postgres;

--
-- TOC entry 5540 (class 0 OID 0)
-- Dependencies: 209
-- Name: carts_cart_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.carts_cart_id_seq OWNED BY public.carts.cart_id;


--
-- TOC entry 205 (class 1259 OID 384516)
-- Name: categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categories (
    category_id integer NOT NULL,
    parent_category_id integer,
    category_title character varying(255),
    image_url character varying(255),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    d_client_id character varying,
    d_org_id character varying,
    create_by character varying,
    update_by character varying
);


ALTER TABLE public.categories OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 384514)
-- Name: categories_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.categories_category_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.categories_category_id_seq OWNER TO postgres;

--
-- TOC entry 5541 (class 0 OID 0)
-- Dependencies: 204
-- Name: categories_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.categories_category_id_seq OWNED BY public.categories.category_id;


--
-- TOC entry 221 (class 1259 OID 384927)
-- Name: credentials; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.credentials (
    credential_id integer NOT NULL,
    user_id integer,
    username character varying(255),
    password character varying(255),
    role character varying(255),
    is_enabled boolean DEFAULT false,
    is_account_non_expired boolean DEFAULT true,
    is_account_non_locked boolean DEFAULT true,
    is_credentials_non_expired boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    created_by character varying,
    updated_by character varying
);


ALTER TABLE public.credentials OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 384925)
-- Name: credentials_credential_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.credentials_credential_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.credentials_credential_id_seq OWNER TO postgres;

--
-- TOC entry 5542 (class 0 OID 0)
-- Dependencies: 220
-- Name: credentials_credential_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.credentials_credential_id_seq OWNED BY public.credentials.credential_id;


--
-- TOC entry 377 (class 1259 OID 394256)
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);


ALTER TABLE public.databasechangelog OWNER TO postgres;

--
-- TOC entry 376 (class 1259 OID 394251)
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 384841)
-- Name: favourites; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.favourites (
    user_id integer NOT NULL,
    product_id integer NOT NULL,
    like_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.favourites OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 384504)
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 384889)
-- Name: order_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.order_items (
    product_id integer NOT NULL,
    order_id integer NOT NULL,
    ordered_quantity integer,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.order_items OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 384861)
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
    order_id integer NOT NULL,
    cart_id integer,
    order_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    order_desc character varying(255),
    order_fee numeric,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 384859)
-- Name: orders_order_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.orders_order_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.orders_order_id_seq OWNER TO postgres;

--
-- TOC entry 5543 (class 0 OID 0)
-- Dependencies: 211
-- Name: orders_order_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.orders_order_id_seq OWNED BY public.orders.order_id;


--
-- TOC entry 214 (class 1259 OID 384881)
-- Name: payments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payments (
    payment_id integer NOT NULL,
    order_id integer,
    is_payed boolean,
    payment_status character varying(255),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.payments OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 384879)
-- Name: payments_payment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.payments_payment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.payments_payment_id_seq OWNER TO postgres;

--
-- TOC entry 5544 (class 0 OID 0)
-- Dependencies: 213
-- Name: payments_payment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.payments_payment_id_seq OWNED BY public.payments.payment_id;


--
-- TOC entry 207 (class 1259 OID 384529)
-- Name: products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.products (
    product_id integer NOT NULL,
    category_id integer,
    product_title character varying(255),
    image_url character varying(255),
    sku character varying(255),
    price_unit numeric,
    quantity integer,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.products OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 384527)
-- Name: products_product_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.products_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.products_product_id_seq OWNER TO postgres;

--
-- TOC entry 5545 (class 0 OID 0)
-- Dependencies: 206
-- Name: products_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.products_product_id_seq OWNED BY public.products.product_id;


--
-- TOC entry 306 (class 1259 OID 388537)
-- Name: revinfo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.revinfo (
    rev integer NOT NULL,
    revtstmp bigint
);


ALTER TABLE public.revinfo OWNER TO postgres;

--
-- TOC entry 378 (class 1259 OID 394262)
-- Name: tenants; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tenants (
    id bigint,
    name character varying,
    db_name character varying,
    user_name character varying,
    db_password character varying,
    creation_status character varying
);


ALTER TABLE public.tenants OWNER TO postgres;

--
-- TOC entry 379 (class 1259 OID 394268)
-- Name: tenants_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tenants_id_seq
    START WITH 2
    INCREMENT BY 1
    MINVALUE 2
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tenants_id_seq OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 384898)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id integer NOT NULL,
    first_name character varying(255),
    last_name character varying(255),
    image_url character varying(255) DEFAULT 'https://bootdey.com/img/Content/avatar/avatar7.png'::character varying,
    email character varying(255) DEFAULT 'springxyzabcboot@gmail.com'::character varying,
    phone character varying(255) DEFAULT '+21622125144'::character varying,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_by character varying,
    created_by character varying
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 384896)
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_user_id_seq OWNER TO postgres;

--
-- TOC entry 5546 (class 0 OID 0)
-- Dependencies: 216
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;


--
-- TOC entry 223 (class 1259 OID 384944)
-- Name: verification_tokens; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.verification_tokens (
    verification_token_id integer NOT NULL,
    credential_id integer,
    verif_token character varying(255),
    expire_date date,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.verification_tokens OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 384942)
-- Name: verification_tokens_verification_token_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.verification_tokens_verification_token_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.verification_tokens_verification_token_id_seq OWNER TO postgres;

--
-- TOC entry 5547 (class 0 OID 0)
-- Dependencies: 222
-- Name: verification_tokens_verification_token_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.verification_tokens_verification_token_id_seq OWNED BY public.verification_tokens.verification_token_id;


--
-- TOC entry 4722 (class 2604 OID 394971)
-- Name: users id; Type: DEFAULT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.users ALTER COLUMN id SET DEFAULT nextval('pos.users_id_seq'::regclass);


--
-- TOC entry 4332 (class 2604 OID 384917)
-- Name: address address_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.address ALTER COLUMN address_id SET DEFAULT nextval('public.address_address_id_seq'::regclass);


--
-- TOC entry 4314 (class 2604 OID 384854)
-- Name: carts cart_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carts ALTER COLUMN cart_id SET DEFAULT nextval('public.carts_cart_id_seq'::regclass);


--
-- TOC entry 4305 (class 2604 OID 384519)
-- Name: categories category_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories ALTER COLUMN category_id SET DEFAULT nextval('public.categories_category_id_seq'::regclass);


--
-- TOC entry 4335 (class 2604 OID 384930)
-- Name: credentials credential_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.credentials ALTER COLUMN credential_id SET DEFAULT nextval('public.credentials_credential_id_seq'::regclass);


--
-- TOC entry 4317 (class 2604 OID 384864)
-- Name: orders order_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders ALTER COLUMN order_id SET DEFAULT nextval('public.orders_order_id_seq'::regclass);


--
-- TOC entry 4321 (class 2604 OID 384884)
-- Name: payments payment_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payments ALTER COLUMN payment_id SET DEFAULT nextval('public.payments_payment_id_seq'::regclass);


--
-- TOC entry 4308 (class 2604 OID 384532)
-- Name: products product_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products ALTER COLUMN product_id SET DEFAULT nextval('public.products_product_id_seq'::regclass);


--
-- TOC entry 4326 (class 2604 OID 384901)
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);


--
-- TOC entry 4342 (class 2604 OID 384947)
-- Name: verification_tokens verification_token_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.verification_tokens ALTER COLUMN verification_token_id SET DEFAULT nextval('public.verification_tokens_verification_token_id_seq'::regclass);


--
-- TOC entry 5525 (class 0 OID 394886)
-- Dependencies: 402
-- Data for Name: d_api_trace_log; Type: TABLE DATA; Schema: pos; Owner: adempiere
--

COPY pos.d_api_trace_log (d_api_trace_log, d_tenant_id, d_org_id, description, created, created_by, updated, updated_by, d_api_trace_log_uu, is_active, payload, data_type, in_out, exception) FROM stdin;
\.


--
-- TOC entry 5472 (class 0 OID 392797)
-- Dependencies: 331
-- Data for Name: d_assign_org_product; Type: TABLE DATA; Schema: pos; Owner: adempiere
--

COPY pos.d_assign_org_product (d_assign_org_id, d_tenant_id, d_product_id, d_org_id, d_assign_org_uu, created, created_by, updated, updated_by, is_active) FROM stdin;
1000000	1000004	1000473	1000020	7d0fef7f-5d64-4007-8b70-a408dc4ab75b	2024-09-05	1000069	2024-09-05	\N	Y
1000001	1000004	1000473	1000021	b181224e-f821-45e9-a025-e30906826137	2024-09-05	1000069	2024-09-05	\N	Y
1000002	1000004	1000476	1000020	3addd4bd-a4d6-48e4-a1f4-d9e045c0a9d8	2024-09-05	1000069	2024-09-05	\N	Y
1000003	1000004	1000476	1000021	5541b1be-6022-4bcd-a743-29ec684d190f	2024-09-05	1000069	2024-09-05	\N	Y
1000004	1000004	1000479	1000020	4c5b6fd5-8299-4cc7-a701-d76bc5fef4f7	2024-09-05	1000069	2024-09-05	\N	Y
1000005	1000004	1000479	1000021	5bc8f6e2-5189-4b96-9242-cfd9c3c281a0	2024-09-05	1000069	2024-09-05	\N	Y
1000006	1000004	1000482	1000020	f453e978-ff2c-4fa0-9e9e-b50e1d254412	2024-09-05	1000069	2024-09-05	\N	Y
1000007	1000004	1000482	1000021	614d6042-7eb4-4ba4-8d77-302ce4359149	2024-09-05	1000069	2024-09-05	\N	Y
1000008	1000004	1000485	1000020	a6409092-77fa-42e6-87b0-7c7c324e5752	2024-09-05	1000069	2024-09-05	\N	Y
1000009	1000004	1000485	1000021	7818ca5b-1eef-428c-bc82-d06f2074cbf5	2024-09-05	1000069	2024-09-05	\N	Y
1000010	1000004	1000488	1000020	03a2c0b4-d1ce-4049-9d10-29be3af7d006	2024-09-05	1000069	2024-09-05	\N	Y
1000011	1000004	1000488	1000021	afe14c2e-7ec5-49d0-a8b4-2d8eee398be5	2024-09-05	1000069	2024-09-05	\N	Y
1000014	1000004	1000434	1000016	506e1b4a-7731-4012-99a9-109a5afd5b63	2024-09-19	0	2024-09-19	0	Y
1000015	1000004	1000443	1000016	2f99cccf-7b17-4716-b78b-0f84da838400	2024-09-19	0	2024-09-19	0	Y
1000016	1000004	1000452	1000016	68fe94c4-fa1f-4286-a09e-1253f61478ae	2024-09-19	0	2024-09-19	0	Y
1000017	1000004	1000626	1000016	dd1bac7b-2bc2-4249-9ee6-c8a96e44ad1f	2024-09-19	0	2024-09-19	0	Y
1000018	1000004	1000431	1000016	3e2ff2bd-0155-416a-97a5-236f23591edc	2024-09-19	0	2024-09-19	0	Y
1000019	1000004	1000383	1000016	c11dfcb8-3bac-4446-a139-0563dc2a6c05	2024-09-19	0	2024-09-19	0	Y
1000020	1000004	1000644	1000016	903bb624-2e36-41fd-9792-2a5c6298d5cc	2024-09-19	0	2024-09-19	0	Y
1000021	1000004	1000395	1000016	79c1dbae-b8ee-4b6c-b08d-bb751470856e	2024-09-19	0	2024-09-19	0	Y
1000022	1000004	1000608	1000016	5b8bf3ee-c776-4f09-87a3-d71bcae112a5	2024-09-19	0	2024-09-19	0	Y
1000023	1000004	1000392	1000016	53c5b56d-0a91-4be3-a26c-45d19315cb0e	2024-09-19	0	2024-09-19	0	Y
1000024	1000004	1000623	1000016	1ba5034b-28b1-4883-8fd8-7b67ad827e11	2024-09-19	0	2024-09-19	0	Y
1000025	1000004	1000380	1000016	30441cd5-0416-4a14-813b-c3be5c1541e5	2024-09-19	0	2024-09-19	0	Y
1000026	1000004	1000130	1000016	61e8b684-4138-4b70-84f1-4c96fd601fb1	2024-09-19	0	2024-09-19	0	Y
1000027	1000004	1000643	1000016	ca33bbda-6e29-458b-a830-1c210b724d3d	2024-09-19	0	2024-09-19	0	Y
1000028	1000004	1000416	1000016	0a21c610-80cd-4fee-a8d0-e299b1895cc1	2024-09-19	0	2024-09-19	0	Y
1000029	1000004	1000476	1000016	9a7b0ced-8ab4-45fa-adc4-222114414865	2024-09-19	0	2024-09-19	0	Y
1000030	1000004	1000500	1000016	c07f8faa-2c8d-4727-ae5d-202ffa4bffeb	2024-09-19	0	2024-09-19	0	Y
1000031	1000004	1000651	1000016	e0a6ba1d-07e6-44e6-be86-dca51af5ff97	2024-09-19	0	2024-09-19	0	Y
1000032	1000004	1000464	1000016	7b7359ce-b12d-4970-94ad-c40a8679135e	2024-09-19	0	2024-09-19	0	Y
1000033	1000004	1000104	1000016	0ea881d2-7e72-45c2-a6e8-5f12adfa3b41	2024-09-19	0	2024-09-19	0	Y
1000034	1000004	1000413	1000016	f45d3aa6-825c-4704-8596-8c6bb21e964a	2024-09-19	0	2024-09-19	0	Y
1000035	1000004	1000138	1000016	f91f647f-c03c-4372-9e6a-e30ee1a27c7c	2024-09-19	0	2024-09-19	0	Y
1000036	1000004	1000509	1000016	f403c0e3-c035-4441-a79d-a05cba3a18db	2024-09-19	0	2024-09-19	0	Y
1000037	1000004	1000488	1000016	de7c971b-75c4-4ce8-946a-f47550fd31c2	2024-09-19	0	2024-09-19	0	Y
1000038	1000004	1000353	1000016	5ad198cd-c230-4f22-9abe-16bde062bcf5	2024-09-19	0	2024-09-19	0	Y
1000039	1000004	1000627	1000016	c74ce417-a128-4b55-9b14-9e8deebbe746	2024-09-19	0	2024-09-19	0	Y
1000040	1000004	1000404	1000016	a666f0c7-1f1e-4b09-88a3-aded19435674	2024-09-19	0	2024-09-19	0	Y
1000041	1000004	1000134	1000016	64790b17-7936-4004-88a7-ccf7bef79f86	2024-09-19	0	2024-09-19	0	Y
1000042	1000004	1000485	1000016	3f566e4d-9372-4004-820b-3a90cdd3b42b	2024-09-19	0	2024-09-19	0	Y
1000043	1000004	1000109	1000016	56d1727a-39a5-443f-b40c-3613e89bbf72	2024-09-19	0	2024-09-19	0	Y
1000044	1000004	1000113	1000016	51baaa5b-996f-4ad4-8d0f-7de20eb83755	2024-09-19	0	2024-09-19	0	Y
1000045	1000004	1000482	1000016	0b119dee-da0f-4838-b45e-452985394c42	2024-09-19	0	2024-09-19	0	Y
1000046	1000004	1000129	1000016	d691843a-83fb-4974-b764-184733a6992e	2024-09-19	0	2024-09-19	0	Y
1000047	1000004	1000106	1000016	22777197-464a-4494-bcec-fdcf13a7a2b3	2024-09-19	0	2024-09-19	0	Y
1000048	1000004	1000132	1000016	6ea72931-a9fc-473c-9b69-3c24de09ceaa	2024-09-19	0	2024-09-19	0	Y
1000049	1000004	1000389	1000016	3be8c567-b100-4e48-9701-b42b72df4090	2024-09-19	0	2024-09-19	0	Y
1000050	1000004	1000356	1000016	a0601432-176a-4773-ad31-6ce23895f01a	2024-09-19	0	2024-09-19	0	Y
1000051	1000004	1000359	1000016	12bbe2dd-8b19-4659-8aab-286a5d9cdebe	2024-09-19	0	2024-09-19	0	Y
1000052	1000004	1000140	1000016	8f6303af-59f1-4f10-a1d7-63be3cf9ffdd	2024-09-19	0	2024-09-19	0	Y
1000053	1000004	1000437	1000016	749c16c3-47bb-417b-b127-1932519dd84f	2024-09-19	0	2024-09-19	0	Y
1000054	1000004	1000112	1000016	6a255b85-89ad-48f4-b3c6-2a1f1d827add	2024-09-19	0	2024-09-19	0	Y
1000055	1000004	1000374	1000016	86ac7599-ce56-4ee7-a303-a42f0d9064fa	2024-09-19	0	2024-09-19	0	Y
1000056	1000004	1000428	1000016	87da2bf5-5bd6-4e1c-b5ba-8a694ddd426e	2024-09-19	0	2024-09-19	0	Y
1000057	1000004	1000642	1000016	30903aef-8d1d-4f19-ab9c-2d72a9a6e82d	2024-09-19	0	2024-09-19	0	Y
1000058	1000004	1000621	1000016	06b12216-fb13-4c8c-a461-2e5d753dbca4	2024-09-19	0	2024-09-19	0	Y
1000059	1000004	1000461	1000016	56ca484e-2177-4979-baba-777023c31973	2024-09-19	0	2024-09-19	0	Y
1000060	1000004	1000470	1000016	91f5c528-12d3-4b67-9ead-2b9d5b1c0a71	2024-09-19	0	2024-09-19	0	Y
1000061	1000004	1000458	1000016	f09b9c0d-907d-481c-9b5a-28fa833fb226	2024-09-19	0	2024-09-19	0	Y
1000062	1000004	1000467	1000016	5178ce67-002c-4199-ae60-ea90befd9aae	2024-09-19	0	2024-09-19	0	Y
1000063	1000004	1000622	1000016	0d93dde8-e43e-43ac-b1dc-61218cb1a763	2024-09-19	0	2024-09-19	0	Y
1000064	1000004	1000440	1000016	f31795b5-2d37-485a-a74e-3791083c7aa6	2024-09-19	0	2024-09-19	0	Y
1000065	1000004	1000425	1000016	2c3457fc-a9aa-4acf-bf71-ee680744e522	2024-09-19	0	2024-09-19	0	Y
1000066	1000004	1000139	1000016	67187543-e58f-4ff6-b81a-f6e3a20cfa46	2024-09-19	0	2024-09-19	0	Y
1000067	1000004	1000407	1000016	41125e07-7f0d-4628-8505-94ef4a6e5368	2024-09-19	0	2024-09-19	0	Y
1000068	1000004	1000398	1000016	cd1e8c12-dbb6-42eb-8666-ad2acdd37396	2024-09-19	0	2024-09-19	0	Y
1000069	1000004	1000111	1000016	27380ad2-de0b-4f6a-b38e-3f31652a4ca2	2024-09-19	0	2024-09-19	0	Y
1000070	1000004	1000422	1000016	aaab458f-976f-4fd6-8d55-6ae04568110a	2024-09-19	0	2024-09-19	0	Y
1000071	1000004	1000386	1000016	01dc5b36-bb2b-40ad-85a5-2421f91fe6c3	2024-09-19	0	2024-09-19	0	Y
1000072	1000004	1000479	1000016	d21cb2ab-ddf2-4582-a253-b83bffa5f57e	2024-09-19	0	2024-09-19	0	Y
1000073	1000004	1000449	1000016	8a622d9e-65ae-4f7c-9a49-f803c13e2fdf	2024-09-19	0	2024-09-19	0	Y
1000074	1000004	1000491	1000016	99428217-6799-4eb1-a7b1-0f7a8b0b9330	2024-09-19	0	2024-09-19	0	Y
1000075	1000004	1000377	1000016	5f2d1b03-9a7d-45e1-b7b4-0770547217f7	2024-09-19	0	2024-09-19	0	Y
1000076	1000004	1000497	1000016	d2585a6c-13e6-4eab-a2ff-8aad646cea10	2024-09-19	0	2024-09-19	0	Y
1000077	1000004	1000107	1000016	8be408b2-a7c8-46eb-b6c9-c60486deaaf1	2024-09-19	0	2024-09-19	0	Y
1000078	1000004	1000108	1000016	4f05a935-d0a7-4835-a26b-e0a1f3823a36	2024-09-19	0	2024-09-19	0	Y
1000079	1000004	1000494	1000016	2ea60728-113b-49c8-a8ae-9cbe72011a2d	2024-09-19	0	2024-09-19	0	Y
1000080	1000004	1000503	1000016	05a8384e-fbb0-4d48-847b-aafc17a94f3d	2024-09-19	0	2024-09-19	0	Y
1000081	1000004	1000103	1000016	eee224b5-59f7-429e-a1b5-19b8edc581b3	2024-09-19	0	2024-09-19	0	Y
1000082	1000004	1000401	1000016	8e3030b1-97e2-45bd-b033-0166b3e855d1	2024-09-19	0	2024-09-19	0	Y
1000083	1000004	1000410	1000016	a68b531f-cfa2-4e7c-9f2c-8d9743a88697	2024-09-19	0	2024-09-19	0	Y
1000084	1000004	1000136	1000016	a1f79703-7273-4572-8573-8c459450eef6	2024-09-19	0	2024-09-19	0	Y
1000085	1000004	1000473	1000016	1ce1ba5d-113b-4c47-9eaf-f8fd2c051ffe	2024-09-19	0	2024-09-19	0	Y
1000086	1000004	1000625	1000016	719bf72b-4c2c-4007-b858-65e73520f714	2024-09-19	0	2024-09-19	0	Y
1000087	1000004	1000446	1000016	f7d1f253-d4a3-4c41-89be-5316554db296	2024-09-19	0	2024-09-19	0	Y
1000088	1000004	1000506	1000016	d74f3809-4d1e-4c0d-acec-122f22d47442	2024-09-19	0	2024-09-19	0	Y
1000089	1000004	1000105	1000016	000eabab-4172-487d-b015-bae211484546	2024-09-19	0	2024-09-19	0	Y
1000090	1000004	1000371	1000016	61af2394-8040-4542-82e5-fcda7dd4567e	2024-09-19	0	2024-09-19	0	Y
1000091	1000004	1000119	1000016	debb3bb2-52c8-49d4-a145-d3be0e8d7071	2024-09-19	0	2024-09-19	0	Y
1000092	1000004	1000127	1000016	9a36d06c-0994-4136-b88e-a364a35118b3	2024-09-19	0	2024-09-19	0	Y
1000093	1000004	1000419	1000016	805040a5-85d3-40bb-8528-eb89067a3f9e	2024-09-19	0	2024-09-19	0	Y
1000094	1000004	1000368	1000016	cfe846ed-4ebc-45da-9b18-f32d246cc4ae	2024-09-19	0	2024-09-19	0	Y
1000095	1000004	1000455	1000016	0ab04b47-c743-4ad1-9a50-5ba146a6e11a	2024-09-19	0	2024-09-19	0	Y
1000096	1000004	1000577	1000016	46926801-ddcf-4635-8686-68154e341519	2024-09-19	0	2024-09-19	0	Y
1000097	1000004	1000562	1000016	595d35d7-f137-43e0-a269-3d61de640f02	2024-09-19	0	2024-09-19	0	Y
1000098	1000004	1000568	1000016	bb2e12a6-9dc5-401e-a887-8f1d7e68d983	2024-09-19	0	2024-09-19	0	Y
1000099	1000004	1000565	1000016	2c17a9eb-7c42-4a7c-b089-abdc91021cdb	2024-09-19	0	2024-09-19	0	Y
1000101	1000004	1000674	1000016	4ab08980-cb3d-4268-8590-d11117de7a80	2024-09-19	1000069	2024-09-19	\N	Y
1000102	1000004	1000675	1000016	3b4d38e8-15bf-48f6-9ca0-27148c22ba9d	2024-09-19	1000069	2024-09-19	\N	Y
1000103	1000004	1000676	1000016	b2f7e56f-80c1-461c-a806-224ac9887458	2024-09-19	1000069	2024-09-19	\N	Y
1000104	1000004	1000677	1000016	f6800247-f2e1-41c6-9f07-744ee40d481a	2024-09-19	1000069	2024-09-19	\N	Y
1000105	1000004	1000678	1000016	a26d62d3-ba4f-4d3b-98a5-01cd3a8b433f	2024-09-19	1000069	2024-09-19	\N	Y
1000106	1000004	1000679	1000016	dc168587-97f2-45f8-be4e-dc4195d98cf0	2024-09-19	1000069	2024-09-19	\N	Y
\.


--
-- TOC entry 5474 (class 0 OID 392842)
-- Dependencies: 333
-- Data for Name: d_attribute; Type: TABLE DATA; Schema: pos; Owner: adempiere
--

COPY pos.d_attribute (d_attribute_id, d_tenant_id, created, created_by, updated, updated_by, is_active, code, d_attribute_uu, d_org_id) FROM stdin;
1000002	1000004	2024-08-27	1000069	2024-08-27	1000069	Y	Size1	dcc75129-4418-4902-999d-d49055cc7c63	0
1000001	1000004	2024-08-27	1000069	2024-08-27	1000069	Y	Size	514724e9-531a-477c-8855-07e57597173b	0
1000004	1000004	2024-08-29	1000069	2024-08-29	1000069	Y	Size2	b6c02f7f-bd14-4f10-9c40-b18bb78bda0f	0
1000003	1000004	2024-08-29	1000069	2024-08-29	1000069	Y	Kích Thước	a599c891-e6d4-431b-a094-14f293c3cb22	0
1000005	1000004	2024-08-29	1000069	2024-08-29	1000069	Y	Size1	42389216-df52-4861-a744-5aa8eef3f9ba	1000016
1000006	1000004	2024-08-29	1000069	2024-08-29	1000069	Y	Size	b91de280-18d8-4585-8137-5f2e63a540a4	1000016
1000007	1000004	2024-09-05	1000069	2024-09-05	1000069	Y	Size2	ca11f797-a51d-4000-9318-b087e9fd82ef	1000016
1000008	1000004	2024-09-05	1000069	2024-09-05	1000069	Y	Size2	457d39a9-2fe8-41c6-90a2-3e5dc3b166ad	1000016
1000009	1000009	2024-09-12	1000071	2024-09-12	1000071	Y	Sóng	d24057a5-e70f-4534-973f-40528feb99c7	1000022
1000010	1000009	2024-09-12	1000071	2024-09-12	1000071	Y	Độ dày	9db6761e-3bf7-4349-8b57-a382288c15ed	1000022
\.


--
-- TOC entry 5492 (class 0 OID 393182)
-- Dependencies: 351
-- Data for Name: d_attribute_value; Type: TABLE DATA; Schema: pos; Owner: adempiere
--

COPY pos.d_attribute_value (d_attribute_value_id, d_tenant_id, created, created_by, updated, updated_by, is_active, value, name, d_attribute_uu, d_org_id, d_attribute_id) FROM stdin;
1000001	1000004	2024-08-29	1000069	2024-08-29	1000069	Y	M	\N	cf396ce4-d727-4c36-a575-5910a870f6c0	0	1000001
1000000	1000004	2024-08-29	1000069	2024-08-29	1000069	Y	S	\N	a0929bfd-07fb-4a58-b5b1-2091629eb4ef	0	1000001
\.


--
-- TOC entry 5494 (class 0 OID 393235)
-- Dependencies: 353
-- Data for Name: d_bank; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_bank (d_bank_id, d_tenant_id, d_org_id, name, description, bin_code, swift_code, d_image_id, created, created_by, updated, updated_by, d_bank_uu, is_active) FROM stdin;
1000003	1000004	0	Ngân hàng TMCP Đầu tư và Phát triển Việt Nam	\N	970418	BIDVVNVX	\N	2024-09-09 07:05:47.133238	0	2024-09-09 07:05:47.133238	0	948a3e54-70a5-47f9-92a0-d54caf093944	Y
1000004	1000004	0	Ngân hàng TMCP Kỹ thương Việt Nam	970407	970407	VTCBVNVX	\N	2024-09-09 07:07:17.046737	0	2024-09-09 07:07:17.046737	0	fea7892a-8d64-405d-85ea-c77b8b8727f9	Y
1000002	1000004	0	Ngân hàng TMCP Ngoại Thương Việt Nam	970436	970436	BFTVVNVX	\N	2024-09-09 07:04:43.641768	0	2024-09-09 07:04:43.641768	0	10da6fb2-cd33-4fec-baf9-7e46dd634e4b	Y
1000001	1000004	0	Ngân hàng thương mại cổ phần Quân đội	970422	970422	MSCBVNVX	\N	2024-09-09 00:06:58.36863	1000069	2024-09-09 00:06:58.36863	1000069	ddae593a-79a5-4363-8f6e-3ea4f42ee6cc	Y
1000005	1000004	0	Ngân hàng TMCP Công thương Việt Nam	\N	970415	ICBVVNVX	\N	2024-09-10 03:51:52.358861	0	2024-09-10 03:51:52.358861	0	36fec6b2-e843-4348-b02e-5e5f897cba4c	Y
1000006	1000004	0	Ngân hàng Nông nghiệp và Phát triển Nông thôn Việt Nam	\N	970405	VBAAVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	79fc1119-7a0a-4795-a5df-fc16d0eb3a60	Y
1000007	1000004	0	Ngân hàng TMCP Phương Đông	\N	970448	ORCOVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	e093e23b-8193-449f-8db4-0be7c9dcb33f	Y
1000008	1000004	0	Ngân hàng TMCP Á Châu	\N	970416	ASCBVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	16def695-87ad-4445-a556-47d33524436b	Y
1000009	1000004	0	Ngân hàng TMCP Việt Nam Thịnh Vượng	\N	970432	VPBKVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	37d03e3f-2ad8-4798-88e9-96566e9e137e	Y
1000010	1000004	0	Ngân hàng TMCP Tiên Phong	\N	970423	TPBVVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	080b1a71-26a3-4ef5-88e5-3734b1a8f40d	Y
1000011	1000004	0	Ngân hàng TMCP Sài Gòn Thương Tín	\N	970403	SGTTVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	5ca607c5-eebb-40b4-896b-0e9619b4466c	Y
1000012	1000004	0	Ngân hàng TMCP Phát triển Thành phố Hồ Chí Minh	\N	970437	HDBCVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	7d78072d-aecc-42e9-a500-348739c695fe	Y
1000013	1000004	0	Ngân hàng TMCP Bản Việt	\N	970454	VCBCVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	308a1d8e-4ba5-4a7e-b87a-47d506e122ee	Y
1000014	1000004	0	Ngân hàng TMCP Sài Gòn	\N	970429	SACLVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	206a5f09-53e0-4270-a36b-b53e1f3824e3	Y
1000015	1000004	0	Ngân hàng TMCP Quốc tế Việt Nam	\N	970441	VNIBVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	4d14e695-725f-495f-bf22-4aff504fb82d	Y
1000016	1000004	0	Ngân hàng TMCP Sài Gòn - Hà Nội	\N	970443	SHBAVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	3690e4e2-224f-4eca-a84a-11bc0772a3c4	Y
1000017	1000004	0	Ngân hàng TMCP Xuất Nhập khẩu Việt Nam	\N	970431	EBVIVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	f85dd136-affb-47fc-b499-a07e6714ec10	Y
1000018	1000004	0	Ngân hàng TMCP Hàng Hải	\N	970426	MCOBVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	dc67292a-7acd-457c-a860-35f79fc3c8a4	Y
1000019	1000004	0	TMCP Việt Nam Thịnh Vượng - Ngân hàng số CAKE by VPBank	\N	546034	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	2f3d88d7-5fdf-4d54-9b84-7fb9ca2329f6	Y
1000020	1000004	0	TMCP Việt Nam Thịnh Vượng - Ngân hàng số Ubank by VPBank	\N	546035	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	28d8d6f2-03d0-4dd0-951a-f3ba5f9f9311	Y
1000021	1000004	0	Ngân hàng số Timo by Ban Viet Bank (Timo by Ban Viet Bank)	\N	963388	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	c789deed-8e1f-4f63-9994-e1ba01b64079	Y
1000022	1000004	0	Tổng Công ty Dịch vụ số Viettel - Chi nhánh tập đoàn công nghiệp viễn thông Quân Đội	\N	971005	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	3c290458-25cf-4c4f-a81a-d088d3c2f359	Y
1000023	1000004	0	VNPT Money	\N	971011	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	33048ea6-e84d-4bf5-b6f6-700890fe4021	Y
1000024	1000004	0	Ngân hàng TMCP Sài Gòn Công Thương	\N	970400	SBITVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	c0a34d64-62c3-41b7-8311-1093da7b19c7	Y
1000025	1000004	0	Ngân hàng TMCP Bắc Á	\N	970409	NASCVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	cec57356-de51-4005-9898-04cbf4737286	Y
1000026	1000004	0	Ngân hàng TMCP Đại Chúng Việt Nam	\N	970412	WBVNVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	49cbed90-b7ae-4fb0-b1d8-240d5ca84144	Y
1000027	1000004	0	Ngân hàng Thương mại TNHH MTV Đại Dương	\N	970414	OCBKUS3M	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	e0bb4fff-7de3-47e7-be57-46bbaed26fa6	Y
1000028	1000004	0	Ngân hàng TMCP Quốc Dân	\N	970419	NVBAVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	99e10b0f-24be-4bfa-b546-57ac8b5eefdf	Y
1000029	1000004	0	Ngân hàng TNHH MTV Shinhan Việt Nam	\N	970424	SHBKVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	b0b04b97-7c62-4cc3-8930-b706fe8b29c3	Y
1000030	1000004	0	Ngân hàng TMCP An Bình	\N	970425	ABBKVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	550ab041-27f5-4d79-80b9-c7f124335ab7	Y
1000031	1000004	0	Ngân hàng TMCP Việt Á	\N	970427	VNACVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	3da9de86-7226-45dd-8b5e-d610ad496e3e	Y
1000032	1000004	0	Ngân hàng TMCP Nam Á	\N	970428	NAMAVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	1451ddb5-66b5-4a99-a479-8a76d59d922a	Y
1000033	1000004	0	Ngân hàng TMCP Xăng dầu Petrolimex	\N	970430	PGBLVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	d1d54505-1db7-4472-86f0-9e74d18f2a8a	Y
1000034	1000004	0	Ngân hàng TMCP Việt Nam Thương Tín	\N	970433	VNTTVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	12a89b79-4d33-413e-bc57-d42570d6510b	Y
1000035	1000004	0	Ngân hàng TMCP Bảo Việt	\N	970438	BVBVVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	4251b3ad-19aa-4a70-b33f-cad01ecf4318	Y
1000036	1000004	0	Ngân hàng TMCP Đông Nam Á	\N	970440	SEAVVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	54360eb4-8b44-40b9-ac6f-fa2129c700f0	Y
1000037	1000004	0	Ngân hàng Hợp tác xã Việt Nam	\N	970446	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	8701e85e-b8be-4b78-94ae-288c871f34de	Y
1000038	1000004	0	Ngân hàng TMCP Lộc Phát Việt Nam	\N	970449	LVBKVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	fd44cac6-e633-4c75-92f8-3b74fc2ed58c	Y
1000039	1000004	0	Ngân hàng TMCP Kiên Long	\N	970452	KLBKVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	a3d618d9-932f-4b21-a3e4-9468ffbe025b	Y
1000040	1000004	0	Ngân hàng Đại chúng TNHH Kasikornbank	\N	668888	KASIVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	065c51e2-171c-4fed-bed5-55f50b4cd355	Y
1000041	1000004	0	Ngân hàng Kookmin - Chi nhánh Hà Nội	\N	970462	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	afe97236-51f6-4159-9c80-af6677883702	Y
1000042	1000004	0	Ngân hàng KEB Hana – Chi nhánh Thành phố Hồ Chí Minh	\N	970466	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	be2e3c00-7e36-45eb-b6e8-6b64b8fa5202	Y
1000043	1000004	0	Ngân hàng KEB Hana – Chi nhánh Hà Nội	\N	970467	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	7731bbcb-6bfe-442b-b9fa-7d8c0e2aa8ae	Y
1000044	1000004	0	Công ty Tài chính TNHH MTV Mirae Asset (Việt Nam)	\N	977777	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	bf3da4b0-f013-4f4d-bcd2-163f739dc917	Y
1000045	1000004	0	Ngân hàng Citibank, N.A. - Chi nhánh Hà Nội	\N	533948	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	ed6a3ed0-191d-43de-9082-8c830f6ac245	Y
1000046	1000004	0	Ngân hàng Kookmin - Chi nhánh Thành phố Hồ Chí Minh	\N	970463	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	a55cc281-1815-48f1-88b1-e3ca236d4b63	Y
1000047	1000004	0	Ngân hàng Chính sách Xã hội	\N	999888	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	08bc96c6-f889-4167-93c1-a3dbdbf274bd	Y
1000048	1000004	0	Ngân hàng TNHH MTV Woori Việt Nam	\N	970457	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	f1188262-1de2-4e52-9a51-1a4bc959d3e0	Y
1000049	1000004	0	Ngân hàng Liên doanh Việt - Nga	\N	970421	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	97f7b0b3-c198-472d-bb30-f16e9add6891	Y
1000050	1000004	0	Ngân hàng United Overseas - Chi nhánh TP. Hồ Chí Minh	\N	970458	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	95ba567a-44a6-4c35-94e7-e13b65d663c3	Y
1000051	1000004	0	Ngân hàng TNHH MTV Standard Chartered Bank Việt Nam	\N	970410	SCBLVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	b5340ee7-014c-461f-9e2d-7f021a4fc98c	Y
1000052	1000004	0	Ngân hàng TNHH MTV Public Việt Nam	\N	970439	VIDPVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	d9d3a955-fe46-4881-93c8-cb4d841b283a	Y
1000053	1000004	0	Ngân hàng Nonghyup - Chi nhánh Hà Nội	\N	801011	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	76c6880f-83df-4405-a518-adb0972a9243	Y
1000054	1000004	0	Ngân hàng TNHH Indovina	\N	970434	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	2e9d8eb5-9755-4a68-b971-6208215d4dfc	Y
1000055	1000004	0	Ngân hàng Công nghiệp Hàn Quốc - Chi nhánh TP. Hồ Chí Minh	\N	970456	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	6dc5634f-a57b-44e1-b478-123c3c310f96	Y
1000056	1000004	0	Ngân hàng Công nghiệp Hàn Quốc - Chi nhánh Hà Nội	\N	970455	\N	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	1fa6bed0-73cf-4906-96a1-35761a6a52a9	Y
1000057	1000004	0	Ngân hàng TNHH MTV HSBC (Việt Nam)	\N	458761	HSBCVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	076f1b0a-a50b-453b-9711-b76f49bcae9e	Y
1000058	1000004	0	Ngân hàng TNHH MTV Hong Leong Việt Nam	\N	970442	HLBBVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	9021ea11-7c40-4a43-bb66-4ccc4547cac1	Y
1000059	1000004	0	Ngân hàng Thương mại TNHH MTV Dầu Khí Toàn Cầu	\N	970408	GBNKVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	29e7d293-f8fa-4aa3-9664-5176a003865c	Y
1000060	1000004	0	Ngân hàng TMCP Đông Á	\N	970406	EACBVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	4c805eaf-61e8-4e2b-9156-f4335f64da50	Y
1000061	1000004	0	DBS Bank Ltd - Chi nhánh Thành phố Hồ Chí Minh	\N	796500	DBSSVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	b4f50d56-b748-4609-ae34-658bb756e328	Y
1000062	1000004	0	Ngân hàng TNHH MTV CIMB Việt Nam	\N	422589	CIBBVNVN	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	068eef83-eb49-4cdf-9c3d-ee364a83aa06	Y
1000063	1000004	0	Ngân hàng Thương mại TNHH MTV Xây dựng Việt Nam	\N	970444	GTBAVNVX	\N	2024-09-10 04:15:35.581725	0	2024-09-10 04:15:35.581725	0	b15a0e4a-6cb5-49b1-ae73-f83d7cc70707	Y
1000064	1000004	0	Quỹ tiền mặt	\N	\N	\N	\N	2024-09-19 02:46:55.665321	0	2024-09-19 02:46:55.665321	0	78d3172e-9c2a-424a-8494-56ab76e43068	Y
\.


--
-- TOC entry 5496 (class 0 OID 393261)
-- Dependencies: 355
-- Data for Name: d_bankaccount; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_bankaccount (d_bankaccount_id, d_bank_id, d_tenant_id, d_org_id, account_no, description, name, is_default, bankaccount_type, created, created_by, updated, updated_by, d_bankaccount_uu, is_active) FROM stdin;
1000001	1000001	1000004	1000016	0121921		CTY DBIZ	N	CHE	2024-09-09 00:10:06.13349	0	2024-09-19 14:38:57.651304	1000069	025412cb-4279-43d5-b246-15fb49f96136	Y
\.


--
-- TOC entry 5468 (class 0 OID 390960)
-- Dependencies: 327
-- Data for Name: d_cancel_reason; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_cancel_reason (d_cancel_reason_id, d_tenant_id, d_org_id, is_active, name, description, created, created_by, updated, updated_by, d_cancel_reason_uu) FROM stdin;
1000005	1000004	1000016	Y	Tôi muốn đi quán khác		2024-08-13 10:54:41.883003	1000069	2024-08-13 10:54:41.883003	1000069	220bdc43-79ee-4d84-8377-58175c5e4fca
1000011	1000004	1000016	Y	Món lên chậm	\N	2024-08-29 11:42:48.722403	1000069	2024-08-29 11:42:48.722404	1000069	5b5320e8-21b4-47e6-b3d8-fc1248d3371b
1000012	1000004	1000016	Y	Hết nguyên liệu rồii	\N	2024-08-29 11:44:53.775779	1000069	2024-08-29 11:44:53.77578	1000069	79bf8298-6976-43cc-8794-57e1a7655428
1000013	1000009	1000016	Y	Tôi mệt muốn về nhà		2024-09-16 10:07:38.345766	1000071	2024-09-16 10:07:38.345766	1000071	4dafe017-6d64-435b-a0a9-31296d95f227
\.


--
-- TOC entry 5365 (class 0 OID 385481)
-- Dependencies: 224
-- Data for Name: d_changelog; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_changelog (d_changelog_id, d_tenant_id, table_name, old_value, new_value, created, created_by, updated, updated_by, d_changelog_uu, entity_id) FROM stdin;
\.


--
-- TOC entry 5366 (class 0 OID 385491)
-- Dependencies: 225
-- Data for Name: d_config; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_config (d_config_id, d_tenant_id, d_org_id, name, value, description, created, created_by, updated_by, updated, d_config_uu, is_active) FROM stdin;
1000000	0	0	D_MBB_URL_PRE	https://oil.digitalbiz.com.vn/		2024-09-09 01:04:58.939128	0	0	2024-09-09 01:04:58.939128	e1780624-66d8-4b61-9a2d-69e9459febee	Y
1000001	0	0	D_MBB_URL_CREATEQR	payment/createqr/v1.0		2024-09-09 01:06:09.497997	0	0	2024-09-09 01:06:09.497997	16ebd5cc-6917-42a0-be04-9ba12c1d0716	Y
1000002	0	0	D_MBB_URL_GETTOKEN	token/v1		2024-09-09 01:06:20.422088	0	0	2024-09-09 01:06:20.422088	ad73f958-cd74-4855-9eac-7ea38356812d	Y
1000003	0	0	D_MBB_TOKEN_AUTHOR	Basic NVAwdEFtdlpVb2I5MWxqaEZWWXA5OTBkbXA0c2thV3g6aDFwNUgyVEphdmNNQVdmRQ==		2024-09-09 01:06:28.132357	0	0	2024-09-09 01:06:28.132357	efedc5f9-b6ff-49ad-b6f8-77f68f366abe	Y
1000004	0	0	MDM_URL_SAVE_IMAGE	https://apim.digitalbiz.com.vn:8243/digitalasset/insertupdate/v1	\N	2024-09-16 03:05:53.008648	0	0	2024-09-16 03:05:53.008648	9016d126-4a62-4d24-91bf-a877ca172ed4	Y
1000005	0	0	D_MBB_KeyCheckSum	SsgAccesskey		2024-09-17 16:24:33.451632	0	0	2024-09-17 16:24:33.451632	9733001a-9bec-4f9f-9129-f9acdff679aa	Y
1000006	0	0	D_MBB_URL_CHECK_ORDER	https://oil.digitalbiz.com.vn/payment/checkorder/v1.0		2024-09-17 18:22:43.417318	0	0	2024-09-17 18:22:43.417318	6697d81d-a83c-48fe-867b-40de121c3781	Y
\.


--
-- TOC entry 5478 (class 0 OID 392910)
-- Dependencies: 337
-- Data for Name: d_coupon; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_coupon (d_coupon_id, d_tenant_id, d_org_id, code, balance_amount, description, d_pos_terminal_id, is_available, d_vendor_id, d_customer_id, erp_coupon_id, created, created_by, updated, updated_by, d_coupon_uu, is_active) FROM stdin;
\.


--
-- TOC entry 5367 (class 0 OID 385501)
-- Dependencies: 226
-- Data for Name: d_currency; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_currency (d_currency_id, d_tenant_id, d_org_id, currency_code, description, standard_precision, created, created_by, updated, updated_by, d_currency_uu) FROM stdin;
1000000	1000004	1000016	VND	Dong	0	2024-08-17 12:23:06.712676	0	2024-08-17 12:23:06.712676	0	0c989d62-fb89-4b4d-af35-59753e0dde5c
\.


--
-- TOC entry 5368 (class 0 OID 385512)
-- Dependencies: 227
-- Data for Name: d_customer; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_customer (d_customer_id, d_tenant_id, code, name, phone1, phone2, address1, address2, customer_point, tax_code, email, debit_amount, company, birthday, d_image_id, created, created_by, updated, updated_by, is_active, d_customer_uu, is_customer_type, area, wards, d_partner_group_id, gender, description, discount, erp_customer_id) FROM stdin;
1000062	1000004	HIENHN	Nguyễn Hiếu Hiền	123123	0	Thủ Đức	456 Elm Street	1500	01626816	hienhn@digitalbiz.com.vn	5000	DigitalBiz	\N	\N	2024-08-15	1000069	2024-08-15	\N	Y	bb1005de-df56-426a-ba7a-2ac228a550fc	\N	\N	\N	\N	\N	\N	\N	\N
1000085	1000004	CUS1000084	Nguyễn Chí THanh	0384491	0	Thủ Đức	456 Elm Street	1500	01626816	thanhnc@digitalbiz.com.vn	5000	DigitalBiz	2001-06-03	\N	2024-08-18	1000069	2024-08-18	\N	Y	8c31524c-9285-4e43-beda-034b1174a828	\N	Sài Gòn - Thủ Đức	THủ Đức	1000021		\N	\N	\N
1000032	1000002	123	anhtu3	0898449505	\N	\N	\N	\N	ádf	\N	0	Y	\N	\N	2024-07-31	1000037	2024-07-31	\N	Y	5e1a921e-86b5-4647-a2a3-6e126fccbaad	Y	\N	\N	\N	M	\N	\N	\N
1000087	1000004	CUS1000086	Nguyễn Lệ Nam	0327666766	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2024-09-06	1000069	2024-09-06	\N	Y	ebd5d4d1-c6db-4ce8-8132-3e9f28b55c9b	\N	\N	\N	1000042	F	\N	\N	\N
1000079	1000004	TUHA	Hà Anh 	0237272723	\N	Tân Bình kkk	\N	\N	01626816	tennld@digitalbiz.com.vn	0	\N	2001-06-06	1000312	2024-09-06	1000069	2024-09-06	1000069	Y	254e3e06-4d79-4922-85e0-1d88a91b59b3	\N	Sài Gòn - Tân Bình	Dĩ An	1000022	M	wef	\N	\N
1000000	1000002	NCT	Than H	431	\N	\N	\N	\N	123	chithanh03062001@gmail.com	0	Y	\N	\N	2024-07-11	0	2024-07-11	0	Y	5ea93194-4bb2-4aaf-9667-38d55a4abb02	Y	\N	\N	\N	\N	\N	\N	\N
1000029	1000002	ABC123	John Doe123	2	987654321	123 Main Street	456 Elm Street	1500	123456789	johndoe@example.com	1000.50	Y	1985-01-01	1000169	2024-07-31	1000037	2024-07-31	\N	Y	3e190cd4-3a68-470f-bc89-17d94879ff4b	Y	\N	\N	\N	\N	\N	\N	\N
1000002	1000002	ABC123	John Doe12	0	987654321	123 Main Street	456 Elm Street	1500	123456789	johndoe@example.com	1000.50	Y	1985-01-01	1000029	2024-07-12	1000037	\N	\N	Y	ee70899a-587b-43c4-8e52-3cab0ce4ba12	Y	\N	\N	\N	\N	\N	\N	\N
1000055	1000004		Tuyết Như	0326647677	\N	\N	\N	\N		\N	0	\N	\N	1000317	2024-09-06	1000069	2024-09-06	1000069	Y	c6c09c74-1e20-4acb-a7cc-253949889e59	N	\N	\N	1000026	F		\N	\N
1000010	1000002	ABC123	John Doe213	1	987654321	123 Main Street	456 Elm Street	1500	123456789	johndoe@example.com	1000.50	Y	1985-01-01	1000134	2024-07-20	1000037	2024-07-20	\N	Y	c44b21d0-560a-4bad-91d0-62fd8a89cbe4	Y	\N	\N	\N	\N	\N	\N	\N
1000033	1000002	1234	anhtu2	089844911	\N	\N	\N	\N	123345	\N	0	Y	\N	\N	2024-07-31	1000037	2024-07-31	\N	Y	350e6f09-41fb-4d56-b546-e55f70f12fc8	Y	\N	\N	\N	M	\N	\N	\N
1000034	1000002	tu3	tu3	0898449112	\N	\N	\N	\N	123456	\N	0	Y	\N	\N	2024-07-31	1000037	2024-07-31	\N	Y	865f9d4f-5023-4f64-8985-e214bb24f5ef	Y	\N	\N	\N	M	\N	\N	\N
1000030	1000002	\N	John Doe421	121243222	987654321	123 Main Street	456 Elm Street	1500	123456789	johndoe@example.com	1000.50	Y	1985-01-01	1000170	2024-07-31	1000037	2024-07-31	\N	Y	b4943828-cf4e-429b-baa3-9d3477d00dc0	Y	\N	\N	\N	\N	\N	\N	\N
1000095	1000009	1000004	Nguyễn Văn A	0971902294	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2024-09-12	1000071	2024-09-12	1000071	Y	f9fceb5f-c452-4ba1-8ce6-0edcc1e690bc	\N	\N	\N	1000060	M	\N	\N	\N
1000096	1000009	1000005	Nguyễn Thị Hồng	0971902291	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2024-09-12	1000071	2024-09-12	1000071	Y	b80643f9-0113-4f1e-bef8-ff4e0645b627	\N	\N	\N	1000060	M	\N	\N	\N
1000082	1000004	TUHA	Hà Anh Tú 5	222	\N	Tân Bình kkk	\N	\N	01626816	tennld@digitalbiz.com.vn	0	\N	2001-06-03	\N	2024-08-15	1000069	2024-08-15	\N	Y	488bfc77-d0d6-41e8-93a2-82b43f80d222	\N	Sài Gòn - Tân Bình	Dĩ An	1000022	M	\N	\N	\N
1000003	1000002	ABC123	John Doe	123214	987654321	123 Main Street	456 Elm Street	1500	123456789	johndoe@example.com	1000.50	Y	1985-01-01	1000031	2024-07-12	1000037	\N	\N	Y	9c4068f2-18f8-43e6-a1f2-907ea322c407	Y	\N	\N	\N	\N	\N	\N	\N
1000009	1000002	ABC123	John Doe111	3	987654321	123 Main Street	456 Elm Street	1500	123456789	johndoe@example.com	1000.50	Y	1985-01-01	1000116	2024-07-17	1000037	2024-07-17	\N	Y	e720f792-e960-4453-87a3-1e634435a820	Y	\N	\N	\N	\N	\N	\N	\N
1000035	1000002	tu4	tu4	08984442	\N	\N	\N	\N	123456	\N	0	Y	\N	1000171	2024-07-31	1000037	2024-07-31	\N	Y	619beb3c-83b6-4a60-badf-8df22e2e9163	Y	\N	\N	\N	M	\N	\N	\N
1000031	1000002	\N	anhtu	0898441	\N	\N	\N	\N	123	\N	0	Y	\N	\N	2024-07-31	1000037	2024-07-31	\N	Y	f9441e94-07a7-40d3-87a3-367138aec282	Y	\N	\N	\N	M	\N	\N	\N
1000036	1000002	123	123	123	\N	\N	\N	\N	\N	\N	0	Y	\N	\N	2024-08-02	1000037	2024-08-02	\N	Y	b6001ba8-88b1-44c3-a608-2ebd7833d1a7	Y	\N	\N	1000019	M	\N	\N	\N
1000081	1000004	TUHA	Hà Anh	0123456789	\N	Tân Bình kkk	\N	\N	01626816	tennld@digitalbiz.com.vn	0	\N	2001-06-03	1000282	2024-09-06	1000069	2024-09-06	1000069	Y	34b4ac87-384c-4e7e-bd42-8751178b1657	\N	Sài Gòn - Tân Bình	Dĩ An	1000022	M	\N	\N	\N
1000094	1000009	1000003	Lầu Sẹc Dần	0971902299	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2024-09-11	1000071	2024-09-11	1000071	Y	0b57763d-6300-4f9a-9e12-c8f19f582f1a	\N	\N	\N	1000060	\N	\N	\N	\N
1000090	1000009	10000001	DBIZ	0971902297	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2024-09-11	1000071	2024-09-11	1000071	Y	49f87e90-9a82-4045-b69c-8634ef0ca74b	\N	\N	\N	1000063	M	\N	\N	\N
1000093	1000009	1000002	SSG	0971902298	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2024-09-11	1000071	2024-09-11	1000071	Y	d3ca8f13-3781-4dea-a7c1-7c624fe1bc11	\N	\N	\N	1000063	M	\N	\N	\N
1000061	1000004	HIENHN	Nguyễn Hiếu Hiền	5454	0	Thủ Đức	456 Elm Street	1500	01626816	hienhn@digitalbiz.com.vn	5000	DigitalBiz	2001-06-03	1000273	2024-08-15	1000069	2024-08-15	\N	Y	a7da6805-e5e1-472f-89da-8d001b47188b	\N	\N	\N	1000021	M	\N	\N	\N
1000044	1000004	DANLS	Lầu Sẹc Dần 	45123	0	Thủ Đức	456 Elm Street	1500	01626816	danls@digitalbiz.com.vn	5000	DigitalBiz	2001-06-03	\N	2024-08-07	1000069	2024-08-15	1000069	Y	c73898e9-8e63-4dfa-94d3-a3d1cb86bf23	Y	Sài Gòn - Thủ Đức	THủ Đức	1000021	F		\N	\N
1000042	1000004	HUNGNMM	Nguyễn Minh Hưngg	3212	0	Linh Xuân Thủ Đức	456 Elm Street	1500	016268167	hunglm@digitalbiz.com.vnN	5000	DigitalBiz	2001-06-03	\N	2024-08-07	1000069	2024-08-15	1000069	Y	131e1284-99ec-4579-b91b-3e133ac743f6	N	Sài Gòn - Thủ Đức	Linh Xuân	1000022	M		\N	\N
1000040	1000004	TUHA	Hà Anh Tú 1	12312	0	Tân Bình kkk	456 Elm Street	1500	01626816	tennld@digitalbiz.com.vn	5000	DigitalBiz	2001-06-03	1000265	2024-08-07	1000069	2024-08-15	1000069	Y	3725e564-1c3c-4d7a-96fc-ee59615a784c	N	Sài Gòn - Tân Bình	Dĩ An	1000022	M		\N	\N
1000039	1000004	TIENNLD	Nguyễn Lê Duy Tiến	1212	0	Dĩ AN BÌnh DƯơng	456 Elm Street	1500	01626816	tennld@digitalbiz.com.vn	5000	DigitalBiz	2001-06-03	\N	2024-08-07	1000069	2024-08-07	1000069	Y	e075fe11-bc06-4e54-8f70-8e02ac2bbcf4	N	Sài Gòn - Thủ Đức	Dĩ An	1000021	M		\N	\N
1000007	1000002	ABC123	ưerwerew	123456789	987654321	123 Main Street	456 Elm Street	1500	123456789	johndoe@example.com	1000.50	Y	1985-01-01	1000053	2024-07-12	1000037	2024-07-12	\N	Y	8adbca53-48c1-4994-bc0a-1b2d6f82eced	Y	\N	\N	\N	\N	\N	\N	\N
1000037	1000004	THANHNC	Nguyễn Chí Thanh 1	0384449114	0	Số 10 đường số 1 khu phố 5	45	1500	01626816	thanhnc@digitalbiz.com.vn	5000	DigitalBiz	2001-06-03	1000276	2024-08-07	1000069	2024-08-15	1000069	Y	388544c9-f3bd-4760-9490-a0376c2a527e	Y	Sài Gòn - Thủ Đức	Hiệp Bình chánh	1000021	M		\N	\N
\.


--
-- TOC entry 5369 (class 0 OID 385528)
-- Dependencies: 228
-- Data for Name: d_doctype; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_doctype (d_doctype_id, d_tenant_id, code, name, created, created_by, updated, updated_by, d_doctype_uu) FROM stdin;
1000000	1000004	KITCHEN_ORDER	Kitchen Order	2024-08-17 11:45:03.580072	0	2024-08-17 11:45:03.580072	0	2f801d53-e82a-4902-9843-c2e4f06a360c
1000001	1000004	PRODUCTION	Production	2024-09-08 17:34:03.447374	0	2024-09-08 17:34:03.447374	0	98c84f11-8411-4d41-8672-e308e1a66527
1000002	1000004	POS_ORDER	POS ORDER	2024-09-09 07:43:43.835164	0	2024-09-09 07:43:43.835164	0	d49b4f9a-7932-48c7-bf65-4703da4622d9
1000003	1000004	AR_INVOICE	AR Invoice	2024-09-09 11:09:56.291993	0	2024-09-09 11:09:56.291993	0	8dd54249-347e-4e42-b301-ffd79cd1682d
1000004	1000004	AR_PAYMENT	AR Payment	2024-09-09 11:09:56.291993	0	2024-09-09 11:09:56.291993	0	ae24a20b-5d0b-4856-8c3f-82fc9e996a71
1000005	1000004	PO_ORDER	Purchase Order	2024-09-11 09:52:54.781867	0	2024-09-11 09:52:54.781867	0	3b56b489-7edc-4735-8b3d-6402d494c08e
1000006	1000004	REQUEST_ORDER	Request Order	2024-09-17 04:27:07.924916	0	2024-09-17 04:27:07.924916	0	04e77c82-b93f-42aa-ad3d-b7c743e2a872
\.


--
-- TOC entry 5498 (class 0 OID 393356)
-- Dependencies: 357
-- Data for Name: d_erp_integration; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_erp_integration (d_erp_integration_id, d_tenant_id, d_org_id, erp_platform, erp_url, ad_client_id, ad_org_id, ad_role_id, m_warehouse_id, username, password, description, is_default, bankaccount_type, created, created_by, updated, updated_by, d_erp_integration_uu, is_active) FROM stdin;
1000010	1000004	0	Idempiere	https://apim.digitalbiz.com.vn:8243/t/onsenfuji.com.vn	1000066	0	1000707	0	Webservice	Webservice		Y	C	2024-09-18 13:55:09.126914	1000069	2024-09-18 13:55:09.126917	1000069	1df48648-b5b0-4ec2-82a7-a620f68ece47	Y
\.


--
-- TOC entry 5370 (class 0 OID 385535)
-- Dependencies: 229
-- Data for Name: d_expense; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_expense (d_expense_id, d_expense_category_id, d_tenant_id, expense_date, name, payment_method, amount, document_no, description, created, created_by, updated, updated_by, d_expense_uu) FROM stdin;
\.


--
-- TOC entry 5371 (class 0 OID 385546)
-- Dependencies: 230
-- Data for Name: d_expense_category; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_expense_category (d_expense_category_id, name, description, d_tenant_id, created, created_by, updated, updated_by, d_expense_category_uu) FROM stdin;
\.


--
-- TOC entry 5372 (class 0 OID 385554)
-- Dependencies: 231
-- Data for Name: d_floor; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_floor (d_floor_id, d_org_id, d_tenant_id, created, created_by, updated, updated_by, is_active, floor_no, name, description, d_floor_uu, display_index, d_pos_terminal_id) FROM stdin;
999957	1000005	1000002	2024-07-17 10:42:38.676678	1000037	2024-07-17 10:42:38.676678	1000037	Y	F019	F 1 2	no	a7904ae2-5f05-4eb9-9312-88bb39f29527	\N	\N
999966	1000003	1000002	2024-07-23 11:07:11.752786	1000037	2024-07-23 11:07:11.752787	1000037	Y	F03	F 3	no	ba76207d-e72e-48ed-9118-4052cfbd0fcf	\N	\N
999995	1000019	1000004	2024-09-19 09:20:28.928579	1000069	2024-09-19 09:47:31.870353	1000069	N	12312	Khu vực 10111ab	dsfsdaf	d91a05bf-b45e-4d89-a26d-036978e6dd5f	\N	\N
999988	1000019	1000004	2024-09-19 08:17:54.946735	1000069	2024-09-19 08:17:54.946735	1000069	Y	92	Khu vực 1	123	9a55d316-043d-4614-a252-09618354581a	0	\N
999975	1000016	1000004	2024-08-07 09:21:28.028374	1000069	2024-09-16 10:29:32.435136	1000069	N	F04	Khu vực 04	 Tầng lớn	5662290d-2eab-45f0-abb0-199f0c739fb9	0	\N
999977	1000016	1000004	2024-08-14 15:46:09.135348	1000069	2024-09-13 14:13:19.642073	1000069	Y	F06	Khu vực 053	 	1e750f76-a88f-4011-ba85-0d91928aa57f	\N	\N
999976	1000016	1000004	2024-08-07 09:22:07.855007	1000069	2024-09-18 11:24:08.299131	1000069	Y	F05	Khu vực 051	 Tầng to	8d57d092-7e9f-49e9-8a6f-a1ee74042e84	0	\N
999971	1000003	1000002	2024-07-24 15:10:26.004022	1000037	2024-07-24 15:10:26.004023	1000037	Y	F05	F5 1	Tần trống	5cf07d01-7059-478e-8916-bc02ff8cebda	\N	\N
999974	1000016	1000004	2024-08-07 09:20:51.79403	1000069	2024-08-14 11:23:51.206147	1000069	Y	F03	Khu vực 03	Tầng dành cho vip	de0712ff-75e2-473b-aceb-fead5bacd860	1	\N
999970	1000003	1000002	2024-07-24 15:02:39.784176	1000037	2024-07-24 15:02:39.784178	1000037	Y	F04	F4 1	Tang trong	de53ca0d-95a4-4179-842b-3e299c7e1b8e	\N	\N
999953	1000005	1000002	2024-07-17 10:11:55.903075	1000037	2024-07-17 10:11:55.903075	1000037	Y	01	Tầng 1 2	no	35bcb9e8-3f53-4952-993d-3eaeb15a8165	\N	\N
999968	1000003	1000003	2024-07-23 23:48:25.294976	1000068	2024-07-23 23:48:25.294976	1000068	Y	F02	F 1 2 3	no	539a0c74-eb66-4bef-9d11-dce7098a81b7	\N	\N
999956	1000005	1000002	2024-07-17 10:41:55.208227	1000037	2024-07-17 10:41:55.208227	1000037	Y	F01	Tầng 1 2 3	no	b1d03718-fd86-4517-8573-17c0272c924b	\N	\N
999978	1000030	1000004	2024-09-18 17:57:25.857493	1000069	2024-09-18 17:57:25.857493	1000069	Y	F01	Khu vực 01	 	bd7e46da-dc66-4528-a5c2-2333c09a2236	\N	\N
999973	1000016	1000004	2024-08-07 09:20:10.406461	1000069	2024-09-09 11:51:42.806811	1000069	Y	F021	Khu vực 02	 	4ce0a546-2f7d-4902-b0ef-043c7ea42ce7	1	\N
999972	1000016	1000004	2024-08-07 09:17:50.223201	1000069	2024-09-16 09:57:06.414335	1000069	Y	F011	Khu vực 01	 	4cb9d16a-d006-4437-bf75-7e8548a33ff6	\N	\N
999965	1000005	1000002	2024-07-23 11:06:52.055181	1000037	2024-07-23 11:06:52.055202	1000037	Y	F011	F 1 6	no	1b42868e-af7a-407f-a1cc-d284c66dcfac	\N	\N
999963	1000003	1000002	2024-07-22 04:51:52.682438	1000037	2024-07-22 04:51:52.682438	1000037	Y	F0122	F 1 4	no	3f57764f-c85c-4a5a-8f12-c68b170495bb	\N	\N
999962	1000005	1000002	2024-07-21 23:19:51.491064	1000037	2024-07-21 23:19:51.491064	1000037	Y	F015	F 1 3	no	c05b0a5f-92fe-4b08-8ec9-a9810ad5b44b	\N	\N
999982	1000030	1000004	2024-09-18 17:57:44.319429	1000069	2024-09-18 17:57:44.319429	1000069	Y	F026	Khu vực 02	 	748a8234-8138-4330-88f4-23f4d34d171f	\N	\N
999969	1000003	1000002	2024-07-24 15:02:00.915362	1000037	2024-07-24 15:02:00.915364	1000037	Y	F025	F2 1	tầng trống	f78fe176-64ac-40ea-ad7f-9841e9a2a7e7	\N	\N
999964	1000003	1000002	2024-07-22 05:06:26.319524	1000037	2024-07-22 05:06:26.319524	1000037	Y	F023	F 1 5	no	1413050e-7fb7-44d1-8ca7-a75835c5dedf	\N	\N
999967	1000003	1000003	2024-07-23 23:48:20.633139	1000068	2024-07-23 23:48:20.633139	1000068	Y	F022	F 1 2	no	b63bf998-8acb-44b7-bcda-c7ecd338da8e	\N	\N
999955	1000005	1000002	2024-07-17 10:15:27.923026	1000037	2024-07-17 10:15:57.688979	1000037	Y	016	Tầng 1	no 1	9d518cfd-d56f-4564-a031-88fad91717c7	\N	\N
1000012	1000016	1000004	2024-08-14 15:53:11.671368	1000069	2024-09-18 11:24:15.719937	1000069	N	F061	Khu vực 052	 	4ba48aac-6766-4fc2-bdd3-00e16cb6c72e	0	\N
\.


--
-- TOC entry 5373 (class 0 OID 385568)
-- Dependencies: 232
-- Data for Name: d_image; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_image (d_image_id, image_url, is_active, created, created_by, updated, updated_by, d_tenant_id, d_image_uu, image_code) FROM stdin;
1000278	https:	Y	2024-08-24 10:23:20.647405	1000069	2024-08-24 10:23:20.647405	1000069	1000004	bdb5f5bd-4d19-4032-b4fc-282da4509348	\N
1000073	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct?alt=media&token=2cccd589-a640-4642-a7e6-883631109781	Y	2024-07-15 00:03:12.449398	1000037	2024-07-15 00:03:12.449398	1000037	1000002	18fd9f0e-a6be-4024-ab3f-a76d28a12e98	\N
1000074	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct?alt=media&token=2cccd589-a640-4642-a7e6-883631109781	Y	2024-07-15 00:03:18.388541	1000037	2024-07-15 00:03:18.388541	1000037	1000002	76b51315-c578-43f5-811b-529a0898bbee	\N
1000075	http://example.com/image.jpg	Y	2024-07-15 00:12:31.375656	1000037	2024-07-15 00:12:31.375656	1000037	1000002	170f79a6-cb0d-41c6-92b5-0523e7be4a84	\N
1000282	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fcustomer%2Fda458900-65b9-11ef-9ca4-8f55da70b4d5?alt=media&token=9286eb1b-071f-4bfa-9cd7-1c76bfab83e2	Y	2024-08-29 10:50:38.555065	1000069	2024-08-29 10:50:38.555067	1000069	1000004	65a86654-3d7d-4090-adca-0c821da4a6f5	\N
1000283	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F6f54b4a0-6a68-11ef-8bff-5562517f218b?alt=media&token=e4ce39f1-8af3-429b-a6e9-57f527670e40	Y	2024-09-04 09:49:27.120252	1000069	2024-09-04 09:49:27.120253	1000069	1000004	a8722d62-ebca-42f8-abe3-3c82e9194ad2	\N
1000284	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F6f54b4a0-6a68-11ef-8bff-5562517f218b?alt=media&token=e4ce39f1-8af3-429b-a6e9-57f527670e40	Y	2024-09-04 09:49:27.141765	1000069	2024-09-04 09:49:27.141766	1000069	1000004	6977df72-f007-4e4f-b7dd-30c0f769c06c	\N
1000285	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.597226	1000069	2024-09-05 09:22:46.597228	1000069	1000004	e4bfd3a5-c8c5-4c56-aba1-0817a90fca85	\N
1000081	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F95cae910-42bd-11ef-ab9c-0b6385838227?alt=media&token=d451ad9a-e433-48bc-9e89-a9501939b3a1	Y	2024-07-15 22:19:12.411529	1000037	2024-07-15 22:19:12.411592	1000037	1000002	2b44040a-e9dd-40ab-930b-186234aaf904	\N
1000082	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe2817130-42c1-11ef-a75d-b1fc9c3dabda?alt=media&token=4d6f9f79-41f2-4f0a-9e96-72d4fa65bc3b	Y	2024-07-15 22:50:07.185278	1000037	2024-07-15 22:50:07.185279	1000037	1000002	c07109e9-114b-48d9-bb01-b62e311f4264	\N
1000091	http://example.com/image.jpg	Y	2024-07-15 21:36:53.57507	1000037	2024-07-15 21:36:53.57507	1000037	1000002	a4bc7b07-abe3-4744-ae81-c72b89773157	\N
1000092	http://example.com/image.jpg	Y	2024-07-16 09:13:59.47372	1000037	2024-07-16 09:13:59.47372	1000037	1000002	2b373661-641b-4e28-8d6a-af749e4baa90	\N
1000022	https:	Y	2024-07-12 01:27:06.237895	1000037	2024-07-12 01:27:06.237895	1000037	1000002	c7be4b3f-df1f-45ed-99ed-5f38d65b9957	\N
1000023	https:	Y	2024-07-12 01:41:00.316313	1000037	2024-07-12 01:41:00.316313	1000037	1000002	b93450bb-1842-4de9-9b60-9cb5eea4d95b	\N
1000024	https:	Y	2024-07-12 01:41:47.858138	1000037	2024-07-12 01:41:47.858138	1000037	1000002	52e4eba7-aad9-4f21-bfc6-5e11dbc8a04c	\N
1000028	http://example.com/image.jpg	Y	2024-07-12 12:07:02.846855	1000037	2024-07-12 12:07:02.846855	1000037	1000002	88b4f6a9-01fb-4b84-a9f7-bf8800873bc2	\N
1000029	http://example.com/image.jpg	Y	2024-07-12 12:17:17.570641	1000037	2024-07-12 12:17:17.570641	1000037	1000002	36ca91f5-2222-4b95-b511-bf2ef1938283	\N
1000031	http://example.com/image.jpg	Y	2024-07-12 02:50:26.613443	1000037	2024-07-12 02:50:26.613443	1000037	1000002	c19ba5a3-c13e-4376-82a8-69bebe09b5d1	\N
1000099	http://example.com/image.jpg	Y	2024-07-16 04:16:43.470328	1000037	2024-07-16 04:16:43.470328	1000037	1000002	c9eda593-5cfd-41e7-aa6b-b0b1a578862b	\N
1000104	http://example.com/image.jpg	Y	2024-07-16 04:40:01.886673	1000037	2024-07-16 04:40:01.886673	1000037	1000002	c125dd87-6766-4110-b9f3-3845ddafb3a5	\N
1000106	http://example.com/image.jpg	Y	2024-07-16 06:13:57.403418	1000037	2024-07-16 06:13:57.403418	1000037	1000002	7db3d314-7fac-4d1f-886c-b1fba1a4b21b	\N
1000107	http://example.com/image.jpg	Y	2024-07-16 06:21:46.843544	1000037	2024-07-16 06:21:46.844544	1000037	1000002	022bb570-24a6-48be-a5a6-cc733f850416	\N
1000043	http://example.com/image.jpg	Y	2024-07-12 16:03:39.982005	1000037	2024-07-12 16:03:39.982005	1000037	1000002	b730657f-da13-45b6-83fe-4b67e3a0b51c	\N
1000044	http://example.com/image.jpg	Y	2024-07-12 16:07:41.966692	1000037	2024-07-12 16:07:41.966692	1000037	1000002	b63802fd-64cb-4b28-a21d-8c9c8a819db7	\N
1000045	http://example.com/image.jpg	Y	2024-07-12 16:11:22.463406	1000037	2024-07-12 16:11:22.463406	1000037	1000002	d98cd0b3-af9c-45d1-9f7a-7a1a612f409a	\N
1000046	http://example.com/image.jpg	Y	2024-07-12 16:20:30.525207	1000037	2024-07-12 16:20:30.525207	1000037	1000002	08deba21-992c-4426-bddb-fa12900e31bc	\N
1000108	http://example.com/image.jpg	Y	2024-07-16 06:34:09.790553	1000037	2024-07-16 06:34:09.790553	1000037	1000002	77edb69a-a861-452a-aea4-bc795bab325d	\N
1000109	http://example.com/image.jpg	Y	2024-07-16 06:34:14.999277	1000037	2024-07-16 06:34:14.999277	1000037	1000002	9693236f-38e7-4827-9f37-d06c5e05f114	\N
1000110	http://example.com/image.jpg	Y	2024-07-17 11:47:45.563803	1000037	2024-07-17 11:47:45.563803	1000037	1000002	ccc7356d-3c3c-4b26-8d5e-a8b07adae0f9	\N
1000050	http://example.com/image.jpg	Y	2024-07-12 16:42:15.354124	1000037	2024-07-12 16:42:15.354126	1000037	1000002	3b060f86-0625-491e-877a-43d3b0f79105	\N
1000053	http://example.com/image.jpg	Y	2024-07-12 16:45:58.881554	1000037	2024-07-12 16:45:58.881555	1000037	1000002	d9e2ab53-136d-4c7c-8251-9627f7119667	\N
1000113	http://example.com/image.jpg	Y	2024-07-17 03:45:45.921983	1000037	2024-07-17 03:45:45.921983	1000037	1000002	00d035ca-e2d6-4335-a808-df71543adcb9	\N
1000114	http://example.com/image.jpg	Y	2024-07-17 03:46:00.072456	1000037	2024-07-17 03:46:00.072456	1000037	1000002	11e54bc0-4099-4f31-a16e-aea952e44307	\N
1000116	http://example.com/image.jpg	Y	2024-07-17 03:54:05.674986	1000037	2024-07-17 03:54:05.674986	1000037	1000002	159f114e-8205-4a32-ae22-93778280d329	\N
1000117	http://example.com/image.jpg	Y	2024-07-17 06:48:45.257576	1000037	2024-07-17 06:48:45.257576	1000037	1000002	2b82d283-34d3-48fc-b1b9-5722ec22bcf3	\N
1000118	http://example.com/image.jpg	Y	2024-07-17 06:49:46.863075	1000037	2024-07-17 06:49:46.863075	1000037	1000002	63a820cd-9aac-4839-87f9-c0fcf9e0c3c8	\N
1000119	http://example.com/image.jpg	Y	2024-07-17 06:50:24.438485	1000037	2024-07-17 06:50:24.438485	1000037	1000002	654dbf3e-22d5-46af-88ec-2b281144d6c2	\N
1000120	http://example.com/image.jpg	Y	2024-07-17 06:51:09.187505	1000037	2024-07-17 06:51:09.187505	1000037	1000002	3dc2d774-589a-4422-8629-4a6b8f84bf7c	\N
1000121	http://example.com/image.jpg	Y	2024-07-17 06:51:33.679513	1000037	2024-07-17 06:51:33.679513	1000037	1000002	6346b344-8fe9-4a03-87fb-3d476d313cc4	\N
1000122	http://example.com/image.jpg	Y	2024-07-17 06:55:14.777814	1000037	2024-07-17 06:55:14.777814	1000037	1000002	adcf9498-d990-4fd6-ab5d-6d7868f62735	\N
1000123	http://example.com/image.jpg	Y	2024-07-17 06:55:20.53815	1000037	2024-07-17 06:55:20.53815	1000037	1000002	fa82f8cd-91c4-43ac-b30c-345826074421	\N
1000124	http://example.com/image.jpg	Y	2024-07-17 06:55:27.667491	1000037	2024-07-17 06:55:27.667491	1000037	1000002	fbd004d0-6549-4dad-8c95-08c72c3e4113	\N
1000125	http://example.com/image.jpg	Y	2024-07-17 06:56:39.82245	1000037	2024-07-17 06:56:39.82245	1000037	1000002	11d76a31-db0c-4ff7-9c56-337ab4a6cfc9	\N
1000126	http://example.com/image.jpg	Y	2024-07-17 07:02:30.859301	1000037	2024-07-17 07:02:30.859301	1000037	1000002	e50e6325-631f-409d-86ef-7853278631c8	\N
1000127	http://example.com/image.jpg	Y	2024-07-17 07:04:53.794743	1000037	2024-07-17 07:04:53.794743	1000037	1000002	90113878-826d-4f45-8323-182d3252bdf0	\N
1000128	http://example.com/image.jpg	Y	2024-07-17 07:06:22.288671	1000037	2024-07-17 07:06:22.288671	1000037	1000002	d5f2a342-6a21-4caf-bff3-87d5f1ff36c4	\N
1000129	http://example.com/image.jpg	Y	2024-07-17 07:08:33.629326	1000037	2024-07-17 07:08:33.629326	1000037	1000002	66ee8e98-05f9-48b4-a990-6fc91cc15b68	\N
1000130	http://example.com/image.jpg	Y	2024-07-17 07:35:13.2141	1000037	2024-07-17 07:35:13.2141	1000037	1000002	4dae2cc3-66c5-457f-887a-433cf109c0cb	\N
1000131	http://example.com/image.jpg	Y	2024-07-18 01:28:49.824899	1000037	2024-07-18 01:28:49.824899	1000037	1000002	97a18bdc-d1a3-49ce-8d53-43cff0722007	\N
1000132	http://example.com/image.jpg	Y	2024-07-18 01:28:56.668496	1000037	2024-07-18 01:28:56.668496	1000037	1000002	eb13fc89-eab2-42ba-a62b-04363465c53d	\N
1000133	http://example.com/image.jpg	Y	2024-07-18 01:31:46.522515	1000037	2024-07-18 01:31:46.522515	1000037	1000002	626804b9-db00-4b24-ab3f-1263b336a9cb	\N
1000134	http://example.com/image.jpg	Y	2024-07-20 12:32:09.726722	1000037	2024-07-20 12:32:09.726722	1000037	1000002	9d16cb7f-9729-426d-a2ab-20a19d8b021f	\N
1000136	https:	Y	2024-07-20 15:39:46.790768	1000037	2024-07-20 15:39:46.790768	1000037	1000002	650cd288-8785-4a74-8177-1299ac72b912	\N
1000137	https:	Y	2024-07-20 15:42:18.939457	1000037	2024-07-20 15:42:18.939457	1000037	1000002	9d972d92-f1ad-4ea5-bb26-92104215a25e	\N
1000138	https:	Y	2024-07-20 15:45:34.378842	1000037	2024-07-20 15:45:34.378842	1000037	1000002	b4eb5b61-b839-46be-bb46-bb2dc2923f7b	\N
1000139	https:	Y	2024-07-21 23:09:18.846114	1000037	2024-07-21 23:09:18.846114	1000037	1000002	09d1fcb2-c796-461f-bbb8-7817eddd5c7c	\N
1000140	https:	Y	2024-07-21 23:09:44.748868	1000037	2024-07-21 23:09:44.748868	1000037	1000002	1ef8bac5-0aaa-4e46-9fcd-7322cc55ae69	\N
1000141	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fadfed620-4869-11ef-a756-779d7aba7476?alt=media&token=4a089680-086e-4e35-a8c9-1de4cbfd2608	Y	2024-07-22 16:33:11.410926	1000037	2024-07-22 16:33:11.410928	1000037	1000002	c3be376d-428c-44d5-9a33-af04125f7d82	\N
1000142	https:	Y	2024-07-22 16:36:22.371896	1000037	2024-07-22 16:36:22.371897	1000037	1000002	3113aca0-cda0-48c6-b94a-e76bf06e69db	\N
1000143	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct?alt=media&token=92cdcbab-0940-482c-bc5e-de80018d0184	Y	2024-07-23 15:04:59.058446	1000037	2024-07-23 15:04:59.058448	1000037	1000002	41dfaeb3-227b-42c8-a30b-99f2aded26e2	\N
1000147	http://example.com/image.jpg	Y	2024-07-23 23:20:46.892697	1000068	2024-07-23 23:20:46.892697	1000068	1000003	665a3854-a746-4001-af99-078375478a12	\N
1000148	http://example.com/image.jpg	Y	2024-07-23 23:34:17.416559	1000068	2024-07-23 23:34:17.416559	1000068	1000003	44643b27-a18f-4500-9d8a-2da4936b5dca	\N
1000150	https:	Y	2024-07-23 23:41:11.015371	1000068	2024-07-23 23:41:11.015371	1000068	1000003	965aee14-a8d9-4aa8-8cb5-d52be03b37b0	\N
1000152	https:	Y	2024-07-23 23:42:09.248032	1000068	2024-07-23 23:42:09.248032	1000068	1000003	8e08eb85-d7e6-41b4-957a-660b6dcbb951	\N
1000153	https:11	Y	2024-07-23 23:42:59.861985	1000068	2024-07-23 23:42:59.861985	1000068	1000003	44654e19-c21e-4bfb-9fa0-7071eca2c3eb	\N
1000154	http://example.com/image.jpg	Y	2024-07-30 09:49:36.03897	1000068	2024-07-30 09:49:36.038972	1000068	1000003	2f2e11fc-5df0-43f9-be4c-96dafe874e4c	\N
1000157	http://example.com/image.jpg	Y	2024-07-30 10:27:32.728176	1000068	2024-07-30 10:27:32.728176	1000068	1000003	9c5aab8d-1b5d-4015-971f-09e50e86e309	\N
1000162	http://example.com/image.jpg	Y	2024-07-30 10:40:47.38273	1000068	2024-07-30 10:40:47.38273	1000068	1000003	5045c53a-dc4c-497a-8e72-c82ac9964f31	\N
1000163	http://example.com/image.jpg	Y	2024-07-30 10:50:24.056245	1000068	2024-07-30 10:50:24.056245	1000068	1000003	0783106f-fb19-44e6-a89a-3d64ad22ff3d	\N
1000164	http://example.com/image.jpg	Y	2024-07-30 10:50:34.378537	1000068	2024-07-30 10:50:34.378537	1000068	1000003	01783c51-522b-4156-928f-89bbe4cddb35	\N
1000165	http://example.com/image.jpg	Y	2024-07-30 15:51:53.928666	1000068	2024-07-30 15:51:53.928666	1000068	1000003	459122f2-854a-4714-a1ee-1757bf069d24	\N
1000166	http://example.com/image.jpg	Y	2024-07-30 16:13:54.921987	1000068	2024-07-30 16:13:54.921989	1000068	1000003	9d1f5967-bcbf-4eb6-937e-da1348d36b74	\N
1000167	http://example.com/image.jpg	Y	2024-07-30 16:23:47.662801	1000068	2024-07-30 16:23:47.662801	1000068	1000003	16c86000-f096-4e7f-85a4-6c5a10560825	\N
1000168	http://example.com/image.jpg	Y	2024-07-30 16:26:02.283691	1000068	2024-07-30 16:26:02.283691	1000068	1000003	5dc7ee0a-d46f-4780-ab44-40787ae7071e	\N
1000169	http://example.com/image.jpg	Y	2024-07-31 09:27:13.009834	1000037	2024-07-31 09:27:13.009836	1000037	1000002	d022dbe0-f7b2-4b30-9f16-d3a9cfc7878a	\N
1000170	http://example.com/image.jpg	Y	2024-07-31 11:26:23.743427	1000037	2024-07-31 11:26:23.743428	1000037	1000002	e32428dc-3852-4902-916c-a75f6ef44e03	\N
1000171	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fcustomer%2Fe5a39b80-4f74-11ef-b390-4bf3918ef9fe?alt=media&token=126e0013-25ea-4a52-b40d-3890ccfb37ea	Y	2024-07-31 15:40:59.420559	1000037	2024-07-31 15:40:59.42056	1000037	1000002	01c9c3a0-c254-4cdc-ad6d-df16fc956ee5	\N
1000172	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F3f297120-5024-11ef-b846-d5a791d3e1d9?alt=media&token=f81ebf29-4ede-43b4-bfce-2e9657388732	Y	2024-08-01 12:36:12.554835	1000037	2024-08-01 12:36:12.554836	1000037	1000002	7c5f1717-6d44-46a0-b899-1c410d197904	\N
1000173	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F507eb2a0-5024-11ef-b846-d5a791d3e1d9?alt=media&token=545eccda-6a3d-4838-9966-7bef111f34b2	Y	2024-08-01 12:36:40.541666	1000037	2024-08-01 12:36:40.541666	1000037	1000002	cc17e0cd-2a46-4d8e-a9c2-9b8c1e3e2ac3	\N
1000174	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct?alt=media&token=4e8d9708-986d-4ebc-91d5-9fc7d8562b28	Y	2024-08-01 12:37:14.354845	1000037	2024-08-01 12:37:14.354845	1000037	1000002	b7c94078-0167-49ee-a2d5-866b152d7bde	\N
1000175	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct?alt=media&token=6a3a22c9-6756-49c6-9276-823b96d49bcb	Y	2024-08-01 12:37:30.031552	1000037	2024-08-01 12:37:30.031553	1000037	1000002	020d31aa-64ea-41c3-9af7-5ff0f45b9d3b	\N
1000176	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct?alt=media&token=e3595cfe-7d51-4558-9d30-a567fd2bb386	Y	2024-08-01 12:38:08.496338	1000037	2024-08-01 12:38:08.496338	1000037	1000002	441f3f01-7407-43e1-b91c-7224d3704e90	\N
1000177	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct?alt=media&token=aa43445f-a281-4999-9978-cc39d36cfa70	Y	2024-08-01 12:39:34.940159	1000037	2024-08-01 12:39:34.94016	1000037	1000002	4681fc09-fcf8-4ced-9bf4-621e9234fbb3	\N
1000178	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct?alt=media&token=03c0bff3-4d3e-4ac4-9e42-d52037837fea	Y	2024-08-01 12:39:45.459583	1000037	2024-08-01 12:39:45.459584	1000037	1000002	c4ddd194-5546-42a9-b17c-d614c1ea4a47	\N
1000179	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct?alt=media&token=2ef6b274-28bd-4871-909a-006766a3438a	Y	2024-08-01 12:39:58.237979	1000037	2024-08-01 12:39:58.237979	1000037	1000002	6f9e024b-e3d3-4c20-a404-547e971bc1ad	\N
1000180	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct?alt=media&token=d2a78edd-63a0-4b11-9c08-1b7f64c0067e	Y	2024-08-01 12:40:10.978074	1000037	2024-08-01 12:40:10.978074	1000037	1000002	ea879f7e-b5e2-472e-a79a-81834a6dcf68	\N
1000181	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fd5bf8d40-5024-11ef-b846-d5a791d3e1d9?alt=media&token=7158bcd8-0896-472a-9745-704d12490f45	Y	2024-08-01 12:40:23.841965	1000037	2024-08-01 12:40:23.841965	1000037	1000002	c07ae333-936d-4098-9140-d68fbd61a9c9	\N
1000182	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct?alt=media&token=30773016-5bfb-45fb-9592-52936c998fef	Y	2024-08-01 12:48:23.103849	1000037	2024-08-01 12:48:23.103849	1000037	1000002	82226769-609e-4753-acfa-42ebca517c0a	\N
1000183	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct?alt=media&token=a50e6be2-0fd7-4827-898a-c2a4e6ba683e	Y	2024-08-01 12:50:46.028227	1000037	2024-08-01 12:50:46.028228	1000037	1000002	d0e45f9d-af8f-47d7-afc9-0240e5ed94e4	\N
1000184	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F08e7a480-5027-11ef-a0ab-a5a49a30567d?alt=media&token=5c5e6458-6377-4dd3-9723-5ff7a50c12c2	Y	2024-08-01 12:56:08.879012	1000037	2024-08-01 12:56:08.879012	1000037	1000002	14807f60-284e-4654-a34d-f5e45ef52547	\N
1000185	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F12902d40-5027-11ef-ad03-df65065b6121?alt=media&token=6152046d-8f28-4fcc-ae4d-1a8fe970aa12	Y	2024-08-01 12:56:25.067986	1000037	2024-08-01 12:56:25.067986	1000037	1000002	89fab2ff-8c47-4ae7-bb63-237a949e7dfe	\N
1000186	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F198b7050-5027-11ef-ad03-df65065b6121?alt=media&token=ab4870a6-d85e-4bd9-91ec-19da218eb538	Y	2024-08-01 12:56:36.769421	1000037	2024-08-01 12:56:36.769421	1000037	1000002	2dd4a15e-7fa7-46ae-865b-556d96299fa2	\N
1000187	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F20a6e590-5027-11ef-ad03-df65065b6121?alt=media&token=b60e1824-03a7-4e32-8de2-2318eb2c6a06	Y	2024-08-01 12:56:48.71627	1000037	2024-08-01 12:56:48.71627	1000037	1000002	ef6676a7-06b1-47c6-85f9-ab063848f3c8	\N
1000188	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F27d6a620-5027-11ef-ad03-df65065b6121?alt=media&token=68e637d0-4415-4ad4-bb20-6be58a60886c	Y	2024-08-01 12:57:00.737281	1000037	2024-08-01 12:57:00.737281	1000037	1000002	214acb5b-3e93-4875-8e04-c7bfae59fd2d	\N
1000189	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F318c0020-5027-11ef-aba1-0bc01a8fe205?alt=media&token=1309287a-3363-4916-bd6f-be0758dc40f2	Y	2024-08-01 12:57:17.010015	1000037	2024-08-01 12:57:17.010015	1000037	1000002	56b66ac4-0d7a-4b1e-8a38-eeb8542e03ce	\N
1000190	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F42ff9f10-5027-11ef-aba1-0bc01a8fe205?alt=media&token=dd232ee3-fce2-4a1d-93fb-46a1ca5f0181	Y	2024-08-01 12:57:46.456357	1000037	2024-08-01 12:57:46.456357	1000037	1000002	eaaf9e7d-16d2-4f6c-bcb1-c0e77d1d3568	\N
1000191	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F49864e60-5027-11ef-aba1-0bc01a8fe205?alt=media&token=911d7e82-f6cc-4af2-98e9-98a7772289d2	Y	2024-08-01 12:57:57.494553	1000037	2024-08-01 12:57:57.494554	1000037	1000002	82f9f7cd-afaa-42fa-9184-a9109c1e6e20	\N
1000192	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F61a5d7e0-5027-11ef-aba1-0bc01a8fe205?alt=media&token=6c26b31f-ed33-4597-b160-3d482aeb308e	Y	2024-08-01 12:58:37.941965	1000037	2024-08-01 12:58:37.941965	1000037	1000002	593a9fea-75b3-49d7-9d1c-44527940b8d2	\N
1000193	https:	Y	2024-08-03 11:14:04.623803	1000068	2024-08-03 11:14:04.623803	1000068	1000003	450e4ff9-02f5-497b-a012-f110b1646edf	\N
1000194	https:	Y	2024-08-03 11:19:28.876326	1000068	2024-08-03 11:19:28.876326	1000068	1000003	36d19ad7-e2bb-44a0-ac06-bc3c19b59209	\N
1000195	https:	Y	2024-08-03 11:28:20.398806	1000068	2024-08-03 11:28:20.398806	1000068	1000003	a6b20637-4122-4e29-9a51-e3dad9e3443a	\N
1000279	https:	Y	2024-08-24 10:26:55.239821	1000069	2024-08-24 10:26:55.239821	1000069	1000004	263c39a6-a8a6-4718-8b9c-5a686b0140ae	\N
1000280	https:	Y	2024-08-24 10:27:50.563041	1000069	2024-08-24 10:27:50.563041	1000069	1000004	7b8fa2ba-64bf-4fbc-a49c-78869c32e88a	\N
1000281	https:	Y	2024-08-24 10:28:16.171985	1000069	2024-08-24 10:28:16.171985	1000069	1000004	afc7578f-38b6-491c-89fe-a978dd987860	\N
1000199	https:	Y	2024-08-04 11:18:14.202505	1000068	2024-08-04 11:18:14.202505	1000068	1000003	61ad0012-71c0-45a5-b6fd-47f01c84da5c	\N
1000200	https:	Y	2024-08-04 12:05:48.830868	1000069	2024-08-04 12:05:48.830868	1000069	1000004	f35b3d06-bc22-4e29-a3bb-a6721de7befa	\N
1000201	https:	Y	2024-08-04 12:06:30.955142	1000069	2024-08-04 12:06:30.955142	1000069	1000004	6dbdf6fb-5178-4582-8a92-02f8014ef64d	\N
1000202	https:	Y	2024-08-04 12:06:46.680951	1000069	2024-08-04 12:06:46.680951	1000069	1000004	b00458f3-e984-4f36-889e-49d2bc13aaa1	\N
1000203	https:	Y	2024-08-04 12:09:40.179352	1000069	2024-08-04 12:09:40.179352	1000069	1000004	8a5c35a5-5315-409e-9e16-c13f353e3498	\N
1000204	https:	Y	2024-08-04 12:10:06.397991	1000069	2024-08-04 12:10:06.397991	1000069	1000004	b63244a3-9f61-44f5-8807-3bb53a1df964	\N
1000205	https:	Y	2024-08-04 12:10:19.150705	1000069	2024-08-04 12:10:19.150705	1000069	1000004	472b2761-8c3e-432f-bcb4-02be122f636f	\N
1000206	https:	Y	2024-08-04 12:15:19.430717	1000069	2024-08-04 12:15:19.430717	1000069	1000004	b2d938e5-f693-4992-b955-de75ae4de434	\N
1000286	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.713208	1000069	2024-09-05 09:22:46.713209	1000069	1000004	62a45bda-33cc-452b-9dec-f4f735d8b406	\N
1000208	https:	Y	2024-08-04 12:15:41.448188	1000069	2024-08-04 12:15:41.448188	1000069	1000004	aa0c9e4d-955d-4950-bbe0-72d389a848f5	\N
1000209	https:	Y	2024-08-04 12:15:57.247049	1000069	2024-08-04 12:15:57.247049	1000069	1000004	7befe5f2-6169-4c79-84a5-846c4e1d469c	\N
1000210	https:	Y	2024-08-04 12:18:29.023503	1000069	2024-08-04 12:18:29.023503	1000069	1000004	641956a8-3d90-4d2c-ba0a-241ee1e2fbf2	\N
1000287	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.723041	1000069	2024-09-05 09:22:46.723042	1000069	1000004	6c302a2c-70e8-497d-be5a-eccf8b9f6d6a	\N
1000288	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.734018	1000069	2024-09-05 09:22:46.734018	1000069	1000004	56fbfaa0-46da-4dc6-adac-f2c447de5867	\N
1000289	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.7501	1000069	2024-09-05 09:22:46.750101	1000069	1000004	c9bb9ecd-f09d-4169-aeac-88f918cc60f0	\N
1000290	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.777648	1000069	2024-09-05 09:22:46.777648	1000069	1000004	bf7d25b9-1273-4130-8392-e76d990fe51a	\N
1000291	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.788978	1000069	2024-09-05 09:22:46.788979	1000069	1000004	3f9c19b5-d6f8-4a74-9afa-f02fad52ea77	\N
1000216	https:	Y	2024-08-04 12:21:04.508071	1000069	2024-08-04 12:21:04.508071	1000069	1000004	d8ad01bd-a7dd-4c99-818c-6d99a6f53f51	\N
1000292	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.799304	1000069	2024-09-05 09:22:46.799304	1000069	1000004	e4782994-ecee-4171-b457-f3bca44a9a91	\N
1000224	https:	Y	2024-08-07 09:12:12.147562	1000069	2024-08-07 09:12:12.147563	1000069	1000004	3c7fa0d1-6588-4466-9b90-7254240c9f42	\N
1000226	https:	Y	2024-08-07 09:12:30.21283	1000069	2024-08-07 09:12:30.212831	1000069	1000004	5a1bd14b-610f-467a-9a19-4cf44275ecc5	\N
1000227	https:	Y	2024-08-07 09:12:46.602297	1000069	2024-08-07 09:12:46.602298	1000069	1000004	d0b4f04a-2e5c-452d-ab14-1939edcc8daf	\N
1000229	https:	Y	2024-08-07 09:13:10.805687	1000069	2024-08-07 09:13:10.805687	1000069	1000004	7099dcbf-16d0-4b57-b2a9-2881ae540b43	\N
1000231	https:	Y	2024-08-07 09:13:29.522661	1000069	2024-08-07 09:13:29.522662	1000069	1000004	0b9bfcc1-70c7-4c24-8e02-5ff305e0c276	\N
1000233	https:	Y	2024-08-07 09:13:50.259342	1000069	2024-08-07 09:13:50.259343	1000069	1000004	40565115-57d0-41ab-8a35-c056d01f1120	\N
1000235	https:	Y	2024-08-07 09:14:55.067519	1000069	2024-08-07 09:14:55.06752	1000069	1000004	4b87768c-e1c1-4ccd-8af3-b45a1c1f135b	\N
1000236	https:	Y	2024-08-07 09:33:47.274957	1000069	2024-08-07 09:33:47.274958	1000069	1000004	8f607f50-0d33-47a8-b75d-eed43b0f5217	\N
1000293	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.808897	1000069	2024-09-05 09:22:46.808898	1000069	1000004	a939f052-5571-42fc-814f-c2ad79677b2a	\N
1000294	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.816589	1000069	2024-09-05 09:22:46.816589	1000069	1000004	b6d73e4b-9682-4bc4-9657-84433c1554a8	\N
1000239	https:	Y	2024-08-07 09:36:49.217237	1000069	2024-08-07 09:36:49.217238	1000069	1000004	bddf7f5a-c7ca-4d98-8394-8cfec504f290	\N
1000240	http://example.com/image.jpg	Y	2024-08-07 10:36:18.213937	1000069	2024-08-07 10:36:18.213937	1000069	1000004	72bce512-3d95-4bdb-a53d-9bf4a49b0feb	\N
1000295	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.825998	1000069	2024-09-05 09:22:46.825998	1000069	1000004	a8fe18d5-8fe6-4161-9fc2-f34e842974ed	\N
1000242	http://example.com/image.jpg	Y	2024-08-07 12:02:06.485202	1000069	2024-08-07 12:02:06.485202	1000069	1000004	7bc95c0d-93d0-424e-8be4-9fe73aac2032	\N
1000296	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.833275	1000069	2024-09-05 09:22:46.833275	1000069	1000004	9f54de1c-4f00-47e6-876d-a1e28cadd91f	\N
1000244	http://example.com/image.jpg	Y	2024-08-07 02:56:08.693655	1000069	2024-08-07 02:56:08.693655	1000069	1000004	80b7955f-345d-4f79-9cf9-36f6637f5f80	\N
1000245	http://example.com/image.jpg	Y	2024-08-07 03:07:38.544886	1000069	2024-08-07 03:07:38.544886	1000069	1000004	9d920785-00c4-4524-85af-a5ca8122c70d	\N
1000246	http://example.com/image.jpg	Y	2024-08-07 03:15:48.061747	1000069	2024-08-07 03:15:48.061747	1000069	1000004	c1c3cf8f-c39b-4c3c-bb3a-aba77f88c062	\N
1000247	http://example.com/image.jpg	Y	2024-08-07 03:20:09.812765	1000069	2024-08-07 03:20:09.812765	1000069	1000004	d301196f-0e25-404a-8f3f-f2984107ddfa	\N
1000248	http://example.com/image.jpg	Y	2024-08-07 03:26:07.395787	1000069	2024-08-07 03:26:07.395787	1000069	1000004	8c7c7131-bd06-44eb-8142-e2a6746db728	\N
1000249	http://example.com/image.jpg	Y	2024-08-07 03:35:52.863545	1000069	2024-08-07 03:35:52.863545	1000069	1000004	a456c742-0c88-44a9-92fc-f96f65795131	\N
1000250	http://example.com/image.jpg	Y	2024-08-07 03:38:06.627177	1000069	2024-08-07 03:38:06.627177	1000069	1000004	db180c54-f77b-4be6-9944-65b4fbfeae61	\N
1000251	http://example.com/image.jpg	Y	2024-08-07 03:41:03.649861	1000069	2024-08-07 03:41:03.649861	1000069	1000004	47e88ac3-491e-4b81-b997-109f45b852af	\N
1000252	http://example.com/image.jpg	Y	2024-08-07 03:44:18.619811	1000069	2024-08-07 03:44:18.619811	1000069	1000004	698def35-cadb-4b10-99d7-6f71a117f0f2	\N
1000253	http://example.com/image.jpg	Y	2024-08-07 03:52:08.432153	1000069	2024-08-07 03:52:08.432153	1000069	1000004	83d70ab8-5238-41f6-afa3-ba82d5fb6f13	\N
1000254	http://example.com/image.jpg	Y	2024-08-07 14:57:07.164201	1000069	2024-08-07 14:57:07.164201	1000069	1000004	ec766f84-4721-47c0-a652-38ce2b1f8eff	\N
1000255	http://example.com/image.jpg	Y	2024-08-07 14:58:47.187309	1000069	2024-08-07 14:58:47.187309	1000069	1000004	00f81c71-2ba7-4026-a9f6-bd4562d35832	\N
1000256	http://example.com/image.jpg	Y	2024-08-07 15:02:21.924152	1000069	2024-08-07 15:02:21.924152	1000069	1000004	d55e978b-1975-465e-8b34-13c03baa84c3	\N
1000257	http://example.com/image.jpg	Y	2024-08-07 15:14:48.939272	1000069	2024-08-07 15:14:48.939272	1000069	1000004	39b97365-6366-458f-8a5f-d779f5995c7b	\N
1000258	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fcustomer?alt=media&token=627f35fa-4e77-480b-8075-a70b828f34d9	Y	2024-08-07 16:42:29.896471	1000069	2024-08-07 16:42:29.896473	1000069	1000004	01f32214-db12-4d8b-af7a-448e278aed31	\N
1000259	http://example.com/image.jpg	Y	2024-08-07 06:32:41.721595	1000069	2024-08-07 06:32:41.721595	1000069	1000004	1ff30b6e-d6b1-4014-beaa-0fe27d7fd988	\N
1000260	http://example.com/image.jpg	Y	2024-08-07 06:33:05.558576	1000069	2024-08-07 06:33:05.558576	1000069	1000004	e5e3f9b7-03f8-43a8-a1ef-7573206a3f70	\N
1000297	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.8427	1000069	2024-09-05 09:22:46.8427	1000069	1000004	e705f836-3846-4bda-adaf-4f1051167e83	\N
1000262	http://example.com/image.jpg	Y	2024-08-07 06:36:51.014459	1000069	2024-08-07 06:36:51.014459	1000069	1000004	b870e54e-7751-4631-90bb-642707dfb17c	\N
1000263	http://example.com/image.jpg	Y	2024-08-07 17:36:15.604067	1000069	2024-08-07 17:36:15.604069	1000069	1000004	54b7c62b-4944-41d0-a0d1-93fff39dfdcc	\N
1000264	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fcustomer%2Fe74554a0-5506-11ef-96f2-11998e9f7803?alt=media&token=8fa6e5d3-cf67-4348-b4ca-afb0fcaf4219	Y	2024-08-07 17:48:39.698262	1000069	2024-08-07 17:48:39.698263	1000069	1000004	b8a433f7-2b75-4606-aa23-ba670d8427ba	\N
1000265	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fcustomer?alt=media&token=2c00db7a-8961-48bc-ba1b-6e573f2f2ac8	Y	2024-08-07 17:48:47.454711	1000069	2024-08-07 17:48:47.454712	1000069	1000004	f6ac4cdb-e81e-4e37-857b-d500098a095d	\N
1000266	http://example.com/image.jpg	Y	2024-08-13 09:52:29.620512	1000069	2024-08-13 09:52:29.620536	1000069	1000004	de1da430-5091-4d8c-85e5-7d60d28eb791	\N
1000267	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fcustomer?alt=media&token=ee94f869-bdee-4a84-83cd-9dc67a7e4f59	Y	2024-08-13 15:05:00.492165	1000069	2024-08-13 15:05:00.492167	1000069	1000004	f37df900-603e-4198-b7c7-f4e5512e0a81	\N
1000268	http://example.com/image.jpg	Y	2024-08-14 09:50:49.387547	1000069	2024-08-14 09:50:49.387549	1000069	1000004	78728177-af58-4f93-bba8-b62333716a0e	\N
1000269	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fvendor?alt=media&token=5f317d61-e92b-4168-8950-59c289fcb21a	Y	2024-08-14 11:21:18.662262	1000069	2024-08-14 11:21:18.662264	1000069	1000004	8b6bdf97-dbe7-43f4-9a04-75fd4b24d6e0	\N
1000298	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.854861	1000069	2024-09-05 09:22:46.854861	1000069	1000004	da650599-b9d2-4f8c-877e-c7a596739bbb	\N
1000273	http://example.com/image.jpg	Y	2024-08-15 14:16:56.651195	1000069	2024-08-15 14:16:56.651196	1000069	1000004	1aca78d2-b47a-4f3b-b9d6-2c606f4142bb	\N
1000299	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.870892	1000069	2024-09-05 09:22:46.870892	1000069	1000004	e68da321-84cb-4634-8f5a-c17eb853bf56	\N
1000300	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.894142	1000069	2024-09-05 09:22:46.894143	1000069	1000004	d9c98571-7ea6-4f33-95d8-73b1b3ca96c7	\N
1000276	http://example.com/image.jpg	Y	2024-08-15 15:25:44.600274	1000069	2024-08-15 15:25:44.600274	1000069	1000004	604b67e2-bac5-47ef-b431-7678caa6eb42	\N
1000350	https://assets.digitalbiz.com.vn/Images/*/F&B/000000010.png	Y	2024-09-16 13:43:23.904973	1000069	2024-09-16 13:43:23.904973	1000069	1000004	2bbd8ca4-0ce0-4cc6-b19b-602d364c45fb	000000010
1000351	https://assets.digitalbiz.com.vn/Images/*/F&B/000000011.png	Y	2024-09-18 14:07:15.412383	1000069	2024-09-18 14:07:15.412383	1000069	1000004	37c35d9d-2f66-44d7-abf5-49076957d6f2	000000011
1000301	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.909211	1000069	2024-09-05 09:22:46.909211	1000069	1000004	610fbc3b-509d-49f0-88ec-8c808849859f	\N
1000302	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.919996	1000069	2024-09-05 09:22:46.919996	1000069	1000004	083f5dd7-8260-4bcf-93f7-6c85d1ee99dd	\N
1000303	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe0950e60-6b2d-11ef-ac87-6f2239da2424?alt=media&token=52de666e-9b9c-4982-9bb1-0ce838b69910	Y	2024-09-05 09:22:46.93223	1000069	2024-09-05 09:22:46.93223	1000069	1000004	5efd7314-f121-49f4-a40d-49591fa84f59	\N
1000304	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fc682e730-6b2e-11ef-8b8d-5d0b5dfdea6d?alt=media&token=73f1ab39-d769-4210-8513-c8bc3c7ab74b	Y	2024-09-05 09:29:12.100888	1000069	2024-09-05 09:29:12.100888	1000069	1000004	be3a70af-9706-401a-9aed-a3ab3f73338d	\N
1000305	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fc682e730-6b2e-11ef-8b8d-5d0b5dfdea6d?alt=media&token=73f1ab39-d769-4210-8513-c8bc3c7ab74b	Y	2024-09-05 09:29:12.120641	1000069	2024-09-05 09:29:12.120641	1000069	1000004	d5e2f6b2-9b50-4207-811b-5b86fc3e70bb	\N
1000306	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fc682e730-6b2e-11ef-8b8d-5d0b5dfdea6d?alt=media&token=73f1ab39-d769-4210-8513-c8bc3c7ab74b	Y	2024-09-05 09:29:12.228413	1000069	2024-09-05 09:29:12.228413	1000069	1000004	a6eb3c2a-af7f-4c57-b8fc-ecbfaaeb9eb0	\N
1000307	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fc682e730-6b2e-11ef-8b8d-5d0b5dfdea6d?alt=media&token=73f1ab39-d769-4210-8513-c8bc3c7ab74b	Y	2024-09-05 09:29:12.246525	1000069	2024-09-05 09:29:12.246525	1000069	1000004	0c1047d6-6482-458b-98e7-5723ec08554d	\N
1000308	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fc682e730-6b2e-11ef-8b8d-5d0b5dfdea6d?alt=media&token=73f1ab39-d769-4210-8513-c8bc3c7ab74b	Y	2024-09-05 09:29:12.26327	1000069	2024-09-05 09:29:12.26327	1000069	1000004	1be0f350-9bd9-4247-bd76-2edd4c9e430d	\N
1000309	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fc682e730-6b2e-11ef-8b8d-5d0b5dfdea6d?alt=media&token=73f1ab39-d769-4210-8513-c8bc3c7ab74b	Y	2024-09-05 09:29:12.275835	1000069	2024-09-05 09:29:12.275835	1000069	1000004	70465fc1-2ac9-4fd3-85d5-af7eecedf147	\N
1000310	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fc682e730-6b2e-11ef-8b8d-5d0b5dfdea6d?alt=media&token=73f1ab39-d769-4210-8513-c8bc3c7ab74b	Y	2024-09-05 09:29:12.292119	1000069	2024-09-05 09:29:12.292119	1000069	1000004	cc0ea9e4-a9ec-42c4-8b4e-a9467335f85d	\N
1000311	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fcustomer%2Faebdc7c0-6bf4-11ef-8f7f-698a7a719f38?alt=media&token=01f468da-70aa-4d7a-ad4b-f1d7567a44f3	Y	2024-09-06 09:06:54.235384	1000069	2024-09-06 09:06:54.235388	1000069	1000004	1ed45d2b-d924-45a4-9997-10cf2966eabc	\N
1000312	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fcustomer%2Fafe7c8d0-6bf4-11ef-8f7f-698a7a719f38?alt=media&token=6c94b56f-048d-4047-820c-c1f9b933f621	Y	2024-09-06 09:06:55.758397	1000069	2024-09-06 09:06:55.758398	1000069	1000004	e1eada81-9028-4dc1-9da3-8ee7f67fb747	\N
1000313	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fcustomer%2Fbe6b9ad0-6bf4-11ef-8f7f-698a7a719f38?alt=media&token=4c52b7ea-a4d7-4f95-bbc7-288dbaf2ed54	Y	2024-09-06 09:07:19.905237	1000069	2024-09-06 09:07:19.905238	1000069	1000004	839798c9-e674-428e-8c5f-aa4dac79b9a0	\N
1000314	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fcustomer%2Fbf10b470-6bf4-11ef-8f7f-698a7a719f38?alt=media&token=8ca371c8-4c72-4353-8d70-62c5bdd0284a	Y	2024-09-06 09:07:20.999942	1000069	2024-09-06 09:07:20.999943	1000069	1000004	f281f4ca-ac8e-4362-a497-ffbb255e7b74	\N
1000315	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fcustomer%2Fbf5f2290-6bf4-11ef-8f7f-698a7a719f38?alt=media&token=f6ca8fe6-51f1-465b-b396-91fd47b2fa0e	Y	2024-09-06 09:07:21.482369	1000069	2024-09-06 09:07:21.482371	1000069	1000004	a9d82c8c-cc27-462c-ba99-e0e90a9972d8	\N
1000316	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fcustomer%2Fbf773e70-6bf4-11ef-8f7f-698a7a719f38?alt=media&token=1279f145-7ac8-4b1a-bbab-2ec4687172c2	Y	2024-09-06 09:07:21.659567	1000069	2024-09-06 09:07:21.659568	1000069	1000004	773030b3-1e3c-463b-8636-080746107e33	\N
1000317	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fcustomer%2Fbf8f5a50-6bf4-11ef-8f7f-698a7a719f38?alt=media&token=bc0a63f3-5f85-4bc3-a3bd-c1ebd972772d	Y	2024-09-06 09:07:21.781709	1000069	2024-09-06 09:07:21.78171	1000069	1000004	1582a01d-d867-419f-86ce-209ac4c3346f	\N
1000320	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe9f89ab0-7016-11ef-8720-edb2eb94a4b7?alt=media&token=7cddb013-d179-4482-ac9e-19b3aabaefcb	Y	2024-09-11 15:56:31.662864	1000071	2024-09-11 15:56:31.662864	1000071	1000009	878a2739-0cdb-4ed2-bb68-b65ccf52b130	\N
1000321	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe9f89ab0-7016-11ef-8720-edb2eb94a4b7?alt=media&token=7cddb013-d179-4482-ac9e-19b3aabaefcb	Y	2024-09-11 16:14:28.739827	1000071	2024-09-11 16:14:28.739827	1000071	1000009	9b20dc0c-68c5-4a10-b97c-6762f16919c3	\N
1000322	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe9f89ab0-7016-11ef-8720-edb2eb94a4b7?alt=media&token=7cddb013-d179-4482-ac9e-19b3aabaefcb	Y	2024-09-11 16:17:49.728416	1000071	2024-09-11 16:17:49.728416	1000071	1000009	919ac154-03ef-46f9-b509-c7c96c26263b	\N
1000323	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F44be95e0-7020-11ef-920d-170f043db168?alt=media&token=ed44899e-b6b9-4cda-b4ba-4de9a07316af	Y	2024-09-11 16:32:36.605648	1000071	2024-09-11 16:32:36.605651	1000071	1000009	5f1aba22-7a58-492c-8d5b-fb5deafcd4dc	\N
1000342	https:	Y	2024-09-12 07:06:16.727094	1000071	2024-09-12 07:06:16.727094	1000071	1000009	32992e61-ee29-4839-a9dc-74f6f713bdb7	\N
1000343	https:	Y	2024-09-12 07:07:14.20407	1000071	2024-09-12 07:07:14.20407	1000071	1000009	835f0cee-dd4e-4a37-8884-d005ca592b0f	\N
1000335	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe9f89ab0-7016-11ef-8720-edb2eb94a4b7?alt=media&token=7cddb013-d179-4482-ac9e-19b3aabaefcb	Y	2024-09-11 17:48:06.480278	1000071	2024-09-11 17:48:06.480279	1000071	1000009	9dbce051-16c6-41f0-b158-02b2a1e586b0	\N
1000336	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F1d7f03f0-702c-11ef-83e7-9f7f8e9bda7f?alt=media&token=37359342-8e70-4ca1-bbc6-526b7a5aaf94	Y	2024-09-11 17:57:24.635515	1000071	2024-09-11 17:57:24.635515	1000071	1000009	7dc4c25c-187d-4b34-9796-85f27feb7aff	\N
1000337	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F6cd1dea0-702c-11ef-83e7-9f7f8e9bda7f?alt=media&token=16ff1171-cddd-4f21-8232-4ce8526a3722	Y	2024-09-11 17:59:37.355367	1000071	2024-09-11 17:59:37.355367	1000071	1000009	1a96c1a9-b39a-483a-bab5-13650c25dd11	\N
1000338	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Feea98120-702d-11ef-83e7-9f7f8e9bda7f?alt=media&token=7cd3b170-6430-4ff3-a8e5-390c178e4671	Y	2024-09-11 18:10:24.894795	1000071	2024-09-11 18:10:24.894795	1000071	1000009	9fe7a9f8-41ee-429e-993a-25d3fee2d0d9	\N
1000339	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F44fa0fe0-702e-11ef-83e7-9f7f8e9bda7f?alt=media&token=162d6487-abff-4cbc-9ccd-61b561a4ebdd	Y	2024-09-11 18:12:49.981952	1000071	2024-09-11 18:12:49.981953	1000071	1000009	3e8ff885-4979-4d16-9668-dd4f19941a59	\N
1000340	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2F7d2136f0-702e-11ef-83e7-9f7f8e9bda7f?alt=media&token=7d250bd7-5723-45c5-bbaa-ae16846f4feb	Y	2024-09-11 18:14:23.698771	1000071	2024-09-11 18:14:23.698772	1000071	1000009	cdbb321b-9cca-44d3-a69a-548b892ae15d	\N
1000341	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fb363bf80-702e-11ef-83e7-9f7f8e9bda7f?alt=media&token=c461e74c-a041-423c-8a56-84bfc6ef5a32	Y	2024-09-11 18:15:54.632733	1000071	2024-09-11 18:15:54.632733	1000071	1000009	bab9c72c-275b-4b40-b462-eadaf2fc7110	\N
1000344	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe8c949f0-70fc-11ef-aa96-ef56ed5353b5?alt=media&token=487ccee7-a187-4c62-9748-4d48c246d646	Y	2024-09-12 18:47:24.924668	1000071	2024-09-12 18:47:24.92467	1000071	1000009	4ab467eb-7e8e-4389-9152-d5f695ef3b42	\N
1000345	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe8c949f0-70fc-11ef-aa96-ef56ed5353b5?alt=media&token=487ccee7-a187-4c62-9748-4d48c246d646	Y	2024-09-12 18:47:25.253466	1000071	2024-09-12 18:47:25.253467	1000071	1000009	34cce2ce-081d-455c-9f3f-9fe4c67768d2	\N
1000346	https://firebasestorage.googleapis.com/v0/b/dbiz-retail.appspot.com/o/image%2Fproduct%2Fe8c949f0-70fc-11ef-aa96-ef56ed5353b5?alt=media&token=487ccee7-a187-4c62-9748-4d48c246d646	Y	2024-09-12 18:47:25.329054	1000071	2024-09-12 18:47:25.329055	1000071	1000009	d7a468e6-accd-4051-a483-e51c20fbb7b9	\N
1000352	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000021.png	Y	2024-09-18 16:46:22.174526	1000069	2024-09-18 16:46:22.174526	1000069	1000004	26780a7d-a456-4db9-8ce6-319adeb56ca2	000000021
1000353	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png	Y	2024-09-18 16:48:58.658339	1000069	2024-09-18 16:48:58.658339	1000069	1000004	1a04d011-ba51-4662-ba6a-bbb88487ac78	000000025
1000354	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png	Y	2024-09-18 16:50:08.287428	1000069	2024-09-18 16:50:08.287428	1000069	1000004	2fa8e810-8e63-438d-a99b-70ed2c278772	000000025
1000355	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png	Y	2024-09-18 16:52:13.210979	1000069	2024-09-18 16:52:13.210979	1000069	1000004	2f4f3a4a-7fb2-4749-ae9b-c11a48f1f837	000000025
1000356	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png	Y	2024-09-18 16:53:59.519223	1000069	2024-09-18 16:53:59.519223	1000069	1000004	4d2e252b-81e8-44ce-9c4e-9adc7bbb3d09	000000025
1000357	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png	Y	2024-09-18 16:55:42.563576	1000069	2024-09-18 16:55:42.563576	1000069	1000004	45ab22da-9c1e-4ecd-8117-61ddba06bcad	000000025
1000358	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png	Y	2024-09-18 17:00:58.773268	1000069	2024-09-18 17:00:58.773268	1000069	1000004	2e9dc3c2-9aa2-43cb-83a3-5898af30aa4e	000000025
1000359	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png	Y	2024-09-18 17:02:43.466467	1000069	2024-09-18 17:02:43.466467	1000069	1000004	906ec046-f688-4012-aef8-efe0db7ec6db	000000025
1000360	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png	Y	2024-09-18 17:15:52.195207	1000069	2024-09-18 17:15:52.195207	1000069	1000004	b9a989f9-c4ff-4a1f-ab0f-02dff9ac023d	000000025
1000361	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png	Y	2024-09-18 17:19:18.332319	1000069	2024-09-18 17:19:18.332319	1000069	1000004	ec1463b9-d678-43f0-9e06-2f0bc57c29e7	000000025
1000362	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png	Y	2024-09-18 17:19:50.140605	1000069	2024-09-18 17:19:50.140605	1000069	1000004	373ee0e6-b9b5-4274-9424-70f19ae6a22d	000000025
1000363	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png	Y	2024-09-18 21:18:35.021132	1000069	2024-09-18 21:18:35.021132	1000069	1000004	43d0aff7-ee69-455c-88c9-6bf1b3cd6fb7	000000025
1000365	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png	Y	2024-09-18 21:53:32.318019	1000069	2024-09-18 21:53:32.318021	1000069	1000004	eef5921c-6492-40fb-b836-eb938a8931b0	000000025
1000380	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000040.png	Y	2024-09-18 23:25:56.47228	1000069	2024-09-18 23:25:56.47228	1000069	1000004	f692df86-0934-4de9-8715-95df4655ce9a	000000040
1000381	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000041.png	Y	2024-09-18 23:24:42.326395	1000069	2024-09-18 23:24:42.326396	1000069	1000004	1c096493-129e-4d18-9343-d539a03305a0	000000041
1000382	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000042.png	Y	2024-09-18 23:36:56.454522	1000069	2024-09-18 23:36:56.454529	1000069	1000004	ddbe9429-55bc-4fe7-9721-2699ff158742	000000042
1000389	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000049.png	Y	2024-09-19 07:14:07.416301	1000069	2024-09-19 07:14:07.416301	1000069	1000004	e5da0935-ef99-4bb9-8613-91a09b57c41a	000000049
1000392	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000052.png	Y	2024-09-19 11:10:15.773899	1000069	2024-09-19 11:10:15.773901	1000069	1000004	cdc3756d-c483-4d7c-a055-a0fad7c1e4b2	000000052
1000395	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000055.png	Y	2024-09-19 11:27:15.481393	1000069	2024-09-19 11:27:15.481398	1000069	1000004	705dfef9-696a-488d-9e61-a45dd4639042	000000055
1000396	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000056.png	Y	2024-09-19 11:27:16.657808	1000069	2024-09-19 11:27:16.65781	1000069	1000004	c2868894-46a6-4338-a74b-5c608c279512	000000056
1000397	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000057.png	Y	2024-09-19 11:27:17.777937	1000069	2024-09-19 11:27:17.77794	1000069	1000004	7c356edb-4fb5-47c7-9f51-f8d522664395	000000057
1000398	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000058.png	Y	2024-09-19 14:29:54.814244	1000069	2024-09-19 14:29:54.814247	1000069	1000004	63e9b9a4-07e1-49ca-8ee7-184260a676e2	000000058
1000399	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000059.png	Y	2024-09-19 14:29:56.113742	1000069	2024-09-19 14:29:56.113745	1000069	1000004	79eb2887-6999-4b6a-8ed9-cf24292c9193	000000059
1000400	https://assets.digitalbiz.com.vn/Images/4772/F&B/000000060.png	Y	2024-09-19 14:29:57.279932	1000069	2024-09-19 14:29:57.279934	1000069	1000004	6258f842-17d2-42dd-b4a4-982b3addcba8	000000060
\.


--
-- TOC entry 5374 (class 0 OID 385579)
-- Dependencies: 233
-- Data for Name: d_industry; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_industry (d_industry_id, code, name, is_active, created, created_by, updated, updated_by, d_industry_uu, d_tenant_id) FROM stdin;
1000008	4772	Cửa hàng mỹ phẩm	Y	2024-07-09 05:01:13.282466	0	2024-07-09 05:01:13.282466	0	7025cfdf-16eb-418d-a855-646eb1cded33	\N
1000001	5630	Coffee & Tea	Y	2024-07-09 05:01:13.282466	0	2024-07-09 05:01:13.282466	0	292e4993-2c25-41f9-9723-9598136a3d64	\N
1000005	4771	Thời trang	Y	2024-07-09 05:01:13.282466	0	2024-07-09 05:01:13.282466	0	eaed1e2e-316c-44d0-8653-96453af59bc6	\N
1000013	4741	Điện tử máy tính	Y	2024-07-09 05:01:13.282466	0	2024-07-09 05:01:13.282466	0	469c353c-2e19-4530-83bd-2ab14db197d1	\N
1000014	4761	Văn phòng phẩm	Y	2024-07-09 05:01:13.282466	0	2024-07-09 05:01:13.282466	0	28e647d9-098a-4263-a82b-cc4282f62c68	\N
1000002	9329	Quán karaoke	Y	2024-07-09 05:01:13.282466	0	2024-07-09 05:01:13.282466	0	b6113380-259f-4cdc-a8f1-28bb05b5e7d9	\N
1000000	5610	Nhà hàng | F&B (Food & Beverage)	Y	2024-07-08 18:22:57.317071	0	2024-07-08 18:22:57.317071	0	e5e9b4f0-f19c-49b0-845c-4610f18588e3	\N
1000010	4741	Cửa hàng điện thoại	Y	2024-07-09 05:01:13.282466	0	2024-07-09 05:01:13.282466	0	a004c3b6-7ae7-442b-b139-0254af23e68c	\N
1000006	4721	Thực phẩm	Y	2024-07-09 05:01:13.282466	0	2024-07-09 05:01:13.282466	0	b0e31d72-c83a-4a4b-b4f4-530e7c7cb3c2	\N
1000003	9329	Quán bida	Y	2024-07-09 05:01:13.282466	0	2024-07-09 05:01:13.282466	0	006670aa-8a2f-4f24-890f-8e9b9da9f7ac	\N
1000011	3290	Cơ sở Sản xuất	Y	2024-07-09 05:01:13.282466	0	2024-07-09 05:01:13.282466	0	fe9b0ff5-649a-4591-90f2-e3dcc4357b86	\N
1000007	4773 	Hoa quà tặng	Y	2024-07-09 05:01:13.282466	0	2024-07-09 05:01:13.282466	0	c31fdb98-99bf-4928-849c-4fe78e2092a4	\N
1000009	4772	Nhà thuốc	Y	2024-07-09 05:01:13.282466	0	2024-07-09 05:01:13.282466	0	d9307207-2c99-4317-b841-0ea4c293d124	\N
1000012	4663	Sắt thép - VLXD	Y	2024-07-09 05:01:13.282466	0	2024-07-09 05:01:13.282466	0	0696de0e-176b-4b3e-a659-cd3a36e01315	\N
1000004	4711	Siêu thị, tạp hóa	Y	2024-07-09 05:01:13.282466	0	2024-07-09 05:01:13.282466	0	20202afb-d20c-4666-a295-0fe6bd1b5f0e	\N
1000015	4541	Xe máy, Phụ tùng	Y	2024-07-09 14:32:23.825227	\N	2024-07-09 14:32:23.825227	\N	38efae84-ff27-4d63-861a-2254150750e6	\N
\.


--
-- TOC entry 5510 (class 0 OID 393795)
-- Dependencies: 373
-- Data for Name: d_integration_history; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_integration_history (d_integration_history_id, d_tenant_id, d_org_id, d_user_id, int_date, int_type, int_flow, int_status, created, created_by, updated, updated_by, d_integration_history_uu, is_active) FROM stdin;
1000006	1000004	0	1000035	2024-09-21 03:55:00	PRO	PTE	COM	2024-09-10 09:48:15.622679	1000069	2024-09-10 09:48:15.622679	1000069	e9aa5a22-3268-41ac-9bd2-2ea02fc0f633	Y
1000007	1000004	0	1000035	2024-09-22 03:55:00	PRO	PTE	COM	2024-09-10 09:51:55.173459	1000069	2024-09-10 09:51:55.173459	1000069	41e3f0de-d000-44e6-b208-8688cf7fd0ea	Y
1000008	1000004	0	1000035	2024-09-22 03:55:00	PRO	ETP	COM	2024-09-10 09:52:27.080392	1000069	2024-09-10 09:52:27.080392	1000069	9df44bc2-04dd-4275-aeaa-e1bfeb7d8c8c	Y
1000009	1000009	0	1000035	2024-09-22 03:55:00	PRO	ETP	COM	2024-09-16 16:05:10.446558	1000071	2024-09-16 16:05:10.446559	1000071	b247bdf3-0a94-432b-8b4f-88145b4af0f6	Y
1000013	1000004	0	1000070	2024-09-17 11:37:46	PRO	ETP	COM	2024-09-17 11:36:47.066782	1000069	2024-09-17 11:36:47.066786	1000069	fe3cc9dd-fe47-4363-b75d-ddf207c0a3a5	Y
1000014	1000004	0	1000070	2024-09-17 11:52:45	PTM	ETP	COM	2024-09-17 11:51:46.190029	1000069	2024-09-17 11:51:46.190035	1000069	2883e68f-bd8c-4121-a0e4-6d26b5f4e331	Y
1000015	1000004	0	1000070	2024-09-17 11:55:55	PTM	PTE	COM	2024-09-17 11:54:56.936713	1000069	2024-09-17 11:54:56.936716	1000069	ad01d185-7af5-4bdd-bd21-fcf5fc9ba202	Y
1000016	1000004	0	1000070	2024-09-17 11:56:14	WHO	PTE	COM	2024-09-17 11:55:14.867282	1000069	2024-09-17 11:55:14.867285	1000069	5644bca5-134d-4b17-b43d-013fa59df019	Y
1000011	1000004	0	1000035	2024-09-17 10:49:56	PRO	ETP	COM	2024-09-17 10:48:57.297851	1000069	2024-09-17 10:48:57.297856	1000069	3144dc9f-6151-4aa8-bf05-23c9c861831c	Y
1000012	1000004	0	1000035	2024-09-17 10:51:02	PRO	PTE	COM	2024-09-17 10:50:07.594844	1000069	2024-09-17 10:50:07.594847	1000069	64e6bb70-6ae8-4374-84b1-91f17f7f9527	Y
1000010	1000009	0	1000035	2024-09-22 03:55:00	PRO	ETP	COM	2024-09-17 10:15:58.46508	1000071	2024-09-17 10:15:58.465083	1000071	83054bae-0e58-407b-8260-d95e6641bedd	Y
1000018	1000004	0	1000070	2024-09-17 16:47:03	ORG	ETP	COM	2024-09-17 16:46:04.728652	1000069	2024-09-17 16:46:04.728654	1000069	65b65486-29f3-4755-a562-8b357b8c6a7c	Y
1000017	1000004	0	1000035	2024-09-22 03:55:00	PRO	ETP	COM	2024-09-17 16:05:30.178677	1000069	2024-09-17 16:05:30.17868	1000069	0f57dc86-6206-4505-9fe7-893f8049a08a	Y
1000019	1000004	0	1000070	2024-09-18 14:50:52	ORG	PTE	COM	2024-09-18 14:45:48.582998	1000069	2024-09-18 14:45:48.583002	1000069	fa8cf0a2-a6d6-4034-a192-52f35314a485	Y
1000020	1000004	0	1000070	2024-09-18 14:50:53	ORG	ETP	COM	2024-09-18 14:45:49.067643	1000069	2024-09-18 14:45:49.067662	1000069	744b014d-f848-421d-b1f7-8690999c1e65	Y
1000021	1000004	0	1000035	2024-09-18 15:13:20.424414	PRO	ETP	COM	2024-09-18 15:13:20.464554	1000069	2024-09-18 15:13:20.464554	1000069	374413be-d9a8-49e7-a1bc-48c797093635	Y
1000023	1000004	0	1000035	2024-09-18 17:54:21.908936	PRO	ETP	COM	2024-09-18 17:54:21.93484	1000069	2024-09-18 17:54:21.93484	1000069	288fd570-bf69-4bbc-8bfd-ce1ff8124aeb	Y
1000024	1000004	0	1000070	2024-09-18 17:54:47.065484	CUS	PTE	COM	2024-09-18 17:54:47.065843	1000069	2024-09-18 17:54:47.065843	1000069	340c60d5-7752-43be-b135-12b7916b114a	Y
1000025	1000004	0	1000070	2024-09-18 17:57:14.158768	PTM	ETP	COM	2024-09-18 17:57:14.159104	1000069	2024-09-18 17:57:14.159105	1000069	26727d21-94c7-44ae-8c56-c0de89e8c948	Y
1000026	1000004	0	1000070	2024-09-18 17:57:35.189391	FLO	PTE	COM	2024-09-18 17:57:35.189773	1000069	2024-09-18 17:57:35.189774	1000069	64163607-bfcf-4719-929d-baf5bc10f741	Y
1000027	1000004	0	1000070	2024-09-18 17:59:31.659904	PRO	PTE	COM	2024-09-18 17:59:31.660413	1000069	2024-09-18 17:59:31.660413	1000069	fd42e023-2ead-4b14-88bb-6800f51ab2c1	Y
1000028	1000004	0	1000070	2024-09-18 18:00:23.133308	CUS	PTE	COM	2024-09-18 18:00:23.133745	1000069	2024-09-18 18:00:23.133745	1000069	e3bfcbba-93fb-42b1-a158-15dee36875e5	Y
1000029	1000004	0	1000070	2024-09-18 18:00:35.275081	PTM	PTE	COM	2024-09-18 18:00:35.275454	1000069	2024-09-18 18:00:35.275454	1000069	075bd892-7893-401e-a77d-aed3011efdf4	Y
1000030	1000004	0	1000070	2024-09-19 00:11:28.013503	PCG	PTE	COM	2024-09-19 00:11:28.017483	1000069	2024-09-19 00:11:28.017484	1000069	bca3ef0a-2283-4aac-b8fd-4ed1bb0ddb19	Y
1000031	1000004	0	1000070	2024-09-19 00:11:59.506199	PRO	ETP	COM	2024-09-19 00:11:59.506733	1000069	2024-09-19 00:11:59.506733	1000069	57f296a6-fa5d-49c9-8ec0-28fedc453ac3	Y
\.


--
-- TOC entry 5375 (class 0 OID 385587)
-- Dependencies: 234
-- Data for Name: d_invoice; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_invoice (d_invoice_id, d_tenant_id, d_org_id, d_customer_id, d_vendor_id, d_order_id, document_no, date_invoiced, d_doctype_id, d_currency_id, accounting_date, buyer_name, buyer_tax_code, buyer_email, buyer_address, buyer_phone, total_amount, invoice_status, created, created_by, updated, updated_by, d_invoice_uu, d_pricelist_id, d_user_id, reference_invoice_id, invoice_form, invoice_sign, invoice_no, search_code, search_link, invoice_error, is_active, d_pos_order_id) FROM stdin;
1000014	1000002	1000001	1000010	\N	1000016	000	2024-07-25	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-07-25 03:16:30.856553	1000037	2024-07-25 03:16:30.856553	1000037	2e082a58-09d2-473c-8c5a-07c4e55e5a55	\N	1000037	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000015	1000002	1000001	1000010	\N	1000017	000	2024-07-25	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-07-25 04:16:13.441366	1000037	2024-07-25 04:16:13.441366	1000037	dece18f0-bc5b-4294-ad2d-1c31a5fece4a	\N	1000037	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000016	1000002	1000001	1000010	\N	1000019	000	2024-07-29	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-07-29 04:38:20.403633	1000037	2024-07-29 04:38:20.403633	1000037	b8439dee-e9b7-4b76-bbfa-2c5a500ebe8b	\N	1000037	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000024	1000002	1000001	1000010	\N	1000016	000	2024-07-29	0	13	\N	Test	Test	Test	Test	Test	150000	CO	2024-07-29 05:18:39.048618	1000037	2024-07-29 05:18:39.048618	1000037	c8d7a546-9ec5-48ef-a782-a594890d15d0	\N	1000037	\N	Test	Test	Test	Test	Test	Test	Y	\N
1000029	1000002	1000001	1000010	\N	1000023	000	2024-07-30	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-07-30 03:16:46.12524	1000037	2024-07-30 03:16:46.12524	1000037	f077a141-a21b-496b-ae87-446424e14780	\N	1000037	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000030	1000002	1000001	1000010	\N	1000024	000	2024-07-30	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-07-30 03:24:35.526937	1000037	2024-07-30 03:24:35.526937	1000037	43a2b03d-3dfe-4731-912f-0d3d587b7e44	\N	1000037	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000031	1000002	1000001	1000010	\N	1000025	000	2024-07-30	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-07-30 03:25:08.130968	1000037	2024-07-30 03:25:08.130968	1000037	36f94e5d-1edf-45d3-b58c-fe7467369bed	\N	1000037	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000032	1000002	1000001	1000010	\N	1000027	000	2024-07-31	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-07-31 10:56:36.01942	1000037	2024-07-31 10:56:36.019425	1000037	4d6b4033-c1b2-4de0-9d5a-d91adf4a81da	\N	1000037	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000033	1000002	1000001	1000010	\N	1000028	000	2024-07-31	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-07-31 10:59:26.978306	1000037	2024-07-31 10:59:26.978308	1000037	91cc03af-7393-4e63-ab26-4726eb8b8b47	\N	1000037	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000034	1000003	1000001	1000010	\N	1000029	000	2024-08-09	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-09 15:03:53.322346	1000068	2024-08-09 15:03:53.32235	1000068	58ca5627-ec63-4260-a93c-ddbbec3d5880	\N	1000068	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000037	1000004	1000016	1000010	\N	1000032	000	2024-08-09	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-10 10:00:52.614626	1000069	2024-08-10 10:00:52.614629	1000069	24432e4f-3b49-4f8d-bcfd-c3829c2ecf7c	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000038	1000004	1000016	1000010	\N	1000033	000	2024-08-09	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-10 10:00:53.034217	1000069	2024-08-10 10:00:53.034219	1000069	849fb228-c1a5-435d-aa8b-9cd2ff17e923	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000039	1000004	1000016	1000010	\N	1000034	000	2024-08-09	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-10 10:58:37.699129	1000069	2024-08-10 10:58:37.69913	1000069	d8b9e3b2-1c55-4db7-8078-fb8b9cc5c4e7	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000040	1000004	1000016	1000010	\N	1000035	000	2024-08-09	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-10 11:43:05.110472	1000069	2024-08-10 11:43:05.110474	1000069	b5f989b1-8dce-4b7c-b969-f3a90d2fdb4d	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000041	1000004	1000016	1000010	\N	1000036	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:04:55.802837	1000069	2024-08-12 10:04:55.802842	1000069	c2942316-d290-41f4-a236-43d293bfd36f	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000042	1000004	1000016	1000010	\N	1000037	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:10:48.4353	1000069	2024-08-12 10:10:48.435302	1000069	47361e0d-0b65-4170-bd94-23debd23fd7d	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000043	1000004	1000016	1000010	\N	1000038	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:11:12.677795	1000069	2024-08-12 10:11:12.677796	1000069	91e1dd65-dfc8-45ed-b21a-70b893eb7d14	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000044	1000004	1000016	1000010	\N	1000039	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:19:11.092536	1000069	2024-08-12 10:19:11.092538	1000069	60fa5c93-2bea-48aa-be7d-c583e7c25d61	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000045	1000004	1000016	1000010	\N	1000040	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:23:20.069688	1000069	2024-08-12 10:23:20.06969	1000069	76500cad-74e4-4f6f-8f5c-81854d51f8e3	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000046	1000004	1000016	1000010	\N	1000041	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:29:07.64286	1000069	2024-08-12 10:29:07.642862	1000069	7a3335ff-86f2-430f-8fb0-3ce0b96d36c4	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000047	1000004	1000016	1000010	\N	1000042	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:29:38.133544	1000069	2024-08-12 10:29:38.133546	1000069	18158ad0-cea8-464d-a130-9f6cf35f2246	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000048	1000004	1000016	1000010	\N	1000043	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:33:26.311555	1000069	2024-08-12 10:33:26.311556	1000069	7eda6c50-a467-46d1-9d34-cbe90c64564a	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000049	1000004	1000016	1000010	\N	1000044	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:36:41.522222	1000069	2024-08-12 10:36:41.522224	1000069	49630455-cf4b-4d48-a7fb-53366226e569	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000050	1000004	1000016	1000010	\N	1000045	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:38:20.315314	1000069	2024-08-12 10:38:20.315316	1000069	013b087a-e616-437f-8884-b179dd239c0c	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000051	1000004	1000016	1000010	\N	1000046	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:44:29.378386	1000069	2024-08-12 10:44:29.378387	1000069	386f515e-2f9c-4d1b-a2ac-2ed9f5331131	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000052	1000004	1000016	1000010	\N	1000047	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:50:23.537054	1000069	2024-08-12 10:50:23.537056	1000069	b3dc6d68-9b48-4055-8d2b-5cc857f0b52d	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000053	1000004	1000016	1000010	\N	1000048	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:51:02.644781	1000069	2024-08-12 10:51:02.644783	1000069	34d20e5d-44e7-4b51-810d-4ff56a2b9f40	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000054	1000004	1000016	1000010	\N	1000049	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:55:36.694081	1000069	2024-08-12 10:55:36.694083	1000069	c7983ea4-e15a-46d0-80bd-30785e14f0b8	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000055	1000004	1000016	1000010	\N	1000050	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:57:12.962378	1000069	2024-08-12 10:57:12.96238	1000069	abe731e0-8669-40f2-a833-59cc4f5077a7	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000056	1000004	1000016	1000010	\N	1000051	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:57:29.700396	1000069	2024-08-12 10:57:29.700397	1000069	d6ec9808-b2b0-499c-9442-81c058b47ac9	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000057	1000004	1000016	1000010	\N	1000052	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:58:53.817981	1000069	2024-08-12 10:58:53.817982	1000069	f4863b31-ee18-4eac-9b18-7456a039b65e	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000058	1000004	1000016	1000010	\N	1000053	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 10:59:07.960477	1000069	2024-08-12 10:59:07.960478	1000069	a91bfa3a-845f-4789-8ad1-e2578ff05687	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000059	1000004	1000016	1000010	\N	1000054	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 11:01:04.063071	1000069	2024-08-12 11:01:04.063071	1000069	c84d5e77-c3c9-4242-befa-e94a93d63ace	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000060	1000004	1000016	1000010	\N	1000055	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 11:01:52.026284	1000069	2024-08-12 11:01:52.026285	1000069	9a3e6f7b-b482-4831-a75d-876d9c4f8969	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000061	1000004	1000016	1000010	\N	1000056	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 11:07:40.208311	1000069	2024-08-12 11:07:40.208311	1000069	08142c16-f744-4457-8a11-b22e0b921f2a	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000062	1000004	1000016	1000010	\N	1000057	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 11:09:02.017672	1000069	2024-08-12 11:09:02.017672	1000069	932b6082-67b1-42fb-a4c3-c0266a9b201c	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000063	1000004	1000016	1000010	\N	1000058	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 11:09:15.127233	1000069	2024-08-12 11:09:15.127234	1000069	5fa9f106-79a2-4d1d-a60f-180ac02cd09e	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000064	1000004	1000016	1000010	\N	1000059	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 11:09:57.420731	1000069	2024-08-12 11:09:57.420731	1000069	19af47bf-2455-47c3-8f87-d42cc773c337	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000065	1000004	1000016	1000010	\N	1000060	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 11:10:42.929478	1000069	2024-08-12 11:10:42.929479	1000069	b21860b8-5624-4dd3-b0bb-68515515c78a	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000066	1000004	1000016	1000010	\N	1000061	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 11:11:01.810761	1000069	2024-08-12 11:11:01.810762	1000069	756710e6-e5b4-4eac-9456-071529c97987	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000067	1000004	1000016	1000010	\N	1000062	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 11:15:13.727484	1000069	2024-08-12 11:15:13.727484	1000069	f8577921-7bbd-43d1-a263-9152055e0a84	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000068	1000004	1000016	1000010	\N	1000063	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 11:18:34.790504	1000069	2024-08-12 11:18:34.790505	1000069	e2e6fafd-36f6-456e-beef-6554511eca6f	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000069	1000004	1000016	1000010	\N	1000064	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 14:35:56.10608	1000069	2024-08-12 14:35:56.106081	1000069	9f0b9731-4c15-452c-9bcf-e337239fa9e4	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000070	1000004	1000016	1000010	\N	1000065	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-12 22:44:34.085045	1000069	2024-08-12 22:44:34.085046	1000069	e6246688-d6f6-451b-ab1c-ae709bb32b57	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000071	1000004	1000016	1000010	\N	1000066	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-13 18:43:45.12981	1000069	2024-08-13 18:43:45.129811	1000069	0699f939-a816-4675-87cd-b6408d1789a7	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000072	1000004	1000016	1000010	\N	1000067	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-14 11:24:37.323779	1000069	2024-08-14 11:24:37.32378	1000069	dc13bc97-ad83-4fb4-b9e0-6fb213a211c9	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000073	1000004	1000016	1000010	\N	1000068	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-14 11:26:10.288647	1000069	2024-08-14 11:26:10.288648	1000069	70821aec-e6a4-4ff8-8117-1aaf96d1cac2	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000074	1000004	1000016	1000010	\N	1000069	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-14 11:27:04.413018	1000069	2024-08-14 11:27:04.413019	1000069	d9ffb404-da6d-4fa6-9423-43e0833aa69b	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000075	1000004	1000016	1000010	\N	1000070	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-14 23:12:30.297126	1000069	2024-08-14 23:12:30.297126	1000069	f59f5d48-2873-40a9-872d-66839735947b	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000076	1000004	1000016	1000010	\N	1000071	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-15 16:14:24.148516	1000069	2024-08-15 16:14:24.148518	1000069	163ba57d-3dfa-4e4e-8d8a-92f176336688	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000077	1000004	1000016	1000010	\N	1000072	000	2024-08-12	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-17 15:11:36.321701	1000069	2024-08-17 15:11:36.321703	1000069	4fac8601-de59-4182-8c7a-d3a6ebc0814e	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000078	1000004	1000016	1000010	\N	1000073	000	2024-08-24	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-24 12:12:48.165933	1000069	2024-08-24 12:12:48.165938	1000069	c50f0bfc-6241-49bb-a8d2-ffa2574577c9	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000079	1000004	1000016	1000010	\N	1000074	000	2024-08-26	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-26 09:51:12.231926	1000069	2024-08-26 09:51:12.23193	1000069	08fff91c-fd90-4ca8-87f5-77f24c658ce0	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000080	1000004	1000016	1000010	\N	1000075	000	2024-08-26	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-26 09:51:35.688016	1000069	2024-08-26 09:51:35.688018	1000069	07b928f5-bd9e-4abd-b63d-caa96c7fa0e0	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000081	1000004	1000016	1000010	\N	1000076	000	2024-08-26	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-27 10:27:48.182925	1000069	2024-08-27 10:27:48.182928	1000069	26fd2e88-3fe0-4d18-99d8-6970120cc989	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000082	1000004	1000016	1000010	\N	1000077	000	2024-08-26	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-27 12:03:16.565077	1000069	2024-08-27 12:03:16.565079	1000069	584930c2-854e-4f2d-8f70-3780a77a5644	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000083	1000004	1000016	1000010	\N	1000078	000	2024-08-26	0	13	\N	\N	\N	\N	\N	\N	\N	CO	2024-08-30 18:50:10.776013	1000069	2024-08-30 18:50:10.776384	1000069	34509544-a596-4310-9828-c941b6744291	\N	1000069	\N	\N	\N	\N	\N	\N	\N	Y	\N
1000085	1000004	1000016	1000085	\N	\N	AR1000084	2024-09-08	0	13	\N	\N	\N	\N	\N	\N	\N	COL	2024-09-08 16:51:46.8167	1000069	2024-09-08 16:51:46.8167	1000069	1d2b54dc-37eb-42cc-aa67-f6d143bfc85b	1000007	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000192
1000087	1000004	1000016	1000085	\N	\N	AR1000086	2024-09-08	0	13	\N	\N	\N	\N	\N	\N	\N	COL	2024-09-08 17:21:35.22701	1000069	2024-09-08 17:21:35.22701	1000069	3e101618-85e9-4348-b66e-4ef42702c9e3	1000007	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000194
1000089	1000004	1000016	1000085	\N	\N	AR1000088	2024-09-08	0	13	\N	\N	\N	\N	\N	\N	\N	COL	2024-09-08 17:22:54.881396	1000069	2024-09-08 17:22:54.881396	1000069	6324993a-9c09-4bc0-91fe-c38059d10bda	1000007	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000194
1000091	1000004	1000016	1000085	\N	\N	AR1000090	2024-09-08	0	13	\N	\N	\N	\N	\N	\N	\N	COL	2024-09-08 17:32:12.508311	1000069	2024-09-08 17:32:12.508311	1000069	7d175885-6017-46a1-a8b7-3204d69d52d5	1000007	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000194
1000093	1000004	1000016	1000085	\N	\N	AR1000092	2024-09-08	0	13	\N	\N	\N	\N	\N	\N	\N	COL	2024-09-08 17:45:09.230645	1000069	2024-09-08 17:45:09.230645	1000069	94c62412-5a40-43a8-aebd-fdb2e35ad968	1000007	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000194
1000095	1000004	1000016	1000085	\N	\N	AR1000094	2024-09-08	0	13	\N	\N	\N	\N	\N	\N	\N	COL	2024-09-08 17:52:41.827834	1000069	2024-09-08 17:52:41.827834	1000069	5a42ffd6-6ebc-445f-b112-c275bb918aff	1000007	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000194
1000097	0	1000016	1000085	\N	\N	AR1000096	2024-09-08	0	13	\N	\N	\N	\N	\N	\N	\N	COL	2024-09-08 11:21:11.289679	1000069	2024-09-08 11:21:11.289681	0	2ddf9c85-de00-40d2-bdf1-628aed343f6d	1000007	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000198
1000099	0	1000016	1000085	\N	\N	AR1000098	2024-09-09	0	13	\N	\N	\N	\N	\N	\N	\N	COL	2024-09-09 00:32:07.915488	1000069	2024-09-09 00:32:07.915488	0	29f1bdcc-30d6-4969-9eca-4a92de11adef	1000007	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000204
1000101	0	1000016	1000085	\N	\N	AR1000100	2024-09-09	0	13	\N	\N	\N	\N	\N	\N	\N	COL	2024-09-09 00:38:21.104372	1000069	2024-09-09 00:38:21.104372	0	f677ca8d-6658-49da-a830-2db7beff9d66	1000007	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000204
1000103	1000004	1000016	1000085	\N	\N	AR1000102	2024-09-09	0	13	\N	\N	\N	\N	\N	\N	\N	COL	2024-09-09 00:46:56.091715	1000069	2024-09-09 00:46:56.091715	0	12eb8f0b-0141-4a2a-9d11-e7cecf3db2f9	1000007	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000204
1000105	1000004	1000016	1000085	\N	\N	AR1000104	2024-09-09	0	13	\N	\N	\N	\N	\N	\N	\N	COL	2024-09-09 00:50:02.644718	1000069	2024-09-09 00:50:02.644718	0	73aa4f2e-a5c5-4c8b-bf2d-f00b4f20b97c	1000007	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000204
1000107	1000004	1000016	1000085	\N	\N	AR1000106	2024-09-09	0	13	\N	\N	\N	\N	\N	\N	\N	COL	2024-09-09 00:50:58.474284	1000069	2024-09-09 00:50:58.474284	0	280eec20-58c4-46da-8713-90ac1bc9a6a7	1000007	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000204
1000109	1000004	1000016	\N	\N	\N	AR1000108	2024-09-18	0	13	\N	\N	\N	\N	\N	\N	2221600	COM	2024-09-18 06:59:58.573626	0	2024-09-18 06:59:58.573626	0	791969ea-a160-4836-ac89-90cf8cfb0dd4	\N	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000489
1000111	1000004	1000016	\N	\N	\N	AR1000110	2024-09-18	0	13	\N	\N	\N	\N	\N	\N	2221600	COM	2024-09-18 07:02:11.089906	0	2024-09-18 07:02:11.089906	0	f7976095-aa3a-4265-8d05-79927ccf2e84	\N	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000489
1000113	1000004	1000016	\N	\N	\N	AR1000112	2024-09-18	0	13	\N	\N	\N	\N	\N	\N	2221600	COM	2024-09-18 07:18:08.983625	0	2024-09-18 07:18:08.983626	0	41cc221f-c08f-4c83-931a-ff2810fe1c92	\N	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000489
1000115	1000004	1000016	\N	\N	\N	AR1000114	2024-09-18	0	13	\N	\N	\N	\N	\N	\N	2221600	COM	2024-09-18 07:30:03.819028	0	2024-09-18 07:30:03.819029	0	103bc9b0-efec-4059-b1c0-ce4ff7cbbf9e	\N	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000489
1000117	1000004	1000016	\N	\N	\N	AR1000116	2024-09-18	0	13	\N	\N	\N	\N	\N	\N	2221600	COM	2024-09-18 07:41:29.003662	0	2024-09-18 07:41:29.003662	0	1cc9f43d-d0b6-43ad-a980-ef716a35901e	\N	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000491
1000119	1000004	1000016	\N	\N	\N	AR1000118	2024-09-18	0	13	\N	\N	\N	\N	\N	\N	2221600	COM	2024-09-18 08:24:09.29804	0	2024-09-18 08:24:09.298041	0	42181dfb-910b-40dc-b30d-1ef5222f2e5f	\N	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000493
1000121	1000004	1000016	\N	\N	\N	AR1000120	2024-09-18	0	13	\N	\N	\N	\N	\N	\N	2221600	COM	2024-09-18 08:26:10.608118	0	2024-09-18 08:26:10.608119	0	f48d4275-df97-4725-975b-2fc87a25eb70	\N	1000042	\N	\N	\N	\N	\N	\N	\N	Y	1000495
\.


--
-- TOC entry 5376 (class 0 OID 385611)
-- Dependencies: 235
-- Data for Name: d_invoiceline; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_invoiceline (d_invoiceline_id, d_invoice_id, d_tenant_id, d_org_id, d_product_id, d_tax_id, lineno, qty, price_entered, linenet_amt, grand_total, d_invoiceline_uu, created, created_by, updated, updated_by, is_active, d_orderline_id, d_pos_orderline_id) FROM stdin;
1000014	1000014	1000002	1000001	1000087	\N	\N	2	30000	100000	100000	a0512516-0149-4607-87d9-6b23b6ea53a8	2024-07-25 03:16:30.869397	1000037	2024-07-25 03:16:30.869397	1000037	Y	1000014	\N
1000015	1000015	1000002	1000001	1000087	\N	\N	2	30000	100000	100000	2e55f328-6df3-4b0d-a93d-3219f27b28e8	2024-07-25 04:16:13.452915	1000037	2024-07-25 04:16:13.452915	1000037	Y	1000015	\N
1000016	1000016	1000002	1000001	1000087	\N	\N	2	30000	100000	100000	1d94d274-86bd-4990-bea3-f03a2f7d7d8f	2024-07-29 04:38:20.435972	1000037	2024-07-29 04:38:20.435972	1000037	Y	1000036	\N
1000023	1000024	1000002	1000001	1000087	\N	\N	2	30000	100000	100000	b4b2bbd0-5508-4bca-9cb2-3c3be4849fbe	2024-07-29 05:18:39.058301	1000037	2024-07-29 05:18:39.058301	1000037	Y	1000036	\N
1000024	1000016	1000002	1000001	1000087	\N	\N	2	30000	100000	100000	ffce97e8-c8f6-453c-9069-4da521d614ec	2024-07-29 05:29:07.386712	1000037	2024-07-29 05:29:07.386712	1000037	Y	1000036	\N
1000025	1000016	1000002	1000001	1000087	\N	\N	2	30000	100000	100000	f41a681b-3d38-4bf5-86d6-888782a496ca	2024-07-29 05:29:33.800227	1000037	2024-07-29 05:29:33.800227	1000037	Y	1000036	\N
1000026	1000016	1000002	1000001	1000087	\N	\N	22	30000	100000	100000	e852779f-e06e-4158-8c38-b70888cd31ab	2024-07-29 05:29:47.734574	1000037	2024-07-29 05:29:47.734574	1000037	Y	1000036	\N
1000029	1000029	1000002	1000001	1000087	\N	\N	2	30000	100000	100000	b87572f6-78a3-4aae-82e1-d8adb4749ece	2024-07-30 03:16:46.13377	1000037	2024-07-30 03:16:46.13377	1000037	Y	1000040	\N
1000030	1000030	1000002	1000001	1000087	\N	\N	2	30000	100000	100000	c441326d-356c-409f-80c5-06b37a55e930	2024-07-30 03:24:35.534936	1000037	2024-07-30 03:24:35.534936	1000037	Y	1000041	\N
1000031	1000031	1000002	1000001	1000087	\N	\N	2	30000	100000	100000	3526350d-f91b-45e8-ab05-bbb623e77c20	2024-07-30 03:25:08.142767	1000037	2024-07-30 03:25:08.142767	1000037	Y	1000042	\N
1000032	1000032	1000002	1000001	1000087	\N	\N	2	30000	100000	100000	9d138b66-e82b-4a47-adcf-17acbd21e7e8	2024-07-31 10:56:36.078328	1000037	2024-07-31 10:56:36.07833	1000037	Y	1000044	\N
1000033	1000033	1000002	1000001	1000087	\N	\N	2	30000	100000	100000	c5685770-267a-4a79-9489-81b4ba297a6d	2024-07-31 10:59:26.979965	1000037	2024-07-31 10:59:26.979967	1000037	Y	1000045	\N
1000034	1000034	1000003	1000001	1000087	\N	\N	2	30000	100000	100000	071982e0-cc69-469a-b345-5ef7b7036683	2024-08-09 15:03:53.389484	1000068	2024-08-09 15:03:53.389485	1000068	Y	1000046	\N
1000036	1000037	1000004	1000016	1000140	\N	\N	1	100	100000	100000	1ba0e48c-abde-4e79-9412-5654ef4335c7	2024-08-10 10:00:52.638001	1000069	2024-08-10 10:00:52.638004	1000069	Y	1000048	\N
1000037	1000037	1000004	1000016	1000139	\N	\N	1	100	100000	100000	5056e5dd-bed0-40d6-8146-8a8ee0607561	2024-08-10 10:00:52.640789	1000069	2024-08-10 10:00:52.64079	1000069	Y	1000049	\N
1000038	1000037	1000004	1000016	1000138	\N	\N	1	100	100000	100000	2c2b186d-77a2-4c4b-a6f6-f8923d51ca81	2024-08-10 10:00:52.642947	1000069	2024-08-10 10:00:52.642948	1000069	Y	1000050	\N
1000039	1000037	1000004	1000016	1000130	\N	\N	1	100	100000	100000	218e2266-0bf2-4455-bd19-73a4afc8c658	2024-08-10 10:00:52.648467	1000069	2024-08-10 10:00:52.648468	1000069	Y	1000051	\N
1000040	1000038	1000004	1000016	1000140	\N	\N	1	100	100000	100000	7f051da3-baba-4554-b9a7-e765e2c01006	2024-08-10 10:00:53.039484	1000069	2024-08-10 10:00:53.039486	1000069	Y	1000052	\N
1000041	1000038	1000004	1000016	1000139	\N	\N	1	100	100000	100000	1e161146-0613-49b0-8364-2b783252d662	2024-08-10 10:00:53.043777	1000069	2024-08-10 10:00:53.043778	1000069	Y	1000053	\N
1000042	1000038	1000004	1000016	1000138	\N	\N	1	100	100000	100000	76019fdf-3962-432f-81e7-4398f29d3e21	2024-08-10 10:00:53.049933	1000069	2024-08-10 10:00:53.049934	1000069	Y	1000054	\N
1000043	1000038	1000004	1000016	1000130	\N	\N	1	100	100000	100000	1e0c2057-8273-4ba4-97d6-b46c71a9e7fa	2024-08-10 10:00:53.052917	1000069	2024-08-10 10:00:53.052918	1000069	Y	1000055	\N
1000044	1000041	1000004	1000016	1000139	\N	\N	1	100	100000	100000	fcf59e51-aaf8-45ba-b1ca-8e7c74915fbe	2024-08-12 10:04:55.867415	1000069	2024-08-12 10:04:55.867418	1000069	Y	1000056	\N
1000045	1000041	1000004	1000016	1000138	\N	\N	1	100	100000	100000	46684ae9-f77d-447a-aebd-dce1995004f6	2024-08-12 10:04:55.869769	1000069	2024-08-12 10:04:55.869771	1000069	Y	1000057	\N
1000046	1000042	1000004	1000016	1000140	\N	\N	1	100	100000	100000	ca8cc3a7-ff4b-4df9-8e07-9af1e22dc90c	2024-08-12 10:10:48.440216	1000069	2024-08-12 10:10:48.440218	1000069	Y	1000058	\N
1000047	1000042	1000004	1000016	1000139	\N	\N	1	100	100000	100000	d4f6b094-d690-4ae8-af83-e67b2898c1ae	2024-08-12 10:10:48.445523	1000069	2024-08-12 10:10:48.445524	1000069	Y	1000059	\N
1000048	1000043	1000004	1000016	1000140	\N	\N	1	100	100000	100000	112f3973-5a6d-47c1-b0c7-d06373cabb25	2024-08-12 10:11:12.682169	1000069	2024-08-12 10:11:12.682171	1000069	Y	1000060	\N
1000049	1000043	1000004	1000016	1000139	\N	\N	1	100	100000	100000	74a57f62-761e-464c-b4f9-a41dac1dcbfb	2024-08-12 10:11:12.686917	1000069	2024-08-12 10:11:12.686918	1000069	Y	1000061	\N
1000050	1000044	1000004	1000016	1000139	\N	\N	1	100	100000	100000	23958fe6-1e62-4ae8-b9a9-391f3dbf7e83	2024-08-12 10:19:11.098918	1000069	2024-08-12 10:19:11.09892	1000069	Y	1000062	\N
1000051	1000044	1000004	1000016	1000138	\N	\N	1	100	100000	100000	cc250821-1688-461b-8ede-b6d2980bd639	2024-08-12 10:19:11.104927	1000069	2024-08-12 10:19:11.104928	1000069	Y	1000063	\N
1000052	1000045	1000004	1000016	1000140	\N	\N	1	100	100000	100000	3c1dd9d9-1ad2-4705-8149-ccc106e10da7	2024-08-12 10:23:20.075221	1000069	2024-08-12 10:23:20.075223	1000069	Y	1000064	\N
1000053	1000045	1000004	1000016	1000139	\N	\N	1	100	100000	100000	65b376fe-2508-4668-bcd7-59f25b35fdc8	2024-08-12 10:23:20.080096	1000069	2024-08-12 10:23:20.080097	1000069	Y	1000065	\N
1000054	1000046	1000004	1000016	1000139	\N	\N	1	100	100000	100000	96f08be9-6fec-4ddd-a0e7-a043dfb6fee2	2024-08-12 10:29:07.647962	1000069	2024-08-12 10:29:07.647963	1000069	Y	1000066	\N
1000055	1000047	1000004	1000016	1000140	\N	\N	1	100	100000	100000	3f3be200-fa50-4b54-930c-eb3117c9d439	2024-08-12 10:29:38.135069	1000069	2024-08-12 10:29:38.13507	1000069	Y	1000067	\N
1000056	1000048	1000004	1000016	1000139	\N	\N	1	100	100000	100000	731225c9-3306-413b-be3e-537a1b872eb5	2024-08-12 10:33:26.316211	1000069	2024-08-12 10:33:26.316212	1000069	Y	1000068	\N
1000057	1000048	1000004	1000016	1000138	\N	\N	1	100	100000	100000	04f75658-ae54-4f0d-8b6e-931367198710	2024-08-12 10:33:26.321127	1000069	2024-08-12 10:33:26.321128	1000069	Y	1000069	\N
1000058	1000049	1000004	1000016	1000138	\N	\N	1	100	100000	100000	111e4571-418a-4a77-8bc7-22db05051019	2024-08-12 10:36:41.527153	1000069	2024-08-12 10:36:41.527154	1000069	Y	1000070	\N
1000059	1000050	1000004	1000016	1000139	\N	\N	1	100	100000	100000	1c5cdaff-5bde-4af7-aa07-6849d4f77caa	2024-08-12 10:38:20.321584	1000069	2024-08-12 10:38:20.321586	1000069	Y	1000071	\N
1000060	1000051	1000004	1000016	1000140	\N	\N	1	100	100000	100000	bd1f98d9-c221-4c7b-85ad-e4fab6570679	2024-08-12 10:44:29.383015	1000069	2024-08-12 10:44:29.383016	1000069	Y	1000072	\N
1000061	1000052	1000004	1000016	1000140	\N	\N	1	100	100000	100000	6fe70e48-6358-45d1-acd2-91196803017c	2024-08-12 10:50:23.54323	1000069	2024-08-12 10:50:23.543232	1000069	Y	1000073	\N
1000062	1000052	1000004	1000016	1000139	\N	\N	1	100	100000	100000	18139bbd-6f49-4a42-b8fa-473ab096b1e7	2024-08-12 10:50:23.548004	1000069	2024-08-12 10:50:23.548005	1000069	Y	1000074	\N
1000063	1000053	1000004	1000016	1000140	\N	\N	1	100	100000	100000	cf9a3ac6-9376-4b94-8b86-623e5f4cb482	2024-08-12 10:51:02.650023	1000069	2024-08-12 10:51:02.650025	1000069	Y	1000075	\N
1000064	1000053	1000004	1000016	1000139	\N	\N	1	100	100000	100000	c627cbb6-cf6e-4acf-9808-a4b8a0a95e62	2024-08-12 10:51:02.655064	1000069	2024-08-12 10:51:02.655065	1000069	Y	1000076	\N
1000065	1000054	1000004	1000016	1000139	\N	\N	1	100	100000	100000	8d925448-f1df-4ad2-8b82-6f906747d832	2024-08-12 10:55:36.699194	1000069	2024-08-12 10:55:36.699195	1000069	Y	1000077	\N
1000066	1000055	1000004	1000016	1000140	\N	\N	1	100	100000	100000	7c5efa07-94bf-4dfb-adba-1895bb38b721	2024-08-12 10:57:12.966709	1000069	2024-08-12 10:57:12.96671	1000069	Y	1000078	\N
1000067	1000056	1000004	1000016	1000139	\N	\N	1	100	100000	100000	dfa94543-6348-4acf-9b08-9f3dc806330b	2024-08-12 10:57:29.704325	1000069	2024-08-12 10:57:29.704325	1000069	Y	1000079	\N
1000068	1000057	1000004	1000016	1000140	\N	\N	1	100	100000	100000	2c97e579-c1a4-43bb-b6c4-9c94ba7a852e	2024-08-12 10:58:53.820888	1000069	2024-08-12 10:58:53.820888	1000069	Y	1000080	\N
1000069	1000058	1000004	1000016	1000140	\N	\N	1	100	100000	100000	94762299-a487-4e22-914f-ca91eb22f6ad	2024-08-12 10:59:07.965449	1000069	2024-08-12 10:59:07.965449	1000069	Y	1000081	\N
1000070	1000059	1000004	1000016	1000140	\N	\N	1	100	100000	100000	ae432f96-c492-4395-b22c-4fced5fda009	2024-08-12 11:01:04.069949	1000069	2024-08-12 11:01:04.069949	1000069	Y	1000082	\N
1000071	1000060	1000004	1000016	1000140	\N	\N	1	100	100000	100000	8eaae0d1-7a24-4b97-8a68-e2c0cf83d469	2024-08-12 11:01:52.031382	1000069	2024-08-12 11:01:52.031382	1000069	Y	1000083	\N
1000072	1000061	1000004	1000016	1000140	\N	\N	1	100	100000	100000	d0ec4f51-3fd0-4885-a588-16635fc73625	2024-08-12 11:07:40.213833	1000069	2024-08-12 11:07:40.213834	1000069	Y	1000084	\N
1000073	1000061	1000004	1000016	1000139	\N	\N	1	100	100000	100000	5c849a0f-a134-4c5d-bd94-fdedeb5ffc95	2024-08-12 11:07:40.218258	1000069	2024-08-12 11:07:40.218258	1000069	Y	1000085	\N
1000074	1000062	1000004	1000016	1000140	\N	\N	1	100	100000	100000	fdba1bc0-39e4-4dca-9857-b1bb8c4d06fd	2024-08-12 11:09:02.022537	1000069	2024-08-12 11:09:02.022538	1000069	Y	1000086	\N
1000075	1000063	1000004	1000016	1000140	\N	\N	1	100	100000	100000	2a29239d-2fe3-421f-be47-8a92519aa020	2024-08-12 11:09:15.132322	1000069	2024-08-12 11:09:15.132322	1000069	Y	1000087	\N
1000076	1000064	1000004	1000016	1000140	\N	\N	1	100	100000	100000	0612151b-7b0c-4e73-886d-2719e85ca59a	2024-08-12 11:09:57.425275	1000069	2024-08-12 11:09:57.425276	1000069	Y	1000088	\N
1000077	1000065	1000004	1000016	1000140	\N	\N	1	100	100000	100000	7123b71f-a2d7-44a0-8822-16616b0f8266	2024-08-12 11:10:42.932561	1000069	2024-08-12 11:10:42.932561	1000069	Y	1000089	\N
1000078	1000066	1000004	1000016	1000140	\N	\N	1	100	100000	100000	f9c29fae-3695-4673-b8fe-5eeed8a6da4c	2024-08-12 11:11:01.815951	1000069	2024-08-12 11:11:01.815951	1000069	Y	1000090	\N
1000079	1000067	1000004	1000016	1000140	\N	\N	1	100	100000	100000	83c4934b-1c39-4f0a-8ee5-f4b01e2abc05	2024-08-12 11:15:13.733839	1000069	2024-08-12 11:15:13.733839	1000069	Y	1000091	\N
1000080	1000068	1000004	1000016	1000140	\N	\N	1	100	100000	100000	888d1852-9a22-4990-889d-8b93e88c2b00	2024-08-12 11:18:34.796113	1000069	2024-08-12 11:18:34.796114	1000069	Y	1000092	\N
1000081	1000069	1000004	1000016	1000139	\N	\N	1	100	100000	100000	1954df88-4446-445f-8b17-2ebe0f90b707	2024-08-12 14:35:56.109543	1000069	2024-08-12 14:35:56.109544	1000069	Y	1000093	\N
1000082	1000070	1000004	1000016	1000140	\N	\N	1	100	100000	100000	02138fe7-4981-4ed9-830f-338c27db02b9	2024-08-12 22:44:34.089789	1000069	2024-08-12 22:44:34.08979	1000069	Y	1000094	\N
1000083	1000070	1000004	1000016	1000139	\N	\N	1	100	100000	100000	dfc56692-c0a4-43bd-b78e-de5d62988f7a	2024-08-12 22:44:34.095611	1000069	2024-08-12 22:44:34.095611	1000069	Y	1000095	\N
1000084	1000070	1000004	1000016	1000132	\N	\N	1	100	100000	100000	6d60fa5c-f7e5-4598-827b-e0eb1ec72a4f	2024-08-12 22:44:34.101581	1000069	2024-08-12 22:44:34.101582	1000069	Y	1000096	\N
1000085	1000070	1000004	1000016	1000134	\N	\N	1	100	100000	100000	a234b206-6056-4d64-a2d8-b71774c4de71	2024-08-12 22:44:34.104006	1000069	2024-08-12 22:44:34.104007	1000069	Y	1000097	\N
1000086	1000071	1000004	1000016	1000139	\N	\N	1	100	100000	100000	1baab7c9-23b4-4ac5-a2b2-ead8deb53037	2024-08-13 18:43:45.203218	1000069	2024-08-13 18:43:45.203219	1000069	Y	1000098	\N
1000087	1000071	1000004	1000016	1000138	\N	\N	1	100	100000	100000	4527384d-2d8f-4f43-affb-73e6d52ed575	2024-08-13 18:43:45.205344	1000069	2024-08-13 18:43:45.205344	1000069	Y	1000099	\N
1000088	1000071	1000004	1000016	1000130	\N	\N	1	100	100000	100000	f1398d03-7669-497d-b8c2-d1c6c9558b46	2024-08-13 18:43:45.206375	1000069	2024-08-13 18:43:45.206375	1000069	Y	1000100	\N
1000089	1000071	1000004	1000016	1000132	\N	\N	1	100	100000	100000	183bb939-4bc1-4581-bf03-7e5a177bdaff	2024-08-13 18:43:45.207257	1000069	2024-08-13 18:43:45.207257	1000069	Y	1000101	\N
1000090	1000072	1000004	1000016	1000132	\N	\N	1	100	100000	100000	c22ea423-c3c7-4b7e-ac74-42e54f22d441	2024-08-14 11:24:37.376179	1000069	2024-08-14 11:24:37.376179	1000069	Y	1000102	\N
1000091	1000072	1000004	1000016	1000130	\N	\N	1	100	100000	100000	7ed3ef33-8eef-48e4-9e69-4998c403b0b8	2024-08-14 11:24:37.377495	1000069	2024-08-14 11:24:37.377495	1000069	Y	1000103	\N
1000092	1000072	1000004	1000016	1000129	\N	\N	1	100	100000	100000	95f4dfa5-b596-46a1-9185-0ae1f679a734	2024-08-14 11:24:37.378278	1000069	2024-08-14 11:24:37.378278	1000069	Y	1000104	\N
1000093	1000073	1000004	1000016	1000132	\N	\N	1	100	100000	100000	55949d6b-5f0d-4077-948b-70c259afb501	2024-08-14 11:26:10.290247	1000069	2024-08-14 11:26:10.290247	1000069	Y	1000105	\N
1000094	1000073	1000004	1000016	1000108	\N	\N	1	100	100000	100000	725cb459-ba1f-4c34-8b43-6c58ca8a0e89	2024-08-14 11:26:10.29164	1000069	2024-08-14 11:26:10.29164	1000069	Y	1000106	\N
1000095	1000074	1000004	1000016	1000138	\N	\N	4	100	100000	100000	e9c5ea02-8ca9-4cc0-b6d6-52b5932b424f	2024-08-14 11:27:04.414625	1000069	2024-08-14 11:27:04.414625	1000069	Y	1000107	\N
1000096	1000074	1000004	1000016	1000130	\N	\N	2	100	100000	100000	e20c23d3-ee9a-4ec5-8aea-2573877589cc	2024-08-14 11:27:04.416048	1000069	2024-08-14 11:27:04.416048	1000069	Y	1000108	\N
1000097	1000075	1000004	1000016	1000119	\N	\N	1	100	100000	100000	de01cb84-b0af-416c-b48b-2e7b502ffad6	2024-08-14 23:12:30.307006	1000069	2024-08-14 23:12:30.307006	1000069	Y	1000109	\N
1000098	1000075	1000004	1000016	1000132	\N	\N	1	100	100000	100000	ee7a00eb-11e9-4414-92d4-59a245194adb	2024-08-14 23:12:30.30836	1000069	2024-08-14 23:12:30.308361	1000069	Y	1000110	\N
1000099	1000075	1000004	1000016	1000134	\N	\N	1	100	100000	100000	54e5f7d8-5fba-428c-af9e-a7dc7a55cb44	2024-08-14 23:12:30.309156	1000069	2024-08-14 23:12:30.309156	1000069	Y	1000111	\N
1000100	1000076	1000004	1000016	1000138	\N	\N	1	100	100000	100000	50c2c566-4fe7-4277-b0e4-8f79085d25e4	2024-08-15 16:14:24.172447	1000069	2024-08-15 16:14:24.17245	1000069	Y	1000112	\N
1000101	1000076	1000004	1000016	1000136	\N	\N	1	100	100000	100000	ef2a07e3-49f6-4620-a3b1-311eb91651df	2024-08-15 16:14:24.175162	1000069	2024-08-15 16:14:24.175162	1000069	Y	1000113	\N
1000102	1000077	1000004	1000016	1000140	\N	\N	1	100	100000	100000	4c3db35b-c921-4a9e-a8ec-62c70ff029a7	2024-08-17 15:11:36.401161	1000069	2024-08-17 15:11:36.401162	1000069	Y	1000114	\N
1000103	1000077	1000004	1000016	1000139	\N	\N	1	100	100000	100000	0aba869f-6073-4378-9ac9-0583009bf1db	2024-08-17 15:11:36.403076	1000069	2024-08-17 15:11:36.403077	1000069	Y	1000115	\N
1000104	1000077	1000004	1000016	1000138	\N	\N	1	100	100000	100000	2a6598be-6096-4b9b-b93b-a2b96f947efc	2024-08-17 15:11:36.404219	1000069	2024-08-17 15:11:36.404219	1000069	Y	1000116	\N
1000105	1000077	1000004	1000016	1000130	\N	\N	1	100	100000	100000	90437e19-95bb-439a-9b78-d2ebe8cdb1bb	2024-08-17 15:11:36.405803	1000069	2024-08-17 15:11:36.405804	1000069	Y	1000117	\N
1000106	1000078	1000004	1000016	1000107	\N	\N	1	100	100000	100000	be13e01b-ecb6-40b5-b59e-b9d973ac0891	2024-08-24 12:12:48.2456	1000069	2024-08-24 12:12:48.245603	1000069	Y	1000118	\N
1000107	1000078	1000004	1000016	1000108	\N	\N	1	100	100000	100000	d5f744e7-8716-4e59-be39-12693d404a95	2024-08-24 12:12:48.248786	1000069	2024-08-24 12:12:48.248787	1000069	Y	1000119	\N
1000108	1000078	1000004	1000016	1000139	\N	\N	1	100	100000	100000	cff014e4-b395-4e1b-bb03-dd45233a099b	2024-08-24 12:12:48.249939	1000069	2024-08-24 12:12:48.24994	1000069	Y	1000120	\N
1000109	1000079	1000004	1000016	1000103	\N	\N	3	0	100000	100000	345fce9d-8c35-4e62-8274-8f0a04676d87	2024-08-26 09:51:12.315992	1000069	2024-08-26 09:51:12.315993	1000069	Y	1000121	\N
1000110	1000079	1000004	1000016	1000136	\N	\N	3	0	100000	100000	06664de5-0976-477a-a848-3faa2c801b2f	2024-08-26 09:51:12.317833	1000069	2024-08-26 09:51:12.317835	1000069	Y	1000122	\N
1000111	1000079	1000004	1000016	1000127	\N	\N	3	0	100000	100000	c925f33f-d58e-4f4e-a1b2-a7450f07cf48	2024-08-26 09:51:12.319103	1000069	2024-08-26 09:51:12.319104	1000069	Y	1000123	\N
1000112	1000079	1000004	1000016	1000129	\N	\N	3	0	100000	100000	4ed96faf-9a5c-4bbd-bba6-c9fa520af72d	2024-08-26 09:51:12.320183	1000069	2024-08-26 09:51:12.320184	1000069	Y	1000124	\N
1000113	1000079	1000004	1000016	1000139	\N	\N	1	0	100000	100000	22be57b7-878e-4ebc-ae05-4d1b8d27852b	2024-08-26 09:51:12.321145	1000069	2024-08-26 09:51:12.321146	1000069	Y	1000125	\N
1000114	1000079	1000004	1000016	1000119	\N	\N	1	0	100000	100000	894589f7-163b-43d2-8faa-6f7d98cd965c	2024-08-26 09:51:12.322166	1000069	2024-08-26 09:51:12.322167	1000069	Y	1000126	\N
1000115	1000079	1000004	1000016	1000107	\N	\N	1	0	100000	100000	ee3a3a7b-c0da-4461-9da4-432c410017d4	2024-08-26 09:51:12.323563	1000069	2024-08-26 09:51:12.323564	1000069	Y	1000127	\N
1000116	1000079	1000004	1000016	1000140	\N	\N	1	0	100000	100000	d53882a4-f6de-4a84-bee1-97d7d09f0b15	2024-08-26 09:51:12.324518	1000069	2024-08-26 09:51:12.324519	1000069	Y	1000128	\N
1000117	1000080	1000004	1000016	1000103	\N	\N	3	0	100000	100000	e0ef37ba-df91-4dd6-83f2-957100234d94	2024-08-26 09:51:35.689387	1000069	2024-08-26 09:51:35.689388	1000069	Y	1000129	\N
1000118	1000080	1000004	1000016	1000136	\N	\N	3	0	100000	100000	c298a1ba-2cad-4b36-b85d-a530d758f046	2024-08-26 09:51:35.690285	1000069	2024-08-26 09:51:35.690286	1000069	Y	1000130	\N
1000119	1000080	1000004	1000016	1000127	\N	\N	3	0	100000	100000	1a08d2cf-f83e-4baf-a2d6-291fa01cabad	2024-08-26 09:51:35.691235	1000069	2024-08-26 09:51:35.691236	1000069	Y	1000131	\N
1000120	1000080	1000004	1000016	1000129	\N	\N	3	0	100000	100000	ad34eca3-6fe3-4711-8a8c-802932455930	2024-08-26 09:51:35.692264	1000069	2024-08-26 09:51:35.692265	1000069	Y	1000132	\N
1000121	1000080	1000004	1000016	1000139	\N	\N	2	0	100000	100000	d1cf8218-c18a-4ce1-9844-179a608c68f8	2024-08-26 09:51:35.698418	1000069	2024-08-26 09:51:35.698419	1000069	Y	1000133	\N
1000122	1000080	1000004	1000016	1000119	\N	\N	2	0	100000	100000	af55e80f-761b-4875-a845-6438134f6927	2024-08-26 09:51:35.69974	1000069	2024-08-26 09:51:35.699741	1000069	Y	1000134	\N
1000123	1000080	1000004	1000016	1000107	\N	\N	2	0	100000	100000	b00bac7a-af46-492b-9e22-900397dfb34b	2024-08-26 09:51:35.700959	1000069	2024-08-26 09:51:35.70096	1000069	Y	1000135	\N
1000124	1000080	1000004	1000016	1000140	\N	\N	2	0	100000	100000	11887c89-1d62-4ca3-9c2b-a6743179590c	2024-08-26 09:51:35.702937	1000069	2024-08-26 09:51:35.702938	1000069	Y	1000136	\N
1000125	1000081	1000004	1000016	1000152	\N	\N	1	0	100000	100000	782ba99e-8164-45ba-b676-2140f16eba39	2024-08-27 10:27:48.2644	1000069	2024-08-27 10:27:48.264402	1000069	Y	1000137	\N
1000126	1000081	1000004	1000016	1000151	\N	\N	1	0	100000	100000	6c489287-15c4-4a4a-8f1f-28b18bd2dfa8	2024-08-27 10:27:48.271695	1000069	2024-08-27 10:27:48.271696	1000069	Y	1000138	\N
1000127	1000082	1000004	1000016	1000152	\N	\N	1	0	100000	100000	2ac14d0c-2abc-4520-bf26-c8c002d9e5c8	2024-08-27 12:03:16.568359	1000069	2024-08-27 12:03:16.56836	1000069	Y	1000139	\N
1000128	1000082	1000004	1000016	1000151	\N	\N	1	0	100000	100000	8ad644e5-762f-4bde-97ed-a65d6ba78882	2024-08-27 12:03:16.572641	1000069	2024-08-27 12:03:16.572642	1000069	Y	1000140	\N
1000129	1000083	1000004	1000016	1000356	\N	\N	1	15000	100000	100000	4f83c5ae-ce9a-46d8-a7c5-c705eb857749	2024-08-30 18:50:10.889777	1000069	2024-08-30 18:50:10.889779	1000069	Y	1000141	\N
1000130	1000083	1000004	1000016	1000138	\N	\N	1	100	100000	100000	4ce0f34e-e285-4bbd-8a3c-a579f26cd962	2024-08-30 18:50:10.892894	1000069	2024-08-30 18:50:10.892895	1000069	Y	1000142	\N
1000131	1000085	1000004	1000016	1000353	1000003	\N	1	100000	110000	110000	f8e329f0-72be-476a-83e7-e22775d39ca7	2024-09-08 16:51:47.414622	1000069	2024-09-08 16:51:47.414622	1000069	Y	\N	1000144
1000132	1000085	1000004	1000016	1000132	1000003	\N	1	100000	110000	110000	3e781048-99d6-48ab-9e53-88a071468903	2024-09-08 16:51:47.430355	1000069	2024-09-08 16:51:47.430355	1000069	Y	\N	1000143
1000133	1000087	1000004	1000016	1000132	1000003	\N	1	100000	100000	110000	44b20d74-9c6d-4b27-9ae6-199203849b7a	2024-09-08 17:21:35.300164	1000069	2024-09-08 17:21:35.300164	1000069	Y	\N	1000145
1000134	1000087	1000004	1000016	1000353	1000003	\N	1	100000	100000	110000	ac8e9963-1cca-48b7-a2e2-8cd9c353fc2c	2024-09-08 17:21:35.308681	1000069	2024-09-08 17:21:35.308681	1000069	Y	\N	1000146
1000135	1000089	1000004	1000016	1000132	1000003	\N	1	100000	100000	110000	4374ae2e-6ef5-4efe-b94c-5e63f7882b5c	2024-09-08 17:23:48.201501	1000069	2024-09-08 17:23:48.201501	1000069	Y	\N	1000145
1000136	1000089	1000004	1000016	1000353	1000003	\N	1	100000	100000	110000	5a751270-aa8e-41b4-a7b8-31677e8a1acb	2024-09-08 17:23:48.208002	1000069	2024-09-08 17:23:48.208002	1000069	Y	\N	1000146
1000137	1000091	1000004	1000016	1000132	1000003	\N	1	100000	100000	110000	a7ec2e40-1006-4451-bb2f-73911f2c2356	2024-09-08 17:32:19.88269	1000069	2024-09-08 17:32:19.88269	1000069	Y	\N	1000145
1000138	1000091	1000004	1000016	1000353	1000003	\N	1	100000	100000	110000	36ac9f7e-ae3c-431f-b509-5a248c308dbe	2024-09-08 17:32:19.8959	1000069	2024-09-08 17:32:19.8959	1000069	Y	\N	1000146
1000139	1000093	1000004	1000016	1000132	1000003	\N	1	100000	100000	110000	355e0736-c328-4f02-841d-f2c2151c0048	2024-09-08 17:45:09.241194	1000069	2024-09-08 17:45:09.241194	1000069	Y	\N	1000145
1000140	1000093	1000004	1000016	1000353	1000003	\N	1	100000	100000	110000	4acf8ebc-386c-4f12-abaf-6958515f9cc5	2024-09-08 17:45:09.246721	1000069	2024-09-08 17:45:09.246721	1000069	Y	\N	1000146
1000141	1000095	1000004	1000016	1000132	1000003	\N	1	100000	100000	110000	8ff9d643-46b8-4d80-aaec-5e88899edfc7	2024-09-08 17:52:41.834963	1000069	2024-09-08 17:52:41.834963	1000069	Y	\N	1000145
1000142	1000095	1000004	1000016	1000353	1000003	\N	1	100000	100000	110000	1a921b3f-5ee0-46a0-b77c-453839be37a6	2024-09-08 17:52:41.841962	1000069	2024-09-08 17:52:41.841962	1000069	Y	\N	1000146
1000143	1000097	0	1000016	1000353	1000003	\N	1	100000	100000	110000	93c4c307-fc89-4fc8-9c81-8f1d14c1a60a	2024-09-08 11:21:11.315491	0	2024-09-08 11:21:11.315492	0	Y	\N	1000150
1000144	1000097	0	1000016	1000132	1000003	\N	1	100000	100000	110000	74f90cd3-4b91-42e2-b858-fc7337e150ef	2024-09-08 11:21:11.322883	0	2024-09-08 11:21:11.322884	0	Y	\N	1000149
1000145	1000099	0	1000016	1000353	1000003	\N	1	100000	100000	110000	bb82b948-4e54-4704-8f43-0ff08a70bdb4	2024-09-09 00:32:07.928589	0	2024-09-09 00:32:07.928589	0	Y	\N	1000156
1000146	1000099	0	1000016	1000132	1000003	\N	1	100000	100000	110000	6ff8d0f4-ad2b-410a-88c9-81aac4c24cc4	2024-09-09 00:32:07.934374	0	2024-09-09 00:32:07.934374	0	Y	\N	1000155
1000147	1000101	0	1000016	1000353	1000003	\N	1	100000	100000	110000	1cd8b7cc-662b-4a7e-91f8-d49a038aac5f	2024-09-09 00:38:21.110445	0	2024-09-09 00:38:21.110445	0	Y	\N	1000156
1000148	1000101	0	1000016	1000132	1000003	\N	1	100000	100000	110000	81e6cfd0-e9b7-42d4-be75-87159071ba1b	2024-09-09 00:38:21.115441	0	2024-09-09 00:38:21.115441	0	Y	\N	1000155
1000149	1000103	1000004	1000016	1000353	1000003	\N	1	100000	100000	110000	6208ad98-b02a-4b80-aefe-2cd7958b1c6f	2024-09-09 00:46:56.095769	0	2024-09-09 00:46:56.095769	0	Y	\N	1000156
1000150	1000103	1000004	1000016	1000132	1000003	\N	1	100000	100000	110000	2d6c551c-dd0f-4de5-82e1-9f624a24ca67	2024-09-09 00:46:56.100813	0	2024-09-09 00:46:56.100813	0	Y	\N	1000155
1000151	1000105	1000004	1000016	1000353	1000003	\N	1	100000	100000	110000	251f59f4-3679-4458-aba5-52fc6bf5d9ca	2024-09-09 00:50:02.651863	0	2024-09-09 00:50:02.651863	0	Y	\N	1000156
1000152	1000105	1000004	1000016	1000132	1000003	\N	1	100000	100000	110000	3f1eb656-4da1-45f4-8824-c9e15004d5bc	2024-09-09 00:50:02.656843	0	2024-09-09 00:50:02.656843	0	Y	\N	1000155
1000153	1000107	1000004	1000016	1000353	1000003	\N	1	100000	100000	110000	43dda786-f27e-487d-b58b-bbfc8272e0ab	2024-09-09 00:50:58.479295	0	2024-09-09 00:50:58.479295	0	Y	\N	1000156
1000154	1000107	1000004	1000016	1000132	1000003	\N	1	100000	100000	110000	fa28352f-3afd-46ae-9752-eaddc5adb32f	2024-09-09 00:50:58.484284	0	2024-09-09 00:50:58.484284	0	Y	\N	1000155
1000155	1000109	1000004	1000016	1000425	\N	\N	1	200000	200000	2200000	92e5c75e-18e7-485e-b2c8-7ca700f28d27	2024-09-18 06:59:58.672534	0	2024-09-18 06:59:58.672535	0	Y	\N	1000366
1000156	1000109	1000004	1000016	1000509	\N	\N	1	20000	20000	21600	1cc3d41a-9acd-43f0-8ec8-7857d607aaa0	2024-09-18 06:59:58.682146	0	2024-09-18 06:59:58.682147	0	Y	\N	1000367
1000157	1000111	1000004	1000016	1000425	1000003	\N	1	200000	200000	2200000	dc7619ca-9029-469b-a1ce-bcadc1d86cb8	2024-09-18 07:02:11.110387	0	2024-09-18 07:02:11.110388	0	Y	\N	1000366
1000158	1000111	1000004	1000016	1000509	1000003	\N	1	20000	20000	21600	c6d31a36-83ee-42de-ac87-8350785c7ce3	2024-09-18 07:02:11.119107	0	2024-09-18 07:02:11.119108	0	Y	\N	1000367
1000159	1000113	1000004	1000016	1000425	1000003	\N	1	200000	200000	2200000	48f9f084-92f8-4a54-8974-e047193a7a09	2024-09-18 07:18:09.089385	0	2024-09-18 07:18:09.089386	0	Y	\N	1000366
1000160	1000113	1000004	1000016	1000509	1000003	\N	1	20000	20000	21600	2a8d8d40-8f83-41da-b326-e6efb1df6d0f	2024-09-18 07:18:09.103617	0	2024-09-18 07:18:09.103618	0	Y	\N	1000367
1000161	1000115	1000004	1000016	1000425	1000003	\N	1	200000	200000	2200000	95e494ee-210e-41b5-aacc-baa5e5a525e8	2024-09-18 07:30:03.903818	0	2024-09-18 07:30:03.903818	0	Y	\N	1000366
1000162	1000115	1000004	1000016	1000509	1000003	\N	1	20000	20000	21600	cacefb1d-fdaa-4bed-bafe-005f0aa48ca7	2024-09-18 07:30:03.913769	0	2024-09-18 07:30:03.91377	0	Y	\N	1000367
1000163	1000117	1000004	1000016	1000425	1000003	\N	1	200000	200000	2200000	e77a77b7-679e-4aec-b446-2c8deb364a4d	2024-09-18 07:41:29.013103	0	2024-09-18 07:41:29.013103	0	Y	\N	1000368
1000164	1000117	1000004	1000016	1000509	1000003	\N	1	20000	20000	21600	01f011da-9639-4cb5-9169-2aa2b060e77d	2024-09-18 07:41:29.022618	0	2024-09-18 07:41:29.022619	0	Y	\N	1000369
1000165	1000119	1000004	1000016	1000440	1000003	\N	1	200000	200000	2200000	89b3d0a9-e975-4d22-9ea6-747e2e161d1d	2024-09-18 08:24:09.33268	0	2024-09-18 08:24:09.332681	0	Y	\N	1000370
1000166	1000119	1000004	1000016	1000509	1000003	\N	1	20000	20000	21600	f28a1c84-30e8-48f7-b553-13c56d48417f	2024-09-18 08:24:09.341621	0	2024-09-18 08:24:09.341622	0	Y	\N	1000371
1000167	1000121	1000004	1000016	1000440	1000003	\N	1	200000	200000	2200000	e8b0c1a1-55ab-4fdf-af88-2965dfd2e97c	2024-09-18 08:26:10.617221	0	2024-09-18 08:26:10.617221	0	Y	\N	1000372
1000168	1000121	1000004	1000016	1000509	1000003	\N	1	20000	20000	21600	1b813165-3bcf-4fc2-aefc-2bd84b0c6116	2024-09-18 08:26:10.626267	0	2024-09-18 08:26:10.626267	0	Y	\N	1000373
\.


--
-- TOC entry 5460 (class 0 OID 390871)
-- Dependencies: 319
-- Data for Name: d_kitchen_order; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_kitchen_order (d_kitchen_order_id, d_tenant_id, d_org_id, documentno, d_pos_order_id, d_doctype_id, d_warehouse_id, dateordered, d_user_id, d_floor_id, d_table_id, order_status, description, created, created_by, updated, updated_by, d_kitchen_order_uu, is_active, d_pos_terminal_id, erp_kitchen_order_id, is_sync_erp) FROM stdin;
\.


--
-- TOC entry 5462 (class 0 OID 390892)
-- Dependencies: 321
-- Data for Name: d_kitchen_orderline; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_kitchen_orderline (d_kitchen_orderline_id, d_kitchen_order_id, d_tenant_id, d_org_id, orderline_status, d_product_id, note, qty, transfer_qty, cancel_qty, priority, is_active, description, created, created_by, updated, updated_by, d_kitchen_orderline_uu, d_cancel_reason_id, d_pos_orderline_id, d_production_id) FROM stdin;
\.


--
-- TOC entry 5377 (class 0 OID 385625)
-- Dependencies: 236
-- Data for Name: d_language; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_language (d_language_id, d_tenant_id, d_org_id, language_code, name, country_code, created, created_by, updated, updated_by, d_language_uu) FROM stdin;
\.


--
-- TOC entry 5378 (class 0 OID 385635)
-- Dependencies: 237
-- Data for Name: d_locator; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_locator (d_locator_id, d_org_id, d_tenant_id, created, created_by, updated, updated_by, is_active, code, description, x, y, z, d_warehouse_id, d_locator_uu, name) FROM stdin;
1000002	1000016	1000004	2024-08-12 10:13:09.479545	1000069	2024-08-12 10:13:09.479545	1000069	Y	L1	\N	10	10	10	1000006	35209962-4522-4b63-93e6-03eb30aadcf7	\N
1000000	0	1000002	2024-07-21 23:26:17.306435	1000037	2024-07-21 23:26:17.306435	1000037	Y	code	\N	10	10	10	1000003	156a7490-7fe3-46c1-85d1-b196d5752fc8	\N
\.


--
-- TOC entry 5466 (class 0 OID 390937)
-- Dependencies: 325
-- Data for Name: d_note; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_note (d_note_id, d_note_group_id, d_tenant_id, d_org_id, is_active, name, description, created, created_by, updated, updated_by, d_note_uu, product_category_ids) FROM stdin;
1000000	1000002	1000004	1000016	Y	30% đường	30% đường	2024-08-14 11:05:14.59281	1000069	2024-08-14 11:05:14.59281	1000069	6a9e49da-e28a-4cfb-b2a0-ca2c6b0d30cd	\N
1000001	1000002	1000004	1000016	Y	30% đường	30% đường	2024-08-15 10:07:20.497753	1000069	2024-08-15 10:07:20.497753	1000069	2441fbba-7e48-44bb-9b16-898124f2352a	1000002
1000005	1000001	1000004	1000016	Y	test 2	\N	2024-08-21 14:02:02.177908	1000069	2024-08-27 14:54:08.817006	1000069	4a1ea983-411b-498b-9682-80522f936118	1000017,1000018
1000006	1000006	1000004	1000016	Y	30% Sữaa	\N	2024-08-29 11:55:42.003343	1000069	2024-08-29 11:56:15.952904	1000069	ddd791ed-441c-4cb4-bfb0-e79296d71fac	1000019
\.


--
-- TOC entry 5464 (class 0 OID 390919)
-- Dependencies: 323
-- Data for Name: d_note_group; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_note_group (d_note_group_id, d_tenant_id, d_org_id, is_active, group_name, description, created, created_by, updated, updated_by, d_note_group_uu) FROM stdin;
1000002	1000004	1000016	Y	Sữa 2	Sữa	2024-08-14 11:04:26.198612	1000069	2024-08-21 14:39:14.431429	1000069	d243aa43-e01f-441e-9941-8af35558d708
1000005	1000004	1000016	Y	Kem	Sữa	2024-08-21 14:48:37.934316	1000069	2024-08-21 14:48:37.934318	1000069	514e360b-c5d4-4396-b9e8-2056827ace44
1000006	1000004	1000016	Y	Phô mai	\N	2024-08-21 14:50:04.426453	1000069	2024-08-21 14:50:04.426455	1000069	21d9cf63-e536-4b23-9843-e4e4319a35bf
1000001	1000004	1000016	Y	Đường 3	Đường	2024-08-14 11:03:55.669727	1000069	2024-08-21 14:53:02.619579	1000069	176e3a56-dc6c-4cda-9197-312e351fd153
1000007	1000004	1000016	Y	Sữa	\N	2024-08-29 11:55:15.517202	1000069	2024-08-29 11:55:15.517204	1000069	2a28275c-1c32-4d2d-8721-25afe8ff34e8
\.


--
-- TOC entry 5379 (class 0 OID 385645)
-- Dependencies: 238
-- Data for Name: d_notification; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_notification (d_notification_id, d_tenant_id, title, content, notification_type, created, created_by, updated, updated_by, status, d_notification_uu) FROM stdin;
\.


--
-- TOC entry 5380 (class 0 OID 385682)
-- Dependencies: 239
-- Data for Name: d_order; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_order (d_order_id, d_tenant_id, d_org_id, document_no, d_customer_id, phone, order_status, source, is_locked, d_table_id, d_floor_id, d_user_id, order_guests, is_active, order_date, created, created_by, updated, updated_by, customer_name, d_currency_id, d_pricelist_id, payment_method, d_doctype_id, d_order_uu, total_amount, d_pos_terminal_id) FROM stdin;
1000016	1000002	1000001	000	1000010	0123456789	CO	WEB	N	1000017	999957	1000037	3	Y	2024-07-25	2024-07-25 03:16:30.802435	1000037	2024-07-25 03:16:30.802435	1000037	John Doe	13	\N	\N	0	c40c8ce3-618d-4efe-8e3c-29785b7bdd7c	\N	\N
1000017	1000002	1000001	000	1000010	0123456789	CO	WEB	N	1000017	999957	1000037	3	Y	2024-07-25	2024-07-25 04:16:13.416352	1000037	2024-07-25 04:16:13.416352	1000037	John Doe	13	\N	\N	0	48c05b07-8fdd-49a3-b703-5c026058e8ae	\N	\N
1000019	1000002	1000001	000	1000010	0123456789	CO	WEB	N	1000017	999957	1000037	3	Y	2024-07-29	2024-07-29 04:38:20.377004	1000037	2024-07-29 04:38:20.377004	1000037	John Doe	13	\N	\N	0	9ed8cb88-4f2a-4b3f-a846-8e770401cda6	\N	\N
1000023	1000002	1000001	000	1000010	0123456789	CO	WEB	N	1000017	999957	1000037	3	Y	2024-07-30	2024-07-30 03:16:45.94057	1000037	2024-07-30 03:16:45.94057	1000037	John Doe	13	\N	\N	0	48902a4e-0a6c-4807-87bd-0ed12b6e2d53	\N	\N
1000024	1000002	1000001	000	1000010	0123456789	CO	WEB	N	1000017	999957	1000037	3	Y	2024-07-30	2024-07-30 03:24:35.494934	1000037	2024-07-30 03:24:35.494934	1000037	John Doe	13	\N	\N	0	958f6082-f2d7-48dd-b00e-d0936cd05259	\N	\N
1000025	1000002	1000001	000	1000010	0123456789	CO	WEB	N	1000017	999957	1000037	3	Y	2024-07-30	2024-07-30 03:25:08.084582	1000037	2024-07-30 03:25:08.084582	1000037	John Doe	13	\N	\N	0	cc5007ec-0852-4764-a960-9564f41a95ee	\N	\N
1000027	1000002	1000001	000	1000010	0123456789	CO	WEB	N	1000017	999957	1000037	3	Y	2024-07-31	2024-07-31 10:56:35.681391	1000037	2024-07-31 10:56:35.681393	1000037	John Doe	13	\N	\N	0	85126e8f-be8a-48b3-9c61-d06d70c3c404	\N	\N
1000028	1000002	1000001	000	1000010	0123456789	CO	WEB	N	1000017	999957	1000037	3	Y	2024-07-31	2024-07-31 10:59:26.964678	1000037	2024-07-31 10:59:26.96468	1000037	John Doe	13	\N	\N	0	ce2b70e2-d8a4-40ba-ba88-0aab349156a1	\N	\N
1000029	1000003	1000001	000	1000010	0123456789	CO	WEB	N	1000017	999957	1000068	3	Y	2024-08-09	2024-08-09 15:03:52.755382	1000068	2024-08-09 15:03:52.755386	1000068	John Doe	13	\N	\N	0	97408b3e-1fd4-47c6-8dc5-170c40ae63ad	\N	\N
1000032	1000004	1000016	000	1000010	0123456789	CO	web	N	1000070	0	1000069	1	Y	2024-08-09	2024-08-10 10:00:52.318829	1000069	2024-08-10 10:00:52.31883	1000069	Nguyen Van A	13	\N	\N	0	b9c25263-736e-454e-b689-a7b27ad29cf9	\N	\N
1000033	1000004	1000016	000	1000010	0123456789	CO	web	N	1000070	0	1000069	1	Y	2024-08-09	2024-08-10 10:00:52.986566	1000069	2024-08-10 10:00:52.986566	1000069	Nguyen Van A	13	\N	\N	0	a12adf22-1caa-4716-b734-2b5a2272962b	\N	\N
1000034	1000004	1000016	000	1000010	0123456789	CO	web	N	0	0	1000069	1	Y	2024-08-09	2024-08-10 10:58:37.678252	1000069	2024-08-10 10:58:37.678252	1000069	Nguyen Van A	13	\N	\N	0	ceaaa183-0878-402e-afe8-d5855eaad3f1	\N	\N
1000035	1000004	1000016	000	1000010	0123456789	CO	web	N	0	0	1000069	1	Y	2024-08-09	2024-08-10 11:43:05.095197	1000069	2024-08-10 11:43:05.095198	1000069	Nguyen Van A	13	\N	\N	0	117d9734-3acd-4718-afe4-1b083eb0f646	\N	\N
1000036	1000004	1000016	000	1000010	0123456789	CO	web	N	1000068	0	1000069	1	Y	2024-08-12	2024-08-12 10:04:55.276888	1000069	2024-08-12 10:04:55.276892	1000069	Nguyen Van A	13	\N	\N	0	c3003f38-8ff8-4a64-975d-165b2a2eaff3	\N	\N
1000037	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 10:10:48.407855	1000069	2024-08-12 10:10:48.407857	1000069	Nguyen Van A	13	\N	\N	0	ab633f40-2918-4bfc-bae4-6558e0d163a8	\N	\N
1000038	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 10:11:12.648243	1000069	2024-08-12 10:11:12.648245	1000069	Nguyen Van A	13	\N	\N	0	fae2d464-d11b-432e-9de2-d8e9975a092e	\N	\N
1000039	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 10:19:11.069253	1000069	2024-08-12 10:19:11.069254	1000069	Nguyen Van A	13	\N	\N	0	a0779b60-e76c-4477-a3bd-c5e41a90bf16	\N	\N
1000040	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 10:23:20.042662	1000069	2024-08-12 10:23:20.042664	1000069	Nguyen Van A	13	\N	\N	0	bd59deaf-6d48-4145-acfd-7d8ccf20ff04	\N	\N
1000041	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 10:29:07.623318	1000069	2024-08-12 10:29:07.62332	1000069	Nguyen Van A	13	\N	\N	0	6b5d28d0-420d-4283-8c55-34ca805d8455	\N	\N
1000042	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 10:29:38.114678	1000069	2024-08-12 10:29:38.114679	1000069	Nguyen Van A	13	\N	\N	0	c007d93b-833b-4a6b-be45-e6692918fd43	\N	\N
1000043	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 10:33:26.289245	1000069	2024-08-12 10:33:26.289247	1000069	Nguyen Van A	13	\N	\N	0	c0ecbb7e-5e09-498e-863a-41b20d445a7b	\N	\N
1000044	1000004	1000016	000	1000010	0123456789	CO	web	N	1000070	0	1000069	1	Y	2024-08-12	2024-08-12 10:36:41.504886	1000069	2024-08-12 10:36:41.504888	1000069	Nguyen Van A	13	\N	\N	0	f1f30e5e-055d-4ca1-b53d-b5426ca3fc2e	\N	\N
1000045	1000004	1000016	000	1000010	0123456789	CO	web	N	1000070	0	1000069	1	Y	2024-08-12	2024-08-12 10:38:20.292516	1000069	2024-08-12 10:38:20.292518	1000069	Nguyen Van A	13	\N	\N	0	3de58bb4-efcd-4e7b-b6b5-8dbac966b64b	\N	\N
1000046	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 10:44:29.36241	1000069	2024-08-12 10:44:29.362411	1000069	Nguyen Van A	13	\N	\N	0	c70f0c2e-5e43-4b15-ba2f-7fd4b3b8d739	\N	\N
1000047	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 10:50:23.511412	1000069	2024-08-12 10:50:23.511413	1000069	Nguyen Van A	13	\N	\N	0	3b8e969d-0279-4874-a187-2aaf81668358	\N	\N
1000048	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 10:51:02.620644	1000069	2024-08-12 10:51:02.620646	1000069	Nguyen Van A	13	\N	\N	0	3a5d18be-06ba-4414-bc1d-3804870ee528	\N	\N
1000049	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 10:55:36.676542	1000069	2024-08-12 10:55:36.676542	1000069	Nguyen Van A	13	\N	\N	0	7c2f0898-d0c2-4acd-b5c7-b4515819afd7	\N	\N
1000050	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 10:57:12.950146	1000069	2024-08-12 10:57:12.950146	1000069	Nguyen Van A	13	\N	\N	0	fcc4467f-7de0-429f-b10e-ee5b40897d8c	\N	\N
1000051	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 10:57:29.670961	1000069	2024-08-12 10:57:29.670961	1000069	Nguyen Van A	13	\N	\N	0	dc697358-0e4c-4ca2-ad27-5e31c7b5bdf5	\N	\N
1000052	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 10:58:53.801656	1000069	2024-08-12 10:58:53.801657	1000069	Nguyen Van A	13	\N	\N	0	f8b77709-a5d8-439e-b3f4-5474359d4cc5	\N	\N
1000053	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 10:59:07.94424	1000069	2024-08-12 10:59:07.944241	1000069	Nguyen Van A	13	\N	\N	0	ddd1da7a-57ef-4cd9-ae36-6599e21565a5	\N	\N
1000054	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 11:01:04.045076	1000069	2024-08-12 11:01:04.045077	1000069	Nguyen Van A	13	\N	\N	0	59cbd6ae-7936-44d7-8ecb-ed5dcd881858	\N	\N
1000055	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 11:01:52.009945	1000069	2024-08-12 11:01:52.009946	1000069	Nguyen Van A	13	\N	\N	0	c83398be-c5db-4f87-802d-6a148884354d	\N	\N
1000056	1000004	1000016	000	1000010	0123456789	CO	web	N	0	0	1000069	1	Y	2024-08-12	2024-08-12 11:07:40.183247	1000069	2024-08-12 11:07:40.183248	1000069	Nguyen Van A	13	\N	\N	0	32acce6d-ac44-465c-a9fc-10964056be6a	\N	\N
1000057	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 11:09:01.997457	1000069	2024-08-12 11:09:01.997457	1000069	Nguyen Van A	13	\N	\N	0	ac327251-2340-49e8-94a7-37c4a6b0e14c	\N	\N
1000058	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 11:09:15.110867	1000069	2024-08-12 11:09:15.110867	1000069	Nguyen Van A	13	\N	\N	0	46fad338-93f5-4f18-9e72-c2efc9d33214	\N	\N
1000059	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 11:09:57.402249	1000069	2024-08-12 11:09:57.402249	1000069	Nguyen Van A	13	\N	\N	0	9a2fde84-f309-45a5-b8bd-e0cb97c2cf48	\N	\N
1000060	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 11:10:42.910655	1000069	2024-08-12 11:10:42.910656	1000069	Nguyen Van A	13	\N	\N	0	494d7dec-d6e4-4017-ab3c-32236b2b1ddd	\N	\N
1000061	1000004	1000016	000	1000010	0123456789	CO	web	N	1000070	0	1000069	1	Y	2024-08-12	2024-08-12 11:11:01.795216	1000069	2024-08-12 11:11:01.795217	1000069	Nguyen Van A	13	\N	\N	0	200c7e41-fa74-4c65-bd75-8198dd8ba9f2	\N	\N
1000062	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 11:15:13.707213	1000069	2024-08-12 11:15:13.707236	1000069	Nguyen Van A	13	\N	\N	0	adb4479b-bdc6-406a-b4cd-0dce12d48d16	\N	\N
1000063	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 11:18:34.770431	1000069	2024-08-12 11:18:34.770431	1000069	Nguyen Van A	13	\N	\N	0	ada40d83-ebc7-4526-8cb9-0cc5eee99e2f	\N	\N
1000064	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-12	2024-08-12 14:35:56.090117	1000069	2024-08-12 14:35:56.090117	1000069	Nguyen Van A	13	\N	\N	0	a50b9ce0-951f-440b-ad47-798e88f0f099	\N	\N
1000065	1000004	1000016	000	1000010	0123456789	CO	web	N	0	0	1000069	1	Y	2024-08-12	2024-08-12 22:44:34.048832	1000069	2024-08-12 22:44:34.048833	1000069	Nguyen Van A	13	\N	\N	0	096a8357-0ac3-4546-b4cd-cea0fa50d876	\N	\N
1000066	1000004	1000016	000	1000010	0123456789	CO	web	N	0	0	1000069	1	Y	2024-08-13	2024-08-13 18:43:44.396378	1000069	2024-08-13 18:43:44.396379	1000069	Nguyen Van A	13	\N	\N	0	7e144456-9f4d-4c3f-b01a-7919e7f0c249	\N	\N
1000067	1000004	1000016	000	1000010	0123456789	CO	web	N	0	0	1000069	1	Y	2024-08-14	2024-08-14 11:24:36.663375	1000069	2024-08-14 11:24:36.663376	1000069	Nguyen Van A	13	\N	\N	0	1b05ed81-9efc-4bad-adaa-ad919839b80b	\N	\N
1000068	1000004	1000016	000	1000010	0123456789	CO	web	N	1000070	0	1000069	1	Y	2024-08-14	2024-08-14 11:26:10.249377	1000069	2024-08-14 11:26:10.249379	1000069	Nguyen Van A	13	\N	\N	0	8124a2c0-dc6e-49ea-8d7e-4ff6daf3ed04	\N	\N
1000069	1000004	1000016	000	1000010	0123456789	CO	web	N	1000061	0	1000069	1	Y	2024-08-14	2024-08-14 11:27:04.39005	1000069	2024-08-14 11:27:04.390052	1000069	Nguyen Van A	13	\N	\N	0	f7e8cbd9-f670-4714-b3a8-8f1be23820ec	\N	\N
1000070	1000004	1000016	000	1000010	0123456789	CO	web	N	0	0	1000069	1	Y	2024-08-14	2024-08-14 23:12:30.102463	1000069	2024-08-14 23:12:30.102464	1000069	Nguyen Van A	13	\N	\N	0	41ac6446-575d-45af-abcc-c09b0d8acf7c	\N	\N
1000071	1000004	1000016	000	1000010	0123456789	CO	web	N	1000066	0	1000069	1	Y	2024-08-15	2024-08-15 16:14:23.715424	1000069	2024-08-15 16:14:23.715428	1000069	Nguyen Van A	13	\N	\N	0	c7bac0b8-2ce8-46be-b1fb-89d7947b705d	\N	\N
1000072	1000004	1000016	000	1000010	0123456789	CO	web	N	1000070	0	1000069	1	Y	2024-08-15	2024-08-17 15:11:35.504647	1000069	2024-08-17 15:11:35.504649	1000069	Nguyen Van A	13	\N	\N	0	0ffd09a9-93c0-424b-a8a1-e976da6e1d8e	\N	\N
1000073	1000004	1000016	000	1000010	0123456789	CO	web	N	0	0	1000069	1	Y	2024-08-24	2024-08-24 12:12:47.321765	1000069	2024-08-24 12:12:47.321765	1000069	Nguyen Van A	13	\N	\N	0	aca32efa-6e47-4c3b-9b0b-a09c4a3fc44b	\N	\N
1000074	1000004	1000016	000	1000010	0123456789	CO	web	N	1000073	0	1000069	1	Y	2024-08-26	2024-08-26 09:51:11.73705	1000069	2024-08-26 09:51:11.737052	1000069	Nguyen Van A	13	\N	\N	0	64941803-96c5-4835-bec0-9787990419ee	\N	\N
1000075	1000004	1000016	000	1000010	0123456789	CO	web	N	1000074	0	1000069	1	Y	2024-08-26	2024-08-26 09:51:35.664209	1000069	2024-08-26 09:51:35.664211	1000069	Nguyen Van A	13	\N	\N	0	9c78bb77-3caa-4f83-a484-57bf53266bcf	\N	\N
1000076	1000004	1000016	000	1000010	0123456789	CO	web	N	1000077	0	1000069	1	Y	2024-08-27	2024-08-27 10:27:47.265302	1000069	2024-08-27 10:27:47.265303	1000069	Nguyen Van A	13	\N	\N	0	2d37b065-f1d5-4441-b310-8cfeaaaef5fc	\N	\N
1000077	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-27	2024-08-27 12:03:16.512446	1000069	2024-08-27 12:03:16.512448	1000069	Nguyen Van A	13	\N	\N	0	7dc775c7-b789-4e40-af24-5a255b832319	\N	\N
1000078	1000004	1000016	000	1000010	0123456789	CO	web	N	1000071	0	1000069	1	Y	2024-08-30	2024-08-30 18:50:09.545064	1000069	2024-08-30 18:50:09.545064	1000069	Nguyen Van A	13	\N	\N	0	5f3f17a2-ee7b-40f5-9149-7641ae5f5ce5	\N	\N
\.


--
-- TOC entry 5381 (class 0 OID 385703)
-- Dependencies: 240
-- Data for Name: d_orderline; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_orderline (d_orderline_id, d_order_id, qty, price_entered, total_amount, d_tax_id, tax_amount, d_tenant_id, d_org_id, discount_percent, discount_amount, d_orderline_uu, created, created_by, updated, updated_by, d_product_id, is_active) FROM stdin;
1000014	1000016	2	30000	60000	\N	\N	1000002	1000001	\N	\N	2bab2089-9426-4bb5-b1fd-2e67ffdb2604	2024-07-25 03:16:30.837533	1000037	2024-07-25 03:16:30.837533	1000037	1000087	Y
1000015	1000017	2	30000	60000	\N	\N	1000002	1000001	\N	\N	57b6cc85-2e19-4736-b612-7cfedc304418	2024-07-25 04:16:13.432299	1000037	2024-07-25 04:16:13.432299	1000037	1000087	Y
1000016	\N	2	40000	450000	\N	\N	1000002	1000001	\N	\N	8594c2bd-96b0-45da-a04d-975a9c781749	2024-07-29 03:52:07.821192	1000037	2024-07-29 03:52:07.821192	1000037	1000087	Y
1000017	\N	2	50000	650000	\N	\N	1000002	1000001	\N	\N	25fa9814-df34-455c-904d-92a5ff60498c	2024-07-29 03:52:07.853581	1000037	2024-07-29 03:52:07.853581	1000037	1000087	Y
1000018	\N	2	40000	450000	\N	\N	1000002	1000001	\N	\N	267943ce-35ac-4776-b90b-525879354873	2024-07-29 03:52:20.586184	1000037	2024-07-29 03:52:20.586184	1000037	1000087	Y
1000019	\N	2	50000	650000	\N	\N	1000002	1000001	\N	\N	ceb80163-bf2a-484b-8bdc-bfe1dfb12b36	2024-07-29 03:52:20.595962	1000037	2024-07-29 03:52:20.595962	1000037	1000087	Y
1000020	\N	2	40000	450000	\N	\N	1000002	1000001	\N	\N	c1b45bf4-1417-4603-b99f-1f1ee55ae716	2024-07-29 03:56:46.930092	1000037	2024-07-29 03:56:46.930092	1000037	1000087	Y
1000021	\N	2	50000	650000	\N	\N	1000002	1000001	\N	\N	7e81d14e-c826-4638-bcb1-7988c256f46b	2024-07-29 03:56:46.949344	1000037	2024-07-29 03:56:46.949344	1000037	1000087	Y
1000022	\N	2	40000	450000	\N	\N	1000002	1000001	\N	\N	af513dcd-ae41-4a06-b799-7d6fc6b995c4	2024-07-29 04:02:20.561156	1000037	2024-07-29 04:02:20.561156	1000037	1000087	Y
1000023	\N	2	50000	650000	\N	\N	1000002	1000001	\N	\N	d24a2855-0249-4e9e-bcc7-5fd7ad661524	2024-07-29 04:02:20.57693	1000037	2024-07-29 04:02:20.57693	1000037	1000087	Y
1000024	\N	2	40000	450000	\N	\N	1000002	1000001	\N	\N	1da373c3-1751-4969-b717-f79450ea1834	2024-07-29 04:06:04.224175	1000037	2024-07-29 04:06:04.224175	1000037	1000087	Y
1000025	\N	2	50000	650000	\N	\N	1000002	1000001	\N	\N	5b19c046-93ec-4925-8f9a-cbef5f64c411	2024-07-29 04:06:04.232716	1000037	2024-07-29 04:06:04.232716	1000037	1000087	Y
1000026	\N	2	40000	450000	\N	\N	1000002	1000001	\N	\N	7543dd51-1ccb-4714-944e-2e6170f008cb	2024-07-29 04:08:08.717289	1000037	2024-07-29 04:08:08.717289	1000037	1000087	Y
1000027	\N	2	50000	650000	\N	\N	1000002	1000001	\N	\N	1f8b21e1-338c-425a-9ce2-706a6a14e4d7	2024-07-29 04:08:08.741994	1000037	2024-07-29 04:08:08.741994	1000037	1000087	Y
1000028	\N	2	40000	450000	\N	\N	1000002	1000001	\N	\N	89ed8398-e881-4eec-98d5-12e03d5552ef	2024-07-29 04:10:50.103997	1000037	2024-07-29 04:10:50.103997	1000037	1000087	Y
1000029	\N	2	50000	650000	\N	\N	1000002	1000001	\N	\N	2262d99a-dd62-4195-b53b-bd0785f29ee9	2024-07-29 04:10:50.125869	1000037	2024-07-29 04:10:50.125869	1000037	1000087	Y
1000030	\N	2	40000	450000	\N	\N	1000002	1000001	\N	\N	2c16c290-1a96-41d0-95f1-0d628331dca0	2024-07-29 04:17:15.186203	1000037	2024-07-29 04:17:15.186203	1000037	1000087	Y
1000031	\N	2	50000	650000	\N	\N	1000002	1000001	\N	\N	ae164d16-3672-4741-83be-e93a211803bd	2024-07-29 04:17:15.19818	1000037	2024-07-29 04:17:15.19818	1000037	1000087	Y
1000032	1000016	2	40000	450000	\N	\N	1000002	1000001	\N	\N	bfd897a1-9f72-49ac-907b-95c18e938f8f	2024-07-29 04:18:33.779007	1000037	2024-07-29 04:18:33.779007	1000037	1000087	Y
1000033	1000016	2	50000	650000	\N	\N	1000002	1000001	\N	\N	57735aad-ca2c-4f14-89a4-91259dd2872b	2024-07-29 04:18:33.803928	1000037	2024-07-29 04:18:33.803928	1000037	1000087	Y
1000034	1000016	2	40000	450000	\N	\N	1000002	1000001	\N	\N	23a2aa91-1b80-4e69-98ee-a7d459aec069	2024-07-29 04:23:12.611939	1000037	2024-07-29 04:23:12.611939	1000037	1000087	Y
1000035	1000016	2	50000	650000	\N	\N	1000002	1000001	\N	\N	a9b50f2b-4aa5-4d1d-92da-3079d1eb4de5	2024-07-29 04:23:12.618734	1000037	2024-07-29 04:23:12.618734	1000037	1000087	Y
1000036	1000019	2	30000	60000	\N	\N	1000002	1000001	\N	\N	60e5057e-679a-4fe1-b927-3a904de3da1a	2024-07-29 04:38:20.387092	1000037	2024-07-29 04:38:20.387092	1000037	1000087	Y
1000040	1000023	2	30000	60000	\N	\N	1000002	1000001	\N	\N	49af6c00-fc51-4de7-907f-2e06e11736d7	2024-07-30 03:16:45.982576	1000037	2024-07-30 03:16:45.982576	1000037	1000087	Y
1000041	1000024	2	30000	60000	\N	\N	1000002	1000001	\N	\N	d7fc0746-c413-450d-8b8c-6d44c9aa12c9	2024-07-30 03:24:35.502935	1000037	2024-07-30 03:24:35.502935	1000037	1000087	Y
1000042	1000025	2	30000	60000	\N	\N	1000002	1000001	\N	\N	d2597430-de37-42d0-9a1a-95a1cf0d6b6d	2024-07-30 03:25:08.094672	1000037	2024-07-30 03:25:08.094672	1000037	1000087	Y
1000044	1000027	2	30000	60000	\N	\N	1000002	1000001	\N	\N	c7585b16-a344-4fe0-94e8-b823a196fc50	2024-07-31 10:56:35.684039	1000037	2024-07-31 10:56:35.68404	1000037	1000087	Y
1000045	1000028	2	30000	60000	\N	\N	1000002	1000001	\N	\N	7615548c-f04a-48aa-ab19-6b19beef3e01	2024-07-31 10:59:26.966309	1000037	2024-07-31 10:59:26.966309	1000037	1000087	Y
1000046	1000029	2	30000	60000	\N	\N	1000003	1000001	\N	\N	d0a6998e-67d8-4855-88d7-4f945bfebc23	2024-08-09 15:03:52.796175	1000068	2024-08-09 15:03:52.796177	1000068	1000087	Y
1000048	1000032	1	100	100	\N	\N	1000004	1000016	\N	\N	7745a6b8-a2a7-4aa1-822d-66327ca94340	2024-08-10 10:00:52.334689	1000069	2024-08-10 10:00:52.334691	1000069	1000140	Y
1000049	1000032	1	100	100	\N	\N	1000004	1000016	\N	\N	1349ead8-848c-465f-969a-a09b4dee95d4	2024-08-10 10:00:52.343598	1000069	2024-08-10 10:00:52.343599	1000069	1000139	Y
1000050	1000032	1	100	100	\N	\N	1000004	1000016	\N	\N	be03dee9-dc98-4393-8d71-2fea31e18537	2024-08-10 10:00:52.348822	1000069	2024-08-10 10:00:52.348822	1000069	1000138	Y
1000051	1000032	1	100	100	\N	\N	1000004	1000016	\N	\N	9cad81da-3f24-4c8c-9644-0cf6dccec78a	2024-08-10 10:00:52.354071	1000069	2024-08-10 10:00:52.354072	1000069	1000130	Y
1000052	1000033	1	100	100	\N	\N	1000004	1000016	\N	\N	163dc2e9-1732-4784-8d38-d4dc2d79bc0a	2024-08-10 10:00:52.988131	1000069	2024-08-10 10:00:52.988132	1000069	1000140	Y
1000053	1000033	1	100	100	\N	\N	1000004	1000016	\N	\N	e7c997bd-aa87-4e85-9bd9-fb4b962e96e4	2024-08-10 10:00:52.993191	1000069	2024-08-10 10:00:52.993192	1000069	1000139	Y
1000054	1000033	1	100	100	\N	\N	1000004	1000016	\N	\N	299020c4-50bc-4f22-94a6-ede093bde9b3	2024-08-10 10:00:52.998205	1000069	2024-08-10 10:00:52.998205	1000069	1000138	Y
1000055	1000033	1	100	100	\N	\N	1000004	1000016	\N	\N	43a6aab7-671d-4717-a238-4f65c84d736c	2024-08-10 10:00:53.002654	1000069	2024-08-10 10:00:53.002654	1000069	1000130	Y
1000056	1000036	1	100	100	\N	\N	1000004	1000016	\N	\N	6af29649-7522-42ae-81dc-3bec12ecf67a	2024-08-12 10:04:55.297659	1000069	2024-08-12 10:04:55.29766	1000069	1000139	Y
1000057	1000036	1	100	100	\N	\N	1000004	1000016	\N	\N	0b31e895-df3c-4f08-95c5-175278d881f0	2024-08-12 10:04:55.302656	1000069	2024-08-12 10:04:55.302657	1000069	1000138	Y
1000058	1000037	1	100	100	\N	\N	1000004	1000016	\N	\N	7ebf4311-0204-462c-97fc-fc7bf5c1bf3a	2024-08-12 10:10:48.412561	1000069	2024-08-12 10:10:48.412563	1000069	1000140	Y
1000059	1000037	1	100	100	\N	\N	1000004	1000016	\N	\N	375eca79-f621-4c68-9432-f9b56f8e6fa8	2024-08-12 10:10:48.41719	1000069	2024-08-12 10:10:48.417191	1000069	1000139	Y
1000060	1000038	1	100	100	\N	\N	1000004	1000016	\N	\N	dc0c35ef-fe38-4c6f-8b32-084af7632ae0	2024-08-12 10:11:12.653403	1000069	2024-08-12 10:11:12.653404	1000069	1000140	Y
1000061	1000038	1	100	100	\N	\N	1000004	1000016	\N	\N	e847de66-3c39-4950-9ea8-ba8447ccde88	2024-08-12 10:11:12.657892	1000069	2024-08-12 10:11:12.657893	1000069	1000139	Y
1000062	1000039	1	100	100	\N	\N	1000004	1000016	\N	\N	1a0a1ed7-a6c1-4509-81c4-7d605691e633	2024-08-12 10:19:11.075801	1000069	2024-08-12 10:19:11.075802	1000069	1000139	Y
1000063	1000039	1	100	100	\N	\N	1000004	1000016	\N	\N	130094dc-de19-42a2-bf4e-18f405268757	2024-08-12 10:19:11.076735	1000069	2024-08-12 10:19:11.076736	1000069	1000138	Y
1000064	1000040	1	100	100	\N	\N	1000004	1000016	\N	\N	4c146bb5-f62e-4458-82fb-fe2f4b3ddc0e	2024-08-12 10:23:20.048991	1000069	2024-08-12 10:23:20.048993	1000069	1000140	Y
1000065	1000040	1	100	100	\N	\N	1000004	1000016	\N	\N	a8a3cf17-5e4c-4dee-9c04-b8ca08ea2065	2024-08-12 10:23:20.054318	1000069	2024-08-12 10:23:20.05432	1000069	1000139	Y
1000066	1000041	1	100	100	\N	\N	1000004	1000016	\N	\N	61da4051-02aa-4c48-a3c2-91fcf49d3979	2024-08-12 10:29:07.62789	1000069	2024-08-12 10:29:07.627891	1000069	1000139	Y
1000067	1000042	1	100	100	\N	\N	1000004	1000016	\N	\N	561d4953-8b6e-4c65-8c7e-cd684daa5932	2024-08-12 10:29:38.119604	1000069	2024-08-12 10:29:38.119605	1000069	1000140	Y
1000068	1000043	1	100	100	\N	\N	1000004	1000016	\N	\N	705a718a-b192-48d6-b998-0c8b06908042	2024-08-12 10:33:26.293762	1000069	2024-08-12 10:33:26.293763	1000069	1000139	Y
1000069	1000043	1	100	100	\N	\N	1000004	1000016	\N	\N	0efff169-8c7f-4117-ba1e-19a4478e2fcf	2024-08-12 10:33:26.294716	1000069	2024-08-12 10:33:26.294717	1000069	1000138	Y
1000070	1000044	1	100	100	\N	\N	1000004	1000016	\N	\N	13ac3713-e1f2-4f66-af4a-4eec27d73883	2024-08-12 10:36:41.506414	1000069	2024-08-12 10:36:41.506415	1000069	1000138	Y
1000071	1000045	1	100	100	\N	\N	1000004	1000016	\N	\N	b2165d1a-5639-498b-bfd2-4cd4e346ea6f	2024-08-12 10:38:20.29509	1000069	2024-08-12 10:38:20.295091	1000069	1000139	Y
1000072	1000046	1	100	100	\N	\N	1000004	1000016	\N	\N	301b660e-3be9-4993-a787-8877ee33abc7	2024-08-12 10:44:29.367182	1000069	2024-08-12 10:44:29.367183	1000069	1000140	Y
1000073	1000047	1	100	100	\N	\N	1000004	1000016	\N	\N	c68e05b8-ce8d-4b1d-a2f6-6615564870ae	2024-08-12 10:50:23.516827	1000069	2024-08-12 10:50:23.516829	1000069	1000140	Y
1000074	1000047	1	100	100	\N	\N	1000004	1000016	\N	\N	aafe16c6-91a2-4808-9ec1-4fe0eae328c2	2024-08-12 10:50:23.521363	1000069	2024-08-12 10:50:23.521364	1000069	1000139	Y
1000075	1000048	1	100	100	\N	\N	1000004	1000016	\N	\N	4442bc05-298c-48b1-bada-d1a59e517cb4	2024-08-12 10:51:02.625838	1000069	2024-08-12 10:51:02.625839	1000069	1000140	Y
1000076	1000048	1	100	100	\N	\N	1000004	1000016	\N	\N	0126634a-58f8-4c8b-9670-0d5c9bcefa2d	2024-08-12 10:51:02.630271	1000069	2024-08-12 10:51:02.630273	1000069	1000139	Y
1000077	1000049	1	100	100	\N	\N	1000004	1000016	\N	\N	b4dfc951-4181-46b8-af24-d496c72241e2	2024-08-12 10:55:36.681673	1000069	2024-08-12 10:55:36.681674	1000069	1000139	Y
1000078	1000050	1	100	100	\N	\N	1000004	1000016	\N	\N	9efd67bc-b894-421d-a5ca-6b3fa3158016	2024-08-12 10:57:12.952385	1000069	2024-08-12 10:57:12.952385	1000069	1000140	Y
1000079	1000051	1	100	100	\N	\N	1000004	1000016	\N	\N	9ba04f33-61ae-4a4b-a6b7-695365de280c	2024-08-12 10:57:29.673929	1000069	2024-08-12 10:57:29.67393	1000069	1000139	Y
1000080	1000052	1	100	100	\N	\N	1000004	1000016	\N	\N	c1bbc95c-0b3c-4ecd-b2c3-b3f098d6b9c4	2024-08-12 10:58:53.80626	1000069	2024-08-12 10:58:53.806261	1000069	1000140	Y
1000081	1000053	1	100	100	\N	\N	1000004	1000016	\N	\N	5e5f785f-3df1-4e8a-a5df-9074b9bce3f9	2024-08-12 10:59:07.945343	1000069	2024-08-12 10:59:07.945343	1000069	1000140	Y
1000082	1000054	1	100	100	\N	\N	1000004	1000016	\N	\N	e73c8943-68d7-4c91-a1fd-345d0cc393e1	2024-08-12 11:01:04.049864	1000069	2024-08-12 11:01:04.049865	1000069	1000140	Y
1000083	1000055	1	100	100	\N	\N	1000004	1000016	\N	\N	f0bc5e59-a833-4877-b642-49a23088c4ce	2024-08-12 11:01:52.01123	1000069	2024-08-12 11:01:52.011231	1000069	1000140	Y
1000084	1000056	1	100	100	\N	\N	1000004	1000016	\N	\N	5616f36b-caea-46b0-a1b7-2c1d4a9e2769	2024-08-12 11:07:40.188671	1000069	2024-08-12 11:07:40.188671	1000069	1000140	Y
1000085	1000056	1	100	100	\N	\N	1000004	1000016	\N	\N	de7631ad-8a30-48ab-8fd8-61226eabc200	2024-08-12 11:07:40.193004	1000069	2024-08-12 11:07:40.193005	1000069	1000139	Y
1000086	1000057	1	100	100	\N	\N	1000004	1000016	\N	\N	ab4d594e-65c0-450f-b751-0ff6dc616d21	2024-08-12 11:09:02.002447	1000069	2024-08-12 11:09:02.002447	1000069	1000140	Y
1000087	1000058	1	100	100	\N	\N	1000004	1000016	\N	\N	99d1db3c-c301-4183-b68e-7b27d281c56d	2024-08-12 11:09:15.112088	1000069	2024-08-12 11:09:15.112088	1000069	1000140	Y
1000088	1000059	1	100	100	\N	\N	1000004	1000016	\N	\N	8975ee61-4cde-4206-bb53-9d82fc6b6aca	2024-08-12 11:09:57.407979	1000069	2024-08-12 11:09:57.40798	1000069	1000140	Y
1000089	1000060	1	100	100	\N	\N	1000004	1000016	\N	\N	66731f02-d6ae-4080-b18f-0e99a191a698	2024-08-12 11:10:42.91517	1000069	2024-08-12 11:10:42.915171	1000069	1000140	Y
1000090	1000061	1	100	100	\N	\N	1000004	1000016	\N	\N	63f624a6-9525-4a77-b4fc-7583dcd20019	2024-08-12 11:11:01.8002	1000069	2024-08-12 11:11:01.800201	1000069	1000140	Y
1000091	1000062	1	100	100	\N	\N	1000004	1000016	\N	\N	c7b97dd3-3f2c-4793-9d3a-4ee1c579933d	2024-08-12 11:15:13.71354	1000069	2024-08-12 11:15:13.71354	1000069	1000140	Y
1000092	1000063	1	100	100	\N	\N	1000004	1000016	\N	\N	657338f4-5f0d-4243-b046-36510b765c0d	2024-08-12 11:18:34.776212	1000069	2024-08-12 11:18:34.776212	1000069	1000140	Y
1000093	1000064	1	100	100	\N	\N	1000004	1000016	\N	\N	dd4acbbc-c0aa-4860-be10-79c0a3f5b04b	2024-08-12 14:35:56.096482	1000069	2024-08-12 14:35:56.096482	1000069	1000139	Y
1000094	1000065	1	100	100	\N	\N	1000004	1000016	\N	\N	c6985e97-5cce-4940-b83f-2b2efd4bd61b	2024-08-12 22:44:34.055248	1000069	2024-08-12 22:44:34.055249	1000069	1000140	Y
1000095	1000065	1	100	100	\N	\N	1000004	1000016	\N	\N	9de3aebd-bbd7-49e8-9557-4de0f055e415	2024-08-12 22:44:34.059881	1000069	2024-08-12 22:44:34.059881	1000069	1000139	Y
1000096	1000065	1	100	100	\N	\N	1000004	1000016	\N	\N	bfc7000e-dc74-4153-b207-6a535291bdb7	2024-08-12 22:44:34.064513	1000069	2024-08-12 22:44:34.064513	1000069	1000132	Y
1000097	1000065	1	100	100	\N	\N	1000004	1000016	\N	\N	d2897d9d-e5c3-4848-8c03-6fbae50f30f0	2024-08-12 22:44:34.06917	1000069	2024-08-12 22:44:34.06917	1000069	1000134	Y
1000098	1000066	1	100	100	\N	\N	1000004	1000016	\N	\N	1a98c1a9-5a87-4c3e-9ff8-61edc17142bd	2024-08-13 18:43:44.407001	1000069	2024-08-13 18:43:44.407002	1000069	1000139	Y
1000099	1000066	1	100	100	\N	\N	1000004	1000016	\N	\N	cc0249ba-6c27-49cf-be81-ee4cff2d623a	2024-08-13 18:43:44.41126	1000069	2024-08-13 18:43:44.411261	1000069	1000138	Y
1000100	1000066	1	100	100	\N	\N	1000004	1000016	\N	\N	6e0533cc-61cc-4833-88b4-88e4e155ebc5	2024-08-13 18:43:44.412309	1000069	2024-08-13 18:43:44.41231	1000069	1000130	Y
1000101	1000066	1	100	100	\N	\N	1000004	1000016	\N	\N	f439ed1a-0588-40cd-bbac-dad0c5da47e5	2024-08-13 18:43:44.413064	1000069	2024-08-13 18:43:44.413065	1000069	1000132	Y
1000102	1000067	1	100	100	\N	\N	1000004	1000016	\N	\N	0d365ed5-5c61-4bf4-8f91-a62ebb9e9bcf	2024-08-14 11:24:36.670891	1000069	2024-08-14 11:24:36.670892	1000069	1000132	Y
1000103	1000067	1	100	100	\N	\N	1000004	1000016	\N	\N	fea1a24b-d231-4af4-975c-36e06d5c5a59	2024-08-14 11:24:36.67446	1000069	2024-08-14 11:24:36.674461	1000069	1000130	Y
1000104	1000067	1	100	100	\N	\N	1000004	1000016	\N	\N	cc28cdf8-d4cb-4299-ab9d-11bf998c4cf7	2024-08-14 11:24:36.675463	1000069	2024-08-14 11:24:36.675464	1000069	1000129	Y
1000105	1000068	1	100	100	\N	\N	1000004	1000016	\N	\N	5eb3cbc4-5743-4c57-a312-bc26c0363aed	2024-08-14 11:26:10.251019	1000069	2024-08-14 11:26:10.25102	1000069	1000132	Y
1000106	1000068	1	100	100	\N	\N	1000004	1000016	\N	\N	1f52339c-9a0a-4037-9d8c-bba2bcc040dd	2024-08-14 11:26:10.252089	1000069	2024-08-14 11:26:10.25209	1000069	1000108	Y
1000107	1000069	4	100	400	\N	\N	1000004	1000016	\N	\N	41aa354f-21db-436f-b04f-187c71e7b881	2024-08-14 11:27:04.391693	1000069	2024-08-14 11:27:04.391694	1000069	1000138	Y
1000108	1000069	2	100	200	\N	\N	1000004	1000016	\N	\N	5c404b3f-cce6-416a-9238-c27ace75837b	2024-08-14 11:27:04.392737	1000069	2024-08-14 11:27:04.392738	1000069	1000130	Y
1000109	1000070	1	100	100	\N	\N	1000004	1000016	\N	\N	db5e0607-611c-4830-af1d-08ff6cab7c20	2024-08-14 23:12:30.111169	1000069	2024-08-14 23:12:30.11117	1000069	1000119	Y
1000110	1000070	1	100	100	\N	\N	1000004	1000016	\N	\N	207ce99a-a5db-4514-a1b1-112c69529233	2024-08-14 23:12:30.115009	1000069	2024-08-14 23:12:30.11501	1000069	1000132	Y
1000111	1000070	1	100	100	\N	\N	1000004	1000016	\N	\N	4b19aa84-1a22-4819-8374-a13d3628dbc0	2024-08-14 23:12:30.115913	1000069	2024-08-14 23:12:30.115919	1000069	1000134	Y
1000112	1000071	1	100	100	\N	\N	1000004	1000016	\N	\N	9c57f78c-df6f-40d2-9748-f52329f22dfe	2024-08-15 16:14:23.737779	1000069	2024-08-15 16:14:23.737781	1000069	1000138	Y
1000113	1000071	1	100	100	\N	\N	1000004	1000016	\N	\N	46fe0931-7819-493b-aee4-89f82d9d57ea	2024-08-15 16:14:23.742217	1000069	2024-08-15 16:14:23.742218	1000069	1000136	Y
1000114	1000072	1	100	100	\N	\N	1000004	1000016	\N	\N	529b7a3e-0765-407a-8879-c324adf43b44	2024-08-17 15:11:35.511643	1000069	2024-08-17 15:11:35.511645	1000069	1000140	Y
1000115	1000072	1	100	100	\N	\N	1000004	1000016	\N	\N	0d9c9e50-962f-4962-a759-697d879cf958	2024-08-17 15:11:35.513728	1000069	2024-08-17 15:11:35.513775	1000069	1000139	Y
1000116	1000072	1	100	100	\N	\N	1000004	1000016	\N	\N	4aca5e9a-e3a0-4eb6-ae13-d815d3c5d8fe	2024-08-17 15:11:35.514631	1000069	2024-08-17 15:11:35.514632	1000069	1000138	Y
1000117	1000072	1	100	100	\N	\N	1000004	1000016	\N	\N	9cbd73ad-b804-441b-861b-288b9dae8554	2024-08-17 15:11:35.51549	1000069	2024-08-17 15:11:35.515491	1000069	1000130	Y
1000118	1000073	1	100	100	\N	\N	1000004	1000016	\N	\N	e6ff3dc5-d4c1-4e53-92d6-ceee7872ad30	2024-08-24 12:12:47.329543	1000069	2024-08-24 12:12:47.329543	1000069	1000107	Y
1000119	1000073	1	100	100	\N	\N	1000004	1000016	\N	\N	e7e27334-0349-4b14-bd16-9e42b68382f8	2024-08-24 12:12:47.332778	1000069	2024-08-24 12:12:47.332778	1000069	1000108	Y
1000120	1000073	1	100	100	\N	\N	1000004	1000016	\N	\N	d9b7af1f-e659-404a-a801-5ef2d22b01fc	2024-08-24 12:12:47.333642	1000069	2024-08-24 12:12:47.333642	1000069	1000139	Y
1000121	1000074	3	0	0	\N	\N	1000004	1000016	\N	\N	1fc094ad-8d44-463c-9ac0-b7ca0023a42a	2024-08-26 09:51:11.743487	1000069	2024-08-26 09:51:11.743488	1000069	1000103	Y
1000122	1000074	3	0	0	\N	\N	1000004	1000016	\N	\N	fd1db05c-b54a-40f0-b5ff-3d4a8a8c2693	2024-08-26 09:51:11.747257	1000069	2024-08-26 09:51:11.747258	1000069	1000136	Y
1000123	1000074	3	0	0	\N	\N	1000004	1000016	\N	\N	be14b741-9f55-4188-9398-cfb3cc8a8a16	2024-08-26 09:51:11.748034	1000069	2024-08-26 09:51:11.748034	1000069	1000127	Y
1000124	1000074	3	0	0	\N	\N	1000004	1000016	\N	\N	c4cb88c0-a30e-4689-8c4b-c3bcc73a6d22	2024-08-26 09:51:11.748726	1000069	2024-08-26 09:51:11.748727	1000069	1000129	Y
1000125	1000074	1	0	0	\N	\N	1000004	1000016	\N	\N	bd1a6d6a-5cd3-4a64-8d03-baa43c695d27	2024-08-26 09:51:11.750253	1000069	2024-08-26 09:51:11.750254	1000069	1000139	Y
1000126	1000074	1	0	0	\N	\N	1000004	1000016	\N	\N	4e80d415-204e-4cff-91c2-242c4c759b9c	2024-08-26 09:51:11.752016	1000069	2024-08-26 09:51:11.752017	1000069	1000119	Y
1000127	1000074	1	0	0	\N	\N	1000004	1000016	\N	\N	e14aeb86-eaf4-4680-beac-ddabca8ed1c0	2024-08-26 09:51:11.752747	1000069	2024-08-26 09:51:11.752748	1000069	1000107	Y
1000128	1000074	1	0	0	\N	\N	1000004	1000016	\N	\N	6444426e-d767-4f98-8725-09b8deae2baf	2024-08-26 09:51:11.753445	1000069	2024-08-26 09:51:11.753446	1000069	1000140	Y
1000129	1000075	3	0	0	\N	\N	1000004	1000016	\N	\N	23f400b8-4f60-4c3a-872b-85637e6d9908	2024-08-26 09:51:35.665308	1000069	2024-08-26 09:51:35.665309	1000069	1000103	Y
1000130	1000075	3	0	0	\N	\N	1000004	1000016	\N	\N	7ee8aaa8-0ebe-4214-84b9-c659243bf18f	2024-08-26 09:51:35.666072	1000069	2024-08-26 09:51:35.666073	1000069	1000136	Y
1000131	1000075	3	0	0	\N	\N	1000004	1000016	\N	\N	734aab9d-4056-46ed-aae4-ec484539281d	2024-08-26 09:51:35.666805	1000069	2024-08-26 09:51:35.666806	1000069	1000127	Y
1000132	1000075	3	0	0	\N	\N	1000004	1000016	\N	\N	80a39427-7a40-4734-9940-7b521b2e7903	2024-08-26 09:51:35.669502	1000069	2024-08-26 09:51:35.669503	1000069	1000129	Y
1000133	1000075	2	0	0	\N	\N	1000004	1000016	\N	\N	1f4981ee-8f04-49ff-bdad-469ffb30b836	2024-08-26 09:51:35.67066	1000069	2024-08-26 09:51:35.670661	1000069	1000139	Y
1000134	1000075	2	0	0	\N	\N	1000004	1000016	\N	\N	92fa3aab-396b-4a56-ae61-5f090d7f345e	2024-08-26 09:51:35.671532	1000069	2024-08-26 09:51:35.671533	1000069	1000119	Y
1000135	1000075	2	0	0	\N	\N	1000004	1000016	\N	\N	73c74298-bd1e-4d78-9957-7a4d17113045	2024-08-26 09:51:35.672425	1000069	2024-08-26 09:51:35.672426	1000069	1000107	Y
1000136	1000075	2	0	0	\N	\N	1000004	1000016	\N	\N	7f81a2ce-3004-41d3-803b-c98d37a78c66	2024-08-26 09:51:35.674343	1000069	2024-08-26 09:51:35.674344	1000069	1000140	Y
1000137	1000076	1	0	0	\N	\N	1000004	1000016	\N	\N	6b4ab338-368f-4c9d-8776-3c037d3341fb	2024-08-27 10:27:47.279215	1000069	2024-08-27 10:27:47.279217	1000069	1000152	Y
1000138	1000076	1	0	0	\N	\N	1000004	1000016	\N	\N	7a8a0359-d0e2-426d-ac13-24ba599c3e3f	2024-08-27 10:27:47.284976	1000069	2024-08-27 10:27:47.284977	1000069	1000151	Y
1000139	1000077	1	0	0	\N	\N	1000004	1000016	\N	\N	764a7a22-3696-4c1d-b538-c24c54ca56bd	2024-08-27 12:03:16.519638	1000069	2024-08-27 12:03:16.519639	1000069	1000152	Y
1000140	1000077	1	0	0	\N	\N	1000004	1000016	\N	\N	80a74791-07f7-42c9-b3fe-8c95bcc141a0	2024-08-27 12:03:16.522733	1000069	2024-08-27 12:03:16.522735	1000069	1000151	Y
1000141	1000078	1	15000	15000	\N	\N	1000004	1000016	\N	\N	8c5000ba-cebb-4bdc-8933-a29e3f38ecd2	2024-08-30 18:50:09.562419	1000069	2024-08-30 18:50:09.562419	1000069	1000356	Y
1000142	1000078	1	100	100	\N	\N	1000004	1000016	\N	\N	de3554ac-261c-4599-933c-84d6b2065640	2024-08-30 18:50:09.568448	1000069	2024-08-30 18:50:09.568448	1000069	1000138	Y
\.


--
-- TOC entry 5382 (class 0 OID 385716)
-- Dependencies: 241
-- Data for Name: d_org; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_org (d_org_id, code, name, d_tenant_id, address, tax_code, created, created_by, updated, is_active, d_org_uu, updated_by, email, phone, area, wards, erp_org_id) FROM stdin;
1000001	*	all	1000002	\N	\N	2024-07-09 08:46:28.023413	0	2024-07-09 08:46:28.023413	Y	4cc5c642-a3fb-440f-9f59-1106c6c257d7	0	\N	\N	\N	\N	\N
1000003	SG	Sài Gòn	1000003	\N	\N	2024-07-12 02:43:36.39605	0	2024-07-12 02:43:36.39605	Y	fe99e717-0650-423a-b18d-7c3d86630028	0	\N	\N	\N	\N	\N
1000005	Osen	OnsenFuji	1000002	\N	\N	2024-07-22 02:58:28.040277	0	2024-07-22 02:58:28.040277	Y	c02d875e-c286-4968-ac31-f04bc06b04b5	0	\N	\N	\N	\N	\N
1000004	HN	Hà Nội	1000003	\N	\N	2024-07-12 02:43:36.39605	0	2024-07-12 02:43:36.39605	Y		0	\N	\N	\N	\N	\N
0	*	All	1000004	\N	\N	2024-08-04 04:45:45.047673	0	2024-08-04 04:45:45.047673	Y	2a377c39-8c87-48ef-90ac-9668d3ae9147	0	\N	\N	\N	\N	\N
1000016	F&B_SG	Chi nhánh Sài Gòn	1000004	Hồ Chí Minh	\N	2024-08-04 12:03:24.721412	1000069	2024-08-04 12:03:24.721412	Y	64a08bb1-00a2-4d8c-93e0-33cc41544f7f	1000069	fbsaigon@gmail.com	099	TPHCM	\N	\N
1000019	F&B_SG	Chi nhánh Hà Nội	1000004	Hà Nội	\N	2024-08-30 03:39:07.929593	0	2024-08-30 03:39:07.929593	Y	01db7cb1-fe99-4510-860a-75fb6e18b29f	0	fbhanoi@gmail.com	\N	HN	\N	\N
1000027	CN1000027	Cửa hàng 3	1000009	Đà Nẵng	\N	2024-09-12 06:58:40.551331	1000071	2024-09-12 06:58:40.551331	Y	475e8fcc-513e-4bce-88f6-20952415d2e7	1000071	T02HN@gmail.com	099000	Đà Nẵng	\N	\N
1000026	CN1000023	Cửa hàng 2	1000009	Hà Nội	\N	2024-09-12 06:57:24.460095	1000071	2024-09-12 06:57:24.460095	Y	eb5002a1-5912-4cb9-a28b-2c26d910eead	1000071	T02HN@gmail.com	099000	Hà Nội	\N	\N
1000022	CN1000022	Cửa hàng 1	1000009	Hồ Chí Minh	\N	2024-09-11 04:26:30.893693	0	2024-09-11 04:26:30.893693	Y	88b07d2c-9024-4ee3-9e03-2df22db4220c	0	tonthep@gmail.com	\N	Hồ Chí Minh	\N	\N
1000030	F&B QN	Chi nhánh Quy Nhơn1	1000004	Binh Dinh	\N	2024-09-16 15:11:28.713499	1000069	2024-09-16 16:03:34.329293	Y	bd9b11cb-cedc-48cc-a211-5ae86d055f5a	1000069	hungnguyen.201102ak@gmail.com	\N	GL	\N	\N
1000021	F&B_GL	Chi nhánh Gia Lai	1000004	Pleiku	\N	2024-09-04 15:54:58.678314	1000069	2024-09-17 15:25:13.347643	Y	5cf01668-52f4-4e3d-9297-b06f9a000eca	1000069	fbgl@gmail.com	093564512	GL1	Thành phố Pleiku	\N
1000020	F&B _DN	Chi nhánh Đà Nẵng	1000004	Da Nang	\N	2024-09-04 15:49:29.280256	1000069	2024-09-17 15:27:20.862812	Y	4a87b858-afae-4f6e-a45b-f6754bd5e3da	1000069	hungnguyen.201102ak@gmail.com	0935703991	ĐN	Trung tâm	\N
\.


--
-- TOC entry 5456 (class 0 OID 389549)
-- Dependencies: 315
-- Data for Name: d_partner_group; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_partner_group (d_partner_group_id, d_tenant_id, d_org_id, is_customer, is_default, group_code, group_name, description, is_active, created, created_by, updated, updated_by, d_partner_group_uu, discount, is_summary, d_partner_group_parent_id, erp_bp_group_id) FROM stdin;
1000011	1000003	0	Y	\N	code	group name1	\N	Y	2024-07-30 15:51:12.040117	1000068	2024-07-30 15:51:12.040117	1000068	ac132268-7c8b-4973-b8bd-6dd0a22089d1	\N	N	\N	\N
1000012	1000003	0	Y	\N	code	group name1	\N	Y	2024-07-31 10:50:54.806971	1000068	2024-07-31 10:50:54.806972	1000068	c51fafb0-5fdb-4a67-af27-171082c51be2	\N	N	\N	\N
1000013	1000002	1000001	Y	\N	code	group name1	\N	Y	2024-07-31 11:04:58.590173	1000037	2024-07-31 11:04:58.590176	1000037	b5f2cbba-be44-4bc9-ad63-bb096d75772e	\N	N	\N	\N
1000014	1000002	1000001	Y	\N	code	group name2	\N	Y	2024-07-31 11:11:27.426795	1000037	2024-07-31 11:11:27.426796	1000037	14457cc2-ad6f-447b-ac24-731b7f3b0454	\N	N	\N	\N
1000015	1000002	1000001	N	\N	code	group name vendor	\N	Y	2024-07-31 21:50:49.090758	1000037	2024-07-31 21:50:49.090763	1000037	080b7691-c8be-42a2-9333-d799b34b0104	\N	N	\N	\N
1000016	1000002	1000001	Y	\N	test	test	sdf	Y	2024-08-01 17:24:11.173212	1000037	2024-08-01 17:24:11.173213	1000037	b0fa850e-dce2-4b05-9cd0-34d30175c48d	\N	N	\N	\N
1000017	1000002	1000001	Y	\N	test2	test2	afs	Y	2024-08-01 17:33:31.861228	1000037	2024-08-01 17:33:31.861228	1000037	c414adfb-8cbd-408e-b53f-bf151cfc7a1a	\N	N	\N	\N
1000018	1000002	1000001	Y	\N	t3	3	3	Y	2024-08-01 17:38:02.948418	1000037	2024-08-01 17:38:02.948418	1000037	0db0e6eb-61b0-4c64-8810-9746acea9f5d	\N	N	\N	\N
1000019	1000002	1000001	Y	\N	4	4	4	Y	2024-08-01 17:40:30.351625	1000037	2024-08-01 17:40:30.351626	1000037	61ca1fd2-3e81-476a-ace9-f94b75c5f5ee	\N	N	\N	\N
1000020	1000002	1000001	N	\N	nhomNCC	nhomNCC	\N	Y	2024-08-02 11:57:47.509889	1000037	2024-08-02 11:57:47.50989	1000037	f91e3da6-5476-462d-a5a1-383b01c366ec	\N	N	\N	\N
1000021	1000004	1000016	Y	\N	NV	Nhân Viên	\N	Y	2024-08-07 10:26:50.521323	1000069	2024-08-07 10:26:50.521323	1000069	5fffb371-4111-44a4-8822-43dad63513e1	\N	N	\N	\N
1000022	1000004	1000016	Y	\N	KH	Khách hàng	\N	Y	2024-08-07 10:28:13.852776	1000069	2024-08-07 10:28:13.852776	1000069	c669f1fc-5bef-4848-a859-05afa04d98f8	\N	N	\N	\N
1000023	1000004	1000016	Y	\N	\N	\N	\N	Y	2024-08-09 16:20:37.29161	1000069	2024-08-09 16:20:37.291614	1000069	5f1b6021-1e76-4c7c-afa8-215816d0f454	\N	N	\N	\N
1000024	1000004	1000016	N	\N	nhomncc	qwe	qwe	Y	2024-08-12 17:21:37.093838	1000069	2024-08-12 17:21:37.093845	1000069	3c754909-1dbd-4a23-b574-48c8749e62e1	\N	N	\N	\N
1000025	1000004	1000016	N	\N	nhom ncc	nhom ncc	123	Y	2024-08-14 15:37:31.809115	1000069	2024-08-14 15:37:31.809116	1000069	c47c2c53-0186-4694-89b3-d80061b1c8db	\N	N	\N	\N
1000026	1000004	1000016	Y	\N		Nhóm VIPU		Y	2024-08-21 09:51:23.936348	1000069	2024-08-21 09:51:23.936348	1000069	f5c00771-632b-439f-943a-c6a583d93f3d	\N	N	\N	\N
1000028	1000004	1000016	Y	\N	HCMGROUP	Nhóm ở Hồ Chí Minh	\N	Y	2024-08-27 16:11:30.587815	1000069	2024-08-27 16:11:30.587815	1000069	4878a534-b768-48a3-9288-4b7c7e1d5769	\N	Y	\N	\N
1000029	1000004	1000016	Y	\N	EATDBIZ	Nhóm ăn uốngDbiz	\N	Y	2024-08-28 08:29:36.529915	1000069	2024-08-28 08:29:36.529915	1000069	d9f79b1d-a7eb-46e9-af8e-08aa5bd8b1f4	\N	N	\N	\N
1000062	1000009	0	N	\N	1000003	Nhà cung cấp	\N	Y	2024-09-11 16:22:51.705894	1000071	2024-09-11 16:22:51.705894	1000071	3a0c8cc0-8191-472a-8026-e00e967d2d82	\N	N	\N	\N
1000063	1000009	0	Y	\N	1000004	Khách hàng doanh nghiệp	\N	Y	2024-09-11 17:40:53.588463	1000071	2024-09-11 17:40:53.588463	1000071	df9542e0-5fc2-4edd-88a6-a6ad7cbc43fe	\N	N	\N	\N
1000041	1000004	1000016	N	\N	111	Nhóm Hải Phòng	\N	Y	2024-09-06 09:02:29.689349	1000069	2024-09-06 09:02:29.68935	1000069	2977d3fd-b7ac-43ae-8f6d-1f09ca52b5e0	\N	N	\N	\N
1000042	1000004	1000016	Y	\N	12112	Nhóm Tuyên Quang	\N	Y	2024-09-06 09:03:47.560755	1000069	2024-09-06 09:03:47.560755	1000069	42a021a0-9ee8-4cec-b949-9e040db876d9	10.00	N	\N	\N
1000043	1000004	1000016	N	\N	12112	Nhóm Tuyên Quang	\N	Y	2024-09-06 09:11:46.203329	1000069	2024-09-06 09:11:46.20333	1000069	f29dedca-f646-4701-8bd5-97aac5bef1ff	0.00	N	\N	\N
1000044	1000004	1000016	Y	\N	1313	Nhóm Quảng Nam	\N	Y	2024-09-06 09:13:30.197859	1000069	2024-09-06 09:13:30.197859	1000069	602f26f8-11cb-480a-865d-b023989cbfdf	15.00	N	\N	\N
1000060	1000009	0	Y	\N	1000001	Khách hàng cá nhân	\N	Y	2024-09-11 16:20:18.961739	1000071	2024-09-11 16:20:18.961739	1000071	8647e56a-c0a6-4f43-83e1-9f28dc10c1fb	\N	N	\N	\N
1000061	1000009	0	Y	\N	1000002	Khách hàng lẻ	\N	Y	2024-09-11 16:20:31.260952	1000071	2024-09-11 16:20:31.260951	1000071	570aaac5-29c3-4808-a0d4-f03751f13ec9	\N	N	\N	\N
\.


--
-- TOC entry 5506 (class 0 OID 393709)
-- Dependencies: 369
-- Data for Name: d_pay_method; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_pay_method (d_pay_method_id, d_bank_id, d_tenant_id, d_org_id, description, name, d_image_id, is_default, created, created_by, updated, updated_by, d_pay_method_uu, is_active) FROM stdin;
1000004	1000001	1000004	1000016	MBB QRCODE	MBB	\N	N	2024-09-10 11:09:58.890435	1000069	2024-09-10 11:09:58.890435	1000069	1112e1cc-0295-4b66-92c3-a526990d24f7	Y
1000009	1000001	1000004	1000016	MBB QRCODE	MBB	0	N	2024-09-17 14:37:52.821461	1000069	2024-09-17 14:37:52.821461	1000069	f76c69f2-341c-44c8-a076-b77281cefc2f	Y
1000008	1000001	1000004	1000016	MBB QRCODE	MBB_QRCODE	0	N	2024-09-17 14:31:40.903479	1000069	2024-09-17 14:31:40.903479	1000069	e272f726-9829-4eab-bc06-b3419a290604	Y
1000010	1000001	1000004	1000016	MBB QRCODE	MBB	0	N	2024-09-18 16:58:12.352586	1000069	2024-09-18 16:58:12.352586	1000069	7895174b-df88-4849-9082-ca07ef875d29	Y
\.


--
-- TOC entry 5383 (class 0 OID 385728)
-- Dependencies: 242
-- Data for Name: d_payment; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_payment (d_payment_id, d_tenant_id, d_org_id, d_doctype_id, d_customer_id, d_vendor_id, d_invoice_id, d_bankaccount_id, payment_date, d_currency_id, payment_status, payment_amount, d_order_id, document_no, is_active, created, created_by, updated, updated_by, d_payment_uu, d_pos_order_id) FROM stdin;
1000002	1000002	1000001	0	1000010	\N	1000024	\N	2024-07-30	13	CL	20000	1000016	000	Y	2024-07-30 10:58:15.429177	1000037	2024-07-30 10:58:15.429177	1000037	74f59be7-8299-4114-8de0-f05411058e96	\N
1000003	1000002	1000001	0	1000010	\N	1000030	\N	2024-07-30	13	CO	25000	1000024	000	Y	2024-07-30 03:24:35.547934	1000037	2024-07-30 03:24:35.547934	1000037	7995a095-a973-402a-8da6-6259f203e417	\N
1000004	1000002	1000001	0	1000010	\N	1000031	\N	2024-07-30	13	CO	25000	1000025	000	Y	2024-07-30 03:25:08.152686	1000037	2024-07-30 03:25:08.152686	1000037	fed36547-dd97-4ca5-b63f-19976b6afa93	\N
1000005	1000002	1000001	0	1000010	\N	1000031	\N	2024-07-30	13	CO	35000	1000025	000	Y	2024-07-30 03:25:08.164264	1000037	2024-07-30 03:25:08.164264	1000037	46c96112-a5c8-4d31-a9c5-3121f9b967cf	\N
1000006	1000002	1000001	0	1000010	\N	1000030	\N	2024-07-30	13	CL	20000	1000016	000	Y	2024-07-30 04:19:48.59907	1000037	2024-07-30 04:19:48.59907	1000037	dadbaf5e-a69c-4269-9fca-f6dada002649	\N
1000007	1000002	1000001	0	1000010	\N	1000030	\N	2024-07-30	13	CL	20000	1000016	000	Y	2024-07-30 06:28:55.853074	1000037	2024-07-30 06:28:55.853074	1000037	f524cbf6-71c6-468d-9e20-8fea9d7d3932	\N
1000008	1000002	1000001	0	1000010	\N	1000032	\N	2024-07-31	13	CO	25000	1000027	000	Y	2024-07-31 10:56:36.090937	1000037	2024-07-31 10:56:36.090939	1000037	b4b1b76e-4f1f-400e-9e11-8f6b3989e1f8	\N
1000009	1000002	1000001	0	1000010	\N	1000032	\N	2024-07-31	13	CO	35000	1000027	000	Y	2024-07-31 10:56:36.093856	1000037	2024-07-31 10:56:36.093857	1000037	839d22cd-4c77-4e5f-b766-5a98e30367dc	\N
1000010	1000002	1000001	0	1000010	\N	1000033	\N	2024-07-31	13	CO	25000	1000028	000	Y	2024-07-31 10:59:26.984011	1000037	2024-07-31 10:59:26.984013	1000037	2a6c0077-b397-4c36-96a5-70efd57bdd11	\N
1000011	1000002	1000001	0	1000010	\N	1000033	\N	2024-07-31	13	CO	35000	1000028	000	Y	2024-07-31 10:59:26.986088	1000037	2024-07-31 10:59:26.98609	1000037	7cbcae1f-01c4-4f25-ac24-3ccd522b9106	\N
1000012	1000003	1000001	0	1000010	\N	1000034	\N	2024-08-09	13	CO	25000	1000029	000	Y	2024-08-09 15:03:53.412495	1000068	2024-08-09 15:03:53.412498	1000068	b48cbd9b-4728-4baa-8906-0696ae4079b5	\N
1000013	1000003	1000001	0	1000010	\N	1000034	\N	2024-08-09	13	CO	35000	1000029	000	Y	2024-08-09 15:03:53.424974	1000068	2024-08-09 15:03:53.424976	1000068	9ba5e37e-bb0a-4a0d-8067-dbabc5df7bda	\N
1000016	1000004	1000016	0	1000010	\N	1000037	\N	2024-08-09	13	CO	25000	1000032	000	Y	2024-08-10 10:00:52.654905	1000069	2024-08-10 10:00:52.654906	1000069	a4249457-f672-459e-bf5f-c73208ca175a	\N
1000017	1000004	1000016	0	1000010	\N	1000038	\N	2024-08-09	13	CO	25000	1000033	000	Y	2024-08-10 10:00:53.058132	1000069	2024-08-10 10:00:53.058133	1000069	ec7248d9-35ab-4b71-9482-6e1d42d06957	\N
1000018	1000004	1000016	0	1000010	\N	1000039	\N	2024-08-09	13	CO	25000	1000034	000	Y	2024-08-10 10:58:37.702719	1000069	2024-08-10 10:58:37.70272	1000069	beaba94c-6601-458d-945d-338c6be41584	\N
1000019	1000004	1000016	0	1000010	\N	1000040	\N	2024-08-09	13	CO	25000	1000035	000	Y	2024-08-10 11:43:05.118111	1000069	2024-08-10 11:43:05.118113	1000069	408f2a90-fcd5-4956-8352-fb9f2ba73a50	\N
1000020	1000004	1000016	0	1000010	\N	1000041	\N	2024-08-12	13	CO	25000	1000036	000	Y	2024-08-12 10:04:55.885472	1000069	2024-08-12 10:04:55.885474	1000069	fee65924-d8c4-456a-b29c-f1286cb70ee8	\N
1000021	1000004	1000016	0	1000010	\N	1000042	\N	2024-08-12	13	CO	200	1000037	000	Y	2024-08-12 10:10:48.45268	1000069	2024-08-12 10:10:48.452681	1000069	6ac97a95-3165-46ab-8ca3-b8602f14aa5b	\N
1000022	1000004	1000016	0	1000010	\N	1000043	\N	2024-08-12	13	CO	200	1000038	000	Y	2024-08-12 10:11:12.691586	1000069	2024-08-12 10:11:12.691587	1000069	128db8b0-1b48-488a-bfcd-3b71c95b2c38	\N
1000023	1000004	1000016	0	1000010	\N	1000044	\N	2024-08-12	13	CO	200	1000039	000	Y	2024-08-12 10:19:11.110392	1000069	2024-08-12 10:19:11.110394	1000069	ceac864a-2c9d-4afa-b502-d84aaa461444	\N
1000024	1000004	1000016	0	1000010	\N	1000045	\N	2024-08-12	13	CO	200	1000040	000	Y	2024-08-12 10:23:20.08498	1000069	2024-08-12 10:23:20.084981	1000069	6343858d-9818-41b8-94f7-d850c4ea75c0	\N
1000025	1000004	1000016	0	1000010	\N	1000046	\N	2024-08-12	13	CO	100	1000041	000	Y	2024-08-12 10:29:07.65298	1000069	2024-08-12 10:29:07.652981	1000069	603c454b-e236-4582-a06a-878046697bb9	\N
1000026	1000004	1000016	0	1000010	\N	1000047	\N	2024-08-12	13	CO	100	1000042	000	Y	2024-08-12 10:29:38.140399	1000069	2024-08-12 10:29:38.1404	1000069	f4127057-f7a4-45b6-a364-5b7f6177a616	\N
1000027	1000004	1000016	0	1000010	\N	1000048	\N	2024-08-12	13	CO	200	1000043	000	Y	2024-08-12 10:33:26.327064	1000069	2024-08-12 10:33:26.327065	1000069	47ae9e1b-766f-4ebf-8703-2b263e8ddcd8	\N
1000028	1000004	1000016	0	1000010	\N	1000049	\N	2024-08-12	13	CO	100	1000044	000	Y	2024-08-12 10:36:41.534121	1000069	2024-08-12 10:36:41.534122	1000069	5f2b5818-bfb6-42bc-967d-62fd15e9e068	\N
1000029	1000004	1000016	0	1000010	\N	1000050	\N	2024-08-12	13	CO	100	1000045	000	Y	2024-08-12 10:38:20.325693	1000069	2024-08-12 10:38:20.325694	1000069	eb8948c3-4ecf-4339-aa5d-21b788c67042	\N
1000030	1000004	1000016	0	1000010	\N	1000051	\N	2024-08-12	13	CO	100	1000046	000	Y	2024-08-12 10:44:29.388314	1000069	2024-08-12 10:44:29.388315	1000069	8e13a10c-c618-4ad1-afa9-37218c368a6c	\N
1000031	1000004	1000016	0	1000010	\N	1000052	\N	2024-08-12	13	CO	200	1000047	000	Y	2024-08-12 10:50:23.552932	1000069	2024-08-12 10:50:23.552933	1000069	4fe4613f-2c9f-478e-86d6-14d67e1297ff	\N
1000032	1000004	1000016	0	1000010	\N	1000053	\N	2024-08-12	13	CO	200	1000048	000	Y	2024-08-12 10:51:02.659424	1000069	2024-08-12 10:51:02.659425	1000069	0ca130f5-4612-448e-8314-401d59def09a	\N
1000033	1000004	1000016	0	1000010	\N	1000054	\N	2024-08-12	13	CO	100	1000049	000	Y	2024-08-12 10:55:36.704522	1000069	2024-08-12 10:55:36.704545	1000069	5eca75f1-3d61-41f2-a68f-1af443e6cd6b	\N
1000034	1000004	1000016	0	1000010	\N	1000055	\N	2024-08-12	13	CO	100	1000050	000	Y	2024-08-12 10:57:12.972683	1000069	2024-08-12 10:57:12.972684	1000069	42bd087d-d22e-4116-970a-a7a3bebf8014	\N
1000035	1000004	1000016	0	1000010	\N	1000056	\N	2024-08-12	13	CO	100	1000051	000	Y	2024-08-12 10:57:29.710343	1000069	2024-08-12 10:57:29.710344	1000069	9d06023e-4f7b-4e75-993a-db1a8ca72460	\N
1000036	1000004	1000016	0	1000010	\N	1000057	\N	2024-08-12	13	CO	100	1000052	000	Y	2024-08-12 10:58:53.825584	1000069	2024-08-12 10:58:53.825585	1000069	1d32ae09-0e0a-4961-bcb0-6c134496ec95	\N
1000037	1000004	1000016	0	1000010	\N	1000058	\N	2024-08-12	13	CO	100	1000053	000	Y	2024-08-12 10:59:07.970559	1000069	2024-08-12 10:59:07.970559	1000069	43aa27fe-3249-43e7-b94e-3a35dc4e1130	\N
1000038	1000004	1000016	0	1000010	\N	1000059	\N	2024-08-12	13	CO	100	1000054	000	Y	2024-08-12 11:01:04.075416	1000069	2024-08-12 11:01:04.075417	1000069	be0ddc89-a528-447b-80ea-2bddcb4f94b6	\N
1000039	1000004	1000016	0	1000010	\N	1000060	\N	2024-08-12	13	CO	100	1000055	000	Y	2024-08-12 11:01:52.036414	1000069	2024-08-12 11:01:52.036414	1000069	c4aff7bf-54a0-4ac6-8095-33afc38c45f2	\N
1000040	1000004	1000016	0	1000010	\N	1000061	\N	2024-08-12	13	CO	200	1000056	000	Y	2024-08-12 11:07:40.223012	1000069	2024-08-12 11:07:40.223012	1000069	9d1a3aef-7179-4830-939e-b64bf0547a2e	\N
1000041	1000004	1000016	0	1000010	\N	1000062	\N	2024-08-12	13	CO	100	1000057	000	Y	2024-08-12 11:09:02.027852	1000069	2024-08-12 11:09:02.027852	1000069	3ac2b2c1-8f19-4172-9de7-43b3a3cb429c	\N
1000042	1000004	1000016	0	1000010	\N	1000063	\N	2024-08-12	13	CO	100	1000058	000	Y	2024-08-12 11:09:15.137215	1000069	2024-08-12 11:09:15.137215	1000069	eb9f0c2c-98a3-4307-9526-1663d46d8d3a	\N
1000043	1000004	1000016	0	1000010	\N	1000064	\N	2024-08-12	13	CO	100	1000059	000	Y	2024-08-12 11:09:57.430168	1000069	2024-08-12 11:09:57.430169	1000069	1138bb91-fe0d-4acc-8ed7-27d4d285f25a	\N
1000044	1000004	1000016	0	1000010	\N	1000065	\N	2024-08-12	13	CO	100	1000060	000	Y	2024-08-12 11:10:42.937737	1000069	2024-08-12 11:10:42.937738	1000069	d16107c6-fa59-4aeb-bad6-caf36e652c78	\N
1000045	1000004	1000016	0	1000010	\N	1000066	\N	2024-08-12	13	CO	100	1000061	000	Y	2024-08-12 11:11:01.816941	1000069	2024-08-12 11:11:01.816941	1000069	ce8a0750-83ba-48c7-8242-f0fc9d90d072	\N
1000046	1000004	1000016	0	1000010	\N	1000067	\N	2024-08-12	13	CO	100	1000062	000	Y	2024-08-12 11:15:13.738545	1000069	2024-08-12 11:15:13.738545	1000069	4e4482ab-d1cb-4642-bf74-9bab337317ff	\N
1000049	1000004	1000016	0	1000010	\N	1000070	\N	2024-08-12	13	CO	0	1000065	000	Y	2024-08-12 22:44:34.109008	1000069	2024-08-12 22:44:34.109009	1000069	98fd0a12-7fca-4f3e-8cc3-59aaee467784	\N
1000048	1000004	1000016	0	\N	1000058	1000069	\N	2024-08-12	13	CO	200	1000064	000	Y	2024-08-12 14:35:56.110887	1000069	2024-08-12 14:35:56.110888	1000069	3b03134d-7103-42ce-88de-4611e57336a0	\N
1000047	1000004	1000016	0	\N	1000058	1000068	\N	2024-08-12	13	CO	150	1000063	000	Y	2024-08-12 11:18:34.801235	1000069	2024-08-12 11:18:34.801236	1000069	450b9ede-a769-4893-9662-2fe4087ac901	\N
1000050	1000004	1000016	0	1000010	\N	1000071	\N	2024-08-12	13	CO	0	1000066	000	Y	2024-08-13 18:43:45.215458	1000069	2024-08-13 18:43:45.215458	1000069	84b2e4f6-54ed-4d00-b36f-345bc4f982e9	\N
1000051	1000004	1000016	0	1000010	\N	1000072	\N	2024-08-12	13	CO	0	1000067	000	Y	2024-08-14 11:24:37.3876	1000069	2024-08-14 11:24:37.3876	1000069	bd55e419-4977-49de-970c-0d3734a4adf3	\N
1000052	1000004	1000016	0	1000010	\N	1000073	\N	2024-08-12	13	CO	200	1000068	000	Y	2024-08-14 11:26:10.292538	1000069	2024-08-14 11:26:10.292539	1000069	62bb822b-3c51-4936-a4f8-c74aa55bedd8	\N
1000053	1000004	1000016	0	1000010	\N	1000074	\N	2024-08-12	13	CO	0	1000069	000	Y	2024-08-14 11:27:04.417646	1000069	2024-08-14 11:27:04.417646	1000069	bd1dbaac-0ac3-42e1-b0fe-829fae03809a	\N
1000054	1000004	1000016	0	1000010	\N	1000075	\N	2024-08-12	13	CO	0	1000070	000	Y	2024-08-14 23:12:30.31151	1000069	2024-08-14 23:12:30.311511	1000069	affe9cb3-0881-4b4c-b26b-8809eee0bce0	\N
1000055	1000004	1000016	0	1000010	\N	1000076	\N	2024-08-12	13	CO	50000	1000071	000	Y	2024-08-15 16:14:24.181167	1000069	2024-08-15 16:14:24.181167	1000069	5c2b8109-8701-457f-9525-aebfd33bf52e	\N
1000056	1000004	1000016	0	1000010	\N	1000077	\N	2024-08-12	13	CO	0	1000072	000	Y	2024-08-17 15:11:36.415061	1000069	2024-08-17 15:11:36.415061	1000069	aa2b0575-0af7-4c5e-af37-38eb57397e39	\N
1000057	1000004	1000016	0	1000010	\N	1000078	\N	2024-08-24	13	CO	0	1000073	000	Y	2024-08-24 12:12:48.32186	1000069	2024-08-24 12:12:48.321864	1000069	002eafd7-8f53-4475-80a3-65cc4e3ffc82	\N
1000058	1000004	1000016	0	1000010	\N	1000079	\N	2024-08-26	13	CO	0	1000074	000	Y	2024-08-26 09:51:12.333887	1000069	2024-08-26 09:51:12.333888	1000069	56cab834-88b5-42ff-add3-d608cae5153c	\N
1000059	1000004	1000016	0	1000010	\N	1000080	\N	2024-08-26	13	CO	0	1000075	000	Y	2024-08-26 09:51:35.704394	1000069	2024-08-26 09:51:35.704395	1000069	6e9bcea8-ccf5-4992-9495-0c2cff3d20af	\N
1000060	1000004	1000016	0	1000010	\N	1000081	\N	2024-08-26	13	CO	0	1000076	000	Y	2024-08-27 10:27:48.284314	1000069	2024-08-27 10:27:48.284316	1000069	6d4e174c-2533-480d-9b46-9cd6ccaab002	\N
1000061	1000004	1000016	0	1000010	\N	1000082	\N	2024-08-26	13	CO	0	1000077	000	Y	2024-08-27 12:03:16.574169	1000069	2024-08-27 12:03:16.57417	1000069	0cccbaa7-e85c-4a94-a576-5a70aef24751	\N
1000062	1000004	1000016	0	1000010	\N	1000083	\N	2024-08-26	13	CO	0	1000078	000	Y	2024-08-30 18:50:10.906742	1000069	2024-08-30 18:50:10.906744	1000069	3b25db70-2a80-4eaf-a00f-b2503cfaf86d	\N
1000064	1000004	1000016	0	1000085	\N	1000091	\N	2024-09-08	13	COL	25000	\N	PAYMENT1000063	Y	2024-09-08 17:32:59.405133	1000069	2024-09-08 17:32:59.405133	1000069	bb1f4a88-929a-4e31-b400-1fc04853ee0f	1000194
1000066	1000004	1000016	0	1000085	\N	1000091	\N	2024-09-08	13	COL	35000	\N	PAYMENT1000065	Y	2024-09-08 17:32:59.487137	1000069	2024-09-08 17:32:59.487137	1000069	abc2500e-31d5-4a2e-b9aa-d4442a9efbdf	1000194
1000068	1000004	1000016	0	1000085	\N	1000093	\N	2024-09-08	13	COL	25000	\N	PAYMENT1000067	Y	2024-09-08 17:45:09.280446	1000069	2024-09-08 17:45:09.280446	1000069	ca2bf5d5-97c4-421d-8765-033144f6f6a4	1000194
1000070	1000004	1000016	0	1000085	\N	1000093	\N	2024-09-08	13	COL	35000	\N	PAYMENT1000069	Y	2024-09-08 17:45:09.31086	1000069	2024-09-08 17:45:09.31086	1000069	6f1638bb-0674-4d55-9c8e-014df4670703	1000194
1000072	1000004	1000016	0	1000085	\N	1000095	\N	2024-09-08	13	COL	25000	\N	PAYMENT1000071	Y	2024-09-08 17:52:41.876163	1000069	2024-09-08 17:52:41.876163	1000069	85b5f6f6-1b14-417b-b178-b6b366a0d095	1000194
1000074	1000004	1000016	0	1000085	\N	1000095	\N	2024-09-08	13	COL	35000	\N	PAYMENT1000073	Y	2024-09-08 17:52:41.90786	1000069	2024-09-08 17:52:41.90786	1000069	320b0f13-5264-4d9c-8f6e-9ce76cad8bcb	1000194
1000076	0	1000016	0	1000085	\N	1000097	\N	2024-09-08	13	COL	25000	\N	PAYMENT1000075	Y	2024-09-08 11:21:11.498938	0	2024-09-08 11:21:11.498939	0	c1d18178-fd96-4f01-aca0-e749c55b489b	1000198
1000078	0	1000016	0	1000085	\N	1000097	\N	2024-09-08	13	COL	35000	\N	PAYMENT1000077	Y	2024-09-08 11:21:11.526461	0	2024-09-08 11:21:11.526462	0	eb8d0771-5f60-46ae-b947-14276f61de64	1000198
1000080	0	1000016	0	1000085	\N	1000099	9000000	2024-09-09	13	COL	25000	\N	PAYMENT1000079	Y	2024-09-09 00:32:08.005665	0	2024-09-09 00:32:08.005665	0	a0668ea1-c870-47ff-923b-97b7453d505e	1000204
1000082	0	1000016	0	1000085	\N	1000099	1000001	2024-09-09	13	COL	35000	\N	PAYMENT1000081	Y	2024-09-09 00:32:08.027396	0	2024-09-09 00:32:08.027396	0	64ce968b-b631-416b-a582-115519c8b298	1000204
1000084	0	1000016	0	1000085	\N	1000101	9000000	2024-09-09	13	COL	25000	\N	PAYMENT1000083	Y	2024-09-09 00:38:21.15569	0	2024-09-09 00:38:21.15569	0	e5d889e0-4b57-45bd-be1e-f73d67988493	1000204
1000086	0	1000016	0	1000085	\N	1000101	1000001	2024-09-09	13	COL	35000	\N	PAYMENT1000085	Y	2024-09-09 00:38:21.176111	0	2024-09-09 00:38:21.176111	0	0eb53e5d-d8a0-4921-8dc3-4a1cac5d3fb4	1000204
1000088	1000004	1000016	0	1000085	\N	1000103	9000000	2024-09-09	13	COL	25000	\N	PAYMENT1000087	Y	2024-09-09 00:46:56.143572	0	2024-09-09 00:46:56.143572	0	9eec7a02-1da3-4ef9-88ea-c93a028ad2d4	1000204
1000090	1000004	1000016	0	1000085	\N	1000103	1000001	2024-09-09	13	COL	35000	\N	PAYMENT1000089	Y	2024-09-09 00:46:56.159513	0	2024-09-09 00:46:56.159513	0	de90adab-86cd-4474-aefd-7f089dcef85c	1000204
1000092	1000004	1000016	0	1000085	\N	1000105	9000000	2024-09-09	13	COL	25000	\N	PAYMENT1000091	Y	2024-09-09 00:50:02.695303	0	2024-09-09 00:50:02.695303	0	f2306c47-d797-4e0b-a4f0-b5ad51100f66	1000204
1000094	1000004	1000016	0	1000085	\N	1000105	1000001	2024-09-09	13	COL	35000	\N	PAYMENT1000093	Y	2024-09-09 00:50:02.713142	0	2024-09-09 00:50:02.713142	0	f44f3f84-8a4c-464e-ba31-609c7249a466	1000204
1000096	1000004	1000016	0	1000085	\N	1000107	9000000	2024-09-09	13	COL	25000	\N	PAYMENT1000095	Y	2024-09-09 00:50:58.518289	0	2024-09-09 00:50:58.518289	0	fdc4627c-ce7d-45d4-b4c3-fa245b1a00c5	1000204
1000098	1000004	1000016	0	1000085	\N	1000107	1000001	2024-09-09	13	COL	35000	\N	PAYMENT1000097	Y	2024-09-09 00:50:58.533285	0	2024-09-09 00:50:58.533285	0	a49ae7bb-8037-4b4a-b6a9-79eeb0eabc1a	1000204
1000100	1000004	1000016	0	\N	\N	1000109	1000001	2024-09-18	13	COM	2000	\N	PAYMENT1000099	Y	2024-09-18 06:59:59.124624	0	2024-09-18 06:59:59.124625	0	f775ca63-5b23-4fa4-bc62-8076a6885f0a	1000489
1000102	1000004	1000016	0	\N	\N	1000111	1000001	2024-09-18	13	COM	2000	\N	PAYMENT1000101	Y	2024-09-18 07:02:11.193346	0	2024-09-18 07:02:11.193347	0	15d988e9-fa48-4492-9ebe-a682e8e77171	1000489
1000104	1000004	1000016	0	\N	\N	1000113	1000001	2024-09-18	13	COM	2000	\N	PAYMENT1000103	Y	2024-09-18 07:18:09.382973	0	2024-09-18 07:18:09.382974	0	7d25a19e-fd67-444c-bba8-50fe39f74ad0	1000489
1000106	1000004	1000016	0	\N	\N	1000115	1000001	2024-09-18	13	COM	2000	\N	PAYMENT1000105	Y	2024-09-18 07:30:04.183111	0	2024-09-18 07:30:04.183111	0	ea0780d9-f752-4f57-a541-f8683de7cce9	1000489
1000108	1000004	1000016	0	\N	\N	1000117	1000001	2024-09-18	13	COM	2000	\N	PAYMENT1000107	Y	2024-09-18 07:41:29.091787	0	2024-09-18 07:41:29.091787	0	5549bff1-03be-4662-97b0-9bc3c777ddde	1000491
1000110	1000004	1000016	0	\N	\N	1000119	1000001	2024-09-18	13	COM	2000	\N	PAYMENT1000109	Y	2024-09-18 08:24:09.502799	0	2024-09-18 08:24:09.5028	0	5a167322-97c9-4d67-86d5-0a541693e48d	1000493
1000112	1000004	1000016	0	\N	\N	1000121	1000001	2024-09-18	13	COM	2000	\N	PAYMENT1000111	Y	2024-09-18 08:26:10.693169	0	2024-09-18 08:26:10.693169	0	68cea754-d63c-410c-b2dd-e46c19f54d26	1000495
\.


--
-- TOC entry 5508 (class 0 OID 393764)
-- Dependencies: 371
-- Data for Name: d_paymethod_org; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_paymethod_org (d_paymethod_org_id, d_pay_method_id, d_tenant_id, d_org_id, access_code, terminal_id, hash_key, description, created, created_by, updated, updated_by, d_paymethod_org_uu, is_active, merchant_code) FROM stdin;
1000003	1000004	1000004	1000016	Access_code	123123	dfads		2024-09-10 11:09:59.483682	1000069	2024-09-10 11:09:59.483682	1000069	b858c845-79bc-41ad-a619-2943070db885	Y	SSGJSC
1000007	1000008	1000004	1000016	MB_SSGJSC	SSGJSC1	NdNgCLQ9Uo7C5qWyZbKWGyONavFUD06XIiDJvDBbi20cRCmcwnUvzDoaRljZsKax		2024-09-17 14:31:41.173638	1000069	2024-09-17 14:31:41.173638	1000069	52e94a6b-d86c-496b-9138-1ed9beab0e1f	N	SSGJSC
1000008	1000009	1000004	1000016	Access_code	123123	dfads		2024-09-17 14:37:52.839271	1000069	2024-09-17 14:37:52.839271	1000069	bf02bf5e-5d5a-4ae4-8807-5d3b75d32b89	N	SSGJSC
1000009	1000010	1000004	1000016	Access_code	123123	dfads		2024-09-18 16:58:12.364933	1000069	2024-09-18 16:58:12.364934	1000069	d0c40710-9476-4775-a0f7-3f2d0495ff99	N	\N
\.


--
-- TOC entry 5490 (class 0 OID 393129)
-- Dependencies: 349
-- Data for Name: d_pc_terminalaccess; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_pc_terminalaccess (d_pc_terminalaccess_id, d_tenant_id, d_org_id, d_pos_terminal_id, erp_pc_terminalaccess_id, created, created_by, updated, updated_by, d_pc_terminalaccess_uu, is_active) FROM stdin;
\.


--
-- TOC entry 5384 (class 0 OID 385742)
-- Dependencies: 243
-- Data for Name: d_pos_order; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_pos_order (d_pos_order_id, d_tenant_id, d_org_id, d_customer_id, phone, order_status, source, is_locked, d_table_id, d_floor_id, d_user_id, order_guests, is_active, order_date, created, created_by, updated, updated_by, customer_name, document_no, d_currency_id, d_pricelist_id, d_pos_id, d_pos_order_uu, total_amount, erp_pos_order_id, is_applied_sercharge, flat_discount, d_pos_terminal_id, erp_pos_order_no, bill_no, d_shift_control_id, is_processed, qrcode_payment, ftcode, d_reconciledetail_id, is_sync_erp, d_bankaccount_id, d_bank_id, total_line, d_doctype_id) FROM stdin;
1000409	1000004	1000016	\N	00000000	DRA	POS	\N	1000073	1000073	1000042	\N	Y	2024-09-16	2024-09-16 10:28:52.346886	1000069	2024-09-18 13:43:52.23869	1000069	Thanh NC	POS1000408	1000000	\N	\N	9861392f-606c-48e6-a68b-a1d1475aa0df	40000	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ00039aanm0208QRIBFTTA53037045405400005802VN62190107NPS68690804CKPM63043C34	\N	\N	\N	\N	\N	40000	\N
1000421	1000004	1000016	\N	00000000	DRA	POS	\N	1000067	1000067	1000042	\N	Y	2024-09-16	2024-09-16 15:38:20.18922	1000069	2024-09-17 09:49:17.886188	1000069	Thanh NC	POS1000420	1000000	\N	\N	0c9e715f-3f87-4296-80c6-7a1e1282bcd9	64800	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ00038t3320208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM63043600	\N	\N	\N	\N	\N	60000	\N
1000383	1000009	1000022	1000094	\N	DRA	POS	Y	0	0	1000073	\N	Y	2024-09-12	2024-09-12 04:40:14.110593	0	2024-09-12 04:40:14.110593	0	Lầu Sẹc Dần	POS1000377	1000000	1000049	\N	01792074-9957-4db5-a47c-9bccd795fb57	\N	\N	N	\N	1000003	\N	\N	\N	N	\N	\N	\N	N	\N	\N	\N	1000002
1000384	1000009	1000026	1000090	\N	DRA	POS	Y	0	0	1000072	\N	Y	2024-09-12	2024-09-12 04:40:14.110593	0	2024-09-12 04:40:14.110593	0	DBIZ	POS1000378	1000000	1000049	\N	b5ddbf75-4eee-4b9a-af6e-5a053c4752ae	\N	\N	N	\N	1000003	\N	\N	\N	N	\N	\N	\N	N	\N	\N	\N	1000002
1000385	1000009	1000027	1000093	\N	DRA	POS	Y	0	0	1000072	\N	Y	2024-09-12	2024-09-12 04:40:14.110593	0	2024-09-12 04:40:14.110593	0	SSG	POS1000379	1000000	1000049	\N	0897a39b-dfd6-4f06-a31c-154e0e776ebb	\N	\N	N	\N	1000003	\N	\N	\N	N	\N	\N	\N	N	\N	\N	\N	1000002
1000397	1000004	1000016	\N	\N	DRA	POS	\N	1000071	999976	0	\N	Y	2024-09-13	2024-09-13 14:11:16.113082	1000069	2024-09-17 16:43:23.779908	1000069	Lầu Sẹc Dần 	POS1000396	1000000	\N	\N	17af3b23-0c2d-4c0e-9440-cd7a6e47db70	\N	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
1000419	1000004	1000016	\N	00000000	DRA	POS	\N	1000067	1000067	1000042	\N	Y	2024-09-16	2024-09-16 15:38:20.091741	1000069	2024-09-16 15:38:20.128898	1000069	Thanh NC	POS1000418	1000000	\N	\N	668548c9-6a10-4ad5-8892-9f0faa476d52	64800	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	60000	\N
1000423	1000004	1000016	\N	00000000	DRA	POS	\N	1000067	1000067	1000042	\N	Y	2024-09-16	2024-09-16 15:38:36.289307	1000069	2024-09-16 15:38:36.352415	1000069	Thanh NC	POS1000422	1000000	\N	\N	4c704d66-cf19-4e98-bcd9-4ba577be483e	20000	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	20000	\N
1000425	1000004	1000016	\N	00000000	DRA	POS	\N	1000067	1000067	1000042	\N	Y	2024-09-16	2024-09-16 15:38:36.346011	1000069	2024-09-16 15:38:38.619117	1000069	Thanh NC	POS1000424	1000000	\N	\N	380d6360-bb8c-4e7b-819b-b7b06cc95b82	20000	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	20000	\N
1000395	1000004	1000016	\N	\N	DRA	POS	\N	1000040	999972	0	\N	Y	2024-09-13	2024-09-13 14:06:43.644118	1000069	2024-09-19 00:32:18.620579	1000069	Lầu Sẹc Dần 	POS1000394	1000000	\N	\N	bbd40c96-5271-4764-b2cd-8953593ee0f3	\N	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
1000429	1000004	1000016	\N	00000000	DRA	POS	\N	1000067	1000067	1000042	\N	Y	2024-09-16	2024-09-16 15:39:02.123092	1000069	2024-09-16 15:39:02.170892	1000069	Thanh NC	POS1000428	1000000	\N	\N	56573632-22e7-412d-9538-8164da73f5aa	61600	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	60000	\N
1000483	1000004	1000016	\N	00000000	DRA	POS	\N	1000070	1000070	1000042	\N	Y	2024-09-17	2024-09-17 17:34:51.456229	1000069	2024-09-17 17:44:50.554452	1000069	Thanh NC	POS1000481	1000000	\N	1000483	e3d73749-44f6-4c79-8e95-87a66da5f9b7	2299100	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	409100	\N
1000485	1000004	1000016	\N	00000000	DRA	POS	\N	1000066	1000066	1000042	\N	Y	2024-09-17	2024-09-17 17:46:18.342901	1000069	2024-09-17 17:48:25.556571	1000069	Thanh NC	POS1000484	1000000	\N	1000485	32c6f1b9-771a-4990-9e28-b773881f7e89	2778000	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	778000	\N
1000509	1000004	1000030	\N	00000000	DRA	POS	\N	1000087	1000087	1000042	\N	Y	2024-09-18	2024-09-18 22:08:09.587176	1000069	2024-09-19 15:25:18.457832	1000069	Thanh NC	POS1000508	1000000	\N	\N	61ca7523-f4a4-4e49-a654-bb03693e7544	0	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	\N
1000505	1000004	1000016	\N	00000000	DRA	POS	\N	1000043	1000043	1000042	\N	Y	2024-09-18	2024-09-18 16:20:44.222253	1000069	2024-09-19 15:25:18.457832	1000069	Thanh NC	POS1000504	1000000	\N	\N	da67c18d-c2e6-4de2-befb-303675d36d26	21600	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	20000	\N
1000507	1000004	1000016	\N	00000000	DRA	POS	\N	1000062	1000062	1000042	\N	Y	2024-09-18	2024-09-18 16:30:38.698382	1000069	2024-09-19 15:25:18.457832	1000069	Thanh NC	POS1000506	1000000	\N	\N	6126afa4-e200-49f9-a814-37f93449df50	209000	\N	\N	\N	\N	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ00039cr9m0208QRIBFTTA5303704540726090005802VN62190107NPS68690804CKPM63046F34	\N	\N	\N	\N	\N	209000	\N
1000427	1000004	1000016	\N	00000000	DRA	POS	\N	1000067	1000067	1000042	\N	Y	2024-09-16	2024-09-16 15:39:02.051837	1000069	2024-09-16 15:39:29.388773	1000069	Thanh NC	POS1000426	1000000	\N	\N	ee049ed2-195c-42f1-9c4d-e71b714ba45e	61600	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	60000	\N
1000437	1000004	1000016	\N	00000000	DRA	POS	\N	1000042	1000042	1000042	\N	Y	2024-09-17	2024-09-17 14:53:37.795622	1000069	2024-09-18 16:14:02.536199	1000069	Thanh NC	POS1000436	1000000	\N	\N	3f131490-15d6-4963-ae15-08e57051c241	0	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	\N
1000431	1000004	1000016	\N	00000000	DRA	POS	\N	1000067	1000067	1000042	\N	Y	2024-09-16	2024-09-16 15:39:34.167018	1000069	2024-09-16 15:39:34.203863	1000069	Thanh NC	POS1000430	1000000	\N	\N	3c58fa65-b89d-493c-8dc0-8bc2a9ce23a1	20000	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	20000	\N
1000433	1000004	1000016	\N	00000000	DRA	POS	\N	1000067	1000067	1000042	\N	Y	2024-09-16	2024-09-16 15:40:46.44708	1000069	2024-09-16 15:40:46.508936	1000069	Thanh NC	POS1000432	1000000	\N	\N	87eef78e-8fa2-461b-b94e-d6e78170f52f	20000	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	20000	\N
1000435	1000004	1000016	\N	00000000	DRA	POS	\N	1000067	1000067	1000042	\N	Y	2024-09-16	2024-09-16 15:40:46.546359	1000069	2024-09-16 15:40:46.592403	1000069	Thanh NC	POS1000434	1000000	\N	\N	f04a3c91-d4e1-4a31-aef3-119bd427c0dd	20000	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	20000	\N
1000439	1000004	1000016	\N	00000000	DRA	POS	\N	1000042	1000042	1000042	\N	Y	2024-09-17	2024-09-17 14:53:37.883039	1000069	2024-09-17 14:53:37.90623	1000069	Thanh NC	POS1000438	1000000	\N	\N	edc68a79-d275-495e-998d-098a7d9910f6	0	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	\N
1000441	1000004	1000016	\N	00000000	DRA	POS	\N	1000068	1000068	1000042	\N	Y	2024-09-17	2024-09-17 16:36:34.104197	1000069	2024-09-18 09:33:19.101162	1000069	Thanh NC	POS1000440	1000000	\N	\N	9703386d-c743-4a18-a215-f98d64a8669d	0	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	\N
1000475	1000004	1000016	\N	00000000	DRA	POS	\N	1000070	1000070	1000042	\N	Y	2024-09-17	2024-09-17 17:24:52.799012	1000069	2024-09-17 17:43:09.975341	1000069	Thanh NC	POS1000474	1000000	\N	1000475	0977d51c-49d4-4259-8b55-5f2f66ca97ae	2322200	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	429000	\N
1000497	1000004	1000016	\N	00000000	DRA	POS	\N	1000063	1000063	1000042	\N	Y	2024-09-17	2024-09-17 18:12:33.418158	1000069	2024-09-19 15:25:18.458833	1000069	Thanh NC	POS1000496	1000000	\N	1000497	361ddb50-f278-439c-9669-4d9e041890c7	2589000	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	589000	\N
1000469	1000004	1000016	\N	00000000	DRA	POS	\N	1000070	1000070	1000042	\N	Y	2024-09-17	2024-09-17 17:21:33.186955	1000069	2024-09-17 17:21:33.251238	1000069	Thanh NC	POS1000468	1000000	\N	\N	2675941e-c2c0-4ef5-85a3-fd2a19f9e18a	243200	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	240000	\N
1000471	1000004	1000016	\N	00000000	DRA	POS	\N	1000070	1000070	1000042	\N	Y	2024-09-17	2024-09-17 17:21:33.227995	1000069	2024-09-17 17:21:33.31489	1000069	Thanh NC	POS1000470	1000000	\N	\N	2a09caf0-9106-481d-93b2-8ade31b0d924	243200	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	240000	\N
1000473	1000004	1000016	\N	00000000	DRA	POS	\N	1000070	1000070	1000042	\N	Y	2024-09-17	2024-09-17 17:23:24.500341	1000069	2024-09-17 17:23:24.546497	1000069	Thanh NC	POS1000472	1000000	\N	\N	4cacbb11-42ea-4f8a-a83e-adfec06ab125	243200	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	240000	\N
1000463	1000004	1000016	\N	00000000	DRA	POS	\N	1000070	1000070	1000042	\N	Y	2024-09-17	2024-09-17 17:20:47.352217	1000069	2024-09-17 10:23:25.141888	1000069	Thanh NC	POS1000462	1000000	\N	\N	4ade39b9-d5ea-49ae-ba78-5d449a8cb2f4	256000	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ00038tt5u0208QRIBFTTA530370454062432005802VN62190107NPS68690804CKPM6304BA0F	\N	\N	\N	\N	\N	240000	\N
1000479	1000004	1000016	\N	00000000	DRA	POS	\N	1000070	1000070	1000042	\N	Y	2024-09-17	2024-09-17 17:35:13.867465	1000069	2024-09-17 17:35:14.099225	1000069	Thanh NC	POS1000478	1000000	\N	\N	6247371e-d79b-4b02-a272-ba80dcce2ed3	2460000	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	460000	\N
1000465	1000004	1000016	\N	00000000	DRA	POS	\N	1000070	1000070	1000042	\N	Y	2024-09-17	2024-09-17 17:20:47.407057	1000069	2024-09-17 17:24:35.48899	1000069	Thanh NC	POS1000464	1000000	\N	\N	a4f70019-7013-48de-a064-6363655b0019	256000	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	240000	\N
1000457	1000004	1000016	\N	00000000	DRA	POS	\N	1000070	1000070	1000042	\N	Y	2024-09-17	2024-09-17 17:07:58.136854	1000069	2024-09-17 10:34:27.271854	1000069	Thanh NC	POS1000456	1000000	\N	\N	055e74c0-9cbd-4427-8d70-b2ffde6794f6	0	\N	\N	\N	1000003	\N	\N	\N	\N		\N	\N	\N	\N	\N	0	\N
1000459	1000004	1000016	\N	00000000	DRA	POS	\N	1000070	1000070	1000042	\N	Y	2024-09-17	2024-09-17 17:07:58.254829	1000069	2024-09-17 17:34:37.540505	1000069	Thanh NC	POS1000458	1000000	\N	\N	53146fb3-0246-478a-be32-3d8968b05222	43200	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ00038tqdq0208QRIBFTTA530370454062560005802VN62190107NPS68690804CKPM6304E457	\N	\N	\N	\N	\N	40000	\N
1000467	1000004	1000016	\N	00000000	DRA	POS	\N	1000070	1000070	1000042	\N	Y	2024-09-17	2024-09-17 17:21:27.743042	1000069	2024-09-17 17:44:02.248831	1000069	Thanh NC	POS1000466	1000000	\N	\N	f28c6ce0-20f6-40b4-bc1e-91dc283a8136	243200	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	240000	\N
1000461	1000004	1000016	\N	00000000	DRA	POS	\N	1000070	1000070	1000042	\N	Y	2024-09-17	2024-09-17 17:20:22.517169	1000069	2024-09-17 18:36:10.907504	1000069	Thanh NC	POS1000460	1000000	\N	\N	7a6a5990-d805-4188-9d4c-51d394470b75	256000	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ00038tqqs0208QRIBFTTA530370454062432005802VN62190107NPS68690804CKPM6304C346	\N	\N	\N	\N	\N	240000	\N
1000369	1000004	1000016	\N	00000000	DRA	POS	\N	1000078	1000078	1000042	\N	Y	2024-09-10	2024-09-10 17:38:19.821419	1000069	2024-09-18 16:13:35.907819	1000069	Thanh NC	POS1000368	1000000	\N	\N	3c457438-c003-4c30-b595-dec7f229fa24	405000	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ000397o7i0208QRIBFTTA5303704540720790005802VN62190107NPS68690804CKPM6304D438	\N	\N	\N	\N	\N	389000	\N
1000443	1000004	1000016	\N	00000000	DRA	POS	\N	1000068	1000068	1000042	\N	Y	2024-09-17	2024-09-17 16:36:34.192571	1000069	2024-09-17 18:35:23.382529	1000069	Thanh NC	POS1000442	1000000	\N	\N	b5fe3f7a-a295-4c50-95c4-c95afc7d2be7	0	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	\N
1000487	1000004	1000016	\N	00000000	DRA	POS	\N	1000066	1000066	1000042	\N	Y	2024-09-17	2024-09-17 17:46:18.447887	1000069	2024-09-17 17:51:01.294891	1000069	Thanh NC	POS1000486	1000000	\N	1000487	52bfbc09-5ece-41d6-b594-bbfb306c27be	599600	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	598000	\N
1000477	1000004	1000016	\N	00000000	DRA	POS	\N	1000070	1000070	1000042	\N	Y	2024-09-17	2024-09-17 17:24:52.922872	1000069	2024-09-17 17:33:57.517715	1000069	Thanh NC	POS1000476	1000000	\N	1000477	1017dc3f-1877-40fe-9de3-9b82d3d3b502	221700	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	220100	\N
1000482	1000004	1000016	\N	00000000	DRA	POS	\N	1000070	1000070	1000042	\N	Y	2024-09-17	2024-09-17 17:34:51.437566	1000069	2024-09-17 17:35:30.943871	1000069	Thanh NC	POS1000480	1000000	\N	1000482	73b91e04-c1d8-4bac-bd06-87ea19a507a3	2299100	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	409100	\N
1000503	1000004	1000016	\N	00000000	DRA	POS	\N	1000077	1000077	1000042	\N	Y	2024-09-18	2024-09-18 11:17:02.956212	1000069	2024-09-19 15:25:18.457832	1000069	Thanh NC	POS1000502	1000000	\N	\N	e6bffb5c-9c75-40a6-b2e7-7365d68a2945	2200000	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	200000	\N
1000416	1000004	1000016	\N	00000000	DRA	POS	\N	1000077	1000077	1000042	\N	Y	2024-09-16	2024-09-16 15:02:16.207468	1000069	2024-09-18 11:17:06.297004	1000069	Thanh NC	POS1000414	1000000	\N	\N	b4eaf4c3-9ae9-4456-bd04-3098a9f09b5c	43200	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	40000	\N
1000417	1000004	1000016	\N	00000000	DRA	POS	\N	1000077	1000077	1000042	\N	Y	2024-09-16	2024-09-16 15:02:16.209716	1000069	2024-09-18 11:24:26.798847	1000069	Thanh NC	POS1000415	1000000	\N	\N	65d4106a-a2ba-4f71-8e76-488965d4c9eb	40000	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ000398gfu0208QRIBFTTA53037045405400005802VN62190107NPS68690804CKPM6304CC7E	\N	\N	\N	\N	\N	40000	\N
1000449	1000004	1000016	\N	00000000	DRA	POS	\N	1000077	1000077	1000042	\N	Y	2024-09-17	2024-09-17 16:41:04.810048	1000069	2024-09-18 04:26:16.171135	1000069	Thanh NC	POS1000448	1000000	\N	\N	d37f4ca3-9636-4197-9b55-9a73d6c666ef	2079000	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ000398ls00208QRIBFTTA5303704540720790005802VN62190107NPS68690804CKPM6304574E	\N	\N	\N	\N	\N	189000	\N
1000499	1000004	1000016	\N	00000000	DRA	POSS	\N	1000069	1000069	1000042	\N	Y	2024-09-17	2024-09-17 18:27:20.567274	1000069	2024-09-19 15:25:18.458833	1000069	Thanh NC	POS1000498	1000000	\N	1000499	6a20d7ea-057c-4c6f-a0f1-a1227a95484f	400000	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	400000	\N
1000489	1000004	1000016	\N	00000000	COM	POS	\N	1000064	1000064	1000042	\N	Y	2024-09-17	2024-09-17 17:52:02.883994	1000069	2024-09-18 07:30:04.271213	0	Thanh NC	POS1000488	1000000	\N	\N	508ce910-10b8-4006-9504-b1610f5edc5d	2221600	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ000397cki0208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM63048B2E	FT24262100200227	1000018	\N	\N	\N	220000	\N
1000495	1000004	1000016	\N	00000000	COM	POS	\N	1000065	1000065	1000042	\N	Y	2024-09-17	2024-09-17 17:54:41.384005	1000069	2024-09-19 15:25:18.458833	1000069	Thanh NC	POS1000494	1000000	\N	\N	40a91b59-c5dc-4276-8790-78927334b2a6	2221600	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ00039bfa60208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM63041B78	FT24262777565653	1000021	\N	\N	\N	220000	\N
1000493	1000004	1000016	\N	00000000	COM	POS	\N	1000065	1000065	1000042	\N	Y	2024-09-17	2024-09-17 17:54:41.284108	1000069	2024-09-19 15:25:18.459831	1000069	Thanh NC	POS1000492	1000000	\N	\N	d54aede6-efbb-47c2-b194-aa8568cae1b6	2221600	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ00039b9f20208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM63046ADD	FT24262343595062	1000020	\N	\N	\N	220000	\N
1000362	1000004	1000016	1000085	00000000	DRA	POS	N	1000078	1000078	1000042	3	Y	2024-09-09	2024-09-09 01:43:59.900457	1000069	2024-09-18 15:41:06.074317	1000069	Thanh NC	POS1000361	1000000	1000007	\N	45a24798-f7ff-4852-880c-b23e0c5794de	2200000	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ0003970pm0208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM6304F66E	\N	\N	\N	\N	\N	200000	\N
1000491	1000004	1000016	\N	00000000	COM	POS	\N	1000064	1000064	1000042	\N	Y	2024-09-17	2024-09-17 17:52:02.975351	1000069	2024-09-19 15:25:18.459831	1000069	Thanh NC	POS1000490	1000000	\N	\N	289f1ba5-d573-47e9-9cce-4cd88eeb4c7e	2221600	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ00039ar980208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM630406A6	FT24262606110623	1000019	\N	\N	\N	220000	\N
1000453	1000004	1000016	\N	00000000	DRA	POS	\N	1000078	1000078	1000042	\N	Y	2024-09-17	2024-09-17 17:05:33.019236	1000069	2024-09-18 08:29:25.106756	1000069	Thanh NC	POS1000452	1000000	\N	\N	69072174-6e85-477f-8ead-5f76d3ffb64b	378000	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ00039bhvc0208QRIBFTTA530370454063780005802VN62190107NPS68690804CKPM6304127C	\N	\N	\N	\N	\N	378000	\N
1000401	1000004	1000016	\N		DRA	POS	\N	1000074	999977	0	\N	Y	2024-09-13	2024-09-13 14:13:20.886976	1000069	2024-09-18 16:08:43.989901	1000069	Thanh NC	POS1000400	1000000	\N	\N	5c6389d7-28e2-4451-a792-13c023b4902e	4158000	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ0003994og0208QRIBFTTA5303704540741580005802VN62190107NPS68690804CKPM6304E4B2	\N	\N	\N	\N	\N	378000	\N
1000501	1000004	1000016	\N	00000000	DRA	POS	\N	1000076	1000076	1000042	\N	Y	2024-09-18	2024-09-18 10:39:04.363593	1000069	2024-09-19 15:25:18.457832	1000069	Thanh NC	POS1000500	1000000	\N	\N	ab66ea1b-bac4-4529-adf7-dd50fe587ae7	809100	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ0003983q40208QRIBFTTA5303704540745890005802VN62190107NPS68690804CKPM6304E087	\N	\N	\N	\N	\N	809100	\N
1000360	1000004	1000016	1000085	00000000	DRA	POS	N	1000078	1000078	1000042	3	Y	2024-09-09	2024-09-09 01:41:46.051101	1000069	2024-09-18 04:48:35.199288	1000069	Thanh NC	POS1000359	1000000	1000007	\N	abf3ad69-48c6-49d3-94bb-79d676867c57	0	\N	\N	\N	1000003	\N	\N	\N	\N		\N	\N	\N	\N	\N	0	\N
1000455	1000004	1000016	\N	00000000	DRA	POS	\N	1000078	1000078	1000042	\N	Y	2024-09-17	2024-09-17 17:05:33.123904	1000069	2024-09-18 04:50:20.933466	1000069	Thanh NC	POS1000454	1000000	\N	\N	89eeeeb7-e1b9-4519-a5a6-0c9585a0dbd6	2578000	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ0003992c60208QRIBFTTA5303704540725780005802VN62190107NPS68690804CKPM63046409	\N	\N	\N	\N	\N	578000	\N
1000370	1000004	1000016	\N	00000000	DRA	POS	\N	1000078	1000078	1000042	\N	Y	2024-09-10	2024-09-10 17:38:19.821418	1000069	2024-09-18 11:51:29.515565	1000069	Thanh NC	POS1000367	1000000	\N	\N	9c6c4a8a-e4c2-4a9d-8e45-2f99325018e0	0	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
1000411	1000004	1000016	\N	00000000	DRA	POS	\N	1000077	1000077	1000042	\N	Y	2024-09-16	2024-09-16 14:26:47.441665	1000069	2024-09-18 11:51:39.661083	1000069	Thanh NC	POS1000410	1000000	\N	\N	db123d28-20d1-4d04-8bb6-bb17149272a5	40000	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ000397sko0208QRIBFTTA53037045405432005802VN62190107NPS68690804CKPM63047695	\N	\N	\N	\N	\N	40000	\N
1000413	1000004	1000016	\N	00000000	DRA	POS	\N	1000077	1000077	1000042	\N	Y	2024-09-16	2024-09-16 14:26:47.53002	1000069	2024-09-18 04:52:50.501476	1000069	Thanh NC	POS1000412	1000000	\N	\N	f6a6d7d1-34f1-4697-a70a-3a6e2ef271c9	2139000	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ0003992450208QRIBFTTA5303704540721390005802VN62190107NPS68690804CKPM630485E2	\N	\N	\N	\N	\N	249000	\N
1000451	1000004	1000016	\N	00000000	DRA	POS	\N	1000077	1000077	1000042	\N	Y	2024-09-17	2024-09-17 16:41:05.160887	1000069	2024-09-18 11:54:51.951626	1000069	Thanh NC	POS1000450	1000000	\N	\N	4b124cda-f5ca-409a-aaa3-73fc604a3eeb	60000	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ000398kkq0208QRIBFTTA53037045405600005802VN62190107NPS68690804CKPM6304F2A9	\N	\N	\N	\N	\N	60000	\N
1000403	1000004	1000016	\N		DRA	POS	\N	1000077	1000012	0	\N	Y	2024-09-13	2024-09-13 14:14:56.335964	1000069	2024-09-18 11:55:14.334288	1000069	Thanh NC	POS1000402	1000000	\N	\N	ce6cecfb-b2b3-483c-a38e-f8454ad810a6	200000	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ000398g680208QRIBFTTA5303704540722000005802VN62190107NPS68690804CKPM6304F541	\N	\N	\N	\N	\N	200000	\N
1000399	1000004	1000016	\N		DRA	POS	\N	1000073	999972	0	\N	Y	2024-09-13	2024-09-13 14:12:47.150189	1000069	2024-09-18 04:58:38.716516	1000069	Thanh NC	POS1000398	1000000	\N	\N	3494741f-c966-4f11-92d0-51e9d42e6b7d	43200	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ0003996b80208QRIBFTTA53037045405432005802VN62190107NPS68690804CKPM6304F359	\N	\N	\N	\N	\N	40000	\N
1000387	1000004	1000016	\N		DRA	POS	\N	1000073	999972	0	\N	Y	2024-09-13	2024-09-13 11:15:42.259004	1000069	2024-09-18 05:04:52.473431	1000069	Thanh NC	POS1000386	1000000	\N	\N	22e7ee48-cd58-41ea-9ba7-88ea3ab2f2fd	2295000	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ00039981m0208QRIBFTTA5303704540722950005802VN62190107NPS68690804CKPM630473F1	\N	\N	\N	\N	\N	389000	\N
1000391	1000004	1000016	\N	\N	DRA	POS	\N	1000074	999977	0	\N	Y	2024-09-13	2024-09-13 13:35:35.310774	1000069	2024-09-18 13:32:08.087103	1000069	Lầu Sẹc Dần 	POS1000390	1000000	\N	\N	6f3d0055-cff2-4e13-99d4-91b3a6a53af7	\N	\N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
1000393	1000004	1000016	\N		DRA	POS	\N	1000073	999972	0	\N	Y	2024-09-13	2024-09-13 13:35:43.000556	1000069	2024-09-18 13:32:52.618775	1000069	Thanh NC	POS1000392	1000000	\N	\N	fb8076f9-ca5d-4312-8b13-b53aa85fd62b	43200	\N	\N	\N	1000003	\N	\N	\N	\N	00020101021238570010A000000727012700069704220113VQRQ00039a84c0208QRIBFTTA53037045405432005802VN62190107NPS68690804CKPM630406E0	\N	\N	\N	\N	\N	40000	\N
\.


--
-- TOC entry 5385 (class 0 OID 385754)
-- Dependencies: 244
-- Data for Name: d_pos_orderline; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_pos_orderline (d_pos_orderline_id, qty, d_product_id, d_tenant_id, d_org_id, created, created_by, updated, updated_by, d_pos_order_id, description, d_pos_orderline_uu, is_active, salesprice, d_production_id, d_tax_id, tax_amount, discount_percent, discount_amount, linenet_amt, grand_total) FROM stdin;
1000291	1	1000509	1000004	1000016	2024-09-09 01:43:59.929885	1000069	2024-09-18 16:13:05.750134	1000069	1000369		1d4bf0c0-f708-4371-9f7b-8544e519abe4	Y	0	\N	0	0	\N	\N	0	0
1000292	1	1000506	1000004	1000016	2024-09-09 01:44:00.163907	1000069	2024-09-18 16:13:05.750172	1000069	1000369		40ae5b45-dabe-42b3-b970-8837ca3368e1	Y	0	\N	0	0	\N	\N	0	0
1000417	1	1000488	1000004	1000016	2024-09-18 16:13:05.719087	1000069	2024-09-18 16:13:33.685485	1000069	1000369	Cua dong siu ngon	085e68bf-ce8f-4791-8384-a372263bcf35	Y	189000	\N	0	0	\N	\N	189000	189000
1000312	1	1000506	1000004	1000016	2024-09-16 15:39:02.072597	1000069	2024-09-16 15:40:46.592585	1000069	1000435		2c68e6b3-dd64-4af8-a51c-c4a41b2ef1e4	Y	20000	\N	1000003	0	\N	\N	20000	20000
1000303	1	1000503	1000004	1000016	2024-09-16 15:02:16.33268	1000069	2024-09-16 15:02:16.33268	1000069	1000416		6294e4bd-a75d-4f30-b458-1cd0e1c5d9e0	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000304	1	1000506	1000004	1000016	2024-09-16 15:02:16.335127	1000069	2024-09-16 15:02:16.335127	1000069	1000416		e68361dc-da6a-4ffd-90df-0de9d5ac8be5	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000306	1	1000503	1000004	1000016	2024-09-16 15:38:20.094441	1000069	2024-09-16 15:38:20.094442	1000069	1000419		0f49f819-6c82-43f2-9c32-025c3c0c012c	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000307	1	1000491	1000004	1000016	2024-09-16 15:38:20.106655	1000069	2024-09-16 15:38:20.106655	1000069	1000419		84960ee4-fc4c-462a-ac7b-408fd875802a	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000308	1	1000494	1000004	1000016	2024-09-16 15:38:20.11311	1000069	2024-09-16 15:38:20.113111	1000069	1000419		26896051-bf44-4a20-ad86-db7b27599fb5	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000309	1	1000503	1000004	1000016	2024-09-16 15:38:20.20136	1000069	2024-09-16 15:38:20.20136	1000069	1000421		8c0e03c6-3a55-4d8f-905d-526f7ebfa3f6	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000310	1	1000491	1000004	1000016	2024-09-16 15:38:20.216504	1000069	2024-09-16 15:38:20.216505	1000069	1000421		fd7d2158-51d9-4950-bc2b-13b26453bd3a	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000333	1	1000506	1000004	1000016	2024-09-17 17:21:33.254342	1000069	2024-09-17 17:21:33.254343	1000069	1000471		cbc8da96-9c3b-4055-a948-9a8b65ed7a6c	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000334	1	1000509	1000004	1000016	2024-09-17 17:21:33.258715	1000069	2024-09-17 17:21:33.258715	1000069	1000471		aa3fe488-d11a-412e-9b28-8abd5dda39c6	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000335	1	1000509	1000004	1000016	2024-09-17 17:23:24.521711	1000069	2024-09-17 17:23:24.521712	1000069	1000473		e0de9af8-a750-45a3-9d7b-93991f0bc61b	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000336	1	1000506	1000004	1000016	2024-09-17 17:23:24.527835	1000069	2024-09-17 17:23:24.527835	1000069	1000473		fb2a583c-66ab-4728-85b6-1c1d8e67c432	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000340	1	1000509	1000004	1000016	2024-09-17 17:33:57.498554	1000069	2024-09-17 17:33:57.498555	1000069	1000477		b73cd8f2-389f-4643-8c0f-b1b8434f92a7	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000342	1	1000608	1000004	1000016	2024-09-17 17:33:57.505152	1000069	2024-09-17 17:33:57.505152	1000069	1000477	exampleDescription	b29bc227-b5b3-4c98-819e-00d3eb82f40a	Y	100	\N	1000003	0	\N	\N	100	100
1000418	1	1000407	1000004	1000016	2024-09-18 16:13:33.682625	1000069	2024-09-18 16:13:33.682626	1000069	1000369	Banh mi Nha Trang	124be1a9-0d9e-48a1-b674-7e688ad5a05c	Y	200000	\N	1000004	16000	\N	\N	200000	216000
1000419	1	1000506	1000004	1000016	2024-09-18 16:20:44.224872	1000069	2024-09-18 16:20:44.224872	1000069	1000505		af8ce45d-5796-4577-a786-07338929ab21	Y	20000	\N	\N	1600	\N	\N	20000	21600
1000328	1	1000407	1000004	1000016	2024-09-17 17:20:47.456841	1000069	2024-09-17 17:35:14.099225	1000069	1000479	Banh mi Nha Trang	0e468528-c22c-4a7c-a7e7-35f447d90219	Y	200000	\N	1000003	0	\N	\N	200000	200000
1000325	2	1000506	1000004	1000016	2024-09-17 17:07:58.272588	1000069	2024-09-17 17:35:14.099225	1000069	1000479		fba95df4-a35d-45c9-9b35-c11d4707d582	Y	20000	\N	1000003	0	\N	\N	40000	40000
1000324	1	1000509	1000004	1000016	2024-09-17 17:07:58.260924	1000069	2024-09-17 17:35:14.099225	1000069	1000479		39477561-2dd9-4396-8254-1de695221d17	Y	20000	\N	1000003	0	\N	\N	20000	20000
1000341	1	1000509	1000004	1000016	2024-09-17 17:33:57.501925	1000069	2024-09-17 17:33:57.501925	1000069	1000477		c2008e3a-1f0d-4e6b-a35d-25d439d8ac10	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000343	1	1000608	1000004	1000016	2024-09-17 17:33:57.507031	1000069	2024-09-17 17:33:57.507032	1000069	1000477	exampleDescription	ea464900-0620-49f2-8dab-e71b880d45b3	Y	100	\N	1000003	0	\N	\N	100	100
1000420	1	1000488	1000004	1000016	2024-09-18 16:30:38.708453	1000069	2024-09-18 16:31:07.514918	1000069	1000507	Cua dong siu ngon	addd23f7-55a1-4230-a311-2ed2e3cd8eaf	Y	189000	\N	0	0	\N	\N	189000	189000
1000421	1	1000407	1000004	1000016	2024-09-18 16:30:38.724028	1000069	2024-09-18 16:31:07.514941	1000069	1000507	Banh mi Nha Trang	eff096a1-1911-462e-b9d5-a9eefc15f1be	Y	200000	\N	0	0	\N	\N	200000	200000
1000422	1	1000509	1000004	1000016	2024-09-18 16:31:07.513083	1000069	2024-09-18 16:31:20.622916	1000069	1000507		977088e0-269a-41b9-8e26-3607e6d1ebe5	Y	20000	\N	0	0	\N	\N	20000	20000
1000423	1	1000458	1000004	1000016	2024-09-18 16:31:20.620132	1000069	2024-09-18 17:04:12.318382	1000069	1000507	Banh cuon thom ngon	81e68bf5-bf98-4afd-aa0b-0e61ca7ab717	Y	200000	\N	0	0	\N	\N	200000	200000
1000339	1	1000440	1000004	1000016	2024-09-17 17:35:14.025851	1000069	2024-09-17 17:35:14.025851	1000069	1000479	Banh cuon thom ngon	00f2f37f-09ba-4983-b5e3-802f906d05ed	Y	200000	\N	1000003	2000000	\N	\N	200000	2200000
1000370	1	1000440	1000004	1000016	2024-09-17 17:54:41.287843	1000069	2024-09-17 17:54:41.287844	1000069	1000493	Banh cuon thom ngon	0cdde732-91fb-489a-b98c-e7554f15b559	Y	200000	\N	1000003	2000000	\N	\N	200000	2200000
1000338	1	1000440	1000004	1000016	2024-09-17 17:24:52.996364	1000069	2024-09-17 17:33:57.517788	1000069	1000477	Banh cuon thom ngon	47262800-cebb-4158-8f60-291e6cda9905	Y	200000	\N	1000003	0	\N	\N	200000	200000
1000345	1	1000608	1000004	1000016	2024-09-17 17:34:51.440782	1000069	2024-09-17 17:34:51.440782	1000069	1000482	exampleDescription	b032f71d-e965-43f5-8713-04da9d20e387	Y	100	\N	1000003	0	\N	\N	100	100
1000391	1	1000488	1000004	1000016	2024-09-18 10:39:04.377485	1000069	2024-09-18 10:50:28.349966	1000069	1000501	Cua dong siu ngon	84e23447-118e-4f5a-866f-de201ea63fee	Y	189000	\N	1000003	0	\N	\N	189000	189000
1000399	1	1000608	1000004	1000016	2024-09-18 10:52:16.385488	1000069	2024-09-18 10:52:16.3855	1000069	1000501	exampleDescription	92102caf-7868-46af-bcad-42aaa61a8b55	Y	100	\N	1000003	0	\N	\N	100	100
1000394	2	1000437	1000004	1000016	2024-09-18 10:50:28.336989	1000069	2024-09-18 10:52:16.401795	1000069	1000501	Banh cuon thom ngon	05b2f092-f2ef-4fcb-8178-5ba6c749cc9f	Y	200000	\N	1000003	0	\N	\N	400000	400000
1000398	1	1000506	1000004	1000016	2024-09-18 10:52:16.372593	1000069	2024-09-18 10:53:50.238149	1000069	1000501		967b5575-1f2f-4eca-82d0-b18b4f9283b9	Y	20000	\N	1000003	0	\N	\N	20000	20000
1000400	1	1000422	1000004	1000016	2024-09-18 10:52:16.387884	1000069	2024-09-18 10:53:50.238225	1000069	1000501	Banh cuon thom ngon	7fa84bc9-1e33-452a-ba76-6c259cc50741	Y	200000	\N	1000003	0	\N	\N	200000	200000
1000402	1	1000440	1000004	1000016	2024-09-18 11:17:02.971018	1000069	2024-09-18 11:17:02.971019	1000069	1000503	Banh cuon thom ngon	4cd3ac17-ed16-4115-aad3-bc93b694e1c1	Y	200000	\N	1000003	2000000	\N	\N	200000	2200000
1000302	1	1000503	1000004	1000016	2024-09-16 15:02:16.326887	1000069	2024-09-18 11:17:45.743701	1000069	1000417		56eff04c-fced-4a95-9ef6-7048e0e12cdc	Y	20000	\N	1000003	0	\N	\N	20000	20000
1000305	1	1000506	1000004	1000016	2024-09-16 15:02:16.338184	1000069	2024-09-18 11:17:45.743726	1000069	1000417		c0be44e4-c5ea-4d27-b31e-1b801ef76f12	Y	20000	\N	1000003	0	\N	\N	20000	20000
1000403	1	1000488	1000004	1000016	2024-09-18 11:26:15.663378	1000069	2024-09-18 11:26:15.663381	1000069	1000449	Cua dong siu ngon	fc84e9b7-9688-4df4-ad46-5700e7364bf8	Y	189000	\N	1000003	1890000	\N	\N	189000	2079000
1000404	1	1000440	1000004	1000016	2024-09-18 11:48:03.086844	1000069	2024-09-18 11:48:03.086845	1000069	1000362	Banh cuon thom ngon	49a50a9f-5a78-4bbb-afd8-a14e6b185726	Y	200000	\N	1000003	2000000	\N	\N	200000	2200000
1000290	1	1000506	1000004	1000016	2024-09-09 01:41:46.32884	1000069	2024-09-18 11:48:34.949115	1000069	1000360		348ee697-22e7-4491-9786-1af3323660d8	Y	0	\N	1000003	0	\N	\N	0	0
1000289	1	1000509	1000004	1000016	2024-09-09 01:41:46.315936	1000069	2024-09-18 11:48:34.949141	1000069	1000360		df885f98-e773-448b-83c4-2e141f06048e	Y	0	\N	1000003	0	\N	\N	0	0
1000405	1	1000425	1000004	1000016	2024-09-18 11:50:20.454538	1000069	2024-09-18 11:50:20.454539	1000069	1000455	Banh cuon thom ngon	f470f18b-ee7a-469d-bb77-ee7ce7bc6d34	Y	200000	\N	1000003	2000000	\N	\N	200000	2200000
1000388	1	1000506	1000004	1000016	2024-09-18 10:34:11.222248	1000069	2024-09-18 11:51:39.66125	1000069	1000411		3c5114e9-6270-4e8e-9957-da8f8f219613	Y	20000	\N	1000003	0	\N	\N	20000	20000
1000387	1	1000509	1000004	1000016	2024-09-18 10:34:11.193759	1000069	2024-09-18 11:51:39.661303	1000069	1000411		e6fd7048-be9d-40c4-8093-6d2dc3d1f7da	Y	20000	\N	1000003	0	\N	\N	20000	20000
1000406	1	1000488	1000004	1000016	2024-09-18 11:52:50.060268	1000069	2024-09-18 11:52:50.060268	1000069	1000413	Cua dong siu ngon	3c7edcf4-307d-4b5d-ad9c-533d674d3faf	Y	189000	\N	1000003	1890000	\N	\N	189000	2079000
1000300	1	1000494	1000004	1000016	2024-09-16 14:26:47.543603	1000069	2024-09-18 11:52:50.073035	1000069	1000413		17b76dc4-98ba-4b90-8af1-14121829f4ed	Y	20000	\N	1000003	0	\N	\N	20000	20000
1000299	1	1000506	1000004	1000016	2024-09-16 14:26:47.539209	1000069	2024-09-18 11:52:50.073085	1000069	1000413		cd66bb71-1c27-4a3d-8c08-20f148a4c06a	Y	20000	\N	1000003	0	\N	\N	20000	20000
1000301	1	1000491	1000004	1000016	2024-09-16 14:26:47.546203	1000069	2024-09-18 11:52:50.073117	1000069	1000413		a55b82fc-8745-43bb-8121-080580dcf4e9	Y	20000	\N	1000003	0	\N	\N	20000	20000
1000313	1	1000506	1000004	1000016	2024-09-16 15:39:02.156272	1000069	2024-09-16 15:39:02.156272	1000069	1000429		0feb5467-2ad0-436e-93ae-f4264a9bbf3f	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000311	2	1000494	1000004	1000016	2024-09-16 15:38:20.22091	1000069	2024-09-16 15:39:02.17105	1000069	1000429		bad1d78e-e3f5-4edf-a5b2-6efe2c558b0b	Y	20000	\N	1000003	0	\N	\N	40000	40000
1000295	2	1000494	1000004	1000016	2024-09-16 10:28:52.367384	1000069	2024-09-18 13:40:44.517198	1000069	1000409		dd00ab1b-576b-4360-b867-f8ec57920050	Y	20000	\N	1000003	0	\N	\N	40000	40000
1000329	1	1000506	1000004	1000016	2024-09-17 17:21:27.769507	1000069	2024-09-17 17:21:27.769507	1000069	1000467		6c7d931c-ce55-481d-bf7e-6411166d2fa6	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000297	1	1000494	1000004	1000016	2024-09-16 14:26:47.445923	1000069	2024-09-17 16:41:05.241418	1000069	1000451		ba344133-de32-47ad-ac65-744a2a72bb9d	Y	20000	\N	1000003	0	\N	\N	20000	20000
1000298	1	1000491	1000004	1000016	2024-09-16 14:26:47.447839	1000069	2024-09-17 16:41:05.241471	1000069	1000451		52ed4ca7-d0dc-425e-964a-a763217cbaaf	Y	20000	\N	1000003	0	\N	\N	20000	20000
1000296	1	1000506	1000004	1000016	2024-09-16 14:26:47.443951	1000069	2024-09-17 16:41:05.241482	1000069	1000451		0c1d3160-c5f9-4290-b402-44677af4ca62	Y	20000	\N	1000003	0	\N	\N	20000	20000
1000318	1	1000488	1000004	1000016	2024-09-17 17:05:33.030903	1000069	2024-09-17 17:05:33.030903	1000069	1000453	Cua dong siu ngon	8de6ae47-bf0d-4032-8558-0fb465fe106d	Y	189000	\N	1000003	0	\N	\N	189000	189000
1000319	1	1000485	1000004	1000016	2024-09-17 17:05:33.034238	1000069	2024-09-17 17:05:33.034238	1000069	1000453	Cua dong siu ngon	a6919a39-2f04-40a8-bcbe-c712ded3d83f	Y	189000	\N	1000003	0	\N	\N	189000	189000
1000320	1	1000488	1000004	1000016	2024-09-17 17:05:33.132642	1000069	2024-09-17 17:05:33.132643	1000069	1000455	Cua dong siu ngon	a4918c43-d655-429a-a693-0c24a32352ef	Y	189000	\N	1000003	0	\N	\N	189000	189000
1000321	1	1000485	1000004	1000016	2024-09-17 17:05:33.137232	1000069	2024-09-17 17:05:33.137233	1000069	1000455	Cua dong siu ngon	bb0a0cb4-0e71-433c-9ed7-9d69b51bebe3	Y	189000	\N	1000003	0	\N	\N	189000	189000
1000322	1	1000509	1000004	1000016	2024-09-17 17:07:58.143668	1000069	2024-09-17 17:07:58.143668	1000069	1000457		2599b661-5794-4f31-ab4e-292df8013524	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000323	1	1000506	1000004	1000016	2024-09-17 17:07:58.154415	1000069	2024-09-17 17:07:58.154421	1000069	1000457		6525a5bc-ad5f-49af-9b1f-4b532c063392	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000330	1	1000509	1000004	1000016	2024-09-17 17:21:27.781623	1000069	2024-09-17 17:21:27.781623	1000069	1000467		88e90a95-2e28-4851-8db5-90312dd8fd6a	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000337	1	1000440	1000004	1000016	2024-09-17 17:24:52.896598	1000069	2024-09-17 17:43:09.975401	1000069	1000475	Banh cuon thom ngon	121013f9-18f4-422a-89b3-253d0ca87e4c	Y	200000	\N	1000003	0	\N	\N	200000	200000
1000326	1	1000407	1000004	1000016	2024-09-17 17:20:22.562375	1000069	2024-09-17 17:21:33.315013	1000069	1000471	Banh mi Nha Trang	934d706e-31c6-4824-8d8d-ee28676cec65	Y	200000	\N	1000003	0	\N	\N	200000	200000
1000331	1	1000506	1000004	1000016	2024-09-17 17:21:33.226582	1000069	2024-09-17 17:21:33.226583	1000069	1000469		46568fd1-4753-4dc8-8d16-2a7ba5aac2ad	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000332	1	1000509	1000004	1000016	2024-09-17 17:21:33.233356	1000069	2024-09-17 17:21:33.23337	1000069	1000469		59c1858e-0f94-4921-9061-7ac9681caab9	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000327	1	1000407	1000004	1000016	2024-09-17 17:20:47.394406	1000069	2024-09-17 17:23:24.546641	1000069	1000473	Banh mi Nha Trang	be41aa44-bc32-428a-9b82-4533cdd21c42	Y	200000	\N	1000003	0	\N	\N	200000	200000
1000348	1	1000608	1000004	1000016	2024-09-17 17:34:51.476067	1000069	2024-09-17 17:34:51.476067	1000069	1000483	exampleDescription	1a8af4bb-2737-4088-8e0f-f7f0a8b8d951	Y	100	\N	1000003	0	\N	\N	100	100
1000350	1	1000485	1000004	1000016	2024-09-17 17:35:30.825155	1000069	2024-09-17 17:35:30.825156	1000069	1000482	Cua dong siu ngon	6492de27-925b-4bc1-987c-d00a5f125c11	Y	189000	\N	1000003	1890000	\N	\N	189000	2079000
1000346	1	1000422	1000004	1000016	2024-09-17 17:34:51.441716	1000069	2024-09-17 17:35:30.827713	1000069	1000482	Banh cuon thom ngon	fb1dbb6c-ae29-42f9-ad0f-7753616ae0b3	Y	200000	\N	1000003	0	\N	\N	200000	200000
1000344	1	1000509	1000004	1000016	2024-09-17 17:34:51.439266	1000069	2024-09-17 17:35:30.827723	1000069	1000482		93efbdce-b6aa-46eb-85f7-5cbd57c26a65	Y	20000	\N	1000003	0	\N	\N	20000	20000
1000351	1	1000485	1000004	1000016	2024-09-17 17:35:30.930911	1000069	2024-09-17 17:35:30.930912	1000069	1000482	Cua dong siu ngon	113e3a8f-4d2b-4d64-8258-8e7a4ec3d8c2	Y	189000	\N	1000003	1890000	\N	\N	189000	2079000
1000352	1	1000485	1000004	1000016	2024-09-17 17:35:54.806972	1000069	2024-09-17 17:35:54.806972	1000069	1000483	Cua dong siu ngon	6aaf40d7-97b0-4c11-9ceb-e8a27f02ddb7	Y	189000	\N	1000003	1890000	\N	\N	189000	2079000
1000349	1	1000422	1000004	1000016	2024-09-17 17:34:51.478148	1000069	2024-09-17 17:35:54.810056	1000069	1000483	Banh cuon thom ngon	5e6071c7-8126-4782-8c98-a1bb07562d63	Y	200000	\N	1000003	0	\N	\N	200000	200000
1000347	1	1000509	1000004	1000016	2024-09-17 17:34:51.467104	1000069	2024-09-17 17:35:54.810083	1000069	1000483		787c82b9-37da-4027-80aa-d0985e36e7f3	Y	20000	\N	1000003	0	\N	\N	20000	20000
1000354	2	1000509	1000004	1000016	2024-09-17 17:43:09.913134	1000069	2024-09-17 17:43:09.913134	1000069	1000475		6f3294ae-ccba-41e8-8c2a-12f819bb4883	Y	20000	\N	1000003	3200	\N	\N	40000	43200
1000355	1	1000488	1000004	1000016	2024-09-17 17:43:09.924374	1000069	2024-09-17 17:43:09.924374	1000069	1000475	Cua dong siu ngon	5fe47865-5722-4ba8-9e78-2f239e7f5b45	Y	189000	\N	1000003	1890000	\N	\N	189000	2079000
1000356	2	1000509	1000004	1000016	2024-09-17 17:43:09.96272	1000069	2024-09-17 17:43:09.962721	1000069	1000475		59c687b5-0ac3-4cb3-a8ba-77c445a21712	Y	20000	\N	1000003	3200	\N	\N	40000	43200
1000357	1	1000488	1000004	1000016	2024-09-17 17:43:09.96942	1000069	2024-09-17 17:43:09.96942	1000069	1000475	Cua dong siu ngon	6803615d-d82f-4fb5-bbed-c90d9d7edd9d	Y	189000	\N	1000003	1890000	\N	\N	189000	2079000
1000362	1	1000458	1000004	1000016	2024-09-17 17:48:25.523739	1000069	2024-09-17 17:48:25.52374	1000069	1000485	Banh cuon thom ngon	6b48eae3-8d75-4b35-b09c-15bade928480	Y	200000	\N	1000003	2000000	\N	\N	200000	2200000
1000371	1	1000509	1000004	1000016	2024-09-17 17:54:41.294174	1000069	2024-09-17 17:54:41.294174	1000069	1000493		43dbb374-fafb-417d-a0c7-4cbbd483ec39	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000363	1	1000458	1000004	1000016	2024-09-17 17:48:25.510382	1000069	2024-09-17 17:48:25.510382	1000069	1000485	Banh cuon thom ngon	f44bd6bc-c97b-43ba-b847-068517db3168	Y	200000	\N	1000003	2000000	\N	\N	200000	2200000
1000372	1	1000440	1000004	1000016	2024-09-17 17:54:41.390309	1000069	2024-09-17 17:54:41.390309	1000069	1000495	Banh cuon thom ngon	5c88d6f5-c80f-4852-8d98-11f65e452fe5	Y	200000	\N	1000003	2000000	\N	\N	200000	2200000
1000359	1	1000407	1000004	1000016	2024-09-17 17:46:18.361316	1000069	2024-09-17 17:48:25.556696	1000069	1000485	Banh mi Nha Trang	76bf422e-8d37-4d0b-aafb-ff05685b57c4	Y	200000	\N	1000003	0	\N	\N	200000	200000
1000358	2	1000488	1000004	1000016	2024-09-17 17:46:18.359691	1000069	2024-09-17 17:48:25.556717	1000069	1000485	Cua dong siu ngon	3810a6e5-525b-406d-9528-dba49f935680	Y	189000	\N	1000003	0	\N	\N	378000	378000
1000364	1	1000509	1000004	1000016	2024-09-17 17:51:01.250474	1000069	2024-09-17 17:51:01.250474	1000069	1000487		5b2ebd29-0047-4eaf-99ab-084fb89036a7	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000373	1	1000509	1000004	1000016	2024-09-17 17:54:41.401491	1000069	2024-09-17 17:54:41.401491	1000069	1000495		af2c9e00-ef99-47c7-9e91-73caa1e772e1	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000365	1	1000509	1000004	1000016	2024-09-17 17:51:01.289271	1000069	2024-09-17 17:51:01.289271	1000069	1000487		6372095a-8021-49ec-aefe-22ada3c514d1	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000361	1	1000407	1000004	1000016	2024-09-17 17:46:18.468464	1000069	2024-09-17 17:51:01.294951	1000069	1000487	Banh mi Nha Trang	b5fef27c-8909-4360-b21c-fea42ee8cf89	Y	200000	\N	1000003	0	\N	\N	200000	200000
1000360	2	1000488	1000004	1000016	2024-09-17 17:46:18.456702	1000069	2024-09-17 17:51:01.294966	1000069	1000487	Cua dong siu ngon	69cf77d8-a66e-43d5-8f09-42569396bc99	Y	189000	\N	1000003	0	\N	\N	378000	378000
1000366	1	1000425	1000004	1000016	2024-09-17 17:52:02.894671	1000069	2024-09-17 17:52:02.894671	1000069	1000489	Banh cuon thom ngon	be618315-ef1d-491e-b853-c37e77236e79	Y	200000	\N	1000003	2000000	\N	\N	200000	2200000
1000367	1	1000509	1000004	1000016	2024-09-17 17:52:02.903381	1000069	2024-09-17 17:52:02.903381	1000069	1000489		9b94f9e0-2582-4605-8144-ba7ffec0e46f	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000368	1	1000425	1000004	1000016	2024-09-17 17:52:02.986476	1000069	2024-09-17 17:52:02.986477	1000069	1000491	Banh cuon thom ngon	5cccfaf9-2fe3-470b-8169-0129edd1c796	Y	200000	\N	1000003	2000000	\N	\N	200000	2200000
1000369	1	1000509	1000004	1000016	2024-09-17 17:52:02.993651	1000069	2024-09-17 17:52:02.993651	1000069	1000491		91e4fa82-2e93-48cb-8cc0-e29c7c19d8a5	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000376	1	1000458	1000004	1000016	2024-09-17 18:13:00.80126	1000069	2024-09-17 18:13:00.80126	1000069	1000497	Banh cuon thom ngon	f57dadc6-976f-4551-9519-ae575bc5212c	Y	200000	\N	1000003	2000000	\N	\N	200000	2200000
1000374	1	1000488	1000004	1000016	2024-09-17 18:12:33.421577	1000069	2024-09-17 18:13:00.810071	1000069	1000497	Cua dong siu ngon	df1029ac-935a-4c73-affd-f38a6fb5afc0	Y	189000	\N	1000003	0	\N	\N	189000	189000
1000375	1	1000407	1000004	1000016	2024-09-17 18:12:33.433613	1000069	2024-09-17 18:13:00.810106	1000069	1000497	Banh mi Nha Trang	0ae0a127-58d1-4e83-a7c9-ff1daa02231e	Y	200000	\N	1000003	0	\N	\N	200000	200000
1000379	1	1000485	1000004	1000016	2024-09-17 18:27:20.580677	1000069	2024-09-17 18:27:52.27291	1000069	1000499	Cua dong siu ngon	163f6e43-c344-4085-af41-7f317b2ff638	Y	189000	\N	1000003	0	\N	\N	189000	189000
1000380	1	1000428	1000004	1000016	2024-09-17 18:27:52.105006	1000069	2024-09-17 18:37:52.090366	1000069	1000499	Banh cuon thom ngon	865315c8-dd7f-4518-8deb-0266ec6769d3	Y	200000	\N	1000003	0	\N	\N	200000	200000
1000383	1	1000608	1000004	1000016	2024-09-17 18:38:58.330415	1000069	2024-09-17 18:37:52.090402	1000069	1000499	Banh cuon thom ngon	b5acafd8-c562-4877-81f2-4fc4a2ebf92c	Y	200000	\N	1000003	0	\N	\N	200000	200000
1000401	1	1000440	1000004	1000016	2024-09-18 11:16:45.251393	1000069	2024-09-18 11:55:14.334469	1000069	1000403	Banh cuon thom ngon	b1b63663-6bc8-4ebe-88d7-ce8f4784cb5d	Y	200000	\N	1000003	0	\N	\N	200000	200000
1000407	2	1000488	1000004	1000016	2024-09-18 11:56:09.492198	1000069	2024-09-18 11:56:09.492199	1000069	1000401	Cua dong siu ngon	58b5709d-d828-45de-9b01-a62799a730f1	Y	189000	\N	1000003	3780000	\N	\N	378000	4158000
1000408	2	1000488	1000004	1000016	2024-09-18 11:57:47.590857	1000069	2024-09-18 11:57:47.590857	1000069	1000401	Cua dong siu ngon	9b86c1a2-1099-4001-89f3-b774259ded1f	Y	189000	\N	1000003	3780000	\N	\N	378000	4158000
1000409	1	1000509	1000004	1000016	2024-09-18 11:58:37.984603	1000069	2024-09-18 11:58:37.984604	1000069	1000399		2d0d03a6-2647-4ca4-8ba3-88f69beb769a	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000410	1	1000506	1000004	1000016	2024-09-18 11:58:37.995951	1000069	2024-09-18 11:58:37.995951	1000069	1000399		2c04d8ec-a9b5-410c-85ef-e8a171037de2	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000411	1	1000488	1000004	1000016	2024-09-18 12:04:51.833456	1000069	2024-09-18 12:04:51.833457	1000069	1000387	Cua dong siu ngon	68e30ed7-cee9-48e1-8edc-74ad105645ea	Y	189000	\N	1000003	1890000	\N	\N	189000	2079000
1000412	1	1000407	1000004	1000016	2024-09-18 12:04:51.840651	1000069	2024-09-18 12:04:51.840652	1000069	1000387	Banh mi Nha Trang	cfbb9335-1293-42f7-a5f2-add2c3eb32bd	Y	200000	\N	1000003	16000	\N	\N	200000	216000
1000413	1	1000509	1000004	1000016	2024-09-18 13:32:22.079428	1000069	2024-09-18 13:32:22.079429	1000069	1000393		52e49e08-51c6-4b02-b48b-783da62d155c	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000414	1	1000506	1000004	1000016	2024-09-18 13:32:22.082599	1000069	2024-09-18 13:32:22.082599	1000069	1000393		e65fe5b7-f910-4ba5-8591-2ffb84fb0fa1	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000415	1	1000509	1000004	1000016	2024-09-18 13:32:52.593404	1000069	2024-09-18 13:32:52.593404	1000069	1000393		ac3a84f7-c61d-4536-beac-e1788e80387c	Y	20000	\N	1000003	1600	\N	\N	20000	21600
1000416	1	1000506	1000004	1000016	2024-09-18 13:32:52.605625	1000069	2024-09-18 13:32:52.605625	1000069	1000393		d35fff87-e73f-4b44-b186-549e138c0a9f	Y	20000	\N	1000003	1600	\N	\N	20000	21600
\.


--
-- TOC entry 5486 (class 0 OID 393071)
-- Dependencies: 345
-- Data for Name: d_pos_payment; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_pos_payment (d_pos_payment_id, d_tenant_id, d_org_id, d_pos_order_id, payment_method, voucher_code, total_amount, transaction_id, tip_amount, note, created, created_by, updated, updated_by, d_pos_payment_uu, is_active, is_processed) FROM stdin;
1000004	1000004	1000016	1000093	CAS	TEST	25000	\N	\N	\N	2024-09-05 17:34:58.573816	1000069	2024-09-05 17:34:58.573816	1000069	e88b406c-8e03-4650-b375-f7b66409e324	Y	Y
1000005	1000004	1000016	1000093	BAN	TEST	35000	\N	\N	\N	2024-09-05 17:34:58.57993	1000069	2024-09-05 17:34:58.57993	1000069	cfd7012f-5bb6-4a08-bfe2-c851f39cb33f	Y	Y
1000006	1000004	1000016	1000090	CAS	TEST	25000	\N	\N	\N	2024-09-05 11:07:42.907062	1000069	2024-09-05 11:07:42.90713	1000069	386ee8ff-9ef4-4d8e-83a1-2d4be8f84bb2	Y	Y
1000007	1000004	1000016	1000090	BAN	TEST	35000	\N	\N	\N	2024-09-05 11:07:43.042743	1000069	2024-09-05 11:07:43.042744	1000069	db11efb4-dae8-472c-bd67-45aaa9e37fb5	Y	Y
1000008	1000004	1000016	1000088	CAS	TEST	25000	\N	\N	\N	2024-09-05 11:07:54.199346	1000069	2024-09-05 11:07:54.199355	1000069	9f99a381-1505-48af-bc10-070c5fa127fd	Y	Y
1000009	1000004	1000016	1000088	BAN	TEST	35000	\N	\N	\N	2024-09-05 11:07:54.209565	1000069	2024-09-05 11:07:54.209568	1000069	84f407e8-180d-458d-b6ce-3b97f243977f	Y	Y
1000010	1000004	1000016	1000086	CAS	TEST	25000	\N	\N	\N	2024-09-05 11:08:03.892404	1000069	2024-09-05 11:08:03.892406	1000069	259d1cf2-21cc-4ef0-8bf5-ae7f44bba54c	Y	Y
1000011	1000004	1000016	1000086	BAN	TEST	35000	\N	\N	\N	2024-09-05 11:08:03.90016	1000069	2024-09-05 11:08:03.900164	1000069	99e1012c-439a-488e-87dc-7c8cc94bdda1	Y	Y
1000012	1000004	1000016	1000050	CAS	TEST	25000	\N	\N	\N	2024-09-05 11:14:19.131173	1000069	2024-09-05 11:14:19.131175	1000069	3efe8600-7638-4a95-888a-c410c1ea666a	Y	Y
1000013	1000004	1000016	1000050	BAN	TEST	35000	\N	\N	\N	2024-09-05 11:14:19.141005	1000069	2024-09-05 11:14:19.141007	1000069	09692f13-3fda-4488-9f8b-0584601aba00	Y	Y
1000014	1000004	1000016	1	CAS	TEST	25000	\N	\N	\N	2024-09-05 12:03:12.772161	1000069	2024-09-05 12:03:12.772165	1000069	452a782d-ce45-4b11-ac61-b4bece989001	Y	Y
1000015	1000004	1000016	1	BAN	TEST	35000	\N	\N	\N	2024-09-05 12:03:12.786807	1000069	2024-09-05 12:03:12.786809	1000069	8a47e406-ed8e-44f9-b356-c623956ed464	Y	Y
1000016	1000004	1000016	2	CAS	TEST	25000	\N	\N	\N	2024-09-05 12:03:17.279923	1000069	2024-09-05 12:03:17.279924	1000069	932096ea-7b51-4a2d-9965-e1e5e74f0386	Y	Y
1000017	1000004	1000016	2	BAN	TEST	35000	\N	\N	\N	2024-09-05 12:03:17.289639	1000069	2024-09-05 12:03:17.289641	1000069	ba4bf2bf-98b1-482a-bf68-f8993abb887b	Y	Y
1000018	1000004	1000016	3	CAS	TEST	25000	\N	\N	\N	2024-09-05 12:03:21.387964	1000069	2024-09-05 12:03:21.387967	1000069	9241c36e-523a-4c22-b8e3-5946a78a3605	Y	Y
1000019	1000004	1000016	3	BAN	TEST	35000	\N	\N	\N	2024-09-05 12:03:21.400948	1000069	2024-09-05 12:03:21.40095	1000069	34a2ad89-b4ba-4087-8042-19d8bbd1ae10	Y	Y
1000020	1000004	1000016	4	CAS	TEST	25000	\N	\N	\N	2024-09-05 12:03:32.972602	1000069	2024-09-05 12:03:32.972604	1000069	d033eb4d-ee5a-4da5-b2eb-6960b67b179b	Y	Y
1000021	1000004	1000016	4	BAN	TEST	35000	\N	\N	\N	2024-09-05 12:03:32.985208	1000069	2024-09-05 12:03:32.98521	1000069	bfe1410f-32f5-4e1c-944d-ba8307495b53	Y	Y
1000022	1000004	1000016	5	CAS	TEST	25000	\N	\N	\N	2024-09-05 12:03:36.318992	1000069	2024-09-05 12:03:36.318993	1000069	3fe86d0a-3fb0-4e4a-8b33-c0515d5cf68c	Y	Y
1000023	1000004	1000016	5	BAN	TEST	35000	\N	\N	\N	2024-09-05 12:03:36.329497	1000069	2024-09-05 12:03:36.329498	1000069	cf9be312-cdb3-4a59-b0af-fed075b99a5e	Y	Y
1000024	1000004	1000016	6	CAS	TEST	25000	\N	\N	\N	2024-09-06 01:44:45.933198	1000069	2024-09-06 01:44:45.933202	1000069	d69a8f64-1c3f-402e-982c-6a16889e9a28	Y	Y
1000025	1000004	1000016	6	BAN	TEST	35000	\N	\N	\N	2024-09-06 01:44:45.944166	1000069	2024-09-06 01:44:45.944167	1000069	b84e7e1b-0138-4bcb-8b46-404d7d037aff	Y	Y
1000026	1000004	1000016	7	CAS	TEST	25000	\N	\N	\N	2024-09-06 02:46:48.444436	1000069	2024-09-06 02:46:48.444438	1000069	61cae924-2bfd-4844-ac37-952d10473d98	Y	Y
1000027	1000004	1000016	7	BAN	TEST	35000	\N	\N	\N	2024-09-06 02:46:48.453625	1000069	2024-09-06 02:46:48.453626	1000069	13cfc406-46d2-4870-a8e2-cbb21033e6f9	Y	Y
1000028	1000004	1000016	8	CAS	TEST	25000	\N	\N	\N	2024-09-06 02:53:15.363879	1000069	2024-09-06 02:53:15.363882	1000069	82d9816d-1097-42fd-a1f1-5e5a84035ef0	Y	Y
1000029	1000004	1000016	8	BAN	TEST	35000	\N	\N	\N	2024-09-06 02:53:15.371986	1000069	2024-09-06 02:53:15.371988	1000069	55072ae7-1115-4be5-8ce8-bd7b08402b80	Y	Y
1000030	1000004	1000016	1000042	CAS	TEST	25000	\N	\N	\N	2024-09-06 03:04:09.784775	1000069	2024-09-06 03:04:09.784777	1000069	c0e6e24c-c69d-4613-9824-af7d2ceb610a	Y	Y
1000031	1000004	1000016	1000042	BAN	TEST	35000	\N	\N	\N	2024-09-06 03:04:09.79382	1000069	2024-09-06 03:04:09.793821	1000069	54f4a7be-52eb-40ab-85dc-111634d82716	Y	Y
1000032	1000004	1000016	1000056	CAS	TEST	25000	\N	\N	\N	2024-09-06 10:20:08.467482	1000069	2024-09-06 10:20:08.467482	1000069	0b12d074-94b7-4b89-a8d4-733f5705e7d8	Y	Y
1000033	1000004	1000016	1000056	BAN	TEST	35000	\N	\N	\N	2024-09-06 10:20:08.49458	1000069	2024-09-06 10:20:08.49458	1000069	9cd7f0fa-9872-4b8c-a211-5619442216b3	Y	Y
1000034	1000004	1000016	1000062	CAS	TEST	25000	\N	\N	\N	2024-09-06 10:21:33.141354	1000069	2024-09-06 10:21:33.141354	1000069	8c09b9a5-e9b8-416d-ad40-dea4ff12da8d	Y	Y
1000035	1000004	1000016	1000062	BAN	TEST	35000	\N	\N	\N	2024-09-06 10:21:33.159348	1000069	2024-09-06 10:21:33.159348	1000069	72866c46-cba7-4911-8668-e3e5a3e5e133	Y	Y
1000036	1000004	1000016	1000294	CAS	TEST	25000	\N	\N	\N	2024-09-08 08:16:41.841858	1000069	2024-09-08 08:16:41.84186	1000069	b8675288-9216-42c3-b485-2dfef39404bd	Y	Y
1000037	1000004	1000016	1000294	BAN	TEST	35000	\N	\N	\N	2024-09-08 08:16:41.852872	1000069	2024-09-08 08:16:41.852874	1000069	780dc50a-679f-48fb-a83e-974f7a0ffb98	Y	Y
1000038	1000004	1000016	1000194	CAS	TEST	25000	\N	\N	\N	2024-09-08 17:32:59.447588	1000069	2024-09-08 17:32:59.447588	1000069	ce90e3a2-c210-4984-bc2f-da019f05dd53	Y	Y
1000039	1000004	1000016	1000194	CAS	TEST	35000	\N	\N	\N	2024-09-08 17:32:59.495149	1000069	2024-09-08 17:32:59.495149	1000069	48543253-5cf5-4534-aa0f-b45b487beb5e	Y	Y
1000040	1000004	1000016	1000194	CAS	TEST	25000	\N	\N	\N	2024-09-08 17:45:09.285951	1000069	2024-09-08 17:45:09.285951	1000069	b3e7b429-1dcb-4f01-a5c2-f125d1e26889	Y	Y
1000041	1000004	1000016	1000194	CAS	TEST	35000	\N	\N	\N	2024-09-08 17:45:09.317412	1000069	2024-09-08 17:45:09.317412	1000069	3eabb4e1-d4e0-4d95-8fc7-100a0dc84f05	Y	Y
1000042	1000004	1000016	1000194	CAS	TEST	25000	\N	\N	\N	2024-09-08 17:52:41.881161	1000069	2024-09-08 17:52:41.881161	1000069	5dc37c55-ff8f-423d-b73f-237c6b9107c1	Y	Y
1000043	1000004	1000016	1000194	CAS	TEST	35000	\N	\N	\N	2024-09-08 17:52:41.91396	1000069	2024-09-08 17:52:41.91396	1000069	23cf4587-c0ec-43a1-8890-bedffee64624	Y	Y
1000044	1000004	1000016	1000196	CAS	TEST	25000	\N	\N	\N	2024-09-08 11:16:59.033039	1000069	2024-09-08 11:16:59.033046	1000069	69337380-50bc-49c4-8f5f-2a3727e76150	Y	Y
1000045	1000004	1000016	1000196	BAN	TEST	35000	\N	\N	\N	2024-09-08 11:16:59.176895	1000069	2024-09-08 11:16:59.176905	1000069	9aca92e6-de45-42df-a514-ddf67f8dcdab	Y	Y
1000046	1000004	1000016	1000198	CAS	TEST	25000	\N	\N	\N	2024-09-08 11:18:27.580091	1000069	2024-09-08 11:18:27.580093	1000069	3c8a34d0-04df-480a-9ace-54ab4f7a1d05	Y	Y
1000047	1000004	1000016	1000198	BAN	TEST	35000	\N	\N	\N	2024-09-08 11:18:27.588549	1000069	2024-09-08 11:18:27.588551	1000069	00ca89c6-e454-46f5-ac3c-6fc63562e723	Y	Y
1000048	1000004	1000016	1000204	CAS	TEST	25000	\N	\N	\N	2024-09-09 00:24:04.126706	1000069	2024-09-09 00:24:04.126706	1000069	0dd02850-628b-4c53-9ba4-c07aa054a3ef	Y	Y
1000049	1000004	1000016	1000204	BAN	TEST	35000	\N	\N	\N	2024-09-09 00:24:04.188527	1000069	2024-09-09 00:24:04.188527	1000069	59a8bd1b-774d-46e2-a2b5-a4b5ce206411	Y	Y
1000050	1000004	1000016	1000192	CAS	TEST	25000	\N	\N	\N	2024-09-09 01:54:13.692907	1000069	2024-09-09 01:54:13.692907	1000069	e296b1ce-dbf3-4639-bd6e-497eb7356aad	Y	Y
1000051	1000004	1000016	1000192	BAN	TEST	35000	\N	\N	\N	2024-09-09 01:54:13.723298	1000069	2024-09-09 01:54:13.723298	1000069	6301187c-19a3-4c4f-a048-fe8fc10ab2ff	Y	Y
1000052	1000004	1000016	1000360	QRC	\N	2000	\N	\N	\N	2024-09-17 08:52:58.579821	1000069	2024-09-17 08:52:58.579822	1000069	74cb0783-eae8-4ba6-b55c-59566ff87268	Y	Y
1000053	1000004	1000016	1000421	QRC		2000	\N	\N	\N	2024-09-17 09:49:17.929341	1000069	2024-09-17 09:49:17.929342	1000069	a9178b59-9486-498c-bb2c-a7b149d58a51	Y	Y
1000054	1000004	1000016	1000405	QRC		2000	\N	\N	\N	2024-09-17 10:00:42.049103	1000069	2024-09-17 10:00:42.049104	1000069	24cca9c9-c7bc-4a78-8f84-1ace251dc2ff	Y	Y
1000055	1000004	1000016	1000457	QRC		40000	\N	\N	\N	2024-09-17 10:08:40.608816	1000069	2024-09-17 10:08:40.608816	1000069	1ecbe335-3327-4525-95c6-c37d096deb4f	Y	Y
1000056	1000004	1000016	1000459	QRC		256000	\N	\N	\N	2024-09-17 10:20:23.243559	1000069	2024-09-17 10:20:23.24356	1000069	22a3b2cc-0fa0-4a51-8080-e7ecba3704e6	Y	Y
1000057	1000004	1000016	1000461	QRC		243200	\N	\N	\N	2024-09-17 10:21:28.278758	1000069	2024-09-17 10:21:28.278759	1000069	f211ee0d-44f1-4360-a4ef-73e487aba25b	Y	Y
1000058	1000004	1000016	1000463	QRC		243200	\N	\N	\N	2024-09-17 10:23:25.168588	1000069	2024-09-17 10:23:25.168588	1000069	938dc9c7-a593-4a09-9b3f-a3ce7b7dfacb	Y	Y
1000059	1000004	1000016	1000457	QRC		0	\N	\N	\N	2024-09-17 10:34:27.299538	1000069	2024-09-17 10:34:27.299538	1000069	b579c4b6-a3fa-446d-9538-a313946ea961	Y	Y
1000060	1000004	1000016	1000362	QRC		2000	\N	\N	\N	2024-09-18 02:32:52.050011	1000069	2024-09-18 02:32:52.050012	1000069	c3e73269-7ee1-447f-a83d-017530f5e067	Y	Y
1000061	1000004	1000016	1000364	QRC		2000	\N	\N	\N	2024-09-18 02:36:37.872638	1000069	2024-09-18 02:36:37.872639	1000069	e311a314-18e7-4a2c-87b8-9cb67f331534	Y	Y
1000062	1000004	1000016	1000489	QRC		2000	\N	\N	\N	2024-09-18 03:00:41.358537	1000069	2024-09-18 03:00:41.358537	1000069	16994e7b-b79f-401a-a3f6-bbea75af4a67	Y	Y
1000063	1000004	1000016	1000369	QRC		2079000	\N	\N	\N	2024-09-18 03:27:59.094645	1000069	2024-09-18 03:27:59.094646	1000069	d57015d6-78db-4431-ab18-ee43980f05c1	Y	Y
1000064	1000004	1000016	1000411	QRC		43200	\N	\N	\N	2024-09-18 03:34:11.722892	1000069	2024-09-18 03:34:11.722892	1000069	321b38d7-c4aa-413e-b648-c6fbc854c019	Y	Y
1000065	1000004	1000016	1000501	QRC		4589000	\N	\N	\N	2024-09-18 03:50:19.812159	1000069	2024-09-18 03:50:19.812159	1000069	3328e420-cfec-413e-af7c-a2a764df4966	Y	Y
1000066	1000004	1000016	1000403	QRC		2200000	\N	\N	\N	2024-09-18 04:16:45.793544	1000069	2024-09-18 04:16:45.793545	1000069	8cf3fa1f-365f-45b6-bc29-bc3f06af0aa5	Y	Y
1000067	1000004	1000016	1000417	QRC		40000	\N	\N	\N	2024-09-18 04:17:46.26786	1000069	2024-09-18 04:17:46.26786	1000069	605946ab-7434-4ed9-9941-53b8df96ed5c	Y	Y
1000068	1000004	1000016	1000451	QRC		60000	\N	\N	\N	2024-09-18 04:25:05.113337	1000069	2024-09-18 04:25:05.113337	1000069	832e4f14-2e43-4eb5-839e-03f67763f8aa	Y	Y
1000069	1000004	1000016	1000449	QRC		2079000	\N	\N	\N	2024-09-18 04:26:16.194685	1000069	2024-09-18 04:26:16.194685	1000069	0ea139e6-e65d-4820-8fea-e0b609ffff51	Y	Y
1000070	1000004	1000016	1000360	QRC		0	\N	\N	\N	2024-09-18 04:48:35.222681	1000069	2024-09-18 04:48:35.222682	1000069	bd5c5ada-30fe-4f77-949f-ae81553aca91	Y	Y
1000071	1000004	1000016	1000360	QRC		0	\N	\N	\N	2024-09-18 04:48:35.253324	1000069	2024-09-18 04:48:35.253324	1000069	fc6acdef-ac89-43b5-85ae-1611da2f7ca8	Y	Y
1000072	1000004	1000016	1000455	QRC		2578000	\N	\N	\N	2024-09-18 04:50:20.957664	1000069	2024-09-18 04:50:20.957665	1000069	3a310705-7587-49e9-b232-c41c31c88e47	Y	Y
1000073	1000004	1000016	1000413	QRC		2139000	\N	\N	\N	2024-09-18 04:52:50.522746	1000069	2024-09-18 04:52:50.522747	1000069	cc056cbc-344d-40f7-8581-a1b91a875cd7	Y	Y
1000074	1000004	1000016	1000401	QRC		4158000	\N	\N	\N	2024-09-18 04:56:09.940288	1000069	2024-09-18 04:56:09.940289	1000069	0e5bda91-0fd7-474c-af17-ce1cd2069e52	Y	Y
1000075	1000004	1000016	1000399	QRC		43200	\N	\N	\N	2024-09-18 04:58:38.737433	1000069	2024-09-18 04:58:38.737433	1000069	a8d7aac0-099b-4d34-9c38-3b8ee1e00a6b	Y	Y
1000076	1000004	1000016	1000387	QRC		2295000	\N	\N	\N	2024-09-18 05:04:52.494385	1000069	2024-09-18 05:04:52.494386	1000069	20b0e869-c51a-40f0-9244-61b50c7cfca3	Y	Y
1000077	1000004	1000016	1000393	QRC		43200	\N	\N	\N	2024-09-18 06:32:22.606736	1000069	2024-09-18 06:32:22.606736	1000069	841cfc1b-874a-45ff-9ea6-41504136deca	Y	Y
1000078	1000004	1000016	1000409	QRC		40000	\N	\N	\N	2024-09-18 06:40:44.902964	1000069	2024-09-18 06:40:44.902965	1000069	89b097a6-3915-4aa6-a8a9-2c1d2e5156b8	Y	Y
1000079	1000004	1000016	1000491	QRC		2000	\N	\N	\N	2024-09-18 07:31:53.519655	1000069	2024-09-18 07:31:53.519655	1000069	b87cd38c-f4c4-4d8b-9f52-09faf0d62d93	Y	Y
1000080	1000004	1000016	1000493	QRC		2000	\N	\N	\N	2024-09-18 08:10:06.406238	1000069	2024-09-18 08:10:06.406239	1000069	388fa331-75dc-4cfb-a900-8598b3b44c86	Y	Y
1000081	1000004	1000016	1000495	QRC		2000	\N	\N	\N	2024-09-18 08:24:34.48831	1000069	2024-09-18 08:24:34.488311	1000069	e454723a-b90f-4e38-a8e0-b18dae307f05	Y	Y
1000082	1000004	1000016	1000453	QRC		378000	\N	\N	\N	2024-09-18 08:29:25.136155	1000069	2024-09-18 08:29:25.136156	1000069	fdc0eeea-205d-4f35-b7f8-b17f72c6c38f	Y	Y
1000083	1000004	1000016	1000407	QRC		20000	\N	\N	\N	2024-09-18 08:48:37.56686	1000069	2024-09-18 08:48:37.566862	1000069	1d7feef4-5140-492f-89f8-ac462532b414	Y	Y
1000084	1000004	1000016	1000507	QRC		2609000	\N	\N	\N	2024-09-18 09:31:21.395763	1000069	2024-09-18 09:31:21.395764	1000069	b434b70e-ecb0-4baa-a3fe-f5d6a44a5440	Y	Y
\.


--
-- TOC entry 5488 (class 0 OID 393099)
-- Dependencies: 347
-- Data for Name: d_pos_taxline; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_pos_taxline (d_pos_taxline_id, d_tenant_id, d_org_id, d_pos_order_id, d_tax_id, tax_amount, tax_base_amount, is_price_intax, created, created_by, updated, updated_by, d_pos_taxline_uu, is_active) FROM stdin;
1000012	1000004	1000016	1000192	1000003	2000	220000	\N	2024-09-08 16:51:36.425018	1000069	2024-09-08 16:51:36.425018	1000069	9abacca2-3c81-450a-bd7a-82200936bd7b	Y
1000021	1000004	1000016	1000194	1000003	2000	200000	\N	2024-09-08 17:21:34.6905	1000069	2024-09-08 17:21:34.6905	1000069	460f25b8-ce2c-4923-9df3-60f2d5cd3cce	Y
1000022	1000004	1000016	1000194	1000003	2000	200000	\N	2024-09-08 17:22:54.838977	1000069	2024-09-08 17:22:54.838977	1000069	8361fcd9-07d9-4c3d-858b-a6bbd6f41e52	Y
1000033	1000004	1000016	1000194	1000003	2000	200000	\N	2024-09-08 17:32:12.194206	1000069	2024-09-08 17:32:12.194206	1000069	1e149052-7b0e-433f-9baf-1d87c837781b	Y
1000034	1000004	1000016	1000194	1000003	2000	200000	\N	2024-09-08 17:45:09.007766	1000069	2024-09-08 17:45:09.007766	1000069	65022dd3-8211-45c9-b42d-d740e8dee7c7	Y
1000035	1000004	1000016	1000194	1000003	2000	200000	\N	2024-09-08 17:52:41.8026	1000069	2024-09-08 17:52:41.8026	1000069	6f81f9f9-1770-46ef-ac89-fb491ffe6119	Y
1000036	0	1000016	1000198	1000003	2000	200000	\N	2024-09-08 11:21:10.526529	0	2024-09-08 11:21:10.52653	0	4aefdc52-1cf5-436e-b3ec-472086efc82d	Y
1000038	0	1000016	1000204	1000003	2000	200000	\N	2024-09-09 00:32:07.812092	0	2024-09-09 00:32:07.812092	0	983c3916-00ec-46e6-92f6-d35ff5ae3a4e	Y
1000039	0	1000016	1000204	1000003	2000	200000	\N	2024-09-09 00:38:21.043489	0	2024-09-09 00:38:21.043489	0	11ad4953-dfa3-442f-8735-7d2f8462ada5	Y
1000040	1000004	1000016	1000204	1000003	2000	200000	\N	2024-09-09 00:46:55.857352	0	2024-09-09 00:46:55.857352	0	7510d9fe-0a41-41a3-8a41-8a21f8740742	Y
1000041	1000004	1000016	1000204	1000003	2000	200000	\N	2024-09-09 00:50:02.435413	0	2024-09-09 00:50:02.435413	0	7d683f1f-c146-4c9c-a3e8-02e966e81166	Y
1000042	1000004	1000016	1000204	1000003	2000	200000	\N	2024-09-09 00:50:58.400716	0	2024-09-09 00:50:58.400716	0	011fbaa2-079c-4cdc-8978-3cb7337e9474	Y
1000045	1000004	1000016	1000489	1000003	2001600	220000	\N	2024-09-18 07:02:10.980252	0	2024-09-18 07:02:10.980254	0	dfa36abf-8a87-4ae5-a239-57b268eab7a9	Y
1000046	1000004	1000016	1000489	1000003	2001600	220000	\N	2024-09-18 07:18:08.422799	0	2024-09-18 07:18:08.422801	0	9e6e79c4-b9ba-49e7-a3b2-bab93b1d606c	Y
1000047	1000004	1000016	1000489	1000003	2001600	220000	\N	2024-09-18 07:30:03.274969	0	2024-09-18 07:30:03.274971	0	7dda3de9-c2e1-465d-9c1e-4e8c7408ad2a	Y
1000048	1000004	1000016	1000491	1000003	2001600	220000	\N	2024-09-18 07:41:28.893444	0	2024-09-18 07:41:28.893445	0	883d41b4-477c-4f4b-979f-30c68a00da47	Y
1000049	1000004	1000016	1000493	1000003	2001600	220000	\N	2024-09-18 08:24:08.706471	0	2024-09-18 08:24:08.706472	0	c7c85c13-a51e-42ad-9d63-9df6e42aa321	Y
1000050	1000004	1000016	1000495	1000003	2001600	220000	\N	2024-09-18 08:26:10.511649	0	2024-09-18 08:26:10.511652	0	70fae662-e954-4fa9-a940-444e4620f522	Y
\.


--
-- TOC entry 5476 (class 0 OID 392879)
-- Dependencies: 335
-- Data for Name: d_pos_terminal; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_pos_terminal (d_pos_terminal_id, d_tenant_id, d_org_id, name, description, d_user_id, d_bank_account_cash_id, is_restaurant, d_pricelist_id, d_warehouse_id, d_bank_account_id, printer_ip, printer_port, is_bill_merge, is_notify_bill, d_bank_account_visa_id, erp_pos_id, created, created_by, updated, updated_by, d_pos_terminal_uu, is_active, d_doctype_id) FROM stdin;
1000003	1000004	1000016	Main POS Terminal	POS terminal located at the main counter	1234	9000000	Y	91011	1213	1000001	192.168.1.100	9100	N	Y	2000000	1819	2024-09-06 16:54:03.361169	1000069	2024-09-06 16:54:03.361169	1000069	6fb40192-0be6-4878-9551-34d77f8ad0b1	Y	1000002
1000004	1000004	1000016	Main POS Terminal	POS terminal located at the main counter	1234	5678	Y	91011	1213	1415	192.168.1.100	9100	N	Y	1617	1819	2024-09-13 11:29:23.73611	1000069	2024-09-13 11:29:23.736118	1000069	29489941-1281-40fa-9242-23f3475ec067	Y	1000002
1000005	1000009	1000016	Main POS Terminal	POS terminal located at the main counter	1234	5678	Y	91011	1213	1415	192.168.1.100	9100	N	Y	1617	1819	2024-09-16 10:06:16.202556	1000071	2024-09-16 10:06:16.202556	1000071	98484925-9192-4c2f-a334-f7e872c0cfd2	Y	1000002
1000008	1000004	1000016	Điểm bán 2	\N	1000070	1000001	\N	1000047	1000024	1000001	123	1234	N	Y	1000001	\N	2024-09-17 09:34:59.725182	1000069	2024-09-17 14:09:37.658208	1000069	970f526f-0659-4d8a-bc1b-cde392c4e8e5	Y	\N
1000009	1000004	1000016	Điểm bán 31	\N	1000070	1000001	\N	1000051	1000024	1000001	123	123	Y	Y	1000001	\N	2024-09-17 14:10:35.853632	1000069	2024-09-17 14:19:59.019964	1000069	5d92c723-f2f9-4922-94e8-0d62be08c874	Y	\N
1000010	1000004	1000016	Điểm bán 1	\N	1000069	1000001	\N	1000046	1000008	1000001	23232	33232	Y	Y	1000001	\N	2024-09-18 10:45:21.538225	1000069	2024-09-18 10:45:21.53823	1000069	1a4e580d-d688-4549-b7ee-2256c4ddd1bf	Y	\N
\.


--
-- TOC entry 5523 (class 0 OID 394786)
-- Dependencies: 397
-- Data for Name: d_posterminal_org_access; Type: TABLE DATA; Schema: pos; Owner: adempiere
--

COPY pos.d_posterminal_org_access (d_user_id, d_org_id, d_pos_terminal_id, d_tenant_id, created, created_by, updated, updated_by, is_active, d_posterminal_org_access_uu) FROM stdin;
1000070	1000016	1000008	1000004	2024-09-18 06:35:14.96859	0	2024-09-18 06:35:14.96859	0	Y	19f8c93e-2e9b-4be5-9135-57a472a49a69
1000079	1000019	1000003	1000004	2024-09-18 08:31:56.599358	0	2024-09-18 08:31:56.599358	0	Y	a76d8e5f-010f-4f05-9030-b885aea08488
1000079	1000030	1000004	1000004	2024-09-18 08:31:56.599358	0	2024-09-18 08:31:56.599358	0	Y	abb94b05-bd36-4cce-b497-fb001b1fe51f
\.


--
-- TOC entry 5450 (class 0 OID 388752)
-- Dependencies: 309
-- Data for Name: d_pricelist; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_pricelist (d_pricelist_id, d_tenant_id, d_org_id, name, is_active, from_date, to_date, is_saleprice, created, created_by, updated, updated_by, d_pricelist_uu, general_pricelist) FROM stdin;
1000059	1000004	0	tét	Y	2024-09-19 00:00:00	2024-09-25 00:00:00	Y	2024-09-18 15:18:46.615725	1000069	2024-09-18 15:18:46.615726	1000069	37026caf-2289-4447-87e6-9a5bea37c36c	N
1000058	1000004	0	new 32	N	2024-09-20 00:00:00	2024-09-21 00:00:00	Y	2024-09-17 16:39:43.170666	1000069	2024-09-18 15:35:53.807296	1000069	3b251ba8-6559-4dd1-b0fa-a29a5f4a156b	N
1000013	1000004	0	Bảng giá chung	N	2024-09-02 00:00:00	2024-09-30 00:00:00	Y	2024-08-29 10:18:38.172948	0	2024-09-18 11:29:07.700524	1000069	239baae8-a2ee-4427-8590-076b40bb641c	Y
1000045	1000004	0	Bảng giá 05 - 09 - 2024	Y	2024-09-05 00:00:00	2024-09-30 00:00:00	Y	2024-09-05 06:36:24.562672	1000069	2024-09-05 06:36:24.562672	1000069	85023acd-c7a7-4ffd-aca3-1bf898175c21	N
1000046	1000004	0	Bảng giá 01 - 09 - 2024	Y	2024-09-01 00:00:00	2024-09-30 00:00:00	Y	2024-09-05 06:37:34.070505	1000069	2024-09-05 06:37:34.070505	1000069	b0dd85c9-71c2-4cd9-ac6b-d247480b3d66	N
1000047	1000004	0	Bảng giá 02 - 09 - 2024	Y	2024-09-02 00:00:00	2024-09-30 00:00:00	Y	2024-09-05 06:37:39.198124	1000069	2024-09-05 06:37:39.198124	1000069	c70b3f4c-9e4d-4edf-8b6f-2a938e0eedc7	N
1000048	1000004	0	a	Y	2024-09-06 00:00:00	2024-09-10 00:00:00	Y	2024-09-06 15:41:48.564904	1000069	2024-09-06 15:41:48.564907	1000069	8a37aed4-881b-45f7-b47f-89b3ca1d84fe	N
1000049	1000009	0	Bảng giá chung	Y	2024-07-11 15:56:06	2024-09-11 15:56:13	Y	2024-09-11 08:51:29.979942	0	2024-09-11 08:51:29.979942	0	93202a8b-2c26-4c18-8d04-71ee24a028d4	Y
1000050	1000004	0	tét	Y	2024-09-13 00:00:00	2024-09-14 00:00:00	Y	2024-09-12 16:23:13.345153	1000069	2024-09-12 16:23:13.345153	1000069	e0eae533-32ea-4015-ae21-544ebf4f52f3	N
1000051	1000004	0	tét	Y	2024-09-13 00:00:00	2024-09-17 00:00:00	Y	2024-09-12 18:11:15.143868	1000069	2024-09-12 18:11:15.143868	1000069	d02c1eda-eb5b-4861-9b0a-631041611689	N
1000056	1000004	0	new	Y	2024-09-17 00:00:00	2024-09-19 00:00:00	Y	2024-09-17 16:25:39.714919	1000069	2024-09-17 16:25:39.71492	1000069	81c7157f-e737-4043-b888-a09fd8b4fa9e	N
1000057	1000004	0	new 2	Y	2024-09-17 00:00:00	2024-09-19 00:00:00	Y	2024-09-17 16:34:02.428085	1000069	2024-09-17 16:34:02.428087	1000069	8d1b6379-c408-470a-b038-8426a9b07ce5	N
\.


--
-- TOC entry 5454 (class 0 OID 388792)
-- Dependencies: 313
-- Data for Name: d_pricelist_org; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_pricelist_org (d_pricelist_org_id, d_pricelist_id, d_tenant_id, d_org_id, isall, is_active, created, created_by, updated, updated_by, d_pricelist_org_uu) FROM stdin;
1000047	1000045	1000004	1000016	\N	Y	2024-09-05 06:36:24.574239	1000069	2024-09-05 06:36:24.574239	1000069	1b32f820-5464-4653-baa0-b4bd26fc43bf
1000048	1000046	1000004	1000016	\N	Y	2024-09-05 06:37:34.077537	1000069	2024-09-05 06:37:34.077537	1000069	7dbd1603-4c55-4ae2-87b6-a5402d2ad5af
1000050	1000047	1000004	1000016	\N	Y	2024-09-05 06:45:17.642755	1000069	2024-09-05 06:45:17.642755	1000069	caeefc74-a5f1-4ac5-b95e-1677790c42f2
1000051	1000047	1000004	1000019	\N	Y	2024-09-05 06:45:17.64843	1000069	2024-09-05 06:45:17.64843	1000069	4510cf4b-1184-4aea-842e-eed6b60c7bc1
1000052	1000047	1000004	1000020	\N	Y	2024-09-05 06:45:17.655466	1000069	2024-09-05 06:45:17.655466	1000069	5b549689-1224-4815-9902-720be19c099d
1000053	1000047	1000004	1000021	\N	Y	2024-09-05 06:45:17.660851	1000069	2024-09-05 06:45:17.660851	1000069	f4db3067-d60a-48ea-b8d5-45b6cd801c38
1000058	1000013	1000004	1000016	\N	Y	2024-09-18 11:29:07.645392	1000069	2024-09-18 11:29:07.645392	1000069	328ef8c1-ab5b-4f61-bdfe-3c076570eef5
1000059	1000013	1000004	1000019	\N	Y	2024-09-18 11:29:07.657852	1000069	2024-09-18 11:29:07.657852	1000069	bc21f43e-7899-48f9-9f51-7d1c2aa00943
1000060	1000013	1000004	1000021	\N	Y	2024-09-18 11:29:07.669152	1000069	2024-09-18 11:29:07.669153	1000069	6823a11c-a6c3-47ac-a27d-4b6cdb3b9c2c
1000061	1000013	1000004	1000020	\N	Y	2024-09-18 11:29:07.673999	1000069	2024-09-18 11:29:07.674	1000069	b6077659-543d-450b-8a2e-4271dccbd993
\.


--
-- TOC entry 5452 (class 0 OID 388771)
-- Dependencies: 311
-- Data for Name: d_pricelist_product; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_pricelist_product (d_pricelist_product_id, d_tenant_id, d_org_id, d_product_id, is_active, costprice, standardprice, salesprice, lastorderprice, created, created_by, updated, updated_by, d_pricelist_product_uu, d_pricelist_id) FROM stdin;
1000260	1000004	0	1000643	Y	100000	\N	200000	0	2024-09-18 23:24:42.594357	1000069	2024-09-18 23:24:42.594359	1000069	3a773cd1-7a42-438a-8e26-bfed92a42f2c	1000013
1000261	1000004	0	1000644	Y	15000	\N	20000	0	2024-09-18 23:36:56.493106	1000069	2024-09-18 23:36:56.493108	1000069	925d0939-83b1-4dac-ba23-6cbc0dbbb207	1000013
1000228	1000004	0	1000506	Y	10000	20000	16000	0	2024-09-18 13:49:33.461165	1000069	2024-09-18 14:53:04.078362	1000069	e7fb4cbf-1d78-45b9-827d-9f38f7749466	1000050
1000229	1000004	0	1000353	Y	10000	15000	12000.0	0	2024-09-18 13:49:45.784023	1000069	2024-09-18 14:53:15.239948	1000069	acb89082-4eeb-4683-a575-9dfa9eb8270f	1000050
1000233	1000004	0	1000503	Y	10000	20000	16000.0	0	2024-09-18 14:43:18.847201	1000069	2024-09-18 14:53:15.240016	1000069	e53ebf57-8a4c-4d4e-b051-f4df12f65dbf	1000050
1000236	1000004	0	1000506	Y	10000	20000	20000	0	2024-09-18 15:32:12.954268	1000069	2024-09-18 15:32:12.954268	1000069	39dfa94e-c308-4564-961c-415a6c44ec3f	1000059
1000237	1000004	0	1000503	Y	10000	20000	20000	0	2024-09-18 15:32:16.363608	1000069	2024-09-18 15:32:16.363608	1000069	d2bec82c-f3f8-4bbf-badc-8dad689af0d4	1000059
1000238	1000004	0	1000621	Y	10000	\N	15000	0	2024-09-18 21:18:35.27178	1000069	2024-09-18 21:18:35.27178	1000069	1c52a135-914a-41bd-b89d-0933450f37d4	1000013
1000239	1000004	0	1000622	Y	10000	\N	15000	0	2024-09-18 21:18:35.452139	1000069	2024-09-18 21:18:35.452139	1000069	53f66efa-cd43-4682-9726-7ecb0831b89d	1000013
1000240	1000004	0	1000623	Y	10000	\N	15000	0	2024-09-18 21:18:35.516557	1000069	2024-09-18 21:18:35.516557	1000069	6e9c186a-0b1a-40f1-9cf5-ce1de2892546	1000013
1000242	1000004	0	1000625	Y	10000	\N	15000	0	2024-09-18 21:53:32.356455	1000069	2024-09-18 21:53:32.356458	1000069	f49fe7cd-328f-4c9a-8d8b-38846a70ac5a	1000013
1000243	1000004	0	1000626	Y	10000	\N	15000	0	2024-09-18 21:53:32.428593	1000069	2024-09-18 21:53:32.428607	1000069	a87f6da2-fa44-43e0-a2f1-c45356a5f85e	1000013
1000244	1000004	0	1000627	Y	10000	\N	15000	0	2024-09-18 21:53:32.483008	1000069	2024-09-18 21:53:32.48301	1000069	8299e873-321b-498b-9b08-80d924bada17	1000013
1000278	1000004	0	1000661	Y	10000	\N	15000	0	2024-09-19 11:20:59.537595	1000069	2024-09-19 11:20:59.537597	1000069	6848c70c-b8a8-427d-8c19-69186da40e2e	1000013
1000279	1000004	0	1000662	Y	10000	\N	15000	0	2024-09-19 11:20:59.608404	1000069	2024-09-19 11:20:59.608406	1000069	03935871-e4ce-4687-85e0-d0e61476133f	1000013
1000280	1000004	0	1000663	Y	10000	\N	15000	0	2024-09-19 11:20:59.655398	1000069	2024-09-19 11:20:59.6554	1000069	afd8c258-96ed-4873-af6c-e49b7b84cfe1	1000013
1000287	1000004	0	1000670	Y	20000	\N	30000	0	2024-09-19 11:27:15.495905	1000069	2024-09-19 11:27:15.495907	1000069	6c4f74ba-207f-4997-ab8f-730696717a31	1000013
1000110	1000004	0	1000383	Y	100000	\N	120000	0	2024-09-04 09:23:01.453249	1000069	2024-09-04 11:48:20.224823	1000069	b453e5ca-1611-4ab1-aa0a-5fcd82af20a4	1000013
1000074	1000004	0	1000111	Y	50	\N	20050	0	2024-08-30 08:36:41.829773	0	2024-09-04 11:48:20.225425	1000069	f1b017db-7d0c-48f5-990d-97da0811f4bd	1000013
1000075	1000004	0	1000138	Y	50	\N	20050	0	2024-08-30 08:36:41.900032	0	2024-09-04 11:48:20.225519	1000069	1991dda1-8f83-42d1-b9ca-5bb70e8be0ad	1000013
1000073	1000004	0	1000140	Y	50	\N	20050	0	2024-08-30 08:36:41.758959	0	2024-09-04 11:48:20.225565	1000069	1568a56b-e942-40ac-b121-b858cb96e28f	1000013
1000089	1000004	0	1000136	Y	50	\N	20050	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.225621	1000069	b6f8b969-1d66-4e2b-a427-42ed8bd02a77	1000013
1000090	1000004	0	1000127	Y	50	\N	20050	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.225655	1000069	cf536b9a-8e04-446a-b5bf-5173413c48b0	1000013
1000091	1000004	0	1000129	Y	50	\N	20050	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.225687	1000069	f7c176cf-4117-4d69-bd35-95c33026af6b	1000013
1000092	1000004	0	1000130	Y	50	\N	20050	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.22572	1000069	3be53fc2-9f0f-4e4f-9f44-5b9c137dfa36	1000013
1000093	1000004	0	1000132	Y	50	\N	20050	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.225775	1000069	d6aa8435-e13c-4db2-b756-6366d1374451	1000013
1000094	1000004	0	1000134	Y	50	\N	20050	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.225876	1000069	fd494918-23a6-4f9d-8d98-fe4cd10c86c0	1000013
1000095	1000004	0	1000103	Y	50000	\N	70000	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.225909	1000069	d50e3d5f-7fc5-44c5-a059-08fcfe6c2dd8	1000013
1000096	1000004	0	1000119	Y	50	\N	20050	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.22594	1000069	e42b0ef0-50cb-4b08-90a3-70f298dc60f2	1000013
1000097	1000004	0	1000113	Y	50	\N	20050	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.225974	1000069	20c8c1b8-f52c-4138-81e1-70250152b3dc	1000013
1000098	1000004	0	1000108	Y	50	\N	20050	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.226005	1000069	f20eae45-70e4-4b8b-9c35-f05a2d57d6c1	1000013
1000099	1000004	0	1000106	Y	50	\N	20050	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.226035	1000069	66b6b954-0ac5-4ce4-9d74-72b0ba5a1caa	1000013
1000080	1000004	0	1000356	Y	10000	15000	15000	0	2024-08-30 08:44:10.373867	1000069	2024-08-30 08:44:10.373867	1000069	e5c109cc-b97e-4655-ac6c-3f0bcc662825	1000013
1000079	1000004	0	1000353	Y	10000	15000	15000	0	2024-08-30 08:44:10.329457	1000069	2024-08-30 08:44:10.329457	1000069	e852f3f0-e492-4226-b500-491ec82c0892	1000013
1000081	1000004	0	1000359	Y	10000	15000	15000	0	2024-08-30 08:44:10.397987	1000069	2024-08-30 08:44:10.397987	1000069	afe236f9-cfea-48db-a324-451d765c1f9b	1000013
1000100	1000004	0	1000112	Y	50	\N	20050	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.226597	1000069	31f5f22e-ce1a-4ecc-9fdd-a0a830fd2f97	1000013
1000101	1000004	0	1000107	Y	50	\N	20050	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.226671	1000069	affd1997-4575-47c2-b116-ba9eb05c55ae	1000013
1000102	1000004	0	1000105	Y	50	\N	20050	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.226731	1000069	f193318b-df55-43bc-9d45-96f4629c62ff	1000013
1000104	1000004	0	1000109	Y	50	\N	20050	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.226789	1000069	38a4b557-0c3d-4e91-b376-5c6c5691755a	1000013
1000108	1000004	0	1000377	Y	100000	\N	120000	0	2024-09-03 22:22:20.643603	1000069	2024-09-04 11:48:20.226815	1000069	9796c2c9-c26f-424e-8acf-781775133f27	1000013
1000109	1000004	0	1000380	Y	100000	\N	120000	0	2024-09-03 22:22:20.763773	1000069	2024-09-04 11:48:20.226842	1000069	704ece79-1f52-446a-a6f0-981f55c4c972	1000013
1000111	1000004	0	1000386	Y	100000	\N	120000	0	2024-09-04 09:23:01.506634	1000069	2024-09-04 11:48:20.226868	1000069	bffe547a-c3f4-4129-a96f-56697010a12f	1000013
1000112	1000004	0	1000389	Y	100000	\N	120000	0	2024-09-04 09:23:01.51609	1000069	2024-09-04 11:48:20.226897	1000069	2110c633-f900-4abc-b5c5-df60481401d6	1000013
1000113	1000004	0	1000392	Y	100000	\N	120000	0	2024-09-04 09:47:43.911829	1000069	2024-09-04 11:48:20.226927	1000069	ca0043fa-742a-43ae-8261-e5b94e2e1a79	1000013
1000114	1000004	0	1000395	Y	100000	\N	120000	0	2024-09-04 09:47:43.969142	1000069	2024-09-04 11:48:20.226967	1000069	e8d7cb08-4b3d-4acf-8622-d25f15c7542c	1000013
1000115	1000004	0	1000398	Y	100000	\N	120000	0	2024-09-04 09:47:43.981981	1000069	2024-09-04 11:48:20.226993	1000069	7bd2955d-95da-4817-aa74-fb1c0ec86445	1000013
1000116	1000004	0	1000401	Y	10000	\N	30000	0	2024-09-04 09:49:27.132154	1000069	2024-09-04 11:48:20.227018	1000069	155e74ca-69c3-4292-94ae-93de08b6ac45	1000013
1000117	1000004	0	1000404	Y	10000	\N	30000	0	2024-09-04 09:49:27.14401	1000069	2024-09-04 11:48:20.227042	1000069	58466a3e-ba54-4918-a580-7f4a89df2458	1000013
1000118	1000004	0	1000139	Y	10000	\N	30000	0	2024-09-04 04:38:56.859959	1000069	2024-09-04 11:48:20.227066	1000069	9225e8a3-05ca-4880-a7e2-6358704c7d96	1000013
1000119	1000004	0	1000368	Y	10000	\N	30000	0	2024-09-04 04:38:56.859959	1000069	2024-09-04 11:48:20.22709	1000069	1aabd71b-73b7-4f15-bcd0-7ac192f3bd4c	1000013
1000120	1000004	0	1000371	Y	10000	\N	30000	0	2024-09-04 04:38:56.859959	1000069	2024-09-04 11:48:20.227143	1000069	a04e1e81-2587-4075-a598-e581f116d085	1000013
1000121	1000004	0	1000374	Y	10000	\N	30000	0	2024-09-04 04:38:56.859959	1000069	2024-09-04 11:48:20.227226	1000069	63f34120-424a-4c0c-bf54-9c52770bab7d	1000013
1000174	1000009	0	1000536	Y	20000	\N	43000	0	2024-09-11 16:32:36.65858	1000071	2024-09-11 16:32:36.658582	1000071	d4ae90a0-c32e-4dd7-be11-e6239591f97e	1000049
1000190	1000009	0	1000565	Y	30000	\N	45000	0	2024-09-11 17:59:37.360786	1000071	2024-09-11 17:59:37.360786	1000071	446f0a40-963e-4987-8898-af8cf26ca289	1000049
1000191	1000009	0	1000568	Y	40000	\N	120000	0	2024-09-11 18:10:24.900583	1000071	2024-09-11 18:10:24.900584	1000071	b418ddfc-33d0-41e3-b372-af4b80fcda82	1000049
1000215	1000004	0	1000562	Y	40000	\N	56000	\N	2024-09-17 09:40:22.923173	0	2024-09-17 09:40:22.923173	0	4f737e14-8057-4430-b0e7-315573a5d304	1000013
1000216	1000004	0	1000608	Y	50	\N	100	\N	2024-09-17 09:40:22.923173	0	2024-09-17 09:40:22.923173	0	eb5ba3ae-2399-463f-adc4-e03c28a789a7	1000013
1000217	1000004	0	1000571	Y	40000	\N	11000	\N	2024-09-17 09:40:22.923173	0	2024-09-17 09:40:22.923173	0	19366006-bc5e-4bb6-8b46-99e5bc0600c5	1000013
1000103	1000004	0	1000104	Y	60	\N	20051	0	2024-08-30 03:06:22.476435	0	2024-09-04 11:48:20.226761	1000069	eaf19392-7c12-4bf2-a9d6-bf01a8601d11	1000013
1000218	1000004	0	1000577	Y	45000	\N	110000	\N	2024-09-17 09:40:22.923173	0	2024-09-17 09:40:22.923173	0	7924b02e-d525-43b2-b097-1a4d8f6940eb	1000013
1000219	1000004	0	1000574	Y	32000	\N	123000	\N	2024-09-17 09:40:22.923173	0	2024-09-17 09:40:22.923173	0	fdb7fb01-080a-45d2-8a1e-09f9dc5b9cff	1000013
1000134	1000004	0	1000407	Y	100000	\N	200000	0	2024-09-04 17:35:06.996798	1000069	2024-09-04 17:35:06.996799	1000069	5682806c-933c-4f81-b0cf-bd8354537bc0	1000013
1000135	1000004	0	1000410	Y	100000	\N	200000	0	2024-09-04 17:35:07.158645	1000069	2024-09-04 17:35:07.158646	1000069	437789e7-4931-4389-b0a1-72654e93ba08	1000013
1000136	1000004	0	1000413	Y	100000	\N	200000	0	2024-09-05 09:22:46.65179	1000069	2024-09-05 09:22:46.65179	1000069	a8cf774a-fe14-48a7-bebe-b002d7fbdb81	1000013
1000137	1000004	0	1000416	Y	100000	\N	200000	0	2024-09-05 09:22:46.715228	1000069	2024-09-05 09:22:46.715228	1000069	3b339e69-af95-46d4-8cef-e118f28ee898	1000013
1000138	1000004	0	1000419	Y	100000	\N	200000	0	2024-09-05 09:22:46.725368	1000069	2024-09-05 09:22:46.725368	1000069	a686e724-5216-4250-8f52-ba8713647913	1000013
1000139	1000004	0	1000422	Y	100000	\N	200000	0	2024-09-05 09:22:46.735774	1000069	2024-09-05 09:22:46.735775	1000069	c5ac67b0-cdcd-4fda-9004-bb2c7ecca031	1000013
1000140	1000004	0	1000425	Y	100000	\N	200000	0	2024-09-05 09:22:46.760431	1000069	2024-09-05 09:22:46.760432	1000069	127fa19f-028a-4e56-a5fb-5b43a88cda09	1000013
1000141	1000004	0	1000428	Y	100000	\N	200000	0	2024-09-05 09:22:46.779993	1000069	2024-09-05 09:22:46.779993	1000069	b7185e2b-8112-4d49-a9ac-13496411a6bf	1000013
1000142	1000004	0	1000431	Y	100000	\N	200000	0	2024-09-05 09:22:46.791324	1000069	2024-09-05 09:22:46.791325	1000069	cde5dbc1-e014-4204-ad57-00e43a69cebd	1000013
1000143	1000004	0	1000434	Y	100000	\N	200000	0	2024-09-05 09:22:46.801089	1000069	2024-09-05 09:22:46.80109	1000069	682d216a-5f4c-49a6-a473-ff06d2e9f31f	1000013
1000144	1000004	0	1000437	Y	100000	\N	200000	0	2024-09-05 09:22:46.810712	1000069	2024-09-05 09:22:46.810712	1000069	2ee858be-4c24-4567-a18f-2c770733c033	1000013
1000145	1000004	0	1000440	Y	100000	\N	200000	0	2024-09-05 09:22:46.819886	1000069	2024-09-05 09:22:46.819887	1000069	6323f0a7-ebb1-4140-a08d-5e0a86c9fdb4	1000013
1000146	1000004	0	1000443	Y	100000	\N	200000	0	2024-09-05 09:22:46.827712	1000069	2024-09-05 09:22:46.827712	1000069	e2ba0a20-2aae-459a-adab-d16e8f221de4	1000013
1000147	1000004	0	1000446	Y	100000	\N	200000	0	2024-09-05 09:22:46.835164	1000069	2024-09-05 09:22:46.835164	1000069	0bac7012-9522-4000-840b-4c7e9dded205	1000013
1000148	1000004	0	1000449	Y	100000	\N	200000	0	2024-09-05 09:22:46.844356	1000069	2024-09-05 09:22:46.844356	1000069	103f7df8-093c-4970-82e0-e15d6aa57248	1000013
1000149	1000004	0	1000452	Y	100000	\N	200000	0	2024-09-05 09:22:46.856741	1000069	2024-09-05 09:22:46.856741	1000069	ccdbfe42-0c00-44a5-82a8-5525ef826c1b	1000013
1000150	1000004	0	1000455	Y	100000	\N	200000	0	2024-09-05 09:22:46.872568	1000069	2024-09-05 09:22:46.872568	1000069	f0ab4baf-11d7-4aa1-abb2-7db4ffc35e7f	1000013
1000151	1000004	0	1000458	Y	100000	\N	200000	0	2024-09-05 09:22:46.896517	1000069	2024-09-05 09:22:46.896517	1000069	f8cdcafb-d6f4-477b-a39e-f8a73d59b275	1000013
1000220	1000004	0	1000536	Y	20000	\N	43000	\N	2024-09-17 09:40:22.923173	0	2024-09-17 09:40:22.923173	0	83d4daf2-7dfe-4a63-8c7c-7b9456a66ee8	1000013
1000152	1000004	0	1000461	Y	100000	\N	200000	0	2024-09-05 09:22:46.910889	1000069	2024-09-05 09:22:46.910889	1000069	643da615-3486-417f-89b5-0196ed38f03c	1000013
1000153	1000004	0	1000464	Y	100000	\N	200000	0	2024-09-05 09:22:46.922729	1000069	2024-09-05 09:22:46.922729	1000069	5792189d-1e19-4e1e-b0b6-107c93df8be0	1000013
1000154	1000004	0	1000467	Y	100000	\N	200000	0	2024-09-05 09:22:46.933816	1000069	2024-09-05 09:22:46.933816	1000069	be4fd61a-5d89-48d2-b8d8-6a8d481fab36	1000013
1000155	1000004	0	1000470	Y	100000	\N	189000	0	2024-09-05 09:29:12.106197	1000069	2024-09-05 09:29:12.106197	1000069	f89da9c0-3025-4ef3-997d-05b42a9748a4	1000013
1000156	1000004	0	1000473	Y	100000	\N	189000	0	2024-09-05 09:29:12.20308	1000069	2024-09-05 09:29:12.20308	1000069	29d208c9-4509-4b68-8ebd-2842fe36c728	1000013
1000157	1000004	0	1000476	Y	100000	\N	189000	0	2024-09-05 09:29:12.232386	1000069	2024-09-05 09:29:12.232386	1000069	6e7b8f6b-9315-4702-aa42-85ff9ef826fc	1000013
1000158	1000004	0	1000479	Y	100000	\N	189000	0	2024-09-05 09:29:12.250635	1000069	2024-09-05 09:29:12.250636	1000069	89087665-9267-45d6-ade5-d865eab8b998	1000013
1000159	1000004	0	1000482	Y	100000	\N	189000	0	2024-09-05 09:29:12.267289	1000069	2024-09-05 09:29:12.26729	1000069	ad13ddea-716d-416e-b157-de28df6549f2	1000013
1000160	1000004	0	1000485	Y	100000	\N	189000	0	2024-09-05 09:29:12.279467	1000069	2024-09-05 09:29:12.279467	1000069	7859e1bd-5ed0-42aa-abbc-0952f723a0e1	1000013
1000161	1000004	0	1000488	Y	100000	\N	189000	0	2024-09-05 09:29:12.296048	1000069	2024-09-05 09:29:12.296048	1000069	0c5c43a4-84cb-4ab4-bf8d-73cde0b662d8	1000013
1000162	1000004	0	1000491	Y	10000	\N	20000	0	2024-09-05 09:32:37.178964	1000069	2024-09-05 09:32:37.178964	1000069	da2f305b-e925-402a-8fe7-cbc4ddbf0e0a	1000013
1000163	1000004	0	1000494	Y	10000	\N	20000	0	2024-09-05 09:32:37.189164	1000069	2024-09-05 09:32:37.189164	1000069	167b9ddd-8908-403c-bb19-b80a768d638f	1000013
1000164	1000004	0	1000497	Y	10000	\N	20000	0	2024-09-05 09:32:37.193866	1000069	2024-09-05 09:32:37.193866	1000069	2a2f6009-7f43-4a00-92ca-209c3d3178bd	1000013
1000165	1000004	0	1000500	Y	10000	\N	20000	0	2024-09-05 09:32:37.198241	1000069	2024-09-05 09:32:37.198241	1000069	2093f22f-5391-455a-b443-670e088bd0ac	1000013
1000166	1000004	0	1000503	Y	10000	\N	20000	0	2024-09-05 09:32:37.20262	1000069	2024-09-05 09:32:37.20262	1000069	cfb94cf9-3c7d-4211-9484-ade22fc4e477	1000013
1000167	1000004	0	1000506	Y	10000	\N	20000	0	2024-09-05 09:32:37.207345	1000069	2024-09-05 09:32:37.207345	1000069	a8c86212-7257-4f5c-8203-e5f700f2f00d	1000013
1000168	1000004	0	1000509	Y	10000	\N	20000	0	2024-09-05 09:32:37.211512	1000069	2024-09-05 09:32:37.211512	1000069	b28ce2d8-5d0c-452b-b228-1fa6df1aa01a	1000013
1000189	1000009	0	1000562	Y	40000	\N	56000	0	2024-09-11 17:57:24.640145	1000071	2024-09-11 17:57:24.640145	1000071	ba0a066e-7b8d-41e9-a84b-55032575b183	1000049
1000192	1000009	0	1000571	Y	40000	\N	11000	0	2024-09-11 18:12:49.98682	1000071	2024-09-11 18:12:49.98682	1000071	f940552f-bf74-4c54-89f7-f2107e160c2d	1000049
1000193	1000009	0	1000574	Y	32000	\N	123000	0	2024-09-11 18:14:23.703583	1000071	2024-09-11 18:14:23.703584	1000071	dbe19190-11c6-4c50-88a3-40171ee13813	1000049
1000194	1000009	0	1000577	Y	45000	\N	110000	0	2024-09-11 18:15:54.636025	1000071	2024-09-11 18:15:54.636025	1000071	43254183-2528-44fc-84e0-3064e4a956a9	1000049
1000221	1000004	0	1000089	Y	50	\N	100	\N	2024-09-17 09:40:22.923173	0	2024-09-17 09:40:22.923173	0	6cbd2a32-da24-441e-8e0a-423db7745d39	1000013
1000222	1000004	0	1000102	Y	50	\N	100	\N	2024-09-17 09:40:22.923173	0	2024-09-17 09:40:22.923173	0	2a9b14ea-c4cd-48fc-b2e7-cad42de672c4	1000013
1000223	1000004	0	1000088	Y	50	\N	100	\N	2024-09-17 09:40:22.923173	0	2024-09-17 09:40:22.923173	0	1910285d-4c71-4e91-92ec-8518a9b56a75	1000013
1000224	1000004	0	1000091	Y	50	\N	100	\N	2024-09-17 09:40:22.923173	0	2024-09-17 09:40:22.923173	0	39b7cfe6-3920-4c17-9e55-746611cfa6d9	1000013
1000225	1000004	0	1000093	Y	50	\N	100	\N	2024-09-17 09:40:22.923173	0	2024-09-17 09:40:22.923173	0	e0ec0131-29d0-400d-964d-fda831ce869c	1000013
1000226	1000004	0	1000568	Y	40000	\N	120000	\N	2024-09-17 09:40:22.923173	0	2024-09-17 09:40:22.923173	0	1af615b6-e9bb-4579-9c62-dda1bdd5237d	1000013
1000227	1000004	0	1000565	Y	30000	\N	45000	\N	2024-09-17 09:40:22.923173	0	2024-09-17 09:40:22.923173	0	47df659c-5fce-4b0c-b853-c9fbc2da2910	1000013
1000232	1000004	0	1000509	Y	10000	20000	20000	0	2024-09-18 14:42:49.322021	1000069	2024-09-18 14:42:49.322021	1000069	ed5683b9-7f73-46b9-88a0-c26ab853f2ce	1000051
1000234	1000004	0	1000356	Y	10000	15000	12000.0	0	2024-09-18 14:43:32.161187	1000069	2024-09-18 14:53:15.240086	1000069	e16fe49a-6ecf-4521-ab1e-4359bb5b144a	1000050
1000235	1000004	0	1000359	Y	10000	15000	20000	0	2024-09-18 14:43:32.172025	1000069	2024-09-18 14:53:45.932803	1000069	33c4a54c-ad84-4c60-9670-a256f3ed24fd	1000050
1000259	1000004	0	1000642	Y	100000	\N	200000	0	2024-09-18 23:25:58.481384	1000069	2024-09-18 23:25:58.481384	1000069	12d1f48a-04c4-4a67-8589-058271ace05c	1000013
1000268	1000004	0	1000651	Y	100000	\N	200000	0	2024-09-19 07:14:09.166929	1000069	2024-09-19 07:14:09.166929	1000069	a3975c97-f800-453b-ba82-52fd92a3f341	1000013
1000271	1000004	0	1000654	Y	10000	\N	25000	0	2024-09-19 11:10:15.807439	1000069	2024-09-19 11:10:15.807441	1000069	13db0070-8085-48fb-8d66-27039b13d34f	1000013
1000274	1000004	0	1000657	Y	10000	\N	15000	0	2024-09-19 11:24:57.821511	1000069	2024-09-19 11:24:57.821511	1000069	b1fc0fab-8b62-4fc1-856e-b67756515338	1000013
1000275	1000004	0	1000658	Y	10000	\N	15000	0	2024-09-19 11:24:58.308392	1000069	2024-09-19 11:24:58.308392	1000069	fe342c2e-668f-4ea7-b89f-e749efdb7c91	1000013
1000276	1000004	0	1000659	Y	10000	\N	15000	0	2024-09-19 11:24:58.847381	1000069	2024-09-19 11:24:58.847381	1000069	19016205-5bba-48bc-9156-232d00edf1fc	1000013
1000288	1000004	0	1000671	Y	20000	\N	30000	0	2024-09-19 11:27:16.669915	1000069	2024-09-19 11:27:16.669917	1000069	a9cca459-5261-45de-aa45-69795dc3b3e2	1000013
1000289	1000004	0	1000672	Y	20000	\N	30000	0	2024-09-19 11:27:17.790177	1000069	2024-09-19 11:27:17.79018	1000069	953f9b50-e6dd-4af2-8c80-cb5b96aaa85b	1000013
1000291	1000004	0	1000674	Y	10000	\N	15000	0	2024-09-19 12:50:00.433149	1000069	2024-09-19 12:50:00.433149	1000069	2e3e7ebe-c9ba-48b8-9560-443ea265f826	1000013
1000292	1000004	0	1000675	Y	10000	\N	15000	0	2024-09-19 12:50:00.750986	1000069	2024-09-19 12:50:00.750986	1000069	47a7db96-2a20-4e83-90c2-a833afbba223	1000013
1000293	1000004	0	1000676	Y	10000	\N	15000	0	2024-09-19 12:50:00.840069	1000069	2024-09-19 12:50:00.840069	1000069	fd0415fa-6521-4f88-b88d-a8aa15986334	1000013
1000294	1000004	0	1000677	Y	20000	\N	30000	0	2024-09-19 14:29:54.871216	1000069	2024-09-19 14:29:54.871217	1000069	fc3c4894-3a0c-4530-80cb-31112fd60287	1000013
1000295	1000004	0	1000678	Y	20000	\N	30000	0	2024-09-19 14:29:56.142983	1000069	2024-09-19 14:29:56.142985	1000069	8740f154-c569-49ae-aa5c-196957a861bf	1000013
1000296	1000004	0	1000679	Y	20000	\N	30000	0	2024-09-19 14:29:57.303408	1000069	2024-09-19 14:29:57.303409	1000069	a4688eda-46b5-45f1-b6d0-fdfa00badc6d	1000013
\.


--
-- TOC entry 5458 (class 0 OID 390225)
-- Dependencies: 317
-- Data for Name: d_print_report; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_print_report (d_print_report_id, d_tenant_id, report_type, report_source, created, created_by, updated, updated_by, d_print_report_uu, is_default, is_active) FROM stdin;
1000015	1000004	VA	aaaa	2024-08-13 17:10:14.732549	1000069	2024-08-13 17:10:14.732549	1000069	9cf6f4ec-9c9d-4d94-8970-502458d805e6	Y	Y
1000016	1000004	VA	aaaa	2024-08-13 17:10:16.707537	1000069	2024-08-13 17:10:16.707537	1000069	a77ae61c-a60f-4485-878f-2094bd0ec37f	N	Y
1000017	1000004	VAaxxx	aaaa	2024-08-13 17:10:22.666691	1000069	2024-08-13 17:11:56.065182	1000069	8332adf1-2800-4559-8499-561e841390d7	N	Y
\.


--
-- TOC entry 5386 (class 0 OID 385767)
-- Dependencies: 245
-- Data for Name: d_product; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_product (d_product_id, d_org_id, d_tenant_id, created, created_by, updated, updated_by, is_active, d_product_category_id, code, name, qrcode, saleprice, costprice, d_uom_id, on_hand, is_purchased, d_image_id, product_type, attribute1, attribute2, attribute3, attribute4, attribute5, attribute6, attribute7, attribute8, attribute9, attribute10, description, d_product_uu, is_topping, min_on_hand, max_on_hand, d_tax_id, group_type, d_product_parent_id, d_locator_id, qty_conversion, weight, erp_product_id, brand, qty) FROM stdin;
1000109	0	1000004	2024-08-04 12:15:19.426606	1000069	2024-09-19 07:41:31.710341	1000069	Y	1000013	PR000012	Tiết canh thỏ	**	20050.00	50.00	1000012	20.00	N	1000206	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	4c6195c5-68d9-461f-b404-c1c021e611e7	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000434	0	1000004	2024-09-05 09:22:46.798956	1000069	2024-09-05 09:22:46.798957	1000069	Y	1000017	\N	Banh cuon Tay Son B-D-M	**	200000.00	100000.00	1000014	200.00	Y	1000292	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	5e75b03d-4709-4c5f-8d45-98ab098dda96	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000674	0	1000004	2024-09-19 12:50:00.400034	1000069	2024-09-19 12:50:00.400034	1000069	Y	1000025	PROD100021651	Cafe đen	**	15000.00	10000.00	1000011	20.00	N	\N	\N	size	\N	\N	\N	\N	\N	\N	\N	\N	\N		92b3325e-64a9-48f6-98e8-874628526dc6	\N	0	100	\N	\N	\N	\N	\N	\N	\N	\N	\N
1000675	0	1000004	2024-09-19 12:50:00.71462	1000069	2024-09-19 12:50:00.71462	1000069	Y	1000025	PROD1000675	Cafe đen M	**	15000.00	10000.00	1000011	20.00	\N	\N	\N	\N	M	\N	\N	\N	\N	\N	\N	\N	\N	exampleDescription	45b6bcda-3f97-4ef6-b136-a69d85d11080	Y	0	100	\N	\N	1000674	\N	\N	\N	\N	\N	\N
1000676	0	1000004	2024-09-19 12:50:00.815666	1000069	2024-09-19 12:50:00.815666	1000069	Y	1000025	PROD1000676	Cafe đen S	**	15000.00	10000.00	1000011	20.00	\N	\N	\N	\N	S	\N	\N	\N	\N	\N	\N	\N	\N	exampleDescription	10e182c9-19c0-41e0-be38-6e05b4c87c35	Y	0	100	\N	\N	1000674	\N	\N	\N	\N	\N	\N
1000574	1000016	1000009	2024-09-11 18:14:23.700732	1000071	2024-09-13 16:21:57.805917	1000071	Y	1000033	PROD1000573	Thép hộp mạ kẽm Z080: 14mmx14mmx1.40mmx6.0m	**	123000.00	32000.00	1000017	607.00	Y	1000340	\N		\N	\N	\N	\N	\N	\N	\N	\N	\N		350b0b04-9af6-4360-888b-0ef5a0607ce2	N	0	0	\N	RGD	0	\N	\N	0	\N	VTS	0
1000443	0	1000004	2024-09-05 09:22:46.825557	1000069	2024-09-05 09:22:46.825558	1000069	Y	1000017	\N	Banh cuon Tay Son B-E-S	**	200000.00	100000.00	1000014	200.00	Y	1000295	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	f7b05b9e-4598-4bb3-9026-aaa212b9147a	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000452	0	1000004	2024-09-05 09:22:46.854447	1000069	2024-09-05 09:22:46.854447	1000069	Y	1000017	\N	Banh cuon Tay Son C-D-M	**	200000.00	100000.00	1000014	200.00	Y	1000298	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	e2c4d7d1-3943-4284-b312-7338b19540dd	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000626	0	1000004	2024-09-18 21:53:32.397699	1000069	2024-09-18 21:53:32.397702	1000069	Y	1000025	PROD1000626	Cafe đen M	**	15000.00	10000.00	1000011	20.00	\N	\N	\N	\N	M	\N	\N	\N	\N	\N	\N	\N	\N	exampleDescription	74773aac-84b2-437d-bc68-8bdcfbdc7e2a	Y	0	100	\N	\N	1000625	\N	\N	\N	\N	\N	\N
1000431	0	1000004	2024-09-05 09:22:46.788439	1000069	2024-09-05 09:22:46.78844	1000069	Y	1000017	\N	Banh cuon Tay Son A-F-S	**	200000.00	100000.00	1000014	200.00	Y	1000291	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	e19c2d5c-0dd0-48a5-ac83-d007079790f7	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000383	0	1000004	2024-09-04 09:23:01.426342	1000069	2024-09-04 02:00:46.48313	1000069	Y	1000020	\N	Lau thai chua cay	**	120000.00	100000.00	1000014	10.00	Y	\N	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N		ef2a1c36-f394-4fa0-ae8f-ac45cc22c4e0	N	1	10	1000003	PRD 	0	\N	\N	\N	\N	\N	0
1000577	1000016	1000009	2024-09-11 18:15:54.633882	1000071	2024-09-12 15:52:02.02603	1000071	Y	1000033	PROD1000576	Thép hộp mạ kẽm Z080: 14mmx14mmx1.20mmx6.0m	**	110000.00	45000.00	1000017	1223.00	Y	1000341	\N		\N	\N	\N	\N	\N	\N	\N	\N	\N		4a63f693-9b9c-47bf-8bff-24db5585ecc5	N	0	0	\N	RGD	0	\N	\N	0	\N	VTS	0
1000562	1000016	1000009	2024-09-11 17:57:24.636877	1000071	2024-09-11 17:57:24.636877	1000071	Y	1000032	PROD1000561	Tôn lạnh AZ100 phủ AF: 0.50mmx1200mm G550	**	56000.00	40000.00	1000016	1200.00	Y	1000336	\N		\N	\N	\N	\N	\N	\N	\N	\N	\N		f70a88c8-2a2d-4057-828a-718d84462041	N	0	0	\N	RGD	0	\N	\N	0	\N	VTS	0
1000568	1000016	1000009	2024-09-11 18:10:24.89649	1000071	2024-09-11 18:10:24.896491	1000071	Y	1000033	PROD1000567	Thép hộp mạ kẽm Z080: 13mmx26mmx1.00mmx6.0m	**	120000.00	40000.00	1000017	900.00	Y	1000338	\N		\N	\N	\N	\N	\N	\N	\N	\N	\N		73c8dc99-ac07-4fc8-8111-06577e0aa0c9	N	0	0	\N	RGD	0	\N	\N	0	\N	VTS	0
1000565	1000016	1000009	2024-09-11 17:59:37.357366	1000071	2024-09-12 18:13:12.061294	1000071	Y	1000032	PROD1000564	Tôn lạnh AZ100 phủ AF: 0.45mmx1000mm G550	**	45000.00	30000.00	1000016	1309.00	Y	1000337	\N		\N	\N	\N	\N	\N	\N	\N	\N	\N		7827fe15-dddc-403a-b113-f023f35e87b0	N	0	0	\N	RGD	0	\N	\N	0	\N	VTS	0
1000644	0	1000004	2024-09-18 23:36:56.480114	1000069	2024-09-18 23:36:56.480115	1000069	Y	1000017	PROD1000644	Banh xeo	**	20000.00	15000.00	1000012	10.00	Y	1000382	FOD		\N	\N	\N	\N	\N	\N	\N	\N	\N		30ebbb2d-97a7-41aa-aed1-fbd808ca2a6e	N	1	10	\N	RGD	0	\N	\N	0	\N	\N	\N
1000654	0	1000004	2024-09-19 11:10:15.780118	1000069	2024-09-19 11:10:15.780121	1000069	Y	1000020	PROD10000052	Pho 2 to Gia Lai	**	25000.00	10000.00	1000012	0.00	Y	1000392	FOD		\N	\N	\N	\N	\N	\N	\N	\N	\N		70b09a52-ab47-41e2-83cc-26e7d48e477d	N	0	0	\N	SVC	0	\N	\N	10	\N	\N	\N
1000677	0	1000004	2024-09-19 14:29:54.844511	1000069	2024-09-19 14:29:54.844513	1000069	Y	1000017	PROD122453	Pho tron	**	30000.00	20000.00	1000012	10.00	Y	1000398	FOD	Size2	\N	\N	\N	\N	\N	\N	\N	\N	\N		b4a6bd88-dc84-41b0-9abd-a2bb253895fb	N	0	10	\N	PRD 	\N	\N	\N	10	\N	\N	\N
1000395	0	1000004	2024-09-04 09:47:43.967438	1000069	2024-09-04 15:15:05.188638	1000069	Y	1000020	\N	Banh trung nuong A	**	120000.00	100000.00	1000014	10.00	Y	\N	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh nong va thom	2b3cdaaf-7455-430a-9394-921d6d660bc7	Y	1	10	1000003	PRD 	0	\N	1	\N	\N	\N	0
1000608	0	1000004	2024-09-16 13:43:23.918403	1000069	2024-09-16 13:43:23.918403	1000069	Y	1000016	\N	Combo Gà Quay	**	100.00	50.00	1000012	20.00	N	1000350	\N				exampleAttribute4	exampleAttribute5	exampleAttribute6	exampleAttribute7	exampleAttribute8	exampleAttribute9	exampleAttribute10	exampleDescription	2e93c1b3-299b-4300-ab50-678b3ff4b0bc	Y	0	100	\N	\N	\N	\N	\N	\N	\N	\N	\N
1000392	0	1000004	2024-09-04 09:47:43.877436	1000069	2024-09-04 15:15:05.1858	1000069	Y	1000020	\N	Banh trung nuong	**	120000.00	100000.00	1000014	10.00	Y	\N	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh nong va thom	e2a14bc9-bacc-4c6f-9d84-c7d299c0c861	N	1	10	1000003	PRD 	0	\N	\N	\N	\N	\N	0
1000623	0	1000004	2024-09-18 21:18:35.50404	1000069	2024-09-18 21:18:35.50404	1000069	Y	1000025	PROD1000623	Cafe đen S	**	15000.00	10000.00	1000011	20.00	\N	\N	\N	\N	S	\N	\N	\N	\N	\N	\N	\N	\N	exampleDescription	1347c47d-941b-485e-bd98-e82a59268bab	Y	0	100	\N	\N	1000621	\N	\N	\N	\N	\N	\N
1000380	0	1000004	2024-09-03 22:22:20.751573	1000069	2024-09-04 15:15:05.17864	1000069	Y	1000021	\N	Lau ga la giang S	**	120000.00	100000.00	1000012	10.00	Y	\N	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Mota chi tiet	36c9678f-e64b-4b9f-9c8a-d273e06c6ef8	Y	1	10	1000003	PRD 	0	\N	1	\N	\N	\N	0
1000130	0	1000004	2024-08-07 09:12:46.598136	1000069	2024-09-04 02:00:45.639771	1000069	Y	1000016	PR000019	Gà nướng tiêu xanh	**	20050.00	50.00	1000013	20.00	N	1000227	\N	\N	\N	\N	exampleAttribute4	exampleAttribute5	exampleAttribute6	exampleAttribute7	exampleAttribute8	exampleAttribute9	exampleAttribute10	exampleDescription	fc25581f-e156-41a2-a017-8f43295ccd1e	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000678	0	1000004	2024-09-19 14:29:56.12086	1000069	2024-09-19 14:29:56.120861	1000069	Y	1000017	PROD1000678	Pho tron A	**	30000.00	20000.00	1000012	10.00	Y	1000399	FOD	Size2	A	\N	\N	\N	\N	\N	\N	\N	\N		c142cfe4-a0af-4e31-a210-eaf440d78794	Y	0	10	\N	PRD 	\N	\N	1	10	\N	\N	\N
1000679	0	1000004	2024-09-19 14:29:57.288213	1000069	2024-09-19 14:29:57.288215	1000069	Y	1000017	PROD1000679	Pho tron S	**	30000.00	20000.00	1000012	10.00	Y	1000400	FOD	Size2	S	\N	\N	\N	\N	\N	\N	\N	\N		5696ee5c-c88d-4a34-8f34-1933afac9b6f	Y	0	10	\N	PRD 	\N	\N	1	10	\N	\N	\N
1000102	1000016	1000003	2024-08-04 11:18:14.197504	1000068	2024-08-04 11:18:14.197504	1000068	Y	1000010	PR000001	exampleName	exampleQrCode	100.00	50.00	1000003	20.00	N	1000199	\N	exampleAttribute1	exampleAttribute2	exampleAttribute3	\N	\N	\N	\N	\N	\N	\N	\N	c3701ce2-940f-4812-9ace-c21eb4b36044	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000093	1000016	1000003	2024-07-23 23:41:11.000341	1000068	2024-07-23 23:41:11.000341	1000068	Y	1000002	PR000006	Ống nhựa PPR 1 lớp chỉ đỏ 20mmx4.0m	exampleQrCode	100.00	50.00	1000009	20.00	N	\N	\N	exampleAttribute1	exampleAttribute2	exampleAttribute3	\N	\N	\N	\N	\N	\N	\N	\N	52f94da0-b2fd-4517-b283-5fa97917b128	N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000643	0	1000004	2024-09-18 23:24:42.559703	1000069	2024-09-18 23:24:42.559705	1000069	Y	1000025	PROD1000643	Bac xiu	**	200000.00	100000.00	1000014	0.00	Y	1000381	DRK		\N	\N	\N	\N	\N	\N	\N	\N	\N		3cf1177b-f5aa-4d68-ba04-2b797a4c3f1f	N	0	0	\N	PRD	0	\N	\N	10	\N	\N	\N
1000416	0	1000004	2024-09-05 09:22:46.712827	1000069	2024-09-05 09:22:46.712828	1000069	Y	1000017	\N	Banh cuon Tay Son A-D-M	**	200000.00	100000.00	1000014	200.00	Y	1000286	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	13ee051d-784a-40b2-ae50-190dce9bb16b	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000476	0	1000004	2024-09-05 09:29:12.227909	1000069	2024-09-05 09:29:12.227909	1000069	Y	1000017	\N	Cua đồng 🦀🦀🦀 A-Y-Z	**	189000.00	100000.00	1000013	800.00	Y	1000306	FOD	A	Y	Z	\N	\N	\N	\N	\N	\N	\N	Cua dong siu ngon	08c59b50-0823-4e46-a588-a920de6bf982	Y	200	789	1000002	RGD	\N	\N	1	123	\N	\N	0
1000571	1000016	1000009	2024-09-11 18:12:49.98331	1000071	2024-09-12 18:30:33.339122	1000071	Y	1000033	PROD1000570	Thép hộp mạ kẽm Z080: 13mmx26mmx1.10mmx6.0m	**	11000.00	40000.00	1000017	712.00	Y	1000339	\N		\N	\N	\N	\N	\N	\N	\N	\N	\N		c91d0923-1041-4527-9ab6-9bf6e65dabdb	N	0	0	\N	RGD	0	\N	\N	0	\N	VTS	0
1000536	1000016	1000009	2024-09-11 16:32:36.640331	1000071	2024-09-12 15:33:32.488921	1000071	Y	1000032	PROD1000536	Tôn lạnh AZ070 phủ AF: 0.18mmx1200mm G550	**	43000.00	20000.00	1000016	1006.00	Y	1000323	\N		\N	\N	\N	\N	\N	\N	\N	\N	\N		0c5da061-329f-4bb4-8b8f-233bce9294da	N	0	0	\N	RGD	0	\N	\N	0	\N	VTS	0
1000500	0	1000004	2024-09-05 09:32:37.197631	1000069	2024-09-05 09:32:37.197631	1000069	Y	1000023	\N	Banh mi thap cam M-A	**	20000.00	10000.00	1000012	800.00	Y	\N	FOD	Size,Size1	M	A	\N	\N	\N	\N	\N	\N	\N		8691b460-45f1-43c1-975a-0f8d2578a3ba	Y	100	800	1000004	PRD 	\N	\N	1	10	\N	\N	0
1000651	0	1000004	2024-09-19 07:14:07.98347	1000069	2024-09-19 07:14:07.98347	1000069	Y	1000025	PROD1000645	Bac xiu	**	200000.00	100000.00	1000014	0.00	Y	1000389	DRK		\N	\N	\N	\N	\N	\N	\N	\N	\N		0620e9cf-5bf1-4c14-8aad-5e49d3229dc1	N	0	0	\N	PRD	0	\N	\N	10	\N	\N	\N
1000657	0	1000004	2024-09-19 11:24:57.617948	1000069	2024-09-19 11:24:57.617948	1000069	Y	1000025	PROD1000215	Cafe đen	**	15000.00	10000.00	1000011	20.00	N	\N	\N	size	\N	\N	\N	\N	\N	\N	\N	\N	\N		795c5183-cc4a-41c7-8134-c0b565dc0614	\N	0	100	\N	\N	\N	\N	\N	\N	\N	\N	\N
1000658	0	1000004	2024-09-19 11:24:58.198291	1000069	2024-09-19 11:24:58.198291	1000069	Y	1000025	PROD1000658	Cafe đen M	**	15000.00	10000.00	1000011	20.00	\N	\N	\N	\N	M	\N	\N	\N	\N	\N	\N	\N	\N	exampleDescription	521e0b40-4c06-436e-b08f-b965d8b666fb	Y	0	100	\N	\N	1000657	\N	\N	\N	\N	\N	\N
1000659	0	1000004	2024-09-19 11:24:58.757703	1000069	2024-09-19 11:24:58.757703	1000069	Y	1000025	PROD1000659	Cafe đen S	**	15000.00	10000.00	1000011	20.00	\N	\N	\N	\N	S	\N	\N	\N	\N	\N	\N	\N	\N	exampleDescription	550fc274-9244-44d3-9b4b-c0e6d3260cdd	Y	0	100	\N	\N	1000657	\N	\N	\N	\N	\N	\N
1000089	1000016	1000002	2024-07-21 23:09:44.73648	1000037	2024-08-01 12:56:36.778489	1000037	Y	1000007	PR000003	Thép hộp mạ kẽm Z120: 100mmx100mmx1.80mmx	exampleQrCode	100.00	50.00	1000000	20.00	Y	1000186	\N	exampleAttribute1	exampleAttribute2	exampleAttribute3	\N	\N	\N	\N	\N	\N	\N	\N	b382ec68-fe2c-4e4a-a72a-28f72868e75b	N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000088	1000016	1000002	2024-07-21 23:09:18.827491	1000037	2024-07-21 23:09:18.827491	1000037	Y	1000007	PR000004	Thép hộp mạ kẽm Z080: 40mmx40mmx6.0m		100.00	50.00	1000000	20.00	Y	\N	\N	exampleAttribute1	exampleAttribute2	exampleAttribute3	\N	\N	\N	\N	\N	\N	\N	\N	33b2fbc7-8c1f-43c3-bd77-a47bddb18df1	N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000091	1000016	1000002	2024-07-22 16:36:22.370828	1000037	2024-07-22 17:13:57.580367	1000037	Y	1000007	PR000005	Thép hộp mạ kẽm Z080: 40mmx80mmx6.0m	exampleQrCode	100.00	50.00	1000000	20.00	N	\N	\N	exampleAttribute1	exampleAttribute2	exampleAttribute3	\N	\N	\N	\N	\N	\N	\N	\N	21e1d773-d40d-46da-b734-3720820b7ccc	N	\N	\N	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000464	0	1000004	2024-09-05 09:22:46.919642	1000069	2024-09-05 09:22:46.919642	1000069	Y	1000017	\N	Banh cuon Tay Son C-F-M	**	200000.00	100000.00	1000014	200.00	Y	1000302	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	8ed1ff49-d3fd-4c49-b27f-2ab2d73ac554	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000104	0	1000004	2024-08-04 12:06:30.949601	1000069	2024-09-04 02:00:39.303072	1000069	Y	1000011	PR000007	Thăn Bò Cuộn Nấm Kim Châm	**	20051.00	60.00	1000010	20.00	N	1000201	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	3bccdc76-4092-46dd-89d6-ebab4f143b4c	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000413	0	1000004	2024-09-05 09:22:46.590856	1000069	2024-09-05 09:22:46.590858	1000069	Y	1000017	\N	Banh cuon Tay Son	**	200000.00	100000.00	1000014	200.00	Y	1000285	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	1452c4ca-38ad-4564-b54c-030f64e5db81	N	50	198	1000002	PRD 	0	\N	\N	15	\N	\N	0
1000138	0	1000004	2024-08-07 09:14:55.063453	1000069	2024-09-04 15:20:47.992399	1000069	Y	1000016	PR000023	Gà hấp muối xả	**	20050.00	50.00	1000013	20.00	N	1000235	\N	\N	\N	\N	exampleAttribute4	exampleAttribute5	exampleAttribute6	exampleAttribute7	exampleAttribute8	exampleAttribute9	exampleAttribute10	exampleDescription	7b16dfdd-e804-496f-a896-fb8a083a4d17	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000509	0	1000004	2024-09-05 09:32:37.211032	1000069	2024-09-12 15:00:36.677351	1000069	Y	1000023	\N	Banh mi thap cam S-B	**	20000.00	10000.00	1000012	817.00	Y	\N	FOD	Size,Size1	S	B	\N	\N	\N	\N	\N	\N	\N		f2d5d929-1716-4b59-933d-1326cbdbb318	Y	100	800	1000004	PRD 	\N	\N	1	10	\N	\N	0
1000488	0	1000004	2024-09-05 09:29:12.291552	1000069	2024-09-05 09:29:12.291553	1000069	Y	1000017	\N	Cua đồng 🦀🦀🦀 C-Y-Z	**	189000.00	100000.00	1000013	800.00	Y	1000310	FOD	C	Y	Z	\N	\N	\N	\N	\N	\N	\N	Cua dong siu ngon	8588e8ae-8bb3-4141-9d49-598bc517ced6	Y	200	789	1000002	RGD	\N	\N	1	123	\N	\N	0
1000353	0	1000004	2024-08-30 08:36:41.746734	1000069	2024-09-19 00:20:42.84633	1000069	Y	1000025	\N	Cafe đen	**	12000.00	10000.00	1000011	20.00	N	\N	\N	size	\N	\N	\N	\N	\N	\N	\N	\N	\N		32ab03d1-f62f-49e0-8803-41b7e0e32648	\N	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000627	0	1000004	2024-09-18 21:53:32.468252	1000069	2024-09-18 21:53:32.468254	1000069	Y	1000025	PROD1000627	Cafe đen S	**	15000.00	10000.00	1000011	20.00	\N	\N	\N	\N	S	\N	\N	\N	\N	\N	\N	\N	\N	exampleDescription	62084d10-f169-473f-baae-2ecf0eb9e3be	Y	0	100	\N	\N	1000625	\N	\N	\N	\N	\N	\N
1000404	0	1000004	2024-09-04 09:49:27.141238	1000069	2024-09-04 02:00:46.815654	1000069	Y	1000017	\N	Banh cuon binh dinh S	**	30000.00	10000.00	1000012	10.00	Y	1000284	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N		ca46ee62-7156-4756-be82-9379d4e73ef2	Y	1	10	1000003	PRD 	0	\N	1	\N	\N	\N	0
1000134	0	1000004	2024-08-07 09:13:29.518318	1000069	2024-09-04 02:00:45.702478	1000069	Y	1000016	PR000021	Gà nướng nướng ống tre	**	20050.00	50.00	1000013	20.00	N	1000231	\N	\N	\N	\N	exampleAttribute4	exampleAttribute5	exampleAttribute6	exampleAttribute7	exampleAttribute8	exampleAttribute9	exampleAttribute10	exampleDescription	8b030807-95eb-493b-925f-a1b2ccbe6c95	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000485	0	1000004	2024-09-05 09:29:12.275376	1000069	2024-09-05 09:29:12.275377	1000069	Y	1000017	\N	Cua đồng 🦀🦀🦀 C-X-Z	**	189000.00	100000.00	1000013	800.00	Y	1000309	FOD	C	X	Z	\N	\N	\N	\N	\N	\N	\N	Cua dong siu ngon	2b868943-711d-4a2b-ab48-4bc9434ed2f5	Y	200	789	1000002	RGD	\N	\N	1	123	\N	\N	0
1000113	0	1000004	2024-08-04 12:18:29.019488	1000069	2024-09-04 04:34:20.88474	1000069	Y	1000014	PR000015	Thịt, Sườn Dê Nướng Sa Tế	**	20050.00	50.00	1000012	20.00	N	1000210	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	3fb83750-3885-4f70-a526-f4e860a4cf22	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000482	0	1000004	2024-09-05 09:29:12.262778	1000069	2024-09-05 09:29:12.262779	1000069	Y	1000017	\N	Cua đồng 🦀🦀🦀 B-Y-Z	**	189000.00	100000.00	1000013	800.00	Y	1000308	FOD	B	Y	Z	\N	\N	\N	\N	\N	\N	\N	Cua dong siu ngon	a5c4ca7b-5c26-43cc-9daa-c0838d01940a	Y	200	789	1000002	RGD	\N	\N	1	123	\N	\N	0
1000129	0	1000004	2024-08-07 09:12:30.208023	1000069	2024-09-04 02:00:45.607741	1000069	Y	1000016	PR000018	Gà nướng đất sét	**	20050.00	50.00	1000013	20.00	N	1000226	\N	\N	\N	\N	exampleAttribute4	exampleAttribute5	exampleAttribute6	exampleAttribute7	exampleAttribute8	exampleAttribute9	exampleAttribute10	exampleDescription	f68a33dc-60ea-4daa-9406-8ba040a8c357	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000106	0	1000004	2024-08-04 12:09:40.176206	1000069	2024-09-04 04:34:21.036267	1000069	Y	1000012	PR000009	Shochu Tensonkorin Imo 720ml	**	20050.00	50.00	1000011	20.00	N	1000203	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	f21eafbb-2ca7-44b5-9fd5-95ee50aeff7d	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000132	0	1000004	2024-08-07 09:13:10.801467	1000069	2024-09-04 02:00:45.675051	1000069	Y	1000016	PR000020	Gà nướng nướng giấy bạc	**	20050.00	50.00	1000013	20.00	N	1000229	\N	\N	\N	\N	exampleAttribute4	exampleAttribute5	exampleAttribute6	exampleAttribute7	exampleAttribute8	exampleAttribute9	exampleAttribute10	exampleDescription	7f20acff-f45b-48af-be8d-15cc98db86ff	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000389	0	1000004	2024-09-04 09:23:01.512915	1000069	2024-09-04 02:00:46.521554	1000069	Y	1000020	\N	Lau thai chua cay S	**	120000.00	100000.00	1000014	10.00	Y	\N	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N		8d46c1ca-1f6a-41e2-abb5-8208d548d990	Y	1	10	1000003	PRD 	0	\N	1	\N	\N	\N	0
1000356	0	1000004	2024-08-30 08:36:41.806706	1000069	2024-09-18 16:37:22.065913	1000069	Y	1000025	\N	Cafe đen M	**	12000.00	10000.00	1000011	20.00	\N	\N	\N	\N	M	\N	\N	\N	\N	\N	\N	\N	\N	exampleDescription	51221654-0f5a-4146-b5bc-995100a1fa56	Y	0	100	1000003	\N	1000353	\N	\N	\N	\N	\N	0
1000140	0	1000004	2024-08-07 09:36:49.212538	1000069	2024-09-04 15:20:48.009515	1000069	Y	1000016	PR000025	Combo Gà Quay	**	20050.00	50.00	1000012	20.00	N	1000239	\N	\N	\N	\N	exampleAttribute4	exampleAttribute5	exampleAttribute6	exampleAttribute7	exampleAttribute8	exampleAttribute9	exampleAttribute10	exampleDescription	14d30cd4-7f3e-45ec-a963-8d3a2cd6a17e	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000437	0	1000004	2024-09-05 09:22:46.808506	1000069	2024-09-05 09:22:46.808507	1000069	Y	1000017	\N	Banh cuon Tay Son B-D-S	**	200000.00	100000.00	1000014	200.00	Y	1000293	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	e9b04fc3-210f-4309-8c5f-96df37ed3ffd	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000112	0	1000004	2024-08-04 12:15:57.24203	1000069	2024-09-04 04:34:21.120088	1000069	Y	1000013	PR000014	Đùi thỏ	**	20050.00	50.00	1000012	20.00	N	1000209	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	8abae216-39ec-4f24-ac08-de1f66e7e796	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000374	0	1000004	2024-09-02 22:44:57.208017	1000069	2024-09-04 14:13:15.425626	1000069	Y	1000021	\N	Tra sua tran chau duong den S	**	30000.00	10000.00	1000012	1000.00	Y	\N	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N		27426cd7-a255-4b04-90b6-4f14e42e74a5	Y	800	1000	1000003	CBP	0	\N	1	\N	\N	\N	0
1000428	0	1000004	2024-09-05 09:22:46.77719	1000069	2024-09-05 09:22:46.777191	1000069	Y	1000017	\N	Banh cuon Tay Son A-F-M	**	200000.00	100000.00	1000014	200.00	Y	1000290	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	aa2e85a2-fc5a-4fb3-bd4e-cd9dd6f3ef31	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000642	0	1000004	2024-09-18 23:25:57.247664	1000069	2024-09-18 23:25:57.247664	1000069	Y	1000025	PROD1000628	Bac xiu	**	200000.00	100000.00	1000014	0.00	Y	1000380	DRK		\N	\N	\N	\N	\N	\N	\N	\N	\N		75716928-fa0c-4c51-95a9-4f6f9c0d0df5	N	0	0	\N	PRD	0	\N	\N	10	\N	\N	\N
1000621	0	1000004	2024-09-18 21:18:35.252186	1000069	2024-09-18 21:18:35.252186	1000069	Y	1000025	PROD1000591	Cafe đen	**	15000.00	10000.00	1000011	20.00	N	1000363	\N	size	\N	\N	\N	\N	\N	\N	\N	\N	\N		d135dc5c-d467-45c6-828c-37ae6359bafc	\N	0	100	\N	\N	\N	\N	\N	\N	\N	\N	\N
1000461	0	1000004	2024-09-05 09:22:46.908884	1000069	2024-09-05 09:22:46.908884	1000069	Y	1000017	\N	Banh cuon Tay Son C-E-S	**	200000.00	100000.00	1000014	200.00	Y	1000301	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	09c087fa-ea8d-46ac-98bd-cfd941d9efb8	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000470	0	1000004	2024-09-05 09:29:12.100408	1000069	2024-09-05 09:29:12.100408	1000069	Y	1000017	\N	Cua đồng 🦀🦀🦀	**	189000.00	100000.00	1000013	800.00	Y	1000304	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Cua dong siu ngon	60bf7c6c-57b4-4e7e-bb35-b8ddc5122ad4	N	200	789	1000002	RGD	0	\N	\N	123	\N	\N	0
1000458	0	1000004	2024-09-05 09:22:46.892741	1000069	2024-09-05 09:22:46.892742	1000069	Y	1000017	\N	Banh cuon Tay Son C-E-M	**	200000.00	100000.00	1000014	200.00	Y	1000300	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	0bcad7ac-5f50-4228-bbc2-901a782a6ff5	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000467	0	1000004	2024-09-05 09:22:46.931828	1000069	2024-09-05 09:22:46.931829	1000069	Y	1000017	\N	Banh cuon Tay Son C-F-S	**	200000.00	100000.00	1000014	200.00	Y	1000303	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	efc4b8f6-adef-4ed3-8ad0-83caa6a4edd8	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000622	0	1000004	2024-09-18 21:18:35.429548	1000069	2024-09-18 21:18:35.429548	1000069	Y	1000025	PROD1000622	Cafe đen M	**	15000.00	10000.00	1000011	20.00	\N	\N	\N	\N	M	\N	\N	\N	\N	\N	\N	\N	\N	exampleDescription	37846aca-747b-48b1-9ab8-1a863d5af174	Y	0	100	\N	\N	1000621	\N	\N	\N	\N	\N	\N
1000359	0	1000004	2024-08-30 08:36:41.885961	1000069	2024-09-19 07:41:31.614729	1000069	Y	1000025	\N	Cafe đen S	**	15000.00	10000.00	1000011	20.00	\N	\N	\N	\N	S	\N	\N	\N	\N	\N	\N	\N	\N	exampleDescription	a201b286-2bbd-4f57-8e39-6fdeb0c761d9	Y	0	100	1000003	\N	1000353	\N	\N	\N	\N	\N	0
1000661	0	1000004	2024-09-19 11:20:59.529273	1000069	2024-09-19 11:20:59.529275	1000069	Y	1000025	PROD10002165	Cafe đen	**	15000.00	10000.00	1000011	20.00	N	\N	\N	size	\N	\N	\N	\N	\N	\N	\N	\N	\N		41643e0d-0318-4b5a-b9b1-0b1156edc33a	\N	0	100	\N	\N	\N	\N	\N	\N	\N	\N	\N
1000662	0	1000004	2024-09-19 11:20:59.580869	1000069	2024-09-19 11:20:59.580872	1000069	Y	1000025	PROD1000662	Cafe đen M	**	15000.00	10000.00	1000011	20.00	\N	\N	\N	\N	M	\N	\N	\N	\N	\N	\N	\N	\N	exampleDescription	8bdd3093-3074-4f93-a15c-30bfe6b4ee3d	Y	0	100	\N	\N	1000661	\N	\N	\N	\N	\N	\N
1000440	0	1000004	2024-09-05 09:22:46.816153	1000069	2024-09-05 09:22:46.816153	1000069	Y	1000017	\N	Banh cuon Tay Son B-E-M	**	200000.00	100000.00	1000014	200.00	Y	1000294	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	ada744a3-0fc0-49e7-938e-7973ebda4444	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000425	0	1000004	2024-09-05 09:22:46.749143	1000069	2024-09-05 09:22:46.749144	1000069	Y	1000017	\N	Banh cuon Tay Son A-E-S	**	200000.00	100000.00	1000014	200.00	Y	1000289	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	d21c1241-1f79-4cfb-ab6c-5d82def957c8	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000139	0	1000004	2024-08-07 09:33:47.26964	1000069	2024-09-12 15:09:19.645743	1000069	Y	1000012	PR000024	Pepsi	**	30000.00	10000.00	1000011	23.00	N	1000236	DRINK	\N	\N	\N	exampleAttribute4	exampleAttribute5	exampleAttribute6	exampleAttribute7	exampleAttribute8	exampleAttribute9	exampleAttribute10	exampleDescription	d2079047-19fd-4417-9221-1a8f9401c567	Y	0	100	1000002	RG	\N	\N	\N	\N	\N	\N	0
1000407	0	1000004	2024-09-04 17:35:06.844185	1000069	2024-09-04 17:35:06.844187	1000069	Y	1000017	\N	Banh mi cha ca	**	200000.00	100000.00	1000012	10.00	Y	\N	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh mi Nha Trang	8160f1d1-f395-4c9d-b277-2f7783fb7101	N	1	10	1000004	PRD 	0	\N	\N	10	\N	\N	0
1000398	0	1000004	2024-09-04 09:47:43.979436	1000069	2024-09-04 15:15:05.191186	1000069	Y	1000020	\N	Banh trung nuong B	**	120000.00	100000.00	1000014	10.00	Y	\N	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh nong va thom	fedaf007-0756-4dde-9401-04d385bb2556	Y	1	10	1000003	PRD 	0	\N	1	\N	\N	\N	0
1000111	0	1000004	2024-08-04 12:15:41.444076	1000069	2024-09-04 15:20:47.98204	1000069	Y	1000013	PR000013	Xào lăn	**	20050.00	50.00	1000012	20.00	N	1000208	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	28024dfd-fa00-45a1-8c7c-c3967c1b2aa3	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000422	0	1000004	2024-09-05 09:22:46.733548	1000069	2024-09-05 09:22:46.733549	1000069	Y	1000017	\N	Banh cuon Tay Son A-E-M	**	200000.00	100000.00	1000014	200.00	Y	1000288	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	1eb6abd3-7dbc-4d1b-8735-47b2c1c2413a	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000386	0	1000004	2024-09-04 09:23:01.505124	1000069	2024-09-04 02:00:46.504816	1000069	Y	1000020	\N	Lau thai chua cay M	**	120000.00	100000.00	1000014	10.00	Y	\N	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N		75d976ed-7502-43df-a98e-5c2ac86becfb	Y	1	10	1000003	PRD 	0	\N	1	\N	\N	\N	0
1000479	0	1000004	2024-09-05 09:29:12.246084	1000069	2024-09-05 09:29:12.246085	1000069	Y	1000017	\N	Cua đồng 🦀🦀🦀 B-X-Z	**	189000.00	100000.00	1000013	800.00	Y	1000307	FOD	B	X	Z	\N	\N	\N	\N	\N	\N	\N	Cua dong siu ngon	817aa70a-d183-45ff-80e9-55250710290c	Y	200	789	1000002	RGD	\N	\N	1	123	\N	\N	0
1000449	0	1000004	2024-09-05 09:22:46.842291	1000069	2024-09-05 09:22:46.842292	1000069	Y	1000017	\N	Banh cuon Tay Son B-F-S	**	200000.00	100000.00	1000014	200.00	Y	1000297	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	c3d8a309-3be6-4fc1-ac6f-306450c4bcea	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000491	0	1000004	2024-09-05 09:32:37.178108	1000069	2024-09-05 09:32:37.178108	1000069	Y	1000023	\N	Banh mi thap cam	**	20000.00	10000.00	1000012	800.00	Y	\N	FOD	Size,Size1	\N	\N	\N	\N	\N	\N	\N	\N	\N		3a10bfad-1852-4514-a0b1-84813ab4ac58	N	100	800	1000004	PRD 	\N	\N	\N	10	\N	\N	0
1000377	0	1000004	2024-09-03 22:22:20.589377	1000069	2024-09-04 14:13:15.436205	1000069	Y	1000021	\N	Lau ga la giang	**	120000.00	100000.00	1000012	10.00	Y	\N	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Mota chi tiet	22cc003a-bfed-42ac-817a-3f8024fc260d	N	1	10	1000003	PRD 	0	\N	\N	\N	\N	\N	0
1000497	0	1000004	2024-09-05 09:32:37.193273	1000069	2024-09-05 09:32:37.193274	1000069	Y	1000023	\N	Banh mi thap cam L-B	**	20000.00	10000.00	1000012	800.00	Y	\N	FOD	Size,Size1	L	B	\N	\N	\N	\N	\N	\N	\N		9dd45f77-d556-40ec-9ef1-9b6d9aba1156	Y	100	800	1000004	PRD 	\N	\N	1	10	\N	\N	0
1000108	0	1000004	2024-08-04 12:10:19.14418	1000069	2024-09-04 04:34:20.996148	1000069	Y	1000012	PR000011	Sake Suishin Kome no Kiwami Junmai 15-16% 720ml	**	20050.00	50.00	1000011	20.00	N	1000205	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	b9d5c078-e89a-43ac-bc34-9b0aa1f43e30	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000494	0	1000004	2024-09-05 09:32:37.188409	1000069	2024-09-05 09:32:37.188409	1000069	Y	1000023	\N	Banh mi thap cam L-A	**	20000.00	10000.00	1000012	800.00	Y	\N	FOD	Size,Size1	L	A	\N	\N	\N	\N	\N	\N	\N		ed559da2-66d2-4fd3-ba95-4fd4864b51ef	Y	100	800	1000004	PRD 	\N	\N	1	10	\N	\N	0
1000103	0	1000004	2024-08-04 12:05:48.822935	1000069	2024-09-04 01:53:41.984398	1000069	Y	1000011	PR000002	Bò Wagyu Nhật A5 Nướng Que	**	70000.00	50000.00	1000010	20.00	N	1000200	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	ba0c87de-1622-47fc-8da4-858be439f83d	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000401	0	1000004	2024-09-04 09:49:27.11932	1000069	2024-09-04 02:00:46.570109	1000069	Y	1000017	\N	Banh cuon binh dinh	**	30000.00	10000.00	1000012	10.00	Y	1000283	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N		b2d4fbd7-5161-4c51-aebc-567ce62c036a	N	1	10	1000003	PRD 	0	\N	\N	\N	\N	\N	0
1000410	0	1000004	2024-09-04 17:35:07.157166	1000069	2024-09-04 17:35:07.157167	1000069	Y	1000017	\N	Banh mi cha ca S	**	200000.00	100000.00	1000012	10.00	Y	\N	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh mi Nha Trang	1ee2c33e-a96c-49b0-aed7-050d45f92166	Y	1	10	1000004	PRD 	0	\N	1	10	\N	\N	0
1000136	0	1000004	2024-08-07 09:13:50.255333	1000069	2024-09-04 02:00:45.541015	1000069	Y	1000016	PR000022	Gà hấp hành	**	20050.00	50.00	1000013	20.00	N	1000233	\N	\N	\N	\N	exampleAttribute4	exampleAttribute5	exampleAttribute6	exampleAttribute7	exampleAttribute8	exampleAttribute9	exampleAttribute10	exampleDescription	0d95ecc4-80a9-469a-a67f-05cc9ae51be3	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000473	0	1000004	2024-09-05 09:29:12.120152	1000069	2024-09-05 09:29:12.120153	1000069	Y	1000017	\N	Cua đồng 🦀🦀🦀 A-X-Z	**	189000.00	100000.00	1000013	800.00	Y	1000305	FOD	A	X	Z	\N	\N	\N	\N	\N	\N	\N	Cua dong siu ngon	0acf121a-6eb3-4ec9-b30d-5a4f16b717bb	Y	200	789	1000002	RGD	\N	\N	1	123	\N	\N	0
1000625	0	1000004	2024-09-18 21:53:32.34744	1000069	2024-09-18 21:53:32.347441	1000069	Y	1000025	PROD1000624	Cafe đen	**	15000.00	10000.00	1000011	20.00	N	1000365	\N	size	\N	\N	\N	\N	\N	\N	\N	\N	\N		6384f8e3-5ac9-4172-b892-a8a1aedd22a6	\N	0	100	\N	\N	\N	\N	\N	\N	\N	\N	\N
1000672	0	1000004	2024-09-19 11:27:17.784007	1000069	2024-09-19 11:27:17.78401	1000069	Y	1000031	PROD1000672	Pho kho gia lai S	**	30000.00	20000.00	1000012	100.00	Y	1000397	FOD	Size	S	\N	\N	\N	\N	\N	\N	\N	\N		1f30aec4-890b-416c-a29a-7701eeeb36fb	Y	1	99	\N	PRD 	\N	\N	1	0	\N	\N	\N
1000503	0	1000004	2024-09-05 09:32:37.201969	1000069	2024-09-19 10:59:29.96714	1000069	Y	1000023	\N	Banh mi thap cam M-B	**	20000.00	10000.00	1000012	800.00	Y	\N	FOD	Size,Size1	M	B	\N	\N	\N	\N	\N	\N	\N		7e02bcda-3c5f-4b15-adf3-003bca334e7e	Y	100	800	1000004	PRD 	\N	\N	1	10	\N	\N	0
1000663	0	1000004	2024-09-19 11:20:59.6402	1000069	2024-09-19 11:20:59.640203	1000069	Y	1000025	PROD1000663	Cafe đen S	**	15000.00	10000.00	1000011	20.00	\N	\N	\N	\N	S	\N	\N	\N	\N	\N	\N	\N	\N	exampleDescription	cabf76b2-f2ae-4110-bd39-8f053c44a563	Y	0	100	\N	\N	1000661	\N	\N	\N	\N	\N	\N
1000107	0	1000004	2024-08-04 12:10:06.39364	1000069	2024-09-19 07:41:31.662524	1000069	Y	1000012	PR000010	Shochu Kan No Ko Mugi 25% 720ml	**	20050.00	50.00	1000011	20.00	N	1000204	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	cb2c1f0b-96a2-4e4a-81d0-a40fe2653ce7	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000670	0	1000004	2024-09-19 11:27:15.488802	1000069	2024-09-19 11:27:15.488806	1000069	Y	1000031	PROD88952	Pho kho gia lai	**	30000.00	20000.00	1000012	100.00	Y	1000395	FOD	Size	\N	\N	\N	\N	\N	\N	\N	\N	\N		4aa2eb1d-fae3-49bf-bb87-8db7171ae661	N	1	99	\N	PRD 	\N	\N	\N	0	\N	\N	\N
1000671	0	1000004	2024-09-19 11:27:16.663056	1000069	2024-09-19 11:27:16.663058	1000069	Y	1000031	PROD1000671	Pho kho gia lai M	**	30000.00	20000.00	1000012	100.00	Y	1000396	FOD	Size	M	\N	\N	\N	\N	\N	\N	\N	\N		387bcf99-fb30-4b66-a758-58b7461b8d3b	Y	1	99	\N	PRD 	\N	\N	1	0	\N	\N	\N
1000506	0	1000004	2024-09-05 09:32:37.206769	1000069	2024-09-19 10:59:29.954054	1000069	Y	1000023	\N	Banh mi thap cam S-A	**	20000.00	10000.00	1000012	803.00	Y	\N	FOD	Size,Size1	S	A	\N	\N	\N	\N	\N	\N	\N		5296c168-740a-4405-84e9-c0171e6485f1	Y	100	800	1000004	PRD 	\N	\N	1	10	\N	\N	0
1000446	0	1000004	2024-09-05 09:22:46.832944	1000069	2024-09-05 09:22:46.832944	1000069	Y	1000017	\N	Banh cuon Tay Son B-F-M	**	200000.00	100000.00	1000014	200.00	Y	1000296	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	6f27b8d1-edb8-43e9-8abe-ff6cb222c01d	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000105	0	1000004	2024-08-04 12:06:46.674131	1000069	2024-09-04 01:53:42.041554	1000069	Y	1000011	PR000008	Bò Wagyu Nhật A5 Áp Chảo Sốt Ponzu	**	20050.00	50.00	1000010	20.00	N	1000202	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	dc98bca8-141f-466d-ac6f-c4bfa2859fca	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000371	0	1000004	2024-09-02 22:44:57.201828	1000069	2024-09-04 14:13:15.422815	1000069	Y	1000021	\N	Tra sua tran chau duong den M	**	30000.00	10000.00	1000012	1000.00	Y	\N	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N		9b8c9c74-0e1c-4255-8574-a5a709dace49	Y	800	1000	1000003	CBP	0	\N	1	\N	\N	\N	0
1000119	0	1000004	2024-08-04 12:21:04.50402	1000069	2024-09-04 14:13:15.368467	1000069	Y	1000014	PR000016	Thịt, Sườn Dê Nướng Nướng Mọi	**	20050.00	50.00	1000012	20.00	N	1000216	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	eb2a4223-8fa3-40f2-a36c-a5f18bbdc649	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000127	0	1000004	2024-08-07 09:12:12.14143	1000069	2024-09-04 02:00:45.574779	1000069	Y	1000016	PR000017	Gà nướng muối ớt	**	20050.00	50.00	1000013	20.00	N	1000224	\N	\N	\N	\N	exampleAttribute4	exampleAttribute5	exampleAttribute6	exampleAttribute7	exampleAttribute8	exampleAttribute9	exampleAttribute10	exampleDescription	fb83378d-df23-4087-a3b8-a13787a448da	Y	0	100	1000003	\N	\N	\N	\N	\N	\N	\N	0
1000419	0	1000004	2024-09-05 09:22:46.722084	1000069	2024-09-05 09:22:46.722085	1000069	Y	1000017	\N	Banh cuon Tay Son A-D-S	**	200000.00	100000.00	1000014	200.00	Y	1000287	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	7f67401d-d9f7-4431-ab26-fce8468c7cd1	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
1000368	0	1000004	2024-09-02 22:44:57.145339	1000069	2024-09-04 14:13:15.419436	1000069	Y	1000021	\N	Tra sua tran chau duong den	**	30000.00	10000.00	1000012	1000.00	Y	\N	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N		2b218196-2473-4c9f-9124-1addde785a82	N	800	1000	1000003	CBP	0	\N	\N	\N	\N	\N	0
1000455	0	1000004	2024-09-05 09:22:46.870501	1000069	2024-09-05 09:22:46.870501	1000069	Y	1000017	\N	Banh cuon Tay Son C-D-S	**	200000.00	100000.00	1000014	200.00	Y	1000299	FOD	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	Banh cuon thom ngon	cb2205c4-4861-4135-978e-9faf99a9dda3	Y	50	198	1000002	PRD 	0	\N	1	15	\N	\N	0
\.


--
-- TOC entry 5387 (class 0 OID 385779)
-- Dependencies: 246
-- Data for Name: d_product_category; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_product_category (d_product_category_id, d_org_id, d_tenant_id, created, created_by, updated, updated_by, is_active, code, name, d_product_category_uu, d_product_category_parent_id, is_summary, qty_product, erp_product_category_id) FROM stdin;
1000007	1000003	1000003	2024-07-12 00:00:00	0	2024-07-12 00:00:00	0	Y	OTMK	Ống thép mạ kẽm	4c6e44c5-c4d9-42c6-836e-09d492ece489	\N	N	\N	\N
1000008	1000001	1000002	2024-07-12 00:00:00	1000037	2024-07-12 00:00:00	1000037	Y	KNN	Tôn lạnh màu	adc1ae75-363a-4408-9849-eb903bfaa81f	\N	N	\N	\N
1000002	1000001	1000002	2024-07-09 00:00:00	0	2024-07-09 00:00:00	0	Y	TMK	Ống nhựa PPR	4b555da7-e067-4c66-a784-97eaf77f0f74	\N	N	\N	\N
1000005	1000001	1000002	2024-07-12 00:00:00	1000037	2024-07-12 00:00:00	1000037	Y	TD	Ống nhựa uPVC	59cd0599-e9b0-4033-89a7-6e3432a4ba4b	\N	N	\N	\N
1000010	1000003	1000003	2024-07-24 00:00:00	1000068	2024-08-03 00:00:00	1000068	Y	THD	Tôn lạnh	cb24b569-d7d0-47fd-9eba-be553f890dc9	\N	N	\N	\N
1000013	1000016	1000004	2024-08-04 00:00:00	1000069	2024-08-04 00:00:00	1000069	Y	100002	Món Thỏ	43915509-2a31-4e7f-b7f6-543aeb7634b2	\N	N	\N	\N
1000012	1000016	1000004	2024-08-04 00:00:00	1000069	2024-08-04 00:00:00	1000069	Y	100001	Đồ Uống	13e8e945-fc38-429f-acf3-8e3ae0ffba10	\N	N	\N	\N
1000014	1000016	1000004	2024-08-04 00:00:00	1000069	2024-08-04 00:00:00	1000069	Y	100003	Món Dê	08c3c58e-89c4-41c4-9d88-d201b0405180	\N	N	\N	\N
1000011	1000016	1000004	2024-08-04 00:00:00	1000069	2024-08-04 00:00:00	1000069	Y	100000	Các món bò	7ecfeaa1-1232-4e6a-a5a1-32eb74d6cde2	\N	N	\N	\N
1000016	1000016	1000004	2024-08-07 00:00:00	1000069	2024-08-07 00:00:00	1000069	Y	100003	Gà quay - Gà rán	09bfaca7-201b-4898-a4b2-e340d9d324af	\N	N	\N	\N
1000019	1000016	1000004	2024-08-26 00:00:00	1000069	2024-08-26 00:00:00	1000069	Y	10000068	Món kem	a779596b-9073-4bbf-a13a-7379c02f0a23	\N	\N	\N	\N
1000018	1000016	1000004	2024-08-14 00:00:00	1000069	2024-08-26 00:00:00	1000069	N	100004	Đồ chay	bcf6a2e1-69fa-4431-9822-6a99c9f067cb	1000017	N	\N	\N
1000022	1000016	1000004	2024-08-26 00:00:00	1000069	2024-08-26 00:00:00	1000069	N	1000087	Món Mỹ	db5c64f6-41d1-4127-9245-2c67a7a1fbc6	1000017	\N	\N	\N
1000020	1000016	1000004	2024-08-26 00:00:00	1000069	2024-08-26 00:00:00	1000069	Y	100004	Đồ nóng	5eb721f6-fb93-4604-bbbf-11c99676a857	\N	\N	\N	\N
1000017	1000016	1000004	2024-08-14 00:00:00	1000069	2024-08-26 00:00:00	1000069	Y	100003	Món Ăn	459427d6-506c-4079-b0f7-f643a5908f71	\N	N	\N	\N
1000023	1000016	1000004	2024-08-26 00:00:00	1000069	2024-08-26 00:00:00	1000069	N	1239856	Món Ý	f16ff30c-e80d-4409-98dd-6ce4b501dc9f	1000017	\N	\N	\N
1000021	1000016	1000004	2024-08-26 00:00:00	1000069	2024-08-26 00:00:00	1000069	N	1050002	Món nóng	7b320d76-689b-4238-a23e-bd7faf7e6c3e	1000017	\N	\N	\N
1000025	1000016	1000004	2024-08-30 08:35:46.779116	1000069	2024-08-30 08:35:46.779116	1000069	Y	\N	Cafe	57a7e69b-f455-4718-8dd4-9d15a2442f25	\N	\N	\N	\N
1000028	1000016	1000004	2024-09-11 14:42:58.649815	1000069	2024-09-11 14:42:58.649817	1000069	Y	\N	Món Châu Phi	223752d3-438c-49be-adcd-aa71b53a6d29	\N	\N	\N	\N
1000029	1000016	1000004	2024-09-11 14:43:43.572896	1000069	2024-09-11 14:43:43.572899	1000069	Y	\N	Món Châu Phi 1	77bcf0b7-c74e-4382-84fb-7202b11cf124	1000028	\N	\N	\N
1000030	1000016	1000004	2024-09-11 14:44:28.200734	1000069	2024-09-11 14:44:28.200738	1000069	Y	\N	Món Châu Phi 01	a7351cb8-3c5b-4d5f-af43-af2bbe75477e	1000029	\N	\N	\N
1000032	1000022	1000009	2024-09-11 15:05:26.260591	1000071	2024-09-11 15:05:26.260593	1000071	Y	1000001	Tôn lạnh	50fef776-2066-4da8-927e-56f2d885de79	\N	\N	\N	\N
1000033	1000022	1000009	2024-09-11 15:15:24.381795	1000071	2024-09-11 15:15:24.381797	1000071	Y	1000002	Thép hộp	55a88fa5-19d2-4034-8018-3c59d8d245a4	\N	\N	\N	\N
1000034	1000022	1000009	2024-09-11 15:15:38.952881	1000071	2024-09-11 15:15:38.952883	1000071	Y	1000003	Thép V	97451d6f-c053-4555-b19e-46d765669942	\N	\N	\N	\N
1000031	1000016	1000004	2024-09-11 14:47:55.168811	1000069	2024-09-11 14:47:55.168814	1000069	Y	\N	Món ngon Châu Thái Bình Dương	4c3c5731-0e59-4ee8-a102-4d826af5a6ca	\N	\N	\N	\N
\.


--
-- TOC entry 5388 (class 0 OID 385787)
-- Dependencies: 247
-- Data for Name: d_product_combo; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_product_combo (d_product_combo_id, d_org_id, d_tenant_id, created, created_by, updated, updated_by, d_product_id, d_product_component_id, description, d_product_combo_uu, is_item, is_active, qty) FROM stdin;
1000009	0	1000004	2024-09-05 09:29:12.173001	1000069	2024-09-05 09:29:12.173001	1000069	1000473	1000136	\N	3594fe53-3584-4284-9062-3598a4c3c368	Y	Y	1
1000010	0	1000004	2024-09-05 09:29:12.174811	1000069	2024-09-05 09:29:12.174811	1000069	1000473	1000139	\N	33fb1f15-94cf-4919-a4c6-21326c40708c	N	Y	\N
1000011	0	1000004	2024-09-05 09:29:12.229968	1000069	2024-09-05 09:29:12.229968	1000069	1000476	1000136	\N	1e76a182-fedd-4934-80df-ddf090f5b7d5	Y	Y	1
1000012	0	1000004	2024-09-05 09:29:12.230608	1000069	2024-09-05 09:29:12.230608	1000069	1000476	1000139	\N	d5b22fba-e472-40fc-91e8-f79c38dbcaf6	N	Y	\N
1000013	0	1000004	2024-09-05 09:29:12.248149	1000069	2024-09-05 09:29:12.248149	1000069	1000479	1000136	\N	58f527f0-5a1f-4310-b96b-cb972cc28feb	Y	Y	1
1000014	0	1000004	2024-09-05 09:29:12.248811	1000069	2024-09-05 09:29:12.248811	1000069	1000479	1000139	\N	aa6826c9-29de-4dcf-8b0b-7103a2b96b0c	N	Y	\N
1000015	0	1000004	2024-09-05 09:29:12.264747	1000069	2024-09-05 09:29:12.264748	1000069	1000482	1000136	\N	13f52a4d-5a78-4bbe-ba0f-7ae614a45cc1	Y	Y	1
1000016	0	1000004	2024-09-05 09:29:12.265614	1000069	2024-09-05 09:29:12.265614	1000069	1000482	1000139	\N	4b65a9a0-a8b1-4727-8065-00af67eb3c9c	N	Y	\N
1000017	0	1000004	2024-09-05 09:29:12.277187	1000069	2024-09-05 09:29:12.277187	1000069	1000485	1000136	\N	786fd678-be2a-45d7-8fd7-f96f6c16d28c	Y	Y	1
1000018	0	1000004	2024-09-05 09:29:12.277783	1000069	2024-09-05 09:29:12.277783	1000069	1000485	1000139	\N	308388f9-8967-49d1-adfe-0c2762ab417b	N	Y	\N
1000019	0	1000004	2024-09-05 09:29:12.293814	1000069	2024-09-05 09:29:12.293814	1000069	1000488	1000136	\N	43bbbadb-759f-4aa0-a121-11cf5034edfd	Y	Y	1
1000020	0	1000004	2024-09-05 09:29:12.294394	1000069	2024-09-05 09:29:12.294395	1000069	1000488	1000139	\N	d229d748-4384-429a-9233-ac988a335049	N	Y	\N
1000005	0	1000004	2024-08-07 09:38:07.440257	1000069	2024-08-07 09:38:07.440258	1000069	1000140	1000134	\N	a6af6846-5ada-47b8-873d-87108b5e9c62	Y	Y	\N
1000006	0	1000004	2024-08-07 09:39:07.501267	1000069	2024-08-07 09:39:07.501268	1000069	1000140	1000139	\N	22a60488-8c78-49dc-9b4e-7922ba573aa4	Y	Y	\N
\.


--
-- TOC entry 5484 (class 0 OID 393046)
-- Dependencies: 343
-- Data for Name: d_product_location; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_product_location (d_product_location_id, d_tenant_id, d_org_id, d_warehouse_id, d_pos_terminal_id, d_product_id, erp_product_location_id, created, created_by, updated, updated_by, d_product_location_uu, is_active, is_default) FROM stdin;
\.


--
-- TOC entry 5500 (class 0 OID 393403)
-- Dependencies: 359
-- Data for Name: d_production; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_production (d_production_id, d_tenant_id, d_org_id, d_product_id, d_doctype_id, documentno, name, movement_date, production_qty, documentstatus, description, created, created_by, updated, updated_by, d_production_uu, is_active, is_processed, is_sync_erp, erp_production_id, d_warehouse_id, d_locator_id) FROM stdin;
\.


--
-- TOC entry 5502 (class 0 OID 393431)
-- Dependencies: 361
-- Data for Name: d_productionline; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_productionline (d_productionline_id, d_production_id, d_tenant_id, d_org_id, lineno, d_product_id, is_end_product, planned_qty, description, created, created_by, updated, updated_by, d_productionline_uu, is_active) FROM stdin;
\.


--
-- TOC entry 5516 (class 0 OID 394286)
-- Dependencies: 381
-- Data for Name: d_purchase_order; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_purchase_order (d_purchase_order_id, d_tenant_id, d_org_id, d_doctype_id, d_user_id, d_vendor_id, documentno, order_status, order_date, total_amount, description, created, created_by, updated, updated_by, d_purchase_order_uu, is_active, d_warehouse_id) FROM stdin;
1000005	1000004	1000016	1000005	\N	1000085	PO1000004	DRA	2024-09-11 17:15:09.151058	10000	Nhap hang	2024-09-11 17:15:09.245721	1000069	2024-09-11 17:15:09.245721	1000069	0790fca2-3a03-456a-8f60-6d73b8518507	Y	\N
1000007	1000004	1000016	1000005	1000042	1000085	PO1000006	DRA	2024-09-11 17:15:09.151058	10000	Nhap hang	2024-09-11 17:16:40.405613	1000069	2024-09-11 17:16:40.405613	1000069	bfa7b2a6-1554-4683-bbf4-e1d272e4a359	Y	\N
1000013	1000004	1000016	1000005	1000042	1000085	PO1000012	DRA	2024-09-11 17:33:22.239484	10000	Nhap hang	2024-09-11 17:33:22.305044	1000069	2024-09-11 17:33:22.305044	1000069	c0107e4d-b5ff-4143-904d-bd442c5dd3be	Y	\N
1000015	1000004	1000016	1000005	1000042	1000085	PO1000014	DRA	2024-09-11 17:33:22.239484	10000	Nhap hang	2024-09-11 17:33:50.19666	1000069	2024-09-11 17:33:50.19666	1000069	65a97d02-5b5e-4a85-8e62-03657460610e	Y	\N
1000017	1000004	1000016	1000005	1000042	1000085	PO1000016	DRA	2024-09-12 14:39:49.825669	10000	Nhap hang	2024-09-12 14:39:49.900086	1000069	2024-09-12 14:39:49.900089	1000069	586f08b0-f625-471c-95cd-2a385fcb03bd	Y	\N
1000023	1000004	1000016	1000005	1000069	1000081	PO1000022	DRA	2024-09-12 14:39:49.825669	60000		2024-09-12 15:00:36.605168	1000069	2024-09-12 15:00:36.605169	1000069	d22e5dc8-5bd9-4569-a9ab-b65a822887de	Y	\N
1000025	1000004	1000016	1000005	1000069	1000081	PO1000024	DRA	2024-09-12 14:39:49.825669	30000		2024-09-12 15:05:13.24396	1000069	2024-09-12 15:05:13.243961	1000069	79b662ec-5cdf-4b76-af20-d902a525447e	Y	\N
1000027	1000004	1000016	1000005	1000069	1000066	PO1000026	DRA	2024-09-12 14:39:49.825669	30000		2024-09-12 15:09:19.628261	1000069	2024-09-12 15:09:19.628262	1000069	ec0217c3-34c0-423f-b164-a3a675c344c4	Y	\N
1000029	1000009	1000022	1000005	1000072	1000087	PO1000028	DRA	2024-09-12 14:39:49.825669	90000		2024-09-12 15:21:45.208159	1000071	2024-09-12 15:21:45.20816	1000071	facefd2b-4e2a-40f3-b3de-0a3cd4dae7e3	Y	\N
1000031	1000009	1000022	1000005	1000073	1000087	PO1000030	DRA	2024-09-12 14:39:49.825669	115000		2024-09-12 15:22:19.734026	1000071	2024-09-12 15:22:19.734026	1000071	22dd6711-ec5c-4548-8308-e99e5a1e7953	Y	\N
1000033	1000009	1000022	1000005	1000073	1000088	PO1000032	DRA	2024-09-12 14:39:49.825669	45000		2024-09-12 15:26:01.561105	1000071	2024-09-12 15:26:01.561106	1000071	6967b77d-545d-4538-af93-ef39720f5796	Y	\N
1000037	1000009	1000022	1000005	1000073	1000088	PO1000036	DRA	2024-09-12 14:39:49.825669	90000		2024-09-12 15:43:35.082683	1000071	2024-09-12 15:43:35.082684	1000071	0fdd95af-8636-495d-b7db-4b8079f743f2	Y	\N
1000041	1000009	1000022	1000005	1000073	1000087	PO1000040	COM	2024-09-12 14:39:49.825669	0		2024-09-12 15:52:01.991621	1000071	2024-09-12 17:03:16.318159	1000071	020f51b3-e6ce-4789-ac61-e67c5bb57f7d	Y	\N
1000043	1000009	1000022	1000005	1000073	1000088	PO1000042	COM	2024-09-12 16:47:23.388742	160000		2024-09-12 16:47:23.459007	1000071	2024-09-12 17:31:07.841449	1000071	ce58a560-6170-45b0-9a64-1f464b05b672	Y	\N
1000045	1000009	1000022	1000005	1000073	1000088	PO1000044	DRA	2024-09-12 17:48:57.384467	60000		2024-09-12 17:48:57.419523	1000071	2024-09-12 17:48:57.419523	1000071	c2d64237-ae31-4eff-85b1-b6be5dc84108	Y	1000027
1000035	1000009	1000022	1000005	1000073	1000087	PO1000034	COM	2024-09-12 14:39:49.825669	0		2024-09-12 15:33:32.446874	1000071	2024-09-12 18:13:12.07385	1000071	9a8f5d39-0bc4-4565-9bd5-f6891291b4d1	Y	\N
1000039	1000009	1000022	1000005	1000072	1000088	PO1000038	COM	2024-09-12 14:39:49.825669	450000		2024-09-12 15:44:18.909134	1000071	2024-09-12 18:30:33.375163	1000071	946ff26a-c10b-4fb0-b6a0-727ca7af83ed	Y	1000027
1000047	1000009	1000022	1000005	1000073	1000087	PO1000046	COM	2024-09-12 17:48:57.384467	0		2024-09-13 16:21:57.630178	1000071	2024-09-13 16:21:57.630179	1000071	cf72b5a6-3ebc-4c21-9ab4-2a890a85222a	Y	1000026
\.


--
-- TOC entry 5518 (class 0 OID 394312)
-- Dependencies: 383
-- Data for Name: d_purchase_orderline; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_purchase_orderline (d_purchase_orderline_id, d_purchase_order_id, d_tenant_id, d_org_id, d_product_id, d_uom_id, qty, priceentered, d_tax_id, tax_amount, net_amount, total_amount, description, created, created_by, updated, updated_by, d_purchase_orderline_uu, is_active) FROM stdin;
1000008	1000013	1000004	1000022	1000509	1	1	1	1	1000509	1000509	1000509	Nhap hang	2024-09-11 17:33:22.34562	1000069	2024-09-11 17:33:22.34562	1000069	8d35e987-d659-45b8-890a-2cd7c8b20796	Y
1000009	1000013	1000004	1000022	1000509	1	1	1	1	1000506	1000509	1000509	Nhap hang	2024-09-11 17:33:22.353622	1000069	2024-09-11 17:33:22.353622	1000069	6a2bfedb-fa5b-4c7d-9472-66616be6e6fa	Y
1000010	1000015	1000004	1000022	1000509	1	1	1	1	1000509	1000509	1000509	Nhap hang	2024-09-11 17:33:50.202661	1000069	2024-09-11 17:33:50.202661	1000069	c1a27da8-ef71-433c-965c-0f576f04112f	Y
1000011	1000015	1000004	1000022	1000509	1	1	1	1	1000506	1000509	1000509	Nhap hang	2024-09-11 17:33:50.209795	1000069	2024-09-11 17:33:50.209795	1000069	169c5e80-0cd3-4af2-b327-4ae48b3a45ee	Y
1000012	1000017	1000004	1000022	1000509	1	1	1	1	1000509	1000509	1000509	Nhap hang	2024-09-12 14:39:49.956087	1000069	2024-09-12 14:39:49.956087	1000069	99516fb5-e91c-4cff-9776-c435f46fbf59	Y
1000013	1000017	1000004	1000022	1000509	1	1	1	1	1000506	1000509	1000509	Nhap hang	2024-09-12 14:39:49.958708	1000069	2024-09-12 14:39:49.958708	1000069	ff64c746-a504-4b94-b866-c6f78ad0e854	Y
1000016	1000023	1000004	1000016	1000509	1	3	\N	1	60000	60000	60000	\N	2024-09-12 15:00:36.617065	1000069	2024-09-12 15:00:36.617065	1000069	7a2d9400-2954-4d5b-9f75-f0f228510b4f	Y
1000017	1000025	1000004	1000016	1000506	1	3	\N	1	30000	30000	30000	\N	2024-09-12 15:05:13.246065	1000069	2024-09-12 15:05:13.246065	1000069	2d223127-46a5-4e6c-8519-972c188cc90b	Y
1000018	1000027	1000004	1000016	1000139	1	3	\N	1	30000	30000	30000	\N	2024-09-12 15:09:19.62994	1000069	2024-09-12 15:09:19.629941	1000069	27827c27-7ba9-40e7-92bf-efee7c015bac	Y
1000019	1000029	1000009	1000022	1000565	1	1	\N	1	30000	30000	30000	\N	2024-09-12 15:21:45.211532	1000071	2024-09-12 15:21:45.211532	1000071	51d4ea9e-baef-472c-9059-3d725b122cfb	Y
1000020	1000029	1000009	1000022	1000562	1	1	\N	1	40000	40000	40000	\N	2024-09-12 15:21:45.215615	1000071	2024-09-12 15:21:45.215618	1000071	cfeafddb-67b4-42c7-a6a7-7e5e6b29a28c	Y
1000021	1000029	1000009	1000022	1000536	1	1	\N	1	20000	20000	20000	\N	2024-09-12 15:21:45.21749	1000071	2024-09-12 15:21:45.217491	1000071	4d58b4b1-194d-46c0-bca9-185908eb4df0	Y
1000022	1000031	1000009	1000022	1000577	1	1	\N	1	45000	45000	45000	\N	2024-09-12 15:22:19.73537	1000071	2024-09-12 15:22:19.73537	1000071	c4950393-eb18-41c9-bc7c-9088b95df3d3	Y
1000023	1000031	1000009	1000022	1000571	1	1	\N	1	40000	40000	40000	\N	2024-09-12 15:22:19.736682	1000071	2024-09-12 15:22:19.736682	1000071	224bbf22-179d-4027-a6b5-c98b6db3cba6	Y
1000024	1000031	1000009	1000022	1000565	1	1	\N	1	30000	30000	30000	\N	2024-09-12 15:22:19.737864	1000071	2024-09-12 15:22:19.737865	1000071	ba7ec8b1-b149-4dfb-84a0-36003f24fdb3	Y
1000025	1000033	1000009	1000022	1000577	1	1	\N	1	45000	45000	45000	\N	2024-09-12 15:26:01.569787	1000071	2024-09-12 15:26:01.569788	1000071	1cd0db5f-27e4-4552-8924-f30eefc89848	Y
1000026	1000035	1000009	1000022	1000565	1	1	\N	1	0	0	0	\N	2024-09-12 15:33:32.44915	1000071	2024-09-12 15:33:32.449154	1000071	e0f81013-787a-43a4-bcfc-07d1c7aba80e	Y
1000027	1000035	1000009	1000022	1000562	1	1	\N	1	0	0	0	\N	2024-09-12 15:33:32.452995	1000071	2024-09-12 15:33:32.452996	1000071	f18326e4-97ff-4b2f-b610-6d88417bc945	Y
1000028	1000035	1000009	1000022	1000536	1	1	\N	1	0	0	0	\N	2024-09-12 15:33:32.454318	1000071	2024-09-12 15:33:32.454319	1000071	c37b76f0-2ee3-41ad-85d6-84adafc788c2	Y
1000029	1000037	1000009	1000022	1000577	1	2	\N	1	90000	90000	90000	\N	2024-09-12 15:43:35.08714	1000071	2024-09-12 15:43:35.08714	1000071	9e015537-960d-47f8-ba7d-524f0432054d	Y
1000030	1000039	1000009	1000022	1000577	1	5	\N	1	225000	225000	225000	\N	2024-09-12 15:44:18.910596	1000071	2024-09-12 15:44:18.910596	1000071	e1d4d8f4-961d-44b1-80d6-1c4c6cc71434	Y
1000031	1000041	1000009	1000022	1000565	1	1	\N	1	0	0	0	\N	2024-09-12 15:52:01.994165	1000071	2024-09-12 15:52:01.994165	1000071	46c2f51f-ef3b-4a5f-93c4-bb219fc70b4c	Y
1000032	1000041	1000009	1000022	1000562	1	1	\N	1	0	0	0	\N	2024-09-12 15:52:01.99575	1000071	2024-09-12 15:52:01.99575	1000071	82613150-3818-4030-a0c6-09275562b59e	Y
1000033	1000041	1000009	1000022	1000577	1	1	\N	1	0	0	0	\N	2024-09-12 15:52:01.997094	1000071	2024-09-12 15:52:01.997094	1000071	ff32ae28-f18c-4f6e-bfe1-223d77e8d1ba	Y
1000034	1000043	1000009	1000022	1000574	1	5	\N	1	160000	160000	160000	\N	2024-09-12 16:47:23.488754	1000071	2024-09-12 16:47:23.488754	1000071	496b0191-763a-400c-873d-aa5934240cc6	Y
1000035	1000041	1000009	1000022	1000577	1	1	\N	1	0	0	0	\N	2024-09-12 17:03:16.038686	1000071	2024-09-12 17:03:16.038687	1000071	479800eb-530b-463f-8afe-9c8c9ffbc9ba	Y
1000036	1000041	1000009	1000022	1000562	1	1	\N	1	0	0	0	\N	2024-09-12 17:03:16.042199	1000071	2024-09-12 17:03:16.0422	1000071	1740aa0b-6dc6-4553-842b-2dd6f1e4bafa	Y
1000037	1000041	1000009	1000022	1000565	1	1	\N	1	0	0	0	\N	2024-09-12 17:03:16.044732	1000071	2024-09-12 17:03:16.044733	1000071	9037f777-83ec-42b5-b579-8698c8cb107d	Y
1000038	1000043	1000009	1000022	1000574	1	5	\N	1	160000	160000	160000	\N	2024-09-12 17:31:07.623474	1000071	2024-09-12 17:31:07.623475	1000071	0093b409-e587-4dcb-b6e4-cea63a2d8db2	Y
1000039	1000039	1000009	1000022	1000577	1	5	\N	1	225000	225000	225000	\N	2024-09-12 17:32:16.621704	1000071	2024-09-12 17:32:16.621705	1000071	06c50ac8-0d6e-4fea-b03a-c60cf1bb415b	Y
1000040	1000045	1000009	1000022	1000574	1	3	\N	1	60000	60000	60000	\N	2024-09-12 17:48:57.450669	1000071	2024-09-12 17:48:57.450669	1000071	fca1de03-cff5-4c35-ae84-8203c056a077	Y
1000041	1000035	1000009	1000022	1000536	1	1	\N	1	0	0	0	\N	2024-09-12 18:13:12.027103	1000071	2024-09-12 18:13:12.027104	1000071	db23a102-e3fe-4fe9-bcd6-adec56fd58ed	Y
1000042	1000035	1000009	1000022	1000562	1	1	\N	1	0	0	0	\N	2024-09-12 18:13:12.029781	1000071	2024-09-12 18:13:12.029781	1000071	e55bfc65-df4e-4fbb-a427-27b777a946a8	Y
1000043	1000035	1000009	1000022	1000565	1	1	\N	1	0	0	0	\N	2024-09-12 18:13:12.031398	1000071	2024-09-12 18:13:12.031398	1000071	167ba02a-b11d-4a60-a1ff-44f975f4eeb9	Y
1000044	1000039	1000009	1000022	1000577	1	5	\N	1	225000	225000	225000	\N	2024-09-12 18:30:33.17823	1000071	2024-09-12 18:30:33.178231	1000071	a50ff9a7-4123-4fd8-a97a-8388204e505e	Y
1000045	1000039	1000009	1000022	1000577	1	5	\N	1	225000	225000	225000	\N	2024-09-12 18:30:33.193661	1000071	2024-09-12 18:30:33.193662	1000071	49b7199a-464f-476c-8b5a-8002ba7a8907	Y
1000046	1000039	1000009	1000022	1000571	1	4	\N	1	0	0	0	\N	2024-09-12 18:30:33.207077	1000071	2024-09-12 18:30:33.207077	1000071	021a21cb-dc9b-4b0f-8fb3-5a156075cb1d	Y
1000047	1000047	1000009	1000022	1000565	1	1	\N	1	0	0	0	\N	2024-09-13 16:21:57.638701	1000071	2024-09-13 16:21:57.638701	1000071	93f12574-401f-4447-874a-2610b49aa90c	Y
1000048	1000047	1000009	1000022	1000574	1	1	\N	1	0	0	0	\N	2024-09-13 16:21:57.648412	1000071	2024-09-13 16:21:57.648412	1000071	78dc85f5-017f-4acc-95f9-0af61bb6d696	Y
\.


--
-- TOC entry 5504 (class 0 OID 393488)
-- Dependencies: 363
-- Data for Name: d_reconciledetail; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_reconciledetail (d_reconciledetail_id, d_tenant_id, d_org_id, is_active, created, created_by, updated, updated_by, d_reconciledetail_uu, cmd, merchant_code, access_code, version, check_sum, error_code, transaction_id, payment_status, return_check_sum, hash_key, merchant_name, terminalid, user_update, payment_amount, qrcode_payment, transaction_no, reference_code, d_bankaccount_id, d_bank_id, accountno, d_customer_id, d_pos_order_id, ftcode) FROM stdin;
1000000	1000004	1000016	Y	2024-09-05 22:44:10.148862	1000069	2024-09-05 22:44:10.148862	1000069	be6935d3-348d-45cb-917d-d6aa950ebcb8	\N	Consumer XYZ	\N	\N	a1b2c3d4e5f6g7h8	00	\N	CO	\N	\N	\N	Terminal 01	\N	150.00	\N	TRX1234567890	1000042	\N	\N	\N	1000085	1000042	FT001
1000001	1000004	1000016	Y	2024-09-05 22:49:46.358424	1000069	2024-09-05 22:49:46.358424	1000069	f634b48b-46e1-47bb-a273-544bc36c74ae	\N	Consumer XYZ	\N	\N	a1b2c3d4e5f6g7h8	00	\N	CO	\N	\N	\N	Terminal 01	\N	150.00	\N	TRX1234567890	1000042	\N	\N	\N	1000085	1000042	FT001
1000002	1000004	1000016	Y	2024-09-06 02:51:12.875625	1000069	2024-09-06 02:51:12.875632	1000069	25f3fd35-37c1-493b-ba4a-bb0e0c3e300a	\N	Consumer XYZ	\N	\N	a1b2c3d4e5f6g7h8	00	\N	CO	\N	\N	\N	Terminal 01	\N	150.00	\N	TRX1234567890	1000042	\N	\N	\N	1000085	1000042	FT001
1000003	0	1000016	Y	2024-09-06 10:14:56.100079	0	2024-09-06 10:14:56.100079	0	e2b21e75-13d9-4b2a-b9cd-78fde9c44709	\N	Consumer XYZ	\N	\N	a1b2c3d4e5f6g7h8	00	\N	CO	\N	\N	\N	Terminal 01	\N	150.00	\N	TRX1234567890	1000042	\N	\N	\N	1000085	1000042	FT001
1000004	0	1000016	Y	2024-09-06 03:23:54.173866	0	2024-09-06 03:23:54.173867	0	f359c7eb-9eba-4184-aab7-2c3c288ce204	\N		\N	\N	f09a480bd756b7ec52f70421a1051177	00	\N	CO	\N	\N	\N	SSGJSC1	\N	2000	\N	Q00033fflu	1000056	\N	\N	\N	1000085	1000056	FT24250380000873
1000005	0	1000016	Y	2024-09-06 03:24:43.646671	0	2024-09-06 03:24:43.646673	0	77c1e9b3-61fc-4121-a94e-7df61eee7204	\N		\N	\N	8adb85c65591f864d1439af598b9e016	00	\N	CO	\N	\N	\N	SSGJSC1	\N	2000	\N	Q00033fgbe	1000062	\N	\N	\N	1000085	1000062	FT24250655150156
1000006	0	1000016	Y	2024-09-08 11:21:10.301447	0	2024-09-08 11:21:10.30145	0	1f4532a1-0941-4ee6-8df2-ccd19169bbe4	\N		\N	\N	e4faef675930707e7b52557e8a198d2b	00	\N	CO	\N	\N	\N	SSGJSC1	\N	2000	\N	Q00034km12	1000198	\N	\N	\N	1000085	1000198	FT24253014104808
1000008	0	1000016	Y	2024-09-09 00:32:07.798092	0	2024-09-09 00:32:07.798092	0	801ecaa8-f05a-44af-8a6f-8bc54144e389	\N		\N	\N	a48a06411b47755f4bdb699d62109e3f	00	\N	CO	\N	\N	\N	SSGJSC1	\N	2000	00020101021238570010A000000727012700069704220113VQRQ00034pahe0208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM63042DFC	Q00033f09o	1000204	1000001	1000001	\N	1000085	1000204	FT24250184370364
1000009	0	1000016	Y	2024-09-09 00:38:21.026934	0	2024-09-09 00:38:21.026934	0	b86200df-5590-42d8-81c8-c7e7751172c0	\N		\N	\N	a48a06411b47755f4bdb699d62109e3f	00	\N	CO	\N	\N	\N	SSGJSC1	\N	2000	00020101021238570010A000000727012700069704220113VQRQ00034pahe0208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM63042DFC	Q00033f09o	1000204	1000001	1000001	\N	1000085	1000204	FT24250184370364
1000010	0	1000016	Y	2024-09-09 00:46:55.764515	0	2024-09-09 00:46:55.764515	0	14d4fe0f-2259-4f3f-aa5c-b70d4781e938	\N		\N	\N	a48a06411b47755f4bdb699d62109e3f	00	\N	CO	\N	\N	\N	SSGJSC1	\N	2000	00020101021238570010A000000727012700069704220113VQRQ00034pahe0208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM63042DFC	Q00033f09o	1000204	1000001	1000001	\N	1000085	1000204	FT24250184370364
1000011	1000004	1000016	Y	2024-09-09 00:50:02.34497	0	2024-09-09 00:50:02.34497	0	f6f55dfd-a76d-400f-a7c7-d9114a8d3f76	\N		\N	\N	a48a06411b47755f4bdb699d62109e3f	00	\N	CO	\N	\N	\N	SSGJSC1	\N	2000	00020101021238570010A000000727012700069704220113VQRQ00034pahe0208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM63042DFC	Q00033f09o	1000204	1000001	1000001	\N	1000085	1000204	FT24250184370364
1000012	1000004	1000016	Y	2024-09-09 00:50:58.387713	0	2024-09-09 00:50:58.387713	0	2ce4c39d-fa1a-4379-819c-b618299cd872	\N		\N	\N	a48a06411b47755f4bdb699d62109e3f	00	\N	CO	\N	\N	\N	SSGJSC1	\N	2000	00020101021238570010A000000727012700069704220113VQRQ00034pahe0208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM63042DFC	Q00033f09o	1000204	1000001	1000001	\N	1000085	1000204	FT24250184370364
1000016	1000004	1000016	Y	2024-09-18 07:02:10.937636	0	2024-09-18 07:02:10.937637	0	dbc1d85c-2866-44e4-a2f1-be36064683ad	\N	\N	\N	\N	e2fdd126a22472f2e3e760340ea41702	00	\N	CO	\N	\N	\N	SSGJSC1	\N	2000	00020101021238570010A000000727012700069704220113VQRQ000397cki0208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM63048B2E	FT24262100200227	1000489	\N	\N	\N	\N	1000489	\N
1000017	1000004	1000016	Y	2024-09-18 07:18:08.394494	0	2024-09-18 07:18:08.394532	0	b601f6a7-0d22-48b6-8d47-92fbf20a29d6	\N	\N	\N	\N	e2fdd126a22472f2e3e760340ea41702	00	\N	CO	\N	\N	\N	SSGJSC1	\N	2000	00020101021238570010A000000727012700069704220113VQRQ000397cki0208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM63048B2E	FT24262100200227	1000489	\N	\N	\N	\N	1000489	FT24262100200227
1000018	1000004	1000016	Y	2024-09-18 07:30:03.247548	0	2024-09-18 07:30:03.247549	0	20ea3eb5-699f-4cb8-801c-c73d010450ea	\N	\N	\N	\N	e2fdd126a22472f2e3e760340ea41702	00	\N	CO	\N	\N	\N	SSGJSC1	\N	2000	00020101021238570010A000000727012700069704220113VQRQ000397cki0208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM63048B2E	FT24262100200227	1000489	\N	\N	\N	\N	1000489	FT24262100200227
1000019	1000004	1000016	Y	2024-09-18 07:41:28.865153	0	2024-09-18 07:41:28.865156	0	ed4e8960-10d0-4da3-888d-263d3d6ca6cf	\N	\N	\N	\N	4f855bc87f33e83b8b7d6642540f3c86	00	\N	CO	\N	\N	\N	SSGJSC1	\N	2000	00020101021238570010A000000727012700069704220113VQRQ00039ar980208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM630406A6	FT24262606110623	1000491	\N	\N	\N	\N	1000491	FT24262606110623
1000020	1000004	1000016	Y	2024-09-18 08:24:08.568787	0	2024-09-18 08:24:08.568788	0	26a56684-4b68-413f-96e7-05fab75f6411	\N	\N	\N	\N	c198699d309efd59247d96a8534b2275	00	\N	CO	\N	\N	\N	SSGJSC1	\N	2000	00020101021238570010A000000727012700069704220113VQRQ00039b9f20208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM63046ADD	FT24262343595062	1000493	\N	\N	\N	\N	1000493	FT24262343595062
1000021	1000004	1000016	Y	2024-09-18 08:26:10.478955	0	2024-09-18 08:26:10.478956	0	562c41a5-b35c-440c-8cce-d10610be6e05	\N		\N	\N	d01858d82afa7c8d6ae6438ff4e463a7	00	\N	CO	\N	\N	\N	SSGJSC1	\N	2000	00020101021238570010A000000727012700069704220113VQRQ00039bfa60208QRIBFTTA5303704540420005802VN62190107NPS68690804CKPM63041B78	Q00039bfa6	1000495	\N	\N	\N	\N	1000495	FT24262777565653
\.


--
-- TOC entry 5389 (class 0 OID 385794)
-- Dependencies: 248
-- Data for Name: d_reference; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_reference (d_reference_id, d_tenant_id, name, description, created, created_by, updated, updated_by, d_reference_uu, is_active) FROM stdin;
1000001	0	Goods Type	Loại hàng hóa	2024-08-18 10:35:40.548938	1000069	2024-08-30 14:16:43.435306	1000069	6466817f-e309-47b7-8dca-5995d97d6682	Y
1000005	0	Order Status	Trạng thái đặt món	2024-08-19 17:45:30.483712	1000069	2024-08-19 17:45:30.483712	1000069	da5eeef7-689d-4b1c-ae84-4b60553f315f	Y
1000004	0	Table Reservation Status	Trạng thái đặt bàn	2024-08-18 11:51:32.934966	1000069	2024-08-18 11:51:32.934967	1000069	86124084-24fc-417c-805d-da0dfdfeeb96	Y
1000006	0	Document Status	Trạng thái chứng từ	2024-08-22 11:48:58.108362	1000069	2024-08-22 11:48:58.108362	1000069	d8e4d745-62d3-4401-a7f1-f953db20afb7	Y
1000003	0	Menu Type	Loại thực đơn 	2024-08-18 10:57:58.151622	1000069	2024-08-18 10:57:58.151622	1000069	cc4c738f-33d5-49da-9d40-aea9b7805c11	Y
1000007	0	Table Status	Trạng thái bàn	2024-08-22 17:43:54.702087	1000069	2024-08-22 17:43:54.702089	1000069	ff7cbc83-3f43-4d94-9b45-b220a58fa5c0	Y
1000009	0	Bank Type	Loại tài khoản	2024-09-16 02:51:24.610322	0	2024-09-16 02:51:24.610322	0	467a90d9-0b4b-48df-924c-7d94bd5561a6	Y
1000010	0	Request Order Status	Trạng thái yêu cầu gọi món	2024-09-17 01:43:50.506325	0	2024-09-17 01:43:50.506325	0	1d0621f8-db74-4874-9c4c-96c16e696c61	Y
1000011	0	Data Type Integration	Loại dữ liệu tích hợp	2024-09-17 04:34:14.383867	0	2024-09-17 04:34:14.383867	0	394e656c-2eb2-4384-a9f3-af0a22184054	Y
1000012	0	Status Integration	Trạng thái tích hợp	2024-09-17 07:28:19.277785	0	2024-09-17 07:28:19.277785	0	19c0f155-6bed-4a5b-9304-9ac922c76852	Y
1000013	0	Flow Integration	Chiều tích hợp	2024-09-17 07:28:19.277785	0	2024-09-17 07:28:19.277785	0	61032b25-772e-425d-bea2-cd908dc9f6a8	0
\.


--
-- TOC entry 5390 (class 0 OID 385816)
-- Dependencies: 249
-- Data for Name: d_reference_list; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_reference_list (d_reference_list_id, d_reference_id, value, name, d_tenant_id, d_reference_list_uu, is_active, created, created_by, updated, updated_by) FROM stdin;
1000004	1000001	CBP	Combo - Đóng gói	0	231cf5c1-c4d7-4bf4-b35f-184dc648801c	Y	2024-08-18 10:57:20.399442	1000069	2024-08-18 10:57:20.399442	1000069
1000013	1000005	WTP	Chờ chế biến	0	b1e9ec62-b746-48c6-a27e-2cc7e756f4e8	Y	2024-08-19 17:48:55.585365	1000069	2024-08-19 17:48:55.585365	1000069
1000017	1000005	DCP	Đã chế biến	0	26e18a61-16cc-4052-8fab-0bdcef9c32e9	Y	2024-08-19 17:50:50.633744	1000069	2024-08-19 17:50:50.633744	1000069
1000023	1000007	TIU	Bàn đang sử dụng	0	b6950ec0-67e2-4ed7-a543-1487788ca97d	Y	2024-08-22 17:47:05.877194	1000069	2024-08-22 17:47:05.877194	1000069
1000016	1000005	BPR	Đang chế biến	0	2a751867-5ded-41b4-a5e7-bd003daee32b	Y	2024-08-19 17:50:27.522691	1000069	2024-08-19 17:50:27.522691	1000069
1000003	1000001	SVC	Hàng dịch vụ	0	31a2af1c-b38a-44a1-bb2e-07e1e08acf1a	Y	2024-08-18 10:57:05.102352	1000069	2024-08-18 10:57:05.102352	1000069
1000014	1000005	NSK	Chưa gửi bếp	0	bcc983c1-fa49-4dcc-8927-1fdf1ab9619a	Y	2024-08-19 17:49:24.039142	1000069	2024-08-19 17:49:24.039142	1000069
1000025	1000005	KOS	Đã gửi bếp	0	3f973ed1-575b-4797-ba83-15f0bac7ed82	Y	2024-08-28 09:26:21.227654	1000069	2024-08-28 09:26:21.227654	1000069
1000012	1000004	CAN	Đã hủy	0	ec93dc3f-1405-42cc-922a-7cfb5e12fa70	Y	2024-08-18 11:53:36.862491	1000069	2024-08-18 11:53:36.862491	1000069
1000019	1000006	DRA	Đơn nháp	0	983af889-f189-4dea-ad46-d19fa6cce77b	Y	2024-08-22 11:50:34.483665	1000069	2024-08-22 11:50:34.483665	1000069
1000024	1000007	TBD	Bàn đã đặt	0	bd64fb82-aec1-4411-af94-e6de82720bc9	Y	2024-08-22 17:47:48.97707	1000069	2024-08-22 17:47:48.97707	1000069
1000007	1000003	OTH	Loại khác	0	d5e75c30-15d7-48af-9ae3-1d237fdc93f1	Y	2024-08-18 10:58:49.859136	1000069	2024-08-18 10:58:49.859136	1000069
1000006	1000003	FOD	Thức ăn	0	fbbcef9c-e35c-479f-807d-882046d79ac8	Y	2024-08-18 10:58:38.161452	1000069	2024-08-18 10:58:38.161452	1000069
1000022	1000007	ETB	Bàn trống	0	29cc5eea-4052-4a5e-ac03-1ab57d8924a8	Y	2024-08-22 17:46:25.15017	1000069	2024-08-22 17:46:25.150171	1000069
1000021	1000006	COM	Đơn hoàn thành	0	6e722a2a-6a3c-4f41-997e-19eaec3b678b	Y	2024-08-22 11:51:20.955447	1000069	2024-08-22 11:51:20.955447	1000069
1000005	1000003	DRK	Nước uống	0	3f461bde-c53b-442a-a9b1-9512caf3bac9	Y	2024-08-18 10:58:24.248605	1000069	2024-08-18 10:58:24.248605	1000069
1000002	1000001	PRD 	Hàng chế biến	0	220f2cf6-34af-4c1f-9254-c506942233c6	Y	2024-08-18 10:56:49.583435	1000069	2024-08-18 10:56:49.583435	1000069
1000009	1000004	PSB	Chờ sắp bàn	0	2d1726fb-d17c-4e5a-9040-3827bf1f9e75	Y	2024-08-18 11:52:10.257728	1000069	2024-08-18 11:52:10.257729	1000069
1000001	1000001	RGD	Hàng hóa thường	0	63ab25cb-d5fe-464b-b3af-10a4f171ca6f	Y	2024-08-18 10:55:27.177022	1000069	2024-08-18 10:55:27.177022	1000069
1000011	1000004	NOS	Không đến	0	3a82861c-9f23-45b8-a84c-ba6033b90606	Y	2024-08-18 11:53:24.759148	1000069	2024-08-18 11:53:24.759148	1000069
1000020	1000006	IPR	Đơn đang xử lý	0	5878ff01-0df2-4611-b940-0d479edf4a9c	Y	2024-08-22 11:51:04.415044	1000069	2024-08-22 11:51:04.415044	1000069
1000008	1000004	TBL	Đã xếp bàn	0	4e310f0f-35a6-4203-9651-e00f17ee5c56	Y	2024-08-18 11:51:55.431381	1000069	2024-08-18 11:51:55.431382	1000069
1000015	1000005	PRD	Đã chế biến	0	d6bb1f15-38db-49d7-bc24-ddfc96236dbf	Y	2024-08-19 17:49:44.796892	1000069	2024-08-19 17:49:44.796892	1000069
1000018	1000005	DMC	Hủy món	0	a45578d3-f182-4099-8d47-24168f19248c	Y	2024-08-19 17:51:08.372483	1000069	2024-08-19 17:51:08.372483	1000069
1000010	1000004	TRC	Đã nhận bàn	0	603ece6a-f05c-4ae1-9d2e-2d180f309037	Y	2024-08-18 11:52:20.28623	1000069	2024-08-18 11:52:20.28623	1000069
1000029	1000006	VOD	Đơn Hủy	0	b650e2aa-bde3-4f18-8e6b-fe16d20a6800	Y	2024-09-06 04:02:05.442333	0	2024-09-06 04:02:05.442333	0
1000030	1000009	CHE	Tài khoản ngân hàng	0	846f208a-5da0-42cf-9386-f39c6aabf6bd	Y	2024-09-16 02:55:06.421122	0	2024-09-16 02:55:06.421122	0
1000031	1000009	CAS	Tài khoản tiền mặt	0	48ab5218-105c-4cf7-b3ac-cf621d2f017d	Y	2024-09-16 02:55:06.421122	0	2024-09-16 02:55:06.421122	0
1000033	1000011	ORG	Cửa hàng chi nhánh	0	88bf93c9-06f9-47b6-b8cb-f9d5280795ef	Y	2024-09-17 04:41:42.43141	0	2024-09-17 04:41:42.43141	0
1000034	1000011	PCG	Nhóm hàng	0	34148213-289b-456d-a967-b870c8141999	Y	2024-09-17 04:41:42.43141	0	2024-09-17 04:41:42.43141	0
1000035	1000011	PRO	Hàng hóa	0	d53f42f9-dbd5-4ae3-973b-67a398bd5dd4	Y	2024-09-17 04:41:42.43141	0	2024-09-17 04:41:42.43141	0
1000036	1000011	PRL	Bảng giá	0	13df22a2-2ead-432b-9164-515016b6065b	Y	2024-09-17 04:41:42.43141	0	2024-09-17 04:41:42.43141	0
1000037	1000011	CAV	Khách hàng / Nhà cung cấp	0	f44ee07e-2b4e-4cdd-82f2-e6918e2dcf8a	Y	2024-09-17 04:41:42.43141	0	2024-09-17 04:41:42.43141	0
1000038	1000011	CUS	Người dùng	0	e9f92632-8f7a-4aee-b620-6f246a690709	Y	2024-09-17 04:41:42.43141	0	2024-09-17 04:41:42.43141	0
1000039	1000011	WHO	Kho hàng	0	bc8c04ae-241d-4e6e-b072-fc0691473e0b	Y	2024-09-17 04:41:42.43141	0	2024-09-17 04:41:42.43141	0
1000040	1000011	FLO	khu vực / tầng	0	dca0ac0f-38c6-4e6c-8c12-70e43dc8e052	Y	2024-09-17 04:41:42.43141	0	2024-09-17 04:41:42.43141	0
1000041	1000011	TBL	Phòng bàn	0	c0699412-f876-4c86-828f-4bee55ae4389	Y	2024-09-17 04:41:42.43141	0	2024-09-17 04:41:42.43141	0
1000042	1000011	COP	Coupo	0	2233188b-29c4-4661-a118-37b315aeb4db	Y	2024-09-17 04:41:42.43141	0	2024-09-17 04:41:42.43141	0
1000043	1000011	SOR	Đơn hàng	0	7b5d030b-225f-479f-842d-ee9f9e9cb0a7	Y	2024-09-17 04:41:42.43141	0	2024-09-17 04:41:42.43141	0
1000044	1000011	KDS	Đơn bar/bếp	0	6279a319-a344-4944-bc1e-12c221fae365	Y	2024-09-17 04:41:42.43141	0	2024-09-17 04:41:42.43141	0
1000045	1000011	PTM	Điểm bán hàng	0	91f45828-02d0-4c4e-a45c-556845f93739	Y	2024-09-17 04:41:42.43141	0	2024-09-17 04:41:42.43141	0
1000047	1000010	PND	Chờ xác nhận	0	54639e83-80e7-4f74-83b9-c9d0bbe93de0	Y	2024-09-17 04:57:18.978784	0	2024-09-17 04:57:18.978784	0
1000048	1000010	CNF	Đã xác nhận	0	903d3993-6a2c-45d5-b4bc-fcd64d6cd584	Y	2024-09-17 04:57:18.978784	0	2024-09-17 04:57:18.978784	0
1000049	1000010	VOID	Đã hủy	0	87d24c78-6ee3-4ba7-a859-f4a1885ee873	Y	2024-09-17 04:57:18.978784	0	2024-09-17 04:57:18.978784	0
1000050	1000012	COM	Thành công	0	eedb8563-844c-4208-be00-f24b79e8c779	Y	2024-09-17 07:35:14.454058	0	2024-09-17 07:35:14.454058	0
1000051	1000012	FAI	Thất bại	0	dbe08b68-3d19-47c4-af1a-75fd6eee0bf2	Y	2024-09-17 07:35:14.454058	0	2024-09-17 07:35:14.454058	0
1000052	1000013	ETP	ERP về POS	0	d7d6f9a5-e736-4c01-8082-95da045427a5	Y	2024-09-17 07:35:14.454058	0	2024-09-17 07:35:14.454058	0
1000053	1000013	PTE	POS về ERP	0	0a9eddf7-f113-439e-ad2c-333251b283c5	Y	2024-09-17 07:35:14.454058	0	2024-09-17 07:35:14.454058	0
\.


--
-- TOC entry 5521 (class 0 OID 394573)
-- Dependencies: 390
-- Data for Name: d_request_order; Type: TABLE DATA; Schema: pos; Owner: adempiere
--

COPY pos.d_request_order (d_request_order_id, d_tenant_id, d_org_id, created, created_by, updated, updated_by, d_doctype_id, document_no, order_status, d_floor_id, d_table_id, order_time, d_request_order_uu, d_customer_id, is_active) FROM stdin;
1000003	1000004	1000016	2024-09-17 23:38:45.228506	1000069	2024-09-17 23:38:45.228506	1000069	1000006	ROD123456789	PND	1000012	1000077	2024-09-17 12:34:56	e5b65f65-22eb-45b6-8d0d-829c659ddea1	\N	Y
1000004	1000004	1000016	2024-09-17 23:45:51.069435	1000069	2024-09-17 23:45:51.069435	1000069	1000006	ROD123456789	PND	1000012	1000077	2024-09-17 12:34:56	5c8ef675-c1b2-458c-8044-bb7870e2b161	\N	Y
1000002	1000004	1000016	2024-09-17 23:30:48.96478	1000069	2024-09-17 23:30:48.96478	1000069	1000006	ROD123456789	PND	1000012	1000077	2024-09-18 08:40:45	9ebcfc49-0e10-48a3-90af-f1095db81023	\N	Y
\.


--
-- TOC entry 5522 (class 0 OID 394610)
-- Dependencies: 391
-- Data for Name: d_request_orderline; Type: TABLE DATA; Schema: pos; Owner: adempiere
--

COPY pos.d_request_orderline (d_request_orderline_id, d_tenant_id, d_org_id, created, created_by, updated, updated_by, d_product_id, d_request_order_id, qty, description, saleprice, total_amount, d_request_orderline_uu, is_active) FROM stdin;
1000002	1000004	1000016	2024-09-17 23:30:52.443081	1000069	2024-09-17 23:30:52.443081	1000069	1000407	1000002	3	Product A Description	16000.00	48000.00	d3aac00d-0753-4c86-9627-28a04ff903c2	Y
1000003	1000004	1000016	2024-09-17 23:38:47.136419	1000069	2024-09-17 23:38:47.136419	1000069	1000407	1000003	3	Product A Description	16000.00	48000.00	8655f2e4-19b2-460d-b7ed-569f2b38c6c2	Y
1000004	1000004	1000016	2024-09-17 23:45:52.668722	1000069	2024-09-17 23:45:52.668722	1000069	1000407	1000004	3	Product A Description	16000.00	48000.00	61f122cc-91f4-4752-be55-4b9da1a62358	Y
\.


--
-- TOC entry 5391 (class 0 OID 385824)
-- Dependencies: 250
-- Data for Name: d_reservation_line; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_reservation_line (d_reservation_line_id, d_org_id, d_tenant_id, created, created_by, updated, updated_by, is_active, description, d_reservation_order_id, d_product_id, qty, d_reservation_line_uu) FROM stdin;
\.


--
-- TOC entry 5392 (class 0 OID 385832)
-- Dependencies: 251
-- Data for Name: d_reservation_order; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_reservation_order (d_reservation_order_id, d_org_id, d_tenant_id, created, created_by, updated, updated_by, is_active, name, d_floor_id, d_table_id, customer_name, phone, company, total_cus, reservation_time, reser_amount, status, d_reservation_order_uu, code, d_customer_id, time_tocome, d_user_id, qty_baby, qty_adult) FROM stdin;
1000123	1000016	1000004	2024-09-06 17:48:33.778868	1000069	2024-09-06 17:48:42.422739	1000069	Y	Hà Anh 	999972	1000039	Hà Anh 	0237272723		1	2024-09-06 19:00:00	\N	TRC	19606780-dec9-487c-8613-f50a46e3ea5e	\N	1000079	1	1000069	\N	\N
1000115	1000016	1000004	2024-09-06 02:14:50.268236	1000069	2024-09-06 03:25:08.556579	1000069	Y		999975	1000060	Thanhnc1	03844	Dbiz	5	2024-08-19 16:55:00	\N	TBL	a4b6eb88-3ff3-4254-9272-a47a3d974b21		1000037	5	1000070	\N	\N
1000117	1000016	1000004	2024-09-06 14:39:04.507846	1000069	2024-09-06 16:07:21.73786	1000069	Y	Lầu Sẹc Dần 	999972	1000076	Lầu Sẹc Dần 	45123		1	2024-09-06 15:00:00	\N	NOS	14127dfd-a50e-428d-a110-b01afd006cae	\N	1000044	1	1000069	\N	\N
1000119	1000016	1000004	2024-09-06 14:58:53.45708	1000069	2024-09-06 17:01:03.279509	1000069	Y	Hà Anh	999972	1000039	Hà Anh	0123456789		1	2024-09-06 16:00:00	\N	NOS	2a74c028-4413-4393-85e1-403f585bc4ef	\N	1000081	1	1000069	\N	\N
1000126	1000016	1000004	2024-09-08 14:33:48.112035	1000069	2024-09-09 07:08:11.74046	1000069	Y	Lầu Sẹc Dần 	999972	1000040	Lầu Sẹc Dần 	45123		1	2024-09-08 16:00:00	\N	NOS	b685b50c-3eff-432d-8641-25259bb0f289	\N	1000044	3	1000069	\N	\N
1000121	1000016	1000004	2024-09-06 16:57:06.987046	1000069	2024-09-06 17:02:31.779966	1000069	Y	Hà Anh	999972	1000040	Hà Anh	0123456789		1	2024-09-06 18:00:00	\N	CAN	733c0560-376b-4bc6-9ae3-9e11a13c0af8	\N	1000081	1	1000069	\N	\N
1000122	1000016	1000004	2024-09-06 17:02:21.779066	1000069	2024-09-06 17:02:49.748734	1000069	Y	Tuyết Như	999972	1000073	Tuyết Như	0326647677		1	2024-09-06 18:00:00	\N	TRC	f099199b-a1fa-4e3b-bcc1-29e5cf3b8ff0	\N	1000055	1	1000069	\N	\N
1000124	1000016	1000004	2024-09-08 14:31:44.263231	1000069	2024-09-09 07:08:11.744785	1000069	Y	Lầu Sẹc Dần 	999972	1000073	Lầu Sẹc Dần 	45123		1	2024-09-08 17:00:00	\N	NOS	c093dced-9866-4581-b908-0fe3a8296a1d	\N	1000044	1	1000069	\N	\N
1000120	1000016	1000004	2024-09-06 15:35:16.354593	1000069	2024-09-06 17:14:46.61225	1000069	Y	Lầu Sẹc Dần 	999972	1000040	Lầu Sẹc Dần 			1	2024-09-06 19:00:00	\N	TRC	32ff0e4a-1990-46a4-a2d0-72f604762392		1000044	1	1000069	\N	\N
1000118	1000016	1000004	2024-09-06 14:43:32.977633	1000069	2024-09-06 17:21:38.59139	1000069	Y	Hà Anh	999972	1000073	Hà Anh	0123456789		1	2024-09-06 16:00:00	\N	CAN	bc8c9155-b92a-4d22-8515-d855eb8bf908	\N	1000081	1	1000069	\N	\N
1000125	1000016	1000004	2024-09-08 14:32:18.01333	1000069	2024-09-09 07:08:11.749517	1000069	Y	Nguyễn Lê Duy Tiến	999972	1000076	Nguyễn Lê Duy Tiến			1	2024-09-08 17:00:00	\N	NOS	8fcb3617-2da3-45d8-a470-10acfcfe1203		1000039	1	1000069	\N	\N
1000128	1000016	1000004	2024-09-09 11:25:23.366352	1000069	2024-09-09 11:29:44.200322	1000069	Y	Tuyết Như	999973	1000042	Tuyết Như	0326647677		2	2024-09-09 11:25:23	\N	TRC	d0d7823e-2cb4-4f26-afd4-fec4318b253b	\N	1000055	1	1000069	\N	\N
1000127	1000016	1000004	2024-09-09 11:07:25.935843	1000069	2024-09-09 11:48:45.963139	1000069	Y	Hà Anh	999972	1000076	Hà Anh	0123456789		1	2024-09-09 12:00:00	\N	CAN	06433ff9-7d10-492c-9656-c69b55f4eb7b	\N	1000081	1	1000069	\N	\N
1000131	1000016	1000004	2024-09-09 11:51:42.799995	1000069	2024-09-09 11:51:42.799996	1000069	Y	Hà Anh Tú 5	999973	1000043	Hà Anh Tú 5	222		2	2024-09-09 12:00:00	\N	TBL	1c5b94a0-ae08-4d4e-a1a0-8744f1685851	\N	1000082	1	1000069	\N	\N
1000132	1000016	1000004	2024-09-09 11:52:17.126504	1000069	2024-09-09 11:54:58.185887	1000069	Y	Nguyễn Minh Hưngg	999974	1000047	Nguyễn Minh Hưngg			2	2024-09-09 20:52:00	\N	TBL	770bbcb9-2a35-43b7-813c-24579c074a29		1000042	1	1000069	\N	\N
1000130	1000016	1000004	2024-09-09 11:49:50.067066	1000069	2024-09-11 11:26:36.413932	1000069	Y	Nguyễn Minh Hưngg	999973	1000043	Nguyễn Minh Hưngg			3	2024-09-11 21:00:00	\N	PSB	f86d43f3-8997-428c-982b-747979c6372e		1000042	1	1000069	\N	\N
1000129	1000016	1000004	2024-09-09 11:39:24.855193	1000069	2024-09-09 15:48:30.087645	1000069	Y	Tuyết Như	999972	1000040	Tuyết Như			3	2024-09-09 12:00:00	\N	NOS	4583f06c-dd57-4874-8adf-4573da2dbe85		1000055	1	1000069	\N	\N
1000139	1000016	1000004	2024-09-11 11:27:33.574267	1000069	2024-09-11 11:33:56.418295	1000069	Y	Tuyết Như	999972	1000076	Tuyết Như	0326647677		2	2024-09-11 12:00:00	\N	TRC	220f420a-bbe5-49ed-964e-4d31b7c90042	\N	1000055	1	1000069	\N	\N
1000140	1000016	1000004	2024-09-11 11:59:58.917456	1000069	2024-09-11 13:49:53.842407	1000069	Y	Hà Anh 	999972	1000040	Hà Anh 	0237272723		2	2024-09-11 16:00:00	\N	TRC	e534a4a6-38cc-4ad9-9981-9ea24aa19453	\N	1000079	1	1000069	\N	\N
1000142	1000016	1000004	2024-09-13 11:39:56.757906	1000069	2024-09-13 11:43:11.253112	1000069	Y	Nguyễn Minh Hưngg	999972	1000040	Nguyễn Minh Hưngg			2	2024-09-13 11:45:00	\N	CAN	d0a3869f-8b9d-4f35-9efb-619e4a088b18		1000042	1	1000069	\N	\N
1000145	1000016	1000004	2024-09-13 13:28:24.632665	1000069	2024-09-13 14:59:11.369158	1000069	Y	Lầu Sẹc Dần 	999972	1000076	Lầu Sẹc Dần 	45123		1	2024-09-13 14:00:00	\N	NOS	509e6288-9e2a-4bbe-8c7d-145526416173	\N	1000044	1	1000069	\N	\N
1000141	1000016	1000004	2024-09-13 11:02:39.891199	1000069	2024-09-13 13:01:25.933512	1000069	Y	Lầu Sẹc Dần 	999972	1000073	Lầu Sẹc Dần 			2	2024-09-13 12:00:00	\N	NOS	9ae25f8e-9eb4-4a33-a572-472bf1256657		1000044	1	1000069	\N	\N
1000153	1000016	1000004	2024-09-13 14:14:50.410493	1000069	2024-09-13 15:59:11.340555	1000069	Y	Lầu Sẹc Dần 	1000012	1000077	Lầu Sẹc Dần 	45123		1	2024-09-13 15:00:00	\N	NOS	a64ecbe4-3085-458b-957f-81b23fb093e2	\N	1000044	1	1000069	\N	\N
1000143	1000016	1000004	2024-09-13 12:02:37.970634	1000069	2024-09-13 13:03:43.516592	1000069	Y	Lầu Sẹc Dần 	\N	\N	Lầu Sẹc Dần 	45123		2	2024-09-13 12:03:36	\N	CAN	0c058370-f93a-47f6-829e-002957176854	\N	1000044	1	1000069	\N	\N
1000148	1000016	1000004	2024-09-13 13:34:08.360981	1000069	2024-09-13 15:59:11.365264	1000069	Y	Lầu Sẹc Dần 	999976	1000071	Lầu Sẹc Dần 	45123		1	2024-09-13 15:00:00	\N	NOS	370bfe53-dfbb-4ada-81c4-6a82d569e62b	\N	1000044	1	1000069	\N	\N
1000150	1000016	1000004	2024-09-13 13:48:34.026078	1000069	2024-09-13 13:48:34.026078	1000069	Y	Nguyễn Lê Duy Tiến	\N	\N	Nguyễn Lê Duy Tiến	1212		1	2024-09-13 13:49:32	\N	PSB	5dec3240-a7eb-461f-98b0-99b09caa3a6b	\N	1000039	1	1000069	\N	\N
1000154	1000016	1000004	2024-09-15 14:43:44.507038	1000069	2024-09-15 14:43:44.507039	1000069	Y	Lầu Sẹc Dần 	999972	1000073	Lầu Sẹc Dần 	45123		2	2024-09-15 15:00:00	\N	TBL	58f03bca-0381-432e-bbec-cf01efab22d8	\N	1000044	1	1000069	\N	\N
1000151	1000016	1000004	2024-09-13 14:12:39.202037	1000069	2024-09-13 18:08:53.571351	1000069	Y	Lầu Sẹc Dần 	999972	1000073	Lầu Sẹc Dần 	45123		1	2024-09-13 16:00:00	\N	NOS	cd797a77-3079-406c-9cef-4c573ef6aadd	\N	1000044	2	1000069	\N	\N
1000155	1000016	1000004	2024-09-15 14:45:15.009583	1000069	2024-09-15 14:45:15.009583	1000069	Y	Nguyễn Lê Duy Tiến	999972	1000040	Nguyễn Lê Duy Tiến	1212		2	2024-09-15 15:00:00	\N	TBL	b2634d02-e74e-4290-bb6b-83790cdd336a	\N	1000039	2	1000069	\N	\N
1000152	1000016	1000004	2024-09-13 14:13:12.629674	1000069	2024-09-13 14:13:19.641955	1000069	Y	Nguyễn Lê Duy Tiến	999977	1000074	Nguyễn Lê Duy Tiến	1212		2	2024-09-13 16:00:00	\N	TRC	23214a7f-20ba-482b-bdc2-97b03c28b732	\N	1000039	3	1000069	\N	\N
1000149	1000016	1000004	2024-09-13 13:38:27.263789	1000069	2024-09-13 14:59:11.316642	1000069	Y	Lầu Sẹc Dần 	999972	1000040	Lầu Sẹc Dần 	45123		1	2024-09-13 14:00:00	\N	NOS	c7ceb2a1-885b-438a-a368-fa3e31a9b527	\N	1000044	1	1000069	\N	\N
1000146	1000016	1000004	2024-09-13 13:29:15.803242	1000069	2024-09-13 14:59:11.350129	1000069	Y	Lầu Sẹc Dần 	999977	1000074	Lầu Sẹc Dần 			1	2024-09-13 14:00:00	\N	NOS	16fc9037-b752-4b70-ab8c-da7f2e3267c2		1000044	1	1000069	\N	\N
1000147	1000016	1000004	2024-09-13 13:30:08.775606	1000069	2024-09-13 14:59:11.347694	1000069	Y	Nguyễn Lê Duy Tiến	999972	1000073	Nguyễn Lê Duy Tiến			1	2024-09-13 14:00:00	\N	NOS	9225ac6a-b716-4091-b8ee-dfaba2b76c00		1000039	1	1000069	\N	\N
1000157	1000016	1000004	2024-09-16 09:57:06.324646	1000069	2024-09-16 09:57:06.324646	1000069	Y	Nguyễn Minh Hưngg	999972	1000039	Nguyễn Minh Hưngg	3212		2	2024-09-16 13:00:00	\N	TBL	5e002771-0b1d-496a-b183-026a523cba34	\N	1000042	1	1000069	\N	\N
1000159	1000016	1000004	2024-09-16 10:18:51.354442	1000069	2024-09-16 10:18:51.354442	1000069	Y	Nguyễn Chí THanh	\N	\N	Nguyễn Chí THanh	0384491		3	2024-09-16 10:18:49	\N	PSB	afbbc443-4a9d-4636-b308-347d36a9df3f	\N	1000085	1	1000069	\N	\N
1000158	1000016	1000004	2024-09-16 10:18:36.43619	1000069	2024-09-16 10:19:24.769098	1000069	Y	Hà Anh 	999977	1000074	Hà Anh 			2	2024-09-16 22:18:00	\N	PSB	6cc71e57-898c-4e37-af89-1e62b71446b2		1000079	1	1000069	\N	\N
1000160	1000016	1000004	2024-09-16 10:29:32.407767	1000069	2024-09-16 10:29:47.563235	1000069	Y	Hà Anh	999975	1000078	Hà Anh	0123456789		1	2024-09-16 11:00:00	\N	CAN	1951ade4-bf73-40b1-ba37-af2751fb7094	\N	1000081	1	1000069	\N	\N
1000161	1000016	1000004	2024-09-16 10:30:28.453885	1000069	2024-09-16 10:30:28.453885	1000069	Y	Hà Anh	1000012	1000077	Hà Anh	0123456789		1	2024-09-16 11:15:00	\N	TBL	0aebf7a3-2d85-4fd1-8e69-88daec8dcf99	\N	1000081	1	1000069	\N	\N
1000156	1000016	1000004	2024-09-16 09:52:07.002909	1000069	2024-09-16 11:00:16.102699	1000069	Y	Tuyết Như	999972	1000040	Tuyết Như	0326647677		2	2024-09-16 10:00:00	\N	NOS	839e10e2-4614-4e33-b89f-cbeb028d2710	\N	1000055	1	1000069	\N	\N
\.


--
-- TOC entry 5393 (class 0 OID 385843)
-- Dependencies: 252
-- Data for Name: d_role; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_role (d_role_id, d_tenant_id, code, name, created, created_by, updated, updated_by, is_active, d_role_uu) FROM stdin;
1000000	1000004	ADMIN	ADMIN	2024-09-17 08:37:03.335228	0	2024-09-17 08:37:03.335228	0	Y	4c79a452-cb12-4eab-9aaa-82e44b62c3dc
1000001	1000004	USER	USER	2024-09-17 08:39:26.885968	0	2024-09-17 08:39:26.885968	0	Y	3dbd931d-7114-4f0c-9b83-fc504df86398
1000002	1000004	STAFF	STAFF	2024-09-18 08:27:05.152365	0	2024-09-18 08:27:05.152365	0	Y	924341e8-8c02-4ec7-9643-925ae9b7dfb0
\.


--
-- TOC entry 5482 (class 0 OID 393021)
-- Dependencies: 341
-- Data for Name: d_shift_control; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_shift_control (d_shift_control_id, d_tenant_id, d_org_id, d_user_id, sequence_no, d_pos_terminal_id, start_date, end_date, shift_type, created, created_by, updated, updated_by, d_shift_control_uu, is_active, is_closed, erp_shift_control_id) FROM stdin;
\.


--
-- TOC entry 5394 (class 0 OID 385855)
-- Dependencies: 253
-- Data for Name: d_storage_onhand; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_storage_onhand (d_storage_onhand_id, d_tenant_id, d_org_id, qty, is_active, d_warehouse_id, d_locator_id, reservation_qty, created, created_by, updated, updated_by, d_storage_onhand_uu) FROM stdin;
\.


--
-- TOC entry 5395 (class 0 OID 385866)
-- Dependencies: 254
-- Data for Name: d_table; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_table (d_table_id, d_org_id, d_tenant_id, created, created_by, updated, updated_by, is_active, table_no, name, description, d_floor_id, table_status, d_table_uu, number_seats, display_index, number_guests, is_room, d_customer_id, is_locked, erp_table_id) FROM stdin;
1000089	1000030	1000004	2024-09-18 22:08:02.190611	1000069	2024-09-18 22:08:47.626136	1000069	Y	B01	Bàn 2	B01	999982	ETB	cad620de-751c-443e-a469-d8c374378929	2	1	\N	N	\N	N	\N
1000030	1000003	1000003	2024-07-31 11:10:40.066871	1000068	2024-07-31 11:10:40.066873	1000068	Y	B9	Bàn - 09	Bàn hư	999964	ETB	1821b8bb-8d0d-4b01-814f-1a4dc6548ad8	2	1	\N	N	\N	N	\N
1000032	1000016	1000004	2024-08-07 09:19:22.070725	1000069	2024-08-07 09:19:22.070726	1000069	Y	B11	Bàn - 11	 	999972	ETB	0f358426-9982-4c95-becf-47afe784160a	2	1	\N	N	\N	N	\N
1000074	1000016	1000004	2024-08-17 10:40:15.123894	1000069	2024-09-13 14:13:20.857452	1000069	N	B42	Bàn - 42	Bàn lớn	999977	TIU	1441339e-6822-44be-88f9-8d3762f4eb1f	10	0	\N	N	\N	N	\N
1000037	1000016	1000004	2024-08-07 09:19:44.255929	1000069	2024-08-07 09:19:44.255931	1000069	Y	B16	Bàn - 16	 	999972	ETB	7a9d7029-003d-4e8e-94bc-af325f05c8ba	2	1	\N	N	\N	N	\N
1000057	1000016	1000004	2024-08-07 09:22:19.317118	1000069	2024-08-07 09:22:19.317119	1000069	Y	B33	Bàn - 33	 	999975	ETB	0e879b35-34f4-4202-b9d0-9192b0ff5653	2	1	\N	N	\N	N	\N
1000063	1000016	1000004	2024-08-07 11:37:01.347313	1000069	2024-09-17 18:12:33.388684	1000069	Y	B47	Bàn - 47	Bàn trống	999976	TIU	05ac63ea-2f84-46df-bcde-66f3d20ae6b0	10	2	\N	N	\N	N	\N
1000056	1000016	1000004	2024-08-07 09:22:14.498161	1000069	2024-08-07 09:22:14.498162	1000069	Y	B32	Bàn - 32	 	999975	ETB	f49f43b5-8ede-4215-afa2-f09982c1c0ca	2	1	\N	N	\N	N	\N
1000047	1000016	1000004	2024-08-07 09:21:05.235521	1000069	2024-08-07 09:21:05.235522	1000069	Y	B24	Bàn - 24	 	999974	ETB	215c925b-2d98-4664-aefa-5175f66cd750	2	1	\N	N	\N	N	\N
1000066	1000016	1000004	2024-08-07 11:38:35.222062	1000069	2024-09-17 17:46:18.367117	1000069	Y	B35	Bàn - 35	Bàn trống	999976	TIU	c61f5fa7-547f-4085-b33c-e7942c671e66	21	0	\N	N	\N	N	\N
1000040	1000016	1000004	2024-08-07 09:19:57.298896	1000069	2024-09-16 09:52:07.455683	1000069	Y	B19	Bàn - 19	 	999972	TBD	aa1a0430-403f-4f86-9059-f6222ff2e0c9	2	1	\N	N	\N	N	\N
1000051	1000016	1000004	2024-08-07 09:21:42.820513	1000069	2024-08-07 09:21:42.820514	1000069	Y	B27	Bàn - 27	 	999975	ETB	ca94d9d9-0123-4412-8bd9-48621c7859ef	2	1	\N	N	\N	N	\N
1000061	1000016	1000004	2024-08-07 11:36:02.627606	1000069	2024-08-07 11:36:02.627609	1000069	Y	B4	Bàn - 04	Bàn trống 	999976	ETB	27485e45-9291-49ce-8dd2-8eef17f230a0	20	1	\N	N	\N	N	\N
1000052	1000016	1000004	2024-08-07 09:21:47.294973	1000069	2024-08-07 09:21:47.294974	1000069	Y	B28	Bàn - 28	 	999975	ETB	a3942c57-9b99-4bfe-ab6c-ed758f392d90	2	1	\N	N	\N	N	\N
1000087	1000030	1000004	2024-09-18 22:07:21.073224	1000069	2024-09-18 22:08:09.566649	1000069	Y	B01	Bàn 1	 	999982	TIU	be300bbe-3e87-4428-8057-0fda12f5a605	2	1	\N	N	\N	N	\N
1000068	1000016	1000004	2024-08-07 11:39:14.230906	1000069	2024-09-17 16:36:34.080153	1000069	Y	B37	Bàn - 37	Bàn	999976	TIU	945958dc-8384-4874-9a39-fedcda4a3ef9	11	0	\N	N	\N	N	\N
1000033	1000016	1000004	2024-08-07 09:19:26.504501	1000069	2024-08-07 09:19:26.504503	1000069	Y	B12	Bàn - 12	 	999972	ETB	3b663d6c-94f3-45be-b23b-ee5ba1beb3c6	2	1	\N	N	\N	N	\N
1000035	1000016	1000004	2024-08-07 09:19:33.78718	1000069	2024-08-07 09:19:33.787181	1000069	Y	B14	Bàn - 14	 	999972	ETB	2950ea6e-a613-4458-8cd2-08bb285052c9	2	1	\N	N	\N	N	\N
1000023	1000003	1000002	2024-07-22 16:34:11.384657	1000037	2024-07-22 16:34:11.384659	1000037	Y	B3	Bàn - 03	Bàn hư	999963	ETB	3dc13179-02ac-4b0e-936e-5a9c03698975	2	0	\N	N	\N	N	\N
1000054	1000016	1000004	2024-08-07 09:21:55.101105	1000069	2024-08-07 09:21:55.101106	1000069	Y	B30	Bàn - 30	 	999975	ETB	c2e836b0-3796-4231-ae5d-f8675038ed89	2	1	\N	N	\N	N	\N
1000069	1000016	1000004	2024-08-07 11:39:35.528769	1000069	2024-09-17 18:27:20.538827	1000069	Y	B38	Bàn - 38	Bàn	999976	TIU	5fca46c5-9a5b-430c-a59c-f5a5119a517b	12	0	\N	N	\N	N	\N
1000039	1000016	1000004	2024-08-07 09:19:52.645008	1000069	2024-09-16 09:57:06.414437	1000069	Y	B18	Bàn - 18	 	999972	TBD	4af92d3a-b7c3-4c36-bf36-3aaed3cc2741	2	1	\N	N	\N	N	\N
1000029	1000001	1000002	2024-07-31 09:51:55.852874	1000037	2024-07-31 09:51:55.852877	1000037	Y	B8	Bàn - 08	Bàn vip	999957	ETB	15eff874-39a7-43dd-9502-d75a779d16dd	3	0	\N	N	\N	N	\N
1000038	1000016	1000004	2024-08-07 09:19:48.432928	1000069	2024-08-07 09:19:48.43293	1000069	Y	B17	Bàn - 17	 	999972	ETB	8047251d-8d4d-4f1d-8bdc-f4ba14e2d06e	2	1	\N	N	\N	N	\N
1000034	1000016	1000004	2024-08-07 09:19:30.022508	1000069	2024-08-07 09:19:30.02251	1000069	Y	B13	Bàn - 13	 	999972	ETB	f1b4178e-0ebb-467b-9ea9-140ec14943e2	2	1	\N	N	\N	N	\N
1000027	1000003	1000002	2024-07-24 12:33:22.15152	1000037	2024-07-25 10:47:26.561879	1000037	Y	B7	Bàn - 07	Bàn vip	999966	ETB	de6aeb46-d976-4adc-98b3-ad67c6b127cd	33	1	\N	N	\N	N	\N
1000025	1000003	1000003	2024-07-24 10:11:53.673266	1000068	2024-07-24 10:11:53.673266	1000068	Y	B2	Bàn - 02	Bàn hư	999964	ETB	9b8b6e96-0818-4d2e-bc9f-7914211b9320	2	1	\N	N	\N	N	\N
1000043	1000016	1000004	2024-08-07 09:20:32.103001	1000069	2024-09-18 16:20:44.131929	1000069	Y	B22	Bàn - 22	 	999973	TIU	934a4294-e6ca-4154-84a8-fe0379ca042e	2	1	\N	N	\N	N	\N
1000026	1000003	1000002	2024-07-24 11:59:13.47666	1000037	2024-07-24 11:59:13.476663	1000037	Y	B5	Bàn - 05	Bàn to	999964	ETB	bf469f3d-c011-4f1f-b149-60eb55a9fe3f	10	0	\N	N	\N	N	\N
1000031	1000016	1000004	2024-08-07 09:19:06.167222	1000069	2024-08-07 09:19:06.167223	1000069	Y	B10	Bàn - 10	 	999972	ETB	61aab7bb-8934-4a72-a1e0-023f1c66b32f	2	1	\N	N	\N	N	\N
1000036	1000016	1000004	2024-08-07 09:19:39.325795	1000069	2024-08-07 09:19:39.325797	1000069	Y	B15	Bàn - 15	 	999972	ETB	e0d07d6c-934f-424d-a9a9-4dee33237c4c	2	1	\N	N	\N	N	\N
1000065	1000016	1000004	2024-08-07 11:37:58.606125	1000069	2024-09-17 17:54:41.289365	1000069	Y	B45	Bàn - 45	Bàn trống rất trống nha	999976	TIU	569de64c-6adf-43a2-8602-da8ba8d8f9e1	23	2	\N	N	\N	N	\N
1000078	1000016	1000004	2024-08-26 17:31:36.481602	1000069	2024-09-17 17:05:33.019761	1000069	Y	B50	Bàn - 50	1	999975	TIU	8c7edaa0-a802-4282-aee9-bb4d77b593fb	12	0	\N	N	\N	N	\N
1000070	1000016	1000004	2024-08-07 11:39:54.893844	1000069	2024-09-17 17:07:58.134574	1000069	Y	B39	Bàn - 39	B	999976	TIU	2581a6ac-a385-4917-80d4-7e00cddd51b8	11	0	\N	N	\N	N	\N
1000073	1000016	1000004	2024-08-17 10:39:15.535796	1000069	2024-09-16 10:28:52.222877	1000069	Y	B41	Bàn - 41	Bàn to	999972	TIU	3e17c40c-5354-428a-aa9d-e5fbb94c78a6	10	0	\N	N	\N	N	\N
1000041	1000016	1000004	2024-08-07 09:20:25.228524	1000069	2024-08-07 09:20:25.228525	1000069	Y	B20	Bàn - 20	 	999973	ETB	75529db2-ca5f-43bf-955b-747aa863d59a	2	1	\N	N	\N	N	\N
1000076	1000016	1000004	2024-08-26 14:17:21.400127	1000069	2024-09-18 10:39:04.176512	1000069	Y	B48	Bàn - 48	hi	999972	TIU	f137c7a8-79fa-4463-91c0-29133710720c	2	2	\N	N	\N	N	\N
1000077	1000016	1000004	2024-08-26 14:23:15.982083	1000069	2024-09-16 14:26:47.494344	1000069	Y	B49	Bàn - 49	h	1000012	TIU	0c80d704-0c9f-42c3-af3c-eb3af68a3989	3	2	\N	N	\N	N	\N
1000062	1000016	1000004	2024-08-07 11:36:29.219726	1000069	2024-09-18 16:30:38.674877	1000069	Y	B6	Bàn - 06	Bàn trống	999976	TIU	b1d8d5ce-d0f4-4ee6-807c-59a871134645	20	1	\N	N	\N	N	\N
1000060	1000016	1000004	2024-08-07 09:22:29.574324	1000069	2024-09-06 02:14:51.046672	1000069	Y	B43	Bàn - 43	 	999975	ETB	0a0ab94a-7924-459c-80e0-fce5d34ec3e5	2	1	\N	N	\N	N	\N
1000067	1000016	1000004	2024-08-07 11:38:52.114481	1000069	2024-09-16 15:38:20.070432	1000069	Y	B36	Bàn - 36	Bàn	999976	TIU	af61d088-d4c9-4bed-9382-7aafda6d47cd	11	0	\N	N	\N	N	\N
1000071	1000016	1000004	2024-08-07 11:40:17.237248	1000069	2024-09-13 14:11:16.10102	1000069	Y	B40	Bàn - 40	 	999976	TIU	e370d294-4a7d-4231-a79c-b4cf27abfa45	12	0	\N	N	\N	N	\N
1000042	1000016	1000004	2024-08-07 09:20:28.932811	1000069	2024-09-17 14:53:37.725186	1000069	Y	B21	Bàn - 21	 	999973	TIU	a1b4de06-f24f-4dfb-aa6d-c3694dc3dde6	2	1	\N	N	\N	N	\N
1000050	1000016	1000004	2024-08-07 09:21:18.91901	1000069	2024-08-07 09:21:18.919011	1000069	Y	B26	Bàn - 26	 	999974	ETB	855a98c7-deab-41e5-9289-b9929d31958c	2	1	\N	N	\N	N	\N
1000059	1000016	1000004	2024-08-07 09:22:26.393149	1000069	2024-08-07 09:22:26.39315	1000069	Y	B1	Bàn - 01	 	999975	ETB	c3ccd71d-3573-4fe2-a80d-58a70b561622	2	1	\N	N	\N	N	\N
1000048	1000016	1000004	2024-08-07 09:21:11.028458	1000069	2024-08-07 09:21:11.028459	1000069	Y	B25	Bàn - 25	 	999974	ETB	0589b329-bcb6-44df-b11e-3be7a6f4be8e	2	1	\N	N	\N	N	\N
1000045	1000016	1000004	2024-08-07 09:20:41.145141	1000069	2024-08-07 09:20:41.145142	1000069	Y	B23	Bàn - 23	 	999973	ETB	6939b089-c942-4df1-bf79-32e70fa88a4d	2	1	\N	N	\N	N	\N
1000053	1000016	1000004	2024-08-07 09:21:50.536471	1000069	2024-08-07 09:21:50.536472	1000069	Y	B29	Bàn - 29	 	999975	ETB	ff122911-b892-4145-948b-92cfb873036c	2	1	\N	N	\N	N	\N
1000058	1000016	1000004	2024-08-07 09:22:23.268453	1000069	2024-08-07 09:22:23.268455	1000069	Y	B34	Bàn - 34	 	999975	ETB	1a718c98-a179-4ce4-8ea2-86b31b4c0457	2	1	\N	N	\N	N	\N
1000049	1000016	1000004	2024-08-07 09:21:15.19093	1000069	2024-08-26 17:29:58.370092	1000069	Y	B46	Bàn - 46	 	999974	ETB	606e309e-ed54-49ac-a447-586744b55616	3	2	\N	N	\N	N	\N
1000046	1000016	1000004	2024-08-07 09:21:02.045519	1000069	2024-08-26 14:25:32.817972	1000069	Y	B44	Bàn - 44	 	999974	ETB	5d54e191-588d-43ea-94fb-bda8dbf0da57	2	1	\N	N	\N	N	\N
1000055	1000016	1000004	2024-08-07 09:21:59.167518	1000069	2024-08-07 09:21:59.16752	1000069	Y	B31	Bàn - 31	 	999975	ETB	12edb49f-2300-4640-a894-4cc4e5c6c564	2	1	\N	N	\N	N	\N
1000064	1000016	1000004	2024-08-07 11:37:34.030055	1000069	2024-09-17 17:52:02.84418	1000069	Y	B51	Bàn - 51	Bàn trống	999976	TIU	a2b84b5d-9244-41b3-a5e9-5febc861cf9a	12	0	\N	N	\N	N	\N
\.


--
-- TOC entry 5480 (class 0 OID 392962)
-- Dependencies: 339
-- Data for Name: d_table_use_ref; Type: TABLE DATA; Schema: pos; Owner: adempiere
--

COPY pos.d_table_use_ref (d_table_use_ref_id, d_tenant_id, created, created_by, updated, updated_by, is_active, domain_name, d_table_use_ref_uu, d_org_id, d_reference_id, domain_column) FROM stdin;
1000002	0	2024-08-28	0	2024-08-28	0	Y	Product	056609f7-adc4-47ac-ab96-e7aecb28eb3b	0	1000003	productType
1000001	0	2024-08-28	0	2024-08-28	0	Y	Table	7b82e1a0-3881-4184-b75b-68ec01929e7e	0	1000007	tableStatus
1000000	0	2024-08-28	0	2024-08-28	0	Y	ReservationOrder	5797d0cb-6a24-47ad-b42d-83de6fafaa66	0	1000004	status
1000005	0	2024-08-28	0	2024-08-28	0	Y	KitchenOrderLine	b6b43d24-e2c0-4a70-9688-f4d6aa5d1844	0	1000005	orderlineStatus
1000003	0	2024-08-28	0	2024-08-28	0	Y	Product	50ed678a-cf52-41ee-b302-90ff90d9e5e7	0	1000001	groupType
1000004	0	2024-08-28	0	2024-08-28	0	Y	KitchenOrder	c273e034-4acf-4602-b9f0-0b6ee452b30d	0	1000005	orderStatus
\.


--
-- TOC entry 5396 (class 0 OID 385874)
-- Dependencies: 255
-- Data for Name: d_tax; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_tax (d_tax_id, d_tenant_id, d_org_id, name, d_tax_category_id, tax_rate, is_active, is_default, is_saletax, created, created_by, updated, updated_by, d_tax_uu) FROM stdin;
1000002	1000004	1000016	NHÓM VAT8	1000008	1000	Y	N	\N	2024-08-15 17:34:34.769153	1000069	2024-08-29 17:37:32.103595	1000069	95a50523-b8bb-468e-810b-29d028192079
1000003	1000004	1000016	VAT 10	1000008	10	Y	N	\N	2024-08-29 17:31:51.832382	1000069	2024-08-29 17:31:51.832383	1000069	5540d837-5124-4647-86a3-5f3b2d666f30
1000005	1000009	1000022	VAT10	1000008	10	Y	N	\N	2024-09-12 09:21:16.366093	1000071	2024-09-12 09:21:16.366096	1000071	c0d30f9b-73da-43f5-9ff5-a4b86615e4a5
1000004	1000004	1000016	VAT 8	1000008	8	N	N	\N	2024-08-29 17:33:04.015281	1000069	2024-09-18 17:34:39.753258	1000069	fbd4a944-9bf8-4753-91fb-0f025f311c84
\.


--
-- TOC entry 5397 (class 0 OID 385884)
-- Dependencies: 256
-- Data for Name: d_tax_category; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_tax_category (d_tax_category_id, d_tenant_id, d_org_id, name, description, is_active, is_default, created_by, created, updated, updated_by, d_tax_category_uu) FROM stdin;
1000006	1000004	1000016	NHÓM VAT5	This is an example description that can be up to 255 characters long.	N	N	1000069	2024-08-15 11:56:25.631311	2024-08-15 11:56:25.631311	1000069	f4ededc7-3b8a-44fb-9e21-9238092bbb40
1000008	1000004	1000016	NHÓM VAT8	This is an example description that can be up to 255 characters long.	Y	Y	1000069	2024-08-15 12:00:50.381239	2024-08-15 12:01:25.749058	1000069	13b02b85-3005-4baa-9c42-fd2abd75a9a1
\.


--
-- TOC entry 5398 (class 0 OID 385893)
-- Dependencies: 257
-- Data for Name: d_tenant; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_tenant (d_tenant_id, code, name, domain_url, d_industry_id, is_active, tax_code, d_image_id, expired_date, created, created_by, updated, updated_by, d_tenant_uu) FROM stdin;
1000002	ONSEN	Onsen FUJI	https://dbizmobile.ssg.vn:8443/webui/index.zul	1000008	Y	100000	\N	\N	2024-07-03 11:58:43.413357	0	2024-07-22 23:14:07.095951	1000037	63a6ad5f-4812-4c31-a90d-b1b7d8dcdc1e
1000003	DBIZ	DBIZ Tech	https://digitalbiz.com.vn/	1000008	Y	100002	\N	\N	2024-07-12 02:40:30.086812	0	2024-07-23 22:30:32.435023	1000068	58cae7a5-6a68-4571-b522-4c04f7e9fe55
1000004	F&B	F&B Sample	https://dbizmobile.ssg.vn:8443/webui/index.zul	1000008	Y	100001	\N	\N	2024-07-03 11:58:43.413357	0	2024-07-22 23:14:07.095951	1000037	732237ed-fcc9-4c5b-a615-fc86ba0e3c59
0	SYSTEM	SYSTEM	\N	\N	Y	\N	\N	\N	2024-08-24 05:51:46.280949	0	2024-08-24 05:51:46.280949	0	2731f792-90fe-46ff-92fc-dd590981ae28
1000009	THEP	Thép Tây Nam	https://taynamsteel.com/vi/san-pham/	\N	Y	\N	\N	\N	2024-09-10 08:33:04.728475	0	2024-09-10 08:33:04.728475	0	b482aa25-79fa-4d0a-976d-cdf548856b5f
\.


--
-- TOC entry 5399 (class 0 OID 385904)
-- Dependencies: 258
-- Data for Name: d_uom; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_uom (d_uom_id, d_org_id, d_tenant_id, created, created_by, updated, updated_by, code, name, description, d_uom_uu, is_active) FROM stdin;
1000003	1000003	1000003	2024-07-12 03:18:38.917481	0	2024-07-12 03:18:38.917481	0	B	Bộ	\N		Y
1000000	1000001	1000002	2024-07-10 05:11:24.951595	0	2024-07-10 05:11:24.951595	0	C	Cây	\N	bb8511c4-a7d0-472d-a9bb-bfc321423bd2	Y
1000002	1000001	1000002	2024-07-12 03:18:38.917481	0	2024-07-12 03:18:38.917481	0	M	Mét	\N	8bfb3525-e011-446d-8527-7e0e1cf47019	Y
1000009	1000003	1000003	2024-07-12 03:18:38.917481	0	2024-07-12 03:18:38.917481	0	CI	Cái	\N	8bfb3525-e011-446d-8527-7e0e1cf47999	Y
1000010	1000016	1000004	2024-08-04 12:04:41.711988	1000069	2024-08-04 12:04:41.711988	1000069	KG	Kilogram	kilogram	ecae96d3-3e47-4a78-951c-0798d2924589	Y
1000011	1000016	1000004	2024-08-04 12:08:40.663141	1000069	2024-08-04 12:08:40.663141	1000069	Trai	Chai	trên 18+ 	256df28d-e71c-4251-a1e8-59606f9f234d	Y
1000012	1000016	1000004	2024-08-04 12:14:50.883728	1000069	2024-08-04 12:14:50.883728	1000069	Phần	Phần	trên 18+ 	d33432de-316c-4d27-84f7-cb8002fd3842	Y
1000013	1000016	1000004	2024-08-07 09:11:14.255668	1000069	2024-08-07 09:11:14.25567	1000069	Con	Con	 	ec83bd4c-c354-4455-93f8-b187c763a375	Y
1000014	1000016	1000004	2024-08-29 09:20:31.082091	1000069	2024-08-29 09:20:31.082093	1000069	Hủ	Hủ	\N	44f6cc94-21c6-4760-836e-a36412264e58	Y
1000015	1000016	1000004	2024-08-29 09:46:42.867592	1000069	2024-08-29 09:46:42.867593	1000069	Lọ	Lọ	\N	c4d14f18-4817-4b0f-9444-8e409078fc35	Y
1000016	1000022	1000009	2024-09-11 15:06:36.296689	1000071	2024-09-11 15:06:36.296691	1000071	M	M	\N	b3a26123-f33e-441c-ac77-ae836fe962ac	Y
1000017	1000022	1000009	2024-09-11 18:07:41.064878	1000071	2024-09-11 18:07:41.064879	1000071	Cây	Cây	 	49fbc434-00c2-4f51-85f4-29d1c8a815a0	Y
\.


--
-- TOC entry 5469 (class 0 OID 391463)
-- Dependencies: 328
-- Data for Name: d_uom_product; Type: TABLE DATA; Schema: pos; Owner: adempiere
--

COPY pos.d_uom_product (d_uom_product_id, d_tenant_id, created, created_by, updated, updated_by, d_product_id, d_uom_id, conversion_value, costprice, d_uom_product_uu, is_active, d_org_id) FROM stdin;
1000026	1000004	2024-08-30 08:36:41.820147	1000069	2024-08-30 08:36:41.820147	1000069	1000356	1000011	\N	10000	b0c9ef62-51d7-4cf4-b6e6-47480b2404f1	\N	0
1000027	1000004	2024-08-30 08:36:41.892703	1000069	2024-08-30 08:36:41.892703	1000069	1000359	1000011	\N	10000	d5478738-e94c-4e6f-89d0-f99d16b22d14	\N	0
1000028	1000004	2024-09-02 22:44:57.159365	1000069	2024-09-02 22:44:57.159366	1000069	1000368	1000012	\N	100000	bea3986a-39f5-44a7-9a38-e0bdc733fea1	\N	\N
1000029	1000004	2024-09-02 22:44:57.202645	1000069	2024-09-02 22:44:57.202646	1000069	1000371	1000012	1	100000	e9d5ef0b-593f-4732-8e4b-0ed15c784c4d	\N	\N
1000030	1000004	2024-09-02 22:44:57.208718	1000069	2024-09-02 22:44:57.208718	1000069	1000374	1000012	1	100000	2e5868b9-c6d5-44f3-8153-f566d1c6e6f2	\N	\N
1000031	1000004	2024-09-03 22:22:20.630087	1000069	2024-09-03 22:22:20.630087	1000069	1000377	1000012	\N	100000	a76a0d7f-a7a3-4b06-aefd-3d7399e557c1	\N	0
1000032	1000004	2024-09-03 22:22:20.757811	1000069	2024-09-03 22:22:20.757811	1000069	1000380	1000012	1	100000	40b2a339-b5ac-4824-a15d-7bd3174038f7	\N	0
1000033	1000004	2024-09-04 09:23:01.448896	1000069	2024-09-04 09:23:01.448897	1000069	1000383	1000014	\N	100000	9c52c8b3-d4cc-4454-baa6-518ae5d64d0b	\N	0
1000034	1000004	2024-09-04 09:23:01.505928	1000069	2024-09-04 09:23:01.505929	1000069	1000386	1000014	1	100000	a188c9be-c571-46d8-819f-6797d81def92	\N	0
1000035	1000004	2024-09-04 09:23:01.513926	1000069	2024-09-04 09:23:01.513927	1000069	1000389	1000014	1	100000	d66c618b-741e-4b52-a89b-a80042451eba	\N	0
1000036	1000004	2024-09-04 09:47:43.904734	1000069	2024-09-04 09:47:43.904736	1000069	1000392	1000014	\N	100000	67d500a1-3a43-44ad-99f1-b7c5ef45e956	\N	0
1000037	1000004	2024-09-04 09:47:43.968447	1000069	2024-09-04 09:47:43.968448	1000069	1000395	1000014	1	100000	e0a01921-3567-47ed-aecb-9e4a2744fc5d	\N	0
1000038	1000004	2024-09-04 09:47:43.981018	1000069	2024-09-04 09:47:43.981019	1000069	1000398	1000014	1	100000	63c4f1bd-0446-497b-9b6b-a8fcc58390e9	\N	0
1000039	1000004	2024-09-04 09:49:27.130973	1000069	2024-09-04 09:49:27.130974	1000069	1000401	1000012	\N	10000	e76c957e-9641-407e-8bae-f6da5878bc1e	\N	0
1000040	1000004	2024-09-04 09:49:27.143326	1000069	2024-09-04 09:49:27.143327	1000069	1000404	1000012	1	10000	253ff8e8-9dd9-4b67-ad86-4c64be7ef705	\N	0
1000041	1000004	2024-09-04 17:35:06.964717	1000069	2024-09-04 17:35:06.964718	1000069	1000407	1000012	\N	100000	f6aeafee-10fc-4690-b9a9-d6bb4431c7a7	\N	0
1000042	1000004	2024-09-04 17:35:07.157993	1000069	2024-09-04 17:35:07.157994	1000069	1000410	1000012	1	100000	7251cf73-d70b-4476-914e-b05a98ac8b78	\N	0
1000043	1000004	2024-09-05 09:22:46.649248	1000069	2024-09-05 09:22:46.649249	1000069	1000413	1000014	\N	100000	25248085-29f1-45b4-be0a-8b1703b0c43d	\N	0
1000044	1000004	2024-09-05 09:22:46.714691	1000069	2024-09-05 09:22:46.714692	1000069	1000416	1000014	1	100000	ad32262b-825d-437d-b6a2-b964d53c473a	\N	0
1000045	1000004	2024-09-05 09:22:46.72476	1000069	2024-09-05 09:22:46.724761	1000069	1000419	1000014	1	100000	18e0bc0b-0271-4e67-8119-32bd485bb054	\N	0
1000046	1000004	2024-09-05 09:22:46.735206	1000069	2024-09-05 09:22:46.735207	1000069	1000422	1000014	1	100000	72a18141-e50d-4b95-9ba8-7d68ae11094e	\N	0
1000047	1000004	2024-09-05 09:22:46.759363	1000069	2024-09-05 09:22:46.759364	1000069	1000425	1000014	1	100000	285ca3f7-fe6e-4d96-aca3-b355e9fcf533	\N	0
1000048	1000004	2024-09-05 09:22:46.779488	1000069	2024-09-05 09:22:46.779489	1000069	1000428	1000014	1	100000	7dc8f013-8904-4d50-8a1f-7f27abac2070	\N	0
1000049	1000004	2024-09-05 09:22:46.790182	1000069	2024-09-05 09:22:46.790183	1000069	1000431	1000014	1	100000	b7825ace-5a42-4513-9c49-2d3b58e9203d	\N	0
1000050	1000004	2024-09-05 09:22:46.800518	1000069	2024-09-05 09:22:46.800543	1000069	1000434	1000014	1	100000	c9c63700-9e1a-4f9c-a6b4-7dc645806b9a	\N	0
1000051	1000004	2024-09-05 09:22:46.810217	1000069	2024-09-05 09:22:46.810217	1000069	1000437	1000014	1	100000	319a79e1-2c18-4603-81db-4ea43c551aab	\N	0
1000052	1000004	2024-09-05 09:22:46.818848	1000069	2024-09-05 09:22:46.818848	1000069	1000440	1000014	1	100000	7ab688bf-fb76-45d7-bce1-f61353e65e33	\N	0
1000053	1000004	2024-09-05 09:22:46.827214	1000069	2024-09-05 09:22:46.827214	1000069	1000443	1000014	1	100000	9f60ec08-14df-4582-becf-e6342fffdac6	\N	0
1000054	1000004	2024-09-05 09:22:46.834585	1000069	2024-09-05 09:22:46.834585	1000069	1000446	1000014	1	100000	d11d0172-da0b-4980-bc1c-876c9f1e18d2	\N	0
1000055	1000004	2024-09-05 09:22:46.843831	1000069	2024-09-05 09:22:46.843832	1000069	1000449	1000014	1	100000	6f83fefd-0b9b-4137-8b3a-8d038b2f71e7	\N	0
1000056	1000004	2024-09-05 09:22:46.856086	1000069	2024-09-05 09:22:46.856086	1000069	1000452	1000014	1	100000	3391556b-1dbf-43d2-b756-6c539a5db701	\N	0
1000057	1000004	2024-09-05 09:22:46.872058	1000069	2024-09-05 09:22:46.872058	1000069	1000455	1000014	1	100000	f166e83e-3432-4d10-a366-42082bc92533	\N	0
1000058	1000004	2024-09-05 09:22:46.895783	1000069	2024-09-05 09:22:46.895783	1000069	1000458	1000014	1	100000	dcc7fe13-71e3-4910-8b40-3d0b3f35cf82	\N	0
1000059	1000004	2024-09-05 09:22:46.91044	1000069	2024-09-05 09:22:46.91044	1000069	1000461	1000014	1	100000	3d22f243-ac43-423b-a566-a2c52ce22aeb	\N	0
1000060	1000004	2024-09-05 09:22:46.922258	1000069	2024-09-05 09:22:46.922258	1000069	1000464	1000014	1	100000	c953aa26-e063-463a-90b2-c01fd5bec184	\N	0
1000061	1000004	2024-09-05 09:22:46.933347	1000069	2024-09-05 09:22:46.933347	1000069	1000467	1000014	1	100000	16c8beaf-9d1b-4b53-8ff0-e5c6704b4861	\N	0
1000062	1000004	2024-09-05 09:29:12.104902	1000069	2024-09-05 09:29:12.104902	1000069	1000470	1000013	\N	100000	dd4855f8-1be2-4ab6-b65b-25015cfd8b9b	\N	0
1000068	1000009	2024-09-11 16:32:36.649926	1000071	2024-09-11 16:32:36.649928	1000071	1000536	1000016	\N	20000	b0dde186-953d-49da-8622-6f7704d45b81	\N	0
1000083	1000009	2024-09-11 17:57:24.638441	1000071	2024-09-11 17:57:24.638441	1000071	1000562	1000016	\N	40000	f0202f0e-5922-459a-ab6d-e1eabb8cbe76	\N	0
1000084	1000009	2024-09-11 17:59:37.359031	1000071	2024-09-11 17:59:37.359032	1000071	1000565	1000016	\N	30000	3ab743ee-096e-4910-9da4-c9d2a2f004c4	\N	0
1000085	1000009	2024-09-11 18:10:24.899082	1000071	2024-09-11 18:10:24.899082	1000071	1000568	1000017	\N	40000	df1f513f-23e4-45dd-bc83-572c52ece57b	\N	0
1000086	1000009	2024-09-11 18:12:49.984683	1000071	2024-09-11 18:12:49.984683	1000071	1000571	1000017	\N	40000	84396159-79a2-4bd1-b999-050f35ed54bc	\N	0
1000087	1000009	2024-09-11 18:14:23.702356	1000071	2024-09-11 18:14:23.702357	1000071	1000574	1000017	\N	32000	fdda1174-9d30-4cf4-9665-14dcc47055eb	\N	0
1000088	1000009	2024-09-11 18:15:54.635062	1000071	2024-09-11 18:15:54.635062	1000071	1000577	1000017	\N	45000	10bfc37d-a8a4-4127-aa66-5f2d0cb07c88	\N	0
1000098	1000004	2024-09-18 21:18:35.443075	1000069	2024-09-18 21:18:35.443075	1000069	1000622	1000011	\N	10000	1449fea7-fe52-44ac-940c-1443305a3940	\N	0
1000099	1000004	2024-09-18 21:18:35.510556	1000069	2024-09-18 21:18:35.510556	1000069	1000623	1000011	\N	10000	ef5ebbcb-8d3e-4fad-a954-d2e49c0c576d	\N	0
1000100	1000004	2024-09-18 21:53:32.414571	1000069	2024-09-18 21:53:32.414572	1000069	1000626	1000011	\N	10000	d923e5ee-3ff5-4c8f-b4f3-767b6420083f	\N	0
1000101	1000004	2024-09-18 21:53:32.474984	1000069	2024-09-18 21:53:32.474986	1000069	1000627	1000011	\N	10000	c68ed3e8-1140-423f-8f85-137d0a27c2bb	\N	0
1000116	1000004	2024-09-18 23:25:57.893959	1000069	2024-09-18 23:25:57.893959	1000069	1000642	1000014	\N	100000	576ca70c-0184-4b94-9663-d8593260fc9f	\N	0
1000117	1000004	2024-09-18 23:24:42.573973	1000069	2024-09-18 23:24:42.573975	1000069	1000643	1000014	\N	100000	3547fd81-7707-478d-b4b7-da1ed0ed457d	\N	0
1000118	1000004	2024-09-18 23:36:56.486572	1000069	2024-09-18 23:36:56.486574	1000069	1000644	1000012	\N	15000	86d7a5cf-0a25-4b3d-b377-bc4cd158baf5	\N	0
1000121	1000004	2024-09-19 07:14:08.53777	1000069	2024-09-19 07:14:08.53777	1000069	1000651	1000014	\N	100000	0e229767-a0eb-415b-a856-83815448ef05	\N	0
1000122	1000004	2024-09-19 11:10:15.800636	1000069	2024-09-19 11:10:15.800638	1000069	1000654	1000012	\N	10000	5e0c9b16-8e10-4690-a9b6-505ec9701313	\N	0
1000123	1000004	2024-09-19 11:24:58.280726	1000069	2024-09-19 11:24:58.280726	1000069	1000658	1000011	\N	10000	dee8461d-89db-4991-8ffa-4cdf1172f9dd	\N	0
1000124	1000004	2024-09-19 11:24:58.827527	1000069	2024-09-19 11:24:58.827527	1000069	1000659	1000011	\N	10000	6fb724b6-ced2-46b1-8554-f397cb099e4f	\N	0
1000125	1000004	2024-09-19 11:20:59.600427	1000069	2024-09-19 11:20:59.60043	1000069	1000662	1000011	\N	10000	3f1bd775-6f54-4242-bc9a-79699d5e8482	\N	0
1000126	1000004	2024-09-19 11:20:59.644053	1000069	2024-09-19 11:20:59.644055	1000069	1000663	1000011	\N	10000	710b72c0-a835-411c-93ff-a11912bb2be9	\N	0
1000127	1000004	2024-09-19 12:50:00.732906	1000069	2024-09-19 12:50:00.732906	1000069	1000675	1000011	\N	10000	985a92d1-3608-4758-b83f-0a5bbd9a8822	\N	0
1000128	1000004	2024-09-19 12:50:00.82391	1000069	2024-09-19 12:50:00.82391	1000069	1000676	1000011	\N	10000	969c4229-660a-4d7c-95c4-ce3c820e7a66	\N	0
\.


--
-- TOC entry 5400 (class 0 OID 385911)
-- Dependencies: 259
-- Data for Name: d_user; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_user (d_user_id, user_name, full_name, phone, password, d_image_id, email, birth_day, is_active, d_tenant_id, user_pin, is_locked, date_locked, date_last_login, created, created_by, updated, updated_by, d_user_uu) FROM stdin;
1000068	WebService	Dbiz	0000000	$2a$10$KiTO2H9FZ5lQjY3ZSI.wAeZGkQkA4iDXdaYzHeREej36/bqkjeIyS	\N	WebService@gmail.com	\N	Y	1000003	\N	Y	\N	\N	2024-07-08 20:50:15.048367	\N	\N	\N	46676fd0-1b0b-4d0c-b0e0-1a40b69a1780
1000071	WebService	WebService	\N	$2a$10$KiTO2H9FZ5lQjY3ZSI.wAeZGkQkA4iDXdaYzHeREej36/bqkjeIyS	\N	\N	\N	Y	1000009	\N	Y	\N	\N	2024-09-11 04:28:32.150994	0	2024-09-11 04:28:32.150994	0	30937152-dab8-48aa-bcfd-a4ae76185f2b
1000076	thanhnc	\N	\N	$2a$10$kxCdazO6xyuWqaF/88U5FOqgnyAQjn7mRJTnksOD78FThKSe04G8e	\N	\N	\N	Y	\N	\N	Y	\N	\N	2024-09-12 09:59:40.367027	0	2024-09-12 09:59:40.367027	0	09842088-5fda-43c6-9b91-09efb7402b79
1000077	thanhnc	\N	\N	$2a$10$11nwzkys1BlKXykGXl.E/e50jNUmyaNxTGkYXyMpIjturIoNuRCSm	\N	\N	\N	Y	\N	\N	Y	\N	\N	2024-09-12 10:00:38.585751	0	2024-09-12 10:00:38.585751	0	3559602f-37ba-4972-988b-287057968a8f
1000073	vts@gmail.com	VLXD VTS	\N	$2a$10$j6s1Hq0C6MmMI2jJtFc/0eKGb.2EQqH9TmASIybaBozy0smAIOfmG	\N	\N	\N	Y	1000009	\N	Y	\N	\N	2024-09-12 02:13:56.582864	0	2024-09-12 02:13:56.582864	0	1fc6b090-9324-4712-9dab-f3589dc48404
1000072	cuahang1@gmail.com	VLXD DEMO	\N	$2a$10$j6s1Hq0C6MmMI2jJtFc/0eKGb.2EQqH9TmASIybaBozy0smAIOfmG	\N	\N	\N	Y	1000009	\N	Y	\N	\N	2024-09-11 04:28:32.150994	0	2024-09-11 04:28:32.150994	0	fd3b321d-8e1f-48eb-a84e-dcd6f3d01765
1000078	Administrator	Administrator	00000	$2a$10$j6s1Hq0C6MmMI2jJtFc/0eKGb.2EQqH9TmASIybaBozy0smAIOfmG	\N	administrator@gmail.com	\N	Y	1000004	\N	Y	\N	\N	2024-09-18 08:24:39.809855	0	2024-09-18 08:24:39.809855	0	54a03466-98dd-472b-89ca-79d579e4cf49
1000070	thanhnc	Nguyễn Chí Thanh	+843444	$2a$10$j6s1Hq0C6MmMI2jJtFc/0eKGb.2EQqH9TmASIybaBozy0smAIOfmG	\N	chithanh@gmail.com	\N	Y	1000004	\N	Y	\N	\N	2024-07-08 16:41:43.061964	0	\N	0	48520631-a0f3-4e7f-8203-ac329b6167af
1000069	WebService	Dbiz	0000000	$2a$10$KiTO2H9FZ5lQjY3ZSI.wAeZGkQkA4iDXdaYzHeREej36/bqkjeIyS	\N	WebService@gmail.com	\N	Y	1000004	\N	Y	\N	\N	2024-07-08 20:50:15.048367	0	\N	0	b1d7a7c6-c406-4c46-9842-dc62d3476aff
1000001	thanhnc0	Nguyễn Chí Thanh	0384449114	$2a$10$uZCq2CDh7IwGOlnhGnv3hu7qVb31CKZzK31sveCqYtxqzKsxISasu	\N	chithanh03062001@gmail.com	\N	Y	1000002	\N	Y	\N	\N	2024-07-08 02:18:27.065047	0	2024-07-08 02:18:27.065047	0	38215a91-7dab-419b-8716-956a8413ec4a
1000079	User	Thanh	\N	$2a$10$j6s1Hq0C6MmMI2jJtFc/0eKGb.2EQqH9TmASIybaBozy0smAIOfmG	\N	\N	\N	Y	1000004	\N	Y	\N	\N	2024-09-18 08:26:13.736236	0	2024-09-18 08:26:13.736236	0	ef943fc4-3064-43f8-ae56-53eb75930a72
1000037	WebService	Dbiz	0000000	$2a$10$KiTO2H9FZ5lQjY3ZSI.wAeZGkQkA4iDXdaYzHeREej36/bqkjeIyS	\N	WebService@gmail.com	\N	Y	1000002	\N	Y	\N	\N	2024-07-08 20:50:15.048367	\N	\N	\N	d192f6d2-5c17-4ffc-b8e9-29628023b186
1000035	thanhnc	Nguyễn Chí Thanh	+843444	$2a$10$j6s1Hq0C6MmMI2jJtFc/0eKGb.2EQqH9TmASIybaBozy0smAIOfmG	\N	chithanh@gmail.com	\N	Y	1000002	\N	Y	\N	\N	2024-07-08 16:41:43.061964	\N	\N	\N	5821f3eb-aefb-44bf-b306-d01a5ee24b56
1000064	tesst1	Dbiz	0000000	$2a$10$k4g68H1eAZIq6.pOoOAo7eh8QMA.eRU1/itEP5Yd2hCrN24vwQJmW	\N	WebService@gmail.com	\N	Y	1000002	\N	Y	\N	\N	2024-07-11 04:47:47.654848	1000037	\N	\N	f7ff763e-df71-404a-92fc-4a433f5f9c27
1000065	WebService13343	Dbiz	0000000	$2a$10$jSqw17teUbkWMgQ2judmRuTalKZ51rRnnCyBBsp4h/A9gxUvaMYAS	\N	WebService@gmail.com	\N	Y	1000002	\N	Y	\N	\N	2024-07-11 16:28:51.23276	1000037	\N	\N	72bb0769-ab2e-4974-aedd-487fad2ebe5e
1000066	test1	Dbiz	0000000	$2a$10$z7yl/zFZQ3B6x9kwXRbp3OH6NZG76AVPep9sw553w9zIhDZRJWexu	\N	WebService@gmail.com	\N	Y	1000002	\N	Y	\N	\N	2024-07-11 16:29:14.726232	1000037	\N	\N	4cb9995e-d53d-40fe-8799-4ceeaecb89ac
\.


--
-- TOC entry 5402 (class 0 OID 385924)
-- Dependencies: 261
-- Data for Name: d_user_role_access; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_user_role_access (d_user_id, d_role_id, d_tenant_id, created, created_by, updated, updated_by, is_active, d_userrole_access_uu) FROM stdin;
1000070	1000001	1000004	2024-09-17 08:41:35.050677	0	2024-09-17 08:41:35.050677	0	Y	9a593c1d-b452-4d64-b09f-186a40ce09f5
1000079	1000001	1000004	2024-09-18 08:30:33.477116	0	2024-09-18 08:30:33.477116	0	Y	6240a303-8ce4-49e3-a2c5-c60d3fe9fa64
1000079	1000002	1000004	2024-09-18 08:30:33.477116	0	2024-09-18 08:30:33.477116	0	Y	d1317b70-2ae7-416f-ae4b-3c3fb9b4aeb0
\.


--
-- TOC entry 5401 (class 0 OID 385920)
-- Dependencies: 260
-- Data for Name: d_userorg_access; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_userorg_access (d_user_id, d_org_id, d_tenant_id, created, created_by, updated, updated_by, is_active, d_userorg_access_uu) FROM stdin;
1000070	1000016	1000004	2024-09-17 08:44:30.587419	0	2024-09-17 08:44:30.587419	0	Y	58a9391c-807e-417d-a5c2-abaf05d9ad6b
1000079	1000019	1000004	2024-09-18 08:30:02.999322	0	2024-09-18 08:30:02.999322	0	Y	646a3798-03ed-48f9-a94d-d4f0ff48a4e8
1000079	1000030	1000004	2024-09-18 08:30:02.999322	0	2024-09-18 08:30:02.999322	0	Y	9652f739-cbdf-41dd-a2a0-5df619aed9d5
\.


--
-- TOC entry 5403 (class 0 OID 385928)
-- Dependencies: 262
-- Data for Name: d_vendor; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_vendor (d_vendor_id, d_tenant_id, code, name, phone1, phone2, address1, address2, tax_code, email, birthday, d_image_id, created, created_by, updated, updated_by, is_active, d_vendor_uu, debit_amount, d_partner_group_id, description, area, wards) FROM stdin;
1000004	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-12 16:11:22.4493	1000037	2024-07-12 16:11:22.4493	\N	Y	eeebae5a-aaeb-427e-8613-6a2be289d334	\N	\N	\N	\N	\N
1000081	1000004	VEN1000080	Dịch vụ ăn uống	0327657667	\N	\N	\N	\N	nhu@gmsil.com	\N	\N	2024-09-09 11:15:53.182502	1000069	2024-09-09 11:15:53.182503	\N	Y	24890e2e-bcd0-47c2-a369-d04ddbb08aee	\N	1000043	\N	\N	\N
1000007	1000002	vendor_code	Vendor Name21	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-15 00:12:31.355372	1000037	2024-07-15 19:08:44.431691	1000037	Y	b07782bf-b3b6-43d9-9d1a-3be1e114a21f	\N	\N	\N	\N	\N
1000015	1000002	vendor_code	Vendor Name123	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-15 21:36:53.567774	1000037	2024-07-15 21:53:34.88454	1000037	Y	884281d6-fbef-4368-8865-e98fec7e2d61	\N	\N	\N	\N	\N
1000073	1000004	Test1	Như Tuyết	0327647667	\N	\N	\N	\N	vvff	\N	\N	2024-09-06 08:59:37.206512	1000069	2024-09-06 08:59:37.206515	\N	Y	e3df36d9-e001-4ac0-a587-d1115670f03d	\N	\N	\N	\N	\N
1000017	1000002	vendor_code1111	Vendor Name100213	1234567189	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-16 09:13:59.458994	1000037	2024-07-16 10:44:17.445838	1000037	Y	2dcdcba0-e3df-4bd5-a95b-9b8158048efc	\N	\N	\N	\N	\N
1000087	1000009	1000001	VTS	0971902297	\N	\N	\N	\N	\N	\N	\N	2024-09-11 16:24:43.843052	1000071	2024-09-11 16:24:43.843052	1000071	Y	65a9d68d-6e57-4139-b5ee-a34a8e957048	\N	1000062	\N	\N	\N
1000088	1000009	1000002	HSG	0971902290	\N	\N	\N	\N	\N	\N	\N	2024-09-11 16:25:20.452855	1000071	2024-09-11 16:25:20.452855	1000071	Y	9dac38c3-61ce-4a61-a7cc-244ed3b6ab10	\N	1000062	\N	\N	\N
1000077	1000004	VEN1000076	 Nguyễn Thị A	0327656666	\N	\N	\N	\N	\N	\N	\N	2024-09-06 09:01:31.844458	1000069	2024-09-06 09:01:31.84446	\N	Y	030b12a6-37ee-4203-bed1-f0243773396d	\N	\N	\N	\N	\N
1000029	1000002	vendor_code11	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-16 04:16:43.462332	1000037	2024-07-16 04:17:41.433785	1000037	Y	26df1367-314a-4cac-9572-8d135e0a7f6c	\N	\N	\N	\N	\N
1000062	1000002	code0--122--00-	Name Vendor-0	12340056789	98760054321	123 Main St	456 Secondary St	TAX12345	vendorloo@example.com	1985-01-01	\N	2024-07-18 01:31:46.515924	1000037	2024-07-18 01:37:29.501178	1000037	Y	1451e131-f5f4-4b25-9200-acee63984d2c	\N	\N	\N	\N	\N
1000053	1000002	ThanhNC1	Vendor Name	----	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-17 06:55:27.65495	1000037	2024-07-17 06:56:30.858778	1000037	Y	2ebc0a0b-a0c1-47de-a0e0-de9560cce8c5	\N	\N	\N	\N	\N
1000054	1000002	ThanhNC1	Vendor Name	----	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-17 06:56:39.811903	1000037	2024-07-17 06:57:24.80838	1000037	Y	69b73554-fe93-40a6-b0d4-8314f6cd953a	\N	\N	\N	\N	\N
1000037	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-16 06:13:57.365719	1000037	2024-07-16 06:13:57.365719	\N	Y	a29b8d0a-d536-4308-a289-0542bf445603	\N	\N	\N	\N	\N
1000038	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-16 06:21:46.820361	1000037	2024-07-16 06:21:46.820361	\N	Y	d5fe553c-f7a2-42fe-8297-813827b4186d	\N	\N	\N	\N	\N
1000039	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-16 06:34:09.760552	1000037	2024-07-16 06:34:09.760552	\N	Y	1e25b14f-4229-4088-ad81-fe0a152fcc30	\N	\N	\N	\N	\N
1000040	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-16 06:34:14.991286	1000037	2024-07-16 06:34:14.991286	\N	Y	f8357f5a-0def-4a73-80b7-ebc141879673	\N	\N	\N	\N	\N
1000044	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-17 03:45:45.892383	1000037	2024-07-17 03:45:45.892383	\N	Y	999ed181-e0c6-44ea-a83f-e33d28707766	\N	\N	\N	\N	\N
1000045	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-17 03:46:00.063189	1000037	2024-07-17 03:46:00.063189	\N	Y	bdb37b50-4c8f-4d90-aff8-050fbf3948ed	\N	\N	\N	\N	\N
1000056	1000002	ThanhNC1	Vendor Name000	----	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-17 07:04:53.779894	1000037	2024-07-17 07:05:32.019273	1000037	Y	5c95aab8-6bd5-4d6b-ba7b-b2a74fa4bc6f	\N	\N	\N	\N	\N
1000034	1000002	000000-----	Nguyễn Chí Thanh-----	12345678900	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-16 04:40:01.875151	1000037	2024-07-17 06:29:41.698049	1000037	Y	490a1f7b-cf73-4ae5-b14e-3bdfe0e28f58	\N	\N	\N	\N	\N
1000064	1000002	\N	tu1	0898449505	\N	123	\N	123		\N	\N	2024-07-31 21:51:11.455828	1000037	2024-07-31 21:51:11.45583	\N	Y	c81029da-f668-4b33-b2e3-cad2be540448	\N	1000015	\N	\N	\N
1000046	1000002	vendor_code	Vendor Name	----	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-17 06:48:45.223079	1000037	2024-07-17 06:49:32.255573	1000037	Y	7f1315b7-f59b-4d15-9965-e580a6a4b891	\N	\N	\N	\N	\N
1000047	1000002	ThanhNC	Vendor Name	----	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-17 06:49:46.851685	1000037	2024-07-17 06:50:10.335734	1000037	Y	a4e6b07b-66d1-4f8a-9d01-75dbe148e7f4	\N	\N	\N	\N	\N
1000048	1000002	ThanhNC	Vendor Name	----	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-17 06:50:24.431939	1000037	2024-07-17 06:50:44.064851	1000037	Y	b5f00430-74c4-41d3-863f-b9f504bcb7a6	\N	\N	\N	\N	\N
1000049	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-17 06:51:09.178846	1000037	2024-07-17 06:51:09.178846	\N	Y	dede7624-2e25-4855-b570-0efb40a3ed00	\N	\N	\N	\N	\N
1000050	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-17 06:51:33.665385	1000037	2024-07-17 06:51:33.665385	\N	Y	a4d2e51e-c2d3-4fa0-b630-f71bd85a434d	\N	\N	\N	\N	\N
1000051	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-17 06:55:14.748552	1000037	2024-07-17 06:55:14.748552	\N	Y	8117bca1-ac35-4a83-936b-55f356c31ab7	\N	\N	\N	\N	\N
1000052	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-17 06:55:20.53064	1000037	2024-07-17 06:55:20.53064	\N	Y	0abb1cd4-6bdd-4ba4-bcdb-ebac11446024	\N	\N	\N	\N	\N
1000057	1000002	----	Vendor Name000	----	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-17 07:06:22.277152	1000037	2024-07-17 07:07:47.034828	1000037	Y	3489fe23-d603-4408-841d-3e833b44d9c6	\N	\N	\N	\N	\N
1000059	1000002	code0--122--00-	Name Vendor-0	12340056789	98760054321	123 Main St	456 Secondary St	TAX12345	vendorloo@example.com	1985-01-01	\N	2024-07-17 07:35:13.182182	1000037	2024-07-17 07:40:35.767904	1000037	Y	7462d817-c8af-46ea-8618-10c2deb3bcbc	\N	\N	\N	\N	\N
1000041	1000002	code0--122--00-	Name Vendor-0	12340056789	98760054321	123 Main St	456 Secondary St	TAX12345	vendorloo@example.com	1985-01-01	\N	2024-07-17 11:47:45.549384	1000037	2024-07-18 01:26:58.687774	1000037	Y	36be592c-15e1-4cef-a1c0-138fcffe8ed4	\N	\N	\N	\N	\N
1000055	1000002	code0--122--00	Vendor 0	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-17 07:02:30.805355	1000037	2024-07-17 07:34:09.726374	1000037	Y	36588965-9f54-4fce-b5cb-1f0483e0bed9	\N	\N	\N	\N	\N
1000060	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-18 01:28:49.778784	1000037	2024-07-18 01:28:49.778784	\N	Y	2c1cddd9-c140-4414-a403-980a4aae9e04	\N	\N	\N	\N	\N
1000061	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-07-18 01:28:56.662971	1000037	2024-07-18 01:28:56.662971	\N	Y	23baa0ac-2c77-405f-9336-6f4636d5f41e	\N	\N	\N	\N	\N
1000065	1000004	\N	qwe	1234567892	\N		\N			\N	\N	2024-08-12 17:21:40.702678	1000069	2024-08-12 17:21:40.702679	\N	Y	13f354c3-15d2-4729-b7d6-59afc46e007a	\N	1000024		\N	\N
1000058	1000004	code0--122--00-	Name Vendor-0	123400567892	98760054321	123 Main St	456 Secondary St	TAX12345	vendorloo@example.com	1985-01-01	\N	2024-07-17 07:08:33.618995	1000037	2024-07-18 01:36:36.51115	1000037	Y	5bd98f77-4f89-4905-9f00-29c3be9246e0	\N	\N	\N	\N	\N
1000066	1000004	vendor_code	Vendor Name 1	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	\N	2024-08-29 12:56:55.505031	1000069	2024-08-29 12:56:55.505031	1000069	Y	3cf58539-505d-4d1a-8c21-15875c80b42c	10000	1000024		\N	\N
1000079	1000004	VEN1000078	Nguyễn B	221111	\N	\N	\N	\N	\N	\N	\N	2024-09-06 09:02:42.447872	1000069	2024-09-06 09:02:42.447874	\N	Y	68668d7a-b192-4d0f-8502-093c7a384559	\N	1000041	\N	\N	\N
\.


--
-- TOC entry 5446 (class 0 OID 388478)
-- Dependencies: 305
-- Data for Name: d_vendor_audit; Type: TABLE DATA; Schema: pos; Owner: adempiere
--

COPY pos.d_vendor_audit (d_vendor_id, d_tenant_id, code, name, phone1, phone2, address1, address2, tax_code, email, birthday, d_image_id, created, created_by, updated, updated_by, is_active, d_vendor_uu, rev, revtype, id) FROM stdin;
1000015	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	1000091	2024-07-15 21:33:08.280473	\N	2024-07-15 21:33:08.280473	\N	Y	884281d6-fbef-4368-8865-e98fec7e2d61	7	0	100000
1000015	1000002	vendor_code	Vendor Name123	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	1000091	2024-07-15 21:49:49.576444	\N	2024-07-15 21:49:49.576444	\N	Y	884281d6-fbef-4368-8865-e98fec7e2d61	10	1	1000016
1000017	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	1000092	2024-07-16 09:10:14.152636	\N	2024-07-16 09:10:14.152636	\N	Y	2dcdcba0-e3df-4bd5-a95b-9b8158048efc	11	0	1000018
1000017	1000002	vendor_code	Vendor Name123	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	1000092	2024-07-16 09:10:20.151585	\N	2024-07-16 09:10:20.151585	\N	Y	2dcdcba0-e3df-4bd5-a95b-9b8158048efc	12	1	1000019
1000017	1000002	vendor_code0000	Vendor Name123	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	1000092	2024-07-16 09:36:06.755714	\N	2024-07-16 09:36:06.755714	\N	Y	2dcdcba0-e3df-4bd5-a95b-9b8158048efc	13	1	1000020
1000017	1000002	vendor_code1111	Vendor Name123	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	1000092	2024-07-16 09:37:19.733532	\N	2024-07-16 09:37:19.733532	\N	Y	2dcdcba0-e3df-4bd5-a95b-9b8158048efc	14	1	1000021
1000017	1000002	vendor_code1111	Vendor Name10023	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	1000092	2024-07-16 09:38:39.769482	\N	2024-07-16 09:38:39.769482	\N	Y	2dcdcba0-e3df-4bd5-a95b-9b8158048efc	15	1	1000022
1000034	1000002	vendor_code	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	1000104	2024-07-16 04:36:16.339614	\N	2024-07-16 04:36:16.339614	\N	Y	490a1f7b-cf73-4ae5-b14e-3bdfe0e28f58	16	0	1000035
1000034	1000002	vendor_code11	Vendor Name	123456789	987654321	123 Main St	456 Secondary St	TAX12345	vendor@example.com	1985-01-01	1000104	2024-07-16 04:37:04.229923	\N	2024-07-16 04:37:04.229923	\N	Y	490a1f7b-cf73-4ae5-b14e-3bdfe0e28f58	17	1	1000036
\.


--
-- TOC entry 5404 (class 0 OID 385939)
-- Dependencies: 263
-- Data for Name: d_voucher; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_voucher (d_voucher_id, d_tenant_id, d_org_id, voucher_code, amount, expired_date, created, created_by, updated, updated_by, is_active, d_voucher_uu) FROM stdin;
\.


--
-- TOC entry 5405 (class 0 OID 385963)
-- Dependencies: 264
-- Data for Name: d_warehouse; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.d_warehouse (d_warehouse_id, d_org_id, d_tenant_id, created, created_by, updated, updated_by, is_active, code, name, description, address, is_negative, d_warehouse_uu, printer_ip) FROM stdin;
1000002	1000005	1000002	2024-07-21 23:19:15.78506	1000037	2024-07-21 23:19:15.78506	1000037	Y	WH1	Warehouse1	\N	Thảo Nguyên Sài GÒn	Y	8120b747-7248-4fc8-9d2d-0d8347746045	\N
1000003	1000001	1000002	2024-07-21 23:19:23.851718	1000037	2024-07-21 23:19:23.851718	1000037	Y	WH1	Warehouse1	\N	Thảo Nguyên Sài GÒn	Y	78dfa021-3de5-40c2-b2db-306913e97411	\N
1000004	1000003	1000003	2024-07-23 23:44:44.411968	1000068	2024-07-23 23:45:44.657173	1000068	Y	WH122	Warehouse1222	\N	Thảo Nguyên Sài GÒn	Y	0e34b401-9dbe-4cbc-9a15-d484c8c0c4ad	\N
1000005	1000003	1000003	2024-07-23 23:45:57.081644	1000068	2024-07-23 23:45:57.081644	1000068	Y	WH2	Warehouse1	\N	Thảo Nguyên Sài GÒn	Y	46cd43f7-b820-4016-b387-f7a736d5c38a	\N
1000006	1000016	1000004	2024-08-12 10:10:21.820852	1000069	2024-08-12 10:10:21.820852	1000069	Y	WH1	Warehouse1	\N	Thảo Nguyên Sài GÒn	Y	e23afa1e-5f74-4d3a-b0ca-60d94711d527	\N
1000008	0	1000004	2024-08-30 11:53:39.013448	1000069	2024-08-30 11:53:39.013448	1000069	Y	WH1000007	Warehouse1	\N	Thảo Nguyên Sài GÒn	Y	21dfb40f-a81f-41c3-97d0-77c644adf0f1	\N
1000000	1000001	1000002	2024-07-15 18:52:03.355836	1000037	2024-07-15 18:52:03.355836	1000037	Y	WH1	Warehouse1	\N	Thảo Nguyên Sài GÒn	Y	0ccfa2b0-511b-4d72-9eb7-17200db17adc	\N
1000026	1000022	1000009	2024-09-11 17:05:42.749598	1000071	2024-09-11 17:05:42.749599	1000071	Y	WSH0001	Kho hàng ký gửi	\N	\N	Y	75110b2f-c182-45ec-9a20-39c01416dab1	\N
1000027	1000022	1000009	2024-09-12 11:11:09.04166	1000071	2024-09-12 11:11:09.041663	1000071	Y	WSH0002	Kho bán lẻ	\N	\N	Y	23a8c1c3-3b74-4831-8d1a-c5b79c0d1c49	\N
1000033	1000021	1000004	2024-09-18 11:25:08.714904	1000069	2024-09-18 11:25:08.714912	1000069	Y	Kho1	KHO 2	hi	\N	Y	2ca1b730-39e7-46a2-975c-e44daa470113	\N
\.


--
-- TOC entry 5445 (class 0 OID 388439)
-- Dependencies: 304
-- Data for Name: revinfo; Type: TABLE DATA; Schema: pos; Owner: adempiere
--

COPY pos.revinfo (rev, revtstmp, revtype) FROM stdin;
7	1721093813588	\N
10	1721094814892	\N
11	1721096039558	\N
12	1721096045498	\N
13	1721097613694	\N
14	1721097677308	\N
15	1721097771071	\N
8	1721117803491	\N
9	1721117865935	\N
16	1721119201950	\N
17	1721119258164	\N
\.


--
-- TOC entry 5529 (class 0 OID 394978)
-- Dependencies: 406
-- Data for Name: tenants; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.tenants (id, name, db_name, user_name, db_password, creation_status) FROM stdin;
2	TIEN	STORE1	TIEN	123456	CREATED
3	TIEN	STORE1	TIEN	123456	CREATED
4	TIEN	STORE1	TIEN	123456	CREATED
\.


--
-- TOC entry 5528 (class 0 OID 394972)
-- Dependencies: 405
-- Data for Name: user_roles; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.user_roles (user_id, role) FROM stdin;
1	SYS_ADMIN
\.


--
-- TOC entry 5526 (class 0 OID 394963)
-- Dependencies: 403
-- Data for Name: users; Type: TABLE DATA; Schema: pos; Owner: postgres
--

COPY pos.users (id, email, password, tenant_id) FROM stdin;
1	defaultSysAdmin@gmail.com	$2a$12$YZMtG18Z4JPNPyn7WhsdZuncgsvAIbdQjn2fPp/Bx0.iXCyp4V0GS	\N
\.


--
-- TOC entry 5360 (class 0 OID 384914)
-- Dependencies: 219
-- Data for Name: address; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.address (address_id, user_id, full_address, postal_code, city, created_at, updated_at) FROM stdin;
1	1	carthage byrsa	2016	carthage	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893
2	2	carthage byrsa	2016	carthage	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893
3	3	carthage byrsa	2016	carthage	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893
4	4	carthage byrsa	2016	carthage	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893
5	2	kram	2015	kram	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893
6	1	kram	2015	kram	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893
\.


--
-- TOC entry 5351 (class 0 OID 384851)
-- Dependencies: 210
-- Data for Name: carts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.carts (cart_id, user_id, created_at, updated_at) FROM stdin;
1	1	2024-06-26 03:59:12.612293	2024-06-26 03:59:12.612293
2	2	2024-06-26 03:59:12.612293	2024-06-26 03:59:12.612293
3	3	2024-06-26 03:59:12.612293	2024-06-26 03:59:12.612293
4	4	2024-06-26 03:59:12.612293	2024-06-26 03:59:12.612293
\.


--
-- TOC entry 5346 (class 0 OID 384516)
-- Dependencies: 205
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.categories (category_id, parent_category_id, category_title, image_url, created_at, updated_at, d_client_id, d_org_id, create_by, update_by) FROM stdin;
1	\N	Computer	\N	2024-06-23 23:35:58.139564	2024-06-23 23:35:58.139564	\N	\N	\N	\N
2	\N	Mode	\N	2024-06-23 23:35:58.139564	2024-06-23 23:35:58.139564	\N	\N	\N	\N
3	\N	Game	\N	2024-06-23 23:35:58.139564	2024-06-23 23:35:58.139564	\N	\N	\N	\N
37	1	Test Cate2	\N	2024-07-05 15:42:24.821964	\N	10000	00001	selimhorri	\N
\.


--
-- TOC entry 5362 (class 0 OID 384927)
-- Dependencies: 221
-- Data for Name: credentials; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.credentials (credential_id, user_id, username, password, role, is_enabled, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, created_at, updated_at, created_by, updated_by) FROM stdin;
2	2	amineladjimi	$2a$04$8D8OuqPbE4LhRckvtBAHrOmpeWmE92xNNVtyK8Z/lrJFjsImpjBkm	ROLE_USER	t	t	t	t	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893	\N	\N
3	3	omarderouiche	$2a$04$jelNGcF4wFHJirT5Pm7jPO8812QE/3tIWIs1DNnajS68iG4aKUqvS	ROLE_USER	t	t	t	t	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893	\N	\N
4	4	admin	$2a$04$1G4TwSzwf5JwZ4dKCXG1Zu1Qh3WIY9JNaM9vF6Ff05QDfyPg7nSxO	ROLE_USER	t	t	t	t	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893	\N	\N
1	1	selimhorri	$2a$04$/S7cWjHPZul03sPEivycWeHEzX5f4Z950e0O3/QFDFzQhe3I3Izea	ROLE_USER	t	t	t	t	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893	\N	\N
11	18	test	$2a$10$uP0kMWFF2Bwvnq1hpdMSmeeyRd44Ife4mrHp/wD7MTDxtjEmEPNoq	ROLE_USER	t	t	t	t	2024-07-02 03:12:46.883355	2024-07-02 03:12:46.883355	\N	\N
\.


--
-- TOC entry 5512 (class 0 OID 394256)
-- Dependencies: 377
-- Data for Name: databasechangelog; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) FROM stdin;
createTenantsTable	konstde00	liquibase/migrations/main_db/changelog/Tenants.yml	2024-09-10 16:30:14.101498	1	EXECUTED	8:dbe9d0a0c6111ddaf34688287937e193	createTable tableName=tenants		\N	4.5.0	\N	\N	5960445509
createTenantsIdSequence	konstde00	liquibase/migrations/main_db/changelog/Tenants.yml	2024-09-10 16:30:14.24144	2	EXECUTED	8:18059f6bd42b195fb686fe17e7d60c37	createSequence sequenceName=tenants_id_seq		\N	4.5.0	\N	\N	5960445509
createUsersTable	konstde00	liquibase/migrations/main_db/changelog/Users.yml	2024-09-19 09:53:22.165668	3	EXECUTED	8:f316c0eeda4ec13ebbaa5052673ccd60	createTable tableName=users		\N	4.5.0	\N	\N	6714228322
createUsersIdSequence	konstde00	liquibase/migrations/main_db/changelog/Users.yml	2024-09-19 09:53:22.230743	4	EXECUTED	8:8829524685fdad218cb831daea089e2b	createSequence sequenceName=users_id_seq		\N	4.5.0	\N	\N	6714228322
addDefaultValueForUserId	konstde00	liquibase/migrations/main_db/changelog/Users.yml	2024-09-19 09:53:22.279656	5	EXECUTED	8:999e2341a7df3453426e70debb8ce082	addDefaultValue columnName=id, tableName=users		\N	4.5.0	\N	\N	6714228322
createDefaultSysAdmin	konstde00	liquibase/migrations/main_db/changelog/Users.yml	2024-09-19 09:53:22.317771	6	EXECUTED	8:6a9a48a388310e43cb3a0998269e6852	insert tableName=users		\N	4.5.0	\N	\N	6714228322
createUserRolesTable	konstde00	liquibase/migrations/main_db/changelog/UserRoles.yml	2024-09-19 09:53:22.358744	7	EXECUTED	8:d8e9b106d1404a96e94422611190ebb1	createTable tableName=user_roles		\N	4.5.0	\N	\N	6714228322
addRolesForDefaultSysAdmin	konstde00	liquibase/migrations/main_db/changelog/UserRoles.yml	2024-09-19 09:53:22.421726	8	EXECUTED	8:0dfef956764c5ab2a97401f0e925d719	insert tableName=user_roles		\N	4.5.0	\N	\N	6714228322
createTenantsTable	dbiz	liquibase/migrations/main_db/changelog/Tenants.yml	2024-09-19 10:03:58.353552	9	EXECUTED	8:544667fa144a120216c444f688997d98	createTable tableName=tenants		\N	4.5.0	\N	\N	6714864241
createTenantsIdSequence	dbiz	liquibase/migrations/main_db/changelog/Tenants.yml	2024-09-19 10:03:58.453881	10	EXECUTED	8:c6cb230129c04560805b73e4d3208f23	createSequence sequenceName=tenants_id_seq		\N	4.5.0	\N	\N	6714864241
\.


--
-- TOC entry 5511 (class 0 OID 394251)
-- Dependencies: 376
-- Data for Name: databasechangeloglock; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.databasechangeloglock (id, locked, lockgranted, lockedby) FROM stdin;
1	f	\N	\N
\.


--
-- TOC entry 5349 (class 0 OID 384841)
-- Dependencies: 208
-- Data for Name: favourites; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.favourites (user_id, product_id, like_date, created_at, updated_at) FROM stdin;
1	1	2024-06-26 03:57:29.869678	2024-06-26 03:57:29.869678	2024-06-26 03:57:29.869678
1	2	2024-06-26 03:57:29.869678	2024-06-26 03:57:29.869678	2024-06-26 03:57:29.869678
2	2	2024-06-26 03:57:29.869678	2024-06-26 03:57:29.869678	2024-06-26 03:57:29.869678
\.


--
-- TOC entry 5344 (class 0 OID 384504)
-- Dependencies: 203
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	create categories table	SQL	V1__create_categories_table.sql	-2019848469	postgres	2024-06-23 23:35:58.011566	50	t
2	2	insert categories table	SQL	V2__insert_categories_table.sql	435827009	postgres	2024-06-23 23:35:58.139564	36	t
3	3	create products table	SQL	V3__create_products_table.sql	1439033602	postgres	2024-06-23 23:35:58.254625	50	t
4	4	insert products table	SQL	V4__insert_products_table.sql	-1691706989	postgres	2024-06-23 23:35:58.37158	33	t
5	5	create categories parent category id fk	SQL	V5__create_categories_parent_category_id_fk.sql	1755218599	postgres	2024-06-23 23:35:58.469604	36	t
6	6	create products category id fk	SQL	V6__create_products_category_id_fk.sql	-507781750	postgres	2024-06-23 23:35:58.573546	34	t
\.


--
-- TOC entry 5356 (class 0 OID 384889)
-- Dependencies: 215
-- Data for Name: order_items; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.order_items (product_id, order_id, ordered_quantity, created_at, updated_at) FROM stdin;
1	1	2	2024-06-26 04:08:58.783126	2024-06-26 04:08:58.783126
1	2	1	2024-06-26 04:08:58.783126	2024-06-26 04:08:58.783126
2	1	1	2024-06-26 04:08:58.783126	2024-06-26 04:08:58.783126
2	2	1	2024-06-26 04:08:58.783126	2024-06-26 04:08:58.783126
\.


--
-- TOC entry 5353 (class 0 OID 384861)
-- Dependencies: 212
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orders (order_id, cart_id, order_date, order_desc, order_fee, created_at, updated_at) FROM stdin;
1	1	2024-06-26 04:00:30.284384	init	5000	2024-06-26 04:00:30.284384	2024-06-26 04:00:30.284384
2	2	2024-06-26 04:00:30.284384	init	5000	2024-06-26 04:00:30.284384	2024-06-26 04:00:30.284384
3	3	2024-06-26 04:00:30.284384	init	5000	2024-06-26 04:00:30.284384	2024-06-26 04:00:30.284384
4	4	2024-06-26 04:00:30.284384	init	5000	2024-06-26 04:00:30.284384	2024-06-26 04:00:30.284384
\.


--
-- TOC entry 5355 (class 0 OID 384881)
-- Dependencies: 214
-- Data for Name: payments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.payments (payment_id, order_id, is_payed, payment_status, created_at, updated_at) FROM stdin;
1	1	f	IN_PROGRESS	2024-06-26 04:07:45.91289	2024-06-26 04:07:45.91289
2	2	f	IN_PROGRESS	2024-06-26 04:07:45.91289	2024-06-26 04:07:45.91289
3	3	f	IN_PROGRESS	2024-06-26 04:07:45.91289	2024-06-26 04:07:45.91289
4	4	f	IN_PROGRESS	2024-06-26 04:07:45.91289	2024-06-26 04:07:45.91289
\.


--
-- TOC entry 5348 (class 0 OID 384529)
-- Dependencies: 207
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.products (product_id, category_id, product_title, image_url, sku, price_unit, quantity, created_at, updated_at) FROM stdin;
1	1	asus	xxx	dfqejklejrkn	0	50	2024-06-23 23:35:58.37158	2024-06-23 23:35:58.37158
2	1	hp	xxx	zsejfedbjh	0	50	2024-06-23 23:35:58.37158	2024-06-23 23:35:58.37158
3	2	Armani	xxx	fjdvf	0	50	2024-06-23 23:35:58.37158	2024-06-23 23:35:58.37158
4	3	GTA	xxx	qsdkjnvfrekjrf	0	50	2024-06-23 23:35:58.37158	2024-06-23 23:35:58.37158
\.


--
-- TOC entry 5447 (class 0 OID 388537)
-- Dependencies: 306
-- Data for Name: revinfo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.revinfo (rev, revtstmp) FROM stdin;
\.


--
-- TOC entry 5513 (class 0 OID 394262)
-- Dependencies: 378
-- Data for Name: tenants; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tenants (id, name, db_name, user_name, db_password, creation_status) FROM stdin;
\.


--
-- TOC entry 5358 (class 0 OID 384898)
-- Dependencies: 217
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (user_id, first_name, last_name, image_url, email, phone, created_at, updated_at, updated_by, created_by) FROM stdin;
1	selim	horri	https://bootdey.com/img/Content/avatar/avatar7.png	springxyzabcboot@gmail.com	+21622125144	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893	\N	\N
2	amine	ladjimi	https://bootdey.com/img/Content/avatar/avatar7.png	springxyzabcboot@gmail.com	+21622125144	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893	\N	\N
3	omar	derouiche	https://bootdey.com/img/Content/avatar/avatar7.png	springxyzabcboot@gmail.com	+21622125144	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893	\N	\N
4	admin	admin	https://bootdey.com/img/Content/avatar/avatar7.png	springxyzabcboot@gmail.com	+21622125144	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893	\N	\N
11	selim	horri	https://bootdey.com/img/Content/avatar/avatar7.png	springxyzabcboot@gmail.com	+21622125144	2024-07-01 23:38:36.2108	2024-07-01 23:38:36.2108	\N	\N
12	selim	horri	https://bootdey.com/img/Content/avatar/avatar7.png	springxyzabcboot@gmail.com	+21622125144	2024-07-01 23:38:54.827579	2024-07-01 23:38:54.827579	\N	\N
14	test	test	https://bootdey.com/img/Content/avatar/avatar7.png	springxyzabcboot@gmail.com	+21622125144	2024-07-02 00:59:54.891598	2024-07-02 00:59:54.891598	\N	\N
15	test	test	https://bootdey.com/img/Content/avatar/avatar7.png	springxyzabcboot@gmail.com	+21622125144	2024-07-02 01:02:44.593423	2024-07-02 01:02:44.593423	\N	\N
17	test	test	https://bootdey.com/img/Content/avatar/avatar7.png	springxyzabcboot@gmail.com	+21622125144	2024-07-02 02:45:07.330563	2024-07-02 02:45:07.330563	\N	\N
18	test	test	https://bootdey.com/img/Content/avatar/avatar7.png	springxyzabcboot@gmail.com	+21622125144	2024-07-02 03:12:46.707819	2024-07-02 03:12:46.707819	\N	\N
29	test	test	https://bootdey.com/img/Content/avatar/avatar7.png	springxyzabcboot@gmail.com	+21622125144	2024-07-04 06:57:36.252436	\N	\N	\N
31	test	test	https://bootdey.com/img/Content/avatar/avatar7.png	springxyzabcboot@gmail.com	+21622125144	2024-07-04 07:04:02.178727	\N	\N	\N
\.


--
-- TOC entry 5364 (class 0 OID 384944)
-- Dependencies: 223
-- Data for Name: verification_tokens; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.verification_tokens (verification_token_id, credential_id, verif_token, expire_date, created_at, updated_at) FROM stdin;
1	1		2021-12-31	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893
2	2		2021-12-31	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893
3	3		2021-12-31	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893
4	4		2021-12-31	2024-06-26 04:12:33.129893	2024-06-26 04:12:33.129893
\.


--
-- TOC entry 5548 (class 0 OID 0)
-- Dependencies: 401
-- Name: d_api_trace_log_sq; Type: SEQUENCE SET; Schema: pos; Owner: adempiere
--

SELECT pg_catalog.setval('pos.d_api_trace_log_sq', 1000000, false);


--
-- TOC entry 5549 (class 0 OID 0)
-- Dependencies: 330
-- Name: d_assign_org_sq; Type: SEQUENCE SET; Schema: pos; Owner: adempiere
--

SELECT pg_catalog.setval('pos.d_assign_org_sq', 1000106, true);


--
-- TOC entry 5550 (class 0 OID 0)
-- Dependencies: 332
-- Name: d_attribute_sq; Type: SEQUENCE SET; Schema: pos; Owner: adempiere
--

SELECT pg_catalog.setval('pos.d_attribute_sq', 1000010, true);


--
-- TOC entry 5551 (class 0 OID 0)
-- Dependencies: 350
-- Name: d_attribute_value_sq; Type: SEQUENCE SET; Schema: pos; Owner: adempiere
--

SELECT pg_catalog.setval('pos.d_attribute_value_sq', 1000001, true);


--
-- TOC entry 5552 (class 0 OID 0)
-- Dependencies: 352
-- Name: d_bank_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_bank_sq', 1000064, true);


--
-- TOC entry 5553 (class 0 OID 0)
-- Dependencies: 354
-- Name: d_bankaccount_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_bankaccount_sq', 1000002, true);


--
-- TOC entry 5554 (class 0 OID 0)
-- Dependencies: 326
-- Name: d_cancel_reason_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_cancel_reason_sq', 1000013, true);


--
-- TOC entry 5555 (class 0 OID 0)
-- Dependencies: 265
-- Name: d_changelog_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_changelog_sq', 1000195, true);


--
-- TOC entry 5556 (class 0 OID 0)
-- Dependencies: 266
-- Name: d_config_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_config_sq', 1000006, true);


--
-- TOC entry 5557 (class 0 OID 0)
-- Dependencies: 336
-- Name: d_coupon_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_coupon_sq', 1000000, false);


--
-- TOC entry 5558 (class 0 OID 0)
-- Dependencies: 267
-- Name: d_currency_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_currency_sq', 1000000, true);


--
-- TOC entry 5559 (class 0 OID 0)
-- Dependencies: 268
-- Name: d_customer_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_customer_sq', 1000096, true);


--
-- TOC entry 5560 (class 0 OID 0)
-- Dependencies: 269
-- Name: d_doctype_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_doctype_sq', 1000006, true);


--
-- TOC entry 5561 (class 0 OID 0)
-- Dependencies: 356
-- Name: d_erp_integration_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_erp_integration_sq', 1000010, true);


--
-- TOC entry 5562 (class 0 OID 0)
-- Dependencies: 271
-- Name: d_expense_category_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_expense_category_sq', 1000000, false);


--
-- TOC entry 5563 (class 0 OID 0)
-- Dependencies: 270
-- Name: d_expense_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_expense_sq', 1000000, false);


--
-- TOC entry 5564 (class 0 OID 0)
-- Dependencies: 272
-- Name: d_floor_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_floor_sq', 1000042, true);


--
-- TOC entry 5565 (class 0 OID 0)
-- Dependencies: 273
-- Name: d_image_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_image_sq', 1000400, true);


--
-- TOC entry 5566 (class 0 OID 0)
-- Dependencies: 274
-- Name: d_industry_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_industry_sq', 1000015, true);


--
-- TOC entry 5567 (class 0 OID 0)
-- Dependencies: 372
-- Name: d_integration_history_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_integration_history_sq', 1000031, true);


--
-- TOC entry 5568 (class 0 OID 0)
-- Dependencies: 275
-- Name: d_invoice_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_invoice_sq', 1000121, true);


--
-- TOC entry 5569 (class 0 OID 0)
-- Dependencies: 276
-- Name: d_invoiceline_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_invoiceline_sq', 1000168, true);


--
-- TOC entry 5570 (class 0 OID 0)
-- Dependencies: 318
-- Name: d_kitchen_order_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_kitchen_order_sq', 1000142, true);


--
-- TOC entry 5571 (class 0 OID 0)
-- Dependencies: 320
-- Name: d_kitchen_orderline_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_kitchen_orderline_sq', 1000156, true);


--
-- TOC entry 5572 (class 0 OID 0)
-- Dependencies: 277
-- Name: d_language_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_language_sq', 1000000, false);


--
-- TOC entry 5573 (class 0 OID 0)
-- Dependencies: 278
-- Name: d_locator_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_locator_sq', 1000020, true);


--
-- TOC entry 5574 (class 0 OID 0)
-- Dependencies: 322
-- Name: d_note_group_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_note_group_sq', 1000007, true);


--
-- TOC entry 5575 (class 0 OID 0)
-- Dependencies: 324
-- Name: d_note_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_note_sq', 1000007, true);


--
-- TOC entry 5576 (class 0 OID 0)
-- Dependencies: 279
-- Name: d_notification_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_notification_sq', 1000000, false);


--
-- TOC entry 5577 (class 0 OID 0)
-- Dependencies: 280
-- Name: d_order_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_order_sq', 1000078, true);


--
-- TOC entry 5578 (class 0 OID 0)
-- Dependencies: 281
-- Name: d_orderline_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_orderline_sq', 1000142, true);


--
-- TOC entry 5579 (class 0 OID 0)
-- Dependencies: 282
-- Name: d_org_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_org_sq', 1000032, true);


--
-- TOC entry 5580 (class 0 OID 0)
-- Dependencies: 314
-- Name: d_partner_group_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_partner_group_sq', 1000063, true);


--
-- TOC entry 5581 (class 0 OID 0)
-- Dependencies: 368
-- Name: d_pay_method_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_pay_method_sq', 1000010, true);


--
-- TOC entry 5582 (class 0 OID 0)
-- Dependencies: 283
-- Name: d_payment_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_payment_sq', 1000112, true);


--
-- TOC entry 5583 (class 0 OID 0)
-- Dependencies: 370
-- Name: d_paymethod_org_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_paymethod_org_sq', 1000009, true);


--
-- TOC entry 5584 (class 0 OID 0)
-- Dependencies: 348
-- Name: d_pc_terminalaccess_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_pc_terminalaccess_sq', 1000000, false);


--
-- TOC entry 5585 (class 0 OID 0)
-- Dependencies: 284
-- Name: d_pos_order_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_pos_order_sq', 1000509, true);


--
-- TOC entry 5586 (class 0 OID 0)
-- Dependencies: 285
-- Name: d_pos_orderline_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_pos_orderline_sq', 1000423, true);


--
-- TOC entry 5587 (class 0 OID 0)
-- Dependencies: 344
-- Name: d_pos_payment_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_pos_payment_sq', 1000084, true);


--
-- TOC entry 5588 (class 0 OID 0)
-- Dependencies: 346
-- Name: d_pos_taxline_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_pos_taxline_sq', 1000050, true);


--
-- TOC entry 5589 (class 0 OID 0)
-- Dependencies: 334
-- Name: d_pos_terminal_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_pos_terminal_sq', 1000010, true);


--
-- TOC entry 5590 (class 0 OID 0)
-- Dependencies: 312
-- Name: d_pricelist_org_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_pricelist_org_sq', 1000070, true);


--
-- TOC entry 5591 (class 0 OID 0)
-- Dependencies: 310
-- Name: d_pricelist_product_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_pricelist_product_sq', 1000296, true);


--
-- TOC entry 5592 (class 0 OID 0)
-- Dependencies: 308
-- Name: d_pricelist_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_pricelist_sq', 1000059, true);


--
-- TOC entry 5593 (class 0 OID 0)
-- Dependencies: 316
-- Name: d_print_report_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_print_report_sq', 1000017, true);


--
-- TOC entry 5594 (class 0 OID 0)
-- Dependencies: 287
-- Name: d_product_category_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_product_category_sq', 1000034, true);


--
-- TOC entry 5595 (class 0 OID 0)
-- Dependencies: 288
-- Name: d_product_combo_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_product_combo_sq', 1000022, true);


--
-- TOC entry 5596 (class 0 OID 0)
-- Dependencies: 342
-- Name: d_product_location_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_product_location_sq', 1000000, false);


--
-- TOC entry 5597 (class 0 OID 0)
-- Dependencies: 286
-- Name: d_product_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_product_sq', 1000679, true);


--
-- TOC entry 5598 (class 0 OID 0)
-- Dependencies: 358
-- Name: d_production_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_production_sq', 1000000, false);


--
-- TOC entry 5599 (class 0 OID 0)
-- Dependencies: 360
-- Name: d_productionline_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_productionline_sq', 1000000, false);


--
-- TOC entry 5600 (class 0 OID 0)
-- Dependencies: 380
-- Name: d_purchase_order_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_purchase_order_sq', 1000047, true);


--
-- TOC entry 5601 (class 0 OID 0)
-- Dependencies: 382
-- Name: d_purchase_orderline_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_purchase_orderline_sq', 1000048, true);


--
-- TOC entry 5602 (class 0 OID 0)
-- Dependencies: 362
-- Name: d_reconciledetail_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_reconciledetail_sq', 1000021, true);


--
-- TOC entry 5603 (class 0 OID 0)
-- Dependencies: 290
-- Name: d_reference_list_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_reference_list_sq', 1000053, true);


--
-- TOC entry 5604 (class 0 OID 0)
-- Dependencies: 289
-- Name: d_reference_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_reference_sq', 1000013, true);


--
-- TOC entry 5605 (class 0 OID 0)
-- Dependencies: 388
-- Name: d_request_order_sq; Type: SEQUENCE SET; Schema: pos; Owner: adempiere
--

SELECT pg_catalog.setval('pos.d_request_order_sq', 1000004, true);


--
-- TOC entry 5606 (class 0 OID 0)
-- Dependencies: 389
-- Name: d_request_orderline_sq; Type: SEQUENCE SET; Schema: pos; Owner: adempiere
--

SELECT pg_catalog.setval('pos.d_request_orderline_sq', 1000004, true);


--
-- TOC entry 5607 (class 0 OID 0)
-- Dependencies: 291
-- Name: d_reservation_line_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_reservation_line_sq', 1000000, false);


--
-- TOC entry 5608 (class 0 OID 0)
-- Dependencies: 292
-- Name: d_reservation_order_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_reservation_order_sq', 1000161, true);


--
-- TOC entry 5609 (class 0 OID 0)
-- Dependencies: 293
-- Name: d_role_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_role_sq', 1000002, true);


--
-- TOC entry 5610 (class 0 OID 0)
-- Dependencies: 340
-- Name: d_shift_control_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_shift_control_sq', 1000000, false);


--
-- TOC entry 5611 (class 0 OID 0)
-- Dependencies: 294
-- Name: d_storage_onhand_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_storage_onhand_sq', 1000000, false);


--
-- TOC entry 5612 (class 0 OID 0)
-- Dependencies: 295
-- Name: d_table_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_table_sq', 1000105, true);


--
-- TOC entry 5613 (class 0 OID 0)
-- Dependencies: 338
-- Name: d_table_use_ref_sq; Type: SEQUENCE SET; Schema: pos; Owner: adempiere
--

SELECT pg_catalog.setval('pos.d_table_use_ref_sq', 1000005, true);


--
-- TOC entry 5614 (class 0 OID 0)
-- Dependencies: 297
-- Name: d_tax_category_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_tax_category_sq', 1000008, true);


--
-- TOC entry 5615 (class 0 OID 0)
-- Dependencies: 296
-- Name: d_tax_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_tax_sq', 1000005, true);


--
-- TOC entry 5616 (class 0 OID 0)
-- Dependencies: 298
-- Name: d_tenant_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_tenant_sq', 1000009, true);


--
-- TOC entry 5617 (class 0 OID 0)
-- Dependencies: 329
-- Name: d_uom_product_sq; Type: SEQUENCE SET; Schema: pos; Owner: adempiere
--

SELECT pg_catalog.setval('pos.d_uom_product_sq', 1000128, true);


--
-- TOC entry 5618 (class 0 OID 0)
-- Dependencies: 299
-- Name: d_uom_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_uom_sq', 1000017, true);


--
-- TOC entry 5619 (class 0 OID 0)
-- Dependencies: 300
-- Name: d_user_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_user_sq', 1000079, true);


--
-- TOC entry 5620 (class 0 OID 0)
-- Dependencies: 301
-- Name: d_vendor_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_vendor_sq', 1000088, true);


--
-- TOC entry 5621 (class 0 OID 0)
-- Dependencies: 302
-- Name: d_voucher_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_voucher_sq', 1000000, false);


--
-- TOC entry 5622 (class 0 OID 0)
-- Dependencies: 303
-- Name: d_warehouse_sq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.d_warehouse_sq', 1000033, true);


--
-- TOC entry 5623 (class 0 OID 0)
-- Dependencies: 307
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: pos; Owner: adempiere
--

SELECT pg_catalog.setval('pos.hibernate_sequence', 17, true);


--
-- TOC entry 5624 (class 0 OID 0)
-- Dependencies: 407
-- Name: tenants_id_seq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.tenants_id_seq', 4, true);


--
-- TOC entry 5625 (class 0 OID 0)
-- Dependencies: 404
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: pos; Owner: postgres
--

SELECT pg_catalog.setval('pos.users_id_seq', 3, false);


--
-- TOC entry 5626 (class 0 OID 0)
-- Dependencies: 218
-- Name: address_address_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.address_address_id_seq', 6, true);


--
-- TOC entry 5627 (class 0 OID 0)
-- Dependencies: 209
-- Name: carts_cart_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.carts_cart_id_seq', 4, true);


--
-- TOC entry 5628 (class 0 OID 0)
-- Dependencies: 204
-- Name: categories_category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.categories_category_id_seq', 37, true);


--
-- TOC entry 5629 (class 0 OID 0)
-- Dependencies: 220
-- Name: credentials_credential_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.credentials_credential_id_seq', 16, true);


--
-- TOC entry 5630 (class 0 OID 0)
-- Dependencies: 211
-- Name: orders_order_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.orders_order_id_seq', 4, true);


--
-- TOC entry 5631 (class 0 OID 0)
-- Dependencies: 213
-- Name: payments_payment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.payments_payment_id_seq', 4, true);


--
-- TOC entry 5632 (class 0 OID 0)
-- Dependencies: 206
-- Name: products_product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.products_product_id_seq', 4, true);


--
-- TOC entry 5633 (class 0 OID 0)
-- Dependencies: 379
-- Name: tenants_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tenants_id_seq', 2, false);


--
-- TOC entry 5634 (class 0 OID 0)
-- Dependencies: 216
-- Name: users_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_user_id_seq', 31, true);


--
-- TOC entry 5635 (class 0 OID 0)
-- Dependencies: 222
-- Name: verification_tokens_verification_token_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.verification_tokens_verification_token_id_seq', 4, true);


--
-- TOC entry 4750 (class 2606 OID 385488)
-- Name: d_changelog D_ChangeLog_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_changelog
    ADD CONSTRAINT "D_ChangeLog_pkey" PRIMARY KEY (d_changelog_id);


--
-- TOC entry 4754 (class 2606 OID 385498)
-- Name: d_config D_Config_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_config
    ADD CONSTRAINT "D_Config_pkey" PRIMARY KEY (d_config_id);


--
-- TOC entry 4952 (class 2606 OID 392923)
-- Name: d_coupon D_Coupon_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_coupon
    ADD CONSTRAINT "D_Coupon_pkey" PRIMARY KEY (d_coupon_id);


--
-- TOC entry 4757 (class 2606 OID 385506)
-- Name: d_currency D_Currency_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_currency
    ADD CONSTRAINT "D_Currency_pkey" PRIMARY KEY (d_currency_id);


--
-- TOC entry 4760 (class 2606 OID 385520)
-- Name: d_customer D_Customer_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_customer
    ADD CONSTRAINT "D_Customer_pkey" PRIMARY KEY (d_customer_id);


--
-- TOC entry 4766 (class 2606 OID 385532)
-- Name: d_doctype D_DocType_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_doctype
    ADD CONSTRAINT "D_DocType_pkey" PRIMARY KEY (d_doctype_id);


--
-- TOC entry 4773 (class 2606 OID 385550)
-- Name: d_expense_category D_Expense_Category_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_expense_category
    ADD CONSTRAINT "D_Expense_Category_pkey" PRIMARY KEY (d_expense_category_id);


--
-- TOC entry 4771 (class 2606 OID 385542)
-- Name: d_expense D_Expense_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_expense
    ADD CONSTRAINT "D_Expense_pkey" PRIMARY KEY (d_expense_id);


--
-- TOC entry 4778 (class 2606 OID 385562)
-- Name: d_floor D_Floor_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_floor
    ADD CONSTRAINT "D_Floor_pkey" PRIMARY KEY (d_floor_id);


--
-- TOC entry 4786 (class 2606 OID 385573)
-- Name: d_image D_Image_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_image
    ADD CONSTRAINT "D_Image_pkey" PRIMARY KEY (d_image_id);


--
-- TOC entry 4790 (class 2606 OID 385584)
-- Name: d_industry D_Industry_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_industry
    ADD CONSTRAINT "D_Industry_pkey" PRIMARY KEY (d_industry_id);


--
-- TOC entry 4798 (class 2606 OID 385622)
-- Name: d_invoiceline D_InvoiceLine_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_invoiceline
    ADD CONSTRAINT "D_InvoiceLine_pkey" PRIMARY KEY (d_invoiceline_id);


--
-- TOC entry 4794 (class 2606 OID 385594)
-- Name: d_invoice D_Invoice_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_invoice
    ADD CONSTRAINT "D_Invoice_pkey" PRIMARY KEY (d_invoice_id);


--
-- TOC entry 4802 (class 2606 OID 385629)
-- Name: d_language D_Language_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_language
    ADD CONSTRAINT "D_Language_pkey" PRIMARY KEY (d_language_id);


--
-- TOC entry 4806 (class 2606 OID 385640)
-- Name: d_locator D_Locator_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_locator
    ADD CONSTRAINT "D_Locator_pkey" PRIMARY KEY (d_locator_id);


--
-- TOC entry 4811 (class 2606 OID 385649)
-- Name: d_notification D_Notification_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_notification
    ADD CONSTRAINT "D_Notification_pkey" PRIMARY KEY (d_notification_id);


--
-- TOC entry 4817 (class 2606 OID 385710)
-- Name: d_orderline D_OrderLine_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_orderline
    ADD CONSTRAINT "D_OrderLine_pkey" PRIMARY KEY (d_orderline_id);


--
-- TOC entry 4814 (class 2606 OID 385691)
-- Name: d_order D_Order_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_order
    ADD CONSTRAINT "D_Order_pkey" PRIMARY KEY (d_order_id);


--
-- TOC entry 4819 (class 2606 OID 385724)
-- Name: d_org D_Org_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_org
    ADD CONSTRAINT "D_Org_pkey" PRIMARY KEY (d_org_id);


--
-- TOC entry 4829 (class 2606 OID 385761)
-- Name: d_pos_orderline D_POS_OrderLine_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_orderline
    ADD CONSTRAINT "D_POS_OrderLine_pkey" PRIMARY KEY (d_pos_orderline_id);


--
-- TOC entry 4826 (class 2606 OID 385751)
-- Name: d_pos_order D_POS_Order_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_order
    ADD CONSTRAINT "D_POS_Order_pkey" PRIMARY KEY (d_pos_order_id);


--
-- TOC entry 4949 (class 2606 OID 392894)
-- Name: d_pos_terminal D_POS_Terminal_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_terminal
    ADD CONSTRAINT "D_POS_Terminal_pkey" PRIMARY KEY (d_pos_terminal_id);


--
-- TOC entry 4823 (class 2606 OID 385736)
-- Name: d_payment D_Payment_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_payment
    ADD CONSTRAINT "D_Payment_pkey" PRIMARY KEY (d_payment_id);


--
-- TOC entry 4838 (class 2606 OID 385784)
-- Name: d_product_category D_Product_Category_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product_category
    ADD CONSTRAINT "D_Product_Category_pkey" PRIMARY KEY (d_product_category_id);


--
-- TOC entry 4841 (class 2606 OID 385791)
-- Name: d_product_combo D_Product_Combo_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product_combo
    ADD CONSTRAINT "D_Product_Combo_pkey" PRIMARY KEY (d_product_combo_id);


--
-- TOC entry 4832 (class 2606 OID 385776)
-- Name: d_product D_Product_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product
    ADD CONSTRAINT "D_Product_pkey" PRIMARY KEY (d_product_id);


--
-- TOC entry 5012 (class 2606 OID 394620)
-- Name: d_request_orderline D_REQUESTLINE_ORDER_pkey; Type: CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_request_orderline
    ADD CONSTRAINT "D_REQUESTLINE_ORDER_pkey" PRIMARY KEY (d_request_orderline_id);


--
-- TOC entry 5010 (class 2606 OID 394584)
-- Name: d_request_order D_REQUEST_ORDER_pkey; Type: CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_request_order
    ADD CONSTRAINT "D_REQUEST_ORDER_pkey" PRIMARY KEY (d_request_order_id);


--
-- TOC entry 4991 (class 2606 OID 393500)
-- Name: d_reconciledetail D_ReconcileDetail_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reconciledetail
    ADD CONSTRAINT "D_ReconcileDetail_pkey" PRIMARY KEY (d_reconciledetail_id);


--
-- TOC entry 4851 (class 2606 OID 385821)
-- Name: d_reference_list D_Reference_List_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reference_list
    ADD CONSTRAINT "D_Reference_List_pkey" PRIMARY KEY (d_reference_list_id);


--
-- TOC entry 4846 (class 2606 OID 385798)
-- Name: d_reference D_Reference_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reference
    ADD CONSTRAINT "D_Reference_pkey" PRIMARY KEY (d_reference_id);


--
-- TOC entry 4856 (class 2606 OID 385829)
-- Name: d_reservation_line D_Reservation_Line_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reservation_line
    ADD CONSTRAINT "D_Reservation_Line_pkey" PRIMARY KEY (d_reservation_line_id);


--
-- TOC entry 4859 (class 2606 OID 385840)
-- Name: d_reservation_order D_Reservation_Order_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reservation_order
    ADD CONSTRAINT "D_Reservation_Order_pkey" PRIMARY KEY (d_reservation_order_id);


--
-- TOC entry 4862 (class 2606 OID 385848)
-- Name: d_role D_Role_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_role
    ADD CONSTRAINT "D_Role_pkey" PRIMARY KEY (d_role_id);


--
-- TOC entry 4959 (class 2606 OID 393031)
-- Name: d_shift_control D_Shift_Control_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_shift_control
    ADD CONSTRAINT "D_Shift_Control_pkey" PRIMARY KEY (d_shift_control_id);


--
-- TOC entry 4865 (class 2606 OID 385863)
-- Name: d_storage_onhand D_Storage_Onhand_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_storage_onhand
    ADD CONSTRAINT "D_Storage_Onhand_pkey" PRIMARY KEY (d_storage_onhand_id);


--
-- TOC entry 4868 (class 2606 OID 385871)
-- Name: d_table D_Table_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_table
    ADD CONSTRAINT "D_Table_pkey" PRIMARY KEY (d_table_id);


--
-- TOC entry 4878 (class 2606 OID 385890)
-- Name: d_tax_category D_Tax_Category_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_tax_category
    ADD CONSTRAINT "D_Tax_Category_pkey" PRIMARY KEY (d_tax_category_id);


--
-- TOC entry 4875 (class 2606 OID 385881)
-- Name: d_tax D_Tax_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_tax
    ADD CONSTRAINT "D_Tax_pkey" PRIMARY KEY (d_tax_id);


--
-- TOC entry 4881 (class 2606 OID 385901)
-- Name: d_tenant D_Tenant_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_tenant
    ADD CONSTRAINT "D_Tenant_pkey" PRIMARY KEY (d_tenant_id);


--
-- TOC entry 4939 (class 2606 OID 391474)
-- Name: d_uom_product D_UOM_Product_pkey; Type: CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_uom_product
    ADD CONSTRAINT "D_UOM_Product_pkey" PRIMARY KEY (d_uom_product_id);


--
-- TOC entry 4884 (class 2606 OID 385908)
-- Name: d_uom D_UOM_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_uom
    ADD CONSTRAINT "D_UOM_pkey" PRIMARY KEY (d_uom_id);


--
-- TOC entry 4889 (class 2606 OID 385917)
-- Name: d_user D_User_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_user
    ADD CONSTRAINT "D_User_pkey" PRIMARY KEY (d_user_id);


--
-- TOC entry 4894 (class 2606 OID 385936)
-- Name: d_vendor D_Vendor_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_vendor
    ADD CONSTRAINT "D_Vendor_pkey" PRIMARY KEY (d_vendor_id);


--
-- TOC entry 4904 (class 2606 OID 388498)
-- Name: d_vendor_audit D_Vendoradu_pkey; Type: CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_vendor_audit
    ADD CONSTRAINT "D_Vendoradu_pkey" PRIMARY KEY (id);


--
-- TOC entry 4897 (class 2606 OID 385947)
-- Name: d_voucher D_Voucher_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_voucher
    ADD CONSTRAINT "D_Voucher_pkey" PRIMARY KEY (d_voucher_id);


--
-- TOC entry 4900 (class 2606 OID 385972)
-- Name: d_warehouse D_Warehouse_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_warehouse
    ADD CONSTRAINT "D_Warehouse_pkey" PRIMARY KEY (d_warehouse_id);


--
-- TOC entry 5014 (class 2606 OID 394899)
-- Name: d_api_trace_log D_api_tracelog_pkey; Type: CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_api_trace_log
    ADD CONSTRAINT "D_api_tracelog_pkey" PRIMARY KEY (d_api_trace_log);


--
-- TOC entry 4975 (class 2606 OID 393247)
-- Name: d_bank D_bank_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_bank
    ADD CONSTRAINT "D_bank_pkey" PRIMARY KEY (d_bank_id);


--
-- TOC entry 4945 (class 2606 OID 392851)
-- Name: d_attribute D_d_attribute_pkey; Type: CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_attribute
    ADD CONSTRAINT "D_d_attribute_pkey" PRIMARY KEY (d_attribute_id);


--
-- TOC entry 4973 (class 2606 OID 393191)
-- Name: d_attribute_value D_d_attribute_value_pkey; Type: CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_attribute_value
    ADD CONSTRAINT "D_d_attribute_value_pkey" PRIMARY KEY (d_attribute_value_id);


--
-- TOC entry 4970 (class 2606 OID 393138)
-- Name: d_pc_terminalaccess D_d_pc_terminalaccess_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pc_terminalaccess
    ADD CONSTRAINT "D_d_pc_terminalaccess_pkey" PRIMARY KEY (d_pc_terminalaccess_id);


--
-- TOC entry 4954 (class 2606 OID 392971)
-- Name: d_table_use_ref D_d_table_use_ref_pkey; Type: CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_table_use_ref
    ADD CONSTRAINT "D_d_table_use_ref_pkey" PRIMARY KEY (d_table_use_ref_id);


--
-- TOC entry 4961 (class 2606 OID 393056)
-- Name: d_product_location D_product_location_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product_location
    ADD CONSTRAINT "D_product_location_pkey" PRIMARY KEY (d_product_location_id);


--
-- TOC entry 4941 (class 2606 OID 392802)
-- Name: d_assign_org_product d_assign_org_pkey; Type: CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_assign_org_product
    ADD CONSTRAINT d_assign_org_pkey PRIMARY KEY (d_assign_org_id);


--
-- TOC entry 4943 (class 2606 OID 392804)
-- Name: d_assign_org_product d_assign_org_product_pk; Type: CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_assign_org_product
    ADD CONSTRAINT d_assign_org_product_pk UNIQUE (d_tenant_id, d_org_id, d_product_id);


--
-- TOC entry 4979 (class 2606 OID 393274)
-- Name: d_bankaccount d_bankaccountpkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_bankaccount
    ADD CONSTRAINT d_bankaccountpkey PRIMARY KEY (d_bankaccount_id);


--
-- TOC entry 4936 (class 2606 OID 390969)
-- Name: d_cancel_reason d_cancel_reason_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_cancel_reason
    ADD CONSTRAINT d_cancel_reason_pkey PRIMARY KEY (d_cancel_reason_id);


--
-- TOC entry 4762 (class 2606 OID 392771)
-- Name: d_customer d_customer_pk; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_customer
    ADD CONSTRAINT d_customer_pk UNIQUE (d_tenant_id, phone1, name);


--
-- TOC entry 4764 (class 2606 OID 392779)
-- Name: d_customer d_customer_pk_2; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_customer
    ADD CONSTRAINT d_customer_pk_2 UNIQUE (d_tenant_id, phone1);


--
-- TOC entry 4982 (class 2606 OID 393369)
-- Name: d_erp_integration d_erp_integrationpkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_erp_integration
    ADD CONSTRAINT d_erp_integrationpkey PRIMARY KEY (d_erp_integration_id);


--
-- TOC entry 4780 (class 2606 OID 394948)
-- Name: d_floor d_floor_pk; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_floor
    ADD CONSTRAINT d_floor_pk UNIQUE (d_org_id, name);


--
-- TOC entry 4782 (class 2606 OID 394960)
-- Name: d_floor d_floor_pk_2; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_floor
    ADD CONSTRAINT d_floor_pk_2 UNIQUE (d_org_id, floor_no);


--
-- TOC entry 5000 (class 2606 OID 393805)
-- Name: d_integration_history d_integration_historypkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_integration_history
    ADD CONSTRAINT d_integration_historypkey PRIMARY KEY (d_integration_history_id);


--
-- TOC entry 4924 (class 2606 OID 390879)
-- Name: d_kitchen_order d_kitchen_order_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_kitchen_order
    ADD CONSTRAINT d_kitchen_order_pkey PRIMARY KEY (d_kitchen_order_id);


--
-- TOC entry 4927 (class 2606 OID 390905)
-- Name: d_kitchen_orderline d_kitchen_orderline_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_kitchen_orderline
    ADD CONSTRAINT d_kitchen_orderline_pkey PRIMARY KEY (d_kitchen_orderline_id);


--
-- TOC entry 4930 (class 2606 OID 390928)
-- Name: d_note_group d_note_group_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_note_group
    ADD CONSTRAINT d_note_group_pkey PRIMARY KEY (d_note_group_id);


--
-- TOC entry 4933 (class 2606 OID 390946)
-- Name: d_note d_note_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_note
    ADD CONSTRAINT d_note_pkey PRIMARY KEY (d_note_id);


--
-- TOC entry 4919 (class 2606 OID 389560)
-- Name: d_partner_group d_partner_group_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_partner_group
    ADD CONSTRAINT d_partner_group_pkey PRIMARY KEY (d_partner_group_id);


--
-- TOC entry 4997 (class 2606 OID 393776)
-- Name: d_paymethod_org d_paymethod_orgpkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_paymethod_org
    ADD CONSTRAINT d_paymethod_orgpkey PRIMARY KEY (d_paymethod_org_id);


--
-- TOC entry 4994 (class 2606 OID 393722)
-- Name: d_pay_method d_paymethodpkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pay_method
    ADD CONSTRAINT d_paymethodpkey PRIMARY KEY (d_pay_method_id);


--
-- TOC entry 4965 (class 2606 OID 393084)
-- Name: d_pos_payment d_pos_payment_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_payment
    ADD CONSTRAINT d_pos_payment_pkey PRIMARY KEY (d_pos_payment_id);


--
-- TOC entry 4968 (class 2606 OID 393112)
-- Name: d_pos_taxline d_pos_taxline_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_taxline
    ADD CONSTRAINT d_pos_taxline_pkey PRIMARY KEY (d_pos_taxline_id);


--
-- TOC entry 4917 (class 2606 OID 388802)
-- Name: d_pricelist_org d_pricelist_org_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pricelist_org
    ADD CONSTRAINT d_pricelist_org_pkey PRIMARY KEY (d_pricelist_org_id);


--
-- TOC entry 4909 (class 2606 OID 388762)
-- Name: d_pricelist d_pricelist_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pricelist
    ADD CONSTRAINT d_pricelist_pkey PRIMARY KEY (d_pricelist_id);


--
-- TOC entry 4912 (class 2606 OID 392755)
-- Name: d_pricelist_product d_pricelist_product_pk; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pricelist_product
    ADD CONSTRAINT d_pricelist_product_pk UNIQUE (d_pricelist_id, d_product_id);


--
-- TOC entry 4914 (class 2606 OID 388783)
-- Name: d_pricelist_product d_pricelist_product_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pricelist_product
    ADD CONSTRAINT d_pricelist_product_pkey PRIMARY KEY (d_pricelist_product_id);


--
-- TOC entry 4922 (class 2606 OID 390236)
-- Name: d_print_report d_print_report_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_print_report
    ADD CONSTRAINT d_print_report_pkey PRIMARY KEY (d_print_report_id);


--
-- TOC entry 4843 (class 2606 OID 390043)
-- Name: d_product_combo d_product_combo_d_org_id_d_tenant_id_d_product_id_d_product_key; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product_combo
    ADD CONSTRAINT d_product_combo_d_org_id_d_tenant_id_d_product_id_d_product_key UNIQUE (d_org_id, d_tenant_id, d_product_id, d_product_component_id);


--
-- TOC entry 4834 (class 2606 OID 390091)
-- Name: d_product d_product_d_tenant_id_d_org_id_code_key; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product
    ADD CONSTRAINT d_product_d_tenant_id_d_org_id_code_key UNIQUE (d_tenant_id, d_org_id, code);


--
-- TOC entry 4985 (class 2606 OID 393417)
-- Name: d_production d_production_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_production
    ADD CONSTRAINT d_production_pkey PRIMARY KEY (d_production_id);


--
-- TOC entry 4988 (class 2606 OID 393444)
-- Name: d_productionline d_productionline_pkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_productionline
    ADD CONSTRAINT d_productionline_pkey PRIMARY KEY (d_productionline_id);


--
-- TOC entry 5008 (class 2606 OID 394324)
-- Name: d_purchase_orderline d_purchase_orderlinepkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_purchase_orderline
    ADD CONSTRAINT d_purchase_orderlinepkey PRIMARY KEY (d_purchase_orderline_id);


--
-- TOC entry 5005 (class 2606 OID 394298)
-- Name: d_purchase_order d_purchase_orderpkey; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_purchase_order
    ADD CONSTRAINT d_purchase_orderpkey PRIMARY KEY (d_purchase_order_id);


--
-- TOC entry 4853 (class 2606 OID 392690)
-- Name: d_reference_list d_reference_list_pk; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reference_list
    ADD CONSTRAINT d_reference_list_pk UNIQUE (d_reference_id, value);


--
-- TOC entry 4848 (class 2606 OID 392620)
-- Name: d_reference d_reference_pk; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reference
    ADD CONSTRAINT d_reference_pk UNIQUE (name, d_tenant_id);


--
-- TOC entry 4870 (class 2606 OID 392793)
-- Name: d_table d_table_pk; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_table
    ADD CONSTRAINT d_table_pk UNIQUE (d_floor_id, name, table_no);


--
-- TOC entry 4872 (class 2606 OID 394828)
-- Name: d_table d_table_uniqu; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_table
    ADD CONSTRAINT d_table_uniqu UNIQUE (d_org_id, d_table_id, name, d_floor_id, table_no);


--
-- TOC entry 4956 (class 2606 OID 392989)
-- Name: d_table_use_ref d_table_use_ref_pk; Type: CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_table_use_ref
    ADD CONSTRAINT d_table_use_ref_pk UNIQUE (d_tenant_id, d_reference_id, domain_name, domain_column);


--
-- TOC entry 4886 (class 2606 OID 393162)
-- Name: d_uom d_uom_pk; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_uom
    ADD CONSTRAINT d_uom_pk UNIQUE (d_tenant_id, code, name);


--
-- TOC entry 4891 (class 2606 OID 388136)
-- Name: d_user d_user_pk; Type: CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_user
    ADD CONSTRAINT d_user_pk UNIQUE (d_tenant_id, user_name);


--
-- TOC entry 4902 (class 2606 OID 388443)
-- Name: revinfo revinfo_pkey; Type: CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.revinfo
    ADD CONSTRAINT revinfo_pkey PRIMARY KEY (rev);


--
-- TOC entry 4743 (class 2606 OID 384924)
-- Name: address address_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.address
    ADD CONSTRAINT address_pkey PRIMARY KEY (address_id);


--
-- TOC entry 4733 (class 2606 OID 384858)
-- Name: carts carts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carts
    ADD CONSTRAINT carts_pkey PRIMARY KEY (cart_id);


--
-- TOC entry 4727 (class 2606 OID 384526)
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (category_id);


--
-- TOC entry 4745 (class 2606 OID 384941)
-- Name: credentials credentials_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.credentials
    ADD CONSTRAINT credentials_pkey PRIMARY KEY (credential_id);


--
-- TOC entry 5002 (class 2606 OID 394255)
-- Name: databasechangeloglock databasechangeloglock_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id);


--
-- TOC entry 4731 (class 2606 OID 384848)
-- Name: favourites favourites_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.favourites
    ADD CONSTRAINT favourites_pkey PRIMARY KEY (user_id, product_id, like_date);


--
-- TOC entry 4724 (class 2606 OID 384512)
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- TOC entry 4739 (class 2606 OID 384895)
-- Name: order_items order_items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_pkey PRIMARY KEY (product_id, order_id);


--
-- TOC entry 4735 (class 2606 OID 384872)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (order_id);


--
-- TOC entry 4737 (class 2606 OID 384888)
-- Name: payments payments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_pkey PRIMARY KEY (payment_id);


--
-- TOC entry 4729 (class 2606 OID 384539)
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (product_id);


--
-- TOC entry 4906 (class 2606 OID 388541)
-- Name: revinfo revinfo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.revinfo
    ADD CONSTRAINT revinfo_pkey PRIMARY KEY (rev);


--
-- TOC entry 4741 (class 2606 OID 384911)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 4747 (class 2606 OID 384951)
-- Name: verification_tokens verification_tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.verification_tokens
    ADD CONSTRAINT verification_tokens_pkey PRIMARY KEY (verification_token_id);


--
-- TOC entry 4748 (class 1259 OID 385490)
-- Name: D_ChangeLog_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_ChangeLog_UU_Idx" ON pos.d_changelog USING btree (d_changelog_uu);


--
-- TOC entry 4751 (class 1259 OID 393899)
-- Name: D_Config_Name_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE INDEX "D_Config_Name_Idx" ON pos.d_config USING btree (name);


--
-- TOC entry 4752 (class 1259 OID 385500)
-- Name: D_Config_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE INDEX "D_Config_UU_Idx" ON pos.d_config USING btree (d_config_uu);


--
-- TOC entry 4950 (class 1259 OID 392934)
-- Name: D_Coupon_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Coupon_UU_Idx" ON pos.d_coupon USING btree (d_coupon_uu);


--
-- TOC entry 4755 (class 1259 OID 385508)
-- Name: D_Currency_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Currency_UU_Idx" ON pos.d_currency USING btree (d_currency_uu);


--
-- TOC entry 4758 (class 1259 OID 385521)
-- Name: D_Customer_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Customer_UU_Idx" ON pos.d_customer USING btree (d_customer_uu);


--
-- TOC entry 4767 (class 1259 OID 385534)
-- Name: D_Doctype_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Doctype_UU_Idx" ON pos.d_doctype USING btree (d_doctype_uu);


--
-- TOC entry 4768 (class 1259 OID 385545)
-- Name: D_Expense_ExpenseCategory; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE INDEX "D_Expense_ExpenseCategory" ON pos.d_expense USING btree (d_expense_category_id);


--
-- TOC entry 4769 (class 1259 OID 385544)
-- Name: D_Expense_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Expense_UU_Idx" ON pos.d_expense USING btree (d_expense_uu);


--
-- TOC entry 4775 (class 1259 OID 385563)
-- Name: D_Floor_ID_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Floor_ID_Idx" ON pos.d_floor USING btree (d_floor_id);


--
-- TOC entry 4776 (class 1259 OID 385564)
-- Name: D_Floor_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Floor_UU_Idx" ON pos.d_floor USING btree (d_floor_uu);


--
-- TOC entry 4783 (class 1259 OID 385574)
-- Name: D_Image_ID_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Image_ID_Idx" ON pos.d_image USING btree (d_image_id);


--
-- TOC entry 4784 (class 1259 OID 385575)
-- Name: D_Image_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Image_UU_Idx" ON pos.d_image USING btree (d_image_uu);


--
-- TOC entry 4787 (class 1259 OID 385585)
-- Name: D_Industry_ID_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Industry_ID_Idx" ON pos.d_industry USING btree (d_industry_id);


--
-- TOC entry 4788 (class 1259 OID 385586)
-- Name: D_Industry_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Industry_UU_Idx" ON pos.d_industry USING btree (d_industry_uu);


--
-- TOC entry 4795 (class 1259 OID 385623)
-- Name: D_InvoiceLine_ID_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_InvoiceLine_ID_Idx" ON pos.d_invoiceline USING btree (d_invoiceline_id);


--
-- TOC entry 4796 (class 1259 OID 385624)
-- Name: D_InvoiceLine_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_InvoiceLine_UU_Idx" ON pos.d_invoiceline USING btree (d_invoiceline_uu);


--
-- TOC entry 4791 (class 1259 OID 385595)
-- Name: D_Invoice_ID_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Invoice_ID_Idx" ON pos.d_invoice USING btree (d_invoice_id);


--
-- TOC entry 4792 (class 1259 OID 385596)
-- Name: D_Invoice_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Invoice_UU_Idx" ON pos.d_invoice USING btree (d_invoice_uu);


--
-- TOC entry 4799 (class 1259 OID 385630)
-- Name: D_Language_ID_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Language_ID_Idx" ON pos.d_language USING btree (d_language_id);


--
-- TOC entry 4800 (class 1259 OID 385631)
-- Name: D_Language_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Language_UU_Idx" ON pos.d_language USING btree (d_language_uu);


--
-- TOC entry 4803 (class 1259 OID 385641)
-- Name: D_Locator_ID_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Locator_ID_Idx" ON pos.d_locator USING btree (d_locator_id);


--
-- TOC entry 4804 (class 1259 OID 385642)
-- Name: D_Locator_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Locator_UU_Idx" ON pos.d_locator USING btree (d_locator_uu);


--
-- TOC entry 4808 (class 1259 OID 385650)
-- Name: D_Notification_ID_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Notification_ID_Idx" ON pos.d_notification USING btree (d_notification_id);


--
-- TOC entry 4809 (class 1259 OID 385651)
-- Name: D_Notification_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Notification_UU_Idx" ON pos.d_notification USING btree (d_notification_uu);


--
-- TOC entry 4815 (class 1259 OID 385712)
-- Name: D_OrderLine_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_OrderLine_UU_Idx" ON pos.d_orderline USING btree (d_orderline_uu);


--
-- TOC entry 4812 (class 1259 OID 385693)
-- Name: D_Order_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Order_UU_Idx" ON pos.d_order USING btree (d_order_uu);


--
-- TOC entry 4827 (class 1259 OID 385763)
-- Name: D_POS_OrderLine_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_POS_OrderLine_UU_Idx" ON pos.d_pos_orderline USING btree (d_pos_orderline_uu);


--
-- TOC entry 4824 (class 1259 OID 385753)
-- Name: D_POS_Order_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_POS_Order_UU_Idx" ON pos.d_pos_order USING btree (d_pos_order_uu);


--
-- TOC entry 4947 (class 1259 OID 392905)
-- Name: D_POS_Terminal_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_POS_Terminal_UU_Idx" ON pos.d_pos_terminal USING btree (d_pos_terminal_uu);


--
-- TOC entry 4821 (class 1259 OID 385738)
-- Name: D_Payment_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Payment_UU_Idx" ON pos.d_payment USING btree (d_payment_uu);


--
-- TOC entry 4915 (class 1259 OID 388808)
-- Name: D_PriceList_Org_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_PriceList_Org_UU_Idx" ON pos.d_pricelist_org USING btree (d_pricelist_org_uu);


--
-- TOC entry 4910 (class 1259 OID 388789)
-- Name: D_PriceList_Product_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_PriceList_Product_UU_Idx" ON pos.d_pricelist_product USING btree (d_pricelist_product_uu);


--
-- TOC entry 4907 (class 1259 OID 388768)
-- Name: D_PriceList_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_PriceList_UU_Idx" ON pos.d_pricelist USING btree (d_pricelist_uu);


--
-- TOC entry 4920 (class 1259 OID 390242)
-- Name: D_Print_Report_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Print_Report_UU_Idx" ON pos.d_print_report USING btree (d_print_report_uu);


--
-- TOC entry 4836 (class 1259 OID 385786)
-- Name: D_Product_Category_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Product_Category_UU_Idx" ON pos.d_product_category USING btree (d_product_category_uu);


--
-- TOC entry 4839 (class 1259 OID 385793)
-- Name: D_Product_Combo_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Product_Combo_UU_Idx" ON pos.d_product_combo USING btree (d_product_combo_uu);


--
-- TOC entry 4830 (class 1259 OID 385778)
-- Name: D_Product_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Product_UU_Idx" ON pos.d_product USING btree (d_product_uu);


--
-- TOC entry 4989 (class 1259 OID 393501)
-- Name: D_ReconcileDetail_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_ReconcileDetail_UU_Idx" ON pos.d_reconciledetail USING btree (d_reconciledetail_uu);


--
-- TOC entry 4849 (class 1259 OID 385823)
-- Name: D_Reference_List_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Reference_List_UU_Idx" ON pos.d_reference_list USING btree (d_reference_list_uu);


--
-- TOC entry 4844 (class 1259 OID 385800)
-- Name: D_Reference_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Reference_UU_Idx" ON pos.d_reference USING btree (d_reference_uu);


--
-- TOC entry 4854 (class 1259 OID 385831)
-- Name: D_Reservation_Line_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Reservation_Line_UU_Idx" ON pos.d_reservation_line USING btree (d_reservation_line_uu);


--
-- TOC entry 4857 (class 1259 OID 385842)
-- Name: D_Reservation_Order_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Reservation_Order_UU_Idx" ON pos.d_reservation_order USING btree (d_reservation_order_uu);


--
-- TOC entry 4860 (class 1259 OID 385850)
-- Name: D_Role_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Role_UU_Idx" ON pos.d_role USING btree (d_role_uu);


--
-- TOC entry 4957 (class 1259 OID 393042)
-- Name: D_Shift_Control_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Shift_Control_UU_Idx" ON pos.d_shift_control USING btree (d_shift_control_uu);


--
-- TOC entry 4863 (class 1259 OID 385865)
-- Name: D_Storage_Onhand_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Storage_Onhand_UU_Idx" ON pos.d_storage_onhand USING btree (d_storage_onhand_uu);


--
-- TOC entry 4866 (class 1259 OID 385873)
-- Name: D_Table_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Table_UU_Idx" ON pos.d_table USING btree (d_table_uu);


--
-- TOC entry 4876 (class 1259 OID 385892)
-- Name: D_Tax_Category_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Tax_Category_UU_Idx" ON pos.d_tax_category USING btree (d_tax_category_uu);


--
-- TOC entry 4873 (class 1259 OID 385883)
-- Name: D_Tax_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Tax_UU_Idx" ON pos.d_tax USING btree (d_tax_uu);


--
-- TOC entry 4879 (class 1259 OID 385903)
-- Name: D_Tenant_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Tenant_UU_Idx" ON pos.d_tenant USING btree (d_tenant_uu);


--
-- TOC entry 4882 (class 1259 OID 385910)
-- Name: D_UOM_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_UOM_UU_Idx" ON pos.d_uom USING btree (d_uom_uu);


--
-- TOC entry 4887 (class 1259 OID 385919)
-- Name: D_User_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_User_UU_Idx" ON pos.d_user USING btree (d_user_uu);


--
-- TOC entry 4892 (class 1259 OID 385938)
-- Name: D_Vendor_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Vendor_UU_Idx" ON pos.d_vendor USING btree (d_vendor_uu);


--
-- TOC entry 4895 (class 1259 OID 385949)
-- Name: D_Voucher_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Voucher_UU_Idx" ON pos.d_voucher USING btree (d_voucher_uu);


--
-- TOC entry 4807 (class 1259 OID 385643)
-- Name: D_Warehouse_ID_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE INDEX "D_Warehouse_ID_Idx" ON pos.d_locator USING btree (d_warehouse_id);


--
-- TOC entry 4898 (class 1259 OID 385973)
-- Name: D_Warehouse_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "D_Warehouse_UU_Idx" ON pos.d_warehouse USING btree (d_warehouse_uu);


--
-- TOC entry 4946 (class 1259 OID 392857)
-- Name: d_attribute_d_attribute_uu_index; Type: INDEX; Schema: pos; Owner: adempiere
--

CREATE INDEX d_attribute_d_attribute_uu_index ON pos.d_attribute USING btree (d_attribute_uu);


--
-- TOC entry 4976 (class 1259 OID 393258)
-- Name: d_bank_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_bank_UU_Idx" ON pos.d_bank USING btree (d_bank_uu);


--
-- TOC entry 4977 (class 1259 OID 393290)
-- Name: d_bankaccount_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_bankaccount_UU_Idx" ON pos.d_bankaccount USING btree (d_bankaccount_uu);


--
-- TOC entry 4937 (class 1259 OID 390975)
-- Name: d_cancel_reason_uu_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_cancel_reason_uu_Idx" ON pos.d_cancel_reason USING btree (d_cancel_reason_uu);


--
-- TOC entry 4980 (class 1259 OID 393380)
-- Name: d_erp_integration_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_erp_integration_UU_Idx" ON pos.d_erp_integration USING btree (d_erp_integration_uu);


--
-- TOC entry 4774 (class 1259 OID 387600)
-- Name: d_expense_category_uu_idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE INDEX d_expense_category_uu_idx ON pos.d_expense_category USING btree (d_expense_category_uu);


--
-- TOC entry 4998 (class 1259 OID 393816)
-- Name: d_integration_historyn_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_integration_historyn_UU_Idx" ON pos.d_integration_history USING btree (d_integration_history_uu);


--
-- TOC entry 4925 (class 1259 OID 390885)
-- Name: d_kitchen_order_uu_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_kitchen_order_uu_Idx" ON pos.d_kitchen_order USING btree (d_kitchen_order_uu);


--
-- TOC entry 4928 (class 1259 OID 390916)
-- Name: d_kitchen_orderline_uu_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_kitchen_orderline_uu_Idx" ON pos.d_kitchen_orderline USING btree (d_kitchen_orderline_uu);


--
-- TOC entry 4931 (class 1259 OID 390934)
-- Name: d_note_group_uu_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_note_group_uu_Idx" ON pos.d_note_group USING btree (d_note_group_uu);


--
-- TOC entry 4934 (class 1259 OID 390957)
-- Name: d_note_uu_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_note_uu_Idx" ON pos.d_note USING btree (d_note_uu);


--
-- TOC entry 4820 (class 1259 OID 387653)
-- Name: d_org_d_org_uu_idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE INDEX d_org_d_org_uu_idx ON pos.d_org USING btree (d_org_uu);


--
-- TOC entry 4992 (class 1259 OID 393738)
-- Name: d_pay_method_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_pay_method_UU_Idx" ON pos.d_pay_method USING btree (d_pay_method_uu);


--
-- TOC entry 4995 (class 1259 OID 393792)
-- Name: d_paymethod_org_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_paymethod_org_UU_Idx" ON pos.d_paymethod_org USING btree (d_paymethod_org_uu);


--
-- TOC entry 4971 (class 1259 OID 393149)
-- Name: d_pc_terminalaccess_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_pc_terminalaccess_UU_Idx" ON pos.d_pc_terminalaccess USING btree (d_pc_terminalaccess_uu);


--
-- TOC entry 4963 (class 1259 OID 393095)
-- Name: d_pos_payment_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_pos_payment_UU_Idx" ON pos.d_pos_payment USING btree (d_pos_payment_uu);


--
-- TOC entry 4966 (class 1259 OID 393123)
-- Name: d_pos_taxline_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_pos_taxline_UU_Idx" ON pos.d_pos_taxline USING btree (d_pos_taxline_uu);


--
-- TOC entry 4962 (class 1259 OID 393067)
-- Name: d_product_location_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_product_location_UU_Idx" ON pos.d_product_location USING btree (d_product_location_uu);


--
-- TOC entry 4835 (class 1259 OID 387579)
-- Name: d_product_productcategory; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE INDEX d_product_productcategory ON pos.d_product USING btree (d_product_category_id);


--
-- TOC entry 4983 (class 1259 OID 393428)
-- Name: d_production_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_production_UU_Idx" ON pos.d_production USING btree (d_production_uu);


--
-- TOC entry 4986 (class 1259 OID 393455)
-- Name: d_productionline_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_productionline_UU_Idx" ON pos.d_productionline USING btree (d_productionline_uu);


--
-- TOC entry 5003 (class 1259 OID 394309)
-- Name: d_purchase_order_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_purchase_order_UU_Idx" ON pos.d_purchase_order USING btree (d_purchase_order_uu);


--
-- TOC entry 5006 (class 1259 OID 394340)
-- Name: d_purchase_orderline_UU_Idx; Type: INDEX; Schema: pos; Owner: postgres
--

CREATE UNIQUE INDEX "d_purchase_orderline_UU_Idx" ON pos.d_purchase_orderline USING btree (d_purchase_orderline_uu);


--
-- TOC entry 4725 (class 1259 OID 384513)
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- TOC entry 5155 (class 2606 OID 393285)
-- Name: d_bankaccount FK_Bank_DBankAccount; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_bankaccount
    ADD CONSTRAINT "FK_Bank_DBankAccount" FOREIGN KEY (d_bank_id) REFERENCES pos.d_bank(d_bank_id);


--
-- TOC entry 5166 (class 2606 OID 393733)
-- Name: d_pay_method FK_Bank_DPayMethod; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pay_method
    ADD CONSTRAINT "FK_Bank_DPayMethod" FOREIGN KEY (d_bank_id) REFERENCES pos.d_bank(d_bank_id);


--
-- TOC entry 5035 (class 2606 OID 386359)
-- Name: d_invoice FK_Customer_DInvoice; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_invoice
    ADD CONSTRAINT "FK_Customer_DInvoice" FOREIGN KEY (d_customer_id) REFERENCES pos.d_customer(d_customer_id);


--
-- TOC entry 5049 (class 2606 OID 386429)
-- Name: d_org FK_D_Tenant_DOrg; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_org
    ADD CONSTRAINT "FK_D_Tenant_DOrg" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5029 (class 2606 OID 386234)
-- Name: d_expense FK_ExpenseCategory_DExpense; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_expense
    ADD CONSTRAINT "FK_ExpenseCategory_DExpense" FOREIGN KEY (d_expense_category_id) REFERENCES pos.d_expense_category(d_expense_category_id);


--
-- TOC entry 5072 (class 2606 OID 386539)
-- Name: d_reservation_order FK_Floor_DReservationOrder; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reservation_order
    ADD CONSTRAINT "FK_Floor_DReservationOrder" FOREIGN KEY (d_floor_id) REFERENCES pos.d_floor(d_floor_id);


--
-- TOC entry 5083 (class 2606 OID 386664)
-- Name: d_table FK_Floor_DTable; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_table
    ADD CONSTRAINT "FK_Floor_DTable" FOREIGN KEY (d_floor_id) REFERENCES pos.d_floor(d_floor_id);


--
-- TOC entry 5091 (class 2606 OID 386754)
-- Name: d_tenant FK_Industry_DTenant; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_tenant
    ADD CONSTRAINT "FK_Industry_DTenant" FOREIGN KEY (d_industry_id) REFERENCES pos.d_industry(d_industry_id);


--
-- TOC entry 5038 (class 2606 OID 386384)
-- Name: d_invoiceline FK_Invoice_DInvoiceLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_invoiceline
    ADD CONSTRAINT "FK_Invoice_DInvoiceLine" FOREIGN KEY (d_invoice_id) REFERENCES pos.d_invoice(d_invoice_id);


--
-- TOC entry 5120 (class 2606 OID 390911)
-- Name: d_kitchen_orderline FK_KitchenOrder_DKitchenOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_kitchen_orderline
    ADD CONSTRAINT "FK_KitchenOrder_DKitchenOrderLine" FOREIGN KEY (d_kitchen_order_id) REFERENCES pos.d_kitchen_order(d_kitchen_order_id);


--
-- TOC entry 5079 (class 2606 OID 386649)
-- Name: d_storage_onhand FK_Locator_DStorageOnhand; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_storage_onhand
    ADD CONSTRAINT "FK_Locator_DStorageOnhand" FOREIGN KEY (d_locator_id) REFERENCES pos.d_locator(d_locator_id);


--
-- TOC entry 5047 (class 2606 OID 386424)
-- Name: d_orderline FK_Order_DOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_orderline
    ADD CONSTRAINT "FK_Order_DOrderLine" FOREIGN KEY (d_order_id) REFERENCES pos.d_order(d_order_id);


--
-- TOC entry 5153 (class 2606 OID 393248)
-- Name: d_bank FK_Org_DBank; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_bank
    ADD CONSTRAINT "FK_Org_DBank" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5156 (class 2606 OID 393275)
-- Name: d_bankaccount FK_Org_DBankAccount; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_bankaccount
    ADD CONSTRAINT "FK_Org_DBankAccount" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5192 (class 2606 OID 394905)
-- Name: d_api_trace_log FK_Org_DBankAccount; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_api_trace_log
    ADD CONSTRAINT "FK_Org_DBankAccount" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5022 (class 2606 OID 386144)
-- Name: d_config FK_Org_DConfig; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_config
    ADD CONSTRAINT "FK_Org_DConfig" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5137 (class 2606 OID 392924)
-- Name: d_coupon FK_Org_DCoupon; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_coupon
    ADD CONSTRAINT "FK_Org_DCoupon" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5024 (class 2606 OID 386154)
-- Name: d_currency FK_Org_DCurrency; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_currency
    ADD CONSTRAINT "FK_Org_DCurrency" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5158 (class 2606 OID 393370)
-- Name: d_erp_integration FK_Org_DERPIntegration; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_erp_integration
    ADD CONSTRAINT "FK_Org_DERPIntegration" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5032 (class 2606 OID 386319)
-- Name: d_floor FK_Org_DFloor; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_floor
    ADD CONSTRAINT "FK_Org_DFloor" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5172 (class 2606 OID 393806)
-- Name: d_integration_history FK_Org_DIntegrationHistory; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_integration_history
    ADD CONSTRAINT "FK_Org_DIntegrationHistory" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5036 (class 2606 OID 386349)
-- Name: d_invoice FK_Org_DInvoice; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_invoice
    ADD CONSTRAINT "FK_Org_DInvoice" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5039 (class 2606 OID 386379)
-- Name: d_invoiceline FK_Org_DInvoiceLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_invoiceline
    ADD CONSTRAINT "FK_Org_DInvoiceLine" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5041 (class 2606 OID 386394)
-- Name: d_language FK_Org_DLanguage; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_language
    ADD CONSTRAINT "FK_Org_DLanguage" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5045 (class 2606 OID 386414)
-- Name: d_order FK_Org_DOrder; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_order
    ADD CONSTRAINT "FK_Org_DOrder" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5149 (class 2606 OID 393139)
-- Name: d_pc_terminalaccess FK_Org_DPCTerminalAccess; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pc_terminalaccess
    ADD CONSTRAINT "FK_Org_DPCTerminalAccess" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5052 (class 2606 OID 386449)
-- Name: d_pos_order FK_Org_DPOSORder; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_order
    ADD CONSTRAINT "FK_Org_DPOSORder" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5055 (class 2606 OID 386459)
-- Name: d_pos_orderline FK_Org_DPOSOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_orderline
    ADD CONSTRAINT "FK_Org_DPOSOrderLine" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5145 (class 2606 OID 393085)
-- Name: d_pos_payment FK_Org_DPOSPayment; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_payment
    ADD CONSTRAINT "FK_Org_DPOSPayment" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5147 (class 2606 OID 393113)
-- Name: d_pos_taxline FK_Org_DPOSTaxLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_taxline
    ADD CONSTRAINT "FK_Org_DPOSTaxLine" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5135 (class 2606 OID 392895)
-- Name: d_pos_terminal FK_Org_DPOSTerminal; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_terminal
    ADD CONSTRAINT "FK_Org_DPOSTerminal" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5167 (class 2606 OID 393723)
-- Name: d_pay_method FK_Org_DPayMethod; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pay_method
    ADD CONSTRAINT "FK_Org_DPayMethod" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5169 (class 2606 OID 393777)
-- Name: d_paymethod_org FK_Org_DPayMethodOrg; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_paymethod_org
    ADD CONSTRAINT "FK_Org_DPayMethodOrg" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5050 (class 2606 OID 386439)
-- Name: d_payment FK_Org_DPayment; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_payment
    ADD CONSTRAINT "FK_Org_DPayment" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5058 (class 2606 OID 386474)
-- Name: d_product FK_Org_DProduct; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product
    ADD CONSTRAINT "FK_Org_DProduct" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5061 (class 2606 OID 386484)
-- Name: d_product_category FK_Org_DProductCategory; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product_category
    ADD CONSTRAINT "FK_Org_DProductCategory" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5064 (class 2606 OID 386489)
-- Name: d_product_combo FK_Org_DProductCombo; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product_combo
    ADD CONSTRAINT "FK_Org_DProductCombo" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5143 (class 2606 OID 393057)
-- Name: d_product_location FK_Org_DProductLocation; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product_location
    ADD CONSTRAINT "FK_Org_DProductLocation" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5160 (class 2606 OID 393418)
-- Name: d_production FK_Org_DProduction; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_production
    ADD CONSTRAINT "FK_Org_DProduction" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5162 (class 2606 OID 393445)
-- Name: d_productionline FK_Org_DProductionLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_productionline
    ADD CONSTRAINT "FK_Org_DProductionLine" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5174 (class 2606 OID 394299)
-- Name: d_purchase_order FK_Org_DPurchaseOrder; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_purchase_order
    ADD CONSTRAINT "FK_Org_DPurchaseOrder" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5176 (class 2606 OID 394325)
-- Name: d_purchase_orderline FK_Org_DPurchaseOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_purchase_orderline
    ADD CONSTRAINT "FK_Org_DPurchaseOrderLine" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5185 (class 2606 OID 394626)
-- Name: d_request_orderline FK_Org_DRQLINEORder; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_request_orderline
    ADD CONSTRAINT "FK_Org_DRQLINEORder" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5179 (class 2606 OID 394590)
-- Name: d_request_order FK_Org_DRQORder; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_request_order
    ADD CONSTRAINT "FK_Org_DRQORder" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5164 (class 2606 OID 393502)
-- Name: d_reconciledetail FK_Org_DReconcileDetail; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reconciledetail
    ADD CONSTRAINT "FK_Org_DReconcileDetail" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5070 (class 2606 OID 386524)
-- Name: d_reservation_line FK_Org_DReservationLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reservation_line
    ADD CONSTRAINT "FK_Org_DReservationLine" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5073 (class 2606 OID 386534)
-- Name: d_reservation_order FK_Org_DReservationOrder; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reservation_order
    ADD CONSTRAINT "FK_Org_DReservationOrder" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5141 (class 2606 OID 393032)
-- Name: d_shift_control FK_Org_DShiftControl; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_shift_control
    ADD CONSTRAINT "FK_Org_DShiftControl" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5080 (class 2606 OID 386639)
-- Name: d_storage_onhand FK_Org_DStorageOnhand; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_storage_onhand
    ADD CONSTRAINT "FK_Org_DStorageOnhand" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5084 (class 2606 OID 386659)
-- Name: d_table FK_Org_DTable; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_table
    ADD CONSTRAINT "FK_Org_DTable" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5086 (class 2606 OID 386714)
-- Name: d_tax FK_Org_DTax; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_tax
    ADD CONSTRAINT "FK_Org_DTax" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5089 (class 2606 OID 386729)
-- Name: d_tax_category FK_Org_DTaxCategory; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_tax_category
    ADD CONSTRAINT "FK_Org_DTaxCategory" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5092 (class 2606 OID 386764)
-- Name: d_uom FK_Org_DUOM; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_uom
    ADD CONSTRAINT "FK_Org_DUOM" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5096 (class 2606 OID 386784)
-- Name: d_userorg_access FK_Org_DUserOrgAccess; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_userorg_access
    ADD CONSTRAINT "FK_Org_DUserOrgAccess" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5189 (class 2606 OID 394798)
-- Name: d_posterminal_org_access FK_Org_DUserOrgAccess; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_posterminal_org_access
    ADD CONSTRAINT "FK_Org_DUserOrgAccess" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5103 (class 2606 OID 386809)
-- Name: d_voucher FK_Org_DVoucher; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_voucher
    ADD CONSTRAINT "FK_Org_DVoucher" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5105 (class 2606 OID 386819)
-- Name: d_warehouse FK_Org_ID; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_warehouse
    ADD CONSTRAINT "FK_Org_ID" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5056 (class 2606 OID 386464)
-- Name: d_pos_orderline FK_POSOrder_DPOSOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_orderline
    ADD CONSTRAINT "FK_POSOrder_DPOSOrderLine" FOREIGN KEY (d_pos_order_id) REFERENCES pos.d_pos_order(d_pos_order_id);


--
-- TOC entry 5170 (class 2606 OID 393787)
-- Name: d_paymethod_org FK_PayMethod_DPayMethodOrg; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_paymethod_org
    ADD CONSTRAINT "FK_PayMethod_DPayMethodOrg" FOREIGN KEY (d_pay_method_id) REFERENCES pos.d_pay_method(d_pay_method_id);


--
-- TOC entry 5129 (class 2606 OID 391480)
-- Name: d_uom_product FK_Product_D; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_uom_product
    ADD CONSTRAINT "FK_Product_D" FOREIGN KEY (d_product_id) REFERENCES pos.d_product(d_product_id);


--
-- TOC entry 5065 (class 2606 OID 386499)
-- Name: d_product_combo FK_Product_DProductCombo; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product_combo
    ADD CONSTRAINT "FK_Product_DProductCombo" FOREIGN KEY (d_product_id) REFERENCES pos.d_product(d_product_id);


--
-- TOC entry 5177 (class 2606 OID 394335)
-- Name: d_purchase_orderline FK_PurchaseOrder_DPurchaseOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_purchase_orderline
    ADD CONSTRAINT "FK_PurchaseOrder_DPurchaseOrderLine" FOREIGN KEY (d_purchase_order_id) REFERENCES pos.d_purchase_order(d_purchase_order_id);


--
-- TOC entry 5068 (class 2606 OID 386514)
-- Name: d_reference_list FK_Reference_DReferenceList; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reference_list
    ADD CONSTRAINT "FK_Reference_DReferenceList" FOREIGN KEY (d_reference_id) REFERENCES pos.d_reference(d_reference_id);


--
-- TOC entry 5098 (class 2606 OID 386794)
-- Name: d_user_role_access FK_Role_DUserRoleAccess; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_user_role_access
    ADD CONSTRAINT "FK_Role_DUserRoleAccess" FOREIGN KEY (d_role_id) REFERENCES pos.d_role(d_role_id);


--
-- TOC entry 5074 (class 2606 OID 386544)
-- Name: d_reservation_order FK_Table_DReservationOrder; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reservation_order
    ADD CONSTRAINT "FK_Table_DReservationOrder" FOREIGN KEY (d_table_id) REFERENCES pos.d_table(d_table_id);


--
-- TOC entry 5087 (class 2606 OID 386719)
-- Name: d_tax FK_TaxCategory_DTax; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_tax
    ADD CONSTRAINT "FK_TaxCategory_DTax" FOREIGN KEY (d_tax_category_id) REFERENCES pos.d_tax_category(d_tax_category_id);


--
-- TOC entry 5134 (class 2606 OID 392852)
-- Name: d_attribute FK_Tenant_DAttr; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_attribute
    ADD CONSTRAINT "FK_Tenant_DAttr" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5139 (class 2606 OID 392972)
-- Name: d_table_use_ref FK_Tenant_DAttr; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_table_use_ref
    ADD CONSTRAINT "FK_Tenant_DAttr" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5151 (class 2606 OID 393192)
-- Name: d_attribute_value FK_Tenant_DAttr; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_attribute_value
    ADD CONSTRAINT "FK_Tenant_DAttr" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5154 (class 2606 OID 393253)
-- Name: d_bank FK_Tenant_DBank; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_bank
    ADD CONSTRAINT "FK_Tenant_DBank" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5157 (class 2606 OID 393280)
-- Name: d_bankaccount FK_Tenant_DBankAccount; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_bankaccount
    ADD CONSTRAINT "FK_Tenant_DBankAccount" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5193 (class 2606 OID 394900)
-- Name: d_api_trace_log FK_Tenant_DBankAccount; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_api_trace_log
    ADD CONSTRAINT "FK_Tenant_DBankAccount" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5128 (class 2606 OID 390970)
-- Name: d_cancel_reason FK_Tenant_DCancelReason; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_cancel_reason
    ADD CONSTRAINT "FK_Tenant_DCancelReason" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5021 (class 2606 OID 385974)
-- Name: d_changelog FK_Tenant_DChangeLog; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_changelog
    ADD CONSTRAINT "FK_Tenant_DChangeLog" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5023 (class 2606 OID 386139)
-- Name: d_config FK_Tenant_DConfig; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_config
    ADD CONSTRAINT "FK_Tenant_DConfig" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5138 (class 2606 OID 392929)
-- Name: d_coupon FK_Tenant_DCoupon; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_coupon
    ADD CONSTRAINT "FK_Tenant_DCoupon" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5025 (class 2606 OID 386149)
-- Name: d_currency FK_Tenant_DCurrency; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_currency
    ADD CONSTRAINT "FK_Tenant_DCurrency" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5026 (class 2606 OID 386219)
-- Name: d_customer FK_Tenant_DCustomer; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_customer
    ADD CONSTRAINT "FK_Tenant_DCustomer" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5028 (class 2606 OID 386224)
-- Name: d_doctype FK_Tenant_DDocType; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_doctype
    ADD CONSTRAINT "FK_Tenant_DDocType" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5159 (class 2606 OID 393375)
-- Name: d_erp_integration FK_Tenant_DERPIntegration; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_erp_integration
    ADD CONSTRAINT "FK_Tenant_DERPIntegration" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5030 (class 2606 OID 386229)
-- Name: d_expense FK_Tenant_DExpense; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_expense
    ADD CONSTRAINT "FK_Tenant_DExpense" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5031 (class 2606 OID 386239)
-- Name: d_expense_category FK_Tenant_DExpenseCategory; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_expense_category
    ADD CONSTRAINT "FK_Tenant_DExpenseCategory" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5033 (class 2606 OID 386314)
-- Name: d_floor FK_Tenant_DFloor; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_floor
    ADD CONSTRAINT "FK_Tenant_DFloor" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5034 (class 2606 OID 386324)
-- Name: d_image FK_Tenant_DImage; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_image
    ADD CONSTRAINT "FK_Tenant_DImage" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5173 (class 2606 OID 393811)
-- Name: d_integration_history FK_Tenant_DIntegrationHistory; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_integration_history
    ADD CONSTRAINT "FK_Tenant_DIntegrationHistory" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5037 (class 2606 OID 386329)
-- Name: d_invoice FK_Tenant_DInvoice; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_invoice
    ADD CONSTRAINT "FK_Tenant_DInvoice" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5040 (class 2606 OID 386374)
-- Name: d_invoiceline FK_Tenant_DInvoiceLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_invoiceline
    ADD CONSTRAINT "FK_Tenant_DInvoiceLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5116 (class 2606 OID 390880)
-- Name: d_kitchen_order FK_Tenant_DKitchenOrder; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_kitchen_order
    ADD CONSTRAINT "FK_Tenant_DKitchenOrder" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5121 (class 2606 OID 390906)
-- Name: d_kitchen_orderline FK_Tenant_DKitchenOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_kitchen_orderline
    ADD CONSTRAINT "FK_Tenant_DKitchenOrderLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5042 (class 2606 OID 386389)
-- Name: d_language FK_Tenant_DLanguage; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_language
    ADD CONSTRAINT "FK_Tenant_DLanguage" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5126 (class 2606 OID 390947)
-- Name: d_note FK_Tenant_DNote; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_note
    ADD CONSTRAINT "FK_Tenant_DNote" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5125 (class 2606 OID 390929)
-- Name: d_note_group FK_Tenant_DNoteGroup; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_note_group
    ADD CONSTRAINT "FK_Tenant_DNoteGroup" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5127 (class 2606 OID 390952)
-- Name: d_note FK_Tenant_DNoteGroup; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_note
    ADD CONSTRAINT "FK_Tenant_DNoteGroup" FOREIGN KEY (d_note_group_id) REFERENCES pos.d_note_group(d_note_group_id);


--
-- TOC entry 5044 (class 2606 OID 386404)
-- Name: d_notification FK_Tenant_DNotification; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_notification
    ADD CONSTRAINT "FK_Tenant_DNotification" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5046 (class 2606 OID 386409)
-- Name: d_order FK_Tenant_DOrder; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_order
    ADD CONSTRAINT "FK_Tenant_DOrder" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5048 (class 2606 OID 386419)
-- Name: d_orderline FK_Tenant_DOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_orderline
    ADD CONSTRAINT "FK_Tenant_DOrderLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5150 (class 2606 OID 393144)
-- Name: d_pc_terminalaccess FK_Tenant_DPCTerminalAccess; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pc_terminalaccess
    ADD CONSTRAINT "FK_Tenant_DPCTerminalAccess" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5053 (class 2606 OID 386444)
-- Name: d_pos_order FK_Tenant_DPOSOrder; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_order
    ADD CONSTRAINT "FK_Tenant_DPOSOrder" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5057 (class 2606 OID 386454)
-- Name: d_pos_orderline FK_Tenant_DPOSOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_orderline
    ADD CONSTRAINT "FK_Tenant_DPOSOrderLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5146 (class 2606 OID 393090)
-- Name: d_pos_payment FK_Tenant_DPOSPayment; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_payment
    ADD CONSTRAINT "FK_Tenant_DPOSPayment" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5148 (class 2606 OID 393118)
-- Name: d_pos_taxline FK_Tenant_DPOSTaxLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_taxline
    ADD CONSTRAINT "FK_Tenant_DPOSTaxLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5136 (class 2606 OID 392900)
-- Name: d_pos_terminal FK_Tenant_DPOSTerminal; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_terminal
    ADD CONSTRAINT "FK_Tenant_DPOSTerminal" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5114 (class 2606 OID 389561)
-- Name: d_partner_group FK_Tenant_DPartnerGroup; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_partner_group
    ADD CONSTRAINT "FK_Tenant_DPartnerGroup" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5168 (class 2606 OID 393728)
-- Name: d_pay_method FK_Tenant_DPayMethod; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pay_method
    ADD CONSTRAINT "FK_Tenant_DPayMethod" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5171 (class 2606 OID 393782)
-- Name: d_paymethod_org FK_Tenant_DPayMethodOrg; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_paymethod_org
    ADD CONSTRAINT "FK_Tenant_DPayMethodOrg" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5051 (class 2606 OID 386434)
-- Name: d_payment FK_Tenant_DPayment; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_payment
    ADD CONSTRAINT "FK_Tenant_DPayment" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5108 (class 2606 OID 388763)
-- Name: d_pricelist FK_Tenant_DPriceList; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pricelist
    ADD CONSTRAINT "FK_Tenant_DPriceList" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5112 (class 2606 OID 388803)
-- Name: d_pricelist_org FK_Tenant_DPriceListOrg; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pricelist_org
    ADD CONSTRAINT "FK_Tenant_DPriceListOrg" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5109 (class 2606 OID 388784)
-- Name: d_pricelist_product FK_Tenant_DPriceListProduct; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pricelist_product
    ADD CONSTRAINT "FK_Tenant_DPriceListProduct" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5115 (class 2606 OID 390237)
-- Name: d_print_report FK_Tenant_DPrintReport; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_print_report
    ADD CONSTRAINT "FK_Tenant_DPrintReport" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5059 (class 2606 OID 386469)
-- Name: d_product FK_Tenant_DProduct; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product
    ADD CONSTRAINT "FK_Tenant_DProduct" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5062 (class 2606 OID 386479)
-- Name: d_product_category FK_Tenant_DProductCategory; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product_category
    ADD CONSTRAINT "FK_Tenant_DProductCategory" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5066 (class 2606 OID 386494)
-- Name: d_product_combo FK_Tenant_DProductCombo; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product_combo
    ADD CONSTRAINT "FK_Tenant_DProductCombo" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5144 (class 2606 OID 393062)
-- Name: d_product_location FK_Tenant_DProductLocation; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product_location
    ADD CONSTRAINT "FK_Tenant_DProductLocation" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5161 (class 2606 OID 393423)
-- Name: d_production FK_Tenant_DProduction; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_production
    ADD CONSTRAINT "FK_Tenant_DProduction" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5163 (class 2606 OID 393450)
-- Name: d_productionline FK_Tenant_DProductionLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_productionline
    ADD CONSTRAINT "FK_Tenant_DProductionLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5175 (class 2606 OID 394304)
-- Name: d_purchase_order FK_Tenant_DPurchaseOrder; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_purchase_order
    ADD CONSTRAINT "FK_Tenant_DPurchaseOrder" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5178 (class 2606 OID 394330)
-- Name: d_purchase_orderline FK_Tenant_DPurchaseOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_purchase_orderline
    ADD CONSTRAINT "FK_Tenant_DPurchaseOrderLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5186 (class 2606 OID 394621)
-- Name: d_request_orderline FK_Tenant_DRQLINEORDER; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_request_orderline
    ADD CONSTRAINT "FK_Tenant_DRQLINEORDER" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5180 (class 2606 OID 394585)
-- Name: d_request_order FK_Tenant_DRQORDER; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_request_order
    ADD CONSTRAINT "FK_Tenant_DRQORDER" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5165 (class 2606 OID 393507)
-- Name: d_reconciledetail FK_Tenant_DReconcileDetail; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reconciledetail
    ADD CONSTRAINT "FK_Tenant_DReconcileDetail" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5067 (class 2606 OID 386504)
-- Name: d_reference FK_Tenant_DReference; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reference
    ADD CONSTRAINT "FK_Tenant_DReference" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5069 (class 2606 OID 386509)
-- Name: d_reference_list FK_Tenant_DReferenceList; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reference_list
    ADD CONSTRAINT "FK_Tenant_DReferenceList" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5071 (class 2606 OID 386519)
-- Name: d_reservation_line FK_Tenant_DReservationLine; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reservation_line
    ADD CONSTRAINT "FK_Tenant_DReservationLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5075 (class 2606 OID 386529)
-- Name: d_reservation_order FK_Tenant_DReservationOrder; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reservation_order
    ADD CONSTRAINT "FK_Tenant_DReservationOrder" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5078 (class 2606 OID 386549)
-- Name: d_role FK_Tenant_DRole; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_role
    ADD CONSTRAINT "FK_Tenant_DRole" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5142 (class 2606 OID 393037)
-- Name: d_shift_control FK_Tenant_DShiftControl; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_shift_control
    ADD CONSTRAINT "FK_Tenant_DShiftControl" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5081 (class 2606 OID 386634)
-- Name: d_storage_onhand FK_Tenant_DStorageOnhand; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_storage_onhand
    ADD CONSTRAINT "FK_Tenant_DStorageOnhand" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5085 (class 2606 OID 386654)
-- Name: d_table FK_Tenant_DTable; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_table
    ADD CONSTRAINT "FK_Tenant_DTable" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5088 (class 2606 OID 386669)
-- Name: d_tax FK_Tenant_DTax; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_tax
    ADD CONSTRAINT "FK_Tenant_DTax" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5090 (class 2606 OID 386724)
-- Name: d_tax_category FK_Tenant_DTaxCategory; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_tax_category
    ADD CONSTRAINT "FK_Tenant_DTaxCategory" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5093 (class 2606 OID 386759)
-- Name: d_uom FK_Tenant_DUOM; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_uom
    ADD CONSTRAINT "FK_Tenant_DUOM" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5130 (class 2606 OID 391475)
-- Name: d_uom_product FK_Tenant_DUOM; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_uom_product
    ADD CONSTRAINT "FK_Tenant_DUOM" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5094 (class 2606 OID 386774)
-- Name: d_user FK_Tenant_DUser; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_user
    ADD CONSTRAINT "FK_Tenant_DUser" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5100 (class 2606 OID 386799)
-- Name: d_vendor FK_Tenant_DVendor; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_vendor
    ADD CONSTRAINT "FK_Tenant_DVendor" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5107 (class 2606 OID 388491)
-- Name: d_vendor_audit FK_Tenant_DVendor; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_vendor_audit
    ADD CONSTRAINT "FK_Tenant_DVendor" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5104 (class 2606 OID 386804)
-- Name: d_voucher FK_Tenant_DVoucher; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_voucher
    ADD CONSTRAINT "FK_Tenant_DVoucher" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5106 (class 2606 OID 386814)
-- Name: d_warehouse FK_Tenant_ID; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_warehouse
    ADD CONSTRAINT "FK_Tenant_ID" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5097 (class 2606 OID 386779)
-- Name: d_userorg_access FK_User_DUserOrgAccess; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_userorg_access
    ADD CONSTRAINT "FK_User_DUserOrgAccess" FOREIGN KEY (d_user_id) REFERENCES pos.d_user(d_user_id);


--
-- TOC entry 5190 (class 2606 OID 394793)
-- Name: d_posterminal_org_access FK_User_DUserOrgAccess; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_posterminal_org_access
    ADD CONSTRAINT "FK_User_DUserOrgAccess" FOREIGN KEY (d_user_id) REFERENCES pos.d_user(d_user_id);


--
-- TOC entry 5099 (class 2606 OID 386789)
-- Name: d_user_role_access FK_User_DUserRoleAccess; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_user_role_access
    ADD CONSTRAINT "FK_User_DUserRoleAccess" FOREIGN KEY (d_user_id) REFERENCES pos.d_user(d_user_id);


--
-- TOC entry 5043 (class 2606 OID 386399)
-- Name: d_locator FK_Warehouse_DLocator; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_locator
    ADD CONSTRAINT "FK_Warehouse_DLocator" FOREIGN KEY (d_warehouse_id) REFERENCES pos.d_warehouse(d_warehouse_id);


--
-- TOC entry 5082 (class 2606 OID 386644)
-- Name: d_storage_onhand FK_Warehouse_DStorageOnhand; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_storage_onhand
    ADD CONSTRAINT "FK_Warehouse_DStorageOnhand" FOREIGN KEY (d_warehouse_id) REFERENCES pos.d_warehouse(d_warehouse_id);


--
-- TOC entry 5152 (class 2606 OID 393197)
-- Name: d_attribute_value FK_attr_attrvalue; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_attribute_value
    ADD CONSTRAINT "FK_attr_attrvalue" FOREIGN KEY (d_attribute_id) REFERENCES pos.d_attribute(d_attribute_id);


--
-- TOC entry 5191 (class 2606 OID 394803)
-- Name: d_posterminal_org_access FK_postermial_org_access; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_posterminal_org_access
    ADD CONSTRAINT "FK_postermial_org_access" FOREIGN KEY (d_pos_terminal_id) REFERENCES pos.d_pos_terminal(d_pos_terminal_id);


--
-- TOC entry 5140 (class 2606 OID 392977)
-- Name: d_table_use_ref FK_reference_domainuse; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_table_use_ref
    ADD CONSTRAINT "FK_reference_domainuse" FOREIGN KEY (d_reference_id) REFERENCES pos.d_reference(d_reference_id);


--
-- TOC entry 5131 (class 2606 OID 392805)
-- Name: d_assign_org_product d_assign_org_product_d_org_d_org_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_assign_org_product
    ADD CONSTRAINT d_assign_org_product_d_org_d_org_id_fk FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5132 (class 2606 OID 392810)
-- Name: d_assign_org_product d_assign_org_product_d_product_d_product_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_assign_org_product
    ADD CONSTRAINT d_assign_org_product_d_product_d_product_id_fk FOREIGN KEY (d_product_id) REFERENCES pos.d_product(d_product_id);


--
-- TOC entry 5133 (class 2606 OID 392815)
-- Name: d_assign_org_product d_assign_org_product_d_tenant_d_tenant_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_assign_org_product
    ADD CONSTRAINT d_assign_org_product_d_tenant_d_tenant_id_fk FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5117 (class 2606 OID 392285)
-- Name: d_kitchen_order d_kitchen_order_d_floor_d_floor_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_kitchen_order
    ADD CONSTRAINT d_kitchen_order_d_floor_d_floor_id_fk FOREIGN KEY (d_floor_id) REFERENCES pos.d_floor(d_floor_id);


--
-- TOC entry 5118 (class 2606 OID 392290)
-- Name: d_kitchen_order d_kitchen_order_d_table_d_table_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_kitchen_order
    ADD CONSTRAINT d_kitchen_order_d_table_d_table_id_fk FOREIGN KEY (d_table_id) REFERENCES pos.d_table(d_table_id);


--
-- TOC entry 5119 (class 2606 OID 392295)
-- Name: d_kitchen_order d_kitchen_order_d_warehouse_d_warehouse_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_kitchen_order
    ADD CONSTRAINT d_kitchen_order_d_warehouse_d_warehouse_id_fk FOREIGN KEY (d_warehouse_id) REFERENCES pos.d_warehouse(d_warehouse_id);


--
-- TOC entry 5122 (class 2606 OID 393878)
-- Name: d_kitchen_orderline d_kitchen_orderline_d_cancel_reason_d_cancel_reason_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_kitchen_orderline
    ADD CONSTRAINT d_kitchen_orderline_d_cancel_reason_d_cancel_reason_id_fk FOREIGN KEY (d_cancel_reason_id) REFERENCES pos.d_cancel_reason(d_cancel_reason_id);


--
-- TOC entry 5123 (class 2606 OID 392300)
-- Name: d_kitchen_orderline d_kitchen_orderline_d_pos_orderline_d_pos_orderline_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_kitchen_orderline
    ADD CONSTRAINT d_kitchen_orderline_d_pos_orderline_d_pos_orderline_id_fk FOREIGN KEY (d_pos_orderline_id) REFERENCES pos.d_pos_orderline(d_pos_orderline_id);


--
-- TOC entry 5124 (class 2606 OID 393862)
-- Name: d_kitchen_orderline d_kitchen_orderline_d_production_d_production_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_kitchen_orderline
    ADD CONSTRAINT d_kitchen_orderline_d_production_d_production_id_fk FOREIGN KEY (d_production_id) REFERENCES pos.d_production(d_production_id);


--
-- TOC entry 5054 (class 2606 OID 394423)
-- Name: d_pos_order d_pos_order_d_doctype_d_doctype_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pos_order
    ADD CONSTRAINT d_pos_order_d_doctype_d_doctype_id_fk FOREIGN KEY (d_doctype_id) REFERENCES pos.d_doctype(d_doctype_id);


--
-- TOC entry 5113 (class 2606 OID 393628)
-- Name: d_pricelist_org d_pricelist_org_d_org__fk; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pricelist_org
    ADD CONSTRAINT d_pricelist_org_d_org__fk FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5110 (class 2606 OID 392503)
-- Name: d_pricelist_product d_pricelist_product_d_pricelist_d_pricelist_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pricelist_product
    ADD CONSTRAINT d_pricelist_product_d_pricelist_d_pricelist_id_fk FOREIGN KEY (d_pricelist_id) REFERENCES pos.d_pricelist(d_pricelist_id);


--
-- TOC entry 5111 (class 2606 OID 392508)
-- Name: d_pricelist_product d_pricelist_product_d_product_d_product_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_pricelist_product
    ADD CONSTRAINT d_pricelist_product_d_product_d_product_id_fk FOREIGN KEY (d_product_id) REFERENCES pos.d_product(d_product_id);


--
-- TOC entry 5060 (class 2606 OID 391903)
-- Name: d_product d_product_d_tax_d_tax_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product
    ADD CONSTRAINT d_product_d_tax_d_tax_id_fk FOREIGN KEY (d_tax_id) REFERENCES pos.d_tax(d_tax_id);


--
-- TOC entry 5063 (class 2606 OID 391812)
-- Name: d_product_category d_productcategory_parent_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_product_category
    ADD CONSTRAINT d_productcategory_parent_id_fk FOREIGN KEY (d_product_category_parent_id) REFERENCES pos.d_product_category(d_product_category_id);


--
-- TOC entry 5076 (class 2606 OID 392141)
-- Name: d_reservation_order d_reservation_order_d_customer_d_customer_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reservation_order
    ADD CONSTRAINT d_reservation_order_d_customer_d_customer_id_fk FOREIGN KEY (d_customer_id) REFERENCES pos.d_customer(d_customer_id);


--
-- TOC entry 5077 (class 2606 OID 392146)
-- Name: d_reservation_order d_reservation_order_d_user_d_user_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_reservation_order
    ADD CONSTRAINT d_reservation_order_d_user_d_user_id_fk FOREIGN KEY (d_user_id) REFERENCES pos.d_user(d_user_id);


--
-- TOC entry 5181 (class 2606 OID 394657)
-- Name: d_request_order d_rq_d_customer; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_request_order
    ADD CONSTRAINT d_rq_d_customer FOREIGN KEY (d_customer_id) REFERENCES pos.d_customer(d_customer_id);


--
-- TOC entry 5182 (class 2606 OID 394595)
-- Name: d_request_order d_rq_d_doctype_d_doctype_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_request_order
    ADD CONSTRAINT d_rq_d_doctype_d_doctype_id_fk FOREIGN KEY (d_doctype_id) REFERENCES pos.d_doctype(d_doctype_id);


--
-- TOC entry 5187 (class 2606 OID 394636)
-- Name: d_request_orderline d_rq_d_rql; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_request_orderline
    ADD CONSTRAINT d_rq_d_rql FOREIGN KEY (d_request_order_id) REFERENCES pos.d_request_order(d_request_order_id);


--
-- TOC entry 5183 (class 2606 OID 394600)
-- Name: d_request_order d_rq_order_d_floor; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_request_order
    ADD CONSTRAINT d_rq_order_d_floor FOREIGN KEY (d_floor_id) REFERENCES pos.d_floor(d_floor_id);


--
-- TOC entry 5184 (class 2606 OID 394605)
-- Name: d_request_order d_rq_order_d_table; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_request_order
    ADD CONSTRAINT d_rq_order_d_table FOREIGN KEY (d_table_id) REFERENCES pos.d_table(d_table_id);


--
-- TOC entry 5188 (class 2606 OID 394631)
-- Name: d_request_orderline d_rqline_d_product; Type: FK CONSTRAINT; Schema: pos; Owner: adempiere
--

ALTER TABLE ONLY pos.d_request_orderline
    ADD CONSTRAINT d_rqline_d_product FOREIGN KEY (d_product_id) REFERENCES pos.d_product(d_product_id);


--
-- TOC entry 5101 (class 2606 OID 394917)
-- Name: d_vendor d_vendor_d_partner_group_d_partner_group_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_vendor
    ADD CONSTRAINT d_vendor_d_partner_group_d_partner_group_id_fk FOREIGN KEY (d_partner_group_id) REFERENCES pos.d_partner_group(d_partner_group_id);


--
-- TOC entry 5102 (class 2606 OID 388542)
-- Name: d_vendor fk1dkfg1ekvbgi1geluuc6jmwbt; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_vendor
    ADD CONSTRAINT fk1dkfg1ekvbgi1geluuc6jmwbt FOREIGN KEY (d_image_id) REFERENCES pos.d_image(d_image_id);


--
-- TOC entry 5027 (class 2606 OID 388254)
-- Name: d_customer fkbse7tsjle0cs5pvoyjp9apunw; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_customer
    ADD CONSTRAINT fkbse7tsjle0cs5pvoyjp9apunw FOREIGN KEY (d_image_id) REFERENCES pos.d_image(d_image_id);


--
-- TOC entry 5095 (class 2606 OID 388259)
-- Name: d_user fkgffw5tlc364ny53nqlaj4dyh6; Type: FK CONSTRAINT; Schema: pos; Owner: postgres
--

ALTER TABLE ONLY pos.d_user
    ADD CONSTRAINT fkgffw5tlc364ny53nqlaj4dyh6 FOREIGN KEY (d_image_id) REFERENCES pos.d_image(d_image_id);


--
-- TOC entry 5018 (class 2606 OID 384952)
-- Name: address fk1_assign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.address
    ADD CONSTRAINT fk1_assign FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 5019 (class 2606 OID 384957)
-- Name: credentials fk2_assign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.credentials
    ADD CONSTRAINT fk2_assign FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 5020 (class 2606 OID 384962)
-- Name: verification_tokens fk3_assign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.verification_tokens
    ADD CONSTRAINT fk3_assign FOREIGN KEY (credential_id) REFERENCES public.credentials(credential_id);


--
-- TOC entry 5017 (class 2606 OID 384873)
-- Name: orders fk5_assign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk5_assign FOREIGN KEY (cart_id) REFERENCES public.carts(cart_id);


--
-- TOC entry 5015 (class 2606 OID 384540)
-- Name: categories fk7_assign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT fk7_assign FOREIGN KEY (parent_category_id) REFERENCES public.categories(category_id);


--
-- TOC entry 5016 (class 2606 OID 384545)
-- Name: products fk8_assign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT fk8_assign FOREIGN KEY (category_id) REFERENCES public.categories(category_id);


--
-- TOC entry 5536 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2024-09-19 15:44:35

--
-- PostgreSQL database dump complete
--

