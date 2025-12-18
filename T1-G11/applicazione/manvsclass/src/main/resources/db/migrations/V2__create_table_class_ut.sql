    create table class_ut (
       name varchar(255) not null,
        category JSONB,
        created_at timestamp,
        date date,
        deleted_at timestamp,
        description TEXT,
        difficulty varchar(255),
        updated_at timestamp,
        uri varchar(255),
        primary key (name)
    );
