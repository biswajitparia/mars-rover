package com.store.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Command {
    String roverCode;
    List<CommandEnum> commands;
}
