package com.galaxy.orbitsatellitevisualizer.create.utility.model;

/**
 * It is the class base of the action
 */
public abstract class Action {

    private long id;
    private int type;

    public Action(int type){
        this.type = type;
    }

    public Action(long id, int type){
        this.id = id;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
