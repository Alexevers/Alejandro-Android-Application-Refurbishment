import 'package:flutter/services.dart';
import 'package:gcmobile/utils/outputs.dart';
import 'package:gcmobile/services/sockets.dart';

class Classifier{

  List<double> min = [0.5206545584383067, 0.4188519909135547, 0.41179456394783004, 0.4111497955247904, 0.5429966798302723, 0.32613685612867527, 0.3437829268606085, 0.3113745478084135, 0.5385456308286789, 0.17648054176216543, 0.24466874724940246, 0.18447053548652992];
  List<double> max = [0.6199360869781315, 0.5993362523426554, 0.5035914877469544, 0.6123965500700853, 0.6923154288100453, 0.7353509205204082, 0.47594892769529107, 0.7595042277652772, 0.7892873998273883, 0.8742400852405382, 0.5024632013332077, 0.8888580813045389];

  List<dynamic> normalize(data){
    List<double> normalized = [];
    print(data);
    for(int i = 0; i < data.length; i++){
      normalized.add((data[i]-min[i])/max[i]-min[i]);
    }
    return normalized;
  }

  List<dynamic> flattenInputs(inputs){
    List<double> flatten = [];
    try{
      List<dynamic> rawData = inputs[0]['keypoints'].values.toList();
      rawData = rawData.sublist(5,11);
      for(var value in rawData){
        flatten.add(value['x']);
        flatten.add(value['y']);
      }
      // flatten = normalize(flatten);
    } catch(exception){
      print(exception);
    }
    print(flatten);
    return flatten;
  }

  Future<int> classify(inputs) async{
    var platform = const MethodChannel('gcmobile');
    if(inputs.isNotEmpty)
      return await platform.invokeMethod('identifyPose', {"arg": inputs});
    return null;
  }

  void handleResult(result){
    String color = terminalColor[result];
    String string = strings[result];
    String event = sockets[result]['event'];
    String data = sockets[result]['data'];
    data = data.replaceFirst('%o', 'P');
    print(color + string);
    Socket().send(event, data);
  }

}
