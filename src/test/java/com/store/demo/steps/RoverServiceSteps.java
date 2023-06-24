package com.store.demo.steps;

import com.store.demo.model.Command;
import com.store.demo.model.CommandEnum;
import com.store.demo.model.DirectionEnum;
import com.store.demo.model.Rover;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoverServiceSteps extends CucumberTest {
    @When("Rover {string} lands in Mars at {int},{int},{string}")
    public void rover_lands_in_mars_at(String roverCode, Integer x, Integer y, String direction) {
        Rover rover = Rover.builder()
                .roverCode(roverCode)
                .direction(DirectionEnum.valueOf(direction))
                .positionX(x)
                .positionY(y)
                .build();
        HttpEntity httpEntity = new HttpEntity<>(rover, new HttpHeaders());
        ParameterizedTypeReference<Boolean> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<Boolean> response =
                testRestTemplate.exchange(baseUrl + port + "/rovers", HttpMethod.POST, httpEntity, responseType);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(response.getBody(), Boolean.TRUE);

    }

    @Then("Remote operator moves rover {string} with command {string}")
    public void remote_operator_moves_rover_with_command(String roverCode, String commands) {
        List<CommandEnum> commandEnumList = Arrays.stream(commands.split(",")).map(e -> CommandEnum.valueOf(e)).collect(Collectors.toList());
        Command command = Command.builder()
                .roverCode(roverCode)
                .commands(commandEnumList)
                .build();

        HttpEntity httpEntity = new HttpEntity<>(command, new HttpHeaders());
        ParameterizedTypeReference<Boolean> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<Boolean> response =
                testRestTemplate.exchange(baseUrl + port + "/rovers/move", HttpMethod.POST, httpEntity, responseType);
        assertEquals(response.getBody(), Boolean.FALSE);
    }


    @Then("Remote operator retrieves rover {string}. Current position is {int}, {int}, {string}")
    public void remote_operator_retrieves_rover_current_position_is(String roverCode, Integer x, Integer y, String direction) {
        HttpEntity httpEntity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Rover> response =
                testRestTemplate.exchange(baseUrl + port + "/rovers/" + roverCode, HttpMethod.GET, httpEntity, Rover.class);
        assertEquals(response.getBody().getRoverCode(), roverCode);
        assertEquals(response.getBody().getPositionX(), x);
        assertEquals(response.getBody().getPositionY(), y);
        assertEquals(response.getBody().getDirection(), DirectionEnum.valueOf(direction));
    }

}
