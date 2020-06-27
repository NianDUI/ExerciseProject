/*
 Navicat Premium Data Transfer

 Source Server         : 39.105.204.226_5432
 Source Server Type    : PostgreSQL
 Source Server Version : 120002
 Source Host           : 39.105.204.226:5432
 Source Catalog        : fiction
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 120002
 File Encoding         : 65001

 Date: 27/06/2020 12:32:16
*/


-- ----------------------------
-- Sequence structure for seq_book
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."seq_book";
CREATE SEQUENCE "public"."seq_book" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for seq_chapter
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."seq_chapter";
CREATE SEQUENCE "public"."seq_chapter" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for seq_site
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."seq_site";
CREATE SEQUENCE "public"."seq_site" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS "public"."book";
CREATE TABLE "public"."book" (
  "bookid" int8 NOT NULL DEFAULT nextval('seq_book'::regclass),
  "name" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "url" varchar(128) COLLATE "pg_catalog"."default",
  "configid" int8 NOT NULL DEFAULT nextval('seq_book'::regclass),
  "createtime" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  "starturl" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "handlerinfo" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "siteid" int8 NOT NULL,
  "taskstatus" int4 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."book"."bookid" IS '书籍id';
COMMENT ON COLUMN "public"."book"."name" IS '书籍名称';
COMMENT ON COLUMN "public"."book"."url" IS '书籍链接';
COMMENT ON COLUMN "public"."book"."configid" IS '书籍配置';
COMMENT ON COLUMN "public"."book"."createtime" IS '创建时间';
COMMENT ON COLUMN "public"."book"."starturl" IS '起始章节链接';
COMMENT ON COLUMN "public"."book"."handlerinfo" IS '处理信息';
COMMENT ON COLUMN "public"."book"."siteid" IS '站点id';
COMMENT ON COLUMN "public"."book"."taskstatus" IS '任务状态：0无，1重新获取全部，2获取后续章节，3重新获取单章';
COMMENT ON TABLE "public"."book" IS '书籍';

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO "public"."book" VALUES (38, '我的代码分身', 'https://www.biquge.tw/522_522678/', 8, '2020-05-10 15:55:23', 'https://www.biquge.tw/522_522678/2660223.html', '{"titleType":1,"startIndex":0,"delimiter":" ","endType":0,"endCharacter":""}', 10, 0);
INSERT INTO "public"."book" VALUES (36, '炮台法师', 'https://www.biqubao.com/book/32998/', 8, '2020-05-02 15:10:38', 'https://www.biqubao.com/book/32998/16669021.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}', 31, 0);
INSERT INTO "public"."book" VALUES (54, '咸鱼的科技直播间', 'https://www.biquge.lu/book/67081/?ivk_sa=1023337b', 5, '2020-06-06 23:52:23', 'https://www.biquge.lu/book/67081/24254224.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}', 2, 0);
INSERT INTO "public"."book" VALUES (55, '咸鱼的科技直播间', 'https://www.boquge.com/book/118730/', 20, '2020-06-06 23:57:02', 'https://www.boquge.com/book/118730/168936226.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}', 25, 0);
INSERT INTO "public"."book" VALUES (43, '科技巫师', 'https://www.biquge.lu/book/44014/', 5, '2020-05-30 00:10:09', 'https://www.biquge.lu/book/44014/16591510.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}', 2, 0);
INSERT INTO "public"."book" VALUES (59, '佛系科技', 'http://www.biquge.info/86_86788', 13, '2020-06-26 22:33:41', 'http://www.biquge.info/86_86788/18530447.html', '{"titleType":1,"startIndex":0,"delimiter":" ","endType":0,"endCharacter":""}', 14, 0);
INSERT INTO "public"."book" VALUES (23, '魔法的学术时代', 'http://www.biquger.com/biquge/112679/', 14, '2020-04-10 10:39:09', 'http://www.biquger.com/biquge/112679/45424996', '{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/"}', 18, 0);
INSERT INTO "public"."book" VALUES (56, '仙界科技', 'http://www.biquge.info/40_40201/', 13, '2020-06-13 17:29:15', 'http://www.biquge.info/40_40201/1166140.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}', 14, 0);
INSERT INTO "public"."book" VALUES (32, '给女装大佬递茶', 'http://www.biquger.com/biquge/109593', 14, '2020-04-20 22:19:54', 'http://www.biquger.com/biquge/109593/41361767', '{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/"}', 18, 0);
INSERT INTO "public"."book" VALUES (35, '我真是修炼天才', 'https://www.biqubao.com/book/34827/', 8, '2020-04-26 20:06:25', 'https://www.biqubao.com/book/34827/17581587.html', '{"titleType":1,"startIndex":0,"delimiter":" ","endType":0,"endCharacter":""}', 31, 0);
INSERT INTO "public"."book" VALUES (9, '重写科技格局', 'https://www.biquge.tw/489_489326/', 8, '2020-04-07 08:58:49', 'https://www.biquge.tw/489_489326/2594513.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}', 10, 0);
INSERT INTO "public"."book" VALUES (58, '小阁老', 'https://www.booktxt.com/28_28228/', 16, '2020-06-20 22:04:49', 'https://www.booktxt.com/28_28228/15630.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}', 28, 0);
INSERT INTO "public"."book" VALUES (51, '我有超体U盘', 'https://www.biquge.lu/book/67863/', 5, '2020-06-04 12:45:08', 'https://www.biquge.lu/book/67863/499829444.html', '{"titleType":1,"startIndex":0,"delimiter":"-","endType":0,"endCharacter":""}', 2, 0);
INSERT INTO "public"."book" VALUES (42, '重生过去当传奇', 'http://www.biquge.info/71_71007/', 13, '2020-05-24 10:28:36', 'http://www.biquge.info/71_71007/13420799.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}', 14, 0);
INSERT INTO "public"."book" VALUES (7, '沧元图', 'https://www.biquge.lu/book/49717/', 5, '2020-04-06 19:50:53', 'https://www.biquge.lu/book/49717/525134674.html', '{"titleType":0,"startIndex":1,"delimiter":"","endType":0,"endCharacter":""}', 2, 0);
INSERT INTO "public"."book" VALUES (34, '回眸1991', 'http://www.biquwo.org/bqw101958/', 8, '2020-04-25 16:45:31', 'http://www.biquwo.org/bqw101958/6063396.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}', 30, 0);
INSERT INTO "public"."book" VALUES (29, '泡面首富', 'http://www.biquges.com/49_49188/index.html', 19, '2020-04-16 16:48:33', 'http://www.biquges.com/49_49188/24396943.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"index.html"}', 29, 0);
INSERT INTO "public"."book" VALUES (31, '修仙从沙漠开始', 'https://www.biquge.lu/book/67051', 5, '2020-04-19 23:31:36', 'https://www.biquge.lu/book/67051/502801184.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}', 2, 0);
INSERT INTO "public"."book" VALUES (57, '最初的巫师', 'https://www.biquge.lu/book/62758', 5, '2020-06-15 23:49:09', 'https://www.biquge.lu/book/62758/463905595.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}', 2, 0);
INSERT INTO "public"."book" VALUES (24, '奥术起源', 'http://www.mcmssc.com/41_41538/', 22, '2020-04-12 18:19:13', 'http://www.mcmssc.com/41_41538/20899313.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}', 27, 0);
INSERT INTO "public"."book" VALUES (40, '信息全知者', 'https://www.biqugexx.com/127_127399/', 15, '2020-05-15 00:03:50', 'https://www.biqugexx.com/127_127399/33738497.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}', 19, 0);
INSERT INTO "public"."book" VALUES (49, '妞非在下', 'http://www.biquge.info/3_3320/', 13, '2020-06-02 23:47:06', 'http://www.biquge.info/3_3320/1763403.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}', 14, 0);
INSERT INTO "public"."book" VALUES (37, '法爷永远是你大爷', 'https://www.biquge.lu/book/59464', 5, '2020-05-05 15:56:32', 'https://www.biquge.lu/book/59464/487314482.html', '{"titleType":1,"startIndex":0,"delimiter":" ","endType":0,"endCharacter":""}', 2, 0);

-- ----------------------------
-- Table structure for chapter
-- ----------------------------
DROP TABLE IF EXISTS "public"."chapter";
CREATE TABLE "public"."chapter" (
  "chapterid" int8 NOT NULL DEFAULT nextval('seq_chapter'::regclass),
  "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "bookid" int8 NOT NULL,
  "configid" int8,
  "rawname" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "createtime" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  "seqid" int8 NOT NULL,
  "url" varchar(128) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."chapter"."chapterid" IS '章节id';
COMMENT ON COLUMN "public"."chapter"."name" IS '章节名称';
COMMENT ON COLUMN "public"."chapter"."bookid" IS '书籍id';
COMMENT ON COLUMN "public"."chapter"."configid" IS '配置id';
COMMENT ON COLUMN "public"."chapter"."rawname" IS '原章节名称';
COMMENT ON COLUMN "public"."chapter"."createtime" IS '创建时间';
COMMENT ON COLUMN "public"."chapter"."seqid" IS '排序id';
COMMENT ON COLUMN "public"."chapter"."url" IS '章节链接';
COMMENT ON TABLE "public"."chapter" IS '章节';

-- ----------------------------
-- Records of chapter
-- ----------------------------

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS "public"."config";
CREATE TABLE "public"."config" (
  "configid" int8 NOT NULL DEFAULT nextval('seq_book'::regclass),
  "name" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "titlematch" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "titlelnnum" int4 NOT NULL,
  "conmatch" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "conlnnum" int4 NOT NULL,
  "startoffset" int4 NOT NULL,
  "endoffset" int4 NOT NULL,
  "amatch" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "nexta" int4 NOT NULL
)
;
COMMENT ON COLUMN "public"."config"."configid" IS '配置id';
COMMENT ON COLUMN "public"."config"."name" IS '配置名称';
COMMENT ON COLUMN "public"."config"."titlematch" IS '标题匹配路径';
COMMENT ON COLUMN "public"."config"."titlelnnum" IS '标题后换行数量';
COMMENT ON COLUMN "public"."config"."conmatch" IS '内容匹配路径';
COMMENT ON COLUMN "public"."config"."conlnnum" IS '内容后换行数量';
COMMENT ON COLUMN "public"."config"."startoffset" IS '内容开始索引偏移量';
COMMENT ON COLUMN "public"."config"."endoffset" IS '内容结束索引偏移量';
COMMENT ON COLUMN "public"."config"."amatch" IS '跳转超链接匹配路径';
COMMENT ON COLUMN "public"."config"."nexta" IS '下一页超链接索引';
COMMENT ON TABLE "public"."config" IS '配置';

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO "public"."config" VALUES (5, 'BiQuGe_LU', '//div[@id=''content'']/../h1/text()', 2, '//div[@id=''content'']/text()', 2, 0, -2, '//div[@class=''page_chapter'']//a', 2);
INSERT INTO "public"."config" VALUES (8, 'BiQuGe_TW', '//div[@class=''bookname'']/h1/text()', 2, '//div[@id=''content'']/text()', 2, 0, 0, '//div[@class=''bottem2'']//a', 2);
INSERT INTO "public"."config" VALUES (10, 'BiDuo_CC', '//div[@class=''bookname'']/h1/text()', 2, '//div[@id=''content'']/text()', 2, 0, -3, '//div[@class=''bottem2'']//a', 2);
INSERT INTO "public"."config" VALUES (11, 'BiQuGe7_COM', '//div[@class=''content'']/h1/text()', 2, '//div[@id=''content'']/text()', 2, 0, -1, '//div[@class=''page_chapter'']//a', 2);
INSERT INTO "public"."config" VALUES (12, 'BiQuGe_COM', '//div[@class=''bookname'']/h1/text()', 2, '//div[@id=''content'']/text()', 2, 0, -3, '//div[@class=''bottem2'']//a', 3);
INSERT INTO "public"."config" VALUES (13, 'BiQuGe_INFO', '//div[@class=''bookname'']/h1/text()', 2, '//div[@id=''content'']/text()', 2, 0, 0, '//div[@class=''bottem'']//a', 3);
INSERT INTO "public"."config" VALUES (16, 'BiQuGg_TW', '//div[@class=''bookname'']/h1/text()', 2, '//div[@id=''content'']/text()', 2, 0, 0, '//div[@class=''bottem2'']//a', 2);
INSERT INTO "public"."config" VALUES (18, 'BiQuWu_COM', '//div[@class=''bookname'']/h1/text()', 2, '//div[@id=''content'']/text()', 1, 0, 0, '//div[@class=''bottem2'']//a', 3);
INSERT INTO "public"."config" VALUES (19, 'BookTXT_NET', '//div[@class=''bookname'']/h1/text()', 2, '//div[@id=''content'']/text()', 2, 0, 0, '//div[@class=''bottem2'']//a', 3);
INSERT INTO "public"."config" VALUES (20, 'BoQuGe_COM', '//div[@id=''h1'']/h1/text()', 2, '//div[@id=''txtContent'']/text()', 2, 0, 0, '//div[@class=''clearfix'']/ul/li/a', 6);
INSERT INTO "public"."config" VALUES (21, 'BQG5_CC', '//div[@class=''bookname'']/h1/text()', 2, '//div[@id=''content'']/p/text()', 2, 0, 0, '//div[@class=''bottem2'']//a', 3);
INSERT INTO "public"."config" VALUES (22, 'MCMSSC_COM', '//div[@class=''bookname'']/h1/text()', 0, '//div[@id=''content'']/text()', 2, 0, 0, '//div[@class=''bottem2'']//a', 3);
INSERT INTO "public"."config" VALUES (14, 'BiQuGer_COM', '//div[@class=''bookname'']/h1/text()', 2, '//div[@class=''content'']/text()', 2, 0, 0, '//div[@class=''bottem'']//a', 3);
INSERT INTO "public"."config" VALUES (15, 'BiQuGexx_COM', '//div[@class=''bookname'']/h1/text()', 2, '//div[@id=''content'']/text()', 2, 0, 0, '//div[@class=''bottem2'']//a', 3);

-- ----------------------------
-- Table structure for paragraph
-- ----------------------------
DROP TABLE IF EXISTS "public"."paragraph";
CREATE TABLE "public"."paragraph" (
  "createtime" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  "seqid" int8 NOT NULL,
  "content" varchar(512) COLLATE "pg_catalog"."default" NOT NULL,
  "bookid" int8 NOT NULL,
  "chapterid" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."paragraph"."createtime" IS '创建时间';
COMMENT ON COLUMN "public"."paragraph"."seqid" IS '排序id';
COMMENT ON COLUMN "public"."paragraph"."content" IS '段落内容';
COMMENT ON COLUMN "public"."paragraph"."bookid" IS '书籍id';
COMMENT ON COLUMN "public"."paragraph"."chapterid" IS '章节id';
COMMENT ON TABLE "public"."paragraph" IS '段落';

-- ----------------------------
-- Records of paragraph
-- ----------------------------

-- ----------------------------
-- Table structure for paragraph_202005
-- ----------------------------
DROP TABLE IF EXISTS "public"."paragraph_202005";
CREATE TABLE "public"."paragraph_202005" (
  "createtime" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  "seqid" int8 NOT NULL,
  "content" varchar(512) COLLATE "pg_catalog"."default" NOT NULL,
  "bookid" int8 NOT NULL,
  "chapterid" int8 NOT NULL
)
INHERITS ("public"."paragraph")
;

-- ----------------------------
-- Records of paragraph_202005
-- ----------------------------

-- ----------------------------
-- Table structure for paragraph_202006
-- ----------------------------
DROP TABLE IF EXISTS "public"."paragraph_202006";
CREATE TABLE "public"."paragraph_202006" (
  "createtime" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  "seqid" int8 NOT NULL,
  "content" varchar(512) COLLATE "pg_catalog"."default" NOT NULL,
  "bookid" int8 NOT NULL,
  "chapterid" int8 NOT NULL
)
INHERITS ("public"."paragraph")
;

-- ----------------------------
-- Records of paragraph_202006
-- ----------------------------

-- ----------------------------
-- Table structure for site
-- ----------------------------
DROP TABLE IF EXISTS "public"."site";
CREATE TABLE "public"."site" (
  "siteid" int8 NOT NULL DEFAULT nextval('seq_site'::regclass),
  "name" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "url" varchar(128) COLLATE "pg_catalog"."default",
  "configid" int8,
  "createtime" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(0)
)
;
COMMENT ON COLUMN "public"."site"."siteid" IS '站点id';
COMMENT ON COLUMN "public"."site"."name" IS '站点名称';
COMMENT ON COLUMN "public"."site"."url" IS '站点链接';
COMMENT ON COLUMN "public"."site"."configid" IS '配置id';
COMMENT ON COLUMN "public"."site"."createtime" IS '创建时间';
COMMENT ON TABLE "public"."site" IS '站点';

-- ----------------------------
-- Records of site
-- ----------------------------
INSERT INTO "public"."site" VALUES (2, 'BiQuGe_LU', 'https://www.biquge.lu/', 5, '2020-03-28 17:27:24');
INSERT INTO "public"."site" VALUES (10, 'BiQuGe_TW', 'https://www.biquge.tw', 8, '2020-04-07 14:27:49');
INSERT INTO "public"."site" VALUES (11, 'BiDuo_CC', 'http://www.biduo.cc', 10, '2020-04-09 17:50:26');
INSERT INTO "public"."site" VALUES (12, 'BiQuGe7_COM', 'http://www.biquge7.com', 11, '2020-04-09 17:50:44');
INSERT INTO "public"."site" VALUES (13, 'BiQuGe_COM', 'http://www.biqige.com', 12, '2020-04-09 17:51:03');
INSERT INTO "public"."site" VALUES (14, 'BiQuGe_INFO', 'http://www.biquge.info', 13, '2020-04-09 17:51:31');
INSERT INTO "public"."site" VALUES (15, 'BiQuMo_COM', 'http://www.biqumo.com', 5, '2020-04-09 17:52:36');
INSERT INTO "public"."site" VALUES (16, 'BQG99_CC', 'http://www.bqg99.cc', 5, '2020-04-09 17:52:58');
INSERT INTO "public"."site" VALUES (17, 'E8ZW_COM', 'http://www.e8zw.com', 8, '2020-04-09 17:53:35');
INSERT INTO "public"."site" VALUES (18, 'BiQuGer_COM', 'http://www.biquger.com', 14, '2020-04-09 17:53:53');
INSERT INTO "public"."site" VALUES (19, 'BiQuGexx_COM', 'http://www.biqugexx.com', 15, '2020-04-09 17:54:11');
INSERT INTO "public"."site" VALUES (20, 'BiQuGg_TW', 'http://www.biqugg.com', 16, '2020-04-09 17:54:30');
INSERT INTO "public"."site" VALUES (21, 'BiQuKan_COM', 'http://www.biqukan.com', 5, '2020-04-09 17:54:47');
INSERT INTO "public"."site" VALUES (22, 'BiQuWu_COM', 'http://www.biqiwu.com', 18, '2020-04-09 17:56:39');
INSERT INTO "public"."site" VALUES (23, 'BookTXT_NET', 'http://www.booktxt.net', 19, '2020-04-09 17:57:06');
INSERT INTO "public"."site" VALUES (24, 'BxQuGe_COM', 'http://www.bxquge.com', 19, '2020-04-09 17:57:44');
INSERT INTO "public"."site" VALUES (25, 'BoQuGe_COM', 'http://www.boquge.com', 20, '2020-04-09 17:58:06');
INSERT INTO "public"."site" VALUES (26, 'BQG5_CC', 'http://www.bqg5.cc', 21, '2020-04-09 17:58:21');
INSERT INTO "public"."site" VALUES (27, 'MCMSSC_COM', 'http://www.mcmssc.com', 22, '2020-04-09 17:58:40');
INSERT INTO "public"."site" VALUES (28, 'BookTXT_COM', 'https://www.booktxt.com/', 16, '2020-04-12 18:26:42');
INSERT INTO "public"."site" VALUES (29, 'BiQuGes_COM', 'http://www.biquges.com/', 19, '2020-04-16 16:46:52');
INSERT INTO "public"."site" VALUES (30, 'BiQuWo_ORG', 'http://www.biquwo.org/', 8, '2020-04-25 16:44:43');
INSERT INTO "public"."site" VALUES (31, 'BiQuBao_COM', 'https://www.biqubao.com', 8, '2020-04-26 20:05:03');

-- ----------------------------
-- Function structure for AUTO_INSERT_INTO_TD_AI
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."AUTO_INSERT_INTO_TD_AI"();
CREATE OR REPLACE FUNCTION "public"."AUTO_INSERT_INTO_TD_AI"()
  RETURNS "pg_catalog"."trigger" AS $BODY$
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
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."seq_book"', 60, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."seq_chapter"', 17086, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."seq_site"', 33, true);

-- ----------------------------
-- Primary Key structure for table book
-- ----------------------------
ALTER TABLE "public"."book" ADD CONSTRAINT "book_pkey" PRIMARY KEY ("bookid");

-- ----------------------------
-- Primary Key structure for table chapter
-- ----------------------------
ALTER TABLE "public"."chapter" ADD CONSTRAINT "chapter_pkey" PRIMARY KEY ("chapterid");

-- ----------------------------
-- Primary Key structure for table config
-- ----------------------------
ALTER TABLE "public"."config" ADD CONSTRAINT "config_pkey" PRIMARY KEY ("configid");

-- ----------------------------
-- Triggers structure for table paragraph
-- ----------------------------
CREATE TRIGGER "AUTO_INSERT_INTO_TD_AI" BEFORE INSERT ON "public"."paragraph"
FOR EACH ROW
EXECUTE PROCEDURE "public"."AUTO_INSERT_INTO_TD_AI"();

-- ----------------------------
-- Primary Key structure for table site
-- ----------------------------
ALTER TABLE "public"."site" ADD CONSTRAINT "site_pkey" PRIMARY KEY ("siteid");
