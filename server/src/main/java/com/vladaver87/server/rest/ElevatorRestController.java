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
	
	@GetMapping("/client/checkCurrentFloor")
	public Integer checkCurrentFloor() {
		return elevatorLogic.getCurrentFloor();
	}
	
	@GetMapping("/client/checkCurrentState")
	public String checkCurrentState() {
		return elevatorLogic.getCurrentState().name();
	}
	
	@GetMapping("/client/checkMaxFloors")
	public Integer checkMaxFloors() {
		return elevatorLogic.getMaxFloors();
	}

}
