import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class PoseImage extends StatelessWidget{
  final String pose;
  final Color color;
  PoseImage(this.pose, this.color);
  @override
  Widget build(BuildContext context) {
    return Container(
      width: 100,
      decoration: BoxDecoration(
        color: color,
        borderRadius: BorderRadius.all(Radius.circular(90))
      ),
      child: Image(
        // color: Colors.,
        width: MediaQuery.of(context).size.width * 0.95,
        image: AssetImage('assets/images/poses/$pose.png'),
      ),
    );
  }

}
