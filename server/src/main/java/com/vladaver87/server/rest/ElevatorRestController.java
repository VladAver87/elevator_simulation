package com.vladaver87.server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vladaver87.server.logic.ElevatorLogic;

@RestController
@RequestMapping("/api")
public class ElevatorRestController {
	
	@Autowired
	private ElevatorLogic elevatorLogic;
	
	
	@PutMapping("/client/{floor}")
	public void moveElevatorToClientFloor(@PathVariable String floor) {
		elevatorLogic.callOnFloor(Integer.valueOf(floor));
	}
	
	@PutMapping("/elevator/{floor}")
	public void moveElevatorToDestinationFloor(@PathVariable String floor) {
		elevatorLogic.callFromElevator(Integer.valueOf(floor));
	}
	
	@GetMapping("/client/currentFloor")
	public Integer getCurrentFloor() {
		return elevatorLogic.getCurrentFloor();
	}
	
	@GetMapping("/client/currentState/{time}")
	public String getCurrentState(@PathVariable String time) {
		return elevatorLogic.getCurrentState(Long.valueOf(time));
	}
	
	@GetMapping("/client/maxFloors")
	public Integer getMaxFloors() {
		return elevatorLogic.getMaxFloors();
	}

}
