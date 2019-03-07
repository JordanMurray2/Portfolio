var Obstacle = require("../models/obstaclesORM.js");

//list all of the obstacles
exports.listObstacles = (req, res) => {
  let options = {};
  if(req.query.location){
    options = {where: {location:req.query.location}};
  }
  Obstacle.findAll(options)
    .then((obstacles) => res.send(obstacles))
    .catch((err) => res.status(400).send(err.message));
}
//get one specific obstacle
exports.getObstacle = (req, res) => {
  Obstacle.findById(req.params.obsID)
    .then((obstacle) => obstacle ? res.send(obstacle):res.sendStatus(404));
}

//update a specific attribute of an obstacle
exports.updateObstacle = (req, res) => {
  try{
    Obstacle.findById(req.params.obsID).then((obstacle) => {
      if(obstacle){
        if(typeof obstacle[req.body.attrib] !== 'undefined'){
          obstacle[req.body.attrib] = req.body.value;
          obstacle.save()
            .then(() => res.sendStatus(204))
            .catch((err) => res.sendStatus(500));
        }else res.sendStatus(400);
      }else res.sendStatus(404);
    });
  }catch(e){
    res.status(400).send("Invalid update");
  }
}

//see if the player has won by checking if all obstacles have been solved
exports.checkWin = (req, res) => {
  Obstacle.findAll({where:{location:'undefined'}})
    .then((obstacles) => {
      if (obstacles.length == 16){
        res.send('true');
      }
      else{
        res.send('false');
      }
    })
    .catch((err) => res.status(400).send(err.message));
}

//delete an obstacle
exports.deleteObstacle = (req, res) => {
  Obstacle.findById(req.params.obsID)
    .then((obstacle) => obstacle ? obstacle.destroy().then(res.sendStatus(204)) : res.status(404).send("delete fail"));
}
