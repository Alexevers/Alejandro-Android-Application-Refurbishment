import 'package:flutter_socket_io/flutter_socket_io.dart';
import 'package:flutter_socket_io/socket_io_manager.dart';

class Socket{
  static SocketIO socket;
  static String domain;
  static Function optionsCallback;
  static int state = 0;

  initialize(String address, Function callback) async{
    domain = address;
    optionsCallback = callback;
    socket = SocketIOManager().createSocketIO('http://' + domain, "/", socketStatusCallback: _connectStatus);
    socket.init();
    socket.connect();
  }

  send(String event, String data){
    socket.sendMessage(event, data);
  }

  close() async{
    socket.disconnect();
  }

  _connectStatus(dynamic status){
    print(status);
    switch(status.toString()){
      case 'connect_error':
        close();
        state = -1;
        break;
      case 'disconnect':
        state = 0;
        break;
      case 'connect':
        state = 1;
        break;
    }
    optionsCallback();
  }
}
