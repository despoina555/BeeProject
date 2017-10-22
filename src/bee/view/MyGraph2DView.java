package bee.view;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


import bee.option.options.Utilities;
import y.layout.hierarchic.EdgeReverser;
import y.view.ArcEdgeRealizer;
import y.view.EdgeRealizer;
import y.view.Graph2D;
import y.view.Graph2DView;
import y.view.NodeRealizer;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.EdgeList;
import y.base.Node;

public class MyGraph2DView extends Graph2DView {

	private static final long serialVersionUID = 1L;
	LinkedList<Node> list;
	
	int [] activePages;

	private double offset;
	private double nodeDist;




	public MyGraph2DView()
	{
		super();
		Node [] nodes=getGraph2D().getNodeArray();
		list= new LinkedList<Node>((Arrays.asList(nodes)));
		//		print();
	}

	public void setGraph2D(Graph2D graph, double offset, double nodeDist)
	{
		super.setGraph2D(graph);
		//		if(graph instanceof MyGraph2DView)
		//		{
		//			list=((MyGraph2DView) graph).getNodeList().
		//		}
		Node [] nodes=getGraph2D().getNodeArray();
		list= new LinkedList<Node>((Arrays.asList(nodes)));
		this.offset=offset;
		this.nodeDist=nodeDist;
		//		print();
	}

	public LinkedList<Node> getNodeList(){		

		//		if(list==null){
		//		Node [] nodes=getGraph2D().getNodeArray();
		//		list= new LinkedList<Node>((Arrays.asList(nodes)));
		//		
		//		}
		//print();
		return list;
	}







	public void setActiveColors(int []activeColors){
		this.activePages=activeColors;
		//		updateEdgeView();
	}




	//	public void updateView(){
	//
	//		updateNodeView();
	//
	//		updateEdgeView();

	//	}



	public void updateNodeView()
	{
//		System.out.println("update view method");
	for(int i=0; i<list.size();i++){
		Node node= list.get(i);
		NodeRealizer nr= getGraph2D().getRealizer(node);
//		nr.setCenter(offset+i*nodeDist, nr.getCenterY());
		(this.getGraph2D()).setCenter(node, offset+i*nodeDist, (this.getGraph2D()).getCenterY(node));
//		System.out.println("graph y:"+(this.getGraph2D()).getCenterY(node)+" realizer y:"+nr.getCenterY());
		
//		reverseEdges( node);//oxii

	}
	//	print();
	//this.paintComponents(this.getGraphics());
	this.repaint();
	super.updateView();
	}


	public void updateEdgeView()
	{
		EdgeCursor ec=this.getGraph2D().edges();
		EdgeReverser erev =new EdgeReverser();	
		EdgeList list=new EdgeList();
		for (ec.toFirst(); ec.ok(); ec.next()){	
			Edge edge= (Edge)ec.current();
			EdgeRealizer er= this.getGraph2D().getRealizer(edge);
			int k=-1;
			for(int i=0;i<Utilities.COLORS.length;i++){
				if(Utilities.COLORS[i].equals(er.getLineColor())){
					k=i;
					break;}

			}

			if(activePages[k] ==Utilities.PAGE_HIDE)
				er.setVisible(false);
			else
			{
				er.setVisible(true);
				Node source=edge.source();		
				Node target= edge.target();

				NodeRealizer nrt =getGraph2D().getRealizer(target);
				NodeRealizer nrs =getGraph2D().getRealizer(source);
				
				ArcEdgeRealizer aer = new ArcEdgeRealizer( er);		
				getGraph2D().setRealizer(edge, aer);
				
				if(activePages[k]==Utilities.PAGE_UP  ){
					if(nrt.getCenterX()<nrs.getCenterX()){						
												
						aer.setRatio(-Math.abs(aer.getRatio()));
					}else{
						aer.setRatio(Math.abs(aer.getRatio()));
					}
				}else{
					if(nrt.getCenterX()<nrs.getCenterX()){						
						
						aer.setRatio(Math.abs(aer.getRatio()));
					}else{
						aer.setRatio(-Math.abs(aer.getRatio()));
					}
				}
				
		
			}
		}
	//	erev.reverseEdges(this.getGraph2D(), list);	

		super.updateView();

	}




public void moveNodes(List<Integer> nodeIndex, int distIndex){		

	LinkedList<Node> list= getNodeList();
	//		System.out.println("embedded graph");



	if(distIndex>0){			//right
		for(int s=nodeIndex.size()-1;s>=0;s--)
		{
			int startIndex=nodeIndex.get(s);
			Node node= list.get(startIndex);

			int endIndex=Math.min(list.size()-1, startIndex+distIndex);
			for(int i=startIndex+1;i<=endIndex;i++){				
				list.set(i-1, list.get(i));
			}
			list.set(endIndex, node);
			//reverseEdges( node);//**
		}
	}else{
		for(int s=0;s<nodeIndex.size();s++)
		{
			int startIndex=nodeIndex.get(s);
			Node node= list.get(startIndex);

			int endIndex= Math.max(startIndex+distIndex, 0);
			for(int it=startIndex-1; it>=endIndex; it--){
				list.set(it+1, list.get(it));
			}
			list.set(endIndex, node);
			//reverseEdges( node);//**
		}
	}
	//TODO reverseEdges
	
}




public void moveEdges(List<int []> edgeIndex, int toPage){

	EdgeCursor ec= this.getGraph2D().edges();
	for(ec.toFirst();ec.ok();ec.next()){
		Edge e= ec.edge();
		int source=e.source().index();
		int target=e.target().index();
		for(int j=0;j<edgeIndex.size();j++)
		{
			if(source==edgeIndex.get(j)[0]&&target==edgeIndex.get(j)[1]
					|| source==edgeIndex.get(j)[1]&&target==edgeIndex.get(j)[0]){
				EdgeRealizer er= this.getGraph2D().getRealizer(e);
				er.setLineColor(Utilities.COLORS[toPage]);

			}
		}

	}
}




//public void reverseEdges(Node node){
//
//	EdgeList elist=new EdgeList();
//	EdgeReverser erev =new EdgeReverser();		
//
//	EdgeCursor ec=node.edges();	
//
//	for (ec.toFirst(); ec.ok(); ec.next()){	
//
//
//		Edge edge= (Edge)ec.current();
//		Node source=edge.source();		
//		Node target= edge.target();
//
//		NodeRealizer nrt =getGraph2D().getRealizer(target);
//		NodeRealizer nrs =getGraph2D().getRealizer(source);
//
//		//		if(edge.getSource()==node){
//		//			if(edge.getTarget()<node){
//		//				edge.setSource(edge.getTarget());
//		//			edge.setTarget(node);
//		//			}
//		//		}
//		//
//		//		if(edge.getTarget()==node){
//		//			if(edge.getSource()>node){
//		//				edge.setTarget(edge.getSource());
//		//				edge.setSource(node);
//		//			}
//		//		}
//
//		if(nrt.getCenterX()<nrs.getCenterX()){
//			elist.add(ec.edge());				
//		}
//
//		erev.reverseEdges(getGraph2D(), elist);	
//	}
//}


public void print()
{
	for(int i=0;i<list.size();i++)

		System.out.print(list.get(i).index()+", ");
	System.out.println("-------");
}



public void showPage(int index, int page)
{
	System.out.println("       showing page "+index);
	if(this.activePages[index]==page)
		return;
	this.activePages[index]=page;
	EdgeCursor ec=this.getGraph2D().edges();
//	EdgeReverser erev =new EdgeReverser();	
//	EdgeList list=new EdgeList();
	for (ec.toFirst(); ec.ok(); ec.next()){	
		Edge edge= (Edge)ec.current();
		EdgeRealizer er= this.getGraph2D().getRealizer(edge);
		if(er.getLineColor().equals(Utilities.COLORS[index]))
		{
			er.setVisible(true);
//			Node source=edge.source();		
//			Node target= edge.target();
//
//			NodeRealizer nrt =getGraph2D().getRealizer(target);
//			NodeRealizer nrs =getGraph2D().getRealizer(source);
//
//			if(nrt.getCenterX()<nrs.getCenterX() && page==Utilities.PAGE_UP ||
//					nrt.getCenterX()>nrs.getCenterX() && page==Utilities.PAGE_DOWN){
//				er.clearBends();
//				list.add(ec.edge());				
//			}
		}
	}
//	System.out.println("     reversing "+list.size()+" edges");
//	erev.reverseEdges(this.getGraph2D(), list);	//
	updateEdgeView();
	
	updateView();

}

public void hidePage(int index)
{
//	System.out.println("       hiding page "+index);
	if(this.activePages[index]==Utilities.PAGE_HIDE)
		return;
	this.activePages[index]=Utilities.PAGE_HIDE;
	EdgeCursor ec=this.getGraph2D().edges();
	for (ec.toFirst(); ec.ok(); ec.next()){	
		Edge edge= (Edge)ec.current();
		EdgeRealizer er= this.getGraph2D().getRealizer(edge);
		if(er.getLineColor().equals(Utilities.COLORS[index]))
		{
			er.setVisible(false);
		}
	}
	updateView();
}

}//class
