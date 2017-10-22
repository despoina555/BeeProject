package bee.view;


import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;



import y.base.Node;
import y.base.Edge;
import y.util.D;
import y.view.PopupMode;
import y.view.Graph2D;

import bee.option.EdgePropertyHandler2;



public class MyPopUpMode2 extends PopupMode {
/*
 * To activate the popup menus right click  on a node
 * 
 * it shows node's info
 * 
 * with a right click on an edge, you can change its color
 * 
 */
	
public MyPopUpMode2(){
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

/** 
 * Action that displays and information dialog for a node. 
 */
class ShowNodeInfo extends AbstractAction
{
  Node v;
  
  ShowNodeInfo(Node v)
  {
    super("Node Info");
    this.v = v;
  }
  
  public void actionPerformed(ActionEvent e)
  {
    String vtext = view.getGraph2D().getLabelText(v);
    JOptionPane.showMessageDialog(view,
                                  "Label text of node is " + 
                                  view.getGraph2D().getLabelText(v));
  }
}// class ShowNodeInfo

/*
 * 
 */

class ShowEdgeOptions extends AbstractAction
{
  Edge edge;
  
  ShowEdgeOptions(Edge edge)
  {
    super("Edge Options");
   
  }
  
  public void actionPerformed(ActionEvent e)
  {
	  try{
  
   Graph2D graph= view.getGraph2D();
   EdgePropertyHandler2 edgeHandler= createEdgePropertyHandler();
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
public EdgePropertyHandler2 createEdgePropertyHandler(){
	EdgePropertyHandler2 edgeHandler = new EdgePropertyHandler2();
	return edgeHandler;
}

	
}//class MyPopupMode