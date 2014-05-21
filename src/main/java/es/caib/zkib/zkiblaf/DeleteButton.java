package es.caib.zkib.zkiblaf;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Toolbarbutton;

import es.caib.zkib.component.DataListbox;

public class DeleteButton extends Toolbarbutton {
	/**
	 * 
	 */
	private static final long serialVersionUID = -530199585286130616L;
	private String listbox;
	private Boolean acces = new Boolean(false);
	
	public DeleteButton ()
	{
		setLabel (Messages.getString("DeleteButton.DeleteSelectedLabel")); //$NON-NLS-1$
		setImage ("~./img/list-remove.gif"); //$NON-NLS-1$
		setVisible (acces.booleanValue());
		addEventListener(Events.ON_CLICK, new EventListener()
		{
			public boolean isAsap() {return true;}
			public void onEvent(org.zkoss.zk.ui.event.Event event) {
				if (listbox != null)
				{
					Component c = Path.getComponent(getSpaceOwner(), listbox);
					if (c != null && c instanceof DataListbox)
					{
						DataListbox dlb = (DataListbox) c;
						dlb.delete();
					}
				}
			}
		});
	}

	public String getListbox() {
		return listbox;
	}

	public void setListbox(String listbox) {
		this.listbox = listbox;
	}

	public Boolean getAcces() {
		return acces;
	}

	public void setAcces(Boolean acces) {
		this.acces = acces;
		this.setVisible(acces.booleanValue());
	}

}
