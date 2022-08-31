const app = require('express')()
const http = require('http').createServer(app)
const socketio = require('socket.io')(http)
const {connection} = require('./sockets/index.js')
const dotenv = require('dotenv');
const exec = require('child_process').exec;

dotenv.config();
const port = process.env.SERVER_PORT;
const logosMachine = process.env.LOGOS_MACHINE;

global.__basedir = __dirname;
global.MASTER_IP = process.env.MASTER_IP;

app.get('/', (req,res)=>{
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "X-Requested-With");
  res.header("Access-Control-Allow-Headers", "Content-Type");
  res.header("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS");
  res.send('Root route.')
})

socketio.on("connection", connection)

function openLogos(){
  if(MASTER_IP == 'localhost'){
    exec(`ssh lg@lg${logosMachine} "export DISPLAY=:0;feh -x -g 400x121 ~/gc-assets/logos.png"`)
  }
}

http.listen(port,() => {
  console.log(`GC Server listening on port ${port}`)
  if(logosMachine!='n')
    openLogos()
})
