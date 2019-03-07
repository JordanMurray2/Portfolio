var Cell = require("../models/cellORM.js");

//get one individual cell in the maze
exports.getRoom = (req, res) => {
  Cell.findOne({where: {x: req.params.cellX, y: req.params.cellY}})
  .then((cell) => cell ? res.send(cell) : res.sendStatus(404));
};

//add a message to any wall
exports.addMessage = (req, res) => {
  Cell.findOne({where: {x: req.params.cellX, y: req.params.cellY}})
  .then((cell) => {
    if(cell){
      if(typeof cell[req.body.dir + "MSG"] !== 'undefined'){
        switch(req.body.dir){
          case "north": cell["northMSG"] = req.body.msg;
            break;
          case "south": cell["southMSG"] = req.body.msg;
            break;
          case "east": cell["eastMSG"] = req.body.msg;
            break;
          case "west": cell["westMSG"] = req.body.msg;
            break;
          default: res.status(400).send("Invalid dir");
            break;
        }

        cell.save()
        .then(() => res.sendStatus(204))
        .catch((err) => res.sendStatus(500));

      }else res.sendStatus(400);
    }else res.sendStatus(404);
  });
};
