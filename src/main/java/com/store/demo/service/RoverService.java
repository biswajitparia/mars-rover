package com.store.demo.service;

import com.store.demo.exception.RoverException;
import com.store.demo.model.Command;
import com.store.demo.model.CommandEnum;
import com.store.demo.model.DirectionEnum;
import com.store.demo.model.Rover;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RoverService {

    private static final Map<String, Rover> roverRepository = new ConcurrentHashMap<>();

    public ResponseEntity<Boolean> createRover(Rover rover) {
        try {
            Optional.ofNullable(rover).orElseThrow(() -> new RoverException("No Rover details are present to create a new Rover!"));
            Rover exisitngRover = roverRepository.get(rover.getRoverCode());
            Optional.ofNullable(exisitngRover).ifPresent(e -> new RoverException("Rover is already present!"));
            roverRepository.put(rover.getRoverCode(), rover);
            log.info("Total rovers are {}", roverRepository.keySet());
        } catch (Exception ex) {
            log.error("Rover can not be added!", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Boolean.FALSE);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Boolean.TRUE);

    }

    public ResponseEntity<Rover> getCurrentPosition(String roverCode) {
        Rover exisitngRover;
        try {
            exisitngRover = roverRepository.get(roverCode);
            Optional.ofNullable(exisitngRover).orElseThrow(() -> new RoverException("Rover is not present!"));
        } catch (Exception ex) {
            log.error("Rover {} can not be retrieved!. Error {}", roverCode, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(exisitngRover);
    }


    public ResponseEntity<Boolean> moveRover(Command commandDetail) {
        try {
            performMove(commandDetail);
        } catch (Exception ex) {
            log.error("Rover can not be moved!", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Boolean.FALSE);
        }
        return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
    }

    private void performMove(Command commandDetail) {

        List<CommandEnum> commands = commandDetail.getCommands();
        for (CommandEnum command : commands) {
            if (!(CommandEnum.r.equals(command) || CommandEnum.l.equals(command) || CommandEnum.f.equals(command) || CommandEnum.b.equals(command))) {
                log.info("Not acceptable command {}. Ignored! ", command);
            }

            Rover rover = roverRepository.get(commandDetail.getRoverCode());
            DirectionEnum direction = rover.getDirection();
            Integer positionX = rover.getPositionX();
            Integer positionY = rover.getPositionY();

            if (DirectionEnum.N.equals(direction)) {
                switch (command) {
                    case f:
                        positionY = positionY + 1;
                        break;
                    case b:
                        positionY = positionY - 1;
                        break;
                }
            }

            if (DirectionEnum.S.equals(direction)) {
                switch (command) {
                    case f:
                        positionY = positionY - 1;
                        break;
                    case b:
                        positionY = positionY + 1;
                        break;
                }
            }

            if (DirectionEnum.E.equals(direction)) {
                switch (command) {
                    case f:
                        positionX = positionX + 1;
                        break;
                    case b:
                        positionX = positionX - 1;
                        break;
                }
            }

            if (DirectionEnum.W.equals(direction)) {
                switch (command) {
                    case f:
                        positionX = positionX - 1;
                        break;
                    case b:
                        positionX = positionX + 1;
                        break;
                }
            }

            if (CommandEnum.r.equals(command)) {
                switch (direction) {
                    case N:
                        direction = DirectionEnum.E;
                        break;
                    case E:
                        direction = DirectionEnum.S;
                        break;
                    case S:
                        direction = DirectionEnum.W;
                        break;
                    case W:
                        direction = DirectionEnum.N;
                        break;
                }
            }

            if (CommandEnum.l.equals(command)) {
                switch (direction) {
                    case N:
                        direction = DirectionEnum.W;
                        break;
                    case W:
                        direction = DirectionEnum.S;
                        break;
                    case S:
                        direction = DirectionEnum.E;
                        break;
                    case E:
                        direction = DirectionEnum.N;
                        break;
                }
            }

            if (collide(rover.getRoverCode(), positionX, positionY)) {
                log.info("This command {} will cause a collide! No further command will be processed", command);
                throw new RoverException("STOP! Current command will cause a collide. No further command will be processed");
            } else {
                log.info("Command {} has been executed!", command);
                rover.setDirection(direction);
                rover.setPositionX(positionX);
                rover.setPositionY(positionY);
                log.info("Current parameters of rover {} - {}", rover.getRoverCode(), rover);
                roverRepository.put(rover.getRoverCode(), rover);
            }
        }
    }

    private Boolean collide(String roverCode, Integer positionX, Integer positionY) {
        Collection<Rover> allRovers = roverRepository.values();
        for (Rover rover : allRovers) {
            if ((!roverCode.equals(rover.getRoverCode())) && (rover.getPositionX() == positionX && rover.getPositionY() == positionY)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
