package bee.option;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import y.base.Edge;
import y.base.EdgeCursor;
import y.option.OptionHandler;
import y.view.EdgeRealizer;
import y.view.Graph2D;
import bee.demo.Changeable;
import bee.demo.EdgeMoveChange;
import bee.demo.LVControler;
import bee.option.options.Utilities;



public class EdgePropertyHandler extends OptionHandler{


	Color preColor;
	Color newColor;


	///ArrayList<Color>  myColors =new ArrayList<Color>();
	static Color [] col=Utilities.COLORS;
		//{Color.cyan,Color.pink,Color.green,Color.black,Color.lightGray,Color.magenta,Color.red,Color.orange, Color.gray,Color.darkGray};

	int pages;

	//boolean check=false;


	LVControler lvcontroler;


	public EdgePropertyHandler(LVControler controler){	

		super("Edge Proprties");

		JList<Color> list= new JList<Color>(col );	 	
		ListCellRenderer<Color> renderer = new MyListCellRenderer();
		list.setCellRenderer(renderer);

		this.lvcontroler=controler;


		addEnum("Page", col, col[1],renderer).setItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED){
					System.out.println("you changed page !!!");
					
				}
			}		

		});

	}//const



	public void updateValuesFromSelection(Graph2D graph){

		EdgeCursor ec = graph.selectedEdges();
		EdgeRealizer er =graph.getRealizer(ec.edge());

		//get the initial values from the first selected edge

		preColor = er.getLineColor();

		boolean samecolor = true;
		//get all further values from the remaining set of selected edges
		if(ec.size() > 1){
			for (ec.next(); ec.ok(); ec.next())
			{
				er = graph.getRealizer(ec.edge());
				if (samecolor && preColor != er.getLineColor())
					samecolor = false;
			}
			set("Page", preColor);
			getItem("Page").setValueUndefined(!samecolor);

		}
	}//updateVal





	
	

public void commitEdgeProperties(Graph2D graph){

		

		List<int[]> edgeIndex=new ArrayList<int []>();
		EdgeCursor ec = graph.selectedEdges();
		int[] previousPages=new int[ec.size()];
		int num=0;
		int pos=0;
		for (ec.toFirst(); ec.ok(); ec.next()){

			Edge edge = ec.edge();
			EdgeRealizer er =graph.getRealizer(edge);

			int [] current=new int[2];
			current[0]=edge.source().index();
			current[1]=edge.target().index();
			edgeIndex.add(current);
			previousPages[pos]=Utilities.getColorIndex(er.getLineColor());
					pos++;
			if (!getItem("Page").isValueUndefined())
				er.setLineColor((Color)get("Page"));



			newColor=(Color)get("Page");			 
			num= getEnum("Page");


			lvcontroler.moveEdges(edgeIndex,num);

			lvcontroler.informMainControler();


		}//for each edge
		Changeable edgeChange=new EdgeMoveChange(edgeIndex, num, previousPages, lvcontroler);


	}//commitEdgeProperies




	public void setPages(int numPages){
		pages=numPages;
	}

	public int getPages(){
		return pages;
	}







	class MyListCellRenderer implements ListCellRenderer {


		protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

		@Override
		public Component getListCellRendererComponent(JList list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel renderer=(JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			renderer.setText(Integer.toString(index));
			if(index>-1){
				renderer.setBackground(new Color(255,255,240));
				renderer.setForeground(col[index].darker());
				renderer.setText("  Page  "+ index +"   "+ Utilities.COLOR_NAMES[index]);
				renderer.setFont(Utilities.CUSTOM_FONT);
				renderer.setVerticalAlignment(SwingConstants.CENTER);
				LineBorder line= new LineBorder(col[index],3,true);
				renderer.setBorder(BorderFactory.createTitledBorder(line));
				
				
			}

			return renderer;
		}//getListCellRendererComponent

	}//implements ListCellRenderer






}// class EdgePropertyHandler	
