package gmsj.robotics.tracker.overview.preferencesviewer;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FontDialog;

public class FontDialogCellEditor extends DialogCellEditor {

	private Composite parent;

	public FontDialogCellEditor ( Composite parent ) {
		super ( parent, SWT.NONE ) ;
		this.parent = parent ;
	}

	@Override
	protected Object openDialogBox ( Control cellEditorWindow ) {
		FontDialog fontDialog = new FontDialog ( parent.getShell () ) ;
		fontDialog.setText( "Change Font" ) ;
		return fontDialog.open () ;
	}

}
