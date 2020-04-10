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

 Date: 10/04/2020 09:03:25
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
COMMENT ON COLUMN "public"."book"."taskstatus" IS '任务状态：0无，1重新获取，2获取后续';
COMMENT ON TABLE "public"."book" IS '书籍';

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO "public"."book" VALUES (7, '沧元图', 'https://www.biquge.lu/book/49717/', 5, '2020-04-06 19:50:53', 'https://www.biquge.lu/book/49717/535396205.html', '{"titleType":0,"startIndex":1,"delimiter":"","endType":0,"endCharacter":""}', 2, 0);
INSERT INTO "public"."book" VALUES (9, '重写科技格局', 'https://www.biquge.tw/489_489326/', 8, '2020-04-07 08:58:49', 'https://www.biquge.tw/489_489326/2594513.html', '{"titleType":0,"startIndex":0,"delimiter":"","endType":0,"endCharacter":""}', 10, 0);
INSERT INTO "public"."book" VALUES (23, '魔法的学术时代', 'http://www.biquger.com/biquge/112679/', 14, '2020-04-10 10:39:09', 'http://www.biquger.com/biquge/112679/46896120', '{"titleType":0,"startIndex":0,"delimiter":"","endType":1,"endCharacter":"/"}', 18, 0);

-- ----------------------------
-- Table structure for chapter
-- ----------------------------
DROP TABLE IF EXISTS "public"."chapter";
CREATE TABLE "public"."chapter" (
  "chapterid" int8 NOT NULL DEFAULT nextval('seq_chapter'::regclass),
  "name" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "bookid" int8 NOT NULL,
  "configid" int8,
  "rawname" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
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
INSERT INTO "public"."config" VALUES (15, 'BiQuGexx_COM', '//div[@class=''bookname'']/h1/text()', 0, '//div[@id=''content'']/text()', 2, 0, 0, '//div[@class=''bottem2'']//a', 3);
INSERT INTO "public"."config" VALUES (14, 'BiQuGer_COM', '//div[@class=''bookname'']/h1/text()', 2, '//div[@class=''content'']/text()', 1, 0, 0, '//div[@class=''bottem'']//a', 3);
INSERT INTO "public"."config" VALUES (16, 'BiQuGg_TW', '//div[@class=''bookname'']/h1/text()', 2, '//div[@id=''content'']/text()', 2, 0, 0, '//div[@class=''bottem2'']//a', 2);
INSERT INTO "public"."config" VALUES (18, 'BiQuWu_COM', '//div[@class=''bookname'']/h1/text()', 2, '//div[@id=''content'']/text()', 1, 0, 0, '//div[@class=''bottem2'']//a', 3);
INSERT INTO "public"."config" VALUES (19, 'BookTXT_NET', '//div[@class=''bookname'']/h1/text()', 2, '//div[@id=''content'']/text()', 2, 0, 0, '//div[@class=''bottem2'']//a', 3);
INSERT INTO "public"."config" VALUES (20, 'BoQuGe_COM', '//div[@id=''h1'']/h1/text()', 2, '//div[@id=''txtContent'']/text()', 2, 0, 0, '//div[@class=''clearfix'']/ul/li/a', 6);
INSERT INTO "public"."config" VALUES (21, 'BQG5_CC', '//div[@class=''bookname'']/h1/text()', 2, '//div[@id=''content'']/p/text()', 2, 0, 0, '//div[@class=''bottem2'']//a', 3);
INSERT INTO "public"."config" VALUES (22, 'MCMSSC_COM', '//div[@class=''bookname'']/h1/text()', 0, '//div[@id=''content'']/text()', 2, 0, 0, '//div[@class=''bottem2'']//a', 3);

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

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."seq_book"', 23, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."seq_chapter"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."seq_site"', 28, true);

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
-- Primary Key structure for table site
-- ----------------------------
ALTER TABLE "public"."site" ADD CONSTRAINT "site_pkey" PRIMARY KEY ("siteid");
