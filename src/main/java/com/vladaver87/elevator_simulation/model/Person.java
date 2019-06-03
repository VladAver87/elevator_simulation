package com.vladaver87.elevator_simulation.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Person {
	
	private Integer arrivalFloor;
	private Integer[] destinationFloors;
	
	public Person(Integer arrivalFloor) {
		this.arrivalFloor = arrivalFloor;

	}

}
