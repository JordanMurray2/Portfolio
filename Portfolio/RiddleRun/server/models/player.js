var players = [
  new Player("Alice", 0)];
var exports = module.exports = {};

//give each player certain objects in the inventory to start the first puzzle
function Player(name, id){
  this.name = name;
  this.health = 100;
  this.steps = 0;
  this.location = "/cells/0/0";
  this.dir = "north";
  this.winState = false;
  //add a direction and the routes for that get direction and update direction patch
  this.id = id;
};

exports.create = (name) => players.push(new Player(name,players.length))-1;

exports.listPlayers = () => {return players;}

exports.getPlayer = (i) => {return players[i];}

exports.updatePlayer = (i, field, value) => {players[i][field] = value; return i;}

exports.getAttrib = (i, field) => {return players[i][field];}

exports.delete = (i) => {return delete players[i];}
