package es.caib.zkib.zkiblaf;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Toolbarbutton;

import es.caib.zkib.component.DataModel;
import es.caib.zkib.datasource.CommitException;

public class CommitButton2 extends Button  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -530199585286130616L;
	private String dataModel;
	private boolean _created = false;
	private Boolean acces = new Boolean(false);
	
	public CommitButton2 ()
	{
		setLabel (Messages.getString("CommitButton2.ConfirmChanges")); //$NON-NLS-1$
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
							//això ha d'anar aquí sino no mostra les alertes en cas que hi hagi un error al commit
							Application.setPage("wf/inbox.zul"); //$NON-NLS-1$
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
	

	protected void checkCommitPending() {
		Component c = Path.getComponent(getSpaceOwner(), dataModel);
		if (c != null && c instanceof DataModel)
			setVisible ( ( (DataModel) c).isCommitPending() );
		else
			setVisible(false);
	}


}
