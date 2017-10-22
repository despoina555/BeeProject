package bee.algo;


import java.awt.Color;
import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;










import bee.demo.graphTemplate.EmbeddedGraph;
import bee.option.options.Utilities;
import bee.view.BeeGui;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.EdgeMap;
import y.base.Node;
import y.base.NodeCursor;
import y.base.NodeList;
import y.view.EdgeRealizer;
import y.view.Graph2D;
import y.view.NodeRealizer;


/*
 * xrhsimopoiei mia paralagh ths check crossings, edw kratanme se ena map kai ta crossings pou kanei h ka8e akmh
 */

public class Heuristic {

	Color [] col= Utilities.COLORS;

	EmbeddedGraph embeddedgraph;

	int maxValue;



	public void setEmbeddedGraph(EmbeddedGraph embeddedgraph){
		this.embeddedgraph=embeddedgraph;
	}








	public EmbeddedGraph run(){


		Map < int [],Integer> edgeMap = new HashMap <int []  ,Integer>();// edge - crossings

		int [] [] embeddedEdges =embeddedgraph.getEmbeddedEdges();	


		for (int i=0; i< embeddedEdges.length; i++)  
		{  
			edgeMap.put(embeddedEdges[i], 0);		  
		} 




		int [] nodeOrder= embeddedgraph.getNodeOrder();	




		final Map<Integer,Integer> nodeIndex= new HashMap<Integer,Integer>();

		for (int k=0;k<nodeOrder.length;k++){
			nodeIndex.put(nodeOrder[k], k);
			//System.out.println(" the node"+ nodeOrder[k] + " is " + k+ " in nodeIndex Map");
		}	



		//*************************
		Comparator edgeComp = new Comparator() {				 
			public int compare(Object o1, Object o2){
				int  [] e1= (int  [] )o1;
				int  [] e2= (int  [])o2;

				int source1=e1  [0];
				int source2=e2  [0];

				int target1=e1  [1];
				int target2=e2  [1];

				int rs1=nodeIndex.get(source1);
				int rs2=nodeIndex.get(source2);


				//int compare = Integer.compare(source1,source2);
				int compare = Integer.compare(rs1,rs2);
				if(compare!=0){
					return compare;
				}
				int rt1=nodeIndex.get(target1);
				int rt2=nodeIndex.get(target2);
				//compare = Integer.compare(target1,target2);
				compare = Integer.compare(rt1,rt2);
				return compare;}
		};	


		Arrays.sort(embeddedEdges,edgeComp);
//************************************************

		edgeMap = checkCrossings(embeddedEdges, nodeIndex, edgeMap);
		
		

		for(int i=0;i<embeddedEdges.length;i++){

			int	[] edge=embeddedEdges[i];

			int source1=(int) edge [0];
			int target1 = (int) edge [1];

			//System.out.println("Edge " +(source1+1)+" - "+(target1+1)+" with node indexes names in views ");

			int rs1=nodeIndex.get(source1);
			int rt2=nodeIndex.get(target1);

			//System.out.println("Edge " +(rs1+1)+" - "+(rt2+1)+" with absolut indexes , according to spine order");
		}


		ArrayList edgeList = new ArrayList<int []>();
		for (int i=0; i< embeddedEdges.length; i++)  
		{  
			System.out.println("edge Map *** edge - crossings");
			System.out.println("edge " + embeddedEdges[i][0]+"-"+	embeddedEdges[i][1]  + "  has  "+edgeMap.get(embeddedEdges[i])+ " ");

			if(edgeMap.get(embeddedEdges[i]) == maxValue)
				edgeList.add(embeddedEdges[i]);
		} 


		for(int i=0;i<edgeList.size();i++){

			System.out.println(" *********** heuristics  loop ********************** ");

			int [] edge= (int[]) edgeList.get(0); 	
			int toPage ;
			//if(i == edge[2]){
//				if(i>1){
//				toPage=i--;
//				}else{
					toPage=edge[2]+1;
			//	}
//			}else{
//				toPage=i;
//			}
			int [] e={(int) edge[0], (int) edge[1]};
			System.out.println("edge which will be moved "+(edge[0]+1)+"-"+(edge[1]+1));
			int oldpage= edge[2];
			embeddedgraph.moveEdge( e,  toPage);
			reset(edgeMap);
			System.out.println("max Value = " + maxValue);
			int oladMaxV=maxValue;
			checkCrossings(embeddedEdges, nodeIndex, edgeMap);
			if(maxValue > oladMaxV){
				embeddedgraph.moveEdge( e,  oldpage);
				//this.run();
				System.out.println("edge  finaly didnt moved ");
				
			}
			System.out.println("maxValue after checkCrossings 2  "+maxValue);
			
			
		}
		

		EmbeddedGraph emG= new EmbeddedGraph();
		emG.setNodeOrder(nodeOrder);
		emG.setEmbeddedEdges(embeddedEdges);
		
		return emG;

	}//run
	
	
	
	public void print(int [][] embeddedEdges){
		
		for(int i=0;i<embeddedEdges.length;i++){
			System.out.println("edge " + (embeddedEdges[i][0]+1) +"-"+ (embeddedEdges[i][1]+1)+" with Color "+ embeddedEdges[i][2] );
			}
	}


	
	
	private void reset(Map<int[], Integer> edgeMap) {
		
		int [] [] embeddedEdges =embeddedgraph.getEmbeddedEdges();	


		for (int i=0; i< embeddedEdges.length; i++)  
		{  
			edgeMap.put(embeddedEdges[i], 0);		  
		} 
		
	}//reset








	private Map checkCrossings(int [] []  embeddedEdges,Map<Integer,Integer> nodeIndex , Map< int [],Integer>  edgeMap){

		Map<Color,Integer> crossingsMap= new HashMap<Color,Integer>();

		for (int k=0;k<col.length;k++){
			crossingsMap.put(col[k], 0);
		}


		maxValue=0;
		

		for(int i=0;i<embeddedEdges.length-1;i++){

			int [] edge=embeddedEdges[i];

			int source1=(int) edge [0];
			int target1 = (int) edge [1];

			//System.out.println("Edge cluster "+(source1+1)+" - "+(target1+1));

			int dif=nodeIndex.get(target1)-nodeIndex.get(source1);
			if(dif>1){

				//System.out.println("dif = "+dif);

				int pointer=i+1;
				boolean continueCheck= true;

				while(continueCheck && pointer<embeddedEdges.length){

					int [] edge2=embeddedEdges[pointer];
					int source2 = (int) edge2 [0];	
					int target2 =(int) edge2 [1];

					if(nodeIndex.get(source2)==nodeIndex.get(source1) && nodeIndex.get(target2)>nodeIndex.get(target1) ){
						//System.out.println(" i dont check the  edge  "+source2 +" -" +target2);
					}else{
						if(nodeIndex.get(source2) >= nodeIndex.get(target1)){
							continueCheck=false;
						}else{
							//System.out.println("check Edge  "+(source2+1)+" - "+(target2+1));

							if(edge [2] == edge2 [2]){

								if(nodeIndex.get(target2)>nodeIndex.get(target1)){



									//System.out.println(" crossings edges , " + (source1+1) +" - " +( target1+1) +" / edge  "+ (source2+1) +" - "+ (target2+1) + " create crossings");

									Integer	 value= crossingsMap.get(col[edge [2]]);

									int newValue1=edgeMap.get(edge)+1;
									int newValue2=edgeMap.get(edge2)+1;
									edgeMap.put(edge,newValue1 );
									edgeMap.put(edge2, newValue2);


									value++;

									crossingsMap.put(col[ edge [2]] ,value );	

									int b;
									if(newValue1>maxValue){
										if(newValue1>newValue2){
											b=maxValue;
											maxValue=newValue1;
										}else{
											b=newValue2;
										}
									}

								}

							}//same color


						}//if	
					}
					pointer++;
				}//while
			}//for
		}//for
		
		 
		return edgeMap;
	}//

}//class
