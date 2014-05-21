package es.caib.zkib.zkiblaf;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import es.caib.zkib.component.DataTree;

public class InsertTreeButton extends Toolbarbutton {
	/**
	 * Modificacions: (by u88683)
	 * 
	 * Atributs:
	 * 		fillSensePare = default: false
	 * 			Per defecte per crear un nou node, s'ha de seleccionar un altre node 
	 * 			com a pare
	 * 
	 */
	private static final long serialVersionUID = -530199585286130616L;
	private String tree;
	private String path;
	private Boolean acces = new Boolean(false);
	private boolean fillSensePare = false;
	private Treeitem pareNode = null; //Para saber el padre del nodo que insertamos (si existe)
	
	public InsertTreeButton () 
	{
		setLabel (Messages.getString("InsertTreeButton.AddNew")); //$NON-NLS-1$
		setImage ("~./img/list-add.gif"); //$NON-NLS-1$
		setVisible (acces.booleanValue());
		addEventListener(Events.ON_CLICK, new TreeEvent() );
	}

	public String getTree() {
		return tree;
	}

	public void setTree(String tree) {
		this.tree = tree;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public Boolean getAcces() {
		return acces;
	}

	public void setAcces(Boolean acces) {
		this.acces = acces;
		this.setVisible(acces.booleanValue());
	}
	
	class TreeEvent implements EventListener {

			public boolean isAsap() {return true;}
			public void onEvent(org.zkoss.zk.ui.event.Event event) throws Exception {
				try {
					pareNode = null; 
					if (tree != null)
					{
						Component c = Path.getComponent(getSpaceOwner(), tree);
						if (c != null && c instanceof DataTree)
						{
							DataTree dlb = (DataTree) c;
							
							// Per evitar el problema que surt quan afegim fills
							// a nodes sense cap fill (apareix des-penjat de la branca)
							if (dlb.getSelectedItem()!=null && dlb.getSelectedItem().getTreechildren() == null) {
								dlb.getSelectedItem().appendChild(new Treechildren());
							}
							
							// Si no està establert que es poden crear nodes sene pare (fillSensePare=true)
							// Obliguem a que tinga un no de com a pare (es dóna missatge d'error)
							if (fillSensePare || dlb.getSelectedCount()>0)  {
								pareNode = dlb.addNew(path); // Obtenim el node pare								
							}
							else {
								throw new UiException(Messages.getString("InsertTreeButton.SelectNodeError")); //$NON-NLS-1$
							}
						}
					}
				} catch (Exception ex) {
					throw ex;
				}
			}
	}

	public boolean getFillSensePare() {
		return fillSensePare;
	}

	public void setFillSensePare(boolean fillSensePare) {
		this.fillSensePare = fillSensePare;
	}

	public Treeitem getPareNode() {
		return pareNode;
	}

}
