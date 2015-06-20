# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  id                        bigserial not null,
  name                      varchar(255),
  email                     varchar(255),
  token                     varchar(255),
  password                  varchar(255),
  role                      varchar(1),
  constraint ck_account_role check (role in ('1','0')),
  constraint pk_account primary key (id))
;




# --- !Downs

drop table if exists account cascade;

