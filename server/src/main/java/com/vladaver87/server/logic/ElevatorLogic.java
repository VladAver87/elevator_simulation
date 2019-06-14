package com.vladaver87.server.logic;

import java.util.ArrayDeque;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.vladaver87.server.model.Elevator;
import com.vladaver87.server.model.State;

@Component
public class ElevatorLogic {
	private final Integer speed;
	private final Integer delay;
	private final Integer numberOfFloors;
	private final Logger LOGGER = LoggerFactory.getLogger(ElevatorLogic.class);
	private final ArrayDeque<Integer> insideCallsQueue = new ArrayDeque<>();
	private final ArrayDeque<Integer> outsideCallsQueue = new ArrayDeque<>();
	private Elevator elevator;
	private ElevatorStateStorage elevatorStateStorage;

	@Autowired
	public ElevatorLogic(@Value("${speed}") Integer speed, @Value("${delay}") Integer delay,
			@Value("${numberOfFloors}") Integer numberOfFloors, ElevatorStateStorage elevatorStateStorage) {
		this.speed = speed;
		this.delay = delay;
		this.numberOfFloors = numberOfFloors;
		this.elevatorStateStorage = elevatorStateStorage;
		elevator = new Elevator();
	}

	@PostConstruct
	public void init() {
		elevatorStateStorage.addState(new Date().getTime(), elevator.getState(), 0, elevator.getCurrentFloor());
	}

	public void moveElevatorToClientFloor() {

		while (!insideCallsQueue.isEmpty() || !outsideCallsQueue.isEmpty()) {
			Integer destinationFloor = 0;
			if (!insideCallsQueue.isEmpty()) {
				destinationFloor = insideCallsQueue.pollFirst();
			} else {
				destinationFloor = outsideCallsQueue.pollFirst();
			}
			if (destinationFloor > elevator.getCurrentFloor()) {
				moveUp(destinationFloor);
			} else {
				moveDown(destinationFloor);
			}
		}
	}

	public void moveUp(int destinationFloor) {
		long currentTime = new Date().getTime();
		if (currentTime < elevatorStateStorage.getLastTime()) {
			currentTime = elevatorStateStorage.getLastTime() + delay * 1000;
		}
		long maxTime = currentTime + delay * 1000 + speed * 1000 * destinationFloor + delay * 1000;
		elevatorStateStorage.addState(currentTime, State.STARTING, destinationFloor, elevator.getCurrentFloor());
		elevatorStateStorage.addState(currentTime + delay * 1000, State.MOVE_UP, destinationFloor,
				elevator.getCurrentFloor());
		for (long i = currentTime + delay * 1000 + speed * 1000; i < currentTime + delay * 1000
				+ speed * 1000 * destinationFloor; i += speed * 1000) {
			elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
			elevatorStateStorage.addState(i, State.MOVE_UP, destinationFloor, elevator.getCurrentFloor());
		}
		elevatorStateStorage.addState(currentTime + delay * 1000 + speed * 1000 * destinationFloor, State.STOPPING,
				destinationFloor, elevator.getCurrentFloor());
		elevatorStateStorage.addState(maxTime, State.STOP, 0, elevator.getCurrentFloor());
		elevatorStateStorage.getElevatorStatesLog().forEach(System.out::println);
	}

	public void moveDown(int destinationFloor) {
		int arrivalFloor = elevator.getCurrentFloor();
		long currentTime = new Date().getTime();
		if (currentTime < elevatorStateStorage.getLastTime()) {
			currentTime = elevatorStateStorage.getLastTime() + delay * 1000;
		}
		long maxTime = currentTime + delay * 1000 + speed * 1000 * arrivalFloor + delay * 1000;
		elevatorStateStorage.addState(currentTime, State.STARTING, destinationFloor, elevator.getCurrentFloor());
		elevatorStateStorage.addState(currentTime + delay * 1000, State.MOVE_DOWN, destinationFloor,
				elevator.getCurrentFloor());
		for (long i = currentTime + delay * 1000 + speed * 1000; i < currentTime + delay * 1000
				+ speed * 1000 * arrivalFloor; i += speed * 1000) {
			elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
			elevatorStateStorage.addState(i, State.MOVE_DOWN, destinationFloor, elevator.getCurrentFloor());
		}
		elevatorStateStorage.addState(currentTime + delay * 1000 + speed * 1000 * arrivalFloor, State.STOPPING,
				destinationFloor, elevator.getCurrentFloor());
		elevatorStateStorage.addState(maxTime, State.STOP, 0, elevator.getCurrentFloor());
		elevatorStateStorage.getElevatorStatesLog().forEach(System.out::println);

	}

	public int getCurrentFloor() {

		return elevator.getCurrentFloor();
	}

	public String getCurrentState(long requestTime) {

		return elevatorStateStorage.getState(requestTime);
	}

	public int getMaxFloors() {

		return numberOfFloors;
	}

	public void callOnFloor(int floor) {
		if (!insideCallsQueue.contains(floor)) {
			outsideCallsQueue.addLast(floor);
			LOGGER.info("Arrival floor # {} is added to queue", floor);
			moveElevatorToClientFloor();
		}
	}

	public void callFromElevator(int floor) {
		if (!outsideCallsQueue.contains(floor)) {
			insideCallsQueue.addLast(floor);
			LOGGER.info("Destination floor # {} is added to queue", floor);
			moveElevatorToClientFloor();
		}
	}

}
