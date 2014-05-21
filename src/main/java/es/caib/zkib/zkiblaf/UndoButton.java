package es.caib.zkib.zkiblaf;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Toolbarbutton;

import es.caib.zkib.component.DataModel;

public class UndoButton extends Toolbarbutton {
	/**
	 * 
	 */
	private static final long serialVersionUID = -530194689246146746L;
	private String dataModel;
	private String listbox;
	private boolean _created = false;
	private Boolean acces = new Boolean(false);

	public UndoButton() {
		setLabel(Messages.getString("UndoButton.CancelChanges")); //$NON-NLS-1$
		setImage("~./img/document-undo.gif"); //$NON-NLS-1$
		setVisible (acces.booleanValue());
		addEventListener(Events.ON_CREATE, new EventListener() {
			public boolean isAsap() {
				return false;
			}

			public void onEvent(Event event) {
				_created = true;
				installHooks();
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
							dm.refresh() ;
						} catch (Exception e) {
							e.printStackTrace();
							throw new UiException (e);
						}
					}					
				}
				if(listbox != null){
					Component c = Path.getComponent(getSpaceOwner(), listbox);
					c.invalidate();
				}
			}
		});
	}		
	
	public String getDatamodel() {
		return dataModel;
	}

	public void setDatamodel(String dataModel) {
		this.dataModel = dataModel;
		if (_created)
			installHooks();
	}
	
	public void setListbox(String listbox) {
		this.listbox = listbox;
	}

	public String getListbox(){
		return this.listbox;
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
		if (c != null && c instanceof DataModel) {
			c.addEventListener("onChange", new EventListener() { //$NON-NLS-1$
				public boolean isAsap() {
					return false;
				}

				public void onEvent(org.zkoss.zk.ui.event.Event event) {
					checkCommitPending();
				};
			});
			c.addEventListener("onCommit", new EventListener() { //$NON-NLS-1$
				public boolean isAsap() {
					return false;
				}

				public void onEvent(org.zkoss.zk.ui.event.Event event) {
					checkCommitPending();
				};
			});
		}
		checkCommitPending();
	}

	protected void checkCommitPending() {
		Component c = Path.getComponent(getSpaceOwner(), dataModel);
		if (c != null && c instanceof DataModel)
			setVisible(((DataModel) c).isCommitPending());
		else
			setVisible(false);
	}

}
