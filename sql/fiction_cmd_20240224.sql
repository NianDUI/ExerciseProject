--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

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

DROP INDEX public.paragraph_h19_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h18_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h17_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h16_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h15_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h14_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h13_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h12_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h11_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h10_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h09_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h08_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h07_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h06_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h05_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h04_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h03_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h02_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h01_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_h00_bookid_chapterid_seqid_idx;
DROP INDEX public.paragraph_bookid_chapterid_seqid_idx;
ALTER TABLE ONLY public.task DROP CONSTRAINT task_pkey;
ALTER TABLE ONLY public.site DROP CONSTRAINT site_pkey;
ALTER TABLE ONLY public.proxy DROP CONSTRAINT proxy_pkey;
ALTER TABLE ONLY public.config DROP CONSTRAINT config_pkey;
ALTER TABLE ONLY public.chapter DROP CONSTRAINT chapter_pkey;
ALTER TABLE ONLY public.book DROP CONSTRAINT book_pkey;
DROP TABLE public.task;
DROP TABLE public.site;
DROP SEQUENCE public.seq_task;
DROP SEQUENCE public.seq_site;
DROP TABLE public.proxy;
DROP TABLE public.paragraph_h19;
DROP TABLE public.paragraph_h18;
DROP TABLE public.paragraph_h17;
DROP TABLE public.paragraph_h16;
DROP TABLE public.paragraph_h15;
DROP TABLE public.paragraph_h14;
DROP TABLE public.paragraph_h13;
DROP TABLE public.paragraph_h12;
DROP TABLE public.paragraph_h11;
DROP TABLE public.paragraph_h10;
DROP TABLE public.paragraph_h09;
DROP TABLE public.paragraph_h08;
DROP TABLE public.paragraph_h07;
DROP TABLE public.paragraph_h06;
DROP TABLE public.paragraph_h05;
DROP TABLE public.paragraph_h04;
DROP TABLE public.paragraph_h03;
DROP TABLE public.paragraph_h02;
DROP TABLE public.paragraph_h01;
DROP TABLE public.paragraph_h00;
DROP TABLE public.paragraph;
DROP TABLE public.config;
DROP SEQUENCE public.seq_config;
DROP TABLE public.chapter;
DROP SEQUENCE public.seq_chapter;
DROP TABLE public.book;
DROP SEQUENCE public.seq_book;
DROP FUNCTION public."AUTO_INSERT_INTO_TD_AI_ID"();
DROP FUNCTION public."AUTO_INSERT_INTO_TD_AI"();
--
-- Name: AUTO_INSERT_INTO_TD_AI(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public."AUTO_INSERT_INTO_TD_AI"() RETURNS trigger
    LANGUAGE plpgsql
    AS $_$
DECLARE
    time_column_name 	text ;			-- 父表中用于分区的时间字段的名称[必须首先初始化!!]
    curMM 		varchar(6);		-- 'YYYYMM'字串,用做分区子表的后缀
    isExist 		boolean;		-- 分区子表,是否已存在
    startTime 		text;
    endTime		text;
    strSQL  		text;
    
BEGIN
    -- 调用前,必须首先初始化(时间字段名):time_column_name [直接从调用参数中获取!!]
    time_column_name := TG_ARGV[0];
   
    -- 判断对应分区表 是否已经存在?
    EXECUTE 'SELECT $1.'||time_column_name INTO strSQL USING NEW;
    curMM := to_char( strSQL::timestamp , 'YYYYMM' );
    select count(*) INTO isExist from pg_class where relname = (TG_RELNAME||'_'||curMM);
 
    -- 若不存在, 则插入前需 先创建子分区
    IF ( isExist = false ) THEN  
        -- 创建子分区表
        startTime := curMM||'01 00:00:00.000';
        endTime := to_char( startTime::timestamp + interval '1 month', 'YYYY-MM-DD HH24:MI:SS.MS');
        strSQL := 'CREATE TABLE IF NOT EXISTS '||TG_RELNAME||'_'||curMM||
                  ' ( CHECK('||time_column_name||'>='''|| startTime ||''' AND '
                             ||time_column_name||'< '''|| endTime ||''' )
                          ) INHERITS ('||TG_RELNAME||') ;'  ;  
        EXECUTE strSQL;
 
        -- 创建索引
        strSQL := 'CREATE INDEX '||TG_RELNAME||'_'||curMM||'_INDEX_'||time_column_name||' ON '
                  ||TG_RELNAME||'_'||curMM||' ('||time_column_name||');' ;
        EXECUTE strSQL;
       
    END IF;
 
    -- 插入数据到子分区!
    strSQL := 'INSERT INTO '||TG_RELNAME||'_'||curMM||' SELECT $1.*' ;
    EXECUTE strSQL USING NEW;
    RETURN NULL; 
END
$_$;


ALTER FUNCTION public."AUTO_INSERT_INTO_TD_AI"() OWNER TO postgres;

--
-- Name: AUTO_INSERT_INTO_TD_AI_ID(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public."AUTO_INSERT_INTO_TD_AI_ID"() RETURNS trigger
    LANGUAGE plpgsql
    AS $_$
DECLARE
    id_column_name 	text ;				-- 父表中用于分区的数字id字段的名称[必须首先初始化!!]
    curMM 					varchar(6);		-- '00'字串,用做分区子表的后缀
    isExist 				boolean;			-- 分区子表,是否已存在
		idInt8					int8;
    strSQL  				text;
    
BEGIN
    -- 调用前,必须首先初始化(数字id字段名):id_column_name [直接从调用参数中获取!!]
    id_column_name := TG_ARGV[0];
   
    -- 判断对应分区表 是否已经存在?
    EXECUTE 'SELECT $1.'||id_column_name INTO strSQL USING NEW;
		idInt8 := strSQL::int8;
		idInt8 := idInt8 % 20;
    curMM := trim(to_char( idInt8 , '00' ));
    select count(*) INTO isExist from pg_class where relname = (TG_TABLE_NAME||'_'||curMM);
 
    -- 若不存在, 则插入前需 先创建子分区
    IF ( isExist = false ) THEN  
        -- 创建子分区表
        strSQL := 'CREATE TABLE IF NOT EXISTS '||TG_TABLE_NAME||'_'||curMM||
                  ' ( CHECK('||id_column_name||' % 20 ='|| idInt8 ||' ) ) INHERITS ('||TG_TABLE_NAME||') ;'  ;  
        EXECUTE strSQL;
 
        -- 创建索引
        strSQL := 'CREATE INDEX '||TG_TABLE_NAME||'_'||curMM||'_INDEX_'||id_column_name||' ON '
                  ||TG_TABLE_NAME||'_'||curMM||' ('||id_column_name||');' ;
        EXECUTE strSQL;
       
    END IF;
 
    -- 插入数据到子分区!
    strSQL := 'INSERT INTO '||TG_TABLE_NAME||'_'||curMM||' SELECT $1.*' ;
    EXECUTE strSQL USING NEW;
    RETURN NULL; 
END
$_$;


ALTER FUNCTION public."AUTO_INSERT_INTO_TD_AI_ID"() OWNER TO postgres;

--
-- Name: seq_book; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_book
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_book OWNER TO postgres;

--
-- Name: SEQUENCE seq_book; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON SEQUENCE public.seq_book IS '书籍序列';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book (
    bookid bigint DEFAULT nextval('public.seq_book'::regclass) NOT NULL,
    name character varying(32) NOT NULL,
    url character varying(128),
    configid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL,
    starturl character varying(128) NOT NULL,
    handlerinfo character varying(256) NOT NULL,
    siteid bigint NOT NULL,
    taskstatus integer DEFAULT 0 NOT NULL,
    taskswitch integer DEFAULT 1 NOT NULL,
    detail character varying(256),
    lastgettime timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.book OWNER TO postgres;

--
-- Name: TABLE book; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.book IS '书籍';


--
-- Name: COLUMN book.bookid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book.bookid IS '书籍id';


--
-- Name: COLUMN book.name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book.name IS '书籍名称';


--
-- Name: COLUMN book.url; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book.url IS '书籍链接';


--
-- Name: COLUMN book.configid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book.configid IS '书籍配置';


--
-- Name: COLUMN book.createtime; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book.createtime IS '创建时间';


--
-- Name: COLUMN book.starturl; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book.starturl IS '起始章节链接';


--
-- Name: COLUMN book.handlerinfo; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book.handlerinfo IS '处理信息';


--
-- Name: COLUMN book.siteid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book.siteid IS '站点id';


--
-- Name: COLUMN book.taskstatus; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book.taskstatus IS '任务状态：0无，1重新获取全部，2获取后续章节，3重新获取单章';


--
-- Name: COLUMN book.taskswitch; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book.taskswitch IS '任务开关：0关闭，1开启';


--
-- Name: COLUMN book.detail; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book.detail IS '书籍详情';


--
-- Name: COLUMN book.lastgettime; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book.lastgettime IS '最后获取时间';


--
-- Name: seq_chapter; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_chapter
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_chapter OWNER TO postgres;

--
-- Name: SEQUENCE seq_chapter; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON SEQUENCE public.seq_chapter IS '章节序列';


--
-- Name: chapter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chapter (
    chapterid bigint DEFAULT nextval('public.seq_chapter'::regclass) NOT NULL,
    name character varying(64),
    bookid bigint NOT NULL,
    configid bigint,
    rawname character varying(64) NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL,
    seqid bigint NOT NULL,
    url character varying(128) NOT NULL
);


ALTER TABLE public.chapter OWNER TO postgres;

--
-- Name: TABLE chapter; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.chapter IS '章节';


--
-- Name: COLUMN chapter.chapterid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.chapter.chapterid IS '章节id';


--
-- Name: COLUMN chapter.name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.chapter.name IS '章节名称';


--
-- Name: COLUMN chapter.bookid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.chapter.bookid IS '书籍id';


--
-- Name: COLUMN chapter.configid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.chapter.configid IS '配置id';


--
-- Name: COLUMN chapter.rawname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.chapter.rawname IS '原章节名称';


--
-- Name: COLUMN chapter.createtime; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.chapter.createtime IS '创建时间';


--
-- Name: COLUMN chapter.seqid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.chapter.seqid IS '排序id';


--
-- Name: COLUMN chapter.url; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.chapter.url IS '章节链接';


--
-- Name: seq_config; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_config
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_config OWNER TO postgres;

--
-- Name: SEQUENCE seq_config; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON SEQUENCE public.seq_config IS '配置序列';


--
-- Name: config; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config (
    configid bigint DEFAULT nextval('public.seq_config'::regclass) NOT NULL,
    name character varying(32) NOT NULL,
    titlematch character varying(128) NOT NULL,
    titlelnnum integer NOT NULL,
    conmatch character varying(128) NOT NULL,
    conlnnum integer NOT NULL,
    startoffset integer NOT NULL,
    endoffset integer NOT NULL,
    amatch character varying(128) NOT NULL,
    nexta integer NOT NULL
);


ALTER TABLE public.config OWNER TO postgres;

--
-- Name: TABLE config; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.config IS '配置';


--
-- Name: COLUMN config.configid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.config.configid IS '配置id';


--
-- Name: COLUMN config.name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.config.name IS '配置名称';


--
-- Name: COLUMN config.titlematch; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.config.titlematch IS '标题匹配路径';


--
-- Name: COLUMN config.titlelnnum; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.config.titlelnnum IS '标题后换行数量';


--
-- Name: COLUMN config.conmatch; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.config.conmatch IS '内容匹配路径';


--
-- Name: COLUMN config.conlnnum; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.config.conlnnum IS '内容后换行数量';


--
-- Name: COLUMN config.startoffset; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.config.startoffset IS '内容开始索引偏移量';


--
-- Name: COLUMN config.endoffset; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.config.endoffset IS '内容结束索引偏移量';


--
-- Name: COLUMN config.amatch; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.config.amatch IS '跳转超链接匹配路径';


--
-- Name: COLUMN config.nexta; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.config.nexta IS '下一页超链接索引';


--
-- Name: paragraph; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
)
PARTITION BY HASH (bookid);


ALTER TABLE public.paragraph OWNER TO postgres;

--
-- Name: TABLE paragraph; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.paragraph IS '段落';


--
-- Name: COLUMN paragraph.bookid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.paragraph.bookid IS '书籍id';


--
-- Name: COLUMN paragraph.chapterid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.paragraph.chapterid IS '章节id';


--
-- Name: COLUMN paragraph.content; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.paragraph.content IS '段落内容';


--
-- Name: COLUMN paragraph.seqid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.paragraph.seqid IS '排序id';


--
-- Name: COLUMN paragraph.createtime; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.paragraph.createtime IS '创建时间';


--
-- Name: paragraph_h00; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h00 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h00 OWNER TO postgres;

--
-- Name: paragraph_h01; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h01 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h01 OWNER TO postgres;

--
-- Name: paragraph_h02; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h02 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h02 OWNER TO postgres;

--
-- Name: paragraph_h03; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h03 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h03 OWNER TO postgres;

--
-- Name: paragraph_h04; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h04 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h04 OWNER TO postgres;

--
-- Name: paragraph_h05; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h05 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h05 OWNER TO postgres;

--
-- Name: paragraph_h06; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h06 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h06 OWNER TO postgres;

--
-- Name: paragraph_h07; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h07 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h07 OWNER TO postgres;

--
-- Name: paragraph_h08; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h08 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h08 OWNER TO postgres;

--
-- Name: paragraph_h09; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h09 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h09 OWNER TO postgres;

--
-- Name: paragraph_h10; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h10 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h10 OWNER TO postgres;

--
-- Name: paragraph_h11; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h11 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h11 OWNER TO postgres;

--
-- Name: paragraph_h12; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h12 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h12 OWNER TO postgres;

--
-- Name: paragraph_h13; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h13 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h13 OWNER TO postgres;

--
-- Name: paragraph_h14; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h14 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h14 OWNER TO postgres;

--
-- Name: paragraph_h15; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h15 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h15 OWNER TO postgres;

--
-- Name: paragraph_h16; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h16 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h16 OWNER TO postgres;

--
-- Name: paragraph_h17; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h17 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h17 OWNER TO postgres;

--
-- Name: paragraph_h18; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h18 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h18 OWNER TO postgres;

--
-- Name: paragraph_h19; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paragraph_h19 (
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL,
    content text NOT NULL,
    seqid bigint NOT NULL,
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.paragraph_h19 OWNER TO postgres;

--
-- Name: proxy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.proxy (
    proxyid integer NOT NULL,
    name character varying(64) NOT NULL,
    host character varying(64) NOT NULL,
    port integer NOT NULL,
    type integer NOT NULL
);


ALTER TABLE public.proxy OWNER TO postgres;

--
-- Name: TABLE proxy; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.proxy IS '代理';


--
-- Name: COLUMN proxy.proxyid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.proxy.proxyid IS '代理id';


--
-- Name: COLUMN proxy.name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.proxy.name IS '名称';


--
-- Name: COLUMN proxy.host; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.proxy.host IS '地址';


--
-- Name: COLUMN proxy.port; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.proxy.port IS '端口号';


--
-- Name: COLUMN proxy.type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.proxy.type IS '类型：1 socks、2 http';


--
-- Name: seq_site; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_site
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_site OWNER TO postgres;

--
-- Name: SEQUENCE seq_site; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON SEQUENCE public.seq_site IS '站点序列';


--
-- Name: seq_task; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_task
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_task OWNER TO postgres;

--
-- Name: SEQUENCE seq_task; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON SEQUENCE public.seq_task IS '任务序列';


--
-- Name: site; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.site (
    siteid bigint DEFAULT nextval('public.seq_site'::regclass) NOT NULL,
    name character varying(20) NOT NULL,
    url character varying(128),
    configid bigint,
    createtime timestamp without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);


ALTER TABLE public.site OWNER TO postgres;

--
-- Name: TABLE site; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.site IS '站点';


--
-- Name: COLUMN site.siteid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.site.siteid IS '站点id';


--
-- Name: COLUMN site.name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.site.name IS '站点名称';


--
-- Name: COLUMN site.url; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.site.url IS '站点链接';


--
-- Name: COLUMN site.configid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.site.configid IS '配置id';


--
-- Name: COLUMN site.createtime; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.site.createtime IS '创建时间';


--
-- Name: task; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.task (
    taskid bigint DEFAULT nextval('public.seq_task'::regclass) NOT NULL,
    name character varying(20) NOT NULL,
    cron character varying(255) NOT NULL,
    taskswitch integer DEFAULT 1 NOT NULL,
    classpath character varying(255) NOT NULL,
    loadmanner integer NOT NULL,
    description character varying(128)
);


ALTER TABLE public.task OWNER TO postgres;

--
-- Name: TABLE task; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.task IS '任务';


--
-- Name: COLUMN task.taskid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.task.taskid IS '任务id';


--
-- Name: COLUMN task.name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.task.name IS '任务名称';


--
-- Name: COLUMN task.cron; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.task.cron IS 'cron表达式';


--
-- Name: COLUMN task.taskswitch; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.task.taskswitch IS '任务开关：0 关闭，1 开启';


--
-- Name: COLUMN task.classpath; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.task.classpath IS '任务类路径';


--
-- Name: COLUMN task.loadmanner; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.task.loadmanner IS '类加载方式：0 反射、1 IOC容器';


--
-- Name: COLUMN task.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.task.description IS '任务描述';


--
-- Name: paragraph_h00; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h00 FOR VALUES WITH (modulus 20, remainder 0);


--
-- Name: paragraph_h01; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h01 FOR VALUES WITH (modulus 20, remainder 1);


--
-- Name: paragraph_h02; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h02 FOR VALUES WITH (modulus 20, remainder 2);


--
-- Name: paragraph_h03; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h03 FOR VALUES WITH (modulus 20, remainder 3);


--
-- Name: paragraph_h04; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h04 FOR VALUES WITH (modulus 20, remainder 4);


--
-- Name: paragraph_h05; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h05 FOR VALUES WITH (modulus 20, remainder 5);


--
-- Name: paragraph_h06; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h06 FOR VALUES WITH (modulus 20, remainder 6);


--
-- Name: paragraph_h07; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h07 FOR VALUES WITH (modulus 20, remainder 7);


--
-- Name: paragraph_h08; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h08 FOR VALUES WITH (modulus 20, remainder 8);


--
-- Name: paragraph_h09; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h09 FOR VALUES WITH (modulus 20, remainder 9);


--
-- Name: paragraph_h10; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h10 FOR VALUES WITH (modulus 20, remainder 10);


--
-- Name: paragraph_h11; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h11 FOR VALUES WITH (modulus 20, remainder 11);


--
-- Name: paragraph_h12; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h12 FOR VALUES WITH (modulus 20, remainder 12);


--
-- Name: paragraph_h13; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h13 FOR VALUES WITH (modulus 20, remainder 13);


--
-- Name: paragraph_h14; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h14 FOR VALUES WITH (modulus 20, remainder 14);


--
-- Name: paragraph_h15; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h15 FOR VALUES WITH (modulus 20, remainder 15);


--
-- Name: paragraph_h16; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h16 FOR VALUES WITH (modulus 20, remainder 16);


--
-- Name: paragraph_h17; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h17 FOR VALUES WITH (modulus 20, remainder 17);


--
-- Name: paragraph_h18; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h18 FOR VALUES WITH (modulus 20, remainder 18);


--
-- Name: paragraph_h19; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paragraph ATTACH PARTITION public.paragraph_h19 FOR VALUES WITH (modulus 20, remainder 19);


--
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book (bookid, name, url, configid, createtime, starturl, handlerinfo, siteid, taskstatus, taskswitch, detail, lastgettime) FROM stdin;
250	我的成就系统大有问题	http://www.xuesoo.com/bqg/32501/	31	2022-09-04 17:08:53	http://www.xuesoo.com/bqg/32501/4060560.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/5184006.html","proxyid":0}	111	0	0	\N	2022-09-04 17:08:56.189326
230	重生飞扬年代	http://www.biququ.com/html/81026/	30	2022-06-11 16:56:13	http://www.biququ.com/html/81026/1012821.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	77	0	0	\N	2022-06-11 16:56:16.728967
238	我们白银都是这么玩的	https://www.zssq8.com/book/61191/	25	2022-07-01 21:08:30	https://www.zssq8.com/61_61191/15328600.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	93	0	0	\N	2022-07-01 21:09:08.519056
226	星际内卷王	http://www.cits0871.com/booktxt/49770/	21	2022-05-29 14:02:19	http://www.cits0871.com/booktxt/49770/28663823.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"/29443980.html","proxyid":0}	90	0	0	\N	2022-05-29 14:03:05.909008
62	末世之黑暗召唤师	http://www.biquge.info/3_3398/	13	2020-07-18 23:14:00	http://www.biquge.info/3_3398/1809204.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0	\N	2021-10-03 10:16:08.402202
210	模拟：大国科技	https://www.vipxs.la/141_141634/	32	2022-04-16 23:30:02	https://www.vipxs.la/141_141634/48639782.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	66	0	0	\N	2022-05-06 11:16:00.014748
194	直播在荒野手搓核聚变	https://www.xbiquge.la/85/85149/	15	2022-01-25 21:49:34	https://www.xbiquge.la/85/85149/32992487.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	70	0	0	\N	2022-01-25 21:49:52.602944
225	一品丹仙	http://www.yetianlian.com/yt72934/	25	2022-05-24 20:05:42	http://www.yetianlian.com/yt72934/27207352.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"/30681060.html","proxyid":0}	103	0	0	\N	2022-05-24 20:05:44.481149
251	苟在女魔头身边偷偷修炼	https://www.zssq8.com/book/54418/	25	2022-09-05 19:28:09	https://www.zssq8.com/54_54418/13987493.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	93	0	0	\N	2022-09-05 19:28:14.54544
219	这个网游策划果然有问题	https://www.vipxs.la/141_141595/	32	2022-05-02 09:35:14	https://www.vipxs.la/141_141595/48625623.html	{"titleType":1,"startIndex":2,"delimiter":"、","endType":1,"endCharacter":"/50951654.html","proxyid":0}	66	0	0	\N	2022-07-09 08:11:47.098376
228	高天之上	https://www.ixs.la/ks102013/	8	2022-06-04 10:35:13	https://www.ixs.la/ks102013/37346637.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/38730270.html","proxyid":0}	104	0	0	\N	2022-06-06 22:16:00.014169
229	我在修仙界长生不死	https://www.ixs.la/ks103786/	8	2022-06-06 23:19:48	https://www.ixs.la/ks103786/37759092.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/38761257.html","proxyid":0}	104	0	0	\N	2022-06-06 23:20:17.071502
252	我在末世种个田	https://www.xhsxsw.com/book/wozaimoshizhonggetian/	30	2022-09-11 17:35:56	https://www.xhsxsw.com/html/107274/1225981.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	112	0	0	\N	2022-09-11 17:35:59.099887
224	大时代从1983开始	http://www.yetianlian.com/yt77942/	25	2022-05-22 11:35:04	http://www.yetianlian.com/yt77942/29222278.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"/30652188.html","proxyid":0}	103	0	0	\N	2022-05-22 11:37:04.887192
231	我写的自传不可能是悲剧	https://www.xbiquge.la/96/96793/	15	2022-06-12 22:37:44	https://www.xbiquge.la/96/96793/36935069.html	{"titleType":-1,"startIndex":3,"delimiter":"","endType":0,"endCharacter":"/38614639.html","proxyid":0}	70	0	0	\N	2022-06-12 22:37:53.339713
236	学霸之巅	https://www.asxs.com/view/179371/	40	2022-06-24 01:40:37	https://www.asxs.com/view/179371/2391611.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	106	0	0	\N	2022-07-09 20:43:25.97209
223	李治你别怂	https://www.shuquge.com/txt/157332/index.html	25	2022-05-14 01:24:09	https://www.shuquge.com/txt/157332/43698768.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/index.html","proxyid":0}	102	0	0	\N	2022-05-14 01:24:34.17847
239	我在异界肝经验	https://www.ddxs.cc/ddxs/194155/	21	2022-07-03 16:01:09	https://www.ddxs.cc/ddxs/194155/28668350.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/29503751.html","proxyid":0}	87	0	0	\N	2022-07-03 16:01:11.936632
244	大明皇长孙	https://www.23xstxt.com/book/70531/70531977/	25	2022-07-24 20:42:54	https://www.23xstxt.com/book/70531/70531977/660728556.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/695947177.html","proxyid":0}	88	0	0	\N	2022-07-24 20:49:38.06511
241	苟在仙界成大佬	https://www.xbiquge.la/100/100608/	15	2022-07-06 19:09:36	https://www.xbiquge.la/100/100608/38089673.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/39063943.html","proxyid":0}	70	0	0	\N	2022-07-06 19:09:47.229037
245	我伪装成了美少女的第二人格	https://www.zwydw.com/book/75/75156/	25	2022-07-28 20:46:35	https://www.zwydw.com/book/75/75156/40636718.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	108	0	0	\N	2022-07-28 20:47:12.781012
233	星环使命	https://www.biqugetv.com/91_91179/	32	2022-06-18 09:53:23	https://www.biqugetv.com/91_91179/10325903.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	36	0	0	\N	2022-06-18 22:19:34.741746
235	星环使命	https://www.asxs.com/view/161713/	40	2022-06-18 22:29:03	https://www.asxs.com/view/161713/2156171.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	106	0	0	\N	2022-06-18 22:29:40.150985
249	从大学讲师到首席院士	https://www.x81xzw.com/60_60333/	21	2022-08-13 11:20:28	https://www.x81xzw.com/60_60333/21755870.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/22499080.html","proxyid":0}	110	0	0	\N	2022-08-13 12:32:42.867088
247	重生在2004年开始	https://www.xbooktxt.net/88_88338/	42	2022-07-30 16:12:22	https://www.xbooktxt.net/88_88338/732781793.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	109	0	0	\N	2022-07-30 16:12:54.624639
179	学霸的未来时代	https://www.xbiquge.la/84/84087/	15	2021-11-21 19:37:55	https://www.xbiquge.la/84/84087/32592628.html	{"titleType":-1,"startIndex":3,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	70	0	0	燕槐山 |科幻|连载\n最新更新 第四百九十八章 上门·2022-01-18\nhttps://book.qidian.com/info/1028410183/	2022-09-24 15:16:00.015198
182	我绑架了时间线	http://www.xpbtxt.com/pbtxt254026/	8	2021-11-30 21:09:49	http://www.xpbtxt.com/pbtxt254026/1280323.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	78	0	0	一刀斩斩斩 |科幻|连载\n最新更新 第302章 抄作业，谁还不会呢！·17小时前\nhttps://book.qidian.com/info/1027606717/	2022-09-24 16:16:00.065282
242	我的属性修行人生	https://www.yqzww.cc/book_95498/	41	2022-07-17 10:25:31	https://www.yqzww.cc/book_95498/32508655.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	107	0	0	\N	2022-07-17 10:25:37.59917
198	脑海带着一扇门	https://www.biqusa.com/139_139829/	15	2022-02-06 22:24:33	https://www.biqusa.com/139_139829/53374663.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	79	0	0	\N	2022-09-24 16:16:00.115396
38	我的代码分身	https://www.biquge.tw/522_522678/	8	2020-05-10 15:55:23	https://www.biquge.tw/522_522678/2660223.html	{"titleType":1,"startIndex":0,"delimiter":" ","endType":0,"endCharacter":""}	10	0	0	\N	2021-10-03 10:16:08.402202
217	我的人生可以无限模拟	https://www.vipxs.la/141_141336/	32	2022-04-24 21:29:52	https://www.vipxs.la/141_141336/48561921.html	{"titleType":0,"startIndex":2,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	66	0	0	\N	2022-09-24 16:16:00.083983
146	请不要打扰我修仙	https://www.miaojiang8.net/13_13892/	8	2021-06-22 09:00:46	https://www.miaojiang8.net/13_13892/4736603.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":1}	58	0	0	山川不念 |仙侠|完结\n最新更新 第661章 结束的开始（大结局）·7小时前\nhttps://book.qidian.com/info/1026067082/	2021-12-17 13:16:03.554557
205	科技霸主从带娃开始	http://www.cits0871.com/booktxt/48942/	21	2022-03-24 00:01:24	http://www.cits0871.com/booktxt/48942/28229015.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/29537236.html","proxyid":0}	90	0	0	\N	2022-06-14 20:57:24.983436
61	太乙	http://www.biquge.info/2_2801	13	2020-07-15 21:23:53	http://www.biquge.info/2_2801/9468276.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0	雾外江山 |仙侠|连载\n最新更新 第五百六十五章 分赃归位，修炼摩柯·13小时前\nhttps://book.qidian.com/info/1021283704/	2022-09-24 16:16:00.110871
124	我能升级避难所	http://www.bswtan.com/71/71433/	15	2021-03-06 12:10:17	http://www.bswtan.com/71/71433/20074055.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	50	0	0	十七筝 |科幻|完结\n最新更新 第七百六十九章 大结局·11小时前\nhttps://book.qidian.com/info/1023505347/	2021-10-20 08:16:00.012252
155	超级母舰	https://www.biquwx.la/59_59103/	13	2021-08-11 20:23:40	https://www.biquwx.la/59_59103/9124360.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	67	0	0	空长青 |科幻|完结\n最新更新 第八百八十二章 最浪漫的结局·昨日01:41\nhttps://book.qidian.com/info/1011126016/	2021-12-06 07:16:01.540112
122	这个诅咒太棒了	http://www.loubiqu.com/100_100131/	13	2021-02-24 20:21:00	http://www.loubiqu.com/100_100131/22688280.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	49	0	0	行者有三 |都市|连载\n最新更新 第六十四章 引动群潮（中）·2022-01-14\nhttps://book.qidian.com/info/1024722226/	2022-09-24 15:16:00.095413
253	我真不想靠反串出名啊	https://www.zssq8.com/book/53372/	25	2022-09-17 17:08:14	https://www.zssq8.com/53_53372/13126905.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	93	0	0	\N	2022-09-17 17:08:18.12524
129	基因大时代	https://www.biquge.info/94_94462	13	2021-03-27 11:49:29	https://www.biquge.info/94_94462/20678990.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	14	0	0	猪三不 |科幻|连载\n最新更新 第1021章 蠢货的真正含义（求订阅）·13小时前\nhttps://book.qidian.com/info/1023212048/	2022-09-24 15:16:00.070972
214	盛唐大公主	https://www.vipxs.la/137_137900/	32	2022-04-22 19:41:43	https://www.vipxs.la/137_137900/47077318.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/51466047.html","proxyid":0}	66	0	0	\N	2022-08-07 23:09:30.11622
204	修真从穿越三年开始	https://www.zssq8.com/book/51155/	25	2022-03-13 22:38:51	https://www.zssq8.com/51_51155/11685767.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	93	0	0	\N	2022-07-01 20:17:10.891545
211	模拟：大国科技	https://m.vipkanshu.vip/shu/36354/	38	2022-04-17 00:22:34	https://www.vipkanshu.vip/shu/36354/15648226.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	97	0	0	\N	2022-05-06 09:16:03.279929
240	我在异界肝经验	https://www.zssq8.com/book/65135/	25	2022-07-03 19:16:26	https://www.zssq8.com/65_65135/15747296.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	93	0	0	\N	2022-07-03 19:16:28.322096
212	模拟：大国科技	https://www.tingshuge.com/book/51613.html	33	2022-04-17 00:38:37	https://www.tingshuge.com/book/51613/24232160.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/51613.html","proxyid":0}	98	0	0	\N	2022-09-24 16:16:04.530271
171	女装走上人生巅峰	https://www.xxyanqing.com/book/29739058/	25	2021-10-27 19:45:39	https://www.xxyanqing.com/book/29739058/629395523.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	76	0	0	作家情人 |都市|连载\n最新更新 第二百一十二章：【钢铁直男3.0】·6分钟前\nhttps://book.qidian.com/info/1029797914/	2022-09-24 16:16:02.807682
112	天幕之下	http://www.biquge.se/56271	15	2021-01-21 12:23:32	http://www.biquge.se/56271/70210910.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	34	0	0	黑暗风 |轻小说|连载\n最新更新 第三百零五章 暂居神庭·2021-01-21\nhttps://book.qidian.com/info/1021010656/	2022-09-24 16:16:02.563276
136	绿龙博士	https://www.biquzhh.com/6_6997/	25	2021-05-09 18:56:07	https://www.biquzhh.com/6_6997/552944321.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	55	0	0	无物 |奇幻|连载\n最新更新 第306章 应对（来晚了）·12小时前\nhttps://book.qidian.com/info/1021420638/	2022-09-24 16:16:03.163392
199	变成人鱼该如何是好	http://www.81zw.me/book/357245/	8	2022-02-06 22:46:51	http://www.81zw.me/book/357245/1827183.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	84	0	0	\N	2022-09-24 16:16:04.963631
183	仙界归来的黑科技	http://www.xpbtxt.com/pbtxt228270/	8	2021-12-06 23:46:15	http://www.xpbtxt.com/pbtxt228270/1159169.html	{"titleType":2,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	78	0	0	缘若水 |科幻|连载\n最新更新 二三三 公布·2022-01-17\nhttps://book.qidian.com/info/1025767871/	2022-09-24 16:16:02.218674
206	深空彼岸	https://www.dingdian6.com/book6/	36	2022-03-27 15:40:46	https://www.dingdian6.com/book6/667.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	94	0	0	\N	2022-09-24 16:16:00.940827
227	召唤师：我能萌化一切	http://www.cits0871.com/booktxt/52315/	21	2022-05-31 20:44:09	http://www.cits0871.com/booktxt/52315/29255775.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	90	0	0	\N	2022-05-31 20:44:22.420396
43	科技巫师	https://www.biquge.lu/book/44014/	5	2020-05-30 00:10:09	https://www.biquge.lu/book/44014/16591510.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	2	0	0	\N	2021-10-03 10:16:08.402202
68	绝世人妖养成系统	http://www.biquge.info/12_12112	13	2020-07-31 00:10:16	http://www.biquge.info/12_12112/5442063.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0	\N	2021-10-03 10:16:08.402202
32	给女装大佬递茶	http://www.biquger.com/biquge/109593	14	2020-04-20 22:19:54	http://www.biquger.com/biquge/109593/41361767	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/"}	18	0	0	\N	2021-10-03 10:16:08.402202
248	修仙：我有一个梦境空间	https://www.yqxs.cc/html/27518/27518170/index.html	25	2022-08-02 00:08:00	https://www.yqxs.cc/html/27518/27518170/704428582.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/723436111.html","proxyid":0}	91	0	0	\N	2022-09-24 16:16:03.174333
186	废土求生我有戴森球	http://www.81zw.me/book/343558/	8	2021-12-21 19:48:01	http://www.81zw.me/book/343558/1751207.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	84	0	0	潇潇的小白 |科幻|连载\n最新更新 第三百三十三章 全员升级完成·2022-01-18\nhttps://book.qidian.com/info/1028314686/	2022-09-24 16:16:01.550493
177	西游：开局绑定白骨精	http://www.xpbtxt.com/pbtxt242862/	8	2021-11-18 23:41:36	http://www.xpbtxt.com/pbtxt242862/1241915.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	78	0	0	小仙大神 |玄幻|连载\n最新更新 第二十二章：务实的白骨·24小时前\nhttps://book.qidian.com/info/1027222531/	2022-09-24 16:16:01.218961
220	开局账号被盗，反手充值一百万	https://www.qingkanshu.cc/57_57708/	33	2022-05-03 11:13:31	https://www.qingkanshu.cc/57_57708/33961642.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	101	0	0	\N	2022-09-24 16:16:02.829911
168	这位斩妖师也太娘了叭	https://www.dengbi.com/18/18805/	35	2021-10-08 19:58:08	https://www.dengbi.com/18/18805/12277710.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/0.html","proxyid":0}	73	0	0	爱饮特仑苏嗷 |仙侠|连载\n最新更新 第三百五十章 先帝之死·16小时前\nhttps://book.qidian.com/info/1025792706/	2022-09-24 16:16:00.807439
104	这个法师很危险	https://www.xsbiquge.com/96_96947	16	2020-12-16 21:29:30	https://www.xsbiquge.com/96_96947/217607.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	39	0	0	阁下贵姓 |奇幻|连载\n最新更新 第268章矮人城市·2021-08-15\nhttps://book.qidian.com/info/1020202542/	2022-09-24 16:16:06.663761
208	公子别秀	https://www.vipxs.la/63_63683/	32	2022-04-05 09:15:16	https://www.vipxs.la/63_63683/20083084.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	66	0	0	\N	2022-09-24 16:16:01.818659
200	从笑傲江湖开始横推武道	http://www.cits0871.com/booktxt/49410/	21	2022-02-11 09:09:22	http://www.cits0871.com/booktxt/49410/28470492.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	90	0	0	\N	2022-09-24 16:16:02.463364
82	我若修仙法力齐天	http://www.biquge.info/91_91040	13	2020-10-26 12:29:31	http://www.biquge.info/91_91040/19652865.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0	一只辣椒精 |游戏|连载\n最新更新 第一千七百八十三章 玉帝与元始（求订阅）·5小时前\nhttps://book.qidian.com/info/1022028160/	2022-09-24 16:16:05.941349
254	退下，让朕来	https://www.xsyq.cc/html/63979/63979675/index.html	25	2022-09-24 18:23:47	https://www.xsyq.cc/html/63979/63979675/662040310.html	{"titleType":1,"startIndex":0,"delimiter":"：","endType":1,"endCharacter":"/736358422.html","proxyid":0}	113	0	0	\N	2022-09-24 18:29:24.959708
256	从一气决开始肝进度	https://www.vipxs.la/151_151228/	32	2022-10-09 19:20:30	https://www.vipxs.la/151_151228/51742374.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/52428603.html","proxyid":0}	66	0	0	\N	2022-10-09 19:28:19.118679
221	百世换新天	https://www.zssq8.com/book/60919/	25	2022-05-06 12:29:40	https://www.zssq8.com/60_60919/15298939.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	93	0	0	\N	2022-07-01 20:19:35.900074
207	夜的命名术	http://www.maydayfans.com/9478/	37	2022-04-03 18:15:00	http://www.maydayfans.com/9478/tc_128827.html	{"titleType":1,"startIndex":5,"delimiter":"、","endType":1,"endCharacter":"readend.html","proxyid":0}	95	0	0	\N	2022-09-24 16:16:32.044998
213	我在坟场画皮十五年	https://www.tmwx.net/49_49099/	33	2022-04-18 09:53:54	https://www.tmwx.net/49_49099/29722710.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	99	0	0	\N	2022-09-24 16:16:38.034696
152	变身之女神养成计划	https://www.biquger.com/biquge/78486/	14	2021-08-07 13:11:38	https://www.biquger.com/biquge/78486/22455661	{"titleType":1,"startIndex":0,"delimiter":".","endType":1,"endCharacter":"/","proxyid":0}	18	0	0	\N	2021-10-03 10:16:08.402202
157	 一人之我变成了老天师的师姐	http://www.qiushuge.net/renzhiwobianchenglelaotianshideshijie/	33	2021-08-22 21:22:18	http://www.qiushuge.net/renzhiwobianchenglelaotianshideshijie/5477014/	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"shijie/","proxyid":0}	69	0	0	\N	2022-09-24 16:16:39.123756
176	我居然有一半的时间要变成女生	https://www.7722.org/html/9223/	25	2021-11-14 16:36:40	https://www.7722.org/html/9223/635706837.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	92	0	0	橖唐 |轻小说|连载\n最新更新 第三百九十章：超级冷知识·10小时前\nhttps://book.qidian.com/info/1025764162/	2022-09-24 16:16:35.767733
7	沧元图	https://www.biquge.lu/book/49717/	5	2020-04-06 19:50:53	https://www.biquge.lu/book/49717/525134674.html	{"titleType":-1,"startIndex":0,"delimiter":"章 ","endType":0,"endCharacter":""}	2	0	0	\N	2021-10-03 10:16:08.402202
190	炼金术士的异界日常	https://www.xbiquge.la/85/85113/	15	2022-01-11 19:17:51	https://www.xbiquge.la/85/85113/32975872.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	70	0	0	书虫冒泡 |奇幻|连载\n最新更新 第168章 你还敢解封吗？·16小时前\nhttps://book.qidian.com/info/1026363869/	2022-09-24 16:16:32.489468
165	家族修仙：我的悟性能储存	https://www.biquges.com/83_83015/	15	2021-10-01 22:20:47	https://www.biquges.com/83_83015/35431632.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"index.html","proxyid":0}	29	0	0	小白变老白 |仙侠|连载\n最新更新 第290章 请道友再考虑一下·15小时前\nhttps://book.qidian.com/info/1029961015/	2022-09-24 16:16:34.900954
196	武装魔女	https://www.23xstxt.com/book/66610/66610895/	25	2022-02-06 22:23:03	https://www.23xstxt.com/book/66610/66610895/663405392.html	{"titleType":1,"startIndex":0,"delimiter":".","endType":0,"endCharacter":"","proxyid":0}	88	0	0	\N	2022-09-24 16:16:50.547975
174	在第四天灾中幸存	http://www.xpbtxt.com/pbtxt220426/	8	2021-11-12 22:17:10	http://www.xpbtxt.com/pbtxt220426/1128792.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	78	0	0	纳西利亚 |游戏|连载\n最新更新 第六百六十九章 谁是第一位？·2分钟前\nhttps://book.qidian.com/info/1025043022/	2022-09-24 16:16:42.102232
127	朕又不想当皇帝	http://www.biquge.info/94_94886/	13	2021-03-18 22:01:14	http://www.biquge.info/94_94886/20813879.html	{"titleType":1,"startIndex":0,"delimiter":"、","endType":0,"endCharacter":"","proxyid":0}	14	0	0	争斤论两花花帽 |历史|连载\n最新更新 490、孝敬·2022-01-17\nhttps://book.qidian.com/info/1023539783/	2022-09-24 16:16:40.579752
195	直播在荒野手搓核聚变	https://www.ddxs.cc/ddxs/189838/	21	2022-01-25 21:57:05	https://www.ddxs.cc/ddxs/189838/26866737.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	87	0	0	\N	2022-09-24 16:16:08.441825
243	从学霸开始打造黑科技帝国	https://www.7722.org/html/10048/	25	2022-07-21 20:08:39	https://www.7722.org/html/10048/712605715.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	92	0	0	\N	2022-07-21 20:09:01.136913
222	重生2008：我阅读能赚钱	https://www.vipxs.la/141_141669/	32	2022-05-08 23:24:32	https://www.vipxs.la/141_141669/48648826.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	66	0	0	\N	2022-09-24 16:16:41.257647
143	迷雾纪元	https://www.ddxstxt8.com/11_11905	19	2021-06-01 21:17:12	https://www.ddxstxt8.com/11_11905/5309748.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	53	0	0	寿限无 |科幻|连载\n最新更新 第1213章 战场，我来了！·16小时前\nhttps://book.qidian.com/info/1013961026/	2022-09-24 16:16:07.519432
202	女主从书里跑出来了怎么办	http://www.81zw.me/book/362029/	8	2022-02-24 21:35:37	http://www.81zw.me/book/362029/1822822.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	84	0	0	\N	2022-09-24 16:16:33.489697
237	开局一间铁匠铺	https://www.x23us.us/128_128072/	13	2022-06-27 00:04:00	https://www.x23us.us/128_128072/47410824.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/48935171.html","proxyid":0}	100	0	0	\N	2022-06-27 00:04:07.513324
255	宅魔女	https://www.shuquge.com/txt/157931/index.html	25	2022-09-24 18:42:35	https://www.shuquge.com/txt/157931/43892928.html	{"titleType":1,"startIndex":0,"delimiter":".","endType":1,"endCharacter":"/47386126.html","proxyid":0}	102	0	0	\N	2022-09-24 18:43:18.391153
257	重回七七种田养娃	https://www.ixsw.la/ks109127	16	2022-10-17 00:36:38	https://www.ixsw.la/ks109127/39229895.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/40796020.html","proxyid":0}	115	0	0	\N	2022-10-17 05:16:00.060713
131	原始文明成长记	https://www.ddxstxt8.com/12_12107	19	2021-04-13 21:22:14	https://www.ddxstxt8.com/12_12107/461786776.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	53	0	0	羽卿书 |奇幻|完结\n最新更新 第1150章 这盛世，如我所愿！！！（大结局）·昨日06:55\nhttps://book.qidian.com/info/1013867997	2021-10-03 10:16:08.402202
149	我的生物黑科技	http://www.b5200.net/144_144173/	21	2021-07-07 21:26:10	http://www.b5200.net/144_144173/176529969.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	62	0	0	麻辣烫专业户 |科幻|完结\n最新更新 完本感言·14小时前\nhttps://book.qidian.com/info/1024723088/	2021-12-01 08:16:00.479644
147	平常人类的平凡生活	https://www.shuge9.com/read/10/10759/	15	2021-06-27 13:36:33	https://www.shuge9.com/read/10/10759/4402913.html	{"titleType":-1,"startIndex":12,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	60	0	0	何处可桃 |都市|连载\n最新更新 第1081章 好笑的婉婉·13小时前\nhttps://book.qidian.com/info/1023473373/	2022-09-24 16:16:45.02476
138	我的帝国	https://www.ddxstxt8.com/2_12584/	19	2021-05-14 09:59:21	https://www.ddxstxt8.com/2_12584/450059799.html	{"titleType":2,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	53	0	0	龙灵骑士 |军事|完结\n最新更新 完结·昨日00:18\nhttps://book.qidian.com/info/1015009905/	2021-10-13 10:16:03.340908
187	重生幕后贵族法师	https://www.xbiquge.la/83/83958/	15	2021-12-25 13:46:23	https://www.xbiquge.la/83/83958/32554312.html	{"titleType":-1,"startIndex":4,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	70	0	0	超大只核平鸽 |游戏|连载\n最新更新 第二十二章 遗迹内的危机·12小时前\nhttps://book.qidian.com/info/1025328610/	2022-09-24 16:16:44.102524
148	我有一群地球玩家	https://www.xbiquwx.la/70_70258/	13	2021-07-01 13:00:44	https://www.xbiquwx.la/70_70258/13224829.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	61	0	0	第七个魔方 |游戏|完结\n最新更新 对观众姥爷们的话·2021-12-04\nhttps://book.qidian.com/info/1015778716/	2021-12-06 09:16:00.572384
232	反派boss：从东方不败开始	https://www.yqxs.cc/html/55996/55996206/index.html	25	2022-06-14 21:06:12	https://www.yqxs.cc/html/55996/55996206/688311492.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/index.html","proxyid":0}	91	0	0	\N	2022-09-24 16:16:45.758279
76	不正常的超凡世界	http://www.biquge.info/69_69817	13	2020-09-20 14:53:07	http://www.biquge.info/69_69817/13119688.html	{"titleType":1,"startIndex":0,"delimiter":"章 ","endType":0,"endCharacter":""}	14	0	0	\N	2021-10-03 10:16:08.402202
154	变身之我的系统有毒	https://www.biquwx.la/68_68986/	13	2021-08-10 08:50:05	https://www.biquwx.la/68_68986/12812533.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	67	0	0	\N	2022-09-24 16:16:45.947157
173	我只想安静的做个苟道中人	http://www.xpbtxt.com/pbtxt238176/	8	2021-11-08 20:02:41	http://www.xpbtxt.com/pbtxt238176/1204713.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	78	0	0	爆炸小拿铁 |仙侠|连载\n最新更新 第一百六十八章：敲棺声。（第二更！求订阅！）·13小时前\nhttps://book.qidian.com/info/1026694115/	2022-09-24 16:16:45.013752
133	何日请长缨	https://www.biquwx.la/74_74640	13	2021-04-26 18:52:47	https://www.biquwx.la/74_74640/14646141.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	14	0	0	齐橙 |都市|完结\n最新更新 第五百七十五章 长缨在手（大结局）·2021-09-16\nhttps://book.qidian.com/info/1016262041	2021-10-03 10:16:08.402202
67	变身大小姐	http://www.biquger.com/biquge/92056	14	2020-07-30 23:41:49	http://www.biquger.com/biquge/92056/25488105	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/"}	18	0	0	\N	2021-10-03 10:16:08.402202
60	魔幻科技工业	https://www.boquge.com/book/121068/	20	2020-07-12 20:42:51	https://www.boquge.com/book/121068/169871249.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	25	0	0	\N	2021-10-03 10:16:08.402202
142	戈壁滩上的黑科技大佬	https://www.biquger.com/biquge/138984/	14	2021-05-30 08:35:20	https://www.biquger.com/biquge/138984/55908113	{"titleType":-1,"startIndex":4,"delimiter":"","endType":1,"endCharacter":"138984/","proxyid":0}	18	0	0	麒麟2 |都市|连载\n最新更新 第二百三十八章 我这就给上头打报告去·2021-07-07\nhttps://book.qidian.com/info/1025174859/	2022-09-24 16:16:43.780255
56	仙界科技	http://www.biquge.info/40_40201/	13	2020-06-13 17:29:15	http://www.biquge.info/40_40201/1166140.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0	\N	2021-10-03 10:16:08.402202
98	从零开始	https://www.xsbiquge.com/0_114/	16	2020-11-27 19:33:49	https://www.xsbiquge.com/0_114/5824807.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	39	0	0	\N	2021-10-03 10:16:08.402202
203	我的成神日志	https://www.yqxs.cc/html/43800/43800173/index.html	25	2022-02-27 18:55:27	https://www.yqxs.cc/html/43800/43800173/685606702.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/723960520.html","proxyid":0}	91	0	0	\N	2022-09-24 16:16:43.802436
209	不科学御兽	https://www.biqugevip.com/61_61911/	15	2022-04-10 22:39:08	https://www.biqugevip.com/61_61911/23449185.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	96	0	0	\N	2022-09-24 16:16:46.42499
259	修仙就是这么科学	https://www.ixsw.la/ks106595/	16	2022-10-20 20:13:23	https://www.ixsw.la/ks106595/38528605.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/40875446.html","proxyid":0}	115	0	0	\N	2022-10-20 20:14:03.918153
258	修仙就是这么科学	https://www.xdingdianxsw.com/68/68914/	43	2022-10-20 20:02:16	https://www.xdingdianxsw.com/68/68914/34548696.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	116	0	0	\N	2022-10-20 20:07:41.494544
261	我的生活有旁白	https://www.ixsw.la/ks112007/	16	2022-10-24 20:31:26	https://www.ixsw.la/ks112007/40156535.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/40916493.html","proxyid":0}	115	0	0	\N	2022-10-24 20:31:34.095203
262	最长一梦	https://www.ishuquge.com/txt/159662/index.html	25	2022-10-26 20:36:13	https://www.ishuquge.com/txt/159662/44391935.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/47814837.html","proxyid":0}	117	0	0	\N	2022-10-26 20:36:21.350442
189	医路坦途	https://www.zhhbqg.com/19_19366/	25	2022-01-01 09:19:40	https://www.zhhbqg.com/19_19366/426670197.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	86	0	0	臧福生 |都市|连载\n最新更新 885 茶素拦路党·18小时前\nhttps://book.qidian.com/info/1013477299/	2022-09-24 16:16:50.025501
126	码农修真	https://www.shuge.la/read/9/9603/	15	2021-03-13 00:03:33	https://www.shuge.la/read/9/9603/4003818.html	{"titleType":-1,"startIndex":7,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	52	0	0	维度论 |科幻|连载\n最新更新 说明·2021-12-25\nhttps://book.qidian.com/info/1023255964/	2022-09-24 16:16:52.570453
100	不会真有人觉得修仙难吧	http://www.biquge.info/93_93131	13	2020-12-07 22:40:50	http://www.biquge.info/93_93131/20270178.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0	\N	2021-10-03 10:16:08.402202
145	我就是不按套路出牌	https://www.jipinxs.com/39_39098	8	2021-06-20 08:31:03	https://www.jipinxs.com/39_39098/564568760.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	59	0	0	百分之七 |轻小说|连载\n最新更新 请个假·14小时前\nhttps://book.qidian.com/info/1021578188/	2022-09-24 16:17:33.754277
151	奥术之主	https://www.xbiqupao.com/book/69664	30	2021-07-24 16:12:15	https://www.xbiqupao.com/book/69664/7107788.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"no_more.html","proxyid":0}	63	0	0	姑苏献芹人 |奇幻|连载\n最新更新 第1618章 返回、二代量产·13小时前\nhttps://book.qidian.com/info/1016794204/	2022-09-24 16:16:53.23726
193	修仙从第三次转生开始	https://www.xbiquge.la/91/91132/	15	2022-01-17 12:14:56	https://www.xbiquge.la/91/91132/34829016.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	70	0	0	何故笑 |仙侠|连载\n最新更新 第二百零六章 故人·3分钟前\nhttps://book.qidian.com/info/1031076481/	2022-09-24 16:16:56.381985
175	重生科技学霸	http://www.xpbtxt.com/pbtxt266215/	8	2021-11-14 16:34:21	http://www.xpbtxt.com/pbtxt266215/1347184.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	78	0	0	疯子C |科幻|连载\n最新更新 第五百五十一章载人登月（5）（求订阅）·5小时前\nhttps://book.qidian.com/info/1030276419/	2022-09-24 16:17:33.39855
84	人在大唐已被退学	http://www.biquge.info/92_92784	13	2020-10-29 20:16:47	http://www.biquge.info/92_92784/20156879.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0	张围 |历史|连载\n最新更新 第八百四十九章 李孝恭辞官·18小时前\nhttps://book.qidian.com/info/1022476878/	2022-09-24 16:16:49.325468
170	莫求仙缘	https://www.sobiquge.com/book/51781/	8	2021-10-16 20:17:58	https://www.sobiquge.com/book/51781/536778.html	{"titleType":2,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	75	0	0	蒙面怪客 |仙侠|连载\n最新更新 784 元婴中期·2小时前\nhttps://book.qidian.com/info/1025282227/	2022-09-24 16:16:47.914248
89	我修仙有提示语	http://www.biquger.com/biquge	14	2020-11-13 19:20:51	http://www.biquger.com/biquge/131966/53626043	{"titleType":1,"startIndex":5,"delimiter":"章 ","endType":1,"endCharacter":"/"}	18	0	0	莫听林声 |仙侠|连载\n最新更新 第534章 月湖·2021-04-04\nhttps://book.qidian.com/info/1022874353/	2022-09-24 16:16:50.003365
185	科技：打破垄断全球的霸权	http://www.81zw.me/book/338618/	8	2021-12-19 14:48:40	http://www.81zw.me/book/338618/1711667.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	83	0	0	女术士之友 |科幻|连载\n最新更新 第四百三十五章：顾青懂个锤子技术·13小时前\nhttps://book.qidian.com/info/1028365974/	2022-09-24 16:16:46.591681
188	重生幕后贵族法师	https://www.23xsw.cc/book/72409/72409770/	25	2021-12-25 14:07:02	https://www.23xsw.cc/book/72409/72409770/648582139.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	85	0	0	超大只核平鸽 |游戏|连载\n最新更新 第二十二章 遗迹内的危机·12小时前\nhttps://book.qidian.com/info/1025328610/	2022-09-24 16:16:57.371051
49	妞非在下	http://www.biquge.info/3_3320/	13	2020-06-02 23:47:06	http://www.biquge.info/3_3320/1763403.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0	\N	2021-10-03 10:16:08.402202
159	学霸跑偏了	http://www.b5200.net/146_146961/	21	2021-08-31 21:33:04	http://www.b5200.net/146_146961/178926538.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	62	0	0	起开让朕先跑 |都市|完结\n最新更新 最后一章屏蔽·昨日15:23\nhttps://book.qidian.com/info/1025767761/	2021-11-09 17:16:00.374271
94	东京绅士物语	http://www.biquge.info/8_8655	13	2020-11-21 13:46:54	http://www.biquge.info/8_8655/4152164.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0	黑暗风 |都市|连载\n最新更新 第一千八百八十六章 新年快乐～·2022-01-01\nhttps://book.qidian.com/info/3488233/	2022-09-24 16:17:32.920766
115	变身之洛神	https://m.biquge5200.com/wapbook-94859/	27	2021-01-26 22:25:32	https://m.biquge5200.com/wapbook-94859-153962049/	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"94859/","proxyid":0}	46	0	0	\N	2021-10-03 10:16:08.402202
158	我有一个武道世界	https://www.xbiquge.la/80/80212/	15	2021-08-25 22:34:18	https://www.xbiquge.la/80/80212/31289590.html	{"titleType":1,"startIndex":4,"delimiter":".","endType":0,"endCharacter":"","proxyid":0}	70	0	0	俺来组成头部 |诸天无限|连载\n最新更新 656.圣女贞德·13小时前\nhttps://book.qidian.com/info/1027239049/	2022-09-24 16:18:15.860052
197	超神制卡师	https://www.lstxt.cc/books/24/24184/455942108.html	25	2022-02-06 22:23:47	https://www.lstxt.cc/books/24/24184/416317043.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/24184.html","proxyid":0}	89	0	0	\N	2022-09-24 16:18:34.184716
201	修仙：从就职德鲁伊开始	http://www.81zw.me/book/362125/	8	2022-02-13 23:20:44	http://www.81zw.me/book/362125/1830196.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	84	0	0	\N	2022-09-24 16:18:16.871324
114	非常大小姐	http://www.biquger.com/biquge/111236/	14	2021-01-25 15:16:54	http://www.biquger.com/biquge/111236/44859574	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/","proxyid":0}	18	0	0	永恒炽天使 |轻小说|连载\n最新更新 第四百一十八章 秒怂·2021-05-23\nhttps://book.qidian.com/info/1014930805/	2022-09-24 16:18:15.837903
160	解构诡异	https://www.biquges.com/78_78012/	15	2021-09-06 22:34:23	https://www.biquges.com/78_78012/33689537.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	29	0	0	懒惰的秀某人 |悬疑|连载\n最新更新 第571章 误会了·21小时前\nhttps://book.qidian.com/info/1028214828/	2022-09-24 16:18:18.383313
118	大唐第一世家	https://www.lvsetxt.cc/books/37/37499	25	2021-02-08 14:33:15	https://www.lvsetxt.cc/books/37/37499/536444888.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"37499.html","proxyid":0}	42	0	0	晴了 |历史|连载\n最新更新 第2218章 夫君，去见我爹娘你怎么比我还开心？（求订阅求票票）·11小时前\nhttps://book.qidian.com/info/1022885215/	2022-09-24 16:18:14.982164
164	联盟语音包，扮演角色就变强	https://www.biquges.com/82_82637/	15	2021-09-26 12:40:24	https://www.biquges.com/82_82637/35285360.html	{"titleType":1,"startIndex":0,"delimiter":".","endType":1,"endCharacter":"index.html","proxyid":0}	29	0	0	诵书zxy |游戏|完结\n最新更新 番外·9小时前\nhttps://book.qidian.com/info/1029692317/	2021-12-01 09:16:01.18073
167	这位斩妖师也太娘了叭	https://www.ibswtan.com/81/81301/	15	2021-10-06 21:13:39	https://www.ibswtan.com/81/81301/23541328.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	68	0	0	爱饮特仑苏嗷 |仙侠|连载\n最新更新 第三百五十章 先帝之死·16小时前\nhttps://book.qidian.com/info/1025792706/	2022-09-24 16:18:16.849164
172	朕	http://www.biququ.com/html/50155/	30	2021-11-01 19:30:14	http://www.biququ.com/html/50155/626931.html	{"titleType":2,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	77	0	0	王梓钧 |历史|连载\n最新更新 555【其徐如林，其疾如风】·12小时前\nhttps://book.qidian.com/info/1028520538/	2022-09-24 16:17:39.510648
64	奥术之主	https://www.78zw.com/31_31962/	15	2020-07-23 22:24:55	https://www.78zw.com/31_31962/21365215.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	33	0	0	姑苏献芹人 |奇幻|连载\n最新更新 第1618章 返回、二代量产·13小时前\nhttps://book.qidian.com/info/1016794204/	2022-09-24 16:17:41.244231
265	逆流1982	https://www.ishuquge.com/txt/110212/index.html	25	2022-11-05 19:53:32	https://www.ishuquge.com/txt/110212/27883599.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/index.html","proxyid":0}	117	0	0	\N	2022-11-05 19:54:09.943294
264	我有一个修仙世界	https://www.7722bar.com/html/60597/	25	2022-10-30 18:59:59	https://www.7722bar.com/html/60597/717570129.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/764164210.html","proxyid":0}	119	0	0	\N	2022-10-30 19:00:05.540307
132	起航1992	https://www.ddxstxt8.com/2_2991	19	2021-04-20 08:53:23	https://www.ddxstxt8.com/2_2991/615023702.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	53	0	0	千年静守 |都市|连载\n最新更新 第698章 终于来了·13小时前\nhttps://book.qidian.com/info/1025762814/	2022-09-24 16:17:36.799132
184	我的基因无限进化	http://www.biququ.com/html/67560/	30	2021-12-13 23:14:07	http://www.biququ.com/html/67560/844501.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	77	0	0	青空洗雨 |玄幻|连载\n最新更新 第三百八十章 光明与黑暗（求月票订阅）·14小时前\nhttps://book.qidian.com/info/1028554266/	2022-09-24 16:17:40.177399
181	重生网游大时代	https://www.zwwx.com/book/61/61016/	8	2021-11-28 23:04:01	https://www.zwwx.com/book/61/61016/113796.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	82	0	0	云天明 |都市|连载\n最新更新 更新说明·2022-01-16\nhttps://book.qidian.com/info/1028971293/	2022-07-30 14:16:39.037454
166	大唐逍遥驸马爷	https://www.xbiquge.la/51/51557/	15	2021-10-05 22:18:45	https://www.xbiquge.la/51/51557/21942711.html	{"titleType":-1,"startIndex":4,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	70	0	0	难山之下 |历史|连载\n最新更新 第1797章 圆满·3小时前\nhttps://book.qidian.com/info/1017281150/	2022-09-24 16:17:37.710419
79	攀科技真的好难啊	https://www.booktxt.net/0_23	19	2020-10-18 18:34:05	https://www.booktxt.net/0_23/530876129.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	23	0	0	拿破那个轮 |科幻|连载\n最新更新 第439章 真乃诚实小郎君也·2021-02-11\nhttps://book.qidian.com/info/1022002204/	2022-09-24 16:17:34.520986
266	穿梭致富从1985开始	https://www.7722bar.com/html/63034822/	25	2022-11-06 12:26:20	https://www.7722bar.com/html/63034822/682206663.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/763474102.html","proxyid":0}	119	0	0	\N	2022-11-06 12:32:32.06824
268	我能对话未来的自己	https://www.147xs.org/book/145551/	30	2022-11-19 16:58:04	https://www.147xs.org/book/145551/2183261.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	121	0	0	\N	2022-11-19 18:04:14.836396
269	从黑科技到超级工程	https://www.ibiquge.la/104/104727/	15	2022-11-23 23:15:27	https://www.ibiquge.la/104/104727/39333363.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/41236589.html","proxyid":0}	70	0	0	\N	2022-11-23 23:16:07.532282
34	回眸1991	http://www.biquwo.org/bqw101958/	8	2020-04-25 16:45:31	http://www.biquwo.org/bqw101958/6063396.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	30	0	0	\N	2021-10-03 10:16:08.402202
218	修复师	https://www.x23us.us/56_56754/	13	2022-04-26 20:54:42	https://www.x23us.us/56_56754/23472975.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	100	0	0	\N	2022-09-24 16:18:39.829973
24	奥术起源	http://www.mcmssc.com/41_41538/	22	2020-04-12 18:19:13	http://www.mcmssc.com/41_41538/20899313.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	27	0	0	\N	2021-10-03 10:16:08.402202
87	传奇1997	https://www.biqubao.com/book/33515	8	2020-11-07 16:24:23	https://www.biqubao.com/book/33515/16936537.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	31	0	0	狂花非叶 |都市|连载\n最新更新 说一下...·2021-05-24\nhttps://book.qidian.com/info/1017278229/	2022-09-24 16:18:37.485253
93	全球游戏进化	https://www.biduo.cc/biquge/60_60481	10	2020-11-20 19:29:34	https://www.biduo.cc/biquge/60_60481/c88195.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	11	0	0	剑无云 |悬疑|完结\n最新更新 完本感言：谢谢大家支持·2021-08-31\nhttps://book.qidian.com/info/1023653417	2021-10-03 10:16:08.402202
109	我在魔法世界开创互联网时代	http://www.biquge.info/96_96355	13	2020-12-29 20:32:54	http://www.biquge.info/96_96355/21314072.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	14	0	0	白天太白 |玄幻|完结\n最新更新 大结局·新王登基·11小时前\nhttps://book.qidian.com/info/1023099668	2021-10-03 10:16:08.402202
130	弃宇宙	https://www.xxbiqudu.com/137_137209	15	2021-04-11 23:36:14	https://www.xxbiqudu.com/137_137209/173127539.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	51	0	0	鹅是老五 |仙侠|连载\n最新更新 第七四五章 七界旗·15小时前\nhttps://book.qidian.com/info/1025677149/	2022-09-24 16:18:36.762947
161	生活系男神	http://www.biququ.com/html/22120/	30	2021-09-12 17:51:50	http://www.biququ.com/html/22120/276501.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	71	0	0	起酥面包 |都市|连载\n最新更新 第635章 为爱琉璃三万顷，迩来有梦必思君·2021-09-21\nhttps://book.qidian.com/info/1015289486/	2022-09-24 16:18:35.584966
163	科技之锤	https://www.biquges.com/78_78152/	15	2021-09-24 22:18:06	https://www.biquges.com/78_78152/33743116.html	{"titleType":1,"startIndex":0,"delimiter":" ","endType":1,"endCharacter":"index.html","proxyid":0}	29	0	0	一桶布丁 |都市|连载\n最新更新 367 原来如此·24小时前\nhttps://book.qidian.com/info/1028469789/	2022-09-24 16:18:38.685401
162	我在凡人科学修仙	https://www.xbiquge.la/72/72725/	15	2021-09-19 09:01:29	https://www.xbiquge.la/72/72725/28722618.html	{"titleType":-1,"startIndex":5,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	70	0	0	洛青子 |仙侠|连载\n最新更新 第八百三十七章 意外插手·18小时前\nhttps://book.qidian.com/info/1024327742/	2022-09-24 16:18:48.242307
276	 科技尽头	http://www.yetianlian.la/yt87396/	25	2022-12-24 12:02:46	http://www.yetianlian.la/yt87396/32349298.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/34140986.html","proxyid":0}	103	0	0	\N	2022-12-24 12:03:56.184604
80	变身之轮回境界	https://www.xsbiquge.com/1_1962	16	2020-10-20 21:56:04	https://www.xsbiquge.com/1_1962/3663179.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	39	0	0	\N	2021-10-03 10:16:08.402202
134	突然成仙了怎么办	http://www.biquger.com/biquge/126780/	14	2021-05-05 16:46:20	http://www.biquger.com/biquge/126780/52338016	{"titleType":-1,"startIndex":4,"delimiter":"","endType":1,"endCharacter":"/","proxyid":0}	18	0	0	欢颜笑语 |都市|完结\n最新更新 苟剩番外---一步一登天·18小时前\nhttps://book.qidian.com/info/1021226784	2021-10-03 10:16:08.402202
153	正在变身中	https://www.vipxs.la/56_56451/	32	2021-08-07 14:00:03	https://www.vipxs.la/56_56451/14493961.html	{"titleType":1,"startIndex":3,"delimiter":" ","endType":0,"endCharacter":"","proxyid":0}	66	0	0	\N	2021-10-03 10:16:08.402202
40	信息全知者	https://www.biqugexx.com/127_127399/	15	2020-05-15 00:03:50	https://www.biqugexx.com/127_127399/33738497.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	19	0	0	魔性沧月 |都市|连载\n最新更新 第九百七十九章 视而不见的生路·2022-01-18\nhttps://book.qidian.com/info/1019222135/	2022-09-24 16:18:42.28598
78	我的隐身战斗姬	http://www.biquge.info/75_75805	13	2020-10-08 12:19:46	http://www.biquge.info/75_75805/15102211.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0	皆破 |都市|连载\n最新更新 第829章 来日方长·2022-01-18\nhttps://book.qidian.com/info/1016948225/	2022-09-24 16:18:49.720352
123	规则系学霸	http://www.loubiqu.com/98_98476	13	2021-03-01 08:59:40	http://www.loubiqu.com/98_98476/22070621.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	49	0	0	不吃小南瓜 |都市|连载\n最新更新 第六百四十章 对口技能人才的巨大缺口·13小时前\nhttps://book.qidian.com/info/1023208487/	2022-09-24 16:18:49.03134
97	诡秘之主	https://www.xsbiquge.com/15_15338	16	2020-11-27 19:02:38	https://www.xsbiquge.com/15_15338/8549128.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	39	0	0	\N	2021-10-03 10:16:08.402202
169	重生1999：开启黑科技时代	http://www.biduowu.com/biquge/70_70188/	33	2021-10-13 12:49:06	http://www.biduowu.com/biquge/70_70188/c33883845.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	74	0	0	红糖醪糟 |科幻|连载\n最新更新 第514章 订单接到手软，西方嫉妒了·13小时前\nhttps://book.qidian.com/info/1028256302/	2022-09-24 16:18:50.453741
150	补天码农	https://www.biquge.win/29_29196/	8	2021-07-14 12:33:20	https://www.biquge.win/29_29196/412575.html	{"titleType":1,"startIndex":0,"delimiter":" ","endType":0,"endCharacter":"no_more.html","proxyid":0}	64	0	0	书客笑藏刀 |都市|连载\n最新更新 494 邪恶的异能皇帝·4小时前\nhttps://book.qidian.com/info/1024644779/	2022-09-24 16:18:40.541246
88	变身之葵花真解	https://www.boquge.com/book/54325	20	2020-11-09 19:36:04	https://www.boquge.com/book/54325/142646578.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	25	0	0	\N	2021-10-03 10:16:08.402202
156	末世之黑暗召唤师	https://www.ibswtan.com/1/1349/	15	2021-08-11 22:11:58	https://www.ibswtan.com/1/1349/1190244.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	68	0	0	\N	2021-10-03 10:16:08.402202
273	脑海带着一扇门	https://www.121du.cc/26797/	47	2022-12-11 16:34:31	https://www.121du.cc/26797/77525.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/392006.html","proxyid":0}	123	0	0	\N	2022-12-11 16:34:43.045673
271	长生武道：从五禽养生拳开始	http://www.yetianlian.la/yt85270/	25	2022-12-02 21:29:55	http://www.yetianlian.la/yt85270/31643123.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/33870657.html","proxyid":0}	103	0	0	\N	2022-12-02 21:45:00.388795
270	从零开始缔造游戏帝国	https://www.ixsw.la/ks103551/	16	2022-11-29 16:54:10	https://www.ixsw.la/ks103551/37701651.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/41469577.html","proxyid":0}	115	0	0	\N	2022-11-29 16:55:35.246976
135	矩阵天王	https://www.mibaoge.com/31_31587	8	2021-05-07 23:22:30	https://www.mibaoge.com/31_31587/516856.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	54	0	0	白雨涵 |奇幻|完结\n最新更新 第二百九十一章 第二条路（大结局）·19小时前\nhttps://book.qidian.com/info/1026633948	2021-10-03 10:16:08.402202
110	重生变身之初始	http://www.biquge.se/14042/	15	2021-01-01 17:53:26	http://www.biquge.se/14042/30129720.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	34	0	0	\N	2021-10-03 10:16:08.402202
272	预判之王	https://www.121du.cc/130614/	47	2022-12-11 11:38:03	https://www.121du.cc/130614/451563.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/500866.html","proxyid":0}	123	0	0	\N	2022-12-11 16:16:00.485702
277	我成了女频修仙小说中的炮灰	https://www.ibiquge.la/107/107789/	15	2022-12-29 12:20:44	https://www.ibiquge.la/107/107789/40371119.html	{"titleType":0,"startIndex":3,"delimiter":"","endType":1,"endCharacter":"/41684957.html","proxyid":0}	70	0	0	\N	2022-12-29 12:20:59.300753
275	什么叫一流救世主啊	https://www.121du.cc/137530/	47	2022-12-21 20:10:27	https://www.121du.cc/137530/435737.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/481509.html","proxyid":0}	123	0	0	\N	2022-12-21 20:10:29.761785
279	巫师：从骑士呼吸法开始肝经验	https://www.asxs.com/view/191190/	40	2023-01-04 21:59:26	https://www.asxs.com/view/191190/2549201.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	106	0	0	\N	2023-01-11 18:16:00.040722
278	斗破：我和美杜莎女王共生了 	https://www.bqwo.cc/bqw83969109/	25	2023-01-02 17:12:09	https://www.bqwo.cc/bqw83969109/696037440.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/751497792.html","proxyid":0}	125	0	0	\N	2023-01-11 15:16:00.015015
139	纳米崛起	https://www.mibaoge.com/29_29448/	8	2021-05-21 19:23:11	https://www.mibaoge.com/29_29448/525602.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	54	0	0	岭南仨人 |科幻|完结\n最新更新 完本感言·2021-09-30\nhttps://book.qidian.com/info/1025502417/	2021-10-06 11:16:03.793634
141	微观黑科技	https://www.miaojiang8.net/15_15357	8	2021-05-26 10:54:36	https://www.miaojiang8.net/15_15357/5126078.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	58	0	0	爱香菜牛肉面 |都市|完结\n最新更新 第二百九十六章 进入星海·13小时前\nhttps://book.qidian.com/info/1026791702	2021-10-03 10:16:08.402202
103	这个大佬有点苟	https://www.biduo.cc/biquge/0_248	10	2020-12-13 21:50:07	https://www.biduo.cc/biquge/0_248/c35804.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	11	0	0	半步沧桑 |玄幻|完结\n最新更新 第581章 落幕·新的开始……·2021-06-01\nhttps://book.qidian.com/info/1021359175	2021-10-03 10:16:08.402202
23	魔法的学术时代	http://www.biquger.com/biquge/112679/	14	2020-04-10 10:39:09	http://www.biquger.com/biquge/112679/45424996	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/"}	18	0	0	鸽子跳 |奇幻|连载\n最新更新 1140. 蹭饭·2021-02-20\nhttps://book.qidian.com/info/1015906924/	2022-09-24 16:18:50.487096
111	剑宗旁门	https://www.biquge11.com/34_34918	15	2021-01-12 09:06:19	https://www.biquge11.com/34_34918/15567219.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	43	0	0	愁啊愁 |仙侠|完结\n最新更新 第八百零八章 下一个传奇（大结局）·2021-05-04\nhttps://book.qidian.com/info/1021423912	2021-10-03 10:16:08.402202
137	重生之黑科技首富	https://www.tvbts.com/17_17252/	29	2021-05-13 17:04:10	https://www.tvbts.com/17_17252/15769286.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	56	0	0	攀爬中的小牜 |都市|连载\n最新更新 第816章 营收主力（求订阅）·13小时前\nhttps://book.qidian.com/info/1020313076/	2022-09-24 16:18:50.50937
29	泡面首富	http://www.biquges.com/49_49188/index.html	15	2020-04-16 16:48:33	http://www.biquges.com/49_49188/24396943.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"index.html","proxyid":0}	29	0	0	\N	2021-10-03 10:16:08.402202
116	我的黑科技庇护所	https://www.bqg99.cc/book/2012213791	5	2021-01-31 23:21:18	https://www.bqg99.cc/book/2012213791/400512248.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	16	0	0	潇潇的小白 |科幻|完结\n最新更新 完本感言·昨日12:32\nhttps://book.qidian.com/info/1024819173	2021-10-03 10:16:08.402202
37	法爷永远是你大爷	https://www.biquge.lu/book/59464	5	2020-05-05 15:56:32	https://www.biquge.lu/book/59464/487314482.html	{"titleType":1,"startIndex":0,"delimiter":" ","endType":0,"endCharacter":"","proxyid":0}	2	0	0	\N	2021-10-03 10:16:08.402202
35	我真是修炼天才	https://www.biqubao.com/book/34827/	8	2020-04-26 20:06:25	https://www.biqubao.com/book/34827/17581587.html	{"titleType":1,"startIndex":0,"delimiter":" ","endType":0,"endCharacter":"","proxyid":0}	31	0	0	\N	2021-10-03 10:16:08.402202
280	巫师：从骑士呼吸法开始肝经验	https://www.biququ.info/html/84732/	30	2023-01-04 22:04:36	https://www.biququ.info/html/84732/1059151.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	77	0	0	\N	2023-01-11 18:16:00.328668
284	绿龙博士	http://www.77dushu.la/novel/105444/	48	2023-01-23 17:09:38	http://www.77dushu.la/chapter/105444/44091662.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/105444/52820496.html","proxyid":0}	127	0	0	\N	2023-01-23 17:21:16.632551
285	重生星际阵器师	https://www.147xs.org/book/154845/	30	2023-01-25 22:08:01	https://www.147xs.org/book/154845/2322671.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	121	0	0	\N	2023-01-25 22:08:03.810447
63	面向阵法修仙	https://www.boquge.com/book/120686	20	2020-07-20 21:37:56	https://www.boquge.com/book/120686/170069677.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	25	0	0	拔剑破东风 |仙侠|连载\n最新更新 第二百八十七章 逃离·2021-12-31\nhttps://book.qidian.com/info/1017652502/	2022-09-24 16:18:59.377312
117	1991从芯开始	https://www.lvsetxt.cc/ebook/962.html	25	2021-02-04 19:58:08	https://www.lvsetxt.cc/books/0/962/592715808.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"962.html","proxyid":0}	42	0	0	三分糊涂 |现实|连载\n最新更新 第895章 传递真相的平台·13小时前\nhttps://book.qidian.com/info/1023504293/	2022-09-24 16:19:00.810559
286	为了妹妹我变成了女生	https://www.biququ.com/html/37901/	30	2023-01-27 15:53:35	https://www.biququ.com/html/37901/473761.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	77	0	0	\N	2023-01-27 15:53:38.159986
120	重生之实业大亨	https://www.vbiquge.com/98_98550/	16	2021-02-17 00:15:12	https://www.vbiquge.com/98_98550/129785.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	48	0	0	过关斩将 |都市|连载\n最新更新 第591章 以彼之道还施彼身·13小时前\nhttps://book.qidian.com/info/1024201760/	2022-09-24 16:18:59.777141
140	快看那个大佬	https://www.miaojiang8.com/10_10559/	8	2021-05-24 15:06:42	https://www.miaojiang8.com/10_10559/4136318.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	57	0	0	奥尔良烤鲟鱼堡 |都市|连载\n最新更新 第七十四章 都怪……·2022-01-18\nhttps://book.qidian.com/info/1024779026/	2022-09-24 16:18:58.488245
90	重生之北国科技	https://www.bqg99.cc/book/2019788102	5	2020-11-15 15:01:44	https://www.bqg99.cc/book/2019788102/504073701.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	16	0	0	\N	2021-10-03 10:16:08.402202
287	开发大西北：我在戈壁建了一座城	https://www.ggzuo.com/book/147651/	50	2023-01-28 19:52:59	https://www.ggzuo.com/read/147651/55477429.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"/55477430.html","proxyid":0}	129	0	0	\N	2023-01-28 19:53:01.710248
55	咸鱼的科技直播间	https://www.boquge.com/book/118730/	20	2020-06-06 23:57:02	https://www.boquge.com/book/118730/168936226.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	25	0	0	\N	2021-10-03 10:16:08.402202
54	咸鱼的科技直播间	https://www.biquge.lu/book/67081/?ivk_sa=1023337b	5	2020-06-06 23:52:23	https://www.biquge.lu/book/67081/24254224.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	2	0	0	\N	2021-10-03 10:16:08.402202
283	重生日常修仙	https://www.biququ.info/html/84212/	30	2023-01-14 14:58:39	https://www.biququ.info/html/84212/1052651.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	77	0	0	\N	2023-01-14 14:58:45.565432
281	重生1999：开启黑科技时代	https://www.23xstxt.net/book/17101/17101619/	25	2023-01-07 16:27:11	https://www.23xstxt.net/book/17101/17101619/620258511.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	126	0	0	\N	2023-01-07 16:27:32.674396
282	大国院士	https://www.147xs.org/book/150820/	30	2023-01-11 19:00:42	https://www.147xs.org/book/150820/2262301.html	{"getUtil":"htmlunit","titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	121	0	0	\N	2023-02-02 18:16:00.01314
291	我的物品有升级面板	https://www.ggzuo.com/book/146370/	50	2023-02-08 18:39:51	https://www.ggzuo.com/read/146370/55099531.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	129	0	0	\N	2023-02-08 18:40:05.370078
288	从低魔世界归来，先做人工智能	https://www.tsxsw.net/html/131/131128/	44	2023-01-29 18:40:31	https://www.tsxsw.net/html/131/131128/1.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	118	0	0	\N	2023-01-29 18:44:25.153128
289	 LPL全能女替补	http://www.paoshubaxs.com/103366/	51	2023-02-02 21:06:40	http://www.paoshubaxs.com/103366/61804787.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	130	0	0	\N	2023-02-02 21:07:01.742625
290	我真的只想回地球啊	https://www.tsxsw.net/html/130/130348/	44	2023-02-04 12:46:05	https://www.tsxsw.net/html/130/130348/1.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	118	0	0	\N	2023-02-04 12:46:09.412282
65	魔法塔的星空	http://www.biquge.se/21078	15	2020-07-25 09:45:03	http://www.biquge.se/21078/38538728.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"/","proxyid":0}	34	0	0	歹丸郎 |奇幻|连载\n最新更新 第1060章 大贤者之塔后续·11小时前\nhttps://book.qidian.com/info/1014215611/	2022-09-24 16:19:04.244659
81	新顺1730	https://www.kuxiaoshuo.com	15	2020-10-25 17:25:15	https://www.kuxiaoshuo.com/130_130633/170337677.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	40	0	0	望舒慕羲和 |历史|连载\n最新更新 第七六零章 堂下何人状告本官（八）·2022-01-18\nhttps://book.qidian.com/info/1022757368/	2022-09-24 16:19:03.511258
102	女帝背后的男人	http://www.biquge.info/98_98084	13	2020-12-13 20:58:37	http://www.biquge.info/98_98084/21942981.html	{"titleType":1,"startIndex":0,"delimiter":".","endType":0,"endCharacter":""}	14	0	0	\N	2022-09-24 16:19:02.588851
70	重生异界做游戏	http://www.biquge.se/55965	15	2020-08-07 21:34:50	http://www.biquge.se/55965/69888060.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	34	0	0	\N	2021-10-03 10:16:08.402202
95	种田系修仙	http://www.biquge.info/89_89088	13	2020-11-23 08:47:58	http://www.biquge.info/89_89088/19120839.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0	追梦萤火虫 |玄幻|完结\n最新更新 第四百二十章 悟性突破·2小时前\nhttps://book.qidian.com/info/1021678077	2021-10-03 10:16:08.402202
74	银龙的黑科技	http://www.biquge.info/82_82740	13	2020-08-23 13:46:27	http://www.biquge.info/82_82740/17265615.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0	木老七 |奇幻|完结\n最新更新 完本感言·2021-05-28\nhttps://book.qidian.com/info/1016831050	2021-10-03 10:16:08.402202
96	变成超人是什么体验之超人日记	https://www.lvsetxt.com/books/22/22215/586140274.html	25	2020-11-25 10:49:12	https://www.lvsetxt.com/books/22/22215/586140274.html	{"titleType":1,"startIndex":0,"delimiter":"、","endType":1,"endCharacter":"22215.html","proxyid":0}	42	0	0	\N	2021-10-03 10:16:08.402202
58	小阁老	https://www.booktxt.com/28_28228/	16	2020-06-20 22:04:49	https://www.booktxt.com/28_28228/15630.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	28	0	0	三戒大师 |历史|连载\n最新更新 第一百三十章 有没有信心？·9小时前\nhttps://book.qidian.com/info/1016855477/	2022-09-24 16:19:04.72256
73	芯片产业帝国	https://www.5atxt.com/27_27803	15	2020-08-23 09:42:09	https://www.5atxt.com/27_27803/24320024.html	{"titleType":1,"startIndex":1,"delimiter":"节 ","endType":0,"endCharacter":"","proxyid":0}	38	0	0	树叶上的水珠 |都市|连载\n最新更新 第472节·2021-11-28\nhttps://book.qidian.com/info/1017367051/	2022-09-24 16:19:05.356031
75	我能魔改黑科技	https://www.booktxt.com/37_37352	16	2020-09-14 21:30:24	https://www.booktxt.com/37_37352/170099.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	28	0	0	凉茶煮酒 |科幻|连载\n最新更新 第十六章 暴躁的女土著·2分钟前\nhttps://book.qidian.com/info/1022007164/	2022-09-24 16:19:04.655787
69	重生之网络争霸	http://www.biquge.info/75_75928/	13	2020-08-07 21:33:36	http://www.biquge.info/75_75928/15136955.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	14	0	0	\N	2021-10-03 10:16:08.402202
42	重生过去当传奇	http://www.biquge.info/71_71007/	13	2020-05-24 10:28:36	http://www.biquge.info/71_71007/13420799.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	14	0	0	\N	2021-10-03 10:16:08.402202
36	炮台法师	https://www.biqubao.com/book/32998/	8	2020-05-02 15:10:38	https://www.biqubao.com/book/32998/16669021.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	31	0	0	\N	2021-10-03 10:16:08.402202
59	佛系科技	http://www.biquge.info/86_86788	13	2020-06-26 22:33:41	http://www.biquge.info/86_86788/18530447.html	{"titleType":1,"startIndex":0,"delimiter":" ","endType":0,"endCharacter":"","proxyid":0}	14	0	0	\N	2021-10-03 10:16:08.402202
85	创造沙盘世界	https://www.bqg99.cc/book/2014569385	5	2020-11-01 14:35:02	https://www.bqg99.cc/book/2014569385/461396298.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	16	0	0	\N	2021-10-03 10:16:08.402202
57	最初的巫师	https://www.biquge.lu/book/62758	5	2020-06-15 23:49:09	https://www.biquge.lu/book/62758/463905595.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	2	0	0	\N	2021-10-03 10:16:08.402202
108	这是我的冰汽时代	http://www.bqge.com/197_197193	15	2020-12-19 23:22:22	http://www.bqge.com/197_197193/59901598.html	{"titleType":2,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	45	0	0	\N	2021-10-03 10:16:08.402202
86	凶猛道侣也重生了	https://www.booktxt.net/2_2535	19	2020-11-05 08:44:28	https://www.booktxt.net/2_2535/541680501.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	23	0	0	雨下的好大 |玄幻|完结\n最新更新 完结感言·2021-08-07\nhttps://book.qidian.com/info/1019254153	2021-10-03 10:16:08.402202
99	千秋不死人	https://www.biqubao.com/book/36074	8	2020-11-30 21:48:13	https://www.biqubao.com/book/36074/18251248.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	31	0	0	\N	2021-10-03 10:16:08.402202
113	缥缈圣女	https://www.bqg99.cc/book/2014184735	5	2021-01-24 18:05:57	https://www.bqg99.cc/book/2014184735/454449410.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	16	0	0	传说的光 |轻小说|连载\n最新更新 第433章 死人复活·22小时前\nhttps://book.qidian.com/info/1022848229/	2022-09-24 16:19:06.322818
121	诅咒之龙	http://www.loubiqu.com/40_40207/	13	2021-02-19 13:46:55	http://www.loubiqu.com/40_40207/1167795.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	49	0	0	路过的穿越者 |奇幻|连载\n最新更新 第二千三百三十章 只要提供技术就够了·11小时前\nhttps://book.qidian.com/info/1009521148/	2022-09-24 16:19:05.622702
101	不会真有人觉得修仙难吧	https://www.biquge11.com/36_36402/	15	2020-12-07 22:48:08	https://www.biquge11.com/36_36402/16160714.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	43	0	0	\N	2021-10-03 10:16:08.402202
119	天道方程式	https://www.meegoq.com/book150658.html	28	2021-02-12 15:37:26	https://www.meegoq.com/book/150658_187056458.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"150658.html","proxyid":0}	47	0	0	二目 |奇幻|连载\n最新更新 第八百二十七章 天眼雏形·18小时前\nhttps://book.qidian.com/info/1023215696/	2022-09-24 16:19:07.322819
51	我有超体U盘	https://www.biquge.lu/book/67863/	5	2020-06-04 12:45:08	https://www.biquge.lu/book/67863/499829444.html	{"titleType":1,"startIndex":0,"delimiter":"-","endType":0,"endCharacter":""}	2	0	0	黑暗狗熊 |科幻|完结\n最新更新 完本感言·20小时前\nhttps://book.qidian.com/info/1017327786	2021-10-03 10:16:08.402202
105	这是我的冰汽时代	http://www.biquge.se/64890	15	2020-12-19 22:53:42	http://www.biquge.se/64890/74463055.html	{"titleType":2,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	34	0	0	搞原创的咸鱼 |奇幻|连载\n最新更新 关于《冰气》后续，与新书《我在人间斩妖除魔》求关注（蹲地抱头别打脸）·2021-01-25\nhttps://book.qidian.com/info/1022015932/	2022-09-24 16:19:07.311883
77	腾飞我的航空时代	https://www.biduo.cc/biquge/53_53393/	10	2020-09-26 15:35:02	https://www.biduo.cc/biquge/53_53393/c22077441.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	11	0	0	安溪柚 |都市|完结\n最新更新 新书上线·19小时前\nhttps://book.qidian.com/info/1016398860/	2021-12-31 08:16:03.684165
71	大唐第一长子	https://www.booktxt.com/35_35812	16	2020-08-17 18:53:47	https://www.booktxt.com/35_35812/258946.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	28	0	0	\N	2021-10-03 10:16:08.402202
9	重写科技格局	https://www.biquge.tw/489_489326/	8	2020-04-07 08:58:49	https://www.biquge.tw/489_489326/2594513.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	10	0	0	\N	2021-10-03 10:16:08.402202
31	修仙从沙漠开始	https://www.biquge.lu/book/67051	5	2020-04-19 23:31:36	https://www.biquge.lu/book/67051/502801184.html	{"titleType":-1,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	2	0	0	中天紫薇大帝 |仙侠|完结\n最新更新 完本感言·2021-06-07\nhttps://book.qidian.com/info/1017079984	2021-10-03 10:16:08.402202
292	我辈女修当自强	https://wap.hetangczj.com/book_10088/	52	2023-02-11 12:56:58	https://wap.hetangczj.com/book_10088_6710568.html	{"getUtil":"htmlunit","titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	131	0	0	\N	2023-02-11 12:57:05.676301
293	初唐：开局告诉李二我是穿越者	https://wap.hetangczj.com/book_43673/	52	2023-02-25 11:40:21	https://wap.hetangczj.com/book_43673_19611224.html	{"getUtil":"htmlunit","titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	131	0	0	\N	2023-03-05 09:16:00.059744
267	我打造了科学魔法	https://www.2shu8.net/txt/171401/	45	2022-11-12 11:51:31	https://www.2shu8.net/txt/171401/196036691/	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/end.htm?aid=171401&cid=199179242","proxyid":0}	120	0	0	\N	2023-03-13 21:32:35.011742
304	转生仙道	https://www.suiyuexs.com/read/11869395.html	34	2023-08-31 21:36:33	https://www.suiyuexs.com/read/11869395/115023651.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/11869395.html","proxyid":0}	143	0	0	\N	2023-11-30 18:16:00.017079
297	龙骑战机	http://www.ibiquw.com/book/126/	55	2023-03-11 09:02:02	http://www.ibiquw.com/book/126/110257.html	{"getUtil":"htmlunit","titleType":-1,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/111543.html","proxyid":0}	136	0	0	\N	2023-03-11 11:26:10.616484
295	长生从娶妻开始	https://www.dianjiangxin.com/book/289159/	54	2023-03-01 17:25:02	https://www.dianjiangxin.com/book/289159/1.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/289159/108.html","proxyid":0}	133	0	0	\N	2023-03-05 09:16:00.159377
294	末世灾变：我的熟练度面板	https://www.mhtxs.la/185/185353/	53	2023-02-26 13:23:28	https://www.mhtxs.la/185/185353/63259631.html	{"getUtil":"htmlunit","titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"63669178.html","proxyid":0}	132	0	0	\N	2023-03-05 09:16:00.06524
296	裂天空骑	https://www.epzw.com/html/108/108178/	44	2023-03-05 13:16:57	https://www.epzw.com/html/108/108178/1.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	134	0	0	\N	2023-03-05 13:17:00.744597
300	苟在异界研究科学	https://www.xswang.com/book/65180/	57	2023-05-14 16:45:55	https://www.xswang.com/book/65180/64803153.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/65180/67082867.html","proxyid":0}	139	0	0	\N	2023-05-14 17:01:55.46463
302	最后的机武神	https://www.xiashuyun.com/584402/	54	2023-07-09 13:00:42	https://www.xiashuyun.com/584402/read_1.html	{"getUtil":"htmlunit","titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/584402/read_171.html","proxyid":0}	141	0	0	\N	2023-07-15 19:16:00.015441
301	解构系巫师	https://www.imianhuatang.info/186/186323/	53	2023-06-24 10:24:48	https://www.imianhuatang.info/186/186323/63584037.html	{"getUtil":"htmlunit","titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"65268326.html","proxyid":0}	140	0	0	\N	2023-07-15 20:16:00.030555
299	识法仙途	https://www.biququ.info/html/81925/	30	2023-04-20 21:28:08	https://www.biququ.info/html/81925/1024061.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	77	0	0	\N	2023-04-20 21:37:44.394425
303	从肉体凡胎到粉碎星球	https://www.ibiquge.info/167_167773/	15	2023-07-15 23:37:11	https://www.ibiquge.info/167_167773/65055706.html	{"getUtil":"htmlunit","titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/167_167773/66542056.html","proxyid":0}	142	0	0	\N	2023-07-15 23:37:17.82598
307	作为太监，我一点也不想长生不死	http://www.gdbzkz.info/zuoweitaijianwoyidianyebuxiangchangshengbusi/	25	2023-10-23 21:12:08	http://www.gdbzkz.info/zuoweitaijianwoyidianyebuxiangchangshengbusi/37448925.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/38865222.html","proxyid":0}	145	0	0	\N	2023-11-30 19:16:00.010686
306	苟在诊所练医术	https://www.yingxiongxs.com/book/97270635/	58	2023-09-18 21:10:46	https://www.yingxiongxs.com/book/97270635/115684593.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/book/97270635/","proxyid":0}	144	0	0	\N	2023-11-30 18:16:00.020424
305	稳健修仙，整个修仙界都是我家	https://www.yingxiongxs.com/book/24572265/	58	2023-09-17 07:54:40	https://www.yingxiongxs.com/book/24572265/116958235.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/book/24572265/","proxyid":0}	144	0	0	\N	2023-11-30 19:16:00.0252
308	这个巫师他就不科学	https://www.mayiwxw.com/116_116408/	15	2023-11-30 21:28:28	https://www.mayiwxw.com/116_116408/47834027.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/116_116408/48467231.html","proxyid":0}	80	0	0	\N	2023-11-30 21:28:32.551433
298	这个明星画风不对	http://www.huangyixiaoshuo.com/117/117164/	15	2023-03-20 21:59:07	http://www.huangyixiaoshuo.com/117/117164/45667746.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/49410961.html","proxyid":0}	138	0	1	\N	2024-02-24 03:16:00.014989
309	星际：从清洁工开始	http://www.26ks.org/book/67768/	57	2024-01-04 22:11:46	http://www.26ks.org/book/67768/70641781.html	{"getUtil":"selenium","titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/67768/74779478.html","proxyid":0}	146	0	0	\N	2024-01-04 22:11:50.258113
\.


--
-- Data for Name: chapter; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: config; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.config (configid, name, titlematch, titlelnnum, conmatch, conlnnum, startoffset, endoffset, amatch, nexta) FROM stdin;
8	BiQuGe_TW	//div[@class='bookname']/h1/text()	2	//div[@id='content']/text()	2	0	0	//div[@class='bottem2']//a	2
11	BiQuGe7_COM	//div[@class='content']/h1/text()	2	//div[@id='content']/text()	2	0	-1	//div[@class='page_chapter']//a	2
12	BiQuGe_COM	//div[@class='bookname']/h1/text()	2	//div[@id='content']/text()	2	0	-3	//div[@class='bottem2']//a	3
13	BiQuGe_INFO	//div[@class='bookname']/h1/text()	2	//div[@id='content']/text()	2	0	0	//div[@class='bottem']//a	3
16	BiQuGg_TW	//div[@class='bookname']/h1/text()	2	//div[@id='content']/text()	2	0	0	//div[@class='bottem2']//a	2
18	BiQuWu_COM	//div[@class='bookname']/h1/text()	2	//div[@id='content']/text()	1	0	0	//div[@class='bottem2']//a	3
20	BoQuGe_COM	//div[@id='h1']/h1/text()	2	//div[@id='txtContent']/text()	2	0	0	//div[@class='clearfix']/ul/li/a	6
21	BQG5_CC	//div[@class='bookname']/h1/text()	2	//div[@id='content']/p/text()	2	0	0	//div[@class='bottem2']//a	3
22	MCMSSC_COM	//div[@class='bookname']/h1/text()	0	//div[@id='content']/text()	2	0	0	//div[@class='bottem2']//a	3
14	BiQuGer_COM	//div[@class='bookname']/h1/text()	2	//div[@class='content']/text()	2	0	0	//div[@class='bottem']//a	3
10	BiDuo_CC	//div[@class='bookname']/h1/text()	2	//div[@id='content']/text()	2	0	0	//div[@class='bottem2']//a	2
5	BiQuGe_LU	//div[@id='content']/../h1/text()	2	//div[@id='content']/text()	2	0	-1	//div[@class='page_chapter']//a	2
27	M_BiQuGe5200_COM	//div[@class='title']/text()	2	//div[@class='text']/p/text()	2	0	0	//div[@class='navigator-nobutton']//a	3
28	MeeGoQ_COM	//header[@class='clearfix']/h1/text()	2	//div[@id='content']/text()	2	0	0	//div[@class='operate']//a	5
19	BookTXT_NET	//div[@class='bookname']/h1/text()	2	//div[@id='content']/text()	2	0	-1	//div[@class='bottem2']//a	2
39	YWGGZY_COM	//h1[@class='title']/text()	2	//div[@id='content']/text()	2	0	0	//div[@class='section-opt']//a	2
15	BiQuGexx_COM	//div[@class='bookname']/h1/text()	2	//div[@id='content']/text()	2	0	0	//div[@class='bottem2']//a	3
40	ASXS_COM	//div[@class='bdsub']//dd/h1/text()	2	//dd[@id='contents']/p/text()	2	0	0	//dd[@id='footlink']//a	2
41	YQZWW_COM	//div[@class='bookname']/h1/text()	2	//div[@id='content']/text()	2	1	0	//div[@class='bottem']//a	3
42	XBookTXT_NET	//div[@class='bookname']/h1/text()	2	//div[@id='content']/text()	2	0	-2	//div[@class='bottem2']//a	2
29	TVBTS_COM	//div[@class='bookname']/h1/text()	2	//div[@id='content']/text()	2	0	-1	//div[@class='bottem2']//a	3
30	XBiQuPao_COM	//div[@class='bookname']/h1/text()	2	//div[@id='content']/p/text()	2	0	0	//div[@class='bottem2']//a	2
32	VipXS_LA	//div[@class='bookname']/h1/text()	2	//div[@id='content']/text()	2	0	-2	//div[@class='bottem2']//a	3
33	QiShuGe_NET	//div[@class='content']/h1/text()	2	//div[@id='content']/text()	2	0	0	//div[@class='page_chapter']//a	2
31	Fyxfcw_COM	//div[@class='reader-main']/h1/text()	2	//div[@id='content']/text()	2	0	0	//div[@class='section-opt m-bottom-opt']//a	2
34	ZJJuShu_COM	//div[@id='chapter-title']/h1/text()	2	//div[@id='txt']/text()	2	0	0	//div[@class='chapter-control']//a	2
35	DengBi_COM	//div[@class='play-title']/h1/text()	2	//div[@id='nr']/text()	2	0	0	//div[@class='operate text-center']//a	2
36	DingDian6_COM	//div[@class='zhangjieming']/h1/text()	2	//div[@id='TXT']/text()	2	0	0	//div[@class='bottem']//a	3
25	LvSeTxt_COM	//div[@class='content']/h1/text()	2	//div[@id='content']/text()	2	0	-2	//div[@class='page_chapter']//a	2
37	MayDayFans_COM	//div[@class='title']/h1/a/text()	2	//div[@id='chaptercontent']/text()	2	0	-2	//div[@class='operate']//a	2
38	VipKanShu_VIP	//div[@class='content']/h1/text()	2	//div[@id='content']/p/text()	2	0	0	//div[@class='page_chapter']//a	2
43	XDingDianXSW_COM	//div[@class='novel_head']/h1/text()	2	//div[@class='novel_content']/text()	2	0	0	//div[@class='novel_bottom']//a	2
44	TSXSW_NET	//div[@class='main w chapter']/h1/text()	2	//div[@id='content']/p/text()	2	0	0	//div[@class='chapterpage']//a	2
45	2Shu8_NET	//div[@class='catalog_h1']/h1/text()	2	//div[@id='content']/text()	2	0	0	//div[@class='sel']//a[@class='next']	0
47	121Du_CC	//div[@id='BookCon']/h1/text()	2	//div[@id='BookText']/text()	2	0	0	//div[@class='link']//a	2
48	77DuShu_LA	//div[@class='bd']/h1/text()	2	//div[@id='ChapterContents']/text()	2	0	-2	//div[@class='control']//a	2
49	XGeDaa_COM	//div[@class='novel']/h1/text()	2	//div[@id='content']/p/text()	2	0	0	//div[@class='pereview']//a	2
50	GGZuo_COM	//div[@class='am-panel-bd']/h1/text()	2	//div[@id='content']/p/text()	2	0	0	//div[@class='am-btn-group book-read-page']//a	3
51	PaoShuBaXS_COM	//div[@class='zhangjieming']/h1/text()	2	//div[@id='content']/p/text()	2	0	0	//div[@class='bottem']//a	3
52	HeTangCZJ_COM	//div[@id='mlfy_main_text']/h1/text()	2	//div[@id='TextContent']/p/text()	2	0	0	//p[@class='mlfy_page']//a	3
53	MHTXS_LA	//div[@class='read_title']/h1/text()	2	//div[@class='content']/text()	2	0	0	//div[@class='pagego']//a	2
54	DianJiangXin_COM	//div[@class='title']/h1/a/text()	2	//div[@id='chaptercontent']/text()	2	0	0	//div[@class='operate']//a	2
55	IBiQuW_COM	//div[@class='h1title']/h1/text()	2	//div[@id='htmlContent']/text()	2	0	0	//div[@class='chapter_Turnpage']//a	4
56	HuJuGe_COM	//div[@class='reader-main']/h1/text()	2	//div[@id='content']/p/text()	2	0	0	//div[@class='section-opt m-bottom-opt']//a	2
57	XSWang_COM	//div[@class='bookname']/h1/text()	2	//div[@id='content']/p[@class='content_detail']/text()	2	0	0	//div[@class='bottem2']//a	3
58	YingXiongXS_COM	//div[@id='chapter-title']/h1/text()	2	//div[@id='txt']/text()	2	1	0	//div[@class='chapter-control']//a	2
\.


--
-- Data for Name: paragraph_h00; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h01; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h02; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h03; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h04; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h05; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h06; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h07; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h08; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h09; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h10; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h11; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h12; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h13; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h14; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h15; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h16; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h17; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h18; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: paragraph_h19; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: proxy; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.proxy (proxyid, name, host, port, type) FROM stdin;
1	Socks代理	39.105.204.226	10080	1
\.


--
-- Data for Name: site; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.site (siteid, name, url, configid, createtime) FROM stdin;
2	BiQuGe_LU	https://www.biquge.lu/	5	2020-03-28 17:27:24
10	BiQuGe_TW	https://www.biquge.tw	8	2020-04-07 14:27:49
11	BiDuo_CC	http://www.biduo.cc	10	2020-04-09 17:50:26
12	BiQuGe7_COM	http://www.biquge7.com	11	2020-04-09 17:50:44
13	BiQuGe_COM	http://www.biqige.com	12	2020-04-09 17:51:03
14	BiQuGe_INFO	http://www.biquge.info	13	2020-04-09 17:51:31
15	BiQuMo_COM	http://www.biqumo.com	5	2020-04-09 17:52:36
16	BQG99_CC	http://www.bqg99.cc	5	2020-04-09 17:52:58
17	E8ZW_COM	http://www.e8zw.com	8	2020-04-09 17:53:35
18	BiQuGer_COM	http://www.biquger.com	14	2020-04-09 17:53:53
19	BiQuGexx_COM	http://www.biqugexx.com	15	2020-04-09 17:54:11
20	BiQuGg_TW	http://www.biqugg.com	16	2020-04-09 17:54:30
21	BiQuKan_COM	http://www.biqukan.com	5	2020-04-09 17:54:47
22	BiQuWu_COM	http://www.biqiwu.com	18	2020-04-09 17:56:39
23	BookTXT_NET	http://www.booktxt.net	19	2020-04-09 17:57:06
25	BoQuGe_COM	http://www.boquge.com	20	2020-04-09 17:58:06
26	BQG5_CC	http://www.bqg5.cc	21	2020-04-09 17:58:21
27	MCMSSC_COM	http://www.mcmssc.com	22	2020-04-09 17:58:40
28	BookTXT_COM	https://www.booktxt.com/	16	2020-04-12 18:26:42
30	BiQuWo_ORG	http://www.biquwo.org/	8	2020-04-25 16:44:43
31	BiQuBao_COM	https://www.biqubao.com	8	2020-04-26 20:05:03
35	BiQu_LA	http://www.biqu.la	8	2020-08-07 21:34:17
24	BxQuGe_COM	http://www.bxquge.com	15	2020-04-09 17:57:44
29	BiQuGes_COM	http://www.biquges.com/	15	2020-04-16 16:46:52
33	78ZW_COM	https://www.78zw.com	15	2020-07-25 10:01:14
34	BiQuGe_SE	http://www.biquge.se/	15	2020-07-25 10:05:58
37	DingDiann_COM	https://www.dingdiann.com/	15	2020-08-23 09:09:22
38	5ATXT_COM	https://www.5atxt.com/	15	2020-08-23 09:41:15
40	KuXiaoShuo_COM	https://www.kuxiaoshuo.com/	15	2020-10-25 17:24:35
42	LvSeTxt_COM	https://www.lvsetxt.com/	25	2020-11-25 10:48:09
43	BiQuGe11_COM	https://www.biquge11.com/	15	2020-12-07 22:47:35
45	GQGe_COM	http://www.bqge.com/	15	2020-12-19 23:21:55
46	M_BiQuGe5200_COM	https://m.biquge5200.com/	27	2021-01-26 22:24:13
47	MeeGoQ_COM	https://www.meegoq.com/	28	2021-02-12 15:36:17
39	XSBiQuGe_COM	https://www.xsbiquge.com/	16	2020-10-20 21:55:14
48	VBiQuGe_COM	https://www.vbiquge.com/	16	2021-02-17 00:17:00
85	23XSW_CC	https://www.23xsw.cc/	25	2021-12-25 14:06:11
86	ZHHBQG_COM	https://www.zhhbqg.com/19_19366/	25	2022-01-01 09:17:07
49	LouBiQu_COM	http://www.loubiqu.com/	13	2021-02-17 00:22:23
50	BswTan_COM	http://www.bswtan.com/	15	2021-03-06 12:09:20
51	XXBiQuDu_COM	https://www.xxbiqudu.com/	15	2021-03-11 08:58:11
52	ShuGe_LA	https://www.shuge.la/	15	2021-03-13 00:01:52
53	DDXSTXT8_COM	https://www.ddxstxt8.com/	19	2021-04-13 21:21:39
54	MiBaoGe_COM	https://www.mibaoge.com	8	2021-05-07 23:21:16
55	BiQuZhh_COM	https://www.biquzhh.com/	25	2021-05-09 18:55:22
56	TVBTS_COM	https://www.tvbts.com/	29	2021-05-13 17:03:18
57	MiaoJiang8_COM	https://www.miaojiang8.com/	8	2021-05-24 15:06:10
58	MiaoJiang8_NET	https://www.miaojiang8.net/	8	2021-05-26 10:53:51
59	JiPinXS_COM	https://www.jipinxs.com/	8	2021-06-20 08:30:13
60	ShuGe9_COM	https://www.shuge9.com/	15	2021-06-27 13:35:05
61	XBiQuWX_LA	https://www.xbiquwx.la/	13	2021-07-01 12:59:47
62	B5200_NET	http://www.b5200.net/	21	2021-07-07 21:32:58
64	BiQuGe_WIN	https://www.biquge.win/	8	2021-07-14 12:38:33
65	Fyxfcw_COM	https://www.fyxfcw.com/	31	2021-07-19 20:24:00
63	XBiQuPao_COM	https://www.xbiqupao.com/	30	2021-07-14 12:32:10
66	VipXS_LA	https://www.vipxs.la/	32	2021-08-07 13:58:33
67	BiQuWX_LA	https://www.biquwx.la/	13	2021-08-10 08:49:25
68	IbswTan_COM	https://www.ibswtan.com/	15	2021-08-11 22:11:07
69	QiShuGe_NET	http://www.qiushuge.net/	33	2021-08-22 21:19:57
70	XBiQuGe_LA	https://www.xbiquge.la/	15	2021-08-25 22:40:30
72	ZJJuShu_COM	https://www.zjjushu.com/	34	2021-10-08 19:57:11
73	DengBi_COM	https://www.dengbi.com/	35	2021-10-08 20:05:16
74	BiDuoWu_COM	http://www.biduowu.com/	33	2021-10-13 12:48:09
75	SoBiQuGe_COM	https://www.sobiquge.com/	8	2021-10-16 20:17:13
76	XXYanQing_COM	https://www.xxyanqing.com/	25	2021-10-27 19:44:57
77	BiQuQu_COM	http://www.biququ.com/	30	2021-11-01 19:29:02
78	XPBTxt_COM	http://www.xpbtxt.com/	8	2021-11-08 20:02:12
79	BiQuSa_COM	https://www.biqusa.com/	15	2021-11-18 23:44:40
80	MaYiWXW_COM	https://www.mayiwxw.com/	15	2021-11-28 12:28:55
81	ShuCW_COM	https://www.shucw.com/	15	2021-11-28 22:52:14
82	ZWWX_COM	https://www.zwwx.com	8	2021-11-28 23:03:22
71	BiQUQU_COM	http://www.biququ.com/	30	2021-09-12 17:50:57
83	84ZW_ME	http://www.81zw.me/	8	2021-12-19 14:48:03
84	81ZW_ME	http://www.81zw.me/	8	2021-12-21 19:47:15
87	DDXS_CC	https://www.ddxs.cc/	21	2022-01-25 21:55:31
88	23XSTxt_COM	https://www.23xstxt.com/	25	2022-02-06 22:17:53
89	LSTxt_CC	https://www.lstxt.cc/	25	2022-02-06 22:21:22
90	CITS0871_COM	http://www.cits0871.com/	21	2022-02-11 09:08:36
91	YQXS_CC	https://www.yqxs.cc/	25	2022-02-27 18:54:34
92	7722_ORG	https://www.7722.org/	25	2022-03-05 15:38:15
93	ZSSQ8_COM	https://www.zssq8.com	25	2022-03-13 22:37:40
94	DingDian6_COM	https://www.dingdian6.com/	36	2022-03-27 15:40:08
95	MayDayFans_COM	http://www.maydayfans.com/	37	2022-04-03 18:13:49
96	BiQuGeVip_COM	https://www.biqugevip.com	15	2022-04-10 22:37:58
97	VipKanShu_VIP	https://www.vipkanshu.vip	38	2022-04-17 00:21:22
98	TingShuGe_COM	https://www.tingshuge.com/	33	2022-04-17 00:36:44
99	TMWX_NET	https://www.tmwx.net/	33	2022-04-18 09:53:24
100	X23US_US	https://www.x23us.us/	13	2022-04-26 20:53:05
101	QingKanShu_CC	https://www.qingkanshu.cc/	33	2022-05-03 11:12:21
102	ShuQuGe_COM	https://www.shuquge.com	25	2022-05-14 01:22:39
103	YeTianLian_COM	http://www.yetianlian.com/	25	2022-05-22 11:34:17
104	IXS_LA	https://www.ixs.la/	8	2022-06-04 10:33:31
36	BiQuGeTv_COM	https://www.biqugetv.com/	32	2020-08-07 21:36:36
105	YWGGZY_COM	http://www.ywggzy.com/	39	2022-06-18 11:44:32
106	ASXS_COM	https://www.asxs.com/	40	2022-06-18 22:28:42
107	YQZWW_COM	https://www.yqzww.cc/	41	2022-07-17 10:24:53
108	ZWYDW_COM	https://www.zwydw.com	25	2022-07-28 20:45:14
109	XBookTXT_NET	https://www.xbooktxt.net/	42	2022-07-30 16:11:15
110	X81XZW_COM	https://www.x81xzw.com/	21	2022-08-13 12:30:16
111	XueSoo_COM	http://www.xuesoo.com/	31	2022-09-04 17:08:18
112	XHSXSW_COM	https://www.xhsxsw.com/	30	2022-09-11 17:35:23
113	XSYQ_CC	https://www.xsyq.cc/	25	2022-09-24 18:22:54
114	QiuShuGe_NET	http://www.qiushuge.net/	38	2022-10-09 19:19:03
115	IXSW_LA	https://www.ixsw.la	16	2022-10-17 00:35:12
116	XDingDianXSW_COM	https://www.xdingdianxsw.com/	43	2022-10-20 19:59:05
117	IShuQuGe_COM	https://www.ishuquge.com/	25	2022-10-26 20:34:38
118	TSXSW_NET	https://www.tsxsw.net/	44	2022-10-28 23:34:22
119	7722Bar_COM	https://www.7722bar.com/	25	2022-10-30 18:58:17
120	2Shu8_NET	https://www.2shu8.net/	45	2022-11-12 11:50:15
121	147XS_ORG	https://www.147xs.org/	30	2022-11-19 17:05:03
122	X222XS_COM	https://www.x222xs.com/	21	2022-12-02 21:29:25
123	121Du_CC	https://www.121du.cc/	47	2022-12-11 11:37:26
124	118Book_COM	https://www.118book.com/	15	2022-12-21 19:57:17
125	BQWo_CC	https://www.bqwo.cc/	25	2023-01-02 17:20:11
126	23XSTxt_NET	https://www.23xstxt.net	25	2023-01-07 16:26:06
127	77DuShu_LA	http://www.77dushu.la/	48	2023-01-23 17:07:16
128	XGeDaa_COM	https://www.xgedaa.com/	49	2023-01-27 15:43:54
129	GGZuo_COM	https://www.ggzuo.com/	50	2023-01-28 19:51:26
130	PaoShuBaXS_COM	http://www.paoshubaxs.com/	51	2023-02-02 21:05:43
131	HeTangCZJ_COM	https://wap.hetangczj.com/	52	2023-02-11 12:55:09
132	MHTXS_LA	https://www.mhtxs.la/	53	2023-02-26 13:22:18
133	DianJiangXin_COM	https://www.dianjiangxin.com/	54	2023-03-01 17:23:04
134	EPZW_COM	https://www.epzw.com/	44	2023-03-05 13:16:30
135	YoDu_ORG	https://www.yodu.org/	52	2023-03-11 09:00:33
136	IBiQuW_COM	http://www.ibiquw.com/	55	2023-03-11 09:11:39
137	HuJuGe_COM	http://www.hujuge.com/	56	2023-03-17 22:22:22
138	HuangYiXiaoShuo_COM	http://www.huangyixiaoshuo.com/	15	2023-03-20 21:58:19
139	XSWang_COM	https://www.xswang.com/	57	2023-05-14 16:44:49
140	IMianHuaTang_INFO	https://www.imianhuatang.info/	53	2023-06-24 10:22:58
141	XiaShuYun_COM	https://www.xiashuyun.com/	54	2023-07-09 12:59:52
142	IBiQuGe_INFO	https://www.ibiquge.info/	15	2023-07-15 23:36:18
143	SuiYeXS_COM	https://www.suiyuexs.com/	34	2023-08-31 21:35:01
144	YingXiongXS_COM	https://www.yingxiongxs.com/	58	2023-09-17 07:53:28
145	GBDZKZ_INFO	http://www.gdbzkz.info/	25	2023-10-23 21:10:28
146	26KS_ORG	http://www.26ks.org/	57	2024-01-04 22:10:37
\.


--
-- Data for Name: task; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.task (taskid, name, cron, taskswitch, classpath, loadmanner, description) FROM stdin;
3	test	0/10 * * * * ?	0	Test	0	test
2	获取书籍详情	0 6 0/2 * * ?	1	top.niandui.task.GetBookDetailTask	1	获取书籍详情任务
1	获取后续章节	0 16 0/1 * * ?	1	top.niandui.task.GetFollowUpChapterTask	1	获取后续章节的定时任务
\.


--
-- Name: seq_book; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_book', 309, true);


--
-- Name: seq_chapter; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

-- SELECT pg_catalog.setval('public.seq_chapter', 177570, true);


--
-- Name: seq_config; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_config', 58, true);


--
-- Name: seq_site; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_site', 146, true);


--
-- Name: seq_task; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_task', 3, true);


--
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (bookid);


--
-- Name: chapter chapter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chapter
    ADD CONSTRAINT chapter_pkey PRIMARY KEY (chapterid);


--
-- Name: config config_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config
    ADD CONSTRAINT config_pkey PRIMARY KEY (configid);


--
-- Name: proxy proxy_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proxy
    ADD CONSTRAINT proxy_pkey PRIMARY KEY (proxyid);


--
-- Name: site site_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.site
    ADD CONSTRAINT site_pkey PRIMARY KEY (siteid);


--
-- Name: task task_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT task_pkey PRIMARY KEY (taskid);


--
-- Name: paragraph_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_bookid_chapterid_seqid_idx ON ONLY public.paragraph USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h00_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h00_bookid_chapterid_seqid_idx ON public.paragraph_h00 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h01_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h01_bookid_chapterid_seqid_idx ON public.paragraph_h01 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h02_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h02_bookid_chapterid_seqid_idx ON public.paragraph_h02 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h03_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h03_bookid_chapterid_seqid_idx ON public.paragraph_h03 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h04_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h04_bookid_chapterid_seqid_idx ON public.paragraph_h04 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h05_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h05_bookid_chapterid_seqid_idx ON public.paragraph_h05 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h06_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h06_bookid_chapterid_seqid_idx ON public.paragraph_h06 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h07_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h07_bookid_chapterid_seqid_idx ON public.paragraph_h07 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h08_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h08_bookid_chapterid_seqid_idx ON public.paragraph_h08 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h09_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h09_bookid_chapterid_seqid_idx ON public.paragraph_h09 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h10_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h10_bookid_chapterid_seqid_idx ON public.paragraph_h10 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h11_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h11_bookid_chapterid_seqid_idx ON public.paragraph_h11 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h12_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h12_bookid_chapterid_seqid_idx ON public.paragraph_h12 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h13_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h13_bookid_chapterid_seqid_idx ON public.paragraph_h13 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h14_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h14_bookid_chapterid_seqid_idx ON public.paragraph_h14 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h15_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h15_bookid_chapterid_seqid_idx ON public.paragraph_h15 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h16_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h16_bookid_chapterid_seqid_idx ON public.paragraph_h16 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h17_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h17_bookid_chapterid_seqid_idx ON public.paragraph_h17 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h18_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h18_bookid_chapterid_seqid_idx ON public.paragraph_h18 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h19_bookid_chapterid_seqid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX paragraph_h19_bookid_chapterid_seqid_idx ON public.paragraph_h19 USING btree (bookid, chapterid, seqid);


--
-- Name: paragraph_h00_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h00_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h01_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h01_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h02_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h02_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h03_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h03_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h04_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h04_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h05_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h05_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h06_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h06_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h07_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h07_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h08_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h08_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h09_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h09_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h10_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h10_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h11_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h11_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h12_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h12_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h13_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h13_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h14_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h14_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h15_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h15_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h16_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h16_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h17_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h17_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h18_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h18_bookid_chapterid_seqid_idx;


--
-- Name: paragraph_h19_bookid_chapterid_seqid_idx; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.paragraph_bookid_chapterid_seqid_idx ATTACH PARTITION public.paragraph_h19_bookid_chapterid_seqid_idx;


--
-- PostgreSQL database dump complete
--

