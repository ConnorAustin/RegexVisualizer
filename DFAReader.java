import java.util.*;
import java.io.*;

public class DFAReader {
	public static DFA read() throws Exception {
		Scanner inputScanner = new Scanner(System.in);
		System.out.print("Enter input filename: ");
		String filename = inputScanner.nextLine();
		
		try {
			Scanner sc = new Scanner(new File(filename));
			
			String[] states = sc.nextLine().split(",");
			String alpha = sc.nextLine();
			String q1;
			Delta delta = new Delta();
			
			for(String state : states) {
				delta.addState(state);
			}
			
			HashSet<String> acceptStates = new HashSet<String>();
			
			while(true) {
				String line = sc.nextLine();
				String[] trans = line.split(",");
				
				if(trans.length == 1) {
					q1 = line;
					break;
				}
				
				delta.connect(trans[0], trans[2], trans[1]);
			}
			
			String[] acceptStatesArr = sc.nextLine().split(",");
			for(String state : acceptStatesArr) {
				acceptStates.add(state);
			}
			
			sc.close();
			return new DFA(states, alpha, delta, q1, acceptStates);
		}
		catch (Exception e) {
			System.err.println("Error: input not in correct format");
			return null;
		}
	}
}