package com.mdevs.phineas.classes;

public class Address {
    String streetName, building, phoneNumber;

    public Address(String streetName, String building, String phoneNumber) {
        this.streetName = streetName;
        this.building = building;
        this.phoneNumber = phoneNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getBuilding() {
        return building;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}