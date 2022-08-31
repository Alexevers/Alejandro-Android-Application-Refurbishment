const exec = require('child_process').exec;
const {command} = require(`../../utils/commands`);

function rotate(data, laststate){
  console.log('rotate ->', data)
  key = `rotate_${data.direction}`
  if(laststate.state!=key)
    exec(`${command(key, laststate.state)}`)
  laststate.state=key
  laststate.mode=data.value
}

module.exports = {
  rotate: rotate
}
