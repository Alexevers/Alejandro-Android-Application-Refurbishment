import 'package:flutter/widgets.dart';

class CompleteIconWidget extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return Image(
      width: MediaQuery.of(context).size.width * 0.95,
      image: AssetImage('assets/images/icon_complete.png'),
    );
  }

}
