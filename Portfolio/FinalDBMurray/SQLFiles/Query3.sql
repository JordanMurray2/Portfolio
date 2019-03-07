--Get the Dancers Name, the Wig Name about a wig that belongs to Camelia Rose company that offers 
-- a bun wig to dancers who are the level Open Champion 

SELECT Dancers.DName, Wig.WigName, Wig.Wtype, WigCompanies.WCompName
FROM Wig, WigCompanies, Dancers
WHERE WigCompanies.WigCoID = Wig.WigCoID
AND WigCompanies.WigCoID = Dancers.WigCoID
AND Dancers.DLevel = 'Open Champion'
AND Wig.WType = 'Bun'
AND WigCompanies.WCompName = 'Camelia Rose'
ORDER BY(Dancers.DName);
