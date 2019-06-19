package com.vladaver87.server.logic;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.vladaver87.server.model.ElevatorTimeState;
import com.vladaver87.server.model.State;

@Component
public class ElevatorLogic {
	private final Integer speed;
	private final Integer delay;
	private final Integer numberOfFloors;
	private final Logger LOGGER = LoggerFactory.getLogger(ElevatorLogic.class);
	private final ArrayDeque<Integer> elevatorCallsQueue = new ArrayDeque<>();
	private ElevatorStateStorage elevatorStateStorage;

	@Autowired
	public ElevatorLogic(@Value("${speed}") Integer speed, @Value("${delay}") Integer delay,
			@Value("${numberOfFloors}") Integer numberOfFloors, ElevatorStateStorage elevatorStateStorage) {
		this.speed = speed;
		this.delay = delay;
		this.numberOfFloors = numberOfFloors;
		this.elevatorStateStorage = elevatorStateStorage;
	}

	@PostConstruct
	public void init() {
		elevatorStateStorage.addState(new Date().getTime(), State.STOP, 0, 1);
	}

	public void moveElevatorToClientFloor() {
		int destinationFloor = elevatorCallsQueue.pollFirst();
		if (destinationFloor > elevatorStateStorage.getCurrentFloor(new Date().getTime())) {
			moveUp(destinationFloor);
		} else {
			moveDown(destinationFloor);
		}
	}

	public void moveUp(int destinationFloor) {
		long currentTime = new Date().getTime();
		if (currentTime < elevatorStateStorage.getLastTime()) {
			currentTime = elevatorStateStorage.getLastTime() + delay * 1000;
		}
		long maxTime = currentTime + delay * 1000 + speed * 1000 * destinationFloor + delay * 1000;
		elevatorStateStorage.addState(currentTime, State.STARTING, destinationFloor,
				elevatorStateStorage.getElevatorStopFloor());
		elevatorStateStorage.addState(currentTime + delay * 1000, State.MOVE_UP, destinationFloor,
				elevatorStateStorage.getElevatorStopFloor());
		for (int i = elevatorStateStorage.getElevatorStopFloor(); i < destinationFloor; i++) {
			long time = currentTime + delay * 1000 + speed * 1000 * i;
			elevatorStateStorage.addState(time, State.MOVE_UP, destinationFloor, i + 1);
		}
		elevatorStateStorage.addState(currentTime + delay * 1000 + speed * 1000 * destinationFloor, State.STOPPING,
				destinationFloor, elevatorStateStorage.getElevatorStopFloor());
		elevatorStateStorage.addState(maxTime, State.STOP, 0, elevatorStateStorage.getElevatorStopFloor());
		
		//to see the changing of plain
		elevatorStateStorage.getElevatorStatesLog().forEach(System.out::println);

	}

	public void moveDown(int destinationFloor) {
		int lastStopFloor = elevatorStateStorage.getElevatorStopFloor();
		long currentTime = new Date().getTime();
		if (destinationFloor > lastStopFloor && destinationFloor < elevatorStateStorage.getCurrentFloor(currentTime)) {
			addStopStateIfMoveDown(destinationFloor);		
		} else {
		if (currentTime < elevatorStateStorage.getLastTime()) {
			currentTime = elevatorStateStorage.getLastTime() + delay * 1000;
		}
		long maxTime = currentTime + delay * 1000 + speed * 1000 * lastStopFloor + delay * 1000;
		elevatorStateStorage.addState(currentTime, State.STARTING, destinationFloor,
				elevatorStateStorage.getElevatorStopFloor());
		elevatorStateStorage.addState(currentTime + delay * 1000, State.MOVE_DOWN, destinationFloor,
				elevatorStateStorage.getElevatorStopFloor());
		for (int i = elevatorStateStorage.getElevatorStopFloor(); i > destinationFloor; i--) {
			long time = currentTime + delay * 1000 + speed * 1000 * (lastStopFloor - i + 1);
			elevatorStateStorage.addState(time, State.MOVE_DOWN, destinationFloor, i - 1);
		}
		elevatorStateStorage.addState(currentTime + delay * 1000 + speed * 1000 * lastStopFloor, State.STOPPING,
				destinationFloor, elevatorStateStorage.getElevatorStopFloor());
		elevatorStateStorage.addState(maxTime, State.STOP, 0, elevatorStateStorage.getElevatorStopFloor());
		}
		//to see the changing of plain
		elevatorStateStorage.getElevatorStatesLog().forEach(System.out::println);
	}
	
	private void addStopStateIfMoveDown(int destinationFloor){
		Iterator<ElevatorTimeState> iterator = elevatorStateStorage.getElevatorStatesLog().descendingIterator();
		while (iterator.hasNext()) {
			ElevatorTimeState temp = iterator.next();
			if (temp.getCurrentFloor() == destinationFloor) {
				temp.setState(State.STOPPING);
				temp.setCurrentFloor(destinationFloor);
				temp.setDestinationFloor(destinationFloor);
				elevatorStateStorage.getElevatorStatesLog().add(new ElevatorTimeState(temp.getTime() + delay * 1000 , 
						State.STARTING, destinationFloor,  destinationFloor));
				break;
			}
			temp.setTime(temp.getTime() + delay * 1000);
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

	public void callOnFloor(int floor) {
		if (!elevatorCallsQueue.contains(floor)) {
			elevatorCallsQueue.addLast(floor);
			LOGGER.info("Arrival floor # {} is added to queue", floor);
			moveElevatorToClientFloor();
		}
	}

	public void callFromElevator(int floor) {
		if (!elevatorCallsQueue.contains(floor)) {
			elevatorCallsQueue.addFirst(floor);
			LOGGER.info("Arrival floor # {} is added to queue", floor);
			moveElevatorToClientFloor();
		}
	}

}
