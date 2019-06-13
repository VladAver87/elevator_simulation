package com.vladaver87.client.logic;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


public class ClientLogic {

	private String url;
	private int arrivalFloor;
	private int maxFloors;
	
	public ClientLogic(String url) {
		this.url = url;
		maxFloors = Integer.valueOf(getMaxFloors());
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
		if (Integer.valueOf(getElevatorFloor()) != arrivalFloor) {
			System.out.println(
					"Wait i fiew seconds, elevator is on the " + Integer.valueOf(getElevatorFloor()) + " " + "floor");
		} else {
			notifyElevatorDestinationFloor(floor);
		}
	}

	public String getElevatorFloor() {
		Client client = new Client();
		WebResource resource = client.resource(url + "api/client/currentFloor");
		ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
		String output = response.getEntity(String.class);
		return output;
	}

	public String getElevatorState(long currentTime) {
		Client client = new Client();
		WebResource resource = client.resource(url + "api/client/currentState").path(String.valueOf(currentTime));
		ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
		String output = response.getEntity(String.class);
		return output;
	}

	private String getMaxFloors() {
		Client client = new Client();
		WebResource resource = client.resource(url + "api/client/maxFloors");
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
