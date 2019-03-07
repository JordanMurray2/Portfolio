--Name all dancers and their awards if they won any

SELECT Dancers.DName, awards.awardName
FROM awards RIGHT JOIN Dancers ON Dancers.DancerID = awards.DancerID;