import 'package:flutter/material.dart';
import 'package:gcmobile/services/sockets.dart';

class SocketIoScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context){
    return Scaffold(
      body: Container(
        child: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: <Widget>[
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  FlatButton(
                    child: Text('Move North'),
                    onPressed: () => Socket().send("move", '{"direction":"N", "value":"1"}'),
                  ),
                  FlatButton(
                    child: Text('Move South'),
                    onPressed: () => Socket().send("move", '{"direction":"S", "value":"1"}'),
                  ),
                ],
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  FlatButton(
                    child: Text('Move West'),
                    onPressed: () => Socket().send("move", '{"direction":"W", "value":"1"}'),
                  ),
                  FlatButton(
                    child: Text('Move East'),
                    onPressed: () => Socket().send("move", '{"direction":"E", "value":"1"}'),
                  ),
                ],
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  FlatButton(
                    child: Text('Rotate Left'),
                    onPressed: () => Socket().send("rotate", '{"direction":"L", "value":"1"}'),
                  ),
                  FlatButton(
                    child: Text('Rotate Right'),
                    onPressed: () => Socket().send("rotate", '{"direction":"R", "value":"1"}'),
                  ),
                ],
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  FlatButton(
                    child: Text('Zoom In'),
                    onPressed: () => Socket().send("zoom", '{"direction":"I", "value":"1"}'),
                  ),
                  FlatButton(
                    child: Text('Zoom out'),
                    onPressed: () => Socket().send("zoom", '{"direction":"O", "value":"1"}'),
                  ),
                ],
              ),
              FlatButton(
                child: Text('Idle'),
                onPressed: () => Socket().send("idle", '{}'),
              ),
            ],
          )
        )
      )
    );
  }
}
