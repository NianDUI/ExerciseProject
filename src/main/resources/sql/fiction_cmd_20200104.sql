--
-- PostgreSQL database dump
--

-- Dumped from database version 13.1
-- Dumped by pg_dump version 13.1

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

DROP TRIGGER "AUTO_INSERT_INTO_TD_AI" ON public.paragraph;
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
DROP TABLE public.paragraph;
DROP TABLE public.config;
DROP SEQUENCE public.seq_config;
DROP TABLE public.chapter;
DROP SEQUENCE public.seq_chapter;
DROP TABLE public.book;
DROP SEQUENCE public.seq_book;
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
-- Name: seq_book; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_book
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_book OWNER TO postgres;

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
    handlerinfo character varying(128) NOT NULL,
    siteid bigint NOT NULL,
    taskstatus integer DEFAULT 0 NOT NULL,
    taskswitch integer DEFAULT 1 NOT NULL
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
-- Name: seq_chapter; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_chapter
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_chapter OWNER TO postgres;

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


ALTER TABLE public.seq_config OWNER TO postgres;

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
    createtime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL,
    seqid bigint NOT NULL,
    content text NOT NULL,
    bookid bigint NOT NULL,
    chapterid bigint NOT NULL
);


ALTER TABLE public.paragraph OWNER TO postgres;

--
-- Name: TABLE paragraph; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.paragraph IS '段落';


--
-- Name: COLUMN paragraph.createtime; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.paragraph.createtime IS '创建时间';


--
-- Name: COLUMN paragraph.seqid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.paragraph.seqid IS '排序id';


--
-- Name: COLUMN paragraph.content; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.paragraph.content IS '段落内容';


--
-- Name: COLUMN paragraph.bookid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.paragraph.bookid IS '书籍id';


--
-- Name: COLUMN paragraph.chapterid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.paragraph.chapterid IS '章节id';

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


ALTER TABLE public.seq_site OWNER TO postgres;

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


ALTER TABLE public.seq_task OWNER TO postgres;

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
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book (bookid, name, url, configid, createtime, starturl, handlerinfo, siteid, taskstatus, taskswitch) FROM stdin;
62	末世之黑暗召唤师	http://www.biquge.info/3_3398/	13	2020-07-18 23:14:00	http://www.biquge.info/3_3398/1809204.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0
38	我的代码分身	https://www.biquge.tw/522_522678/	8	2020-05-10 15:55:23	https://www.biquge.tw/522_522678/2660223.html	{"titleType":1,"startIndex":0,"delimiter":" ","endType":0,"endCharacter":""}	10	0	0
97	诡秘之主	https://www.xsbiquge.com/15_15338	19	2020-11-27 19:02:38	https://www.xsbiquge.com/15_15338/8549128.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	39	0	0
43	科技巫师	https://www.biquge.lu/book/44014/	5	2020-05-30 00:10:09	https://www.biquge.lu/book/44014/16591510.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	2	0	0
68	绝世人妖养成系统	http://www.biquge.info/12_12112	13	2020-07-31 00:10:16	http://www.biquge.info/12_12112/5442063.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0
32	给女装大佬递茶	http://www.biquger.com/biquge/109593	14	2020-04-20 22:19:54	http://www.biquger.com/biquge/109593/41361767	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/"}	18	0	0
67	变身大小姐	http://www.biquger.com/biquge/92056	14	2020-07-30 23:41:49	http://www.biquger.com/biquge/92056/25488105	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/"}	18	0	0
56	仙界科技	http://www.biquge.info/40_40201/	13	2020-06-13 17:29:15	http://www.biquge.info/40_40201/1166140.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0
76	不正常的超凡世界	http://www.biquge.info/69_69817	13	2020-09-20 14:53:07	http://www.biquge.info/69_69817/13119688.html	{"titleType":1,"startIndex":0,"delimiter":"章 ","endType":0,"endCharacter":""}	14	0	0
49	妞非在下	http://www.biquge.info/3_3320/	13	2020-06-02 23:47:06	http://www.biquge.info/3_3320/1763403.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0
100	不会真有人觉得修仙难吧	http://www.biquge.info/93_93131	13	2020-12-07 22:40:50	http://www.biquge.info/93_93131/20270178.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	0
7	沧元图	https://www.biquge.lu/book/49717/	5	2020-04-06 19:50:53	https://www.biquge.lu/book/49717/525134674.html	{"titleType":0,"startIndex":0,"delimiter":"章 ","endType":0,"endCharacter":""}	2	0	0
80	变身之轮回境界	https://www.xsbiquge.com/1_1962	19	2020-10-20 21:56:04	https://www.xsbiquge.com/1_1962/3663179.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	39	0	0
105	这是我的冰汽时代	http://www.biquge.se/64890	15	2020-12-19 22:53:42	http://www.biquge.se/64890/74463055.html	{"titleType":2,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	34	0	1
89	我修仙有提示语	http://www.biquger.com/biquge	14	2020-11-13 19:20:51	http://www.biquger.com/biquge/131966/53626043	{"titleType":1,"startIndex":5,"delimiter":"章 ","endType":1,"endCharacter":"/"}	18	0	1
37	法爷永远是你大爷	https://www.biquge.lu/book/59464	5	2020-05-05 15:56:32	https://www.biquge.lu/book/59464/487314482.html	{"titleType":1,"startIndex":0,"delimiter":" ","endType":0,"endCharacter":""}	2	0	1
55	咸鱼的科技直播间	https://www.boquge.com/book/118730/	20	2020-06-06 23:57:02	https://www.boquge.com/book/118730/168936226.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	25	0	1
23	魔法的学术时代	http://www.biquger.com/biquge/112679/	14	2020-04-10 10:39:09	http://www.biquger.com/biquge/112679/45424996	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/"}	18	0	1
42	重生过去当传奇	http://www.biquge.info/71_71007/	13	2020-05-24 10:28:36	http://www.biquge.info/71_71007/13420799.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	1
79	攀科技真的好难啊	https://www.booktxt.net/0_23	19	2020-10-18 18:34:05	https://www.booktxt.net/0_23/530876129.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	23	0	1
75	我能魔改黑科技	https://www.booktxt.com/37_37352	16	2020-09-14 21:30:24	https://www.booktxt.com/37_37352/170099.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	28	0	1
90	重生之北国科技	https://www.bqg99.cc/book/2019788102	5	2020-11-15 15:01:44	https://www.bqg99.cc/book/2019788102/504073701.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	16	0	1
77	腾飞我的航空时代	https://www.biduo.cc/biquge/53_53393/	10	2020-09-26 15:35:02	https://www.biduo.cc/biquge/53_53393/c22077441.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	11	0	1
98	从零开始	https://www.xsbiquge.com/0_114/	19	2020-11-27 19:33:49	https://www.xsbiquge.com/0_114/5824807.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	39	0	0
88	变身之葵花真解	https://www.boquge.com/book/54325	20	2020-11-09 19:36:04	https://www.boquge.com/book/54325/142646578.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	25	0	0
110	重生变身之初始	http://www.biquge.se/14042/	15	2021-01-01 17:53:26	http://www.biquge.se/14042/30129720.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	34	0	0
58	小阁老	https://www.booktxt.com/28_28228/	16	2020-06-20 22:04:49	https://www.booktxt.com/28_28228/15630.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	28	0	1
29	泡面首富	http://www.biquges.com/49_49188/index.html	15	2020-04-16 16:48:33	http://www.biquges.com/49_49188/24396943.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"index.html","proxyid":0}	29	0	1
84	人在大唐已被退学	http://www.biquge.info/92_92784	13	2020-10-29 20:16:47	http://www.biquge.info/92_92784/20156879.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	1
61	太乙	http://www.biquge.info/2_2801	13	2020-07-15 21:23:53	http://www.biquge.info/2_2801/9468276.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	1
102	女帝背后的男人	http://www.biquge.info/98_98084	13	2020-12-13 20:58:37	http://www.biquge.info/98_98084/21942981.html	{"titleType":1,"startIndex":0,"delimiter":".","endType":0,"endCharacter":""}	14	0	1
99	千秋不死人	https://www.biqubao.com/book/36074	8	2020-11-30 21:48:13	https://www.biqubao.com/book/36074/18251248.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	31	0	1
59	佛系科技	http://www.biquge.info/86_86788	13	2020-06-26 22:33:41	http://www.biquge.info/86_86788/18530447.html	{"titleType":1,"startIndex":0,"delimiter":" ","endType":0,"endCharacter":""}	14	0	1
40	信息全知者	https://www.biqugexx.com/127_127399/	15	2020-05-15 00:03:50	https://www.biqugexx.com/127_127399/33738497.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	19	0	1
51	我有超体U盘	https://www.biquge.lu/book/67863/	5	2020-06-04 12:45:08	https://www.biquge.lu/book/67863/499829444.html	{"titleType":1,"startIndex":0,"delimiter":"-","endType":0,"endCharacter":""}	2	0	1
60	魔幻科技工业	https://www.boquge.com/book/121068/	20	2020-07-12 20:42:51	https://www.boquge.com/book/121068/169871249.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	25	0	1
36	炮台法师	https://www.biqubao.com/book/32998/	8	2020-05-02 15:10:38	https://www.biqubao.com/book/32998/16669021.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	31	0	1
101	不会真有人觉得修仙难吧	https://www.biquge11.com/36_36402/	15	2020-12-07 22:48:08	https://www.biquge11.com/36_36402/16160714.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	43	0	1
35	我真是修炼天才	https://www.biqubao.com/book/34827/	8	2020-04-26 20:06:25	https://www.biqubao.com/book/34827/17581587.html	{"titleType":1,"startIndex":0,"delimiter":" ","endType":0,"endCharacter":""}	31	0	1
94	东京绅士物语	http://www.biquge.info/8_8655	13	2020-11-21 13:46:54	http://www.biquge.info/8_8655/4152164.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	1
71	大唐第一长子	https://www.booktxt.com/35_35812	16	2020-08-17 18:53:47	https://www.booktxt.com/35_35812/258946.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	28	0	1
96	变成超人是什么体验之超人日记	https://www.lvsetxt.com/books/22/22215/586140274.html	25	2020-11-25 10:49:12	https://www.lvsetxt.com/books/22/22215/586140274.html	{"titleType":1,"startIndex":0,"delimiter":"、","endType":1,"endCharacter":"22215.html"}	42	0	1
9	重写科技格局	https://www.biquge.tw/489_489326/	8	2020-04-07 08:58:49	https://www.biquge.tw/489_489326/2594513.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	10	0	1
54	咸鱼的科技直播间	https://www.biquge.lu/book/67081/?ivk_sa=1023337b	5	2020-06-06 23:52:23	https://www.biquge.lu/book/67081/24254224.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	2	0	1
87	传奇1997	https://www.biqubao.com/book/33515	8	2020-11-07 16:24:23	https://www.biqubao.com/book/33515/16936537.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	31	0	1
86	凶猛道侣也重生了	https://www.booktxt.net/2_2535	19	2020-11-05 08:44:28	https://www.booktxt.net/2_2535/541680501.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	23	0	1
73	芯片产业帝国	https://www.5atxt.com/27_27803	19	2020-08-23 09:42:09	https://www.5atxt.com/27_27803/24320024.html	{"titleType":1,"startIndex":1,"delimiter":"节 ","endType":0,"endCharacter":""}	38	0	1
64	奥术之主	https://www.78zw.com/31_31962/	19	2020-07-23 22:24:55	https://www.78zw.com/31_31962/21365215.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	33	0	1
81	新顺1730	https://www.kuxiaoshuo.com	15	2020-10-25 17:25:15	https://www.kuxiaoshuo.com/130_130633/170337677.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	40	0	1
31	修仙从沙漠开始	https://www.biquge.lu/book/67051	5	2020-04-19 23:31:36	https://www.biquge.lu/book/67051/502801184.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	2	0	1
93	全球游戏进化	https://www.biduo.cc/biquge/60_60481	10	2020-11-20 19:29:34	https://www.biduo.cc/biquge/60_60481/c88195.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	11	0	1
24	奥术起源	http://www.mcmssc.com/41_41538/	22	2020-04-12 18:19:13	http://www.mcmssc.com/41_41538/20899313.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	27	0	1
70	重生异界做游戏	http://www.biquge.se/55965	19	2020-08-07 21:34:50	http://www.biquge.se/55965/69888060.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	34	0	1
103	这个大佬有点苟	https://www.biduo.cc/biquge/0_248	10	2020-12-13 21:50:07	https://www.biduo.cc/biquge/0_248/c35804.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	11	0	1
63	面向阵法修仙	https://www.boquge.com/book/120686	20	2020-07-20 21:37:56	https://www.boquge.com/book/120686/170069677.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	25	0	1
108	这是我的冰汽时代	http://www.bqge.com/197_197193	15	2020-12-19 23:22:22	http://www.bqge.com/197_197193/59901598.html	{"titleType":2,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":"0"}	45	0	1
57	最初的巫师	https://www.biquge.lu/book/62758	5	2020-06-15 23:49:09	https://www.biquge.lu/book/62758/463905595.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	2	0	1
34	回眸1991	http://www.biquwo.org/bqw101958/	8	2020-04-25 16:45:31	http://www.biquwo.org/bqw101958/6063396.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	30	0	1
69	重生之网络争霸	http://www.biquge.info/75_75928/	13	2020-08-07 21:33:36	http://www.biquge.info/75_75928/15136955.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	1
95	种田系修仙	http://www.biquge.info/89_89088	13	2020-11-23 08:47:58	http://www.biquge.info/89_89088/19120839.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	1
104	这个法师很危险	https://www.xsbiquge.com/96_96947	19	2020-12-16 21:29:30	https://www.xsbiquge.com/96_96947/217607.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	39	0	1
82	我若修仙法力齐天	http://www.biquge.info/91_91040	13	2020-10-26 12:29:31	http://www.biquge.info/91_91040/19652865.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	1
85	创造沙盘世界	https://www.bqg99.cc/book/2014569385	5	2020-11-01 14:35:02	https://www.bqg99.cc/book/2014569385/461396298.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	16	0	1
74	银龙的黑科技	http://www.biquge.info/82_82740	13	2020-08-23 13:46:27	http://www.biquge.info/82_82740/17265615.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	1
78	我的隐身战斗姬	http://www.biquge.info/75_75805	13	2020-10-08 12:19:46	http://www.biquge.info/75_75805/15102211.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}	14	0	1
109	我在魔法世界开创互联网时代	http://www.biquge.info/96_96355	13	2020-12-29 20:32:54	http://www.biquge.info/96_96355/21314072.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"","proxyid":0}	14	0	1
65	魔法塔的星空	http://www.biquge.se/21078	19	2020-07-25 09:45:03	http://www.biquge.se/21078/38538728.html	{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":"/"}	34	0	1
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
15	BiQuGexx_COM	//div[@class='bookname']/h1/text()	2	//div[@id='content']/text()	2	0	0	//div[@class='bottem2']//a	3
10	BiDuo_CC	//div[@class='bookname']/h1/text()	2	//div[@id='content']/text()	2	0	0	//div[@class='bottem2']//a	2
19	BookTXT_NET	//div[@class='bookname']/h1/text()	2	//div[@id='content']/text()	2	0	0	//div[@class='bottem2']//a	2
5	BiQuGe_LU	//div[@id='content']/../h1/text()	2	//div[@id='content']/text()	2	0	-1	//div[@class='page_chapter']//a	2
25	LvSeTxt_COM	//div[@class='content']/h1/text()	2	//div[@id='content']/text()	2	0	-2	//div[@class='page_chapter']//a	2
\.


--
-- Data for Name: proxy; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.proxy (proxyid, name, host, port, type) FROM stdin;
1	Socks代理	127.0.0.1	10080	1
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
36	BiQuGeTv_COM	https://www.biqugetv.com/	8	2020-08-07 21:36:36
24	BxQuGe_COM	http://www.bxquge.com	15	2020-04-09 17:57:44
29	BiQuGes_COM	http://www.biquges.com/	15	2020-04-16 16:46:52
33	78ZW_COM	https://www.78zw.com	15	2020-07-25 10:01:14
34	BiQuGe_SE	http://www.biquge.se/	15	2020-07-25 10:05:58
37	DingDiann_COM	https://www.dingdiann.com/	15	2020-08-23 09:09:22
38	5ATXT_COM	https://www.5atxt.com/	15	2020-08-23 09:41:15
39	XSBiQuGe_COM	https://www.xsbiquge.com/	19	2020-10-20 21:55:14
40	KuXiaoShuo_COM	https://www.kuxiaoshuo.com/	15	2020-10-25 17:24:35
42	LvSeTxt_COM	https://www.lvsetxt.com/	25	2020-11-25 10:48:09
43	BiQuGe11_COM	https://www.biquge11.com/	15	2020-12-07 22:47:35
45	GQGe_COM	http://www.bqge.com/	15	2020-12-19 23:21:55
\.


--
-- Data for Name: task; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.task (taskid, name, cron, taskswitch, classpath, loadmanner, description) FROM stdin;
1	获取后续章节	0 16 0/6 * * ?	1	top.niandui.task.GetFollowUpChapterTask	1	获取后续章节的定时任务
3	test	0/10 * * * * ?	0	Test	0	test
\.


--
-- Name: seq_book; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_book', 110, true);


--
-- Name: seq_config; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_config', 26, true);


--
-- Name: seq_site; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_site', 45, true);


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
-- Name: paragraph AUTO_INSERT_INTO_TD_AI; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER "AUTO_INSERT_INTO_TD_AI" BEFORE INSERT ON public.paragraph FOR EACH ROW EXECUTE FUNCTION public."AUTO_INSERT_INTO_TD_AI"('createtime');


--
-- PostgreSQL database dump complete
--

