package com.store.demo.service;

import com.store.demo.model.Command;
import com.store.demo.model.CommandEnum;
import com.store.demo.model.DirectionEnum;
import com.store.demo.model.Rover;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoverServiceTest {
    private final RoverService roverService = new RoverService();

    @Test
    public void create_rover_when_typical() {
        String roverCodeR1 = "R1";
        Rover rover1 = Rover.builder()
                .roverCode(roverCodeR1)
                .direction(DirectionEnum.E)
                .positionX(13)
                .positionY(14)
                .build();
        ResponseEntity<Boolean> response = roverService.createRover(rover1);
        assertEquals(response.getBody(), Boolean.TRUE);
    }

    @Test
    public void create_rover_when_no_rover_details_present() {
        Rover rover = null;
        ResponseEntity<Boolean> response = roverService.createRover(rover);
        assertEquals(response.getBody(), Boolean.FALSE);
    }

    @Test
    public void get_current_position_when_typical() {
        String roverCodeR2 = "R2";
        String roverCodeR3 = "R3";
        Rover rover2 = Rover.builder()
                .roverCode(roverCodeR2)
                .direction(DirectionEnum.N)
                .positionX(23)
                .positionY(24)
                .build();

        Rover rover3 = Rover.builder()
                .roverCode(roverCodeR3)
                .direction(DirectionEnum.S)
                .positionX(23)
                .positionY(25)
                .build();
        roverService.createRover(rover2);
        roverService.createRover(rover3);

        ResponseEntity<Rover> response = roverService.getCurrentPosition(roverCodeR2);
        assertEquals(response.getBody().getPositionY(), 24);
    }

    @Test
    public void move_rover_when_typical() {
        String roverCodeR4 = "R4";
        String commandString = "r,r,f,f,f";
        Rover rover4 = Rover.builder()
                .roverCode(roverCodeR4)
                .direction(DirectionEnum.N)
                .positionX(33)
                .positionY(34)
                .build();
        roverService.createRover(rover4);

        List<CommandEnum> commandEnumList = Arrays.stream(commandString.split(",")).map(e -> CommandEnum.valueOf(e)).collect(Collectors.toList());
        Command command = Command.builder()
                .roverCode(roverCodeR4)
                .commands(commandEnumList)
                .build();
        //move rover with command - "r,r,f,f,f";
        roverService.moveRover(command);

        ResponseEntity<Rover> response = roverService.getCurrentPosition(roverCodeR4);
        assertEquals(response.getBody().getPositionX(), 33);
        assertEquals(response.getBody().getPositionY(), 31);
        assertEquals(response.getBody().getDirection(), DirectionEnum.S);
    }

    @Test
    public void move_rover_when_collision_happens() {
        String roverCodeR5 = "R5";
        String roverCodeR6 = "R6";
        String commandString = "r,r,l,r,f,b,f,f,f";
        Rover rover5 = Rover.builder()
                .roverCode(roverCodeR5)
                .direction(DirectionEnum.N)
                .positionX(3)
                .positionY(4)
                .build();

        Rover rover6 = Rover.builder()
                .roverCode(roverCodeR6)
                .direction(DirectionEnum.S)
                .positionX(3)
                .positionY(2)
                .build();
        //Create two rovers
        roverService.createRover(rover5);
        roverService.createRover(rover6);

        //Move rover R4 with command - "r,r,l,r,f,f,f"
        List<CommandEnum> commandEnumList = Arrays.stream(commandString.split(",")).map(e -> CommandEnum.valueOf(e)).collect(Collectors.toList());
        Command command = Command.builder()
                .roverCode(roverCodeR5)
                .commands(commandEnumList)
                .build();
        roverService.moveRover(command);

        //Get current position of Rover-R4
        ResponseEntity<Rover> response = roverService.getCurrentPosition(roverCodeR5);

        //Collision happened during move
        //Check expected current position of Rover-R4. It will stop at (3,3) to avoid collision with Rover-R5 standing at (3,2)
        assertEquals(response.getBody().getPositionX(), 3);
        assertEquals(response.getBody().getPositionY(), 3);
        assertEquals(response.getBody().getDirection(), DirectionEnum.S);
    }
}