package com.galaxy.orbitsatellitevisualizer.create.utility.model;

import android.content.Context;
import android.util.Log;

import com.galaxy.orbitsatellitevisualizer.create.utility.IJsonPacker;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.balloon.Balloon;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.movement.Movement;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.poi.POI;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.shape.Shape;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is in charge of representing a storyboard
 */
public class StoryBoard implements IJsonPacker {

    private static final String TAG_DEBUG = "StoryBoard";

    private long storyBoardId;
    private String storyBoardFileId;
    private String name;
    private List<Action> actions;

    public StoryBoard() {}



    public long getStoryBoardId() {
        return storyBoardId;
    }

    public void setStoryBoardId(long storyBoardId) {
        this.storyBoardId = storyBoardId;
    }

    public String getStoryBoardFileId() {
        return storyBoardFileId;
    }

    public void setStoryBoardFileId(String storyBoardFileId) {
        this.storyBoardFileId = storyBoardFileId;
    }

    public String getNameForExporting() {
        return name.replaceAll("[:/*\"?|<> ]", "_") + ".json";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }


    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("storyboard_id", storyBoardId);
        obj.put("name", name);

        JSONArray jsonActions = new JSONArray();
        Action action;
        for (int i = 0; i < actions.size(); i++) {
            action = actions.get(i);
            if (action instanceof POI) {
                POI poi = (POI) action;
                jsonActions.put(poi.pack());
            } else if (action instanceof Movement) {
                Movement movement = (Movement) action;
                jsonActions.put(movement.pack());
            } else if (action instanceof Balloon) {
                Balloon balloon = (Balloon) action;
                jsonActions.put(balloon.pack());
            } else if (action instanceof Shape) {
                Shape shape = (Shape) action;
                jsonActions.put(shape.pack());
            } else {
                Log.w(TAG_DEBUG, "ERROR TYPE");
            }
        }

        obj.put("jsonActions", jsonActions);

        return obj;
    }

    /**
     * unpack a normal json without images
     * @param obj  json object
     * @return StoryBoard
     * @throws JSONException error converting json to object
     */
    @Override
    public StoryBoard unpack(JSONObject obj) throws JSONException {

        storyBoardId = obj.getLong("storyboard_id");
        name = obj.getString("name");

        JSONArray jsonActions = obj.getJSONArray("jsonActions");
        List<Action> arrayActions = new ArrayList<>();
        JSONObject actionJson;
        int type;
        for (int i = 0; i < jsonActions.length(); i++) {
            actionJson = jsonActions.getJSONObject(i);
            type = actionJson.getInt("type");
            if (type == ActionIdentifier.LOCATION_ACTIVITY.getId()) {
                POI poi = new POI();
                arrayActions.add(poi.unpack(actionJson));
            } else if (type == ActionIdentifier.MOVEMENT_ACTIVITY.getId()) {
                Movement movement = new Movement();
                arrayActions.add(movement.unpack(actionJson));
            } else if (type == ActionIdentifier.BALLOON_ACTIVITY.getId()) {
                Balloon balloon = new Balloon();
                arrayActions.add(balloon.unpack(actionJson));
            } else if (type == ActionIdentifier.SHAPES_ACTIVITY.getId()) {
                Shape shape = new Shape();
                arrayActions.add(shape.unpack(actionJson));
            } else {
                Log.w(TAG_DEBUG, "ERROR TYPE");
            }
        }

        actions = arrayActions;

        return this;
    }

    /**
     * Unpack a json that comes form google drive
     * @param obj  json object
     * @throws JSONException error converting json to object
     */
    public void unpackStoryBoard(JSONObject obj, Context context) throws JSONException {

        storyBoardId = obj.getLong("storyboard_id");
        name = obj.getString("name");

        JSONArray jsonActions = obj.getJSONArray("jsonActions");
        List<Action> arrayActions = new ArrayList<>();
        JSONObject actionJson;
        int type;
        for (int i = 0; i < jsonActions.length(); i++) {
            actionJson = jsonActions.getJSONObject(i);
            type = actionJson.getInt("type");
            if (type == ActionIdentifier.LOCATION_ACTIVITY.getId()) {
                POI poi = new POI();
                arrayActions.add(poi.unpack(actionJson));
            } else if (type == ActionIdentifier.MOVEMENT_ACTIVITY.getId()) {
                Movement movement = new Movement();
                arrayActions.add(movement.unpack(actionJson));
            } else if (type == ActionIdentifier.BALLOON_ACTIVITY.getId()) {
                Balloon balloon = new Balloon();
                arrayActions.add(balloon.unpackBalloon(actionJson, context));
            } else if (type == ActionIdentifier.SHAPES_ACTIVITY.getId()) {
                Shape shape = new Shape();
                arrayActions.add(shape.unpack(actionJson));
            } else {
                Log.w(TAG_DEBUG, "ERROR TYPE");
            }
        }
        actions = arrayActions;
    }

}
