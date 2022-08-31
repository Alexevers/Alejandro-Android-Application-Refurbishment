import 'package:flutter/material.dart';
import 'package:gcmobile/screens/widgets/logos.dart';

class InfoScreen extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
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
                      'PROJECT INFO',
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
            Padding(
              padding: const EdgeInsets.only(top: 50, left: 30, right: 30),
              child: Row(
                mainAxisSize: MainAxisSize.max,
                children: <Widget>[
                  Align(
                    alignment: Alignment.centerLeft,
                    child: Text('Developer: ',
                      style: TextStyle(
                        fontFamily: 'Poppins',
                        fontWeight: FontWeight.w700,
                        color: Colors.black54,
                        fontSize: 15
                      )
                    ),
                  ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 8, left: 30, right: 30),
              child: Row(
                mainAxisSize: MainAxisSize.max,
                children: <Widget>[
                  Align(
                    alignment: Alignment.centerLeft,
                    child: Text('Bruno Faé Faion',
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
              padding: const EdgeInsets.only(top: 20, left: 30, right: 30),
              child: Row(
                mainAxisSize: MainAxisSize.max,
                children: <Widget>[
                  Align(
                    alignment: Alignment.centerLeft,
                    child: Text('Main mentor: ',
                      style: TextStyle(
                        fontFamily: 'Poppins',
                        fontWeight: FontWeight.w700,
                        color: Colors.black54,
                        fontSize: 15
                      )
                    ),
                  ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 8, left: 30, right: 30),
              child: Row(
                mainAxisSize: MainAxisSize.max,
                children: <Widget>[
                  Align(
                    alignment: Alignment.centerLeft,
                    child: Text('Iván Santos González',
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
              padding: const EdgeInsets.only(top: 20, left: 30, right: 30),
              child: Row(
                mainAxisSize: MainAxisSize.max,
                children: <Widget>[
                  Align(
                    alignment: Alignment.centerLeft,
                    child: Text('Project Description',
                      style: TextStyle(
                        fontFamily: 'Poppins',
                        fontWeight: FontWeight.w700,
                        color: Colors.black54,
                        fontSize: 15
                      )
                    ),
                  ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 8, left: 30, right: 30),
              child: Row(
                mainAxisSize: MainAxisSize.max,
                mainAxisAlignment: MainAxisAlignment.start,
                children: <Widget>[
                  Flexible(
                    child:
                    Text(
                      'This is a project for Google Summer of Code 2020, the main goal of project is to add a cheap and accessible body gesture controller for Liquid Galaxy.',
                      textAlign: TextAlign.justify,
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
            Spacer(),
            Padding(
              padding: const EdgeInsets.only(left: 20, right: 20, bottom: 45),
              child: LogosWidget(),
            )
          ],
        )
      )
    );
  }

}
