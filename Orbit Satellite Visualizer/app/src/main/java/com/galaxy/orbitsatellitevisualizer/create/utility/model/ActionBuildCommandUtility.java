package com.galaxy.orbitsatellitevisualizer.create.utility.model;


import android.util.Log;

import com.galaxy.orbitsatellitevisualizer.connection.LGCommand;
import com.galaxy.orbitsatellitevisualizer.connection.LGConnectionManager;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.balloon.Balloon;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.movement.Movement;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.poi.POI;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.poi.POICamera;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.poi.POILocation;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.shape.Point;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.shape.Shape;

import java.util.List;

/**
 * This class is in charge of creating the commands that are going to be send to the liquid galaxy
 */
public class ActionBuildCommandUtility {

    private static final String TAG_DEBUG = "ActionBuildCommandUtility";

    private static String BASE_PATH = "/var/www/html/";
    public static String RESOURCES_FOLDER_PATH = BASE_PATH + "resources/";

    /**
     * @return Command to create the resources fule
     */
    static String buildCommandCreateResourcesFolder() {
        return "mkdir -p " + RESOURCES_FOLDER_PATH;
    }

    /**
     * Build the command to paint a balloon in Liquid Galaxy
     * @return String with command
     */
    public static String buildCommandBalloonWithLogos() {
        String startCommand =  "echo '" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\"\n" +
                "xmlns:atom=\"http://www.w3.org/2005/Atom\" \n" +
                " xmlns:gx=\"http://www.google.com/kml/ext/2.2\"> \n" +
                "<Document id=\"slave_3\">\n" +
                " <Folder> \n" +
                " <name>Logos</name> \n" +
                "  <ScreenOverlay>\n" +
                "  <name>Logo</name> \n" +
                "  <Icon> \n" +
                "   <href>http://lg1:81/resources/logos.png</href> \n" +
                "  </Icon> \n" +
                "  <overlayXY x=\"0\" y=\"1\" xunits=\"fraction\" yunits=\"fraction\"/> \n" +
                "  <screenXY x=\"0.02\" y=\"0.95\" xunits=\"fraction\" yunits=\"fraction\"/> \n" +
                "  <rotationXY x=\"0\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/> \n" +
                "  <size x=\"0.4\" y=\"0.2\" xunits=\"fraction\" yunits=\"fraction\"/> \n" +
                "  </ScreenOverlay> \n" +
                " </Folder> \n" +
                "</Document> \n" +
                "</kml>\n" +
                "' > " +
                BASE_PATH +
                "kml/slave_4.kml";
        Log.w(TAG_DEBUG, "Command: " + startCommand);
        return startCommand;
    }

    /**
     * Get the absolute path of the file
     * @param filePath The path of the file
     * @return the absolute path
     */
    private static String getFileName(String filePath) {
        String[] route = filePath.split("/");
        return route[route.length - 1];
    }

    static String buildWriteStarlinkFile() {
        String command = "echo 'http://lg1:81/resources/Starlink.kml' > " +
                BASE_PATH +
                "kmls.txt";
        Log.w(TAG_DEBUG, "command: " + command);
        return command;
    }

    static String buildWriteISSFile() {
        String command = "echo 'http://lg1:81/resources/ISS.kml' > " +
                BASE_PATH +
                "kmls.txt";
        Log.w(TAG_DEBUG, "command: " + command);
        return command;
    }

    static String buildWriteEnxanetaFile() {
        String command = "echo 'http://lg1:81/resources/Enxaneta.kml' > " +
                BASE_PATH +
                "kmls.txt";
        Log.w(TAG_DEBUG, "command: " + command);
        return command;
    }

    static String buildWriteStarlinkConstFile() {
        String command = "echo 'http://lg1:81/resources/StarlinkConst.kml' > " +
                BASE_PATH +
                "kmls.txt";
        Log.w(TAG_DEBUG, "command: " + command);
        return command;
    }

    static String buildWriteIridiumConstFile() {
        String command = "echo 'http://lg1:81/resources/IridiumConst.kml' > " +
                BASE_PATH +
                "kmls.txt";
        Log.w(TAG_DEBUG, "command: " + command);
        return command;
    }

    static String buildWriteRocketFile() {
        String command = "echo 'http://lg1:81/resources/rocket_simulation.kml' > " +
                BASE_PATH +
                "kmls.txt";
        Log.w(TAG_DEBUG, "command: " + command);
        return command;
    }

    /**
     * @return Command to clean the kmls.txt
     */
    public static String buildCleanKMLs() {
        String command = "echo '' > " +
                BASE_PATH +
                "kmls.txt";
        Log.w(TAG_DEBUG, "command: " + command);
        return command;
    }

    /**
     * @return Command to clean the query.txt
     */
    public static String buildCleanQuery() {
        String command = "echo '' > /tmp/query.txt";
        Log.w(TAG_DEBUG, "command: " + command);
        return command;
    }

    public static String buildCommandInsertOrbit(double[] lla_coordinates, int camera_height) {
        StringBuilder command = new StringBuilder();
        command.append("<gx:Tour> \n").append(" <name>Orbit</name> \n").append(" <gx:Playlist> \n");
        orbitLLA(lla_coordinates, command, camera_height);
        command.append(" </gx:Playlist>\n")
                .append("</gx:Tour>\n");
        Log.w(TAG_DEBUG, "INSERTED ORBIT: " + command.toString());
        return  command.toString();
    }

    public static String buildCommandInsertFlyTo2(double[] lla_coordinates) {
        StringBuilder command = new StringBuilder();
        flyToLLA(lla_coordinates, command);
        String new_command = "echo '" + command + "' > /tmp/query.txt";
        Log.w(TAG_DEBUG, "command: " + new_command);
        return new_command.toString();
    }

    public static String buildCommandInsertFlyTo(double[] lla_coordinates) {
        StringBuilder command = new StringBuilder();
        command.append("<gx:Tour> \n").append(" <name>Orbit</name> \n").append(" <gx:Playlist> \n");
        flyToLLA(lla_coordinates, command);
        command.append(" </gx:Playlist>\n")
                .append("</gx:Tour>\n");
        Log.w(TAG_DEBUG, "INSERTED ORBIT: " + command.toString());
        return  command.toString();
    }


    public static String buildCommandWriteOrbit() {
        String command = "echo 'http://lg1:81/Orbit.kml'  > " +
                BASE_PATH +
                "kmls.txt";
        Log.w(TAG_DEBUG, "command: " + command);
        return command;
    }

    public static String buildCommandStartOrbit() {
        String command = "echo \"playtour=Orbit\" > /tmp/query.txt";
        Log.w(TAG_DEBUG, "command: " + command);
        return command;
    }

    private static String buildTour(List<Action> actions, StringBuilder folderBalloonShapes) {
        StringBuilder command = new StringBuilder();
        Action action;
        for(int i = 0; i < actions.size(); i++){
            action = actions.get(i);
            if (action instanceof POI) {
                POI poi = (POI) action;
                command.append(POICommand(poi));
            } else if (action instanceof Movement) {
                Movement movement = (Movement) action;
                command.append(MovementCommand(movement));
            } else if (action instanceof Balloon) {
                Balloon balloon = (Balloon) action;
                command.append(BalloonCommand(balloon, i, folderBalloonShapes));
            } else if (action instanceof Shape) {
                Shape shape = (Shape) action;
                command.append(ShapeCommand(shape, i, folderBalloonShapes));
            }
        }
        return command.toString();
    }

    private static String POICommand(POI poi) {
        POILocation poiLocation = poi.getPoiLocation();
        POICamera poiCamera = poi.getPoiCamera();
        String command =  "     <gx:FlyTo>\n" +
                "      <gx:duration>" + poiCamera.getDuration() + "</gx:duration>\n" +
                "      <gx:flyToMode>bounce</gx:flyToMode>\n" +
                "      <LookAt>\n" +
                "       <longitude>" + poiLocation.getLongitude() + "</longitude>\n" +
                "       <latitude>" + poiLocation.getLatitude() + "</latitude>\n" +
                "       <altitude>" + poiLocation.getAltitude() + "</altitude>\n" +
                "       <heading>" + poiCamera.getHeading() + "</heading>\n" +
                "       <tilt>" + poiCamera.getTilt() + "</tilt>\n" +
                "       <range>" + poiCamera.getRange() + "</range>\n" +
                "       <gx:altitudeMode>" + poiCamera.getAltitudeMode() + "</gx:altitudeMode>\n" +
                "     </LookAt>\n" +
                "    </gx:FlyTo>\n\n";
        Log.w(TAG_DEBUG, "POI COMMAND: " + command);
        return  command;
    }

    private static String MovementCommand(Movement movement) {
        POI poi = movement.getPoi();
        StringBuilder command = new StringBuilder();
        if(movement.isOrbitMode()){
            orbit(poi, command);
            Log.w(TAG_DEBUG, "ORBIT COMMAND: " + command.toString());
        }else{
            POICamera camera = poi.getPoiCamera();
            POICamera poiCamera = new POICamera(camera.getHeading(), camera.getTilt(), camera.getRange(), camera.getAltitudeMode(), camera.getDuration());
            POI poiSend = new POI();
            poiCamera.setHeading(movement.getNewHeading());
            poiCamera.setTilt(movement.getNewTilt());
            poiSend.setPoiCamera(poiCamera);
            poiSend.setPoiLocation(poi.getPoiLocation());
            movement(poiSend, command, movement.getDuration());
            Log.w(TAG_DEBUG, "MOVEMENT COMMAND: " + command.toString());

        }
        return command.toString();
    }

    private static void orbit(POI poi, StringBuilder command) {
        double heading = poi.getPoiCamera().getHeading();
        int orbit = 0;
        while (orbit <= 36) {
            if (heading >= 360) heading = heading - 360;
            command.append("    <gx:FlyTo>\n").append("    <gx:duration>1.2</gx:duration> \n")
                    .append("    <gx:flyToMode>smooth</gx:flyToMode> \n")
                    .append("     <LookAt> \n")
                    .append("      <longitude>").append(poi.getPoiLocation().getLongitude()).append("</longitude> \n")
                    .append("      <latitude>").append(poi.getPoiLocation().getLatitude()).append("</latitude> \n")
                    .append("      <heading>").append(heading).append("</heading> \n")
                    .append("      <tilt>").append(60).append("</tilt> \n")
                    .append("      <gx:fovy>35</gx:fovy> \n")
                    .append("      <range>").append(poi.getPoiCamera().getRange()).append("</range> \n")
                    .append("      <gx:altitudeMode>absolute</gx:altitudeMode> \n")
                    .append("      </LookAt> \n")
                    .append("    </gx:FlyTo> \n\n");
            heading = heading + 10;
            orbit++;
        }
    }

    private static void orbitLLA(double[] lla_coordinates, StringBuilder command, int camera_height) {
        double heading = 0;
        int orbit = 0;
        while (orbit <= 36) {
            if (heading >= 360) heading = heading - 360;
            command.append("    <gx:FlyTo>\n").append("    <gx:duration>1.2</gx:duration> \n")
                    .append("    <gx:flyToMode>smooth</gx:flyToMode> \n")
                    .append("     <LookAt> \n")
                    .append("      <longitude>").append(lla_coordinates[1]).append("</longitude> \n")
                    .append("      <latitude>").append(lla_coordinates[0]).append("</latitude> \n")
                    .append("      <altitude>").append(lla_coordinates[2]).append("</altitude> \n")
                    .append("      <heading>").append(heading).append("</heading> \n")
                    .append("      <tilt>").append(45).append("</tilt> \n")
                    .append("      <gx:fovy>35</gx:fovy> \n")
                    .append("      <range>").append(lla_coordinates[2] + camera_height).append("</range> \n")
                    .append("      <gx:altitudeMode>relativeToGround</gx:altitudeMode> \n")
                    .append("      </LookAt> \n")
                    .append("    </gx:FlyTo> \n\n");
            heading = heading + 10;
            orbit++;
        }
    }

    private static void flyToLLA(double[] lla_coordinates, StringBuilder command) {
        double heading = 0;

            if (heading >= 360) heading = heading - 360;
            command.append("    <gx:FlyTo>\n").append("    <gx:duration>6</gx:duration> \n")
                    .append("    <gx:flyToMode>smooth</gx:flyToMode> \n")
                    .append("     <LookAt> \n")
                    .append("      <longitude>").append(lla_coordinates[1]).append("</longitude> \n")
                    .append("      <latitude>").append(lla_coordinates[0]).append("</latitude> \n")
                    .append("      <altitude>").append(lla_coordinates[2]).append("</altitude> \n")
                    .append("      <heading>").append(heading).append("</heading> \n")
                    .append("      <tilt>").append(60).append("</tilt> \n")
                    .append("      <gx:fovy>35</gx:fovy> \n")
                    .append("      <range>").append(lla_coordinates[2]+100000).append("</range> \n")
                    .append("      <gx:altitudeMode>relativeToGround</gx:altitudeMode> \n")
                    .append("      </LookAt> \n")
                    .append("    </gx:FlyTo> \n\n");
    }

    private static void movement(POI poi, StringBuilder command, int duration) {
        POILocation poiLocation = poi.getPoiLocation();
        POICamera poiCamera = poi.getPoiCamera();
        command.append("    <gx:FlyTo>\n")
                .append("    <gx:duration>").append(duration).append("</gx:duration>\n")
                .append("    <gx:flyToMode>smooth</gx:flyToMode>\n")
                .append("     <LookAt>\n")
                .append("      <longitude>").append(poiLocation.getLongitude()).append("</longitude>\n")
                .append("      <latitude>").append(poiLocation.getLatitude()).append("</latitude>\n")
                .append("      <altitude>").append(poiLocation.getAltitude()).append("</altitude>\n")
                .append("      <heading>").append(poiCamera.getHeading()).append("</heading>\n")
                .append("      <tilt>").append(poiCamera.getTilt()).append("</tilt>\n")
                .append("      <range>").append(poiCamera.getRange()).append("</range>\n")
                .append("      <gx:altitudeMode>").append(poiCamera.getAltitudeMode()).append("</gx:altitudeMode>\n")
                .append("     </LookAt>\n")
                .append("    </gx:FlyTo>\n\n");
    }

    private static String BalloonCommand(Balloon balloon, int position, StringBuilder folderBalloonShapes) {
        POI poi = balloon.getPoi();
        String TEST_PLACE_MARK_ID = "balloon" + position;

        String animate = "    <gx:AnimatedUpdate>\n" +
                "    <gx:duration>0</gx:duration>\n" +
                "     <Update>\n" +
                "      <targetHref/>\n" +
                "      <Change>\n" +
                "       <Placemark targetId=\"" +  TEST_PLACE_MARK_ID  + "\">\n" +
                "        <gx:balloonVisibility>1</gx:balloonVisibility>\n" +
                "       </Placemark>\n" +
                "      </Change>\n" +
                "     </Update>\n" +
                "    </gx:AnimatedUpdate>\n\n";

        String startCommand = "    <Placemark id=\"" + TEST_PLACE_MARK_ID + "\">\n" +
                "     <name>" + balloon.getPoi().getPoiLocation().getName() + "</name>\n" +
                "     <description>\n" +
                "      <![CDATA[\n" +
                "      <head>\n" +
                "      <!-- Required meta tags -->\n" +
                "      <meta charset=\"UTF-8\">\n" +
                "      <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
                "\n" +
                "      <!-- Bootstrap CSS -->\n" +
                "      <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" +
                "\n" +
                "      </head>\n" +
                "      <body>\n" +
                "       <div class=\"p-lg-5\" align=\"center\">\n" +
                "\n";
        String description = "";
        if(!balloon.getDescription().equals("")) {
            description = "       <h5>" + balloon.getDescription() + "</h5>\n" +
                    "       <br>\n";
        }
        String imageCommand = "";

        if(balloon.getImagePath() != null && !balloon.getImagePath().equals("")) {
            imageCommand =  "       <img src=\"./resources/" + getFileName(balloon.getImagePath()) + "\"> \n" +
                    "       <br>\n";
        }
        String videoCommand = "";
        if(balloon.getVideoPath() != null && !balloon.getVideoPath().equals("")) {
            videoCommand = "       <iframe" +
                    " src=\""+ balloon.getVideoPath() + "\" frameborder=\"0\"" +
                    " allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen>" +
                    "</iframe>";
        }
        String endCommand = "       </div>\n    <script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\n" +
                "       <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\" integrity=\"sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q\" crossorigin=\"anonymous\"></script>\n" +
                "       <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\" integrity=\"sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl\" crossorigin=\"anonymous\"></script>\n" +
                "       </body>\n" +
                "       ]]>\n" +
                "      </description>\n" +
                "      <Point>\n" +
                "       <coordinates>" + poi.getPoiLocation().getLongitude() + "," + poi.getPoiLocation().getLatitude() + "</coordinates>\n" +
                "      </Point>\n" +
                "    </Placemark>\n\n";
        String wait = commandWait(balloon.getDuration());
        folderBalloonShapes.append(startCommand + description + imageCommand + videoCommand + endCommand);
        Log.w(TAG_DEBUG, "BALLOON: " + folderBalloonShapes);
        String animateClose = "    <gx:AnimatedUpdate>\n" +
                "    <gx:duration>0</gx:duration>\n" +
                "     <Update>\n" +
                "      <targetHref/>\n" +
                "      <Change>\n" +
                "       <Placemark targetId=\"" +  TEST_PLACE_MARK_ID  + "\">\n" +
                "        <gx:balloonVisibility>0</gx:balloonVisibility>\n" +
                "       </Placemark>\n" +
                "      </Change>\n" +
                "     </Update>\n" +
                "    </gx:AnimatedUpdate>\n\n";
        return animate + wait + animateClose;
    }

    private static String ShapeCommand(Shape shape, int position, StringBuilder folderBalloonShapes) {
        String TEST_PLACE_MARK_ID = "shape" + position;
        String animate = "    <gx:AnimatedUpdate>\n" +
                "    <gx:duration>0</gx:duration>\n" +
                "     <Update>\n" +
                "      <targetHref/>\n" +
                "       <Change>\n" +
                "       <Placemark targetId=\"" +  TEST_PLACE_MARK_ID  + "\">\n" +
                "        </Placemark>\n" +
                "       </Change>\n" +
                "      </Update>\n" +
                "    </gx:AnimatedUpdate>\n\n";

        StringBuilder command = new StringBuilder();
        command.append("     <Placemark id=\"").append(TEST_PLACE_MARK_ID).append("\">\n")
                .append("      <styleUrl>#linestyleExample</styleUrl>\n")
                .append("     <name>").append(shape.getPoi().getPoiLocation().getName()).append("</name>\n")
                .append("      <LineString>\n");
        if(shape.isExtrude()) command.append("       <extrude>1</extrude>\n");
        command.append("       <tessellate>1</tessellate>\n").append("       <altitudeMode>absolute</altitudeMode>\n")
                .append("       <coordinates>\n");
        List points = shape.getPoints();
        int pointsLength = points.size();
        Point point;
        for(int i = 0; i < pointsLength; i++){
            point = (Point) points.get(i);
            command.append("        ").append(point.getLongitude()).append(",").append(point.getLatitude())
                    .append(",").append(point.getAltitude()).append("\n");
        }
        command.append("       </coordinates>\n")
                .append("      </LineString>\n")
                .append("     </Placemark>\n\n");
        Log.w(TAG_DEBUG, "SHAPE COMMAND: " + command.toString());
        folderBalloonShapes.append(command.toString());
        return  animate;
    }

    private static String commandWait(int duration) {
        String waitCommand =  "    <gx:Wait>\n" +
                "     <gx:duration>" + duration + "</gx:duration>\n" +
                "    </gx:Wait>\n\n";
        Log.w(TAG_DEBUG, "WAIT COMMAND:" + waitCommand);
        return  waitCommand;
    }

    public static String buildCommandExitTour(){
        String command = "echo \"exittour=true\" > /tmp/query.txt";
        Log.w(TAG_DEBUG, "command: " + command);
        return command;
    }

    public static String buildCommandCleanSlaves() {
        String command = "echo '' > " + BASE_PATH + "kmls.txt ";
        Log.w(TAG_DEBUG, "commandCleanSlaves: " + command);
        return command;
    }
}
