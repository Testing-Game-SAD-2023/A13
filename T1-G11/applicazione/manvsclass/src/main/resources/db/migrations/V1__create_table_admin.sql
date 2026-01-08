    create table admin (
       email varchar(255) not null,
        created_at timestamp,
        deleted_at timestamp,
        invitation_token varchar(255),
        name varchar(255) not null,
        password varchar(255) not null,
        reset_token varchar(255),
        surname varchar(255) not null,
        updated_at timestamp,
        username varchar(255) not null,
        primary key (email)
    );

    alter table if exists admin
       add constraint UK_mi8vkhus4xbdbqcac2jm4spvd unique (username);