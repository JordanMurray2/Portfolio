//player model that uses sequelize database
var Sequelize = require("sequelize");
var sequelize = new Sequelize("database", "username", "password", { dialect: "sqlite", storage:"playerDB"} );

//create the player table
var Player = sequelize.define("player", {
  name: { type: Sequelize.STRING, allowNull: false, unique:true},
  health: { type: Sequelize.INTEGER },
  steps: { type: Sequelize.INTEGER },
  location:  { type: Sequelize.STRING },
  dir:  { type: Sequelize.STRING },
  score: {type: Sequelize.INTEGER },
  winState: { type: Sequelize.BOOLEAN }
});

//populate the player table, creates tables for the maze.
sequelize.sync().then(function() {
  Player.create({ name: "Alice", health: 100, steps: 0, location: "/cells/0/1", dir: "north", score: 0, winState: false })
        .catch((err) => console.log(err.name));
  Player.create({ name: "Bob", health: 100, steps: 0,  location: "/cells/0/1", dir: "north", score: 0, winState: false})
        .catch((err) => console.log(err.name));
  Player.create({ name: "Carol", health: 100, steps: 0, location: "/cells/0/1", dir: "north", score: 0, winState: false})
        .catch((err) => console.log(err.name));
});

module.exports = Player;
