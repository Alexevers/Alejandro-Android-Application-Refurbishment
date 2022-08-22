import 'package:controllerapp/widgets/collaboration.dart';
import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';


class Info extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var size = MediaQuery.of(context).size;

    return Scaffold(
      appBar: AppBar(
        title: Text("About this project"),
      ),
      body: Container(
        //width: size.width*.50,
        color: Colors.white,
        child: Column(
          children: <Widget>[
            Row(
              children: <Widget>[
                //Container(width: size.width*.05,),
                Container(
                  height: size.height*.30,
                  width: size.width,
                  child: Image.asset(
                    "assets/gsoc.png",
                  ),
                ),
              ],
            ),
            Row(
              children: <Widget>[
                Container(
                  height: size.height*.02,
                ),
              ],
            ),
            Center(
              child: Container(
                width: size.width*.5,
                height: size.height*.05,
                color: Colors.green,
                child: SizedBox(
                  child: RaisedButton(
                    onPressed: (){
                      String URL = 'https://summerofcode.withgoogle.com/';
                      _launchURL(URL);
                    },
                    child: Row(
                      children: <Widget>[
                        Icon(Icons.insert_link,color: Colors.white,),
                        SizedBox(width: 30),
                        Text("GSoC", style: TextStyle(
                            color: Colors.white,fontSize: 15,fontWeight: FontWeight.w700),
                        ),
                      ],
                    ),
                      color: Colors.green,
                      elevation: 10,
                  ),
                ),
              ),
            ),
            Container(height:18),
            Center(
              child: Container(
                width: size.width*.5,
                height: size.height*.05,
                color: Colors.green,
                child: SizedBox(
                  child: RaisedButton(
                    onPressed: (){
                      String URLGit = 'https://github.com/LiquidGalaxyLAB/Arduino-Controller';
                      _launchURL(URLGit);
                    },
                    child: Row(
                      children: <Widget>[
                        Icon(Icons.code,color: Colors.white,),
                        SizedBox(width: 20),
                        Text("Git Hub Code", style: TextStyle(
                            color: Colors.white,fontSize: 15,fontWeight: FontWeight.w700),
                        ),
                      ],
                    ),
                    color: Colors.black87,
                    elevation: 10,
                  ),
                ),
              ),
            ),
            Container(height:18),
            Center(
              child: Container(
                width: size.width*.5,
                height: size.height*.05,
                color: Colors.green,
                child: SizedBox(
                  child: RaisedButton(
                    onPressed: (){
                      String URLYouTube = 'https://www.youtube.com/watch?v=X55gbz8ri1w&t=710s';
                      _launchURL(URLYouTube);
                    },
                    child: Row(
                      children: <Widget>[
                        Icon(Icons.ondemand_video,color: Colors.white,),
                        SizedBox(width: 10),
                        Text("YouTube Videos", style: TextStyle(
                            color: Colors.white,fontSize: 15,fontWeight: FontWeight.w700),
                        ),
                      ],
                    ),
                    color: Colors.red,
                    elevation: 10,
                  ),
                ),
              ),
            ),
            Container(height:18),
            Center(
              child: Container(
                width: size.width*.5,
                height: size.height*.05,
                color: Colors.green,
                child: SizedBox(
                  child: RaisedButton(
                    onPressed: (){
                      String URLYouTube = 'https://www.liquidgalaxy.eu/';
                      _launchURL(URLYouTube);
                    },
                    child: Row(
                      children: <Widget>[
                        Icon(Icons.home,color: Colors.white,),
                        SizedBox(width: 20),
                        Text("Community", style: TextStyle(
                            color: Colors.white,fontSize: 15,fontWeight: FontWeight.w700),
                        ),
                      ],
                    ),
                    color: Colors.blue,
                    elevation: 10,
                  ),
                ),
              ),
            ),
            Container(height:18),
            Center(
              child: Container(
                width: size.width*.5,
                height: size.height*.05,
                color: Colors.green,
                child: SizedBox(
                  child: RaisedButton(
                    onPressed: (){
                      Navigator.of(context).push(MaterialPageRoute(builder: (context)=>Collaboration()));
                    },
                    child: Row(
                      children: <Widget>[
                        Icon(Icons.people,color: Colors.white,),
                        SizedBox(width: 20),
                        Text("Collaborators", style: TextStyle(
                            color: Colors.white,fontSize: 15,fontWeight: FontWeight.w700),
                        ),
                      ],
                    ),
                    color: Colors.orange,
                    elevation: 10,
                  ),
                ),
              ),
            ),
            Container(height: 30),
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
      )

    );
  }
}

void  _launchURL(String url)async{
  if (await canLaunch(url)) {
    await launch(url);
  } else {
    throw 'Could not launch $url';
  }
}