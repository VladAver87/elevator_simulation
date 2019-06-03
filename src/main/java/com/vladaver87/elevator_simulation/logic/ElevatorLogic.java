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
	private List<Person> persons = new ArrayList<>();
	
	@PostConstruct
	public void setPersonProppperties() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("on what floor are you on?");
		int arrivalFloor = scanner.nextInt();
		Person person = new Person(arrivalFloor);
		persons.add(person);
		scanner.close();
		LOGGER.info("Person at the {} floor", arrivalFloor);
	}
	
	public void moveUp() {
		
		
	}

}
