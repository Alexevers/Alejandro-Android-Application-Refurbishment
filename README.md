
<div align="center">
    <img src="./Images/logo3.png" width="200" height="200" alt="css-in-readme">
    <img src="./Images/LGlogo1.png" width="243" height="175" alt="css-in-readme">
</div>

# Android Application Refurbishment
This is a Google Summer of Code 2022 project. 

Liquid Galaxy LAB has been participating in Google Summer of Code for 12 years,
and during all these years, there have been many contributors who have developed applications to be used in a Liquid Galaxy.
However, some of these applications haven’t been released to the Play Store or App Gallery.
The project's purpose is to update and upload these apps.

Here there are app sections with info:

## Extra work
### Gesture Controller App
<div align="left">
    <img src="./Images/Gesture logo.png"  alt="css-in-readme">
   
</div>

The project has the main goal to add new and more interactive ways to use Liquid Galaxy. First, like the project name suggests, the application let you use body poses to navigate with Google Earth with 9 different possible commands:
- **Idle:** is the "normal pose" and also performs a stop command;
- **Move:** move Google Earth to north, south, west or east;
- **Rotate:** rotates the camera to left or right;
- **Zoom:** make the camera get closer or out (in and out).

Also, the project has support to voice commands, that contains all the previous pose commands plus 2 more, in total 11 commands:
- **Fly to:** perform a query on Google Earth, and just make it go to someplace;
- **Planet:** change Google Earth current planet, with Earth, Moon, Mars, and Sky possibilities.

Gesture Controller uses one Android app to get the user camera pose inputs and the speech to voice commands, and also, a server to properly perform the commands on Liquid Galaxy. You can see the global project schema in the image below.
#### Extra information 
- Dart (Flutter)
- Node.js
- Tensorflow Lite
- Posenet
- Socket.IO
#### Issues
I could get an install version but it doesn't work. Here I give information about issues and how I fixed them:
[Gesture Controller issues](https://docs.google.com/document/d/1qSUQTHxO0o3saJjRrHU0gRuxavHR5tvowl52eNscIYM/edit?usp=sharing)

#### Imformation to you
If you want to modify this app using Tensorflow lite. You have to be carefully, because Tflite plugin is deprecated. I give you a github link where you can migrate Tflite plugin manually: (Tflite migrated to Android V2)[https://github.com/shaqian/flutter_tflite/pull/230/files] 

Instead of change the plugin manually, you can download mine here:[Tflite 1.1.2](https://drive.google.com/file/d/1jzsISIUbbOBSuDL_mSPuKLQA2XswQqm-/view?usp=sharing) 

More information about original project and deployer contact: https://github.com/LiquidGalaxyLAB/Gesture-Controller
