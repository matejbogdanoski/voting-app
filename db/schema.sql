create table users
(
  id bigserial not null
    constraint users_pkey
      primary key,
  email varchar(255),
  first_name varchar(255),
  last_name varchar(255),
  password varchar(255),
  picture_url varchar(255),
  username varchar(255)
);

alter table users owner to postgres;

create table options
(
  id bigserial not null
    constraint options_pkey
      primary key,
  times_voted integer,
  value varchar(255),
  polls_id bigint
    constraint fkku6nr1hulndcj5dkxbbe4kbmj
      references polls
);

alter table options owner to postgres;

create table polls
(
  id bigserial not null
    constraint polls_pkey
      primary key,
  date_created timestamp,
  question varchar(255),
  short_url varchar(255),
  users_id bigint not null
    constraint fkkiohw5fbd9uuyhhbp6bl1fd1q
      references users
);

alter table polls owner to postgres;


create table ips_voted
(
  id bigserial not null
    constraint ips_voted_pkey
      primary key,
  ip_address varchar(255)
);

alter table ips_voted owner to postgres;

create table polls_ips_voted
(
  polls_id bigint not null
    constraint fkcuye2qkuowsgosl64bka6m8ig
      references polls,
  ips_voted_id bigint not null
    constraint fkreoqicu8ykequw297mgi4bqbt
      references ips_voted
);

alter table polls_ips_voted owner to postgres;
