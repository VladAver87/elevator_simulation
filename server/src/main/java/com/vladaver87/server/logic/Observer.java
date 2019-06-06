package com.vladaver87.server.logic;

public interface Observer {
	
	public void callOnFloor(int floor);
	public void callFromElevator(int floor);

}
