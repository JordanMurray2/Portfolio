--Name all dance teachers, the school they teach at,and their certification if they have one 

SELECT DanceTeachers.Certification, DanceTeachers.TName, DanceSchools.DSName
FROM DanceTeachers LEFT JOIN DanceSchools ON DanceSchools.DSID = DanceTeachers.DSID;
