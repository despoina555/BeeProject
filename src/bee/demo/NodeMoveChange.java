package bee.demo;

import java.util.ArrayList;
import java.util.List;

public class NodeMoveChange implements Changeable
{
	String actionCommand;
	List<Integer> nodeIndex;
	List<Integer> endNodeIndex;
	 int distIndex;
	 
	LVControler controler;
	
	public NodeMoveChange(List<Integer> nodeIndex, int distIndex, List<Integer> endNodeIndex,LVControler controler)
	{
		this.nodeIndex=nodeIndex;
		this.distIndex=distIndex;
		this.actionCommand="Moving Nodes";
		this.controler=controler;
		this.endNodeIndex=endNodeIndex;
		controler.addChangeable(this);
	}

	@Override
    public void undo()
    {
		if(distIndex>0)
		{
		for(int i=0;i<this.nodeIndex.size();i++)
		{
			List<Integer> currentNodeIndex=new ArrayList<>();
			currentNodeIndex.add(new Integer(endNodeIndex.get(i)));
			int dist=nodeIndex.get(i)-endNodeIndex.get(i);
			controler.moveNodes(currentNodeIndex, dist);
//			currentNodeIndex.add(new Integer(nodeIndex.get(i).intValue()+distIndex));
		}
		}
		else
		{
			for(int i=nodeIndex.size()-1;i>=0;i--)
			{
				List<Integer> currentNodeIndex=new ArrayList<>();
				currentNodeIndex.add(new Integer(endNodeIndex.get(i)));
				int dist=nodeIndex.get(i)-endNodeIndex.get(i);
				controler.moveNodes(currentNodeIndex, dist);
			}
		}
	    
	    
    }

	@Override
    public void redo()
    {
	    controler.moveNodes(nodeIndex, distIndex);
	    
    }
	
	public String toString()
	{
		return actionCommand;
	}
	 
}
