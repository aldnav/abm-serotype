package com.abm.core;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class Environment implements Runnable{
	protected Map<Object, Object> pref = new HashMap<Object, Object>();
	protected int popCount, runTime, susceptible1, susceptible2, infected1, infected2, resistant1, resistant2;	
	protected Matrix population, _population;
	protected List<Bag> bag = new ArrayList<Bag>();
	private Thread t;
	private long timeStart, timeEnd;
	
	public Environment() {
		initDefaults();
		initPopulation();
		initStates();
		initNeighbor();
	}
	
	public Environment(Map<Object, Object> pr) {
		System.out.println("Initializing environment");
		System.out.print("\tLoading preferences ... ");
		timeStart = System.currentTimeMillis();
		
		pref = pr;		
		popCount = (Integer) pref.get("Population");
		infected1 = (Integer) pref.get("SER1 Init Infected");
		infected2 = (Integer) pref.get("SER2 Init Infected");
		resistant1 = (Integer) pref.get("SER1 Init Resistant");
		resistant2 = (Integer) pref.get("SER2 Init Resistant");
		susceptible1 = popCount - (infected1 + resistant1);
		susceptible2 = popCount - (infected2 + resistant2);
		
		timeEnd = System.currentTimeMillis();
		System.out.println(" done! ( " + (timeEnd-timeStart) + "ms )");
		
		timeStart = System.currentTimeMillis();
		System.out.print("\tLoading population ... ");
		initPopulation();
		System.out.println(" done! ( " + (timeEnd-timeStart) + "ms )");

		timeStart = System.currentTimeMillis();
		System.out.print("\tLoading states ... ");
		initStates();
		System.out.println(" done! ( " + (timeEnd-timeStart) + "ms )");
		
		timeStart = System.currentTimeMillis();
		System.out.print("\tNeighborhood acquaintance ... ");
		initNeighbor();		
		System.out.println(" done! ( " + (timeEnd-timeStart) + "ms )");
		System.out.println("Initialization complete!");
	}
	
	private void initDefaults() {
		pref.put("Population", 1000);
		pref.put("SER1-alpha", 7);
		pref.put("SER2-alpha", 5);
		pref.put("SER1-beta", 4);
		pref.put("SER2-beta", 4);
		pref.put("Run Time", 100);
		pref.put("Time Unit", "days");
		pref.put("Infection Probability", 0.2);
		pref.put("SER1 Init Infected", 50);
		pref.put("SER2 Init Infected", 50);
		pref.put("SER1 Init Resistant", 20);
		pref.put("SER2 Init Resistant", 20);
		
		popCount = (Integer) pref.get("Population");
		infected1 = (Integer) pref.get("SER1 Init Infected");
		infected2 = (Integer) pref.get("SER2 Init Infected");
		resistant1 = (Integer) pref.get("SER1 Init Resistant");
		resistant2 = (Integer) pref.get("SER2 Init Resistant");
		susceptible1 = popCount - (infected1 + resistant1);
		susceptible2 = popCount - (infected2 + resistant2);
	}
	
	public void reset() {
		initDefaults();
		initPopulation();
		initStates();
		initNeighbor();
	}
	
	private void initPopulation() {
		int row, col;
		if (isPowerOfTwo(popCount)) {
			row = col = (int) (Math.sqrt(popCount));
		} else {
			int[] factors = getOptimumFactor(popCount);
			row = factors[0];
			col = factors[1];
		}
		
		population = new Matrix(this, row, col);
		_population = new Matrix(population);
//		_population = population;
	}
	
	private void initNeighbor() {
		int rows = population.getRows() - 1;
		int cols = population.getCols() - 1;
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				population.matrix[i][j].setNeighbors(population.getNeighborStates(population.matrix[i][j]));
//				System.out.println(population.matrix[i][j].state);
			}
		}
	}
	
	private void resetNeighbor(Matrix m) {
		int rows = m.getRows() - 1;
		int cols = m.getCols() - 1;
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				m.matrix[i][j].setNeighbors(m.getNeighborStates(m.matrix[i][j]));
			}
		}
	}
	
	private void initStates() {
		int rows = population.getRows() - 1;
		int cols = population.getCols() - 1;
		int count = -1;
		boolean done = false;
		
		for (int i = 0; i < rows; i++) {
//			if (done)	break;
			for (int j = 0; j < cols; j++) {
				count++;
				if (count < infected1) {
					population.matrix[i][j].changeState(State.INFECTED1);
					population.matrix[i][j].path = 1;
					population.matrix[i][j].addToHistory(State.SUSCEPTIBLE1);
					population.matrix[i][j].addToHistory(State.SUSCEPTIBLE);
//					susceptible1--;
				} else if (count >= infected1 && count < infected1 + infected2) {
					population.matrix[i][j].changeState(State.INFECTED2);
					population.matrix[i][j].addToHistory(State.SUSCEPTIBLE2);
					population.matrix[i][j].addToHistory(State.SUSCEPTIBLE);
					population.matrix[i][j].path = 2;
//					susceptible2--;
				} else if (count >= infected1 + infected2 && count < infected1 + infected2 + resistant1) {
					population.matrix[i][j].changeState(State.RESISTANT1);
					population.matrix[i][j].path = 1;
					population.matrix[i][j].addToHistory(State.INFECTED1);
					population.matrix[i][j].addToHistory(State.SUSCEPTIBLE1);
					population.matrix[i][j].addToHistory(State.SUSCEPTIBLE);
//					susceptible1--;
//					infected1--;
					
					if (Math.random() < 0.5) {
						population.matrix[i][j].passedPrimaryInfection = true;
					}					
				} else if (count >= infected1 + infected2 + resistant1 && count < infected1 + infected2 + resistant1 + resistant2) {
					population.matrix[i][j].changeState(State.RESISTANT2);
                    population.matrix[i][j].addToHistory(State.INFECTED2);
					population.matrix[i][j].addToHistory(State.SUSCEPTIBLE2);
					population.matrix[i][j].addToHistory(State.SUSCEPTIBLE);
					population.matrix[i][j].path = 2;
//					susceptible2--;
//					infected2--;
					
					if (Math.random() < 0.5) {
						population.matrix[i][j].passedPrimaryInfection = true;
					}
				} else {
//					done = true;
					// init path
					/*
					kayasa nalimot ko ani da!
					arbitrary ra diay ni nga pag set ug chance, whether mu follow ug
					path 1 or 2 ang usa ka susceptible human!!!!
						unsa iyang effect if i change nato?
						unsay sakto nga pag determine if mag path1 or path2?
					tabang!!!!!!! >.<
					*/
					if (Math.random() > 0.7)
						population.matrix[i][j].path = 2;
					else
						population.matrix[i][j].path = 1;
				}
			}
		}
		population.shuffle();
	}
	
	public void simulate() {
		runTime = determineTime();
		int rows = population.getRows() - 1;
		int cols = population.getCols() - 1;
		State st;
        Human h = null;
		
//		System.out.print(0 + " : ");
//		displayStat();
		timeStart = System.currentTimeMillis();
		System.out.print("Simulation ... ");
		for (int t = 0; t < runTime; t++) {
			
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
                    h = population.matrix[i][j];
					st = h.updateState();
					if (st != null) {
						if (st == State.INFECTED1) {
							susceptible1--;
							infected1++;
						} else if (st == State.INFECTED2) {
							susceptible2--;
							infected2++;
						} else if (st == State.RESISTANT1) {
                            if (infected1 > 0) {
                                resistant1++;
                                infected1--;
                            }							
						} else if (st == State.RESISTANT2) {
                            if (infected2 > 0) { 
                                resistant2++;
                                infected2--;
                            }							
						} else if (st == State.RECOVERED) {
                            if (h.marked) continue;
                            if (h.path == 1) {
                                if (infected2 > 0) {
                                    infected2--;
                                    resistant2++;
                                    h.marked = true;
                                }
                            } else if (h.path == 2) {
                                if (infected1 > 0) {
                                    infected1--;
                                    resistant1++;
                                    h.marked = true;
                                }
                            }
						}
					}
				}
			}
			// is it necessary to keep track of prev and cur population?
			_population = population;
			resetNeighbor(population);
			
//			System.out.print((t+1) + " : ");
//			displayStat();
			bag.add(new Bag(t, susceptible1, infected1, resistant1, susceptible2, infected2, resistant2));
		}
		timeEnd = System.currentTimeMillis();
		System.out.println(" complete! ( " + (timeEnd - timeStart) + "ms )");
		st = null;
		
		System.out.println("Saving output file.");
		outJS("src/com/abm/out/js/results.js");
		outHtml("src/com/abm/out/results.html");
		
	}
	
	private void outJS(String url) {
		
		try {
			 
			String s = "";
			s += "var config = { " +
					"population: "+ popCount +"," +
					"runtime: " + runTime + "," +
					"duration: " + (Integer)pref.get("Run Time") + "," +
					"timeUnit: '" + pref.get("Time Unit").toString() + "'," +
					"infectionProbability: " + (double)pref.get("Infection Probability") + "," +
					"ser1InitInfected: " + (Integer)pref.get("SER1 Init Infected") + "," +
					"ser2InitInfected: " + (Integer)pref.get("SER2 Init Infected") + "," +
					"ser1InitResistant: " + (Integer)pref.get("SER1 Init Resistant") + "," +
					"ser2InitResistant: " + (Integer)pref.get("SER1 Init Resistant") + "," +
					"};";
			
			s += "var results = [";
			Gson gson = new Gson();
			for (Bag b : bag) {
				if (bag.indexOf(b) < bag.size()-1)
					s += gson.toJson(b) + ",";
				else
					s += gson.toJson(b);
			}
			s += "];";
 
			File file = new File(url);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(s);
			bw.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void outHtml(String url) {
		File htmlFile = new File(url);
		try {
			Desktop.getDesktop().browse(htmlFile.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void displayStat() {
		System.out.println(susceptible1 + " " + infected1 + " " + resistant1 + " - " + susceptible2 + " " + infected2 + " " + resistant2);
	}
	
	private int determineTime() {
		int time = (Integer) pref.get("Run Time");
		String timeUnit = (String) pref.get("Time Unit");
		if (timeUnit.equalsIgnoreCase("days"))
			return time;
		else if (timeUnit.equalsIgnoreCase("weeks"))
			time *= 7;
		else if (timeUnit.equalsIgnoreCase("months"))
			time *= 30;
		else if (timeUnit.equalsIgnoreCase("years"))
			time *= 365;
		return time;
	}
	
	private int[] getOptimumFactor(int n) {
		int x = 1, num = n, y = num;
		int _x = x, _y = y, i = 1;

		while ((x - y) <= 0) {
			if (x * y == num) {
				_x = x;
				_y = y;
			}
			x = i;
			y = num / x;
			i++;
		}
		int[] factors = new int[2];
		factors[0] = _x;
		factors[1] = _y;
		return factors;
	}

	private boolean isPowerOfTwo(int x) {
		return (x != 0) && ((x & (x - 1)) == 0);
	}
	


	@Override
	public void run() {
		simulate();
	}
	
	public void start() {
		if (t == null)  {
	       t = new Thread (this, "Simulation");
	       t.start ();
	    }
	}
	
	public static void main(String[] args) {
		Map<Object, Object> pref = new HashMap<Object, Object>();
//		pref.put("Population", 20000);
        pref.put("Population", 1000);
        pref.put("SER1-alpha", 7);
        pref.put("SER2-alpha", 5);
        pref.put("SER1-beta", 4);
        pref.put("SER2-beta", 4);
        pref.put("Run Time", 100);
        pref.put("Time Unit", "days");
        pref.put("Infection Probability", 0.1);
//        pref.put("SER1 Init Infected", 2000);
        pref.put("SER1 Init Infected", 20);
        pref.put("SER2 Init Infected", 0);
        pref.put("SER1 Init Resistant", 0);
        pref.put("SER2 Init Resistant", 0);
		Environment e = new Environment(pref);
		e.start();

	}
	
}


