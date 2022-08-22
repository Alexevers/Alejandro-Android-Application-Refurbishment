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

#ifndef LiquidGalaxyController_h
#define LiquidGalaxyController_h

#include "Arduino.h"

class LG_JoysticSetup
{
	public:
		LG_JoysticSetup(int piny, int pinx, int button,int *_Range);
		char JoysticRead();
		
	private:
		int _piny,_pinx,_pinPush; // ALL VARIABLES ARE CHANGED
		int *_range;
	
};

class LG_UltrasonicSetup
{
	public:
		LG_UltrasonicSetup(int _Trig, int _Echo);
		int  UltrasonicMensure();
		
	private:
		int _trig, _echo;
		long _duration;
		long _distance;
	
};

class LG_Keypad
{
	public:
		LG_Keypad(int *keyMatriz,byte *rowsPins,byte *colunsPins,byte nRow,byte nColun);
		int KeyPress();
		
	private:
		int KeyUpdate(); // GANGED THE TIPE
		
		byte _rows, _coluns;
    	byte *_rowsPin;
    	byte *_colunsPin;
    	int *_keys;
    	int _a = 0;
    	int _b = 0;
};
	

#endif




