package com.architech.architech.model;

public class Favourite {
    String userId;
    String floorPlanId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFloorPlanId() {
        return floorPlanId;
    }

    public void setFloorPlanId(String floorPlanId) {
        this.floorPlanId = floorPlanId;
    }

    public Favourite(String userId, String floorPlanId) {
        this.userId = userId;
        this.floorPlanId = floorPlanId;
    }
    public Favourite() {

    }
}
