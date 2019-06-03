package com.vladaver87.server.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Component
public class Elevator {
	
	@Value("${speed}")
	private Integer speed;
	@Value("${delay}")
	private Integer delay;
	@Value("${numberOfFloors}")
	private Integer numberOfFloors;
	private Integer currentFloor = 1;
	private State state = State.STOP;

}
