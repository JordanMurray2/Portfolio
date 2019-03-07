//cell model that uses sequelize database
var Sequelize = require("sequelize");
var sequelize = new Sequelize("database", "username", "password", { dialect: "sqlite", storage:"cellDB"} );

//create the cell table
var Cell = sequelize.define("cell", {
  x: { type: Sequelize.INTEGER, allowNull: false, unique:"coords"},
  y: { type: Sequelize.INTEGER, allowNull: false, unique:"coords"},
  northWall: { type: Sequelize.BOOLEAN },
  southWall: { type: Sequelize.BOOLEAN },
  eastWall: { type: Sequelize.BOOLEAN },
  westWall: { type: Sequelize.BOOLEAN },
  northMSG: { type: Sequelize.STRING },
  southMSG: { type: Sequelize.STRING },
  eastMSG: { type: Sequelize.STRING },
  westMSG: { type: Sequelize.STRING }
});

//populate the table-create the maze
sequelize.sync().then(function() {
  Cell.create({ x: 0,y: 0, northWall: true, southWall: false, eastWall: true, westWall: true})
      .catch((err) => console.log(err.name));
  Cell.create({ x: 0,y: 1, northWall: true, southWall: false, eastWall: false, westWall: true})
      .catch((err) => console.log(err.name));
  Cell.create({ x: 0,y: 2, northWall: true, southWall: true, eastWall: false, westWall: false})
      .catch((err) => console.log(err.name));
  Cell.create({ x: 0,y: 3, northWall: true, southWall: false, eastWall: true, westWall: false})
      .catch((err) => console.log(err.name));
  Cell.create({ x: 1,y: 0, northWall: false, southWall: true, eastWall: false, westWall: true})
      .catch((err) => console.log(err.name));
  Cell.create({ x: 1,y: 1, northWall: false, southWall: true, eastWall: true, westWall: false})
      .catch((err) => console.log(err.name));
  Cell.create({ x: 1,y: 2, northWall: true, southWall: false, eastWall: true, westWall: true})
      .catch((err) => console.log(err.name));
  Cell.create({ x: 1,y: 3, northWall: false, southWall: false, eastWall: true, westWall: true})
      .catch((err) => console.log(err.name));
  Cell.create({ x: 2,y: 0, northWall: true, southWall: true, eastWall: false, westWall: true})
      .catch((err) => console.log(err.name));
  Cell.create({ x: 2,y: 1, northWall: true, southWall: false, eastWall: true, westWall: false})
      .catch((err) => console.log(err.name));
  Cell.create({ x: 2,y: 2, northWall: false, southWall: false, eastWall: false, westWall: true})
      .catch((err) => console.log(err.name));
  Cell.create({ x: 2,y: 3, northWall: false, southWall: true, eastWall: true, westWall: false})
      .catch((err) => console.log(err.name));
  Cell.create({ x: 3,y: 0, northWall: true, southWall: true, eastWall: false, westWall: true})
      .catch((err) => console.log(err.name));
  Cell.create({ x: 3,y: 1, northWall: false, southWall: true, eastWall: false, westWall: false})
      .catch((err) => console.log(err.name));
  Cell.create({ x: 3,y: 2, northWall: false, southWall: true, eastWall: false, westWall: false})
      .catch((err) => console.log(err.name));
  Cell.create({ x: 3,y: 3, northWall: true, southWall: true, eastWall: true, westWall: false})
      .catch((err) => console.log(err.name));
});

module.exports = Cell;
