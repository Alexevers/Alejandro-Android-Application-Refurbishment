import 'dart:io';

import 'package:path/path.dart' as path;
import 'package:hive/hive.dart';
import 'package:path_provider/path_provider.dart';
part 'image_data.g.dart';

@HiveType(typeId: 0)
class ImageData extends HiveObject {
  @HiveField(0)
  final String imagePath;

  @HiveField(1)
  String title;

  @HiveField(2)
  String description;

  @HiveField(3)
  final Map<String, String> coordinates;

  @HiveField(4)
  final DateTime date;

  @HiveField(5)
  final String api;

  @HiveField(6)
  final String layer;

  @HiveField(7)
  final String layerDescription;

  @HiveField(8)
  final List<Map<String, String>> colors;

  @HiveField(9)
  final bool demo;

  bool selected = false;

  ImageData({
    required this.imagePath,
    required this.title,
    required this.description,
    required this.coordinates,
    required this.date,
    required this.api,
    required this.layer,
    required this.layerDescription,
    required this.colors,
    required this.demo,
  });

  Future<File> generateKml() async {
    Directory documentDirectory = await getApplicationDocumentsDirectory();
    File file = new File(
        path.join(documentDirectory.path, '${this.getFileName()}.kml'));
    String content = '''
<?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:kml="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom">
<GroundOverlay>
  <name>${this.title}</name>
  <Icon>
    <href>http://lg1:81/${this.getFileName()}.jpeg</href>
    <viewBoundScale>0.75</viewBoundScale>
  </Icon>
  <LatLonBox>
    <north>${this.coordinates['minLat']}</north>
    <south>${this.coordinates['maxLat']}</south>
    <east>${this.coordinates['minLon']}</east>
    <west>${this.coordinates['maxLon']}</west>
    <rotation>0</rotation>
  </LatLonBox>
</GroundOverlay>
</kml>
    ''';
    file.writeAsString(content);

    return file;
  }

  String getFileName() {
    RegExp regexCreated = RegExp(
        "/data/user/0/com.activity.image_satellite_visualizer/app_flutter/(.*).jpeg");
    RegExp regexDemo = RegExp("assets/demos/(.*).jpeg");

    var fileName = this.demo
        ? regexDemo.firstMatch(this.imagePath)?.group(1)
        : regexCreated.firstMatch(this.imagePath)?.group(1);

    return fileName!;
  }

  toString() =>
      "imagePath: ${this.imagePath}\n title: ${this.title}\n description: ${this.description}\n coordinates: ${this.coordinates}";
}
