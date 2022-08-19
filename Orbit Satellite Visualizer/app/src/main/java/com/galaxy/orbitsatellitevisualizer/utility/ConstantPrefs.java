package com.galaxy.orbitsatellitevisualizer.utility;

/**
 * This class is in charge of saving the constants that are use to save in the shared preferences.
 */
public enum ConstantPrefs {

    SHARED_PREFS("sharedPrefs"), TRY_TO_RECONNECT("tryingReconnect"),
    URI_TEXT("uriText"), USER_NAME("userName"),
    USER_PASSWORD("userPassword"), IS_CONNECTED("Connected"),
    FILE_NAME("fileName"), LATITUDE("latitude"), LONGITUDE("longitude"),
    ALTITUDE("altitude"), DURATION("duration"),
    HEADING("heading"), TILT("tilt"), RANGE("range"),
    ALTITUDE_MODE("altitudeMode"), STORY_BOARD_JSON("storyboardjson");

    private final String name;

    ConstantPrefs(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
