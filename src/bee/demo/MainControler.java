package bee.demo;



import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import wrapper.BEWrapper;
import y.view.Graph2D;
import y.view.Graph2DView;
import bee.demo.graphTemplate.EmbeddedGraph;
import bee.io.BEIOhandler;
import bee.option.options.Utilities;
import bee.view.BeeGui;

public class MainControler {

	private BeeGui gui;
	private LVControler lvcontroler;

	
	private ChangeManager changeManager;

	

	public ChangeManager getChangeManager()
		{
			return changeManager;
		}
	
	public MainControler() {
		gui=null;
		lvcontroler=null;
		this.changeManager=new ChangeManager();

	}

	//=================================================================	
	//SETUP
	//=================================================================
	public void setGui(BeeGui gui) {
		this.gui = gui;
		this.createLVControler(gui.getLVPanels(),gui.getViewOriginal().getGraph2D());
	}

	private void createLVControler(List<JPanel> guiPanels, Graph2D intGraph)
	{
		this.setLvcontroler(new LVControler( guiPanels, intGraph));
	}

	private void setLvcontroler(LVControler lvcontroler) {
		this.lvcontroler = lvcontroler;	
		lvcontroler.setMainControler(this);
	}


	//=================================================================	
	//OPEN ACTION
	//=================================================================
	public void open(Graph2D inputGraph)
	{
		//TODO open other extension
		lvcontroler.open(inputGraph);
		gui.resetColorBars();
		
		changeManager.clear();
		
		gui.disableRedo();
		gui.disableUndo();

	}
	
	public void openEmbedded(String name)
	{
		if(name.endsWith(".be"))
		{
			try
            {
	            EmbeddedGraph embeddedGraph=BEIOhandler.readFromFile(name);
	            Graph2D graph=BEWrapper.createGraph2DFromEmbeddedGraph(embeddedGraph);
	            lvcontroler.load(embeddedGraph, graph);
	            gui.setBarsActiveColors(Utilities.createActiveColors());
	            changeManager.clear();
	            gui.disableRedo();
	    		gui.disableUndo();
	            gui.load(graph);
	            
            } catch (Exception e)
            {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }
			
		}
		
	}
	
	public void openEmbedded(EmbeddedGraph embeddedGraph){

		Graph2D graph=BEWrapper.createGraph2DFromEmbeddedGraph(embeddedGraph);
		lvcontroler.load(embeddedGraph, graph);
		gui.setBarsActiveColors(Utilities.createActiveColors());
		changeManager.clear();
		gui.disableRedo();
		gui.disableUndo();
		gui.load(graph);

	}

	//=================================================================	
	//SAVE ACTION
	//=================================================================
	public Graph2D getBEGraph(){
		return lvcontroler.getBEGraph();
	}
	
	public void saveEmbeddedGraph(String fileName)
	{
		BEIOhandler.writeToFile(fileName, lvcontroler.embeddedGraph);
	}
	
	
	//=================================================================	
	//CREATE BOOK EMBEDDING ACTION
	//=================================================================	
	public void createLayoutView(Graph2DView graph2dview, int numPages){
//		gui.setBarsActiveColors(lvcontroler.createActiveColors(numPages));
		gui.setBarsActiveColors(Utilities.createActiveColors());
		lvcontroler.createLayoutView(graph2dview,  numPages);
		
changeManager.clear();
		
		gui.disableRedo();
		gui.disableUndo();
	}
	
	//=================================================================	
	//FIT CONTENT ACTION
	//=================================================================	
	public void fitContent(){
		lvcontroler.fitContent();
	}

	
	//=================================================================	
	//ACTIVATE-DEACTIVATE PAGES
	//=================================================================	
	
	public void showPage(JPanel panel, int index, int page)
	{
//		System.out.println("show page-number "+index+" on drawing page "+page+" for panel "+panel.getName());
		lvcontroler.showPage(panel, index, page);
	}

	public void hidePage(JPanel panel, int index)
	{
//		System.out.println("hide page-number "+index+" for panel "+panel.getName());
		lvcontroler.hidePage(panel, index);
	}

	
	
	
	
	
	public void movePages(){

	}

//*****

	
	
	public void openInGraph2DViews(LinkedList<Graph2D> graphList,boolean firstTime) {
	
		
	}
	
	//**
	
	public void updateCrossings(){
		Map<Color,Integer>	crossingsMap=lvcontroler.updateCrossings();
		gui.upadateCrossingInfoPane(crossingsMap);
	}

	
	//************************ heuristic****************************
	
	public void heuristic() {
	 lvcontroler.heuristic();
		
	}

	//**********************************
	
	
	public void redo()
	
    {
		
	    this.changeManager.redo();
	    
		if(!changeManager.canRedo())
			gui.disableRedo();
	    
    }

public void undo()
    {
	    this.changeManager.undo();
	    
	    gui.enableRedo();
	    
		if(!changeManager.canUndo())
			gui.disableUndo();
	    
    }

public void enableUndo() {
	gui.enableUndo();
	
}
	


}

