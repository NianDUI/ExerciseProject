
/*
参考：PostgreSQL数据库表分区介绍-四种分区方式
https://blog.csdn.net/qq_38567039/article/details/119751897

第四种:Hash哈希分区
哈希分区是指按字段取哈希值后再分区。具体的语句如下：

-- 创建主表
create table pkslow_person_h (
                          age int not null,
                          city varchar not null
) partition by hash (city);

-- 创建分区表
create table pkslow_person_h1 partition of pkslow_person_h for values with (modulus 4, remainder 0);
create table pkslow_person_h2 partition of pkslow_person_h for values with (modulus 4, remainder 1);
create table pkslow_person_h3 partition of pkslow_person_h for values with (modulus 4, remainder 2);
create table pkslow_person_h4 partition of pkslow_person_h for values with (modulus 4, remainder 3);

-- 插入测试数据
insert into pkslow_person_h(age, city) VALUES (1, 'GZ');
insert into pkslow_person_h(age, city) VALUES (2, 'SZ');
insert into pkslow_person_h(age, city) VALUES (21, 'SZ');
insert into pkslow_person_h(age, city) VALUES (13, 'BJ');
insert into pkslow_person_h(age, city) VALUES (43, 'SH');
insert into pkslow_person_h(age, city) VALUES (28, 'HK');
可以看到创建分区表的时候，我们用了取模的方式，所以如果要创建N个分区表，就要取N取模。

当一个city=‘aaaadadfasdfasdfasdfasdf’数据插入到数据库时，首先会计算该字符串的hashcode(id)得到一个整数。
随后我们队这个整数求MODULUS =20的余数，如果余数是0就进入第一个分区。以下类推，余数是19就进入第20个分区
*/



-- 创建主表
CREATE TABLE "public"."paragraph" (
    "bookid" int8 NOT NULL,
    "chapterid" int8 NOT NULL,
    "content" text COLLATE "pg_catalog"."default" NOT NULL,
    "seqid" int8 NOT NULL,
    "createtime" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(0)
) PARTITION BY HASH (bookid);
ALTER TABLE "public"."paragraph"
    OWNER TO "postgres";
COMMENT ON COLUMN "public"."paragraph"."bookid" IS '书籍id';
COMMENT ON COLUMN "public"."paragraph"."chapterid" IS '章节id';
COMMENT ON COLUMN "public"."paragraph"."content" IS '段落内容';
COMMENT ON COLUMN "public"."paragraph"."seqid" IS '排序id';
COMMENT ON COLUMN "public"."paragraph"."createtime" IS '创建时间';
COMMENT ON TABLE "public"."paragraph" IS '段落';

-- 创建索引
CREATE INDEX "paragraph_bookid_chapterid_seqid_idx" ON "public"."paragraph" USING btree (
 "bookid" "pg_catalog"."int8_ops" ASC NULLS LAST,
 "chapterid" "pg_catalog"."int8_ops" ASC NULLS LAST,
 "seqid" "pg_catalog"."int8_ops" ASC NULLS LAST
);

/*
int size = 20;
for (int i = 0; i < size; i++) {
    String sql = "CREATE TABLE \"public\".\"paragraph_h%02d\" PARTITION OF \"public\".\"paragraph\" FOR VALUES WITH (MODULUS %d, REMAINDER %d);";
    System.out.println(String.format(sql, i, size, i));
}
*/
-- 创建分区表
CREATE TABLE "public"."paragraph_h00" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 0);
CREATE TABLE "public"."paragraph_h01" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 1);
CREATE TABLE "public"."paragraph_h02" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 2);
CREATE TABLE "public"."paragraph_h03" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 3);
CREATE TABLE "public"."paragraph_h04" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 4);
CREATE TABLE "public"."paragraph_h05" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 5);
CREATE TABLE "public"."paragraph_h06" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 6);
CREATE TABLE "public"."paragraph_h07" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 7);
CREATE TABLE "public"."paragraph_h08" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 8);
CREATE TABLE "public"."paragraph_h09" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 9);
CREATE TABLE "public"."paragraph_h10" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 10);
CREATE TABLE "public"."paragraph_h11" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 11);
CREATE TABLE "public"."paragraph_h12" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 12);
CREATE TABLE "public"."paragraph_h13" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 13);
CREATE TABLE "public"."paragraph_h14" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 14);
CREATE TABLE "public"."paragraph_h15" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 15);
CREATE TABLE "public"."paragraph_h16" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 16);
CREATE TABLE "public"."paragraph_h17" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 17);
CREATE TABLE "public"."paragraph_h18" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 18);
CREATE TABLE "public"."paragraph_h19" PARTITION OF "public"."paragraph" FOR VALUES WITH (MODULUS 20, REMAINDER 19);



-- 数据

INSERT INTO "public"."paragraph"("bookid", "chapterid", "content", "seqid", "createtime")
SELECT "bookid", "chapterid", "content", "seqid", "createtime"
FROM "paragraph_"
;

