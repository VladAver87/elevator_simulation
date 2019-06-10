package com.vladaver87.client;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vladaver87.client.logic.ClientLogic;




public class App 
{
    public static void main( String[] args )  {
    	Scanner sc = new Scanner(System.in);
    	ClientLogic clientLogic = null;
    	
    	if (args.length == 0 || !testUrl(args[0])) {
    		System.out.println("Restart client with a valid URL adress...for example localhost:8080");
    			System.exit(0);
    			} else {
    				clientLogic = new ClientLogic("http://" + args[0] + "/");
    			}
    	   
        System.out.println("Elevator on:" + clientLogic.checkElevatorFloor() + " floor");
        while(true) {
        System.out.println("Commands: ");
        System.out.println("To call elevator type: call");
        System.out.println("To set destination floor type: set");
        System.out.println("To get elevator state type: state");
        System.out.println("To get elevator current floor type: floor");
        System.out.println("To exit type: exit");
        String command = sc.nextLine();
        if (command.equals("call")) {
        	System.out.println("What floor are you on?");
        	Integer floor = sc.nextInt();
			clientLogic.callElevator(floor);
        }else if (command.equals("set")) {
        	System.out.println("What is destination floor?");
        	Integer floor = sc.nextInt();
			clientLogic.goToDestinationFloor(floor);
        }
        else if (command.equals("state")) {
        	System.out.println("Elevator state is: " +clientLogic.checkElevatorState());
        }
        else if (command.equals("floor")) {
        	System.out.println("Elevator current floor is: " +clientLogic.checkElevatorFloor());
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
    
    private static boolean testUrl(String url) {
    	try {
    	Pattern pattern = Pattern.compile("^[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]:\\d\\d\\d\\d");
    	Matcher matcher = pattern.matcher(url);
    	return matcher.matches();
    	}catch (RuntimeException e) {
    		return false;
    	}
    }
}
