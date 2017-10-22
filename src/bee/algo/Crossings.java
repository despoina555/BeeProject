package bee.algo;

import java.awt.Color;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;








import bee.demo.graphTemplate.EmbeddedGraph;
import bee.option.options.Utilities;




public class Crossings {




	Color [] col= Utilities.COLORS;


	public  Map checkCrossings(EmbeddedGraph graph){



		int crossings=0;

		Map<Color,Integer> crossingsMap= new HashMap<Color,Integer>();

		for (int k=0;k<col.length;k++){
			crossingsMap.put(col[k], 0);
		}


		int [] nodeOrder= graph.getNodeOrder();	


		int	[] [] embeddedEdges=graph.getEmbeddedEdges();	


		final Map<Integer,Integer> nodeIndex= new HashMap<Integer,Integer>();

		for (int k=0;k<nodeOrder.length;k++){
			nodeIndex.put(nodeOrder[k], k);
			System.out.println(" the node"+ nodeOrder[k] + " is " + k+ " in nodeIndex Map");
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


		//********************


		Arrays.sort(embeddedEdges,edgeComp);



		for(int i=0;i<embeddedEdges.length;i++){

			int [] edge=embeddedEdges[i];

			int source1=(int) edge [0];
			int target1 = (int) edge [1];

			System.out.println("Edge " +(source1+1)+" - "+(target1+1)+" with node indexes names in views ");

			int rs1=nodeIndex.get(source1);
			int rt2=nodeIndex.get(target1);

			System.out.println("Edge " +(rs1+1)+" - "+(rt2+1)+" with absolut indexes , according to spine order");
		}



		for(int i=0;i<embeddedEdges.length-1;i++){

			int [] edge=embeddedEdges[i];

			int source1=(int) edge [0];
			int target1 = (int) edge [1];

			System.out.println("Edge cluster "+(source1+1)+" - "+(target1+1));

			int dif=nodeIndex.get(target1)-nodeIndex.get(source1);
			if(dif>1){

				System.out.println("dif = "+dif);


				int pointer=i+1;
				boolean continueCheck= true;

				while(continueCheck && pointer<embeddedEdges.length){

					int [] edge2=embeddedEdges[pointer];
					int source2 = (int) edge2 [0];	
					int target2 =(int) edge2 [1];

					if(nodeIndex.get(source2)==nodeIndex.get(source1) && nodeIndex.get(target2)>nodeIndex.get(target1) ){
						System.out.println(" i dont check the  edge  "+source2 +" -" +target2);
					}else{
						if(nodeIndex.get(source2) >= nodeIndex.get(target1)){
							continueCheck=false;
						}else{
							System.out.println("check Edge  "+(source2+1)+" - "+(target2+1));

							if(edge [2] == edge2 [2]){

								if(nodeIndex.get(target2)>nodeIndex.get(target1)){

									crossings++;

									System.out.println(" crossings edges , " + (source1+1) +" - " +( target1+1) +" / edge  "+ (source2+1) +" - "+ ((int)edge2 [2]+1) + " create crossings");

									Integer	 value= crossingsMap.get(col[edge[2]]);
									value++;
									crossingsMap.put(col[ edge[2] ],value );	

								}

							}//same color


						}//if	
					}
					pointer++;
				}//while






			}


		}//for each edge 



		for(int k=0;k<col.length;k++){
			System.out.println("for the color "+col[k]+" the crossings are "+crossingsMap.get(col[k]));
		}

		System.out.println("crossings "+crossings);


		return crossingsMap;
	}
	//********************************************************************************************************************







}
