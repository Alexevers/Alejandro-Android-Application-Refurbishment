import 'package:flutter/material.dart';

class WordWondersPage extends StatefulWidget {
  final String continent;
  const WordWondersPage(this.continent) ;
  @override
  _WordWondersPageState createState() => _WordWondersPageState();
}

final List<String> _placeName= <String>['Golden Gate','Taj Mahal','Christ the Redeemer','Chichén Itzá','Coliseum','Machu Picchu','Ruins of Petra','Eiffel Tower','Big Bem','Statue of Liberty','St. Basils Cathedral','Empire State Building','Fortress of São José de Macapá','Giza Necropolis','La Seu Vella Castel','Great wall of China'];
final List<String> _coordinates= <String>['-122.485046,37.820047,3000','78.042202,27.172969,1500','-43.210317,-22.951838,400','-88.567935,20.683510,600','12.492135,41.890079,600','-72.545224,-13.163820,600','35.441919,30.328456,600','2.294473,48.857730,1000','-0.124419,51.500769,500','-74.044535,40.689437,500','37.623446,55.752362,500','-73.985359,40.748360,500','-51.049260,0.030478,500','31.132695,29.976603,500','0.626502,41.617540,600','116.562771,40.435456,500'];
List<List> ListWord = [_coordinates];
var _controllCont = "";

class _WordWondersPageState extends State<WordWondersPage> {
  @override
  Widget build(BuildContext context) {
    var size = MediaQuery.of(context).size;
    _controllCont = widget.continent;

    return Container(
        color: Colors.red,
        child: Container(
          child: ListView.builder(
              padding: const EdgeInsets.all(5),
              itemCount:_coordinates.length,
              itemBuilder: (BuildContext context, int index){
                var te = index +1;
                return Card(
                  child: ListTile(
                    leading: CircleAvatar(
                      backgroundColor: Colors.red,
                      child: Text("$te"),
                      foregroundColor: Colors.white,
                    ),
                    title: Text(_placeName[index]),
                    subtitle: Text(_coordinates[index]),
                  ),
                );
              }
          ),
        ),
      );

  }
}
