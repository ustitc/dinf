DROP VIEW dice_details;

CREATE VIEW dice_details
AS
SELECT
    dices.id as id,
    dices.name as name,
    group_concat(edges.value, ';') AS edges,
    dice_owners.user as owner
FROM dices
JOIN dice_owners ON dices.id = dice_owners.dice
LEFT JOIN edges ON dices.id = edges.dice
GROUP BY dices.id;