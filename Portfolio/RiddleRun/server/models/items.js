var exports = module.exports = {};
var items = [
  new Item("Y", "the letter Y",undefined,1, "/cells/0/0")
  , new Item("chalk","A piece of chalk",undefined,20, "/cells/0/0")
  , new Item("water","Water Bottle",undefined,1, "/cells/0/0")
  , new Item("G","the letter G",undefined,1, "/cells/0/0")
  , new Item("A","the letter A",undefined,1, "/cells/0/1")
  , new Item("O","the letter O",undefined,1, "/cells/0/2")
  , new Item("scroll","A scroll of paper",undefined,1,"/cells/0/3")
  , new Item("R","the letter R",undefined,1,"/cells/1/0")
  , new Item("M","the letter M",undefined,1,"/cells/1/1")
  , new Item("U","the letter U",undefined,1,"/cells/1/2")
  , new Item("O","the letter O",undefined,1,"/cells/1/3")
  , new Item("T","the letter T",undefined,1,"/cells/1/3")
  , new Item("A","the letter A",undefined,1,"/cells/2/0")
  , new Item("water","Water Bottle",undefined,1,"/cells/2/1")
  , new Item("I","the letter I",undefined,1,"/cells/2/2")
  , new Item("H","the letter H",undefined,1,"/cells/2/2")
  , new Item("D","the letter D",undefined,1,"/cells/2/3")
  , new Item("F","the letter F",undefined,1,"/cells/3/0")
  , new Item("S","the letter S",undefined,1,"/cells/3/1")
  , new Item("C","the letter C",undefined,1,"/cells/3/2")
  , new Item("food","A Piece of Food",undefined,1,"/cells/3/3")
  , new Item("U","the letter U",undefined,1,"/cells/3/3")
];

items.forEach((itm, index) => itm.id = index);

function Item(name, description, icon, numUses, owner){
  this.name = name;
  this.description = description;
  this.icon = icon;
  this.numUses = numUses;
  this.owner = owner;
};

//exports.create = (desc, icon, uses, owner) => items.push(new Item(desc, icon, uses, owner));

exports.getItem = (i) => {return items[i];}

exports.list = () => items;

exports.update = (i, field, value) => {return items[i][field] = value;}

exports.getAttrib = (i, field) => {items[i][field];}
