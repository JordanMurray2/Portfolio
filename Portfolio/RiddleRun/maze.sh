#!/bin/sh
sqlite3 ./playerDB <<END_SQL 
DELETE FROM players;
END_SQL

sqlite3 ./cellDB <<END_SQL
DELETE FROM cells;
END_SQL

sqlite3 ./itemDB <<END_SQL
DELETE FROM items;
END_SQL

sqlite3 ./obstacleDB <<END_SQL
DELETE FROM obstacles;
END_SQL

node ./server/mazeEscape.js
