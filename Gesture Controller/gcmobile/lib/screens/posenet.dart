import 'package:flutter/material.dart' hide Router;
import 'package:camera/camera.dart';
import 'dart:math' as math;

import 'package:gcmobile/screens/widgets/camera.dart';
import 'package:gcmobile/screens/widgets/bndbox.dart';
import 'package:gcmobile/screens/config.dart';
import 'package:gcmobile/services/classifier.dart';
import 'package:gcmobile/services/posenet.dart';
import 'package:gcmobile/services/options.dart';
import 'package:gcmobile/services/router.dart';
import 'package:gcmobile/services/voice.dart';

import 'help.dart';


class PosenetScreen extends StatefulWidget {
  final List<CameraDescription> cameras;

  PosenetScreen(this.cameras);

  @override
  _PosenetScreenState createState() => new _PosenetScreenState();
}

class _PosenetScreenState extends State<PosenetScreen>{
  Classifier classifier = new Classifier();
  Posenet posenet = new Posenet();
  VoiceCommands voice = new VoiceCommands();
  var data;
  int _imageHeight = 0;
  int _imageWidth = 0;
  List<dynamic> results = [];

  setRecognitions(recognitions, imageHeight, imageWidth) async{
    if(recognitions.isNotEmpty){
      data = classifier.flattenInputs(recognitions);
      print(data);
      data = await classifier.classify(data);
      classifier.handleResult(data);
    }
    else
      print('\x1B[97m');
  }

  @override
  void initState(){
    super.initState();
    voice.listen();
  }

  @override
  Widget build(BuildContext context) {
    Router.state='posenet';
    return Scaffold(
      body: Stack(
        children: <Widget>[
          _camera(),
          Positioned(
            child: SafeArea(
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: <Widget>[
                  Padding(
                    padding: const EdgeInsets.only(top: 10),
                    child: MaterialButton(
                      onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (context) => ConfigScreen())),
                      color: Color.fromRGBO(0, 0, 0, 0.6),
                      textColor: Colors.white,
                      child: Icon(Icons.settings, size: 17),
                      padding: EdgeInsets.all(14),
                      shape: CircleBorder(),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top: 10),
                    child: MaterialButton(
                      onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (context) => HelpScreen())),
                      color: Color.fromRGBO(0, 0, 0, 0.6),
                      textColor: Colors.white,
                      child: Icon(Icons.help, size: 17),
                      padding: EdgeInsets.all(14),
                      shape: CircleBorder(),
                    ),
                  ),
                ],
              ),
            ),
          ),
          _keypointsRender()
        ],
      )
    );
  }

  _camera(){
    if(Router.state=='posenet')
      return Camera(
        widget.cameras,
        setRecognitions,
      );
    else
      return Container(width: 0,);
  }

  _keypointsRender(){
    Size screen = MediaQuery.of(context).size;
    if(Options.renderKeypoints)
      return BndBox(
        results == null ? [] : results,
        math.max(_imageHeight, _imageWidth),
        math.min(_imageHeight, _imageWidth),
        screen.height,
        screen.width,
        data
      );
    else
     return Container(
       width: 0,
     );
  }
}
