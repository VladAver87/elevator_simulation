package com.vladaver87.client;

import java.util.Scanner;
import com.vladaver87.client.logic.ClientLogic;


public class App 
{
    public static void main( String[] args ) {
    	
    	ClientLogic clientLogic = new ClientLogic();
        
        System.out.println("Elevator on:" + clientLogic.checkElevatorFloor() + " floor");
        while(true) {
        System.out.println("Commands: ");
        System.out.println("To call elevator type: call");
        System.out.println("To get elevator state type: state");
        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();
        if (command.equals("call")) {
        	System.out.println("What floor are you on?");
        	int floor = sc.nextInt();
        	clientLogic.callElevator(floor);
        	while (floor != Integer.valueOf(clientLogic.checkElevatorFloor())) {
        		System.out.println("elevator is moving....");
        	}
        	System.out.println("What is destination floor?");
        	floor = sc.nextInt();
        	clientLogic.goToDestinationFloor(floor);
        	
        }
        if (command.equals("state")) {
        	System.out.println("Elevator state is: " +clientLogic.checkElevatorState());
        } else {
        	System.out.println("unknown command");
        }
      }
    }
}
