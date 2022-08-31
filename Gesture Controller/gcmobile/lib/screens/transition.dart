import 'package:flutter/material.dart';
import 'package:gcmobile/screens/widgets/complete_logo.dart';
import 'package:gcmobile/screens/widgets/logos.dart';

class TransitionScreen extends StatefulWidget{
  @override
  _TransitionScreenState createState() => _TransitionScreenState();
}

class _TransitionScreenState extends State<TransitionScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          Padding(
            padding: EdgeInsets.only(bottom: 40, left: 20, right: 20),
            child: Align(
              alignment: Alignment.bottomCenter,
              child: LogosWidget()
            ),
          ),
          Align(
            alignment: Alignment.center,
            child: Container(
              height: 90,
              alignment: Alignment.center,
              child: CompleteIconWidget()
            ),
          ),
        ]
      ),
    );
  }
}
