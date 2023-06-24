package com.store.demo.controller;

import com.store.demo.model.Command;
import com.store.demo.model.Rover;
import com.store.demo.service.RoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoverController {

    @Autowired
    private RoverService roverService;

    @PostMapping("/rovers")
    public ResponseEntity<Boolean> createRover(@RequestBody Rover rover) {
        return roverService.createRover(rover);
    }

    @PostMapping("/rovers/move")
    public ResponseEntity<Boolean> moveRover(@RequestBody Command command) {
        return roverService.moveRover(command);
    }

    @GetMapping("/rovers/{roverCode}")
    public ResponseEntity<Rover> getCurrentPosition(@PathVariable String roverCode) {
        return roverService.getCurrentPosition(roverCode);
    }
}
