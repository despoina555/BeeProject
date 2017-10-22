package bee.layout;

import java.awt.Color;
import java.util.Random;

import y.base.Edge;
import y.base.EdgeCursor;
import y.base.EdgeList;
import y.base.Graph;
import y.base.Node;
import y.layout.LayoutGraph;
import y.layout.hierarchic.EdgeReverser;
import y.layout.hierarchic.incremental.HierarchicLayouter;
import y.view.ArcEdgeRealizer;
import y.view.EdgeRealizer;
import y.view.Graph2D;
import y.view.LineType;
import y.view.NodeRealizer;
import y.view.Port;
import y.view.ShapeNodeRealizer;
import bee.option.options.Utilities;
import bee.view.MyGraph2DView;



public class MySimpleLayouter extends HierarchicLayouter{
	

	int pages;
	double minimalNodeDistance;
	double offset;
	double width;
	double height;
	double size;
	

	
	public MySimpleLayouter(double offset, double width, double height)
	{
		super();
		this.offset=offset;
		this.width=width;
		this.height=height;
	}
	
	public MySimpleLayouter()
	{
		this(100,1250,250);//230
	}
	public void setNumberOfPages(int pagesNum){
		pages =pagesNum;
	}
	
//	public double getNodeDistance()
//	{
//		return minimalNodeDistance;
//	}
	public int getNumberOfPages(){
		return pages;
	}
	
//	public void setMinimalNodeDistance(double d){
//		minimalNodeDistance = d;	
//		}
	
	public double getOffset()
	{return offset;
	
	}
	
	public double getHeight()
	{
		return height;
	}
	
	public double getMinimalNodeDistance(){
		return minimalNodeDistance ;	
		}
	
	public boolean canLayoutCore(LayoutGraph graph)
	  {
	    return true;
	  }
	 
    public void setOrientationLayouterEnabled(boolean enabled){
    	enabled = false;
    }
    
    

	
    
	public Graph2D doLayoutCore(Graph2D graph){
	
		 //place the nodes on a horizontal line
		Node []  nodes = graph.getNodeArray();	 
//	    double offset = 100;
	    double ycoord =height/2;
//	    double l=1250;
	    double xcoord=offset;
//	    MyGraph2DView.offset=offset;
		 minimalNodeDistance = (width-2*offset)/(nodes.length-1);
		System.out.println("minimal node distance "+minimalNodeDistance);
//		MyGraph2DView.minDist=minimalNodeDistance;
	   
	    
	    for(int i = 0; i < nodes.length; i++){
	    Node v = nodes[i];
	          
	      ShapeNodeRealizer snr=new ShapeNodeRealizer(graph.getRealizer(v));
	      
	      snr.setShapeType(ShapeNodeRealizer.ELLIPSE);	
	      double minSize=3;
	      double maxSize=40;
	      double curSize=minimalNodeDistance/2;
	      size=Math.max(Math.min(maxSize, curSize),minSize);
//	      System.out.println(snr.getWidth());
	      snr.setSize(size,size);
//	      snr.setCenter(xcoord,ycoord);
	      graph.setCenter(v, xcoord, ycoord);
	      graph.setSize(v, size, size);
//	      graph.setLocation(v,xcoord-snr.getWidth()/2,ycoord-snr.getHeight()/2);//Sets the upper left coordinates of the node v
//	      System.out.println("x coord for the node "+i+ " is "+xcoord);
	      xcoord=minimalNodeDistance+xcoord; 
	    System.out.println(size);
	    }	    
	
	    setArcEdge(graph);  
	    graph.updateViews();
	    return graph;
	    
	}//doLayoutCore
	
	
	
	//changes the color of the edges 
	public void setArcEdge(Graph2D graph){	

		
		EdgeList elist=new EdgeList();
		EdgeReverser erev =new EdgeReverser();		
		double edgeDist=0;
		EdgeCursor ec =graph.edges();	

		for(ec.toFirst();ec.ok();ec.next()){
			Edge e=ec.edge();
			NodeRealizer nrt= graph.getRealizer(e.target());
			NodeRealizer nrs= graph.getRealizer(e.source());
			
			double distTemp=Math.abs(nrt.getCenterX()- nrs.getCenterX());
			if(distTemp>edgeDist)
				edgeDist=distTemp;
		}
		System.out.println("height="+(height/2)+" width="+edgeDist);
		double ratio=2*height/edgeDist;
		
		for(ec.toFirst();ec.ok();ec.next()){
			
			EdgeRealizer er =graph.getRealizer( ec.edge());	
			er.setLineColor(Utilities.COLORS[new Random().nextInt(pages)]);
			er.setLineType(LineType.LINE_2);
			
			
			
			ArcEdgeRealizer aer = new ArcEdgeRealizer( er);		
			EdgeRealizer.setHighlightedBendColor(Color.yellow);
			
			
			graph.setRealizer(ec.edge(), aer);
			
		
			NodeRealizer nrt= graph.getRealizer(aer.getEdge().target());
			NodeRealizer nrs= graph.getRealizer(aer.getEdge().source());

			
			
	//the ratio of the arc depends on the distance of the nodes		
//			double h=Math.abs(nrt.getCenterX()- nrs.getCenterX())/2;
//			double a;
			
//			if(graph.nodeCount()>2)
//				a= 1/(graph.nodeCount()-2);
//			else
//				a=1/graph.nodeCount();
//			

//			float height=	(float)(a*h);		
			float width=(float) (aer.getTargetPoint().getX()-aer.getSourcePoint().getX());
			float height=	(float)ratio*width;	
						aer.setRatio((float)ratio);

//			int bc=er.bendCount();
//			if(bc>0){
//				System.out.println("you  have bends!");				
//			}else{
//				System.out.println("you dont have bends!");
////				aer.createBend(aer.getSourcePoint().getX()+width/2,height, null, Graph.AFTER);
//				
//			}
			
			
			aer.setPorts(new Port(0,0), new Port(0,0));
			aer.setHeight((float)(height));
			aer.clearBends();
			
			
			
//			//add the back edges to elist in order to reverse them later			
			if(nrt.getCenterX()<nrs.getCenterX()){	
				aer.setRatio(-aer.getRatio());
//					elist.add(ec.edge());	
			}//if		
			
			
			
		}//end for
				
	//reverseEdges(graph, elist);
		
	
		
		
	}//setArcEdge	
	
	public Graph2D doNodeLayoutCore(Graph2D graph){
		
		 //place the nodes on a horizontal line
		Node []  nodes = graph.getNodeArray();	 
//	    double offset = 100;
	    double ycoord =height/2;
//	    double l=1250;
	    double xcoord=offset;
//	    MyGraph2DView.offset=offset;
		 minimalNodeDistance = (width-2*offset)/(nodes.length-1);
		System.out.println("minimal node distance "+minimalNodeDistance);
//		MyGraph2DView.minDist=minimalNodeDistance;
	   
	    
	    for(int i = 0; i < nodes.length; i++){
	    Node v = nodes[i];
	          
	      ShapeNodeRealizer snr=new ShapeNodeRealizer(graph.getRealizer(v));
	      graph.setRealizer(v, snr);
//	     snr=graph.getRealizer(v);
	      snr.setShapeType(ShapeNodeRealizer.ELLIPSE);	
	      double minSize=3;
	      double maxSize=40;
	      double curSize=minimalNodeDistance/2;
	      size=Math.max(Math.min(maxSize, curSize),minSize);
//	      System.out.println(snr.getWidth());
//	      snr.setSize(size,size);
//	      snr.setCenter(xcoord,ycoord);
	      graph.setCenter(v, xcoord, ycoord);
	      graph.setSize(v, size, size);
//	      graph.setLocation(v,xcoord-snr.getWidth()/2,ycoord-snr.getHeight()/2);//Sets the upper left coordinates of the node v
//	      System.out.println("x coord for the node "+i+ " is "+xcoord);
	      xcoord=minimalNodeDistance+xcoord; 
//	    System.out.println(size);
	    }	    
	
	    setSimpleArcEdge(graph);  
	    graph.updateViews();
	    return graph;
	}
	    
	    public void setSimpleArcEdge(Graph2D graph){	

	
			double edgeDist=0;
			EdgeCursor ec =graph.edges();	

			for(ec.toFirst();ec.ok();ec.next()){
				Edge e=ec.edge();
				NodeRealizer nrt= graph.getRealizer(e.target());
				NodeRealizer nrs= graph.getRealizer(e.source());
				
				double distTemp=Math.abs(nrt.getCenterX()- nrs.getCenterX());
				if(distTemp>edgeDist)
					edgeDist=distTemp;
			}
			System.out.println("height="+(height/2)+" width="+edgeDist);
			double ratio=2*height/edgeDist;
			
			for(ec.toFirst();ec.ok();ec.next()){
				
				EdgeRealizer er =graph.getRealizer( ec.edge());	
//				er.setLineColor(Utilities.COLORS[new Random().nextInt(pages)]);
				er.setLineType(LineType.LINE_2);				
				ArcEdgeRealizer aer = new ArcEdgeRealizer( er);		
				EdgeRealizer.setHighlightedBendColor(Color.yellow);
				
				
				graph.setRealizer(ec.edge(), aer);
				
			
				NodeRealizer nrt= graph.getRealizer(aer.getEdge().target());
				NodeRealizer nrs= graph.getRealizer(aer.getEdge().source());
	
				float width=(float) (aer.getTargetPoint().getX()-aer.getSourcePoint().getX());
				float height=	(float)ratio*width;	
							aer.setRatio((float)ratio);

				aer.setPorts(new Port(0,0), new Port(0,0));
				aer.setHeight((float)(height));
				aer.clearBends();
//				//add the back edges to elist in order to reverse them later			
				if(nrt.getCenterX()<nrs.getCenterX()){	
					aer.setRatio(-aer.getRatio());
//						elist.add(ec.edge());	
				}//if
			}//end for
		}//setArcEdge	

	


	
}//class
