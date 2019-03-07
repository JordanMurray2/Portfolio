//item model that uses sequelize database
var Sequelize = require("sequelize");
var sequelize = new Sequelize("database", "username", "password", {dialect:"sqlite", storage:"itemDB"});

//create the item table
var Item = sequelize.define("item", {
  name:{type:Sequelize.STRING, allowNull:false},
  description:{type:Sequelize.STRING, unique:true},
  icon:{type:Sequelize.STRING, defaultValue:null, allowNull:true},
  numUses:{type:Sequelize.INTEGER},
  owner:{type:Sequelize.STRING}
});

//create items for the maze
sequelize.sync().then(function(){
  Item.create({name:"Y", description:"the letter Y", numUses:1, owner:"/cells/0/0"})
      .catch((err) => console.log(err.name));
  Item.create({name:"water", description:"Water Bottle 1", numUses:1, owner:"/cells/0/0"})
      .catch((err) => console.log(err.name));
  Item.create({name:"G", description:"the letter G", numUses:1, owner:"/cells/0/0"})
      .catch((err) => console.log(err.name));
  Item.create({name:"A", description:"the letter A 1", numUses:1, owner:"/cells/0/1"})
      .catch((err) => console.log(err.name));
  Item.create({name:"O", description:"the letter O", numUses:1, owner:"/cells/0/2"})
      .catch((err) => console.log(err.name));
  Item.create({name:"scroll", description:"A scroll of paper", numUses:1, owner:"/cells/0/3"})
      .catch((err) => console.log(err.name));
  Item.create({name:"R", description:"the letter R", numUses:1, owner:"/cells/1/0"})
      .catch((err) => console.log(err.name));
  Item.create({name:"M", description:"the letter M", numUses:1, owner:"/cells/1/1"})
      .catch((err) => console.log(err.name));
  Item.create({name:"U", description:"the letter U 1", numUses:1, owner:"/cells/1/2"})
      .catch((err) => console.log(err.name));
  Item.create({name:"O", description:"the letter O", numUses:1, owner:"/cells/1/3"})
      .catch((err) => console.log(err.name));
  Item.create({name:"T", description:"the letter T", numUses:1, owner:"/cells/1/3"})
      .catch((err) => console.log(err.name));
  Item.create({name:"A", description:"the letter A 2", numUses:1, owner:"/cells/2/0"})
      .catch((err) => console.log(err.name));
  Item.create({name:"water", description:"Water Bottle 2", numUses:1, owner:"/cells/2/1"})
      .catch((err) => console.log(err.name));
  Item.create({name:"I", description:"the letter I", numUses:1, owner:"/cells/2/2"})
      .catch((err) => console.log(err.name));
  Item.create({name:"H", description:"the letter H", numUses:1, owner:"/cells/2/2"})
      .catch((err) => console.log(err.name));
  Item.create({name:"D", description:"the letter D", numUses:1, owner:"/cells/2/3"})
      .catch((err) => console.log(err.name));
  Item.create({name:"F", description:"the letter F", numUses:1, owner:"/cells/3/0"})
      .catch((err) => console.log(err.name));
  Item.create({name:"S", description:"the letter S", numUses:1, owner:"/cells/3/1"})
      .catch((err) => console.log(err.name));
  Item.create({name:"C", description:"the letter C", numUses:1, owner:"/cells/3/2"})
      .catch((err) => console.log(err.name));
  Item.create({name:"water", description:"Water Bottle 3", numUses:1, owner:"/cells/3/3"})
      .catch((err) => console.log(err.name));
});

module.exports = Item;
