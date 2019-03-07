var cells = [
   [new Cell(0,0,true,false,true,true), new Cell(0,1,true,false,false,true), new Cell(0,2,true,true,false,false), new Cell(0,3,true,false,true,false)]
  ,[new Cell(1,0,false,true,false,true), new Cell(1,1,false,true,true,false), new Cell(1,2,true,false,true,true), new Cell(1,3,false,false,true,true)]
  ,[new Cell(2,0,true,true,false,true), new Cell(2,1,true,false,true,false), new Cell(2,2,false,false,false,true), new Cell(2,3,false,true,true,false)]
  ,[new Cell(3,0,true,true,false,true), new Cell(3,1,false,true,false,false), new Cell(3,2,false,true,false,false), new Cell(3,3,true,true,true,false)]
];

var exports = module.exports = {};

function Cell(x,y,frontWall,backWall,rightWall,leftWall){
  this.x = x;
  this.y = y;
  this.northWall = frontWall;
  this.southWall = backWall;
  this.eastWall = rightWall;
  this.westWall = leftWall;
  this.northMSG = new Array(0);
  this.southMSG = new Array(0);
  this.eastMSG = new Array(0);
  this.westMSG = new Array(0);
};

/*
exports.createCell = (x, y, north, south, east, west, northMsg, southMSG, eastMSG, westMSG) => {
  cells.push(new Cell(x, y, north, south, east, west, northMsg, southMSG, eastMSG, westMSG));
};
*/

exports.getCell = (x, y) => {return cells[x][y];}

exports.getLocation = (i, j) => {return cells[i][j]["x"] + ", " + cells[i][j]["y"];}

exports.createMsg = function (i, j, direction, Msg) {
  switch (direction){
    case "north": return cells[i][j]["northMSG"].push(Msg);
      break;
    case "south": return cells[i][j]["southMSG"].push(Msg);
      break;
    case "east": return cells[i][j]["eastMSG"].push(Msg);
      break;
    case "west": return cells[i][j]["westMSG"].push(Msg);
      break;
  }
};

exports.getMsg = function (i, j, direction){
  switch (direction){
    case "front": return cells[i][j]["northMSG"];
      break;
    case "left": return cells[i][j]["southMSG"];
      break;
    case "right": return cells[i][j]["eastMSG"];
      break;
    case "back": return cells[i][j]["westMSG"];
      break;
  }
};

exports.deleteMsg = function (i, j, direction, index){
  switch (direction){
    case "front": return delete cells[i][j]["northMSG"][index];
      break;
    case "left": return delete cells[i][j]["southMSG"][index];
      break;
    case "right": return delete cells[i][j]["eastMSG"][index];
      break;
    case "back": return delete cells[i][j]["westMSG"][index];
      break;
  }
};
