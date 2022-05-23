DROP TABLE IF EXISTS todotask_table;

CREATE TABLE todotask_table (
    id bigint NOT NULL AUTO_INCREMENT,
    task_description VARCHAR(255) NOT NULL,
    status bigint NOT NULL,
    task_priority bigint NOT NULL,
    PRIMARY KEY (id)
);