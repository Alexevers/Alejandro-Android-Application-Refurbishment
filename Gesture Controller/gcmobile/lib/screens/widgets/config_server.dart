import 'package:flutter/material.dart';
import 'package:gcmobile/services/sockets.dart';

class ConfigServerWidget extends StatefulWidget {
  @override
  _ConfigServerWidgetState createState() => _ConfigServerWidgetState();
}

class _ConfigServerWidgetState extends State<ConfigServerWidget> {
  final address = new TextEditingController();
  final port = new TextEditingController();
  final socket = Socket();
  int state;
  String ip;
  String strPort;

  refresh(){
    setState(() {
        try {
          state = Socket.state;
          var arr = Socket.domain.split(':');
          ip = arr[0];
          strPort = arr[1] ?? '';
        } catch (e) {
          ip = '';
          strPort = '';
        }
    });
  }

  connect() async {
    print('${address.text}:${port.text}');
    await socket.initialize('${address.text}:${port.text}', refresh);
    Navigator.pop(context);
  }

  disconnect() async {
    await socket.close();
    Navigator.pop(context);
  }

  initState(){
    super.initState();
    try {
      state = Socket.state;
      var arr = Socket.domain.split(':');
      ip = arr[0];
      strPort = arr[1] ?? '';
    } catch (e) {
      ip = '';
      strPort = '';
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top: 20, left: 30, right: 30),
      child: Column(
        mainAxisSize: MainAxisSize.max,
        children: <Widget>[
          Align(
            alignment: Alignment.centerLeft,
            child: Text('Server Informations',
              style: TextStyle(
                fontFamily: 'Poppins',
                fontWeight: FontWeight.w600,
                color: Colors.black54,
                fontSize: 16
              )
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(top: 10),
            child: Row(
              mainAxisSize: MainAxisSize.max,
              children: <Widget>[
                Align(
                  alignment: Alignment.centerLeft,
                  child: Text('Status: ',
                    style: TextStyle(
                      fontFamily: 'Poppins',
                      color: Colors.black54,
                      fontSize: 15
                    )
                  ),
                ),
                statusWidget(),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(top: 10),
            child: Row(
              mainAxisSize: MainAxisSize.max,
              children: <Widget>[
                Align(
                  alignment: Alignment.centerLeft,
                  child: Text('Address: ',
                    style: TextStyle(
                      fontFamily: 'Poppins',
                      color: Colors.black54,
                      fontSize: 15
                    )
                  ),
                ),
                Align(
                  alignment: Alignment.center,
                  child: Text(
                    ip ?? '',
                    style: TextStyle(
                      fontFamily: 'Poppins',
                      color: Colors.black54,
                      fontSize: 15
                    )
                  ),
                ),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(top: 10),
            child: Row(
              mainAxisSize: MainAxisSize.max,
              children: <Widget>[
                Align(
                  alignment: Alignment.centerLeft,
                  child: Text('Port: ',
                    style: TextStyle(
                      fontFamily: 'Poppins',
                      color: Colors.black54,
                      fontSize: 15
                    )
                  ),
                ),
                Align(
                  alignment: Alignment.center,
                  child: Text(
                    strPort ?? '',
                    style: TextStyle(
                      fontFamily: 'Poppins',
                      color: Colors.black54,
                      fontSize: 15
                    )
                  ),
                ),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(top: 30),
            child: Container(
              width: double.infinity,
              height: 28,
              child: OutlinedButton( //linea comentada por Alejandro Illán Marcos. Habia un error al utilizar el metodo dado que la ruta de acceso al mismo no era correcta
                onPressed: () => _showMyDialog(),
                child: Text('EDIT',
                  style: TextStyle(
                    fontFamily: 'Poppins',
                    color: Colors.black54,
                    fontSize: 15
                  )
                ),
              ),
            ),
          )
        ],
      ),
    );
  }

  Widget statusWidget(){
    if(state == 0){
      return Align(
        alignment: Alignment.center,
        child: Text(
          'disconnected',
          style: TextStyle(
            fontFamily: 'Poppins',
            color: Colors.black54,
            fontSize: 15
          )
        ),
      );
    }
    else if(state == -1){
      return Align(
        alignment: Alignment.center,
        child: Text(
          'connect error',
          style: TextStyle(
            fontFamily: 'Poppins',
            color: Colors.red,
            fontSize: 15
          )
        ),
      );
    }
    return Align(
      alignment: Alignment.center,
      child: Text(
        'connected',
        style: TextStyle(
          fontFamily: 'Poppins',
          color: Colors.green,
          fontSize: 15
        )
      ),
    );
  }

  Future<void> _showMyDialog() async {
    return showDialog<void>(
      context: context,
      barrierDismissible: false, // user must tap button!
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text(
            'Server Address',
            style: TextStyle(
              fontFamily: 'Poppins',
              color: Colors.black,
              fontSize: 18
            )
          ),
          content: SingleChildScrollView(
            child: Column(
              children: <Widget>[
                Container(
                  width: 300,
                  child: TextField(
                    controller: address,
                    decoration: InputDecoration(
                      hintText: '192.168.0.152',
                      labelText: 'IP'
                    ),
                  )
                ),
                Container(
                  width: 300,
                  child: TextField(
                    controller: port,
                    decoration: InputDecoration(
                      hintText: '8080',
                      labelText: 'Port'
                    ),
                  )
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  mainAxisSize: MainAxisSize.max,
                  children: <Widget>[
                    Container(
                      padding: EdgeInsets.only(top:20),
                      width: 110,
                      child: FlatButton(
                        onPressed: () => disconnect(),
                        child: Text(
                          'Disconnect',
                          style: TextStyle(
                            color: Colors.black
                          ),
                        )
                      )
                    ),
                    Spacer(),
                    Container(
                      padding: EdgeInsets.only(top:20),
                      width: 110,
                      child: FlatButton(
                        onPressed: () => connect(),
                        child: Text(
                          'Connect',
                          style: TextStyle(
                            color: Colors.black,
                            fontFamily: 'Roboto'
                          )
                        ),
                      )
                    ),
                  ],
                )
              ],
            ),
          ),
        );
      },
    );
  }
}
