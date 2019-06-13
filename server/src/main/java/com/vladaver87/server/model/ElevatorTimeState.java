package com.vladaver87.server.model;

import java.util.Date;

public class ElevatorTimeState  {
	
	private long time = new Date().getTime();
	private State state;
	private int floor;
	
	
	public ElevatorTimeState(long time) {
		this.time = time;
	}

	public ElevatorTimeState(long time, State state, int floor) {
		this.time = time;
		this.state = state;
		this.floor = floor;
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
		return floor;
	}

	public void setDestinationFloor(int destinationFloor) {
		this.floor = destinationFloor;
	}

	@Override
	public String toString() {
		return "[state =" + state + ", floor =" + floor + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (time ^ (time >>> 32));
		return result;
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
