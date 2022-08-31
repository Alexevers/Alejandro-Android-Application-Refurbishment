const exec = require('child_process').exec;
const {query} = require(`../../utils/commands`);

function planet(data, laststate){
  console.log('change planet ->', data)
  operation = 'planet'
  exec(`${query(operation, data.direction, laststate)}`)
  laststate=''
}

module.exports = {
  planet: planet
}
