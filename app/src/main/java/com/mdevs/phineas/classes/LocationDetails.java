package com.mdevs.phineas.classes;

public class LocationDetails {
    String id, street, building, phone;

    public LocationDetails(String id, String street, String building, String phone) {
        this.id = id;
        this.street = street;
        this.building = building;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getBuilding() {
        return building;
    }

    public String getPhone() {
        return phone;
    }
}
