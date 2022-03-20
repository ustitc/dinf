CREATE TABLE edges (
    value TEXT NOT NULL,
    dice INTEGER NOT NULL,
    FOREIGN KEY (dice) REFERENCES dices(id)
);
