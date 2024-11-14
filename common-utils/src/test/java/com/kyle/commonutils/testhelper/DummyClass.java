package com.kyle.commonutils.testhelper;

public class DummyClass {

    private final String name;
    private final Integer number;

    public DummyClass() {
	name = "default";
	number = 0;
    }

    public DummyClass(String name, Integer num) {
	this.name = name;
	number = num;
    }

    public String getName() {
	return name;
    }

    public Integer getNumber() {
	return number;
    }

}
