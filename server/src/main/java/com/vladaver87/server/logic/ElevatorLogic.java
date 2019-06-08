package com.vladaver87.server.logic;

import java.util.ArrayDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.vladaver87.server.model.Elevator;
import com.vladaver87.server.model.State;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Component
public class ElevatorLogic implements Observer{
	
	private Logger LOGGER = LoggerFactory.getLogger(ElevatorLogic.class);
	private volatile ArrayDeque<Integer> insideCallsQueue = new ArrayDeque<>();
	private volatile ArrayDeque<Integer> outsideCallsQueue = new ArrayDeque<>();
	private ExecutorService service = Executors.newSingleThreadExecutor();
	
	@Autowired
	private Elevator elevator;
	
	@PreDestroy
	public void shutdown() {
		service.shutdown();
	}
	
	public synchronized void moveElevatorToClientFloor() {
		service.execute(new Runnable() {

			@Override
			public void run() {
				
				while(!insideCallsQueue.isEmpty() || !outsideCallsQueue.isEmpty()) {
					Integer destinationFloor = 0;
					if (!insideCallsQueue.isEmpty()) {
						destinationFloor = insideCallsQueue.pollFirst();
					}else {
						destinationFloor = outsideCallsQueue.pollFirst();
					}				
					
					if (destinationFloor > elevator.getCurrentFloor()) {
						
						moveUp(destinationFloor);						
										
					} else {
						
						moveDown(destinationFloor);
						
					}
				}
				elevator.setState(State.STOP);
			}

		});
		
	}
	
	public void moveUp(int destinationFloor) {
		boarding();
		elevator.setState(State.MOVE_UP);
		while(elevator.getCurrentFloor() != destinationFloor) {
			LOGGER.info("Elevator moving the {} floor", elevator.getCurrentFloor() + 1);
			elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
			sleep();
			LOGGER.info("Elevator on the {} floor", elevator.getCurrentFloor());
		}
		arriving();	
	}
	
	public void moveDown(int destinationFloor) {
		int lastFloor = destinationFloor;
		boarding();
		elevator.setState(State.MOVE_DOWN);
		while(elevator.getCurrentFloor() != destinationFloor) {		
			if (!outsideCallsQueue.isEmpty() && outsideCallsQueue.getFirst() > lastFloor) {
				destinationFloor = outsideCallsQueue.getFirst();
			}
			LOGGER.info("Elevator moving the {} floor", elevator.getCurrentFloor() - 1);
			elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);	
			sleep();
			LOGGER.info("Elevator on the {} floor", elevator.getCurrentFloor());	
		}
		arriving();
		outsideCallsQueue.pollFirst();
		if (elevator.getCurrentFloor() != lastFloor) {
			moveDown(lastFloor);
		}
	}
	
	public void boarding() {
		elevator.setState(State.STARTING);
		LOGGER.info("Elevator is starting...");
		try {
			TimeUnit.SECONDS.sleep(elevator.getDelay());
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);;
		}
	}
	
	public void arriving() {
		elevator.setState(State.STOPPING);
		LOGGER.info("Elevator is stopping...");
		try {
			TimeUnit.SECONDS.sleep(elevator.getDelay());
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);;
		}
	}
	
	public void sleep() {
		try {
			TimeUnit.SECONDS.sleep(elevator.getSpeed());
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);;
		}
	}
	
	public int getCurrentFloor() {
		
		return elevator.getCurrentFloor();
	}
	
	public State getCurrentState() {
		
		return elevator.getState();
	}
	
	public int getMaxFloors() {
		
		return elevator.getNumberOfFloors();
	}

	@Override
	public synchronized void callOnFloor(int floor) {
		outsideCallsQueue.addLast(floor);
		LOGGER.info("Arrival floor # {} is added to queue", floor);		
		if (elevator.getState().equals(State.STOP)) {
		moveElevatorToClientFloor();
		}
	}

	@Override
	public synchronized void callFromElevator(int floor) {
		insideCallsQueue.addLast(floor);
		LOGGER.info("Destination floor # {} is added to queue", floor);
		if (elevator.getState().equals(State.STOP)) {
		moveElevatorToClientFloor();
		}
	}

}
