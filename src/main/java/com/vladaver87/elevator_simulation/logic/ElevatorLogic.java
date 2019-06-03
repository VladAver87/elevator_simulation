package com.vladaver87.elevator_simulation.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vladaver87.elevator_simulation.model.Elevator;
import com.vladaver87.elevator_simulation.model.Person;


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

}
