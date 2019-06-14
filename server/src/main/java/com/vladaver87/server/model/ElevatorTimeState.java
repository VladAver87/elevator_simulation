package com.vladaver87.server.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ElevatorTimeState  {
	
	private long time = new Date().getTime();
	private State state = State.STOP;
	private int destinationFloor;
	private int currentFloor = 1;
	private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
	
	public ElevatorTimeState(long time) {
		this.time = time;
	}

	public ElevatorTimeState(long time, State state, int destinationFloor, int currentFloor) {
		this.time = time;
		this.state = state;
		this.destinationFloor = destinationFloor;
		this.currentFloor = currentFloor;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getDestinationFloor() {
		return destinationFloor;
	}

	public void setDestinationFloor(int destinationFloor) {
		this.destinationFloor = destinationFloor;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}
	
	

	@Override
	public String toString() {
		return "[time = " + format.format(time) + ", state = " + state + ", destination floor = " + destinationFloor
				+ ", current floor = " + currentFloor + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElevatorTimeState other = (ElevatorTimeState) obj;
		if (time != other.time)
			return false;
		return true;
	}
	
}
