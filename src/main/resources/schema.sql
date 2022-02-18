DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS post_tag;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

CREATE TABLE users (
    user_id bigserial primary key ,
    username VARCHAR(50) NOT NULL UNIQUE,
    password text,
    first_name text,
    last_name text,
    created_at timestamp not null,
    is_active BOOLEAN DEFAULT false
);

CREATE TABLE roles(
    role_id bigserial primary key ,
    role_name VARCHAR(50) NOT NULL
);

CREATE TABLE users_roles(
    user_id bigint REFERENCES users(user_id) ON DELETE CASCADE,
    role_id bigint REFERENCES roles(role_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE post (
    post_id bigserial PRIMARY KEY,
    user_id bigint REFERENCES users (user_id),
    title varchar(100) NOT NULL,
    content text NOT NULL,
    dt_created timestamp NOT NULL,
    dt_updated timestamp
);

CREATE TABLE tag (
    tag_id bigserial PRIMARY KEY,
    name1 varchar(50)  NOT NULL UNIQUE
);


CREATE TABLE post_tag (
    post_id bigint REFERENCES post(post_id) ON DELETE CASCADE,
    tag_id bigint REFERENCES tag(tag_id) ON DELETE CASCADE,
    PRIMARY KEY (post_id, tag_id)
);

CREATE TABLE comment (
    comment_id bigserial PRIMARY KEY,
    post_id bigint REFERENCES post(post_id) ON DELETE CASCADE,
    content text,
    dt_created timestamp NOT NULL,
    dt_updated timestamp
);





--Data
insert into users (username, password, first_name, last_name, created_at, is_active)
    values('admin', '$2a$10$r2Xwe2787auMUoE0T4Ahl.qm3XwMoN7zzzqrSnwdjtkBF/lZCSDuu', 'Angy','Angel', current_timestamp, false );
insert into users (username, password, first_name, last_name, created_at, is_active)
    values('user1', '$2a$10$73OfGwCsFBmXQYYazs08LufuJD4cNhftOT1jwzlmnQwQ5nXR6IoBi', 'Bob','Bee', current_timestamp, false );

insert into roles( role_name )
    values ('admin');
insert into roles( role_name )
    values ('user');

 insert into users_roles(user_id,role_id)
    values(1, 1);
  insert into users_roles(user_id,role_id)
    values(2, 2);



insert into post (title, content, dt_created, dt_updated, user_id)
	values ('Day 1', 'It''s all good!', current_timestamp - interval '2 days', null, 2);
insert into post (title, content, dt_created, dt_updated, user_id)
	values ('Day 2', 'It''s all ok!', current_timestamp - interval '1 days', null, 2);
insert into post (title, content, dt_created, dt_updated, user_id)
	values ('Day 3', 'It''s all bad!', current_timestamp, null, 2);

insert into tag (name1) values ('news');
insert into tag (name1) values ('life');
insert into tag (name1) values ('day');
insert into tag (name1) values ('mood');
insert into tag (name1) values ('ideas');
insert into tag (name1) values ('thoughts');

insert into post_tag(post_id, tag_id) values (1, 1);
insert into post_tag(post_id, tag_id) values (1, 2);
insert into post_tag(post_id, tag_id) values (2, 3);
insert into post_tag(post_id, tag_id) values (2, 2);
insert into post_tag(post_id, tag_id) values (2, 1);
insert into post_tag(post_id, tag_id) values (2, 5);
insert into post_tag(post_id, tag_id) values (3, 3);
insert into post_tag(post_id, tag_id) values (3, 2);
insert into post_tag(post_id, tag_id) values (3, 6);

insert into comment (post_id, content, dt_created)
    values (2, 'Nice!', current_timestamp);
insert into comment (post_id, content, dt_created)
    values (1, 'Awesome!', current_timestamp);
insert into comment (post_id, content, dt_created)
    values (1, 'Excellent!', current_timestamp);
insert into comment (post_id, content, dt_created)
    values (1, 'Wonderful!', current_timestamp);
insert into comment (post_id, content, dt_created)
    values (3, 'Disgusting!', current_timestamp);
insert into comment (post_id, content, dt_created)
    values (3, 'Atrocious!', current_timestamp);



select * from post;
select * from users;
select * from roles;
select * from users_roles;
select * from users join users_roles
    on users.user_id = users_roles.user_id
    join roles on users_roles.role_id = roles.role_id;

select u.*
                from users u
            join users_roles ur
                on u.user_id = ur.user_id
            join roles r
               on ur.role_id = r.role_id
            where r.role_name = 'admin';