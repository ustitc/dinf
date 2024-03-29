CREATE TABLE edges_v2 (
    id INTEGER PRIMARY KEY,
    value TEXT NOT NULL,
    dice INTEGER NOT NULL,
    FOREIGN KEY (dice) REFERENCES dices(id) ON DELETE CASCADE ON UPDATE NO ACTION
);

INSERT INTO edges_v2 (value, dice)
SELECT value, dice
FROM edges;

DROP INDEX edges_dice_index;
DROP TABLE edges;
ALTER TABLE edges_v2 RENAME to edges;

CREATE INDEX edges_dice_index ON edges(dice);
