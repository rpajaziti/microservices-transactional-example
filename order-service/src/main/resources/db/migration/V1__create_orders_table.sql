CREATE TABLE IF NOT EXISTS orders (
    id           BIGSERIAL PRIMARY KEY,
    user_id      VARCHAR(255) NOT NULL,
    product_id   BIGINT       NOT NULL,
    quantity     INTEGER      NOT NULL,
    total_amount INTEGER      NOT NULL,
    created_at   TIMESTAMP    NOT NULL
);

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
