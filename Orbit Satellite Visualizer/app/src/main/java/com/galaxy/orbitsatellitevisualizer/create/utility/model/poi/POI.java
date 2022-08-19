package com.galaxy.orbitsatellitevisualizer.create.utility.model.poi;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.galaxy.orbitsatellitevisualizer.create.utility.IJsonPacker;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.Action;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.ActionIdentifier;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is in charge of saving and load the POI data for the database.
 */
public class POI extends Action implements IJsonPacker, Parcelable {


    public static final Creator CREATOR = new Creator() {
        public POI createFromParcel(Parcel in) {
            return new POI(in);
        }

        public POI[] newArray(int size) {
            return new POI[size];
        }
    };

    private POILocation poiLocation;
    private POICamera poiCamera;

    /**
     * Empty Constructor
     */
    public POI(){
        super(ActionIdentifier.LOCATION_ACTIVITY.getId());
    }

    public POI(long id, POILocation poiLocation, POICamera poiCamera){
        super(id, ActionIdentifier.LOCATION_ACTIVITY.getId());
        this.poiLocation = poiLocation;
        this.poiCamera = poiCamera;
    }

    public POI(Parcel in) {
        super(in.readLong(), ActionIdentifier.LOCATION_ACTIVITY.getId());

        String name = in.readString();
        double longitude = in.readDouble();
        double latitude = in.readDouble();
        double altitude = in.readDouble();
        this.poiLocation = new POILocation(name, longitude, latitude,altitude);

        double heading = in.readDouble();
        double tilt = in.readDouble();
        double range = in.readDouble();
        String altitudeMode = in.readString();
        int duration = in.readInt();
        this.poiCamera = new POICamera(heading, tilt, range, altitudeMode, duration);
    }

    public POI(POI poi) {
        super(poi.getId(), ActionIdentifier.LOCATION_ACTIVITY.getId());
        this.poiLocation = poi.poiLocation;
        this.poiCamera = poi.poiCamera;
    }


    public POILocation getPoiLocation() {
        return poiLocation;
    }

    public POI setPoiLocation(POILocation poiLocation) {
        this.poiLocation = poiLocation;
        return this;
    }

    public POICamera getPoiCamera() {
        return poiCamera;
    }

    public POI setPoiCamera(POICamera poiCamera) {
        this.poiCamera = poiCamera;
        return this;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("poi_id", this.getId());
        obj.put("type", this.getType());
        obj.put("poi_location_name", poiLocation.getName());
        obj.put("poi_location_longitude", poiLocation.getLongitude());
        obj.put("poi_location_latitude", poiLocation.getLatitude());
        obj.put("poi_camera_altitude", poiLocation.getAltitude());
        obj.put("poi_camera_heading", poiCamera.getHeading());
        obj.put("poi_camera_tilt", poiCamera.getTilt());
        obj.put("poi_camera_range", poiCamera.getRange());
        obj.put("poi_camera_altitudeMode", poiCamera.getAltitudeMode());
        obj.put("poi_camera_duration", poiCamera.getDuration());

        return obj;
    }

    @Override
    public POI unpack(JSONObject obj) throws JSONException {
        this.setId(obj.getLong("poi_id"));
        this.setType(obj.getInt("type"));

        String name = obj.getString("poi_location_name");
        double longitude = obj.getDouble("poi_location_longitude");
        double latitude = obj.getDouble("poi_location_latitude");
        double altitude = obj.getDouble("poi_camera_altitude");
        poiLocation = new POILocation(name, longitude, latitude, altitude);

        double heading = obj.getDouble("poi_camera_heading");
        double tilt = obj.getDouble("poi_camera_tilt");
        double range = obj.getDouble("poi_camera_range");
        String altitudeMode = obj.getString("poi_camera_altitudeMode");
        int duration = obj.getInt("poi_camera_duration");
        poiCamera = new POICamera(heading, tilt, range, altitudeMode, duration);

        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return this.poiLocation.getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(this.getId());
        parcel.writeString(poiLocation.getName());
        parcel.writeDouble(poiLocation.getLongitude());
        parcel.writeDouble(poiLocation.getLatitude());
        parcel.writeDouble(poiLocation.getAltitude());
        parcel.writeDouble(poiCamera.getHeading());
        parcel.writeDouble(poiCamera.getTilt());
        parcel.writeDouble(poiCamera.getRange());
        parcel.writeString(poiCamera.getAltitudeMode());
        parcel.writeInt(poiCamera.getDuration());
    }

}
