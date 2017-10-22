package bee.view;

import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import bee.demo.LVControler;
import y.view.Graph2DCanvas;
import y.view.Graph2DViewMouseWheelZoomListener;

public class MyZoomWheelListener extends Graph2DViewMouseWheelZoomListener
{
	LVControler lvcontroler;
	
	public void setLVControler(LVControler lvcontroler){
		this.lvcontroler=lvcontroler;
	}
	
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		
		super.mouseWheelMoved(e);
		Graph2DCanvas layview=(Graph2DCanvas) e.getComponent();
		double zoom=layview.getZoom();
		Point2D p=layview.getCenter();
		lvcontroler.setZoom(zoom, p);
//		double rotation=e.getPreciseWheelRotation();
	}
}
