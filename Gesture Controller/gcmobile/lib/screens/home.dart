import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:gcmobile/screens/posenet.dart';
import 'package:gcmobile/screens/transition.dart';

class HomeScreen extends StatefulWidget {

  final List<CameraDescription> cameras;
  HomeScreen(this.cameras);

  @override
  _HomeScreenState createState() => _HomeScreenState(cameras);
}

class _HomeScreenState extends State<HomeScreen> {

  final List<CameraDescription> cameras;
  _HomeScreenState(this.cameras);

  bool transition = true;

  updateState() async{
    await new Future.delayed(const Duration(seconds : 2));
    setState((){
      transition = false;
    });
  }

  Widget build(BuildContext context){
    if(transition==true){
      updateState();
      return TransitionScreen();
    }
    else return PosenetScreen(cameras);
  }
}
