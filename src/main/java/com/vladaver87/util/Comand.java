package com.vladaver87.util;


import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement
@Getter
@Setter
@NoArgsConstructor
public class Comand {
	
	private Integer floor;
	
	public Comand(Integer floor) {
		this.floor = floor;

	}

}
