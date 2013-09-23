package question4;

import static common.utils.Utils.readMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class StronglyConnectedComponents {
	
	private int vertexNumber;
	private Map<Integer,List<Integer>> inputGraph;
	private Map<Integer,Vertex> vertecies;
	private Map<Integer,List<Integer>> revertedGraph;
	private int t;//for finishing times
	private Map<Integer,List<Integer>> finishingTimesGraph;
	private Integer s;//for leaders
	private Integer[] leaders;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StronglyConnectedComponents scc=new StronglyConnectedComponents();
		scc.vertexNumber=875714;
		scc.inputGraph=scc.readSCCInput("SCC.txt", scc.vertexNumber);
		scc.revertedGraph=scc.revertGraph(scc.inputGraph);
		scc.computeFinishingTimesNonRecursiveDfs();
		System.out.println("Finishing times:");
		scc.printMap(scc.vertecies);
		System.out.println("Input Graph:");
		scc.printGraph(scc.inputGraph);
		scc.populateGraphWithFinishingTimes();
		System.out.println("Graph with finishing times:");
		scc.printGraph(scc.finishingTimesGraph);
		scc.setExploredToNull();
		scc.computeLeadersNonRecursiveDfs();
		System.out.println("Graph with leaders:");
		scc.printMap(scc.vertecies);
		System.out.println("Leaders:");
		for(int i=0;i<scc.leaders.length;i++){
			Integer leader=scc.leaders[i];
			System.out.println(i+":"+leader);
		}
		List<Integer>res=Arrays.asList(scc.leaders);
		Collections.sort(res);
		for(int i=0;i<res.size();i++){
			System.out.println(i+":"+res.get(i));
		}
	}
	
	private void setExploredToNull() {
		for(Integer i:vertecies.keySet()){
			vertecies.get(i).explored=false;
		}
	}

	private void computeLeadersNonRecursiveDfs() {
		leaders=new Integer[vertecies.size()+1];
		for(int i=0;i<leaders.length;i++){
			leaders[i]=new Integer(0);
		}
		Stack<Vertex> stack=new Stack<Vertex>();
		for(int i=vertexNumber;i>=1;i--){
			Vertex nextVertex=vertecies.get(i);
			if(!nextVertex.explored){
				nextVertex.explored=true;
				stack.push(nextVertex);
				s=i;
			}
			while(!stack.isEmpty()){
				nextVertex=stack.pop();
				nextVertex.leaderLabel=s;
				
				Integer currentNum=leaders[s];
				leaders[s]=++currentNum;
				
				List<Integer> heads=finishingTimesGraph.get(nextVertex.label);
				if(heads!=null){
					for(Integer head:heads){
						if(!vertecies.get(head).explored){
							vertecies.get(head).explored=true;
							stack.push(vertecies.get(head));
						}
					}
				}
				
				
			}
		}
	}

	private void populateGraphWithFinishingTimes() {
		finishingTimesGraph=new HashMap<Integer, List<Integer>>(vertexNumber);
		Set<Integer> tails=inputGraph.keySet();
		for(Integer tail:tails){
			List<Integer> heads=inputGraph.get(tail);
			int newTail=vertecies.get(tail).finishingTime;
			List<Integer> newHeads=new ArrayList<Integer>(heads.size());
			for(Integer head:heads){
				newHeads.add(vertecies.get(head).finishingTime);
			}
			if(!finishingTimesGraph.containsKey(newTail)){
				finishingTimesGraph.put(newTail, newHeads);
			}
		}
	}

	Map<Integer,List<Integer>> revertGraph(Map<Integer,List<Integer>> input){
		Map<Integer,List<Integer>> output=new HashMap<Integer,List<Integer>>(input.size());
		
		Set<Integer> keys=input.keySet();
		for(Integer key:keys){
			List<Integer> values=input.get(key);
			for(Integer value:values){
				List<Integer> revList=output.get(value);
				if(revList==null){
					revList=new ArrayList<Integer>();
					output.put(value, revList);
				}
				if(!revList.contains(key)){
					revList.add(key);
				}
			}
		}
		System.out.println("Reverted Graph:");
		keys=output.keySet();
		for(Integer key:keys){
			List<Integer> values=output.get(key);
			System.out.println("key="+key+"; values="+values);
		}
		return output;
	}

	public Map<Integer,List<Integer>> readSCCInput(String fileName, int vertexNumber){//875714
		List<String> input=readMap(fileName);
		if(input==null || input.isEmpty()){
			return Collections.emptyMap();
		}else{
			Map<Integer,List<Integer>> output=new HashMap<Integer, List<Integer>>(vertexNumber);
			vertecies=new HashMap<Integer,Vertex>(vertexNumber);
			
			for(String line:input){
				String[] split=line.split(" ");
				int tail=Integer.parseInt(split[0]);
				int head=Integer.parseInt(split[1]);
				addVertexToMap(vertecies, tail);
				addVertexToMap(vertecies, head);
				List<Integer> heads=output.get(tail);
				if(heads==null){
					heads=new ArrayList<Integer>();
					output.put(tail,heads);
				}
				if(!heads.contains(head)){
					heads.add(head);
				}
			}
			return output;
		}
		
	}
	
	void computeFinishingTimesNonRecursiveDfs(){
		Stack<Vertex> stack=new Stack<Vertex>();
		
		for(int i=vertexNumber;i>=1;i--){
			Vertex nextVertex=vertecies.get(i);
			if(!nextVertex.explored){
				stack.push(nextVertex);
			}
			
			while(!stack.isEmpty()){
				nextVertex=stack.peek();
				nextVertex.explored=true;
				List<Integer> heads=revertedGraph.get(nextVertex.label);
				boolean unexploredHeads=false;
				if(heads!=null){
					for(Integer head:heads){
						if(!vertecies.get(head).explored){
							vertecies.get(head).explored=true;
							stack.push(vertecies.get(head));
							unexploredHeads=true;
						}
					}
				}
				if(heads==null || !unexploredHeads){
					nextVertex=stack.pop();
					t++;
					vertecies.get(nextVertex.label).finishingTime=t;
					System.out.println("i="+nextVertex.label+" "+t+"    v-"+vertecies.get(nextVertex.label).finishingTime);
				}
			}
		}
	}
	
	void computeFinishingTimes(){
		for(int i=vertexNumber;i>=1;i--){
			Vertex v=vertecies.get(i);
			if(!v.explored){
				dfs1stPass(revertedGraph,i);
			}
		}
	}

	private void dfs1stPass(Map<Integer, List<Integer>> graph, int i) {
		Vertex v=vertecies.get(i);
		v.explored=true;
		List<Integer> heads=graph.get(i);
		if(heads!=null){
			for(Integer head:heads){
				v=vertecies.get(head);
				if(!v.explored){
					dfs1stPass(graph,head);
				}
			}
		}
		t++;
		vertecies.get(i).finishingTime=t;
	}

	private void printGraph(Map<Integer, List<Integer>> output) {
		System.out.println("Graph:");
		Set<Integer> keys=output.keySet();
		for(Integer key:keys){
			List<Integer> values=output.get(key);
			System.out.println(key+" - "+values);
		}
	}
	
	private void printMap(Map<Integer, Vertex> output) {
		System.out.println("Map:");
		Set<Integer> keys=output.keySet();
		for(Integer key:keys){
			Vertex values=output.get(key);
			System.out.println(key+" - "+values.label+","+values.finishingTime+","+values.leaderLabel);
		}
	}

	boolean contains(List<Vertex> list, int label){
		for(Vertex v:list){
			if(v.label==label){
				return true;
			}
		}
		return false;
	}
	
	private void addVertexToMap(Map<Integer,Vertex> map, int vertexLabel){
		if(map.get(vertexLabel)==null){
			map.put(vertexLabel, new Vertex(vertexLabel, false, 0, 0));
		}
	}
	
	
}

class Vertex{
	int label;
	boolean explored;
	int finishingTime;
	int leaderLabel;
	public Vertex(int label, boolean explored, int finishingTime, int leaderLabel) {
		super();
		this.label = label;
		this.explored = explored;
		this.finishingTime = finishingTime;
		this.leaderLabel = leaderLabel;
	}
	
}
