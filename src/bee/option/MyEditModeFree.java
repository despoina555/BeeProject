package bee.option;

import y.view.EditMode;

public class MyEditModeFree extends EditMode
{

	public MyEditModeFree(){
		super();
		this.allowResizeNodes(false);
		this.allowEdgeCreation(true);
		this.allowBendCreation(false);
		this.allowNodeCreation(true);
	}
}
