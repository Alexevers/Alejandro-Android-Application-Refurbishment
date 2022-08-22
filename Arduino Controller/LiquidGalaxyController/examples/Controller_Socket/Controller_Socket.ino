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
This program should only be used when connected with the PC Master Liquid Galaxy, using WiFi communication 
This code allows the change of the connection data with the WiFi network through the application developed for this project. 
With this code the controller will start in standby mode until the connection data to the WiFi network is entered
-------------------------------------------------------------*/
#include <WiFi.h> 
#include <LiquidGalaxyController.h>
#include <SoftwareSerial.h>
//-------------------------------------------------- Bluetooth comunication
#include "BluetoothSerial.h"
#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif
BluetoothSerial SerialBT;
char caracter ;
String NetworkInfo[3];
String wordReceived;
int countLine = 0;
int countColumn =0;
//---------------------------------------------------------------- Network setup
WiFiClient client;
const char * host;
const char* ssid;
const char* password;
const uint16_t port = 8000; //add
//----------------------------------------------------------------- Voice Commands
SoftwareSerial VR3(22,23);
byte a[27];
int rec =18;
int _pinOut = 18;
int received;
byte headVoice[4]={0xAA,0x02,0x31,0x0A};
byte groups[4] [11]={
      {0xAA,0x09,0x30,0x01,0x02,0x03,0x04,0x12,0x13,0x14,0x0A}, // First Group
      {0xAA,0x09,0x30,0x05,0x06,0x07,0x08,0x11,0x13,0x14,0x0A}, // Second Group 
      {0xAA,0x09,0x30,0x09,0x0A,0x0B,0x0C,0x11,0x12,0x14,0x0A},  
      {0xAA,0x09,0x30,0x0D,0x0E,0x0F,0x10,0x11,0x12,0x13,0x0A}};
//---------------------------------------------------------------------------------------------LiquidGalaxyController parameters
const byte NumberRows = 4;
const byte NumberColuns = 4;
byte line[NumberRows] ={13,12,14,27}; 
byte column[NumberColuns]={26,25,33,32};
int Keys[NumberRows*NumberColuns] = 
  {1, 2, 3, 4,
   5, 6, 7, 8,
   9,10,11,12,
  13,14,15,16};
LG_Keypad LGKey(Keys,line,column,NumberRows,NumberColuns);
int JoysticRanges[] = {3200,10,3200,100};        // You can check these values just by reading the analog inputs corresponding to the joystic B,F,R,P and define the value that you consider best
LG_JoysticSetup joystic(A6,A7,4,JoysticRanges);  
LG_UltrasonicSetup Ultrasonic(21,19);
//--------------------------------------------------------------------------------------------------------------------Default commands and starting places
String Commands[]={"zero","linear","zOut","zIn","right","left","up","down","rightUp","rightDown","leftUp","leftDown",
                   "CamUp","CamDown","CamRight","CamLeft","rollRight","rollLeft","tiltUp","tiltDown"};

String Coordnates[16][3]={
  {"-122.485046","37.820047","3000"},{"78.042202","27.172969","1500"},{"-43.210317","-22.951838","400"},{"-88.567935","20.683510","600"},{"12.492135","41.890079","600"},
  {"-72.545224","-13.163820","600"},{"35.441919","30.328456","600"},{"2.294473","48.857730","1000"},{"-0.124419","51.500769","500"},{"-74.044535","40.689437","500"},
  {"37.623446","55.752362","500"},{"-73.985359","40.748360","500"},{"-51.049260","0.030478","500"},{"31.132695","29.976603","500"},{"0.626502","41.617540","600"},{"116.562771","40.435456","500"}
  };

String CoordOrbit[3];
//--------------------------------------------------------------------------------------------------------------------Control variables
int moviment, moviment2,moviment3 =0;
int movJoy =1;
int tourPin = 2;
int OrbitPin = 15;
int changeVoiceGroup = 5;
int TimeTour = 10;
int cicleTimeInit, cicleTimeEnd;
bool startControl = true;
//--------------------------------------------------------------------------------------------------------------------Default commands and starting places
void setup() 
{
  Serial.begin(9600);
  SerialBT.begin("LG Controller"); //Bluetooth device name
  VR3.begin(9600);
  pinMode(OrbitPin, INPUT);
  pinMode(tourPin, INPUT);
  pinMode(changeVoiceGroup, INPUT);
  pinMode(_pinOut,OUTPUT); 
  digitalWrite(_pinOut,LOW);
  delay(1000);
  for(int h =0; h<11;h++)VR3.write(groups[0] [h]);
  delay(100);
 for(int i = 0; i<11;i++)
 {
  a[i] = VR3.read();
  Serial.print( a[i],HEX);
  Serial.print("-");
  delay(1); 
 }
 Serial.println("");
 delay(100);
      VR3.write(0xaa);
      VR3.write(0x0D);
      VR3.write(0x0a);
}

void loop() 
{   
  if(startControl){StartControl();}
  //--------------------------------------------------Read the Bluetooth buffer
    if (SerialBT.available()) 
      {  
        caracter = SerialBT.read();
        if(caracter == '/') ReadNetInfo();
        if(caracter == '#') ReadPlace();
        if(caracter == '&') ReadTimeTour();
      }
   //----------------------------------------------------------------------
    if(digitalRead(OrbitPin)){while(digitalRead(OrbitPin)){} MakeOrbit();} // Start the Orbit
    if(digitalRead(tourPin)){while(digitalRead(tourPin)){} Tour(TimeTour);} // Start and stop the tour
   //------------------------------------------------------------------------------------------------------------------------------------------
    if(digitalRead(changeVoiceGroup))
    { while(digitalRead(changeVoiceGroup) == HIGH){}  // Change the voice group after press the button
      GroupsControll(rec);
      rec ++;
      if(rec == 21)rec =17;
    }
  //-------------------------------------------- Function to receive the voice recognition command and send teh kml corresponfing
    received = GetRec();
    if (received <17 && received>0)
    {
      Serial.println(MakeKML(Coordnates[received-1][0],Coordnates[received-1][1],Coordnates[received-1][2])); 
        if (client.connect(host, port)){ client.print(MakeKML(Coordnates[received-1][0],Coordnates[received-1][1],Coordnates[received-1][2]));}
        client.stop();
    }
  //--------------------------------------------------------------------------------------------------------------------------------------------
   int key = LGKey.KeyPress(); // KEYBOARD FUNCTION, check if a key has ben pressed, and make the flyTo the place corresponding
   if(key)
   {
    Serial.println(MakeKML(Coordnates[key-1][0],Coordnates[key-1][1],Coordnates[key-1][2]));
     if (client.connect(host, port)){ client.print(MakeKML(Coordnates[key-1][0],Coordnates[key-1][1],Coordnates[key-1][2]));}
     client.stop();
    }
  //--------------------------------------------------------------------------------------------------------------------------------------------
   char joy = joystic.JoysticRead();// JOYSTICK FUNCTION, check teh joystick moviments and send the corresponding navigation command
   if(joy)
   {
    if(joy == 'P') 
    {
      movJoy++; digitalWrite(_pinOut,!digitalRead(_pinOut)); delay(200);
      digitalWrite(_pinOut,!digitalRead(_pinOut));
    }
    if(movJoy == 4)movJoy = 1;
    moviment =1;
    JoysticAnalyser(movJoy,joy);
   } else  if(moviment==1)
            { 
              LGMove(0);
              LGMove(0);
              moviment=0;
            }
 //--------------------------------------------------------------------------------------------------------------------------------------------
   int distance = Ultrasonic.UltrasonicMensure();// ULTRASONIC FUNCTION, check the distance and apply zoom In or zoom out
   if(distance < 10) 
   { 
    moviment2 =1;
    LGMove(3);
   } else {
    if(moviment2==1)
    { 
      LGMove(0);
      LGMove(0);
      moviment2=0;
    }
    if(distance < 25 && distance > 15) 
   { 
    moviment3 =1;
    LGMove(2);
   } else  if(moviment3==1)
            { 
              LGMove(0);
              LGMove(0);
              moviment3=0;
            } 
   } 
  
} // End loop
//--------------------------------------------------------------------Function to receive and set the Time for tour by bluetooth connection
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
  digitalWrite(_pinOut,HIGH);
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
  digitalWrite(_pinOut,LOW);
}
//------------------Function to receive the network informations by bluetooth connection and makes the connection with wifi
void ReadNetInfo()
{
  digitalWrite(_pinOut,HIGH);
  cicleTimeInit = cicleTimeEnd = millis();
  countColumn =0;
  while(SerialBT.available())
  {
    caracter = SerialBT.read(); 
    if(caracter != ',')
      {
        wordReceived.concat(caracter);
      } else
        {
          NetworkInfo[countColumn] = wordReceived;
          wordReceived = "";
          countColumn ++;
        }
  }
   delay(1000);
   host = NetworkInfo[0].c_str();
   ssid =  NetworkInfo[1].c_str();
   password =  NetworkInfo[2].c_str();
   WiFi.begin(ssid, password);
   while (WiFi.status() != WL_CONNECTED) 
   {
      delay(500);
      Serial.println("...");
      cicleTimeEnd = millis();
    if((cicleTimeEnd - cicleTimeInit)> (6000))ESP.restart();
   }
    Serial.print("WiFi connected with IP: ");
    Serial.println(WiFi.localIP());
    Serial.println(WiFi.SSID());
    digitalWrite(_pinOut,LOW);
    startControl = false;
}
void StartControl()
{
  delay(500);
  digitalWrite(_pinOut,!digitalRead(_pinOut)); 
  delay(500);
  digitalWrite(_pinOut,!digitalRead(_pinOut));
}
//----------------------------------------------------------- This function receive the info by the joystick checking and send the correct command to Liquid Galay
void LGMove(int Command)
{
  if (client.connect(host, port)) 
  {
    client.print(Commands[Command]);
  }
  client.stop();
  
 Serial.println(Commands[Command]);
}
//-----------------------------------------------------------------------------------This function is responsable for building the simple kml
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
//------------------------------------------------------------------
void MakeOrbit()
{
  bool Step = true;
  digitalWrite(_pinOut,HIGH);
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
      
      if (client.connect(host, port)) 
          {
            client.print(kmlOrbit);
            Serial.println(kmlOrbit);
          }client.stop();
      if(Step){Step = false; delay(8000);}
      if(digitalRead(OrbitPin)){while(digitalRead(OrbitPin)){} break;}
   }
    digitalWrite(_pinOut,LOW);
}
//-------------------------------------------------------This function is responsable for selecting the state of the joystick controller and select the correct command 
void JoysticAnalyser(int State, char Position)
{
  if(State == 1) // Simple Moviments
  {switch (Position)
    {
      case 'F':
      LGMove(7);
      break;
      case 'B':
      LGMove(6);
      break;
      case 'R':
      LGMove(4);
      break;
      case 'L':
      LGMove(5);
      break;
    }// end switch
  }// end Simple Moviments
  if(State == 2) // Camera Moviments
  {switch (Position)
    {
      case 'F':
      LGMove(13);
      break;
      case 'B':
      LGMove(12);
      break;
      case 'R':
      LGMove(14);
      break;
      case 'L':
      LGMove(15);
      break;
    }// end switch
  }// end Cam Moviments
  if(State == 3) // Tlt and Roll
  {switch (Position)
    {
      case 'F':
      LGMove(19);
      break;
      case 'B':
      LGMove(18);
      break;
      case 'R':
      LGMove(16);
      break;
      case 'L':
      LGMove(17);
      break;
    }// end switch
  }// end Tlt and Roll
}
//----------------------------------------------------------Tour is responsable for send the kml's with the selected time by the user, creating the tour for 16 places
void Tour(int Time)
{
  digitalWrite(_pinOut,HIGH);
  while(digitalRead(tourPin)){}
  int Tb,Ta;
  for(int tour = 0 ;tour<16;tour++)
  {
    Tb = Ta = millis();
    if (client.connect(host, port)){ client.print(MakeKML(Coordnates[tour][0],Coordnates[tour][1],Coordnates[tour][2]));}
        client.stop();
    Serial.println(MakeKML(Coordnates[tour][0],Coordnates[tour][1],Coordnates[tour][2]));
   
    while((Tb - Ta) <(Time*1000))
    { 
      if(digitalRead(tourPin)) tour =16;
      Tb = millis();
    }
  }
  digitalWrite(_pinOut,LOW);
}
//-----------------------------------------------GetRec receive the comands sent by the voice module and return the a[5], position corresponding to voice index
int GetRec()
{
    if(VR3.available())
   {
      for(int i = 0; i<27;i++)
     {
      a[i] = VR3.read();
      delay(1); 
     }
   return a[5];
  }
  return 0;
}
//--------------------------------------------------------GroupsControll controls the voice groups and reports the status with a led
void GroupsControll(int rec)
{
  int h,t,p;
  delay(500);
  switch (rec)
  {
    case 17:// First Group
    digitalWrite(_pinOut,!digitalRead(_pinOut)); delay(500);
    digitalWrite(_pinOut,!digitalRead(_pinOut));
    for(t =0; t<4;t++)VR3.write(headVoice[t]);
    delay(500);
    for(h =0; h<11;h++)VR3.write(groups[0] [h]);
    break;
    case 18:// First Group
    for(p =0;p<4;p++){digitalWrite(_pinOut,!digitalRead(_pinOut)); delay(500);}
    for(t =0; t<4;t++)VR3.write(headVoice[t]);
    delay(500);
    for(h =0; h<11;h++)VR3.write(groups[1] [h]);
    break;
    case 19:// First Group
    for(p =0;p<6;p++){digitalWrite(_pinOut,!digitalRead(_pinOut)); delay(500);}
    for(t =0; t<4;t++)VR3.write(headVoice[t]);
    delay(500);
    for(h =0; h<11;h++)VR3.write(groups[2] [h]);
    break;
    case 20:// First Group
    for(p =0;p<8;p++){digitalWrite(_pinOut,!digitalRead(_pinOut)); delay(500);}
    for(t =0; t<4;t++)VR3.write(headVoice[t]);
    delay(500);
    for(h =0; h<11;h++)VR3.write(groups[3] [h]);
    break;
    default:
    break;
  } 
}
