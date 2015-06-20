# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  id                        bigserial not null,
  name                      varchar(255),
  email                     varchar(255),
  constraint pk_account primary key (id))
;




# --- !Downs

drop table if exists account cascade;

