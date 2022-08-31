const map = {
  move_N: 'Up',
  move_S: 'Down',
  move_W: 'Left',
  move_E: 'Right',
  rotate_L: 'ctrl+Left',
  rotate_R: 'ctrl+Right',
  zoom_I: 'equal',
  zoom_O: 'minus',
  planet: 'planet=',
  flyto: 'search='
}

function command(key, laststate){
  let command = ''
  if(laststate != '')
    command += ` xdotool keyup ${map[laststate]};`
  if(key!='idle')
    command += ` xdotool keydown ${map[key]};`
  console.log(command)
  if(MASTER_IP != 'localhost')
    return `ssh lg@${MASTER_IP} 'export DISPLAY=:0; ${command}'`
  else
    return `${command}`
}

function query(operation, data, laststate){
  let command = ''
  if(laststate != '')
    command += ` xdotool keyup ${map[laststate]};`
  if(MASTER_IP != 'localhost')
    return `ssh lg@${MASTER_IP} 'export DISPLAY=:0;${command} echo '${map[operation]+data}' > /tmp/query.txt'`
  else
    return `${command} echo '${map[operation]+data}' > /tmp/query.txt`
}

module.exports = {
  command: command,
  query: query
}
