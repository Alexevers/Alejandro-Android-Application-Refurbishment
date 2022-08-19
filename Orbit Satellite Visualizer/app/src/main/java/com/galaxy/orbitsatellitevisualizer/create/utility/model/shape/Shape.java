package com.galaxy.orbitsatellitevisualizer.create.utility.model.shape;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.galaxy.orbitsatellitevisualizer.create.utility.IJsonPacker;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.Action;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.ActionIdentifier;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.poi.POI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is in charge of the action shape that you can send to LG
 */
public class Shape extends Action implements IJsonPacker, Parcelable{

    public static final Creator CREATOR = new Creator() {
        public Shape createFromParcel(Parcel in) {
            return new Shape(in);
        }

        public Shape[] newArray(int size) {
            return new Shape[size];
        }
    };

    private POI poi;
    private List points;
    private boolean isExtrude;
    private int duration;


    public Shape(){
        super(ActionIdentifier.SHAPES_ACTIVITY.getId());
        poi = null;
        points = new ArrayList<>();
        isExtrude = false;
        duration = 0;
    }


    public Shape(long id, List<Point> points, boolean isExtrude, POI poi, int duration) {
       super(id, ActionIdentifier.SHAPES_ACTIVITY.getId());
       this.poi = poi;
       this.points = points;
       this.isExtrude = isExtrude;
       this.duration = duration;
    }

    public Shape(Parcel in) {
        super(in.readLong(), ActionIdentifier.SHAPES_ACTIVITY.getId());
        this.poi = in.readParcelable(POI.class.getClassLoader());
        this.points = in.readArrayList(Point.class.getClassLoader());
        this.isExtrude = in.readInt() != 0;
        this.duration = in.readInt();
    }

    public Shape(Shape shape){
        super(shape.getId(), ActionIdentifier.SHAPES_ACTIVITY.getId());
        this.poi = shape.poi;
        this.points = shape.points;
        this.isExtrude = shape.isExtrude;
        this.duration = shape.duration;
    }



    public List getPoints() {
        return points;
    }

    public Shape setPoints(List<Point> points) {
        this.points = points;
        return this;
    }

    public boolean isExtrude() {
        return isExtrude;
    }

    public Shape setExtrude(boolean extrude) {
        isExtrude = extrude;
        return this;
    }

    public POI getPoi() {
        return poi;
    }

    public Shape setPoi(POI poi) {
        this.poi = poi;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public Shape setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("shape_id", this.getId());
        obj.put("type", this.getType());
        if(poi != null) obj.put("shape_poi", poi.pack());
        JSONArray jsonPoints = new JSONArray();
        for(int i = 0; i < points.size(); i++){
            jsonPoints.put(((Point) points.get(i)).pack());
        }
        obj.put("jsonPoints", jsonPoints);
        obj.put("isExtrude", isExtrude);
        obj.put("duration", duration);

        return obj;
    }

    @Override
    public Shape unpack(JSONObject obj) throws JSONException {

        this.setId(obj.getLong("shape_id"));
        this.setType(obj.getInt("type"));

        POI newPoi = new POI();
        try{
            poi =  newPoi.unpack(obj.getJSONObject("shape_poi"));
        }catch (JSONException JSONException){
            poi = null;
        }


        JSONArray jsonPoints =  obj.getJSONArray("jsonPoints");
        List<Point> arrayPoint = new ArrayList<>();
        for(int i = 0; i < jsonPoints.length(); i++){
            Point point = new Point();
            point.unpack(jsonPoints.getJSONObject(i));
            arrayPoint.add(point);
        }
        this.points = arrayPoint;
        this.isExtrude = obj.getBoolean("isExtrude");
        this.duration = obj.getInt("duration");

        return this;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Location Name: ").append(poi.getPoiLocation().getName());
        for (int i = 0; i < points.size(); i++){
            stringBuilder.append("Point ").append(i).append(": ").append(points.get(i).toString()).append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(this.getId());
        parcel.writeParcelable(poi, flags);
        parcel.writeList(points);
        parcel.writeInt(isExtrude ? 1 : 0);
        parcel.writeInt(duration);
    }
}
