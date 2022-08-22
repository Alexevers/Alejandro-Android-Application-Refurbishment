#!/usr/bin/python
import socket

from pynput.keyboard import Key, Controller as KeyboardController
keyb = KeyboardController()

s = socket.socket()
s.bind(('0.0.0.0', 8000))
s.listen(2)

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

while True:
    client, addr = s.accept()
    while True:
        content = client.recv(256).decode()
        if len(content) == 0:
            break
        else:
            print(content)
            Position_Controller(content)
    #print("Closing connection")
   # client.close()
