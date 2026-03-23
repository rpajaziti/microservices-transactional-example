CREATE TABLE IF NOT EXISTS inventory (
    id           BIGSERIAL PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    stock        INTEGER      NOT NULL DEFAULT 0
);

INSERT INTO inventory (product_name, stock) VALUES ('Laptop', 100);
INSERT INTO inventory (product_name, stock) VALUES ('Phone', 200);
INSERT INTO inventory (product_name, stock) VALUES ('Headphones', 500);

CREATE TABLE IF NOT EXISTS undo_log (
    id            BIGSERIAL    PRIMARY KEY,
    branch_id     BIGINT       NOT NULL,
    xid           VARCHAR(128) NOT NULL,
    context       VARCHAR(128) NOT NULL,
    rollback_info BYTEA        NOT NULL,
    log_status    INT          NOT NULL,
    log_created   TIMESTAMP(0) NOT NULL,
    log_modified  TIMESTAMP(0) NOT NULL,
    CONSTRAINT ux_undo_log UNIQUE (branch_id, xid)
);
