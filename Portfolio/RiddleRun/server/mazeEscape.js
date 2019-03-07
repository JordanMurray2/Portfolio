const express = require('express')
const app = express()
const port = 3000

//link static files directory
app.use(express.static('client/public'));

//Routes get requests to the specified path and file
app.get('/', function (req, res){
  res.sendFile('index.html', {root: './client/views'});
});

app.get('/menu', function (req, res){
  res.sendFile('menu.html', {root: './client/views'});
});

app.get('/maze', function (req, res){
  res.sendFile('maze.html', {root: './client/views'});
});

app.get('/about', function (req, res){
  res.sendFile('about.html', {root: './client/views'});
});

app.get('/leaders', function (req, res){
  res.sendFile('leaders.html', {root: './client/views'});
});

app.get('/credits', function (req, res){
  res.sendFile('credits.html', {root: './client/views'});
});

//begin listening for connections on the specified port
app.listen(port, () => console.log(`Example app listening on port ${port}!`));

parser = require("body-parser");
routes = require("./routes.js")

//set up parser and map the routes file to the /api path
app.use(parser.urlencoded({ extended: true }));
app.use(parser.json());
app.use("/api", routes);
