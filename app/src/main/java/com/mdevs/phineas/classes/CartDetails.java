package com.mdevs.phineas.classes;

public class CartDetails {
    String id, tag, name, image, quantity, price;

    public CartDetails(String id, String tag, String name, String image, String quantity, String price) {
        this.id = id;
        this.tag = tag;
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
    }



    public CartDetails() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
