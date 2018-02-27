import java.util.*;

// Note: Not a fully implemented GNFA. Only converts the Delta
public class GNFA {
	public final String newStartStateName = "newStartState";
	public final String finalState = "newFinalState";
	
	public String q1;
	
	public Delta delta;
	
	public GNFA(DFA dfa) {
		delta = dfa.delta;
		q1 = dfa.q1;
		
		// Check if we need to make a new start state
		State q1State = delta.state(dfa.q1);
		boolean newStartState = q1State.inputs().size() > 0;
		if(newStartState) {
			delta.addState(newStartStateName);
			delta.state(newStartStateName).connect(q1State, "");
			q1 = newStartStateName;
		}
		
		// Create a new final state
		delta.addState(finalState);
		
		// Connect all the old final states to the new final state
		for(String f : dfa.acceptStates) {
			delta.state(f).connect(delta.state(finalState), "");
		}
		
		// Union state transitions
		for(State s : delta.states.values()) {
			ArrayList<Transition> removedOutputs = new ArrayList<Transition>();
			for(Transition t1 : s.outputs) {
				if(removedOutputs.contains(t1)) {
					continue;
				}
				for(Transition t2 : s.outputs) {
					if(removedOutputs.contains(t2)) {
						continue;
					}
					if(!t1.equals(t2) && t1.in.equals(t2.in) && t1.out.equals(t2.out)) {
						t1.input += " U " + t2.input;
						removedOutputs.add(t2);
					}
				}
			}
			
			for(Transition t : removedOutputs) {
				s.disconnect(t);
			}
		}
		
		for(State s : delta.states.values()) {
			for(Transition t : s.outputs) {
				if(t.input.equals("")) {
					t.input = "ε";
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		DFA dfa = DFAReader.read();
		if(dfa == null) 
			return;
		
		new GNFA(dfa).delta.print();
	}
}