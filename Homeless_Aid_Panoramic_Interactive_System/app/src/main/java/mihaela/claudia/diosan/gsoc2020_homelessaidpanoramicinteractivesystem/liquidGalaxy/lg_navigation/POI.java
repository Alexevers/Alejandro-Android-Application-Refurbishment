package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class POI implements IJsonPacker, Parcelable {

    public static final Creator CREATOR = new Creator() {
        public POI createFromParcel(Parcel in) {
            return new POI(in);
        }

        public POI[] newArray(int size) {
            return new POI[size];
        }
    };
    private long id;
    private String name;
    private String visited_place;
    private double latitude;
    private double longitude;

    private double altitude;
    private double heading;
    private double tilt;
    private double range;
    private String altitudeMode;
    private boolean hidden;
    private int categoryId;

    public POI() {
    }

    public POI(long id, String name, String visited_place, double longitude, double latitude, double altitude, double heading, double tilt, double range, String altitudeMode, boolean hidden, int categoryId) {
        this.id = id;
        this.name = name;
        this.visited_place = visited_place;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.heading = heading;
        this.tilt = tilt;
        this.range = range;
        this.altitudeMode = altitudeMode;
        this.hidden = hidden;
        this.categoryId = categoryId;
    }

    public POI(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.visited_place = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.altitude = in.readDouble();
        this.heading = in.readDouble();
        this.tilt = in.readDouble();
        this.range = in.readDouble();
        this.altitudeMode = in.readString();
        this.hidden = in.readInt() != 0;
        this.categoryId = in.readInt();
    }

    public POI(POI poi) {
        this.id = poi.id;
        this.name = poi.name;
        this.visited_place = poi.visited_place;
        this.longitude = poi.longitude;
        this.latitude = poi.latitude;
        this.altitude = poi.altitude;
        this.heading = poi.heading;
        this.tilt = poi.tilt;
        this.range = poi.range;
        this.altitudeMode = poi.altitudeMode;
        this.hidden = poi.hidden;
        this.categoryId = poi.categoryId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public POI setName(String name) {
        this.name = name;
        return this;
    }

    public String getVisited_place() {
        return visited_place;
    }

    public POI setVisited_place(String visited_place) {
        this.visited_place = visited_place;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public POI setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public POI setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getAltitude() {
        return altitude;
    }

    public POI setAltitude(double altitude) {
        this.altitude = altitude;
        return this;
    }

    public double getHeading() {
        return heading;
    }

    public POI setHeading(double heading) {
        this.heading = heading;
        return this;
    }

    public double getTilt() {
        return tilt;
    }

    public POI setTilt(double tilt) {
        this.tilt = tilt;
        return this;
    }

    public double getRange() {
        return range;
    }

    public POI setRange(double range) {
        this.range = range;
        return this;
    }

    public String getAltitudeMode() {
        return altitudeMode;
    }

    public POI setAltitudeMode(String altitudeMode) {
        this.altitudeMode = altitudeMode;
        return this;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("id", id);
        obj.put("name", name);
        obj.put("visited_place", visited_place);
        obj.put("longitude", longitude);
        obj.put("latitude", latitude);
        obj.put("altitude", altitude);
        obj.put("heading", heading);
        obj.put("tilt", tilt);
        obj.put("range", range);
        obj.put("altitudeMode", altitudeMode);
        obj.put("hidden", hidden);
        obj.put("categoryId", categoryId);

        return obj;
    }

    @Override
    public POI unpack(JSONObject obj) throws JSONException {
        id = obj.getLong("id");
        name = obj.getString("name");
        visited_place = obj.getString("visited_place");
        longitude = obj.getDouble("longitude");
        latitude = obj.getDouble("latitude");
        altitude = obj.getDouble("altitude");
        heading = obj.getDouble("heading");
        tilt = obj.getDouble("tilt");
        range = obj.getDouble("range");
        altitudeMode = obj.getString("altitudeMode");
        hidden = obj.getBoolean("hidden");
        categoryId = obj.getInt("categoryId");

        return this;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(visited_place);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
        parcel.writeDouble(altitude);
        parcel.writeDouble(heading);
        parcel.writeDouble(tilt);
        parcel.writeDouble(range);
        parcel.writeString(altitudeMode);
        parcel.writeInt(hidden ? 1 : 0);
        parcel.writeInt(categoryId);
    }

   /* public static POI getPOIByIDFromDB(int id) {
        Cursor c = POIsContract.POIEntry.getPoiByID(id);

        if (c.moveToNext()) {
            POI poi = new POI(id, c.getString(1), c.getString(2), c.getDouble(3), c.getDouble(4), c.getDouble(5), c.getDouble(6), c.getDouble(7), c.getDouble(8), c.getString(9), c.getInt(10) != 0, c.getInt(11));
            c.close();
            return poi;
        }
        c.close();
        return null;
    }*/
}
