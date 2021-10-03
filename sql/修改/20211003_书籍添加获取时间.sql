
-- 2021.10.03
alter table "book" add column "lastgettime" timestamp not null default now();
comment on column "book"."lastgettime" is '最后获取时间';

