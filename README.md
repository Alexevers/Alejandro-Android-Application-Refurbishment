# Alejandro-Android-Application-Refurbishment
This is a Google Summer of Code 2022 project. 
Liquid Galaxy LAB has been participating in Google Summer of Code for 12 years,
and during all these years, there have been many contributors who have developed applications to be used in a Liquid Galaxy.
However, some of these applications haven’t been released to the Play Store or App Gallery.
The project's purpose is to updated and upload these apps.

Here there are app sections with info:

# Image-Satellite-Visualizer
Liquid Galaxy as a meaningful presentation tool has a lot of information that can be displayed for diverse purposes like an educational tool, or for a monitoring system. With that in mind, the idea of ​​the project is the real time visualization of satellite images that would be attached as layers of google earth, besides being able to have diverse information of the earth being graphically generated as storms, fires, masses of heat and water vapor, a synchronous earth visualization will allow for more complex interactions. From there, the entire application would be controlled through a script responsible for managing calls made to the satellite's external APIs (e.g. NASA API and Copernicus) that responses with the metadata necessary for the kml creation and a tablet application that would give the user control of which layers or information would like to be displayed and responsible for handling the API calls, manipulate KMLs and sending them to Liquid Galaxy through Bash scripts. The application’s front end will be developed with Flutter and the image selected by the user will be generated based on selected options.

More information about: https://github.com/LiquidGalaxyLAB/image-satellite-visualizer


