package bee.option;

import y.view.EditMode;

public class MyEditModeLooseConstraint extends EditMode
{
	public MyEditModeLooseConstraint()
	{
		super();
		this.allowResizeNodes(false);
		this.allowEdgeCreation(false);
		this.allowBendCreation(false);
		this.allowNodeCreation(false);
		this.allowMoving(true);
		this.allowMoveSelection(true);
	}	
}
