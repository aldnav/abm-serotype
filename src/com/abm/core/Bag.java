package com.abm.core;

public class Bag {

	private int timeStep;
	private int susceptible1;
	private int susceptible2;
	private int infected1;
	private int infected2;
	private int resistant1;
	private int resistant2;
	
	public Bag(int t, int s1, int i1, int r1, int s2, int i2, int r2) {
		timeStep = t;
		susceptible1 = s1;
		infected1 = i1;
		resistant1 = r1;
		susceptible2 = s2;
		infected2 = i2;
		resistant2 = r2;
	}
}
