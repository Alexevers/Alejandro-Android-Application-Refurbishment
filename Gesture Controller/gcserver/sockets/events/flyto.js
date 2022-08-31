const exec = require('child_process').exec;
const {query} = require(`../../utils/commands`);

function flyto(data, laststate){
  console.log('fly to ->', data)
  operation = 'flyto'
  exec(`${query(operation, data.direction, laststate.state)}`)
  laststate.state = ''
}

module.exports = {
  flyto: flyto
}
