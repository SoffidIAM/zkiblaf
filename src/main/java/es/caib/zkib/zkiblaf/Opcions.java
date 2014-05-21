package es.caib.zkib.zkiblaf;

import java.util.Collection;
import java.util.Iterator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Space;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import es.caib.zkib.component.DataModel;
import es.caib.zkib.datasource.CommitException;

public class Opcions extends Window implements AfterCompose {
	/**
	 * 
	 */
	private static final long serialVersionUID = 546086727746341924L;
	private Caption caption;
	private Toolbarbutton commitButton;
	
	public Opcions() {
		setWidth("800px"); //$NON-NLS-1$
		addEventListener(Events.ON_CREATE, new EventListener ()
		{
			public boolean isAsap() {return false;}
			public void onEvent(org.zkoss.zk.ui.event.Event event) {
				installHooks ();
			};
		});
		
	}

	protected void installHooks() {
		installHooks (getChildren());
	}

	private void installHooks(Collection roots) {
		Iterator it = roots.iterator();
		while (it.hasNext())
		{
			Component c = (Component) it.next();
			if (c instanceof DataModel)
			{
				addEventListener("onChange", new EventListener () //$NON-NLS-1$
				{
					public boolean isAsap() {return false;}
					public void onEvent(org.zkoss.zk.ui.event.Event event) {
						checkCommitPending ();
					};
				});
			}
			installHooks(c.getChildren()); 
		}
	}
	
	public void afterCompose() {
		caption = new Caption ();
		caption.setParent(this);
		// INSERT
		Toolbarbutton button = new Toolbarbutton();
		button.setParent(caption);
		button.setLabel(Messages.getString("Opcions.AddMessage")); //$NON-NLS-1$
		button.setImage("/img/insert16.gif"); //$NON-NLS-1$
		button.addEventListener(Events.ON_CLICK, new EventListener ()
		{
			public boolean isAsap() {return false;}
			public void onEvent(org.zkoss.zk.ui.event.Event event) {
				Events.postEvent("onInsert", Opcions.this, null); //$NON-NLS-1$
			};
		});
		Space space = new Space ();
		space.setParent(caption);
		space.setWidth("5px"); //$NON-NLS-1$
		// DELETE
		button = new Toolbarbutton();
		button.setParent(caption);
		button.setLabel(Messages.getString("Opcions.DeleteMessage")); //$NON-NLS-1$
		button.setImage("/img/delete16.gif"); //$NON-NLS-1$
		button.addEventListener(Events.ON_CLICK, new EventListener ()
		{
			public boolean isAsap() {return false;}
			public void onEvent(org.zkoss.zk.ui.event.Event event) {
				Events.postEvent("onDelete", Opcions.this, null); //$NON-NLS-1$
			};
		});
		space = new Space ();
		space.setParent(caption);
		space.setWidth("5px"); //$NON-NLS-1$
		// COMMIT
		button = new Toolbarbutton();
		button.setParent(caption);
		button.setLabel(Messages.getString("Opcions.ConfirmChanges")); //$NON-NLS-1$
		button.setImage("/img/save16.gif"); //$NON-NLS-1$
		button.setVisible(false);
		button.addEventListener(Events.ON_CLICK, new EventListener ()
		{
			public boolean isAsap() {return true;}
			public void onEvent(org.zkoss.zk.ui.event.Event event) {
				doCommit ();
			};
		});
		commitButton = button;
		// UNDO
		button = new Toolbarbutton();
		button.setParent(caption);
		button.setLabel(Messages.getString("Opcions.CancelChanges")); //$NON-NLS-1$
		button.setImage("/img/save16.gif"); //$NON-NLS-1$
		button.setVisible(false);
		button.addEventListener(Events.ON_CLICK, new EventListener ()
		{
			public boolean isAsap() {return true;}
			public void onEvent(org.zkoss.zk.ui.event.Event event) {
				undo ();
			};
		});

		installHooks ();
	}

	protected void undo() {
		undo (getChildren());
		checkCommitPending();
	}
	
	private void undo (Collection components) {
		Iterator it = components.iterator();
		while (it.hasNext())
		{
			Component c = (Component) it.next();
			if (c instanceof DataModel)
			{
				try {
					((DataModel)c).refresh();
				} catch (Exception e) {
					throw new UiException(e);
				}
			}
			doCommit(c.getChildren()); 
		}
	}
	
	protected void doCommit() {
		doCommit (getChildren());
		checkCommitPending();
	}

	private void doCommit (Collection components) {
		Iterator it = components.iterator();
		while (it.hasNext())
		{
			Component c = (Component) it.next();
			if (c instanceof DataModel)
			{
				try {
					((DataModel)c).commit();
				} catch (CommitException e) {
					throw new UiException(e);
				}
			}
			doCommit(c.getChildren()); 
		}
	}

	protected void checkCommitPending () {
		commitButton.setVisible (isCommitPending (getChildren()));
	}

	private boolean isCommitPending (Collection components) {
		Iterator it = components.iterator();
		while (it.hasNext())
		{
			Component c = (Component) it.next();
			if (c instanceof DataModel)
			{
				if (((DataModel)c).isCommitPending())
					return true;
			}
			if ( isCommitPending(c.getChildren()))
				return true;
		}
		return false;
	}

	public void recreate() {
		installHooks();
	}
}
