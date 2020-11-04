create database gallery;
use gallery;
create table pictures
(
    id          int          not null auto_increment,
    name        varchar(50)  null,
    description varchar(255) null,
    date        datetime     null,
    primary key (id)
);
create table images
(
    id         int         not null auto_increment,
    picture_id int         not null,
    role       varchar(10) not null,
    data       longblob    not null,
    format     varchar(10) null,
    width      int         not null,
    height     int         not null,
    primary key (id)
);