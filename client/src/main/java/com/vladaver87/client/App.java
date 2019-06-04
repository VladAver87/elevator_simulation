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
        System.out.println("To set destination floor type: set");
        System.out.println("To get elevator state type: state");
        System.out.println("To exit type: exit");
        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();
        if (command.equals("call")) {
        	System.out.println("What floor are you on?");
        	Integer floor = sc.nextInt();
        	clientLogic.callElevator(floor);
        	System.out.println("elevator is moving....");
        }else if (command.equals("set")) {
        	System.out.println("What is destination floor?");
        	Integer floor = sc.nextInt();
        	clientLogic.goToDestinationFloor(floor);      	
        }
        else if (command.equals("state")) {
        	System.out.println("Elevator state is: " +clientLogic.checkElevatorState());
        } 
        else if (command.equals("exit")) {
        	sc.close();
        	System.exit(0);
        }
        else {
        	System.out.println("unknown command");
        }
      }
    }
}
