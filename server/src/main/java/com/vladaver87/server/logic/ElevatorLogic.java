package com.vladaver87.server.logic;

import java.util.Date;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.vladaver87.server.model.ElevatorTimeState;
import com.vladaver87.server.model.State;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ElevatorLogic {
	private final Integer speed;
	private final Integer delay;
	private final Integer numberOfFloors;
	private ElevatorStateStorage elevatorStateStorage;

	@Autowired
	public ElevatorLogic(@Value("${speed}") Integer speed, @Value("${delay}") Integer delay,
			@Value("${numberOfFloors}") Integer numberOfFloors, ElevatorStateStorage elevatorStateStorage) {
		this.speed = speed * 1000;
		this.delay = delay * 1000;
		this.numberOfFloors = numberOfFloors;
		this.elevatorStateStorage = elevatorStateStorage;
	}

	@PostConstruct
	public void init() {
		elevatorStateStorage.addState(new Date().getTime(), State.STOP, 0, 1);
	}

	public synchronized void move(int destinationFloor) {
		if (destinationFloor > elevatorStateStorage.getCurrentFloor(new Date().getTime())) {
			moveUp(destinationFloor);
		} else {
			moveDown(destinationFloor);
		}
	}

	public void moveUp(int destinationFloor) {
		if (destinationFloor < elevatorStateStorage.getElevatorStopFloor()) {
			addPassingStop(destinationFloor);
		} else {
			removeStopState();
			long currentTime = new Date().getTime();
			clearElevatorPlan(currentTime);
			if (currentTime < elevatorStateStorage.getLastTime()) {
				currentTime = elevatorStateStorage.getLastTime() + delay;
			}		
			long maxTime = currentTime + delay * 2 + speed * destinationFloor;
			elevatorStateStorage.addState(currentTime, State.STARTING, destinationFloor,
					elevatorStateStorage.getElevatorStopFloor());
			elevatorStateStorage.addState(currentTime + delay, State.MOVE_UP, destinationFloor,
					elevatorStateStorage.getElevatorStopFloor());
			for (int i = elevatorStateStorage.getElevatorStopFloor(); i < destinationFloor; i++) {
				long time = currentTime + delay + speed * i;
				elevatorStateStorage.addState(time, State.MOVE_UP, destinationFloor, i + 1);
			}
			elevatorStateStorage.addState(currentTime + delay + speed * destinationFloor, State.STOPPING,
					destinationFloor, elevatorStateStorage.getElevatorStopFloor());
			elevatorStateStorage.addState(maxTime, State.STOP, 0, elevatorStateStorage.getElevatorStopFloor());
		}
		// to see the changing of plan
		elevatorStateStorage.getElevatorStatesLog().forEach(currentState -> log.debug(currentState.toString()));

	}

	public void moveDown(int destinationFloor) {
		int lastStopFloor = elevatorStateStorage.getElevatorStopFloor();
		long currentTime = new Date().getTime();
		if (destinationFloor > lastStopFloor && destinationFloor < elevatorStateStorage.getCurrentFloor(currentTime)) {
			addPassingStop(destinationFloor);
		} else {
			clearElevatorPlan(currentTime);
			removeStopState();
			if (currentTime < elevatorStateStorage.getLastTime()) {
				currentTime = elevatorStateStorage.getLastTime() + delay;
			}
			long maxTime = currentTime + delay * 2 + speed * lastStopFloor;
			elevatorStateStorage.addState(currentTime, State.STARTING, destinationFloor,
					elevatorStateStorage.getElevatorStopFloor());
			elevatorStateStorage.addState(currentTime + delay, State.MOVE_DOWN, destinationFloor,
					elevatorStateStorage.getElevatorStopFloor());
			for (int i = elevatorStateStorage.getElevatorStopFloor(); i > destinationFloor; i--) {
				long time = currentTime + delay + speed * (lastStopFloor - i + 1);
				elevatorStateStorage.addState(time, State.MOVE_DOWN, destinationFloor, i - 1);
			}
			elevatorStateStorage.addState(currentTime + delay + speed * lastStopFloor, State.STOPPING,
					destinationFloor, elevatorStateStorage.getElevatorStopFloor());
			elevatorStateStorage.addState(maxTime, State.STOP, 0, elevatorStateStorage.getElevatorStopFloor());
		}
		// to see the changing of plan
		elevatorStateStorage.getElevatorStatesLog().forEach(currentState -> log.debug(currentState.toString()));
	}

	private void addPassingStop(int destinationFloor) {
		Iterator<ElevatorTimeState> iterator = elevatorStateStorage.getElevatorStatesLog().descendingIterator();
		while (iterator.hasNext()) {
			ElevatorTimeState currentElevatorPosition = iterator.next();
			if (currentElevatorPosition.getCurrentFloor() == destinationFloor) {
				currentElevatorPosition.setState(State.STOPPING);
				currentElevatorPosition.setCurrentFloor(destinationFloor);
				currentElevatorPosition.setDestinationFloor(destinationFloor);
				elevatorStateStorage.getElevatorStatesLog()
						.add(new ElevatorTimeState(currentElevatorPosition.getTime() + delay, State.STARTING,
								destinationFloor, destinationFloor));
				break;
			}
			currentElevatorPosition.setTime(currentElevatorPosition.getTime() + delay);
		}
	}
	
	private void clearElevatorPlan(long currentTime) {
		elevatorStateStorage.getElevatorStatesLog().removeIf(currentPosition -> currentPosition.getTime() < currentTime && 
				currentPosition != elevatorStateStorage.getElevatorStatesLog().last());
	}
	
	private void removeStopState() {
		if (elevatorStateStorage.getElevatorStatesLog().size() > 2) {
		elevatorStateStorage.getElevatorStatesLog().remove(elevatorStateStorage.getElevatorStatesLog().last());
		}
	}

	public int getElevatorStopFloor() {

		return elevatorStateStorage.getElevatorStopFloor();
	}

	public String getCurrentState(long requestTime) {

		return elevatorStateStorage.getState(requestTime);
	}

	public int getMaxFloors() {

		return numberOfFloors;
	}

	public void callOutside(int floor) {
		log.info("Arrival floor # {} is added to elevator plan", floor);
		move(floor);
	}

	public void callInside(int floor) {
		log.info("Destination floor # {} is added to elevator plan", floor);
		move(floor);
	}

}
