import 'package:flutter/material.dart' hide Router;
import 'package:gcmobile/services/sockets.dart';
import 'package:gcmobile/screens/widgets/config_server.dart';
import 'package:gcmobile/screens/widgets/config_options.dart';
import 'package:gcmobile/services/router.dart';

class ConfigScreen extends StatefulWidget {

  @override
  _ConfigScreen createState() => new _ConfigScreen();
}

class _ConfigScreen extends State<ConfigScreen>{

  final address = new TextEditingController();

  @override
  Widget build(BuildContext context) {
    Router.state = 'config';
    address.text = Socket.domain;
    return Scaffold(
      body: SafeArea(
        child: Column(
          mainAxisSize: MainAxisSize.max,
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.only(top: 15),
              child: Stack(
                alignment: Alignment.center,
                children: <Widget>[
                  Align(
                    alignment: Alignment.centerLeft,
                    child: MaterialButton(
                      onPressed: () => Navigator.pop(context),
                      textColor: Colors.black,
                      child: Icon(Icons.arrow_back_ios, size: 20,),
                      padding: EdgeInsets.all(14),
                      shape: CircleBorder(),
                    ),
                  ),
                  Center(
                    child: Text(
                      'OPTIONS',
                      style: TextStyle(
                        fontFamily: 'Poppins',
                        color: Colors.black,
                        fontSize: 16
                      )
                    )
                  )
                ],
              ),
            ),
            SizedBox(height: 50,),
            ConfigOptionsWidget(),
            Divider(),
            ConfigServerWidget(),
          ],
        )
      )
    );
  }
}
