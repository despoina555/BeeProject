package bee.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;










import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;








import bee.option.MyEditModeFree;
import y.base.Node;
import y.base.NodeCursor;
import y.io.GMLIOHandler;
import y.util.D;
import y.view.EditMode;
import y.view.Graph2D;
import y.view.Graph2DView;
import y.view.Graph2DViewMouseWheelZoomListener;
import y.view.NodeRealizer;

public class EditView extends JFrame{

	//TODO INTERN CLASS TO BEEGUI


	private static final long serialVersionUID = 1L;
	//	Graph2D graph2d;
	protected Graph2DView editgraphview;

	BeeGui gui;


	public EditView(Graph2DView inputGraph, BeeGui gui) {

		this.gui=gui;

		JFrame editViewFrame=new JFrame("Edit Mode");				        	 
		editViewFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(BeeGui.class.getResource("/javaguiresources/250px-Petersen_double_cover.svg.png")));
		editViewFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);	
		Graph2D graph2d=(Graph2D) inputGraph.getGraph2D().createCopy();

		editgraphview= new Graph2DView();
		if(graph2d!=null){
			editgraphview.setGraph2D(graph2d);
			editgraphview.fitContent();
		}
		editgraphview.getCanvasComponent().addMouseWheelListener(new Graph2DViewMouseWheelZoomListener());

//		EditMode editMode2 = createEditMode2();
		EditMode editMode2 =new MyEditModeFree();
		editMode2.setPopupMode(  new MyPopUpMode2() ); 					    
		editgraphview.addViewMode(editMode2);


		editViewFrame.getContentPane().add(editgraphview, BorderLayout.CENTER);


		editViewFrame.getContentPane().add(editgraphview,BorderLayout.CENTER);		    
		editViewFrame.getContentPane(). add(createToolBar(),BorderLayout.NORTH);


		editViewFrame.pack();
		editViewFrame.setVisible(true);
	}

	public void close()
	{
		//TODO close window after reload..
		super.dispose();
	}
	//	public Graph2D getGraph2D(){
	//		return graph2d;
	//	}

	//	public void setGraph2D(Graph2D graph){
	//		 graph2d=graph;
	//		
	//	}


	//	protected void colorizeNodes(){
	//		if(graph2d!=null){
	//			NodeCursor nc=graph2d.nodes();
	//			for(nc.toFirst();nc.ok();nc.next()){
	//				NodeRealizer nr= graph2d.getRealizer((Node)nc.current());
	//				nr.setFillColor((Color.yellow).darker());
	//			}
	//				
	//		}
	//			
	//	}//colorize

	protected void colorizeNodes(){
		if(editgraphview!=null){
			NodeCursor nc=editgraphview.getGraph2D().nodes();
			for(nc.toFirst();nc.ok();nc.next()){
				NodeRealizer nr= editgraphview.getGraph2D().getRealizer((Node)nc.current());
				nr.setFillColor((Color.yellow).darker());
			}
		}
	}//colorize

	//	  protected EditMode createEditMode2(){
	//		  EditMode editMode= new EditMode();
	//		  
	//		    editMode.allowResizeNodes(false);
	//		    editMode.allowEdgeCreation(true);
	//		    editMode.allowBendCreation(false);
	//		    editMode.allowNodeCreation(true);
	//	  
	//		    return editMode;
	//	  }


	protected JToolBar createToolBar()
	{
		JToolBar bar = new JToolBar();

		JButton saveButton = new JButton(new SaveAction());		
		saveButton.setBackground(SystemColor.menu);
		saveButton.setToolTipText("Save");
		saveButton.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/save.png")));
		bar.add(saveButton);

		JButton btnFitContent = new JButton( new FitContent());	
		btnFitContent.setToolTipText("Fit Content");
		btnFitContent.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/FitContent.gif")));
		bar.add(btnFitContent);

		JButton btnNewButton = new JButton(new DeleteSelection());
		btnNewButton.setBackground(SystemColor.menu);
		btnNewButton.setToolTipText("Delete");
		btnNewButton.setIcon(new ImageIcon(BeeGui.class.getResource("/javaguiresources/delete.png")));
		bar.add(btnNewButton);

		return bar;
	}


	class SaveAction extends AbstractAction
	{

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e)
		{
			JFileChooser chooser = new JFileChooser();
			if(chooser.showSaveDialog(EditView.this) == JFileChooser.APPROVE_OPTION)
			{
				String name = chooser.getSelectedFile().toString();	        
				if (name.endsWith(".gml")){
					GMLIOHandler ioh = new GMLIOHandler();
					try
					{
						colorizeNodes(); 
						ioh.write(editgraphview.getGraph2D(),name);// Writes the contents of the given graph in GML format to a stream
						boolean reload =false; 
						int reply = JOptionPane.showConfirmDialog(editgraphview,
								"Do you want to open the updated graph in the main screen?",
								"ReLoad",
								JOptionPane.YES_NO_OPTION);				
						if(reply== JOptionPane.YES_OPTION){
							reload=true;
						}				            
						if(reload){
							close();
							gui.openFile(name);	            	
						}

					} catch (IOException ioe){
						D.show(ioe);
					}
				}//end if
			}//end if
		}//action performed
	}//class Save Action


	class FitContent extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e)
		{
			editgraphview.fitContent();
			editgraphview.updateView();
		}
	}


	class DeleteSelection extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e)
		{
			editgraphview.getGraph2D().removeSelection();
			editgraphview.getGraph2D().updateViews();
		}
	}
}
