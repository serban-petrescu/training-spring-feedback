create table feedback (
    id serial not null primary key,
    created_at timestamp,
    text varchar(255)
);

create table sentiment (
    id serial not null primary key,
    end_index int4 not null,
    start_index int4 not null,
    strong boolean not null,
    text varchar(255),
    type varchar(255),
    feedback_id int4 references feedback
);
