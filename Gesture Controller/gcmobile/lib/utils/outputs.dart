import 'package:flutter/material.dart';

Map<int, String> strings = {
  0: 'IDLE',
  1: 'MOVE NORTH',
  2: 'MOVE SOUTH',
  3: 'MOVE WEST',
  4: 'MOVE EAST',
  5: 'ROTATE LEFT',
  6: 'ROTATE RIGHT',
  7: 'ZOOM IN',
  8: 'ZOOM OUT',
  9: 'CHANGE PLANET',
  10: 'FLY TO',
};

Map<int, String> terminalColor = {
  0: '\x1B[90m',
  1: '\x1B[34m',
  2: '\x1B[94m',
  3: '\x1B[36m',
  4: '\x1B[96m',
  5: '\x1B[32m',
  6: '\x1B[92m',
  7: '\x1B[35m',
  8: '\x1B[95m',
  9: '\x1B[31m',
  10: '\x1B[91m'
};

Map<int, Color> appColor = {
  0: Colors.black45,
  1: Colors.blue,
  2: Colors.blueAccent,
  3: Colors.cyan,
  4: Colors.cyanAccent,
  5: Colors.green,
  6: Colors.greenAccent,
  7: Colors.purple,
  8: Colors.purpleAccent,
  9: Colors.red,
  10: Colors.redAccent
};

Map<int, dynamic> sockets = {
  0: {'event':'idle','data':"{'direction':'','value':'%o'}"},
  1: {'event':'move','data':"{'direction':'N','value':'%o'}"},
  2: {'event':'move','data':"{'direction':'S','value':'%o'}"},
  3: {'event':'move','data':"{'direction':'W','value':'%o'}"},
  4: {'event':'move','data':"{'direction':'E','value':'%o'}"},
  5: {'event':'rotate','data':"{'direction':'L','value':'%o'}"},
  6: {'event':'rotate','data':"{'direction':'R','value':'%o'}"},
  7: {'event':'zoom','data':"{'direction':'I','value':'%o'}"},
  8: {'event':'zoom','data':"{'direction':'O','value':'%o'}"},
  9: {'event':'planet','data':"{'direction': '%location', 'value': '%o'}"},
  10: {'event':'flyto','data':"{'direction': '%location'}, 'value': '%o'}"}
};
