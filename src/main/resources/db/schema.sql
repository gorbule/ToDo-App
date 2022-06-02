DROP TABLE IF EXISTS todotask_table;

CREATE TABLE todotask_table (
    id bigint NOT NULL AUTO_INCREMENT,
    task_description VARCHAR(255) NOT NULL,
    status ENUM ('TO_DO', 'IN_PROGRESS', 'DONE') NOT NULL,
    task_priority ENUM ('URGENT', 'HIGH', 'MEDIUM', 'LOW') NOT NULL,
--     finish_date date DEFAULT NULL,
    special_message VARCHAR(255),
    PRIMARY KEY (id)
);