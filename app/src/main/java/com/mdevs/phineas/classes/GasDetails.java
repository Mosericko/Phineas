package com.mdevs.phineas.classes;

public class GasDetails {
    String id, tag, category, name, image, quantity, price, availability;

    public GasDetails(String id, String tag, String category, String name, String image, String quantity, String price, String availability) {
        this.id = id;
        this.tag = tag;
        this.category = category;
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.availability = availability;
    }

    public GasDetails(String id, String tag, String category, String name, String image, String quantity, String price) {
        this.id = id;
        this.tag = tag;
        this.category = category;
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getAvailability() {
        return availability;
    }
}
