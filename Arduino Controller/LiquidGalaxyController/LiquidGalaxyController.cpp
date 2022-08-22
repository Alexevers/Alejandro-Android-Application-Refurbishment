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
-------------------------------------------------------------*/

#include "Arduino.h"
#include "LiquidGalaxyController.h"

LG_JoysticSetup::LG_JoysticSetup(int piny, int pinx, int button,int *_Range) // JOYSTICK CONTROLLER CONFIGURATION FUNCTION
{
	if(button)
	{
		pinMode(_pinPush,INPUT);
		_pinPush = button;
	}
	_piny = piny;
	_pinx = pinx;
	_range = _Range;
	
}

char LG_JoysticSetup:: JoysticRead() // FUNCTION FOR TO READ THE JOYSTIC CONTROLLER
{
    if(analogRead(_piny) > _range[0]) return 'B';//IF THE JOYSTIC IS MOVED BACK
    else if(analogRead(_piny) < _range[1]) return 'F';//IF THE JOYSTIC IS MOVED FRONT

    if(analogRead(_pinx) > _range[2]) return 'R';//IF THE JOYSTIC IS MOVED RIGTH
    else if(analogRead(_pinx) < _range[3]) return 'L';//IF THE JOYSTIC IS MOVED LEFT
    
    if(_pinPush != 0)
    {
    	if(digitalRead(_pinPush))return 'P'; //IF THE BUTTON IS PRESSED 
    }

    return 0;
}
//----------------------------------------------------------------------------------------------------------------------------------------------------

LG_UltrasonicSetup::LG_UltrasonicSetup(int _Trig, int _Echo) // PIN MODE SETUP 
{
  	pinMode(_Trig,OUTPUT); // Define the _trig pin OUTPUT
  	digitalWrite(_Trig,LOW);
  	pinMode(_Echo,INPUT);
  	_trig = _Trig;
  	_echo = _Echo;
}

int LG_UltrasonicSetup::UltrasonicMensure()
{
    digitalWrite(_trig, LOW);  
    delayMicroseconds(2);  
    digitalWrite(_trig, HIGH);  
    delayMicroseconds(10);  
    digitalWrite(_trig, LOW);  
    _duration = pulseIn(_echo,HIGH);  
    _distance = _duration /29 / 2 ; 
	 
    return _distance;
}
//----------------------------------------------------------------------------------------------------------------------------------------------------

LG_Keypad::LG_Keypad(int *keyMatriz,byte *rowsPins,byte *colunsPins,byte nRow,byte nColun) // This function receive the all information about the keyboard 
{                                                                                      // And map the pins and variables
  	_rows = nRow;
 	_coluns = nColun;
  	_colunsPin = colunsPins ;
  	_rowsPin = rowsPins ;
 	_keys = keyMatriz;
  
  	for(int x=0;x < _rows; x++)
    	{
      		pinMode(_rowsPin[x], OUTPUT);
      		digitalWrite(_rowsPin[x], LOW);
    	}
    for(int _y=0;_y < _coluns; _y++)
    	pinMode(_colunsPin[_y], INPUT);
}

int LG_Keypad::KeyUpdate()// This function is responsible for to updating the keyboard, if anykey is pressed
{
  	 for(int _x=0; _x < _rows; _x ++) 
  		{
        	for(int _r=0;_r < _rows;_r ++)
        	{
          		digitalWrite(_rowsPin[_r],LOW);  
        	}
      		digitalWrite(_rowsPin[_x], HIGH);

       		for(int _s=0;_s < _coluns; _s ++)
      		{
         		if (digitalRead(_colunsPin[_s])) { return _keys[(_x*4)+_s];}// At that point the function returns the value corresponding to the arrey's position 
         		delayMicroseconds(10);
     		 }
    	}  return 0;
}

int LG_Keypad::KeyPress()
{
	_a = KeyUpdate();
	
	if (_a != _b )
	{
		_b = _a;
		return _a;
	}
	
	return 0;
}








