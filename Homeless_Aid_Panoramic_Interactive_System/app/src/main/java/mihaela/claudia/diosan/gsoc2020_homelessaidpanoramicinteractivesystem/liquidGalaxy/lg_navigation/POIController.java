package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation;


import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGConnectionManager;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.logic.Homeless;

import static mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand.CRITICAL_MESSAGE;

public class POIController {

    private static POIController INSTANCE = null;

    public static final POI EARTH_POI = new POI()
            .setLongitude(10.52668d)
            .setLatitude(40.085941d)
            .setAltitude(0.0d)
            .setRange(10000000.0d)
            .setAltitudeMode("relativeToSeaFloor");

    public synchronized static POIController getInstance() {
        if (INSTANCE == null)
            INSTANCE = new POIController();
        return INSTANCE;
    }

    private POI currentPOI;
    //private POI previousPOI;
    private Homeless homeless;

    private POIController() {
        currentPOI = EARTH_POI;
        moveToPOI(EARTH_POI, null);
    }

    public LGCommand moveToPOI(POI poi, LGCommand.Listener listener) {
        //previousPOI = new POI(currentPOI);
        currentPOI = new POI(poi);
        return sendPoiToLG(listener);
    }

    public LGCommand flyToCity(POI poi, LGCommand.Listener listener){
        currentPOI = new POI(poi);
        return sendCityToLG(listener);
    }

    public LGCommand showPlacemark(POI poi, LGCommand.Listener listener, String placemarkIcon, String route){
        currentPOI = new POI(poi);
        return  sendPlacemarkToLG(listener, placemarkIcon, route);
    }

    public LGCommand showBalloon(POI poi, LGCommand.Listener listener,String description, String image, String route){
        currentPOI = new POI(poi);
        return  sendBalloonToLG(listener,description, image, route);
    }

    public LGCommand showBalloonOnSlave(POI poi, LGCommand.Listener listener,String description,String route, String image, String slave_name){
        currentPOI = new POI(poi);
        return  sendBalloonToSlave(listener,description,route, image, slave_name);
    }

    public LGCommand sendPlacemark(POI poi, LGCommand.Listener listener, String hostIp, String route){
        currentPOI = new POI(poi);
        return  setPlacemark(listener, hostIp, route);
    }

    public LGCommand sendBalloon(POI poi, LGCommand.Listener listener, String route){
        currentPOI = new POI(poi);
        return  setBalloon(listener, route);
    }

    public LGCommand sendPoi(POI poi, LGCommand.Listener listener, String route){
        currentPOI = new POI(poi);
        return  setPoi(listener, route);
    }

    public synchronized void moveXY(double angle, double percentDistance) {
        //.setLongitude() [-180 to +180]: X (cos)
        //.setLatitude() [-90 to +90]: Y (sin)

        /*POI newPoi = new POI(currentPOI);
        //0.0001% of RANGE
        double STEP_XY = 0.000001;
        newPoi.setLongitude(newPoi.getLongitude() + Math.cos(angle) * percentDistance * STEP_XY * newPoi.getRange());
        while (newPoi.getLongitude() > 180) {
            newPoi.setLongitude(newPoi.getLongitude() - 360);
        }
        while (newPoi.getLongitude() < -180) {
            newPoi.setLongitude(newPoi.getLongitude() + 360);
        }

        newPoi.setLatitude(newPoi.getLatitude() - Math.sin(angle) * percentDistance * STEP_XY * newPoi.getRange());
        while (newPoi.getLatitude() > 90) {
            newPoi.setLatitude(newPoi.getLatitude() - 180);
        }
        while (newPoi.getLatitude() < -90) {
            newPoi.setLatitude(newPoi.getLatitude() + 180);
        }

        moveToPOI(newPoi, null);*/
    }

    public synchronized void moveCameraAngle(double angle, double percentDistance) {
        //.setTilt() [0 to 90]: the angle between what you see and the earth (90 means you see horizon) (the sin of the angle)
        //.setHeading() [-180 to 180]: compass degrees (the cos of the angle)
    }

    public synchronized void zoomIn(double percent) {
        //.setRange() [0 to 999999]
    }

    public synchronized void zoomOut(double percent) {
        //.setRange() [0 to 999999]
    }

    private LGCommand sendPoiToLG(LGCommand.Listener listener) {
        LGCommand lgCommand = new LGCommand(buildCommand(currentPOI), CRITICAL_MESSAGE, (String result) -> {
            //currentPOI = new POI(previousPOI);
            if(listener != null)
                listener.onResponse(result);
        });
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
        return lgCommand;
    }


    private static String buildCommand(POI poi) {
        //return "echo 'flytoview=<gx:duration>3</gx:duration><gx:flyToMode>smooth</gx:flyToMode><LookAt><longitude>" + poi.getLongitude() + "</longitude><latitude>" + poi.getLatitude() + "</latitude><altitude>" + poi.getAltitude() + "</altitude><heading>" + poi.getHeading() + "</heading><tilt>" + poi.getTilt() + "</tilt><range>" + poi.getRange() + "</range><gx:altitudeMode>" + poi.getAltitudeMode() + "</gx:altitudeMode></LookAt>' > /tmp/query.txt";

        return "echo 'flytoview=<gx:duration>3</gx:duration><gx:flyToMode>smooth</gx:flyToMode><LookAt>" +
                "<longitude>" + poi.getLongitude() + "</longitude>" +
                "<latitude>" + poi.getLatitude() + "</latitude>" +
                "<altitude>" + poi.getAltitude() + "</altitude>" +
                "<heading>" + poi.getRange() + "</heading>" +
                "<tilt>" + poi.getTilt() + "</tilt>" +
                "<range>" + poi.getRange() + "</range>" +
                "<gx:altitudeMode>" + poi.getAltitudeMode() + "</gx:altitudeMode>" +
                "</LookAt>' > /tmp/query.txt";
    }


    private LGCommand sendCityToLG(LGCommand.Listener listener) {
        LGCommand lgCommand = new LGCommand(flyToCity(currentPOI), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            if(listener != null)
                listener.onResponse(result);
        });
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
        return lgCommand;
    }


    public static String flyToCity(POI poi){
        return "echo 'flytoview=<gx:Wait><gx:duration>10</gx:duration><gx:/Wait><gx:flyToMode>smooth</gx:flyToMode><LookAt>" +
                "<longitude>" + poi.getLongitude() + "</longitude>" +
                "<latitude>" + poi.getLatitude() + "</latitude>" +
                "<altitude>" + poi.getAltitude() + "</altitude>" +
                "<heading>" + poi.getHeading() +"</heading>" +
                "<tilt>" + poi.getTilt() + "</tilt>"+
                "<range>" + poi.getRange() + "</range>" +
                "<gx:altitudeMode>" + poi.getAltitudeMode() + "</gx:altitudeMode>" +
                "</LookAt>' > /tmp/query.txt; sleep 25";
    }


    private LGCommand sendPlacemarkToLG(LGCommand.Listener listener, String placemarkIcon, String route){
        LGCommand lgCommand = new LGCommand(buildPlacemark(currentPOI, placemarkIcon, route), CRITICAL_MESSAGE, (String result) -> {
            //currentPOI = new POI(previousPOI);
            if(listener != null)
                listener.onResponse(result);
        });
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
        return lgCommand;
    }

    private LGCommand sendBalloonToLG(LGCommand.Listener listener,String description, String image, String route){
        LGCommand lgCommand = new LGCommand(buildDescriptionBallon(currentPOI,description, image, route), CRITICAL_MESSAGE, (String result) -> {
            if(listener != null)
                listener.onResponse(result);
        });
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
        return lgCommand;
    }

    private LGCommand sendBalloonToSlave(LGCommand.Listener listener,String description, String route, String image, String slave_name){
        LGCommand lgCommand = new LGCommand(buildDescriptionBalloonStatistics(currentPOI, description,route, image, slave_name), CRITICAL_MESSAGE, (String result) -> {
            if(listener != null)
                listener.onResponse(result);
        });
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
        return lgCommand;
    }

    private LGCommand setPlacemark(LGCommand.Listener listener, String hostIp, String route){
        LGCommand lgCommand = new LGCommand(setPlacemarkRoute(currentPOI, hostIp,route), CRITICAL_MESSAGE, (String result) -> {
            //currentPOI = new POI(previousPOI);
            if(listener != null)
                listener.onResponse(result);
        });
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
        return lgCommand;
    }

    private LGCommand setPoi(LGCommand.Listener listener, String route){
        LGCommand lgCommand = new LGCommand(setPoiRoute(currentPOI,route), CRITICAL_MESSAGE, (String result) -> {
            //currentPOI = new POI(previousPOI);
            if(listener != null)
                listener.onResponse(result);
        });
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
        return lgCommand;
    }

    private LGCommand setBalloon(LGCommand.Listener listener, String route){
        LGCommand lgCommand = new LGCommand(setBalloonRoute(currentPOI,route), CRITICAL_MESSAGE, (String result) -> {
            //currentPOI = new POI(previousPOI);
            if(listener != null)
                listener.onResponse(result);
        });
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
        return lgCommand;
    }



    private static String buildPlacemark(POI poi, String placemarkIcon, String route){
       return "echo '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<kml xmlns=\"http://www.opengis.net/kml/2.2\"> " +
               "<Placemark>\n" +
               "  <Style id=\"homelessIcon\">\n" +
               "      <IconStyle>\n" +
               "        <Icon>\n" +
               "          <href>" + placemarkIcon + "</href>\n" +
               "        </Icon>\n" +
               "      </IconStyle>\n" +
               "    </Style>\n" +
               "  <styleUrl>#homelessIcon</styleUrl>\n" +
               " <Point>\n" +
               " <coordinates>" + poi.getLongitude() + "," + poi.getLatitude() + "," + poi.getAltitude() + "</coordinates>\n" +
               " </Point>\n" +
               " </Placemark> </kml>' > /var/www/html/hapis/" + route + "/" + poi.getName() + ".kml";
    }

    public static void downloadProfilePhoto(String username, String imageUrl){
        String sentence1 = "cd /var/www/html/hapis/balloons/basic/homeless/ ;curl -o " + username + " " + imageUrl;
        String sentence2 = "cd /var/www/html/hapis/balloons/bio/homeless/ ;curl -o " + username + " " + imageUrl;
        String sentence3 = "cd /var/www/html/hapis/balloons/transactions/homeless/ ;curl -o " + username + " " + imageUrl;
        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence1, CRITICAL_MESSAGE, null));
        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence2, CRITICAL_MESSAGE, null));
        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence3, CRITICAL_MESSAGE, null));
    }



    private static String setPlacemarkRoute(POI poi, String hostIp, String route){
        return "echo 'http://" + hostIp + ":81/hapis/" + route + "/" + poi.getName() + ".kml' >> /var/www/html/kmls.txt";
    }

    private static String setPoiRoute(POI poi,String route){
        return "echo 'http://localhost:81/hapis/" + route + "/" + poi.getName() + ".kml' > /var/www/html/kmls.txt;sleep 5";
    }


    private static String setBalloonRoute(POI poi,String route){
        return "echo 'http://localhost:81/hapis/" + route + "/" + poi.getName() + ".kml' >> /var/www/html/kmls.txt";
    }

    public static void cleanKmls(){
        String sentence = "chmod 777 /var/www/html/kmls.txt; echo '' > /var/www/html/kmls.txt";
        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, CRITICAL_MESSAGE, null));
    }


    private static String buildDescriptionBallon(POI poi, String description, String image, String route ){
       return  "echo '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<kml xmlns=\"http://www.opengis.net/kml/2.2\"\n" +
               "  xmlns:gx=\"http://www.google.com/kml/ext/2.2\">\n" +
               "  \n" +
               " <Placemark>\n" +
               " <name>" + poi.getName() + "</name>\n" +
               " <description>\n" +
               " <![CDATA[\n" +
               "<body style=\" margin:5 width:500px; height:550px text-align:center\"> \n" +
               "<center><img src=" + image + " " +  "width = \"400px\" class=\"center\" ></center> \n"+
               "<font size = \"+2\">" + description + "</font> \n" +
               "</body>" +
               "]]> \n" +
               "</description>\n" +
               " <gx:displayMode>panel</gx:displayMode>" +
               " <gx:balloonVisibility>1</gx:balloonVisibility>\n" +
               " <Point>\n" +
               " <coordinates>" + poi.getLongitude() + "," + poi.getLatitude() + "," + poi.getAltitude() +"</coordinates>\n" +
               " </Point>\n" +
               " </Placemark>\n" +
               " \n" +
               "</kml>' > /var/www/html/hapis/" + route + "/" + poi.getName() + ".kml";
    }

    private static String buildDescriptionBalloonStatistics(POI poi, String description,String route, String image, String slave_name ){
        return  "echo '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\"\n" +
                "  xmlns:gx=\"http://www.google.com/kml/ext/2.2\">\n" +
                "  \n" +
                " <Placemark>\n" +
                " <name>" + poi.getName() + "</name>\n" +
                " <description>\n" +
                " <![CDATA[\n" +
                "<body style=\" margin:5 width:700px; height:800px text-align:center\"> \n" +
                "<center><img src=" + route + image + " " +  "width = \"600px\" class=\"center\" ></center> \n"+
                "<font size = \"+3\" >" + description + "</font> \n" +
                "</body>" +
                "]]> \n" +
                "</description>\n" +
                " <gx:displayMode>panel</gx:displayMode>" +
                " <gx:balloonVisibility>1</gx:balloonVisibility>\n" +
                " </Placemark>\n" +
                " \n" +
                "</kml>' > /var/www/html/kml/" + slave_name + ".kml";
    }


    public static void cleanKmlSlave(String slave_name){
        String sentence = "chmod 777 /var/www/html/kml/" + slave_name + ".kml; echo '" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\"" +
                " xmlns:gx=\"http://www.google.com/kml/ext/2.2\"" +
                " xmlns:kml=\"http://www.opengis.net/kml/2.2\" " +
                " xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
                " <Document id=\" " + slave_name + "\"> \n" +
                " </Document>\n" +
                " </kml>\n' > /var/www/html/kml/" + slave_name + ".kml";

        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, CRITICAL_MESSAGE, null));
    }

    public static void setLogos(String slave_name){
        String sentence = "chmod 777 /var/www/html/kml/" + slave_name + ".kml; echo '" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\"\n" +
                "xmlns:atom=\"http://www.w3.org/2005/Atom\" \n"  +
                " xmlns:gx=\"http://www.google.com/kml/ext/2.2\"> \n" +
                " <Document>\n " +
                " <Folder> \n" +
                    "<name>Logos</name> \n" +
                        "<ScreenOverlay>\n" +
                            "<name>Logo</name> \n" +
                            " <Icon> \n" +
                            "<href>http://lg1:81/hapis/logos.png</href> \n" +
                            " </Icon> \n" +
                            " <overlayXY x=\"0\" y=\"1\" xunits=\"fraction\" yunits=\"fraction\"/> \n" +
                            " <screenXY x=\"0.02\" y=\"0.95\" xunits=\"fraction\" yunits=\"fraction\"/> \n" +
                            " <rotationXY x=\"0\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/> \n" +
                            " <size x=\"0.6\" y=\"0.2\" xunits=\"fraction\" yunits=\"fraction\"/> \n" +
                        "</ScreenOverlay> \n" +
                " </Folder> \n" +
                " </Document> \n" +
                " </kml>\n' > /var/www/html/kml/" + slave_name + ".kml";

        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, CRITICAL_MESSAGE, null));
    }


}
