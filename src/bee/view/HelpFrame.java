package bee.view;


import java.awt.Toolkit;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;




import java.awt.Color;





import javax.swing.JList;
import javax.swing.AbstractListModel;



import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import bee.option.options.Utilities;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class HelpFrame extends JFrame {
	
	


	public HelpFrame(){


		super("Help");
		setIconImage(Toolkit.getDefaultToolkit().getImage(BeeGui.class.getResource("/javaguiresources/250px-Petersen_double_cover.svg.png")));
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);	

		setBounds(300, 300, 1000, 1000);
		setPreferredSize(new Dimension(500,500));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);

		final JList list = new JList();

		list.setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"", "Create Book Embedding", "", "EditMode","","Analyze Graph","","Algorithms"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		list.setFont(Utilities.CUSTOM_FONT);

		String text0="";
		String text1="\n For create a book embedding , \n  View > Create Book Embedding  ";
		String text10="";
		String text2 = " \n For open an Edit Mode, \n click on menu Edit > EditMode";
		String text20="";
		String text3="\n Analyze Graph gives informations about the size of the graph (number of nodes and edges) \n and if it is  planar, biconnected or connected	 ";
		String text30="";
		String text4=" \n Offers algorithms related to planarity and minimization crossings number ";
		final String [] text= {text0,text1,text10, text2,text20, text3,text30, text4};




		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridwidth = 7;
		gbc_list.insets = new Insets(0, 0, 0, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 1;
		getContentPane().add(list, gbc_list);


		//******* EDITOR PANE

		final JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);

		editorPane.setText(" \n Welcome to Bee ");
		editorPane.setFont(Utilities.CUSTOM_FONT);
		editorPane.setForeground(new Color(25, 25, 112));

		editorPane.setBackground(new Color(250, 240, 230));

		GridBagConstraints gbc_editorPane = new GridBagConstraints();
		gbc_editorPane.fill = GridBagConstraints.BOTH;
		gbc_editorPane.gridx = 7;
		gbc_editorPane.gridy = 1;
		getContentPane().add(editorPane, gbc_editorPane);



		//============================ listeners

		list.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int i=list.getSelectedIndex();
				editorPane.setText(text[i]);

			}

		});

		//================================================




		pack();

		setVisible(true);

	}//con
	
	
}//class
