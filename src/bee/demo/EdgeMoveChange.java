package bee.demo;

import java.util.ArrayList;
import java.util.List;

public class EdgeMoveChange implements Changeable

{
	String actionCommand;
	List<int []> edgeIndex;
	int toPage;
	int[] fromPage;
	LVControler controler;
	
	public EdgeMoveChange(List<int []> edgeIndex, int toPage, int[] fromPage, LVControler controler)
	{
		this.actionCommand="Coloring edges";
		this.edgeIndex=edgeIndex;
		this.toPage=toPage;
		this.fromPage=fromPage;
		this.controler=controler;
		controler.addChangeable(this);
	}

	@Override
    public void undo()
    {
	    for(int i=0;i<fromPage.length;i++)
	    {
	    	List<int[]> edges=new ArrayList<>();
	    	edges.add(edgeIndex.get(i));
	    	controler.moveEdges(edges, fromPage[i]);
	    }
	    
    }

	@Override
    public void redo()
    {
	    controler.moveEdges(edgeIndex, toPage);
	    
    }

	public String toString()
	{
		return actionCommand;
	}
}
