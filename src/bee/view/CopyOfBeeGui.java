package bee.view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;












//import bee.view.ToolBarsPanel.ColorSelectionAction;
import y.algo.GraphChecker;
import y.algo.GraphConnectivity;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.EdgeList;
import y.base.Graph;
import y.base.Node;
import y.base.NodeCursor;
import y.base.NodeList;
import y.io.GMLIOHandler;
import y.layout.circular.CircularLayouter;
import y.layout.hierarchic.EdgeReverser;
import y.layout.organic.OrganicLayouter;
import y.layout.orthogonal.OrthogonalLayouter;
import y.layout.planar.DrawingEmbedder;
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
import y.view.View;
import bee.demo.MainControler;
import bee.layout.MySimpleLayouter;
import bee.option.EdgePropertyHandler;
import bee.option.MyEditModeConstraint;
import bee.option.MyEditModeLooseConstraint;
import bee.option.options.Utilities;

public class CopyOfBeeGui extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	protected Graph2DView viewOriginal;

	Map<Node, Integer>conNodeMap =new HashMap<Node,Integer>();
	LinkedList<Graph2D> graphList= new LinkedList<Graph2D> ();

	//	private ArrayList<JPanel> lvPanels;
	private ArrayList<Graph2DView> views;
	//	private MainControler mainControler;


	//	JTabbedPane infoPane;



	//Graph2DUndoManager undo;

	//static Map<Color,Integer> crossingsMap= new HashMap<Color,Integer>();


	//	public static  int pages=2;
	//JPanel planaritypanel;


	//List  myColors;
	//	static Font myfont=new Font("Serif", Font.PLAIN, 15);


	//	static Color [] col= {Color.cyan,Color.pink,Color.green,Color.black,Color.lightGray,Color.magenta,Color.red,Color.orange, Color.gray,Color.darkGray};
	//	String[] colorsName={"Cyan","Pink","Green","Black","Light Gray","Magenta","Red","Orange","Gray","Dark Gray"};






	/**
	 * Create the frame.
	 */
	public CopyOfBeeGui() {
		initGui();
	}//constr

	/**
	 * SETUP CONTROLER
	 */
	//	public void setMainControler(MainControler contr)
	//	{
	//		this.mainControler=contr;
	//	}

	//=================================================================	
	//CREATE GUI
	//=================================================================
	private void initGui(){


		this.setIconImage(Toolkit.getDefaultToolkit().getImage(CopyOfBeeGui.class.getResource("/javaguiresources/250px-Petersen_double_cover.svg.png")));
		this.setForeground(Color.PINK);

		this.setTitle("Bee 1.0");
		this.setFont(Utilities.CUSTOM_FONT);
		this.setBounds(0, 0, 1300, 602);

		this.buildMainMenu();


		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);


		//		this.buildToolBars(contentPane);


		//		JScrollPane scrol=new JScrollPane();

		JPanel desktopPane = new JPanel(new GridBagLayout());
		//		scrol.add(desktopPane);
		//		scrol.setViewportView(desktopPane);
		desktopPane.setBackground(new Color(245, 245, 220));		
		desktopPane.setVisible(true);
		GridBagConstraints gbc_desktopPane = new GridBagConstraints();
		gbc_desktopPane.insets = new Insets(0, 0, 0, 5);
		gbc_desktopPane.fill = GridBagConstraints.BOTH;
		gbc_desktopPane.gridx = 0;
		gbc_desktopPane.gridy = 1;
		contentPane.add(desktopPane, gbc_desktopPane);
		//		desktopPane.setBounds(340,6);
		//		contentPane.add(scrol);

		this.makeVieworiginal(desktopPane);

		this.makeLayPanels(desktopPane);

		//		this.makeInfoTabPane(desktopPane);

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

		mntmOpen_1.setIcon(new ImageIcon(CopyOfBeeGui.class.getResource("/javaguiresources/11949984141591900034fileopen.svg.med.png")));
		mnFile.add(mntmOpen_1);

		mnFile.addSeparator();

		JMenuItem mntmSaveBE = new JMenuItem("Save book embedding");
		mntmSaveBE.addActionListener(new SaveBEAction() );
		mntmSaveBE.setIcon(new ImageIcon(CopyOfBeeGui.class.getResource("/javaguiresources/save.png")));
		mnFile.add(mntmSaveBE);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		mnFile.add(mntmExit);





		JMenu mnView = new JMenu("View");
		mnView.setMnemonic(KeyEvent.VK_V);
		mnView.setFont(Utilities.CUSTOM_FONT);
		menuBar.add(mnView);

		JMenuItem mntmLaunchLayout = new JMenuItem("Create Book Embedding");
		mntmLaunchLayout.addActionListener(new CreateBEAction() );
		mntmLaunchLayout.setIcon(new ImageIcon(CopyOfBeeGui.class.getResource("/javaguiresources/layout.gif")));
		mnView.add(mntmLaunchLayout);

		mnView.addSeparator();




		JMenu mnHelp = new JMenu("Help");
		mnHelp.setFont(Utilities.CUSTOM_FONT);
		menuBar.add(mnHelp);

		JMenu mnAbout = new JMenu("About");
		mnAbout.setFont(Utilities.CUSTOM_FONT);
		menuBar.add(mnAbout);
	}



	private void makeVieworiginal(JPanel container){

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


	}

	private void makeLayPanels(JPanel  container){	

		//		lvPanels=new ArrayList<JPanel>();
		JPanel main=new JPanel(new GridLayout(Utilities.INSTANCES,1));
		int x=340;
		int y=6;
		int width=1250;
		int height=300;
		views=new ArrayList<Graph2DView>();
		//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//		int width=screenSize.width-
		for(int i= 0; i<Utilities.INSTANCES;i++){
			Graph2DView currentView=new Graph2DView();
			ToolBarsPanel laypanel= new ToolBarsPanel(i==0);
			laypanel.setBounds(x, y + height*i , width , height);
			laypanel.setBorder( BorderFactory.createEtchedBorder());
			//			laypanel.setPreferredSize(preferredSize);
			laypanel.setName("laypanel-"+i);
			currentView.getGraph2D().clear();
			//			System.out.println(layview);
			currentView.getCanvasComponent().setFont(new Font("Serif", Font.PLAIN, 12));
			currentView.getCanvasComponent().setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			currentView.getCanvasComponent().setAlignmentY(Component.TOP_ALIGNMENT);
			currentView.getCanvasComponent().setAlignmentX(Component.LEFT_ALIGNMENT);
			currentView.getCanvasComponent().setBackground(new Color(253, 245, 230));
			currentView.setBounds(340, 5, 1250 , 220);
			currentView.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.LOWERED, null, null, new Color(191, 205, 219), null), (Border) new LineBorder((new Color(227, 227, 227)))));
			currentView.setPreferredSize(new Dimension(1250,220));
			currentView.setVisible(true);
			views.add(currentView);
			laypanel.add(currentView, BorderLayout.CENTER);
			laypanel.setVisible(true);
			main.add(laypanel);
			main.setVisible(true);
		}
		container.add(main);

	}




	//=================================================================	
	//GETTERS
	//=================================================================


	public Graph2DView getViewOriginal() {
		return viewOriginal;
	}


	//=================================================================	
	//ACTIVATE-RESET COLOR BARS
	//=================================================================
	public void setBarsActiveColors(int[][] activeColors)
	{
		for(int i=0;i<views.size();i++)
		{
			((ToolBarsPanel)views.get(i).getParent()).setBarsActiveColors(activeColors[i]);
		}
	}

	public void resetColorBars()
	{
		for(int i=0;i<views.size();i++)
		{
			((ToolBarsPanel)views.get(i).getParent()).resetToolBars();
		}	    
	}

	//=================================================================
	//=================================================================
	//=================         A C T I O N S         =================



	//=================================================================	
	//OPEN ACTION
	//=================================================================

	public void loadViews()
	{
		for(int i=0;i<views.size();i++)
		{
			views.get(i).getGraph2D().clear();//TODO EXEI PROVLHMA
			views.get(i).updateView();
		}
	}
	public void openFile(String name)
	{
		//TODO open custom file extension
		if (name.endsWith(".gml")){
			GMLIOHandler ioh = new GMLIOHandler();
			try
			{
				viewOriginal.getGraph2D().clear();
				ioh.read(viewOriginal.getGraph2D(),name);

				viewOriginal.setVisible(true);
				loadViews();
				resetColorBars();
				//				this.mainControler.open(viewOriginal.getGraph2D());

			} catch (IOException ioe)
			{
				D.show(ioe);
			}
		} 
		viewOriginal.fitContent();
		viewOriginal.getGraph2D().updateViews();

	}

	class OpenAction extends AbstractAction{

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("------------OPENING FILE------------");
			JFileChooser chooser = new JFileChooser();
			if(chooser.showOpenDialog(CopyOfBeeGui.this) == JFileChooser.APPROVE_OPTION)
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
			if(chooser.showSaveDialog(CopyOfBeeGui.this) == JFileChooser.APPROVE_OPTION)
			{
				String name = chooser.getSelectedFile().toString();
				//TODO save as extention file and take under consideration if no embedding has been computed
				if (name.endsWith(".gml")){
					GMLIOHandler ioh = new GMLIOHandler();
					try
					{

						//						Graph2D embeddedGraph	=	mainControler.getBEGraph();
						ioh.write(viewOriginal.getGraph2D(),name);// Writes the contents of the given graph in GML format to a stream
						//TODO if i dont have embd graph save the view original graph
					} catch (IOException ioe){
						D.show(ioe);
					}
				}//end if
			}//end if
		}//actionPerformed		
	}//AbstractAction







	//=================================================================	
	//CREATE BOOK EMBEDDING ACTION
	//=================================================================	

	private void createLayoutView(int numPages){

		Graph2D singleGraph=(Graph2D)viewOriginal.getGraph2D().createCopy();
		MySimpleLayouter layouter=new MySimpleLayouter();
		layouter.setNumberOfPages(numPages);
		singleGraph=layouter.doLayoutCore(singleGraph);
		int[][] pages=Utilities.createActiveColors();
		for(int i=0;i<views.size();i++)
		{
			views.get(i).setGraph2D(singleGraph);
			//		singleGraph.registerView(views.get(i));
			for(int j=0;j<pages[i].length;j++)
			{
				if(pages[i][j]!=0)
					showPage((JPanel) views.get(i).getParent(),j,pages[i][j]);
				else
					hidePage((JPanel) views.get(i).getParent(),j);
			}
			views.get(i).updateView();
		}
		//		singleGraph.updateViews();
		setBarsActiveColors(Utilities.createActiveColors());
		//		mainControler.createLayoutView(graph2dview,numPages);

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

					JOptionPane.showMessageDialog(views.get(0).getComponent(),
							"The number of pages should belong to range  [1,"+Utilities.TOTAL_PAGES+"]",
							"Forbidden!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			int pages=op.getInt("number of pages");
			createLayoutView(pages);
		}//action performed
	}//launch



	public void showPage(JPanel panel, int index, int page)
	{
		BorderLayout layout=(BorderLayout) panel.getLayout();
		Graph2DView view=(Graph2DView) layout.getLayoutComponent(BorderLayout.CENTER);
		EdgeCursor ec=view.getGraph2D().edges();
		EdgeReverser erev =new EdgeReverser();	
		EdgeList list=new EdgeList();
		for (ec.toFirst(); ec.ok(); ec.next()){	
			Edge edge= (Edge)ec.current();
//			view.set
			EdgeRealizer er= view.getGraph2D().getRealizer(edge);
			if(er.getLineColor().equals(Utilities.COLORS[index]))
			{
				er.setVisible(true);
				Node source=edge.source();		
				Node target= edge.target();

				NodeRealizer nrt =view.getGraph2D().getRealizer(target);
				NodeRealizer nrs =view.getGraph2D().getRealizer(source);

				if(nrt.getCenterX()<nrs.getCenterX() && page==Utilities.PAGE_UP ||
						nrt.getCenterX()>nrs.getCenterX() && page==Utilities.PAGE_DOWN){
					er.clearBends();
					list.add(ec.edge());				
				}
			}
		}
		//		System.out.println("     reversing "+list.size()+" edges");
		erev.reverseEdges(view.getGraph2D(), list);	
		view.updateView();
		//		System.out.println("show page-number "+index+" on drawing page "+page+" for panel "+panel.getName());
		//		lvcontroler.showPage(panel, index, page);
	}

	public void hidePage(JPanel panel, int index)
	{
		System.out.println("hide page-number "+index+" for panel "+panel.getName());
		//		lvcontroler.hidePage(panel, index);
		BorderLayout layout=(BorderLayout) panel.getLayout();
		Graph2DView view=(Graph2DView) layout.getLayoutComponent(BorderLayout.CENTER);
		EdgeCursor ec=view.getGraph2D().edges();
		for (ec.toFirst(); ec.ok(); ec.next()){	
			Edge edge= (Edge)ec.current();
			EdgeRealizer er= view.getGraph2D().getRealizer(edge);
			if(er.getLineColor().equals(Utilities.COLORS[index]))
			{
				er.setVisible(false);
			}
		}
		view.updateView();
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
					showPage(panel,index, page);
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
						showPage(panel,index, page);
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
						hidePage(panel,index);
					}
				}
			}
		}
	}//class










	//	protected EditMode createEditModeViewOriginal() 
	//
	//
	//	{
	//
	//		EditMode editMode= new EditMode();	    
	//		editMode.allowResizeNodes(false);
	//		editMode.allowEdgeCreation(false);
	//		editMode.allowBendCreation(false);
	//		editMode.allowNodeCreation(false);
	//		editMode.allowMoving(false);
	//		editMode.allowMoveSelection(false);
	//
	//		return editMode;     
	//	}



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





















	class EdgePropertyEditorAction extends AbstractAction{

		private static final long serialVersionUID = 1L;

		//		EdgePropertyHandler edgeHandler;	  

		public void actionPerformed(ActionEvent e){
			//			Graph2D graph = layview.getGraph2D();
			//			MyGraph2DView view=layview;
			//
			//			EdgePropertyHandler edgeHandler= createEdgePropertyHandler();
			//			edgeHandler.updateValuesFromSelection(graph);
			//			if(edgeHandler.showEditor(view.getFrame())) {
			//				edgeHandler.commitEdgeProperties(graph);
			//				graph.updateViews();
			//				checkCrossings(view);
			//			}
			//			mainControler.movePages();




		}
	}//EdgepropertyEditorAction



	//	public EdgePropertyHandler createEdgePropertyHandler(){
	//		//EdgePropertyHandler edgeHandler = new EdgePropertyHandler();
	//		//edgeHandler.setPages(pages);
	//		//edgeHandler.setModelGraph(layviewlist.get(0).getGraph2D());
	//		//edgeHandler.setSubViews(subViews);
	//		//return edgeHandler;
	//		return null;
	//	}








	//
	//	protected static EditMode createEditMode()
	//	{
	//
	//		EditMode editMode= new EditMode();	    
	//		editMode.allowResizeNodes(false);
	//		editMode.allowEdgeCreation(false);
	//		editMode.allowBendCreation(false);
	//		editMode.allowNodeCreation(false);
	//		//editMode.allowMoving(false);
	//
	//		return editMode;     
	//	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CopyOfBeeGui frame = new CopyOfBeeGui();

					//					MainControler controler= new MainControler();
					//					controler.setGui(frame);
					//					frame.setMainControler(controler);

					frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					//					frame.pack();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}//class





