import 'package:flutter/widgets.dart';

class LogosWidget extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return Image(
      width: MediaQuery.of(context).size.width * 0.95,
      image: AssetImage('assets/images/logos.png'),
    );
  }

}
