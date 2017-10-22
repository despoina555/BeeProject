package bee.option;

import java.awt.Color;

import y.base.EdgeCursor;

import y.base.Edge;

import y.option.OptionHandler;

import y.view.EdgeRealizer;
import y.view.Graph2D;


public class EdgePropertyHandler2 extends OptionHandler{
	
	

	

	
	public EdgePropertyHandler2(){	
	super("Edge Proprties");
	addColor("Color",null,true).setValueUndefined(true);	
	 
		
	}//const
	
	public void updateValuesFromSelection(Graph2D graph){
		
		EdgeCursor ec = graph.selectedEdges();
		EdgeRealizer er =graph.getRealizer(ec.edge());
		
		//get the initial values from the first selected edge
		
		Color color = er.getLineColor();
		boolean samecolor = true;
		//get all further values from the remaining set of selected edges
		if(ec.size() > 1){
			 for (ec.next(); ec.ok(); ec.next())
		        {
		          er = graph.getRealizer(ec.edge());
		          if (samecolor && color != er.getLineColor())
		              samecolor = false;
		}
			 set("Color", color);
			 getItem("Color").setValueUndefined(!samecolor);
			 //getItem("change").setValueUndefined(false);
	}
	}//updateVal
		public void commitEdgeProperties(Graph2D graph){
			
	for (EdgeCursor ec = graph.selectedEdges(); ec.ok(); ec.next()){
		
			Edge edge = ec.edge();
			EdgeRealizer er =graph.getRealizer(edge);
			
			 if (!getItem("Color").isValueUndefined())
		          er.setLineColor((Color)get("Color"));
			 
			
			 
		 }//for each edge
		

	
		}//commitEdgeProperies


		
}// class EdgePropertyHandler