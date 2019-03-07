-- Name dancers if they have the following: the name of their shoe Company, dress company, wig company, dance school,
--the awards they have won, and the region they dance at for majors.

SELECT Dancers.DName, SC.SCompName, DC.DCompName, WC.WCompName, DS.DSName, awards.awardName, Major.RegionName
FROM Dancers, ShoeCompanies SC, DressCompanies DC, WigCompanies WC, DanceSchools DS, awards, Major
WHERE Dancers.ShoeID = SC.ShoeID
AND Dancers.DressID = DC.DressID
AND Dancers.WigCoID = WC.WigCoID
AND Dancers.DSID = DS.DSID
AND Dancers.DancerID = awards.DancerID
AND Dancers.MajorID = Major.MajorID;