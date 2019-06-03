package com.vladaver87.server.logic;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vladaver87.server.model.Elevator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Component
public class ElevatorLogic {
	
	private Logger LOGGER = LoggerFactory.getLogger(ElevatorLogic.class);
	
	@Autowired
	private Elevator elevator;
	
	@PostConstruct
	public void setPersonProppperties() {

	}
	
	public void moveElevatorToClientFloor(int floor) {
		
		
	}
	
	public int getCurrentFloor() {
		
		return elevator.getCurrentFloor();
	}

}
