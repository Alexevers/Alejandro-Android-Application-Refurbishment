import 'package:controllerapp/pages/ExamplesList.dart';
import 'package:controllerapp/pages/NetworkInfo.dart';
import 'package:controllerapp/pages/About.dart';
import 'package:controllerapp/pages/TourTime.dart';
import 'pages/MyPlaces.dart';
import 'file:///C:/Users/Alejandro/StudioProjects/Arduino-Controller/controller_app/lib/widgets/logos.dart';
import 'package:flutter/material.dart';
import 'package:foldable_sidebar/foldable_sidebar.dart';
import 'package:swipedetector/swipedetector.dart';
import 'dart:async';

import 'pages/Connect.dart';
import 'widgets/list.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  final routes = <String, WidgetBuilder>{
    MyHomePage.tag:(context) =>MyHomePage(),
    ListPage.tag: (context) => ListPage(),
    MyPlaces.tag:(context) => MyPlaces(),
    NetInfoPage.tag:(context) => NetInfoPage(),
    Exampleslist.tag:(context) => Exampleslist()
  };

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: SplashScreen(),
      routes: routes,
    );
  }
}

class SplashScreen extends StatefulWidget {
  @override
  _SplashScreenState createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  @override
  void initState() {
    super.initState();
    Timer(Duration(seconds: 7),()=>Navigator.of(context).popAndPushNamed(MyHomePage.tag));
  }
  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.blue,
      child: Column(
        children: <Widget>[
          Container(
            height: 150,
          ),
          Image.asset(
            "assets/giflogo.gif",
            width: 400,
            height: 400,
          ),
        ],
      )
    );
  }
}

class MyHomePage extends StatefulWidget {
  static final tag = 'myhome-page';
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  FSBStatus drawerStatus;

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body:  SwipeDetector(
          onSwipeRight: (){
            setState(() {
              drawerStatus = FSBStatus.FSB_OPEN;
            });
          },
          onSwipeLeft: (){
            setState(() {
              drawerStatus = FSBStatus.FSB_CLOSE;
            });
          },
          child: FoldableSidebarBuilder(
            drawerBackgroundColor: Colors.blue,
            drawer: CustomDrawer(closeDrawer: (){
              setState(() {
                drawerStatus = FSBStatus.FSB_CLOSE;
              });
            },),
            screenContents: Scaffold(
              appBar: AppBar(
                title: Text("Arduino LG Controller"),
                backgroundColor: Colors.blue,
                leading: IconButton(icon: Icon(Icons.menu,color: Colors.white,size: 35,),
                    onPressed: (){
                      setState(() {
                        drawerStatus = drawerStatus == FSBStatus.FSB_OPEN ? FSBStatus.FSB_CLOSE : FSBStatus.FSB_OPEN;
                      });
                    }),
              ),
              body: FoldableSidebarBuilder(
                screenContents: FirstScreen(), status: null, drawer: null,
              ),
            ),
            status: drawerStatus,
          ),
        ),
      ),
    );
  }
}

class FirstScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {

    return LogosPage();
  }
}

class CustomDrawer extends StatelessWidget {

  final Function closeDrawer;

  const CustomDrawer({Key key, this.closeDrawer}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    MediaQueryData mediaQuery = MediaQuery.of(context);
    var size = MediaQuery.of(context).size;
    return Container(
      color: Colors.white,
      width: mediaQuery.size.width * 0.60,
      height: size.height,
      child: Column(
        children: <Widget>[
          Container(
              width: double.infinity,
              height: 200,
              color: Colors.blue, 
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Image.asset(
                    "assets/logoControl.png",
                    width: 150,
                    height: 150,
                  ),
                  SizedBox(
                    height: 1,
                  ),
                ],
              )),
          Expanded(
            child: Container(
              child: LayoutBuilder(
                builder: (_,constraints){
                  return Container(
                    height: constraints.maxHeight,
                    child: ListView(
                      children: <Widget>[
                        ListTile(
                          onTap: (){
                            Navigator.of(context).push(MaterialPageRoute(builder: (context)=>BluetoothPage()));
                            closeDrawer();
                          },
                          leading: Icon(Icons.bluetooth_connected),
                          title: Text(
                            "Connect",
                          ),
                        ),
                        Divider(
                          height: 1,
                          color: Colors.grey,
                        ),
                        ListTile(
                          onTap: () {
                            Navigator.of(context).push(MaterialPageRoute(builder: (context)=>MyPlaces()));
                            //Navigator.of(context).pushNamed(KeyBoardPage.tag);
                            closeDrawer();
                          },
                          leading: Icon(Icons.list),
                          title: Text("My Places"),
                        ),
                        Divider(
                          height: 1,
                          color: Colors.grey,
                        ),
                        ListTile(
                          onTap: () {
                            Navigator.of(context).push(MaterialPageRoute(builder: (context)=>Exampleslist()));
                            //Navigator.of(context).pushNamed(ExamplesPage.tag);
                            closeDrawer();
                          },
                          leading: Icon(Icons.add_to_photos),
                          title: Text("Examples"),
                        ),
                        Divider(
                          height: 1,
                          color: Colors.grey,
                        ),
                        ListTile(
                          onTap: () {
                            Navigator.of(context).push(MaterialPageRoute(builder: (context)=>NetInfoPage()));
                            closeDrawer();
                           //debugPrint("Settings");
                          },
                          leading: Icon(Icons.wifi),
                          title: Text("Network Info"),
                        ),
                        Divider(
                          height: 1,
                          color: Colors.grey,
                        ),
                        ListTile(
                          onTap: () {
                            Navigator.of(context).push(MaterialPageRoute(builder: (context)=>TourTime()));
                            closeDrawer();
                          },
                          leading: Icon(Icons.access_time),
                          title: Text("Tour Time"),
                        ),
                        Divider(
                          height: 1,
                          color: Colors.grey,
                        ),
                        ListTile(
                          onTap: () {
                            Navigator.of(context).push(MaterialPageRoute(builder: (context)=>Info()));
                            closeDrawer();
                          },
                          leading: Icon(Icons.info_outline),
                          title: Text("About"),
                        ),
                      ],
                    ),
                  );
                },
              ),
            ),
          ),
        ],
      ),
    );
  }
}