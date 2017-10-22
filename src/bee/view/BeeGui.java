package bee.view;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridBagLayoutInfo;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.CellRendererPane;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;















import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;




import javax.swing.table.TableCellRenderer;


import javax.swing.table.TableColumn;



//import bee.view.ToolBarsPanel.ColorSelectionAction;
import y.algo.GraphChecker;
import y.algo.GraphConnectivity;
import y.base.EdgeCursor;
import y.base.Graph;
import y.base.Node;
import y.base.NodeCursor;
import y.base.NodeList;
import y.base.YList;
import y.io.GMLIOHandler;
import y.layout.circular.CircularLayouter;
import y.layout.organic.OrganicLayouter;
import y.layout.orthogonal.OrthogonalLayouter;
import y.layout.planar.DrawingEmbedder;
import y.layout.planar.Face;
import y.layout.planar.PlanarInformation;
import y.option.OptionHandler;
import y.util.D;
import y.view.EdgeRealizer;
import y.view.EditMode;
import y.view.Graph2D;
import y.view.Graph2DSelectionEvent;
import y.view.Graph2DSelectionListener;
import y.view.Graph2DView;
import y.view.Graph2DViewMouseWheelZoomListener;
import y.view.LineType;
import y.view.NodeRealizer;
import y.view.PolyLineEdgeRealizer;
import bee.demo.MainControler;
import bee.option.EdgePropertyHandler;
import bee.option.MyEditModeConstraint;
import bee.option.MyEditModeLooseConstraint;
import bee.option.options.Utilities;



public class BeeGui extends JFrame {

	private static final long serialVersionUID = 1L;

	//private JPanel contentPane;

	protected Graph2DView viewOriginal;

	Map<Node, Integer>conNodeMap =new HashMap<Node,Integer>();
	LinkedList<Graph2D> graphList= new LinkedList<Graph2D> ();

	private ArrayList<JPanel> lvPanels;

	private MainControler mainControler;


	JTabbedPane infoPane;
	JPanel crossingspanel;


	
	JMenuItem undo;
	JMenuItem redo;

	//Graph2DUndoManager undo;



	/**
	 * Create the frame.
	 */
	public BeeGui() {
		initGui();
	}//constr

	/**
	 * SETUP CONTROLER
	 */
	public void setMainControler(MainControler contr)
	{
		this.mainControler=contr;
	}

	//=================================================================	
	//CREATE GUI
	//=================================================================
	private void initGui(){


		this.setIconImage(Toolkit.getDefaultToolkit().getImage(BeeGui.class.getResource("/javaguiresources/250px-Petersen_double_cover.svg.png")));
		this.setForeground(Color.PINK);

		this.setTitle("Bee 1.0");
		this.setFont(Utilities.CUSTOM_FONT);
		this.setBounds(0, 0, 1300, 602);

		this.buildMainMenu();


		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);


		this.buildToolBars(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//scrollPane.enable(true);
		//scrollPane.setOpaque(true);//???
//		scrollPane.getViewport().setMinimumSize(new Dimension(1000,500));
//		scrollPane.getViewport().setPreferredSize(new Dimension(1200,500));//setMinimumSize(getPreferredSize());
		//*********
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		
		
		
		getContentPane().add(scrollPane,gbc_scrollPane);//, BorderLayout.CENTER);
		
	
		
		
		final Dimension d= new Dimension (2000,1150);// = scrollPane.getPreferredSize();

		scrollPane.setPreferredSize( d );
		scrollPane. setMinimumSize(new Dimension(1200,500));
		scrollPane.setMaximumSize(new Dimension(2000,1150));
		    


		
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(new Color(245, 245, 220));
		desktopPane.setSize(new Dimension(1250,300));
		desktopPane.setVisible(true);
		
		desktopPane.setPreferredSize( d );
		desktopPane. setMinimumSize(new Dimension(1200,500));
		desktopPane.setMaximumSize(new Dimension(2000,1150));
		
		GridBagConstraints gbc_desktopPane = new GridBagConstraints();
		gbc_desktopPane.insets = new Insets(0, 0, 0, 5);
		gbc_desktopPane.fill = GridBagConstraints.BOTH;
		gbc_desktopPane.gridx = 0;
		gbc_desktopPane.gridy = 1;
		
		scrollPane.add(desktopPane,gbc_desktopPane);
		
		scrollPane.setViewportView(desktopPane);

		
		this.makeVieworiginal(desktopPane);

		this.makeLayPanels(desktopPane);
		
		this.makeInfoTabPane(desktopPane);
		
		scrollPane.revalidate();
		scrollPane.updateUI();

	}

	private void buildMainMenu(){

		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(Utilities.CUSTOM_FONT);
		menuBar.setBackground(UIManager.getColor("MenuBar.background"));
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic(KeyEvent.VK_F);
		mnFile.setFont(Utilities.CUSTOM_FONT);
		menuBar.add(mnFile);

		JMenuItem mntmOpen_1 = new JMenuItem("Open");		
		mntmOpen_1.addActionListener(new OpenAction());	

		mntmOpen_1.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/11949984141591900034fileopen.svg.med.png")));
		mnFile.add(mntmOpen_1);

		mnFile.addSeparator();

		JMenuItem mntmSaveBE = new JMenuItem("Save book embedding");
		mntmSaveBE.addActionListener(new SaveBEAction() );
		mntmSaveBE.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/save.png")));
		mnFile.add(mntmSaveBE);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		mnFile.add(mntmExit);

		JMenu mnEditMenu = new JMenu("Edit");
		mnEditMenu.setFont(Utilities.CUSTOM_FONT);
		mnEditMenu.setMnemonic(KeyEvent.VK_E);//alt +E
		menuBar.add(mnEditMenu);

		 undo = new JMenuItem("Undo");
		 undo.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/Undo-icon.png")));
		disableUndo();
		undo.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		 undo.addActionListener(new UndoAction());
		mnEditMenu.add(undo);


		redo = new JMenuItem("Redo");
		redo.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/redoo.png")));		
		disableRedo();
		
		redo.addActionListener(new  RedoAction() );
		mnEditMenu.add(redo);

		mnEditMenu.addSeparator();

//		JMenuItem mnEditMode = new JMenuItem("Edit Mode");
//		mnEditMode.addActionListener(new  EditModeAction());
//		mnEditMode.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/application_edit.png")));
//		mnEditMenu.add(mnEditMode);

		JMenu mnView = new JMenu("View");
		mnView.setMnemonic(KeyEvent.VK_V);
		mnView.setFont(Utilities.CUSTOM_FONT);
		menuBar.add(mnView);

		JMenuItem mntmLaunchLayout = new JMenuItem("Create Book Embedding");
		mntmLaunchLayout.addActionListener(new CreateBEAction() );
		mntmLaunchLayout.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/layout.gif")));
		mnView.add(mntmLaunchLayout);

		mnView.addSeparator();

		JMenuItem mntmFitContent = new JMenuItem("Fit Content");		
		mntmFitContent.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/FitContent.gif")));
		mntmFitContent.addActionListener(new FitContentAction());
		mnView.add(mntmFitContent);

		JMenu mnTools = new JMenu("Tools");
		mnTools.setMnemonic(KeyEvent.VK_T);
		mnTools.setFont(Utilities.CUSTOM_FONT);
		menuBar.add(mnTools);


		JMenu mnAlgorithms= new JMenu("Algorithms");
		mnTools.add(mnAlgorithms);

		JMenuItem mntmHeuristics = new JMenuItem("Heuristics");
		mnAlgorithms.add(mntmHeuristics);		
		mntmHeuristics.addActionListener(new HeuristicAction());

		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setFont(Utilities.CUSTOM_FONT);
		JMenuItem mntmHelp = new JMenuItem("Help");
		mnHelp.add(mntmHelp);		
		mntmHelp.addActionListener(new HelpAction());
		menuBar.add(mnHelp);
		

		JMenu mnAbout = new JMenu("About");
		mnAbout.setFont(Utilities.CUSTOM_FONT);
		menuBar.add(mnAbout);
	}

	private void buildToolBars(JPanel container){
		JToolBar toolBar = new JToolBar();
		toolBar.setBackground(SystemColor.menu);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.insets = new Insets(0, 0, 5, 5);
		gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 0;
		container.add(toolBar, gbc_toolBar);

		JButton openButton = new JButton(new OpenAction());
		openButton.setToolTipText("Open");
		openButton.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/11949984141591900034fileopen.svg.med.png")));
		toolBar.add(openButton);

		JButton saveButton = new JButton(new SaveBEAction());		
		saveButton.setBackground(SystemColor.menu);
		saveButton.setToolTipText("Save book embedding");
		saveButton.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/save.png")));
		toolBar.add(saveButton);
		JButton layoutB = new JButton("");
		layoutB.setBackground(SystemColor.menu);
		layoutB.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/layout.gif")));
		layoutB.addActionListener(new CreateBEAction());
		toolBar.add(layoutB);
	}//toolbars

	private void makeVieworiginal(JDesktopPane container){

		JPanel mainViewPanel= new JPanel();
		mainViewPanel.setBounds(5, 5, 310, 310);

		viewOriginal = new Graph2DView();
		viewOriginal.getCanvasComponent().setBackground(new Color(253, 245, 230));
		viewOriginal.setBounds(10, 10, 300, 300);
		viewOriginal.setPreferredSize(new Dimension(300,300));
//		viewOriginal.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.LOWERED, null, null, new Color(191, 205, 219), null), new LineBorder((new Color(227, 227, 227)))));

		mainViewPanel.add(viewOriginal);

		container.add(mainViewPanel); 
		viewOriginal.setVisible(true);
		mainViewPanel.setVisible(true);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(5, 318, 130, 19);
		container.add(menuBar);

		JMenu mnEdit2 = new JMenu("Edit");
		mnEdit2.setFont(Utilities.CUSTOM_FONT);
		menuBar.add(mnEdit2);

		JMenuItem mnEditOpen2 = new JMenuItem("Edit Mode");
		mnEditOpen2.setFont(Utilities.CUSTOM_FONT);
		mnEditOpen2.addActionListener(new EditModeAction());
		mnEdit2.add(mnEditOpen2);

		JMenu mnTools2 = new JMenu("Tools");
		mnTools2.setFont(Utilities.CUSTOM_FONT);
		menuBar.add(mnTools2);

		JMenuItem mntmAnalyze= new JMenuItem("Analyze");
		mntmAnalyze.addActionListener(new AnalysisAction());
		mnTools2.add(mntmAnalyze);

		JMenu mnLayout = new JMenu("Layout");
		mnLayout.setFont(Utilities.CUSTOM_FONT);
		menuBar.add(mnLayout);

		JMenuItem mntmCircular= new JMenuItem("Circular");
		mntmCircular.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/circular-layout.gif")));
		mntmCircular.addActionListener(new CircularLayoutAction());
		mnLayout.add(mntmCircular);

		JMenuItem mntmOrganic= new JMenuItem("Organic");
		mntmOrganic.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/organicLayout.png")));
		mntmOrganic.addActionListener(new OrganicLayoutAction());
		mnLayout.add(mntmOrganic);

		JMenuItem mntmOrthogonal= new JMenuItem("Orthogonal");
		mntmOrthogonal.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/layout_orthogonal.jpg")));
		mntmOrthogonal.addActionListener(new OrthogonalLayoutAction());
		mnLayout.add(mntmOrthogonal);
	}

	private void makeLayPanels(JDesktopPane  container){	

		lvPanels=new ArrayList<JPanel>();
		int x=340;
		int y=6;
		int width=1250;
		int height=350;
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		int width=screenSize.width-
		for(int i= 0; i<Utilities.INSTANCES;i++){
			ToolBarsPanel laypanel= new ToolBarsPanel(i==0);
			laypanel.setBounds(x, y + height*i , width , height);
			laypanel.setBorder( BorderFactory.createEtchedBorder());
//			laypanel.setPreferredSize(preferredSize);
			laypanel.setName("laypanel-"+i);
			lvPanels.add(laypanel);
			container.add(laypanel);
		}
	}

	private void makeInfoTabPane(JDesktopPane container){

		infoPane= new JTabbedPane(JTabbedPane.TOP);   	 
		infoPane.setBounds(5, 348, 310, 310);
		//LineBorder line= new LineBorder(new Color(153,153,255),2,true);
		//infoTab.setBorder(BorderFactory.createTitledBorder(line, "Info"));
		infoPane.setFont(Utilities.CUSTOM_FONT);
		infoPane.setBackground(new Color(224,224,224));			
		container.add(infoPane);
		
		
	}
	
	
	private void createCrossingsPanel(){
		
				crossingspanel = new JPanel();
				crossingspanel.setLayout(new BoxLayout(crossingspanel, BoxLayout.Y_AXIS));
				crossingspanel.setBackground(Color.WHITE);
				infoPane.addTab("Crossings Panel",crossingspanel);
				crossingspanel.setFont(Utilities.CUSTOM_FONT);
				crossingspanel.setVisible(true);
	}


	//=================================================================	
	//GETTERS
	//=================================================================
	public ArrayList<JPanel> getLVPanels()
	{
		return this.lvPanels;
	}

	public Graph2DView getViewOriginal() {
		return viewOriginal;
	}


	//=================================================================	
	//ACTIVATE-RESET COLOR BARS
	//=================================================================
	public void setBarsActiveColors(int[][] activeColors)
	{
		for(int i=0;i<lvPanels.size();i++)
		{
			((ToolBarsPanel)lvPanels.get(i)).setBarsActiveColors(activeColors[i]);
		}
	}

	public void resetColorBars()
	{
		for(int i=0;i<lvPanels.size();i++)
		{
			((ToolBarsPanel)lvPanels.get(i)).resetToolBars();
		}	    
	}

	//=================================================================
	//=================================================================
	//=================         A C T I O N S         =================

	
	//=================================================================
	//HELP ACTION
	//=================================================================
	class HelpAction extends AbstractAction{
		
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {
			System.out.println(" help action");
			new HelpFrame();
			
		}
	}//help

	//=================================================================	
	//OPEN ACTION
	//=================================================================

	public void load(Graph2D graph)
    {
		viewOriginal.getGraph2D().clear();
		viewOriginal.setGraph2D(graph);
		viewOriginal.fitContent();
		viewOriginal.updateView();
	    
    }
	public void openFile(String name)
	{
		if(name.endsWith(".be"))
		{
			this.mainControler.openEmbedded(name);
		}
		if (name.endsWith(".gml")){
			GMLIOHandler ioh = new GMLIOHandler();
			try
			{
				viewOriginal.getGraph2D().clear();
				ioh.read(viewOriginal.getGraph2D(),name);

				viewOriginal.setVisible(true);
				this.mainControler.open(viewOriginal.getGraph2D());

			} catch (IOException ioe)
			{
				D.show(ioe);
			}
		} 
		viewOriginal.fitContent();
		viewOriginal.getGraph2D().updateViews();
		
		

		if(infoPane!=null)
			infoPane.removeAll();
		createCrossingsPanel();
	}


	class OpenAction extends AbstractAction{

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("------------OPENING FILE------------");
			JFileChooser chooser = new JFileChooser();
			if(chooser.showOpenDialog(BeeGui.this) == JFileChooser.APPROVE_OPTION)
			{
				String name = chooser.getSelectedFile().toString();
				openFile(name);
			}
		}
	}//open


	//=================================================================	
	//SAVE ACTION
	//=================================================================
	class SaveBEAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			if(chooser.showSaveDialog(BeeGui.this) == JFileChooser.APPROVE_OPTION)
			{
				String name = chooser.getSelectedFile().toString();
				if(name.endsWith(".be"))
				{
					try
					{
						mainControler.saveEmbeddedGraph(name);
					}
					catch(Exception exc)
					{
						exc.printStackTrace();
					}
				}
				if (name.endsWith(".gml")){
					GMLIOHandler ioh = new GMLIOHandler();
					try
					{

						Graph2D embeddedGraph	=	mainControler.getBEGraph();
						ioh.write(embeddedGraph,name);// Writes the contents of the given graph in GML format to a stream
						
					} catch (IOException ioe){
						D.show(ioe);
					}
				}//end if
			}//end if
		}//actionPerformed		
		
	}//AbstractAction


	//=================================================================	
	//EDIT MODE ACTION
	//=================================================================	
	class EditModeAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {										
			//			EditView editView = new EditView(viewOriginal,(BeeGui)SwingUtilities.getWindowAncestor(contentPane));	
			new EditView(viewOriginal,(BeeGui)SwingUtilities.getWindowAncestor(getContentPane()));	
		}
	}

	//=================================================================	
	//UNDO-REDO ACTION
	//=================================================================
	class RedoAction extends  AbstractAction{
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
		mainControler.redo();
		}
	}

	class UndoAction extends  AbstractAction{
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			mainControler.undo();
		}
	}


	//=================================================================	
	//CREATE BOOK EMBEDDING ACTION
	//=================================================================	

	private void createLayoutView(Graph2DView graph2dview, int numPages){

		mainControler.createLayoutView(graph2dview,numPages);

	}//createLayoutView

	private OptionHandler createOptionHandler(){

		OptionHandler op = new OptionHandler("Options");

		op.addInt("number of pages",2 );
		return op;

	}//createOptionHandler

	class CreateBEAction extends AbstractAction{

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e)
		{
			OptionHandler op = createOptionHandler();
			if( op != null) {
				if( !op.showEditor() ){
					return;
				}
				if((op.getInt("number of pages")<1)||(op.getInt("number of pages")>Utilities.TOTAL_PAGES)){

					JOptionPane.showMessageDialog(lvPanels.get(0),
							"The number of pages should belong to range  [1,"+Utilities.TOTAL_PAGES+"]",
							"Forbidden!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			int pages=op.getInt("number of pages");
			createLayoutView(viewOriginal,pages);
		}//action performed
	}//launch


	//=================================================================	
	//FIT CONTENT ACTION
	//=================================================================		

	class FitContentAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {
			mainControler.fitContent();
		}
	}


	//=================================================================	
	//HEURISTIC ACTION
	//=================================================================		

	class HeuristicAction extends  AbstractAction{

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {
		mainControler.heuristic();
		}
	}


	//=================================================================	
	//ANALYSIS ACTION
	//=================================================================		

	public boolean planarityChecker(JPanel panel){

		//		GraphChecker checker = new GraphChecker();

		boolean	planar=  GraphChecker.isPlanar(viewOriginal.getGraph2D());

//		if(planar){
//
//			JFrame editViewFrame=new JFrame("planar Mode");				        	 
//			editViewFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(BeeGui.class.getResource("/javaguiresources/250px-Petersen_double_cover.svg.png")));
//			editViewFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//			Graph2DView planarView = new Graph2DView();
//			planarView.setGraph2D((Graph2D)viewOriginal.getGraph2D().createCopy());
//			//			EditMode mode= createEditMode();
//			EditMode mode=new MyEditModeLooseConstraint();
//			planarView.addViewMode(mode);
//			editViewFrame.getContentPane().add(planarView);
//			Graph graph=(Graph)planarView.getGraph2D();
////			EdgeCursor ec =graph.edges();
////			for(ec.toFirst();ec.ok();ec.next()){
////
////				EdgeRealizer er =((Graph2D)graph).getRealizer( ec.edge());
////				PolyLineEdgeRealizer  per = new PolyLineEdgeRealizer ( er);		
////				((Graph2D) graph).setRealizer(ec.edge(), per);
////				int bc=er.bendCount();
////				if(bc>0){
////					System.out.println("you  have bends!");
//////					per.clearBends();
////				}
////			}//make arcs lines!!
//
//			PlanarInformation planarinfo= new PlanarInformation(graph);	
//			planarinfo.showFaces();
//
//			//			planarinfo.doEdgeRecovery();
//			//			planarinfo.checkEdgeRecovery();
//			//			planarinfo.showEdgeRecoveryInfo(true);
//			DrawingEmbedder de= new DrawingEmbedder();
//			de.setPlanarInformation(planarinfo);
//			de.setKeepBends(true);
//			de.embed();
//			
//			YList fl=planarinfo.faceList;
//			for(int k=0;k<fl.size();k++)
//			{
//				Face face=(Face)fl.elementAt(k);
//				System.out.println(face.toString());
//			}
//			//planarinfo.showFaces();
//			planarView.fitContent();
//			planarView.updateView();
//			planarView.setVisible(true);
//			editViewFrame.pack();
//			editViewFrame.setVisible(true);
//		}//if planar
		return planar;
	}//graphchecker

	public boolean graphBiconnected(JPanel panel){
		//		GraphConnectivity gc= new GraphConnectivity();
		boolean biconnected=  GraphConnectivity.isBiconnected(viewOriginal.getGraph2D());
		return biconnected;
	}//graphBiconnected

	public boolean graphConnected(JPanel panel){

		//		GraphConnectivity gc= new GraphConnectivity();		
		boolean connected = GraphConnectivity.isConnected(viewOriginal.getGraph2D());
		if(!connected){
			Graph2D g= (Graph2D)viewOriginal.getGraph2D();
			NodeList[] nodelist= GraphConnectivity.connectedComponents(viewOriginal.getGraph2D());

			if(graphList!=null){
				graphList.clear();
			}
			for(int i=0;i<nodelist.length;i++){	

				NodeCursor nc= nodelist[i].nodes();
				Graph2D graph= new Graph2D(g, nc);
				graphList.add(graph);

				int k=Utilities.TOTAL_PAGES;
				for(nc.toFirst();nc.ok();nc.next()){
					NodeRealizer nr =g.getRealizer(nc.node());
					if(i<Utilities.TOTAL_PAGES){
						nr.setFillColor(Utilities.COLORS[i]);
						nr.setLineType(LineType.LINE_2);
						nr.getLabel().setFont(new Font("Serif", Font.BOLD, 18));
						conNodeMap.put(nc.node(), i);
					}
					else{
						Color randomColor= new Color((int)(Math.random() * 0x1000000));
						nr.setFillColor(randomColor.darker() );
						nr.getLabel().setTextColor(Color.WHITE);//???
						conNodeMap.put(nc.node(), k);
						k++;
					}
				}	//gia ka8e node 
			}//for

			NodeCursor nc1= g.nodes();
			for(nc1.toFirst();nc1.ok();nc1.next()){
				System.out.println("conNodeMap "+nc1.node()+" "+  conNodeMap.get(nc1.node()));
			}
			viewOriginal.updateView();
		}//isnt connected
		return connected;
	}//graphconnected
	
	
	

	public void functionality(){	
		
		EditMode editMode=new MyEditModeConstraint();
		viewOriginal.addViewMode(editMode);
		viewOriginal.getCanvasComponent().addMouseWheelListener(new Graph2DViewMouseWheelZoomListener());
		final boolean firstTime =true;
		viewOriginal.getGraph2D().addGraph2DSelectionListener(new Graph2DSelectionListener(){
			@Override
			public void onGraph2DSelectionEvent(Graph2DSelectionEvent event) {				

				if(event.isNodeSelection()){
					Node node=(Node)event.getSubject();
					if(conNodeMap!=null){
						int	i=conNodeMap.get(node);
						System.out.println("you are in connected component "+i);
						Graph2D graph2d= graphList.get(i);
						//mainControler.openInGraph2DViews(graph2d,firstTime);
						mainControler.openInGraph2DViews(graphList,firstTime);
						System.out.println("set the graph componet to layview");
					}//if 
				}//addGraph2DSelectionListener
			}
		});
	}//functionality

	class AnalysisAction extends AbstractAction{

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e){

			Graph graph = viewOriginal.getGraph2D();
			int  nodeCount = graph.nodeCount();
			int edgeCount = graph.edgeCount();
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBackground(Color.WHITE);
			infoPane.addTab("Graph Analysis",panel);
			infoPane.setFont(Utilities.CUSTOM_FONT);
			boolean connected=graphConnected(panel);
			boolean biconnected=graphBiconnected(panel);
			boolean planar=planarityChecker(panel);
			String[] columnProperties={"",""};
			Object [][] data={{"Number Of Nodes",Integer.toString(nodeCount)},
					{"Number Of Edges",Integer.toString(edgeCount)},
					{"Connected",connected},{"Biconnected",biconnected},
					{"Planar",planar}};
			JTable  table = new JTable(data, columnProperties);
			
			table.setShowVerticalLines(false);
			table.setShowHorizontalLines(false);			
			LineBorder line= new LineBorder(new Color(153,153,255),1,true);
			table.setBorder(BorderFactory.createTitledBorder(line));
			table.setDefaultRenderer(Object.class, new ColorRenderer());
			
			panel.add(table);
			table.setVisible(true);
			panel.updateUI();
			functionality();
		}
	}//AnalysisAction


	//=================================================================	
	//LAYOUTS
	//=================================================================
	class CircularLayoutAction extends AbstractAction{

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			CircularLayouter cl = new CircularLayouter();
			if(viewOriginal.getGraph2D()!=null){
				cl.doLayout(viewOriginal.getGraph2D());
				viewOriginal.fitContent(); 
				viewOriginal.updateView();
			}		
		}		
	}

	class OrganicLayoutAction  extends AbstractAction{

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			OrganicLayouter ol = new OrganicLayouter(); 
			ol.setPreferredEdgeLength(40);
			if(viewOriginal.getGraph2D()!=null){
				ol.doLayout(viewOriginal.getGraph2D());
				viewOriginal.fitContent(); 
				viewOriginal.updateView();
			}	

		}

	}

	class OrthogonalLayoutAction extends AbstractAction{

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			OrthogonalLayouter rl = new OrthogonalLayouter(); 
			if(viewOriginal.getGraph2D()!=null){
				rl.doLayout(viewOriginal.getGraph2D());
				viewOriginal.fitContent(); 
				viewOriginal.updateView();
			}		
		}		
	}


	//=================================================================	
	//=================================================================	
	//CUSTOM COLOR BARS CLASS
	//=================================================================

	class ToolBarsPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		JToolBar upToolBar;
		JToolBar downToolBar;
		boolean showAll;

		public ToolBarsPanel(boolean showAll) {
			super(new BorderLayout());
			createToolBars();	
			this.showAll=showAll;
		}

		public boolean isShowAll() {
			return showAll;
		}

		public void setBarsActiveColors(int[] pageIndex){
			System.out.println(Arrays.toString(pageIndex));
			for(int i=0;i<pageIndex.length;i++)
			{
				if(pageIndex[i]==Utilities.PAGE_UP)
				{
					((JCheckBox)this.upToolBar.getComponentAtIndex(i)).setSelected(true);
					((JCheckBox)this.downToolBar.getComponentAtIndex(i)).setSelected(false);
				}

				else if(pageIndex[i]==Utilities.PAGE_DOWN)
				{
					((JCheckBox)this.upToolBar.getComponentAtIndex(i)).setSelected(false);
					((JCheckBox)this.downToolBar.getComponentAtIndex(i)).setSelected(true);
				}
				else
				{
					((JCheckBox)this.upToolBar.getComponentAtIndex(i)).setSelected(false);
					((JCheckBox)this.downToolBar.getComponentAtIndex(i)).setSelected(false);
				}
			}
		}

		public int[] getBarsActiveColors()
		{
			int[] pageIndex=new int[Utilities.COLORS.length];
			for(int i=0;i<pageIndex.length;i++)
			{
				if(((JCheckBox)this.upToolBar.getComponentAtIndex(i)).isSelected())
				{
					pageIndex[i]=Utilities.PAGE_UP;
				}
				else if(((JCheckBox)this.downToolBar.getComponentAtIndex(i)).isSelected())
				{
					pageIndex[i]=Utilities.PAGE_DOWN;
				}
				else
					pageIndex[i]=Utilities.PAGE_HIDE;
			}
			return pageIndex;
		}

		public void createToolBars(){
			upToolBar= new JToolBar();
			upToolBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);	
			upToolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
			this.add(upToolBar,BorderLayout.NORTH);
			for(int j=0;j<Utilities.COLORS.length;j++){
				final JCheckBox chckbxCol = new JCheckBox(Utilities.COLOR_NAMES[j]);
				chckbxCol.setName("up-"+Utilities.COLOR_NAMES[j]);
				upToolBar.add(chckbxCol);	
				//				chckbxCol.addItemListener(new ColorSelectionAction());
				chckbxCol.addActionListener(new ColorSelectionAction());
				chckbxCol.setSelected(false);
				chckbxCol.setBorderPainted(true);
				chckbxCol.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Utilities.COLORS[j]));
			}

			downToolBar= new JToolBar();
			downToolBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);	
			downToolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
			this.add(downToolBar,BorderLayout.SOUTH);
			for(int j=0;j<Utilities.COLORS.length;j++){
				final JCheckBox chckbxCol = new JCheckBox(Utilities.COLOR_NAMES[j]);
				chckbxCol.setName("down-"+Utilities.COLOR_NAMES[j]);
				downToolBar.add(chckbxCol);	
				//				chckbxCol.addItemListener(new ColorSelectionAction());
				chckbxCol.addActionListener(new ColorSelectionAction());
				chckbxCol.setSelected(false);
				chckbxCol.setBorderPainted(true);
				chckbxCol.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Utilities.COLORS[j]));
			}
		}

		public void resetToolBars()
		{
			int[] zeros=new int[Utilities.COLORS.length];
			for(int i=0;i<zeros.length;i++)
				zeros[i]=Utilities.PAGE_HIDE;
			this.setBarsActiveColors(zeros);
		}

		class ColorSelectionAction implements ActionListener{
			//		class ColorSelectionAction implements ItemListener{

			@Override
			//			public void itemStateChanged(ItemEvent e) {
			public void actionPerformed(ActionEvent e) {
				JCheckBox checkbox=(JCheckBox) e.getSource();
				JPanel panel=(JPanel) upToolBar.getParent();
				int index;
				int page=Utilities.PAGE_HIDE;
				String colorname=checkbox.getName();
				//				System.out.println(checkbox.getName());
				if(checkbox.isSelected()){

					if(colorname.startsWith("up")){
						index=upToolBar.getComponentIndex(checkbox);
						((JCheckBox)downToolBar.getComponentAtIndex(index)).setSelected(false);
						page=Utilities.PAGE_UP;
					}
					else
					{
						index=downToolBar.getComponentIndex(checkbox);
						((JCheckBox)upToolBar.getComponentAtIndex(index)).setSelected(false);
						page=Utilities.PAGE_DOWN;
					}
					mainControler.showPage(panel,index, page);
				}
				else
				{
					if(showAll)
					{						
						if(colorname.startsWith("up")){
							index=upToolBar.getComponentIndex(checkbox);
							((JCheckBox)downToolBar.getComponentAtIndex(index)).setSelected(true);
							page=Utilities.PAGE_DOWN;
						}
						else
						{
							index=downToolBar.getComponentIndex(checkbox);
							((JCheckBox)upToolBar.getComponentAtIndex(index)).setSelected(true);
							page=Utilities.PAGE_UP;
						}
						mainControler.showPage(panel,index, page);
					}
					else
					{
						if(colorname.startsWith("up")){
							index=upToolBar.getComponentIndex(checkbox);
						}
						else
						{
							index=downToolBar.getComponentIndex(checkbox);
						}
						mainControler.hidePage(panel,index);
					}
				}
				
				mainControler.updateCrossings();//
			}
		}
	}//class










	



	//	  private void openInGraph2DViews(Graph2D graph2d, boolean firstTime){
	//		  for(int i=0;i<layviewlist.size();i++){
	//			  
	//			  if(firstTime){  
	//			  MyLayouter layouter= new MyLayouter();
	//			  layouter.setNumberOfPages(pages);
	//			  layouter.doLayoutCore(graph2d);
	//			  layouter.underSpineEdges(graph2d);
	//			  firstTime=false;}
	//			  
	//			  layviewlist.get(i).getGraph2D().clear();
	//			  layviewlist.get(i).setGraph2D(graph2d);
	//			  layviewlist.get(i).updateView();//TODO active colors check it
	//			  //.setVisible(true);
	//		  }
	//	  }

























	public void upadateCrossingInfoPane(Map<Color, Integer> crossingsMap) {

		crossingspanel.removeAll();
		
		

		BorderLayout bl= new BorderLayout();
		
		JLabel label = new JLabel("  ");
		
		label.setFont(Utilities.CUSTOM_FONT);
		
		label.setVerticalAlignment(SwingConstants.CENTER);
		
		crossingspanel.add(label,bl.NORTH);
		
		crossingspanel.setBackground(new Color(255,250,240));

		String[] columnProperties={" Color ", " # Crossings"};
		Object [][] data= new  Object [ crossingsMap.size()] [];
		
		

		for(int i=0;i<crossingsMap.size();i++){

			Object	[] dataItem	= { Utilities.COLOR_NAMES[i],Integer.toString(crossingsMap.get(Utilities.COLORS[i]))};
			data[i]=dataItem;
		}

		
		
		DefaultTableModel model = new DefaultTableModel(data, columnProperties);

		JTable  table = new JTable(model);
		table.setShowVerticalLines(false);
		//table.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
		//LineBorder line= new LineBorder(new Color(153,153,255),1,true);
		//table.setBorder(BorderFactory.createTitledBorder(line));
		
		
		table.setFont(Utilities.CUSTOM_FONT);

		TableColumn column = null;
		for (int i = 0; i < 2; i++) {

			column = table.getColumnModel().getColumn(i);

			column.setPreferredWidth(100); 

			column.setMinWidth(100);
			
			column.setMaxWidth(100);

			//column.setCellRenderer(new MyColorRenderer());
		}


		table.setDefaultRenderer(Object.class, new ColorRenderer());


		crossingspanel.add(table,bl.LINE_START);
		table.setVisible(true);		


		crossingspanel.updateUI();


	}//upadateCrossingInfoPane

	
	
	public class ColorRenderer  extends DefaultTableCellRenderer  {


		public Component getTableCellRendererComponent(	JTable table, Object color,	boolean isSelected, boolean hasFocus,	int row, int column) {

			Component c = super.getTableCellRendererComponent(table, color, isSelected, hasFocus, row, column);		

			Object valueAt = table.getModel().getValueAt(row, column);
			String s = "";
			if (valueAt != null) {
				s = valueAt.toString();
			}

		
			Color colorBack = new Color(255,255,240);

			if (s.equalsIgnoreCase("Cyan"))				
			{
				c.setForeground(Color.cyan.darker());				
				c.setBackground(colorBack);
				
			} else if(s.equalsIgnoreCase("Pink")) {
				c.setForeground(Color.pink.darker());				
				c.setBackground(colorBack);
			}
			else if (s.equalsIgnoreCase("Green")){
				
				c.setForeground(Color.green.darker());				
				c.setBackground(colorBack);
			}
			else if (s.equalsIgnoreCase("Black")){
				
				c.setForeground(Color.black.darker());				
				c.setBackground(colorBack);
			}
			else if (s.equalsIgnoreCase("Light Gray")){
				
				c.setForeground(Color.lightGray.darker());				
				c.setBackground(colorBack);
			}
			else if (s.equalsIgnoreCase("Magenta")){
				
				c.setForeground(Color.magenta.darker());				
				c.setBackground(colorBack);
			}
			else if (s.equalsIgnoreCase("Red")){
				
				c.setForeground(Color.red.darker());				
				c.setBackground(colorBack);
			}
			
			else if (s.equalsIgnoreCase("Orange")){
				
				c.setForeground(Color.orange.darker());				
				c.setBackground(colorBack);
			}
			
			else if(s.equalsIgnoreCase("Gray")){
				Color col=Color.gray.darker();
				c.setForeground(Color.gray.darker());				
				c.setBackground(colorBack);				
			}
			else if(s.equalsIgnoreCase("Dark Gray")){
				
				c.setForeground(Color.darkGray.darker());				
				c.setBackground(colorBack);
			}else{
				
				 c.setForeground(Color.black);
		         c.setBackground(colorBack);
			}
			
//			LineBorder line= new LineBorder(,1,true);
//			c.setBorder(BorderFactory.createTitledBorder(line));
			c.setFont(Utilities.CUSTOM_FONT);

			return c;
		}


	}
	
	

	
		
	
	

	
	//*****************************************
	
	
	public void disableUndo() {
		
		undo.setEnabled(false);		
		
	}
	

	public void disableRedo() {
		
		redo.setEnabled(false);
		
	}

	public void enableUndo() {
		undo.setEnabled(true);
		
	}

	public void enableRedo() {
		redo.setEnabled(true);		
	}
	
	
	


}//class





