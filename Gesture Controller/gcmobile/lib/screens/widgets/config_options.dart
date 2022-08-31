import 'package:flutter/material.dart';
import 'package:gcmobile/services/options.dart';
import 'package:gcmobile/services/voice.dart';

class ConfigOptionsWidget extends StatefulWidget{
  @override
  _ConfigOptionsWidgetState createState() => _ConfigOptionsWidgetState();
}

class _ConfigOptionsWidgetState extends State<ConfigOptionsWidget> {

  bool voiceEnabled;
  bool keypointsEnabled;

  switchVoice(value){
    Options.voiceCommands = value;
    if(value == true) VoiceCommands().listen();
    setState((){
        voiceEnabled = value;
    });
  }

  switchKeypoints(value){
    Options.renderKeypoints = value;
    setState((){
        keypointsEnabled = value;
    });
  }

  @override
  initState(){
    super.initState();
    voiceEnabled = Options.voiceCommands;
    keypointsEnabled = Options.renderKeypoints;
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(left: 30, right: 30),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: <Widget>[
              Text(
                'Voice Commands',
                style: TextStyle(
                  fontSize: 15,

                )
              ),
              Switch(
                onChanged: (value) => switchVoice(value),
                value: voiceEnabled,
              )
            ],
          ),
          Divider(),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: <Widget>[
              Text(
                'Keypoints Render',
                style: TextStyle(
                  fontSize: 15,

                )
              ),
              Switch(
                onChanged: (value) => switchKeypoints(value),
                value: keypointsEnabled,
              )
            ],
          ),
        ],
      ),
    );
  }
}
