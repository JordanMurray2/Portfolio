express = require("express");

cellCtlr = require("./controllers/CellORMController");
playerCtlr = require("./controllers/playerORMController");
itemCtrl = require("./controllers/itemORMCtrl");
obstCtrl = require("./controllers/obstaclesORMCtrl");

router = express.Router();

//playerController
router.route("/accounts")
.get(playerCtlr.listPlayers)
.post(playerCtlr.createPlayer);

router.route("/players")
.get(playerCtlr.listPlayers);

router.route("/players/:playerID/loss")
.get(playerCtlr.checkLoss);

router.route("/players/:playerID")
.get(playerCtlr.getPlayer)
.patch(playerCtlr.updatePlayer)
.delete(playerCtlr.deletePlayer);

//cellController ORM
router.route("/cells/:cellX/:cellY")
.get(cellCtlr.getRoom)
.put(cellCtlr.addMessage);

//ORM Item routes
router.route("/items/:itemID") 
.get(itemCtrl.getItem)
.patch(itemCtrl.updateItem);

router.route("/items")
.get(itemCtrl.listItems);

//ORM obstacle routes
router.route("/obstacles")
.get(obstCtrl.listObstacles);

router.route("/obstacles/win")
.get(obstCtrl.checkWin);

router.route("/obstacles/:obsID")
.get(obstCtrl.getObstacle)
.patch(obstCtrl.updateObstacle)
.delete(obstCtrl.deleteObstacle);

module.exports = router;
