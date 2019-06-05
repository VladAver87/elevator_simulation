package com.vladaver87.server.logic;

import java.util.ArrayDeque;
import java.util.Queue;
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
	private volatile Queue<Integer> queue = new ArrayDeque<>();
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
				while(!queue.isEmpty()) {
					Integer destinationFloor = queue.poll();
					
					if (destinationFloor > elevator.getCurrentFloor()) {
						boarding();
						moveUp(destinationFloor);
						arriving();
					} else if (destinationFloor == elevator.getCurrentFloor()){
						
						boarding();
						
					} else {
						boarding();
						moveDown(destinationFloor);
						arriving();
					}
				}
				elevator.setState(State.STOP);
			}

		});
		
	}
	
	public void moveUp(int destinationFloor) {
		elevator.setState(State.MOVE_UP);
		while(elevator.getCurrentFloor() != destinationFloor) {
			LOGGER.info("Elevator moving the {} floor", elevator.getCurrentFloor() + 1);
			elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
			sleep();
			LOGGER.info("Elevator on the {} floor", elevator.getCurrentFloor());
			
		}
	}
	
	public void moveDown(int destinationFloor) {
		elevator.setState(State.MOVE_DOWN);
		while(elevator.getCurrentFloor() != destinationFloor) {
			LOGGER.info("Elevator moving the {} floor", elevator.getCurrentFloor() - 1);
			elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);	
			sleep();
			LOGGER.info("Elevator on the {} floor", elevator.getCurrentFloor());
			
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

	@Override
	public synchronized void handleEvent(int floor) {
		queue.add(floor);
		LOGGER.info("Floor # {} is added to queue", floor);
		if (elevator.getState().equals(State.STOP)) {
		moveElevatorToClientFloor();
		}
	}

}
