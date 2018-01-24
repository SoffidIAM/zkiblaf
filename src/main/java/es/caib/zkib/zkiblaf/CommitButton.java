package es.caib.zkib.zkiblaf;

import org.zkoss.zk.au.AuScript;
import org.zkoss.zk.au.out.AuConfirmClose;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Toolbarbutton;

import es.caib.zkib.component.DataModel;
import es.caib.zkib.datasource.CommitException;

public class CommitButton extends Toolbarbutton  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -530199585286130616L;
	private String dataModel;
	private boolean _created = false;
	private Boolean acces = new Boolean(false);
	
	public CommitButton ()
	{
		setLabel (Messages.getString("CommitButton.ConfirmChanges")); //$NON-NLS-1$
		setImage ("~./img/document-save.gif"); //$NON-NLS-1$
		setVisible (acces.booleanValue());
		addEventListener(Events.ON_CREATE, new EventListener () {
			public boolean isAsap() {
				return false;
			}
			public void onEvent(Event event) {
				_created = true;
				installHooks ();
			}
		});
		
		addEventListener(Events.ON_CLICK, new EventListener() {
			public boolean isAsap() {return true;}
			public void onEvent(org.zkoss.zk.ui.event.Event event) {
				if (dataModel != null)
				{
					Component c = Path.getComponent(getSpaceOwner(), dataModel);
					if (c != null && c instanceof DataModel)
					{
						DataModel dm = (DataModel) c;
						try {
							dm.commit() ;
						} catch (CommitException e) {
							throw new UiException (e);
						}
					}
				}
			}
		});
	}

	public String getDatamodel() {
		return dataModel;
	}

	public void setDatamodel(String listbox) {
		this.dataModel = listbox;
		if (_created)
			installHooks();
	}
	
	public Boolean getAcces() {
		return acces;
	}

	public void setAcces(Boolean acces) {
		this.acces = acces;
		this.setVisible(acces.booleanValue());
	}

	protected void installHooks() {
		Component c = Path.getComponent(getSpaceOwner(), dataModel);
		if (c != null && c instanceof DataModel)
		{
			c.addEventListener("onChange", new EventListener () //$NON-NLS-1$
			{
				public boolean isAsap() {return false;}
				public void onEvent(org.zkoss.zk.ui.event.Event event) {
					checkCommitPending ();
				};
			});
			c.addEventListener("onCommit", new EventListener () //$NON-NLS-1$
			{
					public boolean isAsap() {return false;}
					public void onEvent(org.zkoss.zk.ui.event.Event event) {
						checkCommitPending ();
					};
			});
		}
		checkCommitPending();
	}
	

	private void enableExit(boolean exit)
	{
		if (!exit)
			Executions.getCurrent().addAuResponse("unableExit", new AuScript(this , "zkau.confirmClose=true;"));
		else
			Executions.getCurrent().addAuResponse("unableExit", new AuScript(this , "zkau.confirmClose=false;"));
	}
	
	protected void checkCommitPending() {
		Component c = Path.getComponent(getSpaceOwner(), dataModel);
		if (c != null && c instanceof DataModel)
		{
			boolean commitPending = ( (DataModel) c).isCommitPending();
			setVisible ( commitPending );
			enableExit(!commitPending);
		}
		else
		{
			setVisible(false);
			enableExit(true);
		}
	}


}
