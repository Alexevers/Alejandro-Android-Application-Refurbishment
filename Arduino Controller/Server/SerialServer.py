#!/usr/bin/python
import serial
import keyboard
import serial.tools.list_ports

from pynput.keyboard import Key, Controller as KeyboardController

keyb = KeyboardController()

esp_ports = [
    p.device
    for p in serial.tools.list_ports.comports()
    if 'CP2102' in p.description
]

if not esp_ports:
    raise IOError("No ESP found")
if len(esp_ports) == 1:
    comport = serial.Serial('/dev/ttyUSB0', 9600)
    #comport = serial.Serial('COM3', 9600)

def Position_Controller(dataRec):
    data = dataRec

    if "linear" in data:
        keyb.press('r')
    elif "zOut" in data:
        keyb.press(Key.page_down)
    elif "zIn" in data:
        keyb.press(Key.page_up)
    elif "right" in data:
        keyb.press(Key.right)  # RIGHT
    elif "left" in data:
        keyb.press(Key.left)  # LEFT
    elif "up" in data:
        keyb.press(Key.up)  # UP
    elif "down" in data:
        keyb.press(Key.down)  # DOWN
    elif "CamUp" in data:
        keyb.press(Key.ctrl)
        keyb.press(Key.up)
    elif "CamDown" in data:
        keyb.press(Key.ctrl)
        keyb.press(Key.down)
    elif "CamRight" in data:
        keyb.press(Key.ctrl)
        keyb.press(Key.right)
    elif "CamLeft" in data:
        keyb.press(Key.ctrl)
        keyb.press(Key.left)
    elif "rollRight" in data:
        keyb.press(Key.shift)
        keyb.press(Key.right)
    elif "rollLeft" in data:
        keyb.press(Key.shift)
        keyb.press(Key.left)
    elif "tiltUp" in data:
        keyb.press(Key.shift)
        keyb.press(Key.up)  # UP Tilt
    elif "tiltDown" in data:
        keyb.press(Key.shift)
        keyb.press(Key.down)  # DOWN Tilt
    elif "zero" in data:
        keyb.release(Key.shift)
        keyb.release(Key.ctrl)
        keyb.release(Key.down)
        keyb.release(Key.right)
        keyb.release(Key.left)
        keyb.release(Key.up)
        keyb.release(Key.page_up)
        keyb.release(Key.page_down)
    else:

        f = open("/tmp/query.txt", "w")
        f.write(data)
        f.close()



if __name__ == "__main__":

    while True:
        valor = comport.readline().decode() #'UTF-8')
        Position_Controller(valor)


