package question3;

import static common.utils.Utils.readMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinCut {

	final static int END_COUNTER=2000;

	List<Integer> vertices;
	List<Edge> edges;
	
	class Edge{
		Integer firstVertex;
		Integer secondVertex;
		public Edge(Integer firstV, Integer secondV) {
			super();
			this.firstVertex=firstV;
			this.secondVertex=secondV;
		}

		public int contains(Integer vertex){
			if(this.firstVertex.equals(vertex)){
				return 1;
			}else if(this.secondVertex.equals(vertex)){
				return 2;
			}
			return -1;
		}
	}

	int edgesContain(Edge edge){
		for(int i=0;i<edges.size();i++){
			Edge e=edges.get(i);
			if(e.firstVertex.equals(edge.firstVertex) && e.secondVertex.equals(edge.secondVertex)){
				return i;
			}
		}
		return -1;
	}
	
	public static void main(String[] args) {
		
		MinCut minCut=new MinCut();
		
		List<String> list=readMap("kargerMinCut.txt");
		int minCutCount=Integer.MAX_VALUE;
		
		for(int i=0;i<END_COUNTER;i++){
			minCut.initialize(list);
			int res=minCut.process();
			minCutCount=minCutCount>res?res:minCutCount;
		}
		System.out.println("result="+minCutCount);
	}

	private int process() {
		Random random=new Random();
		while(vertices.size()>2){
			//choose edge at random
			int index=random.nextInt(edges.size());
			Edge e=edges.remove(index);
			Integer first=e.firstVertex;
			Integer second=e.secondVertex;
			Edge inversedEdge=new Edge(second,first);
			int inversedEdgePosition=edgesContain(inversedEdge);
			if(inversedEdgePosition>-1){
				edges.remove(inversedEdgePosition);
			}
			
			if(vertices.contains(first)){
				vertices.remove(first);
			}
			//contraction
			for(Edge edge:edges){
				int posFirst=edge.contains(first);
				if(posFirst==1){
					edge.firstVertex=second;
				}else if(posFirst==2){
					edge.secondVertex=second;
				}
			}
			//removing self-loops
			List<Edge> newEdges=new ArrayList<Edge>();
			
			for(Edge edge:edges){
				if(!edge.firstVertex.equals(edge.secondVertex)){
					newEdges.add(edge);
				}
			}
			edges=newEdges;
		}
		return edges.size();
	}
	
	void initialize(List<String> input){
		vertices=new ArrayList<Integer>(200);
		edges=new ArrayList<MinCut.Edge>();
		
		for(String line:input){
			String[] split=line.split("	");
			Integer firstVertex=Integer.valueOf(split[0])-1;
			if(!vertices.contains(firstVertex)){
				vertices.add(firstVertex);
			}
			for(int i=1;i<split.length;i++){
				Integer nextVertex=Integer.valueOf(split[i])-1;
				//populate vertices
				if(!vertices.contains(nextVertex)){
					vertices.add(nextVertex);
				}
				//populate edges
				Edge e=new Edge(firstVertex, nextVertex);
				Edge e1=new Edge(nextVertex, firstVertex);
				if(edgesContain(e)==-1 && edgesContain(e1)==-1){
					edges.add(e);
				}
			}
		}
	}
	
	
}
