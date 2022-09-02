CREATE TABLE "public"."task" (
    "uuid" varchar(256),
    "title" text DEFAULT ''::text,
    "completed" bool DEFAULT false,
    "status" int2 DEFAULT 0,
    "deadline" int8 DEFAULT '-1'::integer,
    "created_time" int8 DEFAULT '-1'::integer,
    "modified_time" int8 DEFAULT '-1'::integer,
    "deleted" bool DEFAULT false,
    "username" varchar(256)
);

CREATE TABLE "public"."user" (
    "username" varchar(256),
    "password" varchar(256),
    "display_name" text DEFAULT ''::text
);