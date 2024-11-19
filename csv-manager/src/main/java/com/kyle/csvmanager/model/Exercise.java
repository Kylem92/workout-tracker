package com.kyle.csvmanager.model;

import java.util.Arrays;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Exercise {

    private String name;
    private Integer sets;
    private Integer reps;
    private String intensity;
    private Double load;
    private String tempo;
    private Integer rest;

    public List<String> toCsvList() {
	return Arrays.asList(name, String.valueOf(sets), String.valueOf(reps), intensity,
		load == 0 ? "" : String.valueOf(load), // Only show load if it's not 0
		tempo, String.valueOf(rest));
    }

}
