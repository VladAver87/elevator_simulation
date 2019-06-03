package com.vladaver87.elevator_simulation.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vladaver87.elevator_simulation.logic.ElevatorLogic;

@RestController
@RequestMapping("/api")
public class ElevatorRestController {
	
	@Autowired
	private ElevatorLogic elevatorLogic;
	
	@PutMapping("/client/{floorNo}")
	public void moveElevatorToClientFloor(@PathVariable int floorNo) {
		elevatorLogic.moveToClientFloor(floorNo);
	}

}
