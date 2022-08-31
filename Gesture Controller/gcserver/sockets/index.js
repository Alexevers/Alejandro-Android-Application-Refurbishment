const exec = require('child_process').exec;
const fs = require('fs')
const {move} = require('./events/move.js')
const {rotate} = require('./events/rotate.js')
const {zoom} = require('./events/zoom.js')
const {flyto} = require('./events/flyto.js')
const {planet} = require('./events/planet.js')
const {idle} = require('./events/idle.js')

var lastState = {
  pose: '',
  voice: ''
}

function connection(userSocket){

  console.log("Client connected!")
  userSocket.on("move", (data) => move(data, lastState))
  userSocket.on("rotate", (data) => rotate(data, lastState))
  userSocket.on("zoom", (data) => zoom(data, lastState))
  userSocket.on("planet", (data) => planet(data, lastState))
  userSocket.on("flyto", (data) => flyto(data, lastState))
  userSocket.on("idle", (data) => idle(data, lastState))
}

module.exports = {
  connection: connection
}
