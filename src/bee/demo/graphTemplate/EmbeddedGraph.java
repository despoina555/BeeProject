package bee.demo.graphTemplate;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bee.option.options.Utilities;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.EdgeList;
import y.base.Node;
import y.layout.hierarchic.EdgeReverser;
import y.view.EdgeRealizer;
import y.view.Graph2D;
import y.view.NodeRealizer;

public class EmbeddedGraph
{
	private int [] nodeOrder;

	private int [] [] embeddedEdges;
	
	public EmbeddedGraph()
	{
		this.nodeOrder=null;
		this.embeddedEdges=null;
	}
	
	public EmbeddedGraph(int[] nodeOrder, int[][] embeddedEdges)
	{
		this.nodeOrder=nodeOrder;
		this.embeddedEdges=embeddedEdges;
	}
	
	public EmbeddedGraph(Graph2D graph)
	{
		load(graph, false);
	}
	
	public void clear()
	{
		this.nodeOrder=null;
		this.embeddedEdges=null;
	}
	
	public void load(Graph2D graph, boolean colored)
	{
		Node[] nodes=graph.getNodeArray();
		this.nodeOrder=new int [nodes.length];
		for(int i=0;i<nodes.length;i++)
		{
			this.nodeOrder[i]=nodes[i].index();
		}
		Edge[] edges=graph.getEdgeArray();
		this.embeddedEdges=new int [edges.length] [3];
		for(int i=0;i<edges.length;i++)
		{
			int source=Math.min(edges[i].source().index(), edges[i].target().index());
			int target=Math.max(edges[i].source().index(), edges[i].target().index());
			
			if(!colored){
				this.embeddedEdges[i][0]= source; //new EmbeddedEdge(source, target);
				this.embeddedEdges[i][1]= target;
			}
			else{
				EdgeRealizer er=graph.getRealizer(edges[i]);
				//this.embeddedEdges[i]=new EmbeddedEdge(source, target, er.getLineColor());
				this.embeddedEdges[i][0]= source; 
				this.embeddedEdges[i][1]= target;
				this.embeddedEdges[i][2]= Utilities.getColorIndex(er.getLineColor());//Utilities.getColorIndex(c)
			}
		}
	}
	
	public int[] getNodeOrder()
	{
		return nodeOrder;
	}
	
	public void setNodeOrder(int[] nodeOrder){
		this.nodeOrder=nodeOrder;
	}

	public int [][] getEmbeddedEdges()
	{
		return embeddedEdges;
	}
	
	public void setEmbeddedEdges(int [][] embeddedEdges){
		this.embeddedEdges=embeddedEdges;
	}
	
	public void colorEdge(Edge e, int colorIndex)
	{
		int source=e.source().index();
		int target=e.target().index();
		this.colorEdge(source, target, colorIndex);
	}
	
	public void colorEdge(int source, int target, int colorIndex)
	{
		for(int i=0;i<this.embeddedEdges.length;i++)
		{
			if((int)this.embeddedEdges[i][0]==source && (int)this.embeddedEdges[i][1]==target
					||(int)this.embeddedEdges[i][1]==source && (int)this.embeddedEdges[i][0]==target)
			{
				this.embeddedEdges[i][2]=colorIndex;//.setColor(colorIndex);
				return;
			}
		}
	}
	
	public void colorEdge(Edge e, Color c)
	{
		int colorIndex=Utilities.getColorIndex(c);
		this.colorEdge(e, colorIndex);
	}
	
	public void colorEdge(int source, int target, Color c)
	{
		int colorIndex=Utilities.getColorIndex(c);
		this.colorEdge(source, target, colorIndex);
	}
	
	public boolean isEmbedded()
	{
		for(int i=0;i<this.embeddedEdges.length;i++)
		{
			if(! ((int)(this.embeddedEdges[i][2])>-1))
				return false;
		}
		return true;
	}
	
	public void moveEdges(List<int[]> edgeIndex, int toPage)
    {
	    for(int i=0;i<edgeIndex.size();i++)
	    {
	    	this.colorEdge(edgeIndex.get(i)[0], edgeIndex.get(i)[1], toPage);
	    }
	    
    }
	
	public void moveEdge(int[] e, int toPage) {
		this.colorEdge(e[0], e[1], toPage);
		
	}
	
	public void moveNodes(List<Integer> nodeIndex, int distIndex)
    {
		if(distIndex>0){			//right
			for(int s=nodeIndex.size()-1;s>=0;s--)
			{
				int startIndex=nodeIndex.get(s);
				int node= this.nodeOrder[startIndex];

				int endIndex=Math.min(this.nodeOrder.length-1, startIndex+distIndex);
				for(int i=startIndex+1;i<=endIndex;i++){				
					this.nodeOrder[i-1]=this.nodeOrder[i];
				}
				this.nodeOrder[endIndex]=node;
				reverseEdges( node);//**
			}
		}else{
			for(int s=0;s<nodeIndex.size();s++)
			{
				int startIndex=nodeIndex.get(s);
				int node= this.nodeOrder[startIndex];
				
				int endIndex= Math.max(startIndex+distIndex, 0);
				for(int it=startIndex-1; it>=endIndex; it--){
					this.nodeOrder[it+1]=this.nodeOrder[it];
				}
				this.nodeOrder[endIndex]=node;
				reverseEdges( node);//**
			}
		}
	    
    }
	
	//*****
	public void reverseEdges(int node){
		
		Map<Integer,Integer> nodeIndex= new HashMap<Integer,Integer>();
		
		for (int k=0;k<nodeOrder.length;k++){
			nodeIndex.put(nodeOrder[k], k);
			System.out.println(" the node"+ nodeOrder[k] + " is " + k+ " in nodeIndex Map");
		}
		
		
		for(int i=0;i<embeddedEdges.length;i++){
			int [] edge =embeddedEdges[i];
			
			if((int)edge[0] ==node){
				if(nodeIndex.get(edge [1]) <nodeIndex.get(node)){
					edge[0]=edge[1];
				edge [1]=node;
				}
			}

			if((int)edge[1]==node){
				if(nodeIndex.get(edge[0]) > nodeIndex.get(node)){
					edge[1]=edge[0];
					edge[0]=node;
				}
			}
		}
	}

	

//*****
	
	
	
	
	
//	public class EmbeddedEdge
//	{
//		private int source;
//		private int target;
//		private int color;
//		
//		public EmbeddedEdge(int source, int target)
//		{
//			this(source, target, -1);
//		}
//
//		
//
//		public EmbeddedEdge(int source, int target, int colorIndex)
//		{
//			this.source=source;
//			this.target=target;
//			this.color=colorIndex;
//		}
//		
//		public EmbeddedEdge(int source, int target, Color c)
//		{
//			this(source, target,Utilities.getColorIndex(c));
//		}
//		
//		public int getSource()
//		{
//			return source;
//		}
//
//
//		public int getTarget()
//		{
//			return target;
//		}
//
//
//		public int getColor()
//		{
//			return color;
//		}
//
//		public void setColor(int color)
//		{
//			this.color = color;
//		}
//		
//		public boolean isEmbedded()
//		{
//			return this.color>-1;
//		}
//		
//		//***
//		public void setSource(int source2) {
//			this.source=source2;			
//		}
//	
//		public void setTarget(int target2) {
//			this.target=target2;			
//		}
//	}




}




	

