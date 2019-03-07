var player = require("../models/player.js");
var item = require("../models/items.js");
var cell = require("../models/cell.js");
var obstacle = require("../models/obstacles.js");

exports.getItem = (req, res) => {
  let i = item.getItem(req.params.itemID); //done
  if (typeof i === 'undefined')
    res.sendStatus(404);
  else
    res.send(i);
};

exports.listItems = (req, res) => {
  let items = item.list();
  console.log(items);
  if (req.query.owner)
    items = items.filter((i) => i.owner == req.query.owner);
  res.send(items);
};

exports.updateItem = (req, res) => {
  if(req.body)
    if(typeof item.update(req.params.itemID, req.body.attrib, req.body.value) === 'undefined')
      res.sendStatus(404);
    else
      res.sendStatus(204);
  else
    res.status(400).send('Attribute or value cannot be empty');
}; //done

exports.getRoom = (req, res) => {
  let room = cell.getCell(req.params.cellX, req.params.cellY);
  if (typeof room === 'undefined')
    res.sendStatus(404)
  else
    res.send(room);
}

exports.getPlayer = (req, res) => {
  let play = player.getPlayer(req.params.playerID); //done
  if(typeof play === 'undefined')
    res.sendStatus(404);
  else
    res.send(play);
};

exports.updatePlayer = (req, res) => {
  if (req.body)
    if(typeof player.updatePlayer(req.params.playerID, req.body.attrib, req.body.value) === 'undefined')
      res.sendStatus(404);
    else
      res.sendStatus(204);
  else
    res.status(400).send("Attribute or value cannot be empty.");
}; //done
/*
exports.getLocation = (req, res) => {
  let loc = player.getAttrib(req.params.playerID, "location");
  if(typeof loc === 'undefined')
    res.sendStatus(404);
  else
    res.send(loc);
}; //done
*/
exports.addMessage = (req, res) => {
  if(req.body)
    if(typeof cell.createMsg(req.params.cellX, req.params.cellY, req.body.dir, req.body.msg) === 'undefined')
      res.sendStatus(404);
    else
      res.sendStatus(204);
  else
    res.status(400).send('Direction or message may not be left empty.');
}; //done

exports.getMessage = (req, res) => {
  let msg = cell.getMsg(req.params.cellX, req.params.cellY, req.params.dir);
  if(typeof msg === 'undefined')
    res.sendStatus(404);
  else
    res.send(msg);
}; //done

exports.deleteMessage = (req, res) => {
  if(typeof cell.deleteMsg(req.params.cellX, req.params.cellY, req.body.dir, req.body.id) === 'undefined')
    res.sendStatus(404);
  else
    res.sendStatus(204);
}

exports.listObstacles = (req, res) => {
  let obstacles = obstacle.list();
  console.log(obstacles);
  if (req.query.location)
    obstacles = obstacles.filter((i) => i.location == req.query.location);
  res.send(obstacles);
};

exports.getObstacle = (req, res) =>{
  let obs = obstacle.getObstacle(req.params.obsID);
  if(typeof obs === 'undefined')
    res.sendStatus(404);
  else
    res.send(obs);
};

exports.updateObstacle = (req, res) => {
  if(req.body)
    if(typeof obstacle.updateObstacle(req.params.obsID, req.body.fix) === 'undefined')
      res.sendStatus(404);
    else
      res.sendStatus(204);
  else
    res.status(400).send('Fix may not be left empty');
};

exports.deleteObstacle = (req, res) => {
  if(typeof obstacle.delete(req.params.obsID) === 'undefined')
    res.sendStatus(404);
  else
    res.sendStatus(204);
}
//done status codes
