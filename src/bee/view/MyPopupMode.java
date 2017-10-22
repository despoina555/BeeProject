package bee.view;


import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;



import y.base.EdgeList;
import y.base.Node;
import y.base.Edge;
import y.util.D;
import y.view.PopupMode;
import y.view.Graph2D;
import bee.option.EdgePropertyHandler;



public class MyPopupMode extends PopupMode {
	/*
	 * To activate the popup menus right click  on a node
	 * 
	 * it shows node's info
	 * 
	 * with a right click on an edge, you can change its color
	 * 
	 */

	public MyPopupMode(){
		super();
	}//constr

	/** Popup menu for a  node */
	public JPopupMenu getNodePopup(Node v)
	{
		JPopupMenu pm = new JPopupMenu();
		pm.add(new ShowNodeInfo(v));

		return pm;
	}

	public JPopupMenu getEdgePopup(Edge e){
		JPopupMenu pm = new JPopupMenu();
		pm.add(new ShowEdgeOptions(e));

		return pm;
	}

	public JPopupMenu getSelectionPopup(double x, double y){
		JPopupMenu pm = new JPopupMenu();
		pm.add(new ShowSelectedEdgesOptions());
		return pm;	
	}

	/** 
	 * Action that displays and information dialog for a node. 
	 */
	class ShowNodeInfo extends AbstractAction
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Node v;

		ShowNodeInfo(Node v)
		{
			super("Node Info");
			this.v = v;
		}

		public void actionPerformed(ActionEvent e)
		{
			//    String vtext = view.getGraph2D().getLabelText(v);
			JOptionPane.showMessageDialog(view,
					"Label text of node is " + 
							view.getGraph2D().getLabelText(v));
		}
	}// class ShowNodeInfo

	/*
	 * 
	 */
	EdgePropertyHandler edgeHandler;
	Graph2D graph;



	class ShowEdgeOptions extends AbstractAction
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Edge edge;


		ShowEdgeOptions(Edge edge)
		{
			super("Edge Options");

		}


		public void actionPerformed(ActionEvent e)
		{
			try{

				edgeHandler.updateValuesFromSelection(graph);

				if(edgeHandler.showEditor(view.getFrame())) {
					edgeHandler.commitEdgeProperties(graph);
					graph.updateViews();

				}

			}
			catch(NullPointerException exc){
				D.show(exc);
			}
		}
	}


	class ShowSelectedEdgesOptions extends AbstractAction
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		EdgeList edgeList;


		ShowSelectedEdgesOptions()
		{
			super("Edge Options");

		}

		public void actionPerformed(ActionEvent e)
		{
			try{


				edgeHandler.updateValuesFromSelection(graph);
				if(edgeHandler.showEditor(view.getFrame())) {	 

					edgeHandler.commitEdgeProperties(graph);

					graph.updateViews();
				}

			}
			catch(NullPointerException exc){
				D.show(exc);
			}
		}
	}

	//public EdgePropertyHandler createEdgePropertyHandler(){
	//	EdgePropertyHandler edgeHandler = new EdgePropertyHandler();
	//	return edgeHandler;
	//}

	public void setEdgePropertyHandler(EdgePropertyHandler eph){
		edgeHandler=eph;
	}

	public EdgePropertyHandler getEdgePropertyHandler(){
		return edgeHandler;
	}

	public void setGraph2D(Graph2D graph2d){
		graph=graph2d;
	}

}//class MyPopupMode
