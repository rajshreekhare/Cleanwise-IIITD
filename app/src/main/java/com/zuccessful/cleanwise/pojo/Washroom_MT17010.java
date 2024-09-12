package com.zuccessful.cleanwise.pojo;

public class Washroom_MT17010 {
    private String id;
    private String location;
    private int floor;

    public Washroom_MT17010() {
    }

    public Washroom_MT17010(String id, String location, int floor) {
        this.id = id;
        this.location = location;
        this.floor = floor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}
