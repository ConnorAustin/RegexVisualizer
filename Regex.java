import java.util.*;

public class Regex {
	String regex;
	ArrayList<Delta> deltas = new ArrayList<Delta>();
	
	public String simplify(String regex) {
		regex = regex.replace("()*", "");
		regex = regex.replace(" U ()", "");
		return regex.replace("()", "");
	}
	
	public Regex(GNFA gnfa) {
		Delta delta = gnfa.delta;
		deltas.add(new Delta(delta));
		
		String[] states = delta.states.keySet().toArray(new String[1]);
		
		for(String ripStateName : states) {
			if(ripStateName.equals(gnfa.q1) || ripStateName.equals(gnfa.finalState)) {
				continue;
			}
			
			State ripState = delta.state(ripStateName);
			
			// Look for a loop transition in the rip state
			String loopTransInput = "";
			for(Transition input : ripState.inputs()) {
				if(input.in.equals(ripStateName)) {
					loopTransInput = input.input;
					break;
				}
			}
			
			for(Transition input : ripState.inputs()) {
				State inState = delta.state(input.in);
				if(input.in.equals(ripStateName)) {
					continue;
				}
				
				for(Transition output : ripState.outputs) {
					State outputState = delta.state(output.out);
					if(output.out.equals(ripStateName)) {
						continue;
					}
					
					String regexTrans = "(" + input.input + ")(" + loopTransInput +")*(" + output.input + ")";
					regexTrans = simplify(regexTrans);
					
					// Attempt to find a transition from the input state that shares 
					// the same output as the current output for the rip state
					boolean haveShared = false;
					for(Transition inputOutput : inState.outputs) {
						if(inputOutput.out.equals(output.out)) {
							// Just union the two together instead of making a new connection
							inputOutput.input = simplify("(" + regexTrans + ") U (" + inputOutput.input + ")");
							haveShared = true;
							break;
						}
					}
					
					if(!haveShared) {
						inState.connect(outputState, regexTrans);
					}
				}
			}
			delta.removeState(ripStateName);
			deltas.add(new Delta(delta));
		}
		
		regex = simplify(delta.state(gnfa.q1).outputs.get(0).input);
	}
	
	public void printRegex() {
		System.out.println(regex); 
	}
	
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in); 
		DFA dfa = DFAReader.read();
		if(dfa == null)
			return;
		
		GNFA gnfa = new GNFA(dfa);
		
		Regex regex = new Regex(gnfa);
		System.out.println("Regex: ");
		regex.printRegex();
		
		System.out.println("Would you like to show this graphically? (y/n)");
		
		String in = sc.nextLine();
		if(in.equals("y") || in.equals("Y")) {
			System.out.println("Showing it graphically!");
			System.out.println("Press [enter] to go to next state in the regex transformation");
			RegexGraphical.showRegex(regex);
		}
		else {
			System.out.println("Not showing it graphically. Exiting happily!");
		}
	}
}