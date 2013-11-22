create table Users (
        id int8 not null,
        email varchar(128) not null,
        fullname varchar(40) not null,
        login varchar(25) not null,
        password varchar(80) not null,
        primary key (id)
    );