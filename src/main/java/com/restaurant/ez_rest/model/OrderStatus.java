package com.restaurant.ez_rest.model;

public enum OrderStatus {

    OPEN("open"),
    CLOSED("closed");


    private final String orderStatus;

    OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
