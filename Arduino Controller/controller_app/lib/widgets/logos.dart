
import 'package:flutter/material.dart';

class LogosPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var size = MediaQuery.of(context).size;
    return  Container(
        //width: size.width*.50,
        color: Colors.white,
        child: Column(
          children: <Widget>[
            Row(
              children: <Widget>[
                //Container(width: size.width*.05,),
                Container(
                  height: size.height*.25,
                  width: size.width,
                  child: Image.asset(
                    "assets/gsoc.png",
                  ),
                ),
              ],
            ),
            Row(
              children: <Widget>[
                Container(width: size.width*.05,),
                Container(
                  height: size.height*.15,
                  width: size.width*.4,
                  child: Image.asset(
                    "assets/lgLab.png",
                  ),
                ),
                Container(width: size.width*.1,),
                Container(
                  height: size.height*.12,
                  width: size.width*.4,
                  child: Image.asset(
                    "assets/tic.png",
                  ),
                ),
              ],
            ),
            Row(
              children: <Widget>[
                Container(width: size.width*.05,),
                Container(
                  height: size.height*.2,
                  width: size.width*.4,
                  child: Image.asset(
                    "assets/pcital.jpg",
                  ),
                ),
                Container(width: size.width*.1,),
                Container(
                  height: size.height*.2,
                  width: size.width*.4,
                  child: Image.asset(
                    "assets/intech3d.JPG",
                  ),
                ),
              ],
            ),
            Row(
              children: <Widget>[
                Container(width: size.width*.05,),
                Container(
                  height: size.height*.1,
                  width: size.width*.4,
                  child: Image.asset(
                    "assets/LogoLGEU.png",
                  ),
                ),
                Container(width: size.width*.1,),
                Container(
                  height: size.height*.1,
                  width: size.width*.4,
                  child: Image.asset(
                    "assets/facens.png",
                  ),
                ),
              ],
            ),
            Row(
              children: <Widget>[
                //Container(width: size.width*.05,),
                Container(
                  height: size.height*.1,
                  width: size.width,
                  child: Image.asset(
                    "assets/lg_logo.png",
                  ),
                ),
              ],
            ),
          ],
        ),
      );
  }
}
