package com.vladaver87.server.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Comand {
	
	private Integer floor;
	
	public Comand(Integer floor) {
		this.floor = floor;

	}

}
