import 'package:flutter/material.dart';
import 'package:gcmobile/screens/widgets/pose_image.dart';

import 'info.dart';

class HelpScreen extends StatelessWidget{

  final List poses = [
    {
      'name': 'Idle',
      'description': 'Stop the current command.',
      'image': 'idle',
      'voice': 'stop'
    },
    {
      'name': 'North',
      'description': 'Make Earth go north.',
      'image': 'move_north',
      'voice': 'north'
    },
    {
      'name': 'South',
      'description': "It's just go south.",
      'image': 'move_south',
      'voice': 'south'
    },
    {
      'name': 'West',
      'description': 'Move to west direction.',
      'image': 'move_west',
      'voice': 'west'
    },
    {
      'name': 'East',
      'description': 'Go to east.',
      'image': 'move_east',
      'voice': 'east'
    },
    {
      'name': 'Rotate Left',
      'description': 'Rotate camera to left.',
      'image': 'rotate_left',
      'voice': 'rotate left'
    },
    {
      'name': 'Rotate Right',
      'description': 'Rotate camera to...',
      'image': 'rotate_right',
      'voice': 'rotate right'
    },
    {
      'name': 'Zoom In',
      'description': 'Get closer with camera.',
      'image': 'zoom_in',
      'voice': 'zoom in'
    },
    {
      'name': 'Zoom Out',
      'description': 'Get away with camera.',
      'image': 'zoom_out',
      'voice': 'zoom out'
    },
    {
      'name': 'Fly',
      'description': 'Fly to any place that you want.',
      'image': 'fly',
      'voice': 'fly to ...'
    },
    {
      'name': 'Planet',
      'description': 'Allow you explore other planets.',
      'image': 'planet',
      'voice': 'planet ...'
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Color(0xFF151515),
      body: SafeArea(
        child: SingleChildScrollView(
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
                        textColor: Color(0xFFebebeb),
                        child: Icon(Icons.arrow_back_ios, size: 20,),
                        padding: EdgeInsets.all(14),
                        shape: CircleBorder(),
                      ),
                    ),
                    Center(
                      child: Text(
                        'HELP',
                        style: TextStyle(
                          fontFamily: 'Poppins',
                          color: Color(0xFFebebeb),
                          fontSize: 16
                        )
                      )
                    ),
                    Align(
                      alignment: Alignment.centerRight,
                      child: MaterialButton(
                        onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (context) => InfoScreen())),
                        textColor: Color(0xFFebebeb),
                        child: Icon(Icons.info, size: 20,),
                        padding: EdgeInsets.all(14),
                        shape: CircleBorder(),
                      ),
                    ),
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(top: 40,left: 10, right:10),
                child: ListView.separated(
                  physics: const NeverScrollableScrollPhysics(),
                  shrinkWrap: true,
                  separatorBuilder: (context, index) => SizedBox(height: 25,),
                  itemCount: poses.length,
                  itemBuilder: (context, index) {
                    return _containerLeft(index);
                  },
                ),
              ),
              SizedBox(height: 100,)
            ],
          ),
        )
      )
    );
  }
 _containerLeft(index){
   return Container(
     width: double.infinity,
     child: Stack(
       children: [
         Padding(
           padding: const EdgeInsets.only(left: 25),
           child: Container(
             height: 100,
             child: Card(
               color: Color(0xFF353535),
               child: Align(
                 alignment: Alignment.centerLeft,
                 child: Padding(
                   padding: const EdgeInsets.only(left: 70),
                   child: Column(
                     mainAxisAlignment: MainAxisAlignment.center,
                     crossAxisAlignment: CrossAxisAlignment.start,
                     children: [
                       _poseName(poses[index]['name']),
                       Padding(
                         padding: const EdgeInsets.only(left: 20, top: 5),
                         child: Text(
                           poses[index]['description'],
                           style: TextStyle(
                             fontFamily: 'Poppins',
                             color: Color(0xFF808080),
                             fontSize: 14
                           )
                         ),
                       ),
                       Padding(
                         padding: const EdgeInsets.only(left: 20, top: 10),
                         child: Row(
                           children: [
                             Icon(
                               Icons.volume_up,
                               size: 12,
                               color: Color(0xFF808080)
                             ),
                             SizedBox(width: 5),
                             Text(
                               poses[index]['voice'],
                               style: TextStyle(
                                 fontFamily: 'Poppins',
                                 color: Color(0xFF808080),
                                 fontSize: 14
                               )
                             ),
                           ],
                         ),
                       ),
                     ],
                   ),
                 )
               ),
             ),
           ),
         ),
         Align(
           alignment: Alignment.centerLeft,
           child: PoseImage(poses[index]['image'], Color(0xFFca3e47))
         ),
       ],
     ),
   );
 }

  _poseName(String name){
    return Padding(
      padding: const EdgeInsets.only(left: 20, right: 40),
      child: Text(
        name,
        style: TextStyle(
          fontFamily: 'Poppins',
          color: Color(0xFFebebeb),
          fontWeight: FontWeight.w700,
          fontSize: 16
        )
      ),
    );
  }
}
