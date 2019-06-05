package com.vladaver87.client.logic;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vladaver87.client.exception.InvalidFloorNumberExcerpion;
import com.vladaver87.util.Comand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ClientLogic implements Observable {

	private String url;

	public void callElevator(int floor) {
		if (floor < 1 || floor > 7) {
			try {
				throw new InvalidFloorNumberExcerpion();
			} catch (InvalidFloorNumberExcerpion e) {
				System.out.println("Invalid floor number");
			}
		} else {
			notifyOnserver(floor);
			System.out.println("elevator is moving....");
		}
	}

	public void goToDestinationFloor(int floor) {
		if (floor < 1 || floor > 7) {
			try {
				throw new InvalidFloorNumberExcerpion();
			} catch (InvalidFloorNumberExcerpion e) {
				System.out.println("Invalid floor number");
			}
		}
		if (Integer.valueOf(checkElevatorFloor()) != floor) {
			System.out.println("Wait i fiew seconds, elevator is on the " + Integer.valueOf(checkElevatorFloor()) + " " + "floor");
		} else {
			notifyOnserver(floor);
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

	@Override
	public void notifyOnserver(int floor) {
		Client client = new Client();
		Comand comand = new Comand(floor);
		WebResource resource = client.resource(url + "api/client/comand");
		ClientResponse response = resource.type("application/json").put(ClientResponse.class, comand);
		response.close();
	}

}
