package com.vladaver87.server.logic;

import java.util.NavigableSet;
import java.util.TreeSet;
import com.vladaver87.server.model.ElevatorTimeState;
import com.vladaver87.server.model.State;
import com.vladaver87.server.util.ElevatorTimeStateComparator;
import org.springframework.stereotype.Service;


@Service
public class ElevatorStateStorage {
	
	private NavigableSet<ElevatorTimeState> elevatorTimeStates = new TreeSet<>(new ElevatorTimeStateComparator());

	public NavigableSet<ElevatorTimeState> getElevatorStatesLog() {
		return elevatorTimeStates;
	}
	
	public void addState(long time, State state, int destinationFloor, int currentFloor) {
		ElevatorTimeState elevatorTimeState = new ElevatorTimeState(time, state, destinationFloor, currentFloor);
		elevatorTimeStates.add(elevatorTimeState);
	}
	
	public String getState(long requestTime) {
		ElevatorTimeState stateAtRequestTime = elevatorTimeStates.lower(new ElevatorTimeState(requestTime));
		if (stateAtRequestTime.getState().equals(State.STOP)) {
			return stateAtRequestTime.showStateIfElevatorStop();
		} 
		return stateAtRequestTime.toString();
	}
	
	public int getCurrentFloor(long requestTime) {
		
		return elevatorTimeStates.lower(new ElevatorTimeState(requestTime)).getCurrentFloor();
	}
	
	public long getLastTime() {
		return elevatorTimeStates.last().getTime();
	}
	
	public int getElevatorStopFloor() {
		return elevatorTimeStates.last().getCurrentFloor();
	}
	
}
