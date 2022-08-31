#!/bin/bash

LOGOS_MACHINE=4

echo
echo Gesture Controller Installation
echo
echo "Want to install major packages? This can affect this packages versions and configurations:"
echo " - SSH"
echo " - NodeJs"
echo " - npm"
echo -n "y/n: "
read OP
if [ $OP == 'y' ]; then
  sudo snap install node --channel=12/stable --classic
  sudo apt install ssh
fi
echo
cd gcserver
npm install
echo Installation finished...
echo
echo
echo -n "Server port (8080):"
read SERVER_PORT
if [ -z $SERVER_PORT ] || [ $SERVER_PORT = '8080' ]; then
  SERVER_PORT='8080'
fi
echo -n "Master IP (localhost):"
read MASTER_IP
THIS_IP=$(hostname -I | cut -d' ' -f1)
if [ -z $MASTER_IP ] || [ $MASTER_IP = 'localhost' ] || [ $MASTER_IP = $THIS_IP]; then
  MASTER_IP='localhost'
  echo -n "Want to display logos (y/n)? "
  read OP
  if [ $OP == 'y' ]; then
    ssh lg@lg$LOGOS_MACHINE -t "sudo apt install feh"
    ssh lg@lg$LOGOS_MACHINE "mkdir -p gc-assets"
    scp utils/logos.png lg@lg$LOGOS_MACHINE:~/gc-assets/
  else
    LOGOS_MACHINE='n'
  fi
fi
echo -n "" > .env
echo SERVER_PORT=$SERVER_PORT >> .env
echo MASTER_IP=$MASTER_IP >> .env
echo LOGOS_MACHINE=$LOGOS_MACHINE >> .env

sudo iptables -I INPUT 1 -i eth0 -p tcp --dport $SERVER_PORT -j ACCEPT
sudo iptables-save

# ssh-keygen -f /tmp/gcserver -t rsa -b 4096 -q -N "" -C "$SUDO_USER@$HOSTNAME"
# ssh-keygen -t rsa -f gcserver

if [ $MASTER_IP != 'localhost' ]; then
  if [ ! -f ~/.ssh/id_rsa ]; then
    ssh-keygen -t rsa -q -N ""
  fi
  sudo cat ~/.ssh/id_rsa.pub | ssh lg@$MASTER_IP 'cat >> .ssh/authorized_keys'
fi
