-- Name the dress companies that designed a dress for a dancer who has won no awards.

SELECT DressCompanies.DCompName
FROM DressCompanies
WHERE DressCompanies.DressID NOT IN
  (SELECT Dancers.DressID
   FROM Dancers
   WHERE Dancers.DancerID NOT IN
     (SELECT awards.DancerID
      FROM awards));