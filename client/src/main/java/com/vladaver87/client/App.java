package com.vladaver87.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
    	
    	Client client = new Client();
    	WebResource resource = client.resource("http://localhost:8080/api//client/checkCurrentFloor");
    	ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
    	
        String output = response.getEntity(String.class);
        
        System.out.println("Elevator on :" + output + " floor");

    }
}
