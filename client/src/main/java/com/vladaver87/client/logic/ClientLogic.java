package com.vladaver87.client.logic;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ClientLogic {

	private String url;
	private int arrivalFloor;
	private int maxFloors;

	public void callElevator(int floor) {
		arrivalFloor = floor;
		if (floor < 1 || floor > Integer.valueOf(checkMaxFloors())) {
			System.out.println("Invalid floor number");
		} else {
			notifyElevator(floor);
			System.out.println("elevator is moving....");
		}
	}

	public void goToDestinationFloor(int floor) {
		if (floor < 1 || floor > Integer.valueOf(checkMaxFloors())) {
			System.out.println("Invalid floor number");
		}
		if (Integer.valueOf(checkElevatorFloor()) != arrivalFloor) {
			System.out.println(
					"Wait i fiew seconds, elevator is on the " + Integer.valueOf(checkElevatorFloor()) + " " + "floor");
		} else {
			notifyElevator(floor);
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
	
	public void notifyElevator(int floor) {
		String param = String.valueOf(floor);
		Client client = new Client();
		WebResource resource = client.resource(url + "api/client/").path(param);
		resource.put();

	}
}
