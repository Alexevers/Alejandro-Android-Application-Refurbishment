package com.galaxy.orbitsatellitevisualizer.create.utility.model.balloon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

//import com.google.api.client.util.Base64;
import com.galaxy.orbitsatellitevisualizer.create.utility.IJsonPacker;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.Action;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.ActionIdentifier;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.poi.POI;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * This class is in charge of creating a placemark in current location
 * The class has a poi with the location information
 * making the class use composition over inheritance
 */
public class Balloon extends Action implements IJsonPacker, Parcelable {

    private static final String TAG_DEBUG = "Balloon";

    public static final Creator CREATOR = new Creator() {
        public Balloon createFromParcel(Parcel in) {
            return new Balloon(in);
        }

        public Balloon[] newArray(int size) {
            return new Balloon[size];
        }
    };

    private POI poi;
    private String description;
    private Uri imageUri;
    private String imagePath;
    private String videoPath;
    private int duration;

    /**
     * Empty Constructor
     */
    public Balloon() {
        super(ActionIdentifier.BALLOON_ACTIVITY.getId());
    }

    public Balloon(long id, POI poi, String description, Uri imageUri, String imagePath, String videoPath, int duration) {
        super(id, ActionIdentifier.BALLOON_ACTIVITY.getId());
        this.poi = poi;
        this.description = description;
        this.imageUri = imageUri;
        this.imagePath = imagePath;
        this.videoPath = videoPath;
        this.duration = duration;
    }

    public Balloon(Parcel in) {
        super(in.readLong(), ActionIdentifier.BALLOON_ACTIVITY.getId());
        this.poi = in.readParcelable(POI.class.getClassLoader());
        this.description = in.readString();
        this.imageUri = in.readParcelable(Uri.class.getClassLoader());
        this.imagePath = in.readString();
        this.videoPath = in.readString();
        this.duration = in.readInt();
    }

    public Balloon(Balloon balloon) {
        super(balloon.getId(), ActionIdentifier.BALLOON_ACTIVITY.getId());
        this.poi = balloon.poi;
        this.description = balloon.description;
        this.imageUri = balloon.imageUri;
        this.imagePath = balloon.imagePath;
        this.videoPath = balloon.videoPath;
        this.duration = balloon.duration;
    }


    public POI getPoi() {
        return poi;
    }

    public Balloon setPoi(POI poi) {
        this.poi = poi;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Balloon setDescription(String description) {
        this.description = description;
        return this;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public Balloon setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
        return this;
    }


    public String getImagePath() {
        return imagePath;
    }

    public Balloon setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public Balloon setVideoPath(String videoPath) {
        this.videoPath = videoPath;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public Balloon setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("balloon_id", this.getId());
        obj.put("type", this.getType());
        if(poi != null) obj.put("place_mark_poi", poi.pack());
        obj.put("description", description);
        obj.put("image_uri", imageUri != null ? imageUri.toString(): "");
        obj.put("image_path", imagePath != null ? imagePath: "");

        String encodedImage = "";
        if(imagePath != null){
            encodedImage = encodeFileToBase64Binary();
        }
        if(encodedImage == null) encodedImage = "";
        obj.put("encodedImage", encodedImage);


        obj.put("video_path", videoPath != null ? videoPath: "");
        obj.put("duration", duration);

        return obj;
    }

    private String encodeFileToBase64Binary(){
        File imageFile = new File(imagePath);
        String encodedFile = null;
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            Bitmap bm = BitmapFactory.decodeStream(fis);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100 , out);
            byte[] bytes = out.toByteArray();
            encodedFile = new String(Base64.encode(bytes,0), StandardCharsets.UTF_8); //To check! Not the same library
        } catch (IOException e) {
            Log.w(TAG_DEBUG, "ERROR: " + e.getMessage());
        }
        return encodedFile;
    }

    @Override
    public Balloon unpack(JSONObject obj) throws JSONException {

        this.setId(obj.getLong("balloon_id"));
        this.setType(obj.getInt("type"));

        POI newPoi = new POI();
        try{
            poi =  newPoi.unpack(obj.getJSONObject("place_mark_poi"));
        }catch (JSONException JSONException){
            poi = null;
        }

        description = obj.getString("description");
        String uri = obj.getString("image_uri");
        imageUri = !uri.equals("") ?  Uri.parse(obj.getString("image_uri")):null;
        imagePath = obj.getString("image_path");
        videoPath = obj.getString("video_path");
        duration = obj.getInt("duration");

        return this;
    }

    public Balloon unpackBalloon(JSONObject obj, Context context) throws JSONException {

        this.setId(obj.getLong("balloon_id"));
        this.setType(obj.getInt("type"));

        POI newPoi = new POI();
        try{
            poi =  newPoi.unpack(obj.getJSONObject("place_mark_poi"));
        }catch (JSONException JSONException){
            poi = null;
        }

        description = obj.getString("description");

        String uri = obj.getString("image_uri");
        imageUri = !uri.equals("") ?  Uri.parse(obj.getString("image_uri")):null;
        imagePath = obj.getString("image_path");

        String encodedImage = obj.getString("encodedImage");
        if(!encodedImage.equals("")){
            Log.w(TAG_DEBUG, "Encoded Image: " + encodedImage);
            try{
                byte[] imageByte = Base64.decode(encodedImage,1);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

                String[] route = imagePath.split("/");
                String fileName = route[route.length - 1];
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                OutputStream fOut;

                File file = new File(path, fileName);
                Log.w(TAG_DEBUG, "PATH VERIFY: " + file.getAbsolutePath());

                if(!file.exists()){
                    Log.w(TAG_DEBUG, "FILE DOESN'T EXIST");
                    // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                    file = new File(path, fileName);

                    fOut = new FileOutputStream(file);

                    decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                    String url = MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
                    imageUri = Uri.parse(url);
                    imagePath = file.getAbsolutePath();

                } else{
                    MediaScannerConnection.scanFile(context, new String[] { file.getAbsolutePath() }, null,
                            (pathImage, uriImage) -> {
                                imageUri = uriImage;
                                Log.w(TAG_DEBUG, "URI: " + imageUri);
                            });
                    imagePath = file.getAbsolutePath();
                }
            }catch (Exception e){
                Log.w(TAG_DEBUG, "ERROR: " + e.getMessage());
            }
        }


        videoPath = obj.getString("video_path");
        duration = obj.getInt("duration");

        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "Location Name: " + this.poi.getPoiLocation().getName() + " Image Uri: " + this.imageUri.toString()  + " Video URL: " +  this.videoPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(this.getId());
        parcel.writeParcelable(poi, flags);
        parcel.writeString(description);
        parcel.writeParcelable(imageUri, flags);
        parcel.writeString(imagePath);
        parcel.writeString(videoPath);
        parcel.writeInt(duration);
    }

}
