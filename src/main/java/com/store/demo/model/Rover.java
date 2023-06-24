package com.store.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Rover {
    String roverCode;
    Integer positionX;
    Integer positionY;
    DirectionEnum direction;

    @Override
    public String toString() {
        return "Rover{" +
                "roverCode='" + roverCode + '\'' +
                ", positionX=" + positionX +
                ", positionY=" + positionY +
                ", direction=" + direction +
                '}';
    }
}
