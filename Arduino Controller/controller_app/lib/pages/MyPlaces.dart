import 'dart:ui';
import 'package:controllerapp/widgets/list.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_slidable/flutter_slidable.dart';

class MyPlaces extends StatefulWidget {
  static final tag = 'myplaces-page';
  @override
  _MyPlacesState createState() => _MyPlacesState();
}

List<String> ListWordName = List();
List<List> ListWord = [];
var indexcontrol = 0;

class _MyPlacesState extends State<MyPlaces> {
  final TextEditingController _c = new TextEditingController();
  final _formNameKey = GlobalKey<FormState>();
  final _formNameKeyEdit = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    var appBar = AppBar(title: Text("My Places"));
    var size = MediaQuery.of(context).size;
    return Scaffold(
      key: UniqueKey(),
      appBar: appBar,
      body: Container(
        width: size.width,
        height: size.height,
        child: Column(
          children: <Widget>[
            Container(height: 2),
            Container(
              width: size.width,
              height: 40,
              color: Colors.white,
              child: Center(
                  child: Text("Add a new navigation list",textAlign: TextAlign.center,
                    style: TextStyle(color: Colors.blue,fontSize: 20,fontWeight: FontWeight.w700,),
                  )),
            ),
            Container(
              width: size.width,
              height: 60 ,
              color: Colors.white,
              child: LayoutBuilder(
                builder: (_,constraints){
                  return Row(
                    children: <Widget>[
                      Container(width: constraints.maxWidth*.48),
                      Container(
                        child: SizedBox(
                          child: RaisedButton(
                            onPressed: (){
                              showDialog(
                                context: context,
                                builder: (BuildContext ctx){
                                  return Form(
                                    key: _formNameKey,
                                    child: AlertDialog(
                                      title: Text('New List'),
                                      content: SingleChildScrollView(
                                        child: ListBody(
                                          children: <Widget>[
                                            TextFormField(
                                              keyboardType: TextInputType.text,
                                              validator: (value){
                                                if(value.isEmpty) return "Name is mandatory";
                                                return null;
                                              },
                                              controller: _c,
                                              decoration: InputDecoration(
                                                  hintText: 'List name',
                                                  contentPadding: EdgeInsets.fromLTRB(20, 10, 20, 10),
                                                  border: OutlineInputBorder(borderRadius: BorderRadius.circular(32))
                                              ),
                                            )
                                          ],
                                        ),
                                      ),
                                      actions: <Widget>[
                                        RaisedButton(
                                          color: Colors.green,
                                            child: Text('Cancel'),
                                            onPressed: (){
                                            _c.clear();
                                            Navigator.of(ctx).pop();
                                            }
                                        ),
                                        RaisedButton(
                                            color: Colors.blue,
                                            child: Text('Add'),
                                            onPressed: (){
                                              if(_formNameKey.currentState.validate()) {
                                                ListWord.add(<List>[<String>[],<String>[]]);
                                                //ListWord.add(<List>[<String>['No list'],<String>['No list']],);
                                              //ListWord.add(<List>[]);
                                                ListWordName.add(_c.text);
                                                _c.clear();
                                                Navigator.of(ctx).pop();
                                              }
                                            }
                                        )
                                      ],
                                    ),
                                  );
                                }
                              );
                            },
                            child: Text("Add new list", style: TextStyle(color: Colors.white,fontSize: 20,fontWeight: FontWeight.w700)),
                            color: Colors.blue,
                            elevation: 10,
                          ),
                        ),
                        width: constraints.maxWidth*.50,
                        height: constraints.maxHeight*.70,
                      )
                    ],
                  );
                },
              ),
            ),
            Container(height: 10),
            Expanded(
              child: Container(
                child: ListView.builder(
                  itemCount: ListWordName.length,
                  itemBuilder: (context, index){
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
                          title: Text(ListWordName[index]),
                          onLongPress: (){
                            showDialog(
                                context: context,
                                builder: (BuildContext ctx){
                                  return Form(
                                    key: _formNameKeyEdit,
                                    child: AlertDialog(
                                      title: Text('Edit List'),
                                      content: SingleChildScrollView(
                                        child: ListBody(
                                          children: <Widget>[
                                            TextFormField(
                                              keyboardType: TextInputType.text,
                                              validator: (value){
                                                if(value.isEmpty) return "Name is mandatory";
                                                return null;
                                              },
                                              controller: _c,
                                              decoration: InputDecoration(
                                                  hintText: 'New name',
                                                  contentPadding: EdgeInsets.fromLTRB(20, 10, 20, 10),
                                                  border: OutlineInputBorder(borderRadius: BorderRadius.circular(32))
                                              ),
                                            )
                                          ],
                                        ),
                                      ),
                                      actions: <Widget>[
                                        RaisedButton(
                                            color: Colors.green,
                                            child: Text('Cancel'),
                                            onPressed: (){
                                              _c.clear();
                                              Navigator.of(ctx).pop();
                                            }
                                        ),
                                        RaisedButton(
                                            color: Colors.blue,
                                            child: Text('Save'),
                                            onPressed: (){
                                              if(_formNameKeyEdit.currentState.validate()) {
                                                ListWordName.removeAt(index);
                                                ListWordName.insert(index, _c.text);
                                                Navigator.of(ctx).pop();
                                                _c.clear();
                                              }}
                                        )
                                      ],
                                    ),
                                  );
                                }
                            );
                          }, // Adicionar Campo Editar
                        ),
                        secondaryActions: <Widget>[
                          IconSlideAction(
                            caption: 'Places',
                            color: Colors.blue,
                            icon: Icons.place,
                            onTap: () {
                              indexcontrol = index;
                              Navigator.of(context).push(MaterialPageRoute(builder: (context)=>ListPage()));
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
                                            ListWordName.removeAt(index);
                                            ListWord.removeAt(index);
                                            Navigator.of(context).pop();
                                            Navigator.pushReplacement(context, MaterialPageRoute(builder: (BuildContext context) => MyPlaces()));
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
                  },
                ),
              ),
              //),
              //Container(height: 8,color: Colors.white,)
            )
            //Container(height: 8,color: Colors.white,)
          ],
        ),
      ),
    );
  }
}

