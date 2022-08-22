import 'package:flutter/material.dart';

class Collaboration extends StatefulWidget {
  @override
  _CollaborationState createState() => _CollaborationState();
}

class _CollaborationState extends State<Collaboration> {
  @override
  Widget build(BuildContext context) {
    var size = MediaQuery.of(context).size;

    return Scaffold(
        key: UniqueKey(),
        appBar: AppBar(
          title: Text('Creation and collaboration'),
        ),
      body:Container(
        height: size.height,
        width: size.width,
        child: SingleChildScrollView(
          child: Column(
            children: <Widget>[
              Container(height: 40),
              Container(
               width: size.width*.95,
                child: Row(children: <Widget>[
                  Text("Project created by",textAlign: TextAlign.center,
                      style: TextStyle(color: Colors.black87,fontSize: 18,)),
                ],),),
              Card(
                shape: RoundedRectangleBorder(
                    side: BorderSide(color: Colors.blue,width: 2),
                    borderRadius: BorderRadius.circular(8)),
                child: ListTile(
                  leading: CircleAvatar(
                    backgroundColor: Colors.blue,
                    child: Icon(Icons.person),
                    foregroundColor: Colors.white,
                  ),
                  title: Text('Otávio Jesus França Oliveira'),
                  subtitle: Text('Linkedin: www.linkedin.com/in/otaviojfoliveira'),
                ),
              ),
              Container(height: 20),
              Container(
                width: size.width*.95,
                child: Row(children: <Widget>[
                  Text("Mentor",textAlign: TextAlign.center,
                      style: TextStyle(color: Colors.black87,fontSize: 18,)),
                ],),),
              Card(
                shape: RoundedRectangleBorder(
                    side: BorderSide(color: Colors.blue,width: 2),
                    borderRadius: BorderRadius.circular(8)),
                child: ListTile(
                  leading: CircleAvatar(
                    backgroundColor: Colors.blue,
                    child: Icon(Icons.person),
                    foregroundColor: Colors.white,
                  ),
                  title: Text("Andreu Ibáñez"),
                  //subtitle: Text('Personal info'),
                ),
              ),
              Container(height: 20),
              Container(
                width: size.width*.95,
                child: Row(children: <Widget>[
                  Text("Creating examples",textAlign: TextAlign.center,
                      style: TextStyle(color: Colors.black87,fontSize: 18,)),
                ],),),
              Card(
                shape: RoundedRectangleBorder(
                    side: BorderSide(color: Colors.blue,width: 2),
                    borderRadius: BorderRadius.circular(8)),
                child: ListTile(
                  leading: CircleAvatar(
                    backgroundColor: Colors.blue,
                    child: Icon(Icons.person),
                    foregroundColor: Colors.white,
                  ),
                  title: Text('Roger Colomo Espot'),
                  //subtitle: Text('Personal info'),
                ),
              ),
              Container(height: 20),
              Container(
                width: size.width*.95,
                child: Row(children: <Widget>[
                  Text("Assembly of the test prototype",textAlign: TextAlign.center,
                      style: TextStyle(color: Colors.black87,fontSize: 18,)),
                ],),),
              Card(
                shape: RoundedRectangleBorder(
                    side: BorderSide(color: Colors.blue,width: 2),
                    borderRadius: BorderRadius.circular(8)),
                child: ListTile(
                  leading: CircleAvatar(
                    backgroundColor: Colors.blue,
                    child: Icon(Icons.person),
                    foregroundColor: Colors.white,
                  ),
                  title: Text('Josep Cuñat Porta'),
                  //subtitle: Text('Personal info'),
                ),
              ),
              Container(height: 20),
              Container(
                width: size.width*.95,
                child: Row(children: <Widget>[
                  Text("Other collaborations",textAlign: TextAlign.center,
                      style: TextStyle(color: Colors.black87,fontSize: 18,)),
                ],),),
              Card(
                shape: RoundedRectangleBorder(
                    side: BorderSide(color: Colors.blue,width: 2),
                    borderRadius: BorderRadius.circular(8)),
                child: ListTile(
                  leading: CircleAvatar(
                    backgroundColor: Colors.blue,
                    child: Icon(Icons.person),
                    foregroundColor: Colors.white,
                  ),
                  title: Text("Guillem Felis de Dios \nGCIER's Students \nJaniru Semitha \nDiego Riveros"),
                  //subtitle: Text('Personal info'),
                ),
              ),
            ],
          ),
        ),
      ) ,
    );
  }
}
