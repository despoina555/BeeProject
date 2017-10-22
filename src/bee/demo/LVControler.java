package bee.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import javax.swing.JPanel;

import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import y.view.EditMode;
import y.view.Graph2D;
import y.view.Graph2DView;
import bee.algo.Crossings;
import bee.algo.Heuristic;
import bee.demo.graphTemplate.EmbeddedGraph;
import bee.layout.MySimpleLayouter;
import bee.option.EdgePropertyHandler;
import bee.option.MyEditModeLooseConstraint;
import bee.option.options.Utilities;

import bee.view.MyGraph2DView;
import bee.view.MyMoveSelectionMode;
import bee.view.MyPopupMode;
import bee.view.MyZoomWheelListener;
import y.view.Graph2DCanvas;

public class LVControler {

	
	EmbeddedGraph embeddedGraph;
	List<MyGraph2DView> layviewlist;

	int pages;
	MainControler mainControler;
	Crossings crossinghecker;

	


	public LVControler(List<JPanel> panels, Graph2D intGraph) {
		this.pages=0;
		
		 createCrossings();

		
		embeddedGraph=new EmbeddedGraph((Graph2D) intGraph);
		layviewlist= new ArrayList<MyGraph2DView>();

		for(int i=0; i<panels.size();i++){

			MyGraph2DView layview= new MyGraph2DView();
			layviewlist.add(layview);
			layview.getGraph2D().clear();
			//			System.out.println(layview);
			layview.getCanvasComponent().setFont(new Font("Serif", Font.PLAIN, 12));
			layview.getCanvasComponent().setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			layview.getCanvasComponent().setAlignmentY(Component.TOP_ALIGNMENT);
			layview.getCanvasComponent().setAlignmentX(Component.LEFT_ALIGNMENT);
			layview.setBounds(340, 5, 1250 , 250);
			layview.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.LOWERED, null, null, new Color(191, 205, 219), null), (Border) new LineBorder((new Color(227, 227, 227)))));
			layview.setPreferredSize(new Dimension(1250,220));
			//			layviewlist.add(layview);
			panels.get(i).add(layview,BorderLayout.CENTER);
		}//for
	}

	//=================================================================	
	//OPEN-RESET ACTION
	//=================================================================		
	
	public void open(Graph2D intGraph)
	{
		this.pages=0;

		//		embeddedGraph.getGraph2D().clear();
		embeddedGraph.clear();
		//embeddedGraph.setGraph2D((Graph2D) intGraph.createCopy());
		for(int i=0;i<layviewlist.size();i++)
		{
			layviewlist.get(i).getGraph2D().clear();
			layviewlist.get(i).updateView();
			//layviewlist.get(i).setGraph2D((Graph2D)intGraph.createCopy());
		}
	}

	
	//=================================================================	
	//CREATE BOOK EMBEDDING ACTION
	//=================================================================		
	public void createLayoutView(Graph2DView graph2dview, int numPages){

		pages=numPages;
		
		MySimpleLayouter layouter= new  MySimpleLayouter(100,layviewlist.get(0).getCanvasComponent().getWidth(),layviewlist.get(0).getCanvasSize().height);
		
		layouter.setNumberOfPages(pages);
		
			
		Graph2D tempGraph=layouter.doLayoutCore((Graph2D)graph2dview.getGraph2D().createCopy());		
		embeddedGraph.load(tempGraph, true);

		double minDist=layouter.getMinimalNodeDistance();
		double offset=layouter.getOffset();
		double height=layouter.getHeight();

		MyZoomWheelListener mwzl =   new MyZoomWheelListener();
		mwzl.setLVControler(this);
		EdgePropertyHandler eph= new EdgePropertyHandler(this);
		eph.setPages(pages);
	
		int[][]activeAllColors= Utilities.createActiveColors();
		for(int i=0; i<layviewlist.size();i++){
			MyGraph2DView layview= layviewlist.get(i);
	
			layview.setGraph2D((Graph2D)tempGraph.createCopy(), offset, minDist);
		
			layview.setActiveColors(activeAllColors[i]);
			layview.setFitContentOnResize(true);

			EditMode  editMode = new MyEditModeLooseConstraint(); 
			//graphList.add(layview.getGraph2D());
			MyMoveSelectionMode	msm=new MyMoveSelectionMode(offset, height/2, minDist);
			
			msm.setLVControler(this);
		
			editMode.setMoveSelectionMode(msm);
			MyPopupMode pUm= new MyPopupMode();
			pUm.setGraph2D(layview.getGraph2D());
			pUm.setEdgePropertyHandler(eph);
			editMode.setPopupMode(pUm);
			layview.addViewMode(editMode); 
			layview.getCanvasComponent().addMouseWheelListener(mwzl);
			
			
				
			layview.updateNodeView();
			layview.updateEdgeView();
			layview.updateView();
			layview.setVisible(true);
			
			informMainControler();
//			System.out.println(((Graph2DCanvas)layview.getCanvasComponent()).getZoom());
		}
	}

	//=================================================================	
	//FIT CONTENT ACTION
	//=================================================================	
	public void fitContent()
    {
	    for(int i=0;i<layviewlist.size();i++)
	    {

			//	    	layviewlist.get(i).fitContent();
	    	layviewlist.get(i).setZoom(1);
//	    	layviewlist.get(i).setCenter(layviewlist.get(i).getWidth()/2, layviewlist.get(i).getHeight()/2);
	    	layviewlist.get(i).updateNodeView();
	    	layviewlist.get(i).updateView();
	    }
	    
    }
	//=================================================================	
	//MOVE ELEMENTS
	//=================================================================		

	public void moveEdges(List<int []> edgeIndex, int toPage){

		embeddedGraph.moveEdges(edgeIndex, toPage);
		
		for(int i=0; i<layviewlist.size();i++){

			layviewlist.get(i).moveEdges(edgeIndex, toPage);
			layviewlist.get(i).updateEdgeView();
			layviewlist.get(i).setVisible(true);
		}
	}


	public void moveNodes(List<Integer> nodeIndex, int distIndex){		

		embeddedGraph.moveNodes(nodeIndex, distIndex);
		
		for(int s=0;s<layviewlist.size();s++)
		{
			layviewlist.get(s).moveNodes(nodeIndex, distIndex);
			//			layview.reverseEdges(node2);
			layviewlist.get(s).updateNodeView();
			layviewlist.get(s).updateEdgeView();
			layviewlist.get(s).setVisible(true);
			layviewlist.get(s).updateView();	
			
		}
	}//moveNodes

	
	
	
	//=================================================================	
	//ACTIVATE-DEACTIVATE PAGES
	//=================================================================	

	public void hidePage(JPanel panel, int index)
	{
		BorderLayout layout = (BorderLayout) panel.getLayout();
		MyGraph2DView graph=(MyGraph2DView)layout.getLayoutComponent(BorderLayout.CENTER);
		graph.hidePage(index);
	}

	public void showPage(JPanel panel, int index, int page)
	{
		BorderLayout layout = (BorderLayout) panel.getLayout();
		MyGraph2DView graph=(MyGraph2DView)layout.getLayoutComponent(BorderLayout.CENTER);
		graph.showPage(index, page);
	}








	public List<MyGraph2DView> getLayviewlist() {
		return layviewlist;
	}
	
	
	
	//*************  Open Connected Components
	
	
	public void open(LinkedList<Graph2D> graphList,	boolean firstTime)
	{
		Graph2D graph=new Graph2D();
		
		for(int i=0;i<graphList.size();i++){
			//for(int j=0;j<
			Graph2D g=	graphList.get(i);
			//TODO CREATE METHOD
		}

	
	//	embeddedGraph.clear();
	
		for(int i=0;i<layviewlist.size();i++)
		{
			layviewlist.get(i).getGraph2D().clear();
			
			
			layviewlist.get(i).updateView();
			
		}
	}
	
	//***Crossing
	
	public void createCrossings(){
		//Crossings
		crossinghecker= new Crossings();
		//crossinghecker.setPages(pages);
	}

	public Map updateCrossings() {
		
		Map<Color,Integer> crossingsMap= crossinghecker.checkCrossings(embeddedGraph);
		return crossingsMap;
	}
	
	

	
	//*****Zoom

	public void setZoom(double zoom, Point2D p)
    {
	    for(int i=0;i<this.layviewlist.size();i++)
	    {
	    	Graph2DView graph=layviewlist.get(i);
	    	Graph2DCanvas canvas=(Graph2DCanvas)graph.getCanvasComponent();
	    	canvas.setZoom(zoom);
	    	canvas.setCenter(p.getX(), p.getY());
	    	graph.updateView();
	    }
    }
	
//*********************************************
	public void informMainControler() {
	 mainControler.updateCrossings();
		
		
	}

	public void setMainControler(MainControler mainControler) {
		this.mainControler=mainControler;
		
	}
	
	//***************************************************
	
	

public void addChangeable(Changeable change)
    {
		this.mainControler.enableUndo();
		
	    this.mainControler.getChangeManager().addChangeable(change);
	   // this.controler.getChangeManager().print();
    }

//****************************************

	public void heuristic() {
		Heuristic heuristic= new Heuristic();
		heuristic.setEmbeddedGraph(embeddedGraph);
		EmbeddedGraph embeddedgraph=heuristic.run();
//		int [] []edges= embeddedGraph.getEmbeddedEdges();
//		heuristic.print(edges);
		this.embeddedGraph=embeddedgraph;
		mainControler.openEmbedded(embeddedGraph );
		informMainControler();
		
	}

	//*********************************************************************
	public Graph2D getBEGraph()
    {
	    return layviewlist.get(0).getGraph2D();
    }
	
	
	
	

	public void load(EmbeddedGraph embeddedGraph, Graph2D graphView)
    {
		this.embeddedGraph=embeddedGraph;
		

		
		MySimpleLayouter layouter= new  MySimpleLayouter(100, layviewlist.get(0).getCanvasSize().width, layviewlist.get(0).getCanvasSize().height);

//		layouter.setNumberOfPages(Utilities.TOTAL_PAGES);

			
		Graph2D tempGraph=layouter.doNodeLayoutCore((Graph2D) graphView.createCopy());		
//		embeddedGraph.load(tempGraph, true);
		
		double minDist=layouter.getMinimalNodeDistance();
		double offset=layouter.getOffset();
		double height=layouter.getHeight();

		MyZoomWheelListener mwzl =   new MyZoomWheelListener();
		mwzl.setLVControler(this);
		EdgePropertyHandler eph= new EdgePropertyHandler(this);
		eph.setPages(pages);
	
		int[][]activeAllColors= Utilities.createActiveColors();
		for(int i=0; i<layviewlist.size();i++){
			MyGraph2DView layview= layviewlist.get(i);
	
			layview.setGraph2D((Graph2D)tempGraph.createCopy(), offset, minDist);
		
			layview.setActiveColors(activeAllColors[i]);
			layview.setFitContentOnResize(true);

			EditMode  editMode = new MyEditModeLooseConstraint(); 
			//graphList.add(layview.getGraph2D());
			MyMoveSelectionMode	msm=new MyMoveSelectionMode(offset, height/2, minDist);
			
			msm.setLVControler(this);
		
			editMode.setMoveSelectionMode(msm);
			MyPopupMode pUm= new MyPopupMode();
			pUm.setGraph2D(layview.getGraph2D());
			pUm.setEdgePropertyHandler(eph);
			editMode.setPopupMode(pUm);
			layview.addViewMode(editMode); 
			layview.getCanvasComponent().addMouseWheelListener(mwzl);
			
			
		
			layview.updateNodeView();
			layview.updateEdgeView();
			layview.updateView();
			layview.setVisible(true);
//			System.out.println(((Graph2DCanvas)layview.getCanvasComponent()).getZoom());
		}
	    
    }


}
