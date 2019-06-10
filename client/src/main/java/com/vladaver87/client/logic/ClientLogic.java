package com.vladaver87.client.logic;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientLogic {

	private String url;
	private int arrivalFloor;
	private int maxFloors;
	
	public ClientLogic(String url) {
		this.url = url;
		maxFloors = Integer.valueOf(checkMaxFloors());
	}

	public void callElevator(int floor) {
		arrivalFloor = floor;
		if (floor < 1 || floor > maxFloors) {
			System.out.println("Invalid floor number");
		} else {
			notifyElevatorClientFloor(floor);
			System.out.println("elevator is moving....");
		}
	}

	public void goToDestinationFloor(int floor) {
		if (floor < 1 || floor > maxFloors) {
			System.out.println("Invalid floor number");
		}
		if (Integer.valueOf(checkElevatorFloor()) != arrivalFloor) {
			System.out.println(
					"Wait i fiew seconds, elevator is on the " + Integer.valueOf(checkElevatorFloor()) + " " + "floor");
		} else {
			notifyElevatorDestinationFloor(floor);
		}
	}

	public String checkElevatorFloor() {
		Client client = new Client();
		WebResource resource = client.resource(url + "api/client/checkCurrentFloor");
		ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
		String output = response.getEntity(String.class);
		return output;
	}

	public String checkElevatorState() {
		Client client = new Client();
		WebResource resource = client.resource(url + "api/client/checkCurrentState");
		ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
		String output = response.getEntity(String.class);
		return output;
	}

	private String checkMaxFloors() {
		Client client = new Client();
		WebResource resource = client.resource(url + "api/client/checkMaxFloors");
		ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
		String output = response.getEntity(String.class);
		return output;
	}
	
	public void notifyElevatorClientFloor(int floor) {
		String param = String.valueOf(floor);
		Client client = new Client();
		WebResource resource = client.resource(url + "api/client/").path(param);
		resource.put();
	}
	
	public void notifyElevatorDestinationFloor(int floor) {
		String param = String.valueOf(floor);
		Client client = new Client();
		WebResource resource = client.resource(url + "api/elevator/").path(param);
		resource.put();
	}
}
