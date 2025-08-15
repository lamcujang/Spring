
--liquibase formatted sql
--changeset dbiz:test

CREATE SCHEMA pos;


-- DROP FUNCTION pos.uuid_generate_v4();

CREATE OR REPLACE FUNCTION pos.uuid_generate_v4()
 RETURNS uuid
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_generate_v4$function$
;

CREATE SEQUENCE pos.d_api_trace_log_sq
    START WITH 1000000
    INCREMENT BY 1
    MINVALUE 1000000
    NO MAXVALUE
    CACHE 1;


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