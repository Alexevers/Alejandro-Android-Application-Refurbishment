import 'package:controllerapp/ExamplePages/Football.dart';
import 'package:controllerapp/ExamplePages/RaccingCircuit.dart';
import 'package:controllerapp/ExamplePages/WordWonders.dart';
import 'package:flutter/material.dart';

import 'Connect.dart';


String dropdownValue = '';
var _Continents = ['America','Africa','Europe','Oceania','Asia'];
var select = '';
var _page ='1';
var colorController = Colors.black87;
String StringToSend;

class Exampleslist extends StatefulWidget {
  static final tag = 'examples-list';
  Exampleslist({Key key}):super(key:key);
  @override
  _ExampleslistState createState() => _ExampleslistState();
}

class _ExampleslistState extends State<Exampleslist> {

  var pageControl;

  @override
  Widget build(BuildContext context) {
    var appBar = AppBar(title: Text("Navigation Examples"));
    var size = MediaQuery.of(context).size;

    return Scaffold(
      appBar: appBar,
      body: Container(
        width: size.width,
        height: size.height,
        color: Colors.white,
        child: Column(
          children: <Widget>[
            SizedBox(height: 10),
            Row(
              children: <Widget>[
                Container(width: size.width * .025),
                Container(
                  width: size.width * .3,
                  height: size.height * .07,
                  child: SizedBox(
                    child: RaisedButton(
                      onPressed: () {
                        setState(() {
                          select = _Continents[0];
                          _page = '1';
                          colorController = Colors.green;
                          pageControl = futlist(select);
                        });
                      },
                      child: Row(
                        children: <Widget>[
                          //SizedBox(width: 5),
                          Text("Football \nStadiums", style: TextStyle(
                              color: Colors.white,
                              fontSize: 15,
                              fontWeight: FontWeight.w700),
                          ),
                        ],
                      ),
                      color: Colors.green,
                      elevation: 10,
                    ),
                  ),
                ),
                Container(width: size.width * .025),
                Container(
                  width: size.width * .3,
                  height: size.height * .07,
                  child: SizedBox(
                    child: RaisedButton(
                      onPressed: () {
                        setState(() {
                          select = _Continents[0];
                          _page = '2';
                          colorController = Colors.blue;
                          pageControl = RaccingPage(select);
                        });
                      },
                      child: Row(
                        children: <Widget>[
                          Text("Racing \nCircuits", style: TextStyle(
                              color: Colors.white,
                              fontSize: 15,
                              fontWeight: FontWeight.w700),
                          ),
                        ],
                      ),
                      color: Colors.blue,
                      elevation: 10,
                    ),
                  ),
                ),
                Container(width: size.width * .025),
                Container(
                  width: size.width * .3,
                  height: size.height * .07,
                  child: SizedBox(
                    child: RaisedButton(
                      onPressed: () {
                        setState(() {
                          //pageControl = ExamplePages[2];
                          pageControl = WordWondersPage(select);
                          select = '';
                          colorController = Colors.red;
                          _page = '3';
                        });
                      },
                      child: Row(
                        children: <Widget>[
                          Text("Word \nWonders", style: TextStyle(
                              color: Colors.white,
                              fontSize: 15,
                              fontWeight: FontWeight.w700),
                          ),
                        ],
                      ),
                      color: Colors.red,
                      elevation: 10,
                    ),
                  ),
                ),
              ],
            ),
            Container(height: 10),
            Container(
                width: size.width,
                height: size.height * .07,
                child: Padding(
                  padding: const EdgeInsets.all(1),
                  child: _SendController()
                )
            ),
            Container(height: 10),
            Expanded(
              child: Container(
                width: size.width * .96,
                height: size.height * .5,
                color: Colors.white,
                child: LayoutBuilder(
                    builder: (_, constraints) {
                      if (pageControl == null) {
                        return Container(
                          color: Colors.white,
                          child: Column(
                            children: <Widget>[
                              Container(
                                height: size.height*.5,
                                width: size.width*.95,
                                child: Card(
                                  color: Colors.blue,
                                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(60)),
                                    child: Center(
                                      child: Text("Hello! \n Select an option", textAlign: TextAlign.center,style: TextStyle(
                                          color: Colors.black87,fontSize: 28,fontWeight: FontWeight.w700),
                                      ),
                                    )
                                ),
                              ),
                            ],
                          ),
                        );
                      }
                      return pageControl;
                    }
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  _pageController(String selection) {
    if (_page == '1') {
      return futlist(selection);
    }
    if (_page == '2') {
      return RaccingPage(selection);
    }
    if (_page == '3') {
      return WordWondersPage(select);
    }
  }

  _SendController() {
    if(pageControl == null){
      return Container();
    }
    else{
      return Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          Container(),
          Text('Select a Continent', style: TextStyle(
              color: Colors.black, fontSize: 18)),
          DropdownButton<String>(
            hint: Text(select, style: TextStyle(
                color: Colors.black, fontSize: 20),
            ),
            items: _Continents.map((String value) {
              return DropdownMenuItem<String>(
                value: value,
                child: Text(value, style: TextStyle(
                    color: Colors.black, fontSize: 18),
                ),
              );
            }).toList(),
            onChanged: (String value) {
              setState(() {
                select = value;
                pageControl = _pageController(select);
              });
              //Navigator.of(context).pushReplacementNamed(ExamplesPage.tag);
            },
          ),
          Container(
              child: RaisedButton(
                  child: Text("Save\nto send", textAlign: TextAlign.center,style: TextStyle(
                      color: Colors.white,fontSize: 10,fontWeight: FontWeight.w700),
                  ),
                  color: colorController,
                  onPressed: (){
                    _makeString();
                    setState(() {
                      controlBottonSend = 'SendList';
                    });
                    Navigator.of(context).push(MaterialPageRoute(builder: (context)=>BluetoothPage()));
                  }
              )
          ),
          Container()
        ],
      );
    }
  }
}

void _makeString() {
  List<String> _listSelected;

  if(_page == '3'){
     _listSelected = ListWord [0];
  }
  if(_page == '2' && select!= null){
    switch(select)
    {
      case 'America':
        {
           _listSelected = raccingCircuits [0];
        }
        break;
      case 'Africa':
        {
           _listSelected = raccingCircuits [1];
        }
        break;
      case 'Europe':
        {
           _listSelected = raccingCircuits [2];
        }
        break;
      case 'Oceania':
        {
          _listSelected = raccingCircuits [3];
        }
        break;
      case 'Asia':
        {
          _listSelected = raccingCircuits [4];
        }
        break;
    }
  }
  if(_page == '1' && select!= null){
    switch(select)
    {
      case 'America':
        {
          _listSelected = footStadiums[0];
        }
        break;
      case 'Africa':
        {
          _listSelected = footStadiums[1];
        }
        break;
      case 'Europe':
        {
          _listSelected = footStadiums[2];
        }
        break;
      case 'Oceania':
        {
          _listSelected = footStadiums[3];
        }
        break;
      case 'Asia':
        {
          _listSelected = footStadiums[4];
        }
        break;
    }
  }
  StringToSend = "";
  for(var i = 0; i<(_listSelected.length);i++)
    {
      String pass =  _listSelected[i];
      if(i < (_listSelected.length - 1)){
        StringToSend = StringToSend + pass + ",*";
      }
      else{
        StringToSend = StringToSend + pass + ",";
      }

    }

  //print(StringToSend);
}