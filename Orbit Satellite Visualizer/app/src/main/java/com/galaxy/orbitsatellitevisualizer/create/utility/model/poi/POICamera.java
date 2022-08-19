package com.galaxy.orbitsatellitevisualizer.create.utility.model.poi;

/**
 * This is the class in charge of the action of the camera in the location
 */
public class POICamera {


    private double heading;
    private double tilt;
    private double range;
    private String altitudeMode;
    private int duration;


    public POICamera(double heading, double tilt, double range, String altitudeMode, int duration) {
        this.heading = heading;
        this.tilt = tilt;
        this.range = range;
        this.altitudeMode = altitudeMode;
        this.duration = duration;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getTilt() {
        return tilt;
    }

    public void setTilt(double tilt) {
        this.tilt = tilt;
    }

    public double getRange() {
        return range;
    }

    public String getAltitudeMode() {
        return altitudeMode;
    }

    public int getDuration() {
        return duration;
    }
}
