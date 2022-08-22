import 'dart:convert';
import 'dart:ui';


import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bluetooth_serial/flutter_bluetooth_serial.dart';
import 'package:controllerapp/widgets/list.dart';

import 'ExamplesList.dart';
import 'NetworkInfo.dart';
import 'TourTime.dart';

class BluetoothPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Bluetooth Connection"),
      ),
      body:BluetoothApp() ,
    );
  }
}
class BluetoothApp extends StatefulWidget {
  @override
  _BluetoothAppState createState() => _BluetoothAppState();
}

var controlBottonSend = '';

class _BluetoothAppState extends State<BluetoothApp> {
  // Initializing the Bluetooth connection state to be unknown
  BluetoothState _bluetoothState = BluetoothState.UNKNOWN;
  // Initializing a global key, as it would help us in showing a SnackBar later
  final GlobalKey<ScaffoldState> _scaffoldKey = new GlobalKey<ScaffoldState>();
  // Get the instance of the Bluetooth
  FlutterBluetoothSerial _bluetooth = FlutterBluetoothSerial.instance;
  // Track the Bluetooth connection with the remote device
  BluetoothConnection connection;

  int _deviceState;

  bool isDisconnecting = false;

  Map<String, Color> colors = {
    'onBorderColor': Colors.green,
    'offBorderColor': Colors.red,
    'neutralBorderColor': Colors.transparent,
    'onTextColor': Colors.green[700],
    'offTextColor': Colors.red[700],
    'neutralTextColor': Colors.blue,
  };

  // To track whether the device is still connected to Bluetooth
  bool get isConnected => connection != null && connection.isConnected;

  // Define some variables, which will be required later
  List<BluetoothDevice> _devicesList = [];
  BluetoothDevice _device;
  bool _connected = false;
  bool _isButtonUnavailable = false;

  @override
  void initState() {
    super.initState();

    // Get current state
    FlutterBluetoothSerial.instance.state.then((state) {
      setState(() {
        _bluetoothState = state;
      });
    });

    _deviceState = 0; // neutral

    // If the bluetooth of the device is not enabled,
    // then request permission to turn on bluetooth
    // as the app starts up
    enableBluetooth();

    // Listen for further state changes
    FlutterBluetoothSerial.instance
        .onStateChanged()
        .listen((BluetoothState state) {
      setState(() {
        _bluetoothState = state;
        if (_bluetoothState == BluetoothState.STATE_OFF) {
          _isButtonUnavailable = true;
        }
        getPairedDevices();
      });
    });
  }

  @override
  void dispose() {
    // Avoid memory leak and disconnect
    if (isConnected) {
      isDisconnecting = true;
      connection.dispose();
      connection = null;
    }

    super.dispose();
  }

  // Request Bluetooth permission from the user
  Future<void> enableBluetooth() async {
    // Retrieving the current Bluetooth state
    _bluetoothState = await FlutterBluetoothSerial.instance.state;

    // If the bluetooth is off, then turn it on first
    // and then retrieve the devices that are paired.
    if (_bluetoothState == BluetoothState.STATE_OFF) {
      await FlutterBluetoothSerial.instance.requestEnable();
      await getPairedDevices();
      return true;
    } else {
      await getPairedDevices();
    }
    return false;
  }

  // For retrieving and storing the paired devices
  // in a list.
  Future<void> getPairedDevices() async {
    List<BluetoothDevice> devices = [];

    // To get the list of paired devices
    try {
      devices = await _bluetooth.getBondedDevices();
    } on PlatformException {
      print("Error");
    }

    // It is an error to call [setState] unless [mounted] is true.
    if (!mounted) {
      return;
    }

    // Store the [devices] list in the [_devicesList] for accessing
    // the list outside this class
    setState(() {
      _devicesList = devices;
    });
  }

  // Now, its time to build the UI
  @override
  Widget build(BuildContext context) {
    var size = MediaQuery.of(context).size;
    return Scaffold(
      body: Container(
        width: size.width,
        height: size.height,
        child: Column(
          children: <Widget>[
            FlatButton.icon(
              icon: Icon(
                Icons.refresh,
                color: Colors.blue,
              ),
              label: Text(
                "Refresh",
                style: TextStyle(
                  color: Colors.blue,fontSize: 20,fontWeight: FontWeight.w700,
                ),
              ),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(30),
              ),
              splashColor: Colors.blue,
              onPressed: () async {
                await getPairedDevices().then((_) {
                  show('Device list refreshed');
                });
              },
            ),
            Visibility(
              visible: _isButtonUnavailable &&
                  _bluetoothState == BluetoothState.STATE_ON,
              child: LinearProgressIndicator(
                backgroundColor: Colors.yellow,
                valueColor: AlwaysStoppedAnimation<Color>(Colors.red),
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(10),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: <Widget>[
                  Expanded(
                    child: Text(
                      'Enable Bluetooth',
                      style: TextStyle(
                        color: Colors.black,
                        fontSize: 16,
                      ),
                    ),
                  ),
                  Switch(
                    value: _bluetoothState.isEnabled,
                    onChanged: (bool value) {
                      future() async {
                        if (value) {
                          await FlutterBluetoothSerial.instance
                              .requestEnable();
                        } else {
                          await FlutterBluetoothSerial.instance
                              .requestDisable();
                        }

                        await getPairedDevices();
                        _isButtonUnavailable = false;

                        if (_connected) {
                          _disconnect();
                        }
                      }

                      future().then((_) {
                        setState(() {});
                      });
                    },
                  )
                ],
              ),
            ),
            Stack(
              children: <Widget>[
                Column(
                  children: <Widget>[
                    Padding(
                      padding: const EdgeInsets.only(top: 10),
                      child: Text(
                        "PAIRED DEVICES",
                        style: TextStyle(fontSize: 24, color: Colors.blue),
                        textAlign: TextAlign.center,
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: <Widget>[
                          Text(
                            'Device:',
                            style: TextStyle(
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                          DropdownButton(
                            items: _getDeviceItems(),
                            onChanged: (value) =>
                                setState(() => _device = value),
                            value: _devicesList.isNotEmpty ? _device : null,
                          ),
                          RaisedButton(
                            onPressed: _isButtonUnavailable
                                ? null
                                : _connected ? _disconnect : _connect,
                            child:
                            Text(_connected ? 'Disconnect' : 'Connect'),
                          ),
                        ],
                      ),
                    ),
                    Container(height: 20,),
                    Container(
                      width: size.width*.9,
                      child: _dataToSend(),
                    )
                  ],
                ),
                Container(color: Colors.blue),
              ],
            ),
            Expanded(
              child: Padding(
                padding: const EdgeInsets.all(20),
                child: Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      Text(
                        "NOTE: If you cannot find the device in the list, please pair the device by going to the bluetooth settings",
                        style: TextStyle(
                          fontSize: 15,
                          fontWeight: FontWeight.bold,
                          color: Colors.red,
                        ),
                      ),
                      SizedBox(height: 15),
                      RaisedButton(
                        elevation: 2,
                        child: Text("Bluetooth Settings"),
                        onPressed: () {
                          FlutterBluetoothSerial.instance.openSettings();
                        },
                      ),
                    ],
                  ),
                ),
              ),
            )
          ],
        ),
      ),
    );
  }

  // Create the List of devices to be shown in Dropdown Menu
  List<DropdownMenuItem<BluetoothDevice>> _getDeviceItems() {
    List<DropdownMenuItem<BluetoothDevice>> items = [];
    if (_devicesList.isEmpty) {
      items.add(DropdownMenuItem(
        child: Text('NONE'),
      ));
    } else {
      _devicesList.forEach((device) {
        items.add(DropdownMenuItem(
          child: Text(device.name),
          value: device,
        ));
      });
    }
    return items;
  }

  // Method to connect to bluetooth
  void _connect() async {
    setState(() {
      _isButtonUnavailable = true;
    });
    if (_device == null) {
      show('No device selected');
    } else {
      if (!isConnected) {
        await BluetoothConnection.toAddress(_device.address)
            .then((_connection) {
          print('Connected to the device');
          connection = _connection;
          setState(() {
            _connected = true;
          });

          connection.input.listen(null).onDone(() {
            if (isDisconnecting) {
              print('Disconnecting locally!');
            } else {
              print('Disconnected remotely!');
            }
            if (this.mounted) {
              setState(() {});
            }
          });
        }).catchError((error) {
          print('Cannot connect, exception occurred');
          print(error);
        });
        show('Device connected');

        setState(() => _isButtonUnavailable = false);
      }
    }
  }

  // void _onDataReceived(Uint8List data) {
  //   // Allocate buffer for parsed data
  //   int backspacesCounter = 0;
  //   data.forEach((byte) {
  //     if (byte == 8 || byte == 127) {
  //       backspacesCounter++;
  //     }
  //   });
  //   Uint8List buffer = Uint8List(data.length - backspacesCounter);
  //   int bufferIndex = buffer.length;

  //   // Apply backspace control character
  //   backspacesCounter = 0;
  //   for (int i = data.length - 1; i >= 0; i--) {
  //     if (data[i] == 8 || data[i] == 127) {
  //       backspacesCounter++;
  //     } else {
  //       if (backspacesCounter > 0) {
  //         backspacesCounter--;
  //       } else {
  //         buffer[--bufferIndex] = data[i];
  //       }
  //     }
  //   }
  // }

  // Method to disconnect bluetooth
  void _disconnect() async {
    setState(() {
      _isButtonUnavailable = true;
      _deviceState = 0;
    });

    await connection.close();
    show('Device disconnected');
    if (!connection.isConnected) {
      setState(() {
        _connected = false;
        _isButtonUnavailable = false;
      });
    }
  }

  // Method to send message,
  // for turning the Bluetooth device on
  void _sendNetInfo() async {
    connection.output.add(utf8.encode("/"+netIP+","+netSSID+","+netPASSWORD+","));
    await connection.output.allSent;
    setState(() {
      controlBottonSend = '';
      _deviceState = 1; // device on
    });
  }

  // Method to send message,
  // for turning the Bluetooth device off
  void _sendPlaceString() async {
    connection.output.add(utf8.encode("#"+StringToSend));
    await connection.output.allSent;
    setState(() {
      controlBottonSend = '';
      _deviceState = -1; // device off
    });
  }
  void _sendTime() async {
    connection.output.add(utf8.encode("&"+time+","));
    await connection.output.allSent;
    setState(() {
      controlBottonSend = '';
      _deviceState = -1; // device off
    });
  }
  void _sendNewList() async {
    connection.output.add(utf8.encode("#"+NewStringToSend));
    await connection.output.allSent;
    setState(() {
      controlBottonSend = '';
      _deviceState = -1; // device off
    });
  }

  // Method to show a Snackbar,
  // taking message as the text
  Future show(
      String message, {
        Duration duration: const Duration(seconds: 3),
      }) async {
    await new Future.delayed(new Duration(milliseconds: 100));
    _scaffoldKey.currentState.showSnackBar(
      new SnackBar(
        content: new Text(
          message,
        ),
        duration: duration,
      ),
    );
  }

  _dataToSend() {
    if(controlBottonSend == ''){
      return Container( height:50,);
    }else
    if(controlBottonSend == 'NetInfo'){
      return Card(
        shape: RoundedRectangleBorder(
            side: BorderSide(color: Colors.red,width: 3),
            borderRadius: BorderRadius.circular(4)),
        elevation: _deviceState == 0 ? 4 : 0,
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Row(
            children: <Widget>[
              Expanded(
                child: Text(
                  "Send Net Information",
                  style: TextStyle(
                    fontSize: 20,
                    color: Colors.red,//colors['neutralTextColor'],
                  ),
                ),
              ),
              FlatButton(
                onPressed: _connected
                    ? _sendNetInfo
                    : null,
                child: Text("Send"),
              ),
            ],
          ),
        ),
      );
    }else
    if(controlBottonSend == 'SendList'){
      return Card(
        shape: RoundedRectangleBorder(
            side: BorderSide(color: Colors.blue,width: 3),
            borderRadius: BorderRadius.circular(4)),
        elevation: _deviceState == 0 ? 4 : 0,
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Row(
            children: <Widget>[
              Expanded(
                child: Text(
                  "Send List",
                  style: TextStyle(
                    fontSize: 20,
                    color: colors['neutralTextColor'],
                  ),
                ),
              ),
              FlatButton(
                onPressed: _connected
                    ? _sendPlaceString
                    : null,
                child: Text("Send"),
              ),
            ],
          ),
        ),
      );
    }
    if(controlBottonSend == 'SendNewList'){
      return Card(
        shape: RoundedRectangleBorder(
            side: BorderSide(color: Colors.deepOrange,width: 3),
            borderRadius: BorderRadius.circular(4)),
        elevation: _deviceState == 0 ? 4 : 0,
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Row(
            children: <Widget>[
              Expanded(
                child: Text(
                  "Send my list",
                  style: TextStyle(
                    fontSize: 20,
                    color: Colors.deepOrange,//colors['neutralTextColor'],
                  ),
                ),
              ),
              FlatButton(
                onPressed: _connected
                    ? _sendNewList
                    : null,
                child: Text("Send"),
              ),
            ],
          ),
        ),
      );
    }
    if(controlBottonSend == 'Time'){
      return Card(
        shape: RoundedRectangleBorder(
            side: BorderSide(color: Colors.green,width: 3),
            borderRadius: BorderRadius.circular(4)),
        elevation: _deviceState == 0 ? 4 : 0,
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Row(
            children: <Widget>[
              Expanded(
                child: Text(
                  "Send Time",
                  style: TextStyle(
                    fontSize: 20,
                    color:Colors.green,// colors['neutralTextColor'],
                  ),
                ),
              ),
              FlatButton(
                onPressed: _connected
                    ? _sendTime
                    : null,
                child: Text("Send"),
              ),
            ],
          ),
        ),
      );
    }
  }
}