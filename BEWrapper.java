package wrapper;


import y.base.Edge;
import y.base.Node;
import y.layout.circular.CircularLayouter;
import y.view.EdgeRealizer;
import y.view.Graph2D;
import y.view.NodeRealizer;
import bee.demo.graphTemplate.EmbeddedGraph;
import bee.option.options.Utilities;

public class BEWrapper
{
	public static Graph2D createGraph2DFromEmbeddedGraph(EmbeddedGraph graph)
	{
//		Graph simpleGraph=new Graph();
		Graph2D result=new Graph2D();
//		result.
		int[] nodeOrder=graph.getNodeOrder();
		int[][] embeddedEdges=graph.getEmbeddedEdges();
		Node[] nodes=new Node[nodeOrder.length];
		for(int i=0;i<nodes.length;i++)
		{
			Node node=result.createNode();
			result.setLabelText(node, ""+node.index());
			nodes[i]=node;
//			NodeRealizer nr=result.getRealizer(node);
//			nr.
		}
		for(int i=0;i<embeddedEdges.length;i++)
		{
			int[] embeddedEdge=embeddedEdges[i];
			Edge edge=result.createEdge(nodes[embeddedEdge[0]], nodes[embeddedEdge[1]]);
			EdgeRealizer er=result.getRealizer(edge);
			er.setLineColor(Utilities.COLORS[embeddedEdge[2]]);			
		}
		CircularLayouter cl = new CircularLayouter();
		cl.doLayout(result);		
		return result;
	}
}
