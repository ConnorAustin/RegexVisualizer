/* 
Note: the graph libraries are used ONLY for the graphical representation
of the regex transformation and does not assist with creation of the DFA, GNFA or Regex in the assignment in any way
*/
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.util.*;

public class RegexGraphical {
	static Graph graph;
	
	public static void drawGraph(Delta delta) {
		graph = new MultiGraph("Regex");
		
		String stylesheet = 
		" edge { text-size: 20px; text-color: blue; size: 2px; } node { size: 35px; fill-color: gray; text-color: black; text-size: 20px; }";
		
		graph.addAttribute("ui.stylesheet", stylesheet);
		
		for(String stateName : delta.states.keySet()) {
			graph.addNode(stateName).addAttribute("label", stateName);
		}
		
		for(String stateName : delta.states.keySet()) {
			State state = delta.state(stateName);
			for(Transition t : state.outputs) {
				graph.addEdge(t.in + t.out, t.in, t.out, true).addAttribute("label", t.input);
			}
		}
		
		graph.display();
	}
	
	public static void showRegex(Regex regex) throws Exception {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		Scanner sc = new Scanner(System.in);
		int index = 0;
		while(true) {
			drawGraph(regex.deltas.get(index));
			sc.nextLine();
			
			++index;
			if(index >= regex.deltas.size()) {
				return;
			}
		}
	}
}