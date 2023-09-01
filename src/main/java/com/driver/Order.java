package com.driver;

public class Order {



    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM

        String[] parts = deliveryTime.split(":");
        int time=0;
        time+=Integer.parseInt(parts[0])*60;
        time+=Integer.parseInt(parts[1]);
        this.id=id;
        this.deliveryTime=time;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
