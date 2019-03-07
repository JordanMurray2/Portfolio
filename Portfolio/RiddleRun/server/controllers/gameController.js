var player = require("../models/player.js");
var loadGame = require("../models/obstacles.js");


/*
exports.create = (req, res) => {
  if (req.body){
    createGame.create(req.body.name);
    res.sendStatus(200);
  }
  else
    res.sendStatus(400).send("Bad Request");
}

//win
exports.getObstacle = (req, res) => {
  res.send(loadGame.getObstacle(0) === undefined ? "win":"not yet");
}

//lose
exports.getAttrib = (req, res) => {
  res.send(createGame.getAttrib(req.params.playerID, 'health') == 0 ? "You're Dead!":"not yet");
}
*/

exports.checkWin = (req, res) => {
  if(typeof player.getAttrib(req.params.playerID, 'location') === 'undefined')
    player.updatePlayer(req.params.playerID, 'winState', true);
  res.send(player.getAttrib(req.params.playerID, 'winState'));
}
