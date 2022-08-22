import 'package:controllerapp/pages/Connect.dart';
import 'package:flutter/material.dart';

class TourTime extends StatefulWidget {
  @override
  _TourTimeState createState() => _TourTimeState();
}

String time = "";

class _TourTimeState extends State<TourTime> {
  final _TIME = TextEditingController();
  final _formInfoKey = GlobalKey<FormState>();

  @override
  void dispose() {
    _TIME.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    var size = MediaQuery.of(context).size;
    return Scaffold(
      appBar: AppBar(
        title: Text("Tour Time"),
      ),
      body: GestureDetector(
        onTap: (){
          FocusScope.of(context).requestFocus(new FocusNode());
        },
        child: SingleChildScrollView(
          child: Container(
              decoration: BoxDecoration(
                  gradient: LinearGradient(
                      begin: Alignment.topCenter,
                      end: Alignment.bottomCenter,
                      colors:[
                        Colors.white10,
                        Colors.white
                      ]
                  )
              ),
              child: Column(
                children: <Widget>[
                  Container(height:45),
                  Container(
                    //height: 100,
                      width: size.width*.8,
                      //color: Colors.green,
                      child: Form(
                        key: _formInfoKey,
                        child: Card(
                          shape: RoundedRectangleBorder(
                              side: BorderSide(color: Colors.blue,width: 3),
                              borderRadius: BorderRadius.circular(20)),
                          child: Container(
                            child: Column(
                              children: <Widget>[
                                Container(height:30),
                                Container(
                                  width: size.width*.7,
                                  child: TextFormField(
                                    keyboardType: TextInputType.number,
                                    validator: (value){
                                      if(value.isEmpty) return "The Time is mandatory";
                                      return null;
                                    },
                                    controller: _TIME,
                                    decoration: InputDecoration(
                                      labelText: "Time",
                                      labelStyle: TextStyle(color: Colors.black87),
                                      contentPadding: EdgeInsets.fromLTRB(30, 10,20, 10),
                                      border: OutlineInputBorder(borderRadius: BorderRadius.circular(32)),
                                    ),
                                  ),
                                ),
                                Container(height:50),
                                Container(
                                    width: size.width*.6,
                                    height: 50,
                                    color: Colors.white,
                                    child: RaisedButton(
                                        child: Text("Save to send", style: TextStyle(
                                            color: Colors.white,fontSize: 20,fontWeight: FontWeight.w700),
                                        ),
                                        color: Colors.blue,
                                        onPressed: (){
                                          if(_formInfoKey.currentState.validate()){
                                            FocusScope.of(context).requestFocus(new FocusNode());
                                            time= _TIME.text;
                                            setState(() {
                                              controlBottonSend = 'Time';
                                            });
                                            Navigator.of(context).push(MaterialPageRoute(builder: (context)=>BluetoothPage()));
                                          }
                                        }
                                    )
                                ),
                                Container(height:30),
                              ],
                            ),
                          ),
                        ),
                      )
                  ),
                ],
              )
          ),
        ),
      ),

    );
  }
}
