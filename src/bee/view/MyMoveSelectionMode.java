package bee.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import y.base.Node;
import y.base.NodeList;
import y.view.MoveSelectionMode;
import y.view.NodeRealizer;
import bee.demo.Changeable;
import bee.demo.LVControler;
import bee.demo.MainControler;
import bee.demo.NodeMoveChange;

public  class MyMoveSelectionMode extends MoveSelectionMode {

	double offset;	
	double ycoord;
	




	double minDist;

	LVControler lvcontroler;
	
	
	
	public MyMoveSelectionMode(double offset, double ycoord, double minDist)
	{
		super();
		this.offset=offset;
		this.ycoord=ycoord;
		this.minDist=minDist;
	}





	public void setLVControler(LVControler lvcontroler){
		this.lvcontroler=lvcontroler;
	}


	public void selectionOnMove(double dx, double dy, double x, double y){

		super.selectionOnMove( dx, dy , x, y);

		NodeList list = getNodesToBeMoved();
				
		if(list!= null){			


			for(int i=0; i<list.size();i++){

				

				NodeRealizer nr = getGraph2D().getRealizer((Node)list.elementAt(i));

				nr.setCenter(nr.getCenterX(), ycoord);	
				
				
				
			}//for each node										

		}//end if	
		
		((MyGraph2DView)view).updateEdgeView();;
		
		
	}//selectionOnMove	
	



	public void selectionMovedAction(double dx, double dy,  double x,   double y){	

		NodeList list = getNodesToBeMoved();
		
		
		if(list!= null){
		int k;
		k=(int) Math.floor(dx/minDist);
		if(k<0)
			k=k+1;
//		System.out.println(minDist);
//	System.out.println("NODES TO MOVE by " +k+":");
		List<Integer> nodeIndex=new ArrayList<Integer>();
					
			

			for(int i=0; i<list.size();i++){

				NodeRealizer nr = getGraph2D().getRealizer((Node)list.elementAt(i));

//				double xOld=nr.getX()-dx;

	
//				double oldIndex=(xOld-offset)/minDist;
			
//				int oldIndex2;
				int temp=((MyGraph2DView)this.view).list.indexOf((Node)list.elementAt(i));
//				System.out.println(temp);
//				oldIndex2=(int)Math.floor(oldIndex);
			
//				nodeIndex.add(oldIndex2);
				nodeIndex.add(temp);
//				nr.setSelected(false);
//				System.out.println(((Node)list.elementAt(i)).index()+"at index "+oldIndex2);
				

			}
			Arrays.sort(nodeIndex.toArray());
//			System.out.println("first Node "+nodeIndex.get(0));
			//lvcontroler.moveNodes(nodeIndex,k);
			//lvcontroler.updateCrossings();//TODO AN DOULEPSEI TO APO KATW , NA TO SVHSW AUUTO
			
			
			
			List<Integer> endNodeIndex=new ArrayList<>();
			Arrays.sort(nodeIndex.toArray());
			if(k>0){			//right
				for(int s=nodeIndex.size()-1;s>=0;s--)
				{
					int startIndex=nodeIndex.get(s);
					int endIndex=Math.min(getGraph2D().nodeCount()-1, startIndex+k);
					endNodeIndex.add(0,new Integer(endIndex));
					System.out.println("   "+startIndex+"-->"+endIndex);
				}
			}else{
				for(int s=0;s<nodeIndex.size();s++)
				{
					int startIndex=nodeIndex.get(s);
					int endIndex= Math.max(startIndex+k, 0);
					endNodeIndex.add(new Integer(endIndex));
					System.out.println("   "+startIndex+"-->"+endIndex);
				}
			}
			for(int i=0;i<nodeIndex.size();i++)
			{
				System.out.println("   "+nodeIndex.get(i)+"-->"+endNodeIndex.get(i)+" (dist="+(nodeIndex.get(i)-endNodeIndex.get(i))+")");
			}
//			System.out.println("first Node "+nodeIndex.get(0));
			Changeable change=new NodeMoveChange(nodeIndex, k, endNodeIndex, lvcontroler);
			
			
			lvcontroler.moveNodes(nodeIndex,k);
			
			
			lvcontroler.informMainControler();

		}
	
		

				

	}//SlectionMovedAction






	




//	public void reverseEdges(Node node){
//		EdgeList elist=new EdgeList();
//		EdgeReverser erev =new EdgeReverser();		
//
//		EdgeCursor ec=node.edges();	
//
//		for (ec.toFirst(); ec.ok(); ec.next()){
//			Graph2D graph= myView.getGraph2D();
//			EdgeRealizer er =graph.getRealizer( ec.edge());	
//			Color color=er.getLineColor();
//
//			Edge edge= (Edge)ec.current();
//			Node source=edge.source();
//
//			Node target= edge.target();
//			NodeRealizer nrt =getGraph2D().getRealizer(target);
//			NodeRealizer nrs =getGraph2D().getRealizer(source);
//			if(nrt.getCenterX()<nrs.getCenterX()){
//				if(color.equals(Color.pink)|| color.equals( Color.black)){
//					return;
//				}else{
//					elist.add(ec.edge());
//				}	
//			}
//
//			if(nrs.getCenterX()<nrt.getCenterX()){
//				if(color.equals(Color.pink)|| color.equals( Color.black)){
//					elist.add(ec.edge());
//				}	
//			}
//
//			erev.reverseEdges(getGraph2D(), elist);	
//		}
//	}
//


//	public MyGraph2DView getView(){
//		return myView;
//	}
//
//	public void setView(MyGraph2DView view){
//		myView=view;
//	}



//	public void setMinDist(double dist){
//		minDist=dist;
//	}
//
//	public double  getMinDist(){
//		return minDist;
//	}
//




}//MyMoveSelectionMode
