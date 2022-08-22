/*-------------------------------------------------------------
 _ _             _     _               _                  
| (_) __ _ _   _(_) __| |   __ _  __ _| | __ ___  ___   _ 
| | |/ _` | | | | |/ _` |  / _` |/ _` | |/ _` \ \/ / | | |
| | | (_| | |_| | | (_| | | (_| | (_| | | (_| |>  <| |_| |
|_|_|\__, |\__,_|_|\__,_|  \__, |\__,_|_|\__,_/_/\_\\__, |
        |_|                |___/                    |___/ 
https://github.com/LiquidGalaxy/liquid-galaxy
https://github.com/LiquidGalaxyLAB/liquid-galaxy

https://github.com/LiquidGalaxyLAB/Arduino-Controller

Otávio Jesus França Oliveira - GSoC 2020
-------------------------------------------------------------
This code is directed to the use of the Controller Glove, the Accelerometer sensor must be mounted on the central axis of the hand,
to allow a better reading of the user's movements.
This code allows control of Liquid Galaxy only using Serial communication and NEEDS the USB cable to be connected
to the pc Liquid Master Galaxy
-------------------------------------------------------------*/
//-------------------------------------------------- Bluetooth comunication
#include "BluetoothSerial.h"
#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif
BluetoothSerial SerialBT;
char caracter ;
String wordReceived;
int countLine = 0;
int countColumn =0;
//---------------------------------------------------------------- Acelerometer sensor data
#include <Wire.h>
#include <ADXL345.h>
ADXL345 adxl; //variable adxl is an instance of the ADXL345 library

//---------------------------------------------------------------------------------------------------Default commands and starting places
String Commands[]={"zero","linear","zOut","zIn","right","left","up","down","rightUp","rightDown","leftUp","leftDown",
                   "CamUp","CamDown","CamRight","CamLeft","rollRight","rollLeft","tiltUp","tiltDown"};

String Coordnates[16][3]={
  {"-122.485046","37.820047","3000"},{"78.042202","27.172969","1500"},{"-43.210317","-22.951838","400"},{"-88.567935","20.683510","600"},{"12.492135","41.890079","600"},
  {"-72.545224","-13.163820","600"},{"35.441919","30.328456","600"},{"2.294473","48.857730","1000"},{"-0.124419","51.500769","500"},{"-74.044535","40.689437","500"},
  {"37.623446","55.752362","500"},{"-73.985359","40.748360","500"},{"-51.049260","0.030478","500"},{"31.132695","29.976603","500"},{"0.626502","41.617540","600"},{"116.562771","40.435456","500"}
  };

String CoordOrbit[3]; 
//-------------------------------------------Control variables
int x,y,z;
int control = 1;
int controlNav = 0;
int placeAdd = 16;
int placeSub = 4;
int changeControl = 15;
int tourPin = 27;
int OrbitPin = 2;
int TimeTour = 10;
int r = 14;
int g =12;
int b =13;
int xLimitUp = 240;
int xLimitD = -150; 
int yLimitUp = 245; 
int yLimitD = -245;
int tag,tag2,tag3,tagx,tagx2,tagx3,count =0;

void setup()
{
  Serial.begin(9600);
  SerialBT.begin("Glove Controller"); //Bluetooth device name
       
  adxl.powerOn();
  pinMode(changeControl, INPUT);
  pinMode(tourPin, INPUT);
  pinMode(OrbitPin, INPUT);
  pinMode(r, OUTPUT);digitalWrite(r,LOW);
  pinMode(g, OUTPUT);digitalWrite(g,LOW);
  pinMode(b, OUTPUT);digitalWrite(b,LOW);

}

void loop()
{
     //--------------------------------------------------Read the Bluetooth buffer
    if (SerialBT.available()) 
      {  
        caracter = SerialBT.read();
        if(caracter == '#') ReadPlace();
        if(caracter == '&') ReadTimeTour();
      }
    //-----------------------------------------------------------------------
      if(digitalRead(OrbitPin)){while(digitalRead(OrbitPin)){} MakeOrbit();} // Start the Orbit
      if(digitalRead(tourPin)){while(digitalRead(tourPin)){} Tour(TimeTour);} // Start and stop the tour
   //----------------------------------------------------------------------------Function to make navigation by 16 places
     if(digitalRead(placeAdd))
     {
      while(digitalRead(placeAdd)){} 
      controlNav ++;
      if(controlNav == 16)controlNav =0;
      Serial.println(MakeKML(Coordnates[controlNav][0],Coordnates[controlNav][1],Coordnates[controlNav][2]));
     }
      if(digitalRead(placeSub))
     {
      while(digitalRead(placeSub)){} 
      controlNav --;
      if(controlNav == -1)controlNav =15;
      Serial.println(MakeKML(Coordnates[controlNav][0],Coordnates[controlNav][1],Coordnates[controlNav][2]));
     }
   //----------------------------------------------------------------------------Function to Read gloves moviment  
    adxl.readXYZ(&x, &y, &z); //read the accelerometer values and store them in variables  x,y,z
    if (digitalRead(changeControl)) 
    { //Serial.println("erro");
      control ++;
      while (digitalRead(changeControl)){}
      if(control == 4)control =1;
      RGB(control);
    }
    switch (control)
     {
      case 1:
      if(x < -150){LGMove(4);tagx =1;}
      else if(x > 240) {LGMove(5);tagx =1;}
          else if(tagx == 1) {LGMove(0); tagx = 0;}
      if(y < -245){LGMove(6);tag =1;}
      else if(y > 245){LGMove(7);tag =1;}
          else if(tag == 1) {LGMove(0); tag = 0;}
      break;
      case 2:
      if(x < -150){LGMove(13);tagx2 = 1;}
      else if(x > 240) {LGMove(12);tagx2 = 1;}
          else if(tagx2 == 1) {LGMove(0); tagx2 = 0;}
      if(y < -245){LGMove(2);tag2 = 1;}
      else if(y > 245){LGMove(3);tag2 = 1;}
          else if(tag2 == 1) {LGMove(0); tag2 = 0;}
      break;
      case 3:
      if(x < -150){LGMove(17);tagx3 = 1;}
      else if(x > 240) {LGMove(16);tagx3 = 1;}
      else if(tagx3 == 1) {LGMove(0); tagx3 = 0;}
      if(y < -245){LGMove(19);tag3 = 1;}
      else if(y > 245){LGMove(18);tag3 = 1;}
          else if(tag3 == 1) {LGMove(0); tag3 = 0;}
      break;
      default:
      break;
     }
} // End loop
//--------------------------------------------------------------------Function to receive and set the Time for tourPin by bluetooth connection
void ReadTimeTour()
{
  while(SerialBT.available())
  {
    caracter = SerialBT.read(); 
    if(caracter != ',')
      {
        wordReceived.concat(caracter);
      } else
        {
          TimeTour = atoi(wordReceived.c_str());
          wordReceived = "";
        }
  }
}
//--------------------------------------------------------------------Function to receive the coordinates list by bluetooth connection
void ReadPlace()
{  
  countLine = 0; countColumn =0;
  while(SerialBT.available())
  {
   caracter = SerialBT.read(); 
   if(caracter != '*') // ao inves de { usar *
    { 
      if(caracter != ',')
      {
        wordReceived.concat(caracter);
      } else
        {
          Coordnates[countLine][countColumn] = wordReceived;
          wordReceived = "";
          countColumn ++;
        }
    }
    else
      { countLine++; countColumn =0;
      } 
  }
}
//------------------------------------------ This function receive the info by the joystick checking and send the correct command to Liquid Galay
void LGMove(int Command)
{
 Serial.println(Commands[Command]);
}
//--------------------------------------------This function performs the lighting control of the RGB led
void RGB(int cont)
{
  switch (cont)
  {
    case 1:
    digitalWrite(r,!digitalRead(r)); delay(500);
    digitalWrite(r,!digitalRead(r));
    break;
    case 2:
    digitalWrite(g,!digitalRead(g)); delay(500);
    digitalWrite(g,!digitalRead(g));
    break;
    case 3:
    digitalWrite(b,!digitalRead(b)); delay(500);
    digitalWrite(b,!digitalRead(b));
    break;
  }
}
//-----------------------------------------------------------------------------------This function is responsable for building the simple km
String MakeKML(String longitude, String latitude, String range)
{
  CoordOrbit[0] = longitude;  
  CoordOrbit[1] = latitude; 
  CoordOrbit[2]= range;
  String kml ="flytoview=<LookAt><longitude>";
  kml += longitude;
  kml += "</longitude><latitude>";
  kml +=latitude ;
  kml += "</latitude><range>";
  kml += range;
  kml += "</range></LookAt>";
  return kml;
}
//---------------------------------------------------------
void MakeOrbit()
{
  bool Step = true;
  digitalWrite(r,HIGH);
  String kmlOrbit = "";
   for(int g =0; g<361; g ++)
    {
      kmlOrbit = "";
      kmlOrbit = "flytoview=<LookAt><longitude>";
      kmlOrbit += CoordOrbit[0];
      kmlOrbit += "</longitude><latitude>";
      kmlOrbit += CoordOrbit[1];
      kmlOrbit += "</latitude><heading>";
      kmlOrbit += String(g);
      kmlOrbit += "</heading><range>";
      kmlOrbit += CoordOrbit[2];
      kmlOrbit += "</range><tilt>40</tilt></LookAt>";
      Serial.println(kmlOrbit);
      if(Step){Step = false; delay(8000);}
      if(digitalRead(OrbitPin)){while(digitalRead(OrbitPin)){} break;}
   }
    digitalWrite(r,LOW);
}
//----------------------------------------------------------tourPin is responsable for send the kml's with the selected time by the user, creating the tourPin for 16 places
void Tour(int Time)
{
  int Tb,Ta;
  for(int tour = 0 ;tour<16;tour++)
  {
    Tb = Ta = millis();
    Serial.println(MakeKML(Coordnates[tour][0],Coordnates[tour][1],Coordnates[tour][2]));
   
    while((Tb - Ta) <(Time*1000))
    { 
      if(digitalRead(tourPin)) tour =16;
      Tb = millis();
    }
  }
}
