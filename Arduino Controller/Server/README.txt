
Before running the server script, it is necessary to update the pip: 

$sudo python -m pip install --upgrade pip 

And install these dependencies below:

$ pip install pynput 1.6.8
$ pip install python-xlib 0.27
$ pip install pyuserinput 0.1.11
$ pip install pyserial 3.4
$ pip install keyboard 0.13.5

Open the terminal and include the user in the "tty" and "dialout" groups, with the commands:
 
$ sudo usermod -a -G tty lg
$ sudo usermod -a -G dialout lg

Now logout and then log in again.
To verify that groups have been added, use the command:

$ groups

The command output must contain the groups to which we include the user.
These commands are necessary to enable the use of the serial port for the controller


For the case of making the connection between the controller and the PC Master Liquid Galaxy use the commands below to allow connection via tcp on the firewall on the master pc:

$sudo iptables -F
$sudo iptables -A INPUT -i eth0 -j ACCEPT
$sudo iptables -A INPUT -i lo -j ACCEPT
ssudo iptables -A INPUT -p tcp --dport 8000 -j ACCEPT
$sudo iptables -A INPUT -p tcp --syn -j DROP
