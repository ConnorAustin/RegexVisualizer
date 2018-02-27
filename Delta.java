import java.util.*;

class Transition {
	public String in;
	public String out;
	public String input;
	
	public Transition(String in, String out, String input) {
		this.in = in;
		this.out = out;
		this.input = input;
	}
	
	public boolean equals(Transition t) {
		return in.equals(t.in) && out.equals(t.out) && input.equals(t.input);
	}
}

class State {
	public String stateName;
	public ArrayList<Transition> outputs = new ArrayList<Transition>();
	Delta delta;
	
	public State(Delta delta, String stateName) {
		this.stateName = stateName;
		this.delta = delta;
	}
	
	public String out(String input) {
		for(Transition out : outputs) {
			if(out.input.equals(input)) {
				return out.out;
			}
		}
		return null;
	}
	
	public ArrayList<Transition> inputs() {
		ArrayList<Transition> ins = new ArrayList<Transition>();
		
		for(State s : delta.states.values()) {
			for(Transition t : s.outputs) {
				if(t.out.equals(stateName)) {
					ins.add(t);
				}
			}
		}
		return ins;
	}
	
	public void connect(State outState, String input) {
		Transition t = new Transition(stateName, outState.stateName, input);
		outputs.add(t);
	}
	
	public void disconnect(Transition trans) {
		outputs.remove(trans);
	}
}

class Delta {
	public HashMap<String, State> states = new HashMap<String, State>();
	
	public Delta() {
		
	}
	
	public Delta(Delta d) {
		for(String stateName : d.states.keySet()) {
			State state = new State(this, stateName);
			states.put(stateName, state);
			
			for(Transition t : d.state(stateName).outputs) {
				state.connect(d.state(t.out), t.input);
			}
		}
	}
	
	public void addState(String stateName) {
		states.put(stateName, new State(this, stateName));
	}
	
	public void removeState(String stateName) {
		ArrayList<Transition> inputs = state(stateName).inputs();
		
		for(Transition t : inputs) {
			state(t.in).disconnect(t);
		}
		
		states.remove(stateName);
	}
	
	public State state(String stateName) {
		return states.get(stateName);
	}
	
	public void connect(String in, String out, String input) {
		state(in).connect(state(out), input);
	}
	
	public String outState(String in, String input) {
		return state(in).out(input);
	}
	
	public void print() {
		for(State state : states.values()) {
			for(Transition t : state.outputs) {
				System.out.println(state.stateName + " on " + t.input + " -> " + t.out);
			}
		}
	}
}