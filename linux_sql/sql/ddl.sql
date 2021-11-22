CREATE TABLE IF NOT EXISTS PUBLIC.host_info
    (
        id                  SERIAL NOT NULL PRIMARY KEY,
        hostname            VARCHAR(255) NOT NULL UNIQUE,
        cpu_number          INTEGER NOT NULL,
        cpu_architecture    VARCHAR(255) NOT NULL,
        cpu_model           VARCHAR(255) NOT NULL,
        cpu_mhz             DOUBLE PRECISION NOT NULL,
        L2_cache            INTEGER NOT NULL,
        total_mem           INTEGER NOT NULL,
        "timestamp"         TIMESTAMP NOT NULL
    );

CREATE TABLE IF NOT EXISTS PUBLIC.host_usage
    (
        "timestamp"           TIMESTAMP NOT NULL,
        host_id               INTEGER NOT NULL REFERENCES host_info(id),
        memory_free           INTEGER NOT NULL,
        cpu_idle              INTEGER NOT NULL,
        cpu_kernel            INTEGER NOT NULL,
        disk_io               INTEGER NOT NULL,
        disk_available        INTEGER NOT NULL
    );