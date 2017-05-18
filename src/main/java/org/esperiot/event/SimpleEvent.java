package org.esperiot.event;

public class SimpleEvent {
    private String name;
    private double value;

    public SimpleEvent() {
    }

    public SimpleEvent(String itemName, double price) {
        this.name = itemName;
        this.value = price;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }
}
