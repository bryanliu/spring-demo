drop table t_coffee if exists;
drop table t_order if exists;
drop table t_coffee_order if exists;

create table t_coffee
(
    id          int identity,
    name        varchar(255),
    price       int,
    create_time timestamp,
    update_time timestamp,
    primary key (id)

);

create table t_order
(
    id          int identity,
    customer    varchar(255),
    state       int not null,
    create_time timestamp,
    update_time timestamp,
    primary key (id)
);

create table t_coffee_order
(
    coffee_order_id int not null,
    item_id         int not null,
    primary key (coffee_order_id, item_id)

);
insert into t_coffee(name, price, create_time, update_time)
values ('espresso', 10, now(), now()),
       ('latte', 20, now(), now()),
       ('capuccino', 30, now(), now()),
       ('mocha', 25, now(), now()),
       ('macchiato', 21, now(), now());