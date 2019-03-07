--name the judges for which there is not a competition they did not adjudicate
--name the judges who judge all of the competitions

SELECT Judges.JName
FROM Judges
WHERE NOT EXISTS
  (SELECT *
   FROM Competitions
   WHERE NOT EXISTS
     (SELECT *
      FROM Adjudicated
      WHERE Judges.JudgeID = Adjudicated.JudgeID
      AND Competitions.CompID = Adjudicated.CompID));