package bee.option.options;

import java.awt.Color;
import java.awt.Font;

public class Utilities
{
	public static final int PAGE_UP=1;
	public static final int PAGE_DOWN=-1;
	public static final int PAGE_HIDE=0;
	public static final int TOTAL_PAGES=10;
	public static final Color [] COLORS= {Color.cyan,Color.pink,Color.green,Color.black,Color.lightGray,Color.magenta,Color.red,Color.orange, Color.gray,Color.darkGray};
	public static final String[] COLOR_NAMES={"Cyan","Pink","Green","Black","Light Gray","Magenta","Red","Orange","Gray","Dark Gray"};
	public static final Font CUSTOM_FONT=new Font("Serif", Font.PLAIN, 15);
	public static final int INSTANCES=3;
	
//	public static final int 
	
	public static int getColorIndex(Color c)
	{
		for(int i=0;i<COLORS.length;i++)
		{
			if(c.equals(COLORS[i]))
				return i;
		}
		return -1;
	}
	
	public static int[][] createActiveColors()
	{
			int [][]activeAllColors=new int[Utilities.INSTANCES][Utilities.TOTAL_PAGES];
	for(int i=0;i<activeAllColors[0].length;i++)
	{
		activeAllColors[0][i]=Utilities.PAGE_UP;
		if(i%2==1)
			activeAllColors[0][i]=Utilities.PAGE_DOWN;
	}
	for(int i=0;i<activeAllColors[0].length;i++)
		for(int j=1;j<activeAllColors.length;j++)
		{
		activeAllColors[j][i]=Utilities.PAGE_HIDE;
		//activeAllColors[2//][i]=false;
	}
	for(int j=1;j<activeAllColors.length;j++)
	{
	activeAllColors[j][j-1]=Utilities.PAGE_UP;
//	activeAllColors[2][1]=true;
	}
	return activeAllColors;
	}
}
