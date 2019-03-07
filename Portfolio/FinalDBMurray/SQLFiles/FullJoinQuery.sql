--name all dancers, the major they competed at if they did and the award they won if they won an award.

SELECT Dancers.DName, Major.RegionName, awards.awardName
FROM Dancers FULL JOIN awards ON Dancers.DancerID = awards.DancerID
     FULL JOIN Major ON Major.MajorID = Dancers.MajorID;