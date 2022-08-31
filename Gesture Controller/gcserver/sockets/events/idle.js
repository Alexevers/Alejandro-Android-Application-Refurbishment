const exec = require('child_process').exec;
const {command} = require(`../../utils/commands`);

function idle(data, lastState){
  console.log(data)
  console.log('idle')
  if(lastState.state!=data && lastState.state!='' && (lastState.mode=='P' || lastState.mode == data.value))
    exec(`${command('idle',lastState.state)}`)
  if(lastState.mode == 'P' || data.value == 'V'){
    lastState.state=''
    lastState.mode=data.value
  }
}

module.exports = {
  idle: idle
}
