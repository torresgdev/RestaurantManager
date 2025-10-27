package com.restaurant.ez_rest.model;

public enum TableStatus {
    FREE("free"),
    OCCUPIED("ocupied");

    private final String tableStatus;

    TableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    public String getTableStatus() {
        return tableStatus;
    }
}
