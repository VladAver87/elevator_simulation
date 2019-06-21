package com.vladaver87.server.util;

import java.util.Comparator;
import com.vladaver87.server.model.ElevatorTimeState;

public class ElevatorTimeStateComparator implements Comparator<ElevatorTimeState>{

	@Override
	public int compare(ElevatorTimeState obj1, ElevatorTimeState obj2) {
		
		return Long.valueOf(obj1.getTime()).compareTo(obj2.getTime());
	}
}
