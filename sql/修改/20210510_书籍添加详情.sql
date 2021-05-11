-- 2021.05.10

alter table "book" add column "detail" varchar(256);
COMMENT ON COLUMN "public"."book"."detail" IS '书籍详情';
