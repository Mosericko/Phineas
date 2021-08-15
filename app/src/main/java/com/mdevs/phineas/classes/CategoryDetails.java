package com.mdevs.phineas.classes;

public class CategoryDetails {
    int id;
    String name;
    int image;

    public CategoryDetails(int id,String name, int image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public int getId() {
        return id;
    }
}
