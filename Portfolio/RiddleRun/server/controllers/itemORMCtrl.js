var Item = require("../models/itemORM.js");

//list all of the items
exports.listItems = (req, res) => {
  let options = {};
  if(req.query.owner){
    options = {where:{owner:req.query.owner}}
  }
  Item.findAll(options)
    .then((items) => res.send(items))
    .catch((err) => res.status(400).send(err));
}

//get one individual item
exports.getItem = (req, res) => {
  Item.findById(req.params.itemID)
    .then((item) => item ? res.send(item) : res.sendStatus(404));
}

//update a specific attribute of one item
exports.updateItem = (req, res) => {
  try{
    Item.findById(req.params.itemID).then((item) => {
      if(item){
        if(typeof item[req.body.attrib] !== "undefined"){
          item[req.body.attrib] = req.body.value;
          item.save()
            .then(() => res.sendStatus(204))
            .catch((err) => res.sendStatus(500));
        }else res.sendStatus(400);
      }else res.sendStatus(404);
    });
  }catch(e){
    res.status(400).send("Invalid update");
  }
}
