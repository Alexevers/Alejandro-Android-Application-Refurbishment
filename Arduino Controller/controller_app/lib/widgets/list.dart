import 'package:controllerapp/pages/Connect.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_slidable/flutter_slidable.dart';

import '../pages/MyPlaces.dart';

var indexcontroledit = 0;
String NewStringToSend;

class ListPage extends StatefulWidget {
  static final tag = 'list-page';
  @override
  _ListPageState createState() => _ListPageState();
}


class _ListPageState extends State<ListPage> {
  final TextEditingController _name = new TextEditingController();
  final TextEditingController _longitude = new TextEditingController();
  final TextEditingController _latitude  = new TextEditingController();
  final TextEditingController _range  = new TextEditingController();

  @override
  Widget build(BuildContext context) {
    var size = MediaQuery.of(context).size;
    final _formData = GlobalKey<FormState>();
    final _formEdit = GlobalKey<FormState>();

    List<List> ListEdit = ListWord[indexcontrol];
    List<String> placeName;
    List<String> placeCoordinate;
    placeName = ListEdit[0];
    if( placeName.length != 0)
      {
        placeName = ListEdit[0];
        placeCoordinate = ListEdit[1];
      }
    else{
       placeName = ['No list'];
       placeCoordinate = [''];
    }

    return Scaffold(
       key: UniqueKey(),
      appBar: AppBar(
        title: Text('Edit List'),
      ),
      body: Container(
        height: size.height,
        width: size.width,
        child: Column(
          children: <Widget>[
            Container(
              alignment: Alignment.topLeft,
              width: size.width,
              height: 60 ,
              color: Colors.white,
              child: LayoutBuilder(
                builder: (_,constraints){
                  return Row(
                    children: <Widget>[
                      Container(width: constraints.maxWidth*.05),
                      Container(
                        width: constraints.maxWidth*.15,
                        child: Row(children: <Widget>[
                          Text("List: ",textAlign: TextAlign.center,
                            style: TextStyle(color: Colors.black87,fontSize: 20,)),
                      ],),),
                      Container(width: constraints.maxWidth*.02),
                      Container(
                        width: constraints.maxWidth*.30,
                        child: Row(children: <Widget>[
                          Text(ListWordName[indexcontrol],textAlign: TextAlign.center,
                              style: TextStyle(color: Colors.black87,fontSize: 18,)),
                        ],),
                      ),
                      Container(
                        child: SizedBox(
                          child: RaisedButton(
                            onPressed: (){
                              if(placeName.length < 16)
                                {
                                  showDialog(
                                      context: context,
                                      barrierDismissible:false,
                                      builder: (BuildContext context){
                                        return Form(
                                          key: _formData,
                                          child: AlertDialog(
                                            title: Text('Add Places'),
                                            content: SingleChildScrollView(
                                              child: ListBody(
                                                children: <Widget>[
                                                  TextFormField(
                                                    keyboardType: TextInputType.text,
                                                    controller: _name,
                                                    validator: (value){
                                                      if(value.isEmpty) return "The name is mandatory";
                                                      return null;
                                                    },
                                                    decoration: InputDecoration(
                                                        hintText: "Place Name",
                                                        contentPadding: EdgeInsets.fromLTRB(20, 10, 20, 10),
                                                        border: OutlineInputBorder(borderRadius: BorderRadius.circular(32))
                                                    ),
                                                  ),
                                                  Container(height:10,),
                                                  TextFormField(
                                                    keyboardType: TextInputType.number,
                                                    controller: _longitude,
                                                    validator: (value){
                                                      if(value.isEmpty) return "The longitude is mandatory";
                                                      return null;
                                                    },
                                                    decoration: InputDecoration(
                                                        hintText: "Longitude",
                                                        contentPadding: EdgeInsets.fromLTRB(20, 10, 20, 10),
                                                        border: OutlineInputBorder(borderRadius: BorderRadius.circular(32))
                                                    ),
                                                  ),
                                                  Container(height:10,),
                                                  TextFormField(
                                                    keyboardType: TextInputType.number,
                                                    controller: _latitude,
                                                    validator: (value){
                                                      if(value.isEmpty) return "The latitude is mandatory";
                                                      return null;
                                                    },
                                                    decoration: InputDecoration(
                                                        hintText: "Latitude",
                                                        contentPadding: EdgeInsets.fromLTRB(20, 10, 20, 10),
                                                        border: OutlineInputBorder(borderRadius: BorderRadius.circular(32))
                                                    ),
                                                  ),
                                                  Container(height:10,),
                                                  TextFormField(
                                                    keyboardType: TextInputType.number,
                                                    controller: _range,
                                                    validator: (value){
                                                      if(value.isEmpty) return "The range is mandatory";
                                                      return null;
                                                    },
                                                    decoration: InputDecoration(
                                                        hintText: "Range",
                                                        contentPadding: EdgeInsets.fromLTRB(20, 10, 20, 10),
                                                        border: OutlineInputBorder(borderRadius: BorderRadius.circular(32))
                                                    ),
                                                  )
                                                ],
                                              ),
                                            ),
                                            actions: <Widget>[
                                              RaisedButton(
                                                color:Colors.green,
                                                child: Text('Cancel'),
                                                onPressed: (){Navigator.of(context).pop();},
                                              ),
                                              RaisedButton(
                                                color:Colors.blue,
                                                child: Text('Add'),
                                                onPressed: (){
                                                  if(_formData.currentState.validate()) {
                                                    String newcoord = _longitude.text +','+ _latitude.text +','+ _range.text;
                                                    ListWord[indexcontrol][0].add(_name.text);
                                                    ListWord[indexcontrol][1].add(newcoord);
                                                    _closeInput();
                                                    Navigator.of(context).pop();
                                                  }},
                                              )
                                            ],
                                          ),
                                        );
                                      }
                                  );
                                }
                              else{
                                showDialog(
                                    context: context,
                                    barrierDismissible: true,
                                    builder: (BuildContext context){
                                      return AlertDialog(
                                        title: Text('Alert!'),
                                        content: Text('The list can only have 16 places'),
                                        actions: <Widget>[
                                          RaisedButton(
                                            color: Colors.green,
                                            child: Text('Ok'),
                                            onPressed: (){
                                              Navigator.of(context).pop();
                                            },
                                          ),
                                        ],
                                      );
                                    }
                                );
                              }
                            },
                            child: Text("Add", style: TextStyle(
                                color: Colors.white,fontSize: 20,fontWeight: FontWeight.w700),
                            ),
                            color: Colors.blue,
                            elevation: 10,
                          ),
                        ),
                        width: constraints.maxWidth*.2,
                        height: constraints.maxHeight*.70,
                        alignment: Alignment.topCenter,
                      ),
                      Container(width: constraints.maxWidth*.02,),
                      Container(
                        child: SizedBox(
                          child: RaisedButton(
                              onPressed: (){
                                if(placeName.length < 16){
                                  showDialog(
                                      context: context,
                                      barrierDismissible: true,
                                      builder: (BuildContext context){
                                        return AlertDialog(
                                          title: Text('Alert!'),
                                          content: Text('The list must contain 16 places to be sent'),
                                          actions: <Widget>[
                                            RaisedButton(
                                              color: Colors.green,
                                              child: Text('Ok'),
                                              onPressed: (){
                                                Navigator.of(context).pop();
                                              },
                                            ),
                                          ],
                                        );
                                      }
                                  );
                                }
                                else{
                                  _StringConstructor();
                                  setState(() {
                                    controlBottonSend = 'SendNewList';
                                  });
                                  Navigator.of(context).push(MaterialPageRoute(builder: (context)=>BluetoothPage()));
                                }
                              },
                            child: Text("Send", style: TextStyle(
                                color: Colors.white,fontSize: 20,fontWeight: FontWeight.w700),
                            ),
                            color: Colors.green,
                            elevation: 10,
                          ),
                        ),
                        width: constraints.maxWidth*.22,
                        height: constraints.maxHeight*.70,
                        alignment: Alignment.topCenter,
                      )
                    ],
                  );
                },
              ),
            ),
            Container(height: 2,color: Colors.blue),
            Container(height: 8),
            Expanded(
              child: Container(
                width: size.width*.96,
                color: Colors.white,
                child: LayoutBuilder(
                    builder: (_,constraints){
                      return Container(
                        color: Colors.white,
                        child: Container(
                          child: ListView.builder(
                              padding: const EdgeInsets.all(5),
                              itemCount:placeName.length,
                              itemBuilder: (BuildContext context, int index){
                                var te = index +1;
                                return Card(
                                  shape: RoundedRectangleBorder(
                                      side: BorderSide(color: Colors.blue,width: .5),
                                      borderRadius: BorderRadius.circular(5)),
                                  child: Slidable(
                                    actionPane: SlidableDrawerActionPane(),
                                    actionExtentRatio: 0.25,
                                    child: ListTile(
                                      leading: CircleAvatar(
                                        backgroundColor: Colors.blue,
                                        child: Text("$te"),
                                        foregroundColor: Colors.white,
                                      ),
                                      title: Text(placeName[index]),
                                      subtitle: Text(placeCoordinate[index]),
                                    ),
                                    secondaryActions: <Widget>[
                                      IconSlideAction(
                                        caption: 'Edit',
                                        color: Colors.green,
                                        icon: Icons.edit,
                                        onTap: () {
                                          showDialog(
                                              context: context,
                                              barrierDismissible:false,
                                              builder: (BuildContext context){
                                                return Form(
                                                  key: _formData,
                                                  child: AlertDialog(
                                                    title: Text('Edit Place'),
                                                    content: SingleChildScrollView(
                                                      child: ListBody(
                                                        children: <Widget>[
                                                          TextFormField(
                                                            keyboardType: TextInputType.text,
                                                            controller: _name,
                                                            validator: (value){
                                                              if(value.isEmpty) return "The name is mandatory";
                                                              return null;
                                                            },
                                                            decoration: InputDecoration(
                                                                hintText: "Place Name",
                                                                contentPadding: EdgeInsets.fromLTRB(20, 10, 20, 10),
                                                                border: OutlineInputBorder(borderRadius: BorderRadius.circular(32))
                                                            ),
                                                          ),
                                                          Container(height:10,),
                                                          TextFormField(
                                                            keyboardType: TextInputType.number,
                                                            controller: _longitude,
                                                            validator: (value){
                                                              if(value.isEmpty) return "The longitude is mandatory";
                                                              return null;
                                                            },
                                                            decoration: InputDecoration(
                                                                hintText: "Longitude",
                                                                contentPadding: EdgeInsets.fromLTRB(20, 10, 20, 10),
                                                                border: OutlineInputBorder(borderRadius: BorderRadius.circular(32))
                                                            ),
                                                          ),
                                                          Container(height:10,),
                                                          TextFormField(
                                                            keyboardType: TextInputType.number,
                                                            controller: _latitude,
                                                            validator: (value){
                                                              if(value.isEmpty) return "The latitude is mandatory";
                                                              return null;
                                                            },
                                                            decoration: InputDecoration(
                                                                hintText: "Latitude",
                                                                contentPadding: EdgeInsets.fromLTRB(20, 10, 20, 10),
                                                                border: OutlineInputBorder(borderRadius: BorderRadius.circular(32))
                                                            ),
                                                          ),
                                                          Container(height:10,),
                                                          TextFormField(
                                                            keyboardType: TextInputType.number,
                                                            controller: _range,
                                                            validator: (value){
                                                              if(value.isEmpty) return "The range is mandatory";
                                                              return null;
                                                            },
                                                            decoration: InputDecoration(
                                                                hintText: "Range",
                                                                contentPadding: EdgeInsets.fromLTRB(20, 10, 20, 10),
                                                                border: OutlineInputBorder(borderRadius: BorderRadius.circular(32))
                                                            ),
                                                          )
                                                        ],
                                                      ),
                                                    ),
                                                    actions: <Widget>[
                                                      RaisedButton(
                                                        color:Colors.green,
                                                        child: Text('Cancel'),
                                                        onPressed: (){Navigator.of(context).pop();},
                                                      ),
                                                      RaisedButton(
                                                        color:Colors.blue,
                                                        child: Text('Save'),
                                                        onPressed: (){
                                                          if(_formData.currentState.validate()) {
                                                            String newcoord = _longitude.text +','+ _latitude.text +','+ _range.text;
                                                            ListWord[indexcontrol][0].removeAt(index);
                                                            ListWord[indexcontrol][1].removeAt(index);
                                                            ListWord[indexcontrol][0].insert(index, _name.text);
                                                            ListWord[indexcontrol][1].insert(index, newcoord);
                                                            _closeInput();
                                                            Navigator.of(context).pop();
                                                          }},
                                                      )
                                                    ],
                                                  ),
                                                );
                                              }
                                          );
                                        },
                                      ),
                                      IconSlideAction(
                                        caption: 'Delete',
                                        color: Colors.red,
                                        icon: Icons.delete_forever,
                                        onTap: () {
                                          showDialog(
                                              context: context,
                                              barrierDismissible: true,
                                              builder: (BuildContext context){
                                                return AlertDialog(
                                                  title: Text('Delete'),
                                                  content: Text('If you click delete the file will be deleted and cannot be recovered'),
                                                  actions: <Widget>[
                                                    RaisedButton(
                                                      color: Colors.green,
                                                      child: Text('Cancel'),
                                                      onPressed: (){
                                                        Navigator.of(context).pop();
                                                      },
                                                    ),
                                                    RaisedButton(
                                                      color: Colors.red,
                                                      child: Text('Delete'),
                                                      onPressed: (){
                                                        ListWord[indexcontrol][0].removeAt(index);
                                                        ListWord[indexcontrol][1].removeAt(index);
                                                        Navigator.of(context).pop();
                                                        Navigator.pushReplacement(context, MaterialPageRoute(builder: (BuildContext context) => ListPage()));
                                                      },
                                                    )
                                                  ],
                                                );
                                              }
                                          );
                                        },
                                      )
                                    ],
                                  ),
                                );
                              }
                          ),
                        ),
                      );
                      // return HomeList();
                    }
                ),
              ),
            ),
          ],
        ),
      )
    );
  }

  void _closeInput() {
    _name.clear();
    _longitude.clear();
    _latitude.clear();
    _range.clear();
  }

  void _StringConstructor() {
    List<String> _listSelected = ListWord[indexcontrol][1];
    NewStringToSend = "";
    for(var i = 0; i<(_listSelected.length);i++)
    {
      String pass =  _listSelected[i];

      if(i < (_listSelected.length - 1)){
        NewStringToSend = NewStringToSend +_listSelected[i] + ",*";
      }
      else{
        NewStringToSend = NewStringToSend +_listSelected[i] + ",";
      }
    }
   // print(NewStringToSend);
  }
}

