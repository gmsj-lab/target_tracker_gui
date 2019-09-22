package gmsj.robotics.tracker.widgets.prefs;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.KeyEvent;
import org.eclipse.draw2d.KeyListener;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.nebula.visualization.xygraph.figures.IXYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.ToolbarArmedXYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.ZoomType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

public class GraphFigure extends Figure {

	private IXYGraph 			xyGraph ;
	private ToolbarArmedXYGraph toolbarArmedXYGraph ;

	public GraphFigure () {

		xyGraph = new XYGraph () ;
		xyGraph.getPrimaryXAxis().setDateEnabled ( false ) ;

		setToolBarArmedXYGraph () ;
	}
	public IXYGraph getXyGraph () {
		return xyGraph ;
	}
	public void addTrace ( Trace trace ) {
		xyGraph.addTrace ( trace ) ;
		layout () ;
	}
	public void removeTrace ( Trace trace ) {
		xyGraph.removeTrace ( trace ) ;
		layout() ;
	}
	@Override
	public void layout () {
			toolbarArmedXYGraph.setBounds ( bounds.getCopy().shrink ( 5 , 5 ) ) ;
			super.layout () ;
	}
	private void setToolBarArmedXYGraph () {

		toolbarArmedXYGraph = new ToolbarArmedXYGraph ( xyGraph ) ;
		add ( toolbarArmedXYGraph ) ;

		//add key listener to XY-Graph. The key pressing will only be monitored when the graph gains focus.
		toolbarArmedXYGraph.addMouseListener ( new MouseListener.Stub () {
			@Override
			public void mousePressed ( final MouseEvent me ) {
				toolbarArmedXYGraph.requestFocus () ;
			}
		});

		toolbarArmedXYGraph.addKeyListener ( new KeyListener.Stub () {
			@Override
			public void keyPressed ( final KeyEvent ke ) {
				if ( ( ke.getState() == SWT.CONTROL ) && ( ke.keycode == 'z' ) ) {
					xyGraph.getOperationsManager().undo() ;
				}
				if ( ( ke.getState() == SWT.CONTROL ) && ( ke.keycode == 'y' ) ) {
					xyGraph.getOperationsManager().redo() ;
				}
				if ( ( ke.getState() == SWT.CONTROL ) && ( ke.keycode == 'x' ) ) {
					xyGraph.performAutoScale() ;
				}
				if ( ( ke.getState() == SWT.CONTROL ) && ( ke.keycode == 's' ) ) {
					final ImageLoader loader = new ImageLoader () ;
					loader.data = new ImageData [] { xyGraph.getImage().getImageData() } ;
					final FileDialog dialog = new FileDialog ( Display.getDefault().getShells()[0], SWT.SAVE ) ;
					dialog.setFilterNames ( new String[] { "PNG Files", "All Files (*.*)" } ) ;
					dialog.setFilterExtensions ( new String[] { "*.png", "*.*" } ) ; // Windows
					final String path = dialog.open () ;
					if ( ( path != null ) && !path.equals ("") ) {
						loader.save ( path, SWT.IMAGE_PNG ) ;
					}
				}
				if ( ( ke.getState() == SWT.CONTROL ) && ( ke.keycode + 'a' -97 == 't' ) ) {
					switch ( xyGraph.getZoomType () ) {
					case RUBBERBAND_ZOOM:
						xyGraph.setZoomType ( ZoomType.RUBBERBAND_ZOOM ) ;
						break;
					case HORIZONTAL_ZOOM:
						xyGraph.setZoomType ( ZoomType.HORIZONTAL_ZOOM ) ;
						break;
					case VERTICAL_ZOOM:
						xyGraph.setZoomType ( ZoomType.VERTICAL_ZOOM ) ;
						break;
					case ZOOM_IN:
						xyGraph.setZoomType ( ZoomType.ZOOM_IN ) ;
						break;
					case ZOOM_OUT:
						xyGraph.setZoomType ( ZoomType.ZOOM_OUT ) ;
						break;
					case PANNING:
						xyGraph.setZoomType ( ZoomType.PANNING ) ;
						break;
					case NONE:
						xyGraph.setZoomType ( ZoomType.NONE ) ;
						break;
					default:
						break;
					}
				}
			}
		});
	}

	public ToolbarArmedXYGraph getToolbar ( ) {
		return toolbarArmedXYGraph ;
	}
}
