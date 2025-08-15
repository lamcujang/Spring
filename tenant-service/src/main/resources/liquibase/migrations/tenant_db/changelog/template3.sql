--liquibase formatted sql
--changeset dbiz:template_db2

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
-- TOC entry 7 (class 2615 OID 385480)
-- Name: pos; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA pos;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA pos;




CREATE SEQUENCE pos.d_api_trace_log_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 397 (class 1259 OID 394886)
-- Name: d_api_trace_log; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 330 (class 1259 OID 392795)
-- Name: d_assign_org_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_assign_org_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 331 (class 1259 OID 392797)
-- Name: d_assign_org_product; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 333 (class 1259 OID 392842)
-- Name: d_attribute; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 332 (class 1259 OID 392832)
-- Name: d_attribute_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_attribute_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 351 (class 1259 OID 393182)
-- Name: d_attribute_value; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 350 (class 1259 OID 393180)
-- Name: d_attribute_value_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_attribute_value_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 352 (class 1259 OID 393233)
-- Name: d_bank_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_bank_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 353 (class 1259 OID 393235)
-- Name: d_bank; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 355 (class 1259 OID 393261)
-- Name: d_bankaccount; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 354 (class 1259 OID 393259)
-- Name: d_bankaccount_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_bankaccount_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 327 (class 1259 OID 390960)
-- Name: d_cancel_reason; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 326 (class 1259 OID 390958)
-- Name: d_cancel_reason_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_cancel_reason_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 224 (class 1259 OID 385481)
-- Name: d_changelog; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 265 (class 1259 OID 386825)
-- Name: d_changelog_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_changelog_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 225 (class 1259 OID 385491)
-- Name: d_config; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 266 (class 1259 OID 386827)
-- Name: d_config_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_config_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 337 (class 1259 OID 392910)
-- Name: d_coupon; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 336 (class 1259 OID 392908)
-- Name: d_coupon_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_coupon_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 226 (class 1259 OID 385501)
-- Name: d_currency; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 267 (class 1259 OID 386829)
-- Name: d_currency_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_currency_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 227 (class 1259 OID 385512)
-- Name: d_customer; Type: TABLE; Schema: pos; Owner: -
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
    erp_customer_id numeric(10,0),
    is_pos_vip character varying(1) DEFAULT 'N'::character varying,
    partnername character varying(500),
    credit_limit numeric
);


--
-- TOC entry 268 (class 1259 OID 386831)
-- Name: d_customer_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_customer_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 269 (class 1259 OID 386833)
-- Name: d_doctype_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_doctype_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 228 (class 1259 OID 385528)
-- Name: d_doctype; Type: TABLE; Schema: pos; Owner: -
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
    d_doctype_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    d_org_id numeric,
    is_active character varying(1) DEFAULT 'Y'::character varying
);


--
-- TOC entry 357 (class 1259 OID 393356)
-- Name: d_erp_integration; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 356 (class 1259 OID 393354)
-- Name: d_erp_integration_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_erp_integration_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 229 (class 1259 OID 385535)
-- Name: d_expense; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 230 (class 1259 OID 385546)
-- Name: d_expense_category; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 271 (class 1259 OID 386837)
-- Name: d_expense_category_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_expense_category_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 270 (class 1259 OID 386835)
-- Name: d_expense_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_expense_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 272 (class 1259 OID 386839)
-- Name: d_floor_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_floor_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 231 (class 1259 OID 385554)
-- Name: d_floor; Type: TABLE; Schema: pos; Owner: -
--

CREATE TABLE pos.d_floor (
    d_floor_id numeric(10,0) DEFAULT nextval('pos.d_floor_sq'::regclass) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    floor_no character varying(20) NOT NULL,
    name character varying(255),
    description character varying(255),
    d_floor_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    display_index numeric,
    d_pos_terminal_id numeric(10,0),
    erp_floor_id numeric
);


--
-- TOC entry 282 (class 1259 OID 386859)
-- Name: d_org_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_org_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 241 (class 1259 OID 385716)
-- Name: d_org; Type: TABLE; Schema: pos; Owner: -
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
    erp_org_id numeric(10,0),
    description character varying(255),
    is_summary character varying(1) DEFAULT 'N'::character varying NOT NULL
);


--
-- TOC entry 260 (class 1259 OID 385920)
-- Name: d_userorg_access; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 394 (class 1259 OID 394842)
-- Name: d_get_user_org_access_v; Type: VIEW; Schema: pos; Owner: -
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


--
-- TOC entry 293 (class 1259 OID 386881)
-- Name: d_role_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_role_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 252 (class 1259 OID 385843)
-- Name: d_role; Type: TABLE; Schema: pos; Owner: -
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
    d_role_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    erp_role_id numeric
);


--
-- TOC entry 261 (class 1259 OID 385924)
-- Name: d_user_role_access; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 395 (class 1259 OID 394865)
-- Name: d_get_user_role_access_v; Type: VIEW; Schema: pos; Owner: -
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


--
-- TOC entry 232 (class 1259 OID 385568)
-- Name: d_image; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 273 (class 1259 OID 386841)
-- Name: d_image_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_image_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 233 (class 1259 OID 385579)
-- Name: d_industry; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 274 (class 1259 OID 386843)
-- Name: d_industry_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_industry_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 373 (class 1259 OID 393795)
-- Name: d_integration_history; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 289 (class 1259 OID 386873)
-- Name: d_reference_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_reference_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 248 (class 1259 OID 385794)
-- Name: d_reference; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 290 (class 1259 OID 386875)
-- Name: d_reference_list_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_reference_list_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 249 (class 1259 OID 385816)
-- Name: d_reference_list; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 300 (class 1259 OID 386895)
-- Name: d_user_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_user_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 259 (class 1259 OID 385911)
-- Name: d_user; Type: TABLE; Schema: pos; Owner: -
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
    is_locked character varying(10) DEFAULT 'N'::character varying,
    date_locked date,
    date_last_login date,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    created_by numeric(10,0),
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_by numeric(10,0),
    d_user_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    erp_user_id numeric
);


--
-- TOC entry 390 (class 1259 OID 394705)
-- Name: d_integration_history_get_v; Type: VIEW; Schema: pos; Owner: -
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


--
-- TOC entry 372 (class 1259 OID 393793)
-- Name: d_integration_history_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_integration_history_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 234 (class 1259 OID 385587)
-- Name: d_invoice; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 275 (class 1259 OID 386845)
-- Name: d_invoice_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_invoice_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 242 (class 1259 OID 385728)
-- Name: d_payment; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 284 (class 1259 OID 386863)
-- Name: d_pos_order_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_pos_order_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 243 (class 1259 OID 385742)
-- Name: d_pos_order; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 308 (class 1259 OID 388750)
-- Name: d_pricelist_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_pricelist_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 309 (class 1259 OID 388752)
-- Name: d_pricelist; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 262 (class 1259 OID 385928)
-- Name: d_vendor; Type: TABLE; Schema: pos; Owner: -
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
    wards character varying(100),
    erp_vendor_id numeric,
    is_pos_vip character varying(1) DEFAULT 'N'::character varying,
    partnername character varying(500),
    discount numeric,
    credit_limit numeric
);


--
-- TOC entry 403 (class 1259 OID 394991)
-- Name: d_invoice_v; Type: VIEW; Schema: pos; Owner: -
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


--
-- TOC entry 235 (class 1259 OID 385611)
-- Name: d_invoiceline; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 276 (class 1259 OID 386847)
-- Name: d_invoiceline_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_invoiceline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 319 (class 1259 OID 390871)
-- Name: d_kitchen_order; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 318 (class 1259 OID 390869)
-- Name: d_kitchen_order_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_kitchen_order_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 321 (class 1259 OID 390892)
-- Name: d_kitchen_orderline; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 320 (class 1259 OID 390890)
-- Name: d_kitchen_orderline_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_kitchen_orderline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 286 (class 1259 OID 386867)
-- Name: d_product_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_product_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 245 (class 1259 OID 385767)
-- Name: d_product; Type: TABLE; Schema: pos; Owner: -
--

CREATE TABLE pos.d_product (
    d_product_id numeric(10,0) DEFAULT nextval('pos.d_product_sq'::regclass) NOT NULL,
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


--
-- TOC entry 410 (class 1259 OID 426995)
-- Name: d_kitchen_orderline_v; Type: VIEW; Schema: pos; Owner: -
--

CREATE VIEW pos.d_kitchen_orderline_v AS
 SELECT dkol.d_kitchen_orderline_id,
    dkol.d_kitchen_order_id,
    dkol.d_tenant_id,
    dkol.d_org_id,
    dkol.orderline_status AS order_status_value,
    dkol.note,
    dkol.qty,
    dkol.transfer_qty,
    dkol.cancel_qty,
    dkol.priority,
    dkol.is_active,
    dkol.description,
    dkol.created,
    dkol.created_by,
    dkol.updated,
    dkol.updated_by,
    dkol.d_kitchen_orderline_uu,
    dkol.d_cancel_reason_id,
    dcr.name AS cancel_reason,
    drl.name AS order_status,
    dkol.d_pos_orderline_id,
    dkol.d_production_id,
    dp.name AS product_name,
    dp.d_product_id,
    dp.product_type
   FROM ((((pos.d_kitchen_orderline dkol
     LEFT JOIN pos.d_cancel_reason dcr ON ((dkol.d_cancel_reason_id = dcr.d_cancel_reason_id)))
     LEFT JOIN pos.d_reference_list drl ON (((dkol.orderline_status)::text = (drl.value)::text)))
     LEFT JOIN pos.d_reference dr ON (((drl.d_reference_id = dr.d_reference_id) AND ((dr.name)::text = 'Order Status'::text))))
     LEFT JOIN pos.d_product dp ON ((dkol.d_product_id = dp.d_product_id)));


--
-- TOC entry 236 (class 1259 OID 385625)
-- Name: d_language; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 277 (class 1259 OID 386849)
-- Name: d_language_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_language_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 237 (class 1259 OID 385635)
-- Name: d_locator; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 278 (class 1259 OID 386851)
-- Name: d_locator_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_locator_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 325 (class 1259 OID 390937)
-- Name: d_note; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 323 (class 1259 OID 390919)
-- Name: d_note_group; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 322 (class 1259 OID 390917)
-- Name: d_note_group_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_note_group_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 324 (class 1259 OID 390935)
-- Name: d_note_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_note_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 238 (class 1259 OID 385645)
-- Name: d_notification; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 279 (class 1259 OID 386853)
-- Name: d_notification_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_notification_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 239 (class 1259 OID 385682)
-- Name: d_order; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 280 (class 1259 OID 386855)
-- Name: d_order_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_order_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 240 (class 1259 OID 385703)
-- Name: d_orderline; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 281 (class 1259 OID 386857)
-- Name: d_orderline_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_orderline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 315 (class 1259 OID 389549)
-- Name: d_partner_group; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 314 (class 1259 OID 389417)
-- Name: d_partner_group_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_partner_group_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 369 (class 1259 OID 393709)
-- Name: d_pay_method; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 368 (class 1259 OID 393707)
-- Name: d_pay_method_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_pay_method_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 283 (class 1259 OID 386861)
-- Name: d_payment_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_payment_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 371 (class 1259 OID 393764)
-- Name: d_paymethod_org; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 370 (class 1259 OID 393739)
-- Name: d_paymethod_org_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_paymethod_org_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 349 (class 1259 OID 393129)
-- Name: d_pc_terminalaccess; Type: TABLE; Schema: pos; Owner: -
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
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    d_product_category_id numeric
);


--
-- TOC entry 348 (class 1259 OID 393127)
-- Name: d_pc_terminalaccess_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_pc_terminalaccess_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 380 (class 1259 OID 394310)
-- Name: d_purchase_orderline_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_purchase_orderline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 381 (class 1259 OID 394312)
-- Name: d_purchase_orderline; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 382 (class 1259 OID 394365)
-- Name: d_po_detail_v; Type: VIEW; Schema: pos; Owner: -
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


--
-- TOC entry 378 (class 1259 OID 394284)
-- Name: d_purchase_order_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_purchase_order_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 379 (class 1259 OID 394286)
-- Name: d_purchase_order; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 367 (class 1259 OID 393667)
-- Name: d_reference_get_v; Type: VIEW; Schema: pos; Owner: -
--

CREATE VIEW pos.d_reference_get_v AS
 SELECT drl.d_reference_list_id AS d_reference_id,
    dr.name AS name_reference,
    drl.name,
    drl.value
   FROM (pos.d_reference dr
     JOIN pos.d_reference_list drl ON ((dr.d_reference_id = drl.d_reference_id)));


--
-- TOC entry 264 (class 1259 OID 385963)
-- Name: d_warehouse; Type: TABLE; Schema: pos; Owner: -
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
    printer_ip character varying(100),
    device_token character varying(255)
);


--
-- TOC entry 384 (class 1259 OID 394445)
-- Name: d_po_header_v; Type: VIEW; Schema: pos; Owner: -
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


--
-- TOC entry 406 (class 1259 OID 409266)
-- Name: d_pos_closedcash_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_pos_closedcash_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 407 (class 1259 OID 409290)
-- Name: d_pos_closedcash; Type: TABLE; Schema: pos; Owner: -
--

CREATE TABLE pos.d_pos_closedcash (
    d_pos_closedcash_id numeric(10,0) DEFAULT nextval('pos.d_pos_closedcash_sq'::regclass) NOT NULL,
    d_tenant_id numeric(10,0) NOT NULL,
    d_org_id numeric(10,0) NOT NULL,
    is_active character varying(1) DEFAULT 'Y'::character varying NOT NULL,
    created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by numeric(10,0) NOT NULL,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by numeric(10,0) NOT NULL,
    shift_type character varying(15),
    start_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    end_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    d_pos_terminal_id numeric(10,0) NOT NULL,
    pos_host character varying(255),
    d_user_id numeric(10,0) NOT NULL,
    host_sequence numeric(10,0),
    d_pos_closedcash_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    start_cash numeric,
    end_cash numeric,
    transfer_cash numeric,
    cash_diff numeric,
    d_doctype_id numeric,
    document_no character varying(100)
);


--
-- TOC entry 244 (class 1259 OID 385754)
-- Name: d_pos_orderline; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 285 (class 1259 OID 386865)
-- Name: d_pos_orderline_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_pos_orderline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 412 (class 1259 OID 496489)
-- Name: d_pos_orderline_v_all; Type: VIEW; Schema: pos; Owner: -
--

CREATE VIEW pos.d_pos_orderline_v_all AS
 SELECT DISTINCT dpo.d_pos_orderline_id,
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
    dp.product_type,
    dp.d_product_category_id
   FROM ((((pos.d_pos_orderline dpo
     LEFT JOIN pos.d_product dp ON ((dpo.d_product_id = dp.d_product_id)))
     LEFT JOIN pos.d_kitchen_orderline dko ON ((dko.d_pos_orderline_id = dpo.d_pos_orderline_id)))
     LEFT JOIN pos.d_reference_list drl ON (((dko.orderline_status)::text = (drl.value)::text)))
     LEFT JOIN pos.d_reference dr ON (((drl.d_reference_id = dr.d_reference_id) AND ((dr.name)::text = 'Order Status'::text))));


--
-- TOC entry 393 (class 1259 OID 394786)
-- Name: d_pos_org_access; Type: TABLE; Schema: pos; Owner: -
--

CREATE TABLE pos.d_pos_org_access (
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


--
-- TOC entry 345 (class 1259 OID 393071)
-- Name: d_pos_payment; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 344 (class 1259 OID 393069)
-- Name: d_pos_payment_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_pos_payment_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 347 (class 1259 OID 393099)
-- Name: d_pos_taxline; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 346 (class 1259 OID 393097)
-- Name: d_pos_taxline_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_pos_taxline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 335 (class 1259 OID 392879)
-- Name: d_pos_terminal; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 334 (class 1259 OID 392877)
-- Name: d_pos_terminal_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_pos_terminal_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 385 (class 1259 OID 394526)
-- Name: d_pos_terminal_v; Type: VIEW; Schema: pos; Owner: -
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


--
-- TOC entry 404 (class 1259 OID 395000)
-- Name: d_posorder_complete_v; Type: VIEW; Schema: pos; Owner: -
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


--
-- TOC entry 405 (class 1259 OID 409261)
-- Name: d_posterminal_org_access_v; Type: VIEW; Schema: pos; Owner: -
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
   FROM (pos.d_pos_org_access dpoa
     LEFT JOIN pos.d_pos_terminal dpt ON ((dpoa.d_pos_terminal_id = dpt.d_pos_terminal_id)));


--
-- TOC entry 313 (class 1259 OID 388792)
-- Name: d_pricelist_org; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 366 (class 1259 OID 393623)
-- Name: d_pricelist_org_info_v; Type: VIEW; Schema: pos; Owner: -
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


--
-- TOC entry 312 (class 1259 OID 388790)
-- Name: d_pricelist_org_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_pricelist_org_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 310 (class 1259 OID 388769)
-- Name: d_pricelist_product_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_pricelist_product_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 311 (class 1259 OID 388771)
-- Name: d_pricelist_product; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 365 (class 1259 OID 393588)
-- Name: d_pricelist_v; Type: VIEW; Schema: pos; Owner: -
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


--
-- TOC entry 364 (class 1259 OID 393562)
-- Name: d_pricelist_v_find_id; Type: VIEW; Schema: pos; Owner: -
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


--
-- TOC entry 317 (class 1259 OID 390225)
-- Name: d_print_report; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 316 (class 1259 OID 390223)
-- Name: d_print_report_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_print_report_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 246 (class 1259 OID 385779)
-- Name: d_product_category; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 287 (class 1259 OID 386869)
-- Name: d_product_category_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_product_category_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 288 (class 1259 OID 386871)
-- Name: d_product_combo_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_product_combo_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 247 (class 1259 OID 385787)
-- Name: d_product_combo; Type: TABLE; Schema: pos; Owner: -
--

CREATE TABLE pos.d_product_combo (
    d_product_combo_id numeric(10,0) DEFAULT nextval('pos.d_product_combo_sq'::regclass) NOT NULL,
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


--
-- TOC entry 343 (class 1259 OID 393046)
-- Name: d_product_location; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 342 (class 1259 OID 393044)
-- Name: d_product_location_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_product_location_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 358 (class 1259 OID 393401)
-- Name: d_production_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_production_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 359 (class 1259 OID 393403)
-- Name: d_production; Type: TABLE; Schema: pos; Owner: -
--

CREATE TABLE pos.d_production (
    d_production_id numeric(10,0) DEFAULT nextval('pos.d_production_sq'::regclass) NOT NULL,
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


--
-- TOC entry 361 (class 1259 OID 393431)
-- Name: d_productionline; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 360 (class 1259 OID 393429)
-- Name: d_productionline_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_productionline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 363 (class 1259 OID 393488)
-- Name: d_reconciledetail; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 362 (class 1259 OID 393486)
-- Name: d_reconciledetail_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_reconciledetail_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 386 (class 1259 OID 394549)
-- Name: d_request_order_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_request_order_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 388 (class 1259 OID 394573)
-- Name: d_request_order; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 254 (class 1259 OID 385866)
-- Name: d_table; Type: TABLE; Schema: pos; Owner: -
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
    erp_table_id numeric(10,0),
    is_buffet character varying(1) DEFAULT 'N'::character varying
);


--
-- TOC entry 391 (class 1259 OID 394744)
-- Name: d_request_order_get_all_v; Type: VIEW; Schema: pos; Owner: -
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


--
-- TOC entry 387 (class 1259 OID 394551)
-- Name: d_request_orderline_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_request_orderline_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 389 (class 1259 OID 394610)
-- Name: d_request_orderline; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 258 (class 1259 OID 385904)
-- Name: d_uom; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 392 (class 1259 OID 394759)
-- Name: d_request_orderline_get_all_v; Type: VIEW; Schema: pos; Owner: -
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


--
-- TOC entry 250 (class 1259 OID 385824)
-- Name: d_reservation_line; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 291 (class 1259 OID 386877)
-- Name: d_reservation_line_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_reservation_line_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 251 (class 1259 OID 385832)
-- Name: d_reservation_order; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 292 (class 1259 OID 386879)
-- Name: d_reservation_order_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_reservation_order_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 408 (class 1259 OID 426972)
-- Name: d_reservation_v_all; Type: VIEW; Schema: pos; Owner: -
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
     LEFT JOIN pos.d_user du ON ((dro.d_user_id = du.d_user_id)))
  WHERE ((dr.name)::text = 'Table Reservation Status'::text);


--
-- TOC entry 411 (class 1259 OID 473363)
-- Name: d_sc_posorder_get_v; Type: VIEW; Schema: pos; Owner: -
--

CREATE VIEW pos.d_sc_posorder_get_v AS
SELECT
    NULL::numeric(10,0) AS d_shift_control_id,
    NULL::numeric(10,0) AS d_tenant_id,
    NULL::numeric(10,0) AS d_org_id,
    NULL::timestamp without time zone AS created,
    NULL::numeric(10,0) AS created_by,
    NULL::character varying(1) AS is_active,
    NULL::timestamp without time zone AS updated,
    NULL::numeric(10,0) AS updated_by,
    NULL::numeric AS cash,
    NULL::bigint AS qty_cash,
    NULL::numeric AS visa,
    NULL::bigint AS qty_visa,
    NULL::numeric AS deb,
    NULL::bigint AS qty_deb,
    NULL::numeric AS loyalty,
    NULL::bigint AS qty_loyalty,
    NULL::numeric AS bank,
    NULL::bigint AS qty_bank;


--
-- TOC entry 340 (class 1259 OID 392997)
-- Name: d_shift_control_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_shift_control_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 341 (class 1259 OID 393021)
-- Name: d_shift_control; Type: TABLE; Schema: pos; Owner: -
--

CREATE TABLE pos.d_shift_control (
    d_shift_control_id numeric(10,0) DEFAULT nextval('pos.d_shift_control_sq'::regclass) NOT NULL,
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
    erp_shift_control_id numeric(10,0),
    start_cash numeric,
    transfer_cash numeric,
    cash_diff numeric,
    d_doctype_id numeric,
    document_no character varying(100),
    descriptions character varying(255)
);


--
-- TOC entry 253 (class 1259 OID 385855)
-- Name: d_storage_onhand; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 294 (class 1259 OID 386883)
-- Name: d_storage_onhand_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_storage_onhand_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 409 (class 1259 OID 426981)
-- Name: d_table_pos_v; Type: VIEW; Schema: pos; Owner: -
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


--
-- TOC entry 295 (class 1259 OID 386885)
-- Name: d_table_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_table_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 339 (class 1259 OID 392962)
-- Name: d_table_use_ref; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 338 (class 1259 OID 392960)
-- Name: d_table_use_ref_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_table_use_ref_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 255 (class 1259 OID 385874)
-- Name: d_tax; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 256 (class 1259 OID 385884)
-- Name: d_tax_category; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 297 (class 1259 OID 386889)
-- Name: d_tax_category_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_tax_category_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 296 (class 1259 OID 386887)
-- Name: d_tax_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_tax_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 298 (class 1259 OID 386891)
-- Name: d_tenant_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_tenant_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 257 (class 1259 OID 385893)
-- Name: d_tenant; Type: TABLE; Schema: pos; Owner: -
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
    d_tenant_uu character varying(36) DEFAULT pos.uuid_generate_v4() NOT NULL,
    db_user_name character varying(64),
    db_password character varying(100),
    db_name character varying(255),
    creation_status character varying(50)
);


--
-- TOC entry 328 (class 1259 OID 391463)
-- Name: d_uom_product; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 329 (class 1259 OID 391485)
-- Name: d_uom_product_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_uom_product_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 299 (class 1259 OID 386893)
-- Name: d_uom_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_uom_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 305 (class 1259 OID 388478)
-- Name: d_vendor_audit; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 301 (class 1259 OID 386897)
-- Name: d_vendor_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_vendor_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 263 (class 1259 OID 385939)
-- Name: d_voucher; Type: TABLE; Schema: pos; Owner: -
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


--
-- TOC entry 302 (class 1259 OID 386899)
-- Name: d_voucher_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_voucher_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 303 (class 1259 OID 386901)
-- Name: d_warehouse_sq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.d_warehouse_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 413 (class 1259 OID 498701)
-- Name: get_kitchen_order_bystatus_v; Type: VIEW; Schema: pos; Owner: -
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
    dp.product_type,
    dp.d_product_category_id
   FROM ((((pos.d_kitchen_orderline dkol
     LEFT JOIN pos.d_product dp ON ((dkol.d_product_id = dp.d_product_id)))
     LEFT JOIN pos.d_reference_get_v drgv ON (((drgv.value)::text = (dkol.orderline_status)::text)))
     LEFT JOIN pos.d_production dpr ON ((dkol.d_production_id = dpr.d_production_id)))
     LEFT JOIN pos.d_cancel_reason dcr ON ((dcr.d_cancel_reason_id = dkol.d_cancel_reason_id)))
  WHERE ((drgv.name_reference)::text = 'Order Status'::text);


--
-- TOC entry 307 (class 1259 OID 388552)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 383 (class 1259 OID 394438)
-- Name: report_sales; Type: VIEW; Schema: pos; Owner: -
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


--
-- TOC entry 304 (class 1259 OID 388439)
-- Name: revinfo; Type: TABLE; Schema: pos; Owner: -
--

CREATE TABLE pos.revinfo (
    rev bigint NOT NULL,
    revtstmp bigint NOT NULL,
    revtype smallint
);


--
-- TOC entry 414 (class 1259 OID 500927)
-- Name: shift_control_get_list_pos_order_v; Type: VIEW; Schema: pos; Owner: -
--

CREATE VIEW pos.shift_control_get_list_pos_order_v AS
 SELECT dpo.d_pos_order_id,
    dpo.d_tenant_id,
    dpo.d_org_id,
    dpo.created_by,
    dpo.created,
    dpo.updated_by,
    dpo.updated,
    dpo.is_active,
    dpo.document_no,
    dpo.d_shift_control_id,
    dpo.order_date,
    dc.name,
    dpp.payment_method,
    di.total_amount AS total_invoice,
    dp.payment_amount AS total_payment,
    0 AS dbeb,
    dpo.order_status
   FROM ((((pos.d_pos_order dpo
     JOIN pos.d_customer dc ON ((dpo.d_customer_id = dc.d_customer_id)))
     LEFT JOIN pos.d_payment dp ON ((dpo.d_pos_order_id = dp.d_pos_order_id)))
     LEFT JOIN pos.d_pos_payment dpp ON ((dpo.d_pos_order_id = dpp.d_pos_order_id)))
     LEFT JOIN pos.d_invoice di ON ((dpo.d_pos_order_id = di.d_pos_order_id)))
  WHERE (((dpo.order_status)::text = 'COM'::text) OR ((dpo.order_status)::text = 'COM'::text));


--
-- TOC entry 415 (class 1259 OID 500932)
-- Name: shift_control_get_payment_v; Type: VIEW; Schema: pos; Owner: -
--

CREATE VIEW pos.shift_control_get_payment_v AS
 SELECT dpo.d_pos_order_id,
    dpo.d_tenant_id,
    dpo.d_org_id,
    dpo.created_by,
    dpo.created,
    dpo.updated_by,
    dpo.updated,
    dpo.is_active,
    dp.document_no,
    dpo.d_shift_control_id,
    dpo.order_date,
    dc.name AS customer_name,
    du.full_name AS user_name,
    dpp.payment_method,
    dp.payment_amount AS total_payment,
    ''::text AS payment_type
   FROM ((((pos.d_pos_order dpo
     JOIN pos.d_customer dc ON ((dpo.d_customer_id = dc.d_customer_id)))
     LEFT JOIN pos.d_payment dp ON ((dpo.d_pos_order_id = dp.d_pos_order_id)))
     LEFT JOIN pos.d_pos_payment dpp ON ((dpo.d_pos_order_id = dpp.d_pos_order_id)))
     LEFT JOIN pos.d_user du ON ((du.d_user_id = dpo.d_user_id)))
  WHERE (((dpo.order_status)::text = 'COM'::text) OR ((dpo.order_status)::text = 'COM'::text));


--
-- TOC entry 401 (class 1259 OID 394978)
-- Name: tenants; Type: TABLE; Schema: pos; Owner: -
--

CREATE TABLE pos.tenants (
    id bigint,
    name character varying,
    db_name character varying,
    user_name character varying,
    db_password character varying,
    creation_status character varying
);


--
-- TOC entry 402 (class 1259 OID 394984)
-- Name: tenants_id_seq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.tenants_id_seq
    START WITH 2
    INCREMENT BY 1
    MINVALUE 2
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 400 (class 1259 OID 394972)
-- Name: user_roles; Type: TABLE; Schema: pos; Owner: -
--

CREATE TABLE pos.user_roles (
    user_id bigint,
    role character varying
);


--
-- TOC entry 398 (class 1259 OID 394963)
-- Name: users; Type: TABLE; Schema: pos; Owner: -
--

CREATE TABLE pos.users (
    id bigint,
    email character varying,
    password character varying,
    tenant_id bigint
);


--
-- TOC entry 399 (class 1259 OID 394969)
-- Name: users_id_seq; Type: SEQUENCE; Schema: pos; Owner: -
--

CREATE SEQUENCE pos.users_id_seq
    START WITH 3
    INCREMENT BY 1
    MINVALUE 3
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5487 (class 0 OID 0)
-- Dependencies: 399
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: pos; Owner: -
--

ALTER SEQUENCE pos.users_id_seq OWNED BY pos.users.id;


--
-- TOC entry 4703 (class 2604 OID 394971)
-- Name: users id; Type: DEFAULT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.users ALTER COLUMN id SET DEFAULT nextval('pos.users_id_seq'::regclass);


--
-- TOC entry 5474 (class 0 OID 394886)
-- Dependencies: 397
-- Data for Name: d_api_trace_log; Type: TABLE DATA; Schema: pos; Owner: -
--



--
-- TOC entry 5425 (class 0 OID 392797)
-- Dependencies: 331
-- Data for Name: d_assign_org_product; Type: TABLE DATA; Schema: pos; Owner: -
--


--
-- TOC entry 5427 (class 0 OID 392842)
-- Dependencies: 333
-- Data for Name: d_attribute; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_attribute VALUES (1000002, 1000004, '2024-08-27', 1000069, '2024-08-27', 1000069, 'Y', 'Size1', 'dcc75129-4418-4902-999d-d49055cc7c63', 0);
INSERT INTO pos.d_attribute VALUES (1000001, 1000004, '2024-08-27', 1000069, '2024-08-27', 1000069, 'Y', 'Size', '514724e9-531a-477c-8855-07e57597173b', 0);
INSERT INTO pos.d_attribute VALUES (1000004, 1000004, '2024-08-29', 1000069, '2024-08-29', 1000069, 'Y', 'Size2', 'b6c02f7f-bd14-4f10-9c40-b18bb78bda0f', 0);
INSERT INTO pos.d_attribute VALUES (1000003, 1000004, '2024-08-29', 1000069, '2024-08-29', 1000069, 'Y', 'Kích Thước', 'a599c891-e6d4-431b-a094-14f293c3cb22', 0);
INSERT INTO pos.d_attribute VALUES (1000005, 1000004, '2024-08-29', 1000069, '2024-08-29', 1000069, 'Y', 'Size1', '42389216-df52-4861-a744-5aa8eef3f9ba', 1000016);
INSERT INTO pos.d_attribute VALUES (1000006, 1000004, '2024-08-29', 1000069, '2024-08-29', 1000069, 'Y', 'Size', 'b91de280-18d8-4585-8137-5f2e63a540a4', 1000016);
INSERT INTO pos.d_attribute VALUES (1000007, 1000004, '2024-09-05', 1000069, '2024-09-05', 1000069, 'Y', 'Size2', 'ca11f797-a51d-4000-9318-b087e9fd82ef', 1000016);
INSERT INTO pos.d_attribute VALUES (1000008, 1000004, '2024-09-05', 1000069, '2024-09-05', 1000069, 'Y', 'Size2', '457d39a9-2fe8-41c6-90a2-3e5dc3b166ad', 1000016);
INSERT INTO pos.d_attribute VALUES (1000009, 1000009, '2024-09-12', 1000071, '2024-09-12', 1000071, 'Y', 'Sóng', 'd24057a5-e70f-4534-973f-40528feb99c7', 1000022);
INSERT INTO pos.d_attribute VALUES (1000010, 1000009, '2024-09-12', 1000071, '2024-09-12', 1000071, 'Y', 'Độ dày', '9db6761e-3bf7-4349-8b57-a382288c15ed', 1000022);


--
-- TOC entry 5445 (class 0 OID 393182)
-- Dependencies: 351
-- Data for Name: d_attribute_value; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_attribute_value VALUES (1000001, 1000004, '2024-08-29', 1000069, '2024-08-29', 1000069, 'Y', 'M', NULL, 'cf396ce4-d727-4c36-a575-5910a870f6c0', 0, 1000001);
INSERT INTO pos.d_attribute_value VALUES (1000000, 1000004, '2024-08-29', 1000069, '2024-08-29', 1000069, 'Y', 'S', NULL, 'a0929bfd-07fb-4a58-b5b1-2091629eb4ef', 0, 1000001);


--
-- TOC entry 5447 (class 0 OID 393235)
-- Dependencies: 353
-- Data for Name: d_bank; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_bank VALUES (1000003, 1000004, 0, 'Ngân hàng TMCP Đầu tư và Phát triển Việt Nam', NULL, '970418', 'BIDVVNVX', NULL, '2024-09-09 07:05:47.133238', 0, '2024-09-09 07:05:47.133238', 0, '948a3e54-70a5-47f9-92a0-d54caf093944', 'Y');
INSERT INTO pos.d_bank VALUES (1000004, 1000004, 0, 'Ngân hàng TMCP Kỹ thương Việt Nam', '970407', '970407', 'VTCBVNVX', NULL, '2024-09-09 07:07:17.046737', 0, '2024-09-09 07:07:17.046737', 0, 'fea7892a-8d64-405d-85ea-c77b8b8727f9', 'Y');
INSERT INTO pos.d_bank VALUES (1000002, 1000004, 0, 'Ngân hàng TMCP Ngoại Thương Việt Nam', '970436', '970436', 'BFTVVNVX', NULL, '2024-09-09 07:04:43.641768', 0, '2024-09-09 07:04:43.641768', 0, '10da6fb2-cd33-4fec-baf9-7e46dd634e4b', 'Y');
INSERT INTO pos.d_bank VALUES (1000001, 1000004, 0, 'Ngân hàng thương mại cổ phần Quân đội', '970422', '970422', 'MSCBVNVX', NULL, '2024-09-09 00:06:58.36863', 1000069, '2024-09-09 00:06:58.36863', 1000069, 'ddae593a-79a5-4363-8f6e-3ea4f42ee6cc', 'Y');
INSERT INTO pos.d_bank VALUES (1000005, 1000004, 0, 'Ngân hàng TMCP Công thương Việt Nam', NULL, '970415', 'ICBVVNVX', NULL, '2024-09-10 03:51:52.358861', 0, '2024-09-10 03:51:52.358861', 0, '36fec6b2-e843-4348-b02e-5e5f897cba4c', 'Y');
INSERT INTO pos.d_bank VALUES (1000006, 1000004, 0, 'Ngân hàng Nông nghiệp và Phát triển Nông thôn Việt Nam', NULL, '970405', 'VBAAVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '79fc1119-7a0a-4795-a5df-fc16d0eb3a60', 'Y');
INSERT INTO pos.d_bank VALUES (1000007, 1000004, 0, 'Ngân hàng TMCP Phương Đông', NULL, '970448', 'ORCOVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'e093e23b-8193-449f-8db4-0be7c9dcb33f', 'Y');
INSERT INTO pos.d_bank VALUES (1000008, 1000004, 0, 'Ngân hàng TMCP Á Châu', NULL, '970416', 'ASCBVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '16def695-87ad-4445-a556-47d33524436b', 'Y');
INSERT INTO pos.d_bank VALUES (1000009, 1000004, 0, 'Ngân hàng TMCP Việt Nam Thịnh Vượng', NULL, '970432', 'VPBKVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '37d03e3f-2ad8-4798-88e9-96566e9e137e', 'Y');
INSERT INTO pos.d_bank VALUES (1000010, 1000004, 0, 'Ngân hàng TMCP Tiên Phong', NULL, '970423', 'TPBVVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '080b1a71-26a3-4ef5-88e5-3734b1a8f40d', 'Y');
INSERT INTO pos.d_bank VALUES (1000011, 1000004, 0, 'Ngân hàng TMCP Sài Gòn Thương Tín', NULL, '970403', 'SGTTVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '5ca607c5-eebb-40b4-896b-0e9619b4466c', 'Y');
INSERT INTO pos.d_bank VALUES (1000012, 1000004, 0, 'Ngân hàng TMCP Phát triển Thành phố Hồ Chí Minh', NULL, '970437', 'HDBCVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '7d78072d-aecc-42e9-a500-348739c695fe', 'Y');
INSERT INTO pos.d_bank VALUES (1000013, 1000004, 0, 'Ngân hàng TMCP Bản Việt', NULL, '970454', 'VCBCVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '308a1d8e-4ba5-4a7e-b87a-47d506e122ee', 'Y');
INSERT INTO pos.d_bank VALUES (1000014, 1000004, 0, 'Ngân hàng TMCP Sài Gòn', NULL, '970429', 'SACLVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '206a5f09-53e0-4270-a36b-b53e1f3824e3', 'Y');
INSERT INTO pos.d_bank VALUES (1000015, 1000004, 0, 'Ngân hàng TMCP Quốc tế Việt Nam', NULL, '970441', 'VNIBVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '4d14e695-725f-495f-bf22-4aff504fb82d', 'Y');
INSERT INTO pos.d_bank VALUES (1000016, 1000004, 0, 'Ngân hàng TMCP Sài Gòn - Hà Nội', NULL, '970443', 'SHBAVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '3690e4e2-224f-4eca-a84a-11bc0772a3c4', 'Y');
INSERT INTO pos.d_bank VALUES (1000017, 1000004, 0, 'Ngân hàng TMCP Xuất Nhập khẩu Việt Nam', NULL, '970431', 'EBVIVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'f85dd136-affb-47fc-b499-a07e6714ec10', 'Y');
INSERT INTO pos.d_bank VALUES (1000018, 1000004, 0, 'Ngân hàng TMCP Hàng Hải', NULL, '970426', 'MCOBVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'dc67292a-7acd-457c-a860-35f79fc3c8a4', 'Y');
INSERT INTO pos.d_bank VALUES (1000019, 1000004, 0, 'TMCP Việt Nam Thịnh Vượng - Ngân hàng số CAKE by VPBank', NULL, '546034', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '2f3d88d7-5fdf-4d54-9b84-7fb9ca2329f6', 'Y');
INSERT INTO pos.d_bank VALUES (1000020, 1000004, 0, 'TMCP Việt Nam Thịnh Vượng - Ngân hàng số Ubank by VPBank', NULL, '546035', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '28d8d6f2-03d0-4dd0-951a-f3ba5f9f9311', 'Y');
INSERT INTO pos.d_bank VALUES (1000021, 1000004, 0, 'Ngân hàng số Timo by Ban Viet Bank (Timo by Ban Viet Bank)', NULL, '963388', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'c789deed-8e1f-4f63-9994-e1ba01b64079', 'Y');
INSERT INTO pos.d_bank VALUES (1000022, 1000004, 0, 'Tổng Công ty Dịch vụ số Viettel - Chi nhánh tập đoàn công nghiệp viễn thông Quân Đội', NULL, '971005', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '3c290458-25cf-4c4f-a81a-d088d3c2f359', 'Y');
INSERT INTO pos.d_bank VALUES (1000023, 1000004, 0, 'VNPT Money', NULL, '971011', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '33048ea6-e84d-4bf5-b6f6-700890fe4021', 'Y');
INSERT INTO pos.d_bank VALUES (1000024, 1000004, 0, 'Ngân hàng TMCP Sài Gòn Công Thương', NULL, '970400', 'SBITVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'c0a34d64-62c3-41b7-8311-1093da7b19c7', 'Y');
INSERT INTO pos.d_bank VALUES (1000025, 1000004, 0, 'Ngân hàng TMCP Bắc Á', NULL, '970409', 'NASCVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'cec57356-de51-4005-9898-04cbf4737286', 'Y');
INSERT INTO pos.d_bank VALUES (1000026, 1000004, 0, 'Ngân hàng TMCP Đại Chúng Việt Nam', NULL, '970412', 'WBVNVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '49cbed90-b7ae-4fb0-b1d8-240d5ca84144', 'Y');
INSERT INTO pos.d_bank VALUES (1000027, 1000004, 0, 'Ngân hàng Thương mại TNHH MTV Đại Dương', NULL, '970414', 'OCBKUS3M', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'e0bb4fff-7de3-47e7-be57-46bbaed26fa6', 'Y');
INSERT INTO pos.d_bank VALUES (1000028, 1000004, 0, 'Ngân hàng TMCP Quốc Dân', NULL, '970419', 'NVBAVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '99e10b0f-24be-4bfa-b546-57ac8b5eefdf', 'Y');
INSERT INTO pos.d_bank VALUES (1000029, 1000004, 0, 'Ngân hàng TNHH MTV Shinhan Việt Nam', NULL, '970424', 'SHBKVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'b0b04b97-7c62-4cc3-8930-b706fe8b29c3', 'Y');
INSERT INTO pos.d_bank VALUES (1000030, 1000004, 0, 'Ngân hàng TMCP An Bình', NULL, '970425', 'ABBKVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '550ab041-27f5-4d79-80b9-c7f124335ab7', 'Y');
INSERT INTO pos.d_bank VALUES (1000031, 1000004, 0, 'Ngân hàng TMCP Việt Á', NULL, '970427', 'VNACVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '3da9de86-7226-45dd-8b5e-d610ad496e3e', 'Y');
INSERT INTO pos.d_bank VALUES (1000032, 1000004, 0, 'Ngân hàng TMCP Nam Á', NULL, '970428', 'NAMAVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '1451ddb5-66b5-4a99-a479-8a76d59d922a', 'Y');
INSERT INTO pos.d_bank VALUES (1000033, 1000004, 0, 'Ngân hàng TMCP Xăng dầu Petrolimex', NULL, '970430', 'PGBLVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'd1d54505-1db7-4472-86f0-9e74d18f2a8a', 'Y');
INSERT INTO pos.d_bank VALUES (1000034, 1000004, 0, 'Ngân hàng TMCP Việt Nam Thương Tín', NULL, '970433', 'VNTTVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '12a89b79-4d33-413e-bc57-d42570d6510b', 'Y');
INSERT INTO pos.d_bank VALUES (1000035, 1000004, 0, 'Ngân hàng TMCP Bảo Việt', NULL, '970438', 'BVBVVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '4251b3ad-19aa-4a70-b33f-cad01ecf4318', 'Y');
INSERT INTO pos.d_bank VALUES (1000036, 1000004, 0, 'Ngân hàng TMCP Đông Nam Á', NULL, '970440', 'SEAVVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '54360eb4-8b44-40b9-ac6f-fa2129c700f0', 'Y');
INSERT INTO pos.d_bank VALUES (1000037, 1000004, 0, 'Ngân hàng Hợp tác xã Việt Nam', NULL, '970446', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '8701e85e-b8be-4b78-94ae-288c871f34de', 'Y');
INSERT INTO pos.d_bank VALUES (1000038, 1000004, 0, 'Ngân hàng TMCP Lộc Phát Việt Nam', NULL, '970449', 'LVBKVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'fd44cac6-e633-4c75-92f8-3b74fc2ed58c', 'Y');
INSERT INTO pos.d_bank VALUES (1000039, 1000004, 0, 'Ngân hàng TMCP Kiên Long', NULL, '970452', 'KLBKVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'a3d618d9-932f-4b21-a3e4-9468ffbe025b', 'Y');
INSERT INTO pos.d_bank VALUES (1000040, 1000004, 0, 'Ngân hàng Đại chúng TNHH Kasikornbank', NULL, '668888', 'KASIVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '065c51e2-171c-4fed-bed5-55f50b4cd355', 'Y');
INSERT INTO pos.d_bank VALUES (1000041, 1000004, 0, 'Ngân hàng Kookmin - Chi nhánh Hà Nội', NULL, '970462', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'afe97236-51f6-4159-9c80-af6677883702', 'Y');
INSERT INTO pos.d_bank VALUES (1000042, 1000004, 0, 'Ngân hàng KEB Hana – Chi nhánh Thành phố Hồ Chí Minh', NULL, '970466', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'be2e3c00-7e36-45eb-b6e8-6b64b8fa5202', 'Y');
INSERT INTO pos.d_bank VALUES (1000043, 1000004, 0, 'Ngân hàng KEB Hana – Chi nhánh Hà Nội', NULL, '970467', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '7731bbcb-6bfe-442b-b9fa-7d8c0e2aa8ae', 'Y');
INSERT INTO pos.d_bank VALUES (1000044, 1000004, 0, 'Công ty Tài chính TNHH MTV Mirae Asset (Việt Nam)', NULL, '977777', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'bf3da4b0-f013-4f4d-bcd2-163f739dc917', 'Y');
INSERT INTO pos.d_bank VALUES (1000045, 1000004, 0, 'Ngân hàng Citibank, N.A. - Chi nhánh Hà Nội', NULL, '533948', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'ed6a3ed0-191d-43de-9082-8c830f6ac245', 'Y');
INSERT INTO pos.d_bank VALUES (1000046, 1000004, 0, 'Ngân hàng Kookmin - Chi nhánh Thành phố Hồ Chí Minh', NULL, '970463', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'a55cc281-1815-48f1-88b1-e3ca236d4b63', 'Y');
INSERT INTO pos.d_bank VALUES (1000047, 1000004, 0, 'Ngân hàng Chính sách Xã hội', NULL, '999888', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '08bc96c6-f889-4167-93c1-a3dbdbf274bd', 'Y');
INSERT INTO pos.d_bank VALUES (1000048, 1000004, 0, 'Ngân hàng TNHH MTV Woori Việt Nam', NULL, '970457', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'f1188262-1de2-4e52-9a51-1a4bc959d3e0', 'Y');
INSERT INTO pos.d_bank VALUES (1000049, 1000004, 0, 'Ngân hàng Liên doanh Việt - Nga', NULL, '970421', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '97f7b0b3-c198-472d-bb30-f16e9add6891', 'Y');
INSERT INTO pos.d_bank VALUES (1000050, 1000004, 0, 'Ngân hàng United Overseas - Chi nhánh TP. Hồ Chí Minh', NULL, '970458', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '95ba567a-44a6-4c35-94e7-e13b65d663c3', 'Y');
INSERT INTO pos.d_bank VALUES (1000051, 1000004, 0, 'Ngân hàng TNHH MTV Standard Chartered Bank Việt Nam', NULL, '970410', 'SCBLVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'b5340ee7-014c-461f-9e2d-7f021a4fc98c', 'Y');
INSERT INTO pos.d_bank VALUES (1000052, 1000004, 0, 'Ngân hàng TNHH MTV Public Việt Nam', NULL, '970439', 'VIDPVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'd9d3a955-fe46-4881-93c8-cb4d841b283a', 'Y');
INSERT INTO pos.d_bank VALUES (1000053, 1000004, 0, 'Ngân hàng Nonghyup - Chi nhánh Hà Nội', NULL, '801011', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '76c6880f-83df-4405-a518-adb0972a9243', 'Y');
INSERT INTO pos.d_bank VALUES (1000054, 1000004, 0, 'Ngân hàng TNHH Indovina', NULL, '970434', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '2e9d8eb5-9755-4a68-b971-6208215d4dfc', 'Y');
INSERT INTO pos.d_bank VALUES (1000055, 1000004, 0, 'Ngân hàng Công nghiệp Hàn Quốc - Chi nhánh TP. Hồ Chí Minh', NULL, '970456', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '6dc5634f-a57b-44e1-b478-123c3c310f96', 'Y');
INSERT INTO pos.d_bank VALUES (1000056, 1000004, 0, 'Ngân hàng Công nghiệp Hàn Quốc - Chi nhánh Hà Nội', NULL, '970455', NULL, NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '1fa6bed0-73cf-4906-96a1-35761a6a52a9', 'Y');
INSERT INTO pos.d_bank VALUES (1000057, 1000004, 0, 'Ngân hàng TNHH MTV HSBC (Việt Nam)', NULL, '458761', 'HSBCVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '076f1b0a-a50b-453b-9711-b76f49bcae9e', 'Y');
INSERT INTO pos.d_bank VALUES (1000058, 1000004, 0, 'Ngân hàng TNHH MTV Hong Leong Việt Nam', NULL, '970442', 'HLBBVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '9021ea11-7c40-4a43-bb66-4ccc4547cac1', 'Y');
INSERT INTO pos.d_bank VALUES (1000059, 1000004, 0, 'Ngân hàng Thương mại TNHH MTV Dầu Khí Toàn Cầu', NULL, '970408', 'GBNKVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '29e7d293-f8fa-4aa3-9664-5176a003865c', 'Y');
INSERT INTO pos.d_bank VALUES (1000060, 1000004, 0, 'Ngân hàng TMCP Đông Á', NULL, '970406', 'EACBVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '4c805eaf-61e8-4e2b-9156-f4335f64da50', 'Y');
INSERT INTO pos.d_bank VALUES (1000061, 1000004, 0, 'DBS Bank Ltd - Chi nhánh Thành phố Hồ Chí Minh', NULL, '796500', 'DBSSVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'b4f50d56-b748-4609-ae34-658bb756e328', 'Y');
INSERT INTO pos.d_bank VALUES (1000062, 1000004, 0, 'Ngân hàng TNHH MTV CIMB Việt Nam', NULL, '422589', 'CIBBVNVN', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, '068eef83-eb49-4cdf-9c3d-ee364a83aa06', 'Y');
INSERT INTO pos.d_bank VALUES (1000063, 1000004, 0, 'Ngân hàng Thương mại TNHH MTV Xây dựng Việt Nam', NULL, '970444', 'GTBAVNVX', NULL, '2024-09-10 04:15:35.581725', 0, '2024-09-10 04:15:35.581725', 0, 'b15a0e4a-6cb5-49b1-ae73-f83d7cc70707', 'Y');
INSERT INTO pos.d_bank VALUES (1000064, 1000004, 0, 'Quỹ tiền mặt', NULL, NULL, NULL, NULL, '2024-09-19 02:46:55.665321', 0, '2024-09-19 02:46:55.665321', 0, '78d3172e-9c2a-424a-8494-56ab76e43068', 'Y');


--
-- TOC entry 5449 (class 0 OID 393261)
-- Dependencies: 355
-- Data for Name: d_bankaccount; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_bankaccount VALUES (1000001, 1000064, 1000004, 1000016, '0121921', '', 'CTY DBIZ', 'Y', 'CHE', '2024-09-09 00:10:06.13349', 0, '2024-09-19 16:07:48.755983', 1000069, '025412cb-4279-43d5-b246-15fb49f96136', 'Y');
INSERT INTO pos.d_bankaccount VALUES (1000006, 1000059, 1000004, 1000016, '090909888', NULL, 'Tuyết Như', 'N', 'CHE', '2024-09-27 09:31:58.405576', 1000066, '2024-09-27 09:31:58.405577', 1000066, '65324d4a-b133-492a-b9cf-494a8684dc73', 'Y');


--
-- TOC entry 5421 (class 0 OID 390960)
-- Dependencies: 327
-- Data for Name: d_cancel_reason; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_cancel_reason VALUES (1000005, 1000004, 1000016, 'Y', 'Tôi muốn đi quán khác', '', '2024-08-13 10:54:41.883003', 1000069, '2024-08-13 10:54:41.883003', 1000069, '220bdc43-79ee-4d84-8377-58175c5e4fca');
INSERT INTO pos.d_cancel_reason VALUES (1000011, 1000004, 1000016, 'Y', 'Món lên chậm', NULL, '2024-08-29 11:42:48.722403', 1000069, '2024-08-29 11:42:48.722404', 1000069, '5b5320e8-21b4-47e6-b3d8-fc1248d3371b');
INSERT INTO pos.d_cancel_reason VALUES (1000012, 1000004, 1000016, 'Y', 'Hết nguyên liệu rồii', NULL, '2024-08-29 11:44:53.775779', 1000069, '2024-08-29 11:44:53.77578', 1000069, '79bf8298-6976-43cc-8794-57e1a7655428');
INSERT INTO pos.d_cancel_reason VALUES (1000013, 1000009, 1000016, 'Y', 'Tôi mệt muốn về nhà', '', '2024-09-16 10:07:38.345766', 1000071, '2024-09-16 10:07:38.345766', 1000071, '4dafe017-6d64-435b-a0a9-31296d95f227');


--
-- TOC entry 5319 (class 0 OID 385481)
-- Dependencies: 224
-- Data for Name: d_changelog; Type: TABLE DATA; Schema: pos; Owner: -
--



--
-- TOC entry 5320 (class 0 OID 385491)
-- Dependencies: 225
-- Data for Name: d_config; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_config VALUES (1000000, 0, 0, 'D_MBB_URL_PRE', 'https://oil.digitalbiz.com.vn/', '', '2024-09-09 01:04:58.939128', 0, 0, '2024-09-09 01:04:58.939128', 'e1780624-66d8-4b61-9a2d-69e9459febee', 'Y');
INSERT INTO pos.d_config VALUES (1000001, 0, 0, 'D_MBB_URL_CREATEQR', 'payment/createqr/v1.0', '', '2024-09-09 01:06:09.497997', 0, 0, '2024-09-09 01:06:09.497997', '16ebd5cc-6917-42a0-be04-9ba12c1d0716', 'Y');
INSERT INTO pos.d_config VALUES (1000002, 0, 0, 'D_MBB_URL_GETTOKEN', 'token/v1', '', '2024-09-09 01:06:20.422088', 0, 0, '2024-09-09 01:06:20.422088', 'ad73f958-cd74-4855-9eac-7ea38356812d', 'Y');
INSERT INTO pos.d_config VALUES (1000003, 0, 0, 'D_MBB_TOKEN_AUTHOR', 'Basic NVAwdEFtdlpVb2I5MWxqaEZWWXA5OTBkbXA0c2thV3g6aDFwNUgyVEphdmNNQVdmRQ==', '', '2024-09-09 01:06:28.132357', 0, 0, '2024-09-09 01:06:28.132357', 'efedc5f9-b6ff-49ad-b6f8-77f68f366abe', 'Y');
INSERT INTO pos.d_config VALUES (1000004, 0, 0, 'MDM_URL_SAVE_IMAGE', 'https://apim.digitalbiz.com.vn:8243/digitalasset/insertupdate/v1', NULL, '2024-09-16 03:05:53.008648', 0, 0, '2024-09-16 03:05:53.008648', '9016d126-4a62-4d24-91bf-a877ca172ed4', 'Y');
INSERT INTO pos.d_config VALUES (1000005, 0, 0, 'D_MBB_KeyCheckSum', 'SsgAccesskey', '', '2024-09-17 16:24:33.451632', 0, 0, '2024-09-17 16:24:33.451632', '9733001a-9bec-4f9f-9129-f9acdff679aa', 'Y');
INSERT INTO pos.d_config VALUES (1000006, 0, 0, 'D_MBB_URL_CHECK_ORDER', 'https://oil.digitalbiz.com.vn/payment/checkorder/v1.0', '', '2024-09-17 18:22:43.417318', 0, 0, '2024-09-17 18:22:43.417318', '6697d81d-a83c-48fe-867b-40de121c3781', 'Y');
INSERT INTO pos.d_config VALUES (1000014, 0, 0, 'D_END_POIN_GETTOKEN', '/api/v1/auth/tokens', NULL, '2024-09-21 03:06:37.229447', 0, 0, '2024-09-21 03:06:37.229447', '5e1917ec-2336-4218-b24f-c5fc4f7dcbd8', 'Y');
INSERT INTO pos.d_config VALUES (1000012, 0, 0, 'D_END_POIN_INT_BPN', '/api/v1/int/getPartner', NULL, '2024-09-21 03:05:14.148358', 0, 0, '2024-09-21 03:05:14.148358', '61824067-d846-4f6a-befb-f6dc6e421117', 'Y');
INSERT INTO pos.d_config VALUES (1000007, 0, 0, 'D_END_POIN_INT_ORG', '/api/v1/int/getOrg', NULL, '2024-09-21 03:05:14.148358', 0, 0, '2024-09-21 03:05:14.148358', '581c048e-d1a2-4f37-97ab-b1553470b6f6', 'Y');
INSERT INTO pos.d_config VALUES (1000008, 0, 0, 'D_END_POIN_INT_POS', '/api/v1/int/getPOSTerminal', NULL, '2024-09-21 03:05:14.148358', 0, 0, '2024-09-21 03:05:14.148358', 'fa4f5cc9-5392-47a2-967b-ed247e5de585', 'Y');
INSERT INTO pos.d_config VALUES (1000013, 0, 0, 'D_END_POIN_INT_USR', '/api/v1/int/getUser', NULL, '2024-09-21 03:05:14.148358', 0, 0, '2024-09-21 03:05:14.148358', '2e7328a7-5c13-4aef-a1bb-3c4e4c1fa0a6', 'Y');
INSERT INTO pos.d_config VALUES (1000011, 0, 0, 'D_END_POIN_INT_TBL', '/api/v1/int/getTable', NULL, '2024-09-21 03:05:14.148358', 0, 0, '2024-09-21 03:05:14.148358', '86b46612-a44e-45af-a152-075c30cbc04e', 'Y');
INSERT INTO pos.d_config VALUES (1000010, 0, 0, 'D_END_POIN_INT_FLO', '/api/v1/int/getFloor', NULL, '2024-09-21 03:05:14.148358', 0, 0, '2024-09-21 03:05:14.148358', '9b070966-7830-42f4-8706-0a3999759e43', 'Y');
INSERT INTO pos.d_config VALUES (1000009, 0, 0, 'D_END_POIN_INT_PC', '/api/v1/int/getProductCategory', NULL, '2024-09-21 03:05:14.148358', 0, 0, '2024-09-21 03:05:14.148358', 'b75c440d-ed21-4a14-abb0-55d18303baa1', 'Y');


--
-- TOC entry 5431 (class 0 OID 392910)
-- Dependencies: 337
-- Data for Name: d_coupon; Type: TABLE DATA; Schema: pos; Owner: -
--



--
-- TOC entry 5321 (class 0 OID 385501)
-- Dependencies: 226
-- Data for Name: d_currency; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_currency VALUES (1000000, 1000004, 1000016, 'VND', 'Dong', 0, '2024-08-17 12:23:06.712676', 0, '2024-08-17 12:23:06.712676', 0, '0c989d62-fb89-4b4d-af35-59753e0dde5c');


--
-- TOC entry 5322 (class 0 OID 385512)
-- Dependencies: 227
-- Data for Name: d_customer; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_customer VALUES (1000098, NULL, 'CUS1000097', 'Nguyễn Chí THanh', '000001', '0', 'Thủ Đức', '456 Elm Street', 1500, '01626816', 'thanhnc@digitalbiz.com.vn', 5000, 'DigitalBiz', '2001-06-03', NULL, '2024-09-24', 1000069, '2024-09-24', 1000069, 'Y', '2a669dc5-c565-40fa-a377-7c19aed93d20', NULL, 'Sài Gòn - Thủ Đức', 'THủ Đức', 1000021, '', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000062, 1000004, 'HIENHN', 'Nguyễn Hiếu Hiền', '123123', '0', 'Thủ Đức', '456 Elm Street', 1500, '01626816', 'hienhn@digitalbiz.com.vn', 5000, 'DigitalBiz', NULL, NULL, '2024-08-15', 1000069, '2024-08-15', NULL, 'Y', 'bb1005de-df56-426a-ba7a-2ac228a550fc', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000085, 1000004, 'CUS1000084', 'Nguyễn Chí THanh', '0384491', '0', 'Thủ Đức', '456 Elm Street', 1500, '01626816', 'thanhnc@digitalbiz.com.vn', 5000, 'DigitalBiz', '2001-06-03', NULL, '2024-08-18', 1000069, '2024-08-18', NULL, 'Y', '8c31524c-9285-4e43-beda-034b1174a828', NULL, 'Sài Gòn - Thủ Đức', 'THủ Đức', 1000021, '', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000032, 1000002, '123', 'anhtu3', '0898449505', NULL, NULL, NULL, NULL, 'ádf', NULL, 0, 'Y', NULL, NULL, '2024-07-31', 1000037, '2024-07-31', NULL, 'Y', '5e1a921e-86b5-4647-a2a3-6e126fccbaad', 'Y', NULL, NULL, NULL, 'M', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000087, 1000004, 'CUS1000086', 'Nguyễn Lệ Nam', '0327666766', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-09-06', 1000069, '2024-09-06', NULL, 'Y', 'ebd5d4d1-c6db-4ce8-8132-3e9f28b55c9b', NULL, NULL, NULL, 1000042, 'F', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000010, 1000002, 'ABC123', 'John Doe213', '1', '987654321', '123 Main Street', '456 Elm Street', 1500, '123456789', 'johndoe@example.com', 1000.50, 'Y', '1985-01-01', NULL, '2024-07-20', 1000037, '2024-07-20', NULL, 'Y', 'c44b21d0-560a-4bad-91d0-62fd8a89cbe4', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000055, 1000004, '', 'Tuyết Như', '0326647677', NULL, NULL, NULL, NULL, '', NULL, 0, NULL, NULL, NULL, '2024-09-06', 1000069, '2024-09-06', 1000069, 'Y', 'c6c09c74-1e20-4acb-a7cc-253949889e59', 'N', NULL, NULL, 1000026, 'F', '', NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000000, 1000002, 'NCT', 'Than H', '431', NULL, NULL, NULL, NULL, '123', 'chithanh03062001@gmail.com', 0, 'Y', NULL, NULL, '2024-07-11', 0, '2024-07-11', 0, 'Y', '5ea93194-4bb2-4aaf-9667-38d55a4abb02', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000002, 1000002, 'ABC123', 'John Doe12', '0', '987654321', '123 Main Street', '456 Elm Street', 1500, '123456789', 'johndoe@example.com', 1000.50, 'Y', '1985-01-01', NULL, '2024-07-12', 1000037, NULL, NULL, 'Y', 'ee70899a-587b-43c4-8e52-3cab0ce4ba12', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000030, 1000002, NULL, 'John Doe421', '121243222', '987654321', '123 Main Street', '456 Elm Street', 1500, '123456789', 'johndoe@example.com', 1000.50, 'Y', '1985-01-01', NULL, '2024-07-31', 1000037, '2024-07-31', NULL, 'Y', 'b4943828-cf4e-429b-baa3-9d3477d00dc0', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000029, 1000002, 'ABC123', 'John Doe123', '2', '987654321', '123 Main Street', '456 Elm Street', 1500, '123456789', 'johndoe@example.com', 1000.50, 'Y', '1985-01-01', NULL, '2024-07-31', 1000037, '2024-07-31', NULL, 'Y', '3e190cd4-3a68-470f-bc89-17d94879ff4b', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000033, 1000002, '1234', 'anhtu2', '089844911', NULL, NULL, NULL, NULL, '123345', NULL, 0, 'Y', NULL, NULL, '2024-07-31', 1000037, '2024-07-31', NULL, 'Y', '350e6f09-41fb-4d56-b546-e55f70f12fc8', 'Y', NULL, NULL, NULL, 'M', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000034, 1000002, 'tu3', 'tu3', '0898449112', NULL, NULL, NULL, NULL, '123456', NULL, 0, 'Y', NULL, NULL, '2024-07-31', 1000037, '2024-07-31', NULL, 'Y', '865f9d4f-5023-4f64-8985-e214bb24f5ef', 'Y', NULL, NULL, NULL, 'M', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000079, 1000004, 'TUHA', 'Hà Anh ', '0237272723', NULL, 'Tân Bình kkk', NULL, NULL, '01626816', 'tennld@digitalbiz.com.vn', 0, NULL, '2001-06-06', NULL, '2024-09-06', 1000069, '2024-09-06', 1000069, 'Y', '254e3e06-4d79-4922-85e0-1d88a91b59b3', NULL, 'Sài Gòn - Tân Bình', 'Dĩ An', 1000022, 'M', 'wef', NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000095, 1000009, '1000004', 'Nguyễn Văn A', '0971902294', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-09-12', 1000071, '2024-09-12', 1000071, 'Y', 'f9fceb5f-c452-4ba1-8ce6-0edcc1e690bc', NULL, NULL, NULL, 1000060, 'M', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000096, 1000009, '1000005', 'Nguyễn Thị Hồng', '0971902291', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-09-12', 1000071, '2024-09-12', 1000071, 'Y', 'b80643f9-0113-4f1e-bef8-ff4e0645b627', NULL, NULL, NULL, 1000060, 'M', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000120, NULL, 'CUS1000119', 'Nguyễn Trần Duy Hai', '00000', '0', 'Thủ Đức', '456 Elm Street', 1500, '01626816', 'thanhnc@digitalbiz.com.vn', 5000, 'DigitalBiz', '2001-06-03', NULL, '2024-09-24', 0, '2024-09-24', 0, 'Y', '0c5d0f31-1993-4d6d-88f0-d8306a90d31d', NULL, 'Sài Gòn - Thủ Đức', 'THủ Đức', 1000021, '', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000081, 1000004, 'TUHA', 'Hà Anh', '0123456789', NULL, 'Tân Bình kkk', NULL, NULL, '01626816', 'tennld@digitalbiz.com.vn', 0, NULL, '2001-06-03', NULL, '2024-09-06', 1000069, '2024-09-06', 1000069, 'Y', '34b4ac87-384c-4e7e-bd42-8751178b1657', NULL, 'Sài Gòn - Tân Bình', 'Dĩ An', 1000022, 'M', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000035, 1000002, 'tu4', 'tu4', '08984442', NULL, NULL, NULL, NULL, '123456', NULL, 0, 'Y', NULL, NULL, '2024-07-31', 1000037, '2024-07-31', NULL, 'Y', '619beb3c-83b6-4a60-badf-8df22e2e9163', 'Y', NULL, NULL, NULL, 'M', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000003, 1000002, 'ABC123', 'John Doe', '123214', '987654321', '123 Main Street', '456 Elm Street', 1500, '123456789', 'johndoe@example.com', 1000.50, 'Y', '1985-01-01', NULL, '2024-07-12', 1000037, NULL, NULL, 'Y', '9c4068f2-18f8-43e6-a1f2-907ea322c407', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000082, 1000004, 'TUHA', 'Hà Anh Tú 5', '222', NULL, 'Tân Bình kkk', NULL, NULL, '01626816', 'tennld@digitalbiz.com.vn', 0, NULL, '2001-06-03', NULL, '2024-08-15', 1000069, '2024-08-15', NULL, 'Y', '488bfc77-d0d6-41e8-93a2-82b43f80d222', NULL, 'Sài Gòn - Tân Bình', 'Dĩ An', 1000022, 'M', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000713, 1000004, 'CUS1000712', 'zxcv', '1234567890', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-09-26', 1000066, '2024-09-26', 1000066, 'Y', 'c1f5e33c-9330-4737-a799-de7401c0a1c1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000693, 1000004, 'CUS1000692', 'tu', '0898449505', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-09-26', 1000069, '2024-09-26', 1000069, 'Y', '19a013a1-6f34-4de8-86f1-55c89e6ca600', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000009, 1000002, 'ABC123', 'John Doe111', '3', '987654321', '123 Main Street', '456 Elm Street', 1500, '123456789', 'johndoe@example.com', 1000.50, 'Y', '1985-01-01', NULL, '2024-07-17', 1000037, '2024-07-17', NULL, 'Y', 'e720f792-e960-4453-87a3-1e634435a820', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000031, 1000002, NULL, 'anhtu', '0898441', NULL, NULL, NULL, NULL, '123', NULL, 0, 'Y', NULL, NULL, '2024-07-31', 1000037, '2024-07-31', NULL, 'Y', 'f9441e94-07a7-40d3-87a3-367138aec282', 'Y', NULL, NULL, NULL, 'M', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000036, 1000002, '123', '123', '123', NULL, NULL, NULL, NULL, NULL, NULL, 0, 'Y', NULL, NULL, '2024-08-02', 1000037, '2024-08-02', NULL, 'Y', 'b6001ba8-88b1-44c3-a608-2ebd7833d1a7', 'Y', NULL, NULL, 1000019, 'M', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000094, 1000009, '1000003', 'Lầu Sẹc Dần', '0971902299', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-09-11', 1000071, '2024-09-11', 1000071, 'Y', '0b57763d-6300-4f9a-9e12-c8f19f582f1a', NULL, NULL, NULL, 1000060, NULL, NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000090, 1000009, '10000001', 'DBIZ', '0971902297', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-09-11', 1000071, '2024-09-11', 1000071, 'Y', '49f87e90-9a82-4045-b69c-8634ef0ca74b', NULL, NULL, NULL, 1000063, 'M', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000093, 1000009, '1000002', 'SSG', '0971902298', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-09-11', 1000071, '2024-09-11', 1000071, 'Y', 'd3ca8f13-3781-4dea-a7c1-7c624fe1bc11', NULL, NULL, NULL, 1000063, 'M', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000044, 1000004, 'DANLS', 'Lầu Sẹc Dần ', '45123', '0', 'Thủ Đức', '456 Elm Street', 1500, '01626816', 'danls@digitalbiz.com.vn', 5000, 'DigitalBiz', '2001-06-03', NULL, '2024-08-07', 1000069, '2024-08-15', 1000069, 'Y', 'c73898e9-8e63-4dfa-94d3-a3d1cb86bf23', 'Y', 'Sài Gòn - Thủ Đức', 'THủ Đức', 1000021, 'F', '', NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000042, 1000004, 'HUNGNMM', 'Nguyễn Minh Hưngg', '3212', '0', 'Linh Xuân Thủ Đức', '456 Elm Street', 1500, '016268167', 'hunglm@digitalbiz.com.vnN', 5000, 'DigitalBiz', '2001-06-03', NULL, '2024-08-07', 1000069, '2024-08-15', 1000069, 'Y', '131e1284-99ec-4579-b91b-3e133ac743f6', 'N', 'Sài Gòn - Thủ Đức', 'Linh Xuân', 1000022, 'M', '', NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000039, 1000004, 'TIENNLD', 'Nguyễn Lê Duy Tiến', '1212', '0', 'Dĩ AN BÌnh DƯơng', '456 Elm Street', 1500, '01626816', 'tennld@digitalbiz.com.vn', 5000, 'DigitalBiz', '2001-06-03', NULL, '2024-08-07', 1000069, '2024-08-07', 1000069, 'Y', 'e075fe11-bc06-4e54-8f70-8e02ac2bbcf4', 'N', 'Sài Gòn - Thủ Đức', 'Dĩ An', 1000021, 'M', '', NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000715, 1000004, 'CUS1000714', 'test', '1234567890', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-09-26', 1000066, '2024-09-26', 1000066, 'Y', 'f5ec3a34-5db0-4d58-8bb4-2995917c01c0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000007, 1000002, 'ABC123', 'ưerwerew', '123456789', '987654321', '123 Main Street', '456 Elm Street', 1500, '123456789', 'johndoe@example.com', 1000.50, 'Y', '1985-01-01', NULL, '2024-07-12', 1000037, '2024-07-12', NULL, 'Y', '8adbca53-48c1-4994-bc0a-1b2d6f82eced', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000040, 1000004, 'TUHA', 'Hà Anh Tú 1', '12312', '0', 'Tân Bình kkk', '456 Elm Street', 1500, '01626816', 'tennld@digitalbiz.com.vn', 5000, 'DigitalBiz', '2001-06-03', NULL, '2024-08-07', 1000069, '2024-08-15', 1000069, 'Y', '3725e564-1c3c-4d7a-96fc-ee59615a784c', 'N', 'Sài Gòn - Tân Bình', 'Dĩ An', 1000022, 'M', '', NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000037, 1000004, 'THANHNC', 'Nguyễn Chí Thanh 1', '0384449114', '0', 'Số 10 đường số 1 khu phố 5', '45', 1500, '01626816', 'thanhnc@digitalbiz.com.vn', 5000, 'DigitalBiz', '2001-06-03', NULL, '2024-08-07', 1000069, '2024-08-15', 1000069, 'Y', '388544c9-f3bd-4760-9490-a0376c2a527e', 'Y', 'Sài Gòn - Thủ Đức', 'Hiệp Bình chánh', 1000021, 'M', '', NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000061, 1000004, 'HIENHN', 'Nguyễn Hiếu Hiền', '5454', '0', 'Thủ Đức', '456 Elm Street', 1500, '01626816', 'hienhn@digitalbiz.com.vn', 5000, 'DigitalBiz', '2001-06-03', NULL, '2024-08-15', 1000069, '2024-08-15', NULL, 'Y', 'a7da6805-e5e1-472f-89da-8d001b47188b', NULL, NULL, NULL, 1000021, 'M', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000665, 0, 'CUS1000664', 'Nguyễn Trần Duy Tư', '342112232', '0', 'Thủ Đức', '456 Elm Street', 1500, '01626816', 'thanhnc@digitalbiz.com.vn', 5000, 'DigitalBiz', '2001-06-03', NULL, '2024-09-25', 0, '2024-09-25', 0, 'Y', '8c2e9b9a-2a62-4253-9882-6b6b524df863', NULL, 'Sài Gòn - Thủ Đức', 'THủ Đức', 1000021, '', NULL, NULL, NULL, 'N', NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000666, 1000004, 'KH3', 'Nguyễn Hồng Lam', '009911321988', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-09-25', 1000069, '2024-09-25', 1000069, 'Y', '43ee54d4-143b-464e-b45f-c71508436ef3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000667, 1000004, 'KH3', 'Nguyễn Hồng Lam', '0099999988', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-09-25', 1000069, '2024-09-25', 1000069, 'Y', 'be91659a-ee6d-4043-bcf7-fe98355b08a3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_customer VALUES (1000663, 0, 'CUS1000662', 'Nguyễn Trần Duy Tư', '3421122', '0', 'Thủ Đức', '456 Elm Street', 1500, '01626816', 'thanhnc@digitalbiz.com.vn', 5000, 'DigitalBiz', '2001-06-03', NULL, '2024-09-25', 0, '2024-09-25', 0, 'Y', '8946b643-58e4-4012-9cc9-1530301911a0', NULL, 'Sài Gòn - Thủ Đức', 'THủ Đức', 1000021, '', NULL, NULL, NULL, 'N', NULL, NULL);


--
-- TOC entry 5323 (class 0 OID 385528)
-- Dependencies: 228
-- Data for Name: d_doctype; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_doctype VALUES (1000006, 1000004, 'REQUEST_ORDER', 'Request Order', '2024-09-17 04:27:07.924916', 0, '2024-09-17 04:27:07.924916', 0, '04e77c82-b93f-42aa-ad3d-b7c743e2a872', 0, 'Y');
INSERT INTO pos.d_doctype VALUES (1000003, 1000004, 'AR_INVOICE', 'AR Invoice', '2024-09-09 11:09:56.291993', 0, '2024-09-09 11:09:56.291993', 0, '8dd54249-347e-4e42-b301-ffd79cd1682d', 0, 'Y');
INSERT INTO pos.d_doctype VALUES (1000001, 1000004, 'PRODUCTION', 'Production', '2024-09-08 17:34:03.447374', 0, '2024-09-08 17:34:03.447374', 0, '98c84f11-8411-4d41-8672-e308e1a66527', 0, 'Y');
INSERT INTO pos.d_doctype VALUES (1000005, 1000004, 'PO_ORDER', 'Purchase Order', '2024-09-11 09:52:54.781867', 0, '2024-09-11 09:52:54.781867', 0, '3b56b489-7edc-4735-8b3d-6402d494c08e', 0, 'Y');
INSERT INTO pos.d_doctype VALUES (1000002, 1000004, 'POS_ORDER', 'POS ORDER', '2024-09-09 07:43:43.835164', 0, '2024-09-09 07:43:43.835164', 0, 'd49b4f9a-7932-48c7-bf65-4703da4622d9', 0, 'Y');
INSERT INTO pos.d_doctype VALUES (1000004, 1000004, 'AR_PAYMENT', 'AR Payment', '2024-09-09 11:09:56.291993', 0, '2024-09-09 11:09:56.291993', 0, 'ae24a20b-5d0b-4856-8c3f-82fc9e996a71', 0, 'Y');
INSERT INTO pos.d_doctype VALUES (1000000, 1000004, 'KITCHEN_ORDER', 'Kitchen Order', '2024-08-17 11:45:03.580072', 0, '2024-08-17 11:45:03.580072', 0, '2f801d53-e82a-4902-9843-c2e4f06a360c', 0, 'Y');
INSERT INTO pos.d_doctype VALUES (1000007, 1000004, 'POS_CLOSED_CASH', 'Pos Close Cash', '2024-09-20 01:10:06.200184', 0, '2024-09-20 01:10:06.200184', 0, '57f87ff8-0481-4fba-89bc-653a5b5fb231', 0, 'Y');


--
-- TOC entry 5451 (class 0 OID 393356)
-- Dependencies: 357
-- Data for Name: d_erp_integration; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_erp_integration VALUES (1000010, 1000004, 0, 'Idempiere', 'https://dbizmobile.ssg.vn:8443', 1000066, 0, 1000707, 0, 'WebService', 'WebService', '', 'Y', 'C', '2024-09-18 13:55:09.126914', 1000069, '2024-09-20 11:55:35.631971', 1000069, '1df48648-b5b0-4ec2-82a7-a620f68ece47', 'Y');


--
-- TOC entry 5324 (class 0 OID 385535)
-- Dependencies: 229
-- Data for Name: d_expense; Type: TABLE DATA; Schema: pos; Owner: -
--



--
-- TOC entry 5325 (class 0 OID 385546)
-- Dependencies: 230
-- Data for Name: d_expense_category; Type: TABLE DATA; Schema: pos; Owner: -
--



--
-- TOC entry 5326 (class 0 OID 385554)
-- Dependencies: 231
-- Data for Name: d_floor; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_floor VALUES (999957, 1000005, 1000002, '2024-07-17 10:42:38.676678', 1000037, '2024-07-17 10:42:38.676678', 1000037, 'Y', 'F019', 'F 1 2', 'no', 'a7904ae2-5f05-4eb9-9312-88bb39f29527', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999966, 1000003, 1000002, '2024-07-23 11:07:11.752786', 1000037, '2024-07-23 11:07:11.752787', 1000037, 'Y', 'F03', 'F 3', 'no', 'ba76207d-e72e-48ed-9118-4052cfbd0fcf', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999973, 1000016, 1000004, '2024-08-07 09:20:10.406461', 1000069, '2024-09-26 11:18:56.373691', 1000066, 'Y', 'F021', 'Khu vực 02', ' ', '4ce0a546-2f7d-4902-b0ef-043c7ea42ce7', 1, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999995, 1000019, 1000004, '2024-09-19 09:20:28.928579', 1000069, '2024-09-19 09:47:31.870353', 1000069, 'N', '12312', 'Khu vực 10111ab', 'dsfsdaf', 'd91a05bf-b45e-4d89-a26d-036978e6dd5f', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999988, 1000019, 1000004, '2024-09-19 08:17:54.946735', 1000069, '2024-09-19 08:17:54.946735', 1000069, 'Y', '92', 'Khu vực 1', '123', '9a55d316-043d-4614-a252-09618354581a', 0, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999977, 1000016, 1000004, '2024-08-14 15:46:09.135348', 1000069, '2024-09-26 21:35:34.368824', 1000066, 'Y', 'F06', 'Khu vực 053', ' Tang cao', '1e750f76-a88f-4011-ba85-0d91928aa57f', 0, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999976, 1000016, 1000004, '2024-08-07 09:22:07.855007', 1000069, '2024-09-27 00:37:24.281512', 1000066, 'Y', 'F05', 'Khu vực 051', ' Tầng to lớn', '8d57d092-7e9f-49e9-8a6f-a1ee74042e84', 0, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999975, 1000016, 1000004, '2024-08-07 09:21:28.028374', 1000069, '2024-09-27 11:49:08.887466', 1000066, 'Y', 'F04', 'Khu vực 04', ' Tầng lớn  nnn', '5662290d-2eab-45f0-abb0-199f0c739fb9', 0, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999971, 1000003, 1000002, '2024-07-24 15:10:26.004022', 1000037, '2024-07-24 15:10:26.004023', 1000037, 'Y', 'F05', 'F5 1', 'Tần trống', '5cf07d01-7059-478e-8916-bc02ff8cebda', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999972, 1000016, 1000004, '2024-08-07 09:17:50.223201', 1000069, '2024-09-27 16:13:45.199156', 1000066, 'Y', 'F011', 'Khu vực 01', ' ', '4cb9d16a-d006-4437-bf75-7e8548a33ff6', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999970, 1000003, 1000002, '2024-07-24 15:02:39.784176', 1000037, '2024-07-24 15:02:39.784178', 1000037, 'Y', 'F04', 'F4 1', 'Tang trong', 'de53ca0d-95a4-4179-842b-3e299c7e1b8e', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999953, 1000005, 1000002, '2024-07-17 10:11:55.903075', 1000037, '2024-07-17 10:11:55.903075', 1000037, 'Y', '01', 'Tầng 1 2', 'no', '35bcb9e8-3f53-4952-993d-3eaeb15a8165', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999968, 1000003, 1000003, '2024-07-23 23:48:25.294976', 1000068, '2024-07-23 23:48:25.294976', 1000068, 'Y', 'F02', 'F 1 2 3', 'no', '539a0c74-eb66-4bef-9d11-dce7098a81b7', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999956, 1000005, 1000002, '2024-07-17 10:41:55.208227', 1000037, '2024-07-17 10:41:55.208227', 1000037, 'Y', 'F01', 'Tầng 1 2 3', 'no', 'b1d03718-fd86-4517-8573-17c0272c924b', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999978, 1000030, 1000004, '2024-09-18 17:57:25.857493', 1000069, '2024-09-18 17:57:25.857493', 1000069, 'Y', 'F01', 'Khu vực 01', ' ', 'bd7e46da-dc66-4528-a5c2-2333c09a2236', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999965, 1000005, 1000002, '2024-07-23 11:06:52.055181', 1000037, '2024-07-23 11:06:52.055202', 1000037, 'Y', 'F011', 'F 1 6', 'no', '1b42868e-af7a-407f-a1cc-d284c66dcfac', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999963, 1000003, 1000002, '2024-07-22 04:51:52.682438', 1000037, '2024-07-22 04:51:52.682438', 1000037, 'Y', 'F0122', 'F 1 4', 'no', '3f57764f-c85c-4a5a-8f12-c68b170495bb', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999962, 1000005, 1000002, '2024-07-21 23:19:51.491064', 1000037, '2024-07-21 23:19:51.491064', 1000037, 'Y', 'F015', 'F 1 3', 'no', 'c05b0a5f-92fe-4b08-8ec9-a9810ad5b44b', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999982, 1000030, 1000004, '2024-09-18 17:57:44.319429', 1000069, '2024-09-18 17:57:44.319429', 1000069, 'Y', 'F026', 'Khu vực 02', ' ', '748a8234-8138-4330-88f4-23f4d34d171f', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999969, 1000003, 1000002, '2024-07-24 15:02:00.915362', 1000037, '2024-07-24 15:02:00.915364', 1000037, 'Y', 'F025', 'F2 1', 'tầng trống', 'f78fe176-64ac-40ea-ad7f-9841e9a2a7e7', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999964, 1000003, 1000002, '2024-07-22 05:06:26.319524', 1000037, '2024-07-22 05:06:26.319524', 1000037, 'Y', 'F023', 'F 1 5', 'no', '1413050e-7fb7-44d1-8ca7-a75835c5dedf', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999967, 1000003, 1000003, '2024-07-23 23:48:20.633139', 1000068, '2024-07-23 23:48:20.633139', 1000068, 'Y', 'F022', 'F 1 2', 'no', 'b63bf998-8acb-44b7-bcda-c7ecd338da8e', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999955, 1000005, 1000002, '2024-07-17 10:15:27.923026', 1000037, '2024-07-17 10:15:57.688979', 1000037, 'Y', '016', 'Tầng 1', 'no 1', '9d518cfd-d56f-4564-a031-88fad91717c7', NULL, NULL, NULL);
INSERT INTO pos.d_floor VALUES (999974, 1000016, 1000004, '2024-08-07 09:20:51.79403', 1000069, '2024-09-25 18:17:49.846613', 1000069, 'Y', 'F03', 'Khu vực 03', 'Tầng dành cho vip', 'de0712ff-75e2-473b-aceb-fead5bacd860', 1, NULL, NULL);
INSERT INTO pos.d_floor VALUES (1000012, 1000016, 1000004, '2024-08-14 15:53:11.671368', 1000069, '2024-09-27 11:50:41.074719', 1000066, 'Y', 'F061', 'Khu vực 052', ' to', '4ba48aac-6766-4fc2-bdd3-00e16cb6c72e', 0, NULL, NULL);
INSERT INTO pos.d_floor VALUES (1000013, 1000033, 1000004, '2024-09-22 15:08:40.850915', 1000069, '2024-09-22 15:08:40.850915', 1000069, 'Y', '1', 'Tầng 1', NULL, '52cdde64-0917-47f9-b1d4-d21079ee91ac', NULL, 0, 1000002);
INSERT INTO pos.d_floor VALUES (1000014, 1000033, 1000004, '2024-09-22 15:08:40.900335', 1000069, '2024-09-22 15:08:40.900335', 1000069, 'Y', '2', 'Tầng 2', NULL, 'e6aa8920-5d91-484d-84ef-747a6776ff02', NULL, 0, 1000003);
INSERT INTO pos.d_floor VALUES (1000016, 1000033, 1000004, '2024-09-22 15:08:40.989141', 1000069, '2024-09-22 15:08:40.989141', 1000069, 'Y', '4', 'Tầng 4', NULL, 'e2dd2658-bc39-4686-92d7-2cdba945ce2c', NULL, 0, 1000005);
INSERT INTO pos.d_floor VALUES (1000017, 1000038, 1000004, '2024-09-22 15:08:41.036876', 1000069, '2024-09-22 15:08:41.036876', 1000069, 'Y', '1E', 'Tầng 1E', NULL, '40a22235-02e8-41bb-983a-741296086f6a', NULL, 1000029, 1000011);
INSERT INTO pos.d_floor VALUES (1000018, 1000038, 1000004, '2024-09-22 15:08:41.080599', 1000069, '2024-09-22 15:08:41.080599', 1000069, 'Y', '1B', 'Tầng 1B', NULL, 'dd0038fc-c1d7-426c-adf1-036e2d5921f5', NULL, 1000003, 1000007);
INSERT INTO pos.d_floor VALUES (1000019, 1000038, 1000004, '2024-09-22 15:08:41.123284', 1000069, '2024-09-22 15:08:41.123284', 1000069, 'Y', 'LBT', 'Line Buffet', NULL, 'f94caa4a-f7a3-4c8a-9b50-856e28e55969', NULL, 1000001, 1000013);
INSERT INTO pos.d_floor VALUES (1000020, 1000038, 1000004, '2024-09-22 15:08:41.165421', 1000069, '2024-09-22 15:08:41.165421', 1000069, 'Y', '1ADB', 'DB-Khu vực 1A ', NULL, '3d4fbfab-4b1b-43ee-9766-447f9b237c19', NULL, 1000027, 1000026);
INSERT INTO pos.d_floor VALUES (1000021, 1000038, 1000004, '2024-09-22 15:08:41.205385', 1000069, '2024-09-22 15:08:41.205385', 1000069, 'Y', 'RESKV1B', 'Khu vực 1B', NULL, 'ec151964-cf85-4212-b18d-f332b0bf28f0', NULL, 1000025, 1000015);
INSERT INTO pos.d_floor VALUES (1000022, 1000038, 1000004, '2024-09-22 15:08:41.273457', 1000069, '2024-09-22 15:08:41.273457', 1000069, 'Y', 'RESKV1C', 'Khu vực 1C', NULL, 'e0137f75-171d-439d-b972-a2e9f2b8f239', NULL, 1000025, 1000016);
INSERT INTO pos.d_floor VALUES (1000023, 1000038, 1000004, '2024-09-22 15:08:41.348185', 1000069, '2024-09-22 15:08:41.348185', 1000069, 'Y', 'RESKV2', 'Khu vực 2', NULL, 'ad03f195-5469-4478-9bf7-08dc4165eca2', NULL, 1000025, 1000017);
INSERT INTO pos.d_floor VALUES (1000024, 1000038, 1000004, '2024-09-22 15:08:41.403629', 1000069, '2024-09-22 15:08:41.403629', 1000069, 'Y', 'HOLFL1', 'Hol Tầng 1', NULL, 'd6e976fa-4398-4f32-87c0-e2826552d531', NULL, 1000026, 1000019);
INSERT INTO pos.d_floor VALUES (1000025, 1000038, 1000004, '2024-09-22 15:08:41.485511', 1000069, '2024-09-22 15:08:41.485511', 1000069, 'Y', 'HOLFL2', 'Hol Tầng 2', NULL, '3fbecce7-810e-4c51-b9eb-f0a85e385846', NULL, 1000026, 1000020);
INSERT INTO pos.d_floor VALUES (1000026, 1000038, 1000004, '2024-09-22 15:08:41.543361', 1000069, '2024-09-22 15:08:41.543361', 1000069, 'Y', 'HOLFL3', 'Hol Tầng 3', NULL, '47fff10c-e9d7-4bf9-a909-eb5bfdfed561', NULL, 1000026, 1000021);
INSERT INTO pos.d_floor VALUES (1000027, 1000038, 1000004, '2024-09-22 15:08:41.598607', 1000069, '2024-09-22 15:08:41.598607', 1000069, 'Y', 'HOLFL4', 'Hol Tầng 4', NULL, 'a9f10c64-db50-45b8-a4c5-3c66190ca514', NULL, 1000026, 1000022);
INSERT INTO pos.d_floor VALUES (1000028, 1000038, 1000004, '2024-09-22 15:08:41.682379', 1000069, '2024-09-22 15:08:41.682379', 1000069, 'Y', 'HOLFL5', 'Hol Tầng 5', NULL, 'f645662b-42cd-4f10-91c0-aa3bc9749a78', NULL, 1000026, 1000023);
INSERT INTO pos.d_floor VALUES (1000029, 1000038, 1000004, '2024-09-22 15:08:41.72761', 1000069, '2024-09-22 15:08:41.72761', 1000069, 'Y', 'HOLFL6', 'Hol Tầng 6', NULL, '0511a725-d271-4b0f-b89b-335b37e20c0c', NULL, 1000026, 1000024);
INSERT INTO pos.d_floor VALUES (1000030, 1000038, 1000004, '2024-09-22 15:08:41.770756', 1000069, '2024-09-22 15:08:41.770756', 1000069, 'Y', '1C', 'Tầng 1C', NULL, '4b9d1612-4668-4a64-921e-37c1256c4b83', NULL, 1000029, 1000008);
INSERT INTO pos.d_floor VALUES (1000031, 1000038, 1000004, '2024-09-22 15:08:41.816021', 1000069, '2024-09-22 15:08:41.816021', 1000069, 'Y', '1D', 'Tầng 1D', NULL, 'ab774556-645c-448a-9a90-927e9c5530cf', NULL, 1000029, 1000009);
INSERT INTO pos.d_floor VALUES (1000032, 1000038, 1000004, '2024-09-22 15:08:41.860125', 1000069, '2024-09-22 15:08:41.860125', 1000069, 'Y', '1F', 'Khu vực 1F', NULL, '88e6da0b-a95d-45ff-9f36-a788257a8fdb', NULL, 1000029, 1000012);
INSERT INTO pos.d_floor VALUES (1000033, 1000038, 1000004, '2024-09-22 15:08:41.896742', 1000069, '2024-09-22 15:08:41.896742', 1000069, 'Y', 'RESKV3', 'Khu liền kề K01', NULL, 'af8bf3f3-519b-48ab-8fe1-3f96303dee03', NULL, 1000026, 1000018);
INSERT INTO pos.d_floor VALUES (1000034, 1000038, 1000004, '2024-09-22 15:08:41.938095', 1000069, '2024-09-22 15:08:41.938095', 1000069, 'Y', 'tang1F', 'Tầng 1F', NULL, '48db2e06-c73e-455b-a7e1-3de71ca51e05', NULL, 1000003, 1000031);
INSERT INTO pos.d_floor VALUES (1000035, 1000038, 1000004, '2024-09-22 15:08:41.982288', 1000069, '2024-09-22 15:08:41.982288', 1000069, 'Y', 'RESKV1A', 'Khu vực 1A', NULL, 'd014b43e-a846-46de-8bfa-05d586ae1b74', NULL, 1000025, 1000014);
INSERT INTO pos.d_floor VALUES (1000036, 1000033, 1000004, '2024-09-22 15:08:42.031566', 1000069, '2024-09-22 15:08:42.031566', 1000069, 'Y', '0', 'Tầng trệt', NULL, '0bdbe748-ea84-4585-917d-c0b1758efe58', NULL, 1000003, 1000000);
INSERT INTO pos.d_floor VALUES (1000037, 1000038, 1000004, '2024-09-22 15:08:42.084368', 1000069, '2024-09-22 15:08:42.084368', 1000069, 'Y', '1A', 'Tầng 1A', NULL, '5c9aa4ec-ebdc-47a5-9106-94cd36df754f', NULL, 1000001, 1000006);


--
-- TOC entry 5327 (class 0 OID 385568)
-- Dependencies: 232
-- Data for Name: d_image; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_image VALUES (1000421, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000076.png', 'Y', '2024-09-27 10:16:06.529044', 1000066, '2024-09-27 10:16:06.529044', 1000066, 1000004, 'e92eb412-b8b7-49d4-8185-caf2b393bea8', '000000076');
INSERT INTO pos.d_image VALUES (1000424, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000079.png', 'Y', '2024-09-27 11:36:47.896382', 1000066, '2024-09-27 11:36:47.896383', 1000066, 1000004, '40a43936-6bdf-4e70-9513-5e2fd38eb15d', '000000079');
INSERT INTO pos.d_image VALUES (1000425, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000080.png', 'Y', '2024-09-27 11:36:48.397294', 1000066, '2024-09-27 11:36:48.397295', 1000066, 1000004, 'afb70230-6786-44f3-b6e8-8a22a95c4fd4', '000000080');
INSERT INTO pos.d_image VALUES (1000426, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000081.png', 'Y', '2024-09-27 11:36:48.919155', 1000066, '2024-09-27 11:36:48.919156', 1000066, 1000004, '039ce003-7682-440f-88de-85451616dcf7', '000000081');
INSERT INTO pos.d_image VALUES (1000427, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000082.png', 'Y', '2024-09-27 11:36:49.432013', 1000066, '2024-09-27 11:36:49.432014', 1000066, 1000004, '72f832e6-9e38-4360-900b-dc34cdd45596', '000000082');
INSERT INTO pos.d_image VALUES (1000431, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000086.png', 'Y', '2024-09-27 12:12:05.756873', 1000066, '2024-09-27 12:12:05.756874', 1000066, 1000004, '65340a18-d8f7-4b2a-b36e-268f15e6d505', '000000086');
INSERT INTO pos.d_image VALUES (1000432, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000087.png', 'Y', '2024-09-27 12:12:06.236132', 1000066, '2024-09-27 12:12:06.236133', 1000066, 1000004, 'e5620605-4e25-4d8e-9a91-9c15c849636b', '000000087');
INSERT INTO pos.d_image VALUES (1000433, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000088.png', 'Y', '2024-09-27 12:12:06.689447', 1000066, '2024-09-27 12:12:06.689448', 1000066, 1000004, '04384a0d-e268-4092-a660-d45362d29a38', '000000088');
INSERT INTO pos.d_image VALUES (1000434, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000089.png', 'Y', '2024-09-27 12:12:07.135964', 1000066, '2024-09-27 12:12:07.135964', 1000066, 1000004, 'dde69656-7e95-4709-8b6e-e8f1118ec8ae', '000000089');
INSERT INTO pos.d_image VALUES (1000435, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000090.png', 'Y', '2024-09-27 13:11:42.198373', 1000066, '2024-09-27 13:11:42.198373', 1000066, 1000004, 'f394d44b-7431-4316-aac1-9e3128f03599', '000000090');
INSERT INTO pos.d_image VALUES (1000436, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000091.png', 'Y', '2024-09-27 13:11:43.601671', 1000066, '2024-09-27 13:11:43.601671', 1000066, 1000004, 'f82fa619-cfd0-478f-bbb6-3098ced09636', '000000091');
INSERT INTO pos.d_image VALUES (1000437, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000092.png', 'Y', '2024-09-27 13:11:47.522351', 1000066, '2024-09-27 13:11:47.522352', 1000066, 1000004, 'b8039395-11ab-44f3-9337-7cc70748dfc9', '000000092');
INSERT INTO pos.d_image VALUES (1000352, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000021.png', 'Y', '2024-09-18 16:46:22.174526', 1000069, '2024-09-18 16:46:22.174526', 1000069, 1000004, '26780a7d-a456-4db9-8ce6-319adeb56ca2', '000000021');
INSERT INTO pos.d_image VALUES (1000353, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png', 'Y', '2024-09-18 16:48:58.658339', 1000069, '2024-09-18 16:48:58.658339', 1000069, 1000004, '1a04d011-ba51-4662-ba6a-bbb88487ac78', '000000025');
INSERT INTO pos.d_image VALUES (1000354, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png', 'Y', '2024-09-18 16:50:08.287428', 1000069, '2024-09-18 16:50:08.287428', 1000069, 1000004, '2fa8e810-8e63-438d-a99b-70ed2c278772', '000000025');
INSERT INTO pos.d_image VALUES (1000355, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png', 'Y', '2024-09-18 16:52:13.210979', 1000069, '2024-09-18 16:52:13.210979', 1000069, 1000004, '2f4f3a4a-7fb2-4749-ae9b-c11a48f1f837', '000000025');
INSERT INTO pos.d_image VALUES (1000356, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png', 'Y', '2024-09-18 16:53:59.519223', 1000069, '2024-09-18 16:53:59.519223', 1000069, 1000004, '4d2e252b-81e8-44ce-9c4e-9adc7bbb3d09', '000000025');
INSERT INTO pos.d_image VALUES (1000357, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png', 'Y', '2024-09-18 16:55:42.563576', 1000069, '2024-09-18 16:55:42.563576', 1000069, 1000004, '45ab22da-9c1e-4ecd-8117-61ddba06bcad', '000000025');
INSERT INTO pos.d_image VALUES (1000358, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png', 'Y', '2024-09-18 17:00:58.773268', 1000069, '2024-09-18 17:00:58.773268', 1000069, 1000004, '2e9dc3c2-9aa2-43cb-83a3-5898af30aa4e', '000000025');
INSERT INTO pos.d_image VALUES (1000359, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png', 'Y', '2024-09-18 17:02:43.466467', 1000069, '2024-09-18 17:02:43.466467', 1000069, 1000004, '906ec046-f688-4012-aef8-efe0db7ec6db', '000000025');
INSERT INTO pos.d_image VALUES (1000360, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png', 'Y', '2024-09-18 17:15:52.195207', 1000069, '2024-09-18 17:15:52.195207', 1000069, 1000004, 'b9a989f9-c4ff-4a1f-ab0f-02dff9ac023d', '000000025');
INSERT INTO pos.d_image VALUES (1000361, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png', 'Y', '2024-09-18 17:19:18.332319', 1000069, '2024-09-18 17:19:18.332319', 1000069, 1000004, 'ec1463b9-d678-43f0-9e06-2f0bc57c29e7', '000000025');
INSERT INTO pos.d_image VALUES (1000362, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png', 'Y', '2024-09-18 17:19:50.140605', 1000069, '2024-09-18 17:19:50.140605', 1000069, 1000004, '373ee0e6-b9b5-4274-9424-70f19ae6a22d', '000000025');
INSERT INTO pos.d_image VALUES (1000363, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png', 'Y', '2024-09-18 21:18:35.021132', 1000069, '2024-09-18 21:18:35.021132', 1000069, 1000004, '43d0aff7-ee69-455c-88c9-6bf1b3cd6fb7', '000000025');
INSERT INTO pos.d_image VALUES (1000365, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000025.png', 'Y', '2024-09-18 21:53:32.318019', 1000069, '2024-09-18 21:53:32.318021', 1000069, 1000004, 'eef5921c-6492-40fb-b836-eb938a8931b0', '000000025');
INSERT INTO pos.d_image VALUES (1000382, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000042.png', 'Y', '2024-09-18 23:36:56.454522', 1000069, '2024-09-18 23:36:56.454529', 1000069, 1000004, 'ddbe9429-55bc-4fe7-9721-2699ff158742', '000000042');
INSERT INTO pos.d_image VALUES (1000392, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000052.png', 'Y', '2024-09-19 11:10:15.773899', 1000069, '2024-09-19 11:10:15.773901', 1000069, 1000004, 'cdc3756d-c483-4d7c-a055-a0fad7c1e4b2', '000000052');
INSERT INTO pos.d_image VALUES (1000395, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000055.png', 'Y', '2024-09-19 11:27:15.481393', 1000069, '2024-09-19 11:27:15.481398', 1000069, 1000004, '705dfef9-696a-488d-9e61-a45dd4639042', '000000055');
INSERT INTO pos.d_image VALUES (1000396, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000056.png', 'Y', '2024-09-19 11:27:16.657808', 1000069, '2024-09-19 11:27:16.65781', 1000069, 1000004, 'c2868894-46a6-4338-a74b-5c608c279512', '000000056');
INSERT INTO pos.d_image VALUES (1000397, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000057.png', 'Y', '2024-09-19 11:27:17.777937', 1000069, '2024-09-19 11:27:17.77794', 1000069, 1000004, '7c356edb-4fb5-47c7-9f51-f8d522664395', '000000057');
INSERT INTO pos.d_image VALUES (1000398, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000058.png', 'Y', '2024-09-19 14:29:54.814244', 1000069, '2024-09-19 14:29:54.814247', 1000069, 1000004, '63e9b9a4-07e1-49ca-8ee7-184260a676e2', '000000058');
INSERT INTO pos.d_image VALUES (1000399, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000059.png', 'Y', '2024-09-19 14:29:56.113742', 1000069, '2024-09-19 14:29:56.113745', 1000069, 1000004, '79eb2887-6999-4b6a-8ed9-cf24292c9193', '000000059');
INSERT INTO pos.d_image VALUES (1000400, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000060.png', 'Y', '2024-09-19 14:29:57.279932', 1000069, '2024-09-19 14:29:57.279934', 1000069, 1000004, '6258f842-17d2-42dd-b4a4-982b3addcba8', '000000060');
INSERT INTO pos.d_image VALUES (1000405, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000061.png', 'Y', '2024-09-26 11:27:34.238525', 1000066, '2024-09-26 11:27:34.238525', 1000066, 1000004, '8a1b37cd-e187-4a47-bec8-42b65743e06c', '000000061');
INSERT INTO pos.d_image VALUES (1000406, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000062.png', 'Y', '2024-09-26 11:30:44.078461', 1000066, '2024-09-26 11:30:44.078461', 1000066, 1000004, 'd7e417fa-1e51-43f7-84c3-bae31b8688d3', '000000062');
INSERT INTO pos.d_image VALUES (1000407, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000063.png', 'Y', '2024-09-26 11:40:03.762612', 1000066, '2024-09-26 11:40:03.762613', 1000066, 1000004, 'cdccb738-456a-40fd-aac5-28221be48bd4', '000000063');
INSERT INTO pos.d_image VALUES (1000408, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000064.png', 'Y', '2024-09-26 11:43:04.153475', 1000066, '2024-09-26 11:43:04.153476', 1000066, 1000004, '5c405fd0-a472-40ac-a42f-2fb56f85d34f', '000000064');
INSERT INTO pos.d_image VALUES (1000409, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000065.png', 'Y', '2024-09-26 11:50:54.67987', 1000066, '2024-09-26 11:50:54.679871', 1000066, 1000004, '4839f58c-a3ac-49fd-9b7a-406c1e9244e3', '000000065');
INSERT INTO pos.d_image VALUES (1000411, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000067.png', 'Y', '2024-09-26 14:41:19.610714', 1000066, '2024-09-26 14:41:19.610714', 1000066, 1000004, '4997ffb4-a855-4e3a-87a6-474c13b108ce', '000000067');
INSERT INTO pos.d_image VALUES (1000412, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000068.png', 'Y', '2024-09-26 15:47:32.36879', 1000066, '2024-09-26 15:47:32.368791', 1000066, 1000004, '2861bcc4-226c-4ad3-b7fe-4a8db3860f58', '000000068');
INSERT INTO pos.d_image VALUES (1000413, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000069.png', 'Y', '2024-09-26 15:49:14.712842', 1000066, '2024-09-26 15:49:14.712843', 1000066, 1000004, 'a4d5f59b-c51f-40f1-9823-498ee34f2f4e', '000000069');
INSERT INTO pos.d_image VALUES (1000414, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000070.png', 'Y', '2024-09-26 16:10:56.867178', 1000066, '2024-09-26 16:10:56.867179', 1000066, 1000004, '61616275-d0ac-48d7-8466-0dde5b20d748', '000000070');
INSERT INTO pos.d_image VALUES (1000415, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000071.png', 'Y', '2024-09-26 16:10:57.573534', 1000066, '2024-09-26 16:10:57.573535', 1000066, 1000004, '2b3afe9f-59e4-49e2-aa09-ccb4e01a48fd', '000000071');
INSERT INTO pos.d_image VALUES (1000416, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000072.png', 'Y', '2024-09-26 16:10:58.051485', 1000066, '2024-09-26 16:10:58.051486', 1000066, 1000004, '262ac11b-f23c-412f-8e76-0d3e9f62313a', '000000072');
INSERT INTO pos.d_image VALUES (1000440, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000095.png', 'Y', '2024-09-27 17:41:00.219153', 1000066, '2024-09-27 17:41:00.219154', 1000066, 1000004, '5b7b12dc-6a35-40b1-a48f-d4445f5c4688', '000000095');
INSERT INTO pos.d_image VALUES (1000419, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000074.png', 'Y', '2024-09-26 18:51:18.25422', 1000066, '2024-09-26 18:51:18.254221', 1000066, 1000004, '3cf7419f-ca76-45e0-a1e9-f5ba5d2bfdf1', '000000074');
INSERT INTO pos.d_image VALUES (1000441, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000096.png', 'Y', '2024-09-27 17:41:00.725307', 1000066, '2024-09-27 17:41:00.725308', 1000066, 1000004, '0e6593a0-08c7-4952-8e31-2cd2b8ad605c', '000000096');
INSERT INTO pos.d_image VALUES (1000442, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000097.png', 'Y', '2024-09-27 17:41:01.25223', 1000066, '2024-09-27 17:41:01.252231', 1000066, 1000004, 'd9162b04-6e07-4a78-bed2-75acea3d4ee0', '000000097');
INSERT INTO pos.d_image VALUES (1000443, 'https://assets.digitalbiz.com.vn/Images/4772/F&B/000000098.png', 'Y', '2024-09-27 17:41:01.876301', 1000066, '2024-09-27 17:41:01.876302', 1000066, 1000004, 'b24d3955-c292-4829-9ba2-4ce935106158', '000000098');


--
-- TOC entry 5328 (class 0 OID 385579)
-- Dependencies: 233
-- Data for Name: d_industry; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_industry VALUES (1000008, '4772', 'Cửa hàng mỹ phẩm', 'Y', '2024-07-09 05:01:13.282466', 0, '2024-07-09 05:01:13.282466', 0, '7025cfdf-16eb-418d-a855-646eb1cded33', NULL);
INSERT INTO pos.d_industry VALUES (1000001, '5630', 'Coffee & Tea', 'Y', '2024-07-09 05:01:13.282466', 0, '2024-07-09 05:01:13.282466', 0, '292e4993-2c25-41f9-9723-9598136a3d64', NULL);
INSERT INTO pos.d_industry VALUES (1000005, '4771', 'Thời trang', 'Y', '2024-07-09 05:01:13.282466', 0, '2024-07-09 05:01:13.282466', 0, 'eaed1e2e-316c-44d0-8653-96453af59bc6', NULL);
INSERT INTO pos.d_industry VALUES (1000013, '4741', 'Điện tử máy tính', 'Y', '2024-07-09 05:01:13.282466', 0, '2024-07-09 05:01:13.282466', 0, '469c353c-2e19-4530-83bd-2ab14db197d1', NULL);
INSERT INTO pos.d_industry VALUES (1000014, '4761', 'Văn phòng phẩm', 'Y', '2024-07-09 05:01:13.282466', 0, '2024-07-09 05:01:13.282466', 0, '28e647d9-098a-4263-a82b-cc4282f62c68', NULL);
INSERT INTO pos.d_industry VALUES (1000002, '9329', 'Quán karaoke', 'Y', '2024-07-09 05:01:13.282466', 0, '2024-07-09 05:01:13.282466', 0, 'b6113380-259f-4cdc-a8f1-28bb05b5e7d9', NULL);
INSERT INTO pos.d_industry VALUES (1000000, '5610', 'Nhà hàng | F&B (Food & Beverage)', 'Y', '2024-07-08 18:22:57.317071', 0, '2024-07-08 18:22:57.317071', 0, 'e5e9b4f0-f19c-49b0-845c-4610f18588e3', NULL);
INSERT INTO pos.d_industry VALUES (1000010, '4741', 'Cửa hàng điện thoại', 'Y', '2024-07-09 05:01:13.282466', 0, '2024-07-09 05:01:13.282466', 0, 'a004c3b6-7ae7-442b-b139-0254af23e68c', NULL);
INSERT INTO pos.d_industry VALUES (1000006, '4721', 'Thực phẩm', 'Y', '2024-07-09 05:01:13.282466', 0, '2024-07-09 05:01:13.282466', 0, 'b0e31d72-c83a-4a4b-b4f4-530e7c7cb3c2', NULL);
INSERT INTO pos.d_industry VALUES (1000003, '9329', 'Quán bida', 'Y', '2024-07-09 05:01:13.282466', 0, '2024-07-09 05:01:13.282466', 0, '006670aa-8a2f-4f24-890f-8e9b9da9f7ac', NULL);
INSERT INTO pos.d_industry VALUES (1000011, '3290', 'Cơ sở Sản xuất', 'Y', '2024-07-09 05:01:13.282466', 0, '2024-07-09 05:01:13.282466', 0, 'fe9b0ff5-649a-4591-90f2-e3dcc4357b86', NULL);
INSERT INTO pos.d_industry VALUES (1000007, '4773 ', 'Hoa quà tặng', 'Y', '2024-07-09 05:01:13.282466', 0, '2024-07-09 05:01:13.282466', 0, 'c31fdb98-99bf-4928-849c-4fe78e2092a4', NULL);
INSERT INTO pos.d_industry VALUES (1000009, '4772', 'Nhà thuốc', 'Y', '2024-07-09 05:01:13.282466', 0, '2024-07-09 05:01:13.282466', 0, 'd9307207-2c99-4317-b841-0ea4c293d124', NULL);
INSERT INTO pos.d_industry VALUES (1000012, '4663', 'Sắt thép - VLXD', 'Y', '2024-07-09 05:01:13.282466', 0, '2024-07-09 05:01:13.282466', 0, '0696de0e-176b-4b3e-a659-cd3a36e01315', NULL);
INSERT INTO pos.d_industry VALUES (1000004, '4711', 'Siêu thị, tạp hóa', 'Y', '2024-07-09 05:01:13.282466', 0, '2024-07-09 05:01:13.282466', 0, '20202afb-d20c-4666-a295-0fe6bd1b5f0e', NULL);
INSERT INTO pos.d_industry VALUES (1000015, '4541', 'Xe máy, Phụ tùng', 'Y', '2024-07-09 14:32:23.825227', NULL, '2024-07-09 14:32:23.825227', NULL, '38efae84-ff27-4d63-861a-2254150750e6', NULL);


--
-- TOC entry 5463 (class 0 OID 393795)
-- Dependencies: 373
-- Data for Name: d_integration_history; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_integration_history VALUES (1000006, 1000004, 0, 1000035, '2024-09-21 03:55:00', 'PRO', 'PTE', 'COM', '2024-09-10 09:48:15.622679', 1000069, '2024-09-10 09:48:15.622679', 1000069, 'e9aa5a22-3268-41ac-9bd2-2ea02fc0f633', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000007, 1000004, 0, 1000035, '2024-09-22 03:55:00', 'PRO', 'PTE', 'COM', '2024-09-10 09:51:55.173459', 1000069, '2024-09-10 09:51:55.173459', 1000069, '41e3f0de-d000-44e6-b208-8688cf7fd0ea', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000008, 1000004, 0, 1000035, '2024-09-22 03:55:00', 'PRO', 'ETP', 'COM', '2024-09-10 09:52:27.080392', 1000069, '2024-09-10 09:52:27.080392', 1000069, '9df44bc2-04dd-4275-aeaa-e1bfeb7d8c8c', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000009, 1000009, 0, 1000035, '2024-09-22 03:55:00', 'PRO', 'ETP', 'COM', '2024-09-16 16:05:10.446558', 1000071, '2024-09-16 16:05:10.446559', 1000071, 'b247bdf3-0a94-432b-8b4f-88145b4af0f6', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000013, 1000004, 0, 1000070, '2024-09-17 11:37:46', 'PRO', 'ETP', 'COM', '2024-09-17 11:36:47.066782', 1000069, '2024-09-17 11:36:47.066786', 1000069, 'fe3cc9dd-fe47-4363-b75d-ddf207c0a3a5', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000014, 1000004, 0, 1000070, '2024-09-17 11:52:45', 'PTM', 'ETP', 'COM', '2024-09-17 11:51:46.190029', 1000069, '2024-09-17 11:51:46.190035', 1000069, '2883e68f-bd8c-4121-a0e4-6d26b5f4e331', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000015, 1000004, 0, 1000070, '2024-09-17 11:55:55', 'PTM', 'PTE', 'COM', '2024-09-17 11:54:56.936713', 1000069, '2024-09-17 11:54:56.936716', 1000069, 'ad01d185-7af5-4bdd-bd21-fcf5fc9ba202', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000016, 1000004, 0, 1000070, '2024-09-17 11:56:14', 'WHO', 'PTE', 'COM', '2024-09-17 11:55:14.867282', 1000069, '2024-09-17 11:55:14.867285', 1000069, '5644bca5-134d-4b17-b43d-013fa59df019', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000011, 1000004, 0, 1000035, '2024-09-17 10:49:56', 'PRO', 'ETP', 'COM', '2024-09-17 10:48:57.297851', 1000069, '2024-09-17 10:48:57.297856', 1000069, '3144dc9f-6151-4aa8-bf05-23c9c861831c', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000012, 1000004, 0, 1000035, '2024-09-17 10:51:02', 'PRO', 'PTE', 'COM', '2024-09-17 10:50:07.594844', 1000069, '2024-09-17 10:50:07.594847', 1000069, '64e6bb70-6ae8-4374-84b1-91f17f7f9527', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000010, 1000009, 0, 1000035, '2024-09-22 03:55:00', 'PRO', 'ETP', 'COM', '2024-09-17 10:15:58.46508', 1000071, '2024-09-17 10:15:58.465083', 1000071, '83054bae-0e58-407b-8260-d95e6641bedd', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000018, 1000004, 0, 1000070, '2024-09-17 16:47:03', 'ORG', 'ETP', 'COM', '2024-09-17 16:46:04.728652', 1000069, '2024-09-17 16:46:04.728654', 1000069, '65b65486-29f3-4755-a562-8b357b8c6a7c', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000017, 1000004, 0, 1000035, '2024-09-22 03:55:00', 'PRO', 'ETP', 'COM', '2024-09-17 16:05:30.178677', 1000069, '2024-09-17 16:05:30.17868', 1000069, '0f57dc86-6206-4505-9fe7-893f8049a08a', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000019, 1000004, 0, 1000070, '2024-09-18 14:50:52', 'ORG', 'PTE', 'COM', '2024-09-18 14:45:48.582998', 1000069, '2024-09-18 14:45:48.583002', 1000069, 'fa8cf0a2-a6d6-4034-a192-52f35314a485', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000020, 1000004, 0, 1000070, '2024-09-18 14:50:53', 'ORG', 'ETP', 'COM', '2024-09-18 14:45:49.067643', 1000069, '2024-09-18 14:45:49.067662', 1000069, '744b014d-f848-421d-b1f7-8690999c1e65', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000021, 1000004, 0, 1000035, '2024-09-18 15:13:20.424414', 'PRO', 'ETP', 'COM', '2024-09-18 15:13:20.464554', 1000069, '2024-09-18 15:13:20.464554', 1000069, '374413be-d9a8-49e7-a1bc-48c797093635', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000023, 1000004, 0, 1000035, '2024-09-18 17:54:21.908936', 'PRO', 'ETP', 'COM', '2024-09-18 17:54:21.93484', 1000069, '2024-09-18 17:54:21.93484', 1000069, '288fd570-bf69-4bbc-8bfd-ce1ff8124aeb', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000024, 1000004, 0, 1000070, '2024-09-18 17:54:47.065484', 'CUS', 'PTE', 'COM', '2024-09-18 17:54:47.065843', 1000069, '2024-09-18 17:54:47.065843', 1000069, '340c60d5-7752-43be-b135-12b7916b114a', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000025, 1000004, 0, 1000070, '2024-09-18 17:57:14.158768', 'PTM', 'ETP', 'COM', '2024-09-18 17:57:14.159104', 1000069, '2024-09-18 17:57:14.159105', 1000069, '26727d21-94c7-44ae-8c56-c0de89e8c948', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000026, 1000004, 0, 1000070, '2024-09-18 17:57:35.189391', 'FLO', 'PTE', 'COM', '2024-09-18 17:57:35.189773', 1000069, '2024-09-18 17:57:35.189774', 1000069, '64163607-bfcf-4719-929d-baf5bc10f741', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000027, 1000004, 0, 1000070, '2024-09-18 17:59:31.659904', 'PRO', 'PTE', 'COM', '2024-09-18 17:59:31.660413', 1000069, '2024-09-18 17:59:31.660413', 1000069, 'fd42e023-2ead-4b14-88bb-6800f51ab2c1', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000028, 1000004, 0, 1000070, '2024-09-18 18:00:23.133308', 'CUS', 'PTE', 'COM', '2024-09-18 18:00:23.133745', 1000069, '2024-09-18 18:00:23.133745', 1000069, 'e3bfcbba-93fb-42b1-a158-15dee36875e5', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000029, 1000004, 0, 1000070, '2024-09-18 18:00:35.275081', 'PTM', 'PTE', 'COM', '2024-09-18 18:00:35.275454', 1000069, '2024-09-18 18:00:35.275454', 1000069, '075bd892-7893-401e-a77d-aed3011efdf4', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000030, 1000004, 0, 1000070, '2024-09-19 00:11:28.013503', 'PCG', 'PTE', 'COM', '2024-09-19 00:11:28.017483', 1000069, '2024-09-19 00:11:28.017484', 1000069, 'bca3ef0a-2283-4aac-b8fd-4ed1bb0ddb19', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000031, 1000004, 0, 1000070, '2024-09-19 00:11:59.506199', 'PRO', 'ETP', 'COM', '2024-09-19 00:11:59.506733', 1000069, '2024-09-19 00:11:59.506733', 1000069, '57f296a6-fa5d-49c9-8ec0-28fedc453ac3', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000032, 1000004, 0, 1000035, '2024-09-21 14:28:06.990241', 'ORG', 'PTE', 'COM', '2024-09-21 14:28:07.014909', 1000069, '2024-09-21 14:28:07.014909', 1000069, '0169252a-86c7-4507-8e70-f33117650902', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000033, 1000004, 0, 1000035, '2024-09-21 14:29:25.02074', 'ORG', 'PTE', 'COM', '2024-09-21 14:29:25.02074', 1000069, '2024-09-21 14:29:25.02074', 1000069, '00d97307-956d-4cdf-ad09-c39e69b21f11', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000034, 1000004, 0, 1000035, '2024-09-21 14:29:50.166078', 'ORG', 'PTE', 'COM', '2024-09-21 14:29:50.1671', 1000069, '2024-09-21 14:29:50.1671', 1000069, '93819004-22d7-4c03-83a4-0dae52590ea9', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000035, 1000004, 0, 1000035, '2024-09-21 14:31:07.390269', 'ORG', 'PTE', 'COM', '2024-09-21 14:31:07.390269', 1000069, '2024-09-21 14:31:07.390269', 1000069, '418f502f-abc1-4513-8987-65f01d3ad696', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000036, 1000004, 0, 1000035, '2024-09-21 14:33:08.848767', 'ORG', 'PTE', 'COM', '2024-09-21 14:33:08.848767', 1000069, '2024-09-21 14:33:08.848767', 1000069, '3b6cedbb-d694-4359-a9ba-f8e8ec15d817', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000037, 1000004, 0, 1000035, '2024-09-21 14:33:40.944039', 'ORG', 'PTE', 'COM', '2024-09-21 14:33:40.945042', 1000069, '2024-09-21 14:33:40.945042', 1000069, 'f086a2fc-e4ae-41ce-81f1-b83b5020bfbc', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000038, 1000004, 0, 1000035, '2024-09-21 14:33:49.636985', 'ORG', 'PTE', 'COM', '2024-09-21 14:33:49.637998', 1000069, '2024-09-21 14:33:49.637998', 1000069, '03de63ac-e1f9-40e7-8746-b5cbae999932', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000039, 1000004, 0, 1000035, '2024-09-21 14:33:56.614742', 'ORG', 'PTE', 'COM', '2024-09-21 14:33:56.616309', 1000069, '2024-09-21 14:33:56.616309', 1000069, '24cc1145-7598-4be2-bb0d-fca76f036c5d', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000040, 1000004, 0, 1000035, '2024-09-21 14:34:47.133468', 'ORG', 'PTE', 'COM', '2024-09-21 14:34:47.133468', 1000069, '2024-09-21 14:34:47.133468', 1000069, '263b1aab-c426-42a2-b83f-62cc25965d7c', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000041, 1000004, 0, 1000035, '2024-09-21 16:21:22.415013', 'PTM', 'PTE', 'COM', '2024-09-21 16:21:22.43503', 1000069, '2024-09-21 16:21:22.43503', 1000069, 'd9a318f6-4613-4e45-989a-bf9e53c9f6ab', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000042, 1000004, 0, 1000035, '2024-09-21 16:27:18.729772', 'PTM', 'PTE', 'COM', '2024-09-21 16:27:18.752773', 1000069, '2024-09-21 16:27:18.752773', 1000069, '5564007e-bf71-4ad2-a89e-6b1645ee1438', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000043, 1000004, 0, 1000035, '2024-09-22 15:08:42.145173', 'FLO', 'ETP', 'COM', '2024-09-22 15:08:42.238196', 1000069, '2024-09-22 15:08:42.238196', 1000069, 'b0ba52bc-b112-4bdf-8a42-4c0dbf1143f1', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000044, 1000004, 0, 1000035, '2024-09-22 17:13:20.868453', 'TBL', 'ETP', 'COM', '2024-09-22 17:13:20.910776', 1000069, '2024-09-22 17:13:20.910776', 1000069, 'f1caa89d-c928-4779-862e-8950a4271ae3', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000045, 1000004, 0, 1000035, '2024-09-22 17:14:02.100533', 'TBL', 'ETP', 'COM', '2024-09-22 17:14:02.100533', 1000069, '2024-09-22 17:14:02.100533', 1000069, '7bef816b-7201-497e-ac38-4fc56b27a3a9', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000046, 1000004, 0, 1000035, '2024-09-25 00:15:58.668509', 'CAV', 'ETP', 'COM', '2024-09-25 00:15:58.685123', 1000069, '2024-09-25 00:15:58.685123', 1000069, '596dceba-d5a6-4f56-91f8-b1c13b4346e0', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000047, 1000004, 0, 1000035, '2024-09-25 08:33:21.838997', 'PCG', 'ETP', 'COM', '2024-09-25 08:33:21.983962', 1000069, '2024-09-25 08:33:21.983962', 1000069, 'ab18146f-e645-44fd-b234-01c69916d25d', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000048, 1000004, 0, 1000035, '2024-09-25 08:35:41.906208', 'CAV', 'ETP', 'COM', '2024-09-25 08:35:41.906208', 1000069, '2024-09-25 08:35:41.906208', 1000069, 'c0fe184e-42d7-489d-83f8-4c9d3ddf42d4', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000049, 1000004, 0, 1000035, '2024-09-25 08:51:39.3259', 'PCG', 'ETP', 'COM', '2024-09-25 08:51:39.344541', 1000069, '2024-09-25 08:51:39.344541', 1000069, '2bdfe0cc-d79c-4c54-83fa-d80a02c0c6a8', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000050, 1000004, 0, 1000035, '2024-09-25 08:55:00.201238', 'PCG', 'ETP', 'COM', '2024-09-25 08:55:00.201238', 1000069, '2024-09-25 08:55:00.201238', 1000069, 'e2b8cb17-6eb8-4a37-b669-753c295f7069', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000051, 1000004, 0, 1000035, '2024-09-25 08:56:18.907216', 'PCG', 'ETP', 'COM', '2024-09-25 08:56:18.908216', 1000069, '2024-09-25 08:56:18.908216', 1000069, '4401c622-36ff-4238-bf23-cbe8ddd78a16', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000052, 1000004, 0, 1000035, '2024-09-25 08:59:11.246064', 'PCG', 'ETP', 'COM', '2024-09-25 08:59:11.246064', 1000069, '2024-09-25 08:59:11.246064', 1000069, '712eba57-6c12-4db4-b960-38f10d751f4d', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000053, 1000004, 0, 1000035, '2024-09-25 09:01:24.550521', 'PCG', 'ETP', 'COM', '2024-09-25 09:01:24.551517', 1000069, '2024-09-25 09:01:24.551517', 1000069, 'ca465cdb-f504-49b4-a0a8-8579bfda6bd2', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000054, 1000004, 0, 1000035, '2024-09-25 09:02:18.020192', 'PCG', 'ETP', 'COM', '2024-09-25 09:02:18.021207', 1000069, '2024-09-25 09:02:18.021207', 1000069, 'cefade2a-974b-4be5-81ef-5eb482642299', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000055, 1000004, 0, 1000035, '2024-09-25 09:05:32.971088', 'PCG', 'ETP', 'COM', '2024-09-25 09:05:32.971088', 1000069, '2024-09-25 09:05:32.971088', 1000069, '8953d9fb-68d5-4fcd-8531-d1554951eb62', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000056, 1000004, 0, 1000035, '2024-09-25 23:20:28.61118', 'CUS', 'ETP', 'COM', '2024-09-25 23:20:28.666532', 1000069, '2024-09-25 23:20:28.666532', 1000069, '92bb39ce-8a00-43a8-9706-d23828ae6b7d', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000057, 1000004, 0, 1000035, '2024-09-25 23:50:00.106608', 'PCG', 'ETP', 'COM', '2024-09-25 23:50:00.115662', 1000069, '2024-09-25 23:50:00.115662', 1000069, '066cf45a-3c0d-41f2-80cc-79094d0920f3', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000058, 1000004, 0, 1000035, '2024-09-25 23:51:04.28468', 'PCG', 'ETP', 'COM', '2024-09-25 23:51:04.28468', 1000069, '2024-09-25 23:51:04.28468', 1000069, '6f0c4d31-cf0d-48ba-a2fe-2301688b2a5c', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000059, 1000004, 0, 1000035, '2024-09-25 23:55:40.684004', 'PCG', 'ETP', 'COM', '2024-09-25 23:55:40.685003', 1000069, '2024-09-25 23:55:40.685003', 1000069, '29826941-147d-4382-9925-2ec703f6201d', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000060, 1000004, 0, 1000035, '2024-09-25 23:56:53.032591', 'PCG', 'ETP', 'COM', '2024-09-25 23:56:53.032591', 1000069, '2024-09-25 23:56:53.032591', 1000069, '76e9c282-0ef8-4114-9ad4-28a2d1a05bc5', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000061, 1000004, 0, 1000035, '2024-09-26 00:00:13.96601', 'PCG', 'ETP', 'COM', '2024-09-26 00:00:13.96701', 1000069, '2024-09-26 00:00:13.96701', 1000069, '8bd6c406-92b5-4907-8cd5-064a57baa2af', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000062, 1000004, 0, 1000035, '2024-09-26 00:34:09.904397', 'PCG', 'ETP', 'COM', '2024-09-26 00:34:09.905396', 1000069, '2024-09-26 00:34:09.905396', 1000069, '1763e4b0-2dec-43a2-83b4-65cf240f79b2', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000063, 1000004, 0, 1000070, '2024-09-26 22:32:36.998238', 'ORG', 'ETP', 'COM', '2024-09-26 22:32:37.017692', 1000066, '2024-09-26 22:32:37.017693', 1000066, '21d15f4b-e9f4-448f-a48d-b94c6e5ff694', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000064, 1000004, 0, 1000070, '2024-09-26 22:33:25.908952', 'ORG', 'ETP', 'COM', '2024-09-26 22:33:25.909436', 1000066, '2024-09-26 22:33:25.909436', 1000066, '899ba9a4-2417-4b5a-873b-ef61393fa423', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000065, 1000004, 0, 1000070, '2024-09-27 00:51:30.644348', 'PCG', 'ETP', 'COM', '2024-09-27 00:51:30.645117', 1000066, '2024-09-27 00:51:30.645117', 1000066, '573cbf84-e4eb-4b8a-bc75-facda0144d11', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000066, 1000004, 0, 1000070, '2024-09-27 09:44:51.193674', 'ORG', 'ETP', 'COM', '2024-09-27 09:44:51.19493', 1000066, '2024-09-27 09:44:51.19493', 1000066, 'd486ce9c-26d2-4fd9-83c9-e8457b7f8729', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000067, 1000004, 0, 1000070, '2024-09-27 09:51:37.333407', 'ORG', 'ETP', 'COM', '2024-09-27 09:51:37.333897', 1000066, '2024-09-27 09:51:37.333897', 1000066, 'fe6f72f7-7e82-4bf4-90cb-f9242235d872', 'Y');
INSERT INTO pos.d_integration_history VALUES (1000068, 1000004, 0, 1000070, '2024-09-27 09:54:34.724688', 'ORG', 'ETP', 'COM', '2024-09-27 09:54:34.725086', 1000066, '2024-09-27 09:54:34.725086', 1000066, '8aaf5918-f7a7-483d-b4b6-af5d531e34b7', 'Y');





--
-- TOC entry 5331 (class 0 OID 385625)
-- Dependencies: 236
-- Data for Name: d_language; Type: TABLE DATA; Schema: pos; Owner: -
--



--
-- TOC entry 5332 (class 0 OID 385635)
-- Dependencies: 237
-- Data for Name: d_locator; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_locator VALUES (1000002, 1000016, 1000004, '2024-08-12 10:13:09.479545', 1000069, '2024-08-12 10:13:09.479545', 1000069, 'Y', 'L1', NULL, '10', '10', '10', 1000006, '35209962-4522-4b63-93e6-03eb30aadcf7', NULL);
INSERT INTO pos.d_locator VALUES (1000000, 0, 1000002, '2024-07-21 23:26:17.306435', 1000037, '2024-07-21 23:26:17.306435', 1000037, 'Y', 'code', NULL, '10', '10', '10', 1000003, '156a7490-7fe3-46c1-85d1-b196d5752fc8', NULL);


--
-- TOC entry 5419 (class 0 OID 390937)
-- Dependencies: 325
-- Data for Name: d_note; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_note VALUES (1000000, 1000002, 1000004, 1000016, 'Y', '30% đường', '30% đường', '2024-08-14 11:05:14.59281', 1000069, '2024-08-14 11:05:14.59281', 1000069, '6a9e49da-e28a-4cfb-b2a0-ca2c6b0d30cd', NULL);
INSERT INTO pos.d_note VALUES (1000001, 1000002, 1000004, 1000016, 'Y', '30% đường', '30% đường', '2024-08-15 10:07:20.497753', 1000069, '2024-08-15 10:07:20.497753', 1000069, '2441fbba-7e48-44bb-9b16-898124f2352a', '1000002');
INSERT INTO pos.d_note VALUES (1000005, 1000001, 1000004, 1000016, 'Y', 'test 2', NULL, '2024-08-21 14:02:02.177908', 1000069, '2024-08-27 14:54:08.817006', 1000069, '4a1ea983-411b-498b-9682-80522f936118', '1000017,1000018');
INSERT INTO pos.d_note VALUES (1000006, 1000006, 1000004, 1000016, 'Y', '30% Sữaa', NULL, '2024-08-29 11:55:42.003343', 1000069, '2024-08-29 11:56:15.952904', 1000069, 'ddd791ed-441c-4cb4-bfb0-e79296d71fac', '1000019');
INSERT INTO pos.d_note VALUES (1000008, 1000002, 1000004, 1000017, 'Y', '30% đường', '30% đường', '2024-09-26 11:05:34.768341', 1000069, '2024-09-26 11:05:34.768342', 1000069, 'c20f63df-5814-4291-abf1-85ced20d3544', '1000001,1000002');


--
-- TOC entry 5417 (class 0 OID 390919)
-- Dependencies: 323
-- Data for Name: d_note_group; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_note_group VALUES (1000002, 1000004, 1000016, 'Y', 'Sữa 2', 'Sữa', '2024-08-14 11:04:26.198612', 1000069, '2024-08-21 14:39:14.431429', 1000069, 'd243aa43-e01f-441e-9941-8af35558d708');
INSERT INTO pos.d_note_group VALUES (1000005, 1000004, 1000016, 'Y', 'Kem', 'Sữa', '2024-08-21 14:48:37.934316', 1000069, '2024-08-21 14:48:37.934318', 1000069, '514e360b-c5d4-4396-b9e8-2056827ace44');
INSERT INTO pos.d_note_group VALUES (1000006, 1000004, 1000016, 'Y', 'Phô mai', NULL, '2024-08-21 14:50:04.426453', 1000069, '2024-08-21 14:50:04.426455', 1000069, '21d9cf63-e536-4b23-9843-e4e4319a35bf');
INSERT INTO pos.d_note_group VALUES (1000001, 1000004, 1000016, 'Y', 'Đường 3', 'Đường', '2024-08-14 11:03:55.669727', 1000069, '2024-08-21 14:53:02.619579', 1000069, '176e3a56-dc6c-4cda-9197-312e351fd153');
INSERT INTO pos.d_note_group VALUES (1000007, 1000004, 1000016, 'Y', 'Sữa', NULL, '2024-08-29 11:55:15.517202', 1000069, '2024-08-29 11:55:15.517204', 1000069, '2a28275c-1c32-4d2d-8721-25afe8ff34e8');



--
-- TOC entry 5336 (class 0 OID 385716)
-- Dependencies: 241
-- Data for Name: d_org; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_org VALUES (1000001, '*', 'all', 1000002, NULL, NULL, '2024-07-09 08:46:28.023413', 0, '2024-07-09 08:46:28.023413', 'Y', '4cc5c642-a3fb-440f-9f59-1106c6c257d7', 0, NULL, NULL, NULL, NULL, NULL, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000003, 'SG', 'Sài Gòn', 1000003, NULL, NULL, '2024-07-12 02:43:36.39605', 0, '2024-07-12 02:43:36.39605', 'Y', 'fe99e717-0650-423a-b18d-7c3d86630028', 0, NULL, NULL, NULL, NULL, NULL, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000005, 'Osen', 'OnsenFuji', 1000002, NULL, NULL, '2024-07-22 02:58:28.040277', 0, '2024-07-22 02:58:28.040277', 'Y', 'c02d875e-c286-4968-ac31-f04bc06b04b5', 0, NULL, NULL, NULL, NULL, NULL, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000004, 'HN', 'Hà Nội', 1000003, NULL, NULL, '2024-07-12 02:43:36.39605', 0, '2024-07-12 02:43:36.39605', 'Y', '', 0, NULL, NULL, NULL, NULL, NULL, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000016, 'F&B_SG', 'Chi nhánh Sài Gòn', 1000004, 'Hồ Chí Minh', NULL, '2024-08-04 12:03:24.721412', 1000069, '2024-08-04 12:03:24.721412', 'Y', '64a08bb1-00a2-4d8c-93e0-33cc41544f7f', 1000069, 'fbsaigon@gmail.com', '099', 'TPHCM', NULL, NULL, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000019, 'F&B_SG', 'Chi nhánh Hà Nội', 1000004, 'Hà Nội', NULL, '2024-08-30 03:39:07.929593', 0, '2024-08-30 03:39:07.929593', 'Y', '01db7cb1-fe99-4510-860a-75fb6e18b29f', 0, 'fbhanoi@gmail.com', NULL, 'HN', NULL, NULL, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000036, 'OFJT', 'TẮM KHOÁNG THANH THỦY', 1000004, '', '?', '2024-09-21 14:28:06.851625', 1000069, '2024-09-21 14:28:06.851625', 'Y', '3ec2a406-7e9c-4af2-8e2b-d36d84e6aee8', 1000069, NULL, '', NULL, NULL, 1000460, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000038, 'R001', 'DBIZ', 1000004, 'HK109, Dự án Wyndham Lynn Times Thanh Thủy, xã Bảo Yên, huyện Thanh Thủy, Tỉnh Phú Thọ, Việt Nam, Huyện Thanh Thuỷ, Phú Thọ, Viet Nam', '0100109106-509', '2024-09-21 14:28:06.905972', 1000069, '2024-09-21 14:28:06.905972', 'Y', '16b7c3d4-250b-4ee6-bbcc-77814490739b', 1000069, NULL, '0971902297', NULL, NULL, 1000399, NULL, 'N');
INSERT INTO pos.d_org VALUES (0, '*', 'All', 1000004, NULL, NULL, '2024-08-04 04:45:45.047673', 0, '2024-08-04 04:45:45.047673', 'Y', '2a377c39-8c87-48ef-90ac-9668d3ae9147', 0, NULL, NULL, NULL, NULL, 0, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000033, 'FBHQ', 'Văn Phòng (HeadQuarter)', 1000004, 'HK109, Dự án Wyndham Lynn Times Thanh Thủy, xã Bảo Yên, huyện Thanh Thủy, Tỉnh Phú Thọ, Việt Nam, Viet Nam', '2601096684', '2024-09-21 14:28:06.630957', 1000069, '2024-09-26 22:32:36.835435', 'Y', '040445ce-b04a-44b7-82db-f3f452072669', 1000066, '', '', 'Quan 1', NULL, 1000400, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000027, 'CN1000027', 'Cửa hàng 3', 1000009, 'Đà Nẵng', NULL, '2024-09-12 06:58:40.551331', 1000071, '2024-09-12 06:58:40.551331', 'Y', '475e8fcc-513e-4bce-88f6-20952415d2e7', 1000071, 'T02HN@gmail.com', '099000', 'Đà Nẵng', NULL, NULL, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000026, 'CN1000023', 'Cửa hàng 2', 1000009, 'Hà Nội', NULL, '2024-09-12 06:57:24.460095', 1000071, '2024-09-12 06:57:24.460095', 'Y', 'eb5002a1-5912-4cb9-a28b-2c26d910eead', 1000071, 'T02HN@gmail.com', '099000', 'Hà Nội', NULL, NULL, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000022, 'CN1000022', 'Cửa hàng 1', 1000009, 'Hồ Chí Minh', NULL, '2024-09-11 04:26:30.893693', 0, '2024-09-11 04:26:30.893693', 'Y', '88b07d2c-9024-4ee3-9e03-2df22db4220c', 0, 'tonthep@gmail.com', NULL, 'Hồ Chí Minh', NULL, NULL, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000030, 'F&B QN', 'Chi nhánh Quy Nhơn1', 1000004, 'Binh Dinh', NULL, '2024-09-16 15:11:28.713499', 1000069, '2024-09-16 16:03:34.329293', 'Y', 'bd9b11cb-cedc-48cc-a211-5ae86d055f5a', 1000069, 'hungnguyen.201102ak@gmail.com', NULL, 'GL', NULL, NULL, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000021, 'F&B_GL', 'Chi nhánh Gia Lai', 1000004, 'Pleiku', NULL, '2024-09-04 15:54:58.678314', 1000069, '2024-09-17 15:25:13.347643', 'Y', '5cf01668-52f4-4e3d-9297-b06f9a000eca', 1000069, 'fbgl@gmail.com', '093564512', 'GL1', 'Thành phố Pleiku', NULL, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000020, 'F&B _DN', 'Chi nhánh Đà Nẵng', 1000004, 'Da Nang', NULL, '2024-09-04 15:49:29.280256', 1000069, '2024-09-17 15:27:20.862812', 'Y', '4a87b858-afae-4f6e-a45b-f6754bd5e3da', 1000069, 'hungnguyen.201102ak@gmail.com', '0935703991', 'ĐN', 'Trung tâm', NULL, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000034, 'OFFB', 'DIGITAL BIZ', 1000004, 'HK109, Dự án Wyndham Lynn Times Thanh Thủy, xã Bảo Yên, huyện Thanh Thủy, Tỉnh Phú Thọ, Việt Nam, HK109, Dự án Wyndham Lynn Times Thanh Thủy, Thanh thủy, Phú Thọ, Viet Nam', '0100109106-509', '2024-09-21 14:28:06.801171', 1000069, '2024-09-21 14:28:06.801171', 'Y', '1de40390-31ad-4a9e-b326-58772bdedba2', 1000069, NULL, '02106279955', NULL, NULL, 1000398, NULL, 'Y');
INSERT INTO pos.d_org VALUES (1000035, 'OFJH', 'KHÁCH SẠN ONSEN FUJI', 1000004, '', '?', '2024-09-21 14:28:06.825061', 1000069, '2024-09-21 14:28:06.825061', 'Y', '31bdb1e2-6db3-4e7b-bb47-12a140471f3f', 1000069, NULL, '', NULL, NULL, 1000459, NULL, 'N');
INSERT INTO pos.d_org VALUES (1000037, 'ANPASO', 'CTY ANPASO', 1000004, '11D5 Khu biệt thự thảo nguyên, Phường Long Thạnh Mỹ, Thành phố Thủ Đức, Tp. HCM, Viet Nam', '?', '2024-09-21 14:28:06.875016', 1000069, '2024-09-27 09:44:51.176904', 'Y', '4fbd3f66-bc9d-48ff-821c-ee02814c667e', 1000066, '', '', 'Thu duc', NULL, 1000490, NULL, 'N');


--
-- TOC entry 5409 (class 0 OID 389549)
-- Dependencies: 315
-- Data for Name: d_partner_group; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_partner_group VALUES (1000011, 1000003, 0, 'Y', NULL, 'code', 'group name1', NULL, 'Y', '2024-07-30 15:51:12.040117', 1000068, '2024-07-30 15:51:12.040117', 1000068, 'ac132268-7c8b-4973-b8bd-6dd0a22089d1', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000012, 1000003, 0, 'Y', NULL, 'code', 'group name1', NULL, 'Y', '2024-07-31 10:50:54.806971', 1000068, '2024-07-31 10:50:54.806972', 1000068, 'c51fafb0-5fdb-4a67-af27-171082c51be2', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000013, 1000002, 1000001, 'Y', NULL, 'code', 'group name1', NULL, 'Y', '2024-07-31 11:04:58.590173', 1000037, '2024-07-31 11:04:58.590176', 1000037, 'b5f2cbba-be44-4bc9-ad63-bb096d75772e', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000014, 1000002, 1000001, 'Y', NULL, 'code', 'group name2', NULL, 'Y', '2024-07-31 11:11:27.426795', 1000037, '2024-07-31 11:11:27.426796', 1000037, '14457cc2-ad6f-447b-ac24-731b7f3b0454', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000015, 1000002, 1000001, 'N', NULL, 'code', 'group name vendor', NULL, 'Y', '2024-07-31 21:50:49.090758', 1000037, '2024-07-31 21:50:49.090763', 1000037, '080b7691-c8be-42a2-9333-d799b34b0104', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000016, 1000002, 1000001, 'Y', NULL, 'test', 'test', 'sdf', 'Y', '2024-08-01 17:24:11.173212', 1000037, '2024-08-01 17:24:11.173213', 1000037, 'b0fa850e-dce2-4b05-9cd0-34d30175c48d', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000017, 1000002, 1000001, 'Y', NULL, 'test2', 'test2', 'afs', 'Y', '2024-08-01 17:33:31.861228', 1000037, '2024-08-01 17:33:31.861228', 1000037, 'c414adfb-8cbd-408e-b53f-bf151cfc7a1a', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000018, 1000002, 1000001, 'Y', NULL, 't3', '3', '3', 'Y', '2024-08-01 17:38:02.948418', 1000037, '2024-08-01 17:38:02.948418', 1000037, '0db0e6eb-61b0-4c64-8810-9746acea9f5d', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000019, 1000002, 1000001, 'Y', NULL, '4', '4', '4', 'Y', '2024-08-01 17:40:30.351625', 1000037, '2024-08-01 17:40:30.351626', 1000037, '61ca1fd2-3e81-476a-ace9-f94b75c5f5ee', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000020, 1000002, 1000001, 'N', NULL, 'nhomNCC', 'nhomNCC', NULL, 'Y', '2024-08-02 11:57:47.509889', 1000037, '2024-08-02 11:57:47.50989', 1000037, 'f91e3da6-5476-462d-a5a1-383b01c366ec', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000021, 1000004, 1000016, 'Y', NULL, 'NV', 'Nhân Viên', NULL, 'Y', '2024-08-07 10:26:50.521323', 1000069, '2024-08-07 10:26:50.521323', 1000069, '5fffb371-4111-44a4-8822-43dad63513e1', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000022, 1000004, 1000016, 'Y', NULL, 'KH', 'Khách hàng', NULL, 'Y', '2024-08-07 10:28:13.852776', 1000069, '2024-08-07 10:28:13.852776', 1000069, 'c669f1fc-5bef-4848-a859-05afa04d98f8', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000024, 1000004, 1000016, 'N', NULL, 'nhomncc', 'qwe', 'qwe', 'Y', '2024-08-12 17:21:37.093838', 1000069, '2024-08-12 17:21:37.093845', 1000069, '3c754909-1dbd-4a23-b574-48c8749e62e1', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000025, 1000004, 1000016, 'N', NULL, 'nhom ncc', 'nhom ncc', '123', 'Y', '2024-08-14 15:37:31.809115', 1000069, '2024-08-14 15:37:31.809116', 1000069, 'c47c2c53-0186-4694-89b3-d80061b1c8db', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000026, 1000004, 1000016, 'Y', NULL, '', 'Nhóm VIPU', '', 'Y', '2024-08-21 09:51:23.936348', 1000069, '2024-08-21 09:51:23.936348', 1000069, 'f5c00771-632b-439f-943a-c6a583d93f3d', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000028, 1000004, 1000016, 'Y', NULL, 'HCMGROUP', 'Nhóm ở Hồ Chí Minh', NULL, 'Y', '2024-08-27 16:11:30.587815', 1000069, '2024-08-27 16:11:30.587815', 1000069, '4878a534-b768-48a3-9288-4b7c7e1d5769', NULL, 'Y', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000029, 1000004, 1000016, 'Y', NULL, 'EATDBIZ', 'Nhóm ăn uốngDbiz', NULL, 'Y', '2024-08-28 08:29:36.529915', 1000069, '2024-08-28 08:29:36.529915', 1000069, 'd9f79b1d-a7eb-46e9-af8e-08aa5bd8b1f4', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000062, 1000009, 0, 'N', NULL, '1000003', 'Nhà cung cấp', NULL, 'Y', '2024-09-11 16:22:51.705894', 1000071, '2024-09-11 16:22:51.705894', 1000071, '3a0c8cc0-8191-472a-8026-e00e967d2d82', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000063, 1000009, 0, 'Y', NULL, '1000004', 'Khách hàng doanh nghiệp', NULL, 'Y', '2024-09-11 17:40:53.588463', 1000071, '2024-09-11 17:40:53.588463', 1000071, 'df9542e0-5fc2-4edd-88a6-a6ad7cbc43fe', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000042, 1000004, 1000016, 'Y', NULL, '12112', 'Nhóm Tuyên Quang', NULL, 'Y', '2024-09-06 09:03:47.560755', 1000069, '2024-09-06 09:03:47.560755', 1000069, '42a021a0-9ee8-4cec-b949-9e040db876d9', 10.00, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000096, 1000004, 0, 'N', NULL, 'NCC2', 'NCC2-Nhóm cung cấp nước ngoài ', NULL, 'N', '2024-09-24 23:51:19.481709', 1000069, '2024-09-24 23:51:19.481709', 1000069, '2991cbbc-62f7-4212-974f-f4cd31f3d963', NULL, NULL, NULL, 1001374);
INSERT INTO pos.d_partner_group VALUES (1000043, 1000004, 1000016, 'N', NULL, '1211', 'Nhóm Tuyên Quang', NULL, 'Y', '2024-09-06 09:11:46.203329', 1000069, '2024-09-24 09:18:39.050838', 1000069, 'f29dedca-f646-4701-8bd5-97aac5bef1ff', 0.00, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000041, 1000004, 1000016, 'Y', NULL, '111', 'Nhóm Hải Phòng', NULL, 'Y', '2024-09-06 09:02:29.689349', 1000069, '2024-09-24 11:40:06.477822', 1000069, '2977d3fd-b7ac-43ae-8f6d-1f09ca52b5e0', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000060, 1000009, 0, 'Y', NULL, '1000001', 'Khách hàng cá nhân', NULL, 'Y', '2024-09-11 16:20:18.961739', 1000071, '2024-09-11 16:20:18.961739', 1000071, '8647e56a-c0a6-4f43-83e1-9f28dc10c1fb', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000061, 1000009, 0, 'Y', NULL, '1000002', 'Khách hàng lẻ', NULL, 'Y', '2024-09-11 16:20:31.260952', 1000071, '2024-09-11 16:20:31.260951', 1000071, '570aaac5-29c3-4808-a0d4-f03751f13ec9', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000097, 1000004, 0, 'N', NULL, 'NVCT', 'NVCT-Nhân viên ', NULL, 'N', '2024-09-24 23:51:19.501267', 1000069, '2024-09-24 23:51:19.501267', 1000069, '48a6bfec-f872-45f4-8c98-b18727d1295f', NULL, NULL, NULL, 1001373);
INSERT INTO pos.d_partner_group VALUES (1000098, 1000004, 0, 'N', NULL, 'NCC01', 'NCC01 - Nhà cung cấp trong nước', NULL, 'Y', '2024-09-24 23:51:19.52242', 1000069, '2024-09-24 23:51:19.52242', 1000069, '1921934a-1592-447e-ae51-21fbcde4406e', NULL, NULL, NULL, 1001387);
INSERT INTO pos.d_partner_group VALUES (1000081, 1000004, 0, 'N', NULL, 'CN', 'Chủ nhà', NULL, 'Y', '2024-09-24 23:51:18.089141', 1000069, '2024-09-24 23:51:18.089141', 1000069, 'd0dcb74a-3be9-417d-9c64-93ca78aee7a9', NULL, NULL, NULL, 1001435);
INSERT INTO pos.d_partner_group VALUES (1000082, 1000004, 0, 'Y', NULL, 'KH001', 'KH001 - Khách hàng vãng lai', NULL, 'Y', '2024-09-24 23:51:18.170605', 1000069, '2024-09-24 23:51:18.170605', 1000069, '036e70ae-ad85-46e7-b445-50ff4769f4f0', NULL, NULL, NULL, 1001388);
INSERT INTO pos.d_partner_group VALUES (1000083, 1000004, 0, 'Y', NULL, 'KH003', 'KH003 - Khách hàng doanh nghiệp', NULL, 'Y', '2024-09-24 23:51:18.21113', 1000069, '2024-09-24 23:51:18.21113', 1000069, '35307331-b26e-4a72-83d3-9aedd91a2b27', NULL, NULL, NULL, 1001394);
INSERT INTO pos.d_partner_group VALUES (1000084, 1000004, 0, 'Y', NULL, 'KH004', 'KH004 - Khách hàng nội bộ', NULL, 'Y', '2024-09-24 23:51:18.260232', 1000069, '2024-09-24 23:51:18.260232', 1000069, '2ec07dd3-1257-40a8-8b27-b52a23f1b859', NULL, NULL, NULL, 1001395);
INSERT INTO pos.d_partner_group VALUES (1000085, 1000004, 0, 'N', NULL, 'NVCT1', 'NVCT1-Nhân viên ', NULL, 'Y', '2024-09-24 23:51:18.295689', 1000069, '2024-09-24 23:51:18.295689', 1000069, '36737466-cd7b-4199-b429-b23e1ef7a5ad', NULL, NULL, NULL, 1001390);
INSERT INTO pos.d_partner_group VALUES (1000086, 1000004, 0, 'N', NULL, '1000001', 'Công ty Q9', NULL, 'Y', '2024-09-24 23:51:18.331207', 1000069, '2024-09-24 23:51:18.331207', 1000069, '7f193dd3-0938-42da-a95c-ce1c317cc8d3', NULL, NULL, NULL, 1001436);
INSERT INTO pos.d_partner_group VALUES (1000087, 1000004, 0, 'N', NULL, 'Standard', 'Standard', NULL, 'N', '2024-09-24 23:51:18.377486', 1000069, '2024-09-24 23:51:18.377486', 1000069, '0b3aab0a-11d7-4bc5-a60e-beffefaa6d4c', NULL, NULL, NULL, 1001371);
INSERT INTO pos.d_partner_group VALUES (1000088, 1000004, 0, 'N', NULL, 'TX', 'TX-Tài xế', NULL, 'N', '2024-09-24 23:51:18.420367', 1000069, '2024-09-24 23:51:18.420367', 1000069, '9e7e3b72-dd5c-4537-bf48-ecbbcf952aea', NULL, NULL, NULL, 1001375);
INSERT INTO pos.d_partner_group VALUES (1000089, 1000004, 0, 'N', NULL, 'NCC3', 'NCC3-Vay huy động', NULL, 'N', '2024-09-24 23:51:19.33264', 1000069, '2024-09-24 23:51:19.33264', 1000069, '9ff939a7-46ff-4f8a-a8e4-7f177ecb6dc4', NULL, NULL, NULL, 1001376);
INSERT INTO pos.d_partner_group VALUES (1000090, 1000004, 0, 'N', NULL, 'DTNB', 'DTNB-Nhóm nội bộ', NULL, 'N', '2024-09-24 23:51:19.359751', 1000069, '2024-09-24 23:51:19.359751', 1000069, 'ca88ec00-e0bc-47c4-a0ac-70eb2fcb123f', NULL, NULL, NULL, 1001381);
INSERT INTO pos.d_partner_group VALUES (1000091, 1000004, 0, 'Y', NULL, 'KH01', 'KH1-Khách hàng lẻ, vãng lai', NULL, 'N', '2024-09-24 23:51:19.382289', 1000069, '2024-09-24 23:51:19.382289', 1000069, '1147ce67-43b0-401c-9184-162a75316be5', NULL, NULL, NULL, 1001377);
INSERT INTO pos.d_partner_group VALUES (1000092, 1000004, 0, 'Y', NULL, 'KH02', 'KH2-Công ty lữ hành', NULL, 'N', '2024-09-24 23:51:19.404597', 1000069, '2024-09-24 23:51:19.404597', 1000069, '3978d0d2-fbf7-4d0d-8ea3-93e74a51c5e5', NULL, NULL, NULL, 1001378);
INSERT INTO pos.d_partner_group VALUES (1000093, 1000004, 0, 'Y', NULL, 'KH03', 'KH3-Khách hàng Doanh nghiệp', NULL, 'N', '2024-09-24 23:51:19.423144', 1000069, '2024-09-24 23:51:19.423144', 1000069, 'bf286f13-bc01-4f90-a920-de83b619dae3', NULL, NULL, NULL, 1001379);
INSERT INTO pos.d_partner_group VALUES (1000094, 1000004, 0, 'Y', NULL, 'KH04', 'KH4-Khách hàng nội bộ', NULL, 'N', '2024-09-24 23:51:19.442198', 1000069, '2024-09-24 23:51:19.442198', 1000069, '34be3f7b-fdf4-4d5c-9dde-a7063a56d2cb', NULL, NULL, NULL, 1001380);
INSERT INTO pos.d_partner_group VALUES (1000095, 1000004, 0, 'N', NULL, 'NCC1', 'NCC1-Nhà cung cấp trong nước', NULL, 'N', '2024-09-24 23:51:19.46126', 1000069, '2024-09-24 23:51:19.46126', 1000069, '2e203190-f563-423d-b46d-44c9d6117f25', NULL, NULL, NULL, 1001372);
INSERT INTO pos.d_partner_group VALUES (1000099, 1000004, 0, 'N', NULL, 'NCC02', 'NCC02 - Nhà cung cấp ngoài nước', NULL, 'Y', '2024-09-24 23:51:19.541475', 1000069, '2024-09-24 23:51:19.541475', 1000069, 'fa20723d-b70c-4662-8215-07a1fe0817ed', NULL, NULL, NULL, 1001392);
INSERT INTO pos.d_partner_group VALUES (1000100, 1000004, 0, 'Y', NULL, 'KH002', 'KH002 - Khách hàng cá nhân', NULL, 'Y', '2024-09-24 23:51:19.560538', 1000069, '2024-09-24 23:51:19.560538', 1000069, '2e6ca8ec-eaa1-4eac-bf58-c587a57739ba', NULL, NULL, NULL, 1001393);
INSERT INTO pos.d_partner_group VALUES (1000102, 1000004, 0, 'N', NULL, 'NCC07', 'Nhóm cung cấp dịch vụ', 'Chuyên cung cấp dịch vụ vận chuyển', 'Y', '2024-09-25 10:17:46.086129', 1000069, '2024-09-25 10:17:46.086129', 1000069, '9bcc5b51-c426-461a-a8b9-95610cec47bd', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000105, 1000004, 0, 'Y', NULL, 'KH006', 'Nhóm KH hạng Vàng', 'KH thuộc nhóm này được giảm 12%', 'Y', '2024-09-25 10:26:07.90699', 1000069, '2024-09-25 10:27:06.248572', 1000069, 'adf35eb7-326d-475c-9723-417f05ee05fe', 12.00, NULL, NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000106, 1000004, 0, 'N', NULL, 'NCC08', 'NCC TP.HCM', 'Chuyên cung cấp NVL tại TP.HCM', 'Y', '2024-09-25 10:29:52.613539', 1000069, '2024-09-25 10:29:52.613539', 1000069, '500e8796-87c4-4af9-b8c1-3cca6c394645', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000110, 1000004, 0, 'N', NULL, 'BP1000109', 'sdf', NULL, 'Y', '2024-09-25 15:09:35.961329', 1000069, '2024-09-25 15:09:35.961329', 1000069, '45febfb3-3c57-48d2-8993-ae80952d457e', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000111, 1000004, 0, 'Y', NULL, 'sdf', 'sdf', NULL, 'Y', '2024-09-25 15:09:42.222051', 1000069, '2024-09-25 15:09:42.222051', 1000069, '2a1449d9-40aa-4cc8-8ff7-f1e2cc4d1eee', 123.00, NULL, NULL, NULL);
INSERT INTO pos.d_partner_group VALUES (1000112, 1000004, 0, 'N', NULL, 'sdf', 'sdf', NULL, 'Y', '2024-09-25 16:33:05.24588', 1000069, '2024-09-25 16:33:05.24588', 1000069, 'a003d65d-9ea7-4f0d-9164-732e67ef93dc', NULL, NULL, NULL, NULL);


--
-- TOC entry 5459 (class 0 OID 393709)
-- Dependencies: 369
-- Data for Name: d_pay_method; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_pay_method VALUES (1000011, 1000001, 1000004, 1000020, 'MBB QRCODE', 'MBB_QRCODE', 0, 'N', '2024-09-27 11:46:44.630086', 1000066, '2024-09-27 11:46:44.630087', 1000066, 'eb2c3431-5208-44de-8240-251097373271', 'Y');
INSERT INTO pos.d_pay_method VALUES (1000012, 1000001, 1000004, 1000020, 'MBB QRCODE', 'MBB_QRCODE1', 0, 'N', '2024-09-27 11:48:51.290048', 1000066, '2024-09-27 11:48:51.290048', 1000066, '2e755fc3-8acd-4dfa-ace0-38049d0c6562', 'Y');




--
-- TOC entry 5472 (class 0 OID 394786)
-- Dependencies: 393
-- Data for Name: d_pos_org_access; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_pos_org_access VALUES (1000070, 1000016, 1000008, 1000004, '2024-09-18 06:35:14.96859', 0, '2024-09-18 06:35:14.96859', 0, 'Y', '19f8c93e-2e9b-4be5-9135-57a472a49a69');
INSERT INTO pos.d_pos_org_access VALUES (1000079, 1000019, 1000003, 1000004, '2024-09-18 08:31:56.599358', 0, '2024-09-18 08:31:56.599358', 0, 'Y', 'a76d8e5f-010f-4f05-9030-b885aea08488');
INSERT INTO pos.d_pos_org_access VALUES (1000079, 1000030, 1000004, 1000004, '2024-09-18 08:31:56.599358', 0, '2024-09-18 08:31:56.599358', 0, 'Y', 'abb94b05-bd36-4cce-b497-fb001b1fe51f');
INSERT INTO pos.d_pos_org_access VALUES (1000176, 1000038, 1000035, 1000004, '2024-09-25 23:20:24.86404', 1000069, '2024-09-25 23:20:24.86404', 1000069, 'Y', '4de65d99-eb1b-4528-a4e5-53a4cca70ba1');
INSERT INTO pos.d_pos_org_access VALUES (1000177, 1000038, 1000035, 1000004, '2024-09-25 23:20:25.141815', 1000069, '2024-09-25 23:20:25.141815', 1000069, 'Y', '8f550060-3c06-40a4-bcf5-89d80dc7ac42');
INSERT INTO pos.d_pos_org_access VALUES (1000177, 1000038, 1000031, 1000004, '2024-09-25 23:20:25.202251', 1000069, '2024-09-25 23:20:25.202251', 1000069, 'Y', 'bb791880-1215-4532-91fd-c3bee089eb8a');
INSERT INTO pos.d_pos_org_access VALUES (1000177, 1000033, 1000035, 1000004, '2024-09-25 23:20:25.33208', 1000069, '2024-09-25 23:20:25.33208', 1000069, 'Y', 'cfa7b9b5-4d49-4183-9089-022f7fb59b67');
INSERT INTO pos.d_pos_org_access VALUES (1000177, 1000033, 1000031, 1000004, '2024-09-25 23:20:25.395076', 1000069, '2024-09-25 23:20:25.395076', 1000069, 'Y', 'ccea8f40-d0bc-46a0-a756-a3c162cf8a55');
INSERT INTO pos.d_pos_org_access VALUES (1000179, 1000038, 1000035, 1000004, '2024-09-25 23:20:25.942681', 1000069, '2024-09-25 23:20:25.942681', 1000069, 'Y', 'e796a974-642c-462f-b851-e097f961b24b');
INSERT INTO pos.d_pos_org_access VALUES (1000179, 1000038, 1000029, 1000004, '2024-09-25 23:20:26.017689', 1000069, '2024-09-25 23:20:26.017689', 1000069, 'Y', 'c2a36380-1758-44d0-bbe8-a06ae9712a48');
INSERT INTO pos.d_pos_org_access VALUES (1000179, 1000038, 1000021, 1000004, '2024-09-25 23:20:26.082784', 1000069, '2024-09-25 23:20:26.082784', 1000069, 'Y', '6b80efa4-88aa-484f-81f9-5b8c1ee6cf46');
INSERT INTO pos.d_pos_org_access VALUES (1000181, 1000038, 1000035, 1000004, '2024-09-25 23:20:26.519374', 1000069, '2024-09-25 23:20:26.519374', 1000069, 'Y', '1dbee5c3-daf2-4488-8859-57d7a77b2d03');
INSERT INTO pos.d_pos_org_access VALUES (1000181, 1000038, 1000031, 1000004, '2024-09-25 23:20:26.584892', 1000069, '2024-09-25 23:20:26.584892', 1000069, 'Y', 'ede85a35-a10f-4b91-81aa-6f628b66a6df');
INSERT INTO pos.d_pos_org_access VALUES (1000181, 1000038, 1000034, 1000004, '2024-09-25 23:20:26.621906', 1000069, '2024-09-25 23:20:26.621906', 1000069, 'Y', '6b18003a-3fef-48e0-9d10-89cfd5e294e9');
INSERT INTO pos.d_pos_org_access VALUES (1000181, 1000038, 1000032, 1000004, '2024-09-25 23:20:26.671426', 1000069, '2024-09-25 23:20:26.671426', 1000069, 'Y', '122212c3-711d-40f9-b46c-f837fa33675c');
INSERT INTO pos.d_pos_org_access VALUES (1000181, 1000038, 1000028, 1000004, '2024-09-25 23:20:26.721439', 1000069, '2024-09-25 23:20:26.721439', 1000069, 'Y', 'b5f79fd3-636a-48f3-a3b8-82238c98be1d');
INSERT INTO pos.d_pos_org_access VALUES (1000181, 1000038, 1000036, 1000004, '2024-09-25 23:20:26.763965', 1000069, '2024-09-25 23:20:26.763965', 1000069, 'Y', '0383f617-8faf-4ecf-9d23-d4d07734dd4f');
INSERT INTO pos.d_pos_org_access VALUES (1000181, 1000033, 1000035, 1000004, '2024-09-25 23:20:26.86038', 1000069, '2024-09-25 23:20:26.86038', 1000069, 'Y', '485416ba-488c-4fbd-b6c3-13e3a954671a');
INSERT INTO pos.d_pos_org_access VALUES (1000181, 1000033, 1000029, 1000004, '2024-09-25 23:20:26.911394', 1000069, '2024-09-25 23:20:26.911394', 1000069, 'Y', 'b01c9237-fdb1-4640-b225-88eee059f26d');
INSERT INTO pos.d_pos_org_access VALUES (1000181, 1000033, 1000021, 1000004, '2024-09-25 23:20:27.009945', 1000069, '2024-09-25 23:20:27.009945', 1000069, 'Y', 'e2db5759-93e8-401a-9291-6e920a96d32b');
INSERT INTO pos.d_pos_org_access VALUES (1000185, 1000038, 1000035, 1000004, '2024-09-25 23:20:28.492649', 1000069, '2024-09-25 23:20:28.492649', 1000069, 'Y', 'ad4b00ea-c68b-4ab5-894d-8200433eb7d4');





--
-- TOC entry 5429 (class 0 OID 392879)
-- Dependencies: 335
-- Data for Name: d_pos_terminal; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_pos_terminal VALUES (1000021, 1000004, 1000038, 'POS06', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '', 0, 'N', '', 0, 1000006, '2024-09-21 16:21:14.063139', 1000069, '2024-09-21 16:27:18.297859', 1000069, 'c9d417d0-9ea7-4503-9d99-d12797556399', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000034, 1000004, 1000038, 'POS-HOL', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '', 0, 'N', '', 0, 1000026, '2024-09-21 16:21:22.301878', 1000069, '2024-09-21 16:27:18.657028', 1000069, '0f6fb719-e2c8-4148-9439-b6b97f39c89c', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000018, 1000004, 1000038, 'POS20', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '', 0, 'N', '', 0, 1000020, '2024-09-21 16:21:10.79924', 1000069, '2024-09-21 16:27:18.192631', 1000069, '2ba2dc5a-62f9-44f7-88ef-d0c06acd3a5b', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000020, 1000004, 1000038, 'POS05', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '', 0, 'N', '', 0, 1000005, '2024-09-21 16:21:13.355964', 1000069, '2024-09-21 16:27:18.273255', 1000069, '52d2b3f8-f32e-4636-9761-5f74dfba39f3', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000019, 1000004, 1000038, 'POS21', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '', 0, 'N', '', 0, 1000021, '2024-09-21 16:21:12.31381', 1000069, '2024-09-21 16:27:18.231625', 1000069, 'e720b543-f589-47fe-90b6-5956d9c41196', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000028, 1000004, 1000038, 'POS-TEST SYNC DATA THEO DB', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '1', 0, 'N', '', 0, 1000027, '2024-09-21 16:21:22.128118', 1000069, '2024-09-21 16:27:18.493307', 1000069, '6cc6a5ef-9612-4852-ae99-17f36f0a9fa5', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000026, 1000004, 1000038, 'POS04', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '', 0, 'N', '', 0, 1000004, '2024-09-21 16:21:19.959844', 1000069, '2024-09-21 16:27:18.43854', 1000069, 'f5ebd31c-1ec5-4cf3-b150-63a8291e7304', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000008, 1000004, 1000016, 'Điểm bán 2', NULL, 1000070, 1000005, NULL, 1000013, 1000006, 1000006, '123', 1234, 'N', 'Y', 1000001, NULL, '2024-09-17 09:34:59.725182', 1000069, '2024-09-27 09:31:58.461687', 1000066, '970f526f-0659-4d8a-bc1b-cde392c4e8e5', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000024, 1000004, 1000038, 'POS-OFFB', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '', 0, 'N', '', 0, 1000008, '2024-09-21 16:21:15.73643', 1000069, '2024-09-21 16:27:18.38262', 1000069, '59d733dd-22ec-4ddb-8802-ac21a7dbf8d9', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000025, 1000004, 1000038, 'POS17', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '', 0, 'N', '', 0, 1000017, '2024-09-21 16:21:16.45606', 1000069, '2024-09-21 16:27:18.40854', 1000069, 'b85fa66b-ea2f-448b-856c-31fdd352cc3c', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000010, 1000004, 1000016, 'Điểm bán 1', NULL, 1000069, 1000005, NULL, 1000013, 1000006, 1000005, '23232', 33232, 'Y', 'Y', 1000001, NULL, '2024-09-18 10:45:21.538225', 1000069, '2024-09-18 10:45:21.53823', 1000069, '1a4e580d-d688-4549-b7ee-2256c4ddd1bf', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000027, 1000004, 1000038, 'POS18', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '', 0, 'N', '', 0, 1000018, '2024-09-21 16:21:22.093765', 1000069, '2024-09-21 16:27:18.468629', 1000069, 'f7ac341d-b954-4798-9f3d-8cabc327e585', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000031, 1000004, 1000038, 'POS03', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '123.123.123.123', 0, 'N', '', 0, 1000003, '2024-09-21 16:21:22.215796', 1000069, '2024-09-21 16:27:18.579964', 1000069, 'b33aa234-3915-438e-bad5-5a10ac162915', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000029, 1000004, 1000038, 'POS02', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '', 0, 'N', '', 0, 1000002, '2024-09-21 16:21:22.160799', 1000069, '2024-09-21 16:27:18.520265', 1000069, '034c368d-dd80-4b4f-9c55-4dcfead2ae16', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000004, 1000004, 1000016, 'Main POS Terminal', 'POS terminal located at the main counter', 1234, 1000005, 'Y', 1000013, 1000006, 1000005, '192.168.1.100', 9100, 'N', 'Y', 1617, 1819, '2024-09-13 11:29:23.73611', 1000069, '2024-09-13 11:29:23.736118', 1000069, '29489941-1281-40fa-9242-23f3475ec067', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000005, 1000009, 1000016, 'Main POS Terminal', 'POS terminal located at the main counter', 1234, 1000005, 'Y', 1000013, 1000006, 1000005, '192.168.1.100', 9100, 'N', 'Y', 1617, 1819, '2024-09-16 10:06:16.202556', 1000071, '2024-09-16 10:06:16.202556', 1000071, '98484925-9192-4c2f-a334-f7e872c0cfd2', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000003, 1000004, 1000016, 'Main POS Terminal', 'POS terminal located at the main counter', NULL, 1000005, 'Y', 1000013, 1000006, 1000005, '192.168.1.100', 9100, 'N', 'Y', 2000000, 1819, '2024-09-06 16:54:03.361169', 1000069, '2024-09-06 16:54:03.361169', 1000069, '6fb40192-0be6-4878-9551-34d77f8ad0b1', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000036, 1000004, 1000038, 'POS ROOM SERVICE', NULL, 1000178, 1000001, 'N', 1000013, 1000006, 1000001, '1222224442', 0, 'Y', 'N', 1000001, 1000029, '2024-09-21 16:21:22.352776', 1000069, '2024-09-27 12:02:52.189517', 1000066, '79660eb6-35c2-4cd2-8c88-c4a59482307c', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000023, 1000004, 1000038, 'POS09', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '', 0, 'N', '', 0, 1000009, '2024-09-21 16:21:15.198536', 1000069, '2024-09-21 16:27:18.350828', 1000069, 'e83b821c-c83b-4a4d-a0ae-e9451380c5ab', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000009, 1000004, 1000016, 'Điểm bán 31', NULL, 1000070, 1000005, NULL, 1000013, 1000006, 1000005, '123', 123, 'Y', 'Y', 1000001, NULL, '2024-09-17 14:10:35.853632', 1000069, '2024-09-17 14:19:59.019964', 1000069, '5d92c723-f2f9-4922-94e8-0d62be08c874', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000032, 1000004, 1000038, 'POS-RES', NULL, NULL, 1000005, 'Y', 1000013, 1000006, 1000005, '192.168.110.94', 0, 'Y', '', 0, 1000025, '2024-09-21 16:21:22.243877', 1000069, '2024-09-21 16:27:18.604023', 1000069, '30713781-ecd9-428a-8937-fbbc9542ab4a', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000035, 1000004, 1000038, 'POS THE RICE', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '192.168.102.10', 0, 'N', '', 0, 1000001, '2024-09-21 16:21:22.32688', 1000069, '2024-09-21 16:27:18.684774', 1000069, 'c3df7949-59e2-4962-8e00-9cc4f5fa79d8', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000022, 1000004, 1000038, 'POS07', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '', 0, 'N', '', 0, 1000007, '2024-09-21 16:21:14.584251', 1000069, '2024-09-21 16:27:18.324864', 1000069, 'e342a2aa-7d4f-490e-a3b6-a7f056beb0df', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000033, 1000004, 1000037, 'ANPASO', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '192.168.1.1', 0, 'N', '', 0, 1000032, '2024-09-21 16:21:22.273875', 1000069, '2024-09-21 16:27:18.631021', 1000069, '7f3de344-5d08-49e6-8fc8-315358135694', 'Y', 1000002);
INSERT INTO pos.d_pos_terminal VALUES (1000030, 1000004, 1000038, 'POS17', NULL, NULL, 1000005, 'N', 1000013, 1000006, 1000005, '111', 0, 'N', '', 0, 1000028, '2024-09-21 16:21:22.1898', 1000069, '2024-09-21 16:27:18.553267', 1000069, '492c0c41-5168-4d14-8b9b-06b4a985ba19', 'Y', 1000002);


--
-- TOC entry 5403 (class 0 OID 388752)
-- Dependencies: 309
-- Data for Name: d_pricelist; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_pricelist VALUES (1000059, 1000004, 0, 'tét', 'Y', '2024-09-19 00:00:00', '2024-09-25 00:00:00', 'Y', '2024-09-18 15:18:46.615725', 1000069, '2024-09-18 15:18:46.615726', 1000069, '37026caf-2289-4447-87e6-9a5bea37c36c', 'N');
INSERT INTO pos.d_pricelist VALUES (1000058, 1000004, 0, 'new 32', 'N', '2024-09-20 00:00:00', '2024-09-21 00:00:00', 'Y', '2024-09-17 16:39:43.170666', 1000069, '2024-09-18 15:35:53.807296', 1000069, '3b251ba8-6559-4dd1-b0fa-a29a5f4a156b', 'N');
INSERT INTO pos.d_pricelist VALUES (1000013, 1000004, 0, 'Bảng giá chung', 'N', '2024-09-02 00:00:00', '2024-09-30 00:00:00', 'Y', '2024-08-29 10:18:38.172948', 0, '2024-09-18 11:29:07.700524', 1000069, '239baae8-a2ee-4427-8590-076b40bb641c', 'Y');
INSERT INTO pos.d_pricelist VALUES (1000045, 1000004, 0, 'Bảng giá 05 - 09 - 2024', 'Y', '2024-09-05 00:00:00', '2024-09-30 00:00:00', 'Y', '2024-09-05 06:36:24.562672', 1000069, '2024-09-05 06:36:24.562672', 1000069, '85023acd-c7a7-4ffd-aca3-1bf898175c21', 'N');
INSERT INTO pos.d_pricelist VALUES (1000046, 1000004, 0, 'Bảng giá 01 - 09 - 2024', 'Y', '2024-09-01 00:00:00', '2024-09-30 00:00:00', 'Y', '2024-09-05 06:37:34.070505', 1000069, '2024-09-05 06:37:34.070505', 1000069, 'b0dd85c9-71c2-4cd9-ac6b-d247480b3d66', 'N');
INSERT INTO pos.d_pricelist VALUES (1000047, 1000004, 0, 'Bảng giá 02 - 09 - 2024', 'Y', '2024-09-02 00:00:00', '2024-09-30 00:00:00', 'Y', '2024-09-05 06:37:39.198124', 1000069, '2024-09-05 06:37:39.198124', 1000069, 'c70b3f4c-9e4d-4edf-8b6f-2a938e0eedc7', 'N');
INSERT INTO pos.d_pricelist VALUES (1000048, 1000004, 0, 'a', 'Y', '2024-09-06 00:00:00', '2024-09-10 00:00:00', 'Y', '2024-09-06 15:41:48.564904', 1000069, '2024-09-06 15:41:48.564907', 1000069, '8a37aed4-881b-45f7-b47f-89b3ca1d84fe', 'N');
INSERT INTO pos.d_pricelist VALUES (1000049, 1000009, 0, 'Bảng giá chung', 'Y', '2024-07-11 15:56:06', '2024-09-11 15:56:13', 'Y', '2024-09-11 08:51:29.979942', 0, '2024-09-11 08:51:29.979942', 0, '93202a8b-2c26-4c18-8d04-71ee24a028d4', 'Y');
INSERT INTO pos.d_pricelist VALUES (1000050, 1000004, 0, 'tét', 'Y', '2024-09-13 00:00:00', '2024-09-14 00:00:00', 'Y', '2024-09-12 16:23:13.345153', 1000069, '2024-09-12 16:23:13.345153', 1000069, 'e0eae533-32ea-4015-ae21-544ebf4f52f3', 'N');
INSERT INTO pos.d_pricelist VALUES (1000051, 1000004, 0, 'tét', 'Y', '2024-09-13 00:00:00', '2024-09-17 00:00:00', 'Y', '2024-09-12 18:11:15.143868', 1000069, '2024-09-12 18:11:15.143868', 1000069, 'd02c1eda-eb5b-4861-9b0a-631041611689', 'N');
INSERT INTO pos.d_pricelist VALUES (1000056, 1000004, 0, 'new', 'Y', '2024-09-17 00:00:00', '2024-09-19 00:00:00', 'Y', '2024-09-17 16:25:39.714919', 1000069, '2024-09-17 16:25:39.71492', 1000069, '81c7157f-e737-4043-b888-a09fd8b4fa9e', 'N');
INSERT INTO pos.d_pricelist VALUES (1000057, 1000004, 0, 'new 2', 'Y', '2024-09-17 00:00:00', '2024-09-19 00:00:00', 'Y', '2024-09-17 16:34:02.428085', 1000069, '2024-09-17 16:34:02.428087', 1000069, '8d1b6379-c408-470a-b038-8426a9b07ce5', 'N');


--
-- TOC entry 5407 (class 0 OID 388792)
-- Dependencies: 313
-- Data for Name: d_pricelist_org; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_pricelist_org VALUES (1000047, 1000045, 1000004, 1000016, NULL, 'Y', '2024-09-05 06:36:24.574239', 1000069, '2024-09-05 06:36:24.574239', 1000069, '1b32f820-5464-4653-baa0-b4bd26fc43bf');
INSERT INTO pos.d_pricelist_org VALUES (1000048, 1000046, 1000004, 1000016, NULL, 'Y', '2024-09-05 06:37:34.077537', 1000069, '2024-09-05 06:37:34.077537', 1000069, '7dbd1603-4c55-4ae2-87b6-a5402d2ad5af');
INSERT INTO pos.d_pricelist_org VALUES (1000050, 1000047, 1000004, 1000016, NULL, 'Y', '2024-09-05 06:45:17.642755', 1000069, '2024-09-05 06:45:17.642755', 1000069, 'caeefc74-a5f1-4ac5-b95e-1677790c42f2');
INSERT INTO pos.d_pricelist_org VALUES (1000051, 1000047, 1000004, 1000019, NULL, 'Y', '2024-09-05 06:45:17.64843', 1000069, '2024-09-05 06:45:17.64843', 1000069, '4510cf4b-1184-4aea-842e-eed6b60c7bc1');
INSERT INTO pos.d_pricelist_org VALUES (1000052, 1000047, 1000004, 1000020, NULL, 'Y', '2024-09-05 06:45:17.655466', 1000069, '2024-09-05 06:45:17.655466', 1000069, '5b549689-1224-4815-9902-720be19c099d');
INSERT INTO pos.d_pricelist_org VALUES (1000053, 1000047, 1000004, 1000021, NULL, 'Y', '2024-09-05 06:45:17.660851', 1000069, '2024-09-05 06:45:17.660851', 1000069, 'f4db3067-d60a-48ea-b8d5-45b6cd801c38');
INSERT INTO pos.d_pricelist_org VALUES (1000058, 1000013, 1000004, 1000016, NULL, 'Y', '2024-09-18 11:29:07.645392', 1000069, '2024-09-18 11:29:07.645392', 1000069, '328ef8c1-ab5b-4f61-bdfe-3c076570eef5');
INSERT INTO pos.d_pricelist_org VALUES (1000059, 1000013, 1000004, 1000019, NULL, 'Y', '2024-09-18 11:29:07.657852', 1000069, '2024-09-18 11:29:07.657852', 1000069, 'bc21f43e-7899-48f9-9f51-7d1c2aa00943');
INSERT INTO pos.d_pricelist_org VALUES (1000060, 1000013, 1000004, 1000021, NULL, 'Y', '2024-09-18 11:29:07.669152', 1000069, '2024-09-18 11:29:07.669153', 1000069, '6823a11c-a6c3-47ac-a27d-4b6cdb3b9c2c');
INSERT INTO pos.d_pricelist_org VALUES (1000061, 1000013, 1000004, 1000020, NULL, 'Y', '2024-09-18 11:29:07.673999', 1000069, '2024-09-18 11:29:07.674', 1000069, 'b6077659-543d-450b-8a2e-4271dccbd993');


--
-- TOC entry 5405 (class 0 OID 388771)
-- Dependencies: 311
-- Data for Name: d_pricelist_product; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_pricelist_product VALUES (1000260, 1000004, 0, 1000643, 'Y', 100000, NULL, 200000, 0, '2024-09-18 23:24:42.594357', 1000069, '2024-09-18 23:24:42.594359', 1000069, '3a773cd1-7a42-438a-8e26-bfed92a42f2c', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000261, 1000004, 0, 1000644, 'Y', 15000, NULL, 20000, 0, '2024-09-18 23:36:56.493106', 1000069, '2024-09-18 23:36:56.493108', 1000069, '925d0939-83b1-4dac-ba23-6cbc0dbbb207', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000228, 1000004, 0, 1000506, 'Y', 10000, 20000, 16000, 0, '2024-09-18 13:49:33.461165', 1000069, '2024-09-18 14:53:04.078362', 1000069, 'e7fb4cbf-1d78-45b9-827d-9f38f7749466', 1000050);
INSERT INTO pos.d_pricelist_product VALUES (1000229, 1000004, 0, 1000353, 'Y', 10000, 15000, 12000.0, 0, '2024-09-18 13:49:45.784023', 1000069, '2024-09-18 14:53:15.239948', 1000069, 'acb89082-4eeb-4683-a575-9dfa9eb8270f', 1000050);
INSERT INTO pos.d_pricelist_product VALUES (1000233, 1000004, 0, 1000503, 'Y', 10000, 20000, 16000.0, 0, '2024-09-18 14:43:18.847201', 1000069, '2024-09-18 14:53:15.240016', 1000069, 'e53ebf57-8a4c-4d4e-b051-f4df12f65dbf', 1000050);
INSERT INTO pos.d_pricelist_product VALUES (1000236, 1000004, 0, 1000506, 'Y', 10000, 20000, 20000, 0, '2024-09-18 15:32:12.954268', 1000069, '2024-09-18 15:32:12.954268', 1000069, '39dfa94e-c308-4564-961c-415a6c44ec3f', 1000059);
INSERT INTO pos.d_pricelist_product VALUES (1000237, 1000004, 0, 1000503, 'Y', 10000, 20000, 20000, 0, '2024-09-18 15:32:16.363608', 1000069, '2024-09-18 15:32:16.363608', 1000069, 'd2bec82c-f3f8-4bbf-badc-8dad689af0d4', 1000059);
INSERT INTO pos.d_pricelist_product VALUES (1000238, 1000004, 0, 1000621, 'Y', 10000, NULL, 15000, 0, '2024-09-18 21:18:35.27178', 1000069, '2024-09-18 21:18:35.27178', 1000069, '1c52a135-914a-41bd-b89d-0933450f37d4', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000239, 1000004, 0, 1000622, 'Y', 10000, NULL, 15000, 0, '2024-09-18 21:18:35.452139', 1000069, '2024-09-18 21:18:35.452139', 1000069, '53f66efa-cd43-4682-9726-7ecb0831b89d', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000240, 1000004, 0, 1000623, 'Y', 10000, NULL, 15000, 0, '2024-09-18 21:18:35.516557', 1000069, '2024-09-18 21:18:35.516557', 1000069, '6e9c186a-0b1a-40f1-9cf5-ce1de2892546', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000242, 1000004, 0, 1000625, 'Y', 10000, NULL, 15000, 0, '2024-09-18 21:53:32.356455', 1000069, '2024-09-18 21:53:32.356458', 1000069, 'f49fe7cd-328f-4c9a-8d8b-38846a70ac5a', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000243, 1000004, 0, 1000626, 'Y', 10000, NULL, 15000, 0, '2024-09-18 21:53:32.428593', 1000069, '2024-09-18 21:53:32.428607', 1000069, 'a87f6da2-fa44-43e0-a2f1-c45356a5f85e', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000244, 1000004, 0, 1000627, 'Y', 10000, NULL, 15000, 0, '2024-09-18 21:53:32.483008', 1000069, '2024-09-18 21:53:32.48301', 1000069, '8299e873-321b-498b-9b08-80d924bada17', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000278, 1000004, 0, 1000661, 'Y', 10000, NULL, 15000, 0, '2024-09-19 11:20:59.537595', 1000069, '2024-09-19 11:20:59.537597', 1000069, '6848c70c-b8a8-427d-8c19-69186da40e2e', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000279, 1000004, 0, 1000662, 'Y', 10000, NULL, 15000, 0, '2024-09-19 11:20:59.608404', 1000069, '2024-09-19 11:20:59.608406', 1000069, '03935871-e4ce-4687-85e0-d0e61476133f', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000280, 1000004, 0, 1000663, 'Y', 10000, NULL, 15000, 0, '2024-09-19 11:20:59.655398', 1000069, '2024-09-19 11:20:59.6554', 1000069, 'afd8c258-96ed-4873-af6c-e49b7b84cfe1', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000287, 1000004, 0, 1000670, 'Y', 20000, NULL, 30000, 0, '2024-09-19 11:27:15.495905', 1000069, '2024-09-19 11:27:15.495907', 1000069, '6c4f74ba-207f-4997-ab8f-730696717a31', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000110, 1000004, 0, 1000383, 'Y', 100000, NULL, 120000, 0, '2024-09-04 09:23:01.453249', 1000069, '2024-09-04 11:48:20.224823', 1000069, 'b453e5ca-1611-4ab1-aa0a-5fcd82af20a4', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000074, 1000004, 0, 1000111, 'Y', 50, NULL, 20050, 0, '2024-08-30 08:36:41.829773', 0, '2024-09-04 11:48:20.225425', 1000069, 'f1b017db-7d0c-48f5-990d-97da0811f4bd', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000075, 1000004, 0, 1000138, 'Y', 50, NULL, 20050, 0, '2024-08-30 08:36:41.900032', 0, '2024-09-04 11:48:20.225519', 1000069, '1991dda1-8f83-42d1-b9ca-5bb70e8be0ad', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000073, 1000004, 0, 1000140, 'Y', 50, NULL, 20050, 0, '2024-08-30 08:36:41.758959', 0, '2024-09-04 11:48:20.225565', 1000069, '1568a56b-e942-40ac-b121-b858cb96e28f', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000089, 1000004, 0, 1000136, 'Y', 50, NULL, 20050, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.225621', 1000069, 'b6f8b969-1d66-4e2b-a427-42ed8bd02a77', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000090, 1000004, 0, 1000127, 'Y', 50, NULL, 20050, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.225655', 1000069, 'cf536b9a-8e04-446a-b5bf-5173413c48b0', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000091, 1000004, 0, 1000129, 'Y', 50, NULL, 20050, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.225687', 1000069, 'f7c176cf-4117-4d69-bd35-95c33026af6b', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000092, 1000004, 0, 1000130, 'Y', 50, NULL, 20050, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.22572', 1000069, '3be53fc2-9f0f-4e4f-9f44-5b9c137dfa36', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000093, 1000004, 0, 1000132, 'Y', 50, NULL, 20050, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.225775', 1000069, 'd6aa8435-e13c-4db2-b756-6366d1374451', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000094, 1000004, 0, 1000134, 'Y', 50, NULL, 20050, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.225876', 1000069, 'fd494918-23a6-4f9d-8d98-fe4cd10c86c0', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000095, 1000004, 0, 1000103, 'Y', 50000, NULL, 70000, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.225909', 1000069, 'd50e3d5f-7fc5-44c5-a059-08fcfe6c2dd8', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000096, 1000004, 0, 1000119, 'Y', 50, NULL, 20050, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.22594', 1000069, 'e42b0ef0-50cb-4b08-90a3-70f298dc60f2', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000097, 1000004, 0, 1000113, 'Y', 50, NULL, 20050, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.225974', 1000069, '20c8c1b8-f52c-4138-81e1-70250152b3dc', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000098, 1000004, 0, 1000108, 'Y', 50, NULL, 20050, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.226005', 1000069, 'f20eae45-70e4-4b8b-9c35-f05a2d57d6c1', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000099, 1000004, 0, 1000106, 'Y', 50, NULL, 20050, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.226035', 1000069, '66b6b954-0ac5-4ce4-9d74-72b0ba5a1caa', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000080, 1000004, 0, 1000356, 'Y', 10000, 15000, 15000, 0, '2024-08-30 08:44:10.373867', 1000069, '2024-08-30 08:44:10.373867', 1000069, 'e5c109cc-b97e-4655-ac6c-3f0bcc662825', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000079, 1000004, 0, 1000353, 'Y', 10000, 15000, 15000, 0, '2024-08-30 08:44:10.329457', 1000069, '2024-08-30 08:44:10.329457', 1000069, 'e852f3f0-e492-4226-b500-491ec82c0892', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000081, 1000004, 0, 1000359, 'Y', 10000, 15000, 15000, 0, '2024-08-30 08:44:10.397987', 1000069, '2024-08-30 08:44:10.397987', 1000069, 'afe236f9-cfea-48db-a324-451d765c1f9b', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000100, 1000004, 0, 1000112, 'Y', 50, NULL, 20050, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.226597', 1000069, '31f5f22e-ce1a-4ecc-9fdd-a0a830fd2f97', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000101, 1000004, 0, 1000107, 'Y', 50, NULL, 20050, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.226671', 1000069, 'affd1997-4575-47c2-b116-ba9eb05c55ae', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000102, 1000004, 0, 1000105, 'Y', 50, NULL, 20050, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.226731', 1000069, 'f193318b-df55-43bc-9d45-96f4629c62ff', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000104, 1000004, 0, 1000109, 'Y', 50, NULL, 20050, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.226789', 1000069, '38a4b557-0c3d-4e91-b376-5c6c5691755a', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000108, 1000004, 0, 1000377, 'Y', 100000, NULL, 120000, 0, '2024-09-03 22:22:20.643603', 1000069, '2024-09-04 11:48:20.226815', 1000069, '9796c2c9-c26f-424e-8acf-781775133f27', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000109, 1000004, 0, 1000380, 'Y', 100000, NULL, 120000, 0, '2024-09-03 22:22:20.763773', 1000069, '2024-09-04 11:48:20.226842', 1000069, '704ece79-1f52-446a-a6f0-981f55c4c972', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000111, 1000004, 0, 1000386, 'Y', 100000, NULL, 120000, 0, '2024-09-04 09:23:01.506634', 1000069, '2024-09-04 11:48:20.226868', 1000069, 'bffe547a-c3f4-4129-a96f-56697010a12f', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000112, 1000004, 0, 1000389, 'Y', 100000, NULL, 120000, 0, '2024-09-04 09:23:01.51609', 1000069, '2024-09-04 11:48:20.226897', 1000069, '2110c633-f900-4abc-b5c5-df60481401d6', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000113, 1000004, 0, 1000392, 'Y', 100000, NULL, 120000, 0, '2024-09-04 09:47:43.911829', 1000069, '2024-09-04 11:48:20.226927', 1000069, 'ca0043fa-742a-43ae-8261-e5b94e2e1a79', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000114, 1000004, 0, 1000395, 'Y', 100000, NULL, 120000, 0, '2024-09-04 09:47:43.969142', 1000069, '2024-09-04 11:48:20.226967', 1000069, 'e8d7cb08-4b3d-4acf-8622-d25f15c7542c', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000115, 1000004, 0, 1000398, 'Y', 100000, NULL, 120000, 0, '2024-09-04 09:47:43.981981', 1000069, '2024-09-04 11:48:20.226993', 1000069, '7bd2955d-95da-4817-aa74-fb1c0ec86445', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000116, 1000004, 0, 1000401, 'Y', 10000, NULL, 30000, 0, '2024-09-04 09:49:27.132154', 1000069, '2024-09-04 11:48:20.227018', 1000069, '155e74ca-69c3-4292-94ae-93de08b6ac45', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000117, 1000004, 0, 1000404, 'Y', 10000, NULL, 30000, 0, '2024-09-04 09:49:27.14401', 1000069, '2024-09-04 11:48:20.227042', 1000069, '58466a3e-ba54-4918-a580-7f4a89df2458', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000118, 1000004, 0, 1000139, 'Y', 10000, NULL, 30000, 0, '2024-09-04 04:38:56.859959', 1000069, '2024-09-04 11:48:20.227066', 1000069, '9225e8a3-05ca-4880-a7e2-6358704c7d96', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000119, 1000004, 0, 1000368, 'Y', 10000, NULL, 30000, 0, '2024-09-04 04:38:56.859959', 1000069, '2024-09-04 11:48:20.22709', 1000069, '1aabd71b-73b7-4f15-bcd0-7ac192f3bd4c', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000120, 1000004, 0, 1000371, 'Y', 10000, NULL, 30000, 0, '2024-09-04 04:38:56.859959', 1000069, '2024-09-04 11:48:20.227143', 1000069, 'a04e1e81-2587-4075-a598-e581f116d085', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000121, 1000004, 0, 1000374, 'Y', 10000, NULL, 30000, 0, '2024-09-04 04:38:56.859959', 1000069, '2024-09-04 11:48:20.227226', 1000069, '63f34120-424a-4c0c-bf54-9c52770bab7d', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000174, 1000009, 0, 1000536, 'Y', 20000, NULL, 43000, 0, '2024-09-11 16:32:36.65858', 1000071, '2024-09-11 16:32:36.658582', 1000071, 'd4ae90a0-c32e-4dd7-be11-e6239591f97e', 1000049);
INSERT INTO pos.d_pricelist_product VALUES (1000190, 1000009, 0, 1000565, 'Y', 30000, NULL, 45000, 0, '2024-09-11 17:59:37.360786', 1000071, '2024-09-11 17:59:37.360786', 1000071, '446f0a40-963e-4987-8898-af8cf26ca289', 1000049);
INSERT INTO pos.d_pricelist_product VALUES (1000191, 1000009, 0, 1000568, 'Y', 40000, NULL, 120000, 0, '2024-09-11 18:10:24.900583', 1000071, '2024-09-11 18:10:24.900584', 1000071, 'b418ddfc-33d0-41e3-b372-af4b80fcda82', 1000049);
INSERT INTO pos.d_pricelist_product VALUES (1000215, 1000004, 0, 1000562, 'Y', 40000, NULL, 56000, NULL, '2024-09-17 09:40:22.923173', 0, '2024-09-17 09:40:22.923173', 0, '4f737e14-8057-4430-b0e7-315573a5d304', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000216, 1000004, 0, 1000608, 'Y', 50, NULL, 100, NULL, '2024-09-17 09:40:22.923173', 0, '2024-09-17 09:40:22.923173', 0, 'eb5ba3ae-2399-463f-adc4-e03c28a789a7', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000217, 1000004, 0, 1000571, 'Y', 40000, NULL, 11000, NULL, '2024-09-17 09:40:22.923173', 0, '2024-09-17 09:40:22.923173', 0, '19366006-bc5e-4bb6-8b46-99e5bc0600c5', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000103, 1000004, 0, 1000104, 'Y', 60, NULL, 20051, 0, '2024-08-30 03:06:22.476435', 0, '2024-09-04 11:48:20.226761', 1000069, 'eaf19392-7c12-4bf2-a9d6-bf01a8601d11', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000218, 1000004, 0, 1000577, 'Y', 45000, NULL, 110000, NULL, '2024-09-17 09:40:22.923173', 0, '2024-09-17 09:40:22.923173', 0, '7924b02e-d525-43b2-b097-1a4d8f6940eb', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000219, 1000004, 0, 1000574, 'Y', 32000, NULL, 123000, NULL, '2024-09-17 09:40:22.923173', 0, '2024-09-17 09:40:22.923173', 0, 'fdb7fb01-080a-45d2-8a1e-09f9dc5b9cff', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000134, 1000004, 0, 1000407, 'Y', 100000, NULL, 200000, 0, '2024-09-04 17:35:06.996798', 1000069, '2024-09-04 17:35:06.996799', 1000069, '5682806c-933c-4f81-b0cf-bd8354537bc0', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000135, 1000004, 0, 1000410, 'Y', 100000, NULL, 200000, 0, '2024-09-04 17:35:07.158645', 1000069, '2024-09-04 17:35:07.158646', 1000069, '437789e7-4931-4389-b0a1-72654e93ba08', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000136, 1000004, 0, 1000413, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.65179', 1000069, '2024-09-05 09:22:46.65179', 1000069, 'a8cf774a-fe14-48a7-bebe-b002d7fbdb81', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000137, 1000004, 0, 1000416, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.715228', 1000069, '2024-09-05 09:22:46.715228', 1000069, '3b339e69-af95-46d4-8cef-e118f28ee898', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000138, 1000004, 0, 1000419, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.725368', 1000069, '2024-09-05 09:22:46.725368', 1000069, 'a686e724-5216-4250-8f52-ba8713647913', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000139, 1000004, 0, 1000422, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.735774', 1000069, '2024-09-05 09:22:46.735775', 1000069, 'c5ac67b0-cdcd-4fda-9004-bb2c7ecca031', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000140, 1000004, 0, 1000425, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.760431', 1000069, '2024-09-05 09:22:46.760432', 1000069, '127fa19f-028a-4e56-a5fb-5b43a88cda09', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000141, 1000004, 0, 1000428, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.779993', 1000069, '2024-09-05 09:22:46.779993', 1000069, 'b7185e2b-8112-4d49-a9ac-13496411a6bf', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000142, 1000004, 0, 1000431, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.791324', 1000069, '2024-09-05 09:22:46.791325', 1000069, 'cde5dbc1-e014-4204-ad57-00e43a69cebd', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000143, 1000004, 0, 1000434, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.801089', 1000069, '2024-09-05 09:22:46.80109', 1000069, '682d216a-5f4c-49a6-a473-ff06d2e9f31f', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000144, 1000004, 0, 1000437, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.810712', 1000069, '2024-09-05 09:22:46.810712', 1000069, '2ee858be-4c24-4567-a18f-2c770733c033', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000145, 1000004, 0, 1000440, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.819886', 1000069, '2024-09-05 09:22:46.819887', 1000069, '6323f0a7-ebb1-4140-a08d-5e0a86c9fdb4', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000146, 1000004, 0, 1000443, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.827712', 1000069, '2024-09-05 09:22:46.827712', 1000069, 'e2ba0a20-2aae-459a-adab-d16e8f221de4', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000147, 1000004, 0, 1000446, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.835164', 1000069, '2024-09-05 09:22:46.835164', 1000069, '0bac7012-9522-4000-840b-4c7e9dded205', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000148, 1000004, 0, 1000449, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.844356', 1000069, '2024-09-05 09:22:46.844356', 1000069, '103f7df8-093c-4970-82e0-e15d6aa57248', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000149, 1000004, 0, 1000452, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.856741', 1000069, '2024-09-05 09:22:46.856741', 1000069, 'ccdbfe42-0c00-44a5-82a8-5525ef826c1b', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000150, 1000004, 0, 1000455, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.872568', 1000069, '2024-09-05 09:22:46.872568', 1000069, 'f0ab4baf-11d7-4aa1-abb2-7db4ffc35e7f', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000151, 1000004, 0, 1000458, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.896517', 1000069, '2024-09-05 09:22:46.896517', 1000069, 'f8cdcafb-d6f4-477b-a39e-f8a73d59b275', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000220, 1000004, 0, 1000536, 'Y', 20000, NULL, 43000, NULL, '2024-09-17 09:40:22.923173', 0, '2024-09-17 09:40:22.923173', 0, '83d4daf2-7dfe-4a63-8c7c-7b9456a66ee8', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000152, 1000004, 0, 1000461, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.910889', 1000069, '2024-09-05 09:22:46.910889', 1000069, '643da615-3486-417f-89b5-0196ed38f03c', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000153, 1000004, 0, 1000464, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.922729', 1000069, '2024-09-05 09:22:46.922729', 1000069, '5792189d-1e19-4e1e-b0b6-107c93df8be0', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000154, 1000004, 0, 1000467, 'Y', 100000, NULL, 200000, 0, '2024-09-05 09:22:46.933816', 1000069, '2024-09-05 09:22:46.933816', 1000069, 'be4fd61a-5d89-48d2-b8d8-6a8d481fab36', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000155, 1000004, 0, 1000470, 'Y', 100000, NULL, 189000, 0, '2024-09-05 09:29:12.106197', 1000069, '2024-09-05 09:29:12.106197', 1000069, 'f89da9c0-3025-4ef3-997d-05b42a9748a4', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000156, 1000004, 0, 1000473, 'Y', 100000, NULL, 189000, 0, '2024-09-05 09:29:12.20308', 1000069, '2024-09-05 09:29:12.20308', 1000069, '29d208c9-4509-4b68-8ebd-2842fe36c728', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000157, 1000004, 0, 1000476, 'Y', 100000, NULL, 189000, 0, '2024-09-05 09:29:12.232386', 1000069, '2024-09-05 09:29:12.232386', 1000069, '6e7b8f6b-9315-4702-aa42-85ff9ef826fc', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000158, 1000004, 0, 1000479, 'Y', 100000, NULL, 189000, 0, '2024-09-05 09:29:12.250635', 1000069, '2024-09-05 09:29:12.250636', 1000069, '89087665-9267-45d6-ade5-d865eab8b998', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000159, 1000004, 0, 1000482, 'Y', 100000, NULL, 189000, 0, '2024-09-05 09:29:12.267289', 1000069, '2024-09-05 09:29:12.26729', 1000069, 'ad13ddea-716d-416e-b157-de28df6549f2', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000160, 1000004, 0, 1000485, 'Y', 100000, NULL, 189000, 0, '2024-09-05 09:29:12.279467', 1000069, '2024-09-05 09:29:12.279467', 1000069, '7859e1bd-5ed0-42aa-abbc-0952f723a0e1', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000161, 1000004, 0, 1000488, 'Y', 100000, NULL, 189000, 0, '2024-09-05 09:29:12.296048', 1000069, '2024-09-05 09:29:12.296048', 1000069, '0c5c43a4-84cb-4ab4-bf8d-73cde0b662d8', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000162, 1000004, 0, 1000491, 'Y', 10000, NULL, 20000, 0, '2024-09-05 09:32:37.178964', 1000069, '2024-09-05 09:32:37.178964', 1000069, 'da2f305b-e925-402a-8fe7-cbc4ddbf0e0a', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000163, 1000004, 0, 1000494, 'Y', 10000, NULL, 20000, 0, '2024-09-05 09:32:37.189164', 1000069, '2024-09-05 09:32:37.189164', 1000069, '167b9ddd-8908-403c-bb19-b80a768d638f', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000164, 1000004, 0, 1000497, 'Y', 10000, NULL, 20000, 0, '2024-09-05 09:32:37.193866', 1000069, '2024-09-05 09:32:37.193866', 1000069, '2a2f6009-7f43-4a00-92ca-209c3d3178bd', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000165, 1000004, 0, 1000500, 'Y', 10000, NULL, 20000, 0, '2024-09-05 09:32:37.198241', 1000069, '2024-09-05 09:32:37.198241', 1000069, '2093f22f-5391-455a-b443-670e088bd0ac', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000166, 1000004, 0, 1000503, 'Y', 10000, NULL, 20000, 0, '2024-09-05 09:32:37.20262', 1000069, '2024-09-05 09:32:37.20262', 1000069, 'cfb94cf9-3c7d-4211-9484-ade22fc4e477', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000167, 1000004, 0, 1000506, 'Y', 10000, NULL, 20000, 0, '2024-09-05 09:32:37.207345', 1000069, '2024-09-05 09:32:37.207345', 1000069, 'a8c86212-7257-4f5c-8203-e5f700f2f00d', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000168, 1000004, 0, 1000509, 'Y', 10000, NULL, 20000, 0, '2024-09-05 09:32:37.211512', 1000069, '2024-09-05 09:32:37.211512', 1000069, 'b28ce2d8-5d0c-452b-b228-1fa6df1aa01a', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000189, 1000009, 0, 1000562, 'Y', 40000, NULL, 56000, 0, '2024-09-11 17:57:24.640145', 1000071, '2024-09-11 17:57:24.640145', 1000071, 'ba0a066e-7b8d-41e9-a84b-55032575b183', 1000049);
INSERT INTO pos.d_pricelist_product VALUES (1000192, 1000009, 0, 1000571, 'Y', 40000, NULL, 11000, 0, '2024-09-11 18:12:49.98682', 1000071, '2024-09-11 18:12:49.98682', 1000071, 'f940552f-bf74-4c54-89f7-f2107e160c2d', 1000049);
INSERT INTO pos.d_pricelist_product VALUES (1000193, 1000009, 0, 1000574, 'Y', 32000, NULL, 123000, 0, '2024-09-11 18:14:23.703583', 1000071, '2024-09-11 18:14:23.703584', 1000071, 'dbe19190-11c6-4c50-88a3-40171ee13813', 1000049);
INSERT INTO pos.d_pricelist_product VALUES (1000194, 1000009, 0, 1000577, 'Y', 45000, NULL, 110000, 0, '2024-09-11 18:15:54.636025', 1000071, '2024-09-11 18:15:54.636025', 1000071, '43254183-2528-44fc-84e0-3064e4a956a9', 1000049);
INSERT INTO pos.d_pricelist_product VALUES (1000221, 1000004, 0, 1000089, 'Y', 50, NULL, 100, NULL, '2024-09-17 09:40:22.923173', 0, '2024-09-17 09:40:22.923173', 0, '6cbd2a32-da24-441e-8e0a-423db7745d39', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000222, 1000004, 0, 1000102, 'Y', 50, NULL, 100, NULL, '2024-09-17 09:40:22.923173', 0, '2024-09-17 09:40:22.923173', 0, '2a9b14ea-c4cd-48fc-b2e7-cad42de672c4', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000223, 1000004, 0, 1000088, 'Y', 50, NULL, 100, NULL, '2024-09-17 09:40:22.923173', 0, '2024-09-17 09:40:22.923173', 0, '1910285d-4c71-4e91-92ec-8518a9b56a75', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000224, 1000004, 0, 1000091, 'Y', 50, NULL, 100, NULL, '2024-09-17 09:40:22.923173', 0, '2024-09-17 09:40:22.923173', 0, '39b7cfe6-3920-4c17-9e55-746611cfa6d9', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000225, 1000004, 0, 1000093, 'Y', 50, NULL, 100, NULL, '2024-09-17 09:40:22.923173', 0, '2024-09-17 09:40:22.923173', 0, 'e0ec0131-29d0-400d-964d-fda831ce869c', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000226, 1000004, 0, 1000568, 'Y', 40000, NULL, 120000, NULL, '2024-09-17 09:40:22.923173', 0, '2024-09-17 09:40:22.923173', 0, '1af615b6-e9bb-4579-9c62-dda1bdd5237d', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000227, 1000004, 0, 1000565, 'Y', 30000, NULL, 45000, NULL, '2024-09-17 09:40:22.923173', 0, '2024-09-17 09:40:22.923173', 0, '47df659c-5fce-4b0c-b853-c9fbc2da2910', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000232, 1000004, 0, 1000509, 'Y', 10000, 20000, 20000, 0, '2024-09-18 14:42:49.322021', 1000069, '2024-09-18 14:42:49.322021', 1000069, 'ed5683b9-7f73-46b9-88a0-c26ab853f2ce', 1000051);
INSERT INTO pos.d_pricelist_product VALUES (1000234, 1000004, 0, 1000356, 'Y', 10000, 15000, 12000.0, 0, '2024-09-18 14:43:32.161187', 1000069, '2024-09-18 14:53:15.240086', 1000069, 'e16fe49a-6ecf-4521-ab1e-4359bb5b144a', 1000050);
INSERT INTO pos.d_pricelist_product VALUES (1000235, 1000004, 0, 1000359, 'Y', 10000, 15000, 20000, 0, '2024-09-18 14:43:32.172025', 1000069, '2024-09-18 14:53:45.932803', 1000069, '33c4a54c-ad84-4c60-9670-a256f3ed24fd', 1000050);
INSERT INTO pos.d_pricelist_product VALUES (1000259, 1000004, 0, 1000642, 'Y', 100000, NULL, 200000, 0, '2024-09-18 23:25:58.481384', 1000069, '2024-09-18 23:25:58.481384', 1000069, '12d1f48a-04c4-4a67-8589-058271ace05c', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000268, 1000004, 0, 1000651, 'Y', 100000, NULL, 200000, 0, '2024-09-19 07:14:09.166929', 1000069, '2024-09-19 07:14:09.166929', 1000069, 'a3975c97-f800-453b-ba82-52fd92a3f341', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000271, 1000004, 0, 1000654, 'Y', 10000, NULL, 25000, 0, '2024-09-19 11:10:15.807439', 1000069, '2024-09-19 11:10:15.807441', 1000069, '13db0070-8085-48fb-8d66-27039b13d34f', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000274, 1000004, 0, 1000657, 'Y', 10000, NULL, 15000, 0, '2024-09-19 11:24:57.821511', 1000069, '2024-09-19 11:24:57.821511', 1000069, 'b1fc0fab-8b62-4fc1-856e-b67756515338', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000275, 1000004, 0, 1000658, 'Y', 10000, NULL, 15000, 0, '2024-09-19 11:24:58.308392', 1000069, '2024-09-19 11:24:58.308392', 1000069, 'fe342c2e-668f-4ea7-b89f-e749efdb7c91', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000276, 1000004, 0, 1000659, 'Y', 10000, NULL, 15000, 0, '2024-09-19 11:24:58.847381', 1000069, '2024-09-19 11:24:58.847381', 1000069, '19016205-5bba-48bc-9156-232d00edf1fc', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000288, 1000004, 0, 1000671, 'Y', 20000, NULL, 30000, 0, '2024-09-19 11:27:16.669915', 1000069, '2024-09-19 11:27:16.669917', 1000069, 'a9cca459-5261-45de-aa45-69795dc3b3e2', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000289, 1000004, 0, 1000672, 'Y', 20000, NULL, 30000, 0, '2024-09-19 11:27:17.790177', 1000069, '2024-09-19 11:27:17.79018', 1000069, '953f9b50-e6dd-4af2-8c80-cb5b96aaa85b', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000291, 1000004, 0, 1000674, 'Y', 10000, NULL, 15000, 0, '2024-09-19 12:50:00.433149', 1000069, '2024-09-19 12:50:00.433149', 1000069, '2e3e7ebe-c9ba-48b8-9560-443ea265f826', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000292, 1000004, 0, 1000675, 'Y', 10000, NULL, 15000, 0, '2024-09-19 12:50:00.750986', 1000069, '2024-09-19 12:50:00.750986', 1000069, '47a7db96-2a20-4e83-90c2-a833afbba223', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000293, 1000004, 0, 1000676, 'Y', 10000, NULL, 15000, 0, '2024-09-19 12:50:00.840069', 1000069, '2024-09-19 12:50:00.840069', 1000069, 'fd0415fa-6521-4f88-b88d-a8aa15986334', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000294, 1000004, 0, 1000677, 'Y', 20000, NULL, 30000, 0, '2024-09-19 14:29:54.871216', 1000069, '2024-09-19 14:29:54.871217', 1000069, 'fc3c4894-3a0c-4530-80cb-31112fd60287', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000295, 1000004, 0, 1000678, 'Y', 20000, NULL, 30000, 0, '2024-09-19 14:29:56.142983', 1000069, '2024-09-19 14:29:56.142985', 1000069, '8740f154-c569-49ae-aa5c-196957a861bf', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000296, 1000004, 0, 1000679, 'Y', 20000, NULL, 30000, 0, '2024-09-19 14:29:57.303408', 1000069, '2024-09-19 14:29:57.303409', 1000069, 'a4688eda-46b5-45f1-b6d0-fdfa00badc6d', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000297, 1000004, 0, 1000698, 'Y', 10000, NULL, 10000, 0, '2024-09-26 11:27:34.320609', 1000066, '2024-09-26 11:27:34.32061', 1000066, '98222a47-6262-4fb2-86ed-8d89d8103f09', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000298, 1000004, 0, 1000699, 'Y', 30000, NULL, 40000, 0, '2024-09-26 11:30:44.087439', 1000066, '2024-09-26 11:30:44.087439', 1000066, '01f484a7-b942-46c6-b34b-7e01aa93d9b7', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000299, 1000004, 0, 1000700, 'Y', 10000, NULL, 100000, 0, '2024-09-26 11:40:03.772306', 1000066, '2024-09-26 11:40:03.772307', 1000066, '6b28ee0a-1cb6-44f7-9546-cc83a7220759', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000300, 1000004, 0, 1000701, 'Y', 10000, NULL, 100000, 0, '2024-09-26 11:43:04.162007', 1000066, '2024-09-26 11:43:04.162008', 1000066, '8d4ee6c6-91ba-492d-bc19-a6c15f56bc7c', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000301, 1000004, 0, 1000702, 'Y', 10000, NULL, 20000, 0, '2024-09-26 11:50:54.686666', 1000066, '2024-09-26 11:50:54.686666', 1000066, 'ffc3600d-4c97-48f6-bdcb-c6b40a4879bc', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000302, 1000004, 0, 1000704, 'Y', 20000, NULL, 30000, 0, '2024-09-26 14:41:19.63644', 1000066, '2024-09-26 14:41:19.63644', 1000066, '58bfd173-a5ee-4ea5-9b20-b1e2ffd64e48', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000303, 1000004, 0, 1000705, 'Y', 20000, NULL, 40000, 0, '2024-09-26 15:02:45.278376', 1000066, '2024-09-26 15:02:45.278377', 1000066, 'd1d98101-b067-46e9-8715-2cd99ade6fd4', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000304, 1000004, 0, 1000706, 'Y', 20000, NULL, 40000, 0, '2024-09-26 15:02:45.296278', 1000066, '2024-09-26 15:02:45.296278', 1000066, '012a91b0-bb6f-4086-90df-c5f1d34e9f8e', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000305, 1000004, 0, 1000707, 'Y', 20000, NULL, 40000, 0, '2024-09-26 15:02:45.307455', 1000066, '2024-09-26 15:02:45.307455', 1000066, '9d477f2e-c299-4c99-92f7-65a63a835891', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000306, 1000004, 0, 1000708, 'Y', 20000, NULL, 70000, 0, '2024-09-26 15:02:45.317323', 1000066, '2024-09-26 15:08:11.588695', 1000066, 'eb181157-3233-46f7-b348-3ad9febbb40a', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000307, 1000004, 0, 1000709, 'Y', 220000, NULL, 330000, 0, '2024-09-26 15:47:32.377356', 1000066, '2024-09-26 15:47:32.377357', 1000066, '906df538-3b5a-4760-8bb8-dce70ef00ff3', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000308, 1000004, 0, 1000710, 'Y', 300000, NULL, 400000, 0, '2024-09-26 15:49:14.719907', 1000066, '2024-09-26 15:49:14.719908', 1000066, '7cb90a86-6b65-455b-a146-9463dd321cfc', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000309, 1000004, 0, 1000711, 'Y', 10000, NULL, 200000, 0, '2024-09-26 16:10:56.913949', 1000066, '2024-09-26 16:10:56.91395', 1000066, 'bef2597c-0f4e-46fa-8144-ecb0755c5b18', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000310, 1000004, 0, 1000712, 'Y', 10000, NULL, 200000, 0, '2024-09-26 16:10:57.582997', 1000066, '2024-09-26 16:10:57.582997', 1000066, 'f1a59c84-f530-40f7-8395-3d0238ffd7b6', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000311, 1000004, 0, 1000713, 'Y', 10000, NULL, 200000, 0, '2024-09-26 16:10:58.063027', 1000066, '2024-09-26 16:10:58.063027', 1000066, '2281795d-d562-4d68-8c49-be5b62e610f4', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000312, 1000004, 0, 1000714, 'Y', 100000, NULL, 200000, 0, '2024-09-26 16:14:05.614027', 1000066, '2024-09-26 16:14:05.614028', 1000066, 'e59cd4de-d6e5-43b9-a1f7-585c754d01d1', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000313, 1000004, 0, 1000715, 'Y', 100000, NULL, 200000, 0, '2024-09-26 16:14:05.635423', 1000066, '2024-09-26 16:14:05.635423', 1000066, 'eb4c1765-6faa-476e-afd3-d1158c3b97dd', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000331, 1000004, 0, 1000734, 'Y', 20000, NULL, 30000, 0, '2024-09-27 13:11:42.228978', 1000066, '2024-09-27 13:11:42.228979', 1000066, '864242b8-6607-45a1-85b8-a255887426b6', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000332, 1000004, 0, 1000735, 'Y', 20000, NULL, 30000, 0, '2024-09-27 13:11:43.890192', 1000066, '2024-09-27 13:11:43.890192', 1000066, '634453c6-45d5-4a71-9153-1840992b0028', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000333, 1000004, 0, 1000736, 'Y', 20000, NULL, 30000, 0, '2024-09-27 13:11:47.529592', 1000066, '2024-09-27 13:11:47.529593', 1000066, '5f3ae87d-13e4-4744-ace6-4e910bf0cd95', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000314, 1000004, 0, 1000716, 'Y', 100000, NULL, 3000, 0, '2024-09-26 16:14:05.652132', 1000066, '2024-09-26 23:03:58.278453', 1000066, 'b88fa440-a299-4d34-92a0-02b691cea3ca', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000315, 1000004, 0, 1000718, 'Y', 100.0, 120.0, 12100000, 0, '2024-09-26 18:51:18.292766', 1000066, '2024-09-26 23:13:45.36886', 1000066, '529375e9-c03f-477b-b3c4-255b950953ff', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000317, 1000004, 0, 1000720, 'Y', 2000, 2000, 2000, 0, '2024-09-27 11:19:10.252801', 1000066, '2024-09-27 11:19:10.252802', 1000066, '86a2850a-1b2c-455c-94cf-671a8b42643e', 1000046);
INSERT INTO pos.d_pricelist_product VALUES (1000335, 1000004, 0, 1000726, 'Y', 30000, 40000, 40000, 0, '2024-09-27 14:51:52.023819', 1000066, '2024-09-27 14:51:52.023819', 1000066, 'e1c87ed6-2958-486f-ba0e-22f7a330750d', 1000046);
INSERT INTO pos.d_pricelist_product VALUES (1000316, 1000004, 0, 1000720, 'Y', 2000, NULL, 4000, 0, '2024-09-27 10:16:06.561749', 1000066, '2024-09-27 11:26:05.118722', 1000066, '492b33b1-ab16-4a9e-95c7-bee05a123b98', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000334, 1000004, 0, 1000730, 'Y', 20000, 30000, 16000, 0, '2024-09-27 14:51:38.202543', 1000066, '2024-09-27 14:52:10.983573', 1000066, 'e131e0bd-5b8d-4f3e-ad6d-3e6784c4174b', 1000046);
INSERT INTO pos.d_pricelist_product VALUES (1000339, 1000004, 0, 1000743, 'Y', 20000, NULL, 30000, 0, '2024-09-27 15:21:48.926725', 1000066, '2024-09-27 15:21:48.926725', 1000066, '12be83bf-9a99-44dc-b3f9-c5b8e863e377', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000346, 1000004, 0, 1000753, 'Y', 10000, NULL, 20000, 0, '2024-09-27 17:35:22.590446', 1000066, '2024-09-27 17:35:22.590446', 1000066, '9be36b75-846e-4372-81d2-a10afc3de48a', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000318, 1000004, 0, 1000718, 'Y', 100.0, 12100000, 21180, 0, '2024-09-27 11:19:22.69731', 1000066, '2024-09-27 11:28:49.593787', 1000066, 'e489e979-c6fb-45c4-8fc7-c27c63e55c97', 1000046);
INSERT INTO pos.d_pricelist_product VALUES (1000320, 1000004, 0, 1000723, 'Y', 30000, NULL, 40000, 0, '2024-09-27 11:36:47.902736', 1000066, '2024-09-27 11:36:47.902736', 1000066, 'dd9e446e-d966-4bae-bd65-205cf5fdf451', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000321, 1000004, 0, 1000724, 'Y', 30000, NULL, 60000, 0, '2024-09-27 11:36:48.402758', 1000066, '2024-09-27 11:36:48.402759', 1000066, 'c3d5caa5-ec65-4522-b32d-ffc9788804ec', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000322, 1000004, 0, 1000725, 'Y', 30000, NULL, 50000, 0, '2024-09-27 11:36:48.924496', 1000066, '2024-09-27 11:36:48.924496', 1000066, '6378db78-ebb6-4202-9e37-d200f658c4cf', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000323, 1000004, 0, 1000726, 'Y', 30000, NULL, 40000, 0, '2024-09-27 11:36:49.437681', 1000066, '2024-09-27 11:36:49.437682', 1000066, 'fa7754fd-e82e-4c0d-a2fb-77158e6cb663', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000327, 1000004, 0, 1000730, 'Y', 20000, NULL, 30000, 0, '2024-09-27 12:12:05.762193', 1000066, '2024-09-27 12:12:05.762194', 1000066, 'a5bdcc6a-6179-45d8-8ef1-ab5c2867559a', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000328, 1000004, 0, 1000731, 'Y', 20000, NULL, 30000, 0, '2024-09-27 12:12:06.242977', 1000066, '2024-09-27 12:12:06.242977', 1000066, 'a42f7669-d374-4995-bf51-afe999ee26cd', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000329, 1000004, 0, 1000732, 'Y', 20000, NULL, 30000, 0, '2024-09-27 12:12:06.695759', 1000066, '2024-09-27 12:12:06.69576', 1000066, 'a8ef8006-2f92-4d26-8c1c-1589841759d0', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000330, 1000004, 0, 1000733, 'Y', 20000, NULL, 30000, 0, '2024-09-27 12:12:07.141893', 1000066, '2024-09-27 12:12:07.141894', 1000066, 'aaaf8c1a-6fba-4186-8fd8-708267d8c0f2', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000347, 1000004, 0, 1000754, 'Y', 10000, NULL, 20000, 0, '2024-09-27 17:35:22.604785', 1000066, '2024-09-27 17:35:22.604786', 1000066, '027a990e-4420-49ec-be92-42283fec97a2', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000348, 1000004, 0, 1000755, 'Y', 10000, NULL, 20000, 0, '2024-09-27 17:35:22.615009', 1000066, '2024-09-27 17:35:22.61501', 1000066, '02befc6b-3c68-4f36-87f5-f00e051a1601', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000349, 1000004, 0, 1000756, 'Y', 10000, NULL, 20000, 0, '2024-09-27 17:35:22.626015', 1000066, '2024-09-27 17:35:22.626016', 1000066, 'f6e831ba-e376-4a9d-8155-970c0aba9eaa', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000354, 1000004, 0, 1000761, 'Y', 15000, NULL, 25000, 0, '2024-09-27 17:41:00.225652', 1000066, '2024-09-27 17:41:00.225653', 1000066, '2622adf0-65e0-49f7-9508-98fcd6078bb5', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000355, 1000004, 0, 1000762, 'Y', 15000, NULL, 25000, 0, '2024-09-27 17:41:00.741486', 1000066, '2024-09-27 17:41:00.741486', 1000066, 'af8e5063-999a-4336-832b-a7496814ea70', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000356, 1000004, 0, 1000763, 'Y', 15000, NULL, 25000, 0, '2024-09-27 17:41:01.257777', 1000066, '2024-09-27 17:41:01.257778', 1000066, '499a91b1-c6a3-4d98-ac8b-8adc2e66b21f', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000357, 1000004, 0, 1000764, 'Y', 15000, NULL, 25000, 0, '2024-09-27 17:41:01.88233', 1000066, '2024-09-27 17:41:01.882331', 1000066, '18067dc0-5e0b-47f0-ab7c-44ef345efd6a', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000358, 1000004, 0, 1000765, 'Y', 20000, NULL, 30000, 0, '2024-09-28 10:33:39.055754', 1000066, '2024-09-28 10:33:39.055754', 1000066, 'ec6897ac-3fcc-4ea8-8b16-4bfeaaf9f51e', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000359, 1000004, 0, 1000766, 'Y', 20000, NULL, 30000, 0, '2024-09-28 10:48:54.733041', 1000066, '2024-09-28 10:48:54.733041', 1000066, 'fc438f40-19f5-4564-8718-ab6500db35e7', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000360, 1000004, 0, 1000767, 'Y', 20000, NULL, 30000, 0, '2024-09-28 10:50:00.234021', 1000066, '2024-09-28 10:50:00.234021', 1000066, 'd5ec4913-1b39-431c-82c6-2e591a7b37b9', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000361, 1000004, 0, 1000768, 'Y', 20000, NULL, 30000, 0, '2024-09-28 10:50:47.357572', 1000066, '2024-09-28 10:50:47.357572', 1000066, 'd672c1da-d72b-434e-a110-5bce89f3848f', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000362, 1000004, 0, 1000769, 'Y', 20000, NULL, 30000, 0, '2024-09-28 10:51:04.888756', 1000066, '2024-09-28 10:51:04.888756', 1000066, 'ced36ad2-0eae-4e99-9f8b-73608846ef53', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000363, 1000004, 0, 1000770, 'Y', 1000, NULL, 2000, 0, '2024-09-28 11:14:01.754412', 1000066, '2024-09-28 11:14:01.754412', 1000066, '5c6688e8-e895-4dc5-81c6-a142edc0d579', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000364, 1000004, 0, 1000771, 'Y', 16000, NULL, 18000, 0, '2024-09-28 11:15:06.060019', 1000066, '2024-09-28 11:15:06.060019', 1000066, '07e0e07c-3ba1-41fa-a802-73ae8430034d', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000368, 1000004, 0, 1000775, 'Y', 20000, NULL, 30000, 0, '2024-09-28 11:35:07.519579', 1000066, '2024-09-28 11:35:07.51958', 1000066, 'c74f389d-02a8-47cb-b847-237de25bf79d', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000375, 1000004, 0, 1000782, 'Y', 10000, NULL, 20000, 0, '2024-09-28 11:58:16.086184', 1000066, '2024-09-28 11:58:16.086184', 1000066, '93650688-5803-48b9-8a05-ee0942e91421', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000376, 1000004, 0, 1000783, 'Y', 10000, NULL, 20000, 0, '2024-09-28 11:58:16.194554', 1000066, '2024-09-28 11:58:16.194554', 1000066, '2a741ea1-4fd5-4276-8652-00fe1cb65912', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000377, 1000004, 0, 1000784, 'Y', 10000, NULL, 20000, 0, '2024-09-28 11:58:16.277471', 1000066, '2024-09-28 11:58:16.277471', 1000066, '444ea88f-e1a1-409e-922a-03b0c13cffe1', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000378, 1000004, 0, 1000785, 'Y', 10000, NULL, 20000, 0, '2024-09-28 11:58:16.38039', 1000066, '2024-09-28 11:58:16.38039', 1000066, 'b37e826b-c642-4300-9785-827750db1b11', 1000013);
INSERT INTO pos.d_pricelist_product VALUES (1000379, 1000004, 0, 1000786, 'Y', 10000, NULL, 12000, 0, '2024-09-28 11:58:26.163572', 1000066, '2024-09-28 11:58:26.163572', 1000066, 'ab1a5383-1576-4a68-93d5-bd6dbd7a8387', 1000013);


--
-- TOC entry 5411 (class 0 OID 390225)
-- Dependencies: 317
-- Data for Name: d_print_report; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_print_report VALUES (1000015, 1000004, 'VA', 'aaaa', '2024-08-13 17:10:14.732549', 1000069, '2024-08-13 17:10:14.732549', 1000069, '9cf6f4ec-9c9d-4d94-8970-502458d805e6', 'Y', 'Y');
INSERT INTO pos.d_print_report VALUES (1000016, 1000004, 'VA', 'aaaa', '2024-08-13 17:10:16.707537', 1000069, '2024-08-13 17:10:16.707537', 1000069, 'a77ae61c-a60f-4485-878f-2094bd0ec37f', 'N', 'Y');
INSERT INTO pos.d_print_report VALUES (1000017, 1000004, 'VAaxxx', 'aaaa', '2024-08-13 17:10:22.666691', 1000069, '2024-08-13 17:11:56.065182', 1000069, '8332adf1-2800-4559-8499-561e841390d7', 'N', 'Y');


--
-- TOC entry 5340 (class 0 OID 385767)
-- Dependencies: 245
-- Data for Name: d_product; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_product VALUES (1000687, 0, 1000004, '2024-09-25 03:17:16.337818', 0, '2024-09-25 03:17:16.337818', 0, 'Y', 1000011, 'PRO1000462', 'Thịt bò xào tỏi', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '53c48685-91e0-487d-b0b8-2f9b3c558f83', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000698, 0, 1000004, '2024-09-26 11:27:34.297042', 1000066, '2024-09-26 11:27:34.297043', 1000066, 'Y', 1000017, 'PROD1000698', 'Bánh mỳ bơ sữa', '**', 10000.00, 10000.00, 1000012, 0.00, 'Y', 1000405, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'f1050de9-bf46-4b20-af20-375e5df6d98c', 'N', 0, 0, NULL, 'CBP', NULL, NULL, NULL, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000688, 0, 1000004, '2024-09-25 03:17:16.337818', 0, '2024-09-25 03:17:16.337818', 0, 'Y', 1000011, 'PRO1000463', 'Thịt bò tái', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '535d7dff-f6e3-44e8-a2b0-d090c2d713d3', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000699, 0, 1000004, '2024-09-26 11:30:44.080938', 1000066, '2024-09-26 11:30:44.080938', 1000066, 'Y', 1000017, 'PROD1000699', 'Mắm cá lóc', '**', 40000.00, 30000.00, 1000010, 0.00, 'Y', 1000406, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '9f02ba6e-b3fd-406d-8a2b-d01c3d017e2d', 'N', 0, 0, NULL, 'PRD ', NULL, NULL, NULL, 1, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000674, 0, 1000004, '2024-09-19 12:50:00.400034', 1000069, '2024-09-19 12:50:00.400034', 1000069, 'Y', 1000025, 'PROD100021651', 'Cafe đen', '**', 15000.00, 10000.00, 1000011, 20.00, 'N', NULL, NULL, 'size', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '92b3325e-64a9-48f6-98e8-874628526dc6', NULL, 0, 100, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000675, 0, 1000004, '2024-09-19 12:50:00.71462', 1000069, '2024-09-19 12:50:00.71462', 1000069, 'Y', 1000025, 'PROD1000675', 'Cafe đen M', '**', 15000.00, 10000.00, 1000011, 20.00, NULL, NULL, NULL, NULL, 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'exampleDescription', '45b6bcda-3f97-4ef6-b136-a69d85d11080', 'Y', 0, 100, NULL, NULL, 1000674, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000676, 0, 1000004, '2024-09-19 12:50:00.815666', 1000069, '2024-09-19 12:50:00.815666', 1000069, 'Y', 1000025, 'PROD1000676', 'Cafe đen S', '**', 15000.00, 10000.00, 1000011, 20.00, NULL, NULL, NULL, NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'exampleDescription', '10e182c9-19c0-41e0-be38-6e05b4c87c35', 'Y', 0, 100, NULL, NULL, 1000674, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000697, 0, 1000004, '2024-09-25 03:17:16.337818', 0, '2024-09-25 03:17:16.337818', 0, 'Y', 1000011, 'PRO1000472', 'Sốt Mayonnaise', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'bbad053a-7135-4269-a9f3-c15014e541dd', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000681, 0, 1000004, '2024-09-25 03:14:35.299439', 0, '2024-09-25 03:14:35.299439', 0, 'Y', 1000016, 'PRO1000456', 'Rau cải', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '3fe67bbc-8417-49a1-a625-c5129803716a', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000577, 1000016, 1000009, '2024-09-11 18:15:54.633882', 1000071, '2024-09-12 15:52:02.02603', 1000071, 'Y', 1000033, 'PROD1000576', 'Thép hộp mạ kẽm Z080: 14mmx14mmx1.20mmx6.0m', '**', 110000.00, 45000.00, 1000017, 1223.00, 'Y', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '4a63f693-9b9c-47bf-8bff-24db5585ecc5', 'N', 0, 0, NULL, 'RGD', NULL, NULL, NULL, 0, NULL, 'VTS', 0);
INSERT INTO pos.d_product VALUES (1000383, 0, 1000004, '2024-09-04 09:23:01.426342', 1000069, '2024-09-04 02:00:46.48313', 1000069, 'Y', 1000020, NULL, 'Lau thai chua cay', '**', 120000.00, 100000.00, 1000014, 10.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'ef2a1c36-f394-4fa0-ae8f-ac45cc22c4e0', 'N', 1, 10, 1000003, 'PRD ', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000626, 0, 1000004, '2024-09-18 21:53:32.397699', 1000069, '2024-09-18 21:53:32.397702', 1000069, 'Y', 1000025, 'PROD1000626', 'Cafe đen M', '**', 15000.00, 10000.00, 1000011, 20.00, NULL, NULL, NULL, NULL, 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'exampleDescription', '74773aac-84b2-437d-bc68-8bdcfbdc7e2a', 'Y', 0, 100, NULL, NULL, 1000625, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000644, 0, 1000004, '2024-09-18 23:36:56.480114', 1000069, '2024-09-18 23:36:56.480115', 1000069, 'Y', 1000017, 'PROD1000644', 'Banh xeo', '**', 20000.00, 15000.00, 1000012, 10.00, 'Y', NULL, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '30ebbb2d-97a7-41aa-aed1-fbd808ca2a6e', 'N', 1, 10, NULL, 'RGD', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000693, 0, 1000004, '2024-09-25 03:17:16.337818', 0, '2024-09-25 03:17:16.337818', 0, 'Y', 1000020, 'PRO1000468', 'Cá diêu hồng sốt chanh dây', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0808ac44-c296-45bb-a931-f4e649fa1205', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000680, 0, 1000004, '2024-09-25 03:14:35.299439', 0, '2024-09-25 03:14:35.299439', 0, 'Y', 1000016, 'PRO1000455', 'Bom_Gà nướng', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '927309f2-aba8-4832-9cd2-990585dacb5c', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000683, 0, 1000004, '2024-09-25 03:14:35.299439', 0, '2024-09-25 03:14:35.299439', 0, 'Y', 1000016, 'PRO1000458', 'Bột chiên giòn hải sản', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'f19f9198-0a9a-4f9a-9837-9384a95f5cd6', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000696, 0, 1000004, '2024-09-25 03:17:16.337818', 0, '2024-09-25 03:17:16.337818', 0, 'Y', 1000011, 'PRO1000471', 'Thịt bò', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'fb85dc21-cfcb-443c-8487-a74ab39324fa', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000689, 0, 1000004, '2024-09-25 03:17:16.337818', 0, '2024-09-25 03:17:16.337818', 0, 'Y', 1000011, 'PRO1000464', 'Rau các loại ', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '9cc27714-b635-468f-9607-48670aa175ed', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000695, 0, 1000004, '2024-09-25 03:17:16.337818', 0, '2024-09-25 03:17:16.337818', 0, 'Y', 1000011, 'PRO1000470', 'Bò bít tết', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '81dcccd2-2e0f-4bcc-902c-659e4b52c009', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000685, 0, 1000004, '2024-09-25 03:17:16.337818', 0, '2024-09-25 03:17:16.337818', 0, 'Y', 1000020, 'PRO1000460', 'Cocktai Mojito', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'f1722fbd-cf3d-4b08-a7a4-41de72fd20ef', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000691, 0, 1000004, '2024-09-25 03:17:16.337818', 0, '2024-09-25 03:17:16.337818', 0, 'Y', 1000020, 'PRO1000466', 'Tôm sú tươi', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'f1c8422e-d1f7-45e1-99d7-2adc4ecf90a9', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000694, 0, 1000004, '2024-09-25 03:17:16.337818', 0, '2024-09-25 03:17:16.337818', 0, 'Y', 1000020, 'PRO1000469', 'Cá diêu hồng', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '219b1473-0461-4a33-ad2b-4a81f17d4fe7', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000720, 0, 1000004, '2024-09-27 10:16:06.531047', 1000066, '2024-09-27 17:40:25.145863', 1000066, 'Y', 1000017, 'PROD1000719', 'Bánh mỳ', '**', 4000.00, 2000.00, 1000012, 0.00, 'Y', 1000421, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'e4162b97-1568-4f70-884e-3dd3840a893e', 'N', 0, 0, NULL, 'RGD', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000775, 0, 1000004, '2024-09-28 11:35:07.51585', 1000066, '2024-09-28 11:35:07.51585', 1000066, 'Y', 1000423, 'PROD1000772', 'Banh mi bo sua', '**', 30000.00, 20000.00, NULL, 0.00, 'N', NULL, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'f6693675-c07d-49b1-aa46-b233b2a6357c', NULL, 0, 0, NULL, 'SVC', NULL, NULL, NULL, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000677, 0, 1000004, '2024-09-19 14:29:54.844511', 1000069, '2024-09-19 14:29:54.844513', 1000069, 'Y', 1000017, 'PROD122453', 'Pho tron', '**', 30000.00, 20000.00, 1000012, 10.00, 'Y', 1000398, 'FOD', 'Size2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'b4a6bd88-dc84-41b0-9abd-a2bb253895fb', 'N', 0, 10, NULL, 'PRD ', NULL, NULL, NULL, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000623, 0, 1000004, '2024-09-18 21:18:35.50404', 1000069, '2024-09-18 21:18:35.50404', 1000069, 'Y', 1000025, 'PROD1000623', 'Cafe đen S', '**', 15000.00, 10000.00, 1000011, 20.00, NULL, NULL, NULL, NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'exampleDescription', '1347c47d-941b-485e-bd98-e82a59268bab', 'Y', 0, 100, NULL, NULL, 1000621, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000678, 0, 1000004, '2024-09-19 14:29:56.12086', 1000069, '2024-09-19 14:29:56.120861', 1000069, 'Y', 1000017, 'PROD1000678', 'Pho tron A', '**', 30000.00, 20000.00, 1000012, 10.00, 'Y', 1000399, 'FOD', 'Size2', 'A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'c142cfe4-a0af-4e31-a210-eaf440d78794', 'Y', 0, 10, NULL, 'PRD ', NULL, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000679, 0, 1000004, '2024-09-19 14:29:57.288213', 1000069, '2024-09-19 14:29:57.288215', 1000069, 'Y', 1000017, 'PROD1000679', 'Pho tron S', '**', 30000.00, 20000.00, 1000012, 10.00, 'Y', 1000400, 'FOD', 'Size2', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '5696ee5c-c88d-4a34-8f34-1933afac9b6f', 'Y', 0, 10, NULL, 'PRD ', NULL, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000359, 0, 1000004, '2024-08-30 08:36:41.885961', 1000069, '2024-09-26 17:49:56.660508', 1000066, 'Y', 1000025, NULL, 'Cafe đen S', '**', 15000.00, 10000.00, 1000011, 20.00, NULL, NULL, NULL, NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'exampleDescription', 'a201b286-2bbd-4f57-8e39-6fdeb0c761d9', 'Y', 0, 100, 1000003, NULL, 1000353, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000102, 1000016, 1000003, '2024-08-04 11:18:14.197504', 1000068, '2024-08-04 11:18:14.197504', 1000068, 'Y', 1000010, 'PR000001', 'exampleName', 'exampleQrCode', 100.00, 50.00, 1000003, 20.00, 'N', NULL, NULL, 'exampleAttribute1', 'exampleAttribute2', 'exampleAttribute3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'c3701ce2-940f-4812-9ace-c21eb4b36044', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000356, 0, 1000004, '2024-08-30 08:36:41.806706', 1000069, '2024-09-26 17:49:58.25072', 1000066, 'Y', 1000025, NULL, 'Cafe đen M', '**', 15000.00, 10000.00, 1000011, 20.00, NULL, NULL, NULL, NULL, 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'exampleDescription', '51221654-0f5a-4146-b5bc-995100a1fa56', 'Y', 0, 100, 1000003, NULL, 1000353, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000130, 0, 1000004, '2024-08-07 09:12:46.598136', 1000069, '2024-09-04 02:00:45.639771', 1000069, 'Y', 1000016, 'PR000019', 'Gà nướng tiêu xanh', '**', 20050.00, 50.00, 1000013, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, 'exampleAttribute4', 'exampleAttribute5', 'exampleAttribute6', 'exampleAttribute7', 'exampleAttribute8', 'exampleAttribute9', 'exampleAttribute10', 'exampleDescription', 'fc25581f-e156-41a2-a017-8f43295ccd1e', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000608, 0, 1000004, '2024-09-16 13:43:23.918403', 1000069, '2024-09-16 13:43:23.918403', 1000069, 'Y', 1000016, NULL, 'Combo Gà Quay', '**', 100.00, 50.00, 1000012, 20.00, 'N', NULL, NULL, '', '', '', 'exampleAttribute4', 'exampleAttribute5', 'exampleAttribute6', 'exampleAttribute7', 'exampleAttribute8', 'exampleAttribute9', 'exampleAttribute10', 'exampleDescription', '2e93c1b3-299b-4300-ab50-678b3ff4b0bc', 'Y', 0, 100, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000392, 0, 1000004, '2024-09-04 09:47:43.877436', 1000069, '2024-09-04 15:15:05.1858', 1000069, 'Y', 1000020, NULL, 'Banh trung nuong', '**', 120000.00, 100000.00, 1000014, 10.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh nong va thom', 'e2a14bc9-bacc-4c6f-9d84-c7d299c0c861', 'N', 1, 10, 1000003, 'PRD ', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000700, 0, 1000004, '2024-09-26 11:40:03.765964', 1000066, '2024-09-26 11:40:03.765965', 1000066, 'Y', 1000017, 'PROD1000700', 'Mắm ruốc', '**', 100000.00, 10000.00, 1000014, 0.00, 'Y', 1000407, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '00cc7e07-a8e5-4488-9fe5-39caf333b1d4', 'N', 0, 0, NULL, 'RGD', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000093, 1000016, 1000003, '2024-07-23 23:41:11.000341', 1000068, '2024-07-23 23:41:11.000341', 1000068, 'Y', 1000002, 'PR000006', 'Ống nhựa PPR 1 lớp chỉ đỏ 20mmx4.0m', 'exampleQrCode', 100.00, 50.00, 1000009, 20.00, 'N', NULL, NULL, 'exampleAttribute1', 'exampleAttribute2', 'exampleAttribute3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '52f94da0-b2fd-4517-b283-5fa97917b128', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000702, 0, 1000004, '2024-09-26 11:50:54.682152', 1000066, '2024-09-26 11:50:54.682152', 1000066, 'Y', 1000017, 'PROD1000702', 'Mắm cá linh', '**', 20000.00, 10000.00, 1000015, 0.00, 'Y', 1000409, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '6ab63c22-5745-4adf-8461-989efd0fb990', 'N', 0, 0, NULL, 'RGD', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000654, 0, 1000004, '2024-09-19 11:10:15.780118', 1000069, '2024-09-19 11:10:15.780121', 1000069, 'Y', 1000020, 'PROD10000052', 'Pho 2 to Gia Lai', '**', 25000.00, 10000.00, 1000012, 0.00, 'Y', 1000392, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '70b09a52-ab47-41e2-83cc-26e7d48e477d', 'N', 0, 0, NULL, 'SVC', NULL, NULL, NULL, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000105, 0, 1000004, '2024-08-04 12:06:46.674131', 1000069, '2024-09-04 01:53:42.041554', 1000069, 'Y', 1000011, 'PR000008', 'Bò Wagyu Nhật A5 Áp Chảo Sốt Ponzu', '**', 20050.00, 50.00, 1000010, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'dc98bca8-141f-466d-ac6f-c4bfa2859fca', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000431, 0, 1000004, '2024-09-05 09:22:46.788439', 1000069, '2024-09-05 09:22:46.78844', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son A-F-S', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', 'e19c2d5c-0dd0-48a5-ac83-d007079790f7', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000107, 0, 1000004, '2024-08-04 12:10:06.39364', 1000069, '2024-09-19 07:41:31.662524', 1000069, 'Y', 1000012, 'PR000010', 'Shochu Kan No Ko Mugi 25% 720ml', '**', 20050.00, 50.00, 1000011, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'cb2c1f0b-96a2-4e4a-81d0-a40fe2653ce7', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000395, 0, 1000004, '2024-09-04 09:47:43.967438', 1000069, '2024-09-04 15:15:05.188638', 1000069, 'Y', 1000020, NULL, 'Banh trung nuong A', '**', 120000.00, 100000.00, 1000014, 10.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh nong va thom', '2b3cdaaf-7455-430a-9394-921d6d660bc7', 'Y', 1, 10, 1000003, 'PRD ', NULL, NULL, 1, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000380, 0, 1000004, '2024-09-03 22:22:20.751573', 1000069, '2024-09-04 15:15:05.17864', 1000069, 'Y', 1000021, NULL, 'Lau ga la giang S', '**', 120000.00, 100000.00, 1000012, 10.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Mota chi tiet', '36c9678f-e64b-4b9f-9c8a-d273e06c6ef8', 'Y', 1, 10, 1000003, 'PRD ', NULL, NULL, 1, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000701, 0, 1000004, '2024-09-26 11:43:04.156439', 1000066, '2024-09-26 11:43:04.15644', 1000066, 'Y', 1000017, 'PROD1000701', 'Mắm tôm', '**', 100000.00, 10000.00, 1000011, 0.00, 'Y', 1000408, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'b7ac0c46-ce77-4687-b957-7d89a7547381', 'N', 0, 0, NULL, 'RGD', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000455, 0, 1000004, '2024-09-05 09:22:46.870501', 1000069, '2024-09-05 09:22:46.870501', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son C-D-S', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', 'cb2205c4-4861-4135-978e-9faf99a9dda3', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000353, 0, 1000004, '2024-08-30 08:36:41.746734', 1000069, '2024-09-27 16:32:42.222845', 1000066, 'Y', 1000025, NULL, 'Cafe đen', '**', 12000.00, 10000.00, 1000011, 20.00, 'N', NULL, NULL, 'size', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '32ab03d1-f62f-49e0-8803-41b7e0e32648', NULL, 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000503, 0, 1000004, '2024-09-05 09:32:37.201969', 1000069, '2024-09-27 16:32:39.583535', 1000066, 'Y', 1000023, NULL, 'Banh mi thap cam M-B', '**', 20000.00, 10000.00, 1000012, 800.00, 'Y', NULL, 'FOD', 'Size,Size1', 'M', 'B', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '7e02bcda-3c5f-4b15-adf3-003bca334e7e', 'Y', 100, 800, 1000004, 'PRD ', NULL, NULL, 1, 10, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000753, 0, 1000004, '2024-09-27 17:35:22.587674', 1000066, '2024-09-27 17:35:22.587674', 1000066, 'Y', 1000017, 'PROD1000744', 'Muoi', '**', 20000.00, 10000.00, 1000019, 100.00, 'Y', NULL, 'OTH', 'Size', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'e0fae824-e8f4-4eaf-b612-b85b003e0996', 'N', 12, 100, NULL, 'RGD', NULL, NULL, NULL, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000705, 0, 1000004, '2024-09-26 15:02:45.275242', 1000066, '2024-09-26 15:02:45.275243', 1000066, 'Y', 1000012, 'HH01', 'Cà phê sữa', '**', 40000.00, 20000.00, 1000018, 60.00, 'Y', NULL, 'DRK', 'Size', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '65601faf-82a2-4078-a217-448b58539e32', 'N', 0, 0, NULL, 'RGD', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000706, 0, 1000004, '2024-09-26 15:02:45.290826', 1000066, '2024-09-26 15:02:45.290826', 1000066, 'Y', 1000012, 'PROD1000706', 'Cà phê sữa L', '**', 40000.00, 20000.00, 1000018, 60.00, 'Y', NULL, 'DRK', 'Size', 'L', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '608042b6-7234-4bf7-b9c3-c2d8babff39f', 'Y', 0, 0, NULL, 'RGD', NULL, NULL, 1, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000707, 0, 1000004, '2024-09-26 15:02:45.305143', 1000066, '2024-09-26 15:02:45.305143', 1000066, 'Y', 1000012, 'PROD1000707', 'Cà phê sữa M', '**', 40000.00, 20000.00, 1000018, 60.00, 'Y', NULL, 'DRK', 'Size', 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '3a7c8b14-4f8e-4f1a-8d10-0fef46768498', 'Y', 0, 0, NULL, 'RGD', NULL, NULL, 1, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000708, 0, 1000004, '2024-09-26 15:02:45.314887', 1000066, '2024-09-26 15:09:14.599747', 1000066, 'Y', 1000012, 'PROD1000708', 'Cà phê sữa S', '**', 70000.00, 20000.00, 1000018, 60.00, 'Y', NULL, 'DRK', 'Size', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '0dcf838e-67ab-4c88-8bc5-7ddfcfd775dc', 'Y', 0, 0, NULL, 'RGD', NULL, NULL, 1, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000754, 0, 1000004, '2024-09-27 17:35:22.601244', 1000066, '2024-09-27 17:35:22.601245', 1000066, 'Y', 1000017, 'PROD1000754', 'Muoi L', '**', 20000.00, 10000.00, 1000019, 100.00, 'Y', NULL, 'OTH', 'Size', 'L', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '93a59c2c-5027-4de6-a189-28f83dc99759', 'N', 12, 100, NULL, 'RGD', NULL, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000704, 0, 1000004, '2024-09-26 14:41:19.613692', 1000066, '2024-09-26 14:41:19.613693', 1000066, 'Y', 1000012, 'PROD1000703', 'Nuoc ep trai cay', '**', 30000.00, 20000.00, 1000012, 10.00, 'Y', 1000411, 'DRK', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '72fede2b-a89f-42ca-8874-d9f7d3e0c92e', 'N', 1, 10, NULL, 'PRD ', NULL, NULL, NULL, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000755, 0, 1000004, '2024-09-27 17:35:22.612588', 1000066, '2024-09-27 17:35:22.612588', 1000066, 'Y', 1000017, 'PROD1000755', 'Muoi M', '**', 20000.00, 10000.00, 1000019, 100.00, 'Y', NULL, 'OTH', 'Size', 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '87092df7-6025-4b02-92a8-d8d35f09c908', 'N', 12, 100, NULL, 'RGD', NULL, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000404, 0, 1000004, '2024-09-04 09:49:27.141238', 1000069, '2024-09-04 02:00:46.815654', 1000069, 'Y', 1000017, NULL, 'Banh cuon binh dinh S', '**', 30000.00, 10000.00, 1000012, 10.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'ca46ee62-7156-4756-be82-9379d4e73ef2', 'Y', 1, 10, 1000003, 'PRD ', NULL, NULL, 1, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000709, 0, 1000004, '2024-09-26 15:47:32.371713', 1000066, '2024-09-26 15:47:32.371713', 1000066, 'Y', 1000017, 'PROD1000709', 'cua đồng', '**', 330000.00, 220000.00, 1000012, 1000.00, 'Y', 1000412, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'ea773b66-7703-47a2-933e-6c45f71bc510', 'N', 1, 989, NULL, 'PRD ', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000571, 1000016, 1000009, '2024-09-11 18:12:49.98331', 1000071, '2024-09-12 18:30:33.339122', 1000071, 'Y', 1000033, 'PROD1000570', 'Thép hộp mạ kẽm Z080: 13mmx26mmx1.10mmx6.0m', '**', 11000.00, 40000.00, 1000017, 712.00, 'Y', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'c91d0923-1041-4527-9ab6-9bf6e65dabdb', 'N', 0, 0, NULL, 'RGD', NULL, NULL, NULL, 0, NULL, 'VTS', 0);
INSERT INTO pos.d_product VALUES (1000756, 0, 1000004, '2024-09-27 17:35:22.622538', 1000066, '2024-09-27 17:35:22.622538', 1000066, 'Y', 1000017, 'PROD1000756', 'Muoi S', '**', 20000.00, 10000.00, 1000019, 100.00, 'Y', NULL, 'OTH', 'Size', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'c27d14ac-933f-4f91-8a3e-7da0c791ca96', 'N', 12, 100, NULL, 'RGD', NULL, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000710, 0, 1000004, '2024-09-26 15:49:14.715079', 1000066, '2024-09-26 15:49:14.71508', 1000066, 'Y', 1000017, 'PROD1000710', 'Mỳ ý sốt bò bằm', '**', 400000.00, 300000.00, 1000012, 1000.00, 'Y', 1000413, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'fdc2e74f-bb80-4afc-b02c-cd9e6772ee02', 'Y', 12, 985, NULL, 'PRD ', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000458, 0, 1000004, '2024-09-05 09:22:46.892741', 1000069, '2024-09-05 09:22:46.892742', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son C-E-M', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', '0bcad7ac-5f50-4228-bbc2-901a782a6ff5', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000500, 0, 1000004, '2024-09-05 09:32:37.197631', 1000069, '2024-09-05 09:32:37.197631', 1000069, 'Y', 1000023, NULL, 'Banh mi thap cam M-A', '**', 20000.00, 10000.00, 1000012, 800.00, 'Y', NULL, 'FOD', 'Size,Size1', 'M', 'A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '8691b460-45f1-43c1-975a-0f8d2578a3ba', 'Y', 100, 800, 1000004, 'PRD ', NULL, NULL, 1, 10, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000643, 0, 1000004, '2024-09-18 23:24:42.559703', 1000069, '2024-09-18 23:24:42.559705', 1000069, 'Y', 1000025, 'PROD1000643', 'Bac xiu', '**', 200000.00, 100000.00, 1000014, 0.00, 'Y', NULL, 'DRK', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '3cf1177b-f5aa-4d68-ba04-2b797a4c3f1f', 'N', 0, 0, NULL, 'PRD', NULL, NULL, NULL, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000761, 0, 1000004, '2024-09-27 17:41:00.221435', 1000066, '2024-09-27 17:41:00.221436', 1000066, 'Y', 1000011, 'PROD1000757', 'Hamburger', '**', 25000.00, 15000.00, 1000012, 10.00, 'Y', 1000440, 'FOD', 'Size', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'cb431d7d-bf73-4780-be2d-b8cc30459870', 'N', 1, 9, NULL, 'PRD ', NULL, NULL, NULL, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000762, 0, 1000004, '2024-09-27 17:41:00.727137', 1000066, '2024-09-27 17:41:00.727137', 1000066, 'Y', 1000011, 'PROD1000762', 'Hamburger L', '**', 25000.00, 15000.00, 1000012, 10.00, 'Y', 1000441, 'FOD', NULL, 'L', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '68c6c20f-e9f4-4947-951d-d7cf94c53b84', 'N', 1, 9, NULL, 'PRD ', 1000761, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000763, 0, 1000004, '2024-09-27 17:41:01.253929', 1000066, '2024-09-27 17:41:01.25393', 1000066, 'Y', 1000011, 'PROD1000763', 'Hamburger M', '**', 25000.00, 15000.00, 1000012, 10.00, 'Y', 1000442, 'FOD', NULL, 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '3e7dd626-09af-49a8-9c30-19e915e29ced', 'N', 1, 9, NULL, 'PRD ', 1000761, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000764, 0, 1000004, '2024-09-27 17:41:01.878111', 1000066, '2024-09-27 17:41:01.878112', 1000066, 'Y', 1000011, 'PROD1000764', 'Hamburger S', '**', 25000.00, 15000.00, 1000012, 10.00, 'Y', 1000443, 'FOD', NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '75808a96-7f8d-47f9-9fa1-c2e1ffb15b5f', 'N', 1, 9, NULL, 'PRD ', 1000761, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000138, 0, 1000004, '2024-08-07 09:14:55.063453', 1000069, '2024-09-04 15:20:47.992399', 1000069, 'Y', 1000016, 'PR000023', 'Gà hấp muối xả', '**', 20050.00, 50.00, 1000013, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, 'exampleAttribute4', 'exampleAttribute5', 'exampleAttribute6', 'exampleAttribute7', 'exampleAttribute8', 'exampleAttribute9', 'exampleAttribute10', 'exampleDescription', '7b16dfdd-e804-496f-a896-fb8a083a4d17', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000113, 0, 1000004, '2024-08-04 12:18:29.019488', 1000069, '2024-09-04 04:34:20.88474', 1000069, 'Y', 1000014, 'PR000015', 'Thịt, Sườn Dê Nướng Sa Tế', '**', 20050.00, 50.00, 1000012, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '3fb83750-3885-4f70-a526-f4e860a4cf22', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000657, 0, 1000004, '2024-09-19 11:24:57.617948', 1000069, '2024-09-19 11:24:57.617948', 1000069, 'Y', 1000025, 'PROD1000215', 'Cafe đen', '**', 15000.00, 10000.00, 1000011, 20.00, 'N', NULL, NULL, 'size', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '795c5183-cc4a-41c7-8134-c0b565dc0614', NULL, 0, 100, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000658, 0, 1000004, '2024-09-19 11:24:58.198291', 1000069, '2024-09-19 11:24:58.198291', 1000069, 'Y', 1000025, 'PROD1000658', 'Cafe đen M', '**', 15000.00, 10000.00, 1000011, 20.00, NULL, NULL, NULL, NULL, 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'exampleDescription', '521e0b40-4c06-436e-b08f-b965d8b666fb', 'Y', 0, 100, NULL, NULL, 1000657, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000659, 0, 1000004, '2024-09-19 11:24:58.757703', 1000069, '2024-09-19 11:24:58.757703', 1000069, 'Y', 1000025, 'PROD1000659', 'Cafe đen S', '**', 15000.00, 10000.00, 1000011, 20.00, NULL, NULL, NULL, NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'exampleDescription', '550fc274-9244-44d3-9b4b-c0e6d3260cdd', 'Y', 0, 100, NULL, NULL, 1000657, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000711, 0, 1000004, '2024-09-26 16:10:56.892842', 1000066, '2024-09-26 16:10:56.892843', 1000066, 'Y', 1000011, 'PROD1000711', 'Banh mi bo kho', '**', 200000.00, 10000.00, 1000012, 500.00, 'Y', 1000414, 'FOD', 'Size2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '2431804a-b14d-402c-955a-5191a1761188', 'Y', 12, 489, NULL, 'PRD ', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000712, 0, 1000004, '2024-09-26 16:10:57.576084', 1000066, '2024-09-26 16:10:57.576084', 1000066, 'Y', 1000011, 'PROD1000712', 'Banh mi bo kho M', '**', 200000.00, 10000.00, 1000012, 500.00, 'Y', 1000415, 'FOD', 'Size2', 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '1a772fcc-37d3-4b25-b2ee-6f583082b08f', 'N', 12, 489, NULL, 'PRD ', NULL, NULL, 1, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000088, 1000016, 1000002, '2024-07-21 23:09:18.827491', 1000037, '2024-07-21 23:09:18.827491', 1000037, 'Y', 1000007, 'PR000004', 'Thép hộp mạ kẽm Z080: 40mmx40mmx6.0m', '', 100.00, 50.00, 1000000, 20.00, 'Y', NULL, NULL, 'exampleAttribute1', 'exampleAttribute2', 'exampleAttribute3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '33b2fbc7-8c1f-43c3-bd77-a47bddb18df1', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000713, 0, 1000004, '2024-09-26 16:10:58.054989', 1000066, '2024-09-26 16:10:58.05499', 1000066, 'Y', 1000011, 'PROD1000713', 'Banh mi bo kho S', '**', 200000.00, 10000.00, 1000012, 500.00, 'Y', 1000416, 'FOD', 'Size2', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '28545542-eaea-423f-8e54-ccb221ac5555', 'N', 12, 489, NULL, 'PRD ', NULL, NULL, 1, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000089, 1000016, 1000002, '2024-07-21 23:09:44.73648', 1000037, '2024-08-01 12:56:36.778489', 1000037, 'Y', 1000007, 'PR000003', 'Thép hộp mạ kẽm Z120: 100mmx100mmx1.80mmx', 'exampleQrCode', 100.00, 50.00, 1000000, 20.00, 'Y', NULL, NULL, 'exampleAttribute1', 'exampleAttribute2', 'exampleAttribute3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'b382ec68-fe2c-4e4a-a72a-28f72868e75b', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000718, 0, 1000004, '2024-09-26 18:51:18.256641', 1000066, '2024-09-27 17:40:25.154923', 1000066, 'Y', 1000017, 'PROD1000717', 'Banh mi bo', '**', 12100000.00, 100.00, 1000012, 10.00, 'Y', 1000419, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'd607e555-5d67-4195-b687-f72113df9cc0', 'Y', 1, 10, NULL, 'PRD ', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000765, 0, 1000004, '2024-09-28 10:33:39.05222', 1000066, '2024-09-28 10:33:39.052221', 1000066, 'Y', 1000017, 'PROD1000765', 'Banh mi bo kho', '**', 30000.00, 20000.00, NULL, 0.00, 'N', NULL, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '8c0594f4-32d1-46da-ba9a-a31ce3a76c10', NULL, 0, 0, NULL, 'SVC', NULL, NULL, NULL, 100, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000464, 0, 1000004, '2024-09-05 09:22:46.919642', 1000069, '2024-09-05 09:22:46.919642', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son C-F-M', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', '8ed1ff49-d3fd-4c49-b27f-2ab2d73ac554', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000091, 1000016, 1000002, '2024-07-22 16:36:22.370828', 1000037, '2024-07-22 17:13:57.580367', 1000037, 'Y', 1000007, 'PR000005', 'Thép hộp mạ kẽm Z080: 40mmx80mmx6.0m', 'exampleQrCode', 100.00, 50.00, 1000000, 20.00, 'N', NULL, NULL, 'exampleAttribute1', 'exampleAttribute2', 'exampleAttribute3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '21e1d773-d40d-46da-b734-3720820b7ccc', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000443, 0, 1000004, '2024-09-05 09:22:46.825557', 1000069, '2024-09-05 09:22:46.825558', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son B-E-S', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', 'f7b05b9e-4598-4bb3-9026-aaa212b9147a', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000651, 0, 1000004, '2024-09-19 07:14:07.98347', 1000069, '2024-09-19 07:14:07.98347', 1000069, 'Y', 1000025, 'PROD1000645', 'Bac xiu', '**', 200000.00, 100000.00, 1000014, 0.00, 'Y', NULL, 'DRK', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '0620e9cf-5bf1-4c14-8aad-5e49d3229dc1', 'N', 0, 0, NULL, 'PRD', NULL, NULL, NULL, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000129, 0, 1000004, '2024-08-07 09:12:30.208023', 1000069, '2024-09-04 02:00:45.607741', 1000069, 'Y', 1000016, 'PR000018', 'Gà nướng đất sét', '**', 20050.00, 50.00, 1000013, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, 'exampleAttribute4', 'exampleAttribute5', 'exampleAttribute6', 'exampleAttribute7', 'exampleAttribute8', 'exampleAttribute9', 'exampleAttribute10', 'exampleDescription', 'f68a33dc-60ea-4daa-9406-8ba040a8c357', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000479, 0, 1000004, '2024-09-05 09:29:12.246084', 1000069, '2024-09-05 09:29:12.246085', 1000069, 'Y', 1000017, NULL, 'Cua đồng 🦀🦀🦀 B-X-Z', '**', 189000.00, 100000.00, 1000013, 800.00, 'Y', NULL, 'FOD', 'B', 'X', 'Z', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Cua dong siu ngon', '817aa70a-d183-45ff-80e9-55250710290c', 'Y', 200, 789, 1000002, 'RGD', NULL, NULL, 1, 123, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000104, 0, 1000004, '2024-08-04 12:06:30.949601', 1000069, '2024-09-04 02:00:39.303072', 1000069, 'Y', 1000011, 'PR000007', 'Thăn Bò Cuộn Nấm Kim Châm', '**', 20051.00, 60.00, 1000010, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '3bccdc76-4092-46dd-89d6-ebab4f143b4c', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000743, 0, 1000004, '2024-09-27 15:21:48.894663', 1000066, '2024-09-27 15:21:48.894663', 1000066, 'Y', 1000017, 'PROD1000737', 'Banh mi cham sua', '**', 30000.00, 20000.00, 1000013, 1000.00, 'Y', NULL, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '30ac7da4-9f41-47f5-b5b0-803ad5195bda', 'Y', 100, 900, NULL, 'PRD', NULL, NULL, NULL, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000769, 0, 1000004, '2024-09-28 10:51:04.863027', 1000066, '2024-09-28 10:51:04.863027', 1000066, 'Y', 1000017, 'PROD1000769', 'Banh mi bo kho', '**', 30000.00, 20000.00, NULL, 0.00, 'N', NULL, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '050baf49-9710-43ad-8f28-ab66bef98e83', NULL, 0, 0, NULL, 'SVC', NULL, NULL, NULL, 100, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000509, 0, 1000004, '2024-09-05 09:32:37.211032', 1000069, '2024-09-12 15:00:36.677351', 1000069, 'Y', 1000023, NULL, 'Banh mi thap cam S-B', '**', 20000.00, 10000.00, 1000012, 817.00, 'Y', NULL, 'FOD', 'Size,Size1', 'S', 'B', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'f2d5d929-1716-4b59-933d-1326cbdbb318', 'Y', 100, 800, 1000004, 'PRD ', NULL, NULL, 1, 10, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000627, 0, 1000004, '2024-09-18 21:53:32.468252', 1000069, '2024-09-18 21:53:32.468254', 1000069, 'Y', 1000025, 'PROD1000627', 'Cafe đen S', '**', 15000.00, 10000.00, 1000011, 20.00, NULL, NULL, NULL, NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'exampleDescription', '62084d10-f169-473f-baae-2ecf0eb9e3be', 'Y', 0, 100, NULL, NULL, 1000625, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000782, 0, 1000004, '2024-09-28 11:58:15.241248', 1000066, '2024-09-28 11:58:15.241248', 1000066, 'Y', 1000423, 'PROD1000776', 'Banh mi chaca', '**', 20000.00, 10000.00, 1000012, 10.00, 'Y', NULL, 'FOD', 'Size1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '343794b5-93b3-4411-bfb2-4087f6cf5a9a', 'Y', 1, 10, NULL, 'SVC', NULL, NULL, NULL, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000783, 0, 1000004, '2024-09-28 11:58:16.15106', 1000066, '2024-09-28 11:58:16.15106', 1000066, 'Y', 1000423, 'PROD1000783', 'Banh mi chaca L', '**', 20000.00, 10000.00, 1000012, 10.00, 'Y', NULL, 'FOD', NULL, 'L', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '77aa010d-84c1-4e9b-bcc8-076a4af98fd8', 'N', 1, 10, NULL, 'SVC', 1000782, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000784, 0, 1000004, '2024-09-28 11:58:16.255359', 1000066, '2024-09-28 11:58:16.255359', 1000066, 'Y', 1000423, 'PROD1000784', 'Banh mi chaca M', '**', 20000.00, 10000.00, 1000012, 10.00, 'Y', NULL, 'FOD', NULL, 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'db66fd3d-bdb4-4874-a820-3474cce27cb5', 'N', 1, 10, NULL, 'SVC', 1000782, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000374, 0, 1000004, '2024-09-02 22:44:57.208017', 1000069, '2024-09-04 14:13:15.425626', 1000069, 'Y', 1000021, NULL, 'Tra sua tran chau duong den S', '**', 30000.00, 10000.00, 1000012, 1000.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '27426cd7-a255-4b04-90b6-4f14e42e74a5', 'Y', 800, 1000, 1000003, 'CBP', NULL, NULL, 1, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000389, 0, 1000004, '2024-09-04 09:23:01.512915', 1000069, '2024-09-04 02:00:46.521554', 1000069, 'Y', 1000020, NULL, 'Lau thai chua cay S', '**', 120000.00, 100000.00, 1000014, 10.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '8d46c1ca-1f6a-41e2-abb5-8208d548d990', 'Y', 1, 10, 1000003, 'PRD ', NULL, NULL, 1, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000413, 0, 1000004, '2024-09-05 09:22:46.590856', 1000069, '2024-09-05 09:22:46.590858', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', '1452c4ca-38ad-4564-b54c-030f64e5db81', 'N', 50, 198, 1000002, 'PRD ', NULL, NULL, NULL, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000437, 0, 1000004, '2024-09-05 09:22:46.808506', 1000069, '2024-09-05 09:22:46.808507', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son B-D-S', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', 'e9b04fc3-210f-4309-8c5f-96df37ed3ffd', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000785, 0, 1000004, '2024-09-28 11:58:16.358311', 1000066, '2024-09-28 11:58:16.358311', 1000066, 'Y', 1000423, 'PROD1000785', 'Banh mi chaca S', '**', 20000.00, 10000.00, 1000012, 10.00, 'Y', NULL, 'FOD', NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'e4b8c26b-f79d-49cb-b326-a1b67b780d43', 'N', 1, 10, NULL, 'SVC', 1000782, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000642, 0, 1000004, '2024-09-18 23:25:57.247664', 1000069, '2024-09-18 23:25:57.247664', 1000069, 'Y', 1000025, 'PROD1000628', 'Bac xiu', '**', 200000.00, 100000.00, 1000014, 0.00, 'Y', NULL, 'DRK', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '75716928-fa0c-4c51-95a9-4f6f9c0d0df5', 'N', 0, 0, NULL, 'PRD', NULL, NULL, NULL, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000723, 0, 1000004, '2024-09-27 11:36:47.898403', 1000066, '2024-09-27 11:36:47.898404', 1000066, 'Y', 1000422, 'HHH09', 'Trà sữa truyền thống', '**', 40000.00, 30000.00, 1000018, 20.00, 'Y', 1000424, 'DRK', 'Size', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '3c80f117-3708-4d00-a197-a9bd4eba15a4', 'N', 0, 0, NULL, 'RGD', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000724, 0, 1000004, '2024-09-27 11:36:48.399361', 1000066, '2024-09-27 11:36:48.399361', 1000066, 'Y', 1000422, 'PROD1000724', 'Trà sữa truyền thống L', '**', 60000.00, 30000.00, 1000018, 20.00, 'Y', 1000425, 'DRK', 'Size', 'L', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '7234054b-c41f-4c9f-abd1-4e79859cb12e', 'N', 0, 0, NULL, 'RGD', NULL, NULL, 1, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000725, 0, 1000004, '2024-09-27 11:36:48.921008', 1000066, '2024-09-27 11:36:48.921008', 1000066, 'Y', 1000422, 'PROD1000725', 'Trà sữa truyền thống M', '**', 50000.00, 30000.00, 1000018, 20.00, 'Y', 1000426, 'DRK', 'Size', 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'cc640938-a3a5-4f0e-a80e-f8d37ac216f6', 'N', 0, 0, NULL, 'RGD', NULL, NULL, 1, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000726, 0, 1000004, '2024-09-27 11:36:49.434031', 1000066, '2024-09-27 11:36:49.434032', 1000066, 'Y', 1000422, 'PROD1000726', 'Trà sữa truyền thống S', '**', 40000.00, 30000.00, 1000018, 20.00, 'Y', 1000427, 'DRK', 'Size', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'aa38385d-f1c0-4028-be5f-62c1b9ec5de0', 'N', 0, 0, NULL, 'RGD', NULL, NULL, 1, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000621, 0, 1000004, '2024-09-18 21:18:35.252186', 1000069, '2024-09-18 21:18:35.252186', 1000069, 'Y', 1000025, 'PROD1000591', 'Cafe đen', '**', 15000.00, 10000.00, 1000011, 20.00, 'N', 1000363, NULL, 'size', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'd135dc5c-d467-45c6-828c-37ae6359bafc', NULL, 0, 100, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000766, 0, 1000004, '2024-09-28 10:48:54.664368', 1000066, '2024-09-28 10:48:54.664368', 1000066, 'Y', 1000017, 'PROD1000766', 'Banh mi bo kho', '**', 30000.00, 20000.00, NULL, 0.00, 'N', NULL, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'e16aa65e-5c77-4a13-9580-3014bb106338', NULL, 0, 0, NULL, 'SVC', NULL, NULL, NULL, 100, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000767, 0, 1000004, '2024-09-28 10:50:00.204722', 1000066, '2024-09-28 10:50:00.204722', 1000066, 'Y', 1000017, 'PROD1000767', 'Banh mi bo kho', '**', 30000.00, 20000.00, NULL, 0.00, 'N', NULL, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'fc495c3f-85ba-4ba8-b518-c7f66fdb5c8c', NULL, 0, 0, NULL, 'SVC', NULL, NULL, NULL, 100, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000768, 0, 1000004, '2024-09-28 10:50:47.344206', 1000066, '2024-09-28 10:50:47.344206', 1000066, 'Y', 1000017, 'PROD1000768', 'Banh mi bo kho', '**', 30000.00, 20000.00, NULL, 0.00, 'N', NULL, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '8863e4cc-885f-4e4d-8f83-c47058f5e12a', NULL, 0, 0, NULL, 'SVC', NULL, NULL, NULL, 100, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000622, 0, 1000004, '2024-09-18 21:18:35.429548', 1000069, '2024-09-18 21:18:35.429548', 1000069, 'Y', 1000025, 'PROD1000622', 'Cafe đen M', '**', 15000.00, 10000.00, 1000011, 20.00, NULL, NULL, NULL, NULL, 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'exampleDescription', '37846aca-747b-48b1-9ab8-1a863d5af174', 'Y', 0, 100, NULL, NULL, 1000621, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000488, 0, 1000004, '2024-09-05 09:29:12.291552', 1000069, '2024-09-05 09:29:12.291553', 1000069, 'Y', 1000017, NULL, 'Cua đồng 🦀🦀🦀 C-Y-Z', '**', 189000.00, 100000.00, 1000013, 800.00, 'Y', NULL, 'FOD', 'C', 'Y', 'Z', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Cua dong siu ngon', '8588e8ae-8bb3-4141-9d49-598bc517ced6', 'Y', 200, 789, 1000002, 'RGD', NULL, NULL, 1, 123, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000661, 0, 1000004, '2024-09-19 11:20:59.529273', 1000069, '2024-09-19 11:20:59.529275', 1000069, 'Y', 1000025, 'PROD10002165', 'Cafe đen', '**', 15000.00, 10000.00, 1000011, 20.00, 'N', NULL, NULL, 'size', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '41643e0d-0318-4b5a-b9b1-0b1156edc33a', NULL, 0, 100, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000662, 0, 1000004, '2024-09-19 11:20:59.580869', 1000069, '2024-09-19 11:20:59.580872', 1000069, 'Y', 1000025, 'PROD1000662', 'Cafe đen M', '**', 15000.00, 10000.00, 1000011, 20.00, NULL, NULL, NULL, NULL, 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'exampleDescription', '8bdd3093-3074-4f93-a15c-30bfe6b4ee3d', 'Y', 0, 100, NULL, NULL, 1000661, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000485, 0, 1000004, '2024-09-05 09:29:12.275376', 1000069, '2024-09-05 09:29:12.275377', 1000069, 'Y', 1000017, NULL, 'Cua đồng 🦀🦀🦀 C-X-Z', '**', 189000.00, 100000.00, 1000013, 800.00, 'Y', NULL, 'FOD', 'C', 'X', 'Z', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Cua dong siu ngon', '2b868943-711d-4a2b-ab48-4bc9434ed2f5', 'Y', 200, 789, 1000002, 'RGD', NULL, NULL, 1, 123, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000425, 0, 1000004, '2024-09-05 09:22:46.749143', 1000069, '2024-09-05 09:22:46.749144', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son A-E-S', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', 'd21c1241-1f79-4cfb-ab6c-5d82def957c8', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000410, 0, 1000004, '2024-09-04 17:35:07.157166', 1000069, '2024-09-04 17:35:07.157167', 1000069, 'Y', 1000017, NULL, 'Banh mi cha ca S', '**', 200000.00, 100000.00, 1000012, 10.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh mi Nha Trang', '1ee2c33e-a96c-49b0-aed7-050d45f92166', 'Y', 1, 10, 1000004, 'PRD ', NULL, NULL, 1, 10, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000491, 0, 1000004, '2024-09-05 09:32:37.178108', 1000069, '2024-09-05 09:32:37.178108', 1000069, 'Y', 1000023, NULL, 'Banh mi thap cam', '**', 20000.00, 10000.00, 1000012, 800.00, 'Y', NULL, 'FOD', 'Size,Size1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '3a10bfad-1852-4514-a0b1-84813ab4ac58', 'N', 100, 800, 1000004, 'PRD ', NULL, NULL, NULL, 10, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000497, 0, 1000004, '2024-09-05 09:32:37.193273', 1000069, '2024-09-05 09:32:37.193274', 1000069, 'Y', 1000023, NULL, 'Banh mi thap cam L-B', '**', 20000.00, 10000.00, 1000012, 800.00, 'Y', NULL, 'FOD', 'Size,Size1', 'L', 'B', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '9dd45f77-d556-40ec-9ef1-9b6d9aba1156', 'Y', 100, 800, 1000004, 'PRD ', NULL, NULL, 1, 10, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000731, 0, 1000004, '2024-09-27 12:12:06.238061', 1000066, '2024-09-27 12:12:06.238061', 1000066, 'Y', 1000012, 'PROD1000731', 'Nuoc uong trai cay L', '**', 30000.00, 20000.00, 1000012, 1000.00, 'Y', 1000432, 'DRK', NULL, 'L', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'f846c1bb-4d9a-4ef6-96f7-63c0fa5931c1', 'N', 12, 989, NULL, 'SVC', 1000730, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000377, 0, 1000004, '2024-09-03 22:22:20.589377', 1000069, '2024-09-04 14:13:15.436205', 1000069, 'Y', 1000021, NULL, 'Lau ga la giang', '**', 120000.00, 100000.00, 1000012, 10.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Mota chi tiet', '22cc003a-bfed-42ac-817a-3f8024fc260d', 'N', 1, 10, 1000003, 'PRD ', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000494, 0, 1000004, '2024-09-05 09:32:37.188409', 1000069, '2024-09-05 09:32:37.188409', 1000069, 'Y', 1000023, NULL, 'Banh mi thap cam L-A', '**', 20000.00, 10000.00, 1000012, 800.00, 'Y', NULL, 'FOD', 'Size,Size1', 'L', 'A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'ed559da2-66d2-4fd3-ba95-4fd4864b51ef', 'Y', 100, 800, 1000004, 'PRD ', NULL, NULL, 1, 10, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000401, 0, 1000004, '2024-09-04 09:49:27.11932', 1000069, '2024-09-04 02:00:46.570109', 1000069, 'Y', 1000017, NULL, 'Banh cuon binh dinh', '**', 30000.00, 10000.00, 1000012, 10.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'b2d4fbd7-5161-4c51-aebc-567ce62c036a', 'N', 1, 10, 1000003, 'PRD ', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000407, 0, 1000004, '2024-09-04 17:35:06.844185', 1000069, '2024-09-04 17:35:06.844187', 1000069, 'Y', 1000017, NULL, 'Banh mi cha ca', '**', 200000.00, 100000.00, 1000012, 10.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh mi Nha Trang', '8160f1d1-f395-4c9d-b277-2f7783fb7101', 'N', 1, 10, 1000004, 'PRD ', NULL, NULL, NULL, 10, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000386, 0, 1000004, '2024-09-04 09:23:01.505124', 1000069, '2024-09-04 02:00:46.504816', 1000069, 'Y', 1000020, NULL, 'Lau thai chua cay M', '**', 120000.00, 100000.00, 1000014, 10.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '75d976ed-7502-43df-a98e-5c2ac86becfb', 'Y', 1, 10, 1000003, 'PRD ', NULL, NULL, 1, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000625, 0, 1000004, '2024-09-18 21:53:32.34744', 1000069, '2024-09-18 21:53:32.347441', 1000069, 'Y', 1000025, 'PROD1000624', 'Cafe đen', '**', 15000.00, 10000.00, 1000011, 20.00, 'N', 1000365, NULL, 'size', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '6384f8e3-5ac9-4172-b892-a8a1aedd22a6', NULL, 0, 100, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000672, 0, 1000004, '2024-09-19 11:27:17.784007', 1000069, '2024-09-19 11:27:17.78401', 1000069, 'Y', 1000031, 'PROD1000672', 'Pho kho gia lai S', '**', 30000.00, 20000.00, 1000012, 100.00, 'Y', 1000397, 'FOD', 'Size', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '1f30aec4-890b-416c-a29a-7701eeeb36fb', 'Y', 1, 99, NULL, 'PRD ', NULL, NULL, 1, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000714, 0, 1000004, '2024-09-26 16:14:05.606992', 1000066, '2024-09-26 16:14:05.606993', 1000066, 'Y', 1000011, 'PROD1000714', 'Thit bo nuong', '**', 200000.00, 100000.00, 1000012, 100.00, 'Y', NULL, 'FOD', 'Size2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'd2efb767-ce33-4c83-9b50-e7657f7ce519', 'Y', 12, 100, NULL, 'PRD ', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000715, 0, 1000004, '2024-09-26 16:14:05.629962', 1000066, '2024-09-26 16:14:05.629962', 1000066, 'Y', 1000011, 'PROD1000715', 'Thit bo nuong M', '**', 200000.00, 100000.00, 1000012, 100.00, 'Y', NULL, 'FOD', 'Size2', 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'a2f7d305-980a-43df-9028-300d3122cee0', 'N', 12, 100, NULL, 'PRD ', NULL, NULL, 1, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000663, 0, 1000004, '2024-09-19 11:20:59.6402', 1000069, '2024-09-19 11:20:59.640203', 1000069, 'Y', 1000025, 'PROD1000663', 'Cafe đen S', '**', 15000.00, 10000.00, 1000011, 20.00, NULL, NULL, NULL, NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'exampleDescription', 'cabf76b2-f2ae-4110-bd39-8f053c44a563', 'Y', 0, 100, NULL, NULL, 1000661, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000732, 0, 1000004, '2024-09-27 12:12:06.691291', 1000066, '2024-09-27 12:12:06.691291', 1000066, 'Y', 1000012, 'PROD1000732', 'Nuoc uong trai cay M', '**', 30000.00, 20000.00, 1000012, 1000.00, 'Y', 1000433, 'DRK', NULL, 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '300db2a3-aa79-4922-8e29-14f7d250edfd', 'N', 12, 989, NULL, 'SVC', 1000730, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000716, 0, 1000004, '2024-09-26 16:14:05.648824', 1000066, '2024-09-26 23:04:01.832968', 1000066, 'Y', 1000011, 'PROD1000716', 'Thit bo nuong S', '**', 3000.00, 100000.00, 1000012, 100.00, 'Y', NULL, 'FOD', 'Size2', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '237993e4-ae44-4753-855d-930879fea271', 'N', 12, 100, NULL, 'PRD ', NULL, NULL, 1, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000670, 0, 1000004, '2024-09-19 11:27:15.488802', 1000069, '2024-09-19 11:27:15.488806', 1000069, 'Y', 1000031, 'PROD88952', 'Pho kho gia lai', '**', 30000.00, 20000.00, 1000012, 100.00, 'Y', 1000395, 'FOD', 'Size', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '4aa2eb1d-fae3-49bf-bb87-8db7171ae661', 'N', 1, 99, NULL, 'PRD ', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000671, 0, 1000004, '2024-09-19 11:27:16.663056', 1000069, '2024-09-19 11:27:16.663058', 1000069, 'Y', 1000031, 'PROD1000671', 'Pho kho gia lai M', '**', 30000.00, 20000.00, 1000012, 100.00, 'Y', 1000396, 'FOD', 'Size', 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '387bcf99-fb30-4b66-a758-58b7461b8d3b', 'Y', 1, 99, NULL, 'PRD ', NULL, NULL, 1, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000398, 0, 1000004, '2024-09-04 09:47:43.979436', 1000069, '2024-09-04 15:15:05.191186', 1000069, 'Y', 1000020, NULL, 'Banh trung nuong B', '**', 120000.00, 100000.00, 1000014, 10.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh nong va thom', 'fedaf007-0756-4dde-9401-04d385bb2556', 'Y', 1, 10, 1000003, 'PRD ', NULL, NULL, 1, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000733, 0, 1000004, '2024-09-27 12:12:07.137871', 1000066, '2024-09-27 12:12:07.137872', 1000066, 'Y', 1000012, 'PROD1000733', 'Nuoc uong trai cay S', '**', 30000.00, 20000.00, 1000012, 1000.00, 'Y', 1000434, 'DRK', NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '82760918-4db4-43bb-885d-0ab01e84fda1', 'N', 12, 989, NULL, 'SVC', 1000730, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000136, 0, 1000004, '2024-08-07 09:13:50.255333', 1000069, '2024-09-04 02:00:45.541015', 1000069, 'Y', 1000016, 'PR000022', 'Gà hấp hành', '**', 20050.00, 50.00, 1000013, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, 'exampleAttribute4', 'exampleAttribute5', 'exampleAttribute6', 'exampleAttribute7', 'exampleAttribute8', 'exampleAttribute9', 'exampleAttribute10', 'exampleDescription', '0d95ecc4-80a9-469a-a67f-05cc9ae51be3', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000730, 0, 1000004, '2024-09-27 12:12:05.758931', 1000066, '2024-09-27 17:40:22.816062', 1000066, 'Y', 1000012, 'PROD1000727', 'Nuoc uong trai cay', '**', 30000.00, 20000.00, 1000012, 1000.00, 'Y', 1000431, 'DRK', 'Size1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '44fa99ea-7871-470c-96a7-b345f21fac36', 'Y', 12, 989, NULL, 'SVC', NULL, NULL, NULL, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000770, 0, 1000004, '2024-09-28 11:14:01.724966', 1000066, '2024-09-28 11:14:01.724967', 1000066, 'Y', 1000423, 'PROD1000770', 'Sữa ông thọ', '**', 2000.00, 1000.00, NULL, 0.00, 'N', NULL, 'DRK', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'ae595ff8-aa0d-4bf3-8221-2e67f73022f7', 'Y', 0, 0, NULL, 'PRD ', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000786, 0, 1000004, '2024-09-28 11:58:26.065583', 1000066, '2024-09-28 11:58:26.065585', 1000066, 'Y', 1000423, 'PROD1000786', 'Cafe sữa đặc 2 ', '**', 12000.00, 10000.00, NULL, 0.00, 'N', NULL, 'DRK', 'Size', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'd6ed8384-16a7-4020-bc65-7715d2e243f6', NULL, 0, 0, NULL, 'SVC', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000734, 0, 1000004, '2024-09-27 13:11:42.213003', 1000066, '2024-09-27 13:11:42.213003', 1000066, 'Y', 1000017, 'PROD1000734', 'Lau ga la giang', '**', 30000.00, 20000.00, 1000012, 100.00, 'Y', 1000435, 'FOD', 'Size', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '01fb74da-ed41-4101-b81b-11e7d417435c', 'Y', 1, 100, NULL, 'PRD ', NULL, NULL, NULL, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000132, 0, 1000004, '2024-08-07 09:13:10.801467', 1000069, '2024-09-04 02:00:45.675051', 1000069, 'Y', 1000016, 'PR000020', 'Gà nướng nướng giấy bạc', '**', 20050.00, 50.00, 1000013, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, 'exampleAttribute4', 'exampleAttribute5', 'exampleAttribute6', 'exampleAttribute7', 'exampleAttribute8', 'exampleAttribute9', 'exampleAttribute10', 'exampleDescription', '7f20acff-f45b-48af-be8d-15cc98db86ff', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000473, 0, 1000004, '2024-09-05 09:29:12.120152', 1000069, '2024-09-05 09:29:12.120153', 1000069, 'Y', 1000017, NULL, 'Cua đồng 🦀🦀🦀 A-X-Z', '**', 189000.00, 100000.00, 1000013, 800.00, 'Y', NULL, 'FOD', 'A', 'X', 'Z', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Cua dong siu ngon', '0acf121a-6eb3-4ec9-b30d-5a4f16b717bb', 'Y', 200, 789, 1000002, 'RGD', NULL, NULL, 1, 123, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000416, 0, 1000004, '2024-09-05 09:22:46.712827', 1000069, '2024-09-05 09:22:46.712828', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son A-D-M', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', '13ee051d-784a-40b2-ae50-190dce9bb16b', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000446, 0, 1000004, '2024-09-05 09:22:46.832944', 1000069, '2024-09-05 09:22:46.832944', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son B-F-M', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', '6f27b8d1-edb8-43e9-8abe-ff6cb222c01d', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000735, 0, 1000004, '2024-09-27 13:11:43.604719', 1000066, '2024-09-27 13:11:43.60472', 1000066, 'Y', 1000017, 'PROD1000735', 'Lau ga la giang A', '**', 30000.00, 20000.00, 1000012, 100.00, 'Y', 1000436, 'FOD', NULL, 'A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '77c97942-f98f-43be-b721-3397556f320c', 'N', 1, 100, NULL, 'PRD ', 1000734, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000371, 0, 1000004, '2024-09-02 22:44:57.201828', 1000069, '2024-09-04 14:13:15.422815', 1000069, 'Y', 1000021, NULL, 'Tra sua tran chau duong den M', '**', 30000.00, 10000.00, 1000012, 1000.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '9b8c9c74-0e1c-4255-8574-a5a709dace49', 'Y', 800, 1000, 1000003, 'CBP', NULL, NULL, 1, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000434, 0, 1000004, '2024-09-05 09:22:46.798956', 1000069, '2024-09-05 09:22:46.798957', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son B-D-M', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', '5e75b03d-4709-4c5f-8d45-98ab098dda96', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000368, 0, 1000004, '2024-09-02 22:44:57.145339', 1000069, '2024-09-04 14:13:15.419436', 1000069, 'Y', 1000021, NULL, 'Tra sua tran chau duong den', '**', 30000.00, 10000.00, 1000012, 1000.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '2b218196-2473-4c9f-9124-1addde785a82', 'N', 800, 1000, 1000003, 'CBP', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000419, 0, 1000004, '2024-09-05 09:22:46.722084', 1000069, '2024-09-05 09:22:46.722085', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son A-D-S', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', '7f67401d-d9f7-4431-ab26-fce8468c7cd1', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000574, 1000016, 1000009, '2024-09-11 18:14:23.700732', 1000071, '2024-09-13 16:21:57.805917', 1000071, 'Y', 1000033, 'PROD1000573', 'Thép hộp mạ kẽm Z080: 14mmx14mmx1.40mmx6.0m', '**', 123000.00, 32000.00, 1000017, 607.00, 'Y', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '350b0b04-9af6-4360-888b-0ef5a0607ce2', 'N', 0, 0, NULL, 'RGD', NULL, NULL, NULL, 0, NULL, 'VTS', 0);
INSERT INTO pos.d_product VALUES (1000440, 0, 1000004, '2024-09-05 09:22:46.816153', 1000069, '2024-09-05 09:22:46.816153', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son B-E-M', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', 'ada744a3-0fc0-49e7-938e-7973ebda4444', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000452, 0, 1000004, '2024-09-05 09:22:46.854447', 1000069, '2024-09-05 09:22:46.854447', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son C-D-M', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', 'e2c4d7d1-3943-4284-b312-7338b19540dd', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000736, 0, 1000004, '2024-09-27 13:11:47.525102', 1000066, '2024-09-27 13:11:47.525103', 1000066, 'Y', 1000017, 'PROD1000736', 'Lau ga la giang S', '**', 30000.00, 20000.00, 1000012, 100.00, 'Y', 1000437, 'FOD', NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '031414ff-6677-4d88-9339-7bbc8d050371', 'N', 1, 100, NULL, 'PRD ', 1000734, NULL, 1, 10, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000461, 0, 1000004, '2024-09-05 09:22:46.908884', 1000069, '2024-09-05 09:22:46.908884', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son C-E-S', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', '09c087fa-ea8d-46ac-98bd-cfd941d9efb8', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000506, 0, 1000004, '2024-09-05 09:32:37.206769', 1000069, '2024-09-27 16:32:39.579984', 1000066, 'Y', 1000023, NULL, 'Banh mi thap cam S-A', '**', 20000.00, 10000.00, 1000012, 803.00, 'Y', NULL, 'FOD', 'Size,Size1', 'S', 'A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '5296c168-740a-4405-84e9-c0171e6485f1', 'Y', 100, 800, 1000004, 'PRD ', NULL, NULL, 1, 10, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000771, 0, 1000004, '2024-09-28 11:15:06.055885', 1000066, '2024-09-28 11:15:06.055886', 1000066, 'Y', 1000423, 'PROD1000771', 'Cafe Sữa Đặc', '**', 18000.00, 16000.00, NULL, 0.00, 'N', NULL, 'FOD', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'caed48f2-6851-4413-9735-447ebcd75d40', 'N', 0, 0, NULL, 'PRD ', NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO pos.d_product VALUES (1000140, 0, 1000004, '2024-08-07 09:36:49.212538', 1000069, '2024-09-04 15:20:48.009515', 1000069, 'Y', 1000016, 'PR000025', 'Combo Gà Quay', '**', 20050.00, 50.00, 1000012, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, 'exampleAttribute4', 'exampleAttribute5', 'exampleAttribute6', 'exampleAttribute7', 'exampleAttribute8', 'exampleAttribute9', 'exampleAttribute10', 'exampleDescription', '14d30cd4-7f3e-45ec-a963-8d3a2cd6a17e', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000134, 0, 1000004, '2024-08-07 09:13:29.518318', 1000069, '2024-09-04 02:00:45.702478', 1000069, 'Y', 1000016, 'PR000021', 'Gà nướng nướng ống tre', '**', 20050.00, 50.00, 1000013, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, 'exampleAttribute4', 'exampleAttribute5', 'exampleAttribute6', 'exampleAttribute7', 'exampleAttribute8', 'exampleAttribute9', 'exampleAttribute10', 'exampleDescription', '8b030807-95eb-493b-925f-a1b2ccbe6c95', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000127, 0, 1000004, '2024-08-07 09:12:12.14143', 1000069, '2024-09-04 02:00:45.574779', 1000069, 'Y', 1000016, 'PR000017', 'Gà nướng muối ớt', '**', 20050.00, 50.00, 1000013, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, 'exampleAttribute4', 'exampleAttribute5', 'exampleAttribute6', 'exampleAttribute7', 'exampleAttribute8', 'exampleAttribute9', 'exampleAttribute10', 'exampleDescription', 'fb83378d-df23-4087-a3b8-a13787a448da', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000476, 0, 1000004, '2024-09-05 09:29:12.227909', 1000069, '2024-09-05 09:29:12.227909', 1000069, 'Y', 1000017, NULL, 'Cua đồng 🦀🦀🦀 A-Y-Z', '**', 189000.00, 100000.00, 1000013, 800.00, 'Y', NULL, 'FOD', 'A', 'Y', 'Z', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Cua dong siu ngon', '08c59b50-0823-4e46-a588-a920de6bf982', 'Y', 200, 789, 1000002, 'RGD', NULL, NULL, 1, 123, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000106, 0, 1000004, '2024-08-04 12:09:40.176206', 1000069, '2024-09-04 04:34:21.036267', 1000069, 'Y', 1000012, 'PR000009', 'Shochu Tensonkorin Imo 720ml', '**', 20050.00, 50.00, 1000011, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'f21eafbb-2ca7-44b5-9fd5-95ee50aeff7d', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000108, 0, 1000004, '2024-08-04 12:10:19.14418', 1000069, '2024-09-04 04:34:20.996148', 1000069, 'Y', 1000012, 'PR000011', 'Sake Suishin Kome no Kiwami Junmai 15-16% 720ml', '**', 20050.00, 50.00, 1000011, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'b9d5c078-e89a-43ac-bc34-9b0aa1f43e30', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000119, 0, 1000004, '2024-08-04 12:21:04.50402', 1000069, '2024-09-04 14:13:15.368467', 1000069, 'Y', 1000014, 'PR000016', 'Thịt, Sườn Dê Nướng Nướng Mọi', '**', 20050.00, 50.00, 1000012, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'eb2a4223-8fa3-40f2-a36c-a5f18bbdc649', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000111, 0, 1000004, '2024-08-04 12:15:41.444076', 1000069, '2024-09-04 15:20:47.98204', 1000069, 'Y', 1000013, 'PR000013', 'Xào lăn', '**', 20050.00, 50.00, 1000012, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '28024dfd-fa00-45a1-8c7c-c3967c1b2aa3', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000482, 0, 1000004, '2024-09-05 09:29:12.262778', 1000069, '2024-09-05 09:29:12.262779', 1000069, 'Y', 1000017, NULL, 'Cua đồng 🦀🦀🦀 B-Y-Z', '**', 189000.00, 100000.00, 1000013, 800.00, 'Y', NULL, 'FOD', 'B', 'Y', 'Z', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Cua dong siu ngon', 'a5c4ca7b-5c26-43cc-9daa-c0838d01940a', 'Y', 200, 789, 1000002, 'RGD', NULL, NULL, 1, 123, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000103, 0, 1000004, '2024-08-04 12:05:48.822935', 1000069, '2024-09-04 01:53:41.984398', 1000069, 'Y', 1000011, 'PR000002', 'Bò Wagyu Nhật A5 Nướng Que', '**', 70000.00, 50000.00, 1000010, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ba0c87de-1622-47fc-8da4-858be439f83d', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000112, 0, 1000004, '2024-08-04 12:15:57.24203', 1000069, '2024-09-04 04:34:21.120088', 1000069, 'Y', 1000013, 'PR000014', 'Đùi thỏ', '**', 20050.00, 50.00, 1000012, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '8abae216-39ec-4f24-ac08-de1f66e7e796', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000139, 0, 1000004, '2024-08-07 09:33:47.26964', 1000069, '2024-09-12 15:09:19.645743', 1000069, 'Y', 1000012, 'PR000024', 'Pepsi', '**', 30000.00, 10000.00, 1000011, 23.00, 'N', NULL, 'DRINK', NULL, NULL, NULL, 'exampleAttribute4', 'exampleAttribute5', 'exampleAttribute6', 'exampleAttribute7', 'exampleAttribute8', 'exampleAttribute9', 'exampleAttribute10', 'exampleDescription', 'd2079047-19fd-4417-9221-1a8f9401c567', 'Y', 0, 100, 1000002, 'RG', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000109, 0, 1000004, '2024-08-04 12:15:19.426606', 1000069, '2024-09-19 07:41:31.710341', 1000069, 'Y', 1000013, 'PR000012', 'Tiết canh thỏ', '**', 20050.00, 50.00, 1000012, 20.00, 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '4c6195c5-68d9-461f-b404-c1c021e611e7', 'Y', 0, 100, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000428, 0, 1000004, '2024-09-05 09:22:46.77719', 1000069, '2024-09-05 09:22:46.777191', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son A-F-M', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', 'aa2e85a2-fc5a-4fb3-bd4e-cd9dd6f3ef31', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000562, 1000016, 1000009, '2024-09-11 17:57:24.636877', 1000071, '2024-09-11 17:57:24.636877', 1000071, 'Y', 1000032, 'PROD1000561', 'Tôn lạnh AZ100 phủ AF: 0.50mmx1200mm G550', '**', 56000.00, 40000.00, 1000016, 1200.00, 'Y', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'f70a88c8-2a2d-4057-828a-718d84462041', 'N', 0, 0, NULL, 'RGD', NULL, NULL, NULL, 0, NULL, 'VTS', 0);
INSERT INTO pos.d_product VALUES (1000536, 1000016, 1000009, '2024-09-11 16:32:36.640331', 1000071, '2024-09-12 15:33:32.488921', 1000071, 'Y', 1000032, 'PROD1000536', 'Tôn lạnh AZ070 phủ AF: 0.18mmx1200mm G550', '**', 43000.00, 20000.00, 1000016, 1006.00, 'Y', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '0c5da061-329f-4bb4-8b8f-233bce9294da', 'N', 0, 0, NULL, 'RGD', NULL, NULL, NULL, 0, NULL, 'VTS', 0);
INSERT INTO pos.d_product VALUES (1000568, 1000016, 1000009, '2024-09-11 18:10:24.89649', 1000071, '2024-09-11 18:10:24.896491', 1000071, 'Y', 1000033, 'PROD1000567', 'Thép hộp mạ kẽm Z080: 13mmx26mmx1.00mmx6.0m', '**', 120000.00, 40000.00, 1000017, 900.00, 'Y', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '73c8dc99-ac07-4fc8-8111-06577e0aa0c9', 'N', 0, 0, NULL, 'RGD', NULL, NULL, NULL, 0, NULL, 'VTS', 0);
INSERT INTO pos.d_product VALUES (1000565, 1000016, 1000009, '2024-09-11 17:59:37.357366', 1000071, '2024-09-12 18:13:12.061294', 1000071, 'Y', 1000032, 'PROD1000564', 'Tôn lạnh AZ100 phủ AF: 0.45mmx1000mm G550', '**', 45000.00, 30000.00, 1000016, 1309.00, 'Y', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '7827fe15-dddc-403a-b113-f023f35e87b0', 'N', 0, 0, NULL, 'RGD', NULL, NULL, NULL, 0, NULL, 'VTS', 0);
INSERT INTO pos.d_product VALUES (1000467, 0, 1000004, '2024-09-05 09:22:46.931828', 1000069, '2024-09-05 09:22:46.931829', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son C-F-S', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', 'efc4b8f6-adef-4ed3-8ad0-83caa6a4edd8', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000470, 0, 1000004, '2024-09-05 09:29:12.100408', 1000069, '2024-09-05 09:29:12.100408', 1000069, 'Y', 1000017, NULL, 'Cua đồng 🦀🦀🦀', '**', 189000.00, 100000.00, 1000013, 800.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Cua dong siu ngon', '60bf7c6c-57b4-4e7e-bb35-b8ddc5122ad4', 'N', 200, 789, 1000002, 'RGD', NULL, NULL, NULL, 123, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000422, 0, 1000004, '2024-09-05 09:22:46.733548', 1000069, '2024-09-05 09:22:46.733549', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son A-E-M', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', '1eb6abd3-7dbc-4d1b-8735-47b2c1c2413a', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000449, 0, 1000004, '2024-09-05 09:22:46.842291', 1000069, '2024-09-05 09:22:46.842292', 1000069, 'Y', 1000017, NULL, 'Banh cuon Tay Son B-F-S', '**', 200000.00, 100000.00, 1000014, 200.00, 'Y', NULL, 'FOD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Banh cuon thom ngon', 'c3d8a309-3be6-4fc1-ac6f-306450c4bcea', 'Y', 50, 198, 1000002, 'PRD ', NULL, NULL, 1, 15, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000682, 0, 1000004, '2024-09-25 03:14:35.299439', 0, '2024-09-25 03:14:35.299439', 0, 'Y', 1000016, 'PRO1000457', 'Trứng gà ', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '23c99e85-1da7-470a-b619-73df982eee66', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000684, 0, 1000004, '2024-09-25 03:14:35.299439', 0, '2024-09-25 03:14:35.299439', 0, 'Y', 1000016, 'PRO1000459', 'Cà chua', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '332d2049-452c-45a9-9f82-61a129f040c6', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000686, 0, 1000004, '2024-09-25 03:17:16.337818', 0, '2024-09-25 03:17:16.337818', 0, 'Y', 1000020, 'PRO1000461', 'Chanh', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ab5fe201-0006-4a59-9935-72e8d064c26b', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000692, 0, 1000004, '2024-09-25 03:17:16.337818', 0, '2024-09-25 03:17:16.337818', 0, 'Y', 1000020, 'PRO1000467', 'Tôm sú', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'a0398e2b-d086-4149-a279-84541f41f7f9', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO pos.d_product VALUES (1000690, 0, 1000004, '2024-09-25 03:17:16.337818', 0, '2024-09-25 03:17:16.337818', 0, 'Y', 1000020, 'PRO1000465', 'Gỏi ngó sen tôm thịt', NULL, NULL, NULL, 1000013, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '7f228793-eda1-43b5-a76f-32736c749932', 'N', NULL, NULL, 1000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);


--
-- TOC entry 5341 (class 0 OID 385779)
-- Dependencies: 246
-- Data for Name: d_product_category; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_product_category VALUES (1000007, 1000003, 1000003, '2024-07-12 00:00:00', 0, '2024-07-12 00:00:00', 0, 'Y', 'OTMK', 'Ống thép mạ kẽm', '4c6e44c5-c4d9-42c6-836e-09d492ece489', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000008, 1000001, 1000002, '2024-07-12 00:00:00', 1000037, '2024-07-12 00:00:00', 1000037, 'Y', 'KNN', 'Tôn lạnh màu', 'adc1ae75-363a-4408-9849-eb903bfaa81f', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000002, 1000001, 1000002, '2024-07-09 00:00:00', 0, '2024-07-09 00:00:00', 0, 'Y', 'TMK', 'Ống nhựa PPR', '4b555da7-e067-4c66-a784-97eaf77f0f74', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000005, 1000001, 1000002, '2024-07-12 00:00:00', 1000037, '2024-07-12 00:00:00', 1000037, 'Y', 'TD', 'Ống nhựa uPVC', '59cd0599-e9b0-4033-89a7-6e3432a4ba4b', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000010, 1000003, 1000003, '2024-07-24 00:00:00', 1000068, '2024-08-03 00:00:00', 1000068, 'Y', 'THD', 'Tôn lạnh', 'cb24b569-d7d0-47fd-9eba-be553f890dc9', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000013, 1000016, 1000004, '2024-08-04 00:00:00', 1000069, '2024-08-04 00:00:00', 1000069, 'Y', '100002', 'Món Thỏ', '43915509-2a31-4e7f-b7f6-543aeb7634b2', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000012, 1000016, 1000004, '2024-08-04 00:00:00', 1000069, '2024-08-04 00:00:00', 1000069, 'Y', '100001', 'Đồ Uống', '13e8e945-fc38-429f-acf3-8e3ae0ffba10', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000014, 1000016, 1000004, '2024-08-04 00:00:00', 1000069, '2024-08-04 00:00:00', 1000069, 'Y', '100003', 'Món Dê', '08c3c58e-89c4-41c4-9d88-d201b0405180', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000011, 1000016, 1000004, '2024-08-04 00:00:00', 1000069, '2024-08-04 00:00:00', 1000069, 'Y', '100000', 'Các món bò', '7ecfeaa1-1232-4e6a-a5a1-32eb74d6cde2', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000016, 1000016, 1000004, '2024-08-07 00:00:00', 1000069, '2024-08-07 00:00:00', 1000069, 'Y', '100003', 'Gà quay - Gà rán', '09bfaca7-201b-4898-a4b2-e340d9d324af', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000019, 1000016, 1000004, '2024-08-26 00:00:00', 1000069, '2024-08-26 00:00:00', 1000069, 'Y', '10000068', 'Món kem', 'a779596b-9073-4bbf-a13a-7379c02f0a23', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000018, 1000016, 1000004, '2024-08-14 00:00:00', 1000069, '2024-08-26 00:00:00', 1000069, 'N', '100004', 'Đồ chay', 'bcf6a2e1-69fa-4431-9822-6a99c9f067cb', 1000017, 'N', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000022, 1000016, 1000004, '2024-08-26 00:00:00', 1000069, '2024-08-26 00:00:00', 1000069, 'N', '1000087', 'Món Mỹ', 'db5c64f6-41d1-4127-9245-2c67a7a1fbc6', 1000017, NULL, NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000020, 1000016, 1000004, '2024-08-26 00:00:00', 1000069, '2024-08-26 00:00:00', 1000069, 'Y', '100004', 'Đồ nóng', '5eb721f6-fb93-4604-bbbf-11c99676a857', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000017, 1000016, 1000004, '2024-08-14 00:00:00', 1000069, '2024-08-26 00:00:00', 1000069, 'Y', '100003', 'Món Ăn', '459427d6-506c-4079-b0f7-f643a5908f71', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000023, 1000016, 1000004, '2024-08-26 00:00:00', 1000069, '2024-08-26 00:00:00', 1000069, 'N', '1239856', 'Món Ý', 'f16ff30c-e80d-4409-98dd-6ce4b501dc9f', 1000017, NULL, NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000021, 1000016, 1000004, '2024-08-26 00:00:00', 1000069, '2024-08-26 00:00:00', 1000069, 'N', '1050002', 'Món nóng', '7b320d76-689b-4238-a23e-bd7faf7e6c3e', 1000017, NULL, NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000025, 1000016, 1000004, '2024-08-30 08:35:46.779116', 1000069, '2024-08-30 08:35:46.779116', 1000069, 'Y', NULL, 'Cafe', '57a7e69b-f455-4718-8dd4-9d15a2442f25', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000032, 1000022, 1000009, '2024-09-11 15:05:26.260591', 1000071, '2024-09-11 15:05:26.260593', 1000071, 'Y', '1000001', 'Tôn lạnh', '50fef776-2066-4da8-927e-56f2d885de79', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000033, 1000022, 1000009, '2024-09-11 15:15:24.381795', 1000071, '2024-09-11 15:15:24.381797', 1000071, 'Y', '1000002', 'Thép hộp', '55a88fa5-19d2-4034-8018-3c59d8d245a4', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000034, 1000022, 1000009, '2024-09-11 15:15:38.952881', 1000071, '2024-09-11 15:15:38.952883', 1000071, 'Y', '1000003', 'Thép V', '97451d6f-c053-4555-b19e-46d765669942', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000030, 1000016, 1000004, '2024-09-11 14:44:28.200734', 1000069, '2024-09-26 17:11:18.868845', 1000066, 'N', NULL, 'Món Á', 'a7351cb8-3c5b-4d5f-af43-af2bbe75477e', 1000029, NULL, NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000028, 1000016, 1000004, '2024-09-11 14:42:58.649815', 1000069, '2024-09-26 23:31:20.662442', 1000066, 'Y', NULL, 'Món Châu Phi', '223752d3-438c-49be-adcd-aa71b53a6d29', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000029, 1000016, 1000004, '2024-09-11 14:43:43.572896', 1000069, '2024-09-26 23:31:23.720979', 1000066, 'Y', NULL, 'Món Châu Phi 1', '77bcf0b7-c74e-4382-84fb-7202b11cf124', 1000028, NULL, NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000031, 1000016, 1000004, '2024-09-11 14:47:55.168811', 1000069, '2024-09-26 23:31:26.385168', 1000066, 'Y', NULL, 'Món tráng miệng', '4c3c5731-0e59-4ee8-a102-4d826af5a6ca', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000405, 0, 1000004, '2024-09-25 23:55:32.487608', 1000069, '2024-09-25 23:55:32.487608', 1000069, 'N', 'HHDonguongTesst', 'HH  Loại sản phẩm Test Đồ uống', 'c87c6f5d-1017-464a-97bf-bcf6f65a49ad', NULL, 'N', NULL, 1006288);
INSERT INTO pos.d_product_category VALUES (1000406, 0, 1000004, '2024-09-25 23:55:32.600077', 1000069, '2024-09-25 23:55:32.600077', 1000069, 'Y', 'DU01', 'Đồ uống', '6c7b7f00-0270-4df3-a569-c8e5d72f5eb4', NULL, 'N', NULL, 1006263);
INSERT INTO pos.d_product_category VALUES (1000407, 0, 1000004, '2024-09-25 23:55:32.650843', 1000069, '2024-09-25 23:55:32.650843', 1000069, 'N', 'TESTHANGHOA', 'Test nhóm hàng hóa', '6c0d074f-167a-41c1-83d8-64800998563c', NULL, 'N', NULL, 1006291);
INSERT INTO pos.d_product_category VALUES (1000408, 0, 1000004, '2024-09-25 23:55:32.709627', 1000069, '2024-09-25 23:55:32.709627', 1000069, 'N', 'SynCategory25', 'SynCategory25', 'c6523258-c1b6-46da-8cd6-35a36ac18b81', NULL, 'N', NULL, 1006446);
INSERT INTO pos.d_product_category VALUES (1000409, 0, 1000004, '2024-09-25 23:55:32.740872', 1000069, '2024-09-25 23:55:32.740872', 1000069, 'N', 'SynCategory26', 'SynCategory26', '55f77190-2f36-4250-8ecb-b5548ae586b1', NULL, 'N', NULL, 1006447);
INSERT INTO pos.d_product_category VALUES (1000410, 0, 1000004, '2024-09-25 23:55:32.782605', 1000069, '2024-09-25 23:55:32.782605', 1000069, 'N', 'DVT01', 'Dịch vụ tắm khoán', 'f82428a9-f0cc-494a-a062-ccae38207d93', NULL, 'N', NULL, 1006454);
INSERT INTO pos.d_product_category VALUES (1000411, 0, 1000004, '2024-09-25 23:55:32.85893', 1000069, '2024-09-25 23:55:32.85893', 1000069, 'Y', '001', 'Đồ ăn', '8cf0116b-c938-46b5-bda3-9a29ae28610a', NULL, 'N', NULL, 1006458);
INSERT INTO pos.d_product_category VALUES (1000412, 0, 1000004, '2024-09-25 23:55:33.145474', 1000069, '2024-09-25 23:55:33.145474', 1000069, 'N', 'FO', 'Open Food', '6268757f-27fd-4dff-9bcb-610bef5b2d72', NULL, 'N', NULL, 1006467);
INSERT INTO pos.d_product_category VALUES (1000413, 0, 1000004, '2024-09-25 23:55:33.744815', 1000069, '2024-09-25 23:55:33.744815', 1000069, 'Y', 'DV01', '4. Dịch vụ', '7396f455-cef5-4e10-845b-515c9e7e0fc9', NULL, 'N', NULL, 1006251);
INSERT INTO pos.d_product_category VALUES (1000414, 0, 1000004, '2024-09-25 23:55:33.778583', 1000069, '2024-09-25 23:55:33.778583', 1000069, 'Y', 'NTP01', '01. Món Âu', 'bc270fdc-afbd-4de0-bede-fe699d6d91cd', NULL, 'N', NULL, 1006295);
INSERT INTO pos.d_product_category VALUES (1000415, 0, 1000004, '2024-09-25 23:55:33.836281', 1000069, '2024-09-25 23:55:33.836281', 1000069, 'N', 'MA', '02. Món Á', 'b1e7ca87-a85d-4678-8ff2-da9288aba2f7', NULL, 'N', NULL, 1006261);
INSERT INTO pos.d_product_category VALUES (1000416, 0, 1000004, '2024-09-25 23:55:33.904609', 1000069, '2024-09-25 23:55:33.904609', 1000069, 'N', 'CGR POS RES', 'CGR POS RES', 'a9c37fa1-c5ba-4f38-9d9b-5fbb1a4b4eca', NULL, 'N', NULL, 1006456);
INSERT INTO pos.d_product_category VALUES (1000417, 0, 1000004, '2024-09-25 23:55:33.947476', 1000069, '2024-09-25 23:55:33.947476', 1000069, 'N', '005', 'Tạo Bom', '4b72e47d-5b4a-47fd-9400-58101eddcc88', NULL, 'N', NULL, 1006465);
INSERT INTO pos.d_product_category VALUES (1000418, 1000019, 1000004, '2024-09-26 14:09:43.015085', 1000066, '2024-09-26 14:09:43.015086', 1000066, 'Y', '1122', 'Nhóm Cà phê', 'cdd5dc66-c2ed-4a06-96e2-5d9bfea72848', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000421, 1000016, 1000004, '2024-09-26 23:40:41.494127', 1000066, '2024-09-26 23:40:41.494128', 1000066, 'Y', 'P125122', 'Mon Tay Ban Nha', '8d124e8b-655e-49ef-be56-a5e9fb42edbb', NULL, 'Y', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000419, 1000016, 1000004, '2024-09-26 17:15:33.069725', 1000066, '2024-09-26 23:58:30.552988', 1000066, 'N', 'P10020102', 'Món Trung', '099a01b1-84c2-452f-b648-27e877fdcc90', NULL, 'Y', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000420, 1000016, 1000004, '2024-09-26 23:31:55.199151', 1000066, '2024-09-26 23:58:54.946376', 1000066, 'Y', '100000301', 'Mon Nhat ban', '9ce8e786-a648-4d36-afbb-10583f425a2a', 1000421, 'Y', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000422, 1000016, 1000004, '2024-09-27 11:32:58.187639', 1000066, '2024-09-27 11:32:58.18764', 1000066, 'Y', 'NTEST', 'Trà sữa', 'a25fdb7a-2ebd-46f5-8d3b-804f1fd2d20b', 1000420, 'N', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000423, 1000016, 1000004, '2024-09-27 13:11:29.779798', 1000066, '2024-09-27 13:11:29.7798', 1000066, 'Y', 'CAT1000422', 'Nhóm1', 'f6b2ddb1-8475-4694-9dc4-d49a2df3c586', NULL, 'N', NULL, NULL);
INSERT INTO pos.d_product_category VALUES (1000424, 1000016, 1000004, '2024-09-27 14:35:08.668888', 1000066, '2024-09-27 14:35:08.668889', 1000066, 'Y', 'NHOM5', 'Tea', '21a3be3d-bac9-498c-9c9e-1a366a7ac68b', 1000420, 'N', NULL, NULL);




--
-- TOC entry 5437 (class 0 OID 393046)
-- Dependencies: 343
-- Data for Name: d_product_location; Type: TABLE DATA; Schema: pos; Owner: -
--



--
-- TOC entry 5453 (class 0 OID 393403)
-- Dependencies: 359
-- Data for Name: d_production; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_production VALUES (1000016, 1000004, 0, 1000680, 1000001, 'PRO1000001', NULL, NULL, 3, 'DRA', NULL, '2024-09-25 14:08:30.490651', 1000069, '2024-09-25 14:08:30.490651', 1000069, '9e6acc24-cb95-47cd-bb0b-b6b7f7da29f8', 'Y', 'N', 'N', NULL, NULL, NULL);





--
-- TOC entry 5343 (class 0 OID 385794)
-- Dependencies: 248
-- Data for Name: d_reference; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_reference VALUES (1000001, 0, 'Goods Type', 'Loại hàng hóa', '2024-08-18 10:35:40.548938', 1000069, '2024-08-30 14:16:43.435306', 1000069, '6466817f-e309-47b7-8dca-5995d97d6682', 'Y');
INSERT INTO pos.d_reference VALUES (1000005, 0, 'Order Status', 'Trạng thái đặt món', '2024-08-19 17:45:30.483712', 1000069, '2024-08-19 17:45:30.483712', 1000069, 'da5eeef7-689d-4b1c-ae84-4b60553f315f', 'Y');
INSERT INTO pos.d_reference VALUES (1000004, 0, 'Table Reservation Status', 'Trạng thái đặt bàn', '2024-08-18 11:51:32.934966', 1000069, '2024-08-18 11:51:32.934967', 1000069, '86124084-24fc-417c-805d-da0dfdfeeb96', 'Y');
INSERT INTO pos.d_reference VALUES (1000006, 0, 'Document Status', 'Trạng thái chứng từ', '2024-08-22 11:48:58.108362', 1000069, '2024-08-22 11:48:58.108362', 1000069, 'd8e4d745-62d3-4401-a7f1-f953db20afb7', 'Y');
INSERT INTO pos.d_reference VALUES (1000003, 0, 'Menu Type', 'Loại thực đơn ', '2024-08-18 10:57:58.151622', 1000069, '2024-08-18 10:57:58.151622', 1000069, 'cc4c738f-33d5-49da-9d40-aea9b7805c11', 'Y');
INSERT INTO pos.d_reference VALUES (1000007, 0, 'Table Status', 'Trạng thái bàn', '2024-08-22 17:43:54.702087', 1000069, '2024-08-22 17:43:54.702089', 1000069, 'ff7cbc83-3f43-4d94-9b45-b220a58fa5c0', 'Y');
INSERT INTO pos.d_reference VALUES (1000009, 0, 'Bank Type', 'Loại tài khoản', '2024-09-16 02:51:24.610322', 0, '2024-09-16 02:51:24.610322', 0, '467a90d9-0b4b-48df-924c-7d94bd5561a6', 'Y');
INSERT INTO pos.d_reference VALUES (1000010, 0, 'Request Order Status', 'Trạng thái yêu cầu gọi món', '2024-09-17 01:43:50.506325', 0, '2024-09-17 01:43:50.506325', 0, '1d0621f8-db74-4874-9c4c-96c16e696c61', 'Y');
INSERT INTO pos.d_reference VALUES (1000011, 0, 'Data Type Integration', 'Loại dữ liệu tích hợp', '2024-09-17 04:34:14.383867', 0, '2024-09-17 04:34:14.383867', 0, '394e656c-2eb2-4384-a9f3-af0a22184054', 'Y');
INSERT INTO pos.d_reference VALUES (1000012, 0, 'Status Integration', 'Trạng thái tích hợp', '2024-09-17 07:28:19.277785', 0, '2024-09-17 07:28:19.277785', 0, '19c0f155-6bed-4a5b-9304-9ac922c76852', 'Y');
INSERT INTO pos.d_reference VALUES (1000013, 0, 'Flow Integration', 'Chiều tích hợp', '2024-09-17 07:28:19.277785', 0, '2024-09-17 07:28:19.277785', 0, '61032b25-772e-425d-bea2-cd908dc9f6a8', '0');
INSERT INTO pos.d_reference VALUES (1000014, 0, 'Shift Type', 'Loại ca', '2024-09-20 00:53:42.984591', 0, '2024-09-20 00:53:42.984591', 0, 'b4fcca67-4003-44f3-943a-fb66b7a578bc', 'Y');


--
-- TOC entry 5344 (class 0 OID 385816)
-- Dependencies: 249
-- Data for Name: d_reference_list; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_reference_list VALUES (1000004, 1000001, 'CBP', 'Combo - Đóng gói', 0, '231cf5c1-c4d7-4bf4-b35f-184dc648801c', 'Y', '2024-08-18 10:57:20.399442', 1000069, '2024-08-18 10:57:20.399442', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000013, 1000005, 'WTP', 'Chờ chế biến', 0, 'b1e9ec62-b746-48c6-a27e-2cc7e756f4e8', 'Y', '2024-08-19 17:48:55.585365', 1000069, '2024-08-19 17:48:55.585365', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000021, 1000006, 'COM', 'Đã hoàn thành', 0, '6e722a2a-6a3c-4f41-997e-19eaec3b678b', 'Y', '2024-08-22 11:51:20.955447', 1000069, '2024-08-22 11:51:20.955447', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000017, 1000005, 'DCP', 'Đã chế biến', 0, '26e18a61-16cc-4052-8fab-0bdcef9c32e9', 'Y', '2024-08-19 17:50:50.633744', 1000069, '2024-08-19 17:50:50.633744', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000020, 1000006, 'IPR', 'Đang xử lý', 0, '5878ff01-0df2-4611-b940-0d479edf4a9c', 'Y', '2024-08-22 11:51:04.415044', 1000069, '2024-08-22 11:51:04.415044', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000023, 1000007, 'TIU', 'Bàn đang sử dụng', 0, 'b6950ec0-67e2-4ed7-a543-1487788ca97d', 'Y', '2024-08-22 17:47:05.877194', 1000069, '2024-08-22 17:47:05.877194', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000016, 1000005, 'BPR', 'Đang chế biến', 0, '2a751867-5ded-41b4-a5e7-bd003daee32b', 'Y', '2024-08-19 17:50:27.522691', 1000069, '2024-08-19 17:50:27.522691', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000003, 1000001, 'SVC', 'Hàng dịch vụ', 0, '31a2af1c-b38a-44a1-bb2e-07e1e08acf1a', 'Y', '2024-08-18 10:57:05.102352', 1000069, '2024-08-18 10:57:05.102352', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000014, 1000005, 'NSK', 'Chưa gửi bếp', 0, 'bcc983c1-fa49-4dcc-8927-1fdf1ab9619a', 'Y', '2024-08-19 17:49:24.039142', 1000069, '2024-08-19 17:49:24.039142', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000025, 1000005, 'KOS', 'Đã gửi bếp', 0, '3f973ed1-575b-4797-ba83-15f0bac7ed82', 'Y', '2024-08-28 09:26:21.227654', 1000069, '2024-08-28 09:26:21.227654', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000012, 1000004, 'CAN', 'Đã hủy', 0, 'ec93dc3f-1405-42cc-922a-7cfb5e12fa70', 'Y', '2024-08-18 11:53:36.862491', 1000069, '2024-08-18 11:53:36.862491', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000019, 1000006, 'DRA', 'Đơn nháp', 0, '983af889-f189-4dea-ad46-d19fa6cce77b', 'Y', '2024-08-22 11:50:34.483665', 1000069, '2024-08-22 11:50:34.483665', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000024, 1000007, 'TBD', 'Bàn đã đặt', 0, 'bd64fb82-aec1-4411-af94-e6de82720bc9', 'Y', '2024-08-22 17:47:48.97707', 1000069, '2024-08-22 17:47:48.97707', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000007, 1000003, 'OTH', 'Loại khác', 0, 'd5e75c30-15d7-48af-9ae3-1d237fdc93f1', 'Y', '2024-08-18 10:58:49.859136', 1000069, '2024-08-18 10:58:49.859136', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000006, 1000003, 'FOD', 'Thức ăn', 0, 'fbbcef9c-e35c-479f-807d-882046d79ac8', 'Y', '2024-08-18 10:58:38.161452', 1000069, '2024-08-18 10:58:38.161452', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000022, 1000007, 'ETB', 'Bàn trống', 0, '29cc5eea-4052-4a5e-ac03-1ab57d8924a8', 'Y', '2024-08-22 17:46:25.15017', 1000069, '2024-08-22 17:46:25.150171', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000005, 1000003, 'DRK', 'Nước uống', 0, '3f461bde-c53b-442a-a9b1-9512caf3bac9', 'Y', '2024-08-18 10:58:24.248605', 1000069, '2024-08-18 10:58:24.248605', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000002, 1000001, 'PRD ', 'Hàng chế biến', 0, '220f2cf6-34af-4c1f-9254-c506942233c6', 'Y', '2024-08-18 10:56:49.583435', 1000069, '2024-08-18 10:56:49.583435', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000009, 1000004, 'PSB', 'Chờ sắp bàn', 0, '2d1726fb-d17c-4e5a-9040-3827bf1f9e75', 'Y', '2024-08-18 11:52:10.257728', 1000069, '2024-08-18 11:52:10.257729', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000001, 1000001, 'RGD', 'Hàng hóa thường', 0, '63ab25cb-d5fe-464b-b3af-10a4f171ca6f', 'Y', '2024-08-18 10:55:27.177022', 1000069, '2024-08-18 10:55:27.177022', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000011, 1000004, 'NOS', 'Không đến', 0, '3a82861c-9f23-45b8-a84c-ba6033b90606', 'Y', '2024-08-18 11:53:24.759148', 1000069, '2024-08-18 11:53:24.759148', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000008, 1000004, 'TBL', 'Đã xếp bàn', 0, '4e310f0f-35a6-4203-9651-e00f17ee5c56', 'Y', '2024-08-18 11:51:55.431381', 1000069, '2024-08-18 11:51:55.431382', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000015, 1000005, 'PRD', 'Đã chế biến', 0, 'd6bb1f15-38db-49d7-bc24-ddfc96236dbf', 'Y', '2024-08-19 17:49:44.796892', 1000069, '2024-08-19 17:49:44.796892', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000018, 1000005, 'DMC', 'Hủy món', 0, 'a45578d3-f182-4099-8d47-24168f19248c', 'Y', '2024-08-19 17:51:08.372483', 1000069, '2024-08-19 17:51:08.372483', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000010, 1000004, 'TRC', 'Đã nhận bàn', 0, '603ece6a-f05c-4ae1-9d2e-2d180f309037', 'Y', '2024-08-18 11:52:20.28623', 1000069, '2024-08-18 11:52:20.28623', 1000069);
INSERT INTO pos.d_reference_list VALUES (1000029, 1000006, 'VOD', 'Đơn Hủy', 0, 'b650e2aa-bde3-4f18-8e6b-fe16d20a6800', 'Y', '2024-09-06 04:02:05.442333', 0, '2024-09-06 04:02:05.442333', 0);
INSERT INTO pos.d_reference_list VALUES (1000030, 1000009, 'CHE', 'Tài khoản ngân hàng', 0, '846f208a-5da0-42cf-9386-f39c6aabf6bd', 'Y', '2024-09-16 02:55:06.421122', 0, '2024-09-16 02:55:06.421122', 0);
INSERT INTO pos.d_reference_list VALUES (1000031, 1000009, 'CAS', 'Tài khoản tiền mặt', 0, '48ab5218-105c-4cf7-b3ac-cf621d2f017d', 'Y', '2024-09-16 02:55:06.421122', 0, '2024-09-16 02:55:06.421122', 0);
INSERT INTO pos.d_reference_list VALUES (1000033, 1000011, 'ORG', 'Cửa hàng chi nhánh', 0, '88bf93c9-06f9-47b6-b8cb-f9d5280795ef', 'Y', '2024-09-17 04:41:42.43141', 0, '2024-09-17 04:41:42.43141', 0);
INSERT INTO pos.d_reference_list VALUES (1000034, 1000011, 'PCG', 'Nhóm hàng', 0, '34148213-289b-456d-a967-b870c8141999', 'Y', '2024-09-17 04:41:42.43141', 0, '2024-09-17 04:41:42.43141', 0);
INSERT INTO pos.d_reference_list VALUES (1000035, 1000011, 'PRO', 'Hàng hóa', 0, 'd53f42f9-dbd5-4ae3-973b-67a398bd5dd4', 'Y', '2024-09-17 04:41:42.43141', 0, '2024-09-17 04:41:42.43141', 0);
INSERT INTO pos.d_reference_list VALUES (1000036, 1000011, 'PRL', 'Bảng giá', 0, '13df22a2-2ead-432b-9164-515016b6065b', 'Y', '2024-09-17 04:41:42.43141', 0, '2024-09-17 04:41:42.43141', 0);
INSERT INTO pos.d_reference_list VALUES (1000037, 1000011, 'CAV', 'Khách hàng / Nhà cung cấp', 0, 'f44ee07e-2b4e-4cdd-82f2-e6918e2dcf8a', 'Y', '2024-09-17 04:41:42.43141', 0, '2024-09-17 04:41:42.43141', 0);
INSERT INTO pos.d_reference_list VALUES (1000038, 1000011, 'CUS', 'Người dùng', 0, 'e9f92632-8f7a-4aee-b620-6f246a690709', 'Y', '2024-09-17 04:41:42.43141', 0, '2024-09-17 04:41:42.43141', 0);
INSERT INTO pos.d_reference_list VALUES (1000039, 1000011, 'WHO', 'Kho hàng', 0, 'bc8c04ae-241d-4e6e-b072-fc0691473e0b', 'Y', '2024-09-17 04:41:42.43141', 0, '2024-09-17 04:41:42.43141', 0);
INSERT INTO pos.d_reference_list VALUES (1000040, 1000011, 'FLO', 'khu vực / tầng', 0, 'dca0ac0f-38c6-4e6c-8c12-70e43dc8e052', 'Y', '2024-09-17 04:41:42.43141', 0, '2024-09-17 04:41:42.43141', 0);
INSERT INTO pos.d_reference_list VALUES (1000041, 1000011, 'TBL', 'Phòng bàn', 0, 'c0699412-f876-4c86-828f-4bee55ae4389', 'Y', '2024-09-17 04:41:42.43141', 0, '2024-09-17 04:41:42.43141', 0);
INSERT INTO pos.d_reference_list VALUES (1000042, 1000011, 'COP', 'Coupo', 0, '2233188b-29c4-4661-a118-37b315aeb4db', 'Y', '2024-09-17 04:41:42.43141', 0, '2024-09-17 04:41:42.43141', 0);
INSERT INTO pos.d_reference_list VALUES (1000043, 1000011, 'SOR', 'Đơn hàng', 0, '7b5d030b-225f-479f-842d-ee9f9e9cb0a7', 'Y', '2024-09-17 04:41:42.43141', 0, '2024-09-17 04:41:42.43141', 0);
INSERT INTO pos.d_reference_list VALUES (1000044, 1000011, 'KDS', 'Đơn bar/bếp', 0, '6279a319-a344-4944-bc1e-12c221fae365', 'Y', '2024-09-17 04:41:42.43141', 0, '2024-09-17 04:41:42.43141', 0);
INSERT INTO pos.d_reference_list VALUES (1000045, 1000011, 'PTM', 'Điểm bán hàng', 0, '91f45828-02d0-4c4e-a45c-556845f93739', 'Y', '2024-09-17 04:41:42.43141', 0, '2024-09-17 04:41:42.43141', 0);
INSERT INTO pos.d_reference_list VALUES (1000047, 1000010, 'PND', 'Chờ xác nhận', 0, '54639e83-80e7-4f74-83b9-c9d0bbe93de0', 'Y', '2024-09-17 04:57:18.978784', 0, '2024-09-17 04:57:18.978784', 0);
INSERT INTO pos.d_reference_list VALUES (1000048, 1000010, 'CNF', 'Đã xác nhận', 0, '903d3993-6a2c-45d5-b4bc-fcd64d6cd584', 'Y', '2024-09-17 04:57:18.978784', 0, '2024-09-17 04:57:18.978784', 0);
INSERT INTO pos.d_reference_list VALUES (1000049, 1000010, 'VOID', 'Đã hủy', 0, '87d24c78-6ee3-4ba7-a859-f4a1885ee873', 'Y', '2024-09-17 04:57:18.978784', 0, '2024-09-17 04:57:18.978784', 0);
INSERT INTO pos.d_reference_list VALUES (1000050, 1000012, 'COM', 'Thành công', 0, 'eedb8563-844c-4208-be00-f24b79e8c779', 'Y', '2024-09-17 07:35:14.454058', 0, '2024-09-17 07:35:14.454058', 0);
INSERT INTO pos.d_reference_list VALUES (1000051, 1000012, 'FAI', 'Thất bại', 0, 'dbe08b68-3d19-47c4-af1a-75fd6eee0bf2', 'Y', '2024-09-17 07:35:14.454058', 0, '2024-09-17 07:35:14.454058', 0);
INSERT INTO pos.d_reference_list VALUES (1000052, 1000013, 'ETP', 'ERP về POS', 0, 'd7d6f9a5-e736-4c01-8082-95da045427a5', 'Y', '2024-09-17 07:35:14.454058', 0, '2024-09-17 07:35:14.454058', 0);
INSERT INTO pos.d_reference_list VALUES (1000053, 1000013, 'PTE', 'POS về ERP', 0, '0a9eddf7-f113-439e-ad2c-333251b283c5', 'Y', '2024-09-17 07:35:14.454058', 0, '2024-09-17 07:35:14.454058', 0);
INSERT INTO pos.d_reference_list VALUES (1000054, 1000014, 'MOR', 'Buổi sáng', 0, '45031915-0cf1-4cab-99ec-f967d2900089', 'Y', '2024-09-20 00:56:23.760049', 0, '2024-09-20 00:56:23.760049', 0);
INSERT INTO pos.d_reference_list VALUES (1000055, 1000014, 'LUN', 'Buổi chiều', 0, 'b7a00f75-e6b7-4e37-80f1-7a2b843aa674', 'Y', '2024-09-20 00:56:23.760049', 0, '2024-09-20 00:56:23.760049', 0);
INSERT INTO pos.d_reference_list VALUES (1000056, 1000014, 'EVE', 'Buổi tối', 0, '3df5c3be-56d2-4341-b2bf-6decf7afc2b4', 'Y', '2024-09-20 00:56:23.760049', 0, '2024-09-20 00:56:23.760049', 0);



--
-- TOC entry 5345 (class 0 OID 385824)
-- Dependencies: 250
-- Data for Name: d_reservation_line; Type: TABLE DATA; Schema: pos; Owner: -
--




--
-- TOC entry 5347 (class 0 OID 385843)
-- Dependencies: 252
-- Data for Name: d_role; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_role VALUES (1000000, 1000004, 'ADMIN', 'ADMIN', '2024-09-17 08:37:03.335228', 0, '2024-09-17 08:37:03.335228', 0, 'Y', '4c79a452-cb12-4eab-9aaa-82e44b62c3dc', NULL);
INSERT INTO pos.d_role VALUES (1000001, 1000004, 'USER', 'USER', '2024-09-17 08:39:26.885968', 0, '2024-09-17 08:39:26.885968', 0, 'Y', '3dbd931d-7114-4f0c-9b83-fc504df86398', NULL);
INSERT INTO pos.d_role VALUES (1000002, 1000004, 'STAFF', 'STAFF', '2024-09-18 08:27:05.152365', 0, '2024-09-18 08:27:05.152365', 0, 'Y', '924341e8-8c02-4ec7-9643-925ae9b7dfb0', NULL);
INSERT INTO pos.d_role VALUES (1000003, 1000004, 'DIS', 'DBIZ_INV_SuperUser', '2024-09-25 14:55:12.353016', 0, '2024-09-25 14:55:12.353016', 0, 'Y', '91904681-165a-498d-b625-47e1fce4e5a6', 1000717);
INSERT INTO pos.d_role VALUES (1000004, 1000004, 'ADMIN1', 'Administrator POS', '2024-09-25 14:55:12.353016', 0, '2024-09-25 14:55:12.353016', 0, 'Y', '648418ee-3ca0-4d39-a59e-be8b3583f94b', 1000735);
INSERT INTO pos.d_role VALUES (1000005, 1000004, 'KST', 'Kitchen Staff', '2024-09-25 14:55:12.353016', 0, '2024-09-25 14:55:12.353016', 0, 'Y', '42332695-9f66-4c90-9b45-43ed90178911', 1000739);
INSERT INTO pos.d_role VALUES (1000006, 1000004, 'CIS', 'CheckIn Staff', '2024-09-25 14:55:12.353016', 0, '2024-09-25 14:55:12.353016', 0, 'Y', '942043bf-14a0-4607-9529-13d0e002af2f', 1000745);
INSERT INTO pos.d_role VALUES (1000007, 1000004, 'ELP', 'Employee POS', '2024-09-25 14:55:12.353016', 0, '2024-09-25 14:55:12.353016', 0, 'Y', '78e52e82-1406-47b4-b0dd-59e39baa5942', 1000737);
INSERT INTO pos.d_role VALUES (1000008, 1000004, 'SSF', 'Serve Staff', '2024-09-25 14:55:12.353016', 0, '2024-09-25 14:55:12.353016', 0, 'Y', '579db781-7b35-43f5-8b08-4250851ee99d', 1000740);
INSERT INTO pos.d_role VALUES (1000009, 1000004, 'MNP', 'Manager POS', '2024-09-25 14:55:12.353016', 0, '2024-09-25 14:55:12.353016', 0, 'Y', '0109d7b6-c776-4d0d-a769-ba629ad8be0a', 1000736);



--
-- TOC entry 5348 (class 0 OID 385855)
-- Dependencies: 253
-- Data for Name: d_storage_onhand; Type: TABLE DATA; Schema: pos; Owner: -
--



--
-- TOC entry 5349 (class 0 OID 385866)
-- Dependencies: 254
-- Data for Name: d_table; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_table VALUES (1000089, 1000030, 1000004, '2024-09-18 22:08:02.190611', 1000069, '2024-09-18 22:08:47.626136', 1000069, 'Y', 'B01', 'Bàn 2', 'B01', 999982, 'ETB', 'cad620de-751c-443e-a469-d8c374378929', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000030, 1000003, 1000003, '2024-07-31 11:10:40.066871', 1000068, '2024-07-31 11:10:40.066873', 1000068, 'Y', 'B9', 'Bàn - 09', 'Bàn hư', 999964, 'ETB', '1821b8bb-8d0d-4b01-814f-1a4dc6548ad8', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000036, 1000016, 1000004, '2024-08-07 09:19:39.325795', 1000069, '2024-09-26 23:27:24.080499', 1000066, 'Y', 'B15', 'Bàn - 15', ' ', 999972, 'TIU', 'e0d07d6c-934f-424d-a9a9-4dee33237c4c', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000057, 1000016, 1000004, '2024-08-07 09:22:19.317118', 1000069, '2024-09-25 11:07:59.374225', 1000069, 'Y', 'B33', 'Bàn - 33', ' ', 999975, 'TIU', '0e879b35-34f4-4202-b9d0-9192b0ff5653', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000052, 1000016, 1000004, '2024-08-07 09:21:47.294973', 1000069, '2024-09-20 17:29:43.517861', 1000069, 'Y', 'B28', 'Bàn - 28', ' ', 999975, 'ETB', 'a3942c57-9b99-4bfe-ab6c-ed758f392d90', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000087, 1000030, 1000004, '2024-09-18 22:07:21.073224', 1000069, '2024-09-18 22:08:09.566649', 1000069, 'Y', 'B01', 'Bàn 1', ' ', 999982, 'TIU', 'be300bbe-3e87-4428-8057-0fda12f5a605', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000068, 1000016, 1000004, '2024-08-07 11:39:14.230906', 1000069, '2024-09-17 16:36:34.080153', 1000069, 'Y', 'B37', 'Bàn - 37', 'Bàn', 999976, 'TIU', '945958dc-8384-4874-9a39-fedcda4a3ef9', 11, 0, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000023, 1000003, 1000002, '2024-07-22 16:34:11.384657', 1000037, '2024-07-22 16:34:11.384659', 1000037, 'Y', 'B3', 'Bàn - 03', 'Bàn hư', 999963, 'ETB', '3dc13179-02ac-4b0e-936e-5a9c03698975', 2, 0, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000056, 1000016, 1000004, '2024-08-07 09:22:14.498161', 1000069, '2024-09-27 10:39:32.493533', 1000066, 'Y', 'B32', 'Bàn - 32', ' ', 999975, 'TIU', 'f49f43b5-8ede-4215-afa2-f09982c1c0ca', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000040, 1000016, 1000004, '2024-08-07 09:19:57.298896', 1000069, '2024-09-26 10:56:02.940896', 1000069, 'Y', 'B19', 'Bàn - 19', ' Ban to', 999972, 'TIU', 'aa1a0430-403f-4f86-9059-f6222ff2e0c9', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000029, 1000001, 1000002, '2024-07-31 09:51:55.852874', 1000037, '2024-07-31 09:51:55.852877', 1000037, 'Y', 'B8', 'Bàn - 08', 'Bàn vip', 999957, 'ETB', '15eff874-39a7-43dd-9502-d75a779d16dd', 3, 0, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000027, 1000003, 1000002, '2024-07-24 12:33:22.15152', 1000037, '2024-07-25 10:47:26.561879', 1000037, 'Y', 'B7', 'Bàn - 07', 'Bàn vip', 999966, 'ETB', 'de6aeb46-d976-4adc-98b3-ad67c6b127cd', 33, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000025, 1000003, 1000003, '2024-07-24 10:11:53.673266', 1000068, '2024-07-24 10:11:53.673266', 1000068, 'Y', 'B2', 'Bàn - 02', 'Bàn hư', 999964, 'ETB', '9b8b6e96-0818-4d2e-bc9f-7914211b9320', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000066, 1000016, 1000004, '2024-08-07 11:38:35.222062', 1000069, '2024-09-20 10:14:05.145859', 1000069, 'Y', 'B35', 'Bàn - 35', 'Bàn trống', 999976, 'TIU', 'c61f5fa7-547f-4085-b33c-e7942c671e66', 21, 0, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000070, 1000016, 1000004, '2024-08-07 11:39:54.893844', 1000069, '2024-09-27 17:20:12.426424', 1000066, 'Y', 'B39', 'Bàn - 39', 'B', 999976, 'ETB', '2581a6ac-a385-4917-80d4-7e00cddd51b8', 11, 0, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000026, 1000003, 1000002, '2024-07-24 11:59:13.47666', 1000037, '2024-07-24 11:59:13.476663', 1000037, 'Y', 'B5', 'Bàn - 05', 'Bàn to', 999964, 'ETB', 'bf469f3d-c011-4f1f-b149-60eb55a9fe3f', 10, 0, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000034, 1000016, 1000004, '2024-08-07 09:19:30.022508', 1000069, '2024-09-26 23:34:12.898024', 1000066, 'Y', 'B13', 'Bàn - 13', ' ', 999972, 'TIU', 'f1b4178e-0ebb-467b-9ea9-140ec14943e2', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000033, 1000016, 1000004, '2024-08-07 09:19:26.504501', 1000069, '2024-09-27 09:35:08.471513', 1000066, 'Y', 'B12', 'Bàn - 12', ' ', 999972, 'TIU', '3b663d6c-94f3-45be-b23b-ee5ba1beb3c6', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000065, 1000016, 1000004, '2024-08-07 11:37:58.606125', 1000069, '2024-09-17 17:54:41.289365', 1000069, 'Y', 'B45', 'Bàn - 45', 'Bàn trống rất trống nha', 999976, 'TIU', '569de64c-6adf-43a2-8602-da8ba8d8f9e1', 23, 2, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000035, 1000016, 1000004, '2024-08-07 09:19:33.78718', 1000069, '2024-09-22 20:37:05.70676', 1000069, 'Y', 'B14', 'Bàn - 14', ' ', 999972, 'TIU', '2950ea6e-a613-4458-8cd2-08bb285052c9', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000058, 1000016, 1000004, '2024-08-07 09:22:23.268453', 1000069, '2024-09-22 20:34:58.096335', 1000069, 'Y', 'B34', 'Bàn - 34', ' ', 999975, 'TIU', '1a718c98-a179-4ce4-8ea2-86b31b4c0457', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000063, 1000016, 1000004, '2024-08-07 11:37:01.347313', 1000069, '2024-09-27 17:21:34.180539', 1000066, 'Y', 'B47', 'Bàn - 47', 'Bàn trống', 999976, 'ETB', '05ac63ea-2f84-46df-bcde-66f3d20ae6b0', 10, 2, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000059, 1000016, 1000004, '2024-08-07 09:22:26.393149', 1000069, '2024-09-25 11:04:44.799036', 1000069, 'Y', 'B1', 'Bàn - 01', ' ', 999975, 'TIU', 'c3ccd71d-3573-4fe2-a80d-58a70b561622', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000037, 1000016, 1000004, '2024-08-07 09:19:44.255929', 1000069, '2024-09-25 11:21:16.872503', 1000069, 'Y', 'B16', 'Bàn - 16', ' ', 999972, 'TIU', '7a9d7029-003d-4e8e-94bc-af325f05c8ba', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000061, 1000016, 1000004, '2024-08-07 11:36:02.627606', 1000069, '2024-09-20 15:10:11.283911', 1000069, 'Y', 'B4', 'Bàn - 04', 'Bàn trống ', 999976, 'TIU', '27485e45-9291-49ce-8dd2-8eef17f230a0', 20, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000047, 1000016, 1000004, '2024-08-07 09:21:05.235521', 1000069, '2024-09-27 11:28:49.236741', 1000066, 'Y', 'B24', 'Bàn - 24', ' ', 999974, 'TIU', '215c925b-2d98-4664-aefa-5175f66cd750', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000073, 1000016, 1000004, '2024-08-17 10:39:15.535796', 1000069, '2024-09-27 16:12:42.895187', 1000066, 'Y', 'B41', 'Bàn - 41', 'Bàn to', 999972, 'TIU', '3e17c40c-5354-428a-aa9d-e5fbb94c78a6', 10, 0, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000032, 1000016, 1000004, '2024-08-07 09:19:22.070725', 1000069, '2024-09-25 11:20:46.097503', 1000069, 'Y', 'B11', 'Bàn - 11', ' ', 999972, 'TIU', '0f358426-9982-4c95-becf-47afe784160a', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000043, 1000016, 1000004, '2024-08-07 09:20:32.103001', 1000069, '2024-09-26 09:50:58.865849', 1000069, 'N', 'B22', 'Bàn - 22', ' ', 999973, 'TIU', '934a4294-e6ca-4154-84a8-fe0379ca042e', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000074, 1000016, 1000004, '2024-08-17 10:40:15.123894', 1000069, '2024-09-27 09:54:54.063965', 1000066, 'Y', 'B42', 'Bàn - 42', 'Bàn lớn', 999977, 'TIU', '1441339e-6822-44be-88f9-8d3762f4eb1f', 10, 0, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000054, 1000016, 1000004, '2024-08-07 09:21:55.101105', 1000069, '2024-09-27 11:32:47.001279', 1000066, 'Y', 'B30', 'Bàn - 30', ' ', 999975, 'TIU', 'c2e836b0-3796-4231-ae5d-f8675038ed89', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000031, 1000016, 1000004, '2024-08-07 09:19:06.167222', 1000069, '2024-09-26 23:25:30.905435', 1000066, 'Y', 'B10', 'Bàn - 10', ' ', 999972, 'TIU', '61aab7bb-8934-4a72-a1e0-023f1c66b32f', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000060, 1000016, 1000004, '2024-08-07 09:22:29.574324', 1000069, '2024-09-25 14:51:13.996144', 1000069, 'Y', 'B43', 'Bàn - 43', ' ', 999975, 'TIU', '0a0ab94a-7924-459c-80e0-fce5d34ec3e5', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000069, 1000016, 1000004, '2024-08-07 11:39:35.528769', 1000069, '2024-09-27 10:09:10.26793', 1000066, 'Y', 'B38', 'Bàn - 38', 'Bàn', 999976, 'TIU', '5fca46c5-9a5b-430c-a59c-f5a5119a517b', 12, 0, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000038, 1000016, 1000004, '2024-08-07 09:19:48.432928', 1000069, '2024-09-27 10:10:57.857409', 1000066, 'Y', 'B17', 'Bàn - 17', ' ', 999972, 'TIU', '8047251d-8d4d-4f1d-8bdc-f4ba14e2d06e', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000051, 1000016, 1000004, '2024-08-07 09:21:42.820513', 1000069, '2024-09-27 10:36:41.843843', 1000066, 'Y', 'B27', 'Bàn - 27', ' ', 999975, 'TIU', 'ca94d9d9-0123-4412-8bd9-48621c7859ef', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000062, 1000016, 1000004, '2024-08-07 11:36:29.219726', 1000069, '2024-09-27 13:38:52.323876', 1000066, 'Y', 'B6', 'Bàn - 06', 'Bàn trống', 999976, 'ETB', 'b1d8d5ce-d0f4-4ee6-807c-59a871134645', 20, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000114, 1000033, 1000004, '2024-09-22 17:13:17.942176', 1000069, '2024-09-22 17:13:17.942176', 1000069, 'Y', '8', 'Bàn 8', NULL, 1000036, 'ETB', '3911915a-5084-4c0b-8fbe-5148a6e12888', 1, NULL, 0, 'N', NULL, 'N', 1000011, 'N');
INSERT INTO pos.d_table VALUES (1000115, 1000038, 1000004, '2024-09-22 17:13:18.000062', 1000069, '2024-09-22 17:13:18.000062', 1000069, 'Y', '2', 'Bàn 2', NULL, 1000020, 'ETB', 'd50cf756-abd8-4ea4-b9da-71ba96a5cb70', 10, NULL, 0, 'N', NULL, 'N', 1000054, 'N');
INSERT INTO pos.d_table VALUES (1000116, 1000033, 1000004, '2024-09-22 17:13:18.078214', 1000069, '2024-09-22 17:13:18.078214', 1000069, 'Y', '23', 'Bàn 23', NULL, 1000013, 'ETB', 'b2b6df21-12da-48f6-b1d2-5d6cf4520af5', 1, NULL, 0, 'N', NULL, 'N', 1000014, 'N');
INSERT INTO pos.d_table VALUES (1000117, 1000038, 1000004, '2024-09-22 17:13:18.13142', 1000069, '2024-09-22 17:13:18.13142', 1000069, 'Y', '4', 'Room 104', NULL, 1000024, 'ETB', 'd2fdcde8-d113-40ab-a789-e94832c9c956', 10, NULL, 0, 'N', NULL, 'N', 1000044, 'N');
INSERT INTO pos.d_table VALUES (1000042, 1000016, 1000004, '2024-08-07 09:20:28.932811', 1000069, '2024-09-17 14:53:37.725186', 1000069, 'Y', 'B21', 'Bàn - 21', ' ', 999973, 'TIU', 'a1b4de06-f24f-4dfb-aa6d-c3694dc3dde6', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000118, 1000038, 1000004, '2024-09-22 17:13:18.178423', 1000069, '2024-09-22 17:13:18.178423', 1000069, 'Y', '', 'Bàn 1A', NULL, 1000036, 'ETB', '81cc7c6e-7e12-4255-a90f-a4eab20a58fb', 1, NULL, 0, 'N', NULL, 'N', 1000052, 'N');
INSERT INTO pos.d_table VALUES (1000119, 1000033, 1000004, '2024-09-22 17:13:18.224402', 1000069, '2024-09-22 17:13:18.224402', 1000069, 'Y', '10', 'Bàn 10', NULL, 1000036, 'ETB', '5c0d4bf4-6554-42a6-b396-539f34a38a21', 1, NULL, 0, 'N', NULL, 'N', 1000013, 'N');
INSERT INTO pos.d_table VALUES (1000071, 1000016, 1000004, '2024-08-07 11:40:17.237248', 1000069, '2024-09-25 16:09:59.99922', 1000069, 'Y', 'B40', 'Bàn - 40', ' Ban siu nho=', 999976, 'TIU', 'e370d294-4a7d-4231-a79c-b4cf27abfa45', 12, 0, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000120, 1000033, 1000004, '2024-09-22 17:13:18.283456', 1000069, '2024-09-22 17:13:18.283456', 1000069, 'Y', '1', 'Bàn 1', NULL, 1000036, 'ETB', '932fdec1-9352-4603-b880-c39fbcb14b5c', 1, NULL, 0, 'N', NULL, 'N', 1000003, 'N');
INSERT INTO pos.d_table VALUES (1000121, 1000038, 1000004, '2024-09-22 17:13:18.342814', 1000069, '2024-09-22 17:13:18.342814', 1000069, 'Y', '1A', 'Bàn 1A', NULL, 1000037, 'ETB', '44d48950-3c6a-4134-a5f4-6fb75f5e1dbc', 100, NULL, 0, 'N', NULL, 'N', 1000016, 'N');
INSERT INTO pos.d_table VALUES (1000122, 1000038, 1000004, '2024-09-22 17:13:18.390452', 1000069, '2024-09-22 17:13:18.390452', 1000069, 'Y', '1', 'Bàn 1D', NULL, 1000020, 'ETB', 'f42ee9fd-6ab0-4184-bd59-66d8e8bb5861', 1, NULL, 0, 'N', NULL, 'N', 1000050, 'N');
INSERT INTO pos.d_table VALUES (1000123, 1000038, 1000004, '2024-09-22 17:13:18.460409', 1000069, '2024-09-22 17:13:18.460409', 1000069, 'Y', '2B', 'Bàn 2B', NULL, 1000018, 'ETB', 'afd2070a-4220-4a41-8f5d-0461d063e95b', 1, NULL, 0, 'N', NULL, 'N', 1000021, 'N');
INSERT INTO pos.d_table VALUES (1000124, 1000038, 1000004, '2024-09-22 17:13:18.523268', 1000069, '2024-09-22 17:13:18.523268', 1000069, 'Y', '6', 'Bàn 6', NULL, 1000020, 'ETB', '1e60fdb8-69be-4d25-9f38-ab8d9a0aac33', 10, NULL, 0, 'N', NULL, 'N', 1000058, 'N');
INSERT INTO pos.d_table VALUES (1000125, 1000038, 1000004, '2024-09-22 17:13:18.57559', 1000069, '2024-09-22 17:13:18.57559', 1000069, 'Y', '7', 'Bàn 7', NULL, 1000020, 'ETB', 'd247c360-616d-4d15-9355-beddb7a1b54a', 10, NULL, 0, 'N', NULL, 'N', 1000059, 'N');
INSERT INTO pos.d_table VALUES (1000127, 1000038, 1000004, '2024-09-22 17:13:18.728124', 1000069, '2024-09-22 17:13:18.728124', 1000069, 'Y', '2', 'Room 102', NULL, 1000024, 'ETB', '9123129e-8df4-4ad8-a798-05aacc9b5c3d', 10, NULL, 0, 'N', NULL, 'N', 1000041, 'N');
INSERT INTO pos.d_table VALUES (1000128, 1000038, 1000004, '2024-09-22 17:13:18.784327', 1000069, '2024-09-22 17:13:18.784327', 1000069, 'Y', 'T2', 'Bàn 12', NULL, 1000032, 'ETB', '5c88c56d-571c-472d-85c1-28683f3194cc', 20, NULL, 0, 'N', NULL, 'N', 1000025, 'N');
INSERT INTO pos.d_table VALUES (1000129, 1000038, 1000004, '2024-09-22 17:13:18.848269', 1000069, '2024-09-22 17:13:18.848269', 1000069, 'Y', '6', 'Bàn 6', NULL, 1000021, 'ETB', '0b7e6c5b-08b8-42c6-8a64-abe334521c5b', 10, NULL, 0, 'N', NULL, 'N', 1000035, 'N');
INSERT INTO pos.d_table VALUES (1000130, 1000038, 1000004, '2024-09-22 17:13:18.924555', 1000069, '2024-09-22 17:13:18.924555', 1000069, 'Y', '7', 'Bàn 7', NULL, 1000022, 'ETB', '0a2a9d22-d60b-43f4-b3d3-d2c7816e5e36', 10, NULL, 0, 'N', NULL, 'N', 1000036, 'N');
INSERT INTO pos.d_table VALUES (1000126, 1000038, 1000004, '2024-09-22 17:13:18.622468', 1000069, '2024-09-22 22:15:10.230832', 1000069, 'Y', '3', 'Room 103', NULL, 1000024, 'TIU', 'd4064be0-c8f4-4eda-9c58-f9fcbb67e87e', 10, NULL, 0, 'N', NULL, 'N', 1000043, 'N');
INSERT INTO pos.d_table VALUES (1000131, 1000033, 1000004, '2024-09-22 17:13:18.992724', 1000069, '2024-09-22 17:13:18.992724', 1000069, 'Y', '6', 'Bàn 6', NULL, 1000036, 'ETB', '46e34c68-a7a4-451f-b0f0-203d4acdf586', 1, NULL, 0, 'N', NULL, 'N', 1000009, 'N');
INSERT INTO pos.d_table VALUES (1000138, 1000033, 1000004, '2024-09-22 17:13:19.400512', 1000069, '2024-09-22 20:22:44.420203', 1000069, 'Y', '2', 'Bàn 2', NULL, 1000036, 'TIU', 'ded7d181-0ba8-4213-bdd2-fa5a149938ee', 1, NULL, 0, 'N', NULL, 'N', 1000005, 'N');
INSERT INTO pos.d_table VALUES (1000132, 1000038, 1000004, '2024-09-22 17:13:19.059068', 1000069, '2024-09-22 17:13:19.059068', 1000069, 'Y', '1', 'Bàn 1D', NULL, 1000018, 'ETB', 'f5abddd3-41e4-430e-a2e9-00bfbf24c595', 1, NULL, 0, 'N', NULL, 'N', 1000051, 'N');
INSERT INTO pos.d_table VALUES (1000067, 1000016, 1000004, '2024-08-07 11:38:52.114481', 1000069, '2024-09-16 15:38:20.070432', 1000069, 'Y', 'B36', 'Bàn - 36', 'Bàn', 999976, 'TIU', 'af61d088-d4c9-4bed-9382-7aafda6d47cd', 11, 0, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000048, 1000016, 1000004, '2024-08-07 09:21:11.028458', 1000069, '2024-09-27 17:25:55.046737', 1000066, 'Y', 'B25', 'Bàn - 25', ' ', 999974, 'TIU', '0589b329-bcb6-44df-b11e-3be7a6f4be8e', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000045, 1000016, 1000004, '2024-08-07 09:20:41.145141', 1000069, '2024-09-26 10:19:16.435301', 1000066, 'Y', 'B23', 'Bàn - 23', ' ', 999973, 'TIU', '6939b089-c942-4df1-bf79-32e70fa88a4d', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000050, 1000016, 1000004, '2024-08-07 09:21:18.91901', 1000069, '2024-09-20 17:36:44.059121', 1000069, 'Y', 'B26', 'Bàn - 26', ' ', 999974, 'TIU', '855a98c7-deab-41e5-9289-b9929d31958c', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000109, 1000038, 1000004, '2024-09-22 17:13:13.033299', 1000069, '2024-09-22 17:13:13.033299', 1000069, 'Y', '4', 'Bàn 4', NULL, 1000035, 'ETB', '3ed0a346-41fd-4216-9de2-63e62a01c293', 10, NULL, 0, 'N', NULL, 'N', 1000033, 'N');
INSERT INTO pos.d_table VALUES (1000041, 1000016, 1000004, '2024-08-07 09:20:25.228524', 1000069, '2024-09-26 10:19:07.330607', 1000066, 'Y', 'B20', 'Bàn - 20', ' ', 999973, 'TIU', '75529db2-ca5f-43bf-955b-747aa863d59a', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000110, 1000038, 1000004, '2024-09-22 17:13:13.98031', 1000069, '2024-09-22 17:13:13.98031', 1000069, 'Y', '1', 'Bàn 1', NULL, 1000023, 'ETB', 'edf486de-8efd-4482-9651-e676479e7a2a', 10, NULL, 0, 'N', NULL, 'N', 1000037, 'N');
INSERT INTO pos.d_table VALUES (1000111, 1000038, 1000004, '2024-09-22 17:13:14.606534', 1000069, '2024-09-22 17:13:14.606534', 1000069, 'Y', '5', 'Bàn 5', NULL, 1000021, 'ETB', '4ba004ad-e136-4339-b7b1-f8626b62423b', 10, NULL, 0, 'N', NULL, 'N', 1000034, 'N');
INSERT INTO pos.d_table VALUES (1000112, 1000033, 1000004, '2024-09-22 17:13:15.246721', 1000069, '2024-09-22 17:13:15.246721', 1000069, 'Y', '5', 'Bàn 5', NULL, 1000036, 'ETB', '43c0ddc2-a459-4f0b-a584-e349a4845251', 1, NULL, 0, 'N', NULL, 'N', 1000008, 'N');
INSERT INTO pos.d_table VALUES (1000133, 1000033, 1000004, '2024-09-22 17:13:19.114721', 1000069, '2024-09-22 17:13:19.114721', 1000069, 'Y', '9', 'Bàn 9', NULL, 1000036, 'ETB', 'daaaafc0-8a93-443e-a7c0-f1a3a1d72ad1', 1, NULL, 0, 'N', NULL, 'N', 1000012, 'N');
INSERT INTO pos.d_table VALUES (1000113, 1000033, 1000004, '2024-09-22 17:13:17.880087', 1000069, '2024-09-22 17:13:17.880087', 1000069, 'Y', '3', 'Bàn 3', NULL, 1000036, 'ETB', '89045694-bf6d-4180-a292-a57121fa8c9a', 1, NULL, 0, 'N', NULL, 'N', 1000006, 'N');
INSERT INTO pos.d_table VALUES (1000134, 1000038, 1000004, '2024-09-22 17:13:19.162641', 1000069, '2024-09-22 17:13:19.162641', 1000069, 'Y', 'ban3A', 'Bàn 3A', NULL, 1000034, 'ETB', '829d27ce-c299-4ed5-a85f-a6548448343c', 1, NULL, 0, 'N', NULL, 'N', 1000053, 'N');
INSERT INTO pos.d_table VALUES (1000135, 1000038, 1000004, '2024-09-22 17:13:19.223632', 1000069, '2024-09-22 17:13:19.223632', 1000069, 'Y', '1F', 'Bàn 1F', NULL, 1000032, 'ETB', '27253d9c-f409-47fa-bb7a-ffe6745bf5ce', 20, NULL, 0, 'N', NULL, 'N', 1000024, 'N');
INSERT INTO pos.d_table VALUES (1000136, 1000033, 1000004, '2024-09-22 17:13:19.282156', 1000069, '2024-09-22 17:13:19.282156', 1000069, 'Y', '7', 'Bàn 7', NULL, 1000036, 'ETB', '7ce13125-74dc-4a46-bb32-4db4acc8bf10', 1, NULL, 0, 'N', NULL, 'N', 1000010, 'N');
INSERT INTO pos.d_table VALUES (1000137, 1000038, 1000004, '2024-09-22 17:13:19.345437', 1000069, '2024-09-22 17:13:19.345437', 1000069, 'Y', '1', 'Bàn 1E', NULL, 1000017, 'ETB', '0d13d032-d65b-4d80-abb9-a281a05f93aa', 10, NULL, 0, 'N', NULL, 'N', 1000022, 'N');
INSERT INTO pos.d_table VALUES (1000139, 1000033, 1000004, '2024-09-22 17:13:19.450608', 1000069, '2024-09-22 17:13:19.450608', 1000069, 'Y', '4', 'Bàn 4', NULL, 1000036, 'ETB', '389a8d18-4703-4fec-9797-d8ca31a7cb9f', 1, NULL, 0, 'N', NULL, 'N', 1000007, 'N');
INSERT INTO pos.d_table VALUES (1000140, 1000038, 1000004, '2024-09-22 17:13:19.514379', 1000069, '2024-09-22 17:13:19.514379', 1000069, 'Y', '4', 'Bàn 4', NULL, 1000020, 'ETB', '1d31999a-4884-49b3-8c45-5b9d47463429', 10, NULL, 0, 'N', NULL, 'N', 1000056, 'N');
INSERT INTO pos.d_table VALUES (1000077, 1000016, 1000004, '2024-08-26 14:23:15.982083', 1000069, '2024-09-27 11:50:41.514247', 1000066, 'Y', 'B49', 'Bàn - 49', 'h', 1000012, 'TIU', '0c80d704-0c9f-42c3-af3c-eb3af68a3989', 3, 2, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000162, 1000016, 1000004, '2024-09-23 16:54:53.248572', 1000069, '2024-09-27 11:49:37.94189', 1000066, 'Y', 'B02', 'Bàn - 61', 'Bàn to', 999972, 'TBD', '88d4b49e-e286-412b-b712-9033ae1ddc48', 10, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_table VALUES (1000164, 1000016, 1000004, '2024-09-25 16:50:02.418411', 1000069, '2024-09-27 13:03:19.402571', 1000066, 'Y', '1544', 'Bàn -  18', '', 999975, 'ETB', 'f6a6b9a3-f190-4c4c-90cd-2449dc0e4632', 0, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_table VALUES (1000055, 1000016, 1000004, '2024-08-07 09:21:59.167518', 1000069, '2024-09-27 10:19:57.805687', 1000066, 'Y', 'B31', 'Bàn - 31', ' ', 999975, 'ETB', '12edb49f-2300-4640-a894-4cc4e5c6c564', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000064, 1000016, 1000004, '2024-08-07 11:37:34.030055', 1000069, '2024-09-20 08:55:00.59017', 1000069, 'Y', 'B51', 'Bàn - 51', 'Bàn trống', 999976, 'TIU', 'a2b84b5d-9244-41b3-a5e9-5febc861cf9a', 12, 0, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000046, 1000016, 1000004, '2024-08-07 09:21:02.045519', 1000069, '2024-09-27 10:31:26.955781', 1000066, 'Y', 'B44', 'Bàn - 44', ' ', 999974, 'TIU', '5d54e191-588d-43ea-94fb-bda8dbf0da57', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000141, 1000038, 1000004, '2024-09-22 17:13:19.571344', 1000069, '2024-09-22 17:13:19.571344', 1000069, 'Y', '3', 'Bàn 3', NULL, 1000020, 'ETB', 'acaa3813-7721-45e3-a769-c9149bfb7e19', 10, NULL, 0, 'N', NULL, 'N', 1000055, 'N');
INSERT INTO pos.d_table VALUES (1000142, 1000038, 1000004, '2024-09-22 17:13:19.625383', 1000069, '2024-09-22 17:13:19.625383', 1000069, 'Y', '1', 'Room 101', NULL, 1000024, 'ETB', '8098a075-b3a2-4317-857e-5462f0d9d0b2', 10, NULL, 0, 'N', NULL, 'N', 1000039, 'N');
INSERT INTO pos.d_table VALUES (1000143, 1000038, 1000004, '2024-09-22 17:13:19.708399', 1000069, '2024-09-22 17:13:19.708399', 1000069, 'Y', '1', 'Phòng 101', NULL, 1000033, 'ETB', 'c3f0519f-7e0a-4eb2-81af-52e0be23db94', 10, NULL, 0, 'Y', NULL, 'N', 1000038, 'N');
INSERT INTO pos.d_table VALUES (1000144, 1000038, 1000004, '2024-09-22 17:13:19.775534', 1000069, '2024-09-22 17:13:19.775534', 1000069, 'Y', '1B', 'Bàn 1B', NULL, 1000018, 'ETB', 'b7b6d730-2fd3-45d0-9ce1-68d4e466b1a2', 1, NULL, 0, 'N', NULL, 'N', 1000017, 'Y');
INSERT INTO pos.d_table VALUES (1000145, 1000038, 1000004, '2024-09-22 17:13:19.836171', 1000069, '2024-09-22 17:13:19.836171', 1000069, 'Y', '1', 'Room 501', NULL, 1000028, 'ETB', '5cae77e8-c9f2-4c5f-bb04-376ee815cd7e', 10, NULL, 0, 'N', NULL, 'N', 1000048, 'N');
INSERT INTO pos.d_table VALUES (1000146, 1000038, 1000004, '2024-09-22 17:13:19.897352', 1000069, '2024-09-22 17:13:19.897352', 1000069, 'Y', '4', 'Room 201', NULL, 1000025, 'ETB', '2ecd29ed-9cea-484e-ab9a-1c6d4abe4483', 10, NULL, 0, 'N', NULL, 'N', 1000045, 'N');
INSERT INTO pos.d_table VALUES (1000147, 1000038, 1000004, '2024-09-22 17:13:19.948769', 1000069, '2024-09-22 17:13:19.948769', 1000069, 'Y', '1', 'Room 301', NULL, 1000026, 'ETB', '1939bc30-90dd-4b36-84c3-f4f6f9e25b45', 10, NULL, 0, 'N', NULL, 'N', 1000046, 'N');
INSERT INTO pos.d_table VALUES (1000148, 1000038, 1000004, '2024-09-22 17:13:20.010074', 1000069, '2024-09-22 17:13:20.010074', 1000069, 'Y', '1', 'Room 401', NULL, 1000027, 'ETB', 'a4f280d5-9a8d-4b3b-9e14-41e3221a8313', 10, NULL, 0, 'N', NULL, 'N', 1000047, 'N');
INSERT INTO pos.d_table VALUES (1000149, 1000038, 1000004, '2024-09-22 17:13:20.068245', 1000069, '2024-09-22 17:13:20.068245', 1000069, 'Y', '2', 'Bàn 2E', NULL, 1000017, 'ETB', '738cec77-bb35-4295-ad64-901de7963a52', 10, NULL, 0, 'N', NULL, 'N', 1000023, 'N');
INSERT INTO pos.d_table VALUES (1000150, 1000038, 1000004, '2024-09-22 17:13:20.129823', 1000069, '2024-09-22 17:13:20.129823', 1000069, 'Y', '5', 'Bàn 5', NULL, 1000020, 'ETB', '2f189c9b-2971-43a9-b186-9958a8d343da', 10, NULL, 0, 'N', NULL, 'N', 1000057, 'N');
INSERT INTO pos.d_table VALUES (1000151, 1000038, 1000004, '2024-09-22 17:13:20.209464', 1000069, '2024-09-22 17:13:20.209464', 1000069, 'Y', '1', 'Room 601', NULL, 1000029, 'ETB', 'b7591a27-f62d-414f-acd2-c2805ac648d5', 10, NULL, 0, 'N', NULL, 'N', 1000049, 'N');
INSERT INTO pos.d_table VALUES (1000152, 1000038, 1000004, '2024-09-22 17:13:20.278401', 1000069, '2024-09-22 17:13:20.278401', 1000069, 'Y', '3F', 'Bàn 3F', NULL, 1000032, 'ETB', '4cba02dc-52a4-41a8-b783-c88921f34135', 8, NULL, 0, 'N', NULL, 'N', 1000027, 'N');
INSERT INTO pos.d_table VALUES (1000153, 1000038, 1000004, '2024-09-22 17:13:20.340254', 1000069, '2024-09-22 17:13:20.340254', 1000069, 'Y', '2A', 'Bàn 2A', NULL, 1000037, 'ETB', 'e7e533be-a6bc-4174-9929-873331368adb', 1, NULL, 0, 'N', NULL, 'N', 1000020, 'N');
INSERT INTO pos.d_table VALUES (1000154, 1000038, 1000004, '2024-09-22 17:13:20.395721', 1000069, '2024-09-22 17:13:20.395721', 1000069, 'Y', '1D', 'Bàn 1D', NULL, 1000031, 'ETB', '97acccde-8259-4c3c-9e89-2dacc1a3ef7e', 1, NULL, 0, 'N', NULL, 'N', 1000019, 'N');
INSERT INTO pos.d_table VALUES (1000155, 1000038, 1000004, '2024-09-22 17:13:20.455439', 1000069, '2024-09-22 17:13:20.455439', 1000069, 'Y', '2', 'Bàn 2', NULL, 1000035, 'ETB', '28d760b6-7021-46f5-8c3a-84d60a386e6c', 10, NULL, 0, 'N', NULL, 'N', 1000031, 'N');
INSERT INTO pos.d_table VALUES (1000156, 1000038, 1000004, '2024-09-22 17:13:20.512543', 1000069, '2024-09-22 17:13:20.512543', 1000069, 'Y', '2F', 'Bàn 2F', NULL, 1000032, 'ETB', 'c35de24b-71a1-4a5b-9ed5-900f7653e8ad', 10, NULL, 0, 'N', NULL, 'N', 1000026, 'N');
INSERT INTO pos.d_table VALUES (1000157, 1000038, 1000004, '2024-09-22 17:13:20.586096', 1000069, '2024-09-22 17:13:20.586096', 1000069, 'Y', '1C', 'Bàn 1C', NULL, 1000030, 'ETB', '81bf2e44-6ee9-4ff6-81a6-9b69ca544b47', 1, NULL, 0, 'N', NULL, 'N', 1000018, 'N');
INSERT INTO pos.d_table VALUES (1000158, 1000038, 1000004, '2024-09-22 17:13:20.673333', 1000069, '2024-09-22 17:13:20.673333', 1000069, 'Y', '3', 'Bàn 3', NULL, 1000035, 'ETB', '6981f6ae-db35-4f45-ac92-37b397d18768', 10, NULL, 0, 'N', NULL, 'N', 1000032, 'N');
INSERT INTO pos.d_table VALUES (1000159, 1000038, 1000004, '2024-09-22 17:13:20.74451', 1000069, '2024-09-22 17:13:20.74451', 1000069, 'Y', '1', 'Bàn 1', NULL, 1000035, 'ETB', '6379e3d7-fd8a-486b-8c03-b06d0149ce05', 10, NULL, 0, 'Y', NULL, 'N', 1000029, 'N');
INSERT INTO pos.d_table VALUES (1000160, 1000038, 1000004, '2024-09-22 17:13:20.808507', 1000069, '2024-09-22 17:13:20.808507', 1000069, 'Y', 'LB1', 'Line Buffet Tầng 1', NULL, 1000019, 'ETB', 'e3fceecd-98a7-491b-8d20-2b7f10423311', 1, NULL, 0, 'N', NULL, 'N', 1000028, 'Y');
INSERT INTO pos.d_table VALUES (1000053, 1000016, 1000004, '2024-08-07 09:21:50.536471', 1000069, '2024-09-20 17:25:22.965691', 1000069, 'Y', 'B29', 'Bàn - 29', ' ', 999975, 'ETB', 'ff122911-b892-4145-948b-92cfb873036c', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000049, 1000016, 1000004, '2024-08-07 09:21:15.19093', 1000069, '2024-09-24 10:53:21.904703', 1000069, 'Y', 'B46', 'Bàn - 46', ' ', 999974, 'TIU', '606e309e-ed54-49ac-a447-586744b55616', 3, 2, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000076, 1000016, 1000004, '2024-08-26 14:17:21.400127', 1000069, '2024-09-27 11:07:26.389673', 1000066, 'Y', 'B48', 'Bàn - 48', 'hi', 999972, 'TIU', 'f137c7a8-79fa-4463-91c0-29133710720c', 2, 2, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000039, 1000016, 1000004, '2024-08-07 09:19:52.645008', 1000069, '2024-09-27 16:13:45.707249', 1000066, 'Y', 'B18', 'Bàn - 18', ' ', 999972, 'TIU', '4af92d3a-b7c3-4c36-bf36-3aaed3cc2741', 2, 1, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000165, 1000016, 1000004, '2024-09-26 17:58:46.745918', 1000066, '2024-09-27 17:49:41.824378', 1000066, 'N', 'B5', 'Bàn 5', ' ', 999975, 'TIU', '421ce7d3-dc14-4a98-a97b-3fde5e5e902b', 2, 1, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO pos.d_table VALUES (1000078, 1000016, 1000004, '2024-08-26 17:31:36.481602', 1000069, '2024-09-24 10:46:47.365976', 1000069, 'Y', 'B50', 'Bàn - 50', '1', 999975, 'TIU', '8c7edaa0-a802-4282-aee9-bb4d77b593fb', 12, 0, NULL, 'N', NULL, 'N', NULL, 'N');
INSERT INTO pos.d_table VALUES (1000163, 1000016, 1000004, '2024-09-25 15:42:30.249134', 1000069, '2024-09-27 17:24:33.385564', 1000066, 'Y', '112', 'Bàn - 60', 'Ban to', 999973, 'TIU', 'a79901b2-3c96-48ff-ab09-2c7a9c7480c3', 10, 0, NULL, NULL, NULL, NULL, NULL, NULL);


--
-- TOC entry 5433 (class 0 OID 392962)
-- Dependencies: 339
-- Data for Name: d_table_use_ref; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_table_use_ref VALUES (1000002, 0, '2024-08-28', 0, '2024-08-28', 0, 'Y', 'Product', '056609f7-adc4-47ac-ab96-e7aecb28eb3b', 0, 1000003, 'productType');
INSERT INTO pos.d_table_use_ref VALUES (1000001, 0, '2024-08-28', 0, '2024-08-28', 0, 'Y', 'Table', '7b82e1a0-3881-4184-b75b-68ec01929e7e', 0, 1000007, 'tableStatus');
INSERT INTO pos.d_table_use_ref VALUES (1000000, 0, '2024-08-28', 0, '2024-08-28', 0, 'Y', 'ReservationOrder', '5797d0cb-6a24-47ad-b42d-83de6fafaa66', 0, 1000004, 'status');
INSERT INTO pos.d_table_use_ref VALUES (1000005, 0, '2024-08-28', 0, '2024-08-28', 0, 'Y', 'KitchenOrderLine', 'b6b43d24-e2c0-4a70-9688-f4d6aa5d1844', 0, 1000005, 'orderlineStatus');
INSERT INTO pos.d_table_use_ref VALUES (1000003, 0, '2024-08-28', 0, '2024-08-28', 0, 'Y', 'Product', '50ed678a-cf52-41ee-b302-90ff90d9e5e7', 0, 1000001, 'groupType');
INSERT INTO pos.d_table_use_ref VALUES (1000004, 0, '2024-08-28', 0, '2024-08-28', 0, 'Y', 'KitchenOrder', 'c273e034-4acf-4602-b9f0-0b6ee452b30d', 0, 1000005, 'orderStatus');


--
-- TOC entry 5350 (class 0 OID 385874)
-- Dependencies: 255
-- Data for Name: d_tax; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_tax VALUES (1000005, 1000009, 1000022, 'VAT10', 1000008, 10, 'Y', 'N', NULL, '2024-09-12 09:21:16.366093', 1000071, '2024-09-12 09:21:16.366096', 1000071, 'c0d30f9b-73da-43f5-9ff5-a4b86615e4a5');
INSERT INTO pos.d_tax VALUES (1000003, 1000004, 1000016, 'VAT 10', 1000008, 10, 'Y', 'N', NULL, '2024-08-29 17:31:51.832382', 1000069, '2024-09-24 11:20:36.609792', 1000069, '5540d837-5124-4647-86a3-5f3b2d666f30');
INSERT INTO pos.d_tax VALUES (1000002, 1000004, 1000016, 'VAT 5', 1000008, 6, 'Y', 'N', NULL, '2024-08-15 17:34:34.769153', 1000069, '2024-09-27 00:12:30.890014', 1000066, '95a50523-b8bb-468e-810b-29d028192079');
INSERT INTO pos.d_tax VALUES (1000004, 1000004, 1000016, 'VAT 8', 1000008, 8, 'Y', 'N', NULL, '2024-08-29 17:33:04.015281', 1000069, '2024-09-27 00:35:39.156697', 1000066, 'fbd4a944-9bf8-4753-91fb-0f025f311c84');


--
-- TOC entry 5351 (class 0 OID 385884)
-- Dependencies: 256
-- Data for Name: d_tax_category; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_tax_category VALUES (1000006, 1000004, 1000016, 'NHÓM VAT5', 'This is an example description that can be up to 255 characters long.', 'N', 'N', 1000069, '2024-08-15 11:56:25.631311', '2024-08-15 11:56:25.631311', 1000069, 'f4ededc7-3b8a-44fb-9e21-9238092bbb40');
INSERT INTO pos.d_tax_category VALUES (1000008, 1000004, 1000016, 'NHÓM VAT8', 'This is an example description that can be up to 255 characters long.', 'Y', 'Y', 1000069, '2024-08-15 12:00:50.381239', '2024-08-15 12:01:25.749058', 1000069, '13b02b85-3005-4baa-9c42-fd2abd75a9a1');


--
-- TOC entry 5352 (class 0 OID 385893)
-- Dependencies: 257
-- Data for Name: d_tenant; Type: TABLE DATA; Schema: pos; Owner: -
--


--
-- TOC entry 5352 (class 0 OID 385893)
-- Dependencies: 257
-- Data for Name: d_tenant; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_tenant VALUES (1000048, 'F&B', 'FB_XAMPLE1', 'https://duytien14.ssg.vn', 1000000, 'Y', '100000', NULL, NULL, '2024-09-26 10:51:24.106276', 0, '2024-09-26 10:51:24.509833', 0, '855ac760-76c7-42d9-84ed-7409c9d201b7', 'usr_d362c627f0d0', 'MmI1ZWQ2MTAt', 'db_4bb0a314b744', 'SUCCESS');
INSERT INTO pos.d_tenant VALUES (1000049, 'F&B', 'FB_XAMPLE2', 'https://duytien15.ssg.vn', 1000000, 'Y', '100000', NULL, NULL, '2024-09-26 16:37:06.041982', 0, '2024-09-26 16:37:06.365694', 0, '01d5c946-d743-4460-a423-06a6bfb55bfa', 'usr_a30217d18490', 'NGVmNzM5ZTct', 'db_e44341329209', 'SUCCESS');
INSERT INTO pos.d_tenant VALUES (1000002, 'ONSEN', 'Onsen FUJI', 'https://dbizmobile.ssg.vn:8443/webui/index.zul', 1000008, 'Y', '100000', NULL, NULL, '2024-07-03 11:58:43.413357', 0, '2024-07-22 23:14:07.095951', 1000037, '63a6ad5f-4812-4c31-a90d-b1b7d8dcdc1e', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_tenant VALUES (1000003, 'DBIZ', 'DBIZ Tech', 'https://digitalbiz.com.vn/', 1000008, 'Y', '100002', NULL, NULL, '2024-07-12 02:40:30.086812', 0, '2024-07-23 22:30:32.435023', 1000068, '58cae7a5-6a68-4571-b522-4c04f7e9fe55', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_tenant VALUES (1000004, 'F&B', 'F&B Sample', 'https://dbizmobile.ssg.vn:8443/webui/index.zul', 1000008, 'Y', '100001', NULL, NULL, '2024-07-03 11:58:43.413357', 0, '2024-07-22 23:14:07.095951', 1000037, '732237ed-fcc9-4c5b-a615-fc86ba0e3c59', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_tenant VALUES (0, 'SYSTEM', 'SYSTEM', NULL, NULL, 'Y', NULL, NULL, NULL, '2024-08-24 05:51:46.280949', 0, '2024-08-24 05:51:46.280949', 0, '2731f792-90fe-46ff-92fc-dd590981ae28', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_tenant VALUES (1000009, 'THEP', 'Thép Tây Nam', 'https://taynamsteel.com/vi/san-pham/', NULL, 'Y', NULL, NULL, NULL, '2024-09-10 08:33:04.728475', 0, '2024-09-10 08:33:04.728475', 0, 'b482aa25-79fa-4d0a-976d-cdf548856b5f', NULL, NULL, NULL, NULL);
INSERT INTO pos.d_tenant VALUES (1000050, 'F&B', 'FB_XAMPLE3', 'https://duytien16.ssg.vn', 1000000, 'Y', '100000', NULL, NULL, '2024-09-26 16:51:37.163159', 0, '2024-09-26 16:51:37.583105', 0, 'bdc0d11c-8ed1-4391-a30a-251b512cce3f', 'usr_57f99a9d0903', 'NDUyMmM5ZmMt', 'db_cd8d0706dc73', 'SUCCESS');
INSERT INTO pos.d_tenant VALUES (1000051, 'F&B', 'FB_XAMPLE4', 'https://FB_XAMPLE4.ssg.vn', 1000000, 'Y', '100000', NULL, NULL, '2024-09-27 03:00:40.131781', 0, '2024-09-27 03:00:40.633359', 0, '9a559c2c-e7b4-401b-a83c-fc4285dc5124', 'usr_c175f3c86f98', 'OGVkZDQyMzQt', 'db_dbc879798f5b', 'SUCCESS');
INSERT INTO pos.d_tenant VALUES (1000052, 'F&B', 'FB_XAMPLE5', 'https://FB_XAMPLE5.ssg.vn', 1000000, 'Y', '100000', NULL, NULL, '2024-09-27 11:45:23.252334', 0, '2024-09-27 11:45:24.046395', 0, '3e7b117e-0022-40c4-8284-3c56ec1a9f23', 'usr_668306ed7c7f', 'OTA1NGE4OTct', 'db_2482ae1687d3', 'SUCCESS');
INSERT INTO pos.d_tenant VALUES (1000053, 'F&B', 'FB_XAMPLE6', 'https://FB_XAMPLE6.ssg.vn', 1000000, 'Y', '100000', NULL, NULL, '2024-09-28 09:51:30.874647', 0, '2024-09-28 09:51:31.393788', 0, 'df18efd2-81b2-4be4-852b-b09940558627', 'usr_a1da15e2b474', 'MzE3ZWM3MWIt', 'db_944115b0f44e', 'SUCCESS');
INSERT INTO pos.d_tenant VALUES (1000047, 'F&B', 'FB_XAMPLE', 'https://duytien13.ssg.vn', 1000000, 'Y', '100000', NULL, NULL, '2024-09-26 09:23:19.499658', 0, '2024-09-26 09:23:19.83512', 0, 'c85cc0df-a67e-4295-8236-41384cfed545', 'usr_d669d846769c', 'NWZhNzU1MTIt', 'db_e2086d1d873e', 'SUCCESS');



INSERT INTO pos.d_user VALUES (1000073, 'vts@gmail.com', 'VLXD VTS', NULL, '$2a$10$j6s1Hq0C6MmMI2jJtFc/0eKGb.2EQqH9TmASIybaBozy0smAIOfmG', NULL, NULL, NULL, 'Y', 1000009, NULL, 'Y', NULL, NULL, '2024-09-12 02:13:56.582864', 0, '2024-09-12 02:13:56.582864', 0, '1fc6b090-9324-4712-9dab-f3589dc48404', NULL);
INSERT INTO pos.d_user VALUES (1000072, 'cuahang1@gmail.com', 'VLXD DEMO', NULL, '$2a$10$j6s1Hq0C6MmMI2jJtFc/0eKGb.2EQqH9TmASIybaBozy0smAIOfmG', NULL, NULL, NULL, 'Y', 1000009, NULL, 'Y', NULL, NULL, '2024-09-11 04:28:32.150994', 0, '2024-09-11 04:28:32.150994', 0, 'fd3b321d-8e1f-48eb-a84e-dcd6f3d01765', NULL);
INSERT INTO pos.d_user VALUES (1000078, 'Administrator', 'Administrator', '00000', '$2a$10$j6s1Hq0C6MmMI2jJtFc/0eKGb.2EQqH9TmASIybaBozy0smAIOfmG', NULL, 'administrator@gmail.com', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-18 08:24:39.809855', 0, '2024-09-18 08:24:39.809855', 0, '54a03466-98dd-472b-89ca-79d579e4cf49', NULL);
INSERT INTO pos.d_user VALUES (1000070, 'thanhnc', 'Nguyễn Chí Thanh', '+843444', '$2a$10$j6s1Hq0C6MmMI2jJtFc/0eKGb.2EQqH9TmASIybaBozy0smAIOfmG', NULL, 'chithanh@gmail.com', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-07-08 16:41:43.061964', 0, NULL, 0, '48520631-a0f3-4e7f-8203-ac329b6167af', NULL);
INSERT INTO pos.d_user VALUES (1000001, 'thanhnc0', 'Nguyễn Chí Thanh', '0384449114', '$2a$10$uZCq2CDh7IwGOlnhGnv3hu7qVb31CKZzK31sveCqYtxqzKsxISasu', NULL, 'chithanh03062001@gmail.com', NULL, 'Y', 1000002, NULL, 'Y', NULL, NULL, '2024-07-08 02:18:27.065047', 0, '2024-07-08 02:18:27.065047', 0, '38215a91-7dab-419b-8716-956a8413ec4a', NULL);
INSERT INTO pos.d_user VALUES (1000079, 'User', 'Thanh', NULL, '$2a$10$j6s1Hq0C6MmMI2jJtFc/0eKGb.2EQqH9TmASIybaBozy0smAIOfmG', NULL, NULL, NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-18 08:26:13.736236', 0, '2024-09-18 08:26:13.736236', 0, 'ef943fc4-3064-43f8-ae56-53eb75930a72', NULL);
INSERT INTO pos.d_user VALUES (1000037, 'WebService', 'Dbiz', '0000000', '$2a$10$KiTO2H9FZ5lQjY3ZSI.wAeZGkQkA4iDXdaYzHeREej36/bqkjeIyS', NULL, 'WebService@gmail.com', NULL, 'Y', 1000002, NULL, 'Y', NULL, NULL, '2024-07-08 20:50:15.048367', NULL, NULL, NULL, 'd192f6d2-5c17-4ffc-b8e9-29628023b186', NULL);
INSERT INTO pos.d_user VALUES (1000035, 'thanhnc', 'Nguyễn Chí Thanh', '+843444', '$2a$10$j6s1Hq0C6MmMI2jJtFc/0eKGb.2EQqH9TmASIybaBozy0smAIOfmG', NULL, 'chithanh@gmail.com', NULL, 'Y', 1000002, NULL, 'Y', NULL, NULL, '2024-07-08 16:41:43.061964', NULL, NULL, NULL, '5821f3eb-aefb-44bf-b306-d01a5ee24b56', NULL);
INSERT INTO pos.d_user VALUES (1000066, 'WebService', 'Dbiz', '0000000', '$2a$10$KiTO2H9FZ5lQjY3ZSI.wAeZGkQkA4iDXdaYzHeREej36/bqkjeIyS', NULL, 'WebService@gmail.com', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-07-11 16:29:14.726232', 1000037, NULL, NULL, '4cb9995e-d53d-40fe-8799-4ceeaecb89ac', NULL);
INSERT INTO pos.d_user VALUES (1000186, 'thanhnc', NULL, NULL, '$2a$10$vMnadp0oefA8IQSLsAlDUetznuK1JrDMJu/27RMA7jHAs6hknBc7.', NULL, NULL, NULL, 'Y', NULL, NULL, 'Y', NULL, NULL, '2024-09-26 04:55:13.035131', 0, '2024-09-26 04:55:13.035131', 0, '49577b79-5615-4035-9b30-1bec60347efa', NULL);
INSERT INTO pos.d_user VALUES (1000077, 'WebService', NULL, NULL, '$2a$10$KiTO2H9FZ5lQjY3ZSI.wAeZGkQkA4iDXdaYzHeREej36/bqkjeIyS', NULL, NULL, NULL, 'Y', NULL, NULL, 'Y', NULL, NULL, '2024-09-12 10:00:38.585751', 0, '2024-09-12 10:00:38.585751', 0, '3559602f-37ba-4972-988b-287057968a8f', NULL);
INSERT INTO pos.d_user VALUES (1000071, 'WebService', 'WebService', NULL, '$2a$10$KiTO2H9FZ5lQjY3ZSI.wAeZGkQkA4iDXdaYzHeREej36/bqkjeIyS', NULL, NULL, NULL, 'Y', NULL, NULL, 'Y', NULL, NULL, '2024-09-11 04:28:32.150994', 0, '2024-09-11 04:28:32.150994', 0, '30937152-dab8-48aa-bcfd-a4ae76185f2b', NULL);
INSERT INTO pos.d_user VALUES (1000068, 'WebService', 'Dbiz', '0000000', '$2a$10$KiTO2H9FZ5lQjY3ZSI.wAeZGkQkA4iDXdaYzHeREej36/bqkjeIyS', NULL, 'WebService@gmail.com', NULL, 'Y', NULL, NULL, 'Y', NULL, NULL, '2024-07-08 20:50:15.048367', NULL, NULL, NULL, '46676fd0-1b0b-4d0c-b0e0-1a40b69a1780', NULL);
INSERT INTO pos.d_user VALUES (1000064, 'tesst1', 'Dbiz', '0000000', '$2a$10$k4g68H1eAZIq6.pOoOAo7eh8QMA.eRU1/itEP5Yd2hCrN24vwQJmW', NULL, 'WebService@gmail.com', NULL, 'Y', 1000002, NULL, 'Y', NULL, NULL, '2024-07-11 04:47:47.654848', 1000037, NULL, NULL, 'f7ff763e-df71-404a-92fc-4a433f5f9c27', NULL);
INSERT INTO pos.d_user VALUES (1000065, 'WebService13343', 'Dbiz', '0000000', '$2a$10$jSqw17teUbkWMgQ2judmRuTalKZ51rRnnCyBBsp4h/A9gxUvaMYAS', NULL, 'WebService@gmail.com', NULL, 'Y', 1000002, NULL, 'Y', NULL, NULL, '2024-07-11 16:28:51.23276', 1000037, NULL, NULL, '72bb0769-ab2e-4974-aedd-487fad2ebe5e', NULL);
INSERT INTO pos.d_user VALUES (1000069, 'WebService', 'Dbiz', '0000000', '$2a$10$KiTO2H9FZ5lQjY3ZSI.wAeZGkQkA4iDXdaYzHeREej36/bqkjeIyS', NULL, 'WebService@gmail.com', NULL, 'Y', NULL, NULL, 'Y', NULL, NULL, '2024-07-08 20:50:15.048367', 0, NULL, 0, 'b1d7a7c6-c406-4c46-9842-dc62d3476aff', NULL);
INSERT INTO pos.d_user VALUES (1000076, 'WebService', NULL, NULL, '$2a$10$KiTO2H9FZ5lQjY3ZSI.wAeZGkQkA4iDXdaYzHeREej36/bqkjeIyS', NULL, NULL, NULL, 'Y', NULL, NULL, 'Y', NULL, NULL, '2024-09-12 09:59:40.367027', 0, '2024-09-12 09:59:40.367027', 0, '09842088-5fda-43c6-9b91-09efb7402b79', NULL);
INSERT INTO pos.d_user VALUES (1000172, 'ofb_bepnong', 'ofb_bepnong', NULL, '$2a$10$zCH2b.cHkNImqieaaPG6WOsE2FXlp4nAHNNSwzVeFcDUwhJnqPyMS', NULL, '', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-25 23:20:23.836638', 1000069, '2024-09-25 23:20:23.836638', 1000069, '5cf93370-94b8-4bda-92af-b57b4869278e', 1001362);
INSERT INTO pos.d_user VALUES (1000173, 'ofb_bar', 'ofb_bar', NULL, '$2a$10$VOJfoUhGn3HrjdxF6FFE1.uyiSFSV3gdSZ0gQAe2VQL40eq3jh6WW', NULL, '', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-25 23:20:24.090708', 1000069, '2024-09-25 23:20:24.090708', 1000069, '07b8454f-e6dd-4e12-9725-cc73b9abe828', 1001378);
INSERT INTO pos.d_user VALUES (1000174, 'nvbep', 'nvbep', NULL, '$2a$10$NbPiGc1.7YgBAiYfpJHQaOemes5t2gKIQoLlZ0Rv6cXmvxdjccoaK', NULL, 'khanhnq@ssg.vn', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-25 23:20:24.274039', 1000069, '2024-09-25 23:20:24.274039', 1000069, 'dd180783-2fae-4c09-b06b-b2c72e34cd44', 1001336);
INSERT INTO pos.d_user VALUES (1000175, 'OFB_BEPLANH', 'OFB_BEPLANH', NULL, '$2a$10$Xx9XUHLPzmLrxi9OVe/NxOcEOxytO8MEwLopBYueaTq2lCG1h8o0C', NULL, '', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-25 23:20:24.536603', 1000069, '2024-09-25 23:20:24.536603', 1000069, '8b6492ad-abc7-4f4d-998f-ccc70fcfe2a5', 1001368);
INSERT INTO pos.d_user VALUES (1000176, 'testnv', 'testnv', NULL, '$2a$10$ZwPytq/VQeAIcNusdBclFeN/D62TrtZSOO3WtxW6a89h/ANOV6Tmi', NULL, '', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-25 23:20:24.774719', 1000069, '2024-09-25 23:20:24.774719', 1000069, '5c0a51f6-019b-45c4-847e-d761559d4145', 1001787);
INSERT INTO pos.d_user VALUES (1000177, 'dev', 'dev', NULL, '$2a$10$yOBg7Z7EK0uOMqM574YlH.6qMjyja71D7in6qaX3nGJO3HcEe9xhm', NULL, 'chithanh03062001@gmail.com', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-25 23:20:24.999574', 1000069, '2024-09-25 23:20:24.999574', 1000069, '8e93b863-31ad-432b-b343-4fdd01228cb9', 1001324);
INSERT INTO pos.d_user VALUES (1000178, 'KDSTest', 'KDSTest', NULL, '$2a$10$JZTGebsfiMbA76TiwT962.OZjRXerejropYTFV43zqTZsrJpF.RiK', NULL, 'KDSTest@gmail.com', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-25 23:20:25.680673', 1000069, '2024-09-25 23:20:25.680673', 1000069, 'cda90871-b224-41cf-adb3-0d640445b4de', 1001792);
INSERT INTO pos.d_user VALUES (1000179, 'nvphucvu', 'nvphucvu', NULL, '$2a$10$BCC5SztGv2xirVrxyKX0mugvasSF4tYUleRFtC4mt/aNTWC8CDaV.', NULL, 'khanhnq@ssg.vn', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-25 23:20:25.854951', 1000069, '2024-09-25 23:20:25.854951', 1000069, 'b99baece-a16f-495b-ad02-b5b693dbe3d4', 1001338);
INSERT INTO pos.d_user VALUES (1000180, 'OFB_BEPBANH', 'OFB_BEPBANH', NULL, '$2a$10$QcdbjFZe0PftZEGF8arJce2wK6Pc0hx6GO0eEspelpHhNzcpG80Aa', NULL, '', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-25 23:20:26.209932', 1000069, '2024-09-25 23:20:26.209932', 1000069, '93477122-c2de-47b3-a4f1-98ec50598511', 1001375);
INSERT INTO pos.d_user VALUES (1000181, 'nvpos', 'nvpos', NULL, '$2a$10$NoLqIbG7SectbOXL8Na/B.6LNuVhQrQpkYJD7/E9c6TP6DxJcDiGy', NULL, '', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-25 23:20:26.431368', 1000069, '2024-09-25 23:20:26.431368', 1000069, 'd2e94e8c-ac7f-4fa7-9a17-1726abae584c', 1001451);
INSERT INTO pos.d_user VALUES (1000182, 'Staff', 'Staff', NULL, '$2a$10$q3VSO.uFuG11WMX/Dj9/Xe9D56DVTkCVACkZ6cG9YKiJxbqxCLpDa', NULL, '', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-25 23:20:27.382045', 1000069, '2024-09-25 23:20:27.382045', 1000069, 'c5f852b6-a65e-4acd-a89e-16e17d688261', 1001339);
INSERT INTO pos.d_user VALUES (1000183, 'OFB_PVU', 'OFB_PVU', NULL, '$2a$10$Je1fiUlaMZoWYhXH.XitNuO.QvPWOTr4gcFAe06/VRcLYtH/6j9.6', NULL, '', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-25 23:20:27.598526', 1000069, '2024-09-25 23:20:27.598526', 1000069, 'c37a1992-5a6d-4e53-ad7a-0589c1c156c4', 1001376);
INSERT INTO pos.d_user VALUES (1000184, 'nvbar', 'nvbar', NULL, '$2a$10$/TV1jvprw2oE.UnkmXGJdO7CgqGTFc7Qp/n9OLoeZxO6F43nhJd/6', NULL, 'khanhnq@ssg.vn', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-25 23:20:27.895124', 1000069, '2024-09-25 23:20:27.895124', 1000069, '1add1789-6249-4fc9-8ea8-1840e8cc9fe4', 1001337);
INSERT INTO pos.d_user VALUES (1000185, 'testkds', 'testkds', NULL, '$2a$10$eqkgiNM1GhDkjO5FuAfRkO5YsZlZ1onmmpqoRaLHRzon5h4l9xc1u', NULL, '', NULL, 'Y', 1000004, NULL, 'Y', NULL, NULL, '2024-09-25 23:20:28.351115', 1000069, '2024-09-25 23:20:28.351115', 1000069, 'ed5e4f5b-3a6f-420f-8626-0583b7ccce63', 1001788);
INSERT INTO pos.d_user VALUES (1000187, 'WebService', 'WebService', NULL, '$2a$10$ZGwze4Et1Tc7xdvkMVVNYub4OpXSlYjjjklnuhelDlo20yHyWNlwK', NULL, NULL, NULL, 'Y', NULL, NULL, 'Y', NULL, NULL, '2024-09-26 07:30:32.81741', 0, '2024-09-26 07:30:32.81741', 0, '23dfc2b7-a1eb-4c1f-8d91-9a17bb00647d', NULL);
INSERT INTO pos.d_user VALUES (1000194, 'WebService', 'WebService', NULL, '$2a$10$.E1rtXNR.Mu4niOSCvBdcORXLagGL1Lb..IKe7ehvArTSIcXWd63S', NULL, NULL, NULL, 'Y', 1000047, NULL, 'Y', NULL, NULL, '2024-09-26 09:23:36.07188', 0, '2024-09-26 09:23:36.07188', 0, 'c2a40874-746b-4511-8da9-eb549b6b0135', NULL);
INSERT INTO pos.d_user VALUES (1000195, 'WebService', 'WebService', NULL, '$2a$10$WMrD7eDQFEjQspOb2KiqeOrDRyuOY85s0Jesxh9kvC0.egRRH3raK', NULL, NULL, NULL, 'Y', 1000048, NULL, 'Y', NULL, NULL, '2024-09-26 10:51:41.293245', 0, '2024-09-26 10:51:41.293245', 0, '9056771f-f1db-4548-8d16-9bef90c02951', NULL);
INSERT INTO pos.d_user VALUES (1000196, 'WebService', 'WebService', NULL, '$2a$10$36xOzskRGz6L0ETu4T3z/.0YhVyeol8.2QadeuEGOZ5LmcZ3Fzvm.', NULL, NULL, NULL, 'Y', 1000049, NULL, 'Y', NULL, NULL, '2024-09-26 16:37:16.588216', 0, '2024-09-26 16:37:16.588216', 0, 'aa35bfd7-49c0-44a4-9730-fdaa52bdad09', NULL);
INSERT INTO pos.d_user VALUES (1000197, 'WebService', 'WebService', NULL, '$2a$10$8ukhHXGJi2mVwszCZa6Leu1gHpp1PaiCQt8xtTCw9SBIsAPgQB0xG', NULL, NULL, NULL, 'Y', 1000050, NULL, 'Y', NULL, NULL, '2024-09-26 16:51:48.206528', 0, '2024-09-26 16:51:48.206528', 0, '66569d05-10a8-404f-b0cf-eff51e43c632', NULL);
INSERT INTO pos.d_user VALUES (1000198, 'WebService', 'WebService', NULL, '$2a$10$vN30QIiJCNhLupSNgoq2uuRmVblHsiTt5OSNNTHGSBEB2IURKtsf2', NULL, NULL, NULL, 'Y', 1000051, NULL, 'Y', NULL, NULL, '2024-09-27 03:00:59.913548', 0, '2024-09-27 03:00:59.913548', 0, 'f46105bb-7bd4-4e9c-80ec-2b0ba3970b13', NULL);
INSERT INTO pos.d_user VALUES (1000199, 'WebService', 'WebService', NULL, '$2a$10$Ic1jo4UUIi8WNJdh87nEh.6wslc5.QjEO1pJ88VrPvWmIU21HAuPW', NULL, NULL, NULL, 'Y', 1000052, NULL, 'Y', NULL, NULL, '2024-09-27 11:46:09.557958', 0, '2024-09-27 11:46:09.557958', 0, '2d1d4968-b434-49eb-b5a6-100f3495031f', NULL);
INSERT INTO pos.d_user VALUES (1000200, 'WebService', 'WebService', NULL, '$2a$10$lgdKJIdRNPyfTum/r1IJS.4BXTVlu3V2k9ROXfKvKCLnRHQD73saO', NULL, NULL, NULL, 'Y', 1000053, NULL, 'Y', NULL, NULL, '2024-09-28 09:51:46.886827', 0, '2024-09-28 09:51:46.886827', 0, '4e51082b-96af-4ad6-91e0-9e93f9d3a231', NULL);



--
-- TOC entry 5353 (class 0 OID 385904)
-- Dependencies: 258
-- Data for Name: d_uom; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_uom VALUES (1000003, 1000003, 1000003, '2024-07-12 03:18:38.917481', 0, '2024-07-12 03:18:38.917481', 0, 'B', 'Bộ', NULL, '', 'Y');
INSERT INTO pos.d_uom VALUES (1000000, 1000001, 1000002, '2024-07-10 05:11:24.951595', 0, '2024-07-10 05:11:24.951595', 0, 'C', 'Cây', NULL, 'bb8511c4-a7d0-472d-a9bb-bfc321423bd2', 'Y');
INSERT INTO pos.d_uom VALUES (1000002, 1000001, 1000002, '2024-07-12 03:18:38.917481', 0, '2024-07-12 03:18:38.917481', 0, 'M', 'Mét', NULL, '8bfb3525-e011-446d-8527-7e0e1cf47019', 'Y');
INSERT INTO pos.d_uom VALUES (1000009, 1000003, 1000003, '2024-07-12 03:18:38.917481', 0, '2024-07-12 03:18:38.917481', 0, 'CI', 'Cái', NULL, '8bfb3525-e011-446d-8527-7e0e1cf47999', 'Y');
INSERT INTO pos.d_uom VALUES (1000010, 1000016, 1000004, '2024-08-04 12:04:41.711988', 1000069, '2024-08-04 12:04:41.711988', 1000069, 'KG', 'Kilogram', 'kilogram', 'ecae96d3-3e47-4a78-951c-0798d2924589', 'Y');
INSERT INTO pos.d_uom VALUES (1000011, 1000016, 1000004, '2024-08-04 12:08:40.663141', 1000069, '2024-08-04 12:08:40.663141', 1000069, 'Trai', 'Chai', 'trên 18+ ', '256df28d-e71c-4251-a1e8-59606f9f234d', 'Y');
INSERT INTO pos.d_uom VALUES (1000012, 1000016, 1000004, '2024-08-04 12:14:50.883728', 1000069, '2024-08-04 12:14:50.883728', 1000069, 'Phần', 'Phần', 'trên 18+ ', 'd33432de-316c-4d27-84f7-cb8002fd3842', 'Y');
INSERT INTO pos.d_uom VALUES (1000013, 1000016, 1000004, '2024-08-07 09:11:14.255668', 1000069, '2024-08-07 09:11:14.25567', 1000069, 'Con', 'Con', ' ', 'ec83bd4c-c354-4455-93f8-b187c763a375', 'Y');
INSERT INTO pos.d_uom VALUES (1000014, 1000016, 1000004, '2024-08-29 09:20:31.082091', 1000069, '2024-08-29 09:20:31.082093', 1000069, 'Hủ', 'Hủ', NULL, '44f6cc94-21c6-4760-836e-a36412264e58', 'Y');
INSERT INTO pos.d_uom VALUES (1000015, 1000016, 1000004, '2024-08-29 09:46:42.867592', 1000069, '2024-08-29 09:46:42.867593', 1000069, 'Lọ', 'Lọ', NULL, 'c4d14f18-4817-4b0f-9444-8e409078fc35', 'Y');
INSERT INTO pos.d_uom VALUES (1000016, 1000022, 1000009, '2024-09-11 15:06:36.296689', 1000071, '2024-09-11 15:06:36.296691', 1000071, 'M', 'M', NULL, 'b3a26123-f33e-441c-ac77-ae836fe962ac', 'Y');
INSERT INTO pos.d_uom VALUES (1000017, 1000022, 1000009, '2024-09-11 18:07:41.064878', 1000071, '2024-09-11 18:07:41.064879', 1000071, 'Cây', 'Cây', ' ', '49fbc434-00c2-4f51-85f4-29d1c8a815a0', 'Y');
INSERT INTO pos.d_uom VALUES (1000018, 1000016, 1000004, '2024-09-26 15:01:26.503808', 1000066, '2024-09-26 15:01:26.503809', 1000066, 'Ly', 'Ly', NULL, '2646576b-126d-4798-be77-3d5ffe32c269', 'Y');
INSERT INTO pos.d_uom VALUES (1000019, 1000016, 1000004, '2024-09-27 15:51:59.594639', 1000066, '2024-09-27 15:51:59.59464', 1000066, 'g', 'g', NULL, 'cf2dc629-63e8-4d62-bfec-16c2ec5ec463', 'Y');







--

--
-- TOC entry 5358 (class 0 OID 385939)
-- Dependencies: 263
-- Data for Name: d_voucher; Type: TABLE DATA; Schema: pos; Owner: -
--



--
-- TOC entry 5359 (class 0 OID 385963)
-- Dependencies: 264
-- Data for Name: d_warehouse; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.d_warehouse VALUES (1000002, 1000005, 1000002, '2024-07-21 23:19:15.78506', 1000037, '2024-07-21 23:19:15.78506', 1000037, 'Y', 'WH1', 'Warehouse1', NULL, 'Thảo Nguyên Sài GÒn', 'Y', '8120b747-7248-4fc8-9d2d-0d8347746045', NULL, NULL);
INSERT INTO pos.d_warehouse VALUES (1000003, 1000001, 1000002, '2024-07-21 23:19:23.851718', 1000037, '2024-07-21 23:19:23.851718', 1000037, 'Y', 'WH1', 'Warehouse1', NULL, 'Thảo Nguyên Sài GÒn', 'Y', '78dfa021-3de5-40c2-b2db-306913e97411', NULL, NULL);
INSERT INTO pos.d_warehouse VALUES (1000004, 1000003, 1000003, '2024-07-23 23:44:44.411968', 1000068, '2024-07-23 23:45:44.657173', 1000068, 'Y', 'WH122', 'Warehouse1222', NULL, 'Thảo Nguyên Sài GÒn', 'Y', '0e34b401-9dbe-4cbc-9a15-d484c8c0c4ad', NULL, NULL);
INSERT INTO pos.d_warehouse VALUES (1000005, 1000003, 1000003, '2024-07-23 23:45:57.081644', 1000068, '2024-07-23 23:45:57.081644', 1000068, 'Y', 'WH2', 'Warehouse1', NULL, 'Thảo Nguyên Sài GÒn', 'Y', '46cd43f7-b820-4016-b387-f7a736d5c38a', NULL, NULL);
INSERT INTO pos.d_warehouse VALUES (1000006, 1000016, 1000004, '2024-08-12 10:10:21.820852', 1000069, '2024-08-12 10:10:21.820852', 1000069, 'Y', 'WH1', 'Warehouse1', NULL, 'Thảo Nguyên Sài GÒn', 'Y', 'e23afa1e-5f74-4d3a-b0ca-60d94711d527', NULL, NULL);
INSERT INTO pos.d_warehouse VALUES (1000008, 0, 1000004, '2024-08-30 11:53:39.013448', 1000069, '2024-08-30 11:53:39.013448', 1000069, 'Y', 'WH1000007', 'Warehouse1', NULL, 'Thảo Nguyên Sài GÒn', 'Y', '21dfb40f-a81f-41c3-97d0-77c644adf0f1', NULL, NULL);
INSERT INTO pos.d_warehouse VALUES (1000000, 1000001, 1000002, '2024-07-15 18:52:03.355836', 1000037, '2024-07-15 18:52:03.355836', 1000037, 'Y', 'WH1', 'Warehouse1', NULL, 'Thảo Nguyên Sài GÒn', 'Y', '0ccfa2b0-511b-4d72-9eb7-17200db17adc', NULL, NULL);
INSERT INTO pos.d_warehouse VALUES (1000026, 1000022, 1000009, '2024-09-11 17:05:42.749598', 1000071, '2024-09-11 17:05:42.749599', 1000071, 'Y', 'WSH0001', 'Kho hàng ký gửi', NULL, NULL, 'Y', '75110b2f-c182-45ec-9a20-39c01416dab1', NULL, NULL);
INSERT INTO pos.d_warehouse VALUES (1000027, 1000022, 1000009, '2024-09-12 11:11:09.04166', 1000071, '2024-09-12 11:11:09.041663', 1000071, 'Y', 'WSH0002', 'Kho bán lẻ', NULL, NULL, 'Y', '23a8c1c3-3b74-4831-8d1a-c5b79c0d1c49', NULL, NULL);
INSERT INTO pos.d_warehouse VALUES (1000033, 1000021, 1000004, '2024-09-18 11:25:08.714904', 1000069, '2024-09-18 11:25:08.714912', 1000069, 'Y', 'Kho1', 'KHO 2', 'hi', NULL, 'Y', '2ca1b730-39e7-46a2-975c-e44daa470113', NULL, NULL);
INSERT INTO pos.d_warehouse VALUES (1000041, 1000016, 1000004, '2024-09-27 09:04:53.221297', 1000066, '2024-09-27 11:41:41.789913', 1000066, 'Y', 'WH100003427', 'Kho nguyên vật liệu', NULL, NULL, 'Y', 'b239717b-3c46-42c0-957c-daa6be2d112c', NULL, NULL);


--
-- TOC entry 5399 (class 0 OID 388439)
-- Dependencies: 304
-- Data for Name: revinfo; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.revinfo VALUES (7, 1721093813588, NULL);
INSERT INTO pos.revinfo VALUES (10, 1721094814892, NULL);
INSERT INTO pos.revinfo VALUES (11, 1721096039558, NULL);
INSERT INTO pos.revinfo VALUES (12, 1721096045498, NULL);
INSERT INTO pos.revinfo VALUES (13, 1721097613694, NULL);
INSERT INTO pos.revinfo VALUES (14, 1721097677308, NULL);
INSERT INTO pos.revinfo VALUES (15, 1721097771071, NULL);
INSERT INTO pos.revinfo VALUES (8, 1721117803491, NULL);
INSERT INTO pos.revinfo VALUES (9, 1721117865935, NULL);
INSERT INTO pos.revinfo VALUES (16, 1721119201950, NULL);
INSERT INTO pos.revinfo VALUES (17, 1721119258164, NULL);


--
-- TOC entry 5478 (class 0 OID 394978)
-- Dependencies: 401
-- Data for Name: tenants; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.tenants VALUES (62, 'TIEN1', 'tien1', 'TIEN1', '123456', 'CREATED');
INSERT INTO pos.tenants VALUES (63, 'TIEN1', 'tien1', 'TIEN1', '123456', 'FAILED_TO_CREATE');
INSERT INTO pos.tenants VALUES (64, 'TIEN2', 'tien2', 'TIEN2', '123456', 'CREATED');
INSERT INTO pos.tenants VALUES (65, 'TIEN3', 'tien3', 'TIEN3', '123456', 'CREATED');
INSERT INTO pos.tenants VALUES (66, 'TIEN5', NULL, 'TIEN5', '123456', 'CREATED');


--
-- TOC entry 5477 (class 0 OID 394972)
-- Dependencies: 400
-- Data for Name: user_roles; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.user_roles VALUES (1, 'SYS_ADMIN');
INSERT INTO pos.user_roles VALUES (3, 'ADMIN');
INSERT INTO pos.user_roles VALUES (4, 'ADMIN');
INSERT INTO pos.user_roles VALUES (5, 'ADMIN');
INSERT INTO pos.user_roles VALUES (6, 'ADMIN');
INSERT INTO pos.user_roles VALUES (7, 'ADMIN');
INSERT INTO pos.user_roles VALUES (8, 'ADMIN');
INSERT INTO pos.user_roles VALUES (9, 'ADMIN');
INSERT INTO pos.user_roles VALUES (10, 'ADMIN');
INSERT INTO pos.user_roles VALUES (11, 'ADMIN');
INSERT INTO pos.user_roles VALUES (12, 'ADMIN');
INSERT INTO pos.user_roles VALUES (13, 'ADMIN');
INSERT INTO pos.user_roles VALUES (14, 'ADMIN');
INSERT INTO pos.user_roles VALUES (15, 'ADMIN');
INSERT INTO pos.user_roles VALUES (16, 'ADMIN');
INSERT INTO pos.user_roles VALUES (17, 'ADMIN');
INSERT INTO pos.user_roles VALUES (18, 'ADMIN');
INSERT INTO pos.user_roles VALUES (19, 'ADMIN');
INSERT INTO pos.user_roles VALUES (20, 'ADMIN');
INSERT INTO pos.user_roles VALUES (21, 'ADMIN');
INSERT INTO pos.user_roles VALUES (22, 'ADMIN');
INSERT INTO pos.user_roles VALUES (23, 'ADMIN');
INSERT INTO pos.user_roles VALUES (24, 'ADMIN');
INSERT INTO pos.user_roles VALUES (25, 'ADMIN');
INSERT INTO pos.user_roles VALUES (26, 'ADMIN');


--
-- TOC entry 5475 (class 0 OID 394963)
-- Dependencies: 398
-- Data for Name: users; Type: TABLE DATA; Schema: pos; Owner: -
--

INSERT INTO pos.users VALUES (25, 'tien1@gmai.com', '$2a$10$Du0Vtl6acrjzQ5y06wU4oekptxtqYVr28EyoJgq3Lk80Jrs78s6Ge', 62);
INSERT INTO pos.users VALUES (26, 'tien1@gmai.com', '$2a$10$oytzxSj575BKNjsp6OnJO.mgrCErAuc.3dGE2/H3gXVDKvCFzI47q', 64);


--
-- TOC entry 5488 (class 0 OID 0)
-- Dependencies: 396
-- Name: d_api_trace_log_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_api_trace_log_sq', 1000000, false);


--
-- TOC entry 5489 (class 0 OID 0)
-- Dependencies: 330
-- Name: d_assign_org_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_assign_org_sq', 1000188, true);


--
-- TOC entry 5490 (class 0 OID 0)
-- Dependencies: 332
-- Name: d_attribute_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_attribute_sq', 1000010, true);


--
-- TOC entry 5491 (class 0 OID 0)
-- Dependencies: 350
-- Name: d_attribute_value_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_attribute_value_sq', 1000001, true);


--
-- TOC entry 5492 (class 0 OID 0)
-- Dependencies: 352
-- Name: d_bank_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_bank_sq', 1000064, true);


--
-- TOC entry 5493 (class 0 OID 0)
-- Dependencies: 354
-- Name: d_bankaccount_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_bankaccount_sq', 1000006, true);


--
-- TOC entry 5494 (class 0 OID 0)
-- Dependencies: 326
-- Name: d_cancel_reason_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_cancel_reason_sq', 1000013, true);


--
-- TOC entry 5495 (class 0 OID 0)
-- Dependencies: 265
-- Name: d_changelog_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_changelog_sq', 1000195, true);


--
-- TOC entry 5496 (class 0 OID 0)
-- Dependencies: 266
-- Name: d_config_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_config_sq', 1000006, true);


--
-- TOC entry 5497 (class 0 OID 0)
-- Dependencies: 336
-- Name: d_coupon_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_coupon_sq', 1000000, false);


--
-- TOC entry 5498 (class 0 OID 0)
-- Dependencies: 267
-- Name: d_currency_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_currency_sq', 1000000, true);


--
-- TOC entry 5499 (class 0 OID 0)
-- Dependencies: 268
-- Name: d_customer_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_customer_sq', 1000715, true);


--
-- TOC entry 5500 (class 0 OID 0)
-- Dependencies: 269
-- Name: d_doctype_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_doctype_sq', 1000007, true);


--
-- TOC entry 5501 (class 0 OID 0)
-- Dependencies: 356
-- Name: d_erp_integration_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_erp_integration_sq', 1000010, true);


--
-- TOC entry 5502 (class 0 OID 0)
-- Dependencies: 271
-- Name: d_expense_category_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_expense_category_sq', 1000000, false);


--
-- TOC entry 5503 (class 0 OID 0)
-- Dependencies: 270
-- Name: d_expense_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_expense_sq', 1000000, false);


--
-- TOC entry 5504 (class 0 OID 0)
-- Dependencies: 272
-- Name: d_floor_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_floor_sq', 1000048, true);


--
-- TOC entry 5505 (class 0 OID 0)
-- Dependencies: 273
-- Name: d_image_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_image_sq', 1000443, true);


--
-- TOC entry 5506 (class 0 OID 0)
-- Dependencies: 274
-- Name: d_industry_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_industry_sq', 1000015, true);


--
-- TOC entry 5507 (class 0 OID 0)
-- Dependencies: 372
-- Name: d_integration_history_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_integration_history_sq', 1000068, true);


--
-- TOC entry 5508 (class 0 OID 0)
-- Dependencies: 275
-- Name: d_invoice_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_invoice_sq', 1000251, true);


--
-- TOC entry 5509 (class 0 OID 0)
-- Dependencies: 276
-- Name: d_invoiceline_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_invoiceline_sq', 1000347, true);


--
-- TOC entry 5510 (class 0 OID 0)
-- Dependencies: 318
-- Name: d_kitchen_order_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_kitchen_order_sq', 1000167, true);


--
-- TOC entry 5511 (class 0 OID 0)
-- Dependencies: 320
-- Name: d_kitchen_orderline_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_kitchen_orderline_sq', 1000231, true);


--
-- TOC entry 5512 (class 0 OID 0)
-- Dependencies: 277
-- Name: d_language_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_language_sq', 1000000, false);


--
-- TOC entry 5513 (class 0 OID 0)
-- Dependencies: 278
-- Name: d_locator_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_locator_sq', 1000020, true);


--
-- TOC entry 5514 (class 0 OID 0)
-- Dependencies: 322
-- Name: d_note_group_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_note_group_sq', 1000007, true);


--
-- TOC entry 5515 (class 0 OID 0)
-- Dependencies: 324
-- Name: d_note_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_note_sq', 1000008, true);


--
-- TOC entry 5516 (class 0 OID 0)
-- Dependencies: 279
-- Name: d_notification_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_notification_sq', 1000000, false);


--
-- TOC entry 5517 (class 0 OID 0)
-- Dependencies: 280
-- Name: d_order_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_order_sq', 1000078, true);


--
-- TOC entry 5518 (class 0 OID 0)
-- Dependencies: 281
-- Name: d_orderline_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_orderline_sq', 1000142, true);


--
-- TOC entry 5519 (class 0 OID 0)
-- Dependencies: 282
-- Name: d_org_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_org_sq', 1000038, true);


--
-- TOC entry 5520 (class 0 OID 0)
-- Dependencies: 314
-- Name: d_partner_group_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_partner_group_sq', 1000114, true);


--
-- TOC entry 5521 (class 0 OID 0)
-- Dependencies: 368
-- Name: d_pay_method_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_pay_method_sq', 1000018, true);


--
-- TOC entry 5522 (class 0 OID 0)
-- Dependencies: 283
-- Name: d_payment_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_payment_sq', 1000228, true);


--
-- TOC entry 5523 (class 0 OID 0)
-- Dependencies: 370
-- Name: d_paymethod_org_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_paymethod_org_sq', 1000015, true);


--
-- TOC entry 5524 (class 0 OID 0)
-- Dependencies: 348
-- Name: d_pc_terminalaccess_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_pc_terminalaccess_sq', 1000170, true);


--
-- TOC entry 5525 (class 0 OID 0)
-- Dependencies: 406
-- Name: d_pos_closedcash_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_pos_closedcash_sq', 1000000, false);


--
-- TOC entry 5526 (class 0 OID 0)
-- Dependencies: 284
-- Name: d_pos_order_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_pos_order_sq', 1000868, true);


--
-- TOC entry 5527 (class 0 OID 0)
-- Dependencies: 285
-- Name: d_pos_orderline_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_pos_orderline_sq', 1000878, true);


--
-- TOC entry 5528 (class 0 OID 0)
-- Dependencies: 344
-- Name: d_pos_payment_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_pos_payment_sq', 1000316, true);


--
-- TOC entry 5529 (class 0 OID 0)
-- Dependencies: 346
-- Name: d_pos_taxline_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_pos_taxline_sq', 1000115, true);


--
-- TOC entry 5530 (class 0 OID 0)
-- Dependencies: 334
-- Name: d_pos_terminal_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_pos_terminal_sq', 1000046, true);


--
-- TOC entry 5531 (class 0 OID 0)
-- Dependencies: 312
-- Name: d_pricelist_org_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_pricelist_org_sq', 1000070, true);


--
-- TOC entry 5532 (class 0 OID 0)
-- Dependencies: 310
-- Name: d_pricelist_product_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_pricelist_product_sq', 1000379, true);


--
-- TOC entry 5533 (class 0 OID 0)
-- Dependencies: 308
-- Name: d_pricelist_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_pricelist_sq', 1000059, true);


--
-- TOC entry 5534 (class 0 OID 0)
-- Dependencies: 316
-- Name: d_print_report_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_print_report_sq', 1000017, true);


--
-- TOC entry 5535 (class 0 OID 0)
-- Dependencies: 287
-- Name: d_product_category_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_product_category_sq', 1000424, true);


--
-- TOC entry 5536 (class 0 OID 0)
-- Dependencies: 288
-- Name: d_product_combo_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_product_combo_sq', 1000060, true);


--
-- TOC entry 5537 (class 0 OID 0)
-- Dependencies: 342
-- Name: d_product_location_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_product_location_sq', 1000000, false);


--
-- TOC entry 5538 (class 0 OID 0)
-- Dependencies: 286
-- Name: d_product_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_product_sq', 1000786, true);


--
-- TOC entry 5539 (class 0 OID 0)
-- Dependencies: 358
-- Name: d_production_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_production_sq', 1000016, true);


--
-- TOC entry 5540 (class 0 OID 0)
-- Dependencies: 360
-- Name: d_productionline_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_productionline_sq', 1000000, false);


--
-- TOC entry 5541 (class 0 OID 0)
-- Dependencies: 378
-- Name: d_purchase_order_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_purchase_order_sq', 1000047, true);


--
-- TOC entry 5542 (class 0 OID 0)
-- Dependencies: 380
-- Name: d_purchase_orderline_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_purchase_orderline_sq', 1000048, true);


--
-- TOC entry 5543 (class 0 OID 0)
-- Dependencies: 362
-- Name: d_reconciledetail_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_reconciledetail_sq', 1000036, true);


--
-- TOC entry 5544 (class 0 OID 0)
-- Dependencies: 290
-- Name: d_reference_list_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_reference_list_sq', 1000056, true);


--
-- TOC entry 5545 (class 0 OID 0)
-- Dependencies: 289
-- Name: d_reference_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_reference_sq', 1000014, true);


--
-- TOC entry 5546 (class 0 OID 0)
-- Dependencies: 386
-- Name: d_request_order_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_request_order_sq', 1000004, true);


--
-- TOC entry 5547 (class 0 OID 0)
-- Dependencies: 387
-- Name: d_request_orderline_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_request_orderline_sq', 1000004, true);


--
-- TOC entry 5548 (class 0 OID 0)
-- Dependencies: 291
-- Name: d_reservation_line_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_reservation_line_sq', 1000000, false);


--
-- TOC entry 5549 (class 0 OID 0)
-- Dependencies: 292
-- Name: d_reservation_order_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_reservation_order_sq', 1000197, true);


--
-- TOC entry 5550 (class 0 OID 0)
-- Dependencies: 293
-- Name: d_role_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_role_sq', 1000009, true);


--
-- TOC entry 5551 (class 0 OID 0)
-- Dependencies: 340
-- Name: d_shift_control_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_shift_control_sq', 1000036, true);


--
-- TOC entry 5552 (class 0 OID 0)
-- Dependencies: 294
-- Name: d_storage_onhand_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_storage_onhand_sq', 1000000, false);


--
-- TOC entry 5553 (class 0 OID 0)
-- Dependencies: 295
-- Name: d_table_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_table_sq', 1000165, true);


--
-- TOC entry 5554 (class 0 OID 0)
-- Dependencies: 338
-- Name: d_table_use_ref_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_table_use_ref_sq', 1000005, true);


--
-- TOC entry 5555 (class 0 OID 0)
-- Dependencies: 297
-- Name: d_tax_category_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_tax_category_sq', 1000008, true);


--
-- TOC entry 5556 (class 0 OID 0)
-- Dependencies: 296
-- Name: d_tax_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_tax_sq', 1000023, true);


--
-- TOC entry 5557 (class 0 OID 0)
-- Dependencies: 298
-- Name: d_tenant_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_tenant_sq', 1000053, true);


--
-- TOC entry 5558 (class 0 OID 0)
-- Dependencies: 329
-- Name: d_uom_product_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_uom_product_sq', 1000155, true);


--
-- TOC entry 5559 (class 0 OID 0)
-- Dependencies: 299
-- Name: d_uom_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_uom_sq', 1000019, true);


--
-- TOC entry 5560 (class 0 OID 0)
-- Dependencies: 300
-- Name: d_user_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_user_sq', 1000200, true);


--
-- TOC entry 5561 (class 0 OID 0)
-- Dependencies: 301
-- Name: d_vendor_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_vendor_sq', 1000201, true);


--
-- TOC entry 5562 (class 0 OID 0)
-- Dependencies: 302
-- Name: d_voucher_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_voucher_sq', 1000000, false);


--
-- TOC entry 5563 (class 0 OID 0)
-- Dependencies: 303
-- Name: d_warehouse_sq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.d_warehouse_sq', 1000041, true);


--
-- TOC entry 5564 (class 0 OID 0)
-- Dependencies: 307
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.hibernate_sequence', 17, true);


--
-- TOC entry 5565 (class 0 OID 0)
-- Dependencies: 402
-- Name: tenants_id_seq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.tenants_id_seq', 66, true);


--
-- TOC entry 5566 (class 0 OID 0)
-- Dependencies: 399
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: pos; Owner: -
--

SELECT pg_catalog.setval('pos.users_id_seq', 26, true);


--
-- TOC entry 4713 (class 2606 OID 385488)
-- Name: d_changelog D_ChangeLog_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_changelog
    ADD CONSTRAINT "D_ChangeLog_pkey" PRIMARY KEY (d_changelog_id);


--
-- TOC entry 4717 (class 2606 OID 385498)
-- Name: d_config D_Config_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_config
    ADD CONSTRAINT "D_Config_pkey" PRIMARY KEY (d_config_id);


--
-- TOC entry 4915 (class 2606 OID 392923)
-- Name: d_coupon D_Coupon_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_coupon
    ADD CONSTRAINT "D_Coupon_pkey" PRIMARY KEY (d_coupon_id);


--
-- TOC entry 4720 (class 2606 OID 385506)
-- Name: d_currency D_Currency_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_currency
    ADD CONSTRAINT "D_Currency_pkey" PRIMARY KEY (d_currency_id);


--
-- TOC entry 4723 (class 2606 OID 385520)
-- Name: d_customer D_Customer_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_customer
    ADD CONSTRAINT "D_Customer_pkey" PRIMARY KEY (d_customer_id);


--
-- TOC entry 4725 (class 2606 OID 385532)
-- Name: d_doctype D_DocType_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_doctype
    ADD CONSTRAINT "D_DocType_pkey" PRIMARY KEY (d_doctype_id);


--
-- TOC entry 4732 (class 2606 OID 385550)
-- Name: d_expense_category D_Expense_Category_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_expense_category
    ADD CONSTRAINT "D_Expense_Category_pkey" PRIMARY KEY (d_expense_category_id);


--
-- TOC entry 4730 (class 2606 OID 385542)
-- Name: d_expense D_Expense_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_expense
    ADD CONSTRAINT "D_Expense_pkey" PRIMARY KEY (d_expense_id);


--
-- TOC entry 4737 (class 2606 OID 385562)
-- Name: d_floor D_Floor_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_floor
    ADD CONSTRAINT "D_Floor_pkey" PRIMARY KEY (d_floor_id);


--
-- TOC entry 4745 (class 2606 OID 385573)
-- Name: d_image D_Image_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_image
    ADD CONSTRAINT "D_Image_pkey" PRIMARY KEY (d_image_id);


--
-- TOC entry 4749 (class 2606 OID 385584)
-- Name: d_industry D_Industry_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_industry
    ADD CONSTRAINT "D_Industry_pkey" PRIMARY KEY (d_industry_id);


--
-- TOC entry 4757 (class 2606 OID 385622)
-- Name: d_invoiceline D_InvoiceLine_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_invoiceline
    ADD CONSTRAINT "D_InvoiceLine_pkey" PRIMARY KEY (d_invoiceline_id);


--
-- TOC entry 4753 (class 2606 OID 385594)
-- Name: d_invoice D_Invoice_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_invoice
    ADD CONSTRAINT "D_Invoice_pkey" PRIMARY KEY (d_invoice_id);


--
-- TOC entry 4761 (class 2606 OID 385629)
-- Name: d_language D_Language_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_language
    ADD CONSTRAINT "D_Language_pkey" PRIMARY KEY (d_language_id);


--
-- TOC entry 4765 (class 2606 OID 385640)
-- Name: d_locator D_Locator_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_locator
    ADD CONSTRAINT "D_Locator_pkey" PRIMARY KEY (d_locator_id);


--
-- TOC entry 4770 (class 2606 OID 385649)
-- Name: d_notification D_Notification_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_notification
    ADD CONSTRAINT "D_Notification_pkey" PRIMARY KEY (d_notification_id);


--
-- TOC entry 4776 (class 2606 OID 385710)
-- Name: d_orderline D_OrderLine_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_orderline
    ADD CONSTRAINT "D_OrderLine_pkey" PRIMARY KEY (d_orderline_id);


--
-- TOC entry 4773 (class 2606 OID 385691)
-- Name: d_order D_Order_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_order
    ADD CONSTRAINT "D_Order_pkey" PRIMARY KEY (d_order_id);


--
-- TOC entry 4778 (class 2606 OID 385724)
-- Name: d_org D_Org_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_org
    ADD CONSTRAINT "D_Org_pkey" PRIMARY KEY (d_org_id);


--
-- TOC entry 4788 (class 2606 OID 385761)
-- Name: d_pos_orderline D_POS_OrderLine_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_orderline
    ADD CONSTRAINT "D_POS_OrderLine_pkey" PRIMARY KEY (d_pos_orderline_id);


--
-- TOC entry 4785 (class 2606 OID 385751)
-- Name: d_pos_order D_POS_Order_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_order
    ADD CONSTRAINT "D_POS_Order_pkey" PRIMARY KEY (d_pos_order_id);


--
-- TOC entry 4912 (class 2606 OID 392894)
-- Name: d_pos_terminal D_POS_Terminal_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_terminal
    ADD CONSTRAINT "D_POS_Terminal_pkey" PRIMARY KEY (d_pos_terminal_id);


--
-- TOC entry 4782 (class 2606 OID 385736)
-- Name: d_payment D_Payment_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_payment
    ADD CONSTRAINT "D_Payment_pkey" PRIMARY KEY (d_payment_id);


--
-- TOC entry 4797 (class 2606 OID 385784)
-- Name: d_product_category D_Product_Category_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product_category
    ADD CONSTRAINT "D_Product_Category_pkey" PRIMARY KEY (d_product_category_id);


--
-- TOC entry 4800 (class 2606 OID 385791)
-- Name: d_product_combo D_Product_Combo_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product_combo
    ADD CONSTRAINT "D_Product_Combo_pkey" PRIMARY KEY (d_product_combo_id);


--
-- TOC entry 4791 (class 2606 OID 385776)
-- Name: d_product D_Product_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product
    ADD CONSTRAINT "D_Product_pkey" PRIMARY KEY (d_product_id);


--
-- TOC entry 4975 (class 2606 OID 394620)
-- Name: d_request_orderline D_REQUESTLINE_ORDER_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_request_orderline
    ADD CONSTRAINT "D_REQUESTLINE_ORDER_pkey" PRIMARY KEY (d_request_orderline_id);


--
-- TOC entry 4973 (class 2606 OID 394584)
-- Name: d_request_order D_REQUEST_ORDER_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_request_order
    ADD CONSTRAINT "D_REQUEST_ORDER_pkey" PRIMARY KEY (d_request_order_id);


--
-- TOC entry 4954 (class 2606 OID 393500)
-- Name: d_reconciledetail D_ReconcileDetail_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reconciledetail
    ADD CONSTRAINT "D_ReconcileDetail_pkey" PRIMARY KEY (d_reconciledetail_id);


--
-- TOC entry 4810 (class 2606 OID 385821)
-- Name: d_reference_list D_Reference_List_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reference_list
    ADD CONSTRAINT "D_Reference_List_pkey" PRIMARY KEY (d_reference_list_id);


--
-- TOC entry 4805 (class 2606 OID 385798)
-- Name: d_reference D_Reference_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reference
    ADD CONSTRAINT "D_Reference_pkey" PRIMARY KEY (d_reference_id);


--
-- TOC entry 4815 (class 2606 OID 385829)
-- Name: d_reservation_line D_Reservation_Line_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reservation_line
    ADD CONSTRAINT "D_Reservation_Line_pkey" PRIMARY KEY (d_reservation_line_id);


--
-- TOC entry 4818 (class 2606 OID 385840)
-- Name: d_reservation_order D_Reservation_Order_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reservation_order
    ADD CONSTRAINT "D_Reservation_Order_pkey" PRIMARY KEY (d_reservation_order_id);


--
-- TOC entry 4821 (class 2606 OID 385848)
-- Name: d_role D_Role_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_role
    ADD CONSTRAINT "D_Role_pkey" PRIMARY KEY (d_role_id);


--
-- TOC entry 4922 (class 2606 OID 393031)
-- Name: d_shift_control D_Shift_Control_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_shift_control
    ADD CONSTRAINT "D_Shift_Control_pkey" PRIMARY KEY (d_shift_control_id);


--
-- TOC entry 4826 (class 2606 OID 385863)
-- Name: d_storage_onhand D_Storage_Onhand_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_storage_onhand
    ADD CONSTRAINT "D_Storage_Onhand_pkey" PRIMARY KEY (d_storage_onhand_id);


--
-- TOC entry 4829 (class 2606 OID 385871)
-- Name: d_table D_Table_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_table
    ADD CONSTRAINT "D_Table_pkey" PRIMARY KEY (d_table_id);


--
-- TOC entry 4839 (class 2606 OID 385890)
-- Name: d_tax_category D_Tax_Category_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_tax_category
    ADD CONSTRAINT "D_Tax_Category_pkey" PRIMARY KEY (d_tax_category_id);


--
-- TOC entry 4836 (class 2606 OID 385881)
-- Name: d_tax D_Tax_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_tax
    ADD CONSTRAINT "D_Tax_pkey" PRIMARY KEY (d_tax_id);


--
-- TOC entry 4842 (class 2606 OID 385901)
-- Name: d_tenant D_Tenant_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_tenant
    ADD CONSTRAINT "D_Tenant_pkey" PRIMARY KEY (d_tenant_id);


--
-- TOC entry 4902 (class 2606 OID 391474)
-- Name: d_uom_product D_UOM_Product_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_uom_product
    ADD CONSTRAINT "D_UOM_Product_pkey" PRIMARY KEY (d_uom_product_id);


--
-- TOC entry 4845 (class 2606 OID 385908)
-- Name: d_uom D_UOM_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_uom
    ADD CONSTRAINT "D_UOM_pkey" PRIMARY KEY (d_uom_id);


--
-- TOC entry 4850 (class 2606 OID 385917)
-- Name: d_user D_User_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_user
    ADD CONSTRAINT "D_User_pkey" PRIMARY KEY (d_user_id);


--
-- TOC entry 4859 (class 2606 OID 385936)
-- Name: d_vendor D_Vendor_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_vendor
    ADD CONSTRAINT "D_Vendor_pkey" PRIMARY KEY (d_vendor_id);


--
-- TOC entry 4869 (class 2606 OID 388498)
-- Name: d_vendor_audit D_Vendoradu_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_vendor_audit
    ADD CONSTRAINT "D_Vendoradu_pkey" PRIMARY KEY (id);


--
-- TOC entry 4862 (class 2606 OID 385947)
-- Name: d_voucher D_Voucher_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_voucher
    ADD CONSTRAINT "D_Voucher_pkey" PRIMARY KEY (d_voucher_id);


--
-- TOC entry 4865 (class 2606 OID 385972)
-- Name: d_warehouse D_Warehouse_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_warehouse
    ADD CONSTRAINT "D_Warehouse_pkey" PRIMARY KEY (d_warehouse_id);


--
-- TOC entry 4979 (class 2606 OID 394899)
-- Name: d_api_trace_log D_api_tracelog_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_api_trace_log
    ADD CONSTRAINT "D_api_tracelog_pkey" PRIMARY KEY (d_api_trace_log);


--
-- TOC entry 4938 (class 2606 OID 393247)
-- Name: d_bank D_bank_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_bank
    ADD CONSTRAINT "D_bank_pkey" PRIMARY KEY (d_bank_id);


--
-- TOC entry 4981 (class 2606 OID 409301)
-- Name: d_pos_closedcash D_closedcash_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_closedcash
    ADD CONSTRAINT "D_closedcash_pkey" PRIMARY KEY (d_pos_closedcash_id);


--
-- TOC entry 4908 (class 2606 OID 392851)
-- Name: d_attribute D_d_attribute_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_attribute
    ADD CONSTRAINT "D_d_attribute_pkey" PRIMARY KEY (d_attribute_id);


--
-- TOC entry 4936 (class 2606 OID 393191)
-- Name: d_attribute_value D_d_attribute_value_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_attribute_value
    ADD CONSTRAINT "D_d_attribute_value_pkey" PRIMARY KEY (d_attribute_value_id);


--
-- TOC entry 4933 (class 2606 OID 393138)
-- Name: d_pc_terminalaccess D_d_pc_terminalaccess_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pc_terminalaccess
    ADD CONSTRAINT "D_d_pc_terminalaccess_pkey" PRIMARY KEY (d_pc_terminalaccess_id);


--
-- TOC entry 4917 (class 2606 OID 392971)
-- Name: d_table_use_ref D_d_table_use_ref_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_table_use_ref
    ADD CONSTRAINT "D_d_table_use_ref_pkey" PRIMARY KEY (d_table_use_ref_id);


--
-- TOC entry 4924 (class 2606 OID 393056)
-- Name: d_product_location D_product_location_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product_location
    ADD CONSTRAINT "D_product_location_pkey" PRIMARY KEY (d_product_location_id);


--
-- TOC entry 4904 (class 2606 OID 392802)
-- Name: d_assign_org_product d_assign_org_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_assign_org_product
    ADD CONSTRAINT d_assign_org_pkey PRIMARY KEY (d_assign_org_id);


--
-- TOC entry 4906 (class 2606 OID 392804)
-- Name: d_assign_org_product d_assign_org_product_pk; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_assign_org_product
    ADD CONSTRAINT d_assign_org_product_pk UNIQUE (d_tenant_id, d_org_id, d_product_id);


--
-- TOC entry 4942 (class 2606 OID 393274)
-- Name: d_bankaccount d_bankaccountpkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_bankaccount
    ADD CONSTRAINT d_bankaccountpkey PRIMARY KEY (d_bankaccount_id);


--
-- TOC entry 4899 (class 2606 OID 390969)
-- Name: d_cancel_reason d_cancel_reason_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_cancel_reason
    ADD CONSTRAINT d_cancel_reason_pkey PRIMARY KEY (d_cancel_reason_id);


--
-- TOC entry 4945 (class 2606 OID 393369)
-- Name: d_erp_integration d_erp_integrationpkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_erp_integration
    ADD CONSTRAINT d_erp_integrationpkey PRIMARY KEY (d_erp_integration_id);


--
-- TOC entry 4739 (class 2606 OID 394948)
-- Name: d_floor d_floor_pk; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_floor
    ADD CONSTRAINT d_floor_pk UNIQUE (d_org_id, name);


--
-- TOC entry 4741 (class 2606 OID 426971)
-- Name: d_floor d_floor_pk_2; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_floor
    ADD CONSTRAINT d_floor_pk_2 UNIQUE (d_org_id, floor_no);


--
-- TOC entry 4965 (class 2606 OID 393805)
-- Name: d_integration_history d_integration_historypkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_integration_history
    ADD CONSTRAINT d_integration_historypkey PRIMARY KEY (d_integration_history_id);


--
-- TOC entry 4887 (class 2606 OID 390879)
-- Name: d_kitchen_order d_kitchen_order_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_kitchen_order
    ADD CONSTRAINT d_kitchen_order_pkey PRIMARY KEY (d_kitchen_order_id);


--
-- TOC entry 4890 (class 2606 OID 390905)
-- Name: d_kitchen_orderline d_kitchen_orderline_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_kitchen_orderline
    ADD CONSTRAINT d_kitchen_orderline_pkey PRIMARY KEY (d_kitchen_orderline_id);


--
-- TOC entry 4893 (class 2606 OID 390928)
-- Name: d_note_group d_note_group_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_note_group
    ADD CONSTRAINT d_note_group_pkey PRIMARY KEY (d_note_group_id);


--
-- TOC entry 4896 (class 2606 OID 390946)
-- Name: d_note d_note_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_note
    ADD CONSTRAINT d_note_pkey PRIMARY KEY (d_note_id);


--
-- TOC entry 4882 (class 2606 OID 389560)
-- Name: d_partner_group d_partner_group_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_partner_group
    ADD CONSTRAINT d_partner_group_pkey PRIMARY KEY (d_partner_group_id);


--
-- TOC entry 4957 (class 2606 OID 538396)
-- Name: d_pay_method d_pay_method_name_uk; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pay_method
    ADD CONSTRAINT d_pay_method_name_uk UNIQUE (name);


--
-- TOC entry 4962 (class 2606 OID 393776)
-- Name: d_paymethod_org d_paymethod_orgpkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_paymethod_org
    ADD CONSTRAINT d_paymethod_orgpkey PRIMARY KEY (d_paymethod_org_id);


--
-- TOC entry 4959 (class 2606 OID 393722)
-- Name: d_pay_method d_paymethodpkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pay_method
    ADD CONSTRAINT d_paymethodpkey PRIMARY KEY (d_pay_method_id);


--
-- TOC entry 4977 (class 2606 OID 415884)
-- Name: d_pos_org_access d_pos_org_access_pk; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_org_access
    ADD CONSTRAINT d_pos_org_access_pk PRIMARY KEY (d_pos_terminal_id, d_user_id, d_org_id);


--
-- TOC entry 4928 (class 2606 OID 393084)
-- Name: d_pos_payment d_pos_payment_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_payment
    ADD CONSTRAINT d_pos_payment_pkey PRIMARY KEY (d_pos_payment_id);


--
-- TOC entry 4931 (class 2606 OID 393112)
-- Name: d_pos_taxline d_pos_taxline_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_taxline
    ADD CONSTRAINT d_pos_taxline_pkey PRIMARY KEY (d_pos_taxline_id);


--
-- TOC entry 4880 (class 2606 OID 388802)
-- Name: d_pricelist_org d_pricelist_org_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pricelist_org
    ADD CONSTRAINT d_pricelist_org_pkey PRIMARY KEY (d_pricelist_org_id);


--
-- TOC entry 4872 (class 2606 OID 388762)
-- Name: d_pricelist d_pricelist_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pricelist
    ADD CONSTRAINT d_pricelist_pkey PRIMARY KEY (d_pricelist_id);


--
-- TOC entry 4875 (class 2606 OID 392755)
-- Name: d_pricelist_product d_pricelist_product_pk; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pricelist_product
    ADD CONSTRAINT d_pricelist_product_pk UNIQUE (d_pricelist_id, d_product_id);


--
-- TOC entry 4877 (class 2606 OID 388783)
-- Name: d_pricelist_product d_pricelist_product_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pricelist_product
    ADD CONSTRAINT d_pricelist_product_pkey PRIMARY KEY (d_pricelist_product_id);


--
-- TOC entry 4885 (class 2606 OID 390236)
-- Name: d_print_report d_print_report_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_print_report
    ADD CONSTRAINT d_print_report_pkey PRIMARY KEY (d_print_report_id);


--
-- TOC entry 4802 (class 2606 OID 390043)
-- Name: d_product_combo d_product_combo_d_org_id_d_tenant_id_d_product_id_d_product_key; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product_combo
    ADD CONSTRAINT d_product_combo_d_org_id_d_tenant_id_d_product_id_d_product_key UNIQUE (d_org_id, d_tenant_id, d_product_id, d_product_component_id);


--
-- TOC entry 4793 (class 2606 OID 390091)
-- Name: d_product d_product_d_tenant_id_d_org_id_code_key; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product
    ADD CONSTRAINT d_product_d_tenant_id_d_org_id_code_key UNIQUE (d_tenant_id, d_org_id, code);


--
-- TOC entry 4948 (class 2606 OID 393417)
-- Name: d_production d_production_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_production
    ADD CONSTRAINT d_production_pkey PRIMARY KEY (d_production_id);


--
-- TOC entry 4951 (class 2606 OID 393444)
-- Name: d_productionline d_productionline_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_productionline
    ADD CONSTRAINT d_productionline_pkey PRIMARY KEY (d_productionline_id);


--
-- TOC entry 4971 (class 2606 OID 394324)
-- Name: d_purchase_orderline d_purchase_orderlinepkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_purchase_orderline
    ADD CONSTRAINT d_purchase_orderlinepkey PRIMARY KEY (d_purchase_orderline_id);


--
-- TOC entry 4968 (class 2606 OID 394298)
-- Name: d_purchase_order d_purchase_orderpkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_purchase_order
    ADD CONSTRAINT d_purchase_orderpkey PRIMARY KEY (d_purchase_order_id);


--
-- TOC entry 4812 (class 2606 OID 392690)
-- Name: d_reference_list d_reference_list_pk; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reference_list
    ADD CONSTRAINT d_reference_list_pk UNIQUE (d_reference_id, value);


--
-- TOC entry 4807 (class 2606 OID 392620)
-- Name: d_reference d_reference_pk; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reference
    ADD CONSTRAINT d_reference_pk UNIQUE (name, d_tenant_id);


--
-- TOC entry 4823 (class 2606 OID 536170)
-- Name: d_role d_role_pk; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_role
    ADD CONSTRAINT d_role_pk UNIQUE (code);


--
-- TOC entry 4831 (class 2606 OID 392793)
-- Name: d_table d_table_pk; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_table
    ADD CONSTRAINT d_table_pk UNIQUE (d_floor_id, name, table_no);


--
-- TOC entry 4833 (class 2606 OID 394828)
-- Name: d_table d_table_uniqu; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_table
    ADD CONSTRAINT d_table_uniqu UNIQUE (d_org_id, d_table_id, name, d_floor_id, table_no);


--
-- TOC entry 4919 (class 2606 OID 392989)
-- Name: d_table_use_ref d_table_use_ref_pk; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_table_use_ref
    ADD CONSTRAINT d_table_use_ref_pk UNIQUE (d_tenant_id, d_reference_id, domain_name, domain_column);


--
-- TOC entry 4847 (class 2606 OID 393162)
-- Name: d_uom d_uom_pk; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_uom
    ADD CONSTRAINT d_uom_pk UNIQUE (d_tenant_id, code, name);


--
-- TOC entry 4852 (class 2606 OID 388136)
-- Name: d_user d_user_pk; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_user
    ADD CONSTRAINT d_user_pk UNIQUE (d_tenant_id, user_name);


--
-- TOC entry 4856 (class 2606 OID 415880)
-- Name: d_user_role_access d_user_role_access_pk; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_user_role_access
    ADD CONSTRAINT d_user_role_access_pk PRIMARY KEY (d_user_id, d_role_id);


--
-- TOC entry 4854 (class 2606 OID 415882)
-- Name: d_userorg_access d_userorg_access_pk; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_userorg_access
    ADD CONSTRAINT d_userorg_access_pk PRIMARY KEY (d_user_id, d_org_id);


--
-- TOC entry 4867 (class 2606 OID 388443)
-- Name: revinfo revinfo_pkey; Type: CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.revinfo
    ADD CONSTRAINT revinfo_pkey PRIMARY KEY (rev);


--
-- TOC entry 4711 (class 1259 OID 385490)
-- Name: D_ChangeLog_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_ChangeLog_UU_Idx" ON pos.d_changelog USING btree (d_changelog_uu);


--
-- TOC entry 4714 (class 1259 OID 393899)
-- Name: D_Config_Name_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE INDEX "D_Config_Name_Idx" ON pos.d_config USING btree (name);


--
-- TOC entry 4715 (class 1259 OID 385500)
-- Name: D_Config_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE INDEX "D_Config_UU_Idx" ON pos.d_config USING btree (d_config_uu);


--
-- TOC entry 4913 (class 1259 OID 392934)
-- Name: D_Coupon_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Coupon_UU_Idx" ON pos.d_coupon USING btree (d_coupon_uu);


--
-- TOC entry 4718 (class 1259 OID 385508)
-- Name: D_Currency_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Currency_UU_Idx" ON pos.d_currency USING btree (d_currency_uu);


--
-- TOC entry 4721 (class 1259 OID 385521)
-- Name: D_Customer_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Customer_UU_Idx" ON pos.d_customer USING btree (d_customer_uu);


--
-- TOC entry 4726 (class 1259 OID 385534)
-- Name: D_Doctype_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Doctype_UU_Idx" ON pos.d_doctype USING btree (d_doctype_uu);


--
-- TOC entry 4727 (class 1259 OID 385545)
-- Name: D_Expense_ExpenseCategory; Type: INDEX; Schema: pos; Owner: -
--

CREATE INDEX "D_Expense_ExpenseCategory" ON pos.d_expense USING btree (d_expense_category_id);


--
-- TOC entry 4728 (class 1259 OID 385544)
-- Name: D_Expense_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Expense_UU_Idx" ON pos.d_expense USING btree (d_expense_uu);


--
-- TOC entry 4734 (class 1259 OID 385563)
-- Name: D_Floor_ID_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Floor_ID_Idx" ON pos.d_floor USING btree (d_floor_id);


--
-- TOC entry 4735 (class 1259 OID 385564)
-- Name: D_Floor_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Floor_UU_Idx" ON pos.d_floor USING btree (d_floor_uu);


--
-- TOC entry 4742 (class 1259 OID 385574)
-- Name: D_Image_ID_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Image_ID_Idx" ON pos.d_image USING btree (d_image_id);


--
-- TOC entry 4743 (class 1259 OID 385575)
-- Name: D_Image_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Image_UU_Idx" ON pos.d_image USING btree (d_image_uu);


--
-- TOC entry 4746 (class 1259 OID 385585)
-- Name: D_Industry_ID_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Industry_ID_Idx" ON pos.d_industry USING btree (d_industry_id);


--
-- TOC entry 4747 (class 1259 OID 385586)
-- Name: D_Industry_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Industry_UU_Idx" ON pos.d_industry USING btree (d_industry_uu);


--
-- TOC entry 4754 (class 1259 OID 385623)
-- Name: D_InvoiceLine_ID_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_InvoiceLine_ID_Idx" ON pos.d_invoiceline USING btree (d_invoiceline_id);


--
-- TOC entry 4755 (class 1259 OID 385624)
-- Name: D_InvoiceLine_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_InvoiceLine_UU_Idx" ON pos.d_invoiceline USING btree (d_invoiceline_uu);


--
-- TOC entry 4750 (class 1259 OID 385595)
-- Name: D_Invoice_ID_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Invoice_ID_Idx" ON pos.d_invoice USING btree (d_invoice_id);


--
-- TOC entry 4751 (class 1259 OID 385596)
-- Name: D_Invoice_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Invoice_UU_Idx" ON pos.d_invoice USING btree (d_invoice_uu);


--
-- TOC entry 4758 (class 1259 OID 385630)
-- Name: D_Language_ID_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Language_ID_Idx" ON pos.d_language USING btree (d_language_id);


--
-- TOC entry 4759 (class 1259 OID 385631)
-- Name: D_Language_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Language_UU_Idx" ON pos.d_language USING btree (d_language_uu);


--
-- TOC entry 4762 (class 1259 OID 385641)
-- Name: D_Locator_ID_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Locator_ID_Idx" ON pos.d_locator USING btree (d_locator_id);


--
-- TOC entry 4763 (class 1259 OID 385642)
-- Name: D_Locator_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Locator_UU_Idx" ON pos.d_locator USING btree (d_locator_uu);


--
-- TOC entry 4767 (class 1259 OID 385650)
-- Name: D_Notification_ID_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Notification_ID_Idx" ON pos.d_notification USING btree (d_notification_id);


--
-- TOC entry 4768 (class 1259 OID 385651)
-- Name: D_Notification_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Notification_UU_Idx" ON pos.d_notification USING btree (d_notification_uu);


--
-- TOC entry 4774 (class 1259 OID 385712)
-- Name: D_OrderLine_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_OrderLine_UU_Idx" ON pos.d_orderline USING btree (d_orderline_uu);


--
-- TOC entry 4771 (class 1259 OID 385693)
-- Name: D_Order_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Order_UU_Idx" ON pos.d_order USING btree (d_order_uu);


--
-- TOC entry 4786 (class 1259 OID 385763)
-- Name: D_POS_OrderLine_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_POS_OrderLine_UU_Idx" ON pos.d_pos_orderline USING btree (d_pos_orderline_uu);


--
-- TOC entry 4783 (class 1259 OID 385753)
-- Name: D_POS_Order_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_POS_Order_UU_Idx" ON pos.d_pos_order USING btree (d_pos_order_uu);


--
-- TOC entry 4910 (class 1259 OID 392905)
-- Name: D_POS_Terminal_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_POS_Terminal_UU_Idx" ON pos.d_pos_terminal USING btree (d_pos_terminal_uu);


--
-- TOC entry 4780 (class 1259 OID 385738)
-- Name: D_Payment_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Payment_UU_Idx" ON pos.d_payment USING btree (d_payment_uu);


--
-- TOC entry 4878 (class 1259 OID 388808)
-- Name: D_PriceList_Org_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_PriceList_Org_UU_Idx" ON pos.d_pricelist_org USING btree (d_pricelist_org_uu);


--
-- TOC entry 4873 (class 1259 OID 388789)
-- Name: D_PriceList_Product_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_PriceList_Product_UU_Idx" ON pos.d_pricelist_product USING btree (d_pricelist_product_uu);


--
-- TOC entry 4870 (class 1259 OID 388768)
-- Name: D_PriceList_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_PriceList_UU_Idx" ON pos.d_pricelist USING btree (d_pricelist_uu);


--
-- TOC entry 4883 (class 1259 OID 390242)
-- Name: D_Print_Report_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Print_Report_UU_Idx" ON pos.d_print_report USING btree (d_print_report_uu);


--
-- TOC entry 4795 (class 1259 OID 385786)
-- Name: D_Product_Category_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Product_Category_UU_Idx" ON pos.d_product_category USING btree (d_product_category_uu);


--
-- TOC entry 4798 (class 1259 OID 385793)
-- Name: D_Product_Combo_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Product_Combo_UU_Idx" ON pos.d_product_combo USING btree (d_product_combo_uu);


--
-- TOC entry 4789 (class 1259 OID 385778)
-- Name: D_Product_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Product_UU_Idx" ON pos.d_product USING btree (d_product_uu);


--
-- TOC entry 4952 (class 1259 OID 393501)
-- Name: D_ReconcileDetail_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_ReconcileDetail_UU_Idx" ON pos.d_reconciledetail USING btree (d_reconciledetail_uu);


--
-- TOC entry 4808 (class 1259 OID 385823)
-- Name: D_Reference_List_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Reference_List_UU_Idx" ON pos.d_reference_list USING btree (d_reference_list_uu);


--
-- TOC entry 4803 (class 1259 OID 385800)
-- Name: D_Reference_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Reference_UU_Idx" ON pos.d_reference USING btree (d_reference_uu);


--
-- TOC entry 4813 (class 1259 OID 385831)
-- Name: D_Reservation_Line_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Reservation_Line_UU_Idx" ON pos.d_reservation_line USING btree (d_reservation_line_uu);


--
-- TOC entry 4816 (class 1259 OID 385842)
-- Name: D_Reservation_Order_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Reservation_Order_UU_Idx" ON pos.d_reservation_order USING btree (d_reservation_order_uu);


--
-- TOC entry 4819 (class 1259 OID 385850)
-- Name: D_Role_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Role_UU_Idx" ON pos.d_role USING btree (d_role_uu);


--
-- TOC entry 4920 (class 1259 OID 393042)
-- Name: D_Shift_Control_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Shift_Control_UU_Idx" ON pos.d_shift_control USING btree (d_shift_control_uu);


--
-- TOC entry 4824 (class 1259 OID 385865)
-- Name: D_Storage_Onhand_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Storage_Onhand_UU_Idx" ON pos.d_storage_onhand USING btree (d_storage_onhand_uu);


--
-- TOC entry 4827 (class 1259 OID 385873)
-- Name: D_Table_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Table_UU_Idx" ON pos.d_table USING btree (d_table_uu);


--
-- TOC entry 4837 (class 1259 OID 385892)
-- Name: D_Tax_Category_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Tax_Category_UU_Idx" ON pos.d_tax_category USING btree (d_tax_category_uu);


--
-- TOC entry 4834 (class 1259 OID 385883)
-- Name: D_Tax_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Tax_UU_Idx" ON pos.d_tax USING btree (d_tax_uu);


--
-- TOC entry 4840 (class 1259 OID 385903)
-- Name: D_Tenant_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Tenant_UU_Idx" ON pos.d_tenant USING btree (d_tenant_uu);


--
-- TOC entry 4843 (class 1259 OID 385910)
-- Name: D_UOM_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_UOM_UU_Idx" ON pos.d_uom USING btree (d_uom_uu);


--
-- TOC entry 4848 (class 1259 OID 385919)
-- Name: D_User_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_User_UU_Idx" ON pos.d_user USING btree (d_user_uu);


--
-- TOC entry 4857 (class 1259 OID 385938)
-- Name: D_Vendor_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Vendor_UU_Idx" ON pos.d_vendor USING btree (d_vendor_uu);


--
-- TOC entry 4860 (class 1259 OID 385949)
-- Name: D_Voucher_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Voucher_UU_Idx" ON pos.d_voucher USING btree (d_voucher_uu);


--
-- TOC entry 4766 (class 1259 OID 385643)
-- Name: D_Warehouse_ID_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE INDEX "D_Warehouse_ID_Idx" ON pos.d_locator USING btree (d_warehouse_id);


--
-- TOC entry 4863 (class 1259 OID 385973)
-- Name: D_Warehouse_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "D_Warehouse_UU_Idx" ON pos.d_warehouse USING btree (d_warehouse_uu);


--
-- TOC entry 4909 (class 1259 OID 392857)
-- Name: d_attribute_d_attribute_uu_index; Type: INDEX; Schema: pos; Owner: -
--

CREATE INDEX d_attribute_d_attribute_uu_index ON pos.d_attribute USING btree (d_attribute_uu);


--
-- TOC entry 4939 (class 1259 OID 393258)
-- Name: d_bank_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_bank_UU_Idx" ON pos.d_bank USING btree (d_bank_uu);


--
-- TOC entry 4940 (class 1259 OID 393290)
-- Name: d_bankaccount_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_bankaccount_UU_Idx" ON pos.d_bankaccount USING btree (d_bankaccount_uu);


--
-- TOC entry 4900 (class 1259 OID 390975)
-- Name: d_cancel_reason_uu_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_cancel_reason_uu_Idx" ON pos.d_cancel_reason USING btree (d_cancel_reason_uu);


--
-- TOC entry 4943 (class 1259 OID 393380)
-- Name: d_erp_integration_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_erp_integration_UU_Idx" ON pos.d_erp_integration USING btree (d_erp_integration_uu);


--
-- TOC entry 4733 (class 1259 OID 387600)
-- Name: d_expense_category_uu_idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE INDEX d_expense_category_uu_idx ON pos.d_expense_category USING btree (d_expense_category_uu);


--
-- TOC entry 4963 (class 1259 OID 393816)
-- Name: d_integration_historyn_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_integration_historyn_UU_Idx" ON pos.d_integration_history USING btree (d_integration_history_uu);


--
-- TOC entry 4888 (class 1259 OID 390885)
-- Name: d_kitchen_order_uu_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_kitchen_order_uu_Idx" ON pos.d_kitchen_order USING btree (d_kitchen_order_uu);


--
-- TOC entry 4891 (class 1259 OID 390916)
-- Name: d_kitchen_orderline_uu_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_kitchen_orderline_uu_Idx" ON pos.d_kitchen_orderline USING btree (d_kitchen_orderline_uu);


--
-- TOC entry 4894 (class 1259 OID 390934)
-- Name: d_note_group_uu_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_note_group_uu_Idx" ON pos.d_note_group USING btree (d_note_group_uu);


--
-- TOC entry 4897 (class 1259 OID 390957)
-- Name: d_note_uu_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_note_uu_Idx" ON pos.d_note USING btree (d_note_uu);


--
-- TOC entry 4779 (class 1259 OID 387653)
-- Name: d_org_d_org_uu_idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE INDEX d_org_d_org_uu_idx ON pos.d_org USING btree (d_org_uu);


--
-- TOC entry 4955 (class 1259 OID 393738)
-- Name: d_pay_method_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_pay_method_UU_Idx" ON pos.d_pay_method USING btree (d_pay_method_uu);


--
-- TOC entry 4960 (class 1259 OID 393792)
-- Name: d_paymethod_org_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_paymethod_org_UU_Idx" ON pos.d_paymethod_org USING btree (d_paymethod_org_uu);


--
-- TOC entry 4934 (class 1259 OID 393149)
-- Name: d_pc_terminalaccess_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_pc_terminalaccess_UU_Idx" ON pos.d_pc_terminalaccess USING btree (d_pc_terminalaccess_uu);


--
-- TOC entry 4926 (class 1259 OID 393095)
-- Name: d_pos_payment_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_pos_payment_UU_Idx" ON pos.d_pos_payment USING btree (d_pos_payment_uu);


--
-- TOC entry 4929 (class 1259 OID 393123)
-- Name: d_pos_taxline_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_pos_taxline_UU_Idx" ON pos.d_pos_taxline USING btree (d_pos_taxline_uu);


--
-- TOC entry 4925 (class 1259 OID 393067)
-- Name: d_product_location_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_product_location_UU_Idx" ON pos.d_product_location USING btree (d_product_location_uu);


--
-- TOC entry 4794 (class 1259 OID 387579)
-- Name: d_product_productcategory; Type: INDEX; Schema: pos; Owner: -
--

CREATE INDEX d_product_productcategory ON pos.d_product USING btree (d_product_category_id);


--
-- TOC entry 4946 (class 1259 OID 393428)
-- Name: d_production_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_production_UU_Idx" ON pos.d_production USING btree (d_production_uu);


--
-- TOC entry 4949 (class 1259 OID 393455)
-- Name: d_productionline_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_productionline_UU_Idx" ON pos.d_productionline USING btree (d_productionline_uu);


--
-- TOC entry 4966 (class 1259 OID 394309)
-- Name: d_purchase_order_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_purchase_order_UU_Idx" ON pos.d_purchase_order USING btree (d_purchase_order_uu);


--
-- TOC entry 4969 (class 1259 OID 394340)
-- Name: d_purchase_orderline_UU_Idx; Type: INDEX; Schema: pos; Owner: -
--

CREATE UNIQUE INDEX "d_purchase_orderline_UU_Idx" ON pos.d_purchase_orderline USING btree (d_purchase_orderline_uu);


--
-- TOC entry 5314 (class 2618 OID 473366)
-- Name: d_sc_posorder_get_v _RETURN; Type: RULE; Schema: pos; Owner: -
--

CREATE OR REPLACE VIEW pos.d_sc_posorder_get_v AS
 SELECT DISTINCT sh.d_shift_control_id,
    sh.d_tenant_id,
    sh.d_org_id,
    sh.created,
    sh.created_by,
    sh.is_active,
    sh.updated,
    sh.updated_by,
    COALESCE(( SELECT sum(d_pos_payment.total_amount) AS sum
           FROM pos.d_pos_payment
          WHERE ((dp.d_pos_order_id = dp.d_pos_order_id) AND ((d_pos_payment.payment_method)::text = 'CAS'::text))), (0)::numeric) AS cash,
    COALESCE(( SELECT count(*) AS count
           FROM pos.d_pos_payment
          WHERE ((dp.d_pos_order_id = dp.d_pos_order_id) AND ((d_pos_payment.payment_method)::text = 'CAS'::text))), (0)::bigint) AS qty_cash,
    COALESCE(( SELECT sum(d_pos_payment.total_amount) AS sum
           FROM pos.d_pos_payment
          WHERE ((dp.d_pos_order_id = dp.d_pos_order_id) AND ((d_pos_payment.payment_method)::text = 'VIS'::text))), (0)::numeric) AS visa,
    COALESCE(( SELECT count(*) AS count
           FROM pos.d_pos_payment
          WHERE ((dp.d_pos_order_id = dp.d_pos_order_id) AND ((d_pos_payment.payment_method)::text = 'VIS'::text))), (0)::bigint) AS qty_visa,
    COALESCE(( SELECT sum(d_pos_payment.total_amount) AS sum
           FROM pos.d_pos_payment
          WHERE ((dp.d_pos_order_id = dp.d_pos_order_id) AND ((d_pos_payment.payment_method)::text = 'DEB'::text))), (0)::numeric) AS deb,
    COALESCE(( SELECT count(*) AS count
           FROM pos.d_pos_payment
          WHERE ((dp.d_pos_order_id = dp.d_pos_order_id) AND ((d_pos_payment.payment_method)::text = 'DEB'::text))), (0)::bigint) AS qty_deb,
    COALESCE(( SELECT sum(d_pos_payment.total_amount) AS sum
           FROM pos.d_pos_payment
          WHERE ((dp.d_pos_order_id = dp.d_pos_order_id) AND ((d_pos_payment.payment_method)::text = 'LYT'::text))), (0)::numeric) AS loyalty,
    COALESCE(( SELECT count(*) AS count
           FROM pos.d_pos_payment
          WHERE ((dp.d_pos_order_id = dp.d_pos_order_id) AND ((d_pos_payment.payment_method)::text = 'LYT'::text))), (0)::bigint) AS qty_loyalty,
    COALESCE(( SELECT sum(d_pos_payment.total_amount) AS sum
           FROM pos.d_pos_payment
          WHERE ((dp.d_pos_order_id = dp.d_pos_order_id) AND ((d_pos_payment.payment_method)::text = 'BAN'::text))), (0)::numeric) AS bank,
    COALESCE(( SELECT count(*) AS count
           FROM pos.d_pos_payment
          WHERE ((dp.d_pos_order_id = dp.d_pos_order_id) AND ((d_pos_payment.payment_method)::text = 'BAN'::text))), (0)::bigint) AS qty_bank
   FROM (pos.d_shift_control sh
     LEFT JOIN pos.d_pos_order dp ON ((sh.d_shift_control_id = dp.d_shift_control_id)))
  WHERE ((dp.order_status)::text = 'COM'::text)
  GROUP BY dp.d_pos_order_id, dp.document_no, sh.d_shift_control_id, dp.d_tenant_id, dp.d_org_id, dp.created, dp.created_by, dp.is_active, dp.updated, dp.updated_by;


--
-- TOC entry 5121 (class 2606 OID 393285)
-- Name: d_bankaccount FK_Bank_DBankAccount; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_bankaccount
    ADD CONSTRAINT "FK_Bank_DBankAccount" FOREIGN KEY (d_bank_id) REFERENCES pos.d_bank(d_bank_id);


--
-- TOC entry 5132 (class 2606 OID 393733)
-- Name: d_pay_method FK_Bank_DPayMethod; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pay_method
    ADD CONSTRAINT "FK_Bank_DPayMethod" FOREIGN KEY (d_bank_id) REFERENCES pos.d_bank(d_bank_id);


--
-- TOC entry 4997 (class 2606 OID 386359)
-- Name: d_invoice FK_Customer_DInvoice; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_invoice
    ADD CONSTRAINT "FK_Customer_DInvoice" FOREIGN KEY (d_customer_id) REFERENCES pos.d_customer(d_customer_id);


--
-- TOC entry 5011 (class 2606 OID 386429)
-- Name: d_org FK_D_Tenant_DOrg; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_org
    ADD CONSTRAINT "FK_D_Tenant_DOrg" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 4991 (class 2606 OID 386234)
-- Name: d_expense FK_ExpenseCategory_DExpense; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_expense
    ADD CONSTRAINT "FK_ExpenseCategory_DExpense" FOREIGN KEY (d_expense_category_id) REFERENCES pos.d_expense_category(d_expense_category_id);


--
-- TOC entry 5035 (class 2606 OID 386539)
-- Name: d_reservation_order FK_Floor_DReservationOrder; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reservation_order
    ADD CONSTRAINT "FK_Floor_DReservationOrder" FOREIGN KEY (d_floor_id) REFERENCES pos.d_floor(d_floor_id);


--
-- TOC entry 5046 (class 2606 OID 386664)
-- Name: d_table FK_Floor_DTable; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_table
    ADD CONSTRAINT "FK_Floor_DTable" FOREIGN KEY (d_floor_id) REFERENCES pos.d_floor(d_floor_id);


--
-- TOC entry 5054 (class 2606 OID 386754)
-- Name: d_tenant FK_Industry_DTenant; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_tenant
    ADD CONSTRAINT "FK_Industry_DTenant" FOREIGN KEY (d_industry_id) REFERENCES pos.d_industry(d_industry_id);


--
-- TOC entry 5000 (class 2606 OID 386384)
-- Name: d_invoiceline FK_Invoice_DInvoiceLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_invoiceline
    ADD CONSTRAINT "FK_Invoice_DInvoiceLine" FOREIGN KEY (d_invoice_id) REFERENCES pos.d_invoice(d_invoice_id);


--
-- TOC entry 5083 (class 2606 OID 390911)
-- Name: d_kitchen_orderline FK_KitchenOrder_DKitchenOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_kitchen_orderline
    ADD CONSTRAINT "FK_KitchenOrder_DKitchenOrderLine" FOREIGN KEY (d_kitchen_order_id) REFERENCES pos.d_kitchen_order(d_kitchen_order_id);


--
-- TOC entry 5042 (class 2606 OID 386649)
-- Name: d_storage_onhand FK_Locator_DStorageOnhand; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_storage_onhand
    ADD CONSTRAINT "FK_Locator_DStorageOnhand" FOREIGN KEY (d_locator_id) REFERENCES pos.d_locator(d_locator_id);


--
-- TOC entry 5009 (class 2606 OID 386424)
-- Name: d_orderline FK_Order_DOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_orderline
    ADD CONSTRAINT "FK_Order_DOrderLine" FOREIGN KEY (d_order_id) REFERENCES pos.d_order(d_order_id);


--
-- TOC entry 5119 (class 2606 OID 393248)
-- Name: d_bank FK_Org_DBank; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_bank
    ADD CONSTRAINT "FK_Org_DBank" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5122 (class 2606 OID 393275)
-- Name: d_bankaccount FK_Org_DBankAccount; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_bankaccount
    ADD CONSTRAINT "FK_Org_DBankAccount" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5158 (class 2606 OID 394905)
-- Name: d_api_trace_log FK_Org_DBankAccount; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_api_trace_log
    ADD CONSTRAINT "FK_Org_DBankAccount" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 4983 (class 2606 OID 386144)
-- Name: d_config FK_Org_DConfig; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_config
    ADD CONSTRAINT "FK_Org_DConfig" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5101 (class 2606 OID 392924)
-- Name: d_coupon FK_Org_DCoupon; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_coupon
    ADD CONSTRAINT "FK_Org_DCoupon" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 4985 (class 2606 OID 386154)
-- Name: d_currency FK_Org_DCurrency; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_currency
    ADD CONSTRAINT "FK_Org_DCurrency" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5124 (class 2606 OID 393370)
-- Name: d_erp_integration FK_Org_DERPIntegration; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_erp_integration
    ADD CONSTRAINT "FK_Org_DERPIntegration" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 4994 (class 2606 OID 386319)
-- Name: d_floor FK_Org_DFloor; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_floor
    ADD CONSTRAINT "FK_Org_DFloor" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5138 (class 2606 OID 393806)
-- Name: d_integration_history FK_Org_DIntegrationHistory; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_integration_history
    ADD CONSTRAINT "FK_Org_DIntegrationHistory" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 4998 (class 2606 OID 386349)
-- Name: d_invoice FK_Org_DInvoice; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_invoice
    ADD CONSTRAINT "FK_Org_DInvoice" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5001 (class 2606 OID 386379)
-- Name: d_invoiceline FK_Org_DInvoiceLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_invoiceline
    ADD CONSTRAINT "FK_Org_DInvoiceLine" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5003 (class 2606 OID 386394)
-- Name: d_language FK_Org_DLanguage; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_language
    ADD CONSTRAINT "FK_Org_DLanguage" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5007 (class 2606 OID 386414)
-- Name: d_order FK_Org_DOrder; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_order
    ADD CONSTRAINT "FK_Org_DOrder" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5114 (class 2606 OID 393139)
-- Name: d_pc_terminalaccess FK_Org_DPCTerminalAccess; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pc_terminalaccess
    ADD CONSTRAINT "FK_Org_DPCTerminalAccess" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5014 (class 2606 OID 386449)
-- Name: d_pos_order FK_Org_DPOSORder; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_order
    ADD CONSTRAINT "FK_Org_DPOSORder" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5017 (class 2606 OID 386459)
-- Name: d_pos_orderline FK_Org_DPOSOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_orderline
    ADD CONSTRAINT "FK_Org_DPOSOrderLine" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5110 (class 2606 OID 393085)
-- Name: d_pos_payment FK_Org_DPOSPayment; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_payment
    ADD CONSTRAINT "FK_Org_DPOSPayment" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5112 (class 2606 OID 393113)
-- Name: d_pos_taxline FK_Org_DPOSTaxLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_taxline
    ADD CONSTRAINT "FK_Org_DPOSTaxLine" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5099 (class 2606 OID 392895)
-- Name: d_pos_terminal FK_Org_DPOSTerminal; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_terminal
    ADD CONSTRAINT "FK_Org_DPOSTerminal" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5133 (class 2606 OID 393723)
-- Name: d_pay_method FK_Org_DPayMethod; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pay_method
    ADD CONSTRAINT "FK_Org_DPayMethod" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5135 (class 2606 OID 393777)
-- Name: d_paymethod_org FK_Org_DPayMethodOrg; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_paymethod_org
    ADD CONSTRAINT "FK_Org_DPayMethodOrg" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5012 (class 2606 OID 386439)
-- Name: d_payment FK_Org_DPayment; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_payment
    ADD CONSTRAINT "FK_Org_DPayment" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5020 (class 2606 OID 386474)
-- Name: d_product FK_Org_DProduct; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product
    ADD CONSTRAINT "FK_Org_DProduct" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5024 (class 2606 OID 386484)
-- Name: d_product_category FK_Org_DProductCategory; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product_category
    ADD CONSTRAINT "FK_Org_DProductCategory" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5027 (class 2606 OID 386489)
-- Name: d_product_combo FK_Org_DProductCombo; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product_combo
    ADD CONSTRAINT "FK_Org_DProductCombo" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5108 (class 2606 OID 393057)
-- Name: d_product_location FK_Org_DProductLocation; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product_location
    ADD CONSTRAINT "FK_Org_DProductLocation" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5126 (class 2606 OID 393418)
-- Name: d_production FK_Org_DProduction; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_production
    ADD CONSTRAINT "FK_Org_DProduction" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5128 (class 2606 OID 393445)
-- Name: d_productionline FK_Org_DProductionLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_productionline
    ADD CONSTRAINT "FK_Org_DProductionLine" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5140 (class 2606 OID 394299)
-- Name: d_purchase_order FK_Org_DPurchaseOrder; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_purchase_order
    ADD CONSTRAINT "FK_Org_DPurchaseOrder" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5142 (class 2606 OID 394325)
-- Name: d_purchase_orderline FK_Org_DPurchaseOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_purchase_orderline
    ADD CONSTRAINT "FK_Org_DPurchaseOrderLine" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5151 (class 2606 OID 394626)
-- Name: d_request_orderline FK_Org_DRQLINEORder; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_request_orderline
    ADD CONSTRAINT "FK_Org_DRQLINEORder" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5145 (class 2606 OID 394590)
-- Name: d_request_order FK_Org_DRQORder; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_request_order
    ADD CONSTRAINT "FK_Org_DRQORder" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5130 (class 2606 OID 393502)
-- Name: d_reconciledetail FK_Org_DReconcileDetail; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reconciledetail
    ADD CONSTRAINT "FK_Org_DReconcileDetail" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5033 (class 2606 OID 386524)
-- Name: d_reservation_line FK_Org_DReservationLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reservation_line
    ADD CONSTRAINT "FK_Org_DReservationLine" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5036 (class 2606 OID 386534)
-- Name: d_reservation_order FK_Org_DReservationOrder; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reservation_order
    ADD CONSTRAINT "FK_Org_DReservationOrder" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5105 (class 2606 OID 393032)
-- Name: d_shift_control FK_Org_DShiftControl; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_shift_control
    ADD CONSTRAINT "FK_Org_DShiftControl" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5043 (class 2606 OID 386639)
-- Name: d_storage_onhand FK_Org_DStorageOnhand; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_storage_onhand
    ADD CONSTRAINT "FK_Org_DStorageOnhand" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5047 (class 2606 OID 386659)
-- Name: d_table FK_Org_DTable; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_table
    ADD CONSTRAINT "FK_Org_DTable" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5049 (class 2606 OID 386714)
-- Name: d_tax FK_Org_DTax; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_tax
    ADD CONSTRAINT "FK_Org_DTax" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5052 (class 2606 OID 386729)
-- Name: d_tax_category FK_Org_DTaxCategory; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_tax_category
    ADD CONSTRAINT "FK_Org_DTaxCategory" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5055 (class 2606 OID 386764)
-- Name: d_uom FK_Org_DUOM; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_uom
    ADD CONSTRAINT "FK_Org_DUOM" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5059 (class 2606 OID 386784)
-- Name: d_userorg_access FK_Org_DUserOrgAccess; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_userorg_access
    ADD CONSTRAINT "FK_Org_DUserOrgAccess" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5155 (class 2606 OID 394798)
-- Name: d_pos_org_access FK_Org_DUserOrgAccess; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_org_access
    ADD CONSTRAINT "FK_Org_DUserOrgAccess" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5066 (class 2606 OID 386809)
-- Name: d_voucher FK_Org_DVoucher; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_voucher
    ADD CONSTRAINT "FK_Org_DVoucher" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5160 (class 2606 OID 409307)
-- Name: d_pos_closedcash FK_Org_Dclosedcash; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_closedcash
    ADD CONSTRAINT "FK_Org_Dclosedcash" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5068 (class 2606 OID 386819)
-- Name: d_warehouse FK_Org_ID; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_warehouse
    ADD CONSTRAINT "FK_Org_ID" FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5018 (class 2606 OID 386464)
-- Name: d_pos_orderline FK_POSOrder_DPOSOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_orderline
    ADD CONSTRAINT "FK_POSOrder_DPOSOrderLine" FOREIGN KEY (d_pos_order_id) REFERENCES pos.d_pos_order(d_pos_order_id);


--
-- TOC entry 5136 (class 2606 OID 393787)
-- Name: d_paymethod_org FK_PayMethod_DPayMethodOrg; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_paymethod_org
    ADD CONSTRAINT "FK_PayMethod_DPayMethodOrg" FOREIGN KEY (d_pay_method_id) REFERENCES pos.d_pay_method(d_pay_method_id);


--
-- TOC entry 5161 (class 2606 OID 409312)
-- Name: d_pos_closedcash FK_Pos_DTax; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_closedcash
    ADD CONSTRAINT "FK_Pos_DTax" FOREIGN KEY (d_pos_terminal_id) REFERENCES pos.d_pos_terminal(d_pos_terminal_id);


--
-- TOC entry 5093 (class 2606 OID 391480)
-- Name: d_uom_product FK_Product_D; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_uom_product
    ADD CONSTRAINT "FK_Product_D" FOREIGN KEY (d_product_id) REFERENCES pos.d_product(d_product_id);


--
-- TOC entry 5028 (class 2606 OID 386499)
-- Name: d_product_combo FK_Product_DProductCombo; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product_combo
    ADD CONSTRAINT "FK_Product_DProductCombo" FOREIGN KEY (d_product_id) REFERENCES pos.d_product(d_product_id);


--
-- TOC entry 5143 (class 2606 OID 394335)
-- Name: d_purchase_orderline FK_PurchaseOrder_DPurchaseOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_purchase_orderline
    ADD CONSTRAINT "FK_PurchaseOrder_DPurchaseOrderLine" FOREIGN KEY (d_purchase_order_id) REFERENCES pos.d_purchase_order(d_purchase_order_id);


--
-- TOC entry 5031 (class 2606 OID 386514)
-- Name: d_reference_list FK_Reference_DReferenceList; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reference_list
    ADD CONSTRAINT "FK_Reference_DReferenceList" FOREIGN KEY (d_reference_id) REFERENCES pos.d_reference(d_reference_id);


--
-- TOC entry 5061 (class 2606 OID 386794)
-- Name: d_user_role_access FK_Role_DUserRoleAccess; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_user_role_access
    ADD CONSTRAINT "FK_Role_DUserRoleAccess" FOREIGN KEY (d_role_id) REFERENCES pos.d_role(d_role_id);


--
-- TOC entry 5037 (class 2606 OID 386544)
-- Name: d_reservation_order FK_Table_DReservationOrder; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reservation_order
    ADD CONSTRAINT "FK_Table_DReservationOrder" FOREIGN KEY (d_table_id) REFERENCES pos.d_table(d_table_id);


--
-- TOC entry 5050 (class 2606 OID 386719)
-- Name: d_tax FK_TaxCategory_DTax; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_tax
    ADD CONSTRAINT "FK_TaxCategory_DTax" FOREIGN KEY (d_tax_category_id) REFERENCES pos.d_tax_category(d_tax_category_id);


--
-- TOC entry 5098 (class 2606 OID 392852)
-- Name: d_attribute FK_Tenant_DAttr; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_attribute
    ADD CONSTRAINT "FK_Tenant_DAttr" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5103 (class 2606 OID 392972)
-- Name: d_table_use_ref FK_Tenant_DAttr; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_table_use_ref
    ADD CONSTRAINT "FK_Tenant_DAttr" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5117 (class 2606 OID 393192)
-- Name: d_attribute_value FK_Tenant_DAttr; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_attribute_value
    ADD CONSTRAINT "FK_Tenant_DAttr" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5120 (class 2606 OID 393253)
-- Name: d_bank FK_Tenant_DBank; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_bank
    ADD CONSTRAINT "FK_Tenant_DBank" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5123 (class 2606 OID 393280)
-- Name: d_bankaccount FK_Tenant_DBankAccount; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_bankaccount
    ADD CONSTRAINT "FK_Tenant_DBankAccount" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5159 (class 2606 OID 394900)
-- Name: d_api_trace_log FK_Tenant_DBankAccount; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_api_trace_log
    ADD CONSTRAINT "FK_Tenant_DBankAccount" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5092 (class 2606 OID 390970)
-- Name: d_cancel_reason FK_Tenant_DCancelReason; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_cancel_reason
    ADD CONSTRAINT "FK_Tenant_DCancelReason" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 4982 (class 2606 OID 385974)
-- Name: d_changelog FK_Tenant_DChangeLog; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_changelog
    ADD CONSTRAINT "FK_Tenant_DChangeLog" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 4984 (class 2606 OID 386139)
-- Name: d_config FK_Tenant_DConfig; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_config
    ADD CONSTRAINT "FK_Tenant_DConfig" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5102 (class 2606 OID 392929)
-- Name: d_coupon FK_Tenant_DCoupon; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_coupon
    ADD CONSTRAINT "FK_Tenant_DCoupon" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 4986 (class 2606 OID 386149)
-- Name: d_currency FK_Tenant_DCurrency; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_currency
    ADD CONSTRAINT "FK_Tenant_DCurrency" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 4987 (class 2606 OID 386219)
-- Name: d_customer FK_Tenant_DCustomer; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_customer
    ADD CONSTRAINT "FK_Tenant_DCustomer" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 4989 (class 2606 OID 386224)
-- Name: d_doctype FK_Tenant_DDocType; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_doctype
    ADD CONSTRAINT "FK_Tenant_DDocType" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5125 (class 2606 OID 393375)
-- Name: d_erp_integration FK_Tenant_DERPIntegration; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_erp_integration
    ADD CONSTRAINT "FK_Tenant_DERPIntegration" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 4992 (class 2606 OID 386229)
-- Name: d_expense FK_Tenant_DExpense; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_expense
    ADD CONSTRAINT "FK_Tenant_DExpense" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 4993 (class 2606 OID 386239)
-- Name: d_expense_category FK_Tenant_DExpenseCategory; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_expense_category
    ADD CONSTRAINT "FK_Tenant_DExpenseCategory" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 4995 (class 2606 OID 386314)
-- Name: d_floor FK_Tenant_DFloor; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_floor
    ADD CONSTRAINT "FK_Tenant_DFloor" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 4996 (class 2606 OID 386324)
-- Name: d_image FK_Tenant_DImage; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_image
    ADD CONSTRAINT "FK_Tenant_DImage" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5139 (class 2606 OID 393811)
-- Name: d_integration_history FK_Tenant_DIntegrationHistory; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_integration_history
    ADD CONSTRAINT "FK_Tenant_DIntegrationHistory" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 4999 (class 2606 OID 386329)
-- Name: d_invoice FK_Tenant_DInvoice; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_invoice
    ADD CONSTRAINT "FK_Tenant_DInvoice" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5002 (class 2606 OID 386374)
-- Name: d_invoiceline FK_Tenant_DInvoiceLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_invoiceline
    ADD CONSTRAINT "FK_Tenant_DInvoiceLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5079 (class 2606 OID 390880)
-- Name: d_kitchen_order FK_Tenant_DKitchenOrder; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_kitchen_order
    ADD CONSTRAINT "FK_Tenant_DKitchenOrder" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5084 (class 2606 OID 390906)
-- Name: d_kitchen_orderline FK_Tenant_DKitchenOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_kitchen_orderline
    ADD CONSTRAINT "FK_Tenant_DKitchenOrderLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5004 (class 2606 OID 386389)
-- Name: d_language FK_Tenant_DLanguage; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_language
    ADD CONSTRAINT "FK_Tenant_DLanguage" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5090 (class 2606 OID 390947)
-- Name: d_note FK_Tenant_DNote; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_note
    ADD CONSTRAINT "FK_Tenant_DNote" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5089 (class 2606 OID 390929)
-- Name: d_note_group FK_Tenant_DNoteGroup; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_note_group
    ADD CONSTRAINT "FK_Tenant_DNoteGroup" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5091 (class 2606 OID 390952)
-- Name: d_note FK_Tenant_DNoteGroup; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_note
    ADD CONSTRAINT "FK_Tenant_DNoteGroup" FOREIGN KEY (d_note_group_id) REFERENCES pos.d_note_group(d_note_group_id);


--
-- TOC entry 5006 (class 2606 OID 386404)
-- Name: d_notification FK_Tenant_DNotification; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_notification
    ADD CONSTRAINT "FK_Tenant_DNotification" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5008 (class 2606 OID 386409)
-- Name: d_order FK_Tenant_DOrder; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_order
    ADD CONSTRAINT "FK_Tenant_DOrder" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5010 (class 2606 OID 386419)
-- Name: d_orderline FK_Tenant_DOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_orderline
    ADD CONSTRAINT "FK_Tenant_DOrderLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5115 (class 2606 OID 393144)
-- Name: d_pc_terminalaccess FK_Tenant_DPCTerminalAccess; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pc_terminalaccess
    ADD CONSTRAINT "FK_Tenant_DPCTerminalAccess" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5015 (class 2606 OID 386444)
-- Name: d_pos_order FK_Tenant_DPOSOrder; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_order
    ADD CONSTRAINT "FK_Tenant_DPOSOrder" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5019 (class 2606 OID 386454)
-- Name: d_pos_orderline FK_Tenant_DPOSOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_orderline
    ADD CONSTRAINT "FK_Tenant_DPOSOrderLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5111 (class 2606 OID 393090)
-- Name: d_pos_payment FK_Tenant_DPOSPayment; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_payment
    ADD CONSTRAINT "FK_Tenant_DPOSPayment" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5113 (class 2606 OID 393118)
-- Name: d_pos_taxline FK_Tenant_DPOSTaxLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_taxline
    ADD CONSTRAINT "FK_Tenant_DPOSTaxLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5100 (class 2606 OID 392900)
-- Name: d_pos_terminal FK_Tenant_DPOSTerminal; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_terminal
    ADD CONSTRAINT "FK_Tenant_DPOSTerminal" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5077 (class 2606 OID 389561)
-- Name: d_partner_group FK_Tenant_DPartnerGroup; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_partner_group
    ADD CONSTRAINT "FK_Tenant_DPartnerGroup" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5134 (class 2606 OID 393728)
-- Name: d_pay_method FK_Tenant_DPayMethod; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pay_method
    ADD CONSTRAINT "FK_Tenant_DPayMethod" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5137 (class 2606 OID 393782)
-- Name: d_paymethod_org FK_Tenant_DPayMethodOrg; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_paymethod_org
    ADD CONSTRAINT "FK_Tenant_DPayMethodOrg" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5013 (class 2606 OID 386434)
-- Name: d_payment FK_Tenant_DPayment; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_payment
    ADD CONSTRAINT "FK_Tenant_DPayment" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5071 (class 2606 OID 388763)
-- Name: d_pricelist FK_Tenant_DPriceList; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pricelist
    ADD CONSTRAINT "FK_Tenant_DPriceList" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5075 (class 2606 OID 388803)
-- Name: d_pricelist_org FK_Tenant_DPriceListOrg; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pricelist_org
    ADD CONSTRAINT "FK_Tenant_DPriceListOrg" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5072 (class 2606 OID 388784)
-- Name: d_pricelist_product FK_Tenant_DPriceListProduct; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pricelist_product
    ADD CONSTRAINT "FK_Tenant_DPriceListProduct" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5078 (class 2606 OID 390237)
-- Name: d_print_report FK_Tenant_DPrintReport; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_print_report
    ADD CONSTRAINT "FK_Tenant_DPrintReport" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5021 (class 2606 OID 386469)
-- Name: d_product FK_Tenant_DProduct; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product
    ADD CONSTRAINT "FK_Tenant_DProduct" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5025 (class 2606 OID 386479)
-- Name: d_product_category FK_Tenant_DProductCategory; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product_category
    ADD CONSTRAINT "FK_Tenant_DProductCategory" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5029 (class 2606 OID 386494)
-- Name: d_product_combo FK_Tenant_DProductCombo; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product_combo
    ADD CONSTRAINT "FK_Tenant_DProductCombo" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5109 (class 2606 OID 393062)
-- Name: d_product_location FK_Tenant_DProductLocation; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product_location
    ADD CONSTRAINT "FK_Tenant_DProductLocation" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5127 (class 2606 OID 393423)
-- Name: d_production FK_Tenant_DProduction; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_production
    ADD CONSTRAINT "FK_Tenant_DProduction" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5129 (class 2606 OID 393450)
-- Name: d_productionline FK_Tenant_DProductionLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_productionline
    ADD CONSTRAINT "FK_Tenant_DProductionLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5141 (class 2606 OID 394304)
-- Name: d_purchase_order FK_Tenant_DPurchaseOrder; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_purchase_order
    ADD CONSTRAINT "FK_Tenant_DPurchaseOrder" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5144 (class 2606 OID 394330)
-- Name: d_purchase_orderline FK_Tenant_DPurchaseOrderLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_purchase_orderline
    ADD CONSTRAINT "FK_Tenant_DPurchaseOrderLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5152 (class 2606 OID 394621)
-- Name: d_request_orderline FK_Tenant_DRQLINEORDER; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_request_orderline
    ADD CONSTRAINT "FK_Tenant_DRQLINEORDER" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5146 (class 2606 OID 394585)
-- Name: d_request_order FK_Tenant_DRQORDER; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_request_order
    ADD CONSTRAINT "FK_Tenant_DRQORDER" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5131 (class 2606 OID 393507)
-- Name: d_reconciledetail FK_Tenant_DReconcileDetail; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reconciledetail
    ADD CONSTRAINT "FK_Tenant_DReconcileDetail" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5030 (class 2606 OID 386504)
-- Name: d_reference FK_Tenant_DReference; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reference
    ADD CONSTRAINT "FK_Tenant_DReference" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5032 (class 2606 OID 386509)
-- Name: d_reference_list FK_Tenant_DReferenceList; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reference_list
    ADD CONSTRAINT "FK_Tenant_DReferenceList" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5034 (class 2606 OID 386519)
-- Name: d_reservation_line FK_Tenant_DReservationLine; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reservation_line
    ADD CONSTRAINT "FK_Tenant_DReservationLine" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5038 (class 2606 OID 386529)
-- Name: d_reservation_order FK_Tenant_DReservationOrder; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reservation_order
    ADD CONSTRAINT "FK_Tenant_DReservationOrder" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5041 (class 2606 OID 386549)
-- Name: d_role FK_Tenant_DRole; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_role
    ADD CONSTRAINT "FK_Tenant_DRole" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5106 (class 2606 OID 393037)
-- Name: d_shift_control FK_Tenant_DShiftControl; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_shift_control
    ADD CONSTRAINT "FK_Tenant_DShiftControl" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5044 (class 2606 OID 386634)
-- Name: d_storage_onhand FK_Tenant_DStorageOnhand; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_storage_onhand
    ADD CONSTRAINT "FK_Tenant_DStorageOnhand" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5048 (class 2606 OID 386654)
-- Name: d_table FK_Tenant_DTable; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_table
    ADD CONSTRAINT "FK_Tenant_DTable" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5051 (class 2606 OID 386669)
-- Name: d_tax FK_Tenant_DTax; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_tax
    ADD CONSTRAINT "FK_Tenant_DTax" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5162 (class 2606 OID 409302)
-- Name: d_pos_closedcash FK_Tenant_DTax; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_closedcash
    ADD CONSTRAINT "FK_Tenant_DTax" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5053 (class 2606 OID 386724)
-- Name: d_tax_category FK_Tenant_DTaxCategory; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_tax_category
    ADD CONSTRAINT "FK_Tenant_DTaxCategory" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5056 (class 2606 OID 386759)
-- Name: d_uom FK_Tenant_DUOM; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_uom
    ADD CONSTRAINT "FK_Tenant_DUOM" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5094 (class 2606 OID 391475)
-- Name: d_uom_product FK_Tenant_DUOM; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_uom_product
    ADD CONSTRAINT "FK_Tenant_DUOM" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5057 (class 2606 OID 386774)
-- Name: d_user FK_Tenant_DUser; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_user
    ADD CONSTRAINT "FK_Tenant_DUser" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5063 (class 2606 OID 386799)
-- Name: d_vendor FK_Tenant_DVendor; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_vendor
    ADD CONSTRAINT "FK_Tenant_DVendor" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5070 (class 2606 OID 388491)
-- Name: d_vendor_audit FK_Tenant_DVendor; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_vendor_audit
    ADD CONSTRAINT "FK_Tenant_DVendor" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5067 (class 2606 OID 386804)
-- Name: d_voucher FK_Tenant_DVoucher; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_voucher
    ADD CONSTRAINT "FK_Tenant_DVoucher" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5069 (class 2606 OID 386814)
-- Name: d_warehouse FK_Tenant_ID; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_warehouse
    ADD CONSTRAINT "FK_Tenant_ID" FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 5163 (class 2606 OID 409317)
-- Name: d_pos_closedcash FK_User_DTax; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_closedcash
    ADD CONSTRAINT "FK_User_DTax" FOREIGN KEY (d_user_id) REFERENCES pos.d_user(d_user_id);


--
-- TOC entry 5060 (class 2606 OID 386779)
-- Name: d_userorg_access FK_User_DUserOrgAccess; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_userorg_access
    ADD CONSTRAINT "FK_User_DUserOrgAccess" FOREIGN KEY (d_user_id) REFERENCES pos.d_user(d_user_id);


--
-- TOC entry 5156 (class 2606 OID 394793)
-- Name: d_pos_org_access FK_User_DUserOrgAccess; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_org_access
    ADD CONSTRAINT "FK_User_DUserOrgAccess" FOREIGN KEY (d_user_id) REFERENCES pos.d_user(d_user_id);


--
-- TOC entry 5062 (class 2606 OID 386789)
-- Name: d_user_role_access FK_User_DUserRoleAccess; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_user_role_access
    ADD CONSTRAINT "FK_User_DUserRoleAccess" FOREIGN KEY (d_user_id) REFERENCES pos.d_user(d_user_id);


--
-- TOC entry 5005 (class 2606 OID 386399)
-- Name: d_locator FK_Warehouse_DLocator; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_locator
    ADD CONSTRAINT "FK_Warehouse_DLocator" FOREIGN KEY (d_warehouse_id) REFERENCES pos.d_warehouse(d_warehouse_id);


--
-- TOC entry 5045 (class 2606 OID 386644)
-- Name: d_storage_onhand FK_Warehouse_DStorageOnhand; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_storage_onhand
    ADD CONSTRAINT "FK_Warehouse_DStorageOnhand" FOREIGN KEY (d_warehouse_id) REFERENCES pos.d_warehouse(d_warehouse_id);


--
-- TOC entry 5118 (class 2606 OID 393197)
-- Name: d_attribute_value FK_attr_attrvalue; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_attribute_value
    ADD CONSTRAINT "FK_attr_attrvalue" FOREIGN KEY (d_attribute_id) REFERENCES pos.d_attribute(d_attribute_id);


--
-- TOC entry 5157 (class 2606 OID 394803)
-- Name: d_pos_org_access FK_postermial_org_access; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_org_access
    ADD CONSTRAINT "FK_postermial_org_access" FOREIGN KEY (d_pos_terminal_id) REFERENCES pos.d_pos_terminal(d_pos_terminal_id);


--
-- TOC entry 5104 (class 2606 OID 392977)
-- Name: d_table_use_ref FK_reference_domainuse; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_table_use_ref
    ADD CONSTRAINT "FK_reference_domainuse" FOREIGN KEY (d_reference_id) REFERENCES pos.d_reference(d_reference_id);


--
-- TOC entry 5095 (class 2606 OID 392805)
-- Name: d_assign_org_product d_assign_org_product_d_org_d_org_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_assign_org_product
    ADD CONSTRAINT d_assign_org_product_d_org_d_org_id_fk FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5096 (class 2606 OID 392810)
-- Name: d_assign_org_product d_assign_org_product_d_product_d_product_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_assign_org_product
    ADD CONSTRAINT d_assign_org_product_d_product_d_product_id_fk FOREIGN KEY (d_product_id) REFERENCES pos.d_product(d_product_id);


--
-- TOC entry 5097 (class 2606 OID 392815)
-- Name: d_assign_org_product d_assign_org_product_d_tenant_d_tenant_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_assign_org_product
    ADD CONSTRAINT d_assign_org_product_d_tenant_d_tenant_id_fk FOREIGN KEY (d_tenant_id) REFERENCES pos.d_tenant(d_tenant_id);


--
-- TOC entry 4990 (class 2606 OID 409349)
-- Name: d_doctype d_doctype_d_org_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_doctype
    ADD CONSTRAINT d_doctype_d_org_id_fk FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5080 (class 2606 OID 392285)
-- Name: d_kitchen_order d_kitchen_order_d_floor_d_floor_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_kitchen_order
    ADD CONSTRAINT d_kitchen_order_d_floor_d_floor_id_fk FOREIGN KEY (d_floor_id) REFERENCES pos.d_floor(d_floor_id);


--
-- TOC entry 5081 (class 2606 OID 392290)
-- Name: d_kitchen_order d_kitchen_order_d_table_d_table_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_kitchen_order
    ADD CONSTRAINT d_kitchen_order_d_table_d_table_id_fk FOREIGN KEY (d_table_id) REFERENCES pos.d_table(d_table_id);


--
-- TOC entry 5082 (class 2606 OID 392295)
-- Name: d_kitchen_order d_kitchen_order_d_warehouse_d_warehouse_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_kitchen_order
    ADD CONSTRAINT d_kitchen_order_d_warehouse_d_warehouse_id_fk FOREIGN KEY (d_warehouse_id) REFERENCES pos.d_warehouse(d_warehouse_id);


--
-- TOC entry 5085 (class 2606 OID 393878)
-- Name: d_kitchen_orderline d_kitchen_orderline_d_cancel_reason_d_cancel_reason_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_kitchen_orderline
    ADD CONSTRAINT d_kitchen_orderline_d_cancel_reason_d_cancel_reason_id_fk FOREIGN KEY (d_cancel_reason_id) REFERENCES pos.d_cancel_reason(d_cancel_reason_id);


--
-- TOC entry 5086 (class 2606 OID 392300)
-- Name: d_kitchen_orderline d_kitchen_orderline_d_pos_orderline_d_pos_orderline_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_kitchen_orderline
    ADD CONSTRAINT d_kitchen_orderline_d_pos_orderline_d_pos_orderline_id_fk FOREIGN KEY (d_pos_orderline_id) REFERENCES pos.d_pos_orderline(d_pos_orderline_id);


--
-- TOC entry 5087 (class 2606 OID 427017)
-- Name: d_kitchen_orderline d_kitchen_orderline_d_product_d_product_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_kitchen_orderline
    ADD CONSTRAINT d_kitchen_orderline_d_product_d_product_id_fk FOREIGN KEY (d_product_id) REFERENCES pos.d_product(d_product_id);


--
-- TOC entry 5088 (class 2606 OID 393862)
-- Name: d_kitchen_orderline d_kitchen_orderline_d_production_d_production_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_kitchen_orderline
    ADD CONSTRAINT d_kitchen_orderline_d_production_d_production_id_fk FOREIGN KEY (d_production_id) REFERENCES pos.d_production(d_production_id);


--
-- TOC entry 5116 (class 2606 OID 440257)
-- Name: d_pc_terminalaccess d_pc_terminalaccess_d_product_category_id_fkey; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pc_terminalaccess
    ADD CONSTRAINT d_pc_terminalaccess_d_product_category_id_fkey FOREIGN KEY (d_product_category_id) REFERENCES pos.d_product_category(d_product_category_id);


--
-- TOC entry 5016 (class 2606 OID 394423)
-- Name: d_pos_order d_pos_order_d_doctype_d_doctype_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_order
    ADD CONSTRAINT d_pos_order_d_doctype_d_doctype_id_fk FOREIGN KEY (d_doctype_id) REFERENCES pos.d_doctype(d_doctype_id);


--
-- TOC entry 5076 (class 2606 OID 393628)
-- Name: d_pricelist_org d_pricelist_org_d_org__fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pricelist_org
    ADD CONSTRAINT d_pricelist_org_d_org__fk FOREIGN KEY (d_org_id) REFERENCES pos.d_org(d_org_id);


--
-- TOC entry 5073 (class 2606 OID 392503)
-- Name: d_pricelist_product d_pricelist_product_d_pricelist_d_pricelist_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pricelist_product
    ADD CONSTRAINT d_pricelist_product_d_pricelist_d_pricelist_id_fk FOREIGN KEY (d_pricelist_id) REFERENCES pos.d_pricelist(d_pricelist_id);


--
-- TOC entry 5074 (class 2606 OID 392508)
-- Name: d_pricelist_product d_pricelist_product_d_product_d_product_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pricelist_product
    ADD CONSTRAINT d_pricelist_product_d_product_d_product_id_fk FOREIGN KEY (d_product_id) REFERENCES pos.d_product(d_product_id);


--
-- TOC entry 5022 (class 2606 OID 538375)
-- Name: d_product d_product_d_product_d_product_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product
    ADD CONSTRAINT d_product_d_product_d_product_id_fk FOREIGN KEY (d_product_parent_id) REFERENCES pos.d_product(d_product_id);


--
-- TOC entry 5023 (class 2606 OID 391903)
-- Name: d_product d_product_d_tax_d_tax_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product
    ADD CONSTRAINT d_product_d_tax_d_tax_id_fk FOREIGN KEY (d_tax_id) REFERENCES pos.d_tax(d_tax_id);


--
-- TOC entry 5026 (class 2606 OID 391812)
-- Name: d_product_category d_productcategory_parent_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_product_category
    ADD CONSTRAINT d_productcategory_parent_id_fk FOREIGN KEY (d_product_category_parent_id) REFERENCES pos.d_product_category(d_product_category_id);


--
-- TOC entry 5039 (class 2606 OID 392141)
-- Name: d_reservation_order d_reservation_order_d_customer_d_customer_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reservation_order
    ADD CONSTRAINT d_reservation_order_d_customer_d_customer_id_fk FOREIGN KEY (d_customer_id) REFERENCES pos.d_customer(d_customer_id);


--
-- TOC entry 5040 (class 2606 OID 392146)
-- Name: d_reservation_order d_reservation_order_d_user_d_user_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_reservation_order
    ADD CONSTRAINT d_reservation_order_d_user_d_user_id_fk FOREIGN KEY (d_user_id) REFERENCES pos.d_user(d_user_id);


--
-- TOC entry 5147 (class 2606 OID 394657)
-- Name: d_request_order d_rq_d_customer; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_request_order
    ADD CONSTRAINT d_rq_d_customer FOREIGN KEY (d_customer_id) REFERENCES pos.d_customer(d_customer_id);


--
-- TOC entry 5148 (class 2606 OID 394595)
-- Name: d_request_order d_rq_d_doctype_d_doctype_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_request_order
    ADD CONSTRAINT d_rq_d_doctype_d_doctype_id_fk FOREIGN KEY (d_doctype_id) REFERENCES pos.d_doctype(d_doctype_id);


--
-- TOC entry 5153 (class 2606 OID 394636)
-- Name: d_request_orderline d_rq_d_rql; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_request_orderline
    ADD CONSTRAINT d_rq_d_rql FOREIGN KEY (d_request_order_id) REFERENCES pos.d_request_order(d_request_order_id);


--
-- TOC entry 5149 (class 2606 OID 394600)
-- Name: d_request_order d_rq_order_d_floor; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_request_order
    ADD CONSTRAINT d_rq_order_d_floor FOREIGN KEY (d_floor_id) REFERENCES pos.d_floor(d_floor_id);


--
-- TOC entry 5150 (class 2606 OID 394605)
-- Name: d_request_order d_rq_order_d_table; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_request_order
    ADD CONSTRAINT d_rq_order_d_table FOREIGN KEY (d_table_id) REFERENCES pos.d_table(d_table_id);


--
-- TOC entry 5154 (class 2606 OID 394631)
-- Name: d_request_orderline d_rqline_d_product; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_request_orderline
    ADD CONSTRAINT d_rqline_d_product FOREIGN KEY (d_product_id) REFERENCES pos.d_product(d_product_id);


--
-- TOC entry 5064 (class 2606 OID 394917)
-- Name: d_vendor d_vendor_d_partner_group_d_partner_group_id_fk; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_vendor
    ADD CONSTRAINT d_vendor_d_partner_group_d_partner_group_id_fk FOREIGN KEY (d_partner_group_id) REFERENCES pos.d_partner_group(d_partner_group_id);


--
-- TOC entry 5164 (class 2606 OID 409342)
-- Name: d_pos_closedcash doc_closedcash; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_pos_closedcash
    ADD CONSTRAINT doc_closedcash FOREIGN KEY (d_doctype_id) REFERENCES pos.d_doctype(d_doctype_id);


--
-- TOC entry 5065 (class 2606 OID 388542)
-- Name: d_vendor fk1dkfg1ekvbgi1geluuc6jmwbt; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_vendor
    ADD CONSTRAINT fk1dkfg1ekvbgi1geluuc6jmwbt FOREIGN KEY (d_image_id) REFERENCES pos.d_image(d_image_id);


--
-- TOC entry 5107 (class 2606 OID 422469)
-- Name: d_shift_control fk_doctype_dshiftcontrol; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_shift_control
    ADD CONSTRAINT fk_doctype_dshiftcontrol FOREIGN KEY (d_doctype_id) REFERENCES pos.d_doctype(d_doctype_id);


--
-- TOC entry 4988 (class 2606 OID 388254)
-- Name: d_customer fkbse7tsjle0cs5pvoyjp9apunw; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_customer
    ADD CONSTRAINT fkbse7tsjle0cs5pvoyjp9apunw FOREIGN KEY (d_image_id) REFERENCES pos.d_image(d_image_id);


--
-- TOC entry 5058 (class 2606 OID 388259)
-- Name: d_user fkgffw5tlc364ny53nqlaj4dyh6; Type: FK CONSTRAINT; Schema: pos; Owner: -
--

ALTER TABLE ONLY pos.d_user
    ADD CONSTRAINT fkgffw5tlc364ny53nqlaj4dyh6 FOREIGN KEY (d_image_id) REFERENCES pos.d_image(d_image_id);


-- Completed on 2024-09-28 16:54:23

--
-- PostgreSQL database dump complete
--

