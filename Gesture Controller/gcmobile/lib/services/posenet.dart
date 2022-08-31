import 'package:tflite/tflite.dart';
import 'package:camera/camera.dart';

class Posenet{
  void loadPosenetModel() async{
    var res = await Tflite.loadModel(
      model: "assets/posenet_mv1_075_float_from_checkpoints.tflite");
    print(res);
  }

  Future<void> runPosenet(isDetecting, CameraImage img, callback) async{
    if (!isDetecting['value']) {
      isDetecting['value'] = true;
      int startTime = new DateTime.now().millisecondsSinceEpoch;
      await Tflite.runPoseNetOnFrame(
        bytesList: img.planes.map((plane) {
          return plane.bytes;
        }).toList(),
        imageHeight: img.height,
        imageWidth: img.width,
        numResults: 5,
        rotation: -0

      ).then((recognitions) {
        int endTime = new DateTime.now().millisecondsSinceEpoch;
        print("Detection took ${endTime - startTime}");

        callback(recognitions, img.height, img.width);

        isDetecting['value'] = false;
      });
    }
  }
}
