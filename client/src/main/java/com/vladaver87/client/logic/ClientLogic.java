package com.vladaver87.client.logic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ClientLogic implements Observable{
	
	private ExecutorService service = Executors.newCachedThreadPool();

	public void callElevator(int floor) {
		service.execute(new Runnable() {

			@Override
			public void run() {
				notifyOnserver(floor);
			}

		});
	}

	public void goToDestinationFloor(int floor) {
		service.execute(new Runnable() {

			@Override
			public void run() {
				notifyOnserver(floor);
			}

		});
	}
	
	public String checkElevatorFloor() {
    	Client client = new Client();
    	WebResource resource = client.resource("http://localhost:8080/api/client/checkCurrentFloor");
    	ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
    	
        String output = response.getEntity(String.class);
        return output;
	}
	
	public String checkElevatorState() {
    	Client client = new Client();
    	WebResource resource = client.resource("http://localhost:8080/api/client/checkCurrentState");
    	ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
    	
        String output = response.getEntity(String.class);
        return output;
	}

	@Override
	public void notifyOnserver(int floor) {
    	Client client = new Client();
    	Comand comand = new Comand(floor);
    	WebResource resource = client.resource("http://localhost:8080/api//client/comand");
    	ClientResponse response = resource.type("application/json").put(ClientResponse.class, comand);
    	response.close();
	}

}
