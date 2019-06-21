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
	public void callOutside(@PathVariable Integer floor) {
		elevatorLogic.callOutside(floor);
	}
	
	@PutMapping("/elevator/{floor}")
	public void callInside(@PathVariable Integer floor) {
		elevatorLogic.callInside(floor);
	}
	
	@GetMapping("/client/currentFloor")
	public Integer getElevatorStopFloor() {
		return elevatorLogic.getElevatorStopFloor();
	}
	
	@GetMapping("/client/currentState/{time}")
	public String getCurrentState(@PathVariable long time) {
		return elevatorLogic.getCurrentState(time);
	}
	
	@GetMapping("/client/maxFloors")
	public Integer getMaxFloors() {
		return elevatorLogic.getMaxFloors();
	}

}
