package com.arielfriedman.arminesweeperproject.model;

public class Item {
    private String id;
    private String desc;
    private int price;

    public Item(String id, String desc, int price) {
        this.id = id;
        this.desc = desc;
        this.price = price;
    }

    public Item() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", desc='" + desc + '\'' +
                ", price=" + price +
                '}';
    }
}


