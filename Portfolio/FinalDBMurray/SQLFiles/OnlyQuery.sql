--Name dancers, their age and their level who are only taught by Michael Farrell.

SELECT Dancers.DName, Dancers.Age, Dancers.DLevel
FROM Dancers 
WHERE Dancers.DSID NOT IN
  (SELECT DanceSchools.DSID
   FROM DanceSchools
   WHERE DanceSchools.DSID NOT IN
     (SELECT DanceTeachers.DSID
      FROM DanceTeachers
      WHERE DanceTeachers.TName = 'Michael Farrell'));