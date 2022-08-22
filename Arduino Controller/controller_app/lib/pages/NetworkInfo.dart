import 'package:flutter/material.dart';
import 'package:controllerapp/main.dart';

import 'Connect.dart';

class NetInfoPage extends StatefulWidget {
  static final tag = 'netinfo-page';
  NetInfoPage({Key key}):super(key:key);
  @override
  _NetInfoPageState createState() => _NetInfoPageState();
}

String netIP = "";
String netSSID = "";
String netPASSWORD ="";

class _NetInfoPageState extends State<NetInfoPage> {
  final _LGIP = TextEditingController();
  final _SSID = TextEditingController();
  final _PASSWORD = TextEditingController();
  bool _showPassword = true;
  final _formInfoKey = GlobalKey<FormState>();

  @override
  void dispose() {
    _LGIP.dispose();
    _SSID.dispose();
    _PASSWORD.dispose();
    super.dispose();
  }
  @override
  Widget build(BuildContext context) {
    var size = MediaQuery.of(context).size;
    return Scaffold(
      appBar: AppBar(
        title: Text("Network Information"),
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
                                      if(value.isEmpty) return "The Master IP is mandatory";
                                      return null;
                                    },
                                    controller: _LGIP,
                                    decoration: InputDecoration(
                                      labelText: "LG Master IP",
                                      labelStyle: TextStyle(color: Colors.black87),
                                      contentPadding: EdgeInsets.fromLTRB(30, 10,20, 10),
                                      border: OutlineInputBorder(borderRadius: BorderRadius.circular(32)),
                                    ),
                                  ),
                                ),
                                Container(height:15),
                                Container(
                                  width: size.width*.7,
                                  child: TextFormField(
                                    keyboardType: TextInputType.text,
                                    validator: (value){
                                      if(value.isEmpty) return "The SSID is mandatory";
                                      return null;
                                    },
                                    controller: _SSID,
                                    decoration: InputDecoration(
                                      labelText: "SSID",
                                      labelStyle: TextStyle(color: Colors.black87),
                                      contentPadding: EdgeInsets.fromLTRB(30, 10,20, 10),
                                      border: OutlineInputBorder(borderRadius: BorderRadius.circular(32)),
                                    ),
                                  ),
                                ),
                                Container(height:15),
                                Container(
                                  width: size.width*.7,
                                  child: TextFormField(
                                    keyboardType: TextInputType.text,
                                    validator: (value){
                                      if(value.isEmpty) return "The PASSWORD is mandatory";
                                      return null;
                                    },
                                    obscureText: _showPassword,
                                    controller: _PASSWORD,
                                    decoration: InputDecoration(
                                        labelText: "PASSWORD",
                                        labelStyle: TextStyle(color: Colors.black87),
                                        contentPadding: EdgeInsets.fromLTRB(30, 10,20, 10),
                                        border: OutlineInputBorder(borderRadius: BorderRadius.circular(32)),
                                        suffixIcon: GestureDetector(
                                          child: Icon(_showPassword == false ? Icons.visibility :Icons.visibility_off,size: 30,),
                                          onTap: (){
                                            setState(() {
                                              _showPassword =! _showPassword;
                                            });
                                          },
                                        )
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
                                            netIP = _LGIP.text;
                                            netSSID = _SSID.text;
                                            netPASSWORD = _PASSWORD.text;
                                            setState(() {
                                              controlBottonSend = 'NetInfo';
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
