const exec = require('child_process').exec;
const {command} = require(`../../utils/commands`);

function move(data, lastState){
  console.log('move ->', data)
  key = `move_${data.direction}`
  if(lastState.state!=key)
    exec(`${command(key,lastState.state)}`)
  lastState.state=key
  lastState.mode=data.value
}

module.exports = {
  move: move
}
