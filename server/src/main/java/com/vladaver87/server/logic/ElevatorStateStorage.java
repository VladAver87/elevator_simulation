package com.vladaver87.server.logic;

import java.util.Date;
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
		ElevatorTimeState result = elevatorTimeStates.lower(new ElevatorTimeState(requestTime));
		return result.toString();
	}
	
	public int getCurrentFloor(long requestTime) {
		int result = elevatorTimeStates.lower(new ElevatorTimeState(requestTime)).getCurrentFloor();
		return result;
	}
	
	public String getCurrentState(long requestTime) {
		String result = elevatorTimeStates.lower(new ElevatorTimeState(requestTime)).getState().name();
		return result;
	}
	
	public long getLastTime() {
		return elevatorTimeStates.last().getTime();
	}
	
	public int getElevatorStopFloor() {
		return elevatorTimeStates.last().getCurrentFloor();
	}
	
	public void clearLogAfterTimeStump(){
		NavigableSet<ElevatorTimeState> result = new TreeSet<>(new ElevatorTimeStateComparator());
		result.addAll(elevatorTimeStates.headSet(new ElevatorTimeState(new Date().getTime()), true));
		elevatorTimeStates.clear();
		elevatorTimeStates.addAll(result);
	}
	
}
