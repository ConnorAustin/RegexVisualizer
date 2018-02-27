import java.util.*;

class DFA {
	public String[] states;
	public Delta delta;
	public String q1;
	public HashSet<String> acceptStates;
	
	public DFA(String[] states, String alpha, Delta delta, String q1, HashSet<String> acceptStates) {
		this.states = states;
		this.delta = delta;
		this.q1 = q1;
		this.acceptStates = acceptStates;
	}
	
	public boolean simulate(String input) {
		String curState = q1;
		int inputIndex = 0;
		
		while(inputIndex != input.length()) {
			String in = "" + input.charAt(inputIndex);
			curState = delta.outState(curState, in);
			if(curState == null) {
				return false;
			}
			++inputIndex;
		}
		return acceptStates.contains(curState);
	}
	
	public static void main(String[] args) throws Exception {
		DFA dfa = DFAReader.read();
		if(dfa == null) 
			return;
		
		while(true) {
			System.out.println("DFA ready!\nEnter a string and see if it's accepted!\nEnter empty string to quit.");
			
			String input = new Scanner(System.in).nextLine();
			if(input.equals(""))
				break;
		
			System.out.println(dfa.simulate(input));
		}
	}
}