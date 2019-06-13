package com.vladaver87.server.model;


public class Elevator {
	
	private Integer currentFloor = 1;
	private State state = State.STOP;
	
	
	public Integer getCurrentFloor() {
		return currentFloor;
	}
	public void setCurrentFloor(Integer currentFloor) {
		this.currentFloor = currentFloor;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}

}
