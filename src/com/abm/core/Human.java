package com.abm.core;

import java.util.HashSet;
import java.util.Set;

public class Human extends Agent{
	
	protected State state;
	protected Set<State> history = new HashSet<State>();
	protected Environment environment;
	protected int alpha = -1, beta = -1, gamma = -1;
	protected Set<State> neighborStates = new HashSet<State>();
	protected int path = 0;
	protected boolean passedPrimaryInfection = false;
	protected int x, y;
	private int alpha1, alpha2, beta1, beta2;
    protected boolean marked = false;
	
	// delegate variables
	protected double infectionProbability;
	
	public Human() {
		this.state = State.SUSCEPTIBLE;
		this.addToHistory(this.state);
	}
	
	public Human(State state) {
		this.state = state;
		this.addToHistory(this.state);
	}
	
	public Human(Environment e) {
		this.state = State.SUSCEPTIBLE;
		this.addToHistory(this.state);
		this.environment = e;
		this.infectionProbability = (double)e.pref.get("Infection Probability");
		alpha1 = (Integer)this.environment.pref.get("SER1-alpha");
		alpha2 = (Integer)this.environment.pref.get("SER2-alpha");
		beta1 = (Integer)this.environment.pref.get("SER1-beta");
		beta2 = (Integer)this.environment.pref.get("SER2-beta");
		
	}
	
	public boolean isFullySusceptible() {
		return (this.state == State.SUSCEPTIBLE) ? true : false;
	}
	
	public boolean isSusceptibleTo(int n) {
		if (n == 1) {
			return (this.state == State.SUSCEPTIBLE1);
		} else if (n == 2) {
			return (this.state == State.SUSCEPTIBLE2);
		}
		return false;
	}
	
	public State updateState() {
		return updateState( (this.path > 0) ? this.path : 1);
	}
	
	public State updateState(int n) {
		if (this.isFullyResistant()) {
			this.changeState(State.RECOVERED);
			return this.state;
		}
		
		if (this.passedPrimaryInfection) {
			if (n == 1)	n = 2;
			else if (n == 2) n = 1;
            // 
		}
		
		if (this.canBeInfectedWith(n)) {
			if (this.alpha == -1) {
				if (n == 1)
					this.alpha = alpha1;
				else if (n == 2)
					this.alpha = alpha2;
			} else {
				this.updateAlpha();
				if (alpha <= 0) {
					if (n == 1) {
						this.changeState(State.INFECTED1);
						this.alpha = -1;
						this.beta = beta1;
						return this.state;
					} else if (n == 2) {
						this.changeState(State.INFECTED2);
						this.alpha = -1;
						this.beta = beta2;
						return this.state;
					}
				}
			}
		} else if (this.canRecover()) {
			updateBeta();
			if (beta <= 0) {
				if (n == 1) {
					this.changeState(State.RESISTANT1);
					this.beta = -1;
					this.passedPrimaryInfection = true;
					return this.state;
				} else if (n == 2) {
					this.changeState(State.RESISTANT2);
					this.beta = -1;
					this.passedPrimaryInfection = true;
					return this.state;
				}
			}
		}
		return null;
	}
	
	public void changeState(State st) {
		this.state = st;
		addToHistory(this.state);
	}
	
	public void addToHistory(State st) {
		if (!this.history.contains(st))
			this.history.add(st);
	}
    
	public void updateAlpha() {		
		if (alpha > 0) alpha--;
	}
	
	public void updateBeta() {
		if (beta > 0) beta--;
	}
	
	public boolean canBeInfectedWith(int n) {	
		if (n == 1 && !this.hasBeenInfectedWith(n)) {
			if (this.neighborStates.contains(State.INFECTED1) && Math.random() < infectionProbability  || (this.alpha > -1 && this.alpha <= alpha1))
				return true;
		} else if (n == 2 && !this.hasBeenInfectedWith(n)) {
			if (this.neighborStates.contains(State.INFECTED2) && Math.random() < infectionProbability || (this.alpha > -1 && this.alpha <= alpha2))
				return true;
		}
		return false;
	}
	
	public boolean hasBeenInfectedWith(int n) {
		if (n == 1) {
			return this.history.contains(State.INFECTED1);
		} else if (n == 2) {
			return this.history.contains(State.INFECTED2);
		}
		return false;
	}
    
    public State getState() {
        return state;
    }
	
	public boolean canRecover() {
		return ((hasBeenInfectedWith(1) || hasBeenInfectedWith(2)) && (this.beta > -1 && this.beta <= 4)) ? true : false;
	}
	
	public boolean hasBeenResistant() {
		return this.history.contains(State.RESISTANT1) || this.history.contains(State.RESISTANT2);
	}
	
	public boolean isFullyResistant() {
		return this.history.contains(State.RESISTANT1) && this.history.contains(State.RESISTANT2);
	}
	
	public void setNeighbors(Set<State> states) {
		this.neighborStates = states;
	}
    
}
