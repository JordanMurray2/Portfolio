var Player = require("../models/playerORM.js");

//list all of the players
exports.listPlayers = function(req, res) {
  let options = { attributes: ["id", "name", "score"] };
  if (req.query.location) options.where = { location: req.query.location };
  if (req.query.name) options.where = {name: req.query.name};
  Player.findAll(options)
    .then((players) => res.send(players))
    .catch((err) => res.status(400).send(err.message));

};

//create a new player
exports.createPlayer = (req, res) => {
  Player.create({ name: req.body.name, health: 100, steps: 0, location: "/cells/0/1", dir: "north", winState: false})
    .then((player) => res.status(201).send(player.id.toString()))
    .catch((err) => res.status(400).send(err.message));
};

//get one specific player
exports.getPlayer = (req, res) => {
  Player.findById(req.params.playerID)
    .then((player) => player ? res.send(player) : res.sendStatus(404));
};

//check if a player has lost if their health reaches 0
exports.checkLoss = (req, res) => {
  Player.findById(req.params.playerID).then((player) => {
    res.send(player["health"] == 0);
  })
  .catch((err) => res.status(400).send(err.message));
}

//update a specific attribute of a player
exports.updatePlayer = (req, res) => {
  try {
      Player.findById(req.params.playerID).then((player) => {
        if (player) {
          if (typeof player[req.body.attrib] !== "undefined") {
            player[req.body.attrib] = req.body.value;
            player.save()
              .then(() => res.sendStatus(204))
              .catch((err) => res.sendStatus(500));
          } else res.sendStatus(400);
        } else res.sendStatus(404);
      });
  } catch(e) {
    res.status(400).send("Invalid update instructions.");
  }
};

//delete a player
exports.deletePlayer = function(req, res) {
  Player.findById(req.params.id)
    .then((player) => player ? player.destroy().then(res.sendStatus(204)) : res.sendStatus(404));
};
