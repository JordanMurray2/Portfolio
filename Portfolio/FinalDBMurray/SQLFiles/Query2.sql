--Select the dancer ID number for dancers who danced at a major that had a competition judged 
--by a judge from Dublin

SELECT DISTINCT Dancers.DancerID
FROM Dancers, Major, Competitions, Judges, Adjudicated
WHERE Dancers.MajorID = Major.MajorID
AND Competitions.MajorID = Major.MajorID
AND Competitions.CompID = Adjudicated.CompID
AND Adjudicated.JudgeID = Judges.JudgeID
AND Judges.Hometown = 'Dublin';