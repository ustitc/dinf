DROP VIEW dice_details;

CREATE VIEW dice_details
AS
SELECT
    dices.id AS id,
    dices.name AS name,
    json_group_object(edges.id, edges.value)
    	FILTER (WHERE edges.value IS NOT NULL AND edges.id IS NOT NULL)
    	AS edges,
    dice_owners.user AS owner
FROM dices
JOIN dice_owners ON dices.id = dice_owners.dice
LEFT JOIN edges ON dices.id = edges.dice
GROUP BY dices.id;