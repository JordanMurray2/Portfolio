var exports = module.exports = {};
var obstacles = [
    new Obstacle("P_RAMID",undefined,"Y","/cells/0/0")
  , new Obstacle("HIERO_LYPHIC",undefined,"G","/cells/0/1")
  , new Obstacle("INSERT THE MISSING ARTIFACT TO ESCAPE",undefined,"scroll","/cells/0/2")
  , new Obstacle("_BELISK",undefined,"O","/cells/0/3")
  , new Obstacle("TO_BS",undefined,"M","/cells/1/0")
  , new Obstacle("PHAR_OH",undefined,"A","/cells/1/1")
  , new Obstacle("EGYP_IAN",undefined,"T","/cells/1/2")
  , new Obstacle("M_MMY",undefined,"U","/cells/1/3")
  , new Obstacle("PAPY_US",undefined,"R","/cells/2/0")
  , new Obstacle("ANK_",undefined,"H","/cells/2/1")
  , new Obstacle("SAR_OPHAGUS",undefined,"C","/cells/2/2")
  , new Obstacle("SPH_NX",undefined,"I","/cells/2/3")
  , new Obstacle("O_SIS",undefined,"A","/cells/3/0")
  , new Obstacle("ARTI_ACT",undefined,"F","/cells/3/1")
  , new Obstacle("RUN FA_T",undefined,"S","/cells/3/2")
  , new Obstacle("_ESERT",undefined,"D","/cells/3/3")
];

obstacles.forEach((obstacle, index) => obstacle.id = index);

function Obstacle(description, icon, solution, location){
  this.description = description;
  this.icon = icon;
  this.solution = solution;
  this.location = location;
};
/*
exports.createObstacle = (description, icon, solution, location) => {
  obstacles.push(new Obstacle(description, icon, solution, location));
};
*/
exports.getObstacle = (i) => {return obstacles[i];}

exports.list = () => {return obstacles;}

exports.updateObstacle = (i, fix) => {
  if(obstacles[i].solution == fix){
    return exports.delete(i);
  }
};

exports.delete = (i) => {delete obstacles[i];}
