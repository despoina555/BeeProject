package bee.option;

import y.view.EditMode;

public class MyEditModeConstraint extends EditMode
{
	public MyEditModeConstraint()
	{
		super();
		this.allowResizeNodes(false);
		this.allowEdgeCreation(false);
		this.allowBendCreation(false);
		this.allowNodeCreation(false);
		this.allowMoving(false);
		this.allowMoveSelection(false);
	}	
}
