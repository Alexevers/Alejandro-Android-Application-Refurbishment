const exec = require('child_process').exec;
const {command} = require(`../../utils/commands`);

function zoom(data, laststate){
  console.log('zoom ->', data)
  key = `zoom_${data.direction}`
  if(laststate.state!=key)
    exec(`${command(key, laststate.state)}`)
  laststate.state = key
  laststate.mode=data.value
}

module.exports = {
  zoom: zoom
}
