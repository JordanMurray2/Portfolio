//obstacle model that uses sequelize database
var Sequelize = require("sequelize");
var sequelize = new Sequelize("database", "username", "password", {dialect:"sqlite", storage:"obstacleDB"});

//create obstacle table
var Obstacle = sequelize.define("obstacle", {
  description:{type:Sequelize.STRING, allowNull:false, unique:true},
  icon:{type:Sequelize.STRING, defaultValue:null, allowNull:true},
  solution:{type:Sequelize.STRING, allowNull:false},
  location:{type:Sequelize.STRING}
});

//create obstacles for the maze
sequelize.sync().then(function(){
  Obstacle.create({description:"P_RAMID", icon:null, solution:"Y", location:"/cells/0/0"})
          .catch((err) => console.log(err.name));
  Obstacle.create({description:"HIERO_LYPHIC", icon:null, solution:"G", location:"/cells/0/1"})
          .catch((err) => console.log(err.name));
  Obstacle.create({description:"INSERT MISSING ARTIFACT", icon:null, solution:"scroll", location:"/cells/0/2"})
          .catch((err) => console.log(err.name));
  Obstacle.create({description:"_BELISK", icon:null, solution:"O", location:"/cells/0/3"})
          .catch((err) => console.log(err.name));
  Obstacle.create({description:"TO_BS", icon:null, solution:"M", location:"/cells/1/0"})
          .catch((err) => console.log(err.name));
  Obstacle.create({description:"PHAR_OH", icon:null, solution:"A", location:"/cells/1/1"})
          .catch((err) => console.log(err.name));
  Obstacle.create({description:"EGYP_IAN", icon:null, solution:"T", location:"/cells/1/2"})
          .catch((err) => console.log(err.name));
  Obstacle.create({description:"M_MMY", icon:null, solution:"U", location:"/cells/1/3"})
          .catch((err) => console.log(err.name));
  Obstacle.create({description:"PAPY_US", icon:null, solution:"R", location:"/cells/2/0"})
          .catch((err) => console.log(err.name));
  Obstacle.create({description:"ANK_", icon:null, solution:"H", location:"/cells/2/1"})
          .catch((err) => console.log(err.name));
  Obstacle.create({description:"SAR_OPHAGUS", icon:null, solution:"C", location:"/cells/2/2"})
          .catch((err) => console.log(err.name));
  Obstacle.create({description:"SPH_NX", icon:null, solution:"I", location:"/cells/2/3"})
          .catch((err) => console.log(err.name));
  Obstacle.create({description:"O_SIS", icon:null, solution:"A", location:"/cells/3/0"})
          .catch((err) => console.log(err.name));
  Obstacle.create({description:"ARTI_ACT", icon:null, solution:"F", location:"/cells/3/1"})
          .catch((err) => console.log(err.name));
  Obstacle.create({description:"RUN FA_T", icon:null, solution:"S", location:"/cells/3/2"})
          .catch((err) => console.log(err.name));
  Obstacle.create({description:"_ESERT", icon:null, solution:"D", location:"/cells/3/3"})
          .catch((err) => console.log(err.name));
});

module.exports = Obstacle;
