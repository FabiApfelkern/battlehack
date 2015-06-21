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

create table _meal (
  id                        bigserial not null,
  name                      varchar(255),
  price                     float,
  restaurant_id             bigint,
  constraint pk__meal primary key (id))
;

create table _order (
  id                        bigserial not null,
  transaction_id            bigint,
  meal_id                   bigint,
  account_id                bigint,
  restaurant_id             bigint,
  constraint uq__order_transaction_id unique (transaction_id),
  constraint uq__order_meal_id unique (meal_id),
  constraint uq__order_restaurant_id unique (restaurant_id),
  constraint pk__order primary key (id))
;

create table _restaurant (
  id                        bigserial not null,
  name                      varchar(255),
  geo                       varchar(255),
  constraint pk__restaurant primary key (id))
;

create table _transaction (
  id                        bigserial not null,
  transaction_id            varchar(255),
  paid_at                   timestamp,
  created_at                timestamp,
  status                    varchar(255),
  constraint pk__transaction primary key (id))
;

alter table _meal add constraint fk__meal_restaurant_1 foreign key (restaurant_id) references _restaurant (id);
create index ix__meal_restaurant_1 on _meal (restaurant_id);
alter table _order add constraint fk__order_transaction_2 foreign key (transaction_id) references _transaction (id);
create index ix__order_transaction_2 on _order (transaction_id);
alter table _order add constraint fk__order_meal_3 foreign key (meal_id) references _meal (id);
create index ix__order_meal_3 on _order (meal_id);
alter table _order add constraint fk__order_account_4 foreign key (account_id) references account (id);
create index ix__order_account_4 on _order (account_id);
alter table _order add constraint fk__order_restaurant_5 foreign key (restaurant_id) references _restaurant (id);
create index ix__order_restaurant_5 on _order (restaurant_id);



# --- !Downs

drop table if exists account cascade;

drop table if exists _meal cascade;

drop table if exists _order cascade;

drop table if exists _restaurant cascade;

drop table if exists _transaction cascade;

