DROP TABLE IF EXISTS todotask_table;

CREATE TABLE todotask_table (
    id bigint NOT NULL AUTO_INCREMENT,
    taskDescription VARCHAR(255) NOT NULL,
    status bigint NOT NULL,
    taskPriority bigint NOT NULL,
    PRIMARY KEY (id)
);