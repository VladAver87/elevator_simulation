package com.vladaver87.server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vladaver87.server.logic.ElevatorLogic;
import com.vladaver87.server.model.Comand;

@RestController
@RequestMapping("/api")
public class ElevatorRestController {
	
	@Autowired
	private ElevatorLogic elevatorLogic;
	
	
	@PutMapping("/client/comand")
	public @ResponseBody void moveElevatorToClientFloor(@RequestBody Comand comand) {
		int floor = comand.getFloor();
		elevatorLogic.handleEvent(floor);
	}
	
	@GetMapping("/client/checkCurrentFloor")
	public Integer checkCurrentFloor() {
		return elevatorLogic.getCurrentFloor();
	}
	
	@GetMapping("/client/checkCurrentState")
	public String checkCurrentState() {
		return elevatorLogic.getCurrentState().name();
	}

}
