#include <SoftwareSerial.h>
#include <LiquidGalaxyController.h>
SoftwareSerial VR3(22,23);
//------------------------------------------------------------------------------------------------------------
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
//------------------------------------------------------------------------------------------------------------
byte _z[27];
char Rec[] = {0xaa,0x03,0x20};
byte ComandsNumber[] = {0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,0x10,0x11,0x12,0x13,0x14};
//------------------------------------------------------------------------------------------------------------
void setup() 
{
  Serial.begin(9600);
  VR3.begin(9600);
}
void loop() 
{
  int key = LGKey.KeyPress(); // KEYBOARD FUNCTION
   if(key)
   {
    Serial.print("Regording: ");
    Serial.println(key);
    delay(500);
      for(int _t=0;_t<3;_t++) {VR3.write(Rec[_t]);} // At this point the first command is sent for to start recording
      VR3.write(ComandsNumber[key]);
      VR3.write(0x0A);
      delay(1);
   }

    if(VR3.available())
    {
      for(int i = 0; i<27;i++)//27
     {
        _z[i] = VR3.read();
          delay(1); 
          //Serial.print(_z[i],HEX);
     // Serial.print("-");
     }
   //  Serial.println("");
        if((_z[1]==13 && _z[2]==255)||(_z[2]==13 && _z[1]==255)||_z[1] == 13)
        {
          Serial.println("Speak Now");
        } 
           if((_z[1]==255 && _z[2]==15) ||(_z[2]==255 && _z[1]==15)||(_z[1]==15 && _z[2]==10))
                {
                  Serial.println("Speak Again");
                }
                   if((_z[1]==255 && _z[2]==11)||(_z[1]==11 && _z[2]==255)||_z[1] == 11)
                          {
                            Serial.println("Sucess!");
                          }
                           if((_z[1] == 18 && _z[2] == 10)||(_z[1] == 18 && _z[2] == 255)||(_z[2] == 18 && _z[1] == 255)) 
                                {
                                  Serial.println("Error");
                                }
                                if((_z[2] == 10 && _z[3] == 10)||(_z[1] == 10 && _z[2] == 10)) 
                                {
                                  Serial.println("End Time");
                                }
   }
}
