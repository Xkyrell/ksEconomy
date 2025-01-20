-- ksEconomy Schema for SQLite

CREATE TABLE IF NOT EXISTS players (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    uuid TEXT UNIQUE NOT NULL,
    playername TEXT NOT NULL,
    economies TEXT NOT NULL
);