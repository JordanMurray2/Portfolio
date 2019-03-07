var account = require("../models/player.js");

exports.listPlayers = (req, res) => {
  let playerList = account.listPlayers();
  if (req.query.name)
    playerList = playerList.filter((i) => i.name == req.query.name);
  res.send(playerList);
};

exports.create = (req, res) => {
  if(req.body){
    res.status(201).send(account.create(req.body.name).toString());
  }
  else
    res.status(400).send("May not have valid account name.");
};
/*
exports.getPlayer = (req, res) => {
  let player = account.getPlayer(req.params.id);
  if(typeof player === 'undefined')
    res.sendStatus(404);
  else
    res.send(player);
};
*/
exports.deletePlayer = (req, res) => {
  if(typeof account.delete(req.params.id) === 'undefined')
    res.sendStatus(404);
  else
    res.sendStatus(204);
};


//done with status codes
