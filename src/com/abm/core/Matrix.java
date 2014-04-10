package com.abm.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Matrix {
	
	protected Human[][] matrix;
	private int rows, cols;
	protected Environment envi;
	private int _x, _y, x_, y_;
	
	public Matrix(Environment e, int row, int col) {
		rows = row;
		cols = col;
		envi = e;
		Human h;
		matrix = new Human[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				h = new Human(envi);
				h.x = i;
				h.y = j;
				matrix[i][j] = h;
			}
		}
		h = null;
	}
	
	public Matrix(Matrix m) {
		rows = m.getRows();
		cols = m.getCols();
		envi = m.envi;
		Human h;
		matrix = new Human[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				h = new Human(envi);
				h.x = i;
				h.y = j;
				matrix[i][j] = h;
			}
		}
		h = null;
	}

	public Set<State> getNeighborStates(Human h) {		
		Set<State> neighbor = new HashSet<State>();
		_x = (h.x - 1 < 0) ? h.x : h.x - 1;
		_y = (h.y - 1 < 0) ? h.y : h.y - 1;
		x_ = (h.x + 1 > cols) ? h.x : h.x + 1;
		y_ = (h.y + 1 > rows) ? h.y : h.y + 1;
		
		int count = 1;
		for (int i = _x; i <= x_ && i < rows-1; i++) {
			for (int j = _y; j <= y_ && j < cols-1; j++) {
				if (i == h.x && j == h.y)	continue;
				neighbor.add(matrix[i][j].state);
				count++;
			}
		}
		
		return neighbor;
	}
	
	
	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	public void shuffle() {
		List<Human> humans = new ArrayList<Human>();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				humans.add(matrix[i][j]);
			}
		}
		Collections.shuffle(humans);
		Human[] toConv2 = humans.toArray(new Human[humans.size()]);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				matrix[i][j] = toConv2[(j*rows)+i];
			}
		}
	}
	
	
	
}
