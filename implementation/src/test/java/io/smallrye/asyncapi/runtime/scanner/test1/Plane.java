package io.smallrye.asyncapi.runtime.scanner.test1;

import io.smallrye.asyncapi.spec.annotations.media.Schema;

public class Plane {
    @Schema(required = true)
    private String model;

    @Schema(required = true)
    private int passengerCapacity;

    public Plane(String model, int passengerCapacity) {
        this.model = model;
        this.passengerCapacity = passengerCapacity;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }
}
