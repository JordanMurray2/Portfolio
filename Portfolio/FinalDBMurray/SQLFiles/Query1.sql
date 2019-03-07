--get the judge ID numbers of judges who have a lower number of years experience than Ryan Carroll

SELECT Judges2.JudgeID
FROM Judges Judges1, Judges Judges2
WHERE Judges1.JName = 'Ryan Carroll'
AND Judges1.YearsExperience > Judges2.YearsExperience;
